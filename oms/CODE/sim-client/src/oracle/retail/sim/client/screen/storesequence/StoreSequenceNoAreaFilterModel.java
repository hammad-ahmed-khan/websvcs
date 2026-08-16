package oracle.retail.sim.client.screen.storesequence;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.storesequence.StoreSequenceItemQueryFilter;

/********************************************************************************************************
 * Store Sequence No Area Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceNoAreaFilterModel extends SimScreenModel {

    private StoreSequenceItemQueryFilter filter;

    public void setFilter(StoreSequenceItemQueryFilter filter) {
        this.filter = filter;
    }

    public StoreSequenceItemQueryFilter getFilter() {
        return filter;
    }

    public StoreSequenceItemQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createStoreSequenceItemQueryFilter();
        filter.setSearchLimit(getDefaultSearchLimit());
        return filter;
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_STORE_SEQUENCE);
    }
}
