package edu.codiana.core.iface;

import edu.codiana.derivation.TaskSolution;
import edu.codiana.derivation.comparation.ComparatorResult;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * Interface for comparing task solutions
 * Can be implmented to many languages
 * @author Jan Hybš
 * @version 1.0
 */
public interface IComparator {

    /**
     * sets default classname so better logic in comparing might be implemented
     * @param classname
     */
    public void setBaseClassName (String classname);



    /**
     * compare two task solutions and returns similarity in each comparing algorithm
     * @param a
     * @param b
     * @return object contatning info about comparism
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ComparatorResult compare (TaskSolution a, TaskSolution b) throws FileNotFoundException, IOException;



    /**
     * method sets precalculation result
     * @param precalculation 
     */
    public void setFirstPrecalculation (IPrecalculation precalculation);



    /**
     * method sets precalculation result
     * @param precalculation 
     */
    public void setSecondPrecalculation (IPrecalculation precalculation);
}
