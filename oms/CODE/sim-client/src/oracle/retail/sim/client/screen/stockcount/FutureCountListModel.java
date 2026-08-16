package oracle.retail.sim.client.screen.stockcount;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.stockcount.FutureCountQueryFilter;
import oracle.retail.sim.common.stockcount.FutureCountVO;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Future Count List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FutureCountListModel extends SimScreenModel {
    public FutureCountQueryFilter getFilter() throws BusinessException {
        FutureCountQueryFilter filter = (FutureCountQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.FUTURE_COUNT_FILTER);
        if (filter == null) {
            filter = BOFactory.createFutureCountQueryFilter();
            // TODO: Future Release -  Why are we using SimDateUtil.getCurrentDateAtNoonUTC() here?
            Date tomorrow = SimDateUtil.addDays(LocaleManager.getTimeZone(), SimDateUtil.getCurrentDateAtNoonUTC(), 1);
            filter.setStoreId(getStoreId());
            filter.setFromDate(tomorrow);
            filter.setToDate(tomorrow);
            RepositoryManager.addStateObject(SimClientStateKey.FUTURE_COUNT_FILTER, filter);
        }
        return filter;
    }

    public List<FutureCountVO> findFutureStockCounts() throws Exception {
        return ClientServiceFactory.getStockCountServices().findFutureStockCounts(getFilter());
    }

    public Integer calculateTotalItems(List<FutureCountVO> wrappers) {
        int value = 0;
        for (FutureCountVO wrapper : wrappers) {
            value += wrapper.getTotalItems();
        }
        return value;
    }

    public Map<String, String> getDescriptionMap() throws BusinessException {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        FutureCountQueryFilter queryFilter = getFilter();
        if (queryFilter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(queryFilter.getFromDate()));
        }
        if (queryFilter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(queryFilter.getToDate()));
        }
        return descriptionMap;
    }

    public boolean isFutureCountFilterAvailable() {
        return RepositoryManager.getStateObject(SimClientStateKey.FUTURE_COUNT_FILTER) != null;
    }

    public void generateFutureStockCount(FutureCountVO countVO) throws BusinessException {
        StockCount stockCount = null;
        try {
            stockCount = ClientServiceFactory.getStockCountServices().generateFutureStockCount(getStoreId(), countVO.getScheduleId(), countVO.getScheduledDate(), countVO.getType());
        } catch (Throwable exception) {
            throw new BusinessException(StockCountMessageText.FUTURE_GENERATE_FAILURE);
        }
        if (stockCount == null) {
            throw new BusinessException(StockCountMessageText.FUTURE_GENERATE_NO_ITEMS);
        }
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, ClientWrapperFactory.createStockCountWrapper(stockCount));
    }
}
