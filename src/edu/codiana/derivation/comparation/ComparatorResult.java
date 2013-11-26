package edu.codiana.derivation.comparation;



/**
 * Class holding results with comparism
 * @author Jan Hybš
 * @version 1.0
 */
public class ComparatorResult {

    /** used algorithms results */
    public final float identicalCompareResult, LJWCompareResult, histogramCompareResult, structureCompareResult;



    /**
     * Creates instance which stores computed values for further use
     * @param identicalCompareResult
     * @param LJWCompareResult
     * @param histogramCompareResult
     * @param structureCompareResult 
     */
    public ComparatorResult(float identicalCompareResult, float LJWCompareResult, float histogramCompareResult,
			    float structureCompareResult) {
	this.identicalCompareResult = identicalCompareResult;
	this.LJWCompareResult = LJWCompareResult;
	this.histogramCompareResult = histogramCompareResult;
	this.structureCompareResult = structureCompareResult;
    }



    //--------------------------------------------------------------------------
    //
    // OVERRIDES
    //
    //--------------------------------------------------------------------------
    @Override
    public String toString() {
	return String.format("Identical-compare:   %1.2f %%%n" + "LJW-compare:         %1.2f %%%n" +
			     "Histogram-compare:   %1.2f %%%n" + "Structure-compare:   %1.2f %%%n",
			     100 * identicalCompareResult,
			     100 * LJWCompareResult,
			     100 * histogramCompareResult,
			     100 * structureCompareResult);
    }
}
