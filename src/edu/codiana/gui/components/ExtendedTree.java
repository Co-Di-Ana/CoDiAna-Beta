package edu.codiana.gui.components;

import edu.codiana.utils.Files.ExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ExtendedTree extends JTree {

    /**
     * Creates instance using start directory which will be searched recursively
     * @param dir
     * @param filter 
     */
    public ExtendedTree (File dir, ExtensionFilter filter) {
	super.setModel (new DefaultTreeModel (addNodes (null, dir, filter)));
    }



    private DefaultMutableTreeNode addNodes (DefaultMutableTreeNode curTop, File dir, ExtensionFilter filter) {
	String curPath = dir.getPath ();
	DefaultMutableTreeNode curDir = new DefaultMutableTreeNode (curPath);
	if (curTop != null) { // should only be null at root
	    curTop.add (curDir);
	}
	List<File> dirFiles = new ArrayList<File> ();
	dirFiles.addAll (Arrays.asList (dir.listFiles (filter)));
	Collections.sort (dirFiles, new FileComparator ());


	File file;
	List<String> files = new ArrayList<String> ();
	// Make two passes, one for Dirs and one for Files. This is #1.
	for (int i = 0; i < dirFiles.size (); i++) {
	    String thisObject = dirFiles.get (i).getName ();
	    String newPath;
	    if (curPath.equals ("."))
		newPath = thisObject;
	    else
		newPath = curPath + File.separator + thisObject;
	    if ((file = new File (newPath)).isDirectory ())
		addNodes (curDir, file, filter);
	    else
		files.add (thisObject);
	}
	// Pass two: for files.
	for (int fnum = 0; fnum < files.size (); fnum++)
	    curDir.add (new DefaultMutableTreeNode (files.get (fnum)));
	return curDir;
    }



    @Override
    public void setModel (TreeModel newModel) {
	//#
    }



    private static final class FileComparator implements Comparator<File> {

	@Override
	public int compare (File o1, File o2) {
	    return String.CASE_INSENSITIVE_ORDER.compare (o1.toString (), o2.toString ());
	}
    }
}
