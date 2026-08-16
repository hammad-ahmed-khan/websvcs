package oracle.retail.sim.client.screen.storeorder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderMessageText;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;
import oracle.retail.sim.common.storeorder.StoreOrderStatus;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.storeorder.StoreOrderServices;

/********************************************************************************************************
 * Store Orders Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreOrderListModel extends SimScreenModel {

    public List<StoreOrderWrapper> findStoreOrders() throws Exception {
        List<StoreOrderWrapper> storeOrderWrappers = new ArrayList<>();
        List<StoreOrder> storeOrders = ClientServiceFactory.getStoreOrderServices().findStoreOrders(getFilter());
        for (StoreOrder storeOrder : storeOrders) {
            storeOrderWrappers.add(ClientWrapperFactory.createStoreOrderWrapper(storeOrder));
        }
        return storeOrderWrappers;
    }

    public StoreOrderQueryFilter getFilter() {
        StoreOrderQueryFilter filter = (StoreOrderQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.STORE_ORDER_FILTER);
        if (filter == null) {
            filter = BOFactory.createStoreOrderQueryFilter();
            filter.setToLocation(getStore());
            filter.setStatus(StoreOrderStatus.PENDING);
            RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_FILTER, filter);
        }
        return filter;
    }

    public void persistStoreOrder(StoreOrderWrapper wrapper) {
        if (wrapper != null) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_ORDER_DETAIL, wrapper.getStoreOrder());
        }
    }

    public void printStoreOrders(List<StoreOrderWrapper> wrappers) throws Exception {
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.STORE_ORDER);

        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> reportRequests = new ArrayList<>();
            StoreOrderServices storeOrderServices = ClientServiceFactory.getStoreOrderServices();
            for (StoreOrderWrapper wrapper : wrappers) {
                StoreOrder storeOrder = storeOrderServices.updateStoreOrderLineItems(wrapper.getStoreOrder());
                storeOrderServices.createTempRecordsForPrint(storeOrder, getStoreId());

                reportRequests.add(BOFactory.createStoreOrderReportRequest(storeOrder.getStoreOrderNumber()));
            }
            SimClientPrintUtility.printReportRequests(reportRequests, formatPrinters, StoreOrderMessageText.STORE_ORDER_PRINTED);
        }
    }

    public void deleteStore(StoreOrderWrapper wrapper) throws Exception {
        ClientServiceFactory.getStoreOrderServices().delete(wrapper.getStoreOrder());
    }

    public Map<String, String> getDescriptionMap() {
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        StoreOrderQueryFilter filter = getFilter();
        if (filter.getNotBeforeDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getNotBeforeDate()));
        }
        if (filter.getNotAfterDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getNotAfterDate()));
        }
        if (filter.getStoreOrderNumber() != null) {
            descriptionMap.put("Order Id", String.valueOf(filter.getStoreOrderNumber()));
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getFromLocation() != null) {
            descriptionMap.put("Source", filter.getFromLocation().getId());
        }
        return descriptionMap;
    }
}
