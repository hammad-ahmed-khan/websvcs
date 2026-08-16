package oracle.retail.sim.client.screen.tranhistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.tranhistory.TransactionHistoryQueryFilter;
import oracle.retail.sim.common.tranhistory.TransactionHistoryVO;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Transaction History List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TransactionHistoryListModel extends SimScreenModel {

    public TransactionHistoryQueryFilter getFilter() {
        TransactionHistoryQueryFilter filter = (TransactionHistoryQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.TRANSACTION_HISTORY_FILTER);
        if (filter == null) {
            Date today = SimDateUtil.getCurrentDate();
            filter = BOFactory.createTransactionHistoryQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetDateRange(today, today);
            filter.doSetSearchLimit(getDefaultSearchLimit());
            RepositoryManager.addStateObject(SimClientStateKey.TRANSACTION_HISTORY_FILTER, filter);
        }
        return filter;
    }

    private Integer getDefaultSearchLimit() {
        return SimConfigManager.getInteger(SimConfigManager.SEARCH_LIMIT_TRANSACTION_HISTORY);
    }

    public List<TransactionHistoryVOWrapper> findTransactionHistoryRecords() throws Exception {
        List<TransactionHistoryVOWrapper> wrappers = new ArrayList<>();
        for (TransactionHistoryVO historyVO : ClientServiceFactory.getTransactionHistoryServices().findTransactionHistoryVOs(getFilter())) {
            wrappers.add(ClientWrapperFactory.createTransactionHistoryVOWrapper(historyVO));
        }
        return wrappers;
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        TransactionHistoryQueryFilter filter = getFilter();
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getType() != null) {
            descriptionMap.put("Type", Translator.getText(filter.getType().toString()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getReasonDescription() != null) {
            descriptionMap.put("Reason", new TranslatedObjectDisplayer().getDisplayText(filter.getReasonDescription()));
        }
        if (filter.getUsername() != null) {
            descriptionMap.put("User", filter.getUsername());
        }
        descriptionMap.put("Search Limit", String.valueOf(filter.getSearchLimit()));
        return descriptionMap;
    }
}
