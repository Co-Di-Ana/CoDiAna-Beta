package edu.codiana.gui;

import java.io.PrintStream;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public final class ConsoleStream extends PrintStream {

    private static ConsoleStream instance;



    /**
     * Singleton constructor
     * @return singleton instance
     */
    public static ConsoleStream getInstance () {
	return instance == null ? instance = new ConsoleStream () : instance;
    }



    private ConsoleStream () {
	super (System.out);
    }



    @Override
    public void print (String s) {
	super.print (s);

	if (Console.getInstance () != null)
	    Console.getInstance ().append (s);
    }
}