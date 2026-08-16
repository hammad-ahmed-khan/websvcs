package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Customer Order Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemCustomerOrderTabModel extends SimScreenModel {
    private ItemDetailVO detailVO;

    public void setItemDetail(ItemDetailVO itemVO) throws BusinessException {
        if (detailVO != itemVO) {
            detailVO = itemVO;
        }
    }

    public List<ItemFulfillmentOrderVO> getItemCustomerOrderVOs() throws Exception {
        return ClientServiceFactory.getFulfillmentOrderServices().findItemFulfillmentOrderVOs(detailVO.getId(), getStoreId());
    }
}
