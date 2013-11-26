package edu.codiana.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class DateUtil {

    private static final SimpleDateFormat SIMPLE_FORMATTER = new SimpleDateFormat ("yyyy-MM-dd_HH-mm", Locale.getDefault ());



    /**
     * Method returns current system Date
     * @return current Date
     */
    public static Date now () {
	return new Date ();
    }



    /**
     * Method formats given date
     * @param date to be formatted
     * @return formatted date or 'neznáme' if date is null
     */
    public static String format (Date date) {
	if (date == null)
	    return "neznámé";
	return SIMPLE_FORMATTER.format (date);
    }
}
