package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class RESecurityRisk extends SecurityRisk {

    private String regExp;



    /**
     * Creates instance from given regExp and description
     * @param regExp
     * @param ruleDescription 
     */
    public RESecurityRisk (String regExp, String ruleDescription) {
	super (ruleDescription);
	this.regExp = regExp;
    }



    @Override
    public boolean checkLine (String line) {
	return line.matches (regExp);
    }



    @Override
    public String toString () {
	return String.format ("[PackageClassSecurityRisk regExp='%s' desc='%s']",
			      regExp, ruleDescription);
    }
}
