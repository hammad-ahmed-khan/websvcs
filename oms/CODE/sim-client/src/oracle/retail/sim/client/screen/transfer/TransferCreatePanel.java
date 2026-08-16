package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
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
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;

/********************************************************************************************************
 * Transfer Create Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferCreatePanel extends TransferDetailPanel implements REventListener {
    private static final long serialVersionUID = 3818566817709346269L;

    private TransferCreateModel model = new TransferCreateModel();

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

        receivingStoreEditor.setActionsEnabled(false);
        Set<BuddyStore> buddyStores = model.findBuddyStores();
        if (buddyStores != null) {
        	List<Long> restrictedStoreIds = model.getTsfRestrictStores();
        }
        receivingStoreEditor.setItems(new HashSet<>(buddyStores));
        if (transfer.getReceivingStore() != null) {
            BuddyStore buddyStore = BOFactory.createBuddyStore(transfer.getReceivingStore());
            receivingStoreEditor.addItem(buddyStore);
            receivingStoreEditor.setSelectedItem(buddyStore);
        }
        receivingStoreEditor.setActionsEnabled(true);

        sendingStoreEditor.addItem(transfer.getSendingStore());
        sendingStoreEditor.setSelectedItem(transfer.getSendingStore());

        expectedLinesEditor.setData(model.getExpectedLineCount());
        receivedLinesEditor.setData(model.getReceivedLineCount());
        damagedLinesEditor.setData(model.getDamagedLineCount());
        discrepantLinesEditor.setData(model.getDiscrepantLineCount());

        populateTable();

        validateEditorState();
    }

    private void populateTable() {
        lineItemTable = new SimTable(new TransferCreateDefinition());
        lineItemTable.setColumnSize(TransferProperty.UOM_MODE, 100);
        lineItemTable.setColumnSize(TransferProperty.CASE_SIZE, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(TransferProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
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

    protected void validateEditorState() {
        sendingStoreEditor.setEnabled(false);
        receivingStoreEditor.setEnabled(model.isReceivingStoreModifiable());
        requestCommentsEditor.setEnabled(false);
        transferCommentsEditor.setEnabled(true);
        lineItemTable.setTableEditable(model.isLineItemsModifiable());

        contextTypeEditor.setEnabled(true);
        contextValueEditor.setEnabled(model.isContextTypePromotional());
    }

    /****************************************************************************************************
     * Availability Methods
     ***************************************************************************************************/

    protected boolean invalidTransferLock() throws Exception {
        return !model.checkTransferLock();
    }

    /****************************************************************************************************
     * Stop
     ***************************************************************************************************/

    protected void pause() {
        shutdownScanner();
    }

    protected void stop() {
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
     * Handle Scanner
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
        if (transfer.getReceivingStore() == null) {
            displayMessage(TransferMessageText.NO_TO_STORE_ASSIGNED);
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
                lineItemTable.stopEditing();

                List<TransferLineItemWrapper> wrappers = lineItemTable.getAllSelectedRowData();
                for (TransferLineItemWrapper wrapper : wrappers) {
                    if (wrapper.getLineItem() != null) {
                        model.removeLineItem(wrapper);
                    }
                    lineItemTable.removeRow(wrapper);
                }
                validateEditorState();
            }
        }
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        if (model.isEmptyTransfer()) {
            return handleCancel();
        }
        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        if (model.containsNoTransferQuantities()) {
            throw new BusinessException(TransferMessageText.AT_LEAST_ONE_NONZERO_QUANTITY_REQUIRED);
        }

        showScreenBusy(true);
        try {
            model.updateTransfer();
        } finally {
            showScreenBusy(false);
        }
        return true;
    }

    private boolean handleCancel() throws Exception {
        if (RConfirmUtility.confirm("Confirmation", TransferMessageText.EMPTY_TRANSFER_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.cancelTransfer();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    /********************************************************************************
     * Handle Submit
     ********************************************************************************/
    public boolean handleSubmit() throws Exception {
        model.validateSubmitAllowed();
        model.obtainTransferLock();
        Transfer transfer = model.getTransfer();
        transfer.validateIsCoherent();
        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        if (model.containsNoTransferQuantities()) {
            throw new BusinessException(TransferMessageText.AT_LEAST_ONE_NONZERO_QUANTITY_REQUIRED);
        }
        if (RConfirmUtility.confirm("Transfer Submit Confirmation", TransferMessageText.SUBMIT_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.submitTransfer();
                model.clearTransferLock();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Dispatch
     ***************************************************************************************************/

    public boolean handleDispatch() throws Exception {
        Transfer transfer = model.getTransfer();
        transfer.validateIsCoherent();

        model.validateDispatchAllowed();

        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        if (!model.validateShipTrailer()) {
			throw new BusinessException(CommonMessageText.SHIP_TRAILER_REQUIRED);
		}
        model.obtainTransferLock();

        if (model.containsNoTransferQuantities()) {
            if (RConfirmUtility.confirm("Confirmation", TransferMessageText.NO_SHIPPED_QUANTITIES)) {
                try {
                    showScreenBusy(true);
                    model.cancelTransfer();
                } finally {
                    showScreenBusy(false);
                }
                return true;
            }
            return false;
        }
        if (RConfirmUtility.confirm("Transfer Dispatch Confirmation", TransferMessageText.DISPATCH_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.dispatchTransfer();
                model.clearTransferLock();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    private boolean isMissingSerialNumbers() {
        if (model.isSerialNumberProcessingEnabled()) {
            List<String> missingUINItems = model.findItemsWithMissingSerialNumbers();
            if (!missingUINItems.isEmpty()) {
                displayException(new BusinessException(CommonMessageText.UIN_REQUIRED_MULTI, missingUINItems));
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Bill Of Lading
     ***************************************************************************************************/

    public boolean handleBillOfLading() {
        if (model.getTransfer().getReceivingStore() == null) {
            displayError(TransferMessageText.NO_DESTINATION_ASSIGNED);
            return false;
        }
        model.storeTransferForBillOfLading();
        return true;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        switch (event.getEventCommand()) {
            case RECEIVING_STORE_MODIFIED:
                doReceivingStoreModified();
                break;
            case TRANSFER_COMMENTS_MODIFIED:
                doTransferCommentsModified();
                break;
            case CONTEXT_TYPE_MODIFIED:
                doContextTypeChanged();
                break;
            case CONTEXT_VALUE_MODIFIED:
                doContextValueChanged();
                break;
        }
    }

    private void doReceivingStoreModified() {
        Transfer transfer = model.getTransfer();
        try {
            transfer.setReceivingStore(model.findStore(receivingStoreEditor.getSelectedItem()));
            resetPanelState();
        } catch (Exception exception) {
            receivingStoreEditor.setSelectedItem(transfer.getReceivingStore());
            displayException(exception);
        }
    }

    private void doTransferCommentsModified() {
        try {
            model.getTransfer().setTransferComments(transferCommentsEditor.getText());
        } catch (BusinessException exception) {
            displayException(exception);
            assignFocusInScreen(transferCommentsEditor);
        }
    }

    private void doContextTypeChanged() {
        try {
            ContextType contextType = (ContextType) contextTypeEditor.getSelectedItem();
            if (contextType != null && contextType.isPromotion()) {
                contextValueEditor.setEnabled(true);
            } else {
                contextValueEditor.setText(null);
                contextValueEditor.setEnabled(false);
            }
            model.getTransfer().setContextType(contextType);
        } catch (Exception e) {
            displayException(e);
            assignFocusInScreen(contextTypeEditor);
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
     * Transfer Detail Definition
     ***************************************************************************************************/

    private class TransferCreateDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return TransferLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList(TransferProperty.SERIAL_NUMBER_COUNT);
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Item", TransferProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", TransferProperty.DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", TransferProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", TransferProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("SOH", TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, new LineItemInventoryDisplayer()));
            attributes.add(new SimTableAttribute("Shipped", TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
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
            TransferUinDialog dialog = new TransferUinDialog(FunctionalArea.CREATE_TRANSFER);
            dialog.setLineItemWrapper((TransferLineItemWrapper) model);
            dialog.setVisible(true);
            lineItemTable.refreshTable();
        }
    }

    public boolean isSubmitAvailable() {
        return model.isSubmitAvailable();
    }

    public boolean isDispatchAvailable() {
        return model.isDispatchAvailable();
    }

	public void handleShipTrailer() {
		model.storeTransferForShipTrailer();
	}

	public boolean isShipTrailerEnabled() {
		return model.isShipTrailerEnabled();
	}
}
