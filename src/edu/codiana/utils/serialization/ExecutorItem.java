package edu.codiana.utils.serialization;

import edu.codiana.execution.Executor;
import java.io.Serializable;
import java.util.Date;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ExecutorItem implements Serializable {

    //--------------------------------------
    // SERIALIZATION
    //--------------------------------------
    private static final long serialVersionUID = 5864381869486493L;
    //--------------------------------------
    // ADVANCED
    //--------------------------------------
    public final String result, note;
    public final float timeResult, timeValue, outputResult, outputValue, finalResult;
    //--------------------------------------
    // BASE
    //--------------------------------------
    public final String uid, tid, mid, language;
    public final boolean detectTime, generateOutput, checkPlags;
    public final Date date = new Date ();



    /**
     * Create serializable instance from executor
     * @param executor from {@link Executor#generate(edu.codiana.execution.Executor) }
     */
    public ExecutorItem (Executor executor) {
	//--------------------------------------
	// BASE
	//--------------------------------------
	uid = executor.entry.getUid ();
	tid = executor.entry.getTid ();
	mid = executor.entry.getMid ();
	language = executor.entry.getLanguage ();
	detectTime = executor.entry.detectTime ();
	generateOutput = executor.entry.generateOutput ();
	checkPlags = executor.entry.isPlagiarismCheck ();

	//--------------------------------------
	// ADVANCED
	//--------------------------------------
	result = executor.exit.toString ();
	note = executor.getNote ();
	
	timeResult = executor.getTimeResult ();
	outputResult = executor.getOutputResult ();
	finalResult = executor.getFinalResult ();

	timeValue = executor.getTimeRawValue ();
	outputValue = executor.getOutputRawValue ();
    }



    @Override
    public String toString () {
	return String.format ("[ExecutorItem '%s' '%s@%s' exit: %s]", uid, tid, mid, result);
    }
}
