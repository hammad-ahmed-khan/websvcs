package oracle.retail.sim.client.screen.supplier;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Supplier Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupDialog extends RDialog implements REventListener, ChangeListener {
    private static final long serialVersionUID = -8377834951830198040L;

    private static final String SUPPLIER_ROW_SELECTED = "Supplier.rowSelected";

    private RTabbedPane tabbedPane = new RTabbedPane();
    private SupplierLookupTab lookupTab = new SupplierLookupTab();
    private SupplierDetailTab detailTab = new SupplierDetailTab();

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private SearchListener searchListener;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public SupplierLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Supplier Lookup");
        setSize(700, 500);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        lookupTab.registerAction(this, SUPPLIER_ROW_SELECTED);
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
        tabbedPane.setEnabledAt("Detail", lookupTab.isSupplierSelected());
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
            if (selectedTab.equals(detailTab)) {
                detailTab.loadTab(lookupTab.getSelectedSupplier());
            }
        } catch (Throwable t) {
            displayException(t);
        }
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
            } else if (command.equals(SUPPLIER_ROW_SELECTED)) {
                validateEnabledState();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void doApply() throws Exception {
        Supplier supplier = lookupTab.getSelectedSupplier();
        if (supplier == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        searchListener.assign(supplier);
        closeWindow();
    }

    private void doCancel() {
        closeWindow();
    }

    public void windowClosing(WindowEvent event) {
        closeWindow();
    }
}
