package edu.codiana.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class FileLogger {

    private File file;
    private BufferedWriter fw;
    private BufferedReader br;
    private final SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss:SSSS | ");
    private final String FILE_START_SEPARATOR =
			 "--------------------------------";
    private final String FILE_START_SEQUENCE = "|| ";
    private final String LOG_SEPARATOR = "\n\n\n\n\n\n\n\n";
    private final PrintStream ps;



    /**
     * Create instance using destination file and destination stream
     * @param file where to save data
     * @param ps  where to print data
     */
    public FileLogger (File file, PrintStream ps) {
	this.file = file;
	this.ps = ps;
	if (!file.exists ())
	    log ("", false, false, true);
    }



    /**
     * Logs simply value
     * @param value to be logged
     */
    public void log (String value) {
	log (value, true);
    }



    /**
     * Logs value and on demand add current date
     * @param value to be logged
     * @param logDate whether or not append date
     */
    public void log (String value, Boolean logDate) {
	log (value, logDate, true, true);
    }



    /**
     * Method adds few blank line to seperates logs
     */
    public void newLog () {
	log (LOG_SEPARATOR, false);
    }



    /**
     * Method logs entire file (its content)
     * @param file to be logged
     */
    public void log (File file) {
	logFile (file);
    }



    private void logFile (File f) {
	if (!f.exists ())
	    return;
	if (!openStream ())
	    return;

	String line;
	try {
	    br = new BufferedReader (new FileReader (f));
	    write (FILE_START_SEQUENCE);
	    write (FILE_START_SEPARATOR);
	    write (FILE_START_SEPARATOR);
	    newLine ();


	    while ((line = br.readLine ()) != null) {
		write (FILE_START_SEQUENCE);
		write (line);
		newLine ();
	    }

	    br.close ();
	    write (FILE_START_SEQUENCE);
	    write (FILE_START_SEPARATOR);
	    write (FILE_START_SEPARATOR);
	    newLine ();
	} catch (IOException ex) {
	    log ("Failed to open File " + f, true, true, true);
	}

	closeStream ();
    }



    private void log (String value, Boolean logDate, Boolean appendLine, Boolean closeStream) {
	if (openStream ()) {
	    //# append date
	    if (logDate) {
		Calendar c = Calendar.getInstance ();
		write (formatter.format (c.getTime ()));
	    }

	    //# append message
	    write (value);

	    //# append new line
	    if (appendLine)
		newLine ();

	    if (closeStream)
		closeStream ();
	}
    }



    private Boolean openStream () {
	try {
	    fw = new BufferedWriter (new FileWriter (file, true));
	} catch (IOException ex) {
	    return false;
	}
	return true;
    }



    private Boolean closeStream () {
	try {
	    fw.close ();
	} catch (IOException ex) {
	    return false;
	}
	return true;
    }



    private void write (Object s) {
	try {
	    fw.write (s.toString ());
	    if (ps != null)
		ps.print (s.toString ());
	} catch (IOException ex) {
	    //# logger not available
	}
    }



    private void newLine () {
	write ('\n');
    }
}
