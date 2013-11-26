package edu.codiana.parsing.structures;

import japa.parser.ast.body.EnumDeclaration;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class EnumBlock extends CodeBlock {

    private EnumDeclaration enm;



    /**
     * Creates instance using enum declaration
     * @param enm 
     */
    public EnumBlock (EnumDeclaration enm) {
	this.enm = enm;
    }



    /**
     * Method returns reference to the constructor declaration block
     * @return origin block
     */
    public EnumDeclaration getEnumBlock () {
	return enm;
    }



    @Override
    public String toString () {
	return String.format ("enumeration %s", enm.getName ());
    }



    public String getName () {
	return enm.getName ();
    }
}
