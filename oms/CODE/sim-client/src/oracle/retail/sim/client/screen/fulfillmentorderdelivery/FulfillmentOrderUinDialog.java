package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.screen.uin.SerialNumberProperty;
import oracle.retail.sim.client.screen.uin.SerialNumberValueTableEditor;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryValidateUINCommand;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * This dialog handles displaying UIN information for fulfillment order line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FulfillmentOrderUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -8627994647288128511L;

    private FulfillmentOrderUinDialogModel model = new FulfillmentOrderUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

    private SimTable uinTable = new SimTable(new FulfillmentOrderDeliverySerialNumberTableDefinition());
    private SimTablePane uinTablePane = new SimTablePane(uinTable);

    private SerialNumberValueTableEditor uinTableEditor = new SerialNumberValueTableEditor();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton restoreButton = new RButton(SimNavigation.DIALOG_RESTORE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public FulfillmentOrderUinDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(550, 420);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        addButton.registerAction(this, SimNavigation.DIALOG_ADD);
        removeButton.registerAction(this, SimNavigation.DIALOG_REMOVE);
        restoreButton.registerAction(this, SimNavigation.DIALOG_RESTORE);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(addButton);
        addButton(removeButton);
        addButton(restoreButton);
        addButton(cancelButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(uinTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(headerPanel);
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void setLineItemWrapper(FulfillmentOrderDeliveryLineItemWrapper wrapper) {
        model.setLineItemWrapper(wrapper);
        try {
            initializeTable();
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void initializeTable() throws Exception {
        uinTableEditor.addTableEditorListener(buildSerialNumberValueListener());
        uinTable = new SimTable(new FulfillmentOrderDeliverySerialNumberTableDefinition());
        uinTable.setMultipleRowSelectionMode();
        uinTablePane.setTable(uinTable);
    }

    private void populateScreen() throws Exception {
        itemEditor.setData(model.getItemId());
        itemDescEditor.setData(model.getItemDescription());

        boolean isSerialNumbersEditable = model.isSerialNumbersEditable();
        addButton.setVisible(isSerialNumbersEditable);
        removeButton.setVisible(isSerialNumbersEditable);
        restoreButton.setVisible(isSerialNumbersEditable);
        uinTable.setEnabled(isSerialNumbersEditable);

        uinTable.setRows(model.getSerialNumberWrappers());
    }

    public void stopEditing() {
        uinTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_ADD)) {
                doAddRow();
            } else if (command.equals(SimNavigation.DIALOG_REMOVE)) {
                doRemoveRow();
            } else if (command.equals(SimNavigation.DIALOG_RESTORE)) {
                doRestoreRow();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Add Action
     ***************************************************************************************************/
    private void doAddRow() {
        uinTable.stopEditing();
        List<SerialNumberWrapper> wrappers = uinTable.getAllRowData();

        for (SerialNumberWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumberValue() == null) {
                return;
            }
        }
        uinTable.addRow(model.createSerialNumberWrapper());
        uinTable.editCellInLastRow(SerialNumberProperty.SERIAL_NUMBER);
    }

    /****************************************************************************************************
     * Remove Action
     ***************************************************************************************************/

    private void doRemoveRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = uinTable.getAllSelectedRowData();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setUserAction(UINUserAction.REMOVED);
        }
        uinTable.refreshTable();
    }

    /****************************************************************************************************
     * Restore Action
     ***************************************************************************************************/

    private void doRestoreRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = uinTable.getAllSelectedRowData();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setDefaultAction();
        }
        uinTable.refreshTable();
    }

    /****************************************************************************************************
      * Done Action
      ***************************************************************************************************/
    private void doApply() throws Exception {
        uinTable.stopEditing();

        if (model.isSerialNumbersEditable()) {
            List<SerialNumberWrapper> wrappers = uinTable.getAllRowData();
            model.validateSerialNumbers(wrappers);
            model.saveSerialNumbers(wrappers);
            model.updateLineItemQuantites();
        }
        closeWindow();
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default stops editing and closes window.
     ***************************************************************************************************/
    private void doCancel() {
        uinTable.stopEditing();
        closeWindow();
    }

    /****************************************************************************************************
     * Listen to field in order to validate UIN entered
     ***************************************************************************************************/

    protected SimTableEditorListener buildSerialNumberValueListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                if (event.isFocusLostEvent()) {
                    Set<String> serialNumberSet = new HashSet<>();
                    List<SerialNumberWrapper> wrappers = uinTable.getAllRowData();
                    for (SerialNumberWrapper wrapper : wrappers) {
                        SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
                        if (serialNumber != null) {
                            if (serialNumberSet.contains(serialNumber.getUin())) {
                                uinTable.removeRow(wrapper);
                                displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getUINLabel());
                                return;
                            }
                            if (wrapper.isValidated()) {
                                serialNumberSet.add(serialNumber.getUin());
                                continue;
                            }
                            if (validateSerialNumber(serialNumber)) {
                                serialNumberSet.add(serialNumber.getUin());
                                wrapper.setValidated();
                                continue;
                            }
                            uinTable.removeRow(wrapper);
                        }
                    }
                    // Temporary fix - only add the new row if the button panel does not have
                    if (isMouseOverButton()) {
                        return;
                    }
                    try {
                        doAddRow();
                    } catch (Throwable exception) {
                        displayException(exception);
                    }
                }
            }
        };
    }

    /****************************************************************************************************
     * Validate the new serial number
     ***************************************************************************************************/
    private boolean validateSerialNumber(SerialNumberValue serialNumber) {
        if (model.isDuplicateSerialNumber(serialNumber)) {
            displayException(UINMessageText.UIN_ALREADY_ENTERED, model.getUINLabel());
            return false;
        }
        try {
            FulfillmentOrderDeliveryValidateUINCommand command = ClientCommandFactory.createFulfillmentOrderDeliveryValidateUINCommand();
            command.setDelivery(model.getDelivery());
            command.setSerialNumber(serialNumber);
            command.setUINLabel(model.getUINLabel());
            command.setNewOnTransaction(true);
            command.execute();
        } catch (Exception exception) {
            displayException(exception);
            return false;
        }
        if (!serialNumber.getStoreId().equals(model.getStoreId())) {
            if (SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS)) {
                return RConfirmUtility.confirm("Confirmation", UINMessageText.MOVE_STORE_CONFIRM, model.getUINLabel());
            }
            String[] values = new String[3];
            values[0] = model.getUINLabel();
            values[1] = serialNumber.getUin();
            values[2] = serialNumber.getStatus().toString();
            displayException(UINMessageText.UIN_AT_ANOTHER_STORE, values);
            return false;
        }

        return true;
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class FulfillmentOrderDeliverySerialNumberTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("serialNumberValue"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute(model.getUINLabel(), "serialNumberValue", new AttributeDisplayer("uin"), uinTableEditor));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Action", "userAction", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
