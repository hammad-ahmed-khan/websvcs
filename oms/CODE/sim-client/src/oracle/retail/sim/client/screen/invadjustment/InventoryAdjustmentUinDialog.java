package oracle.retail.sim.client.screen.invadjustment;

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
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
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
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.common.uin.UINUserAction;

/********************************************************************************************************
 * This dialog handles entering UIN information for transaction line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 3060067604335689973L;

    private InventoryAdjustmentUinDialogModel model = new InventoryAdjustmentUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");
    private RDisplayLabelEditor nonSellableEditor = new RDisplayLabelEditor("Sub-bucket");
    private RDisplayLabelEditor inventoryEditor = new RDisplayLabelEditor("Inventory");

    private RComboBoxEditor reasonEditor = new RComboBoxEditor("Reason");

    private SimTable uinTable = new SimTable(new InvAdjSerialNumberTableDefinition());
    private SimTablePane uinTablePane = new SimTablePane(uinTable);

    private SerialNumberValueTableEditor uinEditor = new SerialNumberValueTableEditor();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton restoreButton = new RButton(SimNavigation.DIALOG_RESTORE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);
    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    private static final String REASON_MODIFIED = "Reason.modified";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public InventoryAdjustmentUinDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(550, 420);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        reasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        reasonEditor.registerAction(this, REASON_MODIFIED);

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        addButton.registerAction(this, SimNavigation.DIALOG_ADD);
        removeButton.registerAction(this, SimNavigation.DIALOG_REMOVE);
        restoreButton.registerAction(this, SimNavigation.DIALOG_RESTORE);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
        closeButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        closeButton.setVisible(false);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(addButton);
        addButton(removeButton);
        addButton(restoreButton);
        addButton(cancelButton);
        addButton(closeButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));
        headerPanel.add(nonSellableEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));
        headerPanel.add(inventoryEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));
        headerPanel.add(reasonEditor, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 3, 5, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(uinTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(headerPanel);
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void setLineItemWrapper(InventoryAdjustmentLineItemWrapper wrapper) {
        model.setLineItemWrapper(wrapper);
        try {
            initializeTable();
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void initializeTable() {
        uinEditor.addTableEditorListener(buildSerialNumberValueListener());
        uinTable = new SimTable(new InvAdjSerialNumberTableDefinition());
        uinTable.setMultipleRowSelectionMode();
        uinTablePane.setTable(uinTable);
    }

    private void populateScreen() throws Exception {
        itemEditor.setData(model.getItemId());
        itemDescEditor.setData(model.getItemDescription());
        nonSellableEditor.setData(model.getNonSellableTypeDescription());
        inventoryEditor.setData(model.getInventoryDisplayText());

        reasonEditor.setActionsEnabled(false);
        reasonEditor.setItems(model.findAdjustmentReasons());
        reasonEditor.setSelectedItem(model.getAdjustmentReason());
        reasonEditor.setEnabled(model.isReasonEditable());
        reasonEditor.setActionsEnabled(true);

        boolean isSerialNumbersEditable = model.isSerialNumbersEditable();
        applyButton.setVisible(isSerialNumbersEditable);
        addButton.setVisible(isSerialNumbersEditable);
        removeButton.setVisible(isSerialNumbersEditable);
        restoreButton.setVisible(isSerialNumbersEditable);
        cancelButton.setVisible(isSerialNumbersEditable);
        closeButton.setVisible(!isSerialNumbersEditable);
        uinTable.setEnabled(isSerialNumbersEditable);
        nonSellableEditor.setVisible(model.isNonSellableTypesActive());

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
            if (command.equals(REASON_MODIFIED)) {
                doReasonModified();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
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
     * Reason Modified
     ***************************************************************************************************/
    private void doReasonModified() throws Exception {
        model.setAdjustmentReason((InventoryAdjustmentReason) reasonEditor.getSelectedItem());
        nonSellableEditor.setData(model.getNonSellableTypeDescription());
    }

    /****************************************************************************************************
     * Add Action
     ***************************************************************************************************/
    private void doAddRow() {
        uinTable.stopEditing();
        List<SerialNumberWrapper> wrappers = getAllWrappers();
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
        List<SerialNumberWrapper> selectedWrappers = getAllSelectedWrappers();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setUserAction(UINUserAction.REMOVED);
        }
        uinTable.refreshTable();
    }

    /****************************************************************************************************
     * Restore Action
     ***************************************************************************************************/

    private void doRestoreRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = getAllSelectedWrappers();
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            wrapper.setDefaultAction();
        }
        uinTable.refreshTable();
    }

    /****************************************************************************************************
      * Apply Action
      ***************************************************************************************************/
    private void doApply() throws Exception {
        if (model.isSerialNumbersEditable()) {
            List<SerialNumberWrapper> wrappers = getAllWrappers();
            model.validateUINs(wrappers);
            model.saveSerialNumbers(wrappers);
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
                    Set<String> serialNumberSet = new HashSet<String>();
                    List<SerialNumberWrapper> wrappers = getAllWrappers();
                    for (SerialNumberWrapper wrapper : wrappers) {
                        SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
                        if (serialNumber != null) {
                            if (serialNumberSet.contains(serialNumber.getUin())) {
                                uinTable.removeRow(wrapper);
                                displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getUINLabel());
                                return;
                            }
                            if (model.getAdjustmentReason() == null) {
                                uinTable.removeRow(wrapper);
                                displayException(InventoryAdjustmentMessageText.UIN_NO_REASON);
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
                    reasonEditor.setEnabled(serialNumberSet.isEmpty());
                }
            }
        };
    }

    /****************************************************************************************************
     * Validate the new serial number
     ***************************************************************************************************/
    private boolean validateSerialNumber(SerialNumberValue serialNumber) {
        try {
            model.validateSerialNumber(serialNumber);
        } catch (Exception exception) {
            displayException(exception);
            return false;
        }
        if (serialNumber.getStoreId().equals(model.getStoreId()) || serialNumber.getStatus() != UINStatus.IN_STOCK) {
            return true;
        }
        if (SimConfigManager.getBoolean(SimConfigManager.ALLOW_UNEXPECTED_UINS)) {
            return RConfirmUtility.confirm("Confirmation", UINMessageText.MOVE_STORE_CONFIRM, model.getUINLabel());
        }
        String[] values = new String[] { model.getUINLabel(), serialNumber.getUin(), serialNumber.getStatus().toString() };
        displayException(UINMessageText.UIN_AT_ANOTHER_STORE, values);
        return false;
    }

    /****************************************************************************************************
     * Helper table methods
     ***************************************************************************************************/
    private List<SerialNumberWrapper> getAllWrappers() {
        return uinTable.getAllRowData();
    }

    private List<SerialNumberWrapper> getAllSelectedWrappers() {
        return uinTable.getAllSelectedRowData();
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class InvAdjSerialNumberTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("serialNumberValue"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(3);
            attributes.add(new SimTableAttribute(model.getUINLabel(), "serialNumberValue", new AttributeDisplayer("uin"), uinEditor));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Action", "userAction", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
