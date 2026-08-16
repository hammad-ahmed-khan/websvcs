package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.uin.UINAvailability;
import oracle.retail.sim.common.uin.UINDetailLookupQueryFilter;

/********************************************************************************************************
 * UIN Detail Lookup Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINDetailFilterDialogModel extends SimScreenModel {

    private UINDetailLookupQueryFilter filter;
    private String itemId;

    public void setFilter(UINDetailLookupQueryFilter filter) {
        this.filter = filter;
        itemId = filter.getItemId();
    }

    public UINDetailLookupQueryFilter getFilter() {
        return filter;
    }

    public UINDetailLookupQueryFilter resetFilter() {
        filter = BOFactory.createUINDetailLookupQueryFilter();
        filter.doSetItemId(itemId);
        filter.doSetStoreId(getStoreId());
        filter.doSetAvailability(UINAvailability.OPEN);
        filter.doSetSearchLimit(getDefaultSearchLimit());
        RepositoryManager.addStateObject(SimClientStateKey.ITEM_UIN_FILTER, filter);
        return filter;
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_CONTAINER_LOOKUP);
    }
}
