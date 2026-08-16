package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activityhistory.ActivityHistoryVO;
import oracle.retail.sim.common.activityhistory.ActivityType;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryInvoiceEntryType;
import oracle.retail.sim.common.directdelivery.DirectDeliveryLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryLineItemUtility;
import oracle.retail.sim.common.directdelivery.DirectDeliveryMessageText;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliverySimpleLineItem;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.directdelivery.DirectDeliveryValidateUinCommand;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItem;
import oracle.retail.sim.common.directdelivery.PurchaseOrderLineItemUtility;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.DirectDeliveryReportRequest;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Direct Delivery Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class DirectDeliveryDetailModel extends SimScreenModel {
    private static final String INVOICE_LOCK_DELIMITER = ":::";

    private DirectDelivery delivery;
    private boolean viewOnlyMode;
    private Map<String, PurchaseOrderLineItem> purchaseOrderLineItemMap;
    private String supplierInvoiceLockId;

    public void loadState() throws Exception {
        Boolean viewOnly = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.DIRECT_DELIVERY_VIEW_ONLY);
        viewOnlyMode = viewOnly != null && viewOnly || !isDeliveryEditAllowed();
        purchaseOrderLineItemMap = null;
        delivery = (DirectDelivery) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        if (delivery == null) {
            delivery = BOFactory.createDirectDelivery();
            delivery.setStore(getStore());
            delivery.setUserId(getUserName());
            delivery.markInProgress();
        }
        PurchaseOrderVO purchaseOrderVO = delivery.getPurchaseOrder();
        if (purchaseOrderVO != null) {
            PurchaseOrder purchaseOrder = (PurchaseOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
            if (purchaseOrder == null || !purchaseOrderVO.getId().equals(purchaseOrder.getId())) {
                purchaseOrder = ClientServiceFactory.getDirectDeliveryServices().readPurchaseOrder(purchaseOrderVO.getId());
            }
            purchaseOrderLineItemMap = PurchaseOrderLineItemUtility.createPurchaseOrderLineItemMap(purchaseOrder);
        }
    }

    public DirectDelivery getDelivery() {
        return delivery;
    }

    public boolean hasSupplier() {
        return delivery.getSupplier() != null;
    }

    public boolean hasPurchaseOrder() {
        if (delivery == null) {
            delivery = (DirectDelivery) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        }
        return delivery != null && delivery.getPurchaseOrder() != null;
    }

    public boolean isDeliveryEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_DIRECT_DELIVERY);
    }

    public boolean isViewOnlyMode() {
        return viewOnlyMode;
    }

    public boolean isDeliveryNew() {
        return delivery.isNew();
    }

    public boolean isDeliveryClosed() {
        return DirectDeliveryStatus.getClosedSet().contains(delivery.getStatus());
    }

    public boolean isAllowAdjustment() {
        return delivery.isAllowAdjustment();
    }

    public boolean isFulfillmentOrderRelated() {
        return delivery.isFulfillmentOrderRelated();
    }

    public boolean isDeliveryAdjustable() {
        if (viewOnlyMode) {
            return false;
        }
        if (delivery.getStatus() != DirectDeliveryStatus.RECEIVED) {
            return false;
        }
        Date updateDate = delivery.getUpdateDate();
        if (updateDate == null) {
            return false;
        }
        Integer daysAllowed = SimConfigManager.getInteger(SimConfigManager.DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_DIRECT_DELIVERYS);
        if (daysAllowed == null || daysAllowed <= 0) {
            return false;
        }
        TimeZone timeZone = getStore().getTimeZone();
        Date deadlineDate = SimDateUtil.getDateAtEndOfDay(timeZone, updateDate);
        if (daysAllowed > 1) {
            deadlineDate = SimDateUtil.addDays(timeZone, deadlineDate, daysAllowed - 1);
        }
        return SimDateUtil.getCurrentDateAtStartOfDay(timeZone).before(deadlineDate);
    }

    public boolean isAddItemAllowed() {
        if (viewOnlyMode) {
            return false;
        }
        if (isDeliveryClosed()) {
            return false;
        }
        if (isDeliveryNew() && !hasPurchaseOrder()) {
            return true;
        }
        if (!SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_DELIVERY_ON_RECEIVE)) {
            return false;
        }
        return hasPermission(PermissionKey.PC_ADD_ITEM_DIRECT_DELIVERY);
    }

    public boolean isDeleteItemAllowed() {
        if (viewOnlyMode) {
            return false;
        }
        if (isDeliveryClosed()) {
            return false;
        }
        if (isDeliveryNew() && !hasPurchaseOrder()) {
            return true;
        }
        if (!SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_DELIVERY_ON_RECEIVE)) {
            return false;
        }
        return hasPermission(PermissionKey.PC_DELETE_ITEM_DIRECT_DELIVERY);
    }

    public boolean isRejectDeliveryAllowed() {
        if (viewOnlyMode) {
            return false;
        }
        if (isDeliveryClosed()) {
            return false;
        }
        if (!hasPurchaseOrder()) {
            return false;
        }
        if (StringHelper.isNullOrEmpty(delivery.getAsnId())) {
            return false;
        }
        return hasPermission(PermissionKey.PC_ACCESS_REJECT_DELIVERY);
    }

    public boolean isDeliveryEmpty() {
        return delivery.isEmpty();
    }

    public boolean isDeliveryCancelOnConfirm() {
        if (isDeliveryEmpty()) {
            return true;
        }
        if (!hasPurchaseOrder() && !isAllowAdjustment() && !delivery.hasReceivedOrDamagedLineItems()) {
            return true;
        }
        return false;
    }

    public boolean isDeliveryValidForConfirm() throws Exception {
        boolean isDeliveryValidForConfirm = true;
        PurchaseOrder purchaseOrder = selectPurchaseOrder();
        DirectDelivery originalDelivery = ClientServiceFactory.getDirectDeliveryServices().readDirectDelivery(delivery.getId());
        if(purchaseOrder!= null) {
            for (PurchaseOrderLineItem lineItem : purchaseOrder.getLineItems()) {
                boolean isOverReceiptAllowed =  PermissionManager.hasPermission(PermissionKey.PC_OVER_RECEIVE_DIRECT_DELIVERY);
                Quantity totalQtyReceived = Quantity.ZERO;
                for(DirectDeliverySimpleLineItem simpleLineItem : delivery.getSimpleLineItems()) {
                    if(simpleLineItem.getStockItem().getId().equals(lineItem.getSupplierItem().getItemId())) {
                         totalQtyReceived = simpleLineItem.getQuantityDamagedOrZero().add(simpleLineItem.getQuantityReceivedOrZero());
                         break;
                    }
                }
                if (!isOverReceiptAllowed) {
                	Quantity poReceivedQty = null;
                	if(delivery.isAllowAdjustment()) {
                		for(DirectDeliverySimpleLineItem simpleLineItem : originalDelivery.getSimpleLineItems()) {
                            if(simpleLineItem.getStockItem().getId().equals(lineItem.getSupplierItem().getItemId())) {
                                 poReceivedQty = lineItem.getQuantityReceived().subtract(simpleLineItem.getQuantityReceivedOrZero()).subtract(simpleLineItem.getQuantityDamagedOrZero());
                                 break;
                            }
                		}
                	}
                	else {
                		poReceivedQty = lineItem.getQuantityReceived();
                	}
                    if (lineItem.getQuantityExpected().subtract(poReceivedQty).subtract(totalQtyReceived).isNegative()) {
                        isDeliveryValidForConfirm = false;
                        break;
                    }
                }
            }
        }
        return isDeliveryValidForConfirm;
    }

    protected PurchaseOrder selectPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder = null;
        if (delivery.getPurchaseOrder() != null) {
            purchaseOrder = ClientServiceFactory.getDirectDeliveryServices().readPurchaseOrder(delivery.getPurchaseOrder().getId());
        }
        return purchaseOrder;
    }

    public boolean isScannerAvailable() {
        if (isViewOnlyMode() || isDeliveryClosed()) {
            return false;
        }
        return isDeliveryEditAllowed();
    }

    public boolean isSupplierDiscrepancyOverrideAllowed() {
        return hasPermission(PermissionKey.PC_OVERRIDE_SUPPLIER_DISCREPANCY);
    }

    public DirectDeliveryInvoiceEntryType getInvoiceEntryType() {
        DirectDeliveryInvoiceEntryType invoiceEntryType = null;
        Integer value = SimConfigManager.getStoreInteger(StoreConfigKeys.DIRECT_DELIVERY_INVOICE_ENTRY, getStoreId());
        if (value != null) {
            invoiceEntryType = DirectDeliveryInvoiceEntryType.toValue(value);
        }
        return invoiceEntryType != null ? invoiceEntryType : DirectDeliveryInvoiceEntryType.ENABLED;
    }

    public boolean isInvoiceEntryUnique() {
        return getInvoiceEntryType() == DirectDeliveryInvoiceEntryType.UNIQUE;
    }

    public void updateExistingLineItem(DirectDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.isSerialNumberRequired() && !barcodeItem.getStockItem().isAgsnEnabled()) {
            updateExistingLineItemUin(wrapper, barcodeItem);
        } else {
            updateExistingLineItemQty(wrapper, barcodeItem);
        }
    }

    private void updateExistingLineItemQty(DirectDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.getQuantity().isZero()) {
            throw new UIException(CommonMessageText.NO_QUANTITY_APPLIED, RErrorSeverity.WARNING);
        }
        if (barcodeItem.isDamaged()) {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setQuantityDamaged(wrapper.getQuantityDamagedOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setQuantityDamaged(wrapper.getQuantityDamagedOrZero().add(barcodeItem.getQuantity()));
            }
        } else {
            if (wrapper.isCasesMode() && wrapper.isEachesStandardUnitOfMeasure()) {
                wrapper.setQuantityReceived(wrapper.getQuantityReceivedOrZero().add(barcodeItem.getQuantity().multiply(wrapper.getCaseSize())));
            } else {
                wrapper.setQuantityReceived(wrapper.getQuantityReceivedOrZero().add(barcodeItem.getQuantity()));
            }
        }
    }

    private void updateExistingLineItemUin(DirectDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        StockItem stockItem = barcodeItem.getStockItem();
        String itemId = barcodeItem.getId();
        String uin = barcodeItem.getUin();
        UINType uinType = stockItem.getUINType();
        String uinLabel = stockItem.getUINLabel();

        if (StringHelper.isNullOrEmpty(uin)) {
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel, RErrorSeverity.WARNING);
        }

        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(getStoreId(), itemId, uin, uinType, FunctionalArea.DIRECT_DELIVERY_RECEIPT);
        if (serialNumber == null) {
            Object[] values = new Object[3];
            values[0] = uinLabel;
            values[1] = barcodeItem.getUin();
            values[2] = barcodeItem.getId();
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }
        if (isDuplicateSerialNumber(barcodeItem.getId(), serialNumber)) {
            throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
        }

        DirectDeliveryValidateUinCommand command = ClientCommandFactory.createDirectDeliveryValidateUINCommand();
        command.setStoreId(getStoreId());
        command.setNewOnTransaction(true);
        command.setUINLabel(uinLabel);
        command.setSerialNumber(serialNumber);
        command.execute();

        serialNumber.setDamaged(barcodeItem.isDamaged());

        wrapper.addSerialNumber(serialNumber);
        //TODO NEIL: quantities may be defaulted, need to define how this should behave!
        wrapper.setQuantitiesBasedOnSerialNumbers();
    }

    private boolean isDuplicateSerialNumber(String itemId, SerialNumberValue newSerialNumber) {
        Long uinId = newSerialNumber.getUinId();
        for (DirectDeliveryLineItem lineItem : delivery.getLineItems()) {
            if (!itemId.equals(lineItem.getStockItem().getId())) {
                continue;
            }
            for (SerialNumberValue serialNumber : lineItem.getSerialNumbers()) {
                if (uinId.equals(serialNumber.getUinId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printDeliveryReport() throws Exception {
        Long storeId = getStoreId();
        boolean isRemoveDamages = getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_DAMAGES);
        boolean isRemoveOverReceive = getStoreBoolean(StoreConfigKeys.DIRECT_DELIVERY_REMOVE_OVER_RECEIVE);
        List<ReportFormat> formats = new ArrayList<ReportFormat>();
        formats.add(ReportFormat.DIRECT_DELIVERY);
		if(isRemoveDamages || isRemoveOverReceive) {
	        formats.add(ReportFormat.DIRECT_DELIVERY_DISCREPANT_ITEMS);
		}
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, formats);
        if (formatPrinters.isEmpty()) {
            return;
        }
        DirectDeliveryReportRequest reportRequest = BOFactory.createDirectDeliveryReportRequest(delivery.getId());
        SimClientPrintUtility.printReportRequest(reportRequest, formatPrinters, DirectDeliveryMessageText.DIRECT_DELIVERY_PRINTED);
    }

    public void adjustDelivery() throws Exception {
        if (SimConfigManager.getBoolean(SimConfigManager.RECORD_ADJUSTMENT_TO_DELIVERY)) {
            ClientServiceFactory.getActivityHistoryServices().writeActivityRecord(getAdjustHistoryVO());
        }
        delivery.setAllowAdjustment(true);
        delivery.markInProgress();
        viewOnlyMode = !isDeliveryEditAllowed();
    }

    public void receiveAll() throws BusinessException {
        delivery.receiveAll();
    }

    public void rejectDelivery() throws Exception {
        delivery.reject();
        receiveDelivery();
    }

    public void updateDelivery() throws Exception {
        delivery.setUserId(getUserName());
        ClientServiceFactory.getDirectDeliveryServices().updateDirectDelivery(delivery);
        try {
            releaseLock(delivery);
            releaseInvoiceLock();
        } finally {
            delivery = null;
        }
    }

    public void receiveDelivery() throws Exception {
        delivery.setUserId(getUserName());
        ClientServiceFactory.getDirectDeliveryServices().receiveDirectDelivery(delivery);
        try {
            releaseLock(delivery);
            releaseInvoiceLock();
        } finally {
            delivery = null;
        }
    }

    public void cancelDelivery() throws Exception {
        Long deliveryId = delivery.getId();
        if (deliveryId != null) {
            ClientServiceFactory.getDirectDeliveryServices().cancelDirectDelivery(delivery);
        }
        try {
            releaseLock(delivery);
            releaseInvoiceLock();
        } finally {
            delivery = null;
        }
    }

    public List<DirectDeliveryLineItemWrapper> createLineItemWrappers() throws Exception {
        Map<String, DirectDeliveryLineItem> deliveryLineItemMap = DirectDeliveryLineItemUtility.createFlatDeliveryLineItemMap(delivery);
        List<DirectDeliveryLineItemWrapper> lineItems = new ArrayList<DirectDeliveryLineItemWrapper>(deliveryLineItemMap.size());
        for (Map.Entry<String, DirectDeliveryLineItem> entry : deliveryLineItemMap.entrySet()) {
            if (!entry.getValue().isReceivable()) {
                continue;
            }
            DirectDeliveryLineItem lineItem = deliveryLineItemMap.get(entry.getKey());
            DirectDeliveryLineItemWrapper wrapper = ClientWrapperFactory.createDirectDeliveryLineItemWrapper(delivery);
            wrapper.setPurchaseOrderLineItemMap(purchaseOrderLineItemMap);
            wrapper.setLineItem(lineItem);
            wrapper.setPreferredUomConversionFactor(UomUtility.getStandardUomToTargetUom(lineItem.getStockItem(), wrapper.getPreferredUnitOfMeasure()));
            wrapper.setViewOnly(viewOnlyMode);
            lineItems.add(wrapper);
        }
        return lineItems;
    }

    public DirectDeliveryLineItemWrapper createNewLineItemWrapper() {
        DirectDeliveryLineItemWrapper wrapper = ClientWrapperFactory.createDirectDeliveryLineItemWrapper(delivery);
        wrapper.setPurchaseOrderLineItemMap(purchaseOrderLineItemMap);
        return wrapper;
    }

    public boolean obtainLock() throws Exception {
        if (isDeliveryNew()) {
            return true;
        }
        return obtainLock(ActivityLockType.DIRECT_DELIVERY, delivery.getId().toString());
    }

    public boolean checkLock() throws Exception {
    	boolean poLock =false;
    	if(isDeliveryNew())
        {
    		if(delivery.getPurchaseOrder() != null)
        	{
            	return confirmLock(ActivityLockType.PURCHASE_ORDER, delivery.getPurchaseOrder().getExternalId().toString() + getStoreId());
        	}
        	return true;
        }
        else
        {
        	if(delivery.getPurchaseOrder() != null)
        	{
            	poLock = confirmLock(ActivityLockType.PURCHASE_ORDER, delivery.getPurchaseOrder().getExternalId().toString() + getStoreId());
        	}

        	boolean deliveryLock = confirmLock(ActivityLockType.DIRECT_DELIVERY, delivery.getId().toString());
        	return delivery.getPurchaseOrder() != null ? deliveryLock && poLock : deliveryLock;
        }
    }

    public void releaseLock(DirectDelivery delivery) throws Exception {
        if (delivery.getId() != null) {
            releaseLock(ActivityLockType.DIRECT_DELIVERY, delivery.getId().toString());
        }

        if(null !=  delivery.getPurchaseOrder())
        {
        	releaseLock(ActivityLockType.PURCHASE_ORDER, delivery.getPurchaseOrder().getExternalId().toString() + getStoreId());
        }
    }


    public boolean validateInvoiceEntry(String invoiceNumber) throws Exception {
        if (delivery.getSupplier() == null || StringHelper.isNullOrEmpty(invoiceNumber) || invoiceNumber.equals(delivery.getInvoiceNumber())) {
            return true;
        }
        DirectDeliveryQueryFilter filter = BOFactory.createDirectDeliveryQueryFilter();
        filter.setSupplierId(delivery.getSupplier().getId());
        filter.setInvoiceNumber(invoiceNumber);
        return ClientServiceFactory.getDirectDeliveryServices().findDirectDeliveryVOs(filter, false).isEmpty();
    }

    public boolean obtainInvoiceLock(String invoiceNumber) throws Exception {
        String supplierId = delivery.getSupplier() != null ? delivery.getSupplier().getId() : null;
        String invoiceLockId = getInvoiceLockId(supplierId, invoiceNumber);
        if (supplierInvoiceLockId != null) {
            if (supplierInvoiceLockId.equals(invoiceLockId)) {
                return true;
            }
            releaseInvoiceLock();
        }
        if (StringHelper.isNullOrEmpty(supplierId) || StringHelper.isNullOrEmpty(invoiceNumber)) {
            return true;
        }
        if (ActivityLockUtility.createActivityLock(ActivityLockType.DIRECT_DELIVERY_INVOICE, invoiceLockId)) {
            supplierInvoiceLockId = invoiceLockId;
            return true;
        }
        return false;
    }

    public void releaseInvoiceLock() throws Exception {
        if (supplierInvoiceLockId == null) {
            return;
        }
        try {
            releaseLock(ActivityLockType.DIRECT_DELIVERY_INVOICE, supplierInvoiceLockId);
        } finally {
            supplierInvoiceLockId = null;
        }
    }

    public void storeDelivery(DirectDelivery delivery) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY, delivery);
    }

    public void storeCanceled() {
        RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_CANCELED, true);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.DIRECT_DELIVERY_VIEW_ONLY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_PURCHASE_ORDER);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
    }

    private String getInvoiceLockId(String supplierId, String invoiceNumber) {
        return supplierId + INVOICE_LOCK_DELIMITER + invoiceNumber;
    }

    private ActivityHistoryVO getAdjustHistoryVO() {
        ActivityType type = ActivityType.UNIT_RECEIVER_ADJUSTMENT_DIRECT_DELIVERY;
        Long storeId = delivery.getStore().getId();
        String userName = UniversalContext.getUserName();
        DeviceType deviceType = UniversalContext.getDeviceType();
        ActivityHistoryVO activityVO = BOFactory.createActivityHistoryVO(type, storeId, userName, deviceType);
        activityVO.doSetAttributes(delivery.getIdAsString());
        return activityVO;
    }
}
