package oracle.retail.sim.client.screen.customuin;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.screen.uin.SerialNumberProperty;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
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
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;

/********************************************************************************************************
 * This dialog handles entering UIN information for line item from the main UIN Create screen.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUinCreateDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -7885503433032637549L;

    private CustomUinCreateDialogModel model = new CustomUinCreateDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

    private CustomUINCreateDialogTableEditor serialNumberEditor = new CustomUINCreateDialogTableEditor();

    private SimTable serialNumberTable = new SimTable(new CreateUINTableDefinition());
    private SimTablePane serialNumberTablePane = new SimTablePane(serialNumberTable);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public CustomUinCreateDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(550, 400);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
        serialNumberEditor.addTableEditorListener(buildSerialNumberValueListener());

        serialNumberTable.setMultipleRowSelectionMode();

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        addButton.registerAction(this, SimNavigation.DIALOG_ADD);
        removeButton.registerAction(this, SimNavigation.DIALOG_REMOVE);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(addButton);
        addButton(removeButton);
        addButton(cancelButton);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(serialNumberTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/

    public void setWrapper(CustomUINCreateWrapper wrapper) {
        model.setWrapper(wrapper);
        try {
            itemEditor.setData(model.getItemId());
            itemDescEditor.setData(model.getItemDescription());
            serialNumberTable.setRows(model.getSerialNumberWrappers());
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
    
    public void stopEditing() {
        serialNumberTable.stopEditing();
    }

    /****************************************************************************************************
     * Listen to serial number entry field in order to validate UIN entered
     ***************************************************************************************************/

    private SimTableEditorListener buildSerialNumberValueListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                if (event.isFocusLostEvent()) {
                    Set<String> serialNumberSet = new HashSet<>();
                    List<SerialNumberWrapper> wrappers = serialNumberTable.getAllRowData();
                    for (SerialNumberWrapper wrapper : wrappers) {
                        SerialNumberValue serialNumber = wrapper.getSerialNumberValue();
                        if (serialNumber != null) {
                            if (serialNumberSet.contains(serialNumber.getUin())) {
                                serialNumberTable.removeRow(wrapper);
                                displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getUINLabel());
                                continue;
                            }
                            if (serialNumber.getStatus() != UINStatus.UNCONFIRMED) {
                                serialNumberTable.removeRow(wrapper);
                                displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getUINLabel());
                                continue;
                            }
                            serialNumberSet.add(serialNumber.getUin());
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
        try {
            switch (event.getEventCommand()) {
                case SimNavigation.DIALOG_APPLY:
                    doApply();
                    break;
                case SimNavigation.DIALOG_ADD:
                    doAddRow();
                    break;
                case SimNavigation.DIALOG_REMOVE:
                    doRemoveRow();
                    break;
                case SimNavigation.DIALOG_CANCEL:
                    doCancel();
                    break;
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Add Action
     ***************************************************************************************************/

    private void doAddRow() {
        serialNumberTable.stopEditing();
        List<SerialNumberWrapper> wrappers = serialNumberTable.getAllRowData();
        for (SerialNumberWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumberValue() == null) {
                return;
            }
        }
        serialNumberTable.addRow(model.createSerialNumberWrapper());
        serialNumberTable.editCellInLastRow(SerialNumberProperty.SERIAL_NUMBER);
    }

    /****************************************************************************************************
     * Delete Action
     ***************************************************************************************************/

    private void doRemoveRow() throws Exception {
        List<SerialNumberWrapper> selectedWrappers = serialNumberTable.getAllSelectedRowData();
        if (selectedWrappers.isEmpty()) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        for (SerialNumberWrapper wrapper : selectedWrappers) {
            serialNumberTable.removeRow(wrapper);
        }
        serialNumberTable.refreshTable();
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/

    private void doApply() {
        serialNumberTable.stopEditing();
        List<SerialNumberWrapper> wrappers = serialNumberTable.getAllRowData();
        model.saveSerialNumbers(wrappers);
        closeWindow();
    }

    /****************************************************************************************************
     * Override method to alter functionality. Default stops editing and closes window.
     ***************************************************************************************************/
    private void doCancel() {
        serialNumberTable.stopEditing();
        closeWindow();
    }

    /****************************************************************************************************
     * UIN Table Definition
     ***************************************************************************************************/

    private class CreateUINTableDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return SerialNumberWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("serialNumberValue"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(1);
            attributes.add(new SimTableAttribute(model.getUINLabel(), "serialNumberValue", new AttributeDisplayer("uin"), serialNumberEditor));
            return attributes;
        }
    }
}
