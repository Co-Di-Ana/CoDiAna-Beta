package edu.codiana.gui;

import edu.codiana.gui.components.LogEntry;
import java.awt.Dimension;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LogViewer extends javax.swing.JFrame {

    private final Dimension size = new Dimension (800, 480);



    /**
     * Method displays JFrame with information about given LogEntry
     * @param entry 
     */
    public static void show (LogEntry entry) {
	new LogViewer (entry).setVisible (true);
    }



    private LogViewer (LogEntry entry) {
	initComponents ();
	setTitle (entry.toString ());
	updateSize (size);
	infoTA.setText (entry.getLog ());
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoSP = new javax.swing.JScrollPane();
        infoTA = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        infoTA.setColumns(20);
        infoTA.setFont(new java.awt.Font("Consolas", 0, 12));
        infoTA.setRows(5);
        infoSP.setViewportView(infoTA);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoSP, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoSP, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane infoSP;
    private javax.swing.JTextArea infoTA;
    // End of variables declaration//GEN-END:variables



    private void updateSize (Dimension d) {
	setSize (d);
	setPreferredSize (d);
	setMaximumSize (d);
    }
}
