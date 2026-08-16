package oracle.retail.sim.client.screen.itemprice;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.itemprice.PromotionQueryFilter;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Promotion Lookup Tab Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PromotionLookupDialogModel extends SimScreenModel {
    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_PRICE_CHANGE);
    }

    public List<PromotionVO> findPromotions(PromotionQueryFilter filter) throws Exception {
        return ClientServiceFactory.getItemPriceServices().findPromotionVOs(filter);
    }
}
