package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferStatus;

/********************************************************************************************************
 * Transfer Dispatch Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferDispatchPanel extends TransferDetailPanel implements REventListener {
    private static final long serialVersionUID = 6381934524594612258L;

    private final TransferDispatchModel model = new TransferDispatchModel();

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
        if (model.isShippedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
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
        transferCommentsEditor.setEnabled(true);
        lineItemTable.setTableEditable(model.isLineItemsModifiable());

        //not allow sending store modify the context value set by the requested store
        Transfer transfer = model.getTransfer();
        if (!transfer.getSendingStore().getId().equals(transfer.getCreateStoreId())) {
            contextTypeEditor.setEnabled(false);
            contextValueEditor.setEnabled(false);
        } else {
            contextValueEditor.setEnabled(model.isContextTypePromotional());
        }
    }

    protected List<ContextType> findContextTypes() throws Exception {
        return model.findContextTypes();
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

            lineItemWrapper = model.createTransferLineItem();
            if (lineItemWrapper != null) {
                lineItemWrapper.setStockItem(barcodeItem.getStockItem());
                lineItemTable.addRow(lineItemWrapper);
                model.updateExistingLineItem(lineItemWrapper, barcodeItem);
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
     * Handle Default Quantities
     ***************************************************************************************************/

    public void handleDefaultQuantities() {
        model.assignDefaultShippedQuantities();
        lineItemTable.refreshTable();
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() {
        if (model.isLineItemsModifiable()) {
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

    public void handleDeleteItem() throws Exception {
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
                        if (model.removeLineItem(wrapper)) {
                            lineItemTable.removeRow(wrapper);
                        }
                    } else {
                        lineItemTable.removeRow(wrapper);
                    }
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
        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.validateHasQuantity()) {
            return false;
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

    /******************************************************************************************
     * Submit/save Transfer
     *****************************************************************************************/
    public boolean handleSubmit() throws Exception {
        model.validateSubmitAllowed();
        model.obtainTransferLock();
        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        Transfer transfer = model.getTransfer();
        if (!model.validateHasQuantity()) {
            return false;
        }
        transfer.validateIsCoherent();

        if (RConfirmUtility.confirm("Transfer submit Confirmation", TransferMessageText.SUBMIT_CONFIRM)) {
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
        model.validateDispatchAllowed();
        if (isMissingSerialNumbers()) {
            return false;
        }
        if (!model.isPartialFulfillDeliveryAllowed() && model.hasDispatchPartialQuantities()) {
            displayMessage(TransferMessageText.DISPATCH_PARTIAL_DELIVERY_NOT_ALLOWED);
            return false;
        }
        if (model.hasDispatchedMoreThanRequested()) {
            displayMessage(TransferMessageText.TRANSFER_MORE_THAN_CUST_REQUESTED);
            return false;
        }

        Transfer transfer = model.getTransfer();
        transfer.validateIsCoherent();
        
        if(!model.hasShipTrailer()) {
        	displayError(CommonMessageText.SHIP_TRAILER_REQUIRED);
            return false;
        }

        if (!model.confirmDefaultShippedQty()) {
            return false;
        }
        if (model.hasAllZeroQuantities()) {
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

    /****************************************************************************************************
     * Helpers
     ***************************************************************************************************/

    public boolean isSubmitAllowed() {
        TransferStatus status = model.getTransfer().getStatus();
        boolean submitRequired = SimConfigManager.getStoreBoolean(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, model.getTransfer().getSendingStore().getId());
        if (!submitRequired) {
            return false;
        }
        return status == TransferStatus.IN_PROGRESS || status == TransferStatus.SUBMITTED;
    }

    public boolean isDispatchAllowed() {
        return model.isDispatchAvailable();
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

    public void storeTransferForBillOfLading() {
        model.storeTransferForBillOfLading();
    }

    /****************************************************************************************************
     * Handle Transfer Info
     ***************************************************************************************************/

    public boolean handleTransferInfo() throws Exception {
        TransferInfoDialog dialog = new TransferInfoDialog();
        dialog.setTransfer(model.getTransfer());
        dialog.setVisible(true);
        return true;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(TRANSFER_COMMENTS_MODIFIED)) {
            doTransferCommentsModified();
        } else if (command.equals(CONTEXT_TYPE_MODIFIED)) {
            doContextTypeChanged();
        } else if (command.equals(CONTEXT_VALUE_MODIFIED)) {
            doContextValueChanged();
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
            model.getTransfer().setContextType((ContextType) contextTypeEditor.getSelectedItem());
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
            if (model.isShippedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Shipped", TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
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
            TransferUinDialog dialog = new TransferUinDialog(FunctionalArea.CREATE_TRANSFER);
            dialog.setLineItemWrapper((TransferLineItemWrapper) model);
            dialog.setVisible(true);
            lineItemTable.refreshTable();
        }
    }
   
    public void handleShipTrailer() {
		model.storeTransferForShipTrailer();
	}

	public boolean isShipTrailerEnabled() {
		return model.isShipTrailerEnabled();
	}
}
