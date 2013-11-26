package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ClassSecurityRisk extends SecurityRisk {

    private String className;
    private String matchRegExp;



    /**
     * Creates instance using classname and description
     * @param className class name such as "Runtime"
     * @param ruleDescription 
     */
    public ClassSecurityRisk (String className, String ruleDescription) {
	super (ruleDescription);
	this.className = className;
	this.matchRegExp = "(.*)([^a-zA-Z]*" + className + "[^a-zA-Z]*)(.*)";
    }



    @Override
    public boolean checkLine (String line) {
	return (line.matches (matchRegExp));
    }



    @Override
    public String toString () {
	return String.format ("[ClassSecurityRisk class='%s' desc='%s']", className, ruleDescription);
    }
}
