package edu.codiana.execution.security;

import edu.codiana.core.iface.ILanguageGuardian;
import edu.codiana.execution.security.structures.SecurityResult;
import edu.codiana.execution.security.structures.risks.ISecurityRisk;
import edu.codiana.execution.security.structures.risks.RESecurityRisk;
import edu.codiana.utils.io.FileReader;
import java.io.File;
import java.util.Scanner;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class PythonGuardian implements ILanguageGuardian {

    /** dangerous */
    private static final String SUSPICIOUS = "suspicious";
    /** allowed */
    private static final String ALLOWED = "allowed";
    /** black list */
    private ISecurityRisk[] blackList = new ISecurityRisk[] {
	new RESecurityRisk ("^.*delete.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*process.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*remove.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*exec.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*run.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*process.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*file.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*open.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*write.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*subprocess.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*call.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*import.*$", SUSPICIOUS),
	new RESecurityRisk ("^.*from.*$", SUSPICIOUS)
    };
    /** white list */
    private ISecurityRisk[] whiteList = new ISecurityRisk[] {
	new RESecurityRisk ("^.*import random.*$", ALLOWED),
	new RESecurityRisk ("^.*import sys.*$", ALLOWED),
	new RESecurityRisk ("^.*sys\\.stdout\\.write.*$", ALLOWED)
    };



    @Override
    public SecurityResult check (File file) {
	StringBuilder code = FileReader.readFile (file);
	Scanner scanner = new Scanner (code.toString ());
	String line;
	while (scanner.hasNextLine ()) {
	    line = scanner.nextLine ();
	    boolean isWhite = false;

	    for (int i = 0; i < whiteList.length; i++) {
		ISecurityRisk white = whiteList[i];
		if (white.checkLine (line)) {
		    isWhite = true;
		    break;
		}
	    }
	    if (!isWhite) {
		for (int i = 0; i < blackList.length; i++) {
		    ISecurityRisk black = blackList[i];

		    if (black.checkLine (line)) {
			return new SecurityResult (black, line);
		    }
		}
	    }
	}

	return null;
    }
}
