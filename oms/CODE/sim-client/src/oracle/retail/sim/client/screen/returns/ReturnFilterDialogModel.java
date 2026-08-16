package oracle.retail.sim.client.screen.returns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnQueryStatus;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Return Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnFilterDialogModel extends SimScreenModel {

    private List<String> employeeIds;
    private ReturnQueryFilter filter;

    public void setFilter(ReturnQueryFilter filter) {
        this.filter = filter;
    }

    public ReturnQueryFilter getFilter() {
        return filter;
    }

    public ReturnQueryFilter resetFilter() {
        filter = BOFactory.createStockReturnQueryFilter();
        filter.doSetStatus(ReturnQueryStatus.ACTIVE);
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public List<String> findEmployeeIds() throws Exception {
        if (employeeIds == null) {
            employeeIds = ClientServiceFactory.getReturnServices().findEmployeeIds(getStoreId());
        }
        return employeeIds;
    }

    public List<ReturnQueryStatus> findReturnQueryStatus() {
        return SimEnumUtility.findAllReturnQueryStatus();
    }

    public Set<ReturnReason> findReturnReasons() throws Exception {
        Set<ReturnReason> returnReasons = new HashSet<>();
        returnReasons.addAll(ClientDataCacheUtility.getSupplierReturnReasons());
        returnReasons.addAll(ClientDataCacheUtility.getWarehouseReturnReasons());
        if (isFinishersEnabled()) {
            returnReasons.addAll(ClientDataCacheUtility.getFinisherReturnReasons());
        }
        return returnReasons;
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
