package oracle.retail.sim.client.screen.mps;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.integration.SimMessageFamily;
import oracle.retail.sim.common.mps.MpsStagedMessageQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Staged Message Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageFilterDialogModel extends SimScreenModel {
    private MpsStagedMessageQueryFilter filter;

    public void setFilter(MpsStagedMessageQueryFilter filter) {
        this.filter = filter;
    }

    public MpsStagedMessageQueryFilter getFilter() {
        return filter;
    }

    public MpsStagedMessageQueryFilter resetFilter() {
        filter = BOFactory.createMpsStagedMessageQueryFilter();
        filter.doSetShowPending(false);
        filter.doSetShowRetry(true);
        filter.doSetSearchLimit(SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_STAGED_MESSAGE));
        return filter;
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_STAGED_MESSAGE);
    }

    public List<SimMessageFamily> getSimMessageFamilyValues() throws Exception {
        return ClientServiceFactory.getMpsServices().readSimMessageFamilies();
    }
}
