package oracle.retail.sim.client.screen.itemrequest;

import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.deliverytimeslot.DeliveryTimeSlot;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Request Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestFilterDialogModel extends SimScreenModel {
    private ItemRequestQueryFilter filter;
    private List<String> employeeIds;

    public void setFilter(ItemRequestQueryFilter filter) {
        this.filter = filter;
    }

    public ItemRequestQueryFilter getFilter() {
        return filter;
    }

    public ItemRequestQueryFilter resetFilter() {
        filter = BOFactory.createItemRequestQueryFilter();
        filter.doSetStatus(ItemRequestStatus.PENDING);
        filter.doSetStoreId(getStoreId());
        filter.doSetDepartmentId(null);
        return filter;
    }

    public List<String> findEmployees() throws Exception {
        if (employeeIds == null) {
            employeeIds = ClientServiceFactory.getItemRequestServices().findEmployeeIds(getStoreId());
        }
        return employeeIds;
    }

    public List<ItemRequestStatus> findItemRequestStatus() {
        return SimEnumUtility.findItemRequestStatus();
    }

    public List<DeliveryTimeSlot> findDeliveryTimeSlots() throws Exception {
        return ClientDataCacheUtility.getDeliveryTimeSlots();
    }

    public boolean displayItemRequestDeliveryTimeSlot() {
        return getStoreBoolean(StoreConfigKeys.DISPLAY_ITEM_REQUEST_DELIVERY_TIMESLOT);
    }
}
