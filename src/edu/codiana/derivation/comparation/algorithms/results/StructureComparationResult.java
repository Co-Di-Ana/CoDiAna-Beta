package edu.codiana.derivation.comparation.algorithms.results;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class StructureComparationResult extends ComparationResult {

    public final String[] structure;



    public StructureComparationResult(int currentError, int maxError, String[] structure) {
	super(currentError, maxError);
	this.structure = structure;
    }
}
