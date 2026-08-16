package oracle.retail.sim.client.screen.tranhistory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.tranhistory.TransactionHistoryQueryFilter;
import oracle.retail.sim.common.tranhistory.TransactionType;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transaction History Filter Dialog Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryFilterDialogModel extends SimScreenModel {
    private TransactionHistoryQueryFilter filter;
    private Set<String> reasonDescriptions = new HashSet<>();

    public void setFilter(TransactionHistoryQueryFilter filter) {
        this.filter = filter;
    }

    public TransactionHistoryQueryFilter getFilter() {
        return filter;
    }

    public TransactionHistoryQueryFilter resetFilter() {
        Date today = SimDateUtil.getCurrentDate();
        filter = BOFactory.createTransactionHistoryQueryFilter();
        filter.doSetStoreId(getStoreId());
        filter.doSetDateRange(today, today);
        filter.doSetSearchLimit(getDefaultSearchLimit());
        return filter;
    }

    public List<TransactionType> getTransactionTypes() {
        return Arrays.asList(TransactionType.values());
    }

    public Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_TRANSACTION_HISTORY);
    }

    public Set<String> findDescriptions() throws Exception {
        if (reasonDescriptions.isEmpty()) {
            reasonDescriptions = ClientServiceFactory.getTransactionHistoryServices().findHistoryReasonDescriptions(getStoreId());
        }
        return reasonDescriptions;
    }

    public User readUser(String username) throws Exception {
        return ClientServiceFactory.getSecurityServices().readUser(username);
    }
}
