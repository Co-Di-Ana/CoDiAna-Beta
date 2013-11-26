package edu.codiana.utils.tests;



/**
 * @author Hans
 */
public abstract class SimpleCheck {

    protected float weight;
    protected boolean isTeacher;
    protected final String NO_CHECK = "-1";
    protected final int NO_VALUE = -1;
    protected final int ZERO_VALUE = 0;
    protected float rawValue = NO_VALUE, result = NO_VALUE, weightedResult = NO_VALUE;



    /**
     * Method sets whether subject is teacher
     * @param isTeacher 
     */
    public void setIsTeacher (boolean isTeacher) {
	this.isTeacher = isTeacher;
    }



    protected void setResults (float rawValue, float result, float weightedResult) {
	this.rawValue = rawValue;
	this.result = result;
	this.weightedResult = weightedResult;
    }



    /**
     * method sets weight for this check
     * note that weight must be distrubuted in way 
     * that their sum must be equal to 100 otherwise
     * unbalanced resulst may occure
     * @param weight 
     */
    public void setWeight (float weight) {
	this.weight = weight;
    }



    /**
     * method returns whether is this check/monitor supported
     * @return true if it is false otherwise
     */
    public abstract boolean isSupported ();



    /**
     * method returns result multiplied by weight
     * @return number representing weighted result for this check
     */
    public float getWeightedResult () {
	return weightedResult;
    }



    /**
     * method returns raw percentage result
     * @return number from 0.0 to 1.0
     */
    public float getResult () {
	return result;
    }



    /**
     * method returns measured value
     * @return valuw which was measured
     */
    public float getRawValue () {
	return rawValue;
    }
}
