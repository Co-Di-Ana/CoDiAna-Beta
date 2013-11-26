package edu.codiana.derivation;

import edu.codiana.derivation.Derivator.Level;
import edu.codiana.derivation.comparation.ComparatorResult;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class DerivationPair {

    public final TaskSolution entryA, entryB;
    public final ComparatorResult result;
    public final Level level;



    /**
     * Creates simple instance which holds info about derivations
     * @param entryA solution A
     * @param entryB solution B
     * @param result result from algs
     * @param level severness level
     */
    public DerivationPair (TaskSolution entryA, TaskSolution entryB, ComparatorResult result, Derivator.Level level) {
	this.entryA = entryA;
	this.entryB = entryB;
	this.result = result;
	this.level = level;
    }



    @Override
    public String toString () {
	return String.format ("%s × %s [%s]", entryA.entry.uid, entryB.entry.uid, level.toString ());
    }
}
