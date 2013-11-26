package edu.codiana.execution.factory;

import edu.codiana.core.iface.IProgExecutor;
import edu.codiana.core.iface.IProgramEntry;
import edu.codiana.execution.JavaExecutor;
import edu.codiana.execution.PythonExecutor;
import java.util.HashMap;
import java.util.Map;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ProgramFactory {

    private static final String JAVA_LANGUAGE = ".java";
    private static final String PYTHON_LANGUAGE = ".py";
    private static final Map<String, Class> map;



    static {
	map = new HashMap<String, Class> ();
	map.put (JAVA_LANGUAGE, JavaExecutor.class);
	map.put (PYTHON_LANGUAGE, PythonExecutor.class);
    }



    /**
     * Method returns specific Executor instance based on given extension
     * @param extension file extension
     * @return {@code IProgExecutor} or null if extension is not supported
     * @throws Exception
     * @see IProgExecutor
     * @see JavaExecutor
     * @see PythonExecutor
     */
    public static IProgExecutor getInstance (String extension) {
	if (map.containsKey (extension)) {
	    try {
		return (IProgExecutor) map.get (extension).newInstance ();
	    } catch (InstantiationException ex) {
		return null;
	    } catch (IllegalAccessException ex) {
		return null;
	    }
	}
	return null;
    }



    /**
     * Method determines whether given instance of {@code IProgramEntry} is supported
     * @param entry Object containing infomation needed for decision
     * @return true if supprted false otherwise
     */
    public static boolean isSupported (IProgramEntry entry) {
	String language = entry.getLanguage ();
	return map.containsKey (language);
    }
}
