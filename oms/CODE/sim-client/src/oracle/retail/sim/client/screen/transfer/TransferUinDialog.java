package oracle.retail.sim.client.screen.transfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.screen.uin.SerialNumberDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberValueTableEditor;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferProperty;
import oracle.retail.sim.common.transfer.TransferValidateUinCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

/********************************************************************************************************
 * This dialog handles entering serial number information for transfer transaction line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferUinDialog extends SerialNumberDialog {
    private static final long serialVersionUID = -8627994647288128511L;

    private TransferUinDialogModel model = new TransferUinDialogModel();
    private SerialNumberValueTableEditor serialValueEditor = new SerialNumberValueTableEditor();
    private SimTable serialNumberTable;

    /****************************************************************************************************
     * Initialize Dialog
     ***************************************************************************************************/

    public TransferUinDialog(FunctionalArea functionalArea) {
        model.setFunctionalArea(functionalArea);
    }

    public void setLineItemWrapper(TransferLineItemWrapper lineItemWrapper) {
        model.setLineItemWrapper(lineItemWrapper);

        serialValueEditor.addTableEditorListener(buildSerialNumberValueListener());
        serialNumberTable = new SimTable(new TransferSerialNumberTableDefinition());
        serialNumberTable.setMultipleRowSelectionMode();
        if (model.isReceivingMode()) {
            serialNumberTable.setColumnSize(TransferProperty.DAMAGED, SimTable.LABEL_WIDTH);
        }

        reassignTable(serialNumberTable);

        populateItemHeader(model.getItemId(), model.getItemDescription());
        serialNumberTable.setRows(model.getSerialNumberWrappers());
        serialNumberTable.addRow(model.createSerialNumber());
        serialNumberTable.editCellInLastRow(TransferProperty.SERIAL_NUM_VALUE);

        setSerialNumberEditingEnabled(model.isSerialNumbersEditable());
    }

    /****************************************************************************************************
     * Validate the serial number
     ***************************************************************************************************/

    protected boolean validateSerialNumber(SerialNumberValue serialNumber) {
        try {
            TransferValidateUinCommand command = ClientCommandFactory.createTransferValidateUINCommand();
            command.setFunctionalArea(model.getFunctionalArea());
            command.setStoreId(model.getStoreId());
            command.setUINLabel(model.getUINLabel());
            command.setNewOnTransaction(true);
            command.setSerialNumber(serialNumber);
            command.execute();
        } catch (Exception exception) {
            displayException(exception);
            return false;
        }
        if (model.isSerialNumberShipped(serialNumber)) {
            return true;
        }
        if (model.isReceivingMode()) {
            if (serialNumber.getStatus() == UINStatus.SHIPPED_TO_STORE) {
                displayException(TransferMessageText.SHIPPED_TO_OTHER_STORE, model.getUINLabel());
                return false;
            }
            if (serialNumber.getStatus() == UINStatus.IN_STOCK) {
                if (serialNumber.getStoreId().equals(model.getStoreId())) {
                    String[] params = new String[] { model.getUINLabel(), serialNumber.getUin(), serialNumber.getStatus().toString() };
                    displayException(UINMessageText.UIN_CANNOT_BE_RECEIVED, params);
                    return false;
                }
                if (model.isUnexpectedSerialNumberAllowed()) {
                    return RConfirmUtility.confirm("Confirmation", UINMessageText.MOVE_STORE_CONFIRM, model.getUINLabel());
                }
                displayException(UINMessageText.UIN_UNEXPECTED);
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Add Action
     ***************************************************************************************************/

    protected void doAddRow() {
        serialNumberTable.stopEditing();
        List<SerialNumberWrapper> wrappers = serialNumberTable.getAllRowData();
        for (SerialNumberWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumberValue() == null) {
                return;
            }
        }
        serialNumberTable.addRow(model.createSerialNumber());
        if (model.isReceivingMode()) {
            return;
        }
        serialNumberTable.editCellInLastRow(TransferProperty.SERIAL_NUM_VALUE);
    }

    /****************************************************************************************************
     * Delete Action
     ***************************************************************************************************/

    protected void doRemoveRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = serialNumberTable.getAllSelectedRowData();
        if (selectedWrappers.isEmpty()) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            if (wrapper.getSerialNumberValue() != null) {
                model.removeSerialNumber(wrapper);
            }
        }
        serialNumberTable.refreshTable();
    }

    /****************************************************************************************************
     * Restore Action
     ***************************************************************************************************/

    protected void doRestoreRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = serialNumberTable.getAllSelectedRowData();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setDefaultAction();
        }
        serialNumberTable.refreshTable();
    }

    /****************************************************************************************************
     * Done Action
     ***************************************************************************************************/

    protected void doApply() throws Exception {
        serialNumberTable.stopEditing();

        if (model.isSerialNumbersEditable()) {
            List<SerialNumberWrapper> wrappers = serialNumberTable.getAllRowData();

            model.saveSerialNumbers(wrappers);

            TransferMessageText message = model.getSerialNumberCountInvalidMessage();
            if (message != null && !RConfirmUtility.confirm("Confirmation", message, model.getConfirmValues())) {
                return;
            }
            model.updateLineItemQuantites();
        }
        closeWindow();
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class TransferSerialNumberTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(TransferProperty.SERIAL_NUM_VALUE));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(4);
            attributes.add(new SimTableAttribute(model.getUINLabel(), TransferProperty.SERIAL_NUM_VALUE, new AttributeDisplayer("uin"), serialValueEditor));
            attributes.add(new SimTableAttribute("Status", TransferProperty.STATUS, new TranslatedObjectDisplayer()));
            if (model.isReceivingMode()) {
                attributes.add(new SimTableAttribute("Damaged", TransferProperty.DAMAGED, new SimTableCheckBoxRenderer(), new BooleanTableEditor()));
            }
            attributes.add(new SimTableAttribute("Action", TransferProperty.USER_ACTION, new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
