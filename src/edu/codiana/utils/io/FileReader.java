package edu.codiana.utils.io;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class FileReader {

    /**
     * read given file and return StringBuilder representing this file
     * all line will be reduced to \n
     * @param file
     * @return StringBuilder from this file
     */
    public static StringBuilder readFile (java.io.File file, String hideLine) {
	StringBuilder result = new StringBuilder ((int) file.length ());
	try {
	    BufferedReader reader = new BufferedReader (new java.io.FileReader (file));
	    String line;
	    while ((line = reader.readLine ()) != null) {
		if (hideLine != null && line.indexOf (hideLine) != -1) result.append (line.replace (hideLine, "<hidden>"));
		else result.append (line);
		result.append ('\n');
	    }
	    return result;
	} catch (FileNotFoundException ex) {
	    return new StringBuilder ();
	} catch (IOException ex) {
	    return new StringBuilder ();
	}
    }



    /**
     * read given file and return StringBuilder representing this file
     * all line will be reduced to \n
     * @param file
     * @return StringBuilder from this file
     */
    public static StringBuilder readFile (java.io.File file) {
	return readFile (file, null);
    }



    /**
     * Method returns input stream from file
     * @param file
     * @return FileInputStream
     */
    public static InputStream getFileStream (java.io.File file) {
	try {
	    return new FileInputStream (file);
	} catch (FileNotFoundException ex) {
	    return null;
	}
    }



    /**
     * method returns input stream from string
     * @param value
     * @return ByteArrayInputStream
     */
    public static InputStream getStringStream (String value) {
	try {
	    return new ByteArrayInputStream (value.getBytes ("UTF-8"));
	} catch (UnsupportedEncodingException ex) {
	    return null;
	}
    }
}
