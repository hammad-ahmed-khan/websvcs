package oracle.retail.sim.client.screen.shelfreplenishment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.ShelfReplenishmentReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentLineItem;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Shelf Replenishment Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentDetailModel extends SimScreenModel {
    private ShelfReplenishment shelfReplenishment;
    private boolean viewOnly;

    public void loadShelfReplenishment() {
        shelfReplenishment = (ShelfReplenishment) RepositoryManager.getStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL);
    }

    public ShelfReplenishment getShelfReplenishment() {
        return shelfReplenishment;
    }

    public boolean obtainShelfReplenishmentLock() throws Exception {
        if (StringUtility.isNullOrEmpty(shelfReplenishment.getIdAsString())) {
            return true;
        }
        return obtainLock(ActivityLockType.SHELF_REPLENISHMENT, shelfReplenishment.getIdAsString());
    }

    public List<ShelfReplenishmentLineItemWrapper> getLineItems() {
        List<ShelfReplenishmentLineItemWrapper> wrappers = new ArrayList<>();
        for (ShelfReplenishmentLineItem lineItem : shelfReplenishment.getLineItems()) {
            wrappers.add(ClientWrapperFactory.createShelfReplenishmentLineItemWrapper(shelfReplenishment, lineItem));
        }
        return wrappers;
    }

    public ProductGroup readProductGroup(Long productGroupId) throws Exception {
        return ClientServiceFactory.getProductGroupServices().readProductGroup(productGroupId);
    }

    public boolean isLineItemsEditable() {
        if (shelfReplenishment != null) {
            if (isShelfReplenishmentInProgress() || isShelfReplenishmentNew()) {
                return SimDateUtil.isSameDay(getTimeZone(), shelfReplenishment.getCreateDate(), SimDateUtil.getCurrentDate());
            }
            return false;
        }
        return false;
    }

    public boolean isScannerAvailable() {
        return isLineItemsEditable() && !isViewOnly();
    }
    
    public boolean isViewOnly(){
        return viewOnly;
    }
    
    public void setViewOnly(boolean value){
        this.viewOnly = value;
    }

    public boolean isActualQuantityAvailable() {
        if (shelfReplenishment != null) {
            return isShelfReplenishmentInProgress() || isShelfReplenishmentComplete();
        }
        return false;
    }

    public void clearShelfReplenishment() throws Exception {
        if (shelfReplenishment != null) {
            releaseLock(ActivityLockType.SHELF_REPLENISHMENT, shelfReplenishment.getIdAsString());
            shelfReplenishment = null;
        }
    }

    public boolean isCoherent() throws BusinessException {
        return shelfReplenishment.isCoherent();
    }

    public boolean checkShelfReplenishmentLock() throws Exception {
        return confirmLock(ActivityLockType.SHELF_REPLENISHMENT, shelfReplenishment.getIdAsString());
    }

    public boolean isShelfReplenishmentComplete() {
        return shelfReplenishment.getStatus().equals(ShelfReplenishmentStatus.COMPLETE);
    }

    public boolean isShelfReplenishmentInProgress() {
        return shelfReplenishment.getStatus().equals(ShelfReplenishmentStatus.IN_PROGRESS);
    }

    public boolean isShelfReplenishmentNew() {
        return shelfReplenishment.getStatus().equals(ShelfReplenishmentStatus.NEW);
    }

    public boolean isShelfReplenishmentSequenceAltered() {
        return shelfReplenishment.getStatus().equals(ShelfReplenishmentStatus.PENDING_ALTERED);
    }
    
    public boolean isShelfReplenishmentEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_SHELF_REPLENISHMENT);
    }
    
    public boolean isShelfReplenishmentCanceled() {
        return shelfReplenishment.getStatus() == ShelfReplenishmentStatus.CANCELED;
    }
    
    public boolean isCreateDatePastCurrentDate() {
        return !SimDateUtil.isSameDay(getTimeZone(), SimDateUtil.getCurrentDate(), shelfReplenishment.getCreateDate());
    }
    
    public boolean isShelfReplenishmentAdjustable() {
        if (!isShelfReplenishmentEditAllowed()) {
            return false;
        }
        if (shelfReplenishment.getStatus() != ShelfReplenishmentStatus.COMPLETE) {
            return false;
        }
        Date createDate = shelfReplenishment.getCreateDate();
        if (createDate == null) {
            return false;
        }
        TimeZone timeZone = getStore().getTimeZone();
        Date createDateStartOfDay = SimDateUtil.getDateAtStartOfDay(getStore().getTimeZone(), createDate);
        return SimDateUtil.getCurrentDateAtStartOfDay(timeZone).equals(createDateStartOfDay);
    }
    
    public boolean isAllowAdjustment() {
        return shelfReplenishment.isAllowAdjustment();
    }

    public void updateExistingLineItem(ShelfReplenishmentLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isPositive()) {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setActualQuantity(wrapper.getActualQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setActualQuantity(wrapper.getActualQuantityOrZero().add(barcodeItem.getQuantity()));
            }
            return;
        }
        throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
    }

    public void updateShelfReplenishment() throws Exception {
        shelfReplenishment = ClientServiceFactory.getShelfReplenishmentServices().updateShelfReplenishment(shelfReplenishment);
        RepositoryManager.addStateObject(SimClientStateKey.SHELF_REPLENISHMENT_DETAIL_MODIFIED, Boolean.TRUE);
    }

    public void completeShelfReplenishment() throws Exception {
        shelfReplenishment.setComplete();
        updateShelfReplenishment();
    }

    public void printPendingShelfReplenishment() throws Exception {
        ReportResponse response = printShelfReplenishment();
        if (response == null || response.isFailedState()) {
            return;
        }
        completeShelfReplenishment();
    }

    public ReportResponse printShelfReplenishment() throws Exception {
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.SHELF_REPLENISHMENT);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            ShelfReplenishmentReportRequest reportRequest = BOFactory.createShelfReplenishmentReportRequest(shelfReplenishment.getId());
            return SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, ShelfReplenishmentMessageText.SHELF_REPLENISHMENT_PRINTED);
        }
        return null;
    }
    
    public void adjustShelfReplenishment() throws Exception {
    	shelfReplenishment.setAllowAdjustment(true);
        shelfReplenishment.markInProgress();
    }
}
