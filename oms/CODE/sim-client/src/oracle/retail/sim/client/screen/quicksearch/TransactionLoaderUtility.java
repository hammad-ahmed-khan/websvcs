package oracle.retail.sim.client.screen.quicksearch;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.dialog.ItemSelectDialog;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.activitylock.ActivityLockUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferMessageText;
import oracle.retail.sim.common.transfer.TransferStatus;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * A class to help with loading transaction information prior to jump style navigation: quick jump,
 * transaction history, etc
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionLoaderUtility {

    private TransactionLoaderUtility() {
    }

    public static void loadInventoryAdjustment(String adjustmentId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_INVENTORY_ADJUSTMENT)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(adjustmentId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, adjustmentId);
        }
        Long invAdjustmentId = Long.valueOf(adjustmentId);
        InventoryAdjustment invAdjustment = ClientServiceFactory.getInventoryAdjustmentServices().readInventoryAdjustment(invAdjustmentId);
        if (invAdjustment == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_INVENTORY_ADJUSTMENT, invAdjustment);
    }

    public static void loadCustomerOrder(String fulfillmentOrderId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(fulfillmentOrderId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, fulfillmentOrderId);
        }

        Long fulOrderId = Long.valueOf(fulfillmentOrderId);

        FulfillmentOrder fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(fulOrderId);

        if (fulfillmentOrder == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
    }

    public static void loadCustomerOrderDelivery(String deliveryId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER_DELIVERY)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(deliveryId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, deliveryId);
        }
        FulfillmentOrder fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrderByDeliveryId(new Long(deliveryId));
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
        FulfillmentOrderDelivery fulfillmentOrderDelivery = ClientServiceFactory.getFulfillmentOrderDeliveryServices().readFulfillmentOrderDelivery(new Long(deliveryId));

        if (fulfillmentOrderDelivery == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_DELIVERY, fulfillmentOrderDelivery);
    }
    
    public static void loadCustomerOrderReversePick(String reversePickId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER_REVERSE_PICK)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(reversePickId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, reversePickId);
        }
        FulfillmentOrder fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrderByReversePickId(new Long(reversePickId));
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
        FulfillmentOrderReversePick fulfillmentOrderReversePick = ClientServiceFactory.getFulfillmentOrderReversePickServices().readFulfillmentOrderReversePick(new Long(reversePickId));

        if (fulfillmentOrderReversePick == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_REVERSE_PICK, fulfillmentOrderReversePick);
    }

    public static void loadCustomerOrderPick(String pickId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER_PICK)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(pickId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, pickId);
        }
        FulfillmentOrderPick fulfillmentOrderPick = ClientServiceFactory.getFulfillmentOrderPickServices().readFulfillmentOrderPick(new Long(pickId));

        if (fulfillmentOrderPick == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK, fulfillmentOrderPick);
    }

    public static void loadReturn(String returnId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_RETURN)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(returnId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, returnId);
        }
        Return stockReturn = ClientServiceFactory.getReturnServices().readReturn(new Long(returnId));
        if (stockReturn == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_RETURN, stockReturn);
    }

    public static void loadStockCount(String stockCountId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_STOCK_COUNT)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(stockCountId)) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, stockCountId);
        }
        StockCount stockCount = ClientServiceFactory.getStockCountServices().readStockCount(Long.valueOf(stockCountId));
        if (stockCount == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, ClientWrapperFactory.createStockCountWrapper(stockCount));
    }

    public static void loadDirectDelivery(String deliveryOrPOId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_DIRECT_DELIVERY)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        DirectDelivery delivery = null;
        if (NumberHelper.isIdentifierNumeric(deliveryOrPOId)) {
            delivery = ClientServiceFactory.getDirectDeliveryServices().readDirectDelivery(Long.valueOf(deliveryOrPOId));
        }
        if (delivery == null) {
            //Find delivery by purchase order external id
            DirectDeliveryQueryFilter filter = BOFactory.createDirectDeliveryQueryFilter();

            filter.doSetStoreId(SimRepository.getStoreId());
            filter.doSetPurchaseOrderExternalId(deliveryOrPOId);

            List<DirectDeliveryVO> deliveryVOs = ClientServiceFactory.getDirectDeliveryServices().findDirectDeliveryVOs(filter, false);
            if (deliveryVOs.isEmpty()) {
                throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
            }

            delivery = ClientServiceFactory.getDirectDeliveryServices().readDirectDelivery(deliveryVOs.get(0).getId());
            if (delivery == null) {
                throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
            }
        }

        DirectDeliveryStatus status = delivery.getStatus();
        boolean isDeliveryEditAllowed = PermissionManager.hasPermission(PermissionKey.PC_EDIT_DIRECT_DELIVERY);
        boolean isClosedStatus = DirectDeliveryStatus.getClosedSet().contains(status);
        boolean isLockObtained = ActivityLockUtility.createActivityLock(ActivityLockType.DIRECT_DELIVERY, delivery.getId());

        if (isDeliveryEditAllowed || !isClosedStatus && !isLockObtained) {
            RepositoryManager.addStateObject(SimClientStateKey.DIRECT_DELIVERY_VIEW_ONLY, true);
        } else if (status == DirectDeliveryStatus.NEW || status == DirectDeliveryStatus.DEXNEX) {
            delivery.markInProgress();
        }

        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_DIRECT_DELIVERY, delivery);
    }

    public static void loadWarehouseDelivery(String warehouseOrAsnId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_WAREHOUSE_DELIVERY)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        //Find delivery by id
        WarehouseDelivery delivery = null;
        if (NumberHelper.isIdentifierNumeric(warehouseOrAsnId)) {
            delivery = ClientServiceFactory.getWarehouseDeliveryServices().readWarehouseDelivery(Long.valueOf(warehouseOrAsnId));
        }
        if (delivery == null) {
            //Find delivery by asnId
            WarehouseDeliveryQueryFilter filter = BOFactory.createWarehouseDeliveryQueryFilter();
            filter.doSetStoreId(SimRepository.getStoreId());
            filter.doSetAsnId(warehouseOrAsnId);
            List<WarehouseDeliveryVO> deliveryVOs = ClientServiceFactory.getWarehouseDeliveryServices().findWarehouseDeliveryVOs(filter);
            if (deliveryVOs.isEmpty()) {
                throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
            }

            delivery = ClientServiceFactory.getWarehouseDeliveryServices().readWarehouseDelivery(deliveryVOs.get(0).getId());
            if (delivery == null) {
                throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
            }
        }

        WarehouseDeliveryStatus status = delivery.getStatus();
        boolean isDeliveryEditAllowed = PermissionManager.hasPermission(PermissionKey.PC_EDIT_WAREHOUSE_DELIVERY);
        boolean isClosedStatus = WarehouseDeliveryStatus.getClosedSet().contains(status);
        boolean isLockObtained = ActivityLockUtility.createActivityLock(ActivityLockType.WAREHOUSE_DELIVERY, delivery.getId());

        if (!isDeliveryEditAllowed || !isClosedStatus && !isLockObtained) {
            RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY, true);
        } else if (delivery.getStatus() == WarehouseDeliveryStatus.NEW) {
            delivery.markInProgress();
        }

        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY, delivery);
    }

    public static String loadTransfer(String transferId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_TRANSFER)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        if (!NumberHelper.isIdentifierNumeric(transferId)) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        Transfer transfer = ClientServiceFactory.getTransferServices().readTransfer(Long.valueOf(transferId));
        if (transfer == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        if (transfer.getReceivingStore().getId().equals(SimRepository.getStoreId())) {
            if (transfer.getStatus() == TransferStatus.NEW && PermissionManager.hasPermission(PermissionKey.PC_EDIT_TRANSFER_REQUEST)) {
                return storeTransferForEdit(transfer, SimScreenName.TRANSFER_REQUEST_SCREEN);
            }
            if (transfer.getStatus() == TransferStatus.DISPATCHED && PermissionManager.hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
                return storeTransferForEdit(transfer, SimScreenName.TRANSFER_RECEIVE_SCREEN);
            }
            if (transfer.getStatus() == TransferStatus.RECEIVING && PermissionManager.hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
                return storeTransferForEdit(transfer, SimScreenName.TRANSFER_RECEIVE_SCREEN);
            }
        }
        if (transfer.getSendingStore().getId().equals(SimRepository.getStoreId())) {
            if (transfer.getStatus() == TransferStatus.PENDING && PermissionManager.hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
                return storeTransferForEdit(transfer, SimScreenName.TRANSFER_APPROVE_SCREEN);
            }
            if (transfer.getStatus() == TransferStatus.IN_PROGRESS && PermissionManager.hasPermission(PermissionKey.PC_EDIT_TRANSFER)) {
                return storeTransferForEdit(transfer, SimScreenName.TRANSFER_DISPATCH_SCREEN);
            }
        }
        return storeTransferForView(transfer.getId());
    }

    private static String storeTransferForEdit(Transfer transfer, String targetScreen) throws Exception {
        if (ActivityLockUtility.createActivityLock(ActivityLockType.TRANSFER, transfer.getId())) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, transfer);
            return targetScreen;
        }
        return storeTransferForView(transfer.getId());
    }

    private static String storeTransferForView(Long transferId) throws Exception {
        Transfer transfer = ClientServiceFactory.getTransferServices().readTransferForViewOnly(SimRepository.getStoreId(), transferId);
        if (transfer == null) {
            throw new BusinessException(TransferMessageText.ALREADY_CANCELLED_ERROR);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_TRANSFER, transfer);
        return SimScreenName.TRANSFER_VIEW_SCREEN;
    }

    public static void loadItem(String itemId) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_ITEM_LOOKUP)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        List<StockItem> stockItems = ClientServiceFactory.getItemServices().findStockItems(itemId, SimRepository.getStoreId());
        if (stockItems.isEmpty()) {
            throw new BusinessException(ItemMessageText.ITEM_NOT_FOUND);
        }
        if (stockItems.size() == 1) {
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, stockItems.get(0));
        } else {
            ItemSelectDialog dialog = new ItemSelectDialog();
            dialog.setStockItems(stockItems);
            dialog.setVisible(true);
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, dialog.getSelectedItem());
        }
        RepositoryManager.addStateObject(SimClientStateKey.QUICK_JUMP_ITEM, Boolean.TRUE);
    }

    public static void loadProductGroup(String identifier) throws Exception {
        if (!PermissionManager.hasPermission(PermissionKey.PC_ACCESS_PRODUCT_GROUP)) {
            throw new BusinessException(CommonMessageText.NO_ACCESS_PERMISSION);
        }
        Long productGroupId;
        try {
            productGroupId = Long.parseLong(identifier);
        } catch (Throwable ex) {
            throw new BusinessException(CommonMessageText.VALUE_NOT_VALID, identifier);
        }
        ProductGroup productGroup = ClientServiceFactory.getProductGroupServices().readProductGroup(productGroupId);
        if (productGroup == null) {
            throw new BusinessException(CommonMessageText.NO_RECORDS_FOUND);
        }
        RepositoryManager.addStateObject(SimClientStateKey.PRODUCT_GROUP_DETAIL, productGroup);
    }
}
