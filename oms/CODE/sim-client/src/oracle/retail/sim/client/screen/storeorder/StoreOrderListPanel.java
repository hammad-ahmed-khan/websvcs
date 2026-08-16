package oracle.retail.sim.client.screen.storeorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.ExceptionUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Orders Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 8118692246468373054L;

    private StoreOrderListModel model = new StoreOrderListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable ordersTable = new SimTable(new StoreOrderDefinition());
    private SimTablePane ordersPane = new SimTablePane(ordersTable);

    private StoreOrderFilterDialog filterDialog = new StoreOrderFilterDialog();

    private static final String STORE_ORDER_SELECTED = "StoreOrder.selected";
    private static final String STORE_ORDER_FILTER_SELECTED = "StoreOrder.filterSelected";

    public StoreOrderListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, STORE_ORDER_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        ordersTable.setTableEditable(false);
        ordersTable.registerDoubleClickAction(this, STORE_ORDER_SELECTED);
    }

    private void layoutScreen() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(ordersPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return ordersTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (ordersTable.getSelectedRowCount() == 0) {
            displayError(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return;
        }
        model.printStoreOrders(ordersTable.getAllSelectedRowData());
    }

    /****************************************************************************************************
     * Handle Create Order
     ***************************************************************************************************/

    public boolean handleCreateOrder() {
        if (isExternalConnectionValid()) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_DETAIL);
            RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_CREATE_NEW, "");
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleCancelOrder() {
        if (ordersTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }

        List<StoreOrderWrapper> orders = ordersTable.getAllSelectedRowData();
        for (StoreOrderWrapper wrapper : orders) {
            if (!wrapper.getStoreOrder().getStatus().equals(StoreOrderStatus.PENDING)) {
                displayError(StoreOrderMessageText.INVALID_DELETE_STATUS);
                return;
            }
        }
        if (!isExternalConnectionValid()) {
            return;
        }
        if (RConfirmUtility.confirm("Store Order Delete Confirmation", StoreOrderMessageText.STORE_ORDER_DELETE_CONFIRM)) {
            for (StoreOrderWrapper wrapper : orders) {
                try {
                    model.deleteStore(wrapper);
                    ordersTable.removeRow(wrapper);
                } catch (Throwable exception) {
                    displayError(StoreOrderMessageText.DELETE_FAILED);
                    return;
                }
            }
        }
    }

    /****************************************************************************************************
     * Helper Method
     ***************************************************************************************************/

    private boolean isExternalConnectionValid() {
        try {
            ClientServiceFactory.getStoreOrderServices().pingExternalService("test");
        } catch (Throwable exception) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_ORDER_FILTER);
            displayException(exception);
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(STORE_ORDER_SELECTED)) {
                doCreateStoreOrder();
            } else if (command.equals(STORE_ORDER_FILTER_SELECTED)) {
                doStoreOrderFilterSelected();
            } else if (command.equals(SimClientStateKey.STORE_ORDER_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doCreateStoreOrder() {
        model.persistStoreOrder((StoreOrderWrapper) ordersTable.getSelectedRowData());
        if (RepositoryManager.getStateObject(SimClientStateKey.STORE_ORDER_ORIGIN) == null) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_FROM_HOME_ORIGIN, "");
        }
        navigate(SimScreenName.STORE_ORDER_DETAIL_SCREEN);
    }

    private void doStoreOrderFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() {
        filterEditor.setText(model.getDescriptionMap());
        try {
            ordersTable.setRows(model.findStoreOrders());
        } catch (Throwable exception) {
            displayException(ExceptionUtility.getOriginalCause(exception));
        }
    }

    /****************************************************************************************************
     * Store Order Table Definition
     ***************************************************************************************************/

    private class StoreOrderDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return StoreOrderWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("storeOrderNumber"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Order ID", "storeOrderNumber"));
            attributes.add(new SimTableAttribute("Source", "fromLocation.name"));
            attributes.add(new SimTableAttribute("Date", "creationDate"));
            attributes.add(new SimTableAttribute("Status", "statusDescription", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Not Before Date", "notBeforeDate"));
            attributes.add(new SimTableAttribute("Not After Date", "notAfterDate"));
            attributes.add(new SimTableAttribute("User", "creationUser"));
            return attributes;
        }
    }
}
