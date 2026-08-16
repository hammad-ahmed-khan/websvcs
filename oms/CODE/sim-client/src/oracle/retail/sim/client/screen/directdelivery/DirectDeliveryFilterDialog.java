package oracle.retail.sim.client.screen.directdelivery;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
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
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * This dialog handles entering the filter information for direct deliveries.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DirectDeliveryFilterDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 7293012148831144911L;

    private DirectDeliveryFilterDialogModel model = new DirectDeliveryFilterDialogModel();

    private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");
    private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");
    private RTextFieldEditor purchaseOrderExternalIdEditor = new RTextFieldEditor("PO Number");
    private RTextFieldEditor invoiceNumberEditor = new RTextFieldEditor("Invoice Number");
    private RTextFieldEditor customerOrderExternalIdEditor = new RTextFieldEditor("Customer Order ID");
    private RTextFieldEditor fulfillmentOrderExternalIdEditor = new RTextFieldEditor("Fulfillment Order ID");
    private RCheckBoxEditor anyCustomerOrderEditor = new RCheckBoxEditor("Customer Orders");
    private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor userEditor = new RComboBoxEditor("User");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public DirectDeliveryFilterDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Delivery List Filter");
        setSize(450, 400);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        fromDateEditor.setSizeType(EditorConstants.LARGE);
        toDateEditor.setSizeType(EditorConstants.LARGE);
        purchaseOrderExternalIdEditor.setIdentifier(SimName.PURCHASE_ORDER_EXTERNAL_ID);
        invoiceNumberEditor.setIdentifier(SimName.DIRECT_DELIVERY_INVOICE);
        customerOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
        fulfillmentOrderExternalIdEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
        anyCustomerOrderEditor.setSizeType(EditorConstants.SMALL);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
        userEditor.setEmptyType(RComboBoxEmptyType.ALL);
        supplierEditor.setSearchListener(buildSupplierSearchListener());

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

        REditorPanel miscFilterPanel = new REditorPanel(8);
        miscFilterPanel.setTitleBorder("Additional Filters");
        miscFilterPanel.add(purchaseOrderExternalIdEditor);
        if (model.isInvoiceEntryEnabled()) {
            miscFilterPanel.add(invoiceNumberEditor);
        }
        miscFilterPanel.add(customerOrderExternalIdEditor);
        miscFilterPanel.add(fulfillmentOrderExternalIdEditor);
        miscFilterPanel.add(anyCustomerOrderEditor);
        miscFilterPanel.add(supplierEditor);
        miscFilterPanel.add(statusEditor);
        miscFilterPanel.add(userEditor);

        RMatrixPanel mainPanel = new RMatrixPanel(2, 1);
        mainPanel.add(dateFilterPanel);
        mainPanel.add(miscFilterPanel);

        LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Filter To Dialog
     ***************************************************************************************************/

    public void setFilter(DirectDeliveryQueryFilter filter) throws Exception {
        model.setFilter(filter);

        fromDateEditor.setDate(filter.getFromDate());
        toDateEditor.setDate(filter.getToDate());
        purchaseOrderExternalIdEditor.setText(filter.getPurchaseOrderExternalId());
        invoiceNumberEditor.setText(filter.getInvoiceNumber());
        customerOrderExternalIdEditor.setText(filter.getCustomerOrderId());
        fulfillmentOrderExternalIdEditor.setText(filter.getFulfillmentOrderExternalId());
        anyCustomerOrderEditor.setSelected(filter.isAnyFulfillmentOrder());
        supplierEditor.setText(filter.getSupplierId());
        userEditor.setItems(model.findUserIds());
        userEditor.setSelectedItem(filter.getUserId());

        statusEditor.setItems(model.getDirectDeliveryStatusList());
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
    private void doReset() throws Exception {
        setFilter(model.resetFilter());
    }

    /****************************************************************************************************
     * Use Action
     ***************************************************************************************************/
    private void doApply() throws Exception {
        DirectDeliveryQueryFilter filter = model.getFilter();
        filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
        filter.setUserId((String) userEditor.getSelectedItem());
        filter.setPurchaseOrderExternalId(purchaseOrderExternalIdEditor.getTextOrNull());
        filter.setInvoiceNumber(invoiceNumberEditor.getTextOrNull());
        filter.setCustomerOrderId(customerOrderExternalIdEditor.getTextOrNull());
        filter.setFulfillmentOrderExternalId(fulfillmentOrderExternalIdEditor.getTextOrNull());
        filter.setAnyFulfillmentOrder(anyCustomerOrderEditor.isSelected());
        filter.setStatus((DirectDeliveryStatus) statusEditor.getSelectedItem());
        Supplier supplier = (Supplier) supplierEditor.getData();
        filter.setSupplierId(supplier != null ? supplier.getId() : null);

        RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_FILTER, filter);
        notifyREventListeners(new RActionEvent(this, SimClientStateKey.DIRECT_DELIVERY_FILTER_MODIFIED, filter));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
