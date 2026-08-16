package oracle.retail.sim.client.screen.tranhistory;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.screen.quicksearch.TransactionLoaderUtility;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
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
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.tranhistory.TransactionHistoryMessageText;
import oracle.retail.sim.common.tranhistory.TransactionHistoryProperty;

/********************************************************************************************************
 * Transaction History List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 770468453805636664L;

    private TransactionHistoryListModel model = new TransactionHistoryListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable historyTable = new SimTable(new TransactionHistoryTableDefinition());
    private SimTablePane historyPane = new SimTablePane(historyTable);

    private TransactionHistoryFilterDialog filterDialog = new TransactionHistoryFilterDialog();

    private static final String HISTORY_FILTER_SELECTED = "TransactionHistory.filterSelected";
    private static final String HISTORY_RECORD_SELECTED = "TransactionHistory.recordSelected";

    public TransactionHistoryListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, HISTORY_FILTER_SELECTED);
        filterDialog.addREventListener(this);
        historyTable.setTableEditable(false);

        historyTable.setColumnSize(TransactionHistoryProperty.TYPE, EditorConstants.COLUMN_LABEL_WIDTH);
        historyTable.setColumnSize(TransactionHistoryProperty.NON_SELLABLE_QTY, EditorConstants.COLUMN_LABEL_WIDTH);
        historyTable.registerDoubleClickAction(this, HISTORY_RECORD_SELECTED);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(historyPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return historyTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        doHistoryFilterSelected();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(HISTORY_RECORD_SELECTED)) {
                doHistoryRecordSelected();
            } else if (command.equals(HISTORY_FILTER_SELECTED)) {
                doHistoryFilterSelected();
            } else if (command.equals(SimClientStateKey.TRANSACTION_HISTORY_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doHistoryRecordSelected() throws Exception {
        TransactionHistoryVOWrapper wrapper = (TransactionHistoryVOWrapper) historyTable.getSelectedRowData();
        switch (wrapper.getType()) {
            case DIRECT_DELIVERY:
                displayDirectDelivery(wrapper);
                break;
            case STOCK_COUNT:
                displayStockCount(wrapper);
                break;
            case STORE_TRANSFER:
                displayTransfer(wrapper);
                break;
            case WAREHOUSE_DELIVERY:
                displayWarehouseDelivery(wrapper);
                break;
            case RETURN_TO_FINISHER:
            case RETURN_TO_WAREHOUSE:
            case RETURN_TO_VENDOR:
                displayReturn(wrapper);
                break;
            case INVENTORY_ADJUSTMENT:
                displayInventoryAdjustment(wrapper);
                break;
            case FULFILLMENT_ORDER:
                displayFulfillmentOrderDelivery(wrapper);
                break;
            default:
                displayError(TransactionHistoryMessageText.MISSING_TRANSACTION);
        }
    }

    private void doHistoryFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        historyTable.setRows(model.findTransactionHistoryRecords());
    }

    /****************************************************************************************************
     * Transaction Navigation Methods
     ***************************************************************************************************/

    private void displayDirectDelivery(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadDirectDelivery(wrapper.getTransactionId());
        navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
    }

    private void displayFulfillmentOrderDelivery(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadCustomerOrderDelivery(wrapper.getTransactionId());
        navigate(SimScreenName.FULFILLMENT_ORDER_DELIVERY_DETAIL_SCREEN);
    }

    private void displayInventoryAdjustment(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadInventoryAdjustment(wrapper.getTransactionId());
        navigate(SimScreenName.INVENTORY_ADJUSTMENT_DETAIL_SCREEN);
    }

    private void displayReturn(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadReturn(wrapper.getTransactionId());
        navigate(SimScreenName.RETURN_DETAIL_SCREEN);
    }

    private void displayStockCount(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadStockCount(wrapper.getTransactionId());
        navigate(SimScreenName.STOCK_COUNT_LOCATION_SCREEN);
    }

    private void displayTransfer(TransactionHistoryVOWrapper wrapper) throws Exception {
        navigate(TransactionLoaderUtility.loadTransfer(wrapper.getTransactionId()));
    }

    private void displayWarehouseDelivery(TransactionHistoryVOWrapper wrapper) throws Exception {
        TransactionLoaderUtility.loadWarehouseDelivery(wrapper.getTransactionId());
        navigate(SimScreenName.WAREHOUSE_DELIVERY_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * Return Table Definition
     ***************************************************************************************************/

    public class TransactionHistoryTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TransactionHistoryVOWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(TransactionHistoryProperty.TIMESTAMP, false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("Date", TransactionHistoryProperty.TIMESTAMP, new DateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Transaction Type", TransactionHistoryProperty.TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Tran ID", TransactionHistoryProperty.TRAN_ID));
            attributes.add(new SimTableAttribute("Item", TransactionHistoryProperty.ITEM_ID));
            attributes.add(new SimTableAttribute("Description", TransactionHistoryProperty.ITEM_DESC));
            attributes.add(new SimTableAttribute("Reason", TransactionHistoryProperty.TRAN_DESC));
            attributes.add(new SimTableAttribute("SOH", TransactionHistoryProperty.STOCK_ON_HAND_QTY));
            attributes.add(new SimTableAttribute("Unavailable", TransactionHistoryProperty.NON_SELLABLE_QTY));
            attributes.add(new SimTableAttribute("User", TransactionHistoryProperty.USERNAME));
            return attributes;
        }
    }
}
