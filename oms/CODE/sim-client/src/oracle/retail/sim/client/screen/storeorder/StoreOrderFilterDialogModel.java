package oracle.retail.sim.client.screen.storeorder;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;

/********************************************************************************************************
 * Store Order Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderFilterDialogModel extends SimScreenModel {

    private StoreOrderQueryFilter filter;

    public void setFilter(StoreOrderQueryFilter filter) {
        this.filter = filter;
    }

    public StoreOrderQueryFilter getFilter() {
        return filter;
    }

    public StoreOrderQueryFilter resetFilter() {
        filter = BOFactory.createStoreOrderQueryFilter();
        filter.setToLocation(getStore());
        filter.setStatus(StoreOrderStatus.PENDING);
        return filter;
    }

    public List<StoreOrderStatus> findAllStatus() {
        List<StoreOrderStatus> statusList = new ArrayList<>(3);
        statusList.add(StoreOrderStatus.APPROVED);
        statusList.add(StoreOrderStatus.CLOSED);
        statusList.add(StoreOrderStatus.PENDING);
        return statusList;
    }

    public String getDefaultStatus() {
        return StoreOrderStatus.PENDING.toString();
    }

    public Warehouse getWarehouse(Source source) throws Exception {
        return ClientDataCacheUtility.getAllWarehouses().get(source.getId());
    }

    public List<Warehouse> findAllWarehouses() throws Exception {
        return new ArrayList<Warehouse>(ClientDataCacheUtility.getAllWarehouses().values());
    }

    public StoreOrderStatus getStatus(String status) {
        if (status != null) {
            if (status.equals(StoreOrderStatus.PENDING.toString())) {
                return StoreOrderStatus.PENDING;
            } else if (status.equals(StoreOrderStatus.APPROVED.toString())) {
                return StoreOrderStatus.APPROVED;
            } else if (status.equals(StoreOrderStatus.CLOSED.toString())) {
                return StoreOrderStatus.CLOSED;
            }
        }
        return null;
    }
}
