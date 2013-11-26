package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class PackageSecurityRisk extends SecurityRisk {

    private String packagePath;
    private final String lineImport;
    private final String lineUse;



    /**
     * Create instace using package path (such as "java.lang") and rule description
     * @param packagePath name of the package (such as "java.lang")
     * @param ruleDescription description
     */
    public PackageSecurityRisk (String packagePath, String ruleDescription) {
	super (ruleDescription);
	this.packagePath = packagePath;
	this.lineImport = "import " + packagePath + ".";
	this.lineUse = packagePath;
    }



    @Override
    public boolean checkLine (String line) {
	if (line.indexOf (lineImport) != -1)
	    return true;

	if (line.indexOf (lineUse) != -1)
	    return true;
	return false;
    }
    
    
    @Override
    public String toString () {
	return String.format ("[ClassSecurityRisk package='%s' desc='%s']", packagePath, ruleDescription);
    }
}
