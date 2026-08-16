package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.IdNameDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.finisher.FinisherSearchListener;
import oracle.retail.sim.client.screen.itemprice.PromotionSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldRangeUtility;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RMatrixPanel;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.ContextTypeComparator;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseComparator;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/********************************************************************************************************
 * This dialog handles entering the filter information for warehouse deliveries.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class WarehouseDeliveryFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -1330501262453341545L;

    private static final String WAREHOUSE_MODIFIED = "Warehouse.modified";

    private WarehouseDeliveryFilterDialogModel model = new WarehouseDeliveryFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");
    private RTextFieldEditor asnIdEditor = new RTextFieldEditor("ASN ID");
    private RTextFieldEditor customerOrderExternalIdEditor = new RTextFieldEditor("Customer Order ID");
    private RTextFieldEditor fulfillmentOrderExternalIdEditor = new RTextFieldEditor("Fulfillment Order ID");
    private RCheckBoxEditor anyCustomerOrderEditor = new RCheckBoxEditor("Customer Orders");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor warehouseEditor = new RComboBoxEditor("From Warehouse");
    private RSearchFieldEditor finisherEditor = SimEditorFactory.createFinisherSearchFieldEditor("From Finisher");
    private RComboBoxEditor contextTypeEditor = new RComboBoxEditor("Context Type");
    private RSearchFieldEditor contextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public WarehouseDeliveryFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Warehouse Delivery List Filter");
        setSize(450, 425);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);
        asnIdEditor.setSizeType(EditorConstants.LARGE);
        asnIdEditor.setIdentifier(SimName.WAREHOUSE_DELIVERY_ASN);
        customerOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        fulfillmentOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        anyCustomerOrderEditor.setSizeType(EditorConstants.SMALL);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        warehouseEditor.setDisplayer(new IdNameDisplayer());
        warehouseEditor.setEmptyType(RComboBoxEmptyType.ALL);
        warehouseEditor.setComparator(WarehouseComparator.getInstance());
        warehouseEditor.registerAction(this, WAREHOUSE_MODIFIED);
        finisherEditor.setSearchListener(buildFinisherSearchListener());
        contextTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        contextTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
        contextTypeEditor.setComparator(ContextTypeComparator.getInstance());
        contextValueEditor.setSearchListener(buildPromotionSearchListener());

        RDateFieldRangeUtility.setDateRangeEditors(fromDateEditor, toDateEditor);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        REditorPanel dateFilterPanel = new REditorPanel(2);
        dateFilterPanel.setTitleBorder("Date Filters");
        dateFilterPanel.add(fromDateEditor);
        dateFilterPanel.add(toDateEditor);

        REditorPanel miscFilterPanel = new REditorPanel(9);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(asnIdEditor);
        miscFilterPanel.add(customerOrderExternalIdEditor);
        miscFilterPanel.add(fulfillmentOrderExternalIdEditor);
        miscFilterPanel.add(anyCustomerOrderEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(warehouseEditor);
        if (model.isFinishersEnabled()) {
            miscFilterPanel.add(finisherEditor);
        }
        miscFilterPanel.add(contextTypeEditor);
        miscFilterPanel.add(contextValueEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(WarehouseDeliveryQueryFilter filter) throws Exception {
        model.setFilter(filter);

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        asnIdEditor.setText(filter.getAsnId());
        customerOrderExternalIdEditor.setText(filter.getCustomerOrderId());
        fulfillmentOrderExternalIdEditor.setText(filter.getFulfillmentOrderExternalId());
        anyCustomerOrderEditor.setSelected(filter.isAnyFulfillmentOrder());
        statusEditor.setItems(model.getWarehouseDeliveryStatusList());
        statusEditor.setSelectedItem(filter.getStatus());

        List<Warehouse> warehouses = model.findAllWarehouses();
        warehouseEditor.setActionsEnabled(false);
        warehouseEditor.setItems(warehouses);
        warehouseEditor.setEmptySelection();
        warehouseEditor.setEnabled(true);
        warehouseEditor.setActionsEnabled(true);

        finisherEditor.clear();
        finisherEditor.setEnabled(true);

        if (filter.getSourceType() == SourceType.WAREHOUSE) {
            warehouseEditor.setActionsEnabled(false);
            warehouseEditor.setSelectedItem(model.getWarehouse(filter.getSourceId()));
            warehouseEditor.setActionsEnabled(true);
            doWarehouseModified();
        } else if (filter.getSourceType() == SourceType.FINISHER && model.isFinishersEnabled()) {
            if (filter.getSourceId() != null) {
                finisherEditor.setText(filter.getSourceId());
            }
            warehouseEditor.setEnabled(false);
        }

        contextTypeEditor.setItems(model.findAllContextTypes());
        contextTypeEditor.setSelectedItem(filter.getContextType());
        contextValueEditor.setText(filter.getContextValue());

        setDefaultButton(applyButton);
    }

    private FinisherSearchListener buildFinisherSearchListener() {
        return new FinisherSearchListener() {
            public void assignFinisher(Finisher finisher) {
                if (finisher != null) {
                    finisherEditor.setData(finisher);
                    warehouseEditor.setEmptySelection();
                    warehouseEditor.setEnabled(false);
                }
            }
        };
    }

    private PromotionSearchListener buildPromotionSearchListener() {
        return new PromotionSearchListener() {
            public void assignPromotion(PromotionVO promotion) {
                if (promotion != null) {
                    contextValueEditor.setData(promotion);
                }
            }
        };
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(WAREHOUSE_MODIFIED)) {
                doWarehouseModified();
            } else if (command.equals(SimNavigation.DIALOG_RESET)) {
                doReset();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Warehouse Or Finisher Modified
     ***************************************************************************************************/

    private void doWarehouseModified() {
        if (model.isFinishersEnabled() && warehouseEditor.getSelectedItem() != null) {
            finisherEditor.clear();
            finisherEditor.setEnabled(false);
        }
    }

    /****************************************************************************************************
     * Reset Action
     ***************************************************************************************************/
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        WarehouseDeliveryQueryFilter filter = model.getFilter();
        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setAsnId(asnIdEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderExternalIdEditor.getTextOrNull());
        filter.setFulfillmentOrderExternalId(fulfillmentOrderExternalIdEditor.getTextOrNull());
        filter.setAnyFulfillmentOrder(anyCustomerOrderEditor.isSelected());
        filter.setSourceId(null); //Clear source id and type up here, they will be set if need be further down
        filter.setSourceType(null);
        filter.setStatus((WarehouseDeliveryStatus) statusEditor.getSelectedItem());

        Warehouse warehouse = (Warehouse) warehouseEditor.getSelectedItem();
        if (warehouse != null) {
            filter.setSourceId(warehouse.getId());
            filter.setSourceType(SourceType.WAREHOUSE);
        }

        if (model.isFinishersEnabled()) {
            Finisher finisher = (Finisher) finisherEditor.getData();
            if (finisher != null) {
                filter.setSourceId(finisher.getId());
                filter.setSourceType(SourceType.FINISHER);
            }
        }
        filter.setContextType((ContextType) contextTypeEditor.getSelectedItem());

        PromotionVO promotionVO = (PromotionVO) contextValueEditor.getData();
        filter.setContextValue(promotionVO != null ? promotionVO.getId() : null);

        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.WAREHOUSE_DELIVERY_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
