package edu.codiana.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class SQLProgramEntry extends ProgramEntry {

    /**
     * Method parses result set into list of SQLProgramEntry items
     * @param resultSet
     * @return
     * @throws SQLException 
     */
    public static List<SQLProgramEntry> parseResult (ResultSet resultSet) throws SQLException {
	List<SQLProgramEntry> result = new ArrayList<SQLProgramEntry> ();
	boolean hasNext = true;
	if (!resultSet.next ())
	    return result;
	while (hasNext) {
	    result.add (new SQLProgramEntry (resultSet));
	    hasNext = resultSet.next ();
	}

	return result;
    }



    /**
     * Create instance from given result set using {@code resultSet.getString (...)}
     * @param resultSet
     * @throws SQLException 
     */
    public SQLProgramEntry (ResultSet resultSet) throws SQLException {
	if (resultSet == null)
	    return;
	String tmp;
	mid = resultSet.getString ("mid");
	tid = resultSet.getString ("tid");
	uid = resultSet.getString ("uid");

	timeCheck = resultSet.getString ("timeCheck");
	outputCheck = resultSet.getLong ("outputCheck");

	outputWeight = resultSet.getLong ("outputWeight");
	timeWeight = resultSet.getLong ("timeWeight");

	minTime = resultSet.getLong ("timeMinimum");
	maxTime = resultSet.getLong ("timeMaximum");

	tmp = resultSet.getString ("subject");
	subject = tmp.equals (SubjectType.STUDENT.toString ()) ? SubjectType.STUDENT :
		  (tmp.equals (SubjectType.GROUP.toString ()) ? SubjectType.GROUP : SubjectType.TEACHER);
	tmp = resultSet.getString ("action");
	time = tmp.indexOf ("time") != -1;
	output = tmp.indexOf ("output") != -1;
	plags = tmp.indexOf ("plags") != -1;
	language = resultSet.getString ("language");
	fixLanguage ();
    }



    private void fixLanguage () {
	if (language == null || language.length () == 0) language = "java";

	if (language.indexOf ("zip-") == 0) {
	    zipped = true;
	    language = language.substring (4);
	}
	language = "." + language;
    }
}
