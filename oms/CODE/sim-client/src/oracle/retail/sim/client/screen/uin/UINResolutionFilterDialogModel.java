package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.uin.UINProblemDetailQueryFilter;

/********************************************************************************************************
 * UIN Resolution Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class UINResolutionFilterDialogModel extends SimScreenModel {
    private UINProblemDetailQueryFilter filter;

    public UINProblemDetailQueryFilter getFilter() throws BusinessException {
        if (filter == null) {
            resetFilter();
        }
        return filter;
    }

    public void setFilter(UINProblemDetailQueryFilter filter) {
        this.filter = filter;
    }

    public UINProblemDetailQueryFilter resetFilter() throws BusinessException {
        filter = BOFactory.createUINResolutionQueryFilter();
        filter.setStoreId(getStoreId());
        filter.setResolved(Boolean.FALSE);
        filter.setSearchLimit(getDefaultSearchLimit());
        return filter;
    }

    private int getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_UIN_RESOLUTION);
    }
}
