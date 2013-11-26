package edu.codiana.core.iface;

import edu.codiana.execution.results.ProgRunResult;
import edu.codiana.utils.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * Interface which is able to execute program and redirect its outputs
 * @author Jan Hybš
 * @version 1.0
 */
public interface IProgExecutor {

    /**
     * method sets main file to this solution
     * @param file which contains source code
     * @throws Exception when file does not exists
     */
    void setSourceFile (File file) throws FileNotFoundException;



    /**
     * sets default program input
     * @param stdin path to the file
     */
    void setStdin (String stdin);



    /**
     * sets default program output
     * @param stdout path to the file
     */
    void setStdout (String stdout);



    /**
     * sets default program error output
     * @param stderr path to the file
     */
    void setStderr (String stderr);



    /**
     * method execute code and return result. Default execute time limit is 10s.
     * Default number of tests is 3
     * @return <code>ProgRunResult</code> containing all necesseary
     * informations
     */
    ProgRunResult execute () throws IOException;



    /**
     * method execute code and return result
     * @param numberOfTests specified number of tests
     * @param timeLimit specified execute limitation in ms
     * @return <code>ProgRunResult</code> containing all necesseary
     * informations
     */
    ProgRunResult execute (int numberOfTests, int timeLimit) throws IOException;



    /**
     * sets non-standart path (relative path to execute) parameter
     * @param path max added to compilation or execution command
     */
    void setPath (String sourcePath);



    /**
     * returns current compilation and executive path 
     * @return current compilation and executive path which may added to compilation command
     */
    String getPath ();
}
