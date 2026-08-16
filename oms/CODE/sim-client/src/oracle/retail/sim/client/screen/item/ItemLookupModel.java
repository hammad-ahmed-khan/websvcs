package oracle.retail.sim.client.screen.item;

import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.item.ItemVOQueryFilter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Lookup Screen Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemLookupModel extends SimScreenModel {

    public List<ItemVO> findItemVOs(ItemVOQueryFilter filter) throws Exception {
        return ClientServiceFactory.getItemServices().findItemVOs(filter, getStoreId());
    }

    public void storeItemForDetail(ItemVO item) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, item);
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_ITEM_LOOKUP);
    }

    public boolean isFinishersEnabled() {
        return SimConfigManager.getBoolean(SimConfigManager.EXTERNAL_FINISHER_ENABLED);
    }
    
    public boolean isSupplierLookUpAvailable() {
        return hasPermission(PermissionKey.PC_ACCESS_SUPPLIER_LOOKUP);
    }
}
