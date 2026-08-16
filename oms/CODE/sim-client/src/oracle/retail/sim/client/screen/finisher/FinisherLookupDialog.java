package oracle.retail.sim.client.screen.finisher;

import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.source.Finisher;

/********************************************************************************************************
 * Finisher Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherLookupDialog extends RDialog implements REventListener, ChangeListener {
    private static final long serialVersionUID = -3278455081517809172L;

    private RTabbedPane tabbedPane = new RTabbedPane();
    private FinisherLookupTab lookupTab = new FinisherLookupTab();
    private FinisherDetailTab detailTab = new FinisherDetailTab();

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private SearchListener searchListener;

    private static final String FINISHER_ROW_SELECTED = "Finisher.rowSelected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FinisherLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Finisher Lookup");
        setSize(780, 590);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        lookupTab.registerAction(this, FINISHER_ROW_SELECTED);
    }

    private void layoutContent() {
        addButton(searchButton);
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        tabbedPane.addTab("Lookup", lookupTab);
        tabbedPane.addTab("Detail", detailTab);
        tabbedPane.setDoubleBuffered(true);
        tabbedPane.setSelectedIndex(0);
        tabbedPane.addChangeListener(this);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(mainPanel);

        validateEnabledState();
    }

    /****************************************************************************************************
     * Helper method to validate enabled state
     ***************************************************************************************************/

    private void validateEnabledState() {
        tabbedPane.setEnabledAt("Detail", lookupTab.isFinisherSelected());
    }

    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    /****************************************************************************************************
     * Handle Tab Change
     ***************************************************************************************************/

    public void stateChanged(ChangeEvent event) {
        RTab selectedTab = tabbedPane.getSelectedTab();
        try {
            if (selectedTab == detailTab) {
                doDetailTabSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doDetailTabSelected() throws Exception {
        Finisher finisher = lookupTab.getSelectedFinisher();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FINISHER, finisher);
        detailTab.loadTab();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                lookupTab.doReset();
            } else if (command.equals(SimNavigation.DIALOG_SEARCH)) {
                lookupTab.doSearch();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(FINISHER_ROW_SELECTED)) {
                validateEnabledState();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doApply() throws Exception {
        Finisher finisher = lookupTab.getSelectedFinisher();
        if (finisher == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        searchListener.assign(finisher);
        closeWindow();
    }

    private void doCancel() {
        closeWindow();
    }
}
