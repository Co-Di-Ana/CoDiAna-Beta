package edu.codiana.execution.security;

import edu.codiana.core.iface.ILanguageGuardian;
import edu.codiana.execution.security.structures.SecurityResult;
import edu.codiana.execution.security.structures.risks.ClassSecurityRisk;
import edu.codiana.execution.security.structures.risks.ISecurityRisk;
import edu.codiana.execution.security.structures.risks.PackageClassSecurityRisk;
import edu.codiana.execution.security.structures.risks.PackageSecurityRisk;
import edu.codiana.execution.security.structures.risks.RESecurityRisk;
import edu.codiana.execution.security.utils.CodeCleaner;
import edu.codiana.utils.io.FileReader;
import java.io.File;
import java.util.Scanner;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class JavaGuardian implements ILanguageGuardian {

    /** allowed */
    private static final String ALLOWED = "allowed";
    /** black list */
    private ISecurityRisk[] blackList = new ISecurityRisk[] {
	new RESecurityRisk ("^.+([a-zA-Z]*[0-9]*)+Writer.+$", "working with output (Writer)"),
	new ClassSecurityRisk ("Class", "use of class 'Class'"),
	new PackageClassSecurityRisk ("File", "java.io", "working with files"),
	new ClassSecurityRisk ("Runtime", "working with 'Runtime'"),
	new PackageSecurityRisk ("java.net", "working with internet")
    };
    /** white list */
    private ISecurityRisk[] whiteList = new ISecurityRisk[] {
	//# so far no exceptions
    };



    @Override
    public SecurityResult check (File file) {
	StringBuilder code = CodeCleaner.format (FileReader.readFile (file)).getData ();
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
