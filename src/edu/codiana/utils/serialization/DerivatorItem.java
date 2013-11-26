package edu.codiana.utils.serialization;

import edu.codiana.derivation.DerivationPair;
import edu.codiana.derivation.Derivator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class DerivatorItem implements Serializable {
    //--------------------------------------
    // SERIALIZATION
    //--------------------------------------

    private static final long serialVersionUID = 5864381869486415L;
    //--------------------------------------
    // ADVANCED
    //--------------------------------------
    public final String items[];
    public int absoluteDerivations = 0, highDerivations = 0;
    public int mediumDerivations = 0, lowDerivations = 0;
    //--------------------------------------
    // BASE
    //--------------------------------------
    public final String uid, tid, mid, language;
    public final boolean detectTime, generateOutput, checkPlags;
    public final Date date = new Date ();



    /**
     * Create serializable instance from derivator
     * @param executor from {@link Derivator#generate(edu.codiana.derivation.Derivator)  }
     */
    public DerivatorItem (Derivator derivator) {
	//--------------------------------------
	// BASE
	//--------------------------------------
	uid = derivator.entry.getUid ();
	tid = derivator.entry.getTid ();
	mid = derivator.entry.getMid ();
	language = derivator.entry.getLanguage ();
	detectTime = derivator.entry.detectTime ();
	generateOutput = derivator.entry.generateOutput ();
	checkPlags = derivator.entry.isPlagiarismCheck ();

	List<String> tmp = new ArrayList<String> ();
	int size;
	if (derivator.derivatives == null || (size = derivator.derivatives.size ()) == 0) {
	    items = new String[0];

	} else {
	    for (int i = 0; i < size; i++) {
		DerivationPair dp = derivator.derivatives.get (i);
		tmp.add (dp.entryA.entry.uid);
		tmp.add (dp.entryB.entry.uid);

		switch (dp.level) {
		    case ABOSOLUTE:
			absoluteDerivations++;
			break;
		    case HIGH:
			absoluteDerivations++;
			break;
		    case MEDIUM:
			absoluteDerivations++;
			break;
		    case LOW:
			absoluteDerivations++;
			break;
		}
	    }
	    items = tmp.toArray (new String[tmp.size ()]);
	}
    }



    @Override
    public String toString () {
	return String.format ("[DerivatorItem '%s' '%s@%s' result: [absolute: %d, high: %d, medium: %d, low: %d]]",
			      uid, tid, mid, absoluteDerivations, highDerivations, mediumDerivations, lowDerivations);
    }
}
