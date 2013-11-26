package edu.codiana.execution;

import edu.codiana.core.iface.IProgCompiler;
import edu.codiana.execution.monitors.ProcessMonitor;
import edu.codiana.execution.results.ProgCompileResult;
import edu.codiana.execution.results.ProgRunResult;
import edu.codiana.utils.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Jan Hybš
 * @version 1.0
 */
public class JavaExecutor implements IProgCompiler {

    //--------------------------------------
    // STATIC
    //--------------------------------------
    private static final String EMPTY = "";
    //--------------------------------------
    // STDs
    //--------------------------------------
    private String stdin = EMPTY, stdout = EMPTY, stderr = EMPTY;
    //--------------------------------------
    // OTHER
    //--------------------------------------
    private File file;
    private String sourcePath = "";
    private String classPath = "";
    private String path = "";



    /**
     * Create instance of JavaExecutor
     */
    public JavaExecutor() {
    }



    @Override
    public ProgCompileResult compile() throws IOException {
	return compile(10 * 1000);
    }



    @Override
    public ProgCompileResult compile(int timeLimit) throws IOException {

        System.out.format("javac %s %s%n", sourcePath, file.getPath());
	ProcessMonitor pm = new ProcessMonitor(
		String.format("javac %s %s", sourcePath, file.getPath()));
	pm.setStderr(stderr);
	pm.run(timeLimit);

	return new ProgCompileResult(pm);
    }



    @Override
    public ProgRunResult execute() throws IOException {
	return execute(3, 5 * 1000);
    }



    @Override
    public ProgRunResult execute(int numberOfTests, int timeLimit) throws IOException {

	ProgRunResult result = null;

	for (int i = 0; i < numberOfTests; i++) {
	    ProcessMonitor pm = new ProcessMonitor(
		    String.format("java %s %s", classPath, file.name()));
	    pm.setStdin(stdin);
	    pm.setStdout(stdout);
	    pm.setStderr(stderr);
	    pm.run(timeLimit);
	    ProgRunResult temp = new ProgRunResult(pm);

	    if (result == null || (temp.exit == 0 && result.time == 0 && temp.time < result.time))
		result = temp;

	}


	return result;
    }



    @Override
    public void setSourceFile(File file) throws FileNotFoundException {
	if (file == null || !file.exists())
	    throw new FileNotFoundException("File does not exists");

	this.file = file;
    }



    @Override
    public String getPath() {
	return path;
    }



    @Override
    public void setPath(String path) {
	this.path = path;
	if (path == null || path.length() == 0)
	    classPath = sourcePath = "";
	else {
	    sourcePath = "-sourcepath " + path;
	    classPath = "-classpath " + path;
	}
    }



    @Override
    public void setStdin(String stdin) {
	this.stdin = stdin;
    }



    @Override
    public void setStdout(String stdout) {
	this.stdout = stdout;
    }



    @Override
    public void setStderr(String stderr) {
	this.stderr = stderr;
    }



}
