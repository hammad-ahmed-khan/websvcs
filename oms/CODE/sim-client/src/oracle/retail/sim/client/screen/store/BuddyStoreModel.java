package oracle.retail.sim.client.screen.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.SimStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Buddy Store Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BuddyStoreModel extends SimScreenModel {

    public Long getSimStoreId() {
        return SimRepository.getSimStore().getId();
    }

    public List<BuddyStore> getAllBuddyStores() throws Exception {
        List<Store> allStores = ClientServiceFactory.getStoreServices().findStoresInTransferZone(getSimStoreId());
        List<BuddyStore> buddyStores = new ArrayList<>(allStores.size());
        for (Store store : allStores) {
            buddyStores.add(BOFactory.createBuddyStore(store));
        }
        return buddyStores;
    }

    public Set<BuddyStore> getSelectedBuddyStores() {
        return getSimStore().getBuddyStores();
    }

    public void saveBuddyStores(List<BuddyStore> buddyStores) throws Exception {
        SimStore simStore = getSimStore();
        simStore.replaceBuddyStores(buddyStores);
        ClientServiceFactory.getStoreServices().updateBuddyStores(simStore.getId(), simStore.getBuddyStores());
    }
}
