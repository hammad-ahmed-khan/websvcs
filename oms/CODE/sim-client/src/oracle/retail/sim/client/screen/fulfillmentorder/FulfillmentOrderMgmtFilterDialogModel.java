package oracle.retail.sim.client.screen.fulfillmentorder;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.SimEnum;
import oracle.retail.sim.common.directdelivery.DirectDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderTranType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Management Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderMgmtFilterDialogModel extends SimScreenModel {
    private FulfillmentOrderMgmtQueryFilter filter;

    public void setFilter(FulfillmentOrderMgmtQueryFilter filter) {
        this.filter = filter;
    }

    public FulfillmentOrderMgmtQueryFilter getFilter() {
        return filter;
    }

    public FulfillmentOrderMgmtQueryFilter resetFilter() {
        filter = BOFactory.createFulfillmentOrderMgmtQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetStatus(FulfillmentOrderMgmtQueryStatus.OPEN);
        return filter;
    }

    public List<FulfillmentOrderTranType> findFulfillmentOrderTranType() {
        return SimEnumUtility.findAllFulfillmentOrderTranType();
    }

    public ItemVO loadItem() {
        return (ItemVO) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MGMT_FILTER_ITEM_VO);
    }

    public List<? extends SimEnum<?>> findTranStatusList(FulfillmentOrderTranType tranType) {
        List<? extends SimEnum<?>> statusList;
        switch (tranType) {
            case DIRECT_DELIVERY:
                statusList = SimEnumUtility.findAllDirectDeliveryStatus();
                statusList.remove(DirectDeliveryStatus.ACTIVE);
                return statusList;
            case WAREHOUSE_DELIVERY:
                statusList = SimEnumUtility.findAllWarehouseDeliveryStatus();
                statusList.remove(WarehouseDeliveryStatus.ACTIVE);
                return statusList;
            case CUSTOMER_ORDER:
                statusList = SimEnumUtility.findAllCustomerOrderStatus();
                statusList.remove(FulfillmentOrderStatus.ACTIVE);
                return statusList;
            case PICK:
                statusList = SimEnumUtility.findAllFulfillmentOrderPickStatus();
                statusList.remove(FulfillmentOrderPickStatus.ACTIVE);
                return statusList;
            case CUSTOMER_ORDER_DELIVERY:
                statusList = SimEnumUtility.findAllFulfillmentOrderDeliveryStatus();
                statusList.remove(FulfillmentOrderDeliveryStatus.ACTIVE);
                return statusList;
            case TRANSFER:
                return SimEnumUtility.findAllFulfillmentOrderTransferQueryStatus();
            case REVERSE_PICK:
                return SimEnumUtility.findAllFulfillmentOrderReversePickStatus();
            default:
                return Collections.emptyList();
        }
    }

    public SimEnum<?> getTranStatus() {
        Integer tranCode = filter.getTranStatusCode();
        if (tranCode != null) {
            List<? extends SimEnum<?>> statusList = findTranStatusList(filter.getTranType());
            for (SimEnum<?> status : statusList) {
                if (tranCode.equals(status.getCode())) {
                    return status;
                }
            }
        }
        return null;
    }

    public List<FulfillmentOrderMgmtQueryStatus> findStatusList() {
        return SimEnumUtility.findFulfillmentOrderMgmtQueryStatus();
    }

    public List<ShipmentCarrier> findShipmentCarriers() throws Exception {
        return ClientServiceFactory.getShipmentServices().findAllCarriers();
    }

    public List<ShipmentCarrierService> findShipmentCarrierServices() throws Exception {
        return ClientDataCacheUtility.getAllCarrierServices();
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_CUSTOMER_ORDER_MGMT);
    }
}