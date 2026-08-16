package oracle.retail.sim.client.screen.store;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Admin Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimManagedStoresModel extends SimScreenModel {
    public List<Store> getAllStores() throws Exception {
        return ClientServiceFactory.getStoreServices().findAllStores();
    }

    public List<Store> getSelectedStores(List<Store> availableStores) {
        List<Store> selectedStores = new ArrayList<>();
        for (Store testStore : availableStores) {
            if (testStore.getSimFlag()) {
                selectedStores.add(testStore);
            }
        }
        return selectedStores;
    }

    public void updateStores(List<Store> nonSelectedItems, List<Store> selectedItems) throws Exception {
        for (Store tempStore : nonSelectedItems) {
            tempStore.setSimFlag(Boolean.FALSE);
        }
        for (Store tempStore : selectedItems) {
            tempStore.setSimFlag(Boolean.TRUE);
        }
        List<Store> storesToUpdate = new ArrayList<>();
        storesToUpdate.addAll(nonSelectedItems);
        storesToUpdate.addAll(selectedItems);

        ClientServiceFactory.getStoreServices().updateSimStoreFlags(storesToUpdate);
    }
}
