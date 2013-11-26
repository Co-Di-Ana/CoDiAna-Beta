package edu.codiana.sql;

import edu.codiana.core.iface.IProgramEntry;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ProgramEntry implements IProgramEntry {

    protected String mid, tid, uid, timeCheck;
    protected long maxTime, minTime, timeWeight;
    protected long outputCheck, outputWeight;
    protected long memoryCheck, memoryWeight;
    protected boolean time, output, plags, zipped;
    protected String language;
    protected SubjectType subject;



    @Override
    public String toString () {
	return String.format (
		"[[%s] [%s@%s] [%04d < t < %04d] [%d] [%s]]",
		uid, mid, tid, minTime, maxTime, outputCheck, timeCheck);
    }



    @Override
    public String getMainFilePath () {
	return String.format ("%s%s", tid, language == null || language.isEmpty () ? ".java" : language);
    }



    @Override
    public boolean generateOutput () {
	return output;
    }



    @Override
    public boolean detectTime () {
	return time;
    }



    @Override
    public boolean isPlagiarismCheck () {
	return plags;
    }



    //---------------------------------------------------//
    //--------------getters and setters------------------//
    //---------------------------------------------------//
    @Override
    public long getMaxTime () {
	return maxTime;
    }



    @Override
    public long getMemoryCheck () {
	return memoryCheck;
    }



    @Override
    public long getMemoryWeight () {
	return memoryWeight;
    }



    @Override
    public String getMid () {
	return mid;
    }



    @Override
    public long getMinTime () {
	return minTime;
    }



    @Override
    public long getOutputCheck () {
	return outputCheck;
    }



    @Override
    public long getOutputWeight () {
	return outputWeight;
    }



    @Override
    public String getTid () {
	return tid;
    }



    @Override
    public String getTimeCheck () {
	return timeCheck;
    }



    @Override
    public long getTimeWeight () {
	return timeWeight;
    }



    @Override
    public String getUid () {
	return uid;
    }



    @Override
    public String getLanguage () {
	return language;
    }



    @Override
    public SubjectType getSubject () {
	return subject;
    }



    @Override
    public boolean isTeacher () {
	return subject == SubjectType.TEACHER;
    }



    @Override
    public boolean isStudent () {
	return subject == SubjectType.STUDENT;
    }



    @Override
    public boolean isGroup () {
	return subject == SubjectType.GROUP;
    }



    @Override
    public boolean isZipped () {
	return zipped;
    }
}
