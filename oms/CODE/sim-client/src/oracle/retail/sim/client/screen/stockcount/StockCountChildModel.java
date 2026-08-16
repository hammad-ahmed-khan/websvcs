package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountChild;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSalesProcess;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.stockcount.StockCountTimeframe;
import oracle.retail.sim.common.stockcount.StockCountType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.stockcount.StockCountChildServices;

/********************************************************************************************************
 * Stock Count Location Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountChildModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;

    public StockCountWrapper getStockCountWrapper() {
        return stockCountWrapper;
    }

    public void loadStockCountWrapper() {
        stockCountWrapper = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
    }

    public void reloadStockCountWrapper() throws Exception {
        StockCount stockCount = ClientServiceFactory.getStockCountServices().readStockCount(stockCountWrapper.getStockCount().getId());
        stockCountWrapper = ClientWrapperFactory.createStockCountWrapper(stockCount);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, stockCountWrapper);
    }

    public boolean obtainStockCountChildLock(StockCountChildWrapper wrapper) throws Exception {
        return obtainLock(ActivityLockType.STOCK_COUNT_CHILD, wrapper.getId(), CommonMessageText.LOCK_HELD_CONFIRM);
    }

    public void storeStockCountChild(StockCountChildWrapper wrapper) {
        stockCountWrapper.setActiveStockCountChild(wrapper.getStockCountChild());
    }

    public void clearStockCountChild() {
        stockCountWrapper.setActiveStockCountChild(null);
    }

    public List<StockCountChildWrapper> getLocationWrappers() throws Exception {
        List<StockCountChild> stockCountChilds = ClientServiceFactory.getStockCountChildServices().findStockCountChilds(stockCountWrapper.getId());
        List<StockCountChildWrapper> wrappers = new ArrayList<>();
        for (StockCountChild stockCountChild : stockCountChilds) {
            wrappers.add(ClientWrapperFactory.createStockCountChildWrapper(stockCountChild));
        }
        return wrappers;
    }

    public boolean isFutureStockCount() {
        return stockCountWrapper.getPhase() == StockCountPhase.FUTURE;
    }

    public boolean isRMSSyncStockCount() {
        return stockCountWrapper.isRMSSync();
    }

    public boolean isPrintOptionAvailable() {
        return stockCountWrapper.getType() != StockCountType.RMS_SYNC;
    }

    public boolean isCompleteOptionAvailable() {
        return stockCountWrapper.getCountingMethod() != StockCountingMethod.THIRD_PARTY;
    }

    public boolean isRejectItemsOptionAvailable() {
        return stockCountWrapper.getPhase() == StockCountPhase.AUTHORIZE;
    }

    public boolean isAuthorizeOptionAvailable(List<StockCountChildWrapper> wrappers) {
        for (StockCountChildWrapper locationWrapper : wrappers) {
            if (locationWrapper.getPhase() == StockCountPhase.AUTHORIZE) {
                return true;
            }
        }
        return false;
    }

    public boolean isUpdateAuthQtyOptionAvailable(List<StockCountChildWrapper> wrappers) {
        if (stockCountWrapper.getStockCount().isAllItems()) {
            return false;
        }
        if (stockCountWrapper.getCountingMethod() == StockCountingMethod.THIRD_PARTY) {
            return false;
        }
        for (StockCountChildWrapper locationWrapper : wrappers) {
            StockCountStatus status = locationWrapper.getStatus();
            if (status == StockCountStatus.APPROVAL_SCHEDULED || status == StockCountStatus.APPROVAL_IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    public boolean isConfirmAuthorizationOptionAvailable(List<StockCountChildWrapper> wrappers) {
        for (StockCountChildWrapper locationWrapper : wrappers) {
            StockCountStatus status = locationWrapper.getStatus();
            if (status == StockCountStatus.APPROVAL_SCHEDULED || status == StockCountStatus.APPROVAL_IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    public boolean isEditPermissionDenied() {
        StockCountType type = stockCountWrapper.getType();
        if (type == StockCountType.ADHOC) {
            return !hasPermission(PermissionKey.PC_EDIT_ADHOC_STOCK_COUNT);
        } else if (type == StockCountType.UNIT) {
            return !hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
        } else if (type == StockCountType.PROBLEM_LINE) {
            return !hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
        } else if (type == StockCountType.UNIT_AMOUNT) {
            return !hasPermission(PermissionKey.PC_EDIT_UNIT_AMOUNT_STOCK_COUNT);
        }
        return true;
    }

    public boolean isBreakdownSequenced() {
        if (stockCountWrapper != null) {
            return stockCountWrapper.isBreakdownSequenced();
        }
        return false;
    }

    public boolean isProcessTimeFrameRequired() throws Exception {
        StockCount stockCount = stockCountWrapper.getStockCount();
        if (stockCount.getTimeframe() != StockCountTimeframe.NONE) {
            return false;
        }
        if (stockCount.getPhase() != StockCountPhase.COUNT) {
            return false;
        }
        boolean isDailyProcessing = false;
        if (stockCount.isUnitAndAmount()) {
            isDailyProcessing = StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA));
        } else {
            isDailyProcessing = StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT));
        }
        if (isDailyProcessing) {
            if (SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_DISPLAY_DEFAULT_TIMEFRAME)) {
                return true;
            }
            String timeframe = getStoreString(StoreConfigKeys.STOCK_COUNT_DEFAULT_TIMEFRAME);
            setTimeframe(StockCountTimeframe.getDefaultTimeframe(timeframe));
        }
        return false;
    }

    public MessageText getDefaultTimeFrameMessage() {
        String timeframeText = getStoreString(StoreConfigKeys.STOCK_COUNT_DEFAULT_TIMEFRAME);
        if (timeframeText.equals(StockCountMessageText.BEFORE_STORE_OPEN.getText())) {
            return StockCountMessageText.BEFORE_OPEN_MESSAGE;
        }
        return StockCountMessageText.AFTER_CLOSE_MESSAGE;
    }

    public void setTimeframe(StockCountTimeframe timeframe) throws Exception {
        StockCount stockCount = stockCountWrapper.getStockCount();
        stockCount.setTimeframe(timeframe);
        ClientServiceFactory.getStockCountServices().update(stockCount);
    }

    public ReportResponse printStockCountChild(List<RetailStoreFormatPrinter> formatPrinters, StockCountChildWrapper locationWrapper) throws Exception {
        Long stockCountId = stockCountWrapper.getId();
        Long stockCountChildId = locationWrapper.getStockCountChild().getId();
        Long storeId = getStoreId();
        return StockCountPrintUtility.printStockCount(stockCountId, stockCountChildId, storeId, locationWrapper.getPhase(), formatPrinters);
    }

    public void markStockCountAsStarted() throws Exception {
        stockCountWrapper.markStockCountAsStarted();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_MODIFIED, Boolean.TRUE);
    }

    public void markStockCountChildAsStarted(List<StockCountChildWrapper> wrappers) throws Exception {
        List<Long> stockCountChildIds = new ArrayList<Long>();
        for (StockCountChildWrapper wrapper : wrappers) {
            stockCountChildIds.add(wrapper.getId());
        }
        stockCountWrapper.markStockCountChildAsStarted(stockCountChildIds);
        reloadStockCountWrapper();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_MODIFIED, Boolean.TRUE);
    }

    public boolean isAvailableToComplete(StockCountChildWrapper locationWrapper) {
        Date countSnapshotTime = locationWrapper.getStockCountChild().getCountSnapshotTime();
        boolean isLocationSnapshot = isSnapshotTaken(locationWrapper);
        if (stockCountWrapper.getStockCount().isUnitAndAmount() && countSnapshotTime != null) {
            isLocationSnapshot = true;
        }
        if (isLocationSnapshot) {
            StockCountStatus status = locationWrapper.getStatus();
            if (status == StockCountStatus.COUNT_IN_PROGRESS) {
                return true;
            }
            if (status == StockCountStatus.RECOUNT_SCHEDULED || status == StockCountStatus.RECOUNT_IN_PROGRESS) {
                return true;
            }
        }
        return false;
    }

    public boolean isSnapshotTaken(StockCountChildWrapper childCountWrapper) {
        StockCountChild childCount = childCountWrapper.getStockCountChild();
        if (childCount.getPhase() == StockCountPhase.COUNT) {
            return childCount.getCountSnapshotTime() != null;
        }
        if (childCount.getPhase() == StockCountPhase.RECOUNT) {
            if (stockCountWrapper.isUnitAndAmount()) {
                return childCount.getCountSnapshotTime() != null;
            }
            return childCount.getRecountSnapshotTime() != null;
        }
        return true;
    }

    public boolean isAvailableToUpdateAuthQty(StockCountChildWrapper wrapper) {
        StockCountStatus status = wrapper.getStatus();
        if (status == StockCountStatus.APPROVAL_SCHEDULED || status == StockCountStatus.APPROVAL_IN_PROGRESS) {
            return true;
        }
        return false;
    }

    public boolean isNonCountedRowFound(List<StockCountChildWrapper> availableToProcessWrappers) throws Exception {
        List<Long> stockCountChildIds = new ArrayList<>();
        for (StockCountChildWrapper wrapper : availableToProcessWrappers) {
            stockCountChildIds.add(wrapper.getStockCountChild().getId());
        }
        if (stockCountChildIds.isEmpty()) {
            return false;
        }
        Long storeId = stockCountWrapper.getStockCount().getStoreId();
        Long stockcountId = stockCountWrapper.getStockCount().getId();
        return ClientServiceFactory.getStockCountChildServices().isLocationLineItemsUncounted(storeId, stockcountId, stockCountChildIds);
    }

    public boolean isThirdPartyImportNotFinished(StockCountChildWrapper locationWrapper) {
        if (stockCountWrapper.getCountingMethod() == StockCountingMethod.THIRD_PARTY) {
            return locationWrapper.getPhase() != StockCountPhase.AUTHORIZE;
        }
        return false;
    }

    public boolean isMaxSizeExceeded(StockCountChildWrapper locationWrapper) {
        if (stockCountWrapper.isUnitAndAmount()) {
            return locationWrapper.getStockCountChild().getTotalItems() > SimConfigManager.MAX_PRODUCT_GROUP_ITEMS;
        }
        return false;
    }

    public void loadRejectedItems() throws Exception {
        Long storeId = stockCountWrapper.getStoreId();
        Long stockCountId = stockCountWrapper.getId();

        List<StockCountRejectedLineItem> lineItems = ClientServiceFactory.getStockCountLineItemServices().findRejectedLineItems(storeId, stockCountId);
        if (lineItems.isEmpty()) {
            throw new BusinessException(StockCountMessageText.REJECTED_ITEMS_NOT_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, stockCountWrapper);
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS, lineItems);

        if (stockCountWrapper.getStockCount().getStatus().getCode() < StockCountStatus.APPROVAL_PROCESSING.getCode()) {
            RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS_EDITABLE, Boolean.TRUE);
        } else {
            RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_REJECTED_ITEMS_EDITABLE, Boolean.FALSE);
        }
    }

    public MessageText getUpdateAuthQtyMessage() {
        if (stockCountWrapper.isUnitAndAmount() || SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_UPDATE_ALL_SOH)) {
            return StockCountMessageText.NO_AUTH_QTY_ALL_CONFIRM;
        }
        return StockCountMessageText.NO_AUTH_QTY_DISC_CONFIRM;
    }

    // Unit and Amount & Null Quantity System Flag must be checked first.
    public MessageText getStockCountCompleteMessage(boolean hasNullCounts) {
        StockCount stockCount = stockCountWrapper.getStockCount();
        if (hasNullCounts) {
            if (stockCount.isUnitAndAmount() || SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_NULL_QUANTITY)) {
                return StockCountMessageText.BLANK_TO_ZERO_CONFIRM;
            }
            if (StockCountPhase.RECOUNT.equals(stockCount.getPhase())) {
                return StockCountMessageText.NOT_RECOUNTED_CONFIRM;
            }
            return StockCountMessageText.NOT_COUNTED_CONFIRM;
        }
        return StockCountMessageText.COMPLETE_CONFIRM;
    }

    public void markStockCountChildAsCompleted(List<StockCountChildWrapper> wrappers) throws Exception {
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
        for (StockCountChildWrapper wrapper : wrappers) {
            stockCountWrapper.setActiveStockCountChild(wrapper.getStockCountChild());
            stockCountWrapper.markStockCountChildAsCompleted();
        }
    }

    public void updateAuthorizeQuantities(List<StockCountChildWrapper> wrappers) throws Exception {
        List<Long> stockCountChildIds = new ArrayList<>();
        for (StockCountChildWrapper wrapper : wrappers) {
            stockCountChildIds.add(wrapper.getId());
        }
        ClientServiceFactory.getStockCountChildServices().updateAuthorizationQuantities(stockCountWrapper.getId(), stockCountChildIds);
    }

    public List<StockCountChildWrapper> findLocationsReadyToConfirm(List<StockCountChildWrapper> wrappers) {
        List<StockCountChildWrapper> readyWrappers = new ArrayList<StockCountChildWrapper>();
        for (StockCountChildWrapper wrapper : wrappers) {
            StockCountStatus status = wrapper.getStatus();
            if (status == StockCountStatus.APPROVAL_SCHEDULED || status == StockCountStatus.APPROVAL_IN_PROGRESS) {
                readyWrappers.add(wrapper);
            }
        }
        return readyWrappers;
    }

    public void markStockCountChildsReadyToApprove(List<StockCountChildWrapper> wrappers) throws Exception {
        List<Long> childIds = new ArrayList<Long>();
        for (StockCountChildWrapper wrapper : wrappers) {
            childIds.add(wrapper.getId());
        }
        StockCount stockCount = stockCountWrapper.getStockCount();
        ClientServiceFactory.getStockCountChildServices().markStockCountChildsReadyToApprove(stockCount.getId(), childIds);
        StockCount updatedStockCount = ClientServiceFactory.getStockCountServices().readStockCount(stockCount.getId());
        stockCountWrapper = ClientWrapperFactory.createStockCountWrapper(updatedStockCount);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, stockCountWrapper);
    }

    public void markStockCountChildApproved(List<StockCountChildWrapper> wrappers) throws Exception {
        StockCount stockCount = stockCountWrapper.getStockCount();
        StockCountChildServices stockCountChildServices = ClientServiceFactory.getStockCountChildServices();
        for (StockCountChildWrapper wrapper : wrappers) {
            StockCountChild stockCountChild = wrapper.getStockCountChild();
            if (stockCountChild.getStatus() == StockCountStatus.APPROVAL_PROCESSING) {
                stockCountChildServices.markStockCountChildApproved(stockCount.getId(), stockCountChild.getId());
            }
        }
        reloadStockCountWrapper();
    }

    public List<RetailStoreFormatPrinter> getFormatPrinters() throws Exception {
        return SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.STOCK_COUNT);
    }

    public boolean hasActiveUsers(List<StockCountChildWrapper> wrappers) throws Exception {
        Set<String> activityIds = new HashSet<String>();
        for (StockCountChildWrapper wrapper : wrappers) {
            activityIds.add(wrapper.getStockCountChild().getIdAsString());
        }
        Set<String> owners = ActivityLockUtility.findActivityLockOwners(ActivityLockType.STOCK_COUNT_CHILD, activityIds);
        return owners.size() > 0;
    }

    public void breakAllLocks(List<StockCountChildWrapper> wrappers) throws Exception {
        Set<String> activityIds = new HashSet<String>();
        for (StockCountChildWrapper wrapper : wrappers) {
            activityIds.add(wrapper.getStockCountChild().getIdAsString());
        }
        ActivityLockUtility.releaseAllActivityLocks(ActivityLockType.STOCK_COUNT_CHILD, activityIds);
    }
}
