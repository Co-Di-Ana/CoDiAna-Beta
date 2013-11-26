package edu.codiana.derivation.comparation.algorithms.results;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ComparationResult {

    public final int maxError;
    public final int currentError;



    /**
     * Creates instance from current and max error (like distance)
     * @param currentError
     * @param maxError 
     */
    public ComparationResult (int currentError, int maxError) {
	this.maxError = maxError;
	this.currentError = currentError;
    }



    @Override
    public String toString () {
	return String.format ("comparation[%1.3f (%d) (%d)]", 1.0 - (double) currentError / maxError, maxError,
			      currentError);
    }
}
