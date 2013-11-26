package edu.codiana.core.iface;

import edu.codiana.sql.SubjectType;



/**
 * Iterface which offers method to obtain informations about task entry which is being tested
 * @author Hans
 */
public interface IProgramEntry {

    /**
     * method returns current value of mid
     * @return value of mid
     */
    public String getMid ();



    /**
     * method returns current value of tid
     * @return value of tid
     */
    public String getTid ();



    /**
     * method returns current value of uid
     * @return value of uid
     */
    public String getUid ();



    /**
     * method returns current value of outputCheck
     * @return value of outputCheck
     */
    public long getOutputCheck ();



    /**
     * method returns current value of timeCheck
     * @return value of timeCheck
     */
    public String getTimeCheck ();



    /**
     * method returns current value of memoryCheck
     * @return value of memoryCheck
     */
    public long getMemoryCheck ();



    /**
     * method returns current value of timeWeight
     * @return value of timeWeight
     */
    public long getTimeWeight ();



    /**
     * method returns current value of outputWeight
     * @return value of outputWeight
     */
    public long getOutputWeight ();



    /**
     * method returns current value of memoryWeight
     * @return value of memoryWeight
     */
    public long getMemoryWeight ();



    /**
     * method returns current value of minTime
     * @return value of minTime
     */
    public long getMinTime ();



    /**
     * method returns current value of maxTime
     * @return value of maxTime
     */
    public long getMaxTime ();



    /**
     * @return whether is subject student, group or teacher
     */
    public SubjectType getSubject ();



    /**
     * @return whether this entry should generate output
     */
    public boolean generateOutput ();



    /**
     * @return whether this entry should check tast for plagiarism
     */
    public boolean isPlagiarismCheck ();



    /**
     * return whether this entry should detect time
     */
    public boolean detectTime ();



    /**
     * @return path to the main file according to the 
     * set programming language
     */
    public String getMainFilePath ();



    /**
     * Method returns extension of used programming language
     * @return extension, such as 'java' or 'py'
     */
    public String getLanguage ();



    /**
     * @return whether is subject teacher
     */
    public boolean isTeacher ();



    /**
     * @return whether is subject teacher
     */
    public boolean isStudent ();



    /**
     * @return whether is subject teacher
     */
    public boolean isGroup ();
    

    /**
     * @return whether is solution made from zip file (may contain more files)
     */
    public boolean isZipped ();
}
