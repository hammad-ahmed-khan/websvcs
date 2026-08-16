package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.displayer.ItemIdDescriptionDisplayer;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.IntegerDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.PositiveIntegerTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.stockcount.StockCountProperty;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINType;

/********************************************************************************************************
 * Stock Count Multiple Location Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeUinDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -731054068184120047L;

    private StockCountAuthorizeUinDialogModel model = new StockCountAuthorizeUinDialogModel();

    private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor countedEditor = new RDisplayLabelEditor("Total Counted Qty");
    private RDisplayLabelEditor recountedEditor = new RDisplayLabelEditor("Total Re-Counted Qty");
    private RDisplayLabelEditor stockOnHandEditor = new RDisplayLabelEditor("SOH");
    private RDisplayLabelEditor authorizedEditor = new RDisplayLabelEditor("Total Authorized Qty");

    private RComboBoxEditor countedFilterEditor = new RComboBoxEditor("Count Qty");
    private StockCountUinTableEditor serialNumberTableEditor = new StockCountUinTableEditor(true);

    private SimTable lineDetailTable = new SimTable(new SerialNumberAuthorizeTableDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineDetailTable);

    private static final String FILTER_MODIFIED = "Filter.modified";

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton addButton = new RButton(SimNavigation.DIALOG_ADD);
    private RButton removeButton = new RButton(SimNavigation.DIALOG_REMOVE);
    private RButton generateButton = new RButton(SimNavigation.DIALOG_AUTO_GENERATE);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public StockCountAuthorizeUinDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Authorization Detail");
        setSize(750, 550);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        itemEditor.setDisplayer(new ItemIdDescriptionDisplayer());

        countedFilterEditor.setDisplayer(new TranslatedObjectDisplayer());
        countedFilterEditor.setItems(model.getFilterOptions());
        countedFilterEditor.removeEmptySelection();
        countedFilterEditor.setSelectedItem(ItemCountType.COUNTED);
        countedFilterEditor.registerAction(this, FILTER_MODIFIED);

        serialNumberTableEditor.addTableEditorListener(buildSerialNumberValueListener());

        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        addButton.registerAction(this, SimNavigation.DIALOG_ADD);
        removeButton.registerAction(this, SimNavigation.DIALOG_REMOVE);
        generateButton.registerAction(this, SimNavigation.DIALOG_AUTO_GENERATE);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(addButton);
        addButton(removeButton);
        addButton(generateButton);
        addButton(cancelButton);

        REditorPanel headerPanel = new REditorPanel(4, 2);
        headerPanel.add(itemEditor);
        headerPanel.add(countedEditor);
        headerPanel.add(recountedEditor);
        headerPanel.add(countedFilterEditor);
        headerPanel.skip();
        headerPanel.add(stockOnHandEditor);
        headerPanel.add(authorizedEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assign Values
     ***************************************************************************************************/

    public void setStockCount(StockCountWrapper stockCount) {
        model.setStockCount(stockCount);
    }

    public void setLineItem(StockCountLineItemWrapper lineItem) throws Exception {
        model.setLineItem(lineItem);

        List<StockCountAuthorizeUinWrapper> lineItems = model.getLineItems();

        itemEditor.setData(model.getStockCountItem());
        countedEditor.setData(model.getTotalCountedDisplayValue(lineItems));

        if (model.isRecountRequired()) {
            recountedEditor.setData(model.getTotalRecountedDisplayValue(lineItems));
        }

        stockOnHandEditor.setData(model.getStockOnHandDisplayValue());
        authorizedEditor.setData(model.getAuthorizedQuantityDisplayValue(lineItems));

        boolean isSerialNumbersEditable = model.isSerialNumbersEditable();

        recountedEditor.setVisible(model.isRecountRequired());
        addButton.setVisible(isSerialNumbersEditable);
        removeButton.setVisible(isSerialNumbersEditable);
        generateButton.setVisible(model.getUINType() == UINType.AGSN);

        resetLineItemTable();

        lineDetailTable.setTableEditable(isSerialNumbersEditable);
        lineDetailTable.setRows(model.filterLineItems(ItemCountType.COUNTED));
    }

    private void resetLineItemTable() {
        lineDetailTable = new SimTable(new SerialNumberAuthorizeTableDefinition());
        lineDetailTable.setColumnSize("countQuantity", EditorConstants.COLUMN_LABEL_WIDTH);
        if (model.isRecountRequired()) {
            lineDetailTable.setColumnSize("recountQuantity", EditorConstants.COLUMN_LABEL_WIDTH);
        }
        lineDetailTable.setColumnSize("approvedQuantity", EditorConstants.COLUMN_LABEL_WIDTH);
        lineDetailTable.setSingleRowSelectionMode();

        lineItemPane.setTable(lineDetailTable);
    }
    
    public void stopEditing() {
        lineDetailTable.stopEditing();
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            stopEditing();
            if (command.equals(FILTER_MODIFIED)) {
                doFilterModified();
            } else if (command.equals(SimNavigation.DIALOG_AUTO_GENERATE)) {
                doAutoGenerate();
            } else if (command.equals(SimClientStateKey.AGSN_TO_GENERATE)) {
                doGenerateAGSNs(event.getEventData());
            } else if (command.equals(SimNavigation.DIALOG_ADD)) {
                doAddSerialNumber();
            } else if (command.equals(SimNavigation.DIALOG_REMOVE)) {
                doRemoveSerialNumber();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.CANCEL)) {
                doCancel();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Filter Combo Modified
     ***************************************************************************************************/

    private void doFilterModified() {
        ItemCountType itemCountType = (ItemCountType) countedFilterEditor.getSelectedItem();
        lineDetailTable.setRows(model.filterLineItems(itemCountType));
    }

    /****************************************************************************************************
     * Auto Generate
     ***************************************************************************************************/

    private void doAutoGenerate() throws Exception {
        StockCountGenerateUinDialog dialog = new StockCountGenerateUinDialog(this);
        dialog.addREventListener(this);
        dialog.setVisible(true);
    }

    private void doGenerateAGSNs(Object value) throws Exception {
        if (value instanceof Integer) {
            Integer quantityToGenerate = (Integer) value;
            if (quantityToGenerate > 0) {
                if (!model.checkStockCountChildLock()) {
                    displayException(CommonMessageText.LOCK_TAKEN_OVER);
                    return;
                }
                model.generateNewSerialNumbers(quantityToGenerate);
                doFilterModified();
            }
        }
    }

    /****************************************************************************************************
     * Add UIN
     ***************************************************************************************************/

    private void doAddSerialNumber() {
        lineDetailTable.addRow(model.createNewWrapper());
    }

    /****************************************************************************************************
     * Delete UIN
     ***************************************************************************************************/

    private void doRemoveSerialNumber() {
        StockCountAuthorizeUinWrapper wrapper = (StockCountAuthorizeUinWrapper) lineDetailTable.getSelectedRowData();
        if (wrapper == null) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (model.isValidForDelete(wrapper)) {
            lineDetailTable.removeRow(wrapper);
            model.removeSerialNumber(wrapper);
        }
    }

    /****************************************************************************************************
     * Save Information
     ***************************************************************************************************/

    private void doApply() throws Exception {
        if (!model.checkStockCountChildLock()) {
            displayException(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        model.saveSerialNumberDetails();
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Window
     ***************************************************************************************************/

    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * Serial Number Listeners
     ***************************************************************************************************/

    protected SimTableEditorListener buildSerialNumberValueListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                Set<String> serialNumberSet = new HashSet<>();
                List<StockCountAuthorizeUinWrapper> wrappers = model.getActiveLineItems();
                for (StockCountAuthorizeUinWrapper wrapper : wrappers) {
                    StockCountSerialNumber serialNumber = wrapper.getSerialNumber();
                    if (serialNumber != null) {
                        if (serialNumberSet.contains(serialNumber.getSerialNumber())) {
                            model.removeSerialNumber(wrapper);
                            lineDetailTable.removeRow(wrapper);
                            displayException(UINMessageText.UIN_ALREADY_ENTERED, wrapper.getLabel());
                            return;
                        }
                        serialNumberSet.add(serialNumber.getSerialNumber());
                    }
                }
            }
        };
    }

    /****************************************************************************************************
     * Stock Count Line Item Display Definition
     ***************************************************************************************************/

    private class SerialNumberAuthorizeTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountAuthorizeUinWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            List<SimTableSortAttribute> sortAttributes = new ArrayList<SimTableSortAttribute>(2);
            sortAttributes.add(new SimTableSortAttribute("location"));
            sortAttributes.add(new SimTableSortAttribute(StockCountProperty.SERIAL_NUMBER_VALUE));
            return sortAttributes;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
            attributes.add(new SimTableAttribute("Location", "location"));
            attributes.add(new SimTableAttribute(model.getUINLabel(), StockCountProperty.SERIAL_NUMBER_VALUE, new AttributeDisplayer("serialNumber"), serialNumberTableEditor));
            attributes.add(new SimTableAttribute("UIN Status", "uINStatus", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Count Qty", "countQuantity"));
            if (model.isRecountRequired()) {
                attributes.add(new SimTableAttribute("Re-Count Qty", "recountQuantity"));
            }
            attributes.add(new SimTableAttribute("Auth Qty", "approvedQuantity", new IntegerDisplayer(), new PositiveIntegerTableEditor(1)));
            return attributes;
        }
    }
}
