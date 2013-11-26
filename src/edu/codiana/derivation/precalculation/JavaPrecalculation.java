package edu.codiana.derivation.precalculation;

import edu.codiana.core.iface.IPrecalculation;
import edu.codiana.derivation.TaskSolution;
import edu.codiana.parsing.JavaPa;
import japa.parser.ParseException;
import java.io.IOException;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class JavaPrecalculation implements IPrecalculation {

    private TaskSolution taskSolution;
    private JavaPa javaPa;



    @Override
    public void setTaskSolution (TaskSolution value) {
	taskSolution = value;
	try {
	    javaPa = new JavaPa (taskSolution);
	} catch (ParseException ex) {
	    javaPa = null;
	} catch (IOException ex) {
	    javaPa = null;
	}
    }



    /**
     * Method return reference to newly computed and parsed JavaPa
     * @return final JavaPa reference
     */
    public JavaPa getJavaPa () {
	return javaPa;
    }



    @Override
    public String toString () {
	return String.format ("[javaPa='%s', solution='%s']", javaPa, taskSolution);
    }
}
