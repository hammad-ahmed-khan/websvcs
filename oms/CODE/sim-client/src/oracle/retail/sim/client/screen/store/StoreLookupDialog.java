package oracle.retail.sim.client.screen.store;

import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Store Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreLookupDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -8377834951830198040L;

    private final StoreLookupDialogModel model = new StoreLookupDialogModel();

    private final RNumericIdEditor storeIdEditor = new RNumericIdEditor("Store ID", "Store");
    private final RTextFieldEditor storeNameEditor = new RTextFieldEditor("Store Name");

    private final SimTable storeTable = new SimTable(new StoreDefinition());
    private final SimTablePane storePane = new SimTablePane(storeTable);

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private SearchListener searchListener;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StoreLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Store Lookup");
        setSize(600, 400);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        storeIdEditor.setIdentifier(SimName.STORE_ID);
        storeNameEditor.setIdentifier(SimName.STORE_NAME);
        storeTable.setTableEditable(false);

        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(searchButton);
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel topPanel = new REditorPanel(1, 2);
        topPanel.add(storeIdEditor);
        topPanel.add(storeNameEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(storePane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Helper method to validate enabled state
     ***************************************************************************************************/

    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_SEARCH)) {
                doSearch();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doReset() {
        storeIdEditor.clear();
        storeNameEditor.clear();
        storeTable.clearRows();
    }

    private void doSearch() throws Exception {
        storeTable.clearRows();
        List<Store> stores = Collections.emptyList();
        if (!storeIdEditor.isEmpty()) {
            stores = model.findStoresById(Long.parseLong(storeIdEditor.getText()));
        } else if (!storeNameEditor.isEmpty()) {
            stores = model.findStoresByName(storeNameEditor.getText());
        } else {
            stores = model.findStoresByName(StringConstants.EMPTY);
        }
        if (stores.isEmpty()) {
            displayMessage(CommonMessageText.NO_RECORDS_FOUND);
            storeIdEditor.requestFocusInWindow();
            return;
        }
        storeTable.setRows(stores);
    }

    private void doApply() throws Exception {
        Store store = (Store) storeTable.getSelectedRowData();
        if (store == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        searchListener.assign(store);
        closeWindow();
    }

    private void doCancel() {
        closeWindow();
    }

    /**
     * Closes the Store Lookup Dialog Box
     */
    public void windowClosing(WindowEvent event) {
        closeWindow();
    }

    /****************************************************************************************************
     * Store Definition
     ***************************************************************************************************/

    public class StoreDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return Store.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(2);
            attributes.add(new SimTableAttribute("Store ID", "id"));
            attributes.add(new SimTableAttribute("Store Name", "name"));
            return attributes;
        }
    }
}
