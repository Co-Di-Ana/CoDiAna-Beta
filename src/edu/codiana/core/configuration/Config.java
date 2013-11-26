package edu.codiana.core.configuration;

import edu.codiana.utils.io.File;
import java.io.FileReader;
import java.util.Properties;
import edu.codiana.core.configuration.ConfigResult.ConfigResultType;
import java.io.IOException;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Config {

    private static String root, processRunner, username, password, database, server, port;
    private static File mailFile;
    private static final Boolean DEBUG = false;
    private static final String CONFIG_PATH = DEBUG ? "./configDebug.ini" : "./config.ini";



    /**
     * Initation of config File
     * @return result containing info about config loading
     */
    public static ConfigResult init () {
	try {
	    File config = new File (CONFIG_PATH);
	    Properties prop = new Properties ();
	    prop.load (new FileReader (config));

	    if ((root = prop.getProperty ("root")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((processRunner = prop.getProperty ("process-runner")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((username = prop.getProperty ("username")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((password = prop.getProperty ("password")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((database = prop.getProperty ("database")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((port = prop.getProperty ("port")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    if ((server = prop.getProperty ("server")) == null)
		return new ConfigResult (ConfigResultType.MISSING_PROPERTY);

	    mailFile = new File (root);


	} catch (IOException ex) {
	    return new ConfigResult (ConfigResultType.MISSING_CONFIG_FILE, ex);
	} catch (IllegalArgumentException ex) {
	    return new ConfigResult (ConfigResultType.CORRUPTED_FILE_CONTENT, ex);
	} catch (Exception ex) {
	    return new ConfigResult (ConfigResultType.UNKNOWN_ERROR, ex);
	}

	return new ConfigResult (ConfigResultType.OK);
    }



    private Config () {
    }



    //--------------------------------------------------------------------------
    //
    // Getters setters
    //
    //--------------------------------------------------------------------------
    public static File getMailFile () {
	return mailFile;
    }



    /**
     * Method returns config root
     * @return config root
     */
    public static String getRoot () {
	return root;
    }



    /**
     * Method returns config process runner location
     * @return config process runner location (such as "../folder/ProcessRunner.exe")
     */
    public static String getProcessRunner () {
	return processRunner;
    }



    /**
     * Method returns config database name
     * @return config database name
     */
    public static String getDatabase () {
	return database;
    }



    /**
     * Method returns config database password
     * @return config database password
     */
    public static String getPassword () {
	return password;
    }



    /**
     * Method returns config database username
     * @return config databse username
     */
    public static String getUsername () {
	return username;
    }



    /**
     * Method returns config database port
     * @return config database port
     */
    public static String getPort () {
	return port;
    }



    /**
     * Method returns config database server name
     * @return config database server name
     */
    public static String getServer () {
	return server;
    }
}