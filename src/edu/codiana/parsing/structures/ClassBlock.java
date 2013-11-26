package edu.codiana.parsing.structures;

import japa.parser.ast.body.*;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ClassBlock extends CodeBlock {

    private ClassOrInterfaceDeclaration cls;



    /**
     * Creates instance using given class declaration
     * @param cls 
     */
    public ClassBlock (ClassOrInterfaceDeclaration cls) {
	super ();
	this.cls = cls;

	for (BodyDeclaration dec : this.cls.getMembers ())
	    visitBody (dec);

    }



    /**
     * method returns reference of constructor class declaration
     * @return 
     */
    public ClassOrInterfaceDeclaration getClassObject () {
	return cls;
    }



    @Override
    public String toString () {
	return String.format ("class %s", cls.getName ());
    }



    /**
     * Method returns name of this class declaration
     * @return class declaration name
     */
    public String getName () {
	return cls.getName ();
    }



    @Override
    public String getCode () {
	return cls.toString ();
    }
}
