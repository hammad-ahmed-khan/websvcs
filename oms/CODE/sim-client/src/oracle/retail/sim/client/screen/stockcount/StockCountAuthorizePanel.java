package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StockCountChildComparator;
import oracle.retail.sim.client.displayer.StockCountChildDisplayer;
import oracle.retail.sim.client.screen.uin.SerialNumberTableDisplayer;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.PercentDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.task.RProgressFrame;
import oracle.retail.sim.client.swing.task.UIProgressTask;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Authorization Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizePanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4811424043759006754L;

    private StockCountAuthorizeModel model = new StockCountAuthorizeModel();

    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Description");
    private RDisplayLabelEditor scheduleDateEditor = new RDisplayLabelEditor("Date");
    private RDisplayLabelEditor exportUserEditor = new RDisplayLabelEditor("Export User");
    private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
    private RComboBoxEditor childCountEditor = new RComboBoxEditor("Child");
    private RDisplayLabelEditor totalItemsEditor = new RDisplayLabelEditor("Total Items");
    private RDisplayLabelEditor discrepantEditor = new RDisplayLabelEditor("Discrepant Items");
    private RComboBoxEditor itemFilterEditor = new RComboBoxEditor("Item Filter");
    private RComboBoxEditor authQtyEditor = new RComboBoxEditor("Auth Qty");
    private RDisplayLabelEditor unauthorizeEditor = new RDisplayLabelEditor("Unauthorized Items");
    private RDisplayLabelEditor countUserEditor = new RDisplayLabelEditor("Stock Count User");
    private RDisplayLabelEditor recountUserEditor = new RDisplayLabelEditor("Stock Re-Count User");
    private RDisplayLabelEditor authorizeUserEditor = new RDisplayLabelEditor("Authorization User");

    private SimTable lineItemTable = new SimTable(new StockCountAuthorizeDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    private static final String LINE_ITEM_SELECTED = "LineItem.selected";
    private static final String FILTER_MODIFIED = "Filter.selected";
    private static final String STATUS_MODIFIED = "Status.selected";

    private static final String SEARCH = "Search";
    private static final String CLEAR = "Clear";
    private static final String FILTER = "Advanced Filter";
    private RButton searchButton = new RButton(SEARCH);
    private RButton clearButton = new RButton(CLEAR);
    private RButton filterButton = new RButton(FILTER);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public StockCountAuthorizePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        scheduleDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        childCountEditor.setDisplayer(new StockCountChildDisplayer());
        childCountEditor.setComparator(new StockCountChildComparator());

        totalItemsEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        discrepantEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        unauthorizeEditor.setDataType(DataTypeConstants.INTEGER_LEFT);

        itemFilterEditor.addItem(DiscrepantFilterType.ALL);
        itemFilterEditor.addItem(DiscrepantFilterType.DISCREPANT);
        itemFilterEditor.removeEmptySelection();
        itemFilterEditor.setDisplayer(new TranslatedObjectDisplayer());
        itemFilterEditor.setSelectedItem(DiscrepantFilterType.DISCREPANT);
        itemFilterEditor.registerAction(this, FILTER_MODIFIED);

        authQtyEditor.addItem(AuthorizeQtyFilterType.ALL);
        authQtyEditor.addItem(AuthorizeQtyFilterType.AUTHORIZED);
        authQtyEditor.addItem(AuthorizeQtyFilterType.UNAUTHORIZED);
        authQtyEditor.removeEmptySelection();
        authQtyEditor.setDisplayer(new TranslatedObjectDisplayer());
        authQtyEditor.setSelectedItem(AuthorizeQtyFilterType.UNAUTHORIZED);
        authQtyEditor.registerAction(this, FILTER_MODIFIED);

        statusEditor.registerAction(this, STATUS_MODIFIED);
        searchButton.registerAction(this, SEARCH);
        clearButton.registerAction(this, CLEAR);
        filterButton.registerAction(this, FILTER);
    }

    private void layoutScreen() {
        RPanel countPanel = new RPanel(new GridBagLayout());
        countPanel.setTitleBorder("Stock Count");
        countPanel.add(descriptionEditor, GridTool.constraints(0, 0, 1, 1, 2, 0, 0, 3, 0, 0, 2, 0));
        countPanel.add(scheduleDateEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 2, 0));
        countPanel.add(exportUserEditor, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 3, 0, 0, 2, 0));

        RPanel childPanel = new RPanel(new GridBagLayout());
        childPanel.setTitleBorder("Child Stock Count");
        childPanel.add(statusEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        childPanel.add(childCountEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        childPanel.add(totalItemsEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        childPanel.add(discrepantEditor, GridTool.constraints(0, 3, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        childPanel.add(searchButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        childPanel.add(clearButton, GridTool.constraints(1, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.setTitleBorder("Filter");
        filterPanel.add(itemFilterEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        filterPanel.add(authQtyEditor, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        filterPanel.add(unauthorizeEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 3, 0));
        filterPanel.add(filterButton, GridTool.constraints(1, 2, 1, 1, 0, 0, 0, 0, 0, 0, 0, 17));
        filterPanel.add(new JLabel(), GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        REditorPanel userPanel = new REditorPanel(3);
        userPanel.setTitleBorder("Users");
        userPanel.add(countUserEditor);
        userPanel.add(recountUserEditor);
        userPanel.add(authorizeUserEditor);

        LayoutUtility.alignEditorsInGridBag(childPanel);
        LayoutUtility.alignEditorsInGridBag(filterPanel);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(countPanel, GridTool.constraints(0, 0, 3, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(childPanel, GridTool.constraints(0, 1, 1, 1, 2, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(filterPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(userPanel, GridTool.constraints(2, 1, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 2, 3, 1, 1, 1, 0, 3, 0, 0, 0, 0));

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
        model.loadStockCount();

        statusEditor.setActionsEnabled(false);
        statusEditor.setItems(model.getAllFilterStatus());
        statusEditor.setSelectedItem(StockCountDisplayStatus.NEW);
        statusEditor.setActionsEnabled(true);

        childCountEditor.setItems(model.findStockCountChilds((StockCountDisplayStatus) statusEditor.getSelectedItem()));

        resetLineItemTable();

        StockCountWrapper stockCount = model.getStockCountWrapper();

        descriptionEditor.setData(stockCount.getCountDescription());
        scheduleDateEditor.setData(stockCount.getScheduledDate());
        exportUserEditor.setData(stockCount.getStockCount().getExportUser());
        exportUserEditor.setVisible(stockCount.isUnitAndAmount());

        if (model.isStockCountChildFinished()) {
            itemFilterEditor.setActionsEnabled(false);
            itemFilterEditor.setSelectedItem(DiscrepantFilterType.ALL);
            itemFilterEditor.setActionsEnabled(true);

            authQtyEditor.setActionsEnabled(false);
            authQtyEditor.setSelectedItem(AuthorizeQtyFilterType.ALL);
            authQtyEditor.setActionsEnabled(true);
        }

        StockCountChild stockCountChild = stockCount.getStockCountChild();
        if (stockCountChild != null) {
            statusEditor.setActionsEnabled(false);
            statusEditor.setSelectedItem(stockCountChild.getStatus());
            statusEditor.setActionsEnabled(true);

            childCountEditor.setActionsEnabled(false);
            childCountEditor.setSelectedItem(stockCountChild);
            childCountEditor.setActionsEnabled(true);

            searchButton.setEnabled(false);
            statusEditor.setEnabled(false);
            childCountEditor.setEnabled(false);
            filterButton.setEnabled(true);
        } else {
            filterButton.setEnabled(false);
        }

        displayStockCountChild();
    }

    public boolean isStockCountScreenEditable() {
        return model.isStockCountScreenEditable();
    }

    public void stop() {
        model.releaseStockCountChild();
    }

    /****************************************************************************************************
     * Reset Table
     ***************************************************************************************************/

    private void resetLineItemTable() {
        lineItemTable = new SimTable(new StockCountAuthorizeDefinition());
        lineItemTable.setColumnSize("standardUnitOfMeasure", EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize("stockCountedTotal", EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize("stockCountedVariance", EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize("stockCountedPercent", EditorConstants.COLUMN_LABEL_WIDTH);
        if (model.isRecountRequired()) {
            lineItemTable.setColumnSize("stockRecountedTotal", EditorConstants.COLUMN_LABEL_WIDTH);
            lineItemTable.setColumnSize("stockRecountedVariance", EditorConstants.COLUMN_LABEL_WIDTH);
            lineItemTable.setColumnSize("stockRecountedPercent", EditorConstants.COLUMN_LABEL_WIDTH);
        }
        if (model.isBreakdownSequenced()) {
            lineItemTable.setColumnSize("multiLocated", EditorConstants.COLUMN_LABEL_WIDTH);
        }
        if (model.isSerialNumberProcessingEnabled()) {
            lineItemTable.setColumnSize("serialNumberTotal", EditorConstants.COLUMN_LABEL_WIDTH);
        }
        lineItemTable.setSingleRowSelectionMode();
        lineItemTable.registerDoubleClickAction(this, LINE_ITEM_SELECTED);
        lineItemTable.setTableEditable(model.isStockCountTableEditable());
        lineItemPane.setTable(lineItemTable);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SEARCH)) {
                doSearchSelected();
            } else if (command.equals(CLEAR)) {
                doClearSelected();
            } else if (command.equals(FILTER)) {
                doAdvancedFilterSelected();
            } else if (command.equals(STATUS_MODIFIED)) {
                doStatusModified();
            } else if (command.equals(FILTER_MODIFIED)) {
                doFilterModified();
            } else if (command.equals(LINE_ITEM_SELECTED)) {
                doLineItemSelected();
            } else if (command.equals(SimClientStateKey.STOCK_COUNT_AUTH_FILTER_MODIFIED)) {
                doAdvancedFilterModified(event.getEventData());
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * SEARCH SELECTED
     ***************************************************************************************************/

    private void doSearchSelected() throws Exception {
        StockCountChild stockCountChild = (StockCountChild) childCountEditor.getSelectedItem();
        if (stockCountChild == null) {
            displayWarning(StockCountMessageText.SELECT_CHILD_COUNT);
            return;
        }
        searchButton.setEnabled(false);
        statusEditor.setEnabled(false);
        childCountEditor.setEnabled(false);
        filterButton.setEnabled(true);

        model.setActiveLocation(stockCountChild);

        displayStockCountChild();
    }

    private void displayStockCountChild() throws Exception {
        StockCountWrapper stockCount = model.getStockCountWrapper();
        StockCountChild stockCountChild = stockCount.getStockCountChild();

        clearStockCountChild();

        if (stockCountChild == null) {
            clearStockCountChild();
            return;
        }

        countUserEditor.setData(stockCountChild.getCountUser());
        recountUserEditor.setData(stockCountChild.getRecountUser());
        authorizeUserEditor.setData(stockCountChild.getAuthorizeUser());

        int discrepantCount = 0;
        int unauthorizedCount = 0;

        List<StockCountLineItemWrapper> wrappers = stockCount.getLineItemWrappers();
        for (StockCountLineItemWrapper wrapper : wrappers) {
            if (wrapper.getLineItem().isDiscrepant()) {
                discrepantCount++;
            }
            if (wrapper.getStockApproved() == null) {
                unauthorizedCount++;
            }
        }
        totalItemsEditor.setData(wrappers.size());
        discrepantEditor.setData(discrepantCount);
        unauthorizeEditor.setData(unauthorizedCount);

        doFilterModified();
    }

    private void clearStockCountChild() {
        countUserEditor.clear();
        recountUserEditor.clear();
        authorizeUserEditor.clear();
        lineItemTable.clearRows();
    }

    /****************************************************************************************************
     * CLEAR SELECTED
     ***************************************************************************************************/

    private void doClearSelected() throws Exception {
        model.releaseStockCountChild();

        lineItemTable.stopEditing();
        lineItemTable.clearRows();

        searchButton.setEnabled(true);
        childCountEditor.setEnabled(true);
        filterButton.setEnabled(false);

        statusEditor.setEnabled(true);
        statusEditor.setActionsEnabled(false);
        statusEditor.setSelectedItem(StockCountStatus.NEW);
        statusEditor.setActionsEnabled(true);

        childCountEditor.setItems(model.findStockCountChilds((StockCountDisplayStatus) statusEditor.getSelectedItem()));
    }

    /****************************************************************************************************
     * STATUS MODIFIED
     ***************************************************************************************************/

    private void doStatusModified() throws Exception {
        childCountEditor.setItems(model.findStockCountChilds((StockCountDisplayStatus) statusEditor.getSelectedItem()));
    }

    /****************************************************************************************************
     * ADVANCED FILTER SELECTED
     ***************************************************************************************************/

    private void doAdvancedFilterSelected() throws Exception {
        StockCountAuthorizeFilterDialog dialog = new StockCountAuthorizeFilterDialog();
        dialog.setDepartments(model.getAvailableDepartments());
        dialog.setFilter(model.getAuthorizeQueryFilter());
        dialog.addREventListener(this);
        dialog.setVisible(true);
    }

    private void doAdvancedFilterModified(Object object) throws Exception {
        model.setAuthorizeQueryFilter((AuthorizeQueryFilter) object);
        doFilterModified();
    }

    /****************************************************************************************************
     * LINE ITEM FILTER HAS BEEN MODIFIED SELECTED
     ***************************************************************************************************/

    private void doFilterModified() throws Exception {
        DiscrepantFilterType discrepantType = (DiscrepantFilterType) itemFilterEditor.getSelectedItem();
        AuthorizeQtyFilterType authorizeType = (AuthorizeQtyFilterType) authQtyEditor.getSelectedItem();
        lineItemTable.setTableEditable(model.isStockCountTableEditable());
        lineItemTable.setRows(model.getFilteredLineItems(discrepantType, authorizeType));
    }

    /****************************************************************************************************
     * LINE ITEM SELECTED
     ***************************************************************************************************/

    private void doLineItemSelected() throws Exception {
        StockCountLineItemWrapper lineItem = (StockCountLineItemWrapper) lineItemTable.getSelectedRowData();
        if (lineItem != null) {
            StockCountWrapper stockCount = model.getStockCountWrapper();
            if (lineItem.isSerialNumberRequired()) {
                StockCountAuthorizeUinDialog dialog = new StockCountAuthorizeUinDialog();
                dialog.setStockCount(stockCount);
                dialog.setLineItem(lineItem);
                dialog.setVisible(true);
                lineItemTable.refreshTable();
            } else if (stockCount.isBreakdownSequenced()) {
                StockCountAuthorizeDialog dialog = new StockCountAuthorizeDialog();
                dialog.setStockCount(stockCount);
                dialog.setLineItem(lineItem);
                dialog.setVisible(true);
            }
        }
    }

    /****************************************************************************************************
     * Handle Apply Late Sales
     ***************************************************************************************************/

    public void handleApplyLateSales() throws Exception {
        lineItemTable.stopEditing();
        if (isStockCountChildUnmodifiable()) {
            return;
        }
        if (!model.checkStockCountChildLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            doClearSelected();
            return;
        }
        if (RConfirmUtility.confirm("Confirmation", StockCountMessageText.APPLY_LATE_SALES_CONFIRM)) {
            model.applyLateSales();
            displayStockCountChild();
        }
    }

    /****************************************************************************************************
     * Handle Count Detail
     ***************************************************************************************************/

    public void handleCountDetail() throws Exception {
        lineItemTable.stopEditing();
        
        if (lineItemTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        
        StockCountLineItemWrapper wrapper = (StockCountLineItemWrapper) lineItemTable.getSelectedRowData();
        if (wrapper == null) {
            return;
        }
        if (!wrapper.getLineItem().isBreakableComponent()) {
            displayWarning(StockCountMessageText.NO_COUNT_DETAIL_ERROR);
            return;
        }
        StockCountComponentDetailDialog dialog = new StockCountComponentDetailDialog();
        dialog.setStockCount(model.getStockCountWrapper());
        dialog.setLineItem(wrapper);
        dialog.setVisible(true);
    }

    /****************************************************************************************************
     * Handle Update Authorization Qty
     ***************************************************************************************************/

    public void handleUpdateAuthQty() throws Exception {
        lineItemTable.stopEditing();
        if (isStockCountChildUnmodifiable()) {
            return;
        }
        MessageText message;
        if (model.getStockCountWrapper().isUnitAndAmount() || SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_UPDATE_ALL_SOH)) {
            message = StockCountMessageText.NO_AUTH_QTY_ALL_CONFIRM;
        } else {
            message = StockCountMessageText.NO_AUTH_QTY_DISC_CONFIRM;
        }
        if (RConfirmUtility.confirm("Update Auth Qty", message)) {
            model.updateAuthorizationQuantities();
            doFilterModified();
        }
    }

    /****************************************************************************************************
     * Handle Save Child
     ***************************************************************************************************/

    public void handleSaveChild() throws Exception {
        lineItemTable.stopEditing();
        if (isStockCountChildUnmodifiable()) {
            return;
        }
        if (!model.checkStockCountChildLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            doClearSelected();
            return;
        }
        model.saveStockCountChild();
        doClearSelected();
    }

    /****************************************************************************************************
     * Handle Confirm
     ***************************************************************************************************/

    public void handleConfirm() throws Exception {
        lineItemTable.stopEditing();
        if (isStockCountChildUnmodifiable()) {
            return;
        }
        StockCountStatus status = model.getStockCountWrapper().getStockCountChild().getStatus();
        if (status != StockCountStatus.APPROVAL_SCHEDULED && status != StockCountStatus.APPROVAL_IN_PROGRESS) {
            return;
        }
        if (!model.checkStockCountChildLock()) {
            displayError(CommonMessageText.LOCK_TAKEN_OVER);
            doClearSelected();
            return;
        }
        if (!validateConfirmationRequest()) {
            return;
        }
        if (!validateOpenAdjustments()) {
            return;
        }
        showScreenBusy(true);
        try {
            model.saveStockCountChild();
            model.markStockCountChildReadyToApprove();
        } finally {
            showScreenBusy(false);
        }

        // Thread The Actual Authorization
        RProgressFrame frame = new RProgressFrame();
        frame.start(new MonitorAuthorizationTask());

        doClearSelected();
    }

    private boolean validateConfirmationRequest() throws Exception {
        List<StockCountLineItemWrapper> lineItems = model.getStockCountWrapper().getLineItemWrappers();
        for (StockCountLineItemWrapper lineItem : lineItems) {
            if (lineItem.getStockApproved() == null) {
                if (model.getStockCountWrapper().isUnitAndAmount()) {
                    return RConfirmUtility.confirm("Confirmation", StockCountMessageText.BLANK_TO_LAST_COUNTED_CONFIRM);
                }
                return RConfirmUtility.confirm("Confirmation", StockCountMessageText.BLANK_TO_NOT_COUNTED_CONFIRM);
            }
        }
        return true;
    }

    private boolean validateOpenAdjustments() throws Exception {
        if (model.isInventoryAdjustmentValidationNeeded()) {
            return RConfirmUtility.confirm("Confirmation", StockCountMessageText.INVENTORY_ADJUSTMENT_WARNING);
        }
        return true;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleBack() throws Exception{
        if (isStockCountChildUnmodifiable() ||  model.isLineItemsSaved() )  {
            return true;
        }
        if (RConfirmUtility.confirm("Cancel", StockCountMessageText.EXIT_CONFIRM)) {
            model.releaseStockCountChild();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Helper method to indicate if the current child is unmodifiable or not selected
     ***************************************************************************************************/

    private boolean isStockCountChildUnmodifiable() {
        return !model.isStockCountScreenEditable() || !model.isStockCountChildSelected();
    }

    /****************************************************************************************************
     * Stock Count Authorize Definition
     ***************************************************************************************************/

    private class StockCountAuthorizeDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountLineItemWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Item", "itemId"));
            if (SimConfigManager.isItemShortDescription()) {
                attributes.add(new SimTableAttribute("Description", "shortDescription"));
            } else {
                attributes.add(new SimTableAttribute("Description", "longDescription"));
            }
            attributes.add(new SimTableAttribute("UOM", "standardUnitOfMeasure", false));
            attributes.add(new SimTableAttribute("Count Qty", "stockCountedTotal", false));
            attributes.add(new SimTableAttribute("Cnt Var", "stockCountedVariance", false));
            attributes.add(new SimTableAttribute("Cnt Var %", "stockCountedPercent", new PercentDisplayer()));
            if (model.isRecountRequired()) {
                attributes.add(new SimTableAttribute("Re-Count Qty", "stockRecountedTotal", false));
                attributes.add(new SimTableAttribute("Rcnt Var", "stockRecountedVariance", false));
                attributes.add(new SimTableAttribute("Rcnt Var %", "stockRecountedPercent", new PercentDisplayer()));
            }
            if (model.isBreakdownSequenced()) {
                attributes.add(new SimTableAttribute("Multi Locs", "multiLocated", new SimTableCheckBoxRenderer(false)));
            }
            attributes.add(new SimTableAttribute("SOH", "snapshot", false));
            attributes.add(new SimTableAttribute("Authorized Qty", "stockApproved", new QuantityDisplayer(), new LineItemQuantityTableEditor(true)));
            if (model.isSerialNumberProcessingEnabled()) {
                attributes.add(new SimTableAttribute("UIN Qty", "serialNumberTotal", new SerialNumberTableDisplayer()));
            }
            return attributes;
        }
    }

    /****************************************************************************************************
     * Monitor Authorization Task - Exceptions are handled with a recovery process.
     * The request is threaded and executing asynchronously, so exceptions should be logged but not ignored.
     ***************************************************************************************************/

    private class MonitorAuthorizationTask extends UIProgressTask {
        private Long stockCountId;
        private Long stockCountChildId;

        private MonitorAuthorizationTask() {
            setTitle("Monitor");
            setPermanentMessage(StockCountMessageText.AUTHORIZE_IN_PROGRESS);
            stockCountId = model.getStockCountWrapper().getStockCount().getId();
            stockCountChildId = model.getStockCountWrapper().getStockCountChild().getId();
        }

        public boolean executeRequest() {
            try {
                ClientServiceFactory.getStockCountChildServices().markStockCountChildApproved(stockCountId, stockCountChildId);
            } catch (Exception e) {
                LogService.error(this, "Failed to approve stock count child!", e);
            }
            return true;
        }
    }
}
