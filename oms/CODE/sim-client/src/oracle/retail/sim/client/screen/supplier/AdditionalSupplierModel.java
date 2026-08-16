package oracle.retail.sim.client.screen.supplier;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Additional Supplier Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AdditionalSupplierModel extends SimScreenModel {
    private List<String> supplierIds;

    // Fetch the list of Additional Supplier IDs which is loaded into the state in the Item Detail
    // Screen on clicking the Additional Supplier button.

    public void loadItem() {
        supplierIds = (List<String>) RepositoryManager.getStateObject(SimClientStateKey.ITEM_ADDITIONAL_SUPPLIERS);
        RepositoryManager.removeStateObject(SimClientStateKey.ITEM_ADDITIONAL_SUPPLIERS);
    }

    public List<Supplier> getSuppliers() throws Exception {
        return ClientServiceFactory.getSourceServices().readSuppliers(supplierIds, getStoreId());
    }
}
