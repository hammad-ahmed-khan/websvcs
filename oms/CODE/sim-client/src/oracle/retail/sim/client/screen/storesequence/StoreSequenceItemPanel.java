package oracle.retail.sim.client.screen.storesequence;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.TicketTypeFormatTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;

/********************************************************************************************************
 * Sequence Item List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceItemPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = 5505281881513277094L;

    private StoreSequenceItemModel model = new StoreSequenceItemModel();

    private RDisplayLabelEditor sequenceEditor = new RDisplayLabelEditor("Location");
    private RDisplayLabelEditor totalItemsEditor = new RDisplayLabelEditor("Total Items");
    private RDisplayLabelEditor areaTypeEditor = new RDisplayLabelEditor("Shopfloor/Backroom");

    private StockItemTableEditor stockItemEditor = new StockItemTableEditor();
    private TicketTypeFormatTableEditor ticketTypeEditor = new TicketTypeFormatTableEditor();

    private SimTable sequenceItemTable = new SimTable(new StoreSequenceItemDefinition());
    private SimTablePane sequenceItemPane = new SimTablePane(sequenceItemTable);

    private static final String SEQUENCE_ITEM_SELECTED = "SequenceItem.selected";

    private StockItemScannerDialog scannerDialog = null;

    /****************************************************************************************************
     * Initialization & Layout
     ***************************************************************************************************/

    public StoreSequenceItemPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        areaTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        sequenceItemTable.setColumnSize(StoreSequenceProperty.CAPACITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.WIDTH, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.QUANTITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.MULTI_AREA, SimTable.LABEL_WIDTH);
        sequenceItemTable.registerDoubleClickAction(this, SEQUENCE_ITEM_SELECTED);
        stockItemEditor.setSearchListener(buildItemSearchListener());
        stockItemEditor.addTableEditorListener(createStockItemTableListener());
        stockItemEditor.setAllowNonInventoryItems(true);
        ticketTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1, 3);
        headerPanel.add(sequenceEditor);
        headerPanel.add(totalItemsEditor);
        headerPanel.add(areaTypeEditor);

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
        model.loadStoreSequence();

        StoreSequenceArea sequenceArea = model.getSequenceArea();
        sequenceEditor.setData(sequenceArea.getDescription());
        areaTypeEditor.setData(sequenceArea.getAreaType());

        ticketTypeEditor.setItems(model.getShelfLabelFormats());

        sequenceItemTable.setTableEditable(model.isEditMode());
        sequenceItemTable.setRows(model.getSequenceItems());

        totalItemsEditor.setData(sequenceItemTable.getRowCount());

        if (isSequencingUnmodifiable()) {
            sequenceItemTable.setTableEditable(false);
            ticketTypeEditor.setEnabled(false);
            stockItemEditor.setEnabled(false);
        }
        launchScanner();
    }

    public void resume() {
    }

    public void stop() {
        releaseActivityLock();
        shutdownScanner();
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isSequencingUnmodifiable() {
        return model.isSequencingUnmodifiable();
    }

    public boolean isEditMode() {
        return model.isEditMode();
    }

    public boolean isAllItemStockCountActive() {
        return model.isAllItemStockCountActive();
    }

    public boolean isNotSequenced() {
        return model.isNotSequenced();
    }

    /****************************************************************************************************
     * Scanner Methods
     ***************************************************************************************************/

    public boolean isScannerAvailable() {
        return model.isScannerAvailable();
    }

    private void launchScanner() {
        if (model.isScannerAvailable()) {
            if (model.isScannerAutoDisplay()) {
                displayScanner();
            }
        }
    }

    private void displayScanner() {
        if (model.isScannerAvailable()) {
            if (scannerDialog == null) {
                scannerDialog = new StockItemScannerDialog();
                scannerDialog.setItemProcessor(this);
            }
            scannerDialog.setVisible(true);
        }
    }

    private void shutdownScanner() {
        if (scannerDialog != null) {
            scannerDialog.setVisible(false);
            scannerDialog = null;
        }
    }

    public void processBarcodeItem(BarcodeItem barcodeItem) {
        sequenceItemTable.stopEditing();
        try {
            List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();

            StoreSequenceItemWrapper existingWrapper = findExistingWrapper(wrappers, barcodeItem.getId());
            if (existingWrapper != null) {
                model.updateExistingLineItem(existingWrapper);
                return;
            }
            StoreSequenceItemWrapper emptyItemWrapper = findEmptyWrapper(wrappers);
            if (emptyItemWrapper != null) {
                emptyItemWrapper.setStockItem(barcodeItem.getStockItem());
                model.updateExistingLineItem(emptyItemWrapper);
                return;
            }
            StoreSequenceItemWrapper createdWrapper = model.createNewSequenceItem(wrappers);
            if (createdWrapper != null) {
                createdWrapper.setStockItem(barcodeItem.getStockItem());
                model.updateExistingLineItem(createdWrapper);
                sequenceItemTable.addRow(createdWrapper);
                totalItemsEditor.setData(sequenceItemTable.getRowCount());
            }
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            sequenceItemTable.refreshTable();
        }
    }

    private StoreSequenceItemWrapper findExistingWrapper(List<StoreSequenceItemWrapper> wrappers, String itemId) {
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (itemId.equals(wrapper.getStoreSequenceItem().getItemId())) {
                return wrapper;
            }
        }
        return null;
    }

    private StoreSequenceItemWrapper findEmptyWrapper(List<StoreSequenceItemWrapper> wrappers) {
        for (StoreSequenceItemWrapper wrapper : wrappers) {
            if (wrapper.getStoreSequenceItem().getItemId() == null) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Add
     ***************************************************************************************************/

    public void handleAdd() throws Exception {
        List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();

        if (model.containsBlankSequenceItem(wrappers)) {
            displayError(StoreSequenceMessageText.LOCATION_EMPTY_ERROR);
            return;
        }

        StoreSequenceItemWrapper wrapper = model.createNewSequenceItem(wrappers);

        if (wrapper != null) {
            sequenceItemTable.stopEditing();
            sequenceItemTable.addRow(wrapper);
            sequenceItemTable.editCellInLastRow("stockItem");

            totalItemsEditor.setData(sequenceItemTable.getRowCount());
        }
    }

    /****************************************************************************************************
     * Handle Remove Item
     ***************************************************************************************************/

    public void handleRemoveItem() throws Exception {
        if (sequenceItemTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (RConfirmUtility.confirm("Delete Confirmation", StoreSequenceMessageText.DELETE_ITEM_CONFIRM)) {
            sequenceItemTable.stopEditing();

            List<StoreSequenceItemWrapper> deletedRows = sequenceItemTable.getAllSelectedRowData();
            for (StoreSequenceItemWrapper wrapper : deletedRows) {
                model.removeItem(wrapper);
                sequenceItemTable.removeRow(wrapper);
            }
            List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
            model.updateSequenceOrder(wrappers);
            totalItemsEditor.setData(sequenceItemTable.getRowCount());
        }
    }

    /****************************************************************************************************
     * Handle Move Up
     ***************************************************************************************************/

    public void handleMoveUp() throws Exception {
        if (model.isEditMode()) {
            sequenceItemTable.stopEditing();

            int firstRow = sequenceItemTable.getSelectedRow();
            if (firstRow < 0) {
                return;
            }
            int[] rows = sequenceItemTable.getSelectedRows();
            int beforeIndex = firstRow - 1;
            if (beforeIndex < 0) {
                return;
            }

            StoreSequenceItemWrapper beforeWrapper = (StoreSequenceItemWrapper) sequenceItemTable.getRowData(beforeIndex);
            StoreSequenceItemWrapper moveWrapper = null;

            for (int row : rows) {
                moveWrapper = (StoreSequenceItemWrapper) sequenceItemTable.getRowData(row);
                moveWrapper.swapSequenceOrder(beforeWrapper);
            }

            sequenceItemTable.setRows(model.getSequenceItems());
            sequenceItemTable.setRowSelectionInterval(beforeIndex, beforeIndex + rows.length - 1);
        }
    }

    /****************************************************************************************************
     * Handle Move Down
     ***************************************************************************************************/

    public void handleMoveDown() throws Exception {
        if (model.isEditMode()) {
            sequenceItemTable.stopEditing();

            if (sequenceItemTable.getSelectedRow() < 0) {
                return;
            }

            int[] rows = sequenceItemTable.getSelectedRows();
            int lastSelectedRow = rows[rows.length - 1];
            int afterIndex = lastSelectedRow + 1;
            if (afterIndex > sequenceItemTable.getRowCount() - 1) {
                return;
            }

            StoreSequenceItemWrapper afterWrapper = (StoreSequenceItemWrapper) sequenceItemTable.getRowData(afterIndex);
            StoreSequenceItemWrapper moveWrapper = null;

            for (int i = rows.length - 1; i >= 0; i--) {
                moveWrapper = (StoreSequenceItemWrapper) sequenceItemTable.getRowData(rows[i]);
                moveWrapper.swapSequenceOrder(afterWrapper);
            }

            sequenceItemTable.setRows(model.getSequenceItems());
            sequenceItemTable.setRowSelectionInterval(afterIndex - rows.length + 1, afterIndex);
        }
    }

    /****************************************************************************************************
     * Handle Scanner
     ***************************************************************************************************/

    public void handleScanner() {
        sequenceItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Apply Item List
     ***************************************************************************************************/

    public void handleApplyItemList() throws Exception {
        if (!model.getSequenceArea().isCreatedFromHierarchy()) {
            displayError(StoreSequenceMessageText.NOT_HIERARCHY_CREATED);
            return;
        }
        if (RConfirmUtility.confirm("Confirmation", StoreSequenceMessageText.GENERATE_ITEM_CONFIRM)) {
            model.applyItemList();
            sequenceItemTable.setRows(model.getSequenceItems());
            totalItemsEditor.setData(sequenceItemTable.getRowCount());
        }
    }

    /****************************************************************************************************
     * Handle Edit Item Locations
     ***************************************************************************************************/

    public void handleEditItemLocations() throws Exception {
        if (model.obtainSequenceAreaLock()) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_EDIT_MODE, Boolean.TRUE);
        } else {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_EDIT_MODE);
        }
    }

    /****************************************************************************************************
     * Handle Save - Returns true if should perform cancel window after done, false if should remain
     ***************************************************************************************************/

    public boolean handleSave() {
        if (model.isEditMode()) {
            sequenceItemTable.stopEditing();
            try {
                if (!model.checkSequenceAreaLock()) {
                    displayError(CommonMessageText.LOCK_TAKEN_OVER);
                    return true;
                }
                List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
                model.updateSequenceOrder(wrappers);
                model.updateStoreSequenceItems(wrappers);
            } catch (BusinessException be) {
                displayException(be);
                return false;
            } catch (Throwable tex) {
                displayException(tex);
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void releaseActivityLock() {
        if (model.isEditMode()) {
            try {
                model.releaseSequenceAreaLock();
            } catch (Throwable e) {
                LogService.error(this, e.getMessage());
            }
        }
    }

    public void clearScreen() {
        sequenceItemTable.stopEditing();
        sequenceItemTable.clearRows();
        sequenceEditor.clear();
        totalItemsEditor.clear();
        areaTypeEditor.clear();
    }

    /****************************************************************************************************
     * Screen Action
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SEQUENCE_ITEM_SELECTED)) {
                doSequenceItemSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSequenceItemSelected() throws Exception {
        StoreSequenceItemWrapper wrapper = (StoreSequenceItemWrapper) sequenceItemTable.getSelectedRowData();
        if (wrapper.getStockItem() != null && model.isViewMode()) {
            if (model.storeItemAndObtainAllSequenceLock(wrapper.getStockItem())) {
                RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_ORIGIN, SimClientStateKey.STORE_SEQUENCE_ITEM_LIST);
                navigate(SimScreenName.ITEM_STORE_SEQUENCE_SCREEN);
            }
        }
    }

    /****************************************************************************************************
     * Item Search Listener
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void assignStockItem(StockItem stockItem) {
                stockItemEditor.setData(stockItem);
            }
        };
    }

    /****************************************************************************************************
     * Item Table Listener - Validate not duplicate item
     ***************************************************************************************************/

    private SimTableEditorListener createStockItemTableListener() {
        return new SimTableEditorListener() {
            public void performTableEditorEvent(SimTableEditorEvent event) {
                StockItem stockItem = (StockItem) event.getTableEditor().getValue();
                if (stockItem == null) {
                    return;
                }
                String itemId = stockItem.getId();
                if (StringUtility.isNullOrEmpty(itemId)) {
                    return;
                }
                int count = 0;
                List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
                for (StoreSequenceItemWrapper wrapper : wrappers) {
                    if (itemId.equals(wrapper.getStockItem().getId())) {
                        count++;
                    }
                }
                if (count > 1) {
                    String description = stockItem.getId() + " [ " + stockItem.getItemDescription() + " ] ";
                    displayMessage(StoreSequenceMessageText.DUPLICATE_ITEM_FIX, description);

                    for (StoreSequenceItemWrapper wrapper : wrappers) {
                        if (itemId.equals(wrapper.getStockItem().getId())) {
                            if (wrapper.getStoreSequenceItem().getId() == null) {
                                model.removeItem(wrapper);
                                sequenceItemTable.removeRow(wrapper);
                                break;
                            }
                        }
                    }
                }
            }
        };
    }

    /****************************************************************************************************
     * Micro Item Table Definition
     ***************************************************************************************************/

    private class StoreSequenceItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreSequenceItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(9);
            attributes.add(new SimTableAttribute("Item", StoreSequenceProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemEditor));
            attributes.add(new SimTableAttribute("Item Description", StoreSequenceProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("Capacity", StoreSequenceProperty.CAPACITY));
            attributes.add(new SimTableAttribute("Width", StoreSequenceProperty.WIDTH));
            attributes.add(new SimTableAttribute("UOM", StoreSequenceProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Label Format", StoreSequenceProperty.LABEL_FORMAT, new TranslatedObjectDisplayer(), ticketTypeEditor));
            attributes.add(new SimTableAttribute("Label Qty", StoreSequenceProperty.QUANTITY));
            attributes.add(new SimTableAttribute("Multiple Locations", StoreSequenceProperty.MULTI_AREA, new BooleanDisplayer()));
            return attributes;
        }
    }
}
