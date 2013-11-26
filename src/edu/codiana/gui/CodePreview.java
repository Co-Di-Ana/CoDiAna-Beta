package edu.codiana.gui;

import edu.codiana.gui.components.ExtendedTree;
import edu.codiana.utils.Files.ExtensionFilter;
import edu.codiana.utils.io.FileReader;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class CodePreview extends javax.swing.JDialog {

    private final CodePreviewResult result;
    private final File startDirectory;
    private final Dimension size = new Dimension (800, 480);
    private final File[] files;
    private int fileIndex;
    private final ExtensionFilter filter;



    /**
     * Method show dialog window with file check pending
     * @param startDirectory
     * @param files
     * @param filter
     * @return whether or not allow this code
     */
    public static CodePreviewResult allow (File startDirectory, File[] files, ExtensionFilter filter) {
	CodePreviewResult result = new CodePreviewResult ();
	CodePreview window = new CodePreview (null, true, startDirectory, files, result, filter);
	window.setVisible (true);
	return result;
    }



    /** 
     * Creates instance of CodePreview
     */
    public CodePreview (java.awt.Frame parent, boolean modal, File startDirectory, File[] files,
			CodePreviewResult result, ExtensionFilter filter) {
	super (parent, modal);
	this.result = result;
	this.files = files;
	this.filter = filter;
	this.startDirectory = startDirectory;
	initComponents ();
	setMaximumSize (size);
	setPreferredSize (size);
	setSize (size);
	setResizable (true);
	codeTA.setTabSize (4);
	next ();
    }



    private void next () {
	if (fileIndex >= files.length) {
	    dispose ();
	    return;
	}


	setTitle ("Code verification " + files[fileIndex].getName ());
	codeTA.setText (FileReader.readFile (files[fileIndex++]).toString ());
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        codeTA = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree1 = new ExtendedTree (startDirectory, filter);

        FormListener formListener = new FormListener();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        codeTA.setColumns(20);
        codeTA.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        codeTA.setRows(5);
        jScrollPane1.setViewportView(codeTA);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 369;
        gridBagConstraints.ipady = 268;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jButton2.setText("secure");
        jButton2.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        getContentPane().add(jButton2, gridBagConstraints);

        jButton1.setText("dangerous");
        jButton1.addActionListener(formListener);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        getContentPane().add(jButton1, gridBagConstraints);

        jScrollPane2.setViewportView(jTree1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        getContentPane().add(jScrollPane2, gridBagConstraints);

        pack();
    }

    // Code for dispatching events from components to event handlers.

    private class FormListener implements java.awt.event.ActionListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == jButton2) {
                CodePreview.this.onOK(evt);
            }
            else if (evt.getSource() == jButton1) {
                CodePreview.this.onCancel(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents

    private void onOK (ActionEvent evt) {//GEN-FIRST:event_onOK
	result.allowed = true;
	next ();
    }//GEN-LAST:event_onOK

    private void onCancel (ActionEvent evt) {//GEN-FIRST:event_onCancel
	result.allowed = false;
	result.source = files[fileIndex - 1].getName ();
	dispose ();
    }//GEN-LAST:event_onCancel
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea codeTA;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables



    /**
     * Simple Class for holding results
     * @author Jan Hybš
     * @version 1.0
     */
    public static class CodePreviewResult {

	private boolean allowed;
	private String source = "everything";



	/**
	 * Method returns whether or not was solution allowed
	 * @return whether or not was solution allowed
	 */
	public boolean isAllowed () {
	    return allowed;
	}



	/**
	 * Method returns which file was mark as dangerous (if available )
	 * @return which file was mark as dangerous or string 'everything' if window was manually closed
	 */
	public String getSource () {
	    return source;
	}
    }
}
