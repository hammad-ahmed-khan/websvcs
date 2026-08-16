package oracle.retail.sim.client.screen.directdelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.directdelivery.PurchaseOrderQueryFilter;
import oracle.retail.sim.common.directdelivery.PurchaseOrderStatus;

/********************************************************************************************************
 * Purchase Order Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class PurchaseOrderFilterDialogModel extends SimScreenModel {
    private PurchaseOrderQueryFilter filter;

    public PurchaseOrderQueryFilter getFilter() {
        return filter;
    }

    public void setFilter(PurchaseOrderQueryFilter filter) {
        this.filter = filter;
    }

    public PurchaseOrderQueryFilter resetFilter() {
        filter = BOFactory.createPurchaseOrderQueryFilter();
        filter.doSetStatus(PurchaseOrderStatus.ACTIVE);
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public List<PurchaseOrderStatus> getPurchaseOrderStatusList() {
        List<PurchaseOrderStatus> statusList = new ArrayList<PurchaseOrderStatus>();
        statusList.add(PurchaseOrderStatus.ACTIVE);
        statusList.add(PurchaseOrderStatus.APPROVED);
        statusList.add(PurchaseOrderStatus.CLOSED);
        statusList.add(PurchaseOrderStatus.UNKNOWN);
        return statusList;
    }
}
