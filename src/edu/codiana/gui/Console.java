package edu.codiana.gui;

import java.awt.Dimension;
import java.awt.Window;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Console extends javax.swing.JFrame {

    private static Console instance;
    private final Dimension size = new Dimension (900, 480);



    /**
     * Method returns instace of Console if there is such
     * @return reference or null
     */
    public static Console getInstance () {
	return instance;
    }



    /**
     * Method creates if needed instance and shows it
     */
    public static void showConsole () {
	if (instance == null)
	    instance = new Console ();
	instance.setVisible (true);
    }



    /**
     * Method closes Console
     * @see Window#dispose() 
     */
    public static void hideConsole () {
	if (instance != null)
	    instance.dispose ();
	instance = null;
    }



    private Console () {
	super ("Console");
	initComponents ();
	setMaximumSize (size);
	setPreferredSize (size);
	setSize (size);
	setResizable (true);
    }



    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        consoleSP = new javax.swing.JScrollPane();
        consoleTA = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        consoleTA.setColumns(20);
        consoleTA.setFont(new java.awt.Font("Consolas", 0, 11)); // NOI18N
        consoleTA.setRows(5);
        consoleSP.setViewportView(consoleTA);

        getContentPane().add(consoleSP, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane consoleSP;
    private javax.swing.JTextArea consoleTA;
    // End of variables declaration//GEN-END:variables



    /**
     * Method appends new string to JTextArea and scrolls down
     * @param string 
     */
    public void append (String string) {
	consoleTA.setCaretPosition (consoleTA.getDocument ().getLength ());
	consoleTA.append (string);
    }
}
