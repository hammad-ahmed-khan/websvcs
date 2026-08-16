package oracle.retail.sim.client.screen.itemrequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestLineItem;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.itemrequest.ItemRequestStatus;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.ItemRequestReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Request Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestDetailModel extends SimScreenModel {
    private ItemRequest itemRequest;
    private boolean hasItemRequestLock;

    public void loadItemRequest() throws Exception {
        itemRequest = (ItemRequest) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM_REQUEST);

        if (itemRequest == null) {
            itemRequest = BOFactory.createItemRequest(getStoreId());
            itemRequest.setUsername(getUserName());
            itemRequest.doSetCreateDate(SimDateUtil.getCurrentDate());
            itemRequest.doSetReqDeliveryDate(SimDateUtil.getTomorrowAtStartOfDay(getTimeZone()));
        } else {
            itemRequest.setReadyToSendFlag(false);
            itemRequest.setUsername(getUserName());
            validateLockForEditMode();
        }
    }

    private void validateLockForEditMode() throws Exception {
        if (isItemRequestPending() && hasPermission(PermissionKey.PC_EDIT_ITEM_REQUEST)) {
            hasItemRequestLock = obtainLock(ActivityLockType.ITEM_REQUEST, itemRequest.getIdAsString());
        }
    }

    public ItemRequest getItemRequest() {
        return itemRequest;
    }

    public Store getStore(Long storeId) throws Exception {
        return ClientDataCacheUtility.getStore(storeId);
    }

    public boolean isItemRequestPending() {
        return itemRequest.getStatus() == ItemRequestStatus.PENDING;
    }

    public boolean isItemRequestCancelled() {
        return itemRequest.getStatus() == ItemRequestStatus.CANCELED;
    }

    public boolean isItemRequestCompleteOrCancelled() {
        return itemRequest.getStatus() == ItemRequestStatus.COMPLETED || itemRequest.getStatus() == ItemRequestStatus.CANCELED;
    }

    public boolean isItemRequestUnmodifiable() {
        if (itemRequest.isNew()) {
            return !hasPermission(PermissionKey.PC_CREATE_ITEM_REQUEST);
        }
        if (isItemRequestPending()) {
            return !hasPermission(PermissionKey.PC_EDIT_ITEM_REQUEST) && !hasItemRequestLock;
        }
        return true;
    }

    public boolean isAddItemAvailable() {
        if (itemRequest.isNew()) {
            return true;
        }
        return hasPermission(PermissionKey.PC_ADD_ITEM_TO_ITEM_REQUEST);
    }

    public boolean isScannerAvailable() {
        return !isItemRequestUnmodifiable();
    }

    public boolean isMultiDeliveryAllowed() {
        return getStoreBoolean(StoreConfigKeys.DISPLAY_ITEM_REQUEST_DELIVERY_TIMESLOT);
    }

    public void validateItemCount(int rowCount) throws BusinessException {
        Integer maxValue = SimConfigManager.getInteger(SimConfigManager.PRODUCT_GROUP_ITEM_REQUEST_LIMIT);
        if (maxValue == null) {
            maxValue = SimConfigManager.MAX_PRODUCT_GROUP_ITEMS;
        }
        if (rowCount + 1 > maxValue) {
            throw new BusinessException(ItemRequestMessageText.MAX_ITEM_COUNT_EXCEEDED);
        }
    }

    public ItemRequestLineItemWrapper buildRequestLineItem() {
        return ClientWrapperFactory.createItemRequestLineItemWrapper(itemRequest);
    }

    public List<ItemRequestLineItemWrapper> getItemRequestLineItems() {
        List<ItemRequestLineItemWrapper> wrappers = new ArrayList<>(itemRequest.getNumberOfLineItems());
        for (ItemRequestLineItem lineItem : itemRequest.getLineItems()) {
            wrappers.add(ClientWrapperFactory.createItemRequestLineItemWrapper(itemRequest, lineItem));
        }
        return wrappers;
    }

    public void updateExistingLineItem(ItemRequestLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (wrapper.getOrderItem() != null && barcodeItem.getQuantity().isPositive()) {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setQuantity(wrapper.getQuantityOrZero().add(barcodeItem.getQuantity()));
            }
            return;
        }
        throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
    }

    public List<DeliveryTimeSlot> getDeliveryTimeSlots() throws Exception {
        return ClientDataCacheUtility.getDeliveryTimeSlots();
    }

    public void applyDefaultDeliveryTimeSlot(ItemRequestLineItemWrapper wrapper) throws Exception {
        DeliveryTimeSlot defaultTimeSlot = itemRequest.getDeliveryTimeSlot();
        if (wrapper.getDeliveryTimeslot() == null && defaultTimeSlot != null) {
            if (hasDataPermission(PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT, defaultTimeSlot.getId())) {
                wrapper.setDeliveryTimeslot(defaultTimeSlot);
            }
        }
    }

    public boolean hasLineItemWithNoQuantity() {
        for (ItemRequestLineItem lineItem : itemRequest.getLineItems()) {
            if (lineItem.getQuantity() == null) {
                return true;
            }
        }
        return false;
    }

    public void releaseRequestLock() throws Exception {
        if (itemRequest.getId() != null) {
            releaseLock(ActivityLockType.ITEM_REQUEST, itemRequest.getIdAsString());
        }
    }

    public boolean checkRequestLock() throws Exception {
        if (itemRequest.getId() != null) {
            return confirmLock(ActivityLockType.ITEM_REQUEST, itemRequest.getIdAsString());
        }
        return true;
    }

    public boolean isLineItemDeletable() {
        return itemRequest.isPropertyModifiable("itemRequestLineItem");
    }

    public void deleteLineItem(ItemRequestLineItemWrapper wrapper) throws BusinessException {
        ItemRequestLineItem lineItem = wrapper.getLineItem();
        if (lineItem != null) {
            itemRequest.removeLineItem(lineItem);
        }
    }

    public void updateItemRequest(Date deliveryDate) throws Exception {
        itemRequest.setReqDeliveryDate(deliveryDate, getTimeZone());
        if (itemRequest.isCoherent(getTimeZone())) {
            if (itemRequest.isNew()) {
                ClientServiceFactory.getItemRequestServices().insertItemRequest(itemRequest);
            } else {
                ClientServiceFactory.getItemRequestServices().updateItemRequest(itemRequest);
            }
            markRequestAsModified();
            releaseRequestLock();
        }
    }

    public void saveItemRequest() throws Exception {
        ClientServiceFactory.getItemRequestServices().updateItemRequest(itemRequest);
        markRequestAsModified();
        releaseRequestLock();
    }

    public void processItemRequest() throws Exception {
        try {
            itemRequest.setReadyToSendFlag(true);
            if (isItemRequestPending() && itemRequest.isCoherent(getTimeZone())) {
                ClientServiceFactory.getItemRequestServices().confirmItemRequest(itemRequest);
            }
        } catch (Exception exception) {
            itemRequest.setReadyToSendFlag(false);
            throw exception;
        }
    }

    public void cancelRequest() throws Exception {
        if (itemRequest.isNew()) {
            return;
        }
        ClientServiceFactory.getItemRequestServices().cancelItemRequest(itemRequest.getId());
        markRequestAsModified();
    }

    public void printItemRequest() throws Exception {
        if (itemRequest != null) {
            List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.ITEM_REQUEST);

            if (formatPrinters != null && formatPrinters.size() > 0) {
                ItemRequestReportRequest reportRequest = BOFactory.createItemRequestReportRequest(itemRequest.getId());
                SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, ItemRequestMessageText.REPORT_PRINTED);
            }
        }
    }

    private void markRequestAsModified() {
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_REQUEST_DETAIL_MODIFIED, Boolean.TRUE);
    }
}
