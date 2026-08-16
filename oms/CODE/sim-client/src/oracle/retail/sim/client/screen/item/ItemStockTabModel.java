package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.StoreItemStockVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Stock Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemStockTabModel extends SimScreenModel {
    private ItemDetailVO detailVO;

    public boolean setItemDetail(ItemDetailVO itemVO) throws BusinessException {
        if (detailVO != itemVO) {
            detailVO = itemVO;
            return true;
        }
        return false;
    }

    public List<StoreItemStockVO> findAvailableStock() throws Exception {
        return ClientServiceFactory.getItemServices().findStoreItemStockVOs(detailVO.getId(), getStoreId());
    }
}
