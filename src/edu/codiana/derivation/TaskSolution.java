package edu.codiana.derivation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class TaskSolution {

    /** list of all files */
    public final List<TaskSolutionFile> files;
    /** input entry reference */
    public final DerivationEntry entry;



    /**
     * Creates instance using entry and one file
     * @param entry
     * @param file 
     */
    public TaskSolution (DerivationEntry entry, TaskSolutionFile file) {
	this (entry, new ArrayList<TaskSolutionFile> ());
	addFile (file);
    }



    /**
     * Create instance useing entry, files must be added later
     * @param entry 
     * @see #addFile(java.io.File) 
     * @see #addFile(edu.codiana.derivation.TaskSolutionFile) 
     */
    public TaskSolution (DerivationEntry entry) {
	this (entry, new ArrayList<TaskSolutionFile> ());
    }



    /**
     * Creates instance using entry and whole list of files
     * @param entry
     * @param files 
     */
    public TaskSolution (DerivationEntry entry, List<TaskSolutionFile> files) {
	this.files = files == null ? new ArrayList<TaskSolutionFile> () : files;
	this.entry = entry;
    }



    /**
     * Method add new file into solution
     * @param file 
     */
    public void addFile (File file) {
	addFile (new TaskSolutionFile (file));
    }



    /**
     * Method add new file into solution
     * @param file 
     */
    public void addFile (TaskSolutionFile file) {
	files.add (file);
    }



    /**
     * num files
     * @return 
     */
    public int numFiles () {
	return files.size ();
    }



    @Override
    public String toString () {
	return String.format ("[entry='%s', files=%d]", entry, files.size ());
    }
}
