package edu.codiana.utils.io;

import java.io.FileWriter;
import java.io.IOException;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class File extends java.io.File {

    public File (String path) {
	super (path);
    }



    /**
     * returns name of the file
     * @return name of the file without extension
     */
    public String name () {
	String str = getName ();
	int p = str.lastIndexOf ('.');
	return p == -1 ? "" : str.substring (0, p);
    }



    /**
     * returns file extension beggining with dot (.)
     * @return file extension beggining with dot (.)
     */
    public String extension () {
	String str = getName ();
	int p = str.lastIndexOf ('.');
	return p == -1 ? "" : str.substring (p, str.length ());
    }



    /**
     * returns name of the file without extension including path
     * @return name of the file without extension including path
     */
    public String noExtension () {
	String str = getPath ();
	int p = str.lastIndexOf ('.');
	return p == -1 ? str : str.substring (0, p);
    }



    /**
     * returns new File instance with different extension
     * @param ext new extension
     * @return new File instance with different extension
     */
    public File changeExtension (String ext) {
	return new File (noExtension () + ext);
    }



    /**
     * go to parent
     * @return parent File
     */
    public File parent () {
	return new File (getParent ());
    }



    /**
     * go to parents filder with same file name
     * @return parent new file level below current file
     */
    public File dotdot () {
	return new File (parent ().parent ().getPath () + "/" + getName ());
    }



    /**
     * resolve path by additional given parameter
     * @param path path which will be added to current path
     * @return new File consisting from joined paths
     */
    public File resolvePath (String path) {
	return new File (getPath () + "/" + path);
    }



    /**
     * clear file content
     */
    public void clear () {
	FileWriter fw = null;
	try {
	    fw = new FileWriter (this, false);
	    fw.flush ();
	} catch (IOException ex) {
	    System.out.println (ex);
	} finally {
	    try {
		fw.close ();
	    } catch (IOException ex) {
		System.out.println (ex);
	    }
	}
    }



    @Override
    public String toString () {
	return (exists () ? "" : "!") + getPath ();
    }
}
