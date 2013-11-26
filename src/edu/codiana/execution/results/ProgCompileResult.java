package edu.codiana.execution.results;

import edu.codiana.execution.monitors.ProcessMonitor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * @author Hans
 */
public class ProgCompileResult {

    public final float time;
    public final int exit;
    ProcessMonitor process;



    /**
     * Creates instance from ProcessMonitor. This constructor should not be called manually
     * @param process 
     */
    public ProgCompileResult (ProcessMonitor process) throws IOException {
	float tmpT;
	int tmpE;
	try {
	    this.process = process;
	    BufferedReader reader = new BufferedReader (
		    new InputStreamReader (process.getInputStream ()));
	    tmpT = Float.parseFloat (reader.readLine ());
	    tmpE = process.exitValue ();
	    reader.close ();
	    if (tmpE != 0) tmpT = -1;
	} catch (IOException ex) {
	    tmpE = -1;
	    tmpT = -1;
	} catch (Exception ex) {
	    tmpE = -1;
	    tmpT = -1;
	}
	exit = tmpE;
	time = tmpT;
    }



    @Override
    public String toString () {
	return String.format ("[time: %1.3f exit: %d '%s']", time, exit, process.getCommand ());
    }
}
