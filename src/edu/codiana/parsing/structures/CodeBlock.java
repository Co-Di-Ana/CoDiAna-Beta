package edu.codiana.parsing.structures;

import japa.parser.ast.body.*;
import japa.parser.ast.stmt.*;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class CodeBlock {

    //--------------------------------------
    // PUBLIC
    //--------------------------------------
    public List<ClassBlock> classes;
    public List<InterfaceBlock> interfaces;
    public List<EnumBlock> enums;
    public List<MethodBlock> methods;
    public MethodBlock constructor;
    public List<String> structure = new ArrayList<String> ();
    public static final BlocksIdentifiers ID = new BlocksIdentifiers ();
    //--------------------------------------
    // PRIVATES
    //--------------------------------------
    private int level = 0;



    /**
     * Creates instance of CodeBlock
     */
    public CodeBlock () {
	classes = new ArrayList<ClassBlock> ();
	interfaces = new ArrayList<InterfaceBlock> ();
	enums = new ArrayList<EnumBlock> ();
	methods = new ArrayList<MethodBlock> ();
	info ();
    }



    protected void visitBody (BodyDeclaration body) {
	if (body instanceof ClassOrInterfaceDeclaration) {

	    if (((ClassOrInterfaceDeclaration) body).isInterface ())
		interfaces.add (new InterfaceBlock ((ClassOrInterfaceDeclaration) body));
	    else classes.add (new ClassBlock ((ClassOrInterfaceDeclaration) body));

	} else if (body instanceof EnumDeclaration) {
	    enums.add (new EnumBlock ((EnumDeclaration) body));

	} else if (body instanceof TypeDeclaration) {
	    List<BodyDeclaration> members = ((TypeDeclaration) body).getMembers ();
	    if (members == null) return;
	    for (BodyDeclaration member : members)
		visitBody (member);

	} else if (body instanceof ConstructorDeclaration) {
	    constructor = new MethodBlock ((ConstructorDeclaration) body);
	    /*inc();
	    add("constructor");
	    visitBlock(((ConstructorDeclaration) body).getBlock());
	    dec();*/

	} else if (body instanceof MethodDeclaration) {
	    /*inc();
	    add("method-"+((MethodDeclaration) body).getName());
	    visitBlock(((MethodDeclaration) body).getBody());
	    dec();*/

	    methods.add (new MethodBlock ((MethodDeclaration) body));
	}
    }



    protected void visitTopLevel (TypeDeclaration body) {
	if (body instanceof ClassOrInterfaceDeclaration) {

	    if (((ClassOrInterfaceDeclaration) body).isInterface ())
		interfaces.add (new InterfaceBlock ((ClassOrInterfaceDeclaration) body));
	    else classes.add (new ClassBlock ((ClassOrInterfaceDeclaration) body));

	} else if (body instanceof EnumDeclaration) {
	    enums.add (new EnumBlock ((EnumDeclaration) body));
	}
    }



    protected void visitBlock (BlockStmt body) {
	if (body == null)
	    return;
	if (body.getStmts () == null)
	    return;
	visitBlock (body.getStmts ());
    }



    protected void visitBlock (List<Statement> stmts) {
	if (stmts == null)
	    return;

	for (Statement s : stmts)
	    visitStatement (s);
    }



    protected void visitStatement (Statement s) {
	Statement st;

	//# for statement
	if (s instanceof ForStmt) {

	    inc ();
	    add (ID.FOR);
	    ForStmt f = (ForStmt) s;
	    if ((st = f.getBody ()) instanceof BlockStmt)
		visitBlock ((BlockStmt) st);
	    else
		visitStatement (st);
	    dec ();

	} else if (s instanceof ForeachStmt) {
	    //# foreach statement

	    inc ();
	    add (ID.FOREACH);
	    ForeachStmt f = (ForeachStmt) s;
	    if ((st = f.getBody ()) instanceof BlockStmt)
		visitBlock ((BlockStmt) st);
	    else
		visitStatement (st);
	    dec ();

	} else if (s instanceof IfStmt) {
	    //# if (also elifs and else) statement

	    inc ();
	    add (ID.IF);
	    IfStmt f = (IfStmt) s;
	    if ((st = f.getThenStmt ()) instanceof BlockStmt)
		visitBlock ((BlockStmt) st);
	    else
		visitStatement (st);
	    dec ();


	    st = f.getElseStmt ();
	    if (st != null) {
		inc ();
		add ("else");
		if (st instanceof BlockStmt)
		    visitBlock ((BlockStmt) st);
		else
		    visitStatement (st);
		dec ();
	    }

	} else if (s instanceof WhileStmt) {
	    //# while statement
	    inc ();
	    add (ID.WHILE);
	    WhileStmt f = (WhileStmt) s;
	    if ((st = f.getBody ()) instanceof BlockStmt)
		visitBlock ((BlockStmt) st);
	    else
		visitStatement (st);

	    dec ();

	} else if (s instanceof DoStmt) {
	    //# do statement

	    add (ID.DO);
	    DoStmt f = (DoStmt) s;
	    if ((st = f.getBody ()) instanceof BlockStmt)
		visitBlock ((BlockStmt) st);
	    else
		visitStatement (st);

	} else if (s instanceof SwitchStmt) {
	    //# switch statement
	    inc ();
	    add (ID.SWITCH);
	    SwitchStmt f = (SwitchStmt) s;
	    for (SwitchEntryStmt e : f.getEntries ())
		visitBlock (e.getStmts ());
	    dec ();

	} else if (s instanceof SwitchEntryStmt) {
	    //# switch statement
	    info ();
	    add (ID.SWITCH_ENTRY);
	    SwitchEntryStmt f = (SwitchEntryStmt) s;
	    visitBlock (f.getStmts ());
	    dec ();
	} else if (s instanceof TryStmt) {
	    //# try (also catchs and finally) statement

	    inc ();
	    add (ID.TRY);
	    TryStmt f = (TryStmt) s;
	    visitBlock ((BlockStmt) f.getTryBlock ());
	    for (CatchClause c : f.getCatchs ())
		visitBlock ((BlockStmt) c.getCatchBlock ());
	    visitBlock ((BlockStmt) f.getFinallyBlock ());
	    dec ();

	} else if (s instanceof ExpressionStmt) {
	    //# expression statement
	    info ();
	    add (ID.EXPRESSION, 1);
	} else if (s instanceof ReturnStmt) {
	    //# return statement
	    info ();
	    add (ID.RETURN, 1);
	} else if (s instanceof BreakStmt) {
	    //# break statement
	    info ();
	    add (ID.BREAK, 1);
	} else if (s instanceof ContinueStmt) {
	    //# continue statement
	    info ();
	    add (ID.CONTINUE, 1);
	} else if (s instanceof EmptyStmt) {
	    //# empty statement
	    info ();
	    add (ID.EMPTY, 1);
	} else if (s instanceof TypeDeclarationStmt) {
	    //# nothing
	    visitTopLevel (((TypeDeclarationStmt) s).getTypeDeclaration ());
	} else {
	    add (ID.OTHER);
	}
    }



    /**
     * Method returns code if possible
     * @return string or null if no code is found
     */
    public String getCode () {
	return null;
    }



    private void inc () {
	info ();
	level++;
    }



    private void dec () {
	level--;
	info ();
    }



    private void add (String s) {
	add (s, 0);
    }



    private void add (String s, int i) {
	structure.add (s);
	//structure.add (String.format ("%" + ((level + i) * 4 + 1) + "s%s", " ", s));
    }



    private void info () {
	//System.out.format("%"+(level*4+1)+"s[%s] level = %d%n", " ",this.getClass().getSimpleName(), level);
    }
}
