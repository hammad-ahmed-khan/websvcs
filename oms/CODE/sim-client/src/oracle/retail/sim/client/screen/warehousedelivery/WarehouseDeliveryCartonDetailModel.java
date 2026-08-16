package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.util.RErrorSeverity;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderUtility;
import oracle.retail.sim.common.item.BarcodeItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.lineitem.UOMMode;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINType;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCompositeLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryMessageText;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliverySimpleLineItem;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryValidateUINCommand;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Warehouse Delivery Carton Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryCartonDetailModel extends SimScreenModel {
    private WarehouseDeliveryCarton carton;
    private boolean viewOnlyMode;

    public void loadState() {
        carton = (WarehouseDeliveryCarton) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_CARTON);
        Boolean viewOnly = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY);
        viewOnlyMode = viewOnly != null && viewOnly || !isDeliveryEditAllowed();
    }

    public WarehouseDeliveryCarton getCarton() {
        return carton;
    }

    public boolean isDeliveryEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_WAREHOUSE_DELIVERY);
    }

    public boolean isViewOnlyMode() {
        return viewOnlyMode;
    }

    public boolean isDeliveryClosed() {
        return WarehouseDeliveryStatus.getClosedSet().contains(carton.getDelivery().getStatus());
    }

    public boolean isAddItemAllowed() {
        if (viewOnlyMode) {
            return false;
        }
        if (isDeliveryClosed()) {
            return false;
        }
        if (!SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_CARTON_ON_RECEIVE)) {
            return false;
        }
        return hasPermission(PermissionKey.PC_ADD_ITEM_WAREHOUSE_DELIVERY);
    }

    public boolean isDeleteItemAllowed() {
        if (viewOnlyMode) {
            return false;
        }
        if (isDeliveryClosed()) {
            return false;
        }
        if (!SimConfigManager.getBoolean(SimConfigManager.ADD_ITEM_TO_CARTON_ON_RECEIVE)) {
            return false;
        }
        return hasPermission(PermissionKey.PC_DELETE_ITEM_WAREHOUSE_DELIVERY);
    }

    public boolean isScannerAvailable() {
        return !isViewOnlyMode() && isDeliveryEditAllowed() && !isDeliveryClosed();
    }

    public void updateExistingLineItem(WarehouseDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        if (barcodeItem.isSerialNumberRequired()) {
            updateExistingLineItemUin(wrapper, barcodeItem);
        } else {
            updateExistingLineItemQty(wrapper, barcodeItem);
        }
    }

    private void updateExistingLineItemQty(WarehouseDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
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

    private void updateExistingLineItemUin(WarehouseDeliveryLineItemWrapper wrapper, BarcodeItem barcodeItem) throws Exception {
        StockItem stockItem = barcodeItem.getStockItem();
        String itemId = barcodeItem.getId();
        String uin = barcodeItem.getUin();
        UINType uinType = stockItem.getUINType();
        String uinLabel = stockItem.getUINLabel();

        if (StringHelper.isNullOrEmpty(uin)) {
            if (uinType == UINType.AGSN) {
                updateExistingLineItemQty(wrapper, barcodeItem);
                return;
            }
            throw new UIException(CommonMessageText.NO_UIN_CAPTURED, uinLabel, RErrorSeverity.WARNING);
        }

        SerialNumberValue serialNumber = ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(getStoreId(), itemId, uin, uinType, FunctionalArea.WAREHOUSE_DELIVERY_RECEIPT);
        if (serialNumber == null) {
            Object[] values = new Object[3];
            values[0] = uinLabel;
            values[1] = barcodeItem.getUin();
            values[2] = barcodeItem.getId();
            throw new BusinessException(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
        }
        if (barcodeItem.isDamaged()) {
            serialNumber.setDamaged(true);
        }
        if (isDuplicateSerialNumber(barcodeItem.getId(), serialNumber)) {
            throw new BusinessException(UINMessageText.UIN_ALREADY_ENTERED, uinLabel);
        }

        WarehouseDeliveryValidateUINCommand command = ClientCommandFactory.createWarehouseDeliveryValidateUINCommand();
        command.setStoreId(getStoreId());
        command.setSerialNumber(serialNumber);
        command.setUINLabel(uinLabel);
        command.setNewOnTransaction(true);
        command.execute();

        if (carton.getDelivery().getSource().getSourceType() == SourceType.FINISHER) {
            String receiptParentDocumentId = wrapper.getLineItem().getReceiptParentDocumentId();
            if (NumberHelper.isIdentifierNumeric(receiptParentDocumentId)) {
                if (!ClientServiceFactory.getReturnServices().isSerialNumberOnReturn(receiptParentDocumentId, SourceType.FINISHER, serialNumber.getUinId())) {
                    if (!RConfirmUtility.confirm("Confirmation", WarehouseDeliveryMessageText.UIN_VALIDATE_FOR_FINISHER, barcodeItem.getId())) {
                        throw new BusinessException(CommonMessageText.NO_UIN_CAPTURED, uinLabel);
                    }
                }
            }
        }

        wrapper.setUnitOfMeasureMode(UOMMode.STANDARD);
        wrapper.addSerialNumber(serialNumber);

        if (serialNumber.isDamaged()) {
            wrapper.setQuantityDamagedBasedOnUom(wrapper.getQuantityDamagedOrZero().add(Quantity.ONE));
        } else {
            wrapper.setQuantityReceivedBasedOnUom(wrapper.getQuantityReceivedOrZero().add(Quantity.ONE));
        }
    }

    private boolean isDuplicateSerialNumber(String itemId, SerialNumberValue newSerialNumber) {
        Long uinId = newSerialNumber.getUinId();
        for (WarehouseDeliveryLineItem lineItem : carton.getLineItems()) {
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

    public void receiveCarton() throws BusinessException {
        if (carton.getStatus() != WarehouseDeliveryStatus.IN_PROGRESS) {
            carton.setStatus(WarehouseDeliveryStatus.IN_PROGRESS);
        }
        carton.receive();
    }

    public List<WarehouseDeliveryLineItemWrapper> createLineItemWrappers() throws Exception {
        Map<String, Map<String, FulfillmentOrderLineItem>> fulfillmentOrderLineItemsMap = getFulfillmentOrderLineItemsMap();
        List<WarehouseDeliveryLineItem> lineItems = carton.getLineItems();
        List<WarehouseDeliveryLineItemWrapper> lineItemWrappers = new ArrayList<WarehouseDeliveryLineItemWrapper>(lineItems.size());
        for (WarehouseDeliveryLineItem lineItem : lineItems) {
            WarehouseDeliveryLineItemWrapper wrapper = ClientWrapperFactory.createWarehouseDeliveryLineItemWrapper(carton);
            wrapper.setLineItem(lineItem);
            String preferredUom = getPreferredUom(lineItem, fulfillmentOrderLineItemsMap);
            if (preferredUom != null) {
                wrapper.setPreferredUom(preferredUom);
                wrapper.setPreferredUomConversionFactor(UomUtility.getStandardUomToTargetUom(lineItem.getStockItem(), preferredUom));
            }
            wrapper.setViewOnly(viewOnlyMode);
            lineItemWrappers.add(wrapper);
        }
        return lineItemWrappers;
    }

    private String getPreferredUom(WarehouseDeliveryLineItem lineItem, Map<String, Map<String, FulfillmentOrderLineItem>> fulfillmentOrderLineItemsMap) {
        if (fulfillmentOrderLineItemsMap == null) {
            return null;
        }
        if (lineItem instanceof WarehouseDeliverySimpleLineItem) {
            return getPreferredUom((WarehouseDeliverySimpleLineItem) lineItem, fulfillmentOrderLineItemsMap);
        }
        if (lineItem instanceof WarehouseDeliveryCompositeLineItem) {
            return getPreferredUom((WarehouseDeliveryCompositeLineItem) lineItem, fulfillmentOrderLineItemsMap);
        }
        return null;
    }

    private String getPreferredUom(WarehouseDeliverySimpleLineItem lineItem, Map<String, Map<String, FulfillmentOrderLineItem>> fulfillmentOrderLineItemsMap) {
        Map<String, FulfillmentOrderLineItem> fulfillmentOrderLineItems =
                fulfillmentOrderLineItemsMap.get(FulfillmentOrderUtility.buildFulfillmentOrderKey(lineItem.getCustomerOrderId(), lineItem.getFulfillmentOrderExternalId()));
        if (fulfillmentOrderLineItems == null) {
            return null;
        }
        FulfillmentOrderLineItem fulfillmentOrderLineItem = fulfillmentOrderLineItems.get(lineItem.getStockItem().getId());
        if (fulfillmentOrderLineItem == null) {
            return null;
        }
        return fulfillmentOrderLineItem.getPreferredUom();
    }

    private String getPreferredUom(WarehouseDeliveryCompositeLineItem lineItem, Map<String, Map<String, FulfillmentOrderLineItem>> fulfillmentOrderLineItemsMap) {
        String preferredUom = null;
        for (WarehouseDeliverySimpleLineItem simpleLineItem : lineItem.getLineItems()) {
            String uom = getPreferredUom(simpleLineItem, fulfillmentOrderLineItemsMap);
            if (uom == null || preferredUom != null && !preferredUom.equals(uom)) {
                return null;
            }
            preferredUom = uom;
        }
        return preferredUom;
    }

    public Map<String, Map<String, FulfillmentOrderLineItem>> getFulfillmentOrderLineItemsMap() {
        return (Map<String, Map<String, FulfillmentOrderLineItem>>) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_LINE_ITEMS);
    }

    public WarehouseDeliveryLineItemWrapper createNewLineItemWrapper() {
        return ClientWrapperFactory.createWarehouseDeliveryLineItemWrapper(carton);
    }

    public boolean checkLock() throws Exception {
        return confirmLock(ActivityLockType.WAREHOUSE_DELIVERY, carton.getDelivery().getId());
    }

    public void storeLockBroken() {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_LOCK_BROKEN, true);
    }

    public void storeCanceled() {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED, true);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
    }
}
