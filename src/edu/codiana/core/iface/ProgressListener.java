package edu.codiana.core.iface;

import edu.codiana.derivation.Derivator;
import edu.codiana.execution.Executor;
import edu.codiana.sql.ProgramEntry;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public interface ProgressListener {

    /**
     * Method is called when basic info is recieved
     * @param totalWorks int specifying how many tasks is in queue
     */
    void onQueueInfo (int totalWorks);



    /**
     * Method is called when some work is done
     * @param totalWorks int specifying how many tasks is in queue
     * @param currentWork int specifying how many tasks were done
     */
    void onQueueProgress (int totalWorks, int currentWork);



    /**
     * Method is called when some work starts
     * @param entry object containing all information
     */
    void onWorkStart (ProgramEntry entry);



    /**
     * Method is called when some work successfuly ends
     * @param entry object containing all information
     */
    void onWorkSuccess (Executor codiana);



    /**
     * Method is called when some work badly ends
     * @param entry object containing all information
     */
    void onWorkError (Executor codiana);



    /**
     * Method is called when plagiarism check is complete
     * @param derivator object containing all information
     */
    void onPlagComplete (Derivator derivator);



    /**
     * Method is called when update start
     */
    void onUpdateStart ();



    /**
     * Method is called when update ends
     */
    void onUpdateEnd ();



    /**
     * Method is called calculation is interrupted
     */
    void onInterrupted ();



    /**
     * Method is called when task is done and pause begins
     */
    void onPauseStart ();



    /**
     * Method is called when after-task-pause ends
     */
    void onPauseEnd ();
}
