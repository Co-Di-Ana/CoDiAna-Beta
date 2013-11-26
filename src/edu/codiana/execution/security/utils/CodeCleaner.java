package edu.codiana.execution.security.utils;



/**
 * class used for quick formate of source code
 * @author Jan Hybš
 * @version 1.0
 */
public class CodeCleaner {

    static final char COMM_START = '/';
    static final char COMM_LINE_CONT = '/';
    static final char COMM_BLOCK_CONT = '*';
    static final char SINGLE_QUOTE = '\'';
    static final char DOUBLE_QUOTE = '"';
    static final char ESCAPE = '\\';
    public static final char OPEN_BRACE = '{';
    public static final char CLOSE_BRACE = '}';
    public static final char INVALID = (char) -1;
    static final char OPEN_PARENTHESIS = '(';
    static final char CLOSE_PARENTHESIS = ')';
    static final char SEMICOLON = ';';
    static final char COMMA = ',';
    static final char SPACE = ' ';
    static final char NEW_LINE = '\n';
    static final char TAB = '\t';



    /** All possible states, in which can be source code */
    private static enum States {

	normal, singleQuote, doubleQuote, lineComm, blockComm
    }
    /** formated result */
    private StringBuilder data;
    /** formated buffer (last 8 characters) */
    private CharBuffer2 output;
    /** non-formated buffer (last 6 characters and 2 next characters) */
    private CharBuffer2 input;
    /** source code state */
    private States state;



    /**
     * Method returns cleaned code
     * @return 
     */
    public StringBuilder getData () {
	return data;
    }



    /**
     * Method cleans Java code from comments and strings
     * @param source source java File
     */
    public CodeCleaner (StringBuilder source) {
	int l = source.length ();
	state = States.normal;

	output = new CharBuffer2 ();
	output.appendToCenter = true;
	input = new CharBuffer2 ();
	data = new StringBuilder (l * 2);
	int h = input.middle;
	int braces = 0;
	int parenthesis = 0;
	int closeParenthesis = Integer.MIN_VALUE;
	char c;

	//# help flags
	/** flag which signalize urge/necessity to apend space character when time is right */
	boolean needSpace = false;
	/** flag disables 'neddSpace' flag activation (postpone urge to add space after non-white-char is found) */
	boolean ignoreNeedSpace = true;
	/** flag telling whether last append was new line */
	boolean lastNewLine = false;
	/** flag which skips after append control
	 * e.g if we found single quote and we append it and we check whether
	 * some state is ending and we find same quote*/
	boolean skipControl = false;

	//# go through every character plus several characters which were
	//# used to look forward
	for (int i = 0; i < l + h; i++) {
	    //# loading next character
	    input.append (c = i < l ? source.charAt (i) : INVALID);
	    c = input.c;
	    skipControl = false;

	    //# when is not loaded enough characters just skip
	    if (c == INVALID)
		continue;

	    //# changing current state, (from normal)
	    if (state == States.normal)
		if (input.isLineComment ()) {
		    state = States.lineComm;
		    skipControl = true;
		} else if (input.isBlockComment ()) {
		    state = States.blockComm;
		    skipControl = true;
		} else if (c == SINGLE_QUOTE) {
		    state = States.singleQuote;
		    skipControl = true;
		} else if (c == DOUBLE_QUOTE) {
		    state = States.doubleQuote;
		    skipControl = true;
		} else if (c == OPEN_PARENTHESIS)
		    parenthesis++;
		else if (c == CLOSE_PARENTHESIS)
		    parenthesis--;
		else if (c == OPEN_BRACE)
		    braces++;
		else if (c == CLOSE_BRACE)
		    braces--;

	    //# part where all needed characters and appended (if state is normal)
	    if (state == States.normal)
		//# when we reach white char we DO NOT append it right away
		//# we just set flag 'needSpace' to true
		if (input.isWhiteChar ()) {
		    if (!ignoreNeedSpace)
			needSpace = true;

		    //# when er reach characters after which line break is needed
		    //# and based on character and previous appended character
		    //# appended new line/s
		} else if (input.isBreakingChar () || c == COMMA)
		    //# if we are NOT inside of paranthesis
		    if (parenthesis == 0) {
			if (c == OPEN_BRACE)
			    if (!lastNewLine)
				append (NEW_LINE);
			if (c == CLOSE_BRACE)
			    if (output.ppc != NEW_LINE)
				append (NEW_LINE);
			append (c);
			append (NEW_LINE);
			needSpace = false;
			ignoreNeedSpace = true;
			lastNewLine = true;

			//# otherwise we format other way
		    } else {
			append (c);
			lastNewLine = false;
			ignoreNeedSpace = true;
			needSpace = false;
		    }
		//# character which do not need space afre or before
		//# clear flag 'needSpace'
		else if (input.isSpacelessChar ()) {
		    append (c);
		    lastNewLine = false;
		    needSpace = false;
		    ignoreNeedSpace = true;

		    //# any other character just append
		    //# but we check flag 'needSpace' beforehand
		} else {
		    if (needSpace)
			append (' ');
		    lastNewLine = false;
		    needSpace = false;
		    ignoreNeedSpace = false;
		    append (input.c);
		}

	    /*
	    //# if state is not NORMAL, we simple append
	    //# 
	    else if (!(state == States.doubleQuote || state == States.singleQuote))
	    append (c);
	     */

	    //# when we are in parenthesis control block (if, for, ...)
	    //# if true that means we reached condition last parenthesis
	    //# and append new line
	    //# DN: some condions do now have opening braces so code would
	    //#     have been on same line, this way it is seperated
	    if (closeParenthesis == parenthesis) {
		append (NEW_LINE);
		lastNewLine = true;
		closeParenthesis = Integer.MIN_VALUE;
	    }

	    //# looking for control block (if, for, ...)
	    if (c == OPEN_PARENTHESIS)
		if (output.isControlBlock ())
		    closeParenthesis = parenthesis - 1;

	    //# changing state back to normal
	    //# registering sequences which are ending some blocks
	    //# e.g. quotes 
	    if (skipControl == false && state != States.normal) {
		if (state == States.lineComm && c == NEW_LINE)
		    state = States.normal;
		if (state == States.blockComm && c == COMM_START && input.pc == COMM_BLOCK_CONT)
		    state = States.normal;
		if (state == States.doubleQuote && c == DOUBLE_QUOTE && input.isEscaped ())
		    state = States.normal;
		if (state == States.singleQuote && c == SINGLE_QUOTE && input.isEscaped ())
		    state = States.normal;
	    }
	}
    }



    /**
     * append character to string buffer, and char buffer
     * @param c char to be appended
     */
    private void append (char c) {
	data.append (c);
	output.append (c);
    }



    /**
     * format given JAVA source code to light weight code, mark brace blocks, and return CodeCleaner object
     * @param source
     * @return new instance of CodeCleaner, containing formatted code
     */
    public static CodeCleaner format (StringBuilder source) {
	return new CodeCleaner (source);
    }



    /**
     * format given JAVA source code to light weight code, mark brace blocks, and return CodeCleaner object
     * @param source
     * @return new instance of CodeCleaner, containing formatted code
     */
    public static CodeCleaner format (String source) {
	return new CodeCleaner (new StringBuilder (source));
    }
}



/**
 * help class for buffering chars<br />
 * depending on constructor, this object is able to memorize from 
 * <b><code>9</code></b> to <b><code>Integer.MAX_VALUE</code></b> chars<br />
 * this object provides methods for code distinguishing such as:
 * <li> whether is last char sequence escaped </li>
 * <li> whether last char sequence appears as comment</li>
 * <li>and more...</li>
 * @author Hans
 * @see Integer
 */
class CharBuffer2 {

    /** buffer size */
    public final int length;
    /** int pointing to the middle of the buffer */
    public final int middle;
    /** buffer cointaning all chars */
    public final char[] buffer;
    /** easy-to-acces variable */
    public char ppc, pc, c, nc, nnc;
    /** wheter new appended char will be in the middle of the buffer */
    public boolean appendToCenter = false;



    /**
     * Creates instance of CharBuffer2 with default size of 9
     */
    public CharBuffer2 () {
	this (9);
    }



    /**
     * Creates instance of CharBuffer2 with given size
     * @param length size of the buffer, minimum length is 9
     */
    public CharBuffer2 (int length) {
	this.length = length < 9 ? 9 : length;
	this.middle = this.length - 3;
	buffer = new char[this.length];
	for (int i = 0; i < this.length; i++)
	    buffer[i] = CodeCleaner.INVALID;
	updateChars ();
    }



    /** rotate buffer, and updates easy-to-access varibles */
    public void rotate () {
	for (int i = 0; i < length - 1; i++)
	    buffer[i] = buffer[i + 1];
	updateChars ();
    }



    /**
     * append new char to buffer<br />
     * depending on setting in the middle or at the end
     * @param ch char which will be appended
     */
    public void append (char ch) {
	rotate ();
	buffer[!appendToCenter ? length - 1 : middle] = ch;
	nnc = buffer[middle + 2];
    }



    /** updates easy-to-access varibles */
    private void updateChars () {
	ppc = buffer[middle - 2];
	pc = buffer[middle - 1];
	c = buffer[middle];
	nc = buffer[middle + 1];
	nnc = buffer[middle + 2];
    }



    /** method tells whether is sequence escaped<br />
     * <code> ? ? ? ^\ ? </code> or <code> ? ? \ \ ? </code>*/
    public boolean isEscaped () {
	return pc != CodeCleaner.ESCAPE ||
	       (pc == CodeCleaner.ESCAPE && ppc == CodeCleaner.ESCAPE);
    }



    /** method tells whether is sequence beggining of a line comment<br />
     * <code> ? ? ? / / </code>*/
    public boolean isLineComment () {
	return c == CodeCleaner.COMM_START && nc == CodeCleaner.COMM_LINE_CONT;
    }



    /** method tells whether is sequence beggining of a block comment<br />
     * <code> ? ? ? / * </code>*/
    public boolean isBlockComment () {
	return c == CodeCleaner.COMM_START && nc == CodeCleaner.COMM_BLOCK_CONT;
    }



    /** method tells whether is sequence simple white char<br />
     * in ascii: 9, 10, 13, 32*/
    public boolean isWhiteChar () {
	return c == CodeCleaner.SPACE || c == CodeCleaner.TAB ||
	       c == CodeCleaner.NEW_LINE || c == (char) 13;
    }



    /**
     * method tells whether is on <code>middle - s</code> position separating
     * character
     * @param s shift position
     */
    private boolean isSeperatingChar (int s) {
	return (buffer[middle - s] == CodeCleaner.INVALID || buffer[middle - s] == ' ' ||
		buffer[middle - s] == '\n' || buffer[middle - s] == ';' ||
		buffer[middle - s] == '\t');
    }



    /** method tells wheter is current char a char which in some way breaks
     * flow of the text */
    public boolean isBreakingChar () {
	return c == CodeCleaner.SEMICOLON || c == CodeCleaner.OPEN_BRACE ||
	       c == CodeCleaner.CLOSE_BRACE;
    }



    /** method tells wheter is current char a char after which is no need
     * to add space*/
    public boolean isSpacelessChar () {
	return c == CodeCleaner.OPEN_PARENTHESIS || c == CodeCleaner.CLOSE_PARENTHESIS ||
	       c == '-' || c == '+' || c == '*' || c == '/' || c == '%' || c == ':' ||
	       c == '<' || c == '>' || c == '*' || c == '=' || c == '|' || c == '&' ||
	       c == '.' || c == '[' || c == ']' || c == ',' || c == ';' || c == '!' ||
	       c == '?';
    }



    /**
     * method compare given string to a buffer on <code>middle - o</code>
     * position
     * @param s compare string
     * @param o shift
     */
    private boolean eq (String s, int o) {
	char[] cs = s.toCharArray ();
	int l = cs.length;
	for (int i = 0; i < l; i++)
	    if (buffer[middle - i - o] != cs[l - 1 - i])
		return false;
	return true;
    }



    /** method tells wheter is in the middle of current buffer <code>if</code> 
     * statement */
    public boolean isIf () {
	return isSeperatingChar (3) && eq ("if(", 0);
    }



    /** method tells wheter is in the middle of current buffer <code>for</code>
     * statement */
    public boolean isFor () {
	return isSeperatingChar (4) && eq ("for(", 0);
    }



    /** method tells wheter is in the middle of current buffer 
     * <code>while</code> statement */
    public boolean isWhile () {
	return isSeperatingChar (6) && eq ("while(", 0);
    }



    /** method tells wheter is in the middle of current buffer 
     * <code>do</code> statement */
    public boolean isDo () {
	return isSeperatingChar (3) && eq ("do(", 0);
    }



    /** method tells wheter is in the middle of current buffer 
     * <code>if, for, while, or do</code> statement */
    public boolean isControlBlock () {
	return isIf () || isFor () || isWhile () || isDo ();
    }



    @Override
    public String toString () {
	StringBuilder s = new StringBuilder ();
	for (int i = 0; i < length; i++)
	    if (i == middle) {
		s.append ('|');
		s.append (charToString (buffer[i]));
		s.append ('|');
	    } else
		s.append (charToString (buffer[i]));
	return s.toString ();
    }



    /**
     * returns string representation of the given char
     * @param ch char which will be turned in to formatted string
     * @return formatted string, which replace common two-digit char 
     * such as \n \r \t ...
     */
    private String charToString (char ch) {
	switch (ch) {
	    case '\n':
		return "\\n ";
	    case '\t':
		return "\\t ";
	    case '\r':
		return "\\r ";
	    case '\b':
		return "\\b ";
	    case ' ':
		return " • ";
	    case CodeCleaner.INVALID:
		return " ● ";
	    default:
		return " " + ch + " ";
	}
    }
}