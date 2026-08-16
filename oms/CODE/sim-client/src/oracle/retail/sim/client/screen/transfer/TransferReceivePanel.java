package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.LineItemInventoryDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.ItemStatus;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;

/********************************************************************************************************
 * Transfer Receive Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferReceivePanel extends TransferDetailPanel implements REventListener {
    private static final long serialVersionUID = 4710275673948703935L;

    private TransferReceiveModel model = new TransferReceiveModel();

    public SimScreenModel getScreenModel() {
        return model;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTransfer();
        populateScreen();
        launchScanner();
    }

    public void resume() {
        launchScanner();
    }

    private void populateScreen() throws Exception {
        Transfer transfer = model.getTransfer();

        populateBasicInformation(transfer);

        if (model.isContextTypeAvailable()) {
            populateContextType(transfer, model.findContextTypes());
        }

        sendingStoreEditor.addItem(transfer.getSendingStore());
        sendingStoreEditor.setSelectedItem(transfer.getSendingStore());

        receivingStoreEditor.addItem(transfer.getReceivingStore());
        receivingStoreEditor.setSelectedItem(transfer.getReceivingStore());

        expectedLinesEditor.setData(model.getExpectedLineCount());
        receivedLinesEditor.setData(model.getReceivedLineCount());
        damagedLinesEditor.setData(model.getDamagedLineCount());
        discrepantLinesEditor.setData(model.getDiscrepantLineCount());

        if (model.isReceiveEntireTransferOnly()) {
            transfer.assignDefaultReceivedQuantities();
        }

        populateTable();

        validateEditorState();
    }

    private void populateTable() {
        lineItemTable = new SimTable(new TransferDispatchDefinition());
        lineItemTable.setColumnSize(TransferProperty.UOM_MODE, 100);
        lineItemTable.setColumnSize(TransferProperty.CASE_SIZE, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        if (model.isRequestedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isApprovedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        lineItemTable.setColumnSize(TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.RECEIVED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        if (model.isDamagedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.DAMAGED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(TransferProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
        lineItemTable.setRows(model.getLineItemWrappers());
        lineItemPane.setTable(lineItemTable);
    }

    protected void validateEditorState() {
        sendingStoreEditor.setEnabled(false);
        receivingStoreEditor.setEnabled(false);
        requestCommentsEditor.setEnabled(false);
        transferCommentsEditor.setEnabled(false);
        lineItemTable.setTableEditable(model.isLineItemsModifiable());

        contextTypeEditor.setEnabled(false);
        contextValueEditor.setEnabled(false);
    }

    /****************************************************************************************************
     * Availability Methods
     ***************************************************************************************************/

    public boolean invalidTransferLock() throws Exception {
        return !model.checkTransferLock();
    }

    public boolean isAddItemAvailable() {
        return model.isAddItemAvailable();
    }

    public boolean isReceiptAdjustmentMode() {
        return model.isReceiptAdjustmentMode();
    }

    public boolean isReceiveEntireTransferOnly() {
        return model.isReceiveEntireTransferOnly();
    }

    /****************************************************************************************************
     * Stop
     ***************************************************************************************************/

    public void pause() {
        shutdownScanner();
    }

    public void stop() {
        try {
            model.clearTransferLock();
        } catch (Throwable e) {
            displayException(e);
        }
        shutdownScanner();
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_TRANSFER);
    }

    /****************************************************************************************************
     * Scanner Methods
     ***************************************************************************************************/

    protected boolean isScannerAvailable() {
        return model.isScannerAvailable();
    }

    protected boolean isScannerAutoDisplay() {
        return model.isScannerAutoDisplay();
    }

    protected void displayScanner() {
        if (model.isScannerAvailable()) {
            if (scannerDialog == null) {
                scannerDialog = new StockItemScannerDialog();
                scannerDialog.setItemProcessor(this);
                scannerDialog.activateReceiving();
            }
            scannerDialog.setVisible(true);
        }
    }

    public void processBarcodeItem(BarcodeItem barcodeItem) {
        lineItemTable.stopEditing();
        try {
            TransferLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
            if (lineItemWrapper != null) {
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                return;
            }
            lineItemWrapper = findEmptyWrapper();
            if (lineItemWrapper != null) {
                lineItemWrapper.setStockItem(barcodeItem.getStockItem());
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                return;
            }
            if (!model.isAddItemAvailable()) {
                throw new UIException(CommonMessageText.LINE_ITEM_NOT_FOUND, RErrorSeverity.WARNING);
            }
            lineItemWrapper = model.createTransferLineItem();
            if (lineItemWrapper != null) {
                lineItemWrapper.setStockItem(barcodeItem.getStockItem());
                lineItemTable.addRow(lineItemWrapper);
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
            }
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() throws Exception {
        if (model.isLineItemsModifiable()) {
            int row = lineItemTable.getFirstNullValueRow(TransferProperty.STOCK_ITEM);
            if (row < 0) {
                List<TransferLineItemWrapper> wrappers = lineItemTable.getAllRowData();
                model.validateAddLineItemAllowed(wrappers);

                lineItemTable.addRow(model.createTransferLineItem());
                lineItemTable.editCellInLastRow(TransferProperty.STOCK_ITEM);
            }
        }
    }

    /****************************************************************************************************
     * Handle Delete Item
     ***************************************************************************************************/

    public void handleRemoveItem() throws Exception {
        if (lineItemTable.getSelectedRowCount() == 0) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (model.isLineItemsModifiable()) {
            if (RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
                lineItemTable.stopEditing();

                List<TransferLineItemWrapper> wrappers = lineItemTable.getAllSelectedRowData();
                for (TransferLineItemWrapper wrapper : wrappers) {
                    if (model.removeLineItem(wrapper)) {
                        lineItemTable.removeRow(wrapper);
                    }
                }
                validateEditorState();
            }
        }
    }

    /****************************************************************************************************
     * Handle Receive All
     ***************************************************************************************************/

    public void handleReceiveAll() {
        List<TransferLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (TransferLineItemWrapper wrapper : wrappers) {
            ItemStatus status = wrapper.getLineItem().getStockItem().getStatus();
            if (status.equals(ItemStatus.INACTIVE) || status.equals(ItemStatus.DELETED) || status.equals(ItemStatus.DISCONTINUED)) {
                if (!RConfirmUtility.confirm("Confirmation", CommonMessageText.ITEM_RECEIVE_CONFIRM, status.toString())) {
                    return;
                }
                break;
            }
        }
        model.getTransfer().assignDefaultReceivedQuantities();
        lineItemTable.refreshTable();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        try {
            if (confirmReceivedZeroQty(model.getTransfer())) {
                showScreenBusy(true);
                model.updateTransfer();
            }
        } finally {
            showScreenBusy(false);
        }
    }

    /****************************************************************************************************
     * Handle Receive
     ***************************************************************************************************/

    protected boolean confirmReceivedZeroQty(Transfer transfer) {
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (lineItem.getReceivedQuantity() == null) {
                return RConfirmUtility.confirm("Confirmation", TransferMessageText.EMPTY_RECEIVED_QUANTITY_CONFIRM);
            }
        }
        return true;
    }

    public boolean handleReceive() throws Exception {
        Transfer transfer = model.getTransfer();
        transfer.validateReceiveAllowed();

        if (model.isSerialNumberProcessingEnabled() && isMissingSerialNumberQuantity()) {
            return false;
        }
        if (!confirmReceivedZeroQty(transfer)) {
            return false;
        }

        checkContainCustomerFulfillment();

        if (RConfirmUtility.confirm("Confirmation", TransferMessageText.RECEIVE_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.receiveTransfer();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    private boolean isMissingSerialNumberQuantity() throws Exception {
        boolean zeroQtyConfirmed = false;
        for (int rowIndex = 0; rowIndex < lineItemTable.getRowCount(); rowIndex++) {
            TransferLineItemWrapper wrapper = (TransferLineItemWrapper) lineItemTable.getRowData(rowIndex);
            if (wrapper.isSerialNumberRequired()) {
                if (wrapper.getReceivedQuantity() == null && wrapper.getDamagedQuantity() == null) {
                    if (zeroQtyConfirmed) {
                        wrapper.setReceivedQuantityBasedOnUom(Quantity.ZERO);
                        continue;
                    }

                    if (RConfirmUtility.confirm("Transfer", CommonMessageText.UIN_RECEIVE_ZERO_QTY_CONFIRM)) {
                        wrapper.setReceivedQuantityBasedOnUom(Quantity.ZERO);
                        zeroQtyConfirmed = true;
                        continue;
                    }
                    lineItemTable.setRowSelectionInterval(rowIndex, rowIndex);
                    return true;
                }
            }
        }
        return false;
    }

    public void storeTransferForBillOfLading() {
        model.storeTransferForBillOfLading();
    }

    private void checkContainCustomerFulfillment() {
        if (model.getTransfer().isFulfillmentOrderRelated()) {
            RConfirmUtility.showMessage(Application.getFrame(), "Transfer", TransferMessageText.FULFILLMENT_ORDER_RELATED_TRANSFER);
        }
    }

    /*************************************************************************************
     * Handle Transfer Information
     *************************************************************************************/
    public boolean handleTransferInfo() {
        TransferInfoDialog dialog = new TransferInfoDialog();
        dialog.setTransfer(model.getTransfer());
        dialog.setVisible(true);
        return true;

    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
    }

    /****************************************************************************************************
     * Transfer Dispatch Definition
     ***************************************************************************************************/

    private class TransferDispatchDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TransferLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(TransferProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Item", TransferProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", TransferProperty.DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", TransferProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", TransferProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("SOH", TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, new LineItemInventoryDisplayer()));
            if (model.isRequestedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Requested", TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            }
            if (model.isApprovedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Accepted", TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            }
            attributes.add(new SimTableAttribute("Shipped", TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Received", TransferProperty.RECEIVED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isDamagedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Damaged", TransferProperty.DAMAGED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            }
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", TransferProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {

        public void popupDialog(Object model) {
            TransferUinDialog dialog = new TransferUinDialog(FunctionalArea.RECEIVE_TRANSFER);
            dialog.setLineItemWrapper((TransferLineItemWrapper) model);
            dialog.setVisible(true);
            lineItemTable.refreshTable();
        }
    }
}
