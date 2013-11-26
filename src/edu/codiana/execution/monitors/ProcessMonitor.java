package edu.codiana.execution.monitors;

import edu.codiana.core.configuration.Config;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ProcessMonitor {

    private final String EMPTY = "";
    private String command, rawCommand;
    public Process process;
    private String stdin = EMPTY, stdinPart = EMPTY;
    private String stdout = EMPTY, stdoutPart = EMPTY;
    private String stderr = EMPTY, stderrPart = EMPTY;



    public ProcessMonitor (String command) {
	this.command = command;
    }



    /**
     * return proccess OUTPUT stream (which is input from this look)
     * @return process output stream
     */
    public InputStream getInputStream () {
	return process.getInputStream ();
    }



    /**
     * return proccess error stream
     * @return process error stream
     */
    public InputStream getErrorStream () {
	return process.getErrorStream ();
    }



    /**
     * sets default command input
     * @param filepath path to the file
     */
    public void setStdin (String filepath) {
	stdin = filepath;
	stdinPart = stdin == null ||
		    stdin.length () == 0 ||
		    stdin.equalsIgnoreCase ("<null>") ?
		    "" : "-i \"" + filepath + "\"";
    }



    /**
     * sets default command output
     * @param filepath path to the file
     */
    public void setStdout (String filepath) {
	stdout = filepath;
	stdoutPart = stdout == null ||
		     stdout.length () == 0 ||
		     stdout.equalsIgnoreCase ("<null>") ?
		     "" : "-o \"" + filepath + "\"";
    }



    /**
     * sets default command error output
     * @param filepath path to the file
     */
    public void setStderr (String filepath) {
	stderr = filepath;
	stderrPart = stderr == null ||
		     stderr.length () == 0 ||
		     stderr.equalsIgnoreCase ("<null>") ?
		     "" : "-e \"" + filepath + "\"";
    }



    /**
     * method run code and return result. Default run time limit is 10s.
     * @return <code>ProgRunResult</code> containing all necesseary
     * informations
     */
    public int run () {
	return run (10 * 1000);
    }



    /**
     * method run code and return result
     * @param timeLimit specified run limitation in ms
     * @return <code>ProgRunResult</code> containing all necesseary
     * informations
     */
    public int run (int timeLimit) {
	try {
	    rawCommand = String.format (
		    "\"%s\" -m %d -c \"%s\" %s %s %s",
		    Config.getProcessRunner (), timeLimit, command, stdinPart, stdoutPart, stderrPart);
            System.out.println(rawCommand);
	    process = Runtime.getRuntime ().exec (rawCommand);
	} catch (IOException ex) {
	    ex.printStackTrace ();
	    return -1;
	}

	try {
	    Timer timer = new Timer (true);
	    MonitorThread thread = new MonitorThread ();
	    KillingTask task = new KillingTask (thread);

	    Runtime.getRuntime ().addShutdownHook (thread);

	    timer = new Timer (true);
	    timer.schedule (task, timeLimit + 1000);
	    thread.start ();
	    thread.join ();


	    task.cancel ();
	    timer.cancel ();
	    process.destroy ();

	    if (process.exitValue () != 0)
		return process.exitValue ();

	    if (thread.isInterrupted ())
		throw new InterruptedException ("processinterrupted");

	} catch (InterruptedException e1) {
	    System.out.println (e1);
	    return -1;
	} catch (IllegalThreadStateException e2) {
	    System.out.println (e2);
	    return -1;
	} catch (Exception e) {
	    System.out.println (e);
	    return 1;
	}
	return 0;
    }



    /**
     * Method returns process exit value
     * @return process exit value
     */
    public int exitValue () {
	return process.exitValue ();
    }



    /**
     * Method returns command which was used to build process
     * @return command which was used to build process
     */
    public String getCommand () {
	return command;
    }

    //--------------------------------------------------------------------------
    //
    // PRIVATE CLASSES
    //
    //--------------------------------------------------------------------------


    private final class KillingTask extends TimerTask {

	private Thread thread;



	public KillingTask (Thread t) {
	    super ();
	    thread = t;
	}



	@Override
	public void run () {
	    process.destroy ();
	    thread.interrupt ();
	}
    }



    private final class MonitorThread extends Thread {

	private boolean hasBeenInterrupted = false;



	public MonitorThread () {
	    super ();
	}



	@Override
	public void run () {
	    try {
		process.waitFor ();
	    } catch (InterruptedException e) {
		hasBeenInterrupted = true;
	    }
	}



	@Override
	public void interrupt () {
	    super.interrupt ();
	}



	@Override
	public boolean isInterrupted () {
	    return hasBeenInterrupted;
	}
    }
}
