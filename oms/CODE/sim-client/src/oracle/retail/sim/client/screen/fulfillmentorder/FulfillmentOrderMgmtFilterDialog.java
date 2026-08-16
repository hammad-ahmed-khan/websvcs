package oracle.retail.sim.client.screen.fulfillmentorder;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;

/********************************************************************************************************
 * Fulfillment Order Management Filter Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderMgmtFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -4032604723271357715L;

    private static final String TRAN_TYPE_SELECTED = "TranTypeSelected";

    private FulfillmentOrderMgmtFilterDialogModel model = new FulfillmentOrderMgmtFilterDialogModel();

    private RDateFieldEditor fromTranDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toTranDateEditor = new RDateFieldEditor("To Date");

    private RTextFieldEditor customerOrderIdEditor = new RTextFieldEditor("Customer Order ID");
    private RComboBoxEditor tranTypeEditor = new RComboBoxEditor("Tran Type");
    private RComboBoxEditor tranStatusEditor = new RComboBoxEditor("Tran Status");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RTextFieldEditor tranIdEditor = new RTextFieldEditor("Tran ID");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);

    private RComboBoxEditor carrierEditor = new RComboBoxEditor("Carrier");
    private RComboBoxEditor serviceEditor = new RComboBoxEditor("Service");

    private RTextFieldEditor userIdEditor = new RTextFieldEditor("User");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    public FulfillmentOrderMgmtFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Customer Order Management Filter");
        setSize(450, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        fromTranDateEditor.setSizeType(EditorConstants.LARGE);
        toTranDateEditor.setSizeType(EditorConstants.LARGE);
        tranTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        tranStatusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        carrierEditor.setDisplayer(new TranslatedObjectDisplayer());
        serviceEditor.setDisplayer(new TranslatedObjectDisplayer());

        customerOrderIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        tranIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        searchLimitEditor.setIdentifier(SimName.CUSTOMER_ORDER_SEARCH_LIMIT);
        carrierEditor.setIdentifier(SimName.CUSTOMER_ORDER_DELIVERY_CARRIER);
        serviceEditor.setIdentifier(SimName.CUSTOMER_ORDER_DELIVERY_SERVICE);
        userIdEditor.setIdentifier(SimName.USER_ID);
        itemEditor.setSearchListener(buildItemSearchListener());

        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        customerOrderIdEditor.setSizeType(EditorConstants.LARGE);
        customerOrderIdEditor.setLength(128);
        tranIdEditor.setSizeType(EditorConstants.MEDIUM);

        tranStatusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        tranTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        tranTypeEditor.registerAction(this, TRAN_TYPE_SELECTED);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(3);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(fromTranDateEditor);
        dateFilterPanel.add(toTranDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(10);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(customerOrderIdEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(tranTypeEditor);
        miscFilterPanel.add(tranStatusEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(userIdEditor);
        miscFilterPanel.add(tranIdEditor);
        miscFilterPanel.add(carrierEditor);
        miscFilterPanel.add(serviceEditor);
        miscFilterPanel.add(searchLimitEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);
        setContentPane(mainPanel);
    }

    public void setFilter(FulfillmentOrderMgmtQueryFilter filter) throws Exception {
        model.setFilter(filter);
        tranTypeEditor.setItems(model.findFulfillmentOrderTranType());
        tranTypeEditor.setSelectedItem(filter.getTranType());
        statusEditor.setItems(model.findStatusList());
        statusEditor.setSelectedItem(filter.getStatus());
        tranStatusEditor.setSelectedItem(model.getTranStatus());
        fromTranDateEditor.setDate(filter.getFromDate());
        toTranDateEditor.setDate(filter.getToDate());
        searchLimitEditor.setInteger(filter.getSearchLimit());
        itemEditor.setData(model.loadItem());
        userIdEditor.setText(filter.getUserId());
        carrierEditor.setItems(model.findShipmentCarriers());
        carrierEditor.setSelectedItem(filter.getShipmentCarrier());
        serviceEditor.setItems(model.findShipmentCarrierServices());
        serviceEditor.setSelectedItem(filter.getShipmentCarrierService());

        if (filter.getCustomerOrderId() != null) {
            customerOrderIdEditor.setText(filter.getCustomerOrderId());
        } else {
            customerOrderIdEditor.clear();
        }
        if (filter.getTranId() != null) {
            tranIdEditor.setText(filter.getTranId());
        } else {
            tranIdEditor.clear();
        }
        setDefaultButton(applyButton);
    }

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            } else if (command.equals(TRAN_TYPE_SELECTED)) {
                doTranTypeSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doTranTypeSelected() {
        FulfillmentOrderTranType transactionType = (FulfillmentOrderTranType) tranTypeEditor.getSelectedItem();
        if (transactionType == null) {
            tranStatusEditor.clear();
            return;
        }
        tranStatusEditor.setItems(model.findTranStatusList(transactionType));
    }

    private void doApply() throws Exception {
        FulfillmentOrderMgmtQueryFilter filter = model.getFilter();
        filter.setDateRange(fromTranDateEditor.getDateAtStartOfDay(), toTranDateEditor.getDateAtEndOfDay());
        filter.setTranType((FulfillmentOrderTranType) tranTypeEditor.getSelectedItem());

        ItemVO item = (ItemVO) itemEditor.getData();
        if (item != null) {
            filter.setItemId(item.getId());
        } else {
            filter.setItemId(null);
        }

        SimEnum<Integer> tranStatus = (SimEnum<Integer>) tranStatusEditor.getSelectedItem();
        if (tranStatus != null) {
            filter.setTranStatusCode(tranStatus.getCode());
        } else {
            filter.setTranStatusCode(null);
        }
        filter.setStatus((FulfillmentOrderMgmtQueryStatus) statusEditor.getSelectedItem());
        filter.setTranId(tranIdEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderIdEditor.getTextOrNull());
        filter.doSetStoreId(SimRepository.getStoreId());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        filter.setShipmentCarrier((ShipmentCarrier) carrierEditor.getSelectedItem());
        filter.setShipmentCarrierService((ShipmentCarrierService) serviceEditor.getSelectedItem());
        filter.setUserId(userIdEditor.getTextOrNull());
        
        if (item != null) {
            RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MGMT_FILTER_ITEM_VO, item);
        } else {
            RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_MGMT_FILTER_ITEM_VO);
        }

        RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.FULFILLMENT_ORDER_MANAGEMENT_FILTER, filter));
        closeWindow();
    }

    private void doReset() throws Exception {
        fromTranDateEditor.clear();
        toTranDateEditor.clear();
        tranTypeEditor.clear();
        tranStatusEditor.clear();
        searchLimitEditor.clear();
        tranIdEditor.clear();
        customerOrderIdEditor.clear();
        carrierEditor.clear();
        serviceEditor.clear();
        userIdEditor.clear();
        itemEditor.clear();
        setFilter(model.resetFilter());
    }

    private void doCancel() {
        closeWindow();
    }
}
