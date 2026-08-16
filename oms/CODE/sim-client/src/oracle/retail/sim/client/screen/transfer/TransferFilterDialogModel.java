package oracle.retail.sim.client.screen.transfer;

import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimEnumUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.transfer.TransferPhase;
import oracle.retail.sim.common.transfer.TransferQueryFilter;
import oracle.retail.sim.common.transfer.TransferQueryStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transfer Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransferFilterDialogModel extends SimScreenModel {

    private TransferQueryFilter filter;
    private List<String> employeeIds;

    public void setFilter(TransferQueryFilter filter) {
        this.filter = filter;
    }

    public TransferQueryFilter getFilter() {
        return filter;
    }

    public TransferQueryFilter resetFilter() {
        filter = BOFactory.createTransferQueryFilter();
        filter.doSetStatus(TransferQueryStatus.ACTIVE);
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    public Set<BuddyStore> findBuddyStores() {
        return getSimStore().getBuddyStores();
    }

    // Employee Ids are cached until list screen is exited.

    public List<String> findEmployeeIds() throws Exception {
        if (employeeIds == null) {
            employeeIds = ClientServiceFactory.getTransferServices().findEmployeeIds(getStoreId());
        }
        return employeeIds;
    }

    public List<TransferQueryStatus> findAllTransferQueryStatus() {
        return SimEnumUtility.findAllTransferQueryStatus();
    }

    public List<TransferPhase> findAllTransferPhase() {
        return SimEnumUtility.findAllTransferPhase();
    }

    public List<ContextType> findContextTypes() throws Exception {
        return ClientDataCacheUtility.getContextTypes();
    }
}
