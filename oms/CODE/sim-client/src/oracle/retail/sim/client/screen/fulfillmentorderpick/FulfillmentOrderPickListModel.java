package oracle.retail.sim.client.screen.fulfillmentorderpick;

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
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Pick List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickListModel extends SimScreenModel {

    /**
     * Returns a List of FulfillmentOrderPickWrappers representing FulfillmentOrderPicks that fit the criteria of the current FulfillmentOrderPickQueryFilter.
     * @return A List of FulfillmentOrderPickWrappers that fit criteria in the current FulfillmentOrderPickQueryFilter.
     */
    public List<FulfillmentOrderPickWrapper> findFulfillmentOrderPickVOs() throws Exception {
        List<FulfillmentOrderPickWrapper> wrappers = new ArrayList<>();
        for (FulfillmentOrderPickVO pickVO : ClientServiceFactory.getFulfillmentOrderPickServices().findFulfillmentOrderPickVOs(getFilter())) {
            FulfillmentOrderPickWrapper wrapper = ClientWrapperFactory.createFulfillmentOrderPickWrapper(pickVO);
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    /**
     * Loads the FulfillmentOrderPick represented by the FulfillmentOrderPickWrapper and stores the Pick in memory.
     * @param wrapper A FulfillmentOrderPickWrapper representing the FulfillmentOrderPick to load.
     */
    public void storePick(FulfillmentOrderPickWrapper wrapper) throws Exception {
        Long pickId = wrapper.getId();
        FulfillmentOrderPick pick = ClientServiceFactory.getFulfillmentOrderPickServices().readFulfillmentOrderPick(pickId);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK, pick);
    }

    /**
     * Returns the current FulfillmentOrderPickQueryFilter, or a new filter with default values if a current one doesn't exist.
     * @return The current FulfillmentOrderPickQueryFilter, or a new filter with default values if a current one doesn't exist.
     */
    public FulfillmentOrderPickQueryFilter getFilter() throws Exception {
        FulfillmentOrderPickQueryFilter filter = (FulfillmentOrderPickQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER);
        if (filter == null) {
            filter = BOFactory.createFulfillmentOrderPickQueryFilter();
            filter.doSetStatus(FulfillmentOrderPickStatus.ACTIVE);
            RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_PICK_FILTER, filter);
        }
        filter.doSetStoreId(getStoreId());
        return filter;
    }

    /**
     * Cancels the FulfillmentOrderPick represented by the input FulfillmentOrderPickWrapper.
     * @param wrapper A FulfillmentOrderPickWrapper representing a FulfillmentOrderPick to cancel.
     */
    public void cancelPick(FulfillmentOrderPickWrapper wrapper) throws Exception {
        try {
            if (obtainLock(ActivityLockType.FULFILLMENT_ORDER_PICK, wrapper.getId().toString())) {
                ClientServiceFactory.getFulfillmentOrderPickServices().cancelFulfillmentOrderPick(wrapper.getId());
            }
        } finally {
            releaseLock(ActivityLockType.FULFILLMENT_ORDER_PICK, wrapper.getId().toString());
        }
    }

    /**
     * Prints the FulfillmentOrderPicks represented by the input FulfillmentOrderPickWrappers.
     * @param wrappers The wrappers representing the FulfillmentOrderPicks to print.
     */
    public void printPicks(List<FulfillmentOrderPickWrapper> wrappers) throws Exception {
        Long storeId = getStoreId();
        List<ReportFormat> formats = new ArrayList<>();
        formats.add(ReportFormat.CUSTOMER_ORDER_PICK);
        formats.add(ReportFormat.CUSTOMER_ORDER_PICK_DISCREPANCY);

        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, formats);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> requests = new ArrayList<ReportRequest>();
            for (FulfillmentOrderPickWrapper wrapper : wrappers) {
                requests.add(BOFactory.createFulfillmentOrderPickReportRequest(wrapper.getId()));
            }
            SimClientPrintUtility.printReportRequests(requests, formatPrinters, FulfillmentOrderMessageText.PICK_REPORT_PRINTED);
        }
    }

    /**
     * Returns whether the current user is allowed to create a FulfillmentOrderPick.
     * @return True if the current user is allowed to create a FulfillmentOrderPick, otherwise false.
     */
    public boolean isCreateFunctionAvailable() {
        return hasPermission(PermissionKey.PC_CREATE_CUSTOMER_ORDER_PICK);
    }

    /**
     * Returns whether the current user is allowed to delete a FulfillmentOrderPick.
     * @return True if the current user is allowed to delete a FulfillmentOrderPick, otherwise false.
     */
    public boolean isDeleteFunctionAvailable() {
        return hasPermission(PermissionKey.PC_DELETE_CUSTOMER_ORDER_PICK);
    }

    /**
     * Returns a map containing descriptions and values for the FulfillmentOrderPickQueryFilter fields.
     * @return A map containing descriptions and values for the FulfillmentOrderPickQueryFilter fields.
     */
    public Map<String, String> getDescriptionMap() throws Exception {
        FulfillmentOrderPickQueryFilter filter = getFilter();
        Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
        if (filter.getBinId() != null) {
            descriptionMap.put("BIN ID", filter.getBinId());
        }
        if (filter.getCustomerOrderId() != null) {
            descriptionMap.put("Customer Order ID", filter.getCustomerOrderId());
        }
        if (filter.getFromDate() != null) {
            descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
        }
        if (filter.getToDate() != null) {
            descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
        }
        if (filter.getFulfillmentOrderId() != null) {
            descriptionMap.put("Fulfillment Order ID", filter.getFulfillmentOrderId());
        }
        if (filter.getItemId() != null) {
            descriptionMap.put("Item ID", filter.getItemId());
        }
        if (filter.getPickId() != null) {
            descriptionMap.put("Pick ID", filter.getPickId().toString());
        }
        if (filter.getSimCustomerOrderId() != null) {
            descriptionMap.put("SIM Customer Order ID", filter.getSimCustomerOrderId().toString());
        }
        if (filter.getStatus() != null) {
            descriptionMap.put("Status", filter.getStatus().toString());
        }
        if (filter.getType() != null) {
            descriptionMap.put("Type", filter.getType().toString());
        }
        if (filter.getUserId() != null) {
            descriptionMap.put("User", filter.getUserId());
        }
        return descriptionMap;
    }
}
