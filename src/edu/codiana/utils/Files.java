package edu.codiana.utils;

import edu.codiana.core.iface.IProgramEntry;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Hans
 */
public class Files {

    /**
     * Method recursively go throuhg start directory and its sub-folders and return valid files
     * @param entry which holds info about which file accept and which do not
     * @param startDirectory where to start
     * @return 
     */
    public static File[] getSolutionFiles (IProgramEntry entry, File startDirectory) {

	//# invalid
	if (entry == null || startDirectory == null || !startDirectory.exists ())
	    return null;

	//# single solution file
	if (!entry.isZipped ())
	    return new File[] {new File (startDirectory.getPath () + "/" + entry.getMainFilePath ())};

	//# more files
	List<File> result = new ArrayList<File> ();
	getSolutionFiles0 (new ExtensionFilter (entry.getLanguage ()), startDirectory, result);
	return result.toArray (new File[result.size ()]);
    }



    private static void getSolutionFiles0 (ExtensionFilter filter, File directory, List<File> result) {
	File[] files = directory.listFiles (filter);
	for (int i = 0; i < files.length; i++) {
	    File file = files[i];

	    if (file.isFile ()) result.add (file);
	    else getSolutionFiles0 (filter, file, result);
	}
    }



    /**
     * Class for extension filter
     * @author Jan Hybš
     * @version 1.0
     */
    public static final class ExtensionFilter implements FileFilter {

	private final String extension;



	/**
	 * Creates instance with required extension
	 * @param extension 
	 */
	public ExtensionFilter (String extension) {
	    this.extension = extension;
	}



	@Override
	public boolean accept (File file) {
	    return file == null || !file.exists () ?
		   false : file.isDirectory () || file.getName ().toLowerCase ().endsWith (extension);
	}
    }
}
