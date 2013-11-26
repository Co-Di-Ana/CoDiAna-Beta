package edu.codiana.utils.serialization;

import edu.codiana.utils.DateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class History {

    private static final int LIMIT = 256;
    private static final File LOCATION = new File ("./history/");
    private static final File HISTORY_FILE = new File (LOCATION.getAbsolutePath () + "/history.codiana");
    private static ObjectInputStream input;
    private static ObjectOutputStream output;
    private static List<Object> items;
    private static boolean inited = false;



    /**
     * Method check history files and if necesseary, create new hisory file
     */
    public static void init () {
	if (!HISTORY_FILE.exists ()) {
	    try {
		if (!LOCATION.exists ()) LOCATION.mkdirs ();
		HISTORY_FILE.createNewFile ();
	    } catch (IOException ex) {
		return;
	    }
	}
	loadHistory ();
	final int size = items == null ? -1 : items.size ();

	if (size >= LIMIT)
	    createNextFile ();

	inited = true;
    }



    /**
     * Methos saves serializable object to the history file
     * @param object to be saved
     * @return true on success otherwise false
     */
    public static boolean saveObject (Serializable object) {
	if (!inited) init ();

	loadHistory ();
	final int size = items == null ? -1 : items.size ();
	if (size >= LIMIT) {
	    createNextFile ();
	    items = null;
	}

	try {
	    output = new ObjectOutputStream (new FileOutputStream (HISTORY_FILE, false));
	    try {
		if (items != null)
		    for (int i = 0; i < items.size (); i++)
			output.writeObject (items.get (i));

		output.writeObject (object);
		return true;
	    } catch (IOException ex) {
		ex.printStackTrace ();
		return false;
	    }
	} catch (IOException ex) {
	    ex.printStackTrace ();
	    return false;
	} finally {
	    try {
		output.close ();
	    } catch (IOException ex) {
		ex.printStackTrace ();
		//# well that's about it...
	    }
	}
    }



    /**
     * Method cload all items from current history file and adds them to the List
     * @return list with items or null on error
     */
    public static List<Object> loadItems () {
	if (!inited) init ();

	loadHistory ();
	return items;
    }



    private static void loadHistory () {
	try {
	    items = new ArrayList<Object> ();
	    input = new ObjectInputStream (new FileInputStream (HISTORY_FILE));

	    boolean isEmpty = false;
	    while (!isEmpty) {

		try {
		    items.add (input.readObject ());
		} catch (IOException ex) {
		    //ex.printStackTrace ();
		    isEmpty = true;
		} catch (ClassNotFoundException ex) {
		    //ex.printStackTrace ();
		    isEmpty = true;
		}
	    }
	} catch (IOException ex) {
	    //ex.printStackTrace ();
	} finally {
	    try {
		if (input != null) input.close ();
	    } catch (IOException ex) {
		//ex.printStackTrace ();
		//# well that's about it...
	    }
	}
    }



    private static void createNextFile () {
	try {
	    String date = DateUtil.format (new Date ());
	    int index = 0;
	    boolean addIndex = false;
	    String name = null;
	    File newFile = null;
	    File oldFile = new File (HISTORY_FILE.getAbsolutePath ());

	    while (index < 100) {
		name = String.format ("/history-%s" + (addIndex ? "-%02d" : ""), date, index++);
		if (!(newFile = new File (LOCATION.getAbsolutePath () + name)).exists ())
		    break;
		addIndex = true;
	    }

	    oldFile.renameTo (newFile);
	    HISTORY_FILE.createNewFile ();
	} catch (IOException ex) {
	    inited = false;
	    return;
	}
    }
}
