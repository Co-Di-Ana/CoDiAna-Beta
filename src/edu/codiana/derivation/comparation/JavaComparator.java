package edu.codiana.derivation.comparation;

import edu.codiana.core.iface.IComparator;
import edu.codiana.derivation.precalculation.JavaPrecalculation;
import edu.codiana.core.iface.IPrecalculation;
import edu.codiana.derivation.comparation.algorithms.results.ComparationResult;
import edu.codiana.derivation.TaskSolution;
import edu.codiana.parsing.structures.ClassBlock;
import edu.codiana.parsing.JavaPa;
import edu.codiana.parsing.structures.MethodBlock;
import edu.codiana.derivation.comparation.algorithms.HistogramComparator;
import edu.codiana.derivation.comparation.algorithms.IdenticalComparator;
import edu.codiana.derivation.comparation.algorithms.LJWComparator;
import edu.codiana.derivation.comparation.algorithms.StructureComparator;
import japa.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class JavaComparator implements IComparator {

    private String classname;
    private JavaPa parserA, parserB;
    private ClassBlock[] classA, classB;
    private TaskSolution solutionA, solutionB;
    private ComparationResult tmpCalc;
    private float tmpResult;
    private float identicalCR, LJWCR, histogramCR, structureCR;
    public static final float START = (float) 1.0;
    public static boolean DEBUG = Boolean.FALSE;
    public float LJWMultiplier = 1.0f, identicalMultiplier = 1.5f,
	    histogramMultiplier = 1.0f, structureMultiplier = 1f;



    @Override
    public void setFirstPrecalculation (IPrecalculation precalculation) {
	if (precalculation != null && precalculation instanceof JavaPrecalculation)
	    parserA = ((JavaPrecalculation) precalculation).getJavaPa ();

	if (parserA != null)
	    classA = parserA.getAllClasses ();
    }



    @Override
    public void setSecondPrecalculation (IPrecalculation precalculation) {
	if (precalculation != null && precalculation instanceof JavaPrecalculation)
	    parserB = ((JavaPrecalculation) precalculation).getJavaPa ();

	if (parserB != null)
	    classB = parserB.getAllClasses ();
    }



    @Override
    public void setBaseClassName (String classname) {
	this.classname = classname;
    }



    @Override
    public ComparatorResult compare (TaskSolution solutionA, TaskSolution solutionB) throws FileNotFoundException, IOException {
	this.solutionA = solutionA;
	this.solutionB = solutionB;


	//# precalculating programs
	precalculate ();



	float maxValue = Float.MIN_VALUE;
	for (ClassBlock blockA : classA)
	    for (ClassBlock blockB : classB) {
		identicalCR = identicalComparator (blockA, blockB);
		histogramCR = identicalCR == 1.0f ? 1.0f : histogramComparator (blockA, blockB);
		LJWCR = identicalCR == 1.0f ? 1.0f : LVWComparator (blockA, blockB);
		structureCR = identicalCR == 1.0f ? 1.0f : structureComparator (blockA, blockB);

		tmpResult = identicalCR + histogramCR + LJWCR + structureCR;
		if (tmpResult < maxValue) {
		    maxValue = tmpResult;
		}
	    }


	//# some balance
	LJWCR = (float) Math.pow (LJWCR, LJWMultiplier);
	histogramCR = (float) Math.pow (histogramCR, histogramMultiplier);
	identicalCR = (float) Math.pow (identicalCR, identicalMultiplier);
	structureCR = (float) Math.pow (structureCR, structureMultiplier);

	//# returning result
	return new ComparatorResult (identicalCR, LJWCR, histogramCR, structureCR);
    }



    /**
     * Method uses precalculation if possible to lay donw CPU usage
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void precalculate () throws FileNotFoundException, IOException {
	try {
	    if (parserA == null)
		parserA = new JavaPa (solutionA);
	    if (parserB == null)
		parserB = new JavaPa (solutionB);
	} catch (ParseException e) {
	    return;
	}

	if (classA == null)
	    classA = parserA.getAllClasses ();
	if (classB == null)
	    classB = parserB.getAllClasses ();
    }



    /**
     * method compares whole codes against each other using LJWComparator
     * @return number where {@code 1} means exact similarity and 
     * {@code 0} meaning no similarity at all
     * @see LJWComparator
     */
    private float LVWComparator (ClassBlock blockA, ClassBlock blockB) {
	println ("LevenshteinJaroWinkler comparing:");
	int currentDistance = 0;
	int localDistance = 0;
	int totalLines = 0;
	int localLines = 0;
	String bestMatch = null;
	if (blockA != null && blockB != null) {

	    //# if constructors are declared
	    if (blockA.constructor != null && blockB.constructor != null) {
		tmpCalc = LJWComparator.compare (blockA.constructor, blockB.constructor);
		format ("\tconstructors [%d]%n", tmpCalc.currentError);
		currentDistance = tmpCalc.currentError;
		totalLines = (blockA.constructor.numLines + blockB.constructor.numLines) / 2;
	    }

	    for (MethodBlock mtd0 : blockA.methods) {
		localDistance = Integer.MAX_VALUE;

		for (MethodBlock mtd1 : blockB.methods) {
		    tmpCalc = LJWComparator.compare (mtd0, mtd1);

		    if (tmpCalc.currentError < localDistance) {
			localDistance = tmpCalc.currentError;
			localLines = mtd1.numLines;
			bestMatch = String.format ("%s × %s", mtd0.name, mtd1.name);
		    }
		    if (tmpCalc.currentError == 0)
			break;
		}
		currentDistance += localDistance;
		totalLines += (mtd0.numLines + localLines) / 2;
		format ("\tbest local match %s [%d]%n", bestMatch, localDistance);
	    }

	    tmpResult = (START - (float) currentDistance / (totalLines * LJWComparator.SUBSTITUTION));
	    format ("\tsimilarity: %1.2f %%%n", tmpResult * 100);
	}
	return tmpResult;

    }



    /**
     * method compares whole codes against each other using IdenticalComparator
     * @return number where {@code 1} means exact similarity and 
     * {@code 0} meaning no similarity at all
     * @see IdenticalComparator
     */
    private float identicalComparator (ClassBlock blockA, ClassBlock blockB) {
	println ("Identical comparing:");
	String bestMatch = null;

	/** maximum error points avaiable */
	int max = 0;
	/** current error points awarded */
	int cur = 0;
	int loc = 0;

	if (blockA != null && blockB != null) {

	    //# if constructors are declared
	    if (blockA.constructor != null && blockB.constructor != null) {
		tmpCalc = IdenticalComparator.compare (blockA.constructor.code, blockB.constructor.code);
		cur = tmpCalc.currentError;
		format ("\tconstructors [%d]%n", tmpCalc.currentError);
		max++;
	    }

	    max += Math.max (blockA.methods.size (), blockB.methods.size ());

	    for (MethodBlock mtd0 : blockA.methods) {
		loc = 1;
		for (MethodBlock mtd1 : blockB.methods)
		    if (IdenticalComparator.compare (mtd0.code, mtd1.code).currentError == 0) {
			loc = 0;
			bestMatch = String.format ("%s × %s", mtd0.name, mtd1.name);
			break;
		    }
		cur += loc;
		format ("\tbest local match %s [%d]%n", bestMatch, loc);
	    }
	}

	tmpResult = START - ((float) cur / max);
	format ("\tsimilarity: %1.2f %%%n", tmpResult * 100);
	return tmpResult;
    }



    /**
     * method compares whole codes against each other using HistogramComparator
     * @return number where {@code 1} means exact similarity and 
     * {@code 0} meaning no similarity at all
     * @see HistogramComparator
     */
    private float histogramComparator (ClassBlock blockA, ClassBlock blockB) {
	int minLocalDistance, totalDistance = 0;
	Map<String, Integer> r0 = null;
	Map<String, Integer> r1 = null;
	String bestMatch = null;
	int totalSize = 0;
	int localSize = 0;
	println ("Histogram comparing:");

	if (blockA != null && blockB != null) {

	    //# if constructors are declared
	    if (blockA.constructor != null && blockB.constructor != null) {
		r0 = HistogramComparator.computeHistogram (blockA.constructor.code);
		r1 = HistogramComparator.computeHistogram (blockB.constructor.code);
		tmpCalc = HistogramComparator.compare (r0, r1);
		totalSize = tmpCalc.maxError;

		format ("\tconstructors [%d]%n", tmpCalc.currentError);
		totalDistance = (int) tmpCalc.currentError;
	    }

	    for (MethodBlock mtd0 : blockA.methods) {
		minLocalDistance = Integer.MAX_VALUE;
		r0 = HistogramComparator.computeHistogram (mtd0.code);


		for (MethodBlock mtd1 : blockB.methods) {
		    tmpCalc = HistogramComparator.compare (mtd1.code, r0);


		    if (tmpCalc.currentError < minLocalDistance) {
			minLocalDistance = tmpCalc.currentError;
			localSize = tmpCalc.maxError;
			bestMatch = String.format ("%s × %s", mtd0.name, mtd1.name);
		    }
		    if (tmpCalc.currentError == 0)
			break;
		}

		totalSize += localSize;
		totalDistance += minLocalDistance;
		format ("\tbest local match %s [%d]%n", bestMatch, minLocalDistance);

	    }

	    tmpResult = (START - (float) totalDistance / totalSize);
	    tmpResult = (float) Math.pow (tmpResult, 3);
	    format ("\tsimilarity: %1.2f %%%n", tmpResult * 100);
	}

	return tmpResult;
    }



    /**
     * method compares whole codes against each other using StructureComparator
     * @return number where {@code 1} means exact similarity and 
     * {@code 0} meaning no similarity at all
     * @see StructureComparator
     */
    private float structureComparator (ClassBlock blockA, ClassBlock blockB) {
	println ("structureComparator comparing:");
	int currentDistance = 0;
	int localDistance = 0;
	int totalLines = 0;
	int localLines = 0;
	String bestMatch = null;

	if (blockA != null && blockB != null) {

	    //# if constructors are declared
	    if (blockA.constructor != null && blockB.constructor != null) {
		tmpCalc = StructureComparator.compare (blockA.constructor, blockB.constructor);
		format ("\tconstructors [%d]%n", tmpCalc.currentError);
		currentDistance = tmpCalc.currentError;
		totalLines = (blockA.constructor.numLines + blockB.constructor.numLines) / 2;
	    }

	    for (MethodBlock mtd0 : blockA.methods) {
		localDistance = Integer.MAX_VALUE;

		for (MethodBlock mtd1 : blockB.methods) {
		    tmpCalc = StructureComparator.compare (mtd0, mtd1);

		    if (tmpCalc.currentError < localDistance) {
			localDistance = tmpCalc.currentError;
			localLines = mtd1.numLines;
			bestMatch = String.format ("%s × %s", mtd0.name, mtd1.name);
		    }
		    if (tmpCalc.currentError == 0)
			break;
		}
		currentDistance += localDistance;
		totalLines += (mtd0.numLines + localLines) / 2;
		format ("\tbest local match %s [%d]%n", bestMatch, localDistance);
	    }

	    tmpResult = (START - (float) currentDistance / (totalLines * LJWComparator.SUBSTITUTION));
	    format ("\tsimilarity: %1.2f %%%n", tmpResult * 100);
	}
	return tmpResult;

    }



    private void format (String format, Object... args) {
	if (DEBUG)
	    System.out.format (format, args);
    }



    private void println (Object o) {
	if (DEBUG)
	    System.out.println (o);
    }
}
