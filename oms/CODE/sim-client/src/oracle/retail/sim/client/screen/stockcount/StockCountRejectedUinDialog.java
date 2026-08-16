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
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.uin.UINMessageText;

/********************************************************************************************************
 * This dialog handles entering UIN information for stock count line items.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountRejectedUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 4144975947275724093L;

    private StockCountRejectedUinDialogModel model = new StockCountRejectedUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

    private SimTable uinTable = new SimTable(new SerialNumberTableDefinition());
    private SimTablePane uinTablePane = new SimTablePane(uinTable);

    private StockCountUinTableEditor uinValueEditor = new StockCountUinTableEditor(true);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountRejectedUinDialog() {
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

    public void setLineItemWrapper(StockCountRejectedLineItem lineItem) throws Exception {
        model.setLineItemWrapper(lineItem);

        itemEditor.setData(model.getItemId());
        itemDescEditor.setData(model.getItemDescription());

        uinValueEditor.addTableEditorListener(buildSerialNumberTableListener());

        uinTable = new SimTable(new SerialNumberTableDefinition());
        uinTable.setSingleRowSelectionMode();
        uinTable.setTableEditable(true);
        uinTablePane.setTable(uinTable);

        uinTable.setRows(model.getSerialNumberWrappers());

        int row = uinTable.getFirstNullValueRow(StockCountProperty.SERIAL_NUMBER_VALUE);
        if (row > -1) {
            uinTable.editCellInRow(StockCountProperty.SERIAL_NUMBER_VALUE, row);
        }
    }
    
    public void stopEditing() {
        uinTable.stopEditing();
    }

    /****************************************************************************************************
     * Listen to field in order to validate UIN entered
     ***************************************************************************************************/

    private SimTableEditorListener buildSerialNumberTableListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                Set<Long> uinDetailIds = new HashSet<>();
                List<StockCountUinWrapper> wrappers = uinTable.getAllRowData();
                for (StockCountUinWrapper wrapper : wrappers) {
                    if (wrapper.getSerialNumber() != null) {
                        Long uinDetailId = wrapper.getSerialNumber().getUinDetailId();
                        if (uinDetailIds.contains(uinDetailId)) {
                            uinTable.removeRow(wrapper);
                            displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getLabel());
                            return;
                        }
                        uinDetailIds.add(uinDetailId);
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
                doAdd();
            } else if (command.equals(SimNavigation.DIALOG_REMOVE)) {
                doRemove();
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
    protected void doAdd() {
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
    protected void doRemove() throws Exception {
        StockCountUinWrapper wrapper = (StockCountUinWrapper) uinTable.getSelectedRowData();
        if (wrapper == null) {
            throw new BusinessException(CommonMessageText.NO_ROWS_SELECTED_DELETE);
        }
        uinTable.removeRow(wrapper);
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    protected void doApply() throws Exception {
        List<StockCountUinWrapper> wrappers = uinTable.getAllRowData();
        if (wrappers.isEmpty()) {
            throw new BusinessException(CommonMessageText.UIN_REQUIRED, model.getItemId());
        }
        for (StockCountUinWrapper wrapper : wrappers) {
            if (wrapper.getSerialNumber() == null || wrapper.getSerialNumber().getUinDetailId() == null) {
                return;
            }
        }
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
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute(model.getUINLabel(), StockCountProperty.SERIAL_NUMBER_VALUE, new AttributeDisplayer("serialNumber"), uinValueEditor));
            return attributes;
        }
    }
}
