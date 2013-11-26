package edu.codiana.parsing.structures;

import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class InterfaceBlock extends CodeBlock {

    private ClassOrInterfaceDeclaration ifs;



    /**
     * Creates instance using given interface declaration
     * @param ifs 
     */
    public InterfaceBlock (ClassOrInterfaceDeclaration ifs) {
	super ();
	this.ifs = ifs;

	for (BodyDeclaration dec : this.ifs.getMembers ())
	    visitBody (dec);

    }


    @Override
    public String toString () {
	return String.format ("interface %s", ifs.getName ());
    }



    /**
     * Method returns name of this iface
     * @return iface name
     */
    public String getName () {
	return ifs.getName ();
    }
}
