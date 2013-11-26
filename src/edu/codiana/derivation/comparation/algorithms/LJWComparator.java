package edu.codiana.derivation.comparation.algorithms;

import edu.codiana.derivation.comparation.algorithms.results.LJWComparationResult;
import edu.codiana.parsing.structures.MethodBlock;
import edu.codiana.utils.JaroWinkler;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LJWComparator {

    public static final int DELETION = 10;
    public static final int INSERTION = 10;
    public static final int SUBSTITUTION = 10;
    public static final int EDITATION = 1;
    public static final double STRING_EDIT_THRESHOLD = 0.70;
    public static final double STRING_EQUAL_THRESHOLD = 0.85;
    public static final double LINE_COMAPARATIBILITY_THRESHOLD = 2.0;
    private static final int[][] NO_RESULT = null;



    private static int min (int a, int b, int c) {
	a = b < a ? b : a;
	return c < a ? c : a;
    }



    /**
     * Method compares two method blocks and returns LJWComparationResult
     * @param methodA
     * @param methodB
     * @return instance of LJWComparationResult containing all info
     * @see LJWComparationResult
     */
    public static LJWComparationResult compare (MethodBlock methodA, MethodBlock methodB) {
	//# if line count is too different, don't compare
	float min = Math.min (methodA.numLines, methodB.numLines);
	float max = Math.max (methodA.numLines, methodB.numLines);
	if (max / min > LINE_COMAPARATIBILITY_THRESHOLD)
	    return new LJWComparationResult ((int) ((min + max) / 2) * SUBSTITUTION, (int) max * SUBSTITUTION, NO_RESULT);

	//# otherwise compare line by line
	return compare (methodA.getLines (), methodB.getLines ());
    }



    /**
     * Method compares two arrays of string and returns LJWComparationResult
     * @param arrayA
     * @param arrayB
     * @return instance of LJWComparationResult containing all info
     * @see LJWComparationResult
     */
    public static LJWComparationResult compare (String[] arrayA, String[] arrayB) {
	int n, m, i, j;
	double cost;
	String si, ji;
	n = arrayA.length;
	m = arrayB.length;
	int[][] result = new int[n + 1][m + 1];

	for (i = 0; i <= n; i++)
	    result[i][0] = i * SUBSTITUTION;

	for (j = 0; j <= m; j++)
	    result[0][j] = j * SUBSTITUTION;


	for (i = 1; i <= n; i++) {
	    si = arrayA[i - 1];

	    for (j = 1; j <= m; j++) {
		ji = arrayB[j - 1];

		cost = JaroWinkler.similarity (si, ji);

		if (cost >= STRING_EQUAL_THRESHOLD)
		    result[i][j] = result[i - 1][j - 1];
		else if (cost >= STRING_EDIT_THRESHOLD)
		    result[i][j] = min (result[i - 1][j] + DELETION,
					result[i][j - 1] + INSERTION,
					result[i - 1][j - 1] + EDITATION);
		else
		    result[i][j] = min (result[i - 1][j] + DELETION,
					result[i][j - 1] + INSERTION,
					result[i - 1][j - 1] + SUBSTITUTION);
	    }
	}

	return new LJWComparationResult (result[n][m], Math.max (m, n) * SUBSTITUTION, result);
    }
}
