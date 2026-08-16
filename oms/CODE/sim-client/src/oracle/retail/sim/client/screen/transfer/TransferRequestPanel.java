package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.LineItemInventoryDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferLineItem;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;

/********************************************************************************************************
 * Transfer Request Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferRequestPanel extends TransferDetailPanel implements REventListener {
    private static final long serialVersionUID = 8379466700917916583L;

    private TransferRequestModel model = new TransferRequestModel();

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

    private void populateScreen() throws Exception {
        Transfer transfer = model.getTransfer();

        populateBasicInformation(transfer);

        if (model.isContextTypeAvailable()) {
            populateContextType(transfer, model.findContextTypes());
        }

        sendingStoreEditor.setActionsEnabled(false);
        sendingStoreEditor.setItems(new HashSet<>(model.findBuddyStores()));
        if (transfer.getSendingStore() != null) {
            BuddyStore buddyStore = BOFactory.createBuddyStore(transfer.getSendingStore());
            sendingStoreEditor.addItem(buddyStore);
            sendingStoreEditor.setSelectedItem(buddyStore);
        }
        sendingStoreEditor.setActionsEnabled(true);

        receivingStoreEditor.addItem(transfer.getReceivingStore());
        receivingStoreEditor.setSelectedItem(transfer.getReceivingStore());

        expectedLinesEditor.setData(model.getExpectedLineCount());
        receivedLinesEditor.setData(model.getReceivedLineCount());
        damagedLinesEditor.setData(model.getDamagedLineCount());
        discrepantLinesEditor.setData(model.getDiscrepantLineCount());

        populateTable();

        validateEditorState();
    }

    private void populateTable() {
        lineItemTable = new SimTable(new TransferRequestDefinition());
        lineItemTable.setColumnSize(TransferProperty.UOM_MODE, 100);
        lineItemTable.setColumnSize(TransferProperty.CASE_SIZE, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setRows(model.getLineItemWrappers());
        lineItemPane.setTable(lineItemTable);

        validateLineItemNeeded();
    }

    private void validateLineItemNeeded() {
        if (lineItemTable.isEmpty() && model.isAddLineAvailable() && model.isLineItemsModifiable()) {
            lineItemTable.addRow(model.createTransferLineItem());
            lineItemTable.editCellInLastRow(TransferProperty.STOCK_ITEM);
        }
    }

    void validateEditorState() {
        sendingStoreEditor.setEnabled(model.isSendingStoreModifiable());
        receivingStoreEditor.setEnabled(false);
        requestCommentsEditor.setEnabled(true);
        transferCommentsEditor.setEnabled(false);
        lineItemTable.setTableEditable(model.isLineItemsModifiable());
        contextTypeEditor.setEnabled(true);
        contextValueEditor.setEnabled(model.isContextTypePromotional());
    }

    /****************************************************************************************************
     * Availability Methods
     ***************************************************************************************************/

    public boolean invalidTransferLock() throws Exception {
        return !model.checkTransferLock();
    }

    /****************************************************************************************************
     * Stop
     ***************************************************************************************************/

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

    public void processBarcodeItem(BarcodeItem barcodeItem) {
        lineItemTable.stopEditing();
        try {
            Transfer transfer = model.getTransfer();
            if (transfer.getSendingStore() == null || transfer.getReceivingStore() == null) {
                throw new BusinessException(TransferMessageText.NO_DESTINATION_ASSIGNED);
            }
            TransferLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
            if (lineItemWrapper != null) {
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                return;
            }
            if (model.isAddLineAvailable()) {
                lineItemWrapper = findEmptyWrapper();
                if (lineItemWrapper != null) {
                    lineItemWrapper.setStockItem(barcodeItem.getStockItem());
                    model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                    return;
                }
                lineItemWrapper = model.createTransferLineItem();
                if (lineItemWrapper != null) {
                    lineItemWrapper.setStockItem(barcodeItem.getStockItem());
                    lineItemTable.addRow(lineItemWrapper);
                    model.updateExistingLineItem(lineItemWrapper, barcodeItem);
                }
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() {
        Transfer transfer = model.getTransfer();
        if (transfer.getSendingStore() == null || transfer.getReceivingStore() == null) {
            displayMessage(TransferMessageText.MISSING_SENDING_STORE);
            return;
        }
        if (model.isAddLineAvailable() && model.isLineItemsModifiable()) {
            int row = lineItemTable.getFirstNullValueRow(TransferProperty.STOCK_ITEM);
            if (row < 0) {
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
                List<TransferLineItemWrapper> wrappers = lineItemTable.getAllSelectedRowData();
                for (TransferLineItemWrapper wrapper : wrappers) {
                    model.removeLineItem(wrapper);
                    lineItemTable.removeRow(wrapper);
                }
                validateEditorState();
            }
        }
    }

    private void validateHasQuantity() throws Exception {
        Transfer transfer = model.getTransfer();

        boolean hasNoQuantity = true;
        for (TransferLineItem lineItem : transfer.getLineItems()) {
            if (!lineItem.getRequestedQuantityOrZero().isZero()) {
                hasNoQuantity = false;
                break;
            }
        }
        if (hasNoQuantity) {
            throw new BusinessException(TransferMessageText.AT_LEAST_ONE_NONZERO_QUANTITY_REQUIRED);
        }

    }

    /****************************************************************************************************
     * Handle Done/Save Transfer
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        validateHasQuantity();
        showScreenBusy(true);
        try {
            model.updateTransfer();
        } finally {
            showScreenBusy(false);
        }
    }

    /****************************************************************************************************
     * Handle Request Transfer
     ***************************************************************************************************/

    public boolean handleRequest() throws Exception {
        validateHasQuantity();

        if (RConfirmUtility.confirm("Transfer Request Confirmation", TransferMessageText.REQUEST_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.requestTransfer();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    public void storeTransferForBillOfLading() {
        model.storeTransferForBillOfLading();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(SENDING_STORE_MODIFIED)) {
            doSendingStoreModified();
        } else if (command.equals(REQUEST_COMMENTS_MODIFIED)) {
            doRequestCommentsModified();
        } else if (command.equals(CONTEXT_TYPE_MODIFIED)) {
            doContextTypeChanged();
        } else if (command.equals(CONTEXT_VALUE_MODIFIED)) {
            doContextValueChanged();
        }
    }

    private void doSendingStoreModified() {
        Transfer transfer = model.getTransfer();
        try {
            transfer.setSendingStore(model.findStore(sendingStoreEditor.getSelectedItem()));
            resetPanelState();
        } catch (Exception exception) {
            sendingStoreEditor.setSelectedItem(transfer.getSendingStore());
            displayException(exception);
        }
    }

    private void doRequestCommentsModified() {
        try {
            model.getTransfer().setRequestComments(requestCommentsEditor.getText());
        } catch (BusinessException exception) {
            displayException(exception);
            assignFocusInScreen(requestCommentsEditor);
        }
    }

    private void doContextTypeChanged() {
        try {
            setContextValueEditorState();

            model.getTransfer().setContextType((ContextType) contextTypeEditor.getSelectedItem());
        } catch (Exception e) {
            displayException(e);
            assignFocusInScreen(contextTypeEditor);
        }
    }

    private void setContextValueEditorState() {
        Object obj = contextTypeEditor.getSelectedItem();
        if (obj instanceof ContextType) {
            ContextType contextType = (ContextType) obj;
            if (contextType.isPromotion()) {
                contextValueEditor.setEnabled(true);
            } else {
                contextValueEditor.setText(null);
                contextValueEditor.setEnabled(false);
            }
        }
    }

    private void doContextValueChanged() {
        try {
            model.getTransfer().setContextValue(contextValueEditor.getText());
        } catch (Exception e) {
            displayException(e);
            assignFocusInScreen(contextValueEditor);
        }
    }

    private void resetPanelState() {
        lineItemTable.stopEditing();
        lineItemTable.editCellInLastRow(TransferProperty.STOCK_ITEM);
        validateLineItemNeeded();
        validateEditorState();
    }

    /****************************************************************************************************
     * Transfer Request Definition
     ***************************************************************************************************/

    private class TransferRequestDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return TransferLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(6);
            attributes.add(new SimTableAttribute("Item", TransferProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", TransferProperty.DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", TransferProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", TransferProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("SOH", TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, new LineItemInventoryDisplayer()));
            attributes.add(new SimTableAttribute("Requested", TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            return attributes;
        }
    }
}
