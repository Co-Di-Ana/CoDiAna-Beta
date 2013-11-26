package edu.codiana.gui.components;

import edu.codiana.derivation.Derivator;
import edu.codiana.execution.Executor;
import edu.codiana.utils.DateUtil;
import edu.codiana.utils.serialization.DerivatorItem;
import edu.codiana.utils.serialization.ExecutorItem;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class LogEntry {

    private final boolean isPlagCheck;
    private final ExecutorItem executor;
    private final DerivatorItem derivator;
    private static final String SEPARATOR = String.format ("%80s", " ").replaceAll ("\\s", "-");



    /**
     * Creates instance from given ExecutorItem
     * @param entry ExecutorItem created by {@link Executor#generate(edu.codiana.execution.Executor) }
     * @see Executor
     */
    public LogEntry (ExecutorItem entry) {
	isPlagCheck = false;
	executor = entry;
	derivator = null;
    }



    /**
     * Creates instance from given DerivatorItem
     * @param entry DerivatorItem created by {@link Derivator#generate(edu.codiana.derivation.Derivator)  }
     * @see Derivator
     */
    public LogEntry (DerivatorItem entry) {
	isPlagCheck = true;
	derivator = entry;
	executor = null;
    }



    /**
     * Method creates log from either derivator or executor, depending on construction
     * @return log containing all important informations
     */
    public String getLog () {
	StringBuilder sb = new StringBuilder ();

	if (isPlagCheck) {
	    sb.append (String.format ("Kontrola plagiátorství %s v úloze %s (%s)%n",
				      derivator.uid, derivator.tid, DateUtil.format (derivator.date)));
	    sb.append (SEPARATOR);
	    sb.append ('\n');


	    sb.append ("úplných shod:    ");
	    sb.append (derivator.absoluteDerivations);
	    sb.append ('\n');
	    sb.append ("výsokých shod:   ");
	    sb.append (derivator.highDerivations);
	    sb.append ('\n');
	    sb.append ("středních shod:  ");
	    sb.append (derivator.mediumDerivations);
	    sb.append ('\n');
	    sb.append ("nízkých shod:    ");
	    sb.append (derivator.lowDerivations);
	    sb.append ('\n');
	    sb.append (SEPARATOR);
	    sb.append ('\n');
	    sb.append ("Uživatelé: \n");
	    if (derivator.items.length == 0) {
		sb.append ("# žádní uživatelé");
	    } else
		for (int i = 0; i < derivator.items.length; i++) {
		    sb.append (derivator.items[i]);
		    sb.append ('\n');
		}

	} else {
	    sb.append (String.format ("Zpracování kódu od %s, %s v úloze %s (%s)%n",
				      executor.uid, executor.mid, executor.tid, DateUtil.format (executor.date),
				      executor.result));
	    sb.append (SEPARATOR);
	    sb.append ('\n');
	    sb.append (String.format ("Výstup:    %1.3f(%d)\n", executor.outputResult, (int) executor.outputValue));
	    sb.append (String.format ("Čas   :    %1.3f(%d)\n", executor.timeResult, (int) executor.timeValue));
	    sb.append ("Ukončení:  ");
	    sb.append (executor.result);
	    sb.append ('\n');
	    sb.append ("Jazyk:     ");
	    sb.append (executor.language);
	}

	return sb.toString ();
    }



    @Override
    public String toString () {
	if (isPlagCheck)
	    return String.format ("Kontrola plagiátorství %s v úloze %s (%s)",
				  derivator.uid, derivator.tid, DateUtil.format (derivator.date));
	return String.format ("Zpracování kódu od %s, %s v úloze %s (%s), výsledek: %s",
			      executor.uid, executor.mid, executor.tid, DateUtil.format (executor.date), executor.result);
    }
}
