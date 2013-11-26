package edu.codiana.utils.tests;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class TimeCheck extends SimpleCheck {

    private float computedTime = -1;
    private float timeInstantValue = -1;



    /**
     * calculate time result from the given rating formula
     * @param rating formula
     * @param minTime minimum time in ms
     * @param maxTime maximum time in ms
     * time instatnt value will be -1 if there is time overshoot,
     * linear position (0.0 - 1.0) from min time
     * to max time if formula is empty, otherwise linear position of time among
     * subconditions in formula (0.0 - 1.0) e. g.
     * <code>last condition time &lt;= computed time &lt; condition time</code>
     */
    public void calculateResult (String rating, long minTime, long maxTime) {

	//# teacher or no check wont be calculated
	if (isTeacher) {
	    setResults (computedTime, NO_VALUE, NO_VALUE);
	    return;
	}

	if (rating.equals (NO_CHECK)) {
	    setResults (NO_VALUE, NO_VALUE, NO_VALUE);
	    return;
	}

	//# time exceeded maximum so no check and sayonara
	if (computedTime > maxTime) {
	    setResults (computedTime, ZERO_VALUE, ZERO_VALUE);
	    return;
	}

	if (rating.length () != 0) {
	    //# array of conditions
	    String[] conditions = ("0:100-" + rating + "-" + maxTime + ":0").split ("-");

	    int lastConditionTime = (int) minTime;
	    int lastConditionValue = 100;

	    //# searching for the right condition
	    for (int i = 0; i < conditions.length; i++) {
		String[] condition = conditions[i].split (":");
		int conditionTime = Integer.parseInt (condition[0]);
		int conditionValue = Integer.parseInt (condition[1]);
		if (computedTime >= lastConditionTime && computedTime <
							 conditionTime) {
		    timeInstantValue = (conditionValue +
					(1 - (computedTime -
					      lastConditionTime) /
					     (conditionTime -
					      lastConditionTime)) *
					(lastConditionValue -
					 conditionValue));
		    break;
		}
		lastConditionTime = conditionTime;
		lastConditionValue = conditionValue;
	    }
	} else
	    timeInstantValue = ((1 - (computedTime) / maxTime) * 100);


	timeInstantValue /= 100;
	setResults (computedTime, timeInstantValue, timeInstantValue * weight);
    }



    /**
     * Method sets time in milis as computed time
     * @param computedTime time in ms
     */
    public void setComputedTime (float computedTime) {
	this.computedTime = computedTime;
    }



    @Override
    public boolean isSupported () {
	return true;
    }
}
