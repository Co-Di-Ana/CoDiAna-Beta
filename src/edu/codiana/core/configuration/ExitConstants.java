package edu.codiana.core.configuration;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public enum ExitConstants {

    EXIT_OK ("exit-ok"),
    COMPILATION_ERROR ("compilation-error"),
    TIME_OUT_ERROR ("time-out-error"),
    WAITING ("waiting"),
    RUN_ERROR ("run-error"),
    MAIN_FILE_MISSING ("main-file-missing"),
    WRONG_OUTPUT ("wrong-output"),
    UNSUPPORTED_LANGUAGE ("unsupported-language"),
    NO_LANGUAGE_SECURITY_CHECK ("no-language-security-check"),
    SECURITY_RISK ("security-risk"),
    NO_RESULT ("no-result"),
    UNKNOW_EXIT ("unknown-error");
    private String value;



    private ExitConstants (String value) {
	this.value = value;
    }



    @Override
    public String toString () {
	return value;
    }
}