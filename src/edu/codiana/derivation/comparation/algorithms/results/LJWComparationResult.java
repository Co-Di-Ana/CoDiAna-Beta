package edu.codiana.derivation.comparation.algorithms.results;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LJWComparationResult extends ComparationResult {

    public final int[][] matrix;



    public LJWComparationResult (int currentError, int maxError, int[][] matrix) {
	super (currentError, maxError);
	this.matrix = matrix;
    }
}
