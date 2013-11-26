package edu.codiana.gui;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public interface GUIListener {

    /**
     * Method dispatch pause event
     */
    void pause ();



    /**
     * Method dispatch play event
     */
    void play ();



    /**
     * Method dispatch manualCheck change
     */
    void setManualCheck (boolean value);
}
