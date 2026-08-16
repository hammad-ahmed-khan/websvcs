package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.task.RProgressFrame;
import oracle.retail.sim.client.swing.task.UIProgressTask;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.stockcount.StockCountTimeframe;
import oracle.retail.sim.common.stockcount.StockCountType;

/********************************************************************************************************
 * Stock Count Location Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountChildPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -5462394799839415714L;

    private StockCountChildModel model = new StockCountChildModel();

    private RDisplayLabelEditor descriptionEditor = new RDisplayLabelEditor("Count Description");
    private RDisplayLabelEditor countIdEditor = new RDisplayLabelEditor("Count ID");
    private RDisplayLabelEditor scheduleDateEditor = new RDisplayLabelEditor("Stock Count Date");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RDisplayLabelEditor typeEditor = new RDisplayLabelEditor("Type");
    private RDisplayLabelEditor totalItemEditor = new RDisplayLabelEditor("Total Items");

    private RDisplayLabelEditor groupTypeEditor = new RDisplayLabelEditor("Product Group Type");
    private RDisplayLabelEditor groupDescEditor = new RDisplayLabelEditor("Product Group Description");
    private RDisplayLabelEditor countMethodEditor = new RDisplayLabelEditor("Counting Method");

    private static final String VIEW_DETAILS = "View Details";
    private RButton viewDetailButton = new RButton(VIEW_DETAILS);

    private SimTable locationTable = new SimTable(new StockCountChildDefinition());
    private SimTablePane locationPane = new SimTablePane(locationTable);

    private static final String STOCK_COUNT_CHILD_SELECTED = "StockCountChild.selected";

    /****************************************************************************************************
     * Panel Definition
     ***************************************************************************************************/

    public StockCountChildPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        countMethodEditor.setDisplayer(new TranslatedObjectDisplayer());
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeEditor.setDisplayer(new TranslatedObjectDisplayer());
        groupTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
        scheduleDateEditor.setDisplayer(new DateDisplayer(SimDateUtil.getGMTTimeZone()));
        viewDetailButton.registerAction(this, VIEW_DETAILS);
    }

    private void layoutScreen() {
        RPanel stockCountPanel = new RPanel(new GridBagLayout());
        stockCountPanel.setTitleBorder("Stock Count Details");
        stockCountPanel.add(descriptionEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        stockCountPanel.add(scheduleDateEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        stockCountPanel.add(totalItemEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        stockCountPanel.add(countIdEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        stockCountPanel.add(statusEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        stockCountPanel.add(typeEditor, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel productGroupPanel = new RPanel(new GridBagLayout());
        productGroupPanel.setTitleBorder("Product Group Details");
        productGroupPanel.add(groupDescEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        productGroupPanel.add(groupTypeEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        productGroupPanel.add(countMethodEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        productGroupPanel.add(viewDetailButton, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        LayoutUtility.alignEditorsInGridBag(stockCountPanel);
        LayoutUtility.alignEditorsInGridBag(productGroupPanel);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(stockCountPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(productGroupPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(locationPane, GridTool.constraints(0, 2, 2, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return locationTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadStockCountWrapper();
        populateScreen();
    }

    private synchronized void populateScreen() throws Exception {
        StockCountWrapper stockCountWrapper = model.getStockCountWrapper();

        countIdEditor.setData(stockCountWrapper.getId());
        descriptionEditor.setData(stockCountWrapper.getCountDescription());
        scheduleDateEditor.setData(stockCountWrapper.getScheduledDate());
        statusEditor.setData(stockCountWrapper.getDisplayStatus());
        typeEditor.setData(stockCountWrapper.getPhase());
        totalItemEditor.setData(stockCountWrapper.getItemsLeftToCount());

        groupTypeEditor.setData(stockCountWrapper.getType());
        groupDescEditor.setData(stockCountWrapper.getGroupDescription());
        if (stockCountWrapper.isRMSSync()) {
            countMethodEditor.setData("Auto");
        } else {
            countMethodEditor.setData(stockCountWrapper.getCountingMethod());
        }

        locationTable = new SimTable(new StockCountChildDefinition());
        locationTable.setColumnVisible("Area", stockCountWrapper.isBreakdownSequenced());
        locationTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        locationTable.registerDoubleClickAction(this, STOCK_COUNT_CHILD_SELECTED);
        locationTable.setColumnSize("id", SimTable.LABEL_WIDTH);
        locationTable.setTableEditable(false);
        locationTable.setRows(model.getLocationWrappers());

        locationPane.setTable(locationTable);
    }

    public boolean isFutureStockCount() {
        return model.isFutureStockCount();
    }

    public boolean isRMSSyncStockCount() {
        return model.isRMSSyncStockCount();
    }

    public boolean isPrintOptionAvailable() {
        return model.isPrintOptionAvailable();
    }

    public boolean isCompleteOptionAvailable() {
        return model.isCompleteOptionAvailable();
    }

    public boolean isRejectItemsOptionAvailable() {
        return model.isRejectItemsOptionAvailable();
    }

    public boolean isAuthorizeOptionAvailable() {
        return model.isAuthorizeOptionAvailable(getAllRowWrappers());
    }

    public boolean isUpdateAuthQtyOptionAvailable() {
        return model.isUpdateAuthQtyOptionAvailable(getAllRowWrappers());
    }

    public boolean isConfirmAuthorizationOptionAvailable() {
        return model.isConfirmAuthorizationOptionAvailable(getAllRowWrappers());
    }

    public boolean isEditPermissionDenied() {
        return model.isEditPermissionDenied();
    }

    private List<StockCountChildWrapper> getAllRowWrappers() {
        return locationTable.getAllRowData();
    }

    private List<StockCountChildWrapper> getAllSelectedRowWrappers() {
        return locationTable.getAllSelectedRowData();
    }

    /****************************************************************************************************
     * HANDLE REFRESH
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        model.reloadStockCountWrapper();
        populateScreen();
    }

    /****************************************************************************************************
     * HANDLE PRINT
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        if (locationTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<RetailStoreFormatPrinter> formatPrinters = model.getFormatPrinters();
        if (formatPrinters == null || formatPrinters.size() == 0) {
            return;
        }
        for (StockCountChildWrapper locationWrapper : getAllSelectedRowWrappers()) {
            ReportResponse response = model.printStockCountChild(formatPrinters, locationWrapper);
            if (response != null) {
                showScreenBusy(false);
                String message = response.getPrintResponse();
                if (response.getMessage() != null) {
                    message = Translator.getMessage(response.getMessage().getText(), response.getMessageValue());
                }
                if (!RConfirmUtility.confirm("Message", StockCountMessageText.CONTINUE_CONFIRM, message)) {
                    return;
                }
            }
        }
        showScreenBusy(false);
        displayMessage(StockCountMessageText.REPORT_PRINTED);
    }

    /****************************************************************************************************
     * HANDLE TAKE SNAPSHOT
     ***************************************************************************************************/

    public void handleTakeSnapshot() throws Exception {
        StockCountWrapper stockCount = model.getStockCountWrapper();
        if (model.isProcessTimeFrameRequired()) {
            processTimeFrame();
        }
        if (stockCount.isUnitAndAmount() || stockCount.isRMSSync()) {
            handleTakeSnapshotUnitAmount();
        } else {
            handleTakeSnapshotRegular();
        }
    }

    private void handleTakeSnapshotRegular() throws Exception {
        if (locationTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        List<StockCountChildWrapper> readyWrappers = new ArrayList<StockCountChildWrapper>();
        boolean someLocationsAlreadySnapshot = false;
        for (StockCountChildWrapper wrapper : getAllSelectedRowWrappers()) {
            if (model.isSnapshotTaken(wrapper)) {
                someLocationsAlreadySnapshot = true;
            } else {
                readyWrappers.add(wrapper);
            }
        }
        if (readyWrappers.isEmpty()) {
            displayError(StockCountMessageText.SNAPSHOT_ALREADY_TAKEN);
            return;
        }
        if (someLocationsAlreadySnapshot) {
            displayMessage(StockCountMessageText.SNAPSHOT_SOME_TAKEN);
        }
        if (model.hasActiveUsers(readyWrappers)) {
            if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.ACTIVE_LOCK_WARNING)) {
                return;
            }
        }
        showScreenBusy(true);
        try {
            model.breakAllLocks(readyWrappers);
            model.markStockCountChildAsStarted(readyWrappers);
        } finally {
            showScreenBusy(false);
            handleRefresh();
        }
    }

    private void handleTakeSnapshotUnitAmount() throws Exception {
        List<StockCountChildWrapper> wrappers = getAllRowWrappers();
        if (model.isSnapshotTaken(wrappers.iterator().next())) {
            displayError(StockCountMessageText.SNAPSHOT_ALREADY_TAKEN);
            return;
        }
        if (model.hasActiveUsers(wrappers)) {
            if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.ACTIVE_LOCK_WARNING)) {
                return;
            }
        }
        showScreenBusy(true);
        try {
            model.breakAllLocks(wrappers);
            model.markStockCountAsStarted();
        } finally {
            showScreenBusy(false);
            handleRefresh();
        }
        handleAutoAuthorize();
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
    }

    /****************************************************************************************************
     * HANDLE COMPLETE
     ***************************************************************************************************/

    public void handleComplete() throws Exception {
        List<StockCountChildWrapper> selectedLocationWrappers = getAllSelectedRowWrappers();
        if (selectedLocationWrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }

        // Check For Available Stock Count Children From Those Selected
        List<StockCountChildWrapper> availableToProcessWrappers = new ArrayList<>();
        for (StockCountChildWrapper wrapper : selectedLocationWrappers) {
            if (model.isAvailableToComplete(wrapper)) {
                availableToProcessWrappers.add(wrapper);
            }
        }

        // If selected and available, different there is a status mismatch
        if (selectedLocationWrappers.size() != availableToProcessWrappers.size()) {
            displayWarning(StockCountMessageText.COMPLETE_STATUS_WARNING);
        }

        // If nothing to process, don't process
        if (availableToProcessWrappers.isEmpty()) {
            return;
        }

        // Check For Active Locks On All Wrappers
        if (model.hasActiveUsers(availableToProcessWrappers)) {
            if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.COMPLETE_CONFIRM_LOCK)) {
                return;
            }
        }

        // Check For Null Counts
        boolean hasNullCounts = model.isNonCountedRowFound(availableToProcessWrappers);
        MessageText message = model.getStockCountCompleteMessage(hasNullCounts);
        if (!RConfirmUtility.confirm("Missing Count", message)) {
            return;
        }

        // Complete Each Location
        showScreenBusy(true);
        try {
            model.breakAllLocks(availableToProcessWrappers);
            model.markStockCountChildAsCompleted(availableToProcessWrappers);
        } catch (BusinessException exception) {
            displayException(exception);
        } finally {
            showScreenBusy(false);
            handleRefresh();
        }
        handleAutoAuthorize();
    }

    /****************************************************************************************************
     * HANDLE REJECTED ITEMS
     ***************************************************************************************************/

    public void handleRejectedItems() throws Exception {
        List<StockCountChildWrapper> wrappers = getAllRowWrappers();
        if (model.hasActiveUsers(wrappers)) {
            if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.ACTIVE_LOCK_WARNING)) {
                return;
            }
        }
        model.breakAllLocks(wrappers);
        model.loadRejectedItems();
    }

    /****************************************************************************************************
     * HANDLE AUTHORIZE
     ***************************************************************************************************/

    public void handleAuthorize() throws Exception {
        List<StockCountChildWrapper> wrappers = getAllRowWrappers();
        if (wrappers.size() == 1) {
            StockCountChildWrapper wrapper = wrappers.get(0);
            if (model.obtainStockCountChildLock(wrapper)) {
                model.storeStockCountChild(wrapper);
            }
        } else {
            model.clearStockCountChild();
        }
    }

    /****************************************************************************************************
     * HANDLE UPDATE AUTH QUANTITY
     ***************************************************************************************************/

    public void handleUpdateAuthQty() throws Exception {
        if (locationTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Confirmation", model.getUpdateAuthQtyMessage())) {
            List<StockCountChildWrapper> selectedChildWrappers = getAllSelectedRowWrappers();
            List<StockCountChildWrapper> availableChildWrappers = new ArrayList<StockCountChildWrapper>();
            for (StockCountChildWrapper wrapper : selectedChildWrappers) {
                if (model.isAvailableToUpdateAuthQty(wrapper)) {
                    availableChildWrappers.add(wrapper);
                }
            }
            if (selectedChildWrappers.size() != availableChildWrappers.size()) {
                displayWarning(StockCountMessageText.UPDATE_AUTH_WARNING);
            }
            if (availableChildWrappers.size() > 0) {
                if (model.hasActiveUsers(availableChildWrappers)) {
                    if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.COMPLETE_CONFIRM_LOCK)) {
                        return;
                    }
                }
                try {
                    showScreenBusy(true);
                    model.breakAllLocks(availableChildWrappers);
                    model.updateAuthorizeQuantities(availableChildWrappers);
                } finally {
                    showScreenBusy(false);
                }
            }
        }
    }

    /****************************************************************************************************
     * HANDLE CONFIRM AUTHORIZATION
     ***************************************************************************************************/

    public void handleConfirmAuthorization() throws Exception {
        List<StockCountChildWrapper> readyChildCounts = model.findLocationsReadyToConfirm(getAllRowWrappers());
        if (readyChildCounts.isEmpty()) {
            displayWarning(StockCountMessageText.NO_LOCATIONS_TO_CONFIRM);
            return;
        }
        if (RConfirmUtility.confirm("Confirmation", StockCountMessageText.BLANK_TO_LAST_COUNTED_CONFIRM)) {
            if (model.hasActiveUsers(readyChildCounts)) {
                if (!RConfirmUtility.confirm("Confirmation", StockCountMessageText.ACTIVE_LOCK_WARNING)) {
                    return;
                }
            }
            if (model.getStockCountWrapper().isUnitAndAmount()) {
                handleConfirmUnitAndAmount(readyChildCounts);
            } else {
                handleConfirmRegular(readyChildCounts);
            }
        }
    }

    private void handleConfirmRegular(List<StockCountChildWrapper> wrappers) throws Exception {
        showScreenBusy(true);
        try {
            model.breakAllLocks(wrappers);
            model.updateAuthorizeQuantities(wrappers);
            model.markStockCountChildsReadyToApprove(wrappers);
            populateScreen();
        } finally {
            showScreenBusy(false);
        }
        // Thread The Actual Authorization
        RProgressFrame frame = new RProgressFrame();
        frame.start(new MonitorAuthorizationTask());
    }

    private void handleConfirmUnitAndAmount(List<StockCountChildWrapper> wrappers) throws Exception {
        showScreenBusy(true);
        try {
            model.breakAllLocks(wrappers);
            model.markStockCountChildsReadyToApprove(wrappers);
            populateScreen();
        } finally {
            showScreenBusy(false);
        }
        // Thread The Actual Authorization
        RProgressFrame frame = new RProgressFrame();
        frame.start(new MonitorAuthorizationTask());
    }

    /****************************************************************************************************
     * Handle Auto Authorize Of Complete Count
     ***************************************************************************************************/

    private void handleAutoAuthorize() throws Exception {
        StockCount stockCount = model.getStockCountWrapper().getStockCount();
        if (stockCount.isUnitAndAmount() && stockCount.isAutoAuthorize()) {
            List<StockCountChildWrapper> readyWrappers = new ArrayList<StockCountChildWrapper>();
            for (StockCountChildWrapper wrapper : getAllRowWrappers()) {
                StockCountStatus status = wrapper.getStatus();
                if (status == StockCountStatus.APPROVAL_SCHEDULED || status == StockCountStatus.APPROVAL_IN_PROGRESS) {
                    readyWrappers.add(wrapper);
                }
            }
            handleConfirmUnitAndAmount(readyWrappers);
        }
    }

    /****************************************************************************************************
     * Action Listener
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(VIEW_DETAILS)) {
                doViewDetails();
            } else if (command.equals(STOCK_COUNT_CHILD_SELECTED)) {
                doStockCountChildSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doViewDetails() {
        StockCountDetailDialog dialog = new StockCountDetailDialog();
        dialog.setStockCount(model.getStockCountWrapper().getStockCount());
        dialog.setVisible(true);
    }

    private void doStockCountChildSelected() throws Exception {
        StockCountChildWrapper childWrapper = (StockCountChildWrapper) locationTable.getSelectedRowData();
        showScreenBusy(true);
        try {
            StockCount stockCount = model.getStockCountWrapper().getStockCount();
            if (stockCount.getType() == StockCountType.RMS_SYNC) {
                return;
            }
            if (model.isThirdPartyImportNotFinished(childWrapper)) {
                throw new BusinessException(StockCountMessageText.COUNT_NOT_ACCESSIBLE);
            }
            if (model.isMaxSizeExceeded(childWrapper)) {
                throw new BusinessException(StockCountMessageText.MAX_SIZE_EXCEEDED);
            }
            boolean selectionValid = true;
            StockCountStatus status = childWrapper.getStatus();
            if (status.getPhase() != StockCountPhase.AUTHORIZE) {
                if (!isEditPermissionDenied()) {
                    selectionValid = model.obtainStockCountChildLock(childWrapper);
                }
            } else if (status == StockCountStatus.APPROVAL_SCHEDULED) {
                selectionValid = RConfirmUtility.confirmWithOkCancelType("Confirmation", StockCountMessageText.READY_FOR_AUTHORIZE);
            } else if (status == StockCountStatus.APPROVAL_IN_PROGRESS) {
                selectionValid = RConfirmUtility.confirmWithOkCancelType("Confirmation", StockCountMessageText.READY_FOR_AUTHORIZE);
            }
            if (selectionValid) {
                model.storeStockCountChild(childWrapper);
                navigate(SimScreenName.STOCK_COUNT_DETAIL_SCREEN);
            }
        } finally {
            showScreenBusy(false);
        }
    }

    /****************************************************************************************************
     * Stock Count Location Definition
     ***************************************************************************************************/

    private class StockCountChildDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StockCountChildWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Child ID", "id"));
            attributes.add(new SimTableAttribute("Child Description", "description"));
            if (model.isBreakdownSequenced()) {
                attributes.add(new SimTableAttribute("Area", "area", new TranslatedObjectDisplayer()));
            }
            attributes.add(new SimTableAttribute("Type", "phase", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("User", "user"));
            attributes.add(new SimTableAttribute("Items Left To Count", "itemsLeftToCount"));
            return attributes;
        }
    }

    /****************************************************************************************************
     * Monitor Authorization Task - Exceptions are handled with a recovery process.
     ***************************************************************************************************/

    private class MonitorAuthorizationTask extends UIProgressTask {

        public MonitorAuthorizationTask() {
            setTitle("Monitor");
            setPermanentMessage(StockCountMessageText.AUTHORIZE_IN_PROGRESS);
        }

        public boolean executeRequest() {
            try {
                model.markStockCountChildApproved(getAllRowWrappers());
                populateScreen();
            } catch (Exception e) {
                // Ingore Exceptions...
                LogService.debug(this, "ignoring Excepton");
            }
            return true;
        }
    }
}
