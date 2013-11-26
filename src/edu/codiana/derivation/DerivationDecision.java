package edu.codiana.derivation;

import edu.codiana.derivation.comparation.ComparatorResult;
import edu.codiana.derivation.Derivator.Level;



/**
 * Class in which are stored criteria by which derivation is flagged
 * @author Jan Hybš
 * @version 1.0
 */
public class DerivationDecision {

    private float lowBoundary, highBoundary, absoluteBoundary;
    private CheckType check;
    //--------------------------------------
    // PUBLIC STATIC FINALS
    //--------------------------------------
    public static final CheckType DEFAULT_CHECK_TYPE = CheckType.MAJORITY_DECIDE;
    public static final float DEFAULT_LOW_BOUNDARY = 0.50f;
    public static final float DEFAULT_HIGH_BOUNDARY = 0.75f;
    public static final float DEFAULT_ABSOLUTE_BOUNDARY = 0.95f;



    /**
     * Creates enumeration of type of checking ComparatorResult
     * <ul>
     *	    <li>{@code AVERAGE_SUM_DECIDE}: sum of all similarities will be compared to high and low boundary</li>
     *	    <li>{@code MAJORITY_DECIDE}: each pair of similarities will be compared to high and low boundary</li>
     *	    <li>{@code EVERY_RESULT_DECIDE}: every similarity will be compared to high and low boundary</li>
     * </ul>
     * @see ComparatorResult
     */
    public enum CheckType {

	AVERAGE_SUM_DECIDE,
	MAJORITY_DECIDE,
	EVERY_RESULT_DECIDE;
    }



    /**
     * create DerivationDecision instance
     * @param lowBoundary number under which will be result classified as {@code LOW}
     * @param highBoundary number under which will be result classified as {@code HIGH}
     * @param check
     * @see Level
     */
    public DerivationDecision(float lowBoundary, float highBoundary, float absoluteBoundary, CheckType check) {
	this.lowBoundary = lowBoundary;
	this.highBoundary = highBoundary;
	this.absoluteBoundary = absoluteBoundary;
	this.check = check;
    }



    /**
     * create DerivationDecision with low boundary {@code DEFAULT_LOW_BOUNDARY},
     * high boundary {@code DEFAULT_HIGH_BOUNDARY} and check type 
     * {@code CheckType.AVERAGE_SUM_DECIDE}
     * @see CheckType
     */
    public DerivationDecision() {
	this(DEFAULT_LOW_BOUNDARY, DEFAULT_HIGH_BOUNDARY, DEFAULT_ABSOLUTE_BOUNDARY, DEFAULT_CHECK_TYPE);
    }



    /**
     * method will return derivation level of compared task solutions
     * @param r ComparatorResult containing information about similarity
     * @return Level of ComparatorResult
     * @see ComparatorResult
     * @see Level
     * @see TaskSolution
     */
    public Level isDerivative(ComparatorResult r) {
	float[] res = new float[]{r.LJWCompareResult, r.histogramCompareResult, r.structureCompareResult};
	float tmp = 0;


	switch (check) {
	    case AVERAGE_SUM_DECIDE:
		for (float f : res)
		    tmp += f;
		tmp /= res.length;
		return tmp > highBoundary ? tmp >= absoluteBoundary ? Level.ABOSOLUTE : Level.HIGH :
		       tmp < lowBoundary ? Level.LOW : Level.MEDIUM;

	    case MAJORITY_DECIDE:
		int abs = 0,
		 high = 0,
		 low = 0,
		 half = (int) (0.5 + res.length / 2);
		for (int i = 0; i < res.length - 1; i++) {
		    for (int j = i + 1; j < res.length; j++) {
			if (res[i] > highBoundary && res[j] > highBoundary)
			    if (res[i] >= absoluteBoundary && res[j] >= absoluteBoundary) abs++;
			    else high++;
			else if (res[i] < lowBoundary && res[j] < lowBoundary)
			    low++;
		    }
		}
		if (abs >= half) return Level.ABOSOLUTE;
		if (high >= half) return Level.HIGH;
		if (low >= half) return Level.LOW;
		return Level.MEDIUM;

	    case EVERY_RESULT_DECIDE:
		tmp = res[0];
		for (int i = 1; i < res.length; i++) {
		    if (res[i] > tmp)
			tmp = res[i];
		}

		return tmp > highBoundary ? tmp >= absoluteBoundary ? Level.ABOSOLUTE : Level.HIGH :
		       tmp < lowBoundary ? Level.LOW : Level.MEDIUM;
	}
	return Level.LOW;
    }
}
