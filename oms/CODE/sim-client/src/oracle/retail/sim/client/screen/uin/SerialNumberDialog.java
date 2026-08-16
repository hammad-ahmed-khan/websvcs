package oracle.retail.sim.client.screen.uin;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
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
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * This dialog handles entering UIN information for transaction line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class SerialNumberDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -4953519372214733889L;

    RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");
    RComboBoxEditor reasonEditor = new RComboBoxEditor();

    protected SimTable serialNumTable = new SimTable(new SerialNumberTableDefinition());
    protected SimTablePane serialNumTablePane = new SimTablePane(serialNumTable);

    RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    RButton restoreButton = new RButton(SimNavigation.DIALOG_RESTORE);
    RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);
    RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    private static String REASON_MODIFIED = "Reason.modified";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    protected SerialNumberDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(550, 400);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        reasonEditor.setVisible(false);
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
        headerPanel.add(reasonEditor, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(serialNumTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Stops any editing taking place in the serial number table
     ***************************************************************************************************/
    public void stopEditing() {
        serialNumTable.stopEditing();
    }

    /****************************************************************************************************
     * Populates the item header area of the UIN dialog
     ***************************************************************************************************/

    protected void populateItemHeader(String itemId, String itemDescription) {
        itemEditor.setData(itemId);
        itemDescEditor.setData(itemDescription);
    }

    /****************************************************************************************************
     * Swaps out the UIN table being displayed
     ***************************************************************************************************/

    protected void reassignTable(SimTable uinTable) {
        serialNumTable = uinTable;
        serialNumTablePane.setTable(uinTable);
    }

    /****************************************************************************************************
     * Enabled/Disable add button, delete button and table cells
     ***************************************************************************************************/

    protected void setSerialNumberEditingEnabled(boolean isEditable) {
        setSerialNumberEditingEnabled(isEditable, isEditable);
    }

    protected void setSerialNumberEditingEnabled(boolean buttonsEditable, boolean tableEditable) {
        applyButton.setVisible(buttonsEditable);
        addButton.setVisible(buttonsEditable);
        removeButton.setVisible(buttonsEditable);
        restoreButton.setVisible(buttonsEditable);
        cancelButton.setVisible(buttonsEditable);
        closeButton.setVisible(!buttonsEditable);
        serialNumTable.setTableEditable(tableEditable);
    }

    /****************************************************************************************************
     * Retrieves the reason editor
     ***************************************************************************************************/

    protected RComboBoxEditor getReasonEditor() {
        return reasonEditor;
    }

    /****************************************************************************************************
     * Listen to field in order to validate UIN entered
     ***************************************************************************************************/

    protected SimTableEditorListener buildSerialNumberValueListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                if (event.isFocusLostEvent()) {
                    Set<String> serialNumberSet = new HashSet<>();
                    List<SerialNumberWrapper> wrappers = serialNumTable.getAllRowData();
                    for (SerialNumberWrapper wrapper : wrappers) {
                        SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
                        if (serialNumber != null) {
                            if (serialNumberSet.contains(serialNumber.getUin())) {
                                serialNumTable.removeRow(wrapper);
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
                            serialNumTable.removeRow(wrapper);
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
            } else if (command.equals(REASON_MODIFIED)) {
                doReasonModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected boolean validateSerialNumber(SerialNumberValue serialNumber) {
        return true;
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected void doAddRow() throws Exception {
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected void doRemoveRow() throws Exception {
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected void doRestoreRow() throws Exception {
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected void doApply() throws Exception {
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default does nothing.
     ***************************************************************************************************/
    protected void doReasonModified() throws BusinessException {
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default stops editing and closes window.
     ***************************************************************************************************/
    private void doCancel() {
        serialNumTable.stopEditing();
        closeWindow();
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class SerialNumberTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(1);
            attributes.add(new SimTableAttribute(UINType.SERIAL.toString(), "serialNumberValue", new AttributeDisplayer("uin")));
            return attributes;
        }
    }
}
