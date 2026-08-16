package oracle.retail.sim.client.screen.item;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Customer Order Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemCustomerOrderModel extends SimScreenModel {
    private List<ItemFulfillmentOrderVO> itemOrderVOs;

    public void loadCustomerOrders() {
        itemOrderVOs = (List<ItemFulfillmentOrderVO>) RepositoryManager.getStateObject(SimClientStateKey.ITEM_CUSTOMER_ORDERS);
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_CUSTOMER_ORDERS);
    }

    public List<ItemFulfillmentOrderVO> getItemOrderVOs() {
        if (itemOrderVOs != null) {
            return itemOrderVOs;
        }
        return Collections.emptyList();
    }

    public void storeCustomerOrder(ItemFulfillmentOrderVO itemOrderVO) throws Exception {
        if (StringHelper.isNullOrEmpty(itemOrderVO.getCustomerOrderId())) {
            return;
        }
        FulfillmentOrder order = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(itemOrderVO.getFulfillmentOrderId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, order);
    }

    public void storeOrigin() {
        RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_DETAIL_ORIGIN, SimScreenName.ITEM_CUSTOMER_ORDER_SCREEN);
    }

    public void removeOrigin() {
        RepositoryManager.removeStateObject(SimClientStateKey.CUSTOMER_ORDER_DETAIL_ORIGIN);
    }
}
