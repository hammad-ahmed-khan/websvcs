package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Pack Item Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PackComponentModel extends SimScreenModel {

    /** The components should only go down one layer. */
    public List<PackComponentWrapper> findStockItems(ItemDetailVO packItem) throws Exception {
        StockItem stockItem = packItem.getStockItem();

        // Find Item Upcs
        Set<String> itemIds = new HashSet<>();
        for (StockItem componentItem : stockItem.getComponentStockItems()) {
            itemIds.add(componentItem.getId());
        }
        Map<String, String> itemUpcMap = ClientServiceFactory.getItemServices().findPrimaryProductCodes(itemIds);

        // Make One Row For Each Component
        List<PackComponentWrapper> packComponentList = new ArrayList<>();
        for (StockItem componentItem : stockItem.getComponentStockItems()) {
            PackComponentWrapper wrapper = ClientWrapperFactory.createPackComponentWrapper(componentItem);
            wrapper.doSetComponentQuantity(calculateComponentQuantity(stockItem, componentItem));
            wrapper.doSetUPC(itemUpcMap.get(wrapper.getId()));

            packComponentList.add(wrapper);
        }
        return packComponentList;
    }
    
    private Quantity calculateComponentQuantity(StockItem stockItem, StockItem componentItem) {
        return new Quantity(stockItem.getItem().getComponentQuantity(componentItem.getItem()));
    }
}
