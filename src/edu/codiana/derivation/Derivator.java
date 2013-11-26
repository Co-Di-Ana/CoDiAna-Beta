package edu.codiana.derivation;

import edu.codiana.core.configuration.Config;
import edu.codiana.core.iface.IComparator;
import edu.codiana.core.iface.IPrecalculation;
import edu.codiana.core.iface.IProgramEntry;
import edu.codiana.utils.io.File;
import edu.codiana.derivation.comparation.ComparatorResult;
import edu.codiana.derivation.comparation.factory.ComparatorFactory;
import edu.codiana.derivation.precalculation.PrecalculationFactory;
import edu.codiana.sql.MySQL;
import edu.codiana.sql.SQLProgramEntry;
import edu.codiana.utils.Base64;
import edu.codiana.utils.serialization.DerivatorItem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Derivator {

    //--------------------------------------
    // PUBLIC FINALS
    //--------------------------------------
    public final IProgramEntry entry;
    public final List<DerivationPair> derivatives;
    //--------------------------------------
    // PRIVATES
    //--------------------------------------
    private List<DerivationEntry>[] solvers;
    private List<TaskSolution>[] solutions;
    private List<IPrecalculation>[] precalculations;
    private File taskDirectory;



    /**
     * enumeration of derivation level
     * <ul>
     *	    <li>{@code LOW}: low level of similiraty according to settings</li>
     *	    <li>{@code MEDIUM}: medium level of similiraty between low and high</li>
     *	    <li>{@code HIGH}: high level of similiraty according to settings</li>
     *	    <li>{@code ABOSOLUTE}: absolute similiraty</li>
     * </ul>
     * @see ComparatorResult
     */
    public enum Level {

	LOW,
	MEDIUM,
	HIGH,
	ABOSOLUTE;
    }



    /**
     * check all solutions from given task entry with default {@code DerivationDecision} setting
     * @param entry containing informations about task
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException 
     * @see IProgramEntry
     * @see SQLProgramEntry
     */
    public Derivator (IProgramEntry entry) throws SQLException, FileNotFoundException, IOException {
	this (entry, new DerivationDecision ());
    }



    /**
     * check all solutions from given task entry
     * @param entry containing informations about task
     * @param decision balance settings which will be used for determining derivatives
     * @throws SQLException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException 
     * @see IProgramEntry
     * @see SQLProgramEntry
     */
    public Derivator (IProgramEntry entry, DerivationDecision decision) throws SQLException, FileNotFoundException, IOException {
	this.entry = entry;
	solutions = new ArrayList[ComparatorFactory.NUM_COMPARATORS];
	precalculations = new ArrayList[ComparatorFactory.NUM_COMPARATORS];
	derivatives = new ArrayList<DerivationPair> ();
	taskDirectory = Config.getMailFile ().resolvePath (entry.getMid ()).resolvePath (entry.getTid ());

	loadSolvers ();
	/*DerivationEntry tmp = solvers[0].get(0);
	solvers[0] = new ArrayList<DerivationEntry>();
	solvers[0].add(tmp);*/

	DerivationEntry item;
	TaskSolutionFile solutionFile;
	TaskSolution s0, s1;
	File location;
	ComparatorResult result;
	IComparator cmp;

	//# initialization and precomputation
	for (int i = 0; i < ComparatorFactory.NUM_COMPARATORS; i++) {
	    final int size = solvers[i].size ();
	    solutions[i] = new ArrayList<TaskSolution> (size);
	    precalculations[i] = new ArrayList<IPrecalculation> (size);

	    for (int j = 0; j < size; j++) {
		item = solvers[i].get (j);
		location = taskDirectory.resolvePath (
			Base64.cleverEncode (item.uid)).resolvePath ("current/" + entry.getTid () + ".java");
		System.out.println (location);
		solutionFile = new TaskSolutionFile (location);
		solutions[i].add (s0 = new TaskSolution (item, solutionFile));
		precalculations[i].add (PrecalculationFactory.getInstance (s0));
	    }
	}


	for (int k = 0; k < ComparatorFactory.NUM_COMPARATORS; k++) {
	    int size = solutions[k].size ();

	    //# comparing
	    for (int i = 0; i < size - 1 * 1; i++) {
		s0 = solutions[k].get (i);

		for (int j = i + 1 * 1; j < size; j++) {
		    s1 = solutions[k].get (j);

		    cmp = ComparatorFactory.getInstance (k + 1);
		    cmp.setBaseClassName (entry.getTid ());
		    cmp.setFirstPrecalculation (precalculations[k].get (i));
		    cmp.setSecondPrecalculation (precalculations[k].get (j));
		    result = cmp.compare (s0, s1);

		    System.out.println (s0.entry.uid + " × " + s1.entry.uid);
		    System.out.println (result);

		    //# derivatives will be added to list 
		    //# and send to database later all at once
		    Level level = decision.isDerivative (result);
		    derivatives.add (new DerivationPair (s0, s1, result, level));
		}
	    }
	}
	System.out.println (derivatives);
    }



    private void loadSolvers () throws SQLException {
	ResultSet resultSet = MySQL.getSolvers (entry);
	DerivationEntry d;

	solvers = (List<DerivationEntry>[]) new ArrayList[ComparatorFactory.NUM_COMPARATORS];
	for (int i = 0; i < ComparatorFactory.NUM_COMPARATORS; i++)
	    solvers[i] = new ArrayList<DerivationEntry> ();

	boolean hasNext = true;
	int index = -1;


	if (!resultSet.next ())
	    return;

	while (hasNext) {
	    d = new DerivationEntry (resultSet);
	    index = ComparatorFactory.getComparatorIndex (d);
	    if (index == -1) {
		hasNext = resultSet.next ();
		continue;
	    }
	    solvers[index].add (d);
	    hasNext = resultSet.next ();
	}

    }


    //--------------------------------------------------------------------------
    //
    // SERIALIZATION
    //
    //--------------------------------------------------------------------------

    /**
     * Method generates serializable DerivatorItem object which can be write into a file
     * @param derivator result from Derivator containing all info
     * @return unique instance of DerivatorItem
     */
    public static DerivatorItem generate (Derivator derivator) {
	return new DerivatorItem (derivator);
    }
}
