package oracle.retail.sim.client.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickType;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickStatus;
import oracle.retail.sim.common.itemprice.ItemPriceStatus;
import oracle.retail.sim.common.itemprice.PriceType;
import oracle.retail.sim.common.itemrequest.ItemRequestStatus;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.security.UserStatus;
import oracle.retail.sim.common.security.UserType;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountingMethod;
import oracle.retail.sim.common.stockreturn.ReturnQueryStatus;
import oracle.retail.sim.common.tolerance.ToleranceTopic;
import oracle.retail.sim.common.transfer.TransferPhase;
import oracle.retail.sim.common.transfer.TransferQueryStatus;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/********************************************************************************************************
 * SIM Find Utility
 * <p>
 * Utility class for finding/retrieving basic status, types and other common information.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimEnumUtility {
    private SimEnumUtility() {
    }

    /****************************************************************************************************
     * Fulfillment Order Transfer Query Status
     ***************************************************************************************************/
    public static List<TransferQueryStatus> findAllFulfillmentOrderTransferQueryStatus() {
        List<TransferQueryStatus> statusList = new ArrayList<>(16);
        statusList.add(TransferQueryStatus.AWAITING_RESPONSE);
        statusList.add(TransferQueryStatus.CANCELED_INBOUND);
        statusList.add(TransferQueryStatus.CANCELED_REQUEST);
        statusList.add(TransferQueryStatus.CANCELED_TRANSFER);
        statusList.add(TransferQueryStatus.CLOSED);
        statusList.add(TransferQueryStatus.SUBMITTED);
        statusList.add(TransferQueryStatus.DISPATCHED);
        statusList.add(TransferQueryStatus.IN_PROGRESS);
        statusList.add(TransferQueryStatus.IN_TRANSIT);
        statusList.add(TransferQueryStatus.INBOUND_PICKING);
        statusList.add(TransferQueryStatus.INBOUND_REJECTED);
        statusList.add(TransferQueryStatus.NEW);
        statusList.add(TransferQueryStatus.OUTBOUND_REJECTED);
        statusList.add(TransferQueryStatus.PENDING_REQUEST);
        statusList.add(TransferQueryStatus.RECEIVED);
        statusList.add(TransferQueryStatus.RECEIVING);
        return statusList;
    }

    /****************************************************************************************************
     * Transfer Query Status
     ***************************************************************************************************/
    public static List<TransferQueryStatus> findAllTransferQueryStatus() {
        List<TransferQueryStatus> statusList = new ArrayList<>(19);
        statusList.add(TransferQueryStatus.ACTIVE);
        statusList.add(TransferQueryStatus.ACTIVE_INBOUND);
        statusList.add(TransferQueryStatus.ACTIVE_OUTBOUND);
        statusList.add(TransferQueryStatus.AWAITING_RESPONSE);
        statusList.add(TransferQueryStatus.CANCELED_INBOUND);
        statusList.add(TransferQueryStatus.CANCELED_REQUEST);
        statusList.add(TransferQueryStatus.CANCELED_TRANSFER);
        statusList.add(TransferQueryStatus.CLOSED);
        statusList.add(TransferQueryStatus.SUBMITTED);
        statusList.add(TransferQueryStatus.DISPATCHED);
        statusList.add(TransferQueryStatus.IN_PROGRESS);
        statusList.add(TransferQueryStatus.IN_TRANSIT);
        statusList.add(TransferQueryStatus.INBOUND_PICKING);
        statusList.add(TransferQueryStatus.INBOUND_REJECTED);
        statusList.add(TransferQueryStatus.NEW);
        statusList.add(TransferQueryStatus.OUTBOUND_REJECTED);
        statusList.add(TransferQueryStatus.PENDING_REQUEST);
        statusList.add(TransferQueryStatus.RECEIVED);
        statusList.add(TransferQueryStatus.RECEIVING);
        return statusList;
    }

    /****************************************************************************************************
     * Transfer Phase
     ***************************************************************************************************/
    public static List<TransferPhase> findAllTransferPhase() {
        List<TransferPhase> phaseList = new ArrayList<>(2);
        phaseList.add(TransferPhase.REQUEST);
        phaseList.add(TransferPhase.TRANSFER);
        return phaseList;
    }

    /****************************************************************************************************
     * Return Status
     ***************************************************************************************************/
    public static List<ReturnQueryStatus> findAllReturnQueryStatus() {
        List<ReturnQueryStatus> typeList = new ArrayList<>(5);
        typeList.add(ReturnQueryStatus.ACTIVE);
        typeList.add(ReturnQueryStatus.PENDING);
        typeList.add(ReturnQueryStatus.DISPATCHED);
        typeList.add(ReturnQueryStatus.CANCELED);
        typeList.add(ReturnQueryStatus.REQUESTED);
        typeList.add(ReturnQueryStatus.SUBMITTED);
        return typeList;
    }

    /****************************************************************************************************
     * Customer Order Status
     ***************************************************************************************************/
    public static List<FulfillmentOrderStatus> findAllCustomerOrderStatus() {
        List<FulfillmentOrderStatus> statusList = new ArrayList<>(5);
        statusList.add(FulfillmentOrderStatus.NEW);
        statusList.add(FulfillmentOrderStatus.IN_PROGRESS);
        statusList.add(FulfillmentOrderStatus.COMPLETED);
        statusList.add(FulfillmentOrderStatus.CANCELED);
        statusList.add(FulfillmentOrderStatus.ACTIVE);
        return statusList;
    }

    /****************************************************************************************************
     * Fulfillment Order Pick Status
     ***************************************************************************************************/
    public static List<FulfillmentOrderPickStatus> findAllFulfillmentOrderPickStatus() {
        List<FulfillmentOrderPickStatus> statusList = new ArrayList<>(5);
        statusList.add(FulfillmentOrderPickStatus.NEW);
        statusList.add(FulfillmentOrderPickStatus.IN_PROGRESS);
        statusList.add(FulfillmentOrderPickStatus.COMPLETED);
        statusList.add(FulfillmentOrderPickStatus.CANCELED);
        statusList.add(FulfillmentOrderPickStatus.ACTIVE);
        return statusList;
    }

    /****************************************************************************************************
     * Fulfillment Order Reverse Pick Status
     ***************************************************************************************************/
    public static List<FulfillmentOrderReversePickStatus> findAllFulfillmentOrderReversePickStatus() {
        List<FulfillmentOrderReversePickStatus> statusList = new ArrayList<>(5);
        statusList.add(FulfillmentOrderReversePickStatus.NEW);
        statusList.add(FulfillmentOrderReversePickStatus.IN_PROGRESS);
        statusList.add(FulfillmentOrderReversePickStatus.COMPLETED);
        statusList.add(FulfillmentOrderReversePickStatus.CANCELED);
        return statusList;
    }

    /****************************************************************************************************
     * Fulfillment Order Delivery Status
     ***************************************************************************************************/
    public static List<FulfillmentOrderDeliveryStatus> findAllFulfillmentOrderDeliveryStatus() {
        List<FulfillmentOrderDeliveryStatus> statusList = new ArrayList<>(5);
        statusList.add(FulfillmentOrderDeliveryStatus.IN_PROGRESS);
        statusList.add(FulfillmentOrderDeliveryStatus.COMPLETED);
        statusList.add(FulfillmentOrderDeliveryStatus.CANCELED);
        statusList.add(FulfillmentOrderDeliveryStatus.ACTIVE);
        statusList.add(FulfillmentOrderDeliveryStatus.SUBMITTED);
        return statusList;
    }

    /****************************************************************************************************
     * Fulfillment Order Types
     ***************************************************************************************************/
    public static List<FulfillmentOrderType> findAllFulfillmentOrderTypes() {
        List<FulfillmentOrderType> typeList = new ArrayList<>(6);
        typeList.add(FulfillmentOrderType.CUSTOMER_ORDER);
        typeList.add(FulfillmentOrderType.LAYAWAY);
        typeList.add(FulfillmentOrderType.PICKUP_AND_DELIVERY);
        typeList.add(FulfillmentOrderType.PENDING_PURCHASE);
        typeList.add(FulfillmentOrderType.SPECIAL_ORDER);
        typeList.add(FulfillmentOrderType.WEB_ORDER);
        return typeList;
    }

    /****************************************************************************************************
     * Fulfillment Order Pick Types
     ***************************************************************************************************/
    public static List<FulfillmentOrderPickType> findAllFulfillmentOrderPickTypes() {
        List<FulfillmentOrderPickType> typeList = new ArrayList<>(2);
        typeList.add(FulfillmentOrderPickType.BIN);
        typeList.add(FulfillmentOrderPickType.ORDER);
        return typeList;
    }

    /****************************************************************************************************
     * Tolerance Topic Types
     ***************************************************************************************************/
    public static List<ToleranceTopic> findAllToleranceTopics() {
        List<ToleranceTopic> topicList = new ArrayList<>(2);
        topicList.add(ToleranceTopic.ADHOC_STOCK_COUNT);
        topicList.add(ToleranceTopic.FULFILLMENT_ORDER_PICKING);
        return topicList;
    }

    /****************************************************************************************************
     * Return Source Type
     ***************************************************************************************************/
    public static List<SourceType> findReturnSourceTypes() {
        List<SourceType> typeList = new ArrayList<>(3);
        typeList.add(SourceType.SUPPLIER);
        typeList.add(SourceType.WAREHOUSE);
        typeList.add(SourceType.FINISHER);
        return typeList;
    }

    /****************************************************************************************************
     * Item Request Status
     ***************************************************************************************************/
    public static List<ItemRequestStatus> findItemRequestStatus() {
        List<ItemRequestStatus> statusList = new ArrayList<>(3);
        statusList.add(ItemRequestStatus.PENDING);
        statusList.add(ItemRequestStatus.COMPLETED);
        statusList.add(ItemRequestStatus.CANCELED);
        return statusList;
    }

    /****************************************************************************************************
     * Price Change Status
     ***************************************************************************************************/
    public static List<ItemPriceStatus> findAllItemPriceStatus() {
        List<ItemPriceStatus> statusList = new ArrayList<>(4);
        statusList.add(ItemPriceStatus.PENDING);
        statusList.add(ItemPriceStatus.TICKET_LIST);
        statusList.add(ItemPriceStatus.COMPLETED);

        return statusList;
    }

    /****************************************************************************************************
     * Price Descriptions
     ***************************************************************************************************/
    public static List<PriceType> findAllPriceTypes() {
        List<PriceType> typeList = new ArrayList<>(3);
        typeList.add(PriceType.CLEARANCE);
        typeList.add(PriceType.PROMOTIONAL);
        typeList.add(PriceType.PERMANENT);
        return typeList;
    }

    /****************************************************************************************************
     * Stock Counting Methods For Permissions
     ***************************************************************************************************/
    public static List<StockCountingMethod> findStockCountingMethodsForPermissions() {
        return Collections.singletonList(StockCountingMethod.THIRD_PARTY);
    }

    /****************************************************************************************************
     * Stock Count Status
     ***************************************************************************************************/
    public static List<StockCountDisplayStatus> findAllStockCountStatus() {
        List<StockCountDisplayStatus> statusList = new ArrayList<>(5);
        statusList.add(StockCountDisplayStatus.NEW);
        statusList.add(StockCountDisplayStatus.IN_PROGRESS);
        statusList.add(StockCountDisplayStatus.COMPLETED);
        statusList.add(StockCountDisplayStatus.ACTIVE);
        statusList.add(StockCountDisplayStatus.PROCESSING);
        return statusList;
    }

    public static List<StockCountDisplayStatus> findAllAuthorizeStatus() {
        List<StockCountDisplayStatus> statusList = new ArrayList<>(4);
        statusList.add(StockCountDisplayStatus.NEW);
        statusList.add(StockCountDisplayStatus.IN_PROGRESS);
        statusList.add(StockCountDisplayStatus.PROCESSING);
        statusList.add(StockCountDisplayStatus.AUTHORIZED);
        statusList.add(StockCountDisplayStatus.COMPLETED);
        return statusList;
    }

    /****************************************************************************************************
     * Stock Count Types
     ***************************************************************************************************/
    public static List<StockCountPhase> findAllStockCountPhases() {
        List<StockCountPhase> statusList = new ArrayList<>(3);
        statusList.add(StockCountPhase.COUNT);
        statusList.add(StockCountPhase.RECOUNT);
        statusList.add(StockCountPhase.AUTHORIZE);
        return statusList;
    }

    /****************************************************************************************************
     * Product Group Types
     ***************************************************************************************************/
    public static List<ProductGroupType> findProductGroupTypes() {
        List<ProductGroupType> typeList = new ArrayList<>(6);
        typeList.add(ProductGroupType.ITEM_REQUEST);
        typeList.add(ProductGroupType.SHELF_REPLENISHMENT);
        typeList.add(ProductGroupType.STOCK_COUNT_UNIT);
        typeList.add(ProductGroupType.STOCK_COUNT_UNIT_AMOUNT);
        typeList.add(ProductGroupType.STOCK_COUNT_WASTAGE);
        typeList.add(ProductGroupType.STOCK_COUNT_PROBLEM_LINE);
        return typeList;
    }

    /****************************************************************************************************
     * Product Group Schedule Types
     ***************************************************************************************************/
    public static List<ProductGroupType> findProductGroupScheduleTypes() {
        List<ProductGroupType> typeList = new ArrayList<>(6);
        typeList.add(ProductGroupType.STOCK_COUNT_UNIT);
        typeList.add(ProductGroupType.STOCK_COUNT_UNIT_AMOUNT);
        typeList.add(ProductGroupType.STOCK_COUNT_PROBLEM_LINE);
        typeList.add(ProductGroupType.STOCK_COUNT_WASTAGE);
        typeList.add(ProductGroupType.ITEM_REQUEST);
        return typeList;
    }

    /****************************************************************************************************
     * Days Of Week
     ***************************************************************************************************/
    public static List<Integer> findDaysOfWeek() {
        List<Integer> dayList = new ArrayList<>(7);
        dayList.add(Calendar.SUNDAY);
        dayList.add(Calendar.MONDAY);
        dayList.add(Calendar.TUESDAY);
        dayList.add(Calendar.WEDNESDAY);
        dayList.add(Calendar.THURSDAY);
        dayList.add(Calendar.FRIDAY);
        dayList.add(Calendar.SATURDAY);
        return dayList;
    }

    /****************************************************************************************************
     * Months
     ***************************************************************************************************/
    public static List<Integer> findMonths() {
        List<Integer> monthList = new ArrayList<>(12);
        monthList.add(Calendar.JANUARY);
        monthList.add(Calendar.FEBRUARY);
        monthList.add(Calendar.MARCH);
        monthList.add(Calendar.APRIL);
        monthList.add(Calendar.MAY);
        monthList.add(Calendar.JUNE);
        monthList.add(Calendar.JULY);
        monthList.add(Calendar.AUGUST);
        monthList.add(Calendar.SEPTEMBER);
        monthList.add(Calendar.OCTOBER);
        monthList.add(Calendar.NOVEMBER);
        monthList.add(Calendar.DECEMBER);
        return monthList;
    }

    /****************************************************************************************************
     * Device Types
     ***************************************************************************************************/
    public static List<DeviceType> findDeviceTypes() {
        List<DeviceType> deviceTypes = new ArrayList<>(3);
        deviceTypes.add(DeviceType.PC);
        deviceTypes.add(DeviceType.HH);
        deviceTypes.add(DeviceType.SERVER);
        return deviceTypes;
    }

    /****************************************************************************************************
     * Data Permissions
     ***************************************************************************************************/
    public static List<String> findDataPermissionNames() {
        List<String> dataPermissionNames = new ArrayList<>(7);
        dataPermissionNames.add(PermissionKey.DATA_ITEM_REQUEST_DELIVERY_TIMESLOT);
        dataPermissionNames.add(PermissionKey.DATA_INV_ADJUSTMENT_REASON);
        dataPermissionNames.add(PermissionKey.DATA_RETURN_REASON_CODE);
        dataPermissionNames.add(PermissionKey.DATA_PRODUCT_GROUP_TYPE);
        dataPermissionNames.add(PermissionKey.DATA_RETURN_SOURCE);
        dataPermissionNames.add(PermissionKey.DATA_COUNTING_METHOD);
        dataPermissionNames.add(PermissionKey.DATA_ROLE_TYPE);
        dataPermissionNames.add(PermissionKey.DATA_USER_TYPE);
        return dataPermissionNames;
    }

    /****************************************************************************************************
     * User Types
     ***************************************************************************************************/
    public static List<UserType> findUserTypes() {
        List<UserType> userTypes = new ArrayList<>(3);
        userTypes.add(UserType.SUPER_USER);
        userTypes.add(UserType.STORE_USER);
        userTypes.add(UserType.TEMPORARY_USER);
        return userTypes;
    }

    public static List<UserType> findAvailableUserTypes() {
        List<UserType> userTypes = new ArrayList<>();
        for (UserType userType : findUserTypes()) {
            if (PermissionManager.hasDataPermission(PermissionKey.DATA_USER_TYPE, userType.getCode())) {
                userTypes.add(userType);
            }
        }
        return userTypes;
    }

    /****************************************************************************************************
     * User Status
     ***************************************************************************************************/
    public static List<UserStatus> findUserStatuses() {
        List<UserStatus> userStatuses = new ArrayList<>(4);
        userStatuses.add(UserStatus.ACTIVE);
        userStatuses.add(UserStatus.INACTIVE);
        userStatuses.add(UserStatus.DELETE);
        userStatuses.add(UserStatus.LOCKED);
        return userStatuses;
    }

    /****************************************************************************************************
     * Fulfillment Order Tran Types
     ***************************************************************************************************/
    public static List<FulfillmentOrderTranType> findAllFulfillmentOrderTranType() {
        List<FulfillmentOrderTranType> fulfillmentOrderTranTypes = new ArrayList<>();
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.DIRECT_DELIVERY);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.TRANSFER);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.WAREHOUSE_DELIVERY);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.CUSTOMER_ORDER);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.PICK);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.REVERSE_PICK);
        fulfillmentOrderTranTypes.add(FulfillmentOrderTranType.CUSTOMER_ORDER_DELIVERY);

        return fulfillmentOrderTranTypes;
    }

    /****************************************************************************************************
     * Fulfillment Order Management Status Types
     ***************************************************************************************************/
    public static List<FulfillmentOrderMgmtQueryStatus> findFulfillmentOrderMgmtQueryStatus() {
        List<FulfillmentOrderMgmtQueryStatus> fulfillmentOrderMgmtQueryStatus = new ArrayList<>(2);
        fulfillmentOrderMgmtQueryStatus.add(FulfillmentOrderMgmtQueryStatus.OPEN);
        fulfillmentOrderMgmtQueryStatus.add(FulfillmentOrderMgmtQueryStatus.CLOSED);
        return fulfillmentOrderMgmtQueryStatus;
    }

    /****************************************************************************************************
     * Direct Delivery Status
     ***************************************************************************************************/
    public static List<DirectDeliveryStatus> findAllDirectDeliveryStatus() {
        List<DirectDeliveryStatus> directDeliveryStatus = new ArrayList<>(8);
        directDeliveryStatus.add(DirectDeliveryStatus.ACTIVE);
        directDeliveryStatus.add(DirectDeliveryStatus.CANCELED);
        directDeliveryStatus.add(DirectDeliveryStatus.DAMAGED);
        directDeliveryStatus.add(DirectDeliveryStatus.DEXNEX);
        directDeliveryStatus.add(DirectDeliveryStatus.IN_PROGRESS);
        directDeliveryStatus.add(DirectDeliveryStatus.NEW);
        directDeliveryStatus.add(DirectDeliveryStatus.RECEIVED);
        directDeliveryStatus.add(DirectDeliveryStatus.REJECTED);

        return directDeliveryStatus;
    }

    /****************************************************************************************************
     * Warehouse Delivery Status
     ***************************************************************************************************/
    public static List<WarehouseDeliveryStatus> findAllWarehouseDeliveryStatus() {
        List<WarehouseDeliveryStatus> warehouseDeliveryStatus = new ArrayList<>(7);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.ACTIVE);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.IN_PROGRESS);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.CANCELED);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.DAMAGED);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.MISSING);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.NEW);
        warehouseDeliveryStatus.add(WarehouseDeliveryStatus.RECEIVED);

        return warehouseDeliveryStatus;
    }
}
