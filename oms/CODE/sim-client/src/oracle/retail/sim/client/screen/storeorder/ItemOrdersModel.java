package oracle.retail.sim.client.screen.storeorder;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Orders Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemOrdersModel extends SimScreenModel {
    private StoreOrderLineItem lineItem;

    public void loadLineItem() {
        lineItem = (StoreOrderLineItem) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    public StoreOrderLineItem getLineItem() {
        return lineItem;
    }

    public List<StoreOrderWrapper> findStoreOrders() throws Exception {
        StoreOrderQueryFilter filter = BOFactory.createStoreOrderQueryFilter();
        filter.setToLocation(getStore());
        filter.setItemId(lineItem.getOrderItem().getId());

        List<StoreOrder> storeOrders = ClientServiceFactory.getStoreOrderServices().findStoreOrders(filter);
        List<StoreOrderWrapper> wrappers = new ArrayList<>();
        for (StoreOrder storeOrder : storeOrders) {
            wrappers.add(ClientWrapperFactory.createStoreOrderWrapper(storeOrder));
        }
        return wrappers;
    }
}
