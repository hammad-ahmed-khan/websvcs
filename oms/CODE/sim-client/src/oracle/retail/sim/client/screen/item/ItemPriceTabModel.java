package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.itemprice.PriceInfo;

/********************************************************************************************************
 * Price Information Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemPriceTabModel extends SimScreenModel {
    private ItemDetailVO detailVO;

    public boolean setItemDetail(ItemDetailVO itemVO) throws BusinessException {
        if (detailVO == null || !detailVO.equals(itemVO)) {
            detailVO = itemVO;
            return true;
        }
        return false;
    }

    public List<PriceInfo> getPriceHistory() throws Exception {
        return ClientDataCacheUtility.getPriceHistory(detailVO.getId(), getStoreId());
    }
}
