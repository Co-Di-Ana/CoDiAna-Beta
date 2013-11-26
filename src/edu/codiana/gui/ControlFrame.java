package edu.codiana.gui;

import edu.codiana.gui.components.ExtendedList;
import edu.codiana.gui.components.LogEntry;
import edu.codiana.utils.serialization.DerivatorItem;
import edu.codiana.utils.serialization.ExecutorItem;
import edu.codiana.utils.serialization.History;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;



/**
 *
 * @author Hans
 */
public class ControlFrame extends javax.swing.JFrame implements ActionListener {

    //--------------------------------------
    // SIZE
    //--------------------------------------
    private final Dimension bigSize = new Dimension (800, 370);
    private final Dimension smallSize = new Dimension (800, 210);
    //--------------------------------------
    // ICONS
    //--------------------------------------
    private boolean historyEnability = true;



    /**
     * Creates instance and shows it
     */
    public ControlFrame () {
	initComponents ();
	setResizable (false);
	updateSize (smallSize);
	setVisible (true);
	historyEnability (false);
    }



    /**
     * Method adds new entry to the history list
     * @param entry 
     */
    public void addItem (ExecutorItem entry) {
	((ExtendedList) logEL).addItem (new LogEntry (entry));
    }



    /**
     * Method adds new entry to the history list
     * @param entry 
     */
    public void addItem (DerivatorItem entry) {
	((ExtendedList) logEL).addItem (new LogEntry (entry));
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("CoDiAna");

        startPauseBtn.setFocusPainted(false);

        infoTA.setColumns(20);
        infoTA.setFont(new Font("Consolas", 0, 12));
        infoTA.setRows(5);
        infoSP.setViewportView(infoTA);

        manualCheckCB.setSelected(true);
        manualCheckCB.setText("ruční kontrola kódů");
        controlControlsPnl.add(manualCheckCB);

        consoleCB.setText("konzole");
        controlControlsPnl.add(consoleCB);

        logCB.setText("historie");
        logCB.addActionListener(this);
        controlControlsPnl.add(logCB);

        infoLbl.setFont(new Font("Tahoma", 1, 12));
        infoLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        progressPB.setOrientation(JProgressBar.VERTICAL);

        GroupLayout controlPnlLayout = new GroupLayout(controlPnl);
        controlPnl.setLayout(controlPnlLayout);
        controlPnlLayout.setHorizontalGroup(
            controlPnlLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(controlPnlLayout.createSequentialGroup()
                .addComponent(startPauseBtn, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(infoSP, GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(progressPB, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
            .addGroup(controlPnlLayout.createSequentialGroup()
                .addComponent(controlControlsPnl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(infoLbl, GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE))
        );
        controlPnlLayout.setVerticalGroup(
            controlPnlLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(controlPnlLayout.createSequentialGroup()
                .addGroup(controlPnlLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(infoLbl, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(controlControlsPnl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(controlPnlLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(infoSP, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                    .addComponent(progressPB, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                    .addComponent(startPauseBtn, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addContainerGap())
        );

        historyControlPnl.setLayout(new FlowLayout(FlowLayout.LEFT));

        loadHistoryBtn.setText("načísti historii");
        loadHistoryBtn.addActionListener(this);
        historyControlPnl.add(loadHistoryBtn);

        clearSelectedBtn.setText("vymazat vybraný");
        clearSelectedBtn.addActionListener(this);
        historyControlPnl.add(clearSelectedBtn);

        clearHistoryBtn.setText("vymazat vše");
        clearHistoryBtn.addActionListener(this);
        historyControlPnl.add(clearHistoryBtn);

        logSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        logSP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        logEL.setFont(new Font("Consolas", 0, 10)); // NOI18N
        logEL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logEL.setMaximumSize(null);
        logEL.setMinimumSize(null);
        logEL.setPreferredSize(null);
        logEL.setVisibleRowCount(4);
        logSP.setViewportView(logEL);

        GroupLayout historyPnlLayout = new GroupLayout(historyPnl);
        historyPnl.setLayout(historyPnlLayout);
        historyPnlLayout.setHorizontalGroup(
            historyPnlLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(historyControlPnl, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
            .addComponent(logSP, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        historyPnlLayout.setVerticalGroup(
            historyPnlLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(historyPnlLayout.createSequentialGroup()
                .addComponent(historyControlPnl, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(logSP, GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
        );

        GroupLayout mainPnlLayout = new GroupLayout(mainPnl);
        mainPnl.setLayout(mainPnlLayout);
        mainPnlLayout.setHorizontalGroup(
            mainPnlLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(mainPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPnlLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(controlPnl, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(historyPnl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        mainPnlLayout.setVerticalGroup(
            mainPnlLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(mainPnlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controlPnl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(historyPnl, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPnl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPnl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == logCB) {
            ControlFrame.this.onHistoryClicked(evt);
        }
        else if (evt.getSource() == loadHistoryBtn) {
            ControlFrame.this.onLoadHistoryClicked(evt);
        }
        else if (evt.getSource() == clearSelectedBtn) {
            ControlFrame.this.onClearSelectedClicked(evt);
        }
        else if (evt.getSource() == clearHistoryBtn) {
            ControlFrame.this.onClearHistoryClicked(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void onHistoryClicked (ActionEvent evt) {//GEN-FIRST:event_onHistoryClicked
	historyEnability (!historyEnability);
    }//GEN-LAST:event_onHistoryClicked

    private void onClearHistoryClicked (ActionEvent evt) {//GEN-FIRST:event_onClearHistoryClicked
	((ExtendedList) logEL).clearAll ();
    }//GEN-LAST:event_onClearHistoryClicked

    private void onLoadHistoryClicked (ActionEvent evt) {//GEN-FIRST:event_onLoadHistoryClicked
	List<Object> items = History.loadItems ();
	((ExtendedList) logEL).addItems (items);
    }//GEN-LAST:event_onLoadHistoryClicked

    private void onClearSelectedClicked (ActionEvent evt) {//GEN-FIRST:event_onClearSelectedClicked
	((ExtendedList) logEL).removeSelectedItem ();
    }//GEN-LAST:event_onClearSelectedClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public final JButton clearHistoryBtn = new JButton();
    public final JButton clearSelectedBtn = new JButton();
    public final JCheckBox consoleCB = new JCheckBox();
    public final JPanel controlControlsPnl = new JPanel();
    public final JPanel controlPnl = new JPanel();
    public final JPanel historyControlPnl = new JPanel();
    public final JPanel historyPnl = new JPanel();
    public final JLabel infoLbl = new JLabel();
    public final JScrollPane infoSP = new JScrollPane();
    public final JTextArea infoTA = new JTextArea();
    public final JButton loadHistoryBtn = new JButton();
    public final JCheckBox logCB = new JCheckBox();
    public final JList logEL = new ExtendedList ();
    public final JScrollPane logSP = new JScrollPane();
    public final JPanel mainPnl = new JPanel();
    public final JCheckBox manualCheckCB = new JCheckBox();
    public final JProgressBar progressPB = new JProgressBar();
    public final JButton startPauseBtn = new JButton();
    // End of variables declaration//GEN-END:variables



    private void historyEnability (boolean b) {
	if (historyEnability == b)
	    return;

	historyPnl.setVisible (b);

	if ((historyEnability = b)) {
	    updateSize (bigSize);
	} else {
	    updateSize (smallSize);
	}
    }



    private void updateSize (Dimension d) {
	setSize (d);
	setPreferredSize (d);
	setMaximumSize (d);
    }
}
