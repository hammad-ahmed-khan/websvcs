package oracle.retail.sim.client.screen.store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Lookup Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreLookupDialogModel extends SimScreenModel {

    public List<Store> findStoresById(Long storeId) throws Exception {
        if (storeId.equals(getStoreId())) {
            return Collections.emptyList();
        }
        List<Store> stores = ClientServiceFactory.getStoreServices().findStoreIdInTransferZone(storeId, getStore().getTransferZone());
        return filterStores(stores);
    }

    public List<Store> findStoresByName(String storeName) throws Exception {
        List<Store> stores = new ArrayList<>();
        String zoneId = getSimStore().getTransferZone();
        if (StringHelper.isNullOrEmpty(zoneId)) {
            stores = ClientServiceFactory.getStoreServices().findAllStores();
        } else {
            stores = ClientServiceFactory.getStoreServices().findStoreNamesInTransferZone(storeName, getStoreId(), zoneId);
        }
        return filterStores(stores);
    }

    private List<Store> filterStores(List<Store> stores) {
        if (stores == null) {
            return Collections.emptyList();
        }
        stores.remove(getStore());
        return stores;
    }
}
