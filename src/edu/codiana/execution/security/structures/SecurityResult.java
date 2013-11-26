package edu.codiana.execution.security.structures;

import edu.codiana.execution.security.structures.risks.ISecurityRisk;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class SecurityResult {

    private ISecurityRisk rule;
    private String source;



    /**
     * This constructor should not be called manually
     * @param rule
     * @param source 
     */
    public SecurityResult (ISecurityRisk rule, String source) {
	this.rule = rule;
	this.source = source;
    }



    /**
     * Method returns occurrence of the security risk
     * @return occurrence of the security risk if exists
     */
    public String getSource () {
	return source;
    }



    /**
     * Method returns rule which was used to match this risk
     * @return regExp string
     */
    public ISecurityRisk getRule () {
	return rule;
    }



    @Override
    public String toString () {
	return String.format ("[SecurityResult rule='%s' source='%s']", rule, source);
    }
}
