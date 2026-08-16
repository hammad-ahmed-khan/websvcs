package oracle.retail.sim.client.screen.itemrequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.itemrequest.ItemRequest;
import oracle.retail.sim.common.itemrequest.ItemRequestMessageText;
import oracle.retail.sim.common.itemrequest.ItemRequestQueryFilter;
import oracle.retail.sim.common.itemrequest.ItemRequestStatus;
import oracle.retail.sim.common.itemrequest.ItemRequestVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Item Request List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRequestListModel extends SimScreenModel {
    public List<ItemRequestVO> findItemRequestVOs() throws Exception {
        return ClientServiceFactory.getItemRequestServices().findItemRequestVOs(getFilter());
    }

    public ItemRequestQueryFilter getFilter() {
        ItemRequestQueryFilter filter = (ItemRequestQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.ITEM_REQUEST_FILTER);
        if (filter == null) {
            filter = BOFactory.createItemRequestQueryFilter();
            filter.doSetStatus(ItemRequestStatus.PENDING);
            filter.doSetStoreId(getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.ITEM_REQUEST_FILTER, filter);
        }
        return filter;
    }

    public void storeItemRequest(Long itemRequestId) throws Exception {
        ItemRequest itemRequest = ClientServiceFactory.getItemRequestServices().readItemRequest(itemRequestId);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM_REQUEST, itemRequest);
    }

    public void printItemRequests(List<ItemRequestVO> itemRequestVos) throws Exception {
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(getStoreId(), ReportFormat.ITEM_REQUEST);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> printRequests = new ArrayList<ReportRequest>();
            for (ItemRequestVO itemRequestVO : itemRequestVos) {
                printRequests.add(BOFactory.createItemRequestReportRequest(itemRequestVO.getId()));
            }
            SimClientPrintUtility.printReportRequests(printRequests, formatPrinters, ItemRequestMessageText.REPORT_PRINTED);
        }
    }

    public void cancelRows(List<ItemRequestVO> itemRequestVOs) throws Exception {
        List<Long> lockedIds = new ArrayList<>();
        List<String> failedIds = new ArrayList<>();
        for (ItemRequestVO itemRequestVO : itemRequestVOs) {
            if (itemRequestVO.getStatus() != ItemRequestStatus.PENDING) {
                throw new BusinessException(ItemRequestMessageText.DELETE_CANCELLED);
            }
            if (obtainLock(ActivityLockType.ITEM_REQUEST, itemRequestVO.getIdAsString())) {
                lockedIds.add(itemRequestVO.getId());
            } else {
                failedIds.add(itemRequestVO.getIdAsString());
            }
        }
        try {
            ClientServiceFactory.getItemRequestServices().cancelItemRequests(lockedIds);
        } catch (BusinessException exception) {
            if (exception.getPrimaryMessageText() != ItemRequestMessageText.DELETE_FAILURE) {
                throw exception;
            }
            for (Object value : exception.getPrimaryDataList()) {
                failedIds.add(value.toString());
            }
        }
        if (failedIds.size() > 0) {
            throw new BusinessException(ItemRequestMessageText.DELETE_FAILURE, failedIds);
        }
    }

    public Map<String, String> getDescriptionMap() {
        DateFormat dateFormatter = LocaleManager.getShortDateFormatter();
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        ItemRequestQueryFilter filter = getFilter();
        if (filter.getFromRequestDate() != null) {
            descriptionMap.put("Request From Date", dateFormatter.format(filter.getFromRequestDate()));
        }
        if (filter.getToRequestDate() != null) {
            descriptionMap.put("Request To Date", dateFormatter.format(filter.getToRequestDate()));
        }
        if (filter.getFromExpirationDate() != null) {
            descriptionMap.put("Expire From Date", dateFormatter.format(filter.getFromExpirationDate()));
        }
        if (filter.getToExpirationDate() != null) {
            descriptionMap.put("Expire To Date", dateFormatter.format(filter.getToExpirationDate()));
        }
        if (filter.getItemRequestId() != null) {
            descriptionMap.put("Request ID", String.valueOf(filter.getItemRequestId()));
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item", filter.getItemId());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        if (filter.getDeliveryTimeSlot() != null) {
            descriptionMap.put("Delivery Timeslot", filter.getDeliveryTimeSlot().getDescription());
        }
        return descriptionMap;
    }
}
