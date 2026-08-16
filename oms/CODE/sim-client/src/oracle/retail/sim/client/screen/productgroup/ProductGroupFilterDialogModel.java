package oracle.retail.sim.client.screen.productgroup;

import java.util.List;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Product Group Filter Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupFilterDialogModel extends SimScreenModel {

    private ProductGroupQueryFilter filter;
    private List<Store> allowedStores;

    public void setFilter(ProductGroupQueryFilter filter) {
        this.filter = filter;
    }

    public ProductGroupQueryFilter getFilter() {
        return filter;
    }

    public ProductGroupQueryFilter resetFilter() {
        filter = BOFactory.createProductGroupQueryFilter();
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public List<ProductGroupType> getProductGroupTypes() {
        return SimEnumUtility.findProductGroupTypes();
    }

    public List<Store> getAllStores() throws Exception {
        if (allowedStores == null) {
            allowedStores = SimRepository.getAllowedStores();
        }
        return allowedStores;
    }

    public Store getStore(Long storeId) throws Exception {
        List<Store> stores = getAllStores();
        for (Store store : stores) {
            if (store.getId().equals(storeId)) {
                return store;
            }
        }
        return null;
    }

    public void clearAllowedStores() {
        allowedStores = null;
    }
}
