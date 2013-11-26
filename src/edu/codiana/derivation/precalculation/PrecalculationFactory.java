package edu.codiana.derivation.precalculation;

import edu.codiana.core.iface.IPrecalculation;
import edu.codiana.derivation.TaskSolution;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class PrecalculationFactory {

    /**
     * method returns precalculation instance if possible and assign its data
     * @param solution TaskSolution on which object is assigned
     * @return IPrecalculation object
     */
    public static IPrecalculation getInstance (TaskSolution solution) {
	IPrecalculation result;
	if (solution.entry.language.indexOf ("java") != -1) {
	    result = new JavaPrecalculation ();
	    result.setTaskSolution (solution);
	    return result;
	}

	return null;
    }
}
