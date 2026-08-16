package oracle.retail.sim.client.screen.fulfillmentorder;

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.screen.quicksearch.TransactionLoaderUtility;
import oracle.retail.sim.client.swing.displayer.MediumDateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;


/********************************************************************************************************
 * Fulfillment Order Management List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderMgmtListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4294622538633506232L;

    private FulfillmentOrderMgmtListModel model = new FulfillmentOrderMgmtListModel();
    private FulfillmentOrderMgmtQueryFilter filter = new FulfillmentOrderMgmtQueryFilter();

    private static final String CUST_ORDER_MGMT_FILTER_SELECTED = "CustomerOrderMgmt.filterSelected";
    private static final String CUST_ORDER_SELECTED = "CustomerOrder.selected";
    private static final String SEARCH_LIMIT_MODIFIED = "Search Limit Modified";

    private SimFilterFieldEditor filterfieldEditor = new SimFilterFieldEditor();
    private FulfillmentOrderMgmtFilterDialog filterDialog = new FulfillmentOrderMgmtFilterDialog();

    private SimTable customerOrderTable = new SimTable(new CustomerOrderListDefinition());
    private SimTablePane customerOrderPane = new SimTablePane(customerOrderTable);
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public FulfillmentOrderMgmtListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterfieldEditor.registerAction(this, CUST_ORDER_MGMT_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        searchLimitEditor.setIdentifier(SimName.CUSTOMER_ORDER_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setRequired(true);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.registerAction(this, SEARCH_LIMIT_MODIFIED, KeyEvent.VK_ENTER);

        customerOrderTable.setTableEditable(false);
        customerOrderTable.setSingleRowSelectionMode();
        customerOrderTable.registerDoubleClickAction(this, CUST_ORDER_SELECTED);
    }

    private void layoutScreen() {

        REditorPanel searchLimitPanel = new REditorPanel(1);
        searchLimitPanel.add(searchLimitEditor);

        RPanel filterRightPanel = new RPanel(new GridBagLayout());
        filterRightPanel.add(searchLimitPanel, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.add(filterfieldEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        filterPanel.add(filterRightPanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(customerOrderPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        List<REditorPanel> panels = new ArrayList<>();
        panels.add(searchLimitPanel);

        LayoutUtility.alignPanels(panels);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return customerOrderTable;
    }

    /****************************************************************************************************
     * Initialize Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        filterfieldEditor.setText(model.getDescriptionMap());
        filter = model.getFilter();

        searchLimitEditor.setInteger(filter.getSearchLimit());

        populateScreen();
    }

    private void populateScreen() throws Exception {
        filterfieldEditor.setText(model.getDescriptionMap());
        searchLimitEditor.setInteger(model.getFilter().getSearchLimit());
        customerOrderTable.setRows(model.findCustomerOrderListVOs());

        if (customerOrderTable.getRowCount() < 1) {
            displayError(CommonMessageText.NO_RECORDS_FOUND);
        }
    }

    public void stop() {

    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(CUST_ORDER_SELECTED)) {
                doCustomerOrderSelected();
            } else if (command.equals(CUST_ORDER_MGMT_FILTER_SELECTED)) {
                doCustomerOrderMgmtFilterSelected();
            } else if (command.equals(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER)) {
                populateScreen();
            } else if (command.equals(SEARCH_LIMIT_MODIFIED)) {
                doSearchLimitModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSearchLimitModified() throws Exception {
        filter = model.getFilter();
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        populateScreen();
    }

    private void doCustomerOrderSelected() throws Exception {
        FulfillmentOrderMgmtListWrapper wrapper = (FulfillmentOrderMgmtListWrapper) customerOrderTable.getSelectedRowData();
        FulfillmentOrderTranType transactionType = wrapper.getFulfillmentOrderMgmtListVO().getTranType();
        if (transactionType == FulfillmentOrderTranType.DIRECT_DELIVERY) {
            TransactionLoaderUtility.loadDirectDelivery(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.DIRECT_DELIVERY_DETAIL_SCREEN);
        }
        if (transactionType == FulfillmentOrderTranType.TRANSFER) {
            TransactionLoaderUtility.loadTransfer(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.TRANSFER_VIEW_SCREEN);
        }
        if (transactionType == FulfillmentOrderTranType.WAREHOUSE_DELIVERY) {
            TransactionLoaderUtility.loadWarehouseDelivery(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.WAREHOUSE_DELIVERY_DETAIL_SCREEN);
        }

        if (transactionType == FulfillmentOrderTranType.CUSTOMER_ORDER) {
            TransactionLoaderUtility.loadCustomerOrder(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.FULFILLMENT_ORDER_DETAIL_SCREEN);
        }
        if (transactionType == FulfillmentOrderTranType.CUSTOMER_ORDER_DELIVERY) {
            TransactionLoaderUtility.loadCustomerOrderDelivery(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.FULFILLMENT_ORDER_DELIVERY_DETAIL_SCREEN);
        }
        if (transactionType == FulfillmentOrderTranType.REVERSE_PICK) {
            TransactionLoaderUtility.loadCustomerOrderReversePick(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.FULFILLMENT__ORDER_REVERSE_PICK_DETAIL_SCREEN);
        }
        if (transactionType == FulfillmentOrderTranType.PICK) {
            TransactionLoaderUtility.loadCustomerOrderPick(wrapper.getFulfillmentOrderMgmtListVO().getTranId());
            navigate(SimScreenName.FULFILLMENT_ORDER_PICK_DETAIL_SCREEN);
        }
    }

    private void doCustomerOrderMgmtFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    /****************************************************************************************************
     * Customer Order List Table Definition
     ***************************************************************************************************/

    private class CustomerOrderListDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FulfillmentOrderMgmtListWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<>(2);
            sortAttributes.add(new SimTableSortAttribute("tranDate", true));
            sortAttributes.add(new SimTableSortAttribute("customerOrderId", true));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("Tran Id", "tranId"));
            attributes.add(new SimTableAttribute("Tran Type", "tranType", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Customer Order ID", "customerOrderId"));
            attributes.add(new SimTableAttribute("From", "fromLocation"));
            attributes.add(new SimTableAttribute("To", "toLocation"));
            attributes.add(new SimTableAttribute("Date", "tranDate", new MediumDateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Tran Status", "tranStatus", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", "lineItemsCount"));
            attributes.add(new SimTableAttribute("User", "user"));
            return attributes;
        }
    }

}
