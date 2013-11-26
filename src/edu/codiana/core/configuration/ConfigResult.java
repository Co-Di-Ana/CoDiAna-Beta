package edu.codiana.core.configuration;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ConfigResult {

    public static enum ConfigResultType {

	OK, MISSING_CONFIG_FILE, MISSING_PROPERTY, CORRUPTED_FILE_CONTENT, UNKNOWN_ERROR
    }
    public final ConfigResultType type;
    public final Exception error;



    /**
     * Creates instance with given result and error if necesseary
     * @param result of ConfigResult
     * @param error if needed
     */
    public ConfigResult (ConfigResultType result, Exception error) {
	this.type = result;
	this.error = error;
    }



    /**
     * Creates instance with given result and no error
     * @param result of ConfigResult
     */
    public ConfigResult (ConfigResultType result) {
	this (result, null);
    }



    @Override
    public String toString () {
	return error == null ? String.format ("[Configiration: %s]", type.toString ()) :
	       String.format ("[Configiration: %s, error: %s]", type.toString (), error.getMessage ());
    }
}