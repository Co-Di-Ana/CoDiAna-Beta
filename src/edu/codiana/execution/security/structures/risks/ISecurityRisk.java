package edu.codiana.execution.security.structures.risks;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public interface ISecurityRisk {

    /**
     * Method check current line and tells whether is this line forbided 
     * @param line linr to be checked
     * @return true if is found, else otherwise
     */
    public boolean checkLine (String line);



    /**
     * Method returns closer info about this rule
     * @return closer info about this rule
     */
    public String getRuleDescription ();
}
