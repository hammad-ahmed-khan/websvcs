package oracle.retail.sim.client.screen.storeorder;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.storeorder.ItemSale;
import oracle.retail.sim.common.storeorder.StoreOrderLineItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Sales Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemSalesModel extends SimScreenModel {
    private StoreOrderLineItem lineItem;

    public void loadLineItem() {
        lineItem = (StoreOrderLineItem) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    public StoreOrderLineItem getLineItem() {
        return lineItem;
    }

    public List<ItemSale> findStoreSales() throws Exception {
        return ClientServiceFactory.getStoreOrderServices().findItemSales(getStoreId(), lineItem.getOrderItem().getId());
    }
}
