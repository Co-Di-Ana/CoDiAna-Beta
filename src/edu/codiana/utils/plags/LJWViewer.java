package edu.codiana.utils.plags;

import edu.codiana.derivation.comparation.algorithms.LJWComparator;
import edu.codiana.derivation.comparation.algorithms.results.LJWComparationResult;
import java.util.ArrayList;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LJWViewer {

    private final String[] a, b;
    private int[][] matrix;
    private List<String> resultA = new ArrayList<String>();
    private List<String> resultB = new ArrayList<String>();



    /**
     * Enumerations of indents type
     */
    public enum ChangeType {

	NONE, SAME, INDEL_A, INDEL_B, SUBS, A, B;
    }



    /**
     * Creates instance of LJWViewer and calculates and print probable path of LJW algorithm
     * @param arrayA line of code A
     * @param arrayB line of code B
     * @param result see {@link LJWComparator#compare(java.lang.String[], java.lang.String[]) }
     */
    public LJWViewer(String[] arrayA, String[] arrayB, LJWComparationResult result) {
	this.a = arrayA;
	this.b = arrayB;
	matrix = result.matrix;
	jumpToNext(0, 0, ChangeType.NONE);
	
	for (int i = 0; i < resultA.size(); i++) {
	    String la = resultA.get(i);
	    String lb = resultB.get(i);
	    System.out.format("%-50s %-50s %n", la, lb);
	    
	}
    }



    private void jumpToNext(int y, int x, ChangeType type) {
	System.out.println(type + ", y: " + y + ", x: " + x);

	switch (type) {
	    case NONE:
		break;
	    case SAME:
	    case SUBS:
		resultA.add(a[y - 1]);
		resultB.add(b[x - 1]);
		break;
	    case INDEL_B:
		resultA.add("");
		resultB.add(b[x - 1]);
		break;
	    case INDEL_A:
		resultA.add(a[y - 1]);
		resultB.add("");
		break;
		
		
	    case A:
		resultA.add("");
		resultB.add(b[x - 1]);
		break;
	    case B:
		resultA.add(a[y - 1]);
		resultB.add("");
		break;
	}

	int diagRow, indentA, indentB;

	if (y + 1 >= a.length && x + 1 >= b.length)
	    return;


	diagRow = x + 1 >= b.length || y + 1 >= a.length ? Integer.MAX_VALUE : matrix[y + 1][x + 1];
	indentA = x + 1 >= b.length ? Integer.MAX_VALUE : matrix[y + 0][x + 1];
	indentB = y + 1 >= a.length ? Integer.MAX_VALUE : matrix[y + 1][x + 0];

	//System.out.format("%d %d %d %n", diagRow, indentA, indentB);
	if (diagRow <= indentA && diagRow <= indentB)
	    jumpToNext(y + 1, x + 1, diagRow == matrix[y][x] ? ChangeType.SAME : ChangeType.SUBS);
	else if (indentA <= indentB)
	    jumpToNext(y + 0, x + 1, indentA == matrix[y][x] ? ChangeType.A :ChangeType.INDEL_B);
	else
	    jumpToNext(y + 1, x + 0, indentB == matrix[y][x] ? ChangeType.B :ChangeType.INDEL_A);
    }
}
