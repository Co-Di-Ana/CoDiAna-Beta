package edu.codiana.core;

import edu.codiana.core.configuration.Config;
import edu.codiana.core.configuration.ConfigResult;
import edu.codiana.core.configuration.ConfigResult.ConfigResultType;
import edu.codiana.core.iface.ProgressListener;
import edu.codiana.derivation.Derivator;
import edu.codiana.execution.Executor;
import edu.codiana.gui.CoDiAnaGUI;
import edu.codiana.gui.GUIListener;
import edu.codiana.sql.ProgramEntry;
import edu.codiana.utils.serialization.History;
import edu.codiana.utils.Message;
import edu.codiana.utils.io.File;
import edu.codiana.utils.tests.OutputCheck;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author Hans
 */
public class Main {

    private Timer timer;
    private TimerObject timerObject;
    private Thread runner;
    private final CoDiAnaGUI codianaGUI;
    private final CoDiAnaLogic codianaLogic;
    private boolean paused = true;



    public static void main (String[] args) {
	new Main ();
//        
//        File teacher = new File ("./test/test.teacher.out");
//        File student = new File ("./test/test.student.out");
//        OutputCheck output = new OutputCheck ();
//	output.setIsTeacher (false);
//	output.setValueCheck (1);
//        
//        try {
//            output.calculateResult (teacher, student);
//            System.out.println (output.getResult ());
//        } catch (FileNotFoundException ex) {
//        } catch (IOException ex) {
//        }
    }



    public Main () {
	loadConfig ();
	History.init ();
	codianaLogic = new CoDiAnaLogic (new ProgressAdapter ());
	codianaGUI = new CoDiAnaGUI (new GUIAdapter ());
    }



    private void loadConfig () {
	ConfigResult configResult = Config.init ();
	if (configResult.type != ConfigResultType.OK) {
	    Message.showError ("Fatální chyba při načítání konfiguračních souborů: %n%s", configResult.toString ());
	    System.exit (1);
	}
    }



    private void updateCoDiAna (int delay) {
	if (timer != null) {
	    timer.purge ();
	    timer.cancel ();
	}
	if (timerObject != null) {
	    timerObject.cancel ();
	}
	timer = new Timer ("CoDiAna logic timer");
	timer.schedule (timerObject = new TimerObject (), delay);
    }



    private void playCoDiAna () {
	paused = false;
    }



    private void pauseCoDiAna () {
	if (timerObject != null) {
	    timerObject.cancel ();
	    timerObject = null;
	}
	paused = true;
    }



    private final class GUIAdapter implements GUIListener {

	@Override
	public void play () {
	    pauseCoDiAna ();
	    codianaGUI.pause ();
	    codianaLogic.pause ();
	}



	@Override
	public void pause () {
	    playCoDiAna ();
	    codianaGUI.play ();
	    codianaLogic.play ();
	    updateCoDiAna (0);
	}



	@Override
	public void setManualCheck (boolean value) {
	    codianaLogic.setManualControl (value);
	}
    }



    //--------------------------------------------------------------------------
    //
    // PROGRESS
    //
    //--------------------------------------------------------------------------
    private final class ProgressAdapter implements ProgressListener {

	@Override
	public void onQueueInfo (int totalWorks) {
	    codianaGUI.setProgress (totalWorks, 0);
	}



	@Override
	public void onQueueProgress (int totalWorks, int currentWork) {
	    codianaGUI.setProgress (totalWorks, currentWork);
	}



	@Override
	public void onWorkStart (ProgramEntry entry) {
	    codianaGUI.setWorkStart (entry);
	}



	@Override
	public void onWorkSuccess (Executor codiana) {
	    codianaGUI.setWorkEnd (codiana);
	    History.saveObject (Executor.generate (codiana));
	}



	@Override
	public void onWorkError (Executor codiana) {
	    codianaGUI.setWorkEnd (codiana);
	    History.saveObject (Executor.generate (codiana));
	}



	@Override
	public void onPlagComplete (Derivator derivator) {
	    codianaGUI.setWorkEnd (derivator);
	    History.saveObject (Derivator.generate (derivator));
	}



	@Override
	public void onUpdateStart () {
	    codianaGUI.disableControl ();
	}



	@Override
	public void onUpdateEnd () {
	    codianaGUI.enableControl ();
	    codianaGUI.clear ();
	    if (!paused)
		updateCoDiAna (5000);
	}



	@Override
	public void onPauseStart () {
	    codianaGUI.enableControl ();
	}



	@Override
	public void onPauseEnd () {
	    codianaGUI.disableControl ();
	}



	@Override
	public void onInterrupted () {
	    this.onUpdateEnd ();
	}
    }


    //--------------------------------------------------------------------------
    //
    // HELP TIMER-TASK CLASS
    //
    //--------------------------------------------------------------------------

    private final class TimerObject extends TimerTask {

	@Override
	public void run () {
	    runner = new Thread (codianaLogic);
	    runner.start ();
	}



	@Override
	public boolean cancel () {
	    if (runner != null)
		runner.interrupt ();
	    return super.cancel ();
	}
    }
}
