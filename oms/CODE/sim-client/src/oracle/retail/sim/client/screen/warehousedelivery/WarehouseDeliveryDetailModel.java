package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activityhistory.ActivityHistoryVO;
import oracle.retail.sim.common.activityhistory.ActivityType;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderUtility;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Warehouse Delivery Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryDetailModel extends SimScreenModel {
    private WarehouseDelivery delivery;
    private boolean viewOnlyMode;
    private List<FulfillmentOrder> fulfillmentOrders;

    public void loadState() {
        delivery = (WarehouseDelivery) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY);
        Boolean viewOnly = (Boolean) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY);
        viewOnlyMode = viewOnly != null && viewOnly || !isDeliveryEditAllowed();
    }

    public void loadFulfillmentOrders() throws Exception {
        if (!isFulfillmentOrderRelated()) {
            return;
        }
        fulfillmentOrders = ClientServiceFactory.getFulfillmentOrderServices().findFulfillmentOrdersForWarehouseDelivery(delivery.getId());
        if (fulfillmentOrders.isEmpty()) {
            return;
        }
        Map<String, Map<String, FulfillmentOrderLineItem>> fulfillmentOrderLineItemsMap = new HashMap<String, Map<String, FulfillmentOrderLineItem>>(fulfillmentOrders.size());
        for (FulfillmentOrder fulfillmentOrder : fulfillmentOrders) {
            fulfillmentOrderLineItemsMap.put(FulfillmentOrderUtility.buildFulfillmentOrderKey(fulfillmentOrder), FulfillmentOrderUtility.createFulfillmentOrderLineItemMap(fulfillmentOrder));
        }
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_LINE_ITEMS, fulfillmentOrderLineItemsMap);
    }

    public WarehouseDelivery getDelivery() {
        return delivery;
    }

    public boolean isDeliveryEditAllowed() {
        return hasPermission(PermissionKey.PC_EDIT_WAREHOUSE_DELIVERY);
    }

    public boolean isViewOnlyMode() {
        return viewOnlyMode;
    }

    public boolean isDeliveryNew() {
        return delivery.isNew();
    }

    public boolean isAllowAdjustment() {
        return delivery.isAllowAdjustment();
    }

    public boolean isFinisherDelivery() {
        return delivery.isFinisherDelivery();
    }

    public boolean isFulfillmentOrderRelated() {
        return delivery.isFulfillmentOrderRelated();
    }

    public boolean isDeliveryClosed() {
        return WarehouseDeliveryStatus.getClosedSet().contains(delivery.getStatus());
    }

    public boolean isDeliveryAdjustable() {
        if (viewOnlyMode) {
            return false;
        }
        if (delivery.getStatus() != WarehouseDeliveryStatus.RECEIVED) {
            return false;
        }
        Date updateDate = delivery.getUpdateDate();
        if (updateDate == null) {
            return false;
        }
        Integer daysAllowed = SimConfigManager.getInteger(SimConfigManager.DAYS_ALLOWED_FOR_ADJUSTMENTS_TO_WAREHOUSE_DELIVERYS);
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

    public void adjustDelivery() throws Exception {
        if (SimConfigManager.getBoolean(SimConfigManager.RECORD_ADJUSTMENT_TO_DELIVERY)) {
            ActivityType type = ActivityType.UNIT_RECEIVER_ADJUSTMENT_WAREHOUSE_DELIVERY;
            ActivityHistoryVO activityVO = BOFactory.createActivityHistoryVO(type, getStoreId(), getUserName(), DeviceType.PC);
            activityVO.doSetAttributes(delivery.getIdAsString());
            ClientServiceFactory.getActivityHistoryServices().writeActivityRecord(activityVO);
        }
        delivery.setAllowAdjustment(true);
        delivery.markInProgress();
        viewOnlyMode = !isDeliveryEditAllowed();
    }

    public void receiveAll() throws Exception {
        delivery.receiveAll();
    }

    public void unreceiveAll() throws Exception {
        delivery.reset();
    }

    public void receiveCartons(List<WarehouseDeliveryCartonWrapper> cartons) throws Exception {
        for (WarehouseDeliveryCartonWrapper carton : cartons) {
            carton.getCarton().receiveAll();
        }
    }

    public void unreceiveCartons(List<WarehouseDeliveryCartonWrapper> cartons) throws Exception {
        for (WarehouseDeliveryCartonWrapper carton : cartons) {
            carton.getCarton().reset();
        }
    }

    public void updateDelivery() throws Exception {
        delivery.setUserId(getUserName());
        Long deliveryId = delivery.getId();
        ClientServiceFactory.getWarehouseDeliveryServices().updateWarehouseDelivery(delivery);
        delivery = null;
        releaseLock(deliveryId);
    }

    public void receiveDelivery() throws Exception {
        delivery.setUserId(getUserName());
        Long deliveryId = delivery.getId();
        ClientServiceFactory.getWarehouseDeliveryServices().receiveWarehouseDelivery(delivery);
        delivery = null;
        releaseLock(deliveryId);
    }

    public List<WarehouseDeliveryCartonWrapper> getCartonWrappers() {
        List<WarehouseDeliveryCarton> cartons = delivery.getCartons();
        List<WarehouseDeliveryCartonWrapper> wrappers = new ArrayList<WarehouseDeliveryCartonWrapper>(cartons.size());
        for (WarehouseDeliveryCarton carton : cartons) {
            wrappers.add(ClientWrapperFactory.createWarehouseDeliveryCartonWrapper(carton));
        }
        return wrappers;
    }

    public boolean obtainLock() throws Exception {
        return obtainLock(ActivityLockType.WAREHOUSE_DELIVERY, delivery.getId());
    }

    public boolean checkLock() throws Exception {
        return confirmLock(ActivityLockType.WAREHOUSE_DELIVERY, delivery.getId());
    }

    public void releaseLock(Long deliveryId) throws Exception {
        if (deliveryId == null) {
            return;
        }
        releaseLock(ActivityLockType.WAREHOUSE_DELIVERY, deliveryId);
    }

    public boolean isLockBroken() {
        return RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_LOCK_BROKEN) != null;
    }

    public boolean isCanceled() {
        if (RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED) != null) {
            RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED);
            return true;
        }
        return false;
    }

    public void revertDelivery() {
        delivery = (WarehouseDelivery) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_WORKING_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_WORKING_DELIVERY);
    }

    public List<FulfillmentOrderVO> getFulfillmentOrderVOs() {
        List<FulfillmentOrderVO> fulfillmentOrderVOs = (List<FulfillmentOrderVO>) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS);
        if (fulfillmentOrderVOs != null) {
            return fulfillmentOrderVOs;
        }
        if (fulfillmentOrders == null || fulfillmentOrders.isEmpty()) {
            return Collections.emptyList();
        }
        return FulfillmentOrderUtility.convertToValueObjects(fulfillmentOrders);
    }

    public void storeFulfillmentOrderVOs(List<FulfillmentOrderVO> fulfillmentOrderVOs) {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS, fulfillmentOrderVOs);
    }

    public void storeCartonWrappers(List<WarehouseDeliveryCartonWrapper> cartonWrappers) {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_CARTONS, cartonWrappers);
    }

    public void storeCarton(WarehouseDeliveryCarton carton) {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_WORKING_DELIVERY, delivery.clone());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_CARTON, carton);
    }

    public void storeCanceled() {
        RepositoryManager.addStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_CANCELED, true);
    }

    public void clearState() {
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_LOCK_BROKEN);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_VIEW_ONLY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_WORKING_DELIVERY);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_CARTON);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_LINE_ITEMS);
        RepositoryManager.removeStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_CARTONS);
    }
}
