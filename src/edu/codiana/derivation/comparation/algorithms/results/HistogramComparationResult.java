package edu.codiana.derivation.comparation.algorithms.results;

import java.util.Map;

/**
 * @author Jan Hybš
 * @version 1.0
 */
public class HistogramComparationResult extends ComparationResult {

    public final Map<String, Integer> histogramA, histogramB;



    public HistogramComparationResult(int currentError, int maxError, Map<String, Integer> histogramA,
			   Map<String, Integer> histogramB) {
	super(currentError, maxError);
	this.histogramA = histogramA;
	this.histogramB = histogramB;
    }



}
