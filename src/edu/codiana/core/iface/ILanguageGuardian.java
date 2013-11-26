package edu.codiana.core.iface;

import edu.codiana.execution.security.JavaGuardian;
import edu.codiana.execution.security.PythonGuardian;
import edu.codiana.execution.security.structures.SecurityResult;
import java.io.File;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public interface ILanguageGuardian {

    /**
     * Method check given file for security risks
     * @param file
     * @return {@code SecurityResult} if some risk exists otherwise {@code null}
     * @see SecurityResult
     * @see JavaGuardian
     * @see PythonGuardian
     */
    SecurityResult check (File file);
}
