package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockCountItem;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountSalesProcess;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountStatus;
import oracle.retail.sim.common.stockcount.StockCountTimeframe;
import oracle.retail.sim.common.stockcount.StockCountType;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountDetailModel extends SimScreenModel {
    private StockCountWrapper stockCountWrapper;

    public void loadStockCountWrapper() {
        stockCountWrapper = (StockCountWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_STOCK_COUNT);
    }

    public StockCountWrapper getStockCountWrapper() {
        return stockCountWrapper;
    }

    public boolean isCountMode() {
        if (stockCountWrapper == null) {
            loadStockCountWrapper();
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.COUNT) {
            return true;
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.AUTHORIZE) {
            return !stockCountWrapper.isRecountRequired();
        }
        return false;
    }

    public boolean isRecountMode() {
        if (stockCountWrapper == null) {
            loadStockCountWrapper();
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.RECOUNT) {
            return true;
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.AUTHORIZE) {
            return stockCountWrapper.isRecountRequired();
        }
        return false;
    }

    public boolean isGuidedStockCount() {
        if (stockCountWrapper == null) {
            loadStockCountWrapper();
        }
        return stockCountWrapper.getCountingMethod() == StockCountingMethod.GUIDED;
    }

    public boolean isStockCountChildEditable() {
        if (stockCountWrapper == null) {
            loadStockCountWrapper();
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.AUTHORIZE) {
            return false;
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.FUTURE) {
            return false;
        }
        if (stockCountWrapper.getLocationDisplayStatus() == StockCountDisplayStatus.PENDING) {
            return false;
        }
        if (stockCountWrapper.isUnitAndAmount() || stockCountWrapper.isLocationAlreadySnapshot()) {
            return hasStockCountChildPermissions();
        }
        return false;
    }

    public boolean hasStockCountChildPermissions() {
        if (stockCountWrapper.getLocationPhase() != StockCountPhase.AUTHORIZE) {
            StockCountType type = stockCountWrapper.getType();
            if (type == StockCountType.ADHOC) {
                return hasPermission(PermissionKey.PC_EDIT_ADHOC_STOCK_COUNT);
            } else if (type == StockCountType.UNIT) {
                return hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
            } else if (type == StockCountType.PROBLEM_LINE) {
                return hasPermission(PermissionKey.PC_EDIT_UNIT_STOCK_COUNT);
            } else if (type == StockCountType.UNIT_AMOUNT) {
                return hasPermission(PermissionKey.PC_EDIT_UNIT_AMOUNT_STOCK_COUNT);
            }
        }
        return false;
    }

    public boolean isStockCountChildComplete() {
        return stockCountWrapper.getLocationDisplayStatus() == StockCountDisplayStatus.COMPLETED;
    }

    public boolean isFutureStockCount() {
        return stockCountWrapper.getPhase() == StockCountPhase.FUTURE;
    }

    public boolean isTakeSnapshotAvailable() {
        StockCountType type = stockCountWrapper.getType();
        if (type == StockCountType.UNIT || type == StockCountType.PROBLEM_LINE) {
            return !stockCountWrapper.isLocationAlreadySnapshot();
        }
        return false;
    }

    public boolean isCompleteAvailable() {
        StockCount stockCount = stockCountWrapper.getStockCount();
        StockCountStatus status = stockCountWrapper.getStockCountChild().getStatus();
        if (status == StockCountStatus.COUNT_IN_PROGRESS && stockCountWrapper.isLocationAlreadySnapshot()) {
            return hasPermission(PermissionKey.PC_COMPLETE_STOCK_COUNT);
        }
        if (status == StockCountStatus.RECOUNT_SCHEDULED && stockCount.isUnitAndAmount()) {
            return hasPermission(PermissionKey.PC_COMPLETE_STOCK_COUNT);
        }
        if (status == StockCountStatus.RECOUNT_IN_PROGRESS && stockCountWrapper.isLocationAlreadySnapshot()) {
            return hasPermission(PermissionKey.PC_COMPLETE_STOCK_COUNT);
        }
        return false;
    }

    public boolean isScannerAvailable() {
        if (stockCountWrapper == null) {
            loadStockCountWrapper();
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.AUTHORIZE) {
            return false;
        }
        if (stockCountWrapper.getLocationPhase() == StockCountPhase.FUTURE) {
            return false;
        }
        if (stockCountWrapper.getLocationDisplayStatus() == StockCountDisplayStatus.PENDING) {
            return false;
        }
        return true;
    }

    public boolean isSnapshotRequired() {
        if (stockCountWrapper.isUnitAndAmount() || stockCountWrapper.isLocationAlreadySnapshot()) {
            return false;
        }
        return true;
    }

    public boolean isTimeFrameEditorVisible() {
        if (stockCountWrapper.getPhase() == StockCountPhase.COUNT) {
            if (SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_DISPLAY_DEFAULT_TIMEFRAME)) {
                if (stockCountWrapper.isUnitAndAmount()) {
                    return StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA));
                }
                return StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT));
            }
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
        boolean isDisplayActive = SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_DISPLAY_DEFAULT_TIMEFRAME);
        boolean isDailyProcessing = false;
        if (stockCount.isUnitAndAmount()) {
            isDailyProcessing = StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA));
        } else {
            isDailyProcessing = StockCountSalesProcess.isDaily(SimConfigManager.getString(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT));
        }
        if (isDisplayActive && isDailyProcessing) {
            return true;
        }
        String timeframe = getStoreString(StoreConfigKeys.STOCK_COUNT_DEFAULT_TIMEFRAME);
        setTimeframe(StockCountTimeframe.getDefaultTimeframe(timeframe));
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

    public boolean lostStockCountChildLock() throws Exception {
        return !confirmLock(ActivityLockType.STOCK_COUNT_CHILD, stockCountWrapper.getStockCountChild().getId());
    }

    public void releaseStockCountChildLock() throws Exception {
        releaseLock(ActivityLockType.STOCK_COUNT_CHILD, stockCountWrapper.getStockCountChild().getId());
    }

    public List<RetailStoreFormatPrinter> getFormatPrinters() throws Exception {
        return SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.STOCK_COUNT);
    }

    public ReportResponse printStockCountChild(List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        Long stockCountId = stockCountWrapper.getId();
        Long locationId = stockCountWrapper.getStockCountChild().getId();
        Long storeId = getStoreId();
        return StockCountPrintUtility.printStockCount(stockCountId, locationId, storeId, stockCountWrapper.getLocationPhase(), formatPrinters);
    }

    public List<ItemCountType> getItemCountOptions() {
        List<ItemCountType> types = new ArrayList<>(3);
        types.add(ItemCountType.ALL);
        types.add(ItemCountType.COUNTED);
        types.add(ItemCountType.UNCOUNTED);
        return types;
    }

    public List<Long> findAvailableDepartmentIds() throws Exception {
        List<StockCountLineItemWrapper> lineItemWrappers = stockCountWrapper.getAllLineItemWrappers();
        List<Long> departmentIds = new ArrayList<>();
        for (StockCountLineItemWrapper wrapper : lineItemWrappers) {
            Long departmentId = wrapper.getLineItem().getStockCountItem().getDepartmentId();
            if (departmentId != null) {
                departmentIds.add(departmentId);
            }
        }
        return departmentIds;
    }

    public List<StockCountLineItemWrapper> filterLineItems(List<StockCountLineItemWrapper> wrappers, ItemCountType countType, MdseHierarchyNode hierarchyNode, DiscrepantFilterType discrepantType) {
        List<StockCountLineItemWrapper> lineItemWrappers = new ArrayList<>();
        for (StockCountLineItemWrapper wrapper : wrappers) {
            if (countType == ItemCountType.COUNTED) {
                if (isRecountMode() && wrapper.getStockRecounted() == null) {
                    continue;
                }
                if (isCountMode() && wrapper.getStockCounted() == null) {
                    continue;
                }
            }
            if (countType == ItemCountType.UNCOUNTED) {
                if (isRecountMode() && wrapper.getStockRecounted() != null) {
                    continue;
                }
                if (isCountMode() && wrapper.getStockCounted() != null) {
                    continue;
                }
            }
            if (hierarchyNode != null) {
                StockCountItem stockCountItem = wrapper.getLineItem().getStockCountItem();
                if (!hierarchyNode.getDepartmentId().equals(stockCountItem.getDepartmentId())) {
                    continue;
                }
                if (hierarchyNode.getClassId() != null) {
                    if (!hierarchyNode.getClassId().equals(stockCountItem.getClassId())) {
                        continue;
                    }
                }
                if (hierarchyNode.getSubclassId() != null) {
                    if (!hierarchyNode.getSubclassId().equals(stockCountItem.getSubclassId())) {
                        continue;
                    }
                }
            }
            if (discrepantType == DiscrepantFilterType.DISCREPANT) {
                if (!wrapper.getLineItem().isDiscrepant()) {
                    continue;
                }
            }
            lineItemWrappers.add(wrapper);
        }
        return lineItemWrappers;
    }

    public boolean isLineItemQuantityNull() throws Exception {
        List<StockCountLineItemWrapper> lineItemWrappers = stockCountWrapper.getLineItemWrappers();
        if (isRecountMode()) {
            for (StockCountLineItemWrapper wrapper : lineItemWrappers) {
                if (wrapper.getStockRecountedBasedOnUom() == null) {
                    return wrapper.getLineItem().isDiscrepant();
                }
            }
        } else {
            for (StockCountLineItemWrapper wrapper : lineItemWrappers) {
                if (wrapper.getStockCountedBasedOnUom() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public MessageText getStockCountMissingValuesMessage() {
        if (stockCountWrapper.isUnitAndAmount() || SimConfigManager.getBoolean(SimConfigManager.STOCK_COUNT_NULL_QUANTITY)) {
            if (isRecountMode()) {
                return StockCountMessageText.RECOUNT_TO_LAST_COUNTED_CONFIRM;
            }
            return StockCountMessageText.BLANK_TO_ZERO_CONFIRM;
        }
        if (isRecountMode()) {
            return StockCountMessageText.NOT_RECOUNTED_CONFIRM;
        }
        return StockCountMessageText.NOT_COUNTED_CONFIRM;
    }

    public MessageText getStockCountCompleteMessage() {
        if (isRecountMode()) {
            return StockCountMessageText.COMPLETE_CONFIRM_RECOUNT;
        }
        return StockCountMessageText.COMPLETE_CONFIRM;
    }

    public void updateExistingLineItem(StockCountLineItemWrapper lineItemWrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.isSerialNumberRequired()) {
            applyBarcodeUin(lineItemWrapper, barcodeItem);
        } else {
            applyBarcodeQty(lineItemWrapper, barcodeItem);
        }
    }

    private void applyBarcodeQty(StockCountLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        Quantity barcodeQuantity = barcodeItem.getQuantity();
        if (barcodeQuantity.isNegative() || barcodeQuantity.isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
            barcodeQuantity = barcodeQuantity.multiply(wrapper.getCaseSize());
        }
        if (isCountMode()) {
            wrapper.setStockCounted(wrapper.getStockCountedOrZero().add(barcodeQuantity));
        } else {
            wrapper.setStockRecounted(wrapper.getStockRecountedOrZero().add(barcodeQuantity));
        }
    }

    private void applyBarcodeUin(StockCountLineItemWrapper lineItemWrapper, BarcodeItem barcodeItem) throws Exception {
        Long stockCountId = Long.valueOf(stockCountWrapper.getId());
        String itemId = barcodeItem.getId();
        String uin = barcodeItem.getUin();
        String uinLabel = barcodeItem.getStockItem().getUINLabel();
        if (StringHelper.isNullOrEmpty(uin)) {
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel, RErrorSeverity.WARNING);
        }
        StockCountSerialNumber serialNumber = ClientServiceFactory.getStockCountLineItemServices().findStockCountSerialNumber(stockCountId, itemId, uin);
        if (serialNumber == null) {
            SerialNumberValue serialNumberVO = ClientServiceFactory.getUINServices().findSerialNumberValue(itemId, uin);
            if (serialNumberVO == null) {
                Object[] values = new String[] { uinLabel, uin, itemId };
                throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
            }
            if (!UINStatus.getValidSetForStockCount().contains(serialNumberVO.getStatus())) {
                Object[] values = new String[] { uinLabel, uin };
                throw new BusinessException(UINMessageText.UIN_STATUS_INVALID_FOR_ACTION, values);
            }
            if (!serialNumberVO.getStoreId().equals(SimRepository.getStoreId())) {
                Object[] values = new String[] { uinLabel, uin, serialNumberVO.getStatus().toString() };
                throw new BusinessException(UINMessageText.UIN_AT_ANOTHER_STORE, values);
            }
            Object[] values = new String[] { uinLabel, uin };
            throw new BusinessException(StockCountMessageText.UIN_NOT_FOUND, values);
        }

        List<StockCountSerialNumber> serialNumbers = new ArrayList<StockCountSerialNumber>(lineItemWrapper.getSerialNumbers());
        for (StockCountSerialNumber tmpSerialNumber : serialNumbers) {
            if (tmpSerialNumber.equals(serialNumber)) {
                throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
            }
        }
        serialNumbers.add(serialNumber);

        lineItemWrapper.setSerialNumbers(serialNumbers, stockCountWrapper.getLocationPhase());
    }

    public void markStockCountLineItemAsDiscrepant(StockCountLineItemWrapper wrapper) throws Exception {
        Long stockCountId = stockCountWrapper.getId();
        Long stockCountChildId = stockCountWrapper.getStockCountChild().getId();
        String itemId = wrapper.getItemId();
        ClientServiceFactory.getStockCountLineItemServices().markStockCountLineItemAsDiscrepant(stockCountId, stockCountChildId, itemId);
        wrapper.getLineItem().doSetDiscrepant(true);
    }

    public void saveStockCountChild() throws Exception {
        stockCountWrapper.saveDirtyLineItems();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
    }

    public void markStockCountChildAsStarted() throws Exception {
        stockCountWrapper.markStockCountChildAsStarted();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_MODIFIED, Boolean.TRUE);
    }

    public void markStockCountChildAsCompleted() throws Exception {
        stockCountWrapper.markStockCountChildAsCompleted();
        RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_LOCATION_MODIFIED, Boolean.TRUE);
    }
}
