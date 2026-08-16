package oracle.retail.sim.client.swing.tableconfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTabbedPane;

/********************************************************************************************************
 * Controls table configuration information for tables. It is launched from the popup menu on the
 * SimTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RTableConfigDialog extends RDialog implements REventListener, ChangeListener {
    private static final long serialVersionUID = -3987918607599740650L;

    private RTabbedPane tabbedPane = new RTabbedPane();

    private RTableConfigColumnTab columnTab = new RTableConfigColumnTab("Columns");
    private RTableConfigSortTab sortTab = new RTableConfigSortTab("Sort");
    private RTableConfigGeneralTab generalTab = new RTableConfigGeneralTab("General");

    private static final String APPLY = "Apply";
    private static final String CANCEL = "Cancel";

    private RButton applyButton = new RButton(APPLY);
    private RButton cancelButton = new RButton(CANCEL);

    private RButton[] columnTabButtons;
    private RButton[] sortTabButtons;

    /****************************************************************************************************
     * Constructor.
     * @param frame The parent frame the dialog will belong to.
     ***************************************************************************************************/
    public RTableConfigDialog(JFrame frame) {
        super(frame, true);
        initConfigDialog();
        layoutConfigDialog();
    }

    /****************************************************************************************************
     * Constructor.
     * @param dialog The parent dialog this dialog will belong to.
     ***************************************************************************************************/
    public RTableConfigDialog(JDialog dialog) {
        super(dialog, true);
        initConfigDialog();
        layoutConfigDialog();
    }

    /****************************************************************************************************
     * Initializes all the components of the dialog.
     ***************************************************************************************************/
    private void initConfigDialog() {
        setTitle("Table Configuration");
        setSize(700, 450);
        setStatusBarVisible(false);

        columnTabButtons = columnTab.getButtons();
        sortTabButtons = sortTab.getButtons();

        tabbedPane.addChangeListener(this);

        applyButton.registerAction(this, APPLY);
        cancelButton.registerAction(this, CANCEL);
    }

    /****************************************************************************************************
     * Lays out all the components of the dialog.
     ***************************************************************************************************/
    private void layoutConfigDialog() {
        for (RButton columnTabButton : columnTabButtons) {
            addButton(columnTabButton);
        }
        for (RButton sortTabButton : sortTabButtons) {
            addButton(sortTabButton);
        }
        addButton(applyButton);
        addButton(cancelButton);

        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.addTab(columnTab);
        tabbedPane.addTab(sortTab);
        tabbedPane.addTab(generalTab);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        centerOnOwner();
    }

    /****************************************************************************************************
     * Initializes the table configuration dialog.
     * <p>
     * @param identifier The table identifier.
     * @param columnTitles The table column titles.
     ***************************************************************************************************/
    public void initialize(String identifier, List<String> columnTitles) {
        columnTab.loadConfigSettings(identifier, columnTitles);
        sortTab.loadConfigSettings(identifier);
        generalTab.loadConfigSettings(identifier);
    }

    /****************************************************************************************************
     * Implements the listener that receives action events when the buttons are pressed at a dialog
     * level.
     * <p>
     * @param event The event that took place.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(CANCEL)) {
            closeWindow();
        } else if (command.equals(APPLY)) {
            applyConfiguration();
        }
    }

    /****************************************************************************************************
     * Implement the change listener to handle display the correct buttons when the tab changes as well
     * as reloading sort information.
     ***************************************************************************************************/
    public void stateChanged(ChangeEvent event) {
        setColumnButtonsVisible(tabbedPane.getSelectedTab() == columnTab);
        setSortButtonsVisible(tabbedPane.getSelectedTab() == sortTab);
        if (tabbedPane.getSelectedTab() == sortTab) {
            sortTab.reloadSortInformation();
        }
    }

    /****************************************************************************************************
     * Assigns the visiblity of the column data related buttons.
     ***************************************************************************************************/
    private void setColumnButtonsVisible(boolean visible) {
        for (RButton columnTabButton : columnTabButtons) {
            columnTabButton.setVisible(visible);
        }
    }

    /****************************************************************************************************
     * Assigns the visiblity of the sort data related buttons
     ***************************************************************************************************/
    private void setSortButtonsVisible(boolean visible) {
        for (RButton sortTabButton : sortTabButtons) {
            sortTabButton.setVisible(visible);
        }
    }

    /****************************************************************************************************
     * Saves the configuration information. The sort information is always kept in sync so that saving is
     * not required.
     ***************************************************************************************************/
    private void applyConfiguration() {
        columnTab.saveConfigSettings();
        generalTab.saveConfigSettings();
        closeWindow();
        firePropertyChange(UIPropertyName.TABLE_CONFIGURATION_ALTERED, Boolean.FALSE, Boolean.TRUE);
    }
}
