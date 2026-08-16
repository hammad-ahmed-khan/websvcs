package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
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
import oracle.retail.sim.common.directdelivery.PurchaseOrderQueryFilter;
import oracle.retail.sim.common.directdelivery.PurchaseOrderStatus;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * This dialog handles entering the filter information for purchase orders.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -7703115229008858569L;

    private PurchaseOrderFilterDialogModel model = new PurchaseOrderFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");
    private RTextFieldEditor purchaseOrderExternalIdEditor = new RTextFieldEditor("PO Number");
    private RTextFieldEditor customerOrderExternalIdEditor = new RTextFieldEditor("Customer Order ID");
    private RTextFieldEditor fulfillmentOrderExternalIdEditor = new RTextFieldEditor("Fulfillment Order ID");
    private RCheckBoxEditor anyCustomerOrderEditor = new RCheckBoxEditor("Customer Orders");
    private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public PurchaseOrderFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Purchase Order List Filter");
        setSize(450, 375);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);
        purchaseOrderExternalIdEditor.setIdentifier(SimName.PURCHASE_ORDER_EXTERNAL_ID);
        customerOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        fulfillmentOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        anyCustomerOrderEditor.setSizeType(EditorConstants.SMALL);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        supplierEditor.setSearchListener(buildSupplierSearchListener());
        itemEditor.setSearchListener(buildItemSearchListener());

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

        REditorPanel miscFilterPanel = new REditorPanel(7);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(purchaseOrderExternalIdEditor);
        miscFilterPanel.add(customerOrderExternalIdEditor);
        miscFilterPanel.add(fulfillmentOrderExternalIdEditor);
        miscFilterPanel.add(anyCustomerOrderEditor);
        miscFilterPanel.add(supplierEditor);
        miscFilterPanel.add(itemEditor);
        miscFilterPanel.add(statusEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(PurchaseOrderQueryFilter filter) {
        model.setFilter(filter);

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        purchaseOrderExternalIdEditor.setText(filter.getExternalId());
        customerOrderExternalIdEditor.setText(filter.getCustomerOrderId());
        fulfillmentOrderExternalIdEditor.setText(filter.getFulfillmentOrderExternalId());
        anyCustomerOrderEditor.setSelected(filter.isAnyFulfillmentOrder());
        supplierEditor.setText(filter.getSupplierId());
        itemEditor.setText(filter.getItemId());

        statusEditor.setItems(model.getPurchaseOrderStatusList());
        statusEditor.setSelectedItem(filter.getStatus());
        setDefaultButton(applyButton);
    }

    /****************************************************************************************************
     * Supplier Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                if (supplier != null) {
                    supplierEditor.setData(supplier);
                }
            }
        };
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
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
            if (command.equals(SimNavigation.DIALOG_RESET)) {
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
     * Reset Action
     ***************************************************************************************************/
    private void doReset() {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        PurchaseOrderQueryFilter filter = model.getFilter();
        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setStatus((PurchaseOrderStatus) statusEditor.getSelectedItem());
        Supplier supplier = (Supplier) supplierEditor.getData();
        filter.setSupplierId(supplier != null ? supplier.getId() : null);
        ItemVO item = (ItemVO) itemEditor.getData();
        filter.setItemId(item != null ? item.getId() : null);
        filter.setExternalId(purchaseOrderExternalIdEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderExternalIdEditor.getTextOrNull());
        filter.setFulfillmentOrderExternalId(fulfillmentOrderExternalIdEditor.getTextOrNull());
        filter.setAnyFulfillmentOrder(anyCustomerOrderEditor.isSelected());

        RepositoryManager.addStateObject(SimClientStateKey.PURCHASE_ORDER_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.PURCHASE_ORDER_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
