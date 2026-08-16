package oracle.retail.sim.client.screen.stockcount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.stockcount.StockCount;
import oracle.retail.sim.common.stockcount.StockCountDisplayStatus;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountQueryFilter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Stock Count List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountListModel extends SimScreenModel {
    public StockCountQueryFilter getFilter() {
        StockCountQueryFilter filter = (StockCountQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.STOCK_COUNT_FILTER);
        if (filter == null) {
            filter = BOFactory.createStockCountQueryFilter();
            filter.doSetStoreId(getStoreId());
            filter.doSetDisplayStatus(StockCountDisplayStatus.ACTIVE);
            RepositoryManager.addStateObject(SimClientStateKey.STOCK_COUNT_FILTER, filter);
        }
        return filter;
    }

    public List<StockCountWrapper> findStockCounts() throws Exception {
        List<StockCountWrapper> stockCountWrappers = new ArrayList<>();
        for (StockCount stockCount : ClientServiceFactory.getStockCountServices().findStockCounts(getFilter())) {
            if (stockCount.getTotalItemsOnCount() > 0) {
                stockCountWrappers.add(ClientWrapperFactory.createStockCountWrapper(stockCount));
            }
        }
        return stockCountWrappers;
    }

    public Integer calculateTotalItems(List<StockCountWrapper> wrappers) {
        int value = 0;
        for (StockCountWrapper wrapper : wrappers) {
            value += wrapper.getItemsLeftToCount();
        }
        return value;
    }

    public MessageText getDeleteConfirmMessage(List<StockCountWrapper> stockCounts) {
        for (StockCountWrapper stockCount : stockCounts) {
            if (stockCount.isUnitAndAmount()) {
                return StockCountMessageText.DELETE_CONFIRM_UA_COUNT;
            }
        }
        return StockCountMessageText.DELETE_CONFIRM;
    }

    public void cancelStockCount(StockCountWrapper wrapper) throws Exception {
        ClientServiceFactory.getStockCountServices().delete(wrapper.getStockCount().getId());
    }

    public ReportResponse printStockCount(StockCountWrapper stockCountWrapper, List<RetailStoreFormatPrinter> formatPrinters) throws Exception {
        return StockCountPrintUtility.printStockCount(stockCountWrapper.getId(), getStoreId(), formatPrinters);
    }

    public List<RetailStoreFormatPrinter> getFormatPrinters() throws Exception {
        return SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.STOCK_COUNT);
    }

    public void storeStockCount(StockCountWrapper wrapper) {
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_STOCK_COUNT, wrapper);
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        StockCountQueryFilter queryFilter = getFilter();
        if (queryFilter.getScheduleDate() != null) {
            descriptionMap.put("Date", LocaleManager.getShortDateFormatter().format(queryFilter.getScheduleDate()));
        }
        if (queryFilter.getDepartmentId() != null) {
            descriptionMap.put("Dept", String.valueOf(queryFilter.getDepartmentId()));
        }
        if (queryFilter.getClassId() != null) {
            descriptionMap.put("Class", String.valueOf(queryFilter.getClassId()));
        }
        if (queryFilter.getSubclassId() != null) {
            descriptionMap.put("Sub-Class", String.valueOf(queryFilter.getSubclassId()));
        }
        if (queryFilter.getProductGroupId() != null) {
            descriptionMap.put("Count Group", String.valueOf(queryFilter.getProductGroupId()));
        }
        if (queryFilter.getPhase() != null) {
            descriptionMap.put("Type", Translator.getText(queryFilter.getPhase().toString()));
        }
        if (queryFilter.getDisplayStatus() != null) {
            descriptionMap.put("Status", Translator.getText(queryFilter.getDisplayStatus().toString()));
        }
        return descriptionMap;
    }
}
