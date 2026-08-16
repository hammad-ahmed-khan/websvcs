package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.itemprice.PriceInfo;

/********************************************************************************************************
 * Price Information Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PriceInformationModel extends SimScreenModel {

    public List<PriceInfo> getPriceHistory(ItemDetailVO itemDetailVO) throws Exception {
        return ClientDataCacheUtility.getPriceHistory(itemDetailVO.getId(), getStoreId());
    }
}
