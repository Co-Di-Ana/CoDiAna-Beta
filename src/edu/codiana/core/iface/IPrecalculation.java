package edu.codiana.core.iface;

import edu.codiana.derivation.TaskSolution;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public interface IPrecalculation {

    /**
     * Method sets source solution containing solution files<br />
     * During precomputation solutions will contain file list which will be precomputed
     * @param solution 
     */
    public void setTaskSolution (TaskSolution solution);
}
