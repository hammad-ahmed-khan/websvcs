package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
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
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.stockcount.StockCountProperty;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.UINMessageText;

/********************************************************************************************************
 * This dialog handles entering UIN information for stock count line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -492949236154373478L;

    private StockCountUinDialogModel model = new StockCountUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

    private SimTable uinTable = new SimTable(new SerialNumberTableDefinition());
    private SimTablePane uinTablePane = new SimTablePane(uinTable);

    private StockCountUinTableEditor uinTableEditor = new StockCountUinTableEditor();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountUinDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("UIN");
        setSize(500, 400);
        initializeWidgets();
        layoutContent();
        centerWindow();
    }

    private void initializeWidgets() {
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
        mainPanel.add(uinTablePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Initialize Dialog
     ***************************************************************************************************/

    public void setLineItemWrapper(StockCountLineItemWrapper wrapper) {
        model.setLineItemWrapper(wrapper);

        itemEditor.setData(model.getItemId());
        itemDescEditor.setData(model.getItemDescription());

        uinTableEditor.addTableEditorListener(buildSerialNumberListener());

        uinTable = new SimTable(new SerialNumberTableDefinition());
        uinTable.setSingleRowSelectionMode();
        uinTable.setTableEditable(true);

        uinTablePane.setTable(uinTable);

        uinTable.setRows(model.getStockCountSerialNumberWrappers());

        doAddRow();
    }

    public void setSerialNumbersEditable(boolean isEditable) {
        uinTable.setTableEditable(isEditable);
        addButton.setVisible(isEditable);
        removeButton.setVisible(isEditable);
        applyButton.setVisible(isEditable);
    }

    public void stopEditing() {
        uinTable.stopEditing();
    }

    /****************************************************************************************************
     * Listen to field in order to validate UIN entered
     ***************************************************************************************************/

    private SimTableEditorListener buildSerialNumberListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                if (event.isFocusLostEvent()) {
                    Set<Long> serialNumberSet = new HashSet<>();
                    List<StockCountUinWrapper> wrappers = uinTable.getAllRowData();
                    for (StockCountUinWrapper wrapper : wrappers) {
                        if (wrapper.getSerialNumber() != null) {
                            StockCountSerialNumber serialNumber = wrapper.getSerialNumber();
                            if (serialNumberSet.contains(serialNumber.getUinDetailId())) {
                                uinTable.removeRow(wrapper);
                                displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getLabel());
                                return;
                            }
                            serialNumberSet.add(serialNumber.getUinDetailId());
                        }
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
                doDone();
            } else if (command.equals(SimNavigation.DIALOG_ADD)) {
                doAddRow();
            } else if (command.equals(SimNavigation.DIALOG_REMOVE)) {
                doDeleteRow();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Add Row Action
     ***************************************************************************************************/
    protected void doAddRow() {
        List<StockCountUinWrapper> wrappers = uinTable.getAllRowData();
        for (StockCountUinWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumber() == null) {
                return;
            }
        }
        uinTable.addRow(model.createNewWrapper());
        uinTable.editCellInLastRow(StockCountProperty.SERIAL_NUMBER_VALUE);
    }

    /****************************************************************************************************
     * Delete Action
     ***************************************************************************************************/
    protected void doDeleteRow() throws Exception {
        StockCountUinWrapper wrapper = (StockCountUinWrapper) uinTable.getSelectedRowData();
        if (wrapper == null) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        if (model.removeSerialNumber(wrapper)) {
            uinTable.removeRow(wrapper);
        }
    }

    /****************************************************************************************************
     * Done Action
     ***************************************************************************************************/
    protected void doDone() throws Exception {
        List<StockCountUinWrapper> wrappers = uinTable.getAllRowData();
        model.saveSerialNumbers(wrappers);
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
     * Serial Number Table Definition
     ***************************************************************************************************/

    private class SerialNumberTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountUinWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(1);
            attributes.add(new SimTableAttribute(model.getSerialNumberLabel(), StockCountProperty.SERIAL_NUMBER_VALUE, new AttributeDisplayer("serialNumber"), uinTableEditor));
            return attributes;
        }
    }
}
