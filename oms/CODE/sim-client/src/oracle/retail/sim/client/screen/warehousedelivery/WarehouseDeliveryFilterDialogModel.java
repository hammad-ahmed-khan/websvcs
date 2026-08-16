package oracle.retail.sim.client.screen.warehousedelivery;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryStatus;

/********************************************************************************************************
 * Warehouse Delivery Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class WarehouseDeliveryFilterDialogModel extends SimScreenModel {
    private WarehouseDeliveryQueryFilter filter;

    public WarehouseDeliveryQueryFilter getFilter() {
        return filter;
    }

    public void setFilter(WarehouseDeliveryQueryFilter filter) {
        this.filter = filter;
    }

    public WarehouseDeliveryQueryFilter resetFilter() {
        filter = BOFactory.createWarehouseDeliveryQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetStatus(WarehouseDeliveryStatus.ACTIVE);
        return filter;
    }

    public List<WarehouseDeliveryStatus> getWarehouseDeliveryStatusList() {
        List<WarehouseDeliveryStatus> statusList = new ArrayList<WarehouseDeliveryStatus>(4);
        statusList.add(WarehouseDeliveryStatus.ACTIVE);
        statusList.add(WarehouseDeliveryStatus.NEW);
        statusList.add(WarehouseDeliveryStatus.IN_PROGRESS);
        statusList.add(WarehouseDeliveryStatus.RECEIVED);
        return statusList;
    }

    public List<Warehouse> findAllWarehouses() throws Exception {
        return new ArrayList<Warehouse>(ClientDataCacheUtility.getAllWarehouses().values());
    }

    public Warehouse getWarehouse(String warehouseId) throws Exception {
        return ClientDataCacheUtility.getAllWarehouses().get(warehouseId);
    }

    public List<ContextType> findAllContextTypes() throws Exception {
        return ClientDataCacheUtility.getContextTypes();
    }

    public ContextType getContextType(String contextTypeId) throws Exception {
        for (ContextType contextType : findAllContextTypes()) {
            if (contextType.getId().equals(contextTypeId)) {
                return contextType;
            }
        }
        return null;
    }

    public boolean isFinishersEnabled() {
        return SimConfigManager.getBoolean(SimConfigManager.EXTERNAL_FINISHER_ENABLED);
    }
}
