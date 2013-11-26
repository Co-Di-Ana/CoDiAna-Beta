package edu.codiana.derivation;

import edu.codiana.core.configuration.ExitConstants;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Hybš
 * @version 1.0
 */
public class DerivationEntry {

    //--------------------------------------
    // PUBLIC FINALS
    //--------------------------------------
    public final String uid, tid, mid;
    public final String status, language, exit;



    /**
     * Create simple final-based instance
     * @param uid user id
     * @param tid teacher id
     * @param mid mentor id
     * @param status "in" or "out"
     * @param language ".language" extension
     * @param exit such as "exit-ok"
     */
    public DerivationEntry(String uid, String tid, String mid, String status, String language, String exit) {
	this.uid = uid;
	this.tid = tid;
	this.mid = mid;
	this.status = status;
	this.language = language;
	this.exit = exit;
    }


    

    /**
     * Create instance from result set
     * @param result set from select from databse
     * @throws SQLException 
     */
    public DerivationEntry(ResultSet result) throws SQLException {

	//# base info
	uid = result.getString("uid");
	tid = result.getString("tid");
	mid = result.getString("mid");

	//# needed info
	status = result.getString("status");
	language = result.getString("language");
	exit = result.getString("exitValue");
    }



    /**
     * Method tells whether can be this result checked for plagiarism
     * @return true if entry program has exited normally (0), user is still logged in
     */
    public final boolean isValidProgram() {
	return status.equals ("in") && exit.equals (ExitConstants.EXIT_OK.toString ());
    }



    @Override
    public String toString() {
	return String.format(
		"[[%-9s] [%s@%s] [s:%d] [l:%d] [e:%d]]",
		uid, mid, tid, status, language, exit);
    }



}
