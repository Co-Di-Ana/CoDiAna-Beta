package edu.codiana.execution.security.factory;

import edu.codiana.core.iface.ILanguageGuardian;
import edu.codiana.core.iface.IProgramEntry;
import edu.codiana.execution.security.JavaGuardian;
import edu.codiana.execution.security.PythonGuardian;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LanguageGuardianFactory {

    /**
     * Method returns language guardian fro given entry
     * @param entry containing information about solution
     * @return instance of ILanguageGuardian or null if none was found
     */
    public static ILanguageGuardian getInstance (IProgramEntry entry) {
	if (entry.getLanguage ().equalsIgnoreCase (".java"))
	    return new JavaGuardian ();
	if (entry.getLanguage ().equalsIgnoreCase (".py"))
	    return new PythonGuardian ();
	return null;
    }
}
