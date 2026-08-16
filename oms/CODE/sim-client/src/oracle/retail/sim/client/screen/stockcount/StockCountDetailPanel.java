package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.SequenceIdDisplayer;
import oracle.retail.sim.client.screen.item.ItemHierarchyPanel;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.PopupTableEditorListener;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountTimeframe;

/********************************************************************************************************
 * Stock Count Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
    private static final long serialVersionUID = -6089884748726158915L;

    private StockCountDetailModel model = new StockCountDetailModel();

    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Description");
    private RDisplayLabelEditor scheduleDateEditor = new RDisplayLabelEditor("Date");
    private RDisplayLabelEditor totalItemsEditor = new RDisplayLabelEditor("Total Line Items");
    private RDisplayLabelEditor countUserEditor = new RDisplayLabelEditor("Stock Count User");
    private RDisplayLabelEditor recountUserEditor = new RDisplayLabelEditor("Stock Re-Count User");
    private RDisplayLabelEditor timeFrameEditor = new RDisplayLabelEditor("Time Frame");
    private RCheckBoxEditor snapshotTakenEditor = new RCheckBoxEditor("Snapshot Taken");
    private RComboBoxEditor itemCountFilter = new RComboBoxEditor("Count/Re-count Qty");
    private RComboBoxEditor discrepancyEditor = new RComboBoxEditor("Filter");
    private ItemHierarchyPanel hierarchyPanel = new ItemHierarchyPanel();

    private SimTable lineItemTable = new SimTable(new StockCountDetailDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private StockItemScannerDialog scannerDialog = null;

    private static final String FILTER_MODIFIED = "Filter.modified";
    private static final String LINE_ITEM_SELECTED = "LineItem.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public StockCountDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        scheduleDateEditor.setDisplayer(new DateDisplayer(SimDateUtil.getGMTTimeZone()));

        itemCountFilter.setDisplayer(new TranslatedObjectDisplayer());
        itemCountFilter.setItems(model.getItemCountOptions());
        itemCountFilter.removeEmptySelection();
        itemCountFilter.setSelectedItem(ItemCountType.UNCOUNTED);
        itemCountFilter.registerAction(this, FILTER_MODIFIED);

        hierarchyPanel.setEmptyDescriptionToAll();
        hierarchyPanel.registerAction(this, FILTER_MODIFIED);

        snapshotTakenEditor.setEnabled(true, false);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(descriptionEditor, GridTool.constraints(0, 0, 1, 1, 2, 0, 0, 3, 0, 0, 3, 0));
        headerPanel.add(scheduleDateEditor, GridTool.constraints(0, 1, 1, 1, 2, 0, 0, 3, 0, 0, 3, 0));
        headerPanel.add(totalItemsEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        headerPanel.add(snapshotTakenEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        headerPanel.add(countUserEditor, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        headerPanel.add(recountUserEditor, GridTool.constraints(2, 1, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));

        REditorPanel filterPanel = new REditorPanel(3);
        filterPanel.setLineBorder(1);
        filterPanel.add(itemCountFilter);
        filterPanel.add(discrepancyEditor);
        filterPanel.add(timeFrameEditor);

        hierarchyPanel.setLineBorder(1);

        RPanel secondPanel = new RPanel(new GridBagLayout());
        secondPanel.add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 5));
        secondPanel.add(hierarchyPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignEditorsInGridBag(headerPanel);
        LayoutUtility.alignEditorsInGridBag(secondPanel);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(secondPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 2, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadStockCountWrapper();

        hierarchyPanel.loadDepartments(model.findAvailableDepartmentIds());

        StockCountWrapper stockCountWrapper = model.getStockCountWrapper();

        descriptionEditor.setData(stockCountWrapper.getLocationDescription());
        scheduleDateEditor.setData(stockCountWrapper.getScheduledDate());
        countUserEditor.setData(stockCountWrapper.getStockCountChild().getCountUser());
        recountUserEditor.setData(stockCountWrapper.getStockCountChild().getRecountUser());
        timeFrameEditor.setData(stockCountWrapper.getBeforeStoreOpen());
        snapshotTakenEditor.setSelected(stockCountWrapper.isLocationAlreadySnapshot());
        timeFrameEditor.setVisible(model.isTimeFrameEditorVisible());

        if (model.isRecountMode()) {
            snapshotTakenEditor.setVisible(!stockCountWrapper.isUnitAndAmount());

            discrepancyEditor.setDisplayer(new TranslatedObjectDisplayer());
            discrepancyEditor.addItem(DiscrepantFilterType.DISCREPANT);
            discrepancyEditor.addItem(DiscrepantFilterType.ALL);
            discrepancyEditor.removeEmptySelection();
            discrepancyEditor.registerAction(this, FILTER_MODIFIED);
            discrepancyEditor.setVisible(true);
        } else {
            discrepancyEditor.setVisible(false);
        }

        lineItemTable = new SimTable(new StockCountDetailDefinition());
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize("serialNumberTotal", SimTable.LABEL_WIDTH);
        }
        lineItemTable.setSingleRowSelectionMode();
        if (model.isStockCountChildEditable()) {
            lineItemTable.setTableEditable(true);
        } else {
            lineItemTable.registerSingleClickAction(this, LINE_ITEM_SELECTED);
            lineItemTable.setTableEditable(false);
        }
        lineItemPane.setTable(lineItemTable);

        refreshLineItemTable();
        launchScanner();
    }

    public void stop() {
        shutdownScanner();
        try {
            model.releaseStockCountChildLock();
        } catch (Throwable e) {
            displayException(e);
        }
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isRecountMode() {
        return model.isRecountMode();
    }

    public boolean isTakeSnapshotAvailable() {
        return model.isTakeSnapshotAvailable();
    }

    public boolean isCompleteAvailable() {
        return model.isCompleteAvailable();
    }

    public boolean hasStockCountChildPermissions() throws Exception {
        return model.hasStockCountChildPermissions();
    }

    public boolean isStockCountChildEditable() {
        return model.isStockCountChildEditable();
    }

    public boolean isFutureStockCount() {
        return model.isFutureStockCount();
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
        lineItemTable.stopEditing();
        try {
            if (model.isSnapshotRequired()) {
                throw new UIException(StockCountMessageText.SNAPSHOT_REQUIRED, RErrorSeverity.WARNING);
            }
            StockCountLineItemWrapper lineItemWrapper = findExistingWrapper(barcodeItem.getId());
            if (lineItemWrapper == null) {
                throw new UIException(StockCountMessageText.ITEM_NOT_ON_STOCK_COUNT, RErrorSeverity.WARNING);
            }
            model.updateExistingLineItem(lineItemWrapper, barcodeItem);
        } catch (Exception exception) {
            scannerDialog.displayException(exception);
        } finally {
            lineItemTable.refreshTable();
        }
    }

    private StockCountLineItemWrapper findExistingWrapper(String itemId) {
        List<StockCountLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (StockCountLineItemWrapper wrapper : wrappers) {
            if (itemId.equals(wrapper.getItemId())) {
                return wrapper;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (model.isStockCountChildComplete()) {
            return;
        }
        if (model.lostStockCountChildLock()) {
            model.getStockCountWrapper().clearLineItems();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        List<RetailStoreFormatPrinter> formatPrinters = model.getFormatPrinters();
        showScreenBusy(true);
        if (formatPrinters == null || formatPrinters.size() == 0) {
            return;
        }
        showScreenBusy(false);
        ReportResponse response = model.printStockCountChild(formatPrinters);
        if (response != null) {
            if (response.getMessage() != null) {
                displayError(response.getMessage(), response.getMessageValue());
            } else if (response.getPrintResponse() != null) {
                displayException(new Exception(response.getPrintResponse()));
            }
            return;
        }
        displayMessage(StockCountMessageText.REPORT_PRINTED);
    }

    /****************************************************************************************************
     * Handle Take Snapshot
     ***************************************************************************************************/

    public void handleTakeSnapshot() throws Exception {
        if (model.getStockCountWrapper().isLocationAlreadySnapshot()) {
            throw new BusinessException(StockCountMessageText.SNAPSHOT_ALREADY_TAKEN);
        }
        if (model.lostStockCountChildLock()) {
            model.getStockCountWrapper().clearLineItems();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        if (model.isProcessTimeFrameRequired()) {
            processTimeFrame();
        }
        showScreenBusy(true);
        try {
            model.markStockCountChildAsStarted();
        } finally {
            showScreenBusy(false);
        }
        snapshotTakenEditor.setSelected(model.getStockCountWrapper().isLocationAlreadySnapshot());
        refreshLineItemTable();
    }

    private void processTimeFrame() throws Exception {
        boolean isBeforeStoreOpen = true;

        MessageText timeframeMessage = model.getDefaultTimeFrameMessage();
        if (RConfirmUtility.confirm("Default Timeframe", timeframeMessage)) {
            isBeforeStoreOpen = timeframeMessage.equals(StockCountMessageText.BEFORE_OPEN_MESSAGE);
        } else {
            String buttonLabel1 = StockCountMessageText.BEFORE_STORE_OPEN.getText();
            String buttonLabel2 = StockCountMessageText.AFTER_STORE_CLOSE.getText();
            isBeforeStoreOpen = RConfirmUtility.confirm("Default Timeframe", StockCountMessageText.WHEN_COUNTING_CONFIRM, buttonLabel1, buttonLabel2);
        }
        if (isBeforeStoreOpen) {
            model.setTimeframe(StockCountTimeframe.BEFORE_STORE_OPEN);
        } else {
            model.setTimeframe(StockCountTimeframe.AFTER_STORE_CLOSE);
        }
        timeFrameEditor.setData(model.getStockCountWrapper().getStockCount().getTimeframe());
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public boolean handleCancel() throws Exception {
        if (model.isStockCountChildEditable()) {
            if (RConfirmUtility.confirm("Cancel", StockCountMessageText.EXIT_CONFIRM)) {
                model.getStockCountWrapper().clearLineItems();
                return true;
            }
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * SCANNER
     ***************************************************************************************************/

    protected void handleScanner() {
        lineItemTable.stopEditing();
        displayScanner();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public void handleSave() throws Exception {
        if (!model.isStockCountChildEditable()) {
            return;
        }
        if (model.lostStockCountChildLock()) {
            model.getStockCountWrapper().clearLineItems();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        model.saveStockCountChild();
        model.releaseStockCountChildLock();
    }

    /****************************************************************************************************
     * Handle Complete
     ***************************************************************************************************/

    public boolean handleComplete() throws Exception {
        // Handle Initialize State Checks
        if (!model.isStockCountChildEditable()) {
            return true;
        }
        if (model.lostStockCountChildLock()) {
            model.getStockCountWrapper().clearLineItems();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return true;
        }

        // Handle Validation and Confirmation
        boolean hasEmptyQty = model.isLineItemQuantityNull();
        if (hasEmptyQty) {
            if (!RConfirmUtility.confirm("Missing Count", model.getStockCountMissingValuesMessage())) {
                int column = 0;
                if (model.isRecountMode()) {
                    column = lineItemTable.findColumnIndex("stockRecountedBasedOnUom");
                } else {
                    column = lineItemTable.findColumnIndex("stockCountedBasedOnUom");
                }
                int emptyRow = lineItemTable.getFirstNullValueRow(column);
                if (emptyRow > -1) {
                    lineItemTable.editCellAt(emptyRow, column);
                }
                return false;
            }
        } else {
            if (!RConfirmUtility.confirm("Confirmation", model.getStockCountCompleteMessage())) {
                return false;
            }
        }

        // Save The Location And Mark As Completed
        showScreenBusy(true);
        try {
            model.saveStockCountChild();
            model.markStockCountChildAsCompleted();
            model.releaseStockCountChildLock();
        } catch (BusinessException exception) {
            displayException(exception);
            return false;
        } finally {
            showScreenBusy(false);
        }
        return true;
    }

    /****************************************************************************************************
     * Handle Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(LINE_ITEM_SELECTED)) {
                doLineItemSelected();
            } else if (command.equals(FILTER_MODIFIED)) {
                refreshLineItemTable();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doLineItemSelected() throws Exception {
        if (model.isSerialNumberProcessingEnabled()) {
            StockCountLineItemWrapper wrapper = (StockCountLineItemWrapper) lineItemTable.getSelectedRowData();
            if (wrapper != null && wrapper.isSerialNumberRequired()) {
                displaySerialNumberWindow(wrapper, model.isStockCountChildEditable());
            }
        }
    }

    private void displaySerialNumberWindow(StockCountLineItemWrapper wrapper, boolean isEditable) throws Exception {
        if (model.isFutureStockCount()) {
            return;
        }
        if (isEditable && model.lostStockCountChildLock()) {
            model.getStockCountWrapper().clearLineItems();
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            return;
        }
        if (model.isRecountMode() && model.isSerialNumberProcessingEnabled() && wrapper.isSerialNumberRequired()) {
            if (wrapper.getLineItem().isDiscrepant()) {
                launchSerialNumberWindow(wrapper, isEditable);
            } else if (RConfirmUtility.confirm("Confirmation", StockCountMessageText.RECOUNT_CONFIRM)) {
                model.markStockCountLineItemAsDiscrepant(wrapper);
                launchSerialNumberWindow(wrapper, isEditable);
            }
            return;
        }
        launchSerialNumberWindow(wrapper, isEditable);
    }

    private void launchSerialNumberWindow(StockCountLineItemWrapper wrapper, boolean isEditable) {
        StockCountUinDialog dialog = new StockCountUinDialog();
        dialog.setLineItemWrapper(wrapper);
        dialog.setSerialNumbersEditable(isEditable);
        dialog.setVisible(true);

        lineItemTable.refreshTable();
    }

    private void refreshLineItemTable() throws Exception {
        StockCountWrapper stockCountWrapper = model.getStockCountWrapper();
        List<StockCountLineItemWrapper> lineItemWrappers = stockCountWrapper.getAllLineItemWrappers();

        totalItemsEditor.setData(lineItemWrappers.size());

        ItemCountType countType = (ItemCountType) itemCountFilter.getSelectedItem();
        MdseHierarchyNode hierarchyNode = hierarchyPanel.getHierarchyNode();
        DiscrepantFilterType discrepentType = null;
        if (discrepancyEditor.isVisible()) {
            discrepentType = (DiscrepantFilterType) discrepancyEditor.getSelectedItem();
        }

        lineItemTable.setRows(model.filterLineItems(lineItemWrappers, countType, hierarchyNode, discrepentType));
        lineItemTable.setTableEditable(model.isStockCountChildEditable());
    }

    /****************************************************************************************************
     * Stock Count Table Definition
     ***************************************************************************************************/

    private class StockCountDetailDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountLineItemWrapper.class;
        }

        public List<String> getOverrideEditableAttributes() {
            return Collections.singletonList("serialNumberTotal");
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            if (model.isGuidedStockCount()) {
                return Collections.singletonList(new SimTableSortAttribute("sequence"));
            }
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            if (model.isGuidedStockCount()) {
                attributes.add(new SimTableAttribute("Sequence ID", "sequence", new SequenceIdDisplayer()));
            }
            attributes.add(new SimTableAttribute("Item", "itemId"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Description", "longDescription"));
            }
            if (model.isGuidedStockCount()) {
                attributes.add(new SimTableAttribute("Area", "area", new TranslatedObjectDisplayer()));
            }
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasureMode", new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", "caseSize", new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            if (model.isRecountMode()) {
                attributes.add(new SimTableAttribute("Original Count", "stockCountedBasedOnUom", new QuantityDisplayer(), new LineItemQuantityTableEditor(true), false));
                attributes.add(new SimTableAttribute("Re-Count Qty", "stockRecountedBasedOnUom", new QuantityDisplayer(), new LineItemQuantityTableEditor(true)));
            } else {
                attributes.add(new SimTableAttribute("Count", "stockCountedBasedOnUom", new QuantityDisplayer(), new LineItemQuantityTableEditor(true)));
            }
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", "serialNumberTotal", new SerialNumberTableDisplayer(), new SerialNumberTableEditor(new UINPopupListener())));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * UIN POPUP LISTENER
     ***************************************************************************************************/

    private class UINPopupListener implements PopupTableEditorListener {

        public void popupDialog(Object object) {
            try {
                displaySerialNumberWindow((StockCountLineItemWrapper) object, true);
            } catch (Throwable exception) {
                displayException(exception);
            }
        }
    }
}
