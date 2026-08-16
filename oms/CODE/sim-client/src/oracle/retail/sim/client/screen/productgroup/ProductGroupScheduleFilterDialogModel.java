package oracle.retail.sim.client.screen.productgroup;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ScheduleStatus;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Product Group Schedule Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleFilterDialogModel extends SimScreenModel {

    private ProductGroupScheduleQueryFilter filter;

    public void setFilter(ProductGroupScheduleQueryFilter filter) {
        this.filter = filter;
    }

    public ProductGroupScheduleQueryFilter getFilter() {
        return filter;
    }

    public ProductGroupScheduleQueryFilter resetFilter() {
        filter = BOFactory.createProductGroupScheduleQueryFilter();
        filter.doSetStatus(ScheduleStatus.OPEN);
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public Store getStore(Long storeId) throws Exception {
        for (Store store : SimRepository.getAllowedStores()) {
            if (store.getId().equals(storeId)) {
                return store;
            }
        }
        return null;
    }

    public List<ScheduleStatus> getScheduleStatuses() {
        List<ScheduleStatus> statusList = new ArrayList<>(2);
        statusList.add(ScheduleStatus.CLOSED);
        statusList.add(ScheduleStatus.OPEN);
        return statusList;
    }

    public List<ProductGroupType> getProductGroupTypes() {
        List<ProductGroupType> types = new ArrayList<>(5);
        types.add(ProductGroupType.ITEM_REQUEST);
        types.add(ProductGroupType.STOCK_COUNT_UNIT);
        types.add(ProductGroupType.STOCK_COUNT_UNIT_AMOUNT);
        types.add(ProductGroupType.STOCK_COUNT_PROBLEM_LINE);
        types.add(ProductGroupType.STOCK_COUNT_WASTAGE);
        return types;
    }

    public List<Store> getAllowedStores() {
        return SimRepository.getAllowedStores();
    }

    public List<Long> getAllowedStoreIds() {
        List<Long> allowedStoreIds = new ArrayList<>();
        for (Store store : SimRepository.getAllowedStores()) {
            allowedStoreIds.add(store.getId());
        }
        return allowedStoreIds;
    }
}
