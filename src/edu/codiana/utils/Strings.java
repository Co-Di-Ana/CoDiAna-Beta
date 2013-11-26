package edu.codiana.utils;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Strings {

    /**
     * Method compares given string to and whitespace chars
     * @param value
     * @return true if string si made of whitespace
     */
    public static boolean isOnlyWhitespace (String value) {
	if (value == null)
	    return true;

	int l = value.length ();
	char c;

	if (l == 0)
	    return true;
	for (int i = 0; i < l; i++) {
	    c = value.charAt (i);
	    if (!(c == '\n' || c == '\t' || c == '\r' || c == ' '))
		return false;
	}

	return true;
    }



    /**
     * Method splits given string into array of string
     * @param value
     * @return array of lines
     */
    public static String[] toArray (String value) {
	return value.split ("\\r?\\n");
    }



    /**
     * Method count num lines. Counting is clever
     * @param value
     * @return 0 if value is null or its length is 0 otherwise one and more
     */
    public static int numLines (String value) {
	if (value == null || value.length () == 0)
	    return 0;
	int lines = 1;
	int len = value.length ();
	for (int pos = 0; pos < len; pos++) {
	    char c = value.charAt (pos);
	    if (c == '\r') {
		lines++;
		if (pos + 1 < len && value.charAt (pos + 1) == '\n')
		    pos++;
	    } else if (c == '\n')
		lines++;
	}
	return lines;
    }



    /**
     * Method outputs given matrix
     * @param matrix 
     */
    public static void printMatrix (int[][] matrix) {
	printMatrix (matrix, 0, 0);
    }



    /**
     * Method outputs given matrix, slicing from start by the given params
     * @param matrix 
     */
    public static void printMatrix (int[][] matrix, int shiftX, int shiftY) {
	for (int i = shiftX; i < matrix.length; i++) {
	    int[] is = matrix[i];
	    for (int j = shiftY; j < is.length; j++) {
		int k = is[j];
		System.out.format ("%5d", k);
	    }
	    System.out.println ();
	}
    }
}
