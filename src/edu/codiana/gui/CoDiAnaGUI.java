package edu.codiana.gui;

import edu.codiana.core.iface.IProgramEntry;
import edu.codiana.derivation.Derivator;
import edu.codiana.execution.Executor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class CoDiAnaGUI {

    //--------------------------------------
    // ICONS
    //--------------------------------------
    private final Icon playIcon = new ImageIcon (ClassLoader.getSystemResource ("resources/play.png"));
    private final Icon pauseIcon = new ImageIcon (ClassLoader.getSystemResource ("resources/pause.png"));
    //--------------------------------------
    // CORE
    //--------------------------------------
    private final ControlFrame control;
    private final GUIListener listener;
    private boolean paused;



    /**
     * Method sets default text to JTextArea and resets JProgressBar
     */
    public void clear () {
	control.infoTA.setText (
		"Zpracování úloh je pro zatím dokončeno. \nV dalším časovém intervalu bude provedena další kontrola.");
	control.progressPB.setValue (0);
    }



    private enum Type {

	START, OK_END, ERROR_END
    }



    /**
     * Creates instance with valid listener
     * @param listener 
     */
    public CoDiAnaGUI (GUIListener listener) {
	if (listener == null)
	    throw new IllegalArgumentException ("listener must not be null");


	this.listener = listener;
	this.control = new ControlFrame ();
	this.control.startPauseBtn.addActionListener (new PlayPauseBtnListener ());
	this.control.manualCheckCB.addActionListener (new ManualCBListener ());
	this.control.consoleCB.addActionListener (new ConsoleCBListener ());

	pause ();
    }



    /**
     * Method dispatch pause event and takes care of design issue
     */
    public void pause () {
	control.startPauseBtn.setIcon (playIcon);
	paused = false;
	control.infoTA.setText ("Zpracování úloh je pozastaveno...");
	control.infoLbl.setText ("");
    }



    /**
     * Method dispatch play event and takes care of design issue
     */
    public void play () {
	control.startPauseBtn.setIcon (pauseIcon);
	paused = true;
	control.infoTA.setText ("Zpracování úloh je aktivní!");
    }



    /**
     * Method disables start-pause control
     */
    public void disableControl () {
	control.startPauseBtn.setEnabled (false);
    }



    /**
     * Method enables start-pause control
     */
    public void enableControl () {
	control.startPauseBtn.setEnabled (true);
    }



    /**
     * Method sets progress to the JProgresBar
     * @param total current value
     * @param done maxValue
     */
    public void setProgress (int total, int done) {
	control.infoLbl.setText (String.format ("    %d z %d    ", done, total));
	control.progressPB.setMaximum (total);
	control.progressPB.setValue (done);
    }



    private void setCurrentPlag (IProgramEntry entry, Derivator derivator, Type type) {
	control.infoTA.setText (
		String.format (
		"Kontrolování plagiátů v úloze '%s' od učitele '%s'%s",
		entry.getTid (),
		entry.getUid (),
		type == Type.START ? "..." : " dokončeno"));
    }



    private void setCurrentExec (IProgramEntry entry, Executor codiana, Type type) {
	if (entry.isTeacher ()) {
	    control.infoTA.setText (
		    String.format (
		    "Zpracovávání úlohy '%s' od učitele '%s'%s",
		    entry.getTid (),
		    entry.getUid (),
		    type == Type.START ? "..." :
		    (type == Type.OK_END ?
		     String.format ("%n   dokončeno v pořádku (čas: %1.2f ms)",
				    codiana.getTimeRawValue ()) :
		     String.format ("%n   dokončeno v chybou (chyba: %s)",
				    codiana.exit.toString ()))));
	} else {
	    control.infoTA.setText (
		    String.format (
		    "Zpracovávání úlohy '%s' od %s '%s'%s",
		    entry.getTid (),
		    entry.isStudent () ? "studenta" : "skupiny",
		    entry.getUid (),
		    type == Type.START ? "..." :
		    (type == Type.OK_END ?
		     String.format ("%n   dokončeno v pořádku (čas: %1.2f ms, výstup: %1.2f  řádků)",
				    codiana.getTimeRawValue (), codiana.getOutputRawValue ()) :
		     String.format ("%n   dokončeno v chybou (chyba: %s)",
				    codiana.exit.toString ()))));
	}
    }



    /**
     * Method update JTextFields and takes care of design issue
     */
    public void setWorkStart (IProgramEntry entry) {
	if (entry.isPlagiarismCheck ()) setCurrentPlag (entry, null, Type.START);
	else setCurrentExec (entry, null, Type.START);
    }



    /**
     * Method update JTextFields and takes care of design issue
     * also adds item to control History
     */
    public void setWorkEnd (Executor codiana) {
	setCurrentExec (codiana.entry, codiana, codiana.isOK () ? Type.OK_END : Type.ERROR_END);
	control.addItem (Executor.generate (codiana));
    }



    /**
     * Method update JTextFields and takes care of design issue
     * also adds item to control History
     */
    public void setWorkEnd (Derivator derivator) {
	setCurrentPlag (derivator.entry, derivator, Type.OK_END);
	control.addItem (Derivator.generate (derivator));
    }


    //--------------------------------------------------------------------------
    //
    // PRIVATES
    //
    //--------------------------------------------------------------------------

    private final class PlayPauseBtnListener implements ActionListener {

	@Override
	public void actionPerformed (ActionEvent e) {
	    if (!paused)
		listener.pause ();
	    else
		listener.play ();
	}
    }



    private final class ManualCBListener implements ActionListener {

	@Override
	public void actionPerformed (ActionEvent e) {
	    listener.setManualCheck (control.manualCheckCB.isSelected ());
	}
    }



    private final class ConsoleCBListener implements ActionListener {

	@Override
	public void actionPerformed (ActionEvent e) {
	    if (control.consoleCB.isSelected ()) Console.showConsole ();
	    else Console.hideConsole ();
	}
    }
}
