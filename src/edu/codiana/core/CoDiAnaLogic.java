package edu.codiana.core;

import edu.codiana.core.configuration.ExitConstants;
import edu.codiana.core.iface.ProgressListener;
import edu.codiana.execution.Executor;
import edu.codiana.derivation.Derivator;
import edu.codiana.sql.MySQL;
import edu.codiana.sql.ProgramEntry;
import edu.codiana.sql.SQLProgramEntry;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class CoDiAnaLogic implements Runnable {

    private boolean paused = true;
    private final ProgressListener listener;
    private boolean manualControl = true;



    /**
     * Creates instance only with non-null listener
     * @param listener 
     */
    public CoDiAnaLogic (ProgressListener listener) {
	if (listener == null)
	    throw new IllegalArgumentException ("listener must not be null");

	this.listener = listener;
    }



    /**
     * pauses execution
     */
    public void pause () {
	paused = true;
    }



    /**
     * resume execution
     */
    public void play () {
	paused = false;
    }



    @Override
    public void run () {
	try {
	    update ();
	} catch (InterruptedException ex) {
	    listener.onInterrupted ();
	} catch (ClassNotFoundException ex) {
	    Logger.getLogger (CoDiAnaLogic.class.getName ()).log (Level.SEVERE, null, ex);
	} catch (SQLException ex) {
	    Logger.getLogger (CoDiAnaLogic.class.getName ()).log (Level.SEVERE, null, ex);
	} catch (FileNotFoundException ex) {
	    Logger.getLogger (CoDiAnaLogic.class.getName ()).log (Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger (CoDiAnaLogic.class.getName ()).log (Level.SEVERE, null, ex);
	} catch (Exception ex) {
	    ex.printStackTrace ();
	}
    }



    private synchronized void update () throws ClassNotFoundException, SQLException, FileNotFoundException, IOException, InterruptedException {
	if (paused)
	    return;
        
	listener.onUpdateStart ();

	final ResultSet result = MySQL.getQueue ();
	final List<SQLProgramEntry> items = SQLProgramEntry.parseResult (result);
	final int size = items.size ();

	//# reporting back
	listener.onQueueInfo (size);

	ProgramEntry entry;
	Executor codiana;
	Derivator derivator;

	for (int i = 0; i < size; i++) {

	    entry = items.get (i);

	    listener.onWorkStart (entry);
	    if (entry.isPlagiarismCheck ()) {

		//# find plagiarism among given task
		//# save results to MySQL
		//# remove entry from MySQL no matter what was the type
		derivator = new Derivator (entry);
		MySQL.savePlags (derivator);
		MySQL.closeTask (entry);
		MySQL.removeQueueItem (entry);

		listener.onPlagComplete (derivator);
	    } else {

		//# try to run code for the given task
		//# save results to MySQL
		//# remove entry from MySQL no matter what was the type
		codiana = new Executor (entry, manualControl);
		MySQL.saveResult (codiana);
		MySQL.removeQueueItem (entry);

		if (codiana.exit == ExitConstants.EXIT_OK) listener.onWorkSuccess (codiana);
		else listener.onWorkError (codiana);
	    }
	    listener.onQueueProgress (size, i + 1);

	    //# CPU REST
	    listener.onPauseStart ();
	    wait (2000);
	    listener.onPauseEnd ();
	}

	//# end
	listener.onUpdateEnd ();
    }



    /**
     * Method sets manul code control to on/off<br />
     * If true every file will be shown and need to be confirmed
     * @param value 
     */
    public void setManualControl (boolean value) {
	this.manualControl = value;
    }
}
