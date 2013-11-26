package edu.codiana.utils;

import javax.swing.JOptionPane;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Message {

    public static final String ERROR = "Error";
    public static final String ALERT = "Upozornění";
    public static final String INFO = "Oznámení";


    //--------------------------------------------------------------------------
    //
    // ERROR
    //
    //--------------------------------------------------------------------------

    /**
     * Method shows error JOptionPane dialog with given message
     * @param message to be shown
     */
    public static void showError (String message) {
	JOptionPane.showMessageDialog (null, message, ERROR, JOptionPane.ERROR_MESSAGE);
    }



    /**
     * Method shows formatter error JOptionPane dialog with given message
     * @param format to be shown
     * @param args Arguments referenced by the format specifiers in the format string.
     */
    public static void showError (String format, Object... args) {
	JOptionPane.showMessageDialog (null, String.format (format, args), ERROR, JOptionPane.ERROR_MESSAGE);
    }


    //--------------------------------------------------------------------------
    //
    // ALERT
    //
    //--------------------------------------------------------------------------

    /**
     * Method shows alert JOptionPane dialog with given message
     * @param message to be shown
     */
    public static void showAlert (String message) {
	JOptionPane.showMessageDialog (null, message, ALERT, JOptionPane.WARNING_MESSAGE);
    }



    /**
     * Method shows formatter alert JOptionPane dialog with given message
     * @param format to be shown
     * @param args Arguments referenced by the format specifiers in the format string.
     */
    public static void showAlert (String format, Object... args) {
	JOptionPane.showMessageDialog (null, String.format (format, args), ALERT, JOptionPane.WARNING_MESSAGE);
    }

    //--------------------------------------------------------------------------
    //
    // INFO
    //
    //--------------------------------------------------------------------------


    /**
     * Method shows info JOptionPane dialog with given message
     * @param message to be shown
     */
    public static void showInfo (String message) {
	JOptionPane.showMessageDialog (null, message, INFO, JOptionPane.INFORMATION_MESSAGE);
    }



    /**
     * Method shows formatter info JOptionPane dialog with given message
     * @param format to be shown
     * @param args Arguments referenced by the format specifiers in the format string.
     */
    public static void showInfo (String format, Object... args) {
	JOptionPane.showMessageDialog (null, String.format (format, args), INFO, JOptionPane.INFORMATION_MESSAGE);
    }
}
