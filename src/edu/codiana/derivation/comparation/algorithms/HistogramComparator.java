package edu.codiana.derivation.comparation.algorithms;

import edu.codiana.derivation.comparation.algorithms.results.HistogramComparationResult;
import java.util.HashMap;
import java.util.Map;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class HistogramComparator {

    /**
     * Method computes historgram from given string
     * @param value
     * @return Map where keys are tokens and values are counts of string tokens
     */
    public static Map<String, Integer> computeHistogram (String value) {
	Map<String, Integer> result = new HashMap<String, Integer> ();
	StringBuilder token = new StringBuilder ();

	for (int i = 0; i < value.length (); i++) {
	    char c = value.charAt (i);

	    if (isValidChar (c))
		token.append ((char) c);
	    else if (token.length () > 0) {
		if (result.containsKey (token.toString ()))
		    result.put (token.toString (), result.get (token.toString ()) + 1);
		else
		    result.put (token.toString (), 1);

		token.delete (0, token.length ());
	    }
	}


	return result;
    }



    /**
     * Method compares two strings
     * @param valueA
     * @param valueB
     * @return HistogramComparationResult containing info
     */
    public static HistogramComparationResult compare (String valueA, String valueB) {
	return compare (computeHistogram (valueA), computeHistogram (valueB));
    }



    /**
     * Method compares string which will be converted to a map and map
     * @param valueA
     * @param mapB
     * @return HistogramComparationResult containing info
     */
    public static HistogramComparationResult compare (String valueA, Map<String, Integer> mapB) {
	return compare (computeHistogram (valueA), mapB);
    }



    /**
     * Method compares two maps
     * @param mapA
     * @param mapB
     * @return HistogramComparationResult containing info
     */
    public static HistogramComparationResult compare (Map<String, Integer> mapA, Map<String, Integer> mapB) {

	int error = 0;
	int k0, k1;
	int result = 0;
	for (String s : mapA.keySet ())
	    if (mapB.containsKey (s)) {
		result = Math.abs ((k0 = mapA.get (s)) - (k1 = mapB.get (s)));
		error += k0 + k1;
	    } else {
		result += (k0 = mapA.get (s));
		error += k0;
	    }

	for (String s : mapB.keySet ())
	    if (!mapA.containsKey (s)) {
		result += (k1 = mapB.get (s));
		error += k1;
	    }


	return new HistogramComparationResult (result, error, mapA, mapB);
    }



    private static boolean isValidChar (char c) {
	return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == '.' || c == '_';
    }
}
