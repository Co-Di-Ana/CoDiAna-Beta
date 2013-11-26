package edu.codiana.execution;

import edu.codiana.core.configuration.Config;
import edu.codiana.core.configuration.ExitConstants;
import edu.codiana.execution.results.ProgRunResult;
import edu.codiana.execution.results.ProgCompileResult;
import edu.codiana.core.iface.*;
import edu.codiana.utils.io.File;
import edu.codiana.execution.factory.ProgramFactory;
import edu.codiana.execution.security.factory.LanguageGuardianFactory;
import edu.codiana.execution.security.structures.SecurityResult;
import edu.codiana.gui.CodePreview;
import edu.codiana.gui.ConsoleStream;
import edu.codiana.utils.*;
import edu.codiana.utils.io.FileReader;
import edu.codiana.utils.serialization.ExecutorItem;
import edu.codiana.utils.tests.*;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Executor {

    //--------------------------------------------------------------------------
    //
    // STATIC
    //
    //--------------------------------------------------------------------------
    private static final String SEPARATOR = String.format ("%60s", " ").replaceAll ("\\s", "-");
    private static final int totalRuns = 2;
    //--------------------------------------
    // SYSTEM FILE STRUCTURE
    //--------------------------------------
    private static final String EXTENSION_OUT = ".out";
    private static final String EXTENSION_TMP_OUT = ".out_tmp";
    private static final String EXTENSION_IN = ".in";
    private static final String EXTENSION_COMPILE_ERROR = ".compile.err";
    private static final String EXTENSION_RUN_ERROR = ".run.err";
    private static final String EXTENSION_LOG = ".log";
    private static final String FOLDER_STUDENT = "current";
    private static final String FOLDER_TEACHER = "##";
    //--------------------------------------------------------------------------
    //
    // NON STATIC
    //
    //--------------------------------------------------------------------------
    //--------------------------------------
    // FINAL
    //--------------------------------------
    public final IProgramEntry entry;
    //--------------------------------------
    // PRIVATE
    //--------------------------------------
    private ILanguageGuardian guardian;
    private FileLogger logger;
    //--------------------------------------
    // PUBLIC
    //--------------------------------------
    public File taskFile, outFile, userFile, userMainFile, userOutFile;
    public File userCompileErrorFile, userRunErrorFile, userLogFile;
    public IProgExecutor prog;
    public ProgRunResult run;
    public ProgCompileResult compile;
    public TimeCheck time = new TimeCheck ();
    public OutputCheck output = new OutputCheck ();
    public MemoryCheck memory = new MemoryCheck ();
    public ExitConstants exit = ExitConstants.UNKNOW_EXIT;
    private StringBuilder note = new StringBuilder ();



    public Executor (IProgramEntry item, boolean manualControl) throws FileNotFoundException, IOException {
	this.entry = item;
	setUpFiles ();
	setUpLogger ();
	startWork ();

	//# unsupported language are ignored
	if (!ProgramFactory.isSupported (item)) {
	    exit (ExitConstants.UNSUPPORTED_LANGUAGE);
	    endWork ();
	    return;
	}

	//# main file exists
	if (userMainFile == null || !userMainFile.exists ()) {
	    exit (ExitConstants.MAIN_FILE_MISSING);
	    endWork ();
	    return;
	}

	//# try to load language guardian
	if ((guardian = LanguageGuardianFactory.getInstance (item)) == null) {
	    exit (ExitConstants.NO_LANGUAGE_SECURITY_CHECK);
	    endWork ();
	    return;
	}
	//# after check is no thread
	SecurityResult risk;
	java.io.File[] files = Files.getSolutionFiles (item, userFile);
	for (int i = 0; i < files.length; i++) {
	    java.io.File file = files[i];
	    if ((risk = guardian.check (file)) != null) {
		addNote (String.format ("Security risk source%n\tFile: %s%n\tCode: %s",
					file.getName (), risk.getSource ()));
		exit (ExitConstants.SECURITY_RISK);
		endWork ();
		return;
	    }
	}

	CodePreview.CodePreviewResult result;
	if (manualControl) {
	    if (!(result = CodePreview.allow (userFile, files, new Files.ExtensionFilter (item.getLanguage ()))).isAllowed ()) {
		addNote (String.format ("Solution was to manually declared as dangerous%n\tSource: %s",
					result.getSource ()));
		exit (ExitConstants.SECURITY_RISK);
		endWork ();
		return;
	    }
	}

	doWork ();
	endWork ();
    }



    public Executor (IProgramEntry item) throws FileNotFoundException, IOException {
	this (item, true);
    }



    private void setUpFiles () {
	/*
	 * taskFile		= ./mid/tid/
	 * outfile		= ./mid/tid/##/tid.out
	 * userFile		= ./mid/tid/##/
	 *			= ./mid/tid/base64Uid/current/
	 * userMainFile		= ./mid/tid/base64Uid/current/tid.extension
	 * userOutFile		= ./mid/tid/base64Uid/current/tid.out
	 * userCompileErrorFile	= ./mid/tid/base64Uid/current/tid.compile.err
	 * userRunErrorFile	= ./mid/tid/base64Uid/current/tid.run.err
	 * userLogFile		= ./mid/tid/base64Uid/current/tid.logFile
	 */

	taskFile = Config.getMailFile ().resolvePath (entry.getMid ()).resolvePath (entry.getTid ());
	outFile = taskFile.resolvePath (FOLDER_TEACHER).resolvePath (entry.getTid ().concat (EXTENSION_OUT));
	userFile = entry.isTeacher () ? 
		   taskFile.resolvePath (FOLDER_TEACHER) :
		   taskFile.resolvePath (Base64.cleverEncode (entry.getUid ())).resolvePath (FOLDER_STUDENT);
	userMainFile = userFile.resolvePath (entry.getMainFilePath ());
	userOutFile = userMainFile.changeExtension (EXTENSION_OUT);
	userCompileErrorFile = userMainFile.changeExtension (EXTENSION_COMPILE_ERROR);
	userRunErrorFile = userMainFile.changeExtension (EXTENSION_RUN_ERROR);
	userLogFile = userMainFile.changeExtension (EXTENSION_LOG);
	
	System.out.println (taskFile);
	System.out.println (outFile);
	System.out.println (userFile);
	System.out.println (userMainFile);
	System.out.println (userOutFile);
	System.out.println (userCompileErrorFile);
	System.out.println (userRunErrorFile);
	System.out.println (userLogFile);
    }



    private void setUpLogger () {
	if (userLogFile == null)
	    setUpFiles ();
	if (userLogFile.exists ()) {
	    logger = new FileLogger (userLogFile, ConsoleStream.getInstance ());
	    logger.newLog ();
	} else
	    logger = new FileLogger (userLogFile, ConsoleStream.getInstance ());
    }



    private ExitConstants doWork () throws FileNotFoundException, IOException {
	//# if there is non-existing user folder, nothing can be done
	if (!userMainFile.exists ()) {
	    logger.log ("error user main file does not exists");
	    return exit (ExitConstants.MAIN_FILE_MISSING);
	}


	//# create program and give in main file
	prog = ProgramFactory.getInstance (userMainFile.extension ());
	prog.setSourceFile (userMainFile);

	//# input is ALWAYS redirected
	if (entry.isTeacher ()) prog.setStdin (userMainFile.changeExtension (EXTENSION_IN).getPath ());
	else prog.setStdin (taskFile.resolvePath (FOLDER_TEACHER).resolvePath (entry.getTid ().concat (EXTENSION_IN)).getPath ());

	//# on demand generate output
	if (entry.generateOutput ()) prog.setStdout (userMainFile.changeExtension (EXTENSION_OUT).getPath ());
	else prog.setStdout (userMainFile.changeExtension (EXTENSION_TMP_OUT).getPath ());

	//# setting relative path to program
	prog.setPath (userMainFile.getParent ());



	//# is it compile language? so compile
	if (prog instanceof IProgCompiler) {

	    //# error output is ALWAYS redirected
	    prog.setStderr (userCompileErrorFile.getPath ());

	    //# clearing compile error logFile
	    userCompileErrorFile.clear ();
	    compile = ((IProgCompiler) prog).compile (15 * 1000);

	    //# if compilation wasn't successfull (bad code, timeout) terminate
	    if (compile.exit != 0 || userCompileErrorFile.length () != 0) {

		logger.log ("compile error");
		if (compile.exit == 0) {
		    addNote ("Error:\n");
		    addNote (FileReader.readFile (userCompileErrorFile, userFile.getAbsolutePath ()));
		    logger.log (userCompileErrorFile);
		}
		return exit (ExitConstants.COMPILATION_ERROR);
	    }

	    logger.log ("compilation: " + compile.time);

	    //# clean temp file if exists
	    if (!entry.generateOutput ())
		if (userMainFile.changeExtension (EXTENSION_TMP_OUT).exists ())
		    userMainFile.changeExtension (EXTENSION_TMP_OUT).delete ();
	}



	//# error output is ALWAYS redirected
	prog.setStderr (userRunErrorFile.getPath ());

	//# clearing execute error logFile
	userRunErrorFile.clear ();


	//# clear output if we do no desire to keep original (do no detect output)
	if (entry.generateOutput ())
	    userOutFile.delete ();

	//# every program can be execute
	run = prog.execute (totalRuns, entry.isTeacher () ? 60 * 1000 : (int) entry.getMaxTime ());

	//# if execute wasn't successfull (timeout, infinite loop, ...) terminate
	if (run.exit != 0 || userRunErrorFile.length () != 0) {

	    logger.log (run.exit == 0 ? "run error" : "time-out error");
	    if (run.exit == 0) {
		addNote ("Error:\n");
		addNote (FileReader.readFile (userRunErrorFile, userFile.getAbsolutePath ()));
		logger.log (userRunErrorFile);
	    }
	    return exit (run.exit == 0 ? ExitConstants.RUN_ERROR : ExitConstants.TIME_OUT_ERROR);
	}

	logger.log ("best running time: " + run.time);


	//# output result
	final boolean isStrict = entry.getOutputCheck () == 1;
	final boolean isTeacher = entry.isTeacher ();
	output.setWeight (entry.getOutputWeight ());
	output.setIsTeacher (isTeacher);
	output.setValueCheck (entry.getOutputCheck ());
	output.calculateResult (outFile, userOutFile);
	final boolean outputIsOK = output.getResult () == 1.0f;

	//# time result
	time.setWeight (isStrict ? outputIsOK ? 100 : 0 : entry.getTimeWeight ());
	time.setComputedTime (run.time * 1000);
	time.setIsTeacher (isTeacher);
	time.calculateResult (entry.getTimeCheck (), entry.getMinTime (), entry.getMaxTime ());

	//# memory result
	memory.setWeight (entry.getMemoryWeight ());
	memory.setIsTeacher (isTeacher);


	return exit (isTeacher ? ExitConstants.EXIT_OK :
		     (isStrict && !outputIsOK ? ExitConstants.WRONG_OUTPUT : ExitConstants.EXIT_OK));
    }



    /**
     * Method returns whether was run ok
     * @return 
     */
    public boolean isOK () {
	return exit == ExitConstants.EXIT_OK;
    }



    private ExitConstants exit (ExitConstants e) {
	String tmp;
	exit = e;

	if (logger == null)
	    setUpLogger ();

	tmp = String.format ("output:       [raw = %1.2f][result = %1.2f][weighted = %.2f]",
			     output.getRawValue (), output.getResult (), output.getWeightedResult ());
	logger.log (tmp);
	tmp = String.format ("time:         [raw = %1.2f][result = %1.2f][weighted = %.2f]",
			     time.getRawValue (), time.getResult (), time.getWeightedResult ());
	logger.log (tmp);
	tmp = String.format ("memory:       [raw = %1.2f][result = %1.2f][weighted = %.2f]",
			     memory.getRawValue (), memory.getResult (), memory.getWeightedResult ());
	logger.log (tmp);
	tmp = String.format ("final-result: [%1.2f]", getFinalResult ());
	logger.log (tmp);
	return e;
    }



    private void startWork () {
	logger.log (SEPARATOR);
	logger.log (SEPARATOR);
	logger.log (String.format ("%s", entry));
	logger.log (SEPARATOR);
	logger.log ("START");
    }



    private void endWork () {
	logger.log (SEPARATOR);
	logger.log (String.format ("exit: %s", exit.toString ()));
	logger.log ("END");
	logger.log (SEPARATOR);
	logger.log (SEPARATOR);
    }



    /**
     * @return number indicating how many lines were same as original
     * -1 on error
     */
    public float getOutputRawValue () {
	return exit != ExitConstants.EXIT_OK ? -1 : output.getRawValue ();
    }



    /**
     * @return number indicatiog how long was program running
     * -1 on error
     */
    public float getTimeRawValue () {
	return exit != ExitConstants.EXIT_OK ? -1 : time.getRawValue ();
    }



    /**
     * @return number indication how much memory program used
     * -1 on error
     */
    public float getMemoryRawValue () {
	return exit != ExitConstants.EXIT_OK ? -1 : memory.getRawValue ();
    }



    /**
     * @return number <0, 100> indication percentage similarity of output file for given check
     * -1 on error
     */
    public float getOutputResult () {
	return exit != ExitConstants.EXIT_OK ? -1 : output.getResult () == -1 ? -1 : output.getResult () * 100;
    }



    /**
     * @return number <0, 100> indication time value for given task criteria formula
     * -1 on error
     */
    public float getTimeResult () {
	return exit != ExitConstants.EXIT_OK ? -1 : time.getResult () == -1 ? -1 : time.getResult () * 100;
    }



    /**
     * @return number <0, 100> indication percent memory usage for given check
     * -1 on error
     */
    public float getMemoryResult () {
	return exit != ExitConstants.EXIT_OK ? -1 : memory.getResult () == -1 ? -1 : memory.getResult () * 100;
    }



    /**
     * @return sum of all check tests
     * -1 on error
     */
    public float getFinalResult () {
	if (exit != ExitConstants.EXIT_OK) return -1.0f;
	float r = 0, t;
	if (time.isSupported ()) r += (t = time.getWeightedResult ()) == -1 ? 0 : t;
	if (output.isSupported ()) r += (t = output.getWeightedResult ()) == -1 ? 0 : t;
	if (memory.isSupported ()) r += (t = memory.getWeightedResult ()) == -1 ? 0 : t;
	return r;
    }



    private void addNote (String s) {
	note.append (s);
    }



    private void addNote (StringBuilder s) {
	addNote (s.toString ());
    }



    /**
     * Method return current note
     * @return 
     */
    public String getNote () {
	return note.toString ();
    }



    /**
     * Method returns whether run has some note
     * @return 
     */
    public boolean hasNote () {
	return note != null && note.length () != 0;
    }

    //--------------------------------------------------------------------------
    //
    // SERIALIZATION
    //
    //--------------------------------------------------------------------------


    /**
     * Method generates serializable ExecutorItem object which can be write into a file
     * @param executor result from Executor containing all info
     * @return unique instance of ExecutorItem
     */
    public static ExecutorItem generate (Executor executor) {
	return new ExecutorItem (executor);
    }
}
