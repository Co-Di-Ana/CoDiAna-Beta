package edu.codiana.parsing.structures;

import edu.codiana.utils.Strings;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.MethodDeclaration;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class MethodBlock extends CodeBlock {

    private BodyDeclaration mtd;
    public final int numLines;
    public final String code;
    public final String name;
    private String[] lines = null;



    /**
     * Creates instace from given method declaration
     * @param mtd 
     */
    public MethodBlock (MethodDeclaration mtd) {
	super ();
	this.mtd = mtd;
	this.code = mtd.toString ();
	this.name = mtd.getName ();
	this.numLines = Strings.numLines (code);
	visitBlock (mtd.getBody ());
    }



    /**
     * Creates instace from given constructor declaration
     * @param mtd 
     */
    public MethodBlock (ConstructorDeclaration con) {
	this.mtd = con;
	this.code = con.toString ();
	this.name = con.getName ();
	this.numLines = Strings.numLines (code);
    }



    /**
     * Method returns this code as array of lines
     * @return String array of code line
     * @see Strings#toArray(java.lang.String) 
     */
    public String[] getLines () {
	if (lines == null)
	    lines = Strings.toArray (code);
	return lines;
    }



    @Override
    public String toString () {
	return String.format ("method %s", name);
    }
}
