package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class PackageClassSecurityRisk extends SecurityRisk {

    private boolean isImported;
    private String className;
    private String packagePath;
    private final String smallImport, bigImport;
    private final String smallUseRegExp, bigUse;



    /**
     * Create instance using classname, package path, and risk description
     * @param className name of the class (such as "Runtime")
     * @param packagePath name of the package (such as "java.lang")
     * @param ruleDescription description
     */
    public PackageClassSecurityRisk (String className, String packagePath, String ruleDescription) {
	super (ruleDescription);
	this.className = className;
	this.packagePath = packagePath;

	this.bigUse = packagePath + "." + className;
	this.smallUseRegExp = "(.*)([^a-zA-Z]*" + className + "[^a-zA-Z]*)(.*)";
	this.bigImport = "import " + packagePath + ".*";
	this.smallImport = "import " + packagePath + "." + className;
    }



    @Override
    public boolean checkLine (String line) {
	if ((isImported && line.matches (smallUseRegExp)) || (line.indexOf (smallImport) == -1 && line.indexOf (bigUse) != -1))
	    return true;

	if (!isImported)
	    if (line.indexOf (bigImport) != -1 || line.indexOf (smallImport) != -1)
		isImported = true;

	return false;
    }



    @Override
    public String toString () {
	return String.format ("[PackageClassSecurityRisk package='%s' class='%s' desc='%s']",
			      packagePath, className, ruleDescription);
    }
}
