package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public abstract class SecurityRisk implements ISecurityRisk {

    protected String ruleDescription;



    public SecurityRisk (String ruleDescription) {
	this.ruleDescription = ruleDescription;
    }



    @Override
    public String getRuleDescription () {
	return ruleDescription;
    }
}
