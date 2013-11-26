package edu.codiana.sql;

import edu.codiana.core.configuration.Config;
import edu.codiana.execution.Executor;
import edu.codiana.core.iface.IProgramEntry;
import edu.codiana.derivation.DerivationPair;
import edu.codiana.derivation.Derivator;
import edu.codiana.utils.io.FileReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Properties;



/**
 * @author Hans
 */
public class MySQL {

    private static final String DRIVER = "org.gjt.mm.mysql.Driver";
    private static final String URL = String.format ("jdbc:mysql://%s:%s/mysql", Config.getServer (), Config.getPort ());
    private static final String DATABASE = Config.getDatabase ();
    private static final String USERNAME = Config.getUsername ();
    private static final String PASSWORD = Config.getPassword ();
    private static final Charset utf8 = Charset.forName ("utf-8");
    private static boolean connected;
    private static Connection connection;
    private static Statement statement;



    /**
     * Method connects to database
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public static void connect () throws ClassNotFoundException, SQLException {
	if (connected)
	    return;

	Class.forName (DRIVER);
	Properties info = new Properties ();
	info.put ("user", USERNAME);
	info.put ("password", PASSWORD);
	connection = DriverManager.getConnection (URL, info);
	statement = connection.createStatement ();
	statement.executeQuery (String.format ("USE `%s`", DATABASE));
	statement.executeQuery ("SET NAMES UTF8");
	//statement.executeQuery("SET NAMES utf8 COLLATE utf8_czech_ci");
    }



    public static void close () throws SQLException {
	connection.close ();
	connected = false;
    }



    public static ResultSet getQueue () throws ClassNotFoundException, SQLException {
	connect ();
	String s =
	       "SELECT `Q`.*, `T`.* FROM `queue` AS `Q` LEFT JOIN `task` AS `T` ON " +
	       "(`T`.`uid` = `Q`.`mid` AND `T`.`tid` = `Q`.`tid`) ORDER BY `Q`.`timestamp`";
	return statement.executeQuery (s);
    }



    public static boolean removeQueueItem (IProgramEntry item) throws ClassNotFoundException, SQLException {
	connect ();
	int i = 1;
	String s =
	       "DELETE FROM `queue` WHERE `tid` = ? AND `mid` = ? AND `uid` = ?";
	CallableStatement c = connection.prepareCall (s);
	c.setBytes (i++, item.getTid ().getBytes (utf8));
	c.setBytes (i++, item.getMid ().getBytes (utf8));
	c.setBytes (i++, item.getUid ().getBytes (utf8));
	return c.execute ();
    }



    public static void saveResult (Executor codiana) throws ClassNotFoundException, SQLException {
	String s;
	CallableStatement c;
	int i = 1;
	if (codiana.entry.isTeacher ()) {
	    if (codiana.entry.detectTime ()) {
		connect ();
		s = "UPDATE `task` SET `timeMaximum` = ? WHERE `uid` = ? AND `tid` = ?";
		c = connection.prepareCall (s);
		long l = (long) codiana.getTimeRawValue ();
		c.setLong (i++, l);
		c.setString (i++, codiana.entry.getUid ());
		c.setString (i++, codiana.entry.getTid ());
		c.execute ();
	    }
	} else {
	    connect ();
	    s = "UPDATE `intask` SET `exitValue` = ?,`note` = ?, " +
		"`outputValue` = ?,  `timeValue` = ?, " +
		"`outputResult` = ?, `timeResult` = ?, `finalResult` = ? " +
		"WHERE `tid` = ? AND `mid` = ? AND `uid` = ?";
	    c = connection.prepareCall (s);
	    c.setString (i++, codiana.exit.toString ());
	    
	    //# additional note if necceseary
	    c.setAsciiStream (i++, FileReader.getStringStream (codiana.getNote ()));
	    
	    //# calculated raw values
	    c.setLong (i++, (long) codiana.getOutputRawValue ());
	    c.setLong (i++, (long) codiana.getTimeRawValue ());

	    //# values according to checks
	    c.setFloat (i++, codiana.getOutputResult ());
	    c.setFloat (i++, codiana.getTimeResult ());

	    //# final weighted value
	    c.setFloat (i++, codiana.getFinalResult ());

	    //# id's
	    c.setBytes (i++, codiana.entry.getTid ().getBytes (utf8));
	    c.setBytes (i++, codiana.entry.getMid ().getBytes (utf8));
	    c.setBytes (i++, codiana.entry.getUid ().getBytes (utf8));
	    c.execute ();
	}
    }



    public static ResultSet getSolvers (IProgramEntry entry) throws SQLException {
	int i = 1;
	String s = "SELECT * FROM `intask` WHERE `tid` = ? AND `mid` = ? ";
	CallableStatement c = connection.prepareCall (s);
	c.setBytes (i++, entry.getTid ().getBytes (utf8));
	c.setBytes (i++, entry.getMid ().getBytes (utf8));
	return c.executeQuery ();
    }



    public static boolean savePlags (Derivator item) throws SQLException, ClassNotFoundException {
	if (item.derivatives.isEmpty ())
	    return false;

	connect ();
	int i = 1;
	CallableStatement c = connection.prepareCall ("DELETE FROM `plags` WHERE `tid` = ? AND `mid` = ?");
	c.setBytes (i++, item.entry.getTid ().getBytes (utf8));
	c.setBytes (i++, item.entry.getMid ().getBytes (utf8));
	c.execute ();


	i = 1;
	StringBuilder sb = new StringBuilder (
		"INSERT INTO `plags` (`tid`, `mid`, `uid0`, `uid1`, `level`, `iden`, `ljw`, `hist`, `struc`) VALUES ");
	for (int j = 0; j < item.derivatives.size (); j++)
	    sb.append ("(?, ?, ?, ?, ?, ?, ?, ?, ?),");
	sb.setCharAt (sb.length () - 1, ';');
	c = connection.prepareCall (sb.toString ());

	for (int j = 0; j < item.derivatives.size (); j++) {
	    DerivationPair dp = item.derivatives.get (j);

	    //# private key
	    c.setBytes (i++, item.entry.getTid ().getBytes (utf8));
	    c.setBytes (i++, item.entry.getMid ().getBytes (utf8));
	    c.setBytes (i++, dp.entryA.entry.uid.getBytes (utf8));
	    c.setBytes (i++, dp.entryB.entry.uid.getBytes (utf8));

	    //# result
	    c.setInt (i++, dp.level.ordinal ());


	    //# calculated values
	    c.setFloat (i++, dp.result.identicalCompareResult);
	    c.setFloat (i++, dp.result.LJWCompareResult);
	    c.setFloat (i++, dp.result.histogramCompareResult);
	    c.setFloat (i++, dp.result.structureCompareResult);
	}

	return c.execute ();
    }



    public static boolean closeTask (IProgramEntry entry) throws SQLException, ClassNotFoundException {
	connect ();
	int i = 1;
	String s =
	       "UPDATE `task` SET `status` = 'isDead'  WHERE `tid` = ? AND `uid` = ?";
	CallableStatement c = connection.prepareCall (s);
	c.setBytes (i++, entry.getTid ().getBytes (utf8));
	c.setBytes (i++, entry.getUid ().getBytes (utf8));
	return c.execute ();
    }
}
