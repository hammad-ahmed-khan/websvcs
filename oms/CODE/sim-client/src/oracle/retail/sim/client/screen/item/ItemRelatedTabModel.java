package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Related Item Detail Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRelatedTabModel extends SimScreenModel {
    private ItemDetailVO detailVO;

    public void setItemDetail(ItemDetailVO detailVO) throws BusinessException {
        this.detailVO = detailVO;
    }

    public List<RelatedItem> findRelatedItems() throws Exception {
        return ClientServiceFactory.getItemServices().findRelatedItems(detailVO.getParentItemId(), detailVO.getId(), getStoreId());
    }
}
