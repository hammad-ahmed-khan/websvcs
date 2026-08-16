package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Related Item Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RelatedItemModel extends SimScreenModel {
    private ItemDetailVO itemDetailVO;

    public void loadItem() {
        itemDetailVO = (ItemDetailVO) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    public ItemDetailVO getItem() {
        return itemDetailVO;
    }

    public List<RelatedItemWrapper> findRelatedItems() throws Exception {
        List<RelatedItemWrapper> wrappers = new ArrayList<>();
        List<RelatedItem> relatedItems = ClientServiceFactory.getItemServices().findRelatedItems(itemDetailVO.getParentItemId(), itemDetailVO.getId(), getStoreId());
        for (RelatedItem relatedItem : relatedItems) {
            wrappers.add(ClientWrapperFactory.createRelatedItemWrapper(relatedItem));
        }
        return wrappers;
    }

    public StockItem readStockItem(String itemId) throws Exception {
        return ClientServiceFactory.getItemServices().readStockItem(itemId, getStoreId());
    }
}
