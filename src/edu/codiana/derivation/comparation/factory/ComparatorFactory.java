package edu.codiana.derivation.comparation.factory;

import edu.codiana.core.iface.IComparator;
import edu.codiana.derivation.DerivationEntry;
import edu.codiana.derivation.comparation.JavaComparator;

/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ComparatorFactory {
    
    public static final int NUM_COMPARATORS = 1;
    
    /**
     * method resurns specific instance of {@code IComparator} based on given {@code DerivationEntry}
     * @param entry {@code DerivationEntry} containing data about solver
     * @return instance of {@code IComparator} or null if comparations is not supported
     * @see DerivationEntry
     * @see IComparator
     * @see JavaComparator
     */
    public static IComparator getInstance (DerivationEntry entry) {
	if (!entry.isValidProgram())
	    return null;
	
	if (entry.language.indexOf ("java") != -1)
	    return new JavaComparator();
	
	return null;
    }
    /**
     * method resurns specific instance of {@code IComparator} based on given index
     * @param entry {@code DerivationEntry} containing data about solver
     * @return instance of {@code IComparator} or null if comparations is not supported
     * @see DerivationEntry
     * @see IComparator
     * @see JavaComparator
     */
    public static IComparator getInstance (int index) {
	if (index == 1)
	    return new JavaComparator();
	
	return null;
    }
    
    /**
     * method resurns {@code true} or {@code false} based on given {@code DerivationEntry}
     * @param entry {@code DerivationEntry} containing data about solver
     * @return {@code true} if sopported and valid solutions is found otherwise {@code false}
     * @see DerivationEntry
     * @see DerivationEntry#isValidProgram()
     */
    public static int getComparatorIndex  (DerivationEntry entry) {
	if (!entry.isValidProgram())
	    return -1;
	
	if (entry.language.indexOf ("java") != -1)
	    return 0;
	
	return -1;
    }
}
