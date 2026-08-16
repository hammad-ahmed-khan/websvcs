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
 * Auto-Receive Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AutoReceiveModel extends SimScreenModel {
    public SimStore getMiniStore() {
        return SimRepository.getSimStore();
    }

    public Long getMiniStoreId() {
        return SimRepository.getSimStore().getId();
    }

    public List<BuddyStore> getAllAutoStores() throws Exception {
        Long storeId = getMiniStoreId();
        List<Store> possibleStores = ClientServiceFactory.getStoreServices().findStoresInTransferZone(storeId);
        List<BuddyStore> autoStores = new ArrayList<>(possibleStores.size());
        for (Store tempStore : possibleStores) {
            if (!tempStore.getId().equals(storeId)) {
                autoStores.add(BOFactory.createBuddyStore(tempStore));
            }
        }
        return autoStores;
    }

    public Set<BuddyStore> getSelectedAutoStores() {
        return getMiniStore().getAutoReceiveStores();
    }

    public void saveBuddyStores(List<BuddyStore> autoReceiveStores) throws Exception {
        SimStore simStore = getMiniStore();
        simStore.replaceAutoReceiveStores(autoReceiveStores);
        ClientServiceFactory.getStoreServices().updateAutoReceiveStores(simStore.getId(), simStore.getAutoReceiveStores());
    }
}
