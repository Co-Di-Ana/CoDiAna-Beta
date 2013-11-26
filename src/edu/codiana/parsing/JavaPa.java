package edu.codiana.parsing;

import edu.codiana.derivation.TaskSolution;
import edu.codiana.derivation.TaskSolutionFile;
import edu.codiana.parsing.structures.ClassBlock;
import edu.codiana.parsing.structures.CodeBlock;
import edu.codiana.parsing.structures.EnumBlock;
import edu.codiana.parsing.structures.InterfaceBlock;
import edu.codiana.parsing.structures.MethodBlock;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import java.io.IOException;
import java.util.ArrayList;



/**
 * @author Hans
 */
public class JavaPa extends CodeBlock {

    TaskSolution solution;
    private CompilationUnit[] cus;
    private int cusPosition = 0;



    public JavaPa (TaskSolution solution) throws ParseException, IOException {
	super ();
	this.cus = new CompilationUnit[solution.files.size ()];
	this.solution = solution;

	for (TaskSolutionFile file : solution.files)
	    parse (file);
    }



    public ClassBlock[] getAllClasses () {
	ArrayList<ClassBlock> result = new ArrayList<ClassBlock> ();
	getAllClasses (this, result);
	return result.toArray (new ClassBlock[result.size ()]);
    }



    private ArrayList<ClassBlock> getAllClasses (CodeBlock block, ArrayList<ClassBlock> result) {
	for (ClassBlock cls : block.classes) {
	    result.add (cls);
	    getAllClasses (cls, result);
	}

	for (InterfaceBlock ifs : block.interfaces)
	    getAllClasses (ifs, result);

	return result;
    }



    /**
     * prints flow structure
     */
    public String getEntireStructure () {
	StringBuilder sb = new StringBuilder ();
	info (this, sb, 3);
	return sb.toString ();
    }



    protected void info (CodeBlock c, StringBuilder sb, int indent) {
	sb.append (String.format ("%" + (indent - 2) + "s%s%n", " ", c.toString ()));


	if (c.constructor != null)
	    sb.append (String.format ("%" + indent + "sconstructor %s%n", " ", c.constructor.name));



	if (c.methods != null && c.methods.size () > 0)
	    for (MethodBlock mtd : c.methods)
		sb.append (String.format ("%" + indent + "smethod %s%n", " ", mtd.name));


	if (c.classes != null && c.classes.size () > 0)
	    //System.out.format("%" + indent + "sCLASSES:%n", " ");
	    for (ClassBlock cls : c.classes)
		info (cls, sb, indent + 2);


	if (c.interfaces != null && c.interfaces.size () > 0)
	    //System.out.format("%" + indent + "sINTERFACES:%n", " ");
	    for (InterfaceBlock ifs : c.interfaces)
		info (ifs, sb, indent + 2);


	if (c.enums != null && c.enums.size () > 0)
	    //System.out.format("%" + indent + "sENUMS:%n", " ");
	    for (EnumBlock enm : c.enums)
		info (enm, sb, indent + 2);
    }



    private void parse (TaskSolutionFile f) throws ParseException, IOException {
	CompilationUnit cu = JavaParser.parse (f);
	cus[cusPosition++] = cu;
	for (int i = 0; i < cu.getTypes ().size (); i++)
	    visitTopLevel (cu.getTypes ().get (i));
    }



    @Override
    public String toString () {
	return String.format ("%s", solution);
    }



    @Override
    public String getCode () {
	StringBuilder sb = new StringBuilder ();

	for (int i = 0; i < cus.length; i++)
	    sb.append (cus[i].toString ());

	return sb.toString ();
    }



    public CompilationUnit[] getCompilationUnits () {
	return cus;
    }
}
