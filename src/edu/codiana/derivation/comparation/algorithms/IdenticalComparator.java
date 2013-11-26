package edu.codiana.derivation.comparation.algorithms;

import edu.codiana.derivation.comparation.algorithms.results.ComparationResult;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class IdenticalComparator {

    public static ComparationResult compare (String a, String b) {
	return new ComparationResult (a.equals (b) ? 0 : 1, 1);
    }
}
