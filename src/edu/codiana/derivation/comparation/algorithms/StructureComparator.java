package edu.codiana.derivation.comparation.algorithms;

import edu.codiana.derivation.comparation.algorithms.results.LJWComparationResult;
import edu.codiana.parsing.structures.MethodBlock;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class StructureComparator {

    public static LJWComparationResult compare(MethodBlock c0, MethodBlock c1) {

	String[] a = new String[c0.structure.size()];
	c0.structure.toArray(a);
	String[] b = new String[c1.structure.size()];
	c1.structure.toArray(b);
	return LJWComparator.compare(a, b);
    }
}
