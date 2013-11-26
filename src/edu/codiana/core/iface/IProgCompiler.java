package edu.codiana.core.iface;

import edu.codiana.execution.results.ProgCompileResult;
import java.io.IOException;



/**
 * Interface adding compiling functionality to program executor
 * @author Jans Hybš
 * @version 1.0
 * @see IProgExecutor
 */
public interface IProgCompiler extends IProgExecutor {

    /**
     * method compile program code and return result. Default compilation
     * time limit is 10s
     * @return <code>ProgCompileResult</code> containing all necesseary
     * informations
     */
    ProgCompileResult compile () throws IOException;



    /**
     * method compile program code and return result
     * @param timeLimit specified run limitation in ms
     * @return <code>ProgCompileResult</code> containing all necesseary
     * informations
     */
    ProgCompileResult compile (int timeLimit) throws IOException;
}
