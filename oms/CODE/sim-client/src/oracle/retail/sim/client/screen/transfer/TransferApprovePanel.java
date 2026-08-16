package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.LineItemInventoryDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;

/********************************************************************************************************
 * Transfer Approve Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferApprovePanel extends TransferDetailPanel {
    private static final long serialVersionUID = 9062987003270705323L;

    private TransferApproveModel model = new TransferApproveModel();

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public SimScreenModel getScreenModel() {
        return model;
    }

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
        lineItemTable = new SimTable(new TransferDispatchDefinition());
        lineItemTable.setColumnSize(TransferProperty.UOM_MODE, 100);
        lineItemTable.setColumnSize(TransferProperty.CASE_SIZE, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
        lineItemTable.setColumnSize(TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, SimTable.LABEL_WIDTH);
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

    public void stop() {
        try {
            model.clearTransferLock();
        } catch (Throwable e) {
            displayException(e);
        }
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_TRANSFER);
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
     * Handle Accept
     ***************************************************************************************************/

    public boolean handleAccept() throws Exception {
        if (model.hasNoApprovedQuantities()) {
            if (RConfirmUtility.confirm("Transfer Response Confirmation", TransferMessageText.NO_QUANTITY_REJECT_CONFIRM)) {
                showScreenBusy(true);
                try {
                    model.rejectTransfer();
                } finally {
                    showScreenBusy(false);
                }
                displayMessage(TransferMessageText.REJECTION_COMPLETED);
                return true;
            }
            return false;
        }

        if (!model.isPartialFulfillDeliveryAllowed() && model.hasApprovedPartialQuantities()) {
            displayMessage(TransferMessageText.PARTIAL_DELIVERY_NOT_ALLOWED);
            return false;
        }

        if (model.getTransfer().isFulfillmentOrderRelated() && model.hasApprovedMoreThanRequested()) {
            displayMessage(TransferMessageText.TRANSFER_MORE_THAN_CUST_REQUESTED);
            return false;
        }
        if (RConfirmUtility.confirm("Transfer Response Confirmation", TransferMessageText.ACCEPT_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.approveTransfer();
            } finally {
                showScreenBusy(false);
            }
            displayMessage(TransferMessageText.ACCEPT_COMPLETED);
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Reject
     ***************************************************************************************************/
    public boolean handleReject() throws Exception {
        if (RConfirmUtility.confirm("Transfer Response Confirmation", TransferMessageText.REJECTION_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.rejectTransfer();
            } finally {
                showScreenBusy(false);
            }
            displayMessage(TransferMessageText.REJECTION_COMPLETED);
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        showScreenBusy(true);
        try {
            model.updateTransfer();
        } finally {
            showScreenBusy(false);
        }
    }

    public void storeTransferForBillOfLading() {
        model.storeTransferForBillOfLading();
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
     * Handle Default Quantities
     ***************************************************************************************************/

    public void handleDefaultQuantities() {
        model.assignDefaultApprovedQuantities();
        lineItemTable.refreshTable();
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

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(7);
            attributes.add(new SimTableAttribute("Item", TransferProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", TransferProperty.DESCRIPTION));
            attributes.add(new SimTableAttribute("UOM", TransferProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor(true)));
            attributes.add(new SimTableAttribute("Pack Size", TransferProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("SOH", TransferProperty.STOCK_ON_HAND_BASED_ON_UOM, new LineItemInventoryDisplayer()));
            attributes.add(new SimTableAttribute("Requested", TransferProperty.REQUESTED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer()));
            attributes.add(new SimTableAttribute("Accepted", TransferProperty.APPROVED_QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            return attributes;
        }
    }
}
