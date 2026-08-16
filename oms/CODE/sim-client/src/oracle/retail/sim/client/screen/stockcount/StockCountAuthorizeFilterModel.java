package oracle.retail.sim.client.screen.stockcount;

import oracle.retail.sim.client.core.SimScreenModel;

/********************************************************************************************************
 * Stock Count Authorize Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountAuthorizeFilterModel extends SimScreenModel {

    private AuthorizeQueryFilter filter;

    public void setAuthorizeQueryFilter(AuthorizeQueryFilter filter) {
        this.filter = filter;
    }

    public AuthorizeQueryFilter getFilter() {
        return filter;
    }

    public AuthorizeQueryFilter resetFilter() {
        return new AuthorizeQueryFilter();
    }
}
