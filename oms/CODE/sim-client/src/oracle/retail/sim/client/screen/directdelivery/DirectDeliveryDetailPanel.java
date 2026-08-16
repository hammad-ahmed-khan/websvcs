package oracle.retail.sim.client.screen.directdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.SimMoneyDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.supplier.SupplierSearchListener;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.tableeditor.SimMoneyTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryInvoiceEntryType;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryProperty;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierDeliveryDiscrepancyType;
import oracle.retail.sim.common.uin.SerialNumberValue;

/********************************************************************************************************
 * Direct Delivery Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = -3151413736270256843L;

    private static final String SUPPLIER_MODIFIED = "Supplier.modified";
    private static final String COMMENT_MODIFIED = "Comments.modified";
    private static final String INVOICE_NUMBER_MODIFIED = "InvoiceNumber.modified";
    private static final String INVOICE_DATE_MODIFIED = "InvoiceDate.modified";

    private DirectDeliveryDetailModel model = new DirectDeliveryDetailModel();

    private RSearchFieldEditor supplierEditor = SimEditorFactory.createActiveSupplierSearchFieldEditor();
    private RDisplayLabelEditor purchaseOrderEditor = new RDisplayLabelEditor("Purchase Order");
    private RDisplayLabelEditor receiveDateEditor = new RDisplayLabelEditor("Receive Date");
    private RDisplayLabelEditor userEditor = new RDisplayLabelEditor("User");
    private RDisplayLabelEditor customerOrderEditor = new RDisplayLabelEditor("Customer Order");
    private RDisplayLabelEditor fulfillmentOrderEditor = new RDisplayLabelEditor("Fulfillment Order");
    private RTextFieldEditor invoiceNumberEditor = new RTextFieldEditor("Invoice Number");
    private RDateFieldEditor invoiceDateEditor = new RDateFieldEditor("Invoice Date");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor expectedCasesEditor = new RDisplayLabelEditor("Expected Cases");
    private RDisplayLabelEditor receivedCasesEditor = new RDisplayLabelEditor("Received Cases");
    private RDisplayLabelEditor damagedLinesEditor = new RDisplayLabelEditor("Damaged Lines");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");

    private StockItemScannerDialog scannerDialog;

    private StockItemTableEditor stockItemEditor = new StockItemTableEditor(FunctionalArea.DIRECT_DELIVERY_RECEIPT);
    private SimTable lineItemTable = new SimTable(new DirectDeliveryItemDefinition());

    public DirectDeliveryDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        supplierEditor.setSearchListener(buildSupplierSearchListener());
        supplierEditor.registerAction(this, SUPPLIER_MODIFIED);
        receiveDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        invoiceNumberEditor.setIdentifier(SimName.DIRECT_DELIVERY_INVOICE);
        invoiceNumberEditor.setSizeType(EditorConstants.MEDIUM);
        invoiceNumberEditor.registerAction(this, INVOICE_NUMBER_MODIFIED);
        invoiceDateEditor.setSizeType(EditorConstants.MEDIUM);
        invoiceDateEditor.registerAction(this, INVOICE_DATE_MODIFIED);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        expectedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        receivedCasesEditor.setDataType(DataTypeConstants.INTEGER);
        damagedLinesEditor.setDataType(DataTypeConstants.INTEGER);
        commentsEditor.setIdentifier(SimName.DIRECT_DELIVERY_COMMENT);
        commentsEditor.registerAction(this, COMMENT_MODIFIED);
        stockItemEditor.setSearchListener(buildItemSearchListener());
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(DirectDeliveryProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
        lineItemTable.getModel().addTableModelListener(buildTableQuantityListener());
    }

    private void layoutPanel() {
        boolean invoiceEnabled = model.getInvoiceEntryType() != DirectDeliveryInvoiceEntryType.DISABLED;

        REditorPanel headerPanel = new REditorPanel(4, 3);
        headerPanel.add(supplierEditor);
        headerPanel.add(purchaseOrderEditor);
        headerPanel.add(customerOrderEditor);
        headerPanel.add(invoiceEnabled ? invoiceNumberEditor : new JLabel());
        headerPanel.add(receiveDateEditor);
        headerPanel.add(userEditor);
        headerPanel.add(fulfillmentOrderEditor);
        headerPanel.add(invoiceEnabled ? invoiceDateEditor : new JLabel());
        headerPanel.add(statusEditor);
        headerPanel.add(expectedCasesEditor);
        headerPanel.add(receivedCasesEditor);
        headerPanel.add(damagedLinesEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);
        SimTablePane lineItemPane = new SimTablePane(lineItemTable);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(commentsEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 2, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    /****************************************************************************************************
     * State Methods for Screen
     ***************************************************************************************************/

    public boolean isViewOnlyMode() {
        return model.isViewOnlyMode();
    }

    public boolean isDeliveryClosed() {
        return model.isDeliveryClosed();
    }

    public boolean isAddItemAllowed() {
        return model.isAddItemAllowed();
    }

    public boolean isRemoveItemAllowed() {
        return model.isDeleteItemAllowed();
    }

    public boolean hasPurchaseOrder() {
        return model.hasPurchaseOrder();
    }

    public boolean isDeliveryAdjustable() {
        return model.isDeliveryAdjustable();
    }

    public boolean isAllowAdjustment() {
        return model.isAllowAdjustment();
    }

    public boolean isRejectDeliveryAllowed() {
        return model.isRejectDeliveryAllowed();
    }

    /****************************************************************************************************
     * Populate Panel
     ***************************************************************************************************/

    public void start() {
        try {
            model.loadState();
            populateScreen();
            populateTable();
            launchScanner();
        } catch (Throwable t) {
            displayException(t);
        }
    }

    public void stop() {
        model.clearState();
        shutdownScanner();
    }

    /****************************************************************************************************
     * SCANNER METHODS
     ***************************************************************************************************/

    protected boolean isScannerAvailable() {
        return model.isScannerAvailable();
    }

    private void launchScanner() {
        if (model.isScannerAvailable()) {
            if (model.isScannerAutoDisplay()) {
                displayScanner();
            }
        }
    }

    private void displayScanner() {
        if (model.isScannerAvailable()) {
            if (scannerDialog == null) {
                scannerDialog = new StockItemScannerDialog();
                scannerDialog.setItemProcessor(this);
                scannerDialog.activateReceiving();
            }
            scannerDialog.setVisible(true);
        }
    }

    private void shutdownScanner() {
        if (scannerDialog != null) {
            scannerDialog.setVisible(false);
            scannerDialog = null;
        }
    }

    public void processBarcodeItem(BarcodeItem barcodeItem) {
        lineItemTable.stopEditing();
        try {
            if (!model.hasSupplier()) {
                assignFocusInScreen(supplierEditor);
                throw new UIException(CommonMessageText.SUPPLIER_BLANK, RErrorSeverity.WARNING);
            }
            DirectDeliveryLineItemWrapper lineItem = findExistingLineItem(barcodeItem.getId());
            if (lineItem != null) {
                model.updateExistingLineItem(lineItem, barcodeItem);
                return;
            }
            if (!model.isAddItemAllowed()) {
                throw new UIException(CommonMessageText.LINE_ITEM_NOT_FOUND, RErrorSeverity.WARNING);
            }
            lineItem = findNewLineItem();
            if (lineItem == null) {
                lineItem = model.createNewLineItemWrapper();
                lineItemTable.addRow(lineItem);
            }
            lineItem.setStockItem(barcodeItem.getStockItem());
            model.updateExistingLineItem(lineItem, barcodeItem);
            if (model.hasSupplier() && supplierEditor.isEnabled()) {
                supplierEditor.setEnabled(true, false);
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
            refreshHeader();
        }
    }

    /****************************************************************************************************
     * HANDLE SCANNER
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SUPPLIER_MODIFIED)) {
                handleSupplierModified();
            } else if (command.equals(INVOICE_NUMBER_MODIFIED)) {
                handleInvoiceNumberModified();
            } else if (command.equals(INVOICE_DATE_MODIFIED)) {
                handleInvoiceDateModified();
            } else if (command.equals(COMMENT_MODIFIED)) {
                handleCommentsModified();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void handleSupplierModified() {
        if (model.isViewOnlyMode() || model.isDeliveryClosed() || model.isAllowAdjustment() || !model.isDeliveryEmpty()) {
            return;
        }
        DirectDelivery delivery = model.getDelivery();
        try {
            Supplier oldSupplier = delivery.getSupplier();
            Supplier supplier = (Supplier) supplierEditor.getData();
            if (supplier == null && oldSupplier != null) {
                supplierEditor.setActionsEnabled(false);
                supplierEditor.setData(oldSupplier);
                supplierEditor.setActionsEnabled(true);
            }
            if (supplier == null || supplier.equals(oldSupplier)) {
                return;
            }
            if (!supplier.isPurchaseOrderCreationAllowed() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_SUPPLIER_INDICATOR_FOR_PURCHASE_ORDER_CREATION)) {
                displayError(DirectDeliveryMessageText.PO_NO_NEW_ORDERS);
                supplierEditor.setData(oldSupplier);
                return;
            }
            delivery.setSupplier(supplier);
        } catch (Exception e) {
            displayException(e);
            assignFocusInScreen(supplierEditor);
            return;
        }
        if (model.isInvoiceEntryUnique() && !StringHelper.isNullOrEmpty(delivery.getInvoiceNumber())) {
            try {
                delivery.setInvoiceNumber(null);
                model.releaseInvoiceLock();
                handleInvoiceNumberModified();
            } catch (Exception e) {
                displayException(e);
                invoiceNumberEditor.clear();
                assignFocusInScreen(invoiceNumberEditor);
            }
        }
    }

    private void handleInvoiceNumberModified() {
        if (model.isViewOnlyMode() || model.isDeliveryClosed() || model.isAllowAdjustment()) {
            return;
        }
        DirectDelivery delivery = model.getDelivery();
        String invoiceNumber = StringHelper.trimToNull(delivery.getInvoiceNumber());
        try {
            String value = invoiceNumberEditor.getTextOrNull();
            if (value == null && invoiceNumber != null || value != null && !value.equals(invoiceNumber)) {
                if (model.isInvoiceEntryUnique() && (!model.validateInvoiceEntry(value) || !model.obtainInvoiceLock(value))) {
                    throw new BusinessException(DirectDeliveryMessageText.DUPLICATE_INVOICE_NUMBER);
                }
                delivery.setInvoiceNumber(value);
            }
        } catch (Exception e) {
            displayException(e);
            invoiceNumberEditor.setText(invoiceNumber);
            assignFocusInScreen(invoiceNumberEditor);
        }
    }

    private void handleInvoiceDateModified() {
        if (model.isViewOnlyMode() || model.isDeliveryClosed() || model.isAllowAdjustment()) {
            return;
        }
        DirectDelivery delivery = model.getDelivery();
        Date invoiceDate = delivery.getInvoiceDate();
        try {
            Date value = invoiceDateEditor.getDate();
            if (value == null) {
                throw new BusinessException(DirectDeliveryMessageText.INVOICE_DATE_REQUIRED);
            }
            if (!value.equals(invoiceDate)) {
                delivery.setInvoiceDate(value);
            }
        } catch (Exception e) {
            displayException(e);
            invoiceDateEditor.setDate(invoiceDate);
            assignFocusInScreen(invoiceDateEditor);
        }
    }

    private void handleCommentsModified() {
        try {
            DirectDelivery delivery = model.getDelivery();
            String value = commentsEditor.getTextOrNull();
            if (!StringHelper.equalsTrim(delivery.getComments(), value)) {
                delivery.setComments(value);
            }
        } catch (BusinessException e) {
            displayException(e);
            assignFocusInScreen(commentsEditor);
        }
    }

    /****************************************************************************************************
     * Adjust Delivery
     ***************************************************************************************************/

    public boolean handleAdjustDelivery() throws Exception {
        lineItemTable.stopEditing();
        if (!RConfirmUtility.confirm("Re-Open The Delivery", DirectDeliveryMessageText.ASN_REOPEN_DELIVERY)) {
            return false;
        }
        if (!model.obtainLock()) {
            return false;
        }
        model.adjustDelivery();
        populateScreen();
        launchScanner();
        return true;
    }

    /****************************************************************************************************
     * Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        lineItemTable.stopEditing();
        model.printDeliveryReport();
    }

    /****************************************************************************************************
     * Cancel
     ***************************************************************************************************/

    public void handleCancel() {
        if (!model.isViewOnlyMode() && !model.isDeliveryClosed()) {
            try {
                model.releaseLock(model.getDelivery());
                model.releaseInvoiceLock();
            } catch (Throwable t) {
                displayException(t);
            }
        }
        model.storeCanceled();
    }

    /****************************************************************************************************
     * SAVE
     ***************************************************************************************************/

    public boolean handleSave() {
        lineItemTable.stopEditing();
        try {
            if (!checkLock()) {
                return false;
            }
            if (model.isDeliveryEmpty()) {
                return cancelEmptyDelivery();
            }
            removeNewLineItem();
            model.updateDelivery();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    /****************************************************************************************************
     * Reject
     ***************************************************************************************************/

    public boolean handleRejectDelivery() {
        lineItemTable.stopEditing();
        try {
            if (model.isViewOnlyMode()) {
                return true;
            }
            if (model.isDeliveryClosed()) {
                return true;
            }
            if (!checkLock()) {
                return false;
            }
            if (model.isDeliveryEmpty()) {
                return cancelEmptyDelivery();
            }
            if (!RConfirmUtility.confirm("Confirmation", DirectDeliveryMessageText.REJECT_CONFIRM)) {
                return false;
            }
            removeNewLineItem();
            model.rejectDelivery();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    /****************************************************************************************************
     * Confirm
     ***************************************************************************************************/

    public boolean handleConfirm() {
        lineItemTable.stopEditing();
        try {
            if (model.isViewOnlyMode()) {
                return true;
            }
            if (model.isDeliveryClosed()) {
                return true;
            }
            if (!checkLock()) {
                return false;
            }
            if (model.isDeliveryCancelOnConfirm()) {
                return cancelEmptyDelivery();
            }
            if (!model.isDeliveryValidForConfirm()) {
                throw new BusinessException(DirectDeliveryMessageText.OVER_RECEIVING_NOT_ALLOWED);
            }
            if (!RConfirmUtility.confirm("Confirmation", DirectDeliveryMessageText.RECEIVE_CONFIRM)) {
                return false;
            }
            removeNewLineItem();
            validateInactiveItems();
            if (model.isDeliveryCancelOnConfirm()) {
                return cancelEmptyDelivery();
            }
            if (model.isSerialNumberProcessingEnabled()) {
                if (!validateUnreceivedUINItems()) {
                    return false;
                }
                validateReceivedItemsMissingUINs();
            }
            if (!validateSupplierDiscrepancies()) {
                return false;
            }
            if (!validateRemoveQuantities()) {
                return false;
            }
            if (model.isFulfillmentOrderRelated()) {
                displayMessage(DirectDeliveryMessageText.FULFILLMENT_ORDER_RELATED);
            }
            model.receiveDelivery();
            return true;
        } catch (Throwable t) {
            displayException(t);
        }
        return false;
    }

    private void validateInactiveItems() throws Exception {
        boolean isItemShortDescription = SimConfigManager.isItemShortDescription();
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            ItemStatus status = lineItem.getStockItem().getStatus();
            if (status == ItemStatus.DELETED || status == ItemStatus.INACTIVE || status == ItemStatus.DISCONTINUED) {
                String[] values = new String[2];
                values[0] = lineItem.getItemIdDescription(isItemShortDescription);
                values[1] = status.toString();
                if (!RConfirmUtility.confirm("Direct Delivery", ItemRequestMessageText.REQUEST_ITEM_CONFIRM, values)) {
                    if (lineItem.isDeleteAllowed()) {
                        lineItem.removeLineItem();
                        lineItemTable.removeRow(lineItem);
                    } else {
                        lineItemTable.setRowSelection(lineItem);
                        throw new BusinessException(ItemMessageText.ITEM_REMOVE_ERROR);
                    }
                }
            }
        }
    }

    private boolean validateUnreceivedUINItems() throws Exception {
        boolean userConfirmed = false;
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            if (!lineItem.isSerialNumberRequired() || lineItem.getQuantityReceivedBasedOnUom() != null) {
                continue;
            }
            if (!userConfirmed) {
                if (!RConfirmUtility.confirm("Direct Delivery", CommonMessageText.UIN_RECEIVE_ZERO_QTY_CONFIRM)) {
                    lineItemTable.setRowSelection(lineItem);
                    return false;
                }
                userConfirmed = true;
            }
            lineItem.setQuantityReceivedBasedOnUom(Quantity.ZERO);
        }
        return true;
    }

    private void validateReceivedItemsMissingUINs() throws Exception {
        List<String> itemsMissingUins = new ArrayList<String>();
        boolean isItemShortDescription = SimConfigManager.isItemShortDescription();
        DirectDeliveryLineItemWrapper firstLineItemMissingUins = null;
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            if (lineItem.isMissingRequiredUins()) {
                if (firstLineItemMissingUins == null) {
                    firstLineItemMissingUins = lineItem;
                }
                itemsMissingUins.add(lineItem.getItemIdDescription(isItemShortDescription));
            }
        }
        if (itemsMissingUins.isEmpty()) {
            return;
        }
        if (firstLineItemMissingUins != null) {
            lineItemTable.setRowSelection(firstLineItemMissingUins);
        }
        if (itemsMissingUins.size() == 1) {
            throw new BusinessException(CommonMessageText.UIN_REQUIRED, itemsMissingUins.get(0));
        }
        throw new BusinessException(CommonMessageText.UIN_REQUIRED_MULTI, itemsMissingUins);
    }

    public boolean validateSupplierDiscrepancies() throws Exception {
        SupplierDeliveryDiscrepancyType discrepancyType = model.getDelivery().getSupplier().getDeliveryDiscrepancy();
        if (discrepancyType == SupplierDeliveryDiscrepancyType.ALLOW) {
            return true;
        }
        boolean isOverrideAllowed = model.isSupplierDiscrepancyOverrideAllowed();
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            Quantity overage = lineItem.getQuantityOverageOrZero();
            if (overage.isNegative() || overage.isPositive() && discrepancyType == SupplierDeliveryDiscrepancyType.NOT_ALLOW) {
                if (!isOverrideAllowed) {
                    lineItemTable.setRowSelection(lineItem);
                    throw new BusinessException(DirectDeliveryMessageText.SUPPLIER_DISCREPANCY_NOT_ALLOWED);
                }
                if (!RConfirmUtility.confirm("Direct Delivery", DirectDeliveryMessageText.SUPPLIER_DISCREPANCY_OVERRIDE)) {
                    lineItemTable.setRowSelection(lineItem);
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    public boolean validateRemoveQuantities() throws BusinessException {
        if (!model.hasPurchaseOrder()) {
            return true;
        }
        boolean isRemoveDamages = model.getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_DAMAGES);
        boolean isRemoveOverReceive = model.getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_OVER_RECEIVE);
        if (!isRemoveDamages && !isRemoveOverReceive) {
            return true;
        }
        boolean uinEnabled = model.isSerialNumberProcessingEnabled();
        boolean hasDamagesForRemoval = false;
        boolean hasOverReceiveForRemoval = false;
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            Quantity damaged = lineItem.getQuantityDamagedOrZero();
            if (isRemoveDamages && damaged.isPositive()) {
                if (uinEnabled && lineItem.getStockItem().isSerialNumberRequired()) {
                    for (SerialNumberValue serialNumber : lineItem.getSerialNumbers()) {
                        if (serialNumber.isDamaged()) {
                            lineItemTable.setRowSelection(lineItem);
                            throw new BusinessException(DirectDeliveryMessageText.DSD_REMOVE_UIN_DAMAGES_ERROR);
                        }
                    }
                }
                if (!hasDamagesForRemoval) {
                    hasDamagesForRemoval = true;
                }
            }
            if (isRemoveOverReceive) {
                Quantity expected = lineItem.getQuantityExpectedOrZero();
                if (lineItem.getQuantityReceivedOrZero().add(damaged).compareTo(expected) > 0) {
                    if (uinEnabled && lineItem.getStockItem().isSerialNumberRequired() && new Quantity(lineItem.getSerialNumbers().size()).compareTo(expected) > 0) {
                        lineItemTable.setRowSelection(lineItem);
                        throw new BusinessException(DirectDeliveryMessageText.DSD_REMOVE_UIN_OVER_RECEIVE_ERROR);
                    }
                    if (!hasOverReceiveForRemoval) {
                        hasOverReceiveForRemoval = true;
                    }
                }
            }
        }
        MessageText message;
        if (hasDamagesForRemoval && hasOverReceiveForRemoval) {
            message = DirectDeliveryMessageText.DSD_REMOVE_DAMAGES_OVER_RECEIVE;
        } else if (hasDamagesForRemoval) {
            message = DirectDeliveryMessageText.DSD_REMOVE_DAMAGES;
        } else if (hasOverReceiveForRemoval) {
            message = DirectDeliveryMessageText.DSD_REMOVE_OVER_RECEIVE;
        } else {
            return true;
        }
        return RConfirmUtility.confirm("Auto Remove Confirmation", message);
    }

    /****************************************************************************************************
     * Receive All
     ***************************************************************************************************/

    public void handleReceiveAll() throws Exception {
        lineItemTable.stopEditing();
        if (!model.hasPurchaseOrder()) {
            throw new BusinessException(DirectDeliveryMessageText.ASN_RECEIVE_ALL_ERROR);
        }
        if (model.isSerialNumberProcessingEnabled()) {
            checkRequiredUinItems();
        }
        if (checkLock()) {
            model.receiveAll();
            lineItemTable.refreshTable();
            refreshHeader();
        }
    }

    private void checkRequiredUinItems() throws Exception {
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem != null && stockItem.isSerialNumberRequired() && !stockItem.isAgsnEnabled()) {
                throw new BusinessException(CommonMessageText.UIN_REQUIRED_TO_RECEIVE_ALL, "Direct Delivery");
            }
        }
    }

    /****************************************************************************************************
     * Add Item
     ***************************************************************************************************/

    public void handleAddItem() throws Exception {
        lineItemTable.stopEditing();
        if (!model.hasSupplier()) {
            assignFocusInScreen(supplierEditor);
            throw new BusinessException(CommonMessageText.SUPPLIER_BLANK);
        }
        if (!model.isAddItemAllowed()) {
            throw new BusinessException(CommonMessageText.FUNCTION_NOT_AVAILABLE);
        }
        if (findNewLineItem() != null) {
            return;
        }
        if (checkLock()) {
            addNewLineItem();
        }
        if (model.hasSupplier() && supplierEditor.isEnabled()) {
            supplierEditor.setEnabled(true, false);
        }
    }

    private void addNewLineItem() {
        lineItemTable.addRow(model.createNewLineItemWrapper());
        lineItemTable.editCellInLastRow(DirectDeliveryProperty.STOCK_ITEM);
    }

    private void removeNewLineItem() {
        DirectDeliveryLineItemWrapper lineItem = findNewLineItem();
        if (lineItem != null) {
            lineItemTable.removeRow(lineItem);
        }
    }

    private DirectDeliveryLineItemWrapper findNewLineItem() {
        if (lineItemTable.isEmpty()) {
            return null;
        }
        DirectDeliveryLineItemWrapper lineItem = (DirectDeliveryLineItemWrapper) lineItemTable.getRowData(lineItemTable.getLastRowIndex());
        if (lineItem != null && lineItem.isNew()) {
            return lineItem;
        }
        return null;
    }

    private DirectDeliveryLineItemWrapper findExistingLineItem(String itemId) {
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            StockItem stockItem = lineItem.getStockItem();
            if (stockItem != null && itemId.equals(stockItem.getId())) {
                return lineItem;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Delete Item
     ***************************************************************************************************/

    public void handleRemoveItem() throws Exception {
        lineItemTable.stopEditing();
        if (!model.isDeleteItemAllowed()) {
            throw new BusinessException(CommonMessageText.FUNCTION_NOT_AVAILABLE);
        }
        if (lineItemTable.getSelectedRowCount() <= 0) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return;
        }
        if (!checkLock()) {
            return;
        }
        List<DirectDeliveryLineItemWrapper> lineItems = lineItemTable.getAllSelectedRowData();
        for (DirectDeliveryLineItemWrapper lineItem : lineItems) {
            if (!lineItem.isDeleteAllowed()) {
                throw new BusinessException(ItemMessageText.ITEM_REMOVE_ERROR);
            }
            lineItem.removeLineItem();
            lineItemTable.removeRow(lineItem);
        }
        refreshHeader();
        if (model.isDeliveryEmpty()) {
            if (cancelEmptyDelivery()) {
                navigate(SimNavigation.BACK);
                return;
            }
            if (!supplierEditor.isEnabled()) {
                supplierEditor.setEnabled(true);
            }
        }
    }

    private boolean cancelEmptyDelivery() throws Exception {
        if (!RConfirmUtility.confirm("Direct Delivery Delete Confirmation", CommonMessageText.NO_ROWS_REMAINING)) {
            return false;
        }
        model.cancelDelivery();
        return true;
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private void populateScreen() {
        boolean notViewOnly = !model.isViewOnlyMode();
        boolean deliveryClosed = model.isDeliveryClosed();
        boolean invoiceEnabled = model.getInvoiceEntryType() != DirectDeliveryInvoiceEntryType.DISABLED;
        boolean deliveryAdjustment = model.isAllowAdjustment();
        boolean deliveryEmpty = model.isDeliveryEmpty();
        DirectDelivery delivery = model.getDelivery();
        supplierEditor.setActionsEnabled(false);
        supplierEditor.setData(delivery.getSupplier());
        supplierEditor.setActionsEnabled(true);
        PurchaseOrderVO purchaseOrder = delivery.getPurchaseOrder();
        if (purchaseOrder != null) {
            purchaseOrderEditor.setData(purchaseOrder.getExternalId());
            customerOrderEditor.setData(purchaseOrder.getCustomerOrderId());
            fulfillmentOrderEditor.setData(purchaseOrder.getFulfillmentOrderExternalId());
        }
        statusEditor.setData(delivery.getStatus());
        Date completeDate = delivery.getCompleteDate();
        if (completeDate == null && notViewOnly && !deliveryClosed) {
            completeDate = SimDateUtil.getCurrentDate();
        }
        receiveDateEditor.setData(completeDate);
        userEditor.setData(delivery.getUserId());
        if (invoiceEnabled) {
            invoiceNumberEditor.setText(delivery.getInvoiceNumber());
            Date invoiceDate = delivery.getInvoiceDate();
            if (invoiceDate == null && notViewOnly && !deliveryClosed) {
                invoiceDate = SimDateUtil.getCurrentDate();
            }
            invoiceDateEditor.setDate(invoiceDate);
        }
        expectedCasesEditor.setData(delivery.getNumberOfCasesExpected().intValue());
        commentsEditor.setText(delivery.getComments());
        refreshHeader();

        boolean invoiceEditAllowed = invoiceEnabled && notViewOnly && !deliveryClosed && !deliveryAdjustment;
        supplierEditor.setEnabled(true, notViewOnly && !deliveryClosed && !deliveryAdjustment && deliveryEmpty);
        invoiceNumberEditor.setEnabled(invoiceEditAllowed);
        invoiceDateEditor.setEnabled(invoiceEditAllowed);
        commentsEditor.setEnabled(notViewOnly && !deliveryClosed);
    }

    private void refreshHeader() {
        DirectDelivery delivery = model.getDelivery();
        receivedCasesEditor.setData(delivery.getNumberOfCasesReceived().intValue());
        damagedLinesEditor.setData(delivery.getNumberOfLineItemsDamaged());
    }

    private void populateTable() throws Exception {
        lineItemTable.setRows(model.createLineItemWrappers());
        if (lineItemTable.isEmpty() && model.isAddItemAllowed() && model.hasSupplier()) {
            addNewLineItem();
        }
    }

    private boolean checkLock() {
        boolean locked = false;
        try {
            locked = model.checkLock();
        } catch (Throwable t) {
            displayException(t);
        }
        if (!locked) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            navigate(SimNavigation.BACK);
        }
        return locked;
    }

    /****************************************************************************************************
     * Supplier Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private SupplierSearchListener buildSupplierSearchListener() {
        return new SupplierSearchListener() {
            public void assignSupplier(Supplier supplier) {
                if (supplier == null) {
                    return;
                }
                if (!supplier.isPurchaseOrderCreationAllowed() && !SimConfigManager.getBoolean(SimConfigManager.DISABLE_SUPPLIER_INDICATOR_FOR_PURCHASE_ORDER_CREATION)) {
                    displayError(DirectDeliveryMessageText.PO_NO_NEW_ORDERS);
                    return;
                }
                supplierEditor.setData(supplier);
            }
        };
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void search() {
                RepositoryManager.addStateObject(SimClientStateKey.SELECTED_SUPPLIER, model.getDelivery().getSupplier());
                super.search();
            }

            public void assignStockItem(StockItem stockItem) {
                stockItemEditor.setData(stockItem);
            }
        };
    }

    /****************************************************************************************************
     * Table Editor Listeners
     ***************************************************************************************************/

    private TableModelListener buildTableQuantityListener() {
        return new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                refreshHeader();
            }
        };
    }

    /****************************************************************************************************
     * DIRECT DELIVERY TABLE DEFINITION
     ***************************************************************************************************/

    private class DirectDeliveryItemDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return DirectDeliveryLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(DirectDeliveryProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Item", DirectDeliveryProperty.STOCK_ITEM, new AttributeDisplayer(DirectDeliveryProperty.ID), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", DirectDeliveryProperty.STOCK_ITEM_DESCRIPTION, false));
            attributes.add(new SimTableAttribute("UOM", DirectDeliveryProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", DirectDeliveryProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.hasPurchaseOrder()) {
                attributes.add(new SimTableAttribute("On Order", DirectDeliveryProperty.QUANTITY_ORDERED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            }
            attributes.add(new SimTableAttribute("Expected", DirectDeliveryProperty.QUANTITY_EXPECTED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Received", DirectDeliveryProperty.QUANTITY_RECEIVED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Damaged", DirectDeliveryProperty.QUANTITY_DAMAGED_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (SimConfigManager.getBoolean(SimConfigManager.DISPLAY_UNIT_COST_FOR_DIRECT_DELIVERIES)) {
                attributes.add(new SimTableAttribute("Unit Cost", DirectDeliveryProperty.UNIT_COST, new SimMoneyDisplayer(), new SimMoneyTableEditor(true)));
            }
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", DirectDeliveryProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {
        public void popupDialog(Object data) {
            DirectDeliveryUinDialog dialog = new DirectDeliveryUinDialog();
            dialog.setDirectDeliveryLineItemWrapper((DirectDeliveryLineItemWrapper) data);
            dialog.setVisible(true);
            lineItemTable.updateRow(data);
        }
    }
}
