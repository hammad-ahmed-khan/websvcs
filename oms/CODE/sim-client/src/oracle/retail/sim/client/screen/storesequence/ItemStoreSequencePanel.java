package oracle.retail.sim.client.screen.storesequence;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreSequenceAreaDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.StoreSequenceAreaTableEditor;
import oracle.retail.sim.client.tableeditor.TicketTypeFormatTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;

/********************************************************************************************************
 * Item Store Sequence Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemStoreSequencePanel extends ScreenPanel implements SimTableEditorListener {
    private static final long serialVersionUID = -3295704949070762501L;

    private ItemStoreSequenceModel model = new ItemStoreSequenceModel();

    private RDisplayLabelEditor itemIdEditor = new RDisplayLabelEditor("Item");
    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Item Description");

    private StoreSequenceAreaTableEditor sequenceAreaTableEditor = new StoreSequenceAreaTableEditor();
    private TicketTypeFormatTableEditor shelfLabelTableEditor = new TicketTypeFormatTableEditor();
    private BooleanTableEditor primaryTableEditor = new BooleanTableEditor();

    private SimTable sequenceItemTable = new SimTable(new SequenceItemTableDefinition());
    private SimTablePane sequenceItemPane = new SimTablePane(sequenceItemTable);

    public ItemStoreSequencePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        sequenceItemTable.setColumnSize(StoreSequenceProperty.PRIMARY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.CAPACITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.WIDTH, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.QUANTITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setTableEditable(true);
        primaryTableEditor.addTableEditorListener(this);
        shelfLabelTableEditor.setDisplayer(new TranslatedObjectDisplayer());
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 2);
        headerPanel.add(itemIdEditor);
        headerPanel.add(descriptionEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(sequenceItemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return sequenceItemTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        StockItem stockItem = model.loadStockItem();
        itemIdEditor.setData(stockItem.getId());
        descriptionEditor.setData(stockItem.getItemDescription());
        shelfLabelTableEditor.setTicketTypeFormats(model.getShelfLabelFormats());
        sequenceItemTable.setTableEditable(!isActiveAllItemCount());
        if (isItemSequenceUnmodifiable()) {
            sequenceItemTable.setTableEditable(false);
            sequenceAreaTableEditor.setEnabled(false);
            shelfLabelTableEditor.setEnabled(false);
        }
        sequenceItemTable.setRows(model.findStoreSequenceItems());
        sequenceAreaTableEditor.setSequenceAreas(getAvailableSequenceAreas());
        validateNoAreaRow();
    }

    public void stop() {
        model.releaseAllSequenceLocks();
    }

    public boolean isActiveAllItemCount() {
        return model.isActiveAllLocationCount();
    }

    public boolean isItemSequenceUnmodifiable() {
        return model.isSequencingUnmodifiable();
    }

    /****************************************************************************************************
     * Validate The No Location Row And Handle It
     ***************************************************************************************************/

    private void validateNoAreaRow() throws Exception {
        if (sequenceItemTable.isEmpty()) {
            sequenceItemTable.addRow(model.createNewStoreSequenceItem());
        }
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleRemoveSequence() throws Exception {
        sequenceItemTable.stopEditing();

        if (sequenceItemTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }

        List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllSelectedRowData();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (wrapper.getSequenceArea() != null && wrapper.getSequenceArea().isNotSequenced()) {
                return;
            }
        }

        if (!RConfirmUtility.confirm("Delete Confirmation", StoreSequenceMessageText.DELETE_LOCATION_CONFIRM)) {
            return;
        }

        for (StoreSequenceItemWrapper wrapper : wrappers) {
            model.removeSequenceItem(wrapper);
            sequenceItemTable.removeRow(wrapper);
        }

        List<StoreSequenceArea> sequenceAreas = getAvailableSequenceAreas();
        sequenceAreaTableEditor.setSequenceAreas(sequenceAreas);
    }

    /****************************************************************************************************
     * Handle Add Location - If no locations, do nothing
     ***************************************************************************************************/

    public void handleAddSequence() throws Exception {
        sequenceItemTable.stopEditing();

        List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (wrapper.getSequenceArea() == null) {
                throw new BusinessException(StoreSequenceMessageText.MISSING_LOCATION);
            }
        }
        if (wrappers.size() == 1) {
            StoreSequenceItemWrapper wrapper = wrappers.get(0);
            if (wrapper.getSequenceArea().isNotSequenced()) {
                sequenceItemTable.clearRows();
            }
        }
        List<StoreSequenceArea> sequenceAreas = getAvailableSequenceAreas();
        sequenceAreaTableEditor.setSequenceAreas(sequenceAreas);
        if (sequenceAreas.isEmpty()) {
            return;
        }
        sequenceItemTable.addRow(model.createNewStoreSequenceItem());
        sequenceItemTable.editCellInLastRow("storeSequence");
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
        boolean hasPrimaryLocation = false;
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (wrapper.getSequenceArea() == null) {
                throw new BusinessException(StoreSequenceMessageText.MISSING_LOCATION);
            }
            if (wrapper.isPrimary()) {
                hasPrimaryLocation = true;
            }
        }
        if (wrappers.size() > 0 && !hasPrimaryLocation) {
            displayError(StoreSequenceMessageText.NO_PRIMARY_LOCATION);
            return false;
        }
        if (model.isOnlyInNoSequencedArea(wrappers)) {
            if (!RConfirmUtility.confirm("Confirmation", StoreSequenceMessageText.NO_LOCATION_SAVE_CONFIRM)) {
                return false;
            }
        }
        model.updateStoreSequenceItems(wrappers);
        return true;
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private List<StoreSequenceArea> getAvailableSequenceAreas() throws Exception {
        List<StoreSequenceArea> availableSequenceAreas = model.getStoreSequences();
        List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            availableSequenceAreas.remove(wrapper.getSequenceArea());
        }
        return availableSequenceAreas;
    }

    /****************************************************************************************************
     * Table Editor Event Receiver
     ***************************************************************************************************/

    public void performTableEditorEvent(SimTableEditorEvent event) {
        if (event.getTableEditor() == primaryTableEditor) {
            BooleanTableEditor booleanEditor = (BooleanTableEditor) event.getTableEditor();
            if (booleanEditor.getBoolean()) {
                try {
                    StoreSequenceItemWrapper alteredWrapper = (StoreSequenceItemWrapper) sequenceItemTable.getSelectedRowData();
                    List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
                    for (StoreSequenceItemWrapper rowWrapper : wrappers) {
                        if (rowWrapper != alteredWrapper) {
                            if (rowWrapper.isPrimary()) {
                                rowWrapper.setPrimary(Boolean.FALSE);
                            }
                        }
                    }
                    sequenceItemTable.refreshTable();
                } catch (Throwable exception) {
                    displayException(exception);
                }
            }
        }
    }

    /****************************************************************************************************
     * Sequence Item Table Definition
     ***************************************************************************************************/

    private class SequenceItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreSequenceItemWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("primary", false));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(7);
            attributes.add(new SimTableAttribute("Location", StoreSequenceProperty.SEQUENCE_AREA, new StoreSequenceAreaDisplayer(), sequenceAreaTableEditor));
            attributes.add(new SimTableAttribute("Primary", StoreSequenceProperty.PRIMARY, new BooleanDisplayer(), primaryTableEditor));
            attributes.add(new SimTableAttribute("Capacity", StoreSequenceProperty.CAPACITY, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Width", StoreSequenceProperty.WIDTH, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("UOM", StoreSequenceProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Label Format", StoreSequenceProperty.LABEL_FORMAT, new TranslatedObjectDisplayer(), shelfLabelTableEditor));
            attributes.add(new SimTableAttribute("Label Qty", StoreSequenceProperty.QUANTITY));
            return attributes;
        }
    }
}
