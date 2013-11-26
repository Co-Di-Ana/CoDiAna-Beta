package edu.codiana.gui.components;

import edu.codiana.gui.LogViewer;
import edu.codiana.utils.serialization.DerivatorItem;
import edu.codiana.utils.serialization.ExecutorItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class ExtendedList extends JList {

    private static final int NO_SELECTION = -1;
    private final DefaultListModel model;
    


    public ExtendedList () {
	super (new DefaultListModel ());
	model = (DefaultListModel) getModel ();	
	addMouseListener (new MouseAdapter () {



	    @Override
	    public void mouseClicked (MouseEvent e) {
		if (e.getClickCount () == 2) {
		    LogEntry le = getSelectedItem ();
		    if (le != null)
			LogViewer.show (le);
		}
	    }
	    
	});
    }



    /**
     * Method adds log LogEntry at the end
     * @param log 
     */
    public void addItem (LogEntry log) {
	model.add (0, log);
    }



    /**
     * Method remove specific item at given position
     * @param index from 0 to size -1
     * @return true if success false otherwise
     */
    public boolean removeItemAt (int index) {
	model.removeElementAt (index);
	return true;
    }



    /**
     * Method remove specific item
     * @param log item to be removed
     * @return true if success false otherwise
     */
    public boolean removeItem (LogEntry log) {
	return model.removeElement (log);
    }



    /**
     * Method returns selected item if there is such
     * @return true on success false if there is no selection
     */
    public boolean removeSelectedItem () {
	final int index = getSelectedIndex ();
	if (index == NO_SELECTION)
	    return false;

	model.removeElementAt (index);
	return true;

    }



    /**
     * Method returns selected item or null if there is no selection
     * @return null or LogEntry item
     */
    public LogEntry getSelectedItem () {
	final int index = getSelectedIndex ();
	return index == NO_SELECTION ? null : (LogEntry) model.getElementAt (index);
    }



    /**
     * Method clears all items
     */
    public void clearAll () {
	model.removeAllElements ();
    }



    /**
     * Method adds list of items
     * @param items 
     */
    public void addItems (List<Object> items) {
	if (items == null || items.isEmpty ())
	    return;
	final int size = items.size ();
	ExecutorItem ei;
	DerivatorItem di;
	for (int i = 0; i < size; i++) {
	    Object object = items.get (i);
	    
	    if (object instanceof ExecutorItem) {
		ei = (ExecutorItem) object;
		addItem (new LogEntry (ei));
	    } else if (object instanceof DerivatorItem) {
		di = (DerivatorItem) object;
		addItem (new LogEntry (di));
	    }
	}
    }
}
