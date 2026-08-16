package extra.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.store.StoreSearchListener;
import oracle.retail.sim.client.screen.transfer.TransferInfoDialog;
import oracle.retail.sim.client.screen.transfer.TransferLineItemWrapper;
import oracle.retail.sim.client.screen.transfer.TransferViewUinDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferStatus;

/********************************************************************************************************
 * Transfer View Only Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraTransferViewPanel extends ExtraTransferDetailPanel {
    private static final long serialVersionUID = -7949173665510917899L;

    private ExtraTransferViewModel model = new ExtraTransferViewModel();

    private static final String VIEW_SERIAL_NUMS_COMMAND = "ViewSerialNumbersCommand";

    public SimScreenModel getScreenModel() {
        return model;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTransfer();
        populateScreen();
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
        lineItemTable = new SimTable(new TransferViewDefinition());
        lineItemTable.setColumnSize("unitOfMeasureMode", 100);
        lineItemTable.setColumnSize("caseSize", SimTable.LABEL_WIDTH);
        if (model.isRequestedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isApprovedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isShippedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isReceivedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.RECEIVED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isDamagedQuantityDisplayable()) {
            lineItemTable.setColumnSize(TransferProperty.DAMAGED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        }
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize(TransferProperty.SERIAL_NUMBER_COUNT, SimTable.LABEL_WIDTH);
        }
        lineItemTable.setRows(model.getLineItemWrappers());
        lineItemTable.registerDoubleClickAction(this, VIEW_SERIAL_NUMS_COMMAND);

        lineItemPane.setTable(lineItemTable);
    }

    void validateEditorState() {
        sendingStoreEditor.setEnabled(false);
        receivingStoreEditor.setEnabled(false);
        requestCommentsEditor.setEnabled(false);
        transferCommentsEditor.setEnabled(false);
        contextTypeEditor.setEnabled(false);
        contextValueEditor.setEnabled(false);
    }

    List<ContextType> findContextTypes() throws Exception {
        return model.findContextTypes();
    }

    public boolean isAdjustmentAllowed() {
        return model.isAdjustmentAllowed();
    }

    public boolean isDispatchAllowed() {
        return model.isDispatchAllowed();
    }

    /**
     * Helper methods
     */
    public boolean isSubmitAllowed() {
        TransferStatus status = model.getTransfer().getStatus();
        boolean submitRequired = SimConfigManager.getStoreBoolean(StoreConfigKeys.TRANSFER_DISPATCH_VALIDATE, model.getTransfer().getSendingStore().getId());
        if (!submitRequired) {
            return false;
        }
        return status == TransferStatus.IN_PROGRESS || status == TransferStatus.SUBMITTED;
    }

    /****************************************************************************************************
     * Stop
     ***************************************************************************************************/

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_TRANSFER);
        RepositoryManager.removeStateObject(SimClientStateKey.TRANSFER_VIEW_ONLY_MODE);
    }

    /****************************************************************************************************
     * Scanner Methods
     ***************************************************************************************************/

    protected boolean isScannerAvailable() {
        return false;
    }

    protected boolean isScannerAutoDisplay() {
        return false;
    }

    public void processBarcodeItem(BarcodeItem barcodeItem) {
    }

    /****************************************************************************************************
     * Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(VIEW_SERIAL_NUMS_COMMAND)) {
                doViewSerialNumbersCommand();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doViewSerialNumbersCommand() throws Exception {
        TransferLineItemWrapper lineItemWrapper = (TransferLineItemWrapper) lineItemTable.getSelectedRowData();
        if (lineItemWrapper.isSerialNumberRequired()) {
            TransferViewUinDialog dialog = new TransferViewUinDialog();
            dialog.setLineItemWrapper(lineItemWrapper);
            dialog.setVisible(true);
        }
    }

    /****************************************************************************************************
     * Actions
     ***************************************************************************************************/

    public boolean handleAdjustDelivery() throws Exception {
        if (!model.obtainTransferLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return false;
        }
        if (RConfirmUtility.confirm("Re-Open The Delivery", TransferMessageText.REOPEN_TRANSFER_CONFIRM)) {
            model.getTransfer().setAdjustReceivedTransferMode();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Store Bill Of Lading Action
     ***************************************************************************************************/

    public void storeTransferForBillOfLading() {
        model.storeTransferForBillOfLading();
    }

    /****************************************************************************************************
     * handleTransferInfo
     **************************************************************************************************/
    public void handleTransferInfo() throws Exception {
        TransferInfoDialog dialog = new TransferInfoDialog();
        dialog.setTransfer(model.getTransfer());
        dialog.setVisible(true);
    }

    /**
     * dispatch transfer from submit status
     */
    public boolean handleDispatch() throws Exception {
        model.obtainTransferLock();
        model.getTransfer().validateIsCoherent();
        model.validateDispatchAllowed();

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

    /********************************************************************************
     * Handle Cancel Submit
     ********************************************************************************/
    public boolean handleCancelSubmit() throws Exception {
        model.validateCancelSubmitAllowed();
        model.obtainTransferLock();
        model.getTransfer().validateIsCoherent();

        if (RConfirmUtility.confirm("Transfer cancel submit Confirmation", TransferMessageText.CANCEL_SUBMIT_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.cancelSubmit();
                model.clearTransferLock();
            } finally {
                showScreenBusy(false);
            }
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Transfer Line Item Table Definition
     ***************************************************************************************************/

    private class TransferViewDefinition extends SimTableDefinition {

        private QuantityDisplayer quantityDisplayer = new QuantityDisplayer();

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
            attributes.add(new SimTableAttribute("Pack Size", TransferProperty.CASE_SIZE, quantityDisplayer, new LineItemQuantityTableEditor()));
            if (model.isRequestedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Requested", TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, quantityDisplayer, new LineItemQuantityTableEditor()));
            }
            if (model.isApprovedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Accepted", TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, quantityDisplayer, new LineItemQuantityTableEditor()));
            }
            if (model.isShippedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Shipped", TransferProperty.TRANSFER_QUANTITY_BASED_ON_UOM, quantityDisplayer, new LineItemQuantityTableEditor()));
            }
            if (model.isReceivedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Received", TransferProperty.RECEIVED_QUANTITY_BASED_ON_UOM, quantityDisplayer, new LineItemQuantityTableEditor()));
            }
            if (model.isDamagedQuantityDisplayable()) {
                attributes.add(new SimTableAttribute("Damaged", TransferProperty.DAMAGED_QUANTITY_BASED_ON_UOM, quantityDisplayer, new LineItemQuantityTableEditor()));
            }
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", TransferProperty.SERIAL_NUMBER_COUNT, new SerialNumberTableDisplayer()));
            }
            return attributes;
        }
    }

	@Override
	protected StoreSearchListener buildReceivingStoreListener() {
		return null;
	}
}