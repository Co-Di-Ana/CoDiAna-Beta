package edu.codiana.derivation;

import edu.codiana.utils.Strings;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class TaskSolutionFile extends File {

    //--------------------------------------
    // SMLP
    //--------------------------------------
    private boolean isOpen;
    private boolean isOver;
    //--------------------------------------
    // ADV
    //--------------------------------------
    private BufferedReader reader;
    private String nextLine;
    private String currentLine;



    /**
     * Create instance from given file
     * @param file 
     */
    public TaskSolutionFile (final File file) {
	this (file.getPath ());
    }



    /**
     * Create instance from given filepath
     * @param filename 
     */
    public TaskSolutionFile (final String filename) {
	super (filename);
    }



    /**
     * try to reads another file from file
     * @return line of file or {#code null} if end of file was reached
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String readLine () throws FileNotFoundException, IOException {
	if (!isOpen) {
	    open ();
	    currentLine = getNextLine ();
	    nextLine = getNextLine ();
	    return currentLine;
	}
	currentLine = nextLine;
	nextLine = getNextLine ();
	return currentLine;
    }



    /**
     * @return whether has this file next not-null line
     */
    public boolean hasNextLine () {
	return !isOpen || nextLine != null;
    }



    /**
     * returns data of all file as String
     * @return file content
     * @throws FileNotFoundException when file does not exists
     * @throws IOException error during reading
     */
    public String readAll () throws FileNotFoundException, IOException {
	StringBuilder result = new StringBuilder ((int) length ());
	while (hasNextLine ()) {
	    result.append (readLine ());
	    result.append ('\n');
	}

	return result.toString ();
    }



    /**
     * returns {@code array of String} conitaining every line
     * @return String[] of lines
     * @throws FileNotFoundException when file does not exists
     * @throws IOException error during reading
     */
    public String[] readAsArray () throws FileNotFoundException, IOException {
	ArrayList<String> result = new ArrayList<String> (estimatedNumLines () * 2);
	while (hasNextLine ())
	    result.add (readLine ());
	return result.toArray (new String[result.size ()]);
    }



    /**
     * returns {@code array of String} conitaining lines
     * which are not created from whitespace characters (indent, spaces)
     * @return String[] of non-empty lines
     * @throws FileNotFoundException when file does not exists
     * @throws IOException error during reading
     */
    public String[] readAsArrayWithNoBlankLines () throws FileNotFoundException, IOException {
	ArrayList<String> result = new ArrayList<String> (estimatedNumLines () * 2);
	String line;
	while (hasNextLine ()) {
	    line = readLine ();

	    if (!Strings.isOnlyWhitespace (line))
		result.add (line);
	}
	return result.toArray (new String[result.size ()]);
    }



    /**
     * file length in chars
     * @return file length
     */
    public long numChars () {
	return length ();
    }



    /**
     * returns estimated number of lines
     * this value is not precise
     * @return estimated number of lines
     */
    public int estimatedNumLines () {
	return (int) (length () / 30);
    }



    //--------------------------------------------------------------------------
    //
    // PRIVATES
    //
    //--------------------------------------------------------------------------
    private void open () throws FileNotFoundException {
	reader = new BufferedReader (new FileReader (this));
	isOpen = true;
    }



    private void close () throws IOException {
	if (isOpen) {
	    reader.close ();
	    isOver = true;
	}
    }



    private String getNextLine () throws IOException {
	if (isOver) return null;
	String l = reader.readLine ();
	if (l == null)
	    close ();
	return l;
    }
}
