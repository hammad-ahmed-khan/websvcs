package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.screen.uin.SerialNumberDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberProperty;
import oracle.retail.sim.client.screen.uin.SerialNumberValueTableEditor;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
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
import oracle.retail.sim.common.directdelivery.DirectDeliveryValidateUinCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * This dialog handles entering UIN information for transaction line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DirectDeliveryUinDialog extends SerialNumberDialog {
    private static final long serialVersionUID = -8627994647288128511L;

    private DirectDeliveryUinDialogModel model = new DirectDeliveryUinDialogModel();
    private SerialNumberValueTableEditor serialNumberEditor = new SerialNumberValueTableEditor();

    /****************************************************************************************************
     * Initialize Dialog
     ***************************************************************************************************/

    public void setDirectDeliveryLineItemWrapper(DirectDeliveryLineItemWrapper wrapper) {
        model.setDirectDeliveryLineItemWrapper(wrapper);
        try {
            initializeTable();
            populateScreen();
            setSerialNumberEditingEnabled(model.isValuesEditable());
        } catch (Throwable t) {
            displayException(t);
        }
    }

    private void initializeTable() {
        serialNumberEditor.addTableEditorListener(buildSerialNumberValueListener());
        serialNumTable = new SimTable(new UINStockLineItemTableDefinition());
        serialNumTable.setMultipleRowSelectionMode();
        serialNumTable.setColumnSize(SerialNumberProperty.DAMAGED, SimTable.LABEL_WIDTH);
        serialNumTablePane.setTable(serialNumTable);
    }

    private void populateScreen() {
        populateItemHeader(model.getItemId(), model.getItemDescription());
        serialNumTable.setRows(model.getSerialNumberWrappers());
        if (model.isValuesEditable() && !model.isAdjustment()) {
            serialNumTable.addRow(model.createUINWrapper());
        }
    }
    

    /****************************************************************************************************
     * Validate the serial number
     ***************************************************************************************************/

    protected boolean validateSerialNumber(SerialNumberValue serialNumber) {
        if (model.isDuplicateSerialNumber(serialNumber)) {
            displayException(UINMessageText.UIN_ALREADY_ENTERED, model.getUINLabel());
            return false;
        }
        try {
            DirectDeliveryValidateUinCommand command = ClientCommandFactory.createDirectDeliveryValidateUINCommand();
            command.setStoreId(model.getStoreId());
            command.setNewOnTransaction(true);
            command.setUINLabel(model.getUINLabel());
            command.setSerialNumber(serialNumber);
            command.execute();
            return true;
        } catch (Exception e) {
            displayException(e);
        }
        return false;
    }

    /****************************************************************************************************
     * Add Action
     ***************************************************************************************************/

    protected void doAddRow() {
        serialNumTable.stopEditing();
        List<SerialNumberWrapper> wrappers = serialNumTable.getAllRowData();
        for (SerialNumberWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumberValue() == null) {
                return;
            }
        }
        SerialNumberWrapper wrapper = model.createUINWrapper();
        serialNumTable.addRow(wrapper);
        if (wrapper.getSerialNumberValue() == null) {
            serialNumTable.editCellInLastRow(SerialNumberProperty.SERIAL_NUMBER);
        }
    }

    /****************************************************************************************************
     * Delete Action
     ***************************************************************************************************/

    protected void doRemoveRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = serialNumTable.getAllSelectedRowData();
        if (selectedWrappers.isEmpty()) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            if (!wrapper.isDamaged() || model.isDamagesEditable()) {
                wrapper.setUserAction(UINUserAction.REMOVED);
            }
        }
        serialNumTable.refreshTable();
    }

    /****************************************************************************************************
     * Restore Action
     ***************************************************************************************************/

    protected void doRestoreRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = serialNumTable.getAllSelectedRowData();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setDefaultAction();
        }
        serialNumTable.refreshTable();
    }

    /****************************************************************************************************
     * Done Action
     ***************************************************************************************************/

    protected void doApply() throws Exception {
        serialNumTable.stopEditing();
        if (!model.isValuesEditable()) {
            closeWindow();
            return;
        }
        List<SerialNumberWrapper> wrappers = serialNumTable.getAllRowData();
        if (model.saveSerialNumbers(wrappers)) {
            closeWindow();
        }
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class UINStockLineItemTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("serialNumberValue"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(4);
            attributes.add(new SimTableAttribute(model.getUINLabel(), "serialNumberValue", new AttributeDisplayer("uin"), serialNumberEditor));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Damaged", "damaged", new SimTableCheckBoxRenderer(), new BooleanTableEditor()));
            attributes.add(new SimTableAttribute("Action", "userAction", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
