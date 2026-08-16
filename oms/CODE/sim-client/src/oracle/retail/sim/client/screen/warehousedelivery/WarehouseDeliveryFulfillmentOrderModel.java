package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderUtility;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliverySimpleLineItem;

/********************************************************************************************************
 * Warehouse Delivery Fulfillment Order Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class WarehouseDeliveryFulfillmentOrderModel extends SimScreenModel {
    public WarehouseDelivery getDelivery() {
        return (WarehouseDelivery) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_WAREHOUSE_DELIVERY);
    }

    public List<FulfillmentOrderVO> getFulfillmentOrderVOs() {
        return (List<FulfillmentOrderVO>) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDERS);
    }

    public List<WarehouseDeliveryFulfillmentOrderWrapper> getWrappers() {
        WarehouseDelivery delivery = getDelivery();
        List<FulfillmentOrderVO> fulfillmentOrderVOs = getFulfillmentOrderVOs();
        if (delivery == null || fulfillmentOrderVOs == null || fulfillmentOrderVOs.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, FulfillmentOrderVO> fulfillmentOrderVOMap = FulfillmentOrderUtility.createFulfillmentOrderVOMap(fulfillmentOrderVOs);
        List<WarehouseDeliveryFulfillmentOrderWrapper> wrappers = new ArrayList<WarehouseDeliveryFulfillmentOrderWrapper>();
        for (WarehouseDeliveryCartonWrapper cartonWrapper : getCartons()) {

            WarehouseDeliveryCarton carton = cartonWrapper.getCarton();

            //Count ordered line items for each carton
            Map<String, Integer> fulfillmentOrderLineItemCount = new HashMap<String, Integer>();
            for (WarehouseDeliverySimpleLineItem simpleLineItem : carton.getSimpleLineItems()) {
                if (!simpleLineItem.isFulfillmentOrderRelated()) {
                    continue;
                }
                String key = FulfillmentOrderUtility.buildFulfillmentOrderKey(simpleLineItem.getCustomerOrderId(), simpleLineItem.getFulfillmentOrderExternalId());
                Integer count = fulfillmentOrderLineItemCount.get(key);
                if (count == null) {
                    count = 0;
                }
                fulfillmentOrderLineItemCount.put(key, ++count);
            }
            //Build wrappers
            for (Map.Entry<String, Integer> entry : fulfillmentOrderLineItemCount.entrySet()) {
                FulfillmentOrderVO fulfillmentOrderVO = fulfillmentOrderVOMap.get(entry.getKey());
                if (fulfillmentOrderVO == null) {
                    continue;
                }
                WarehouseDeliveryFulfillmentOrderWrapper wrapper = ClientWrapperFactory.createWarehouseDeliveryFulfillmentOrderWrapper();
                wrapper.setCustomerOrderId(fulfillmentOrderVO.getCustomerOrderId());
                wrapper.setFulfillmentOrderExternalId(fulfillmentOrderVO.getExternalId());
                wrapper.setCartonExternalId(carton.getExternalId());
                wrapper.setStatus(fulfillmentOrderVO.getStatus());
                wrapper.setComments(fulfillmentOrderVO.getComments());
                wrapper.setNumberOfLineItems(entry.getValue());
                wrappers.add(wrapper);
            }
        }
        return wrappers;
    }

    public List<WarehouseDeliveryCartonWrapper> getCartons() {
        return (List<WarehouseDeliveryCartonWrapper>) RepositoryManager.getStateObject(SimClientStateKey.WAREHOUSE_DELIVERY_FULFILLMENT_ORDER_CARTONS);
    }
}
