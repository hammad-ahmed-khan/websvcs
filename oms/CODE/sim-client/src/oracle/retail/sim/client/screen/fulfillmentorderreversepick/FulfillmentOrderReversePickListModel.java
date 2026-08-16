package oracle.retail.sim.client.screen.fulfillmentorderreversepick;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickCreateCommand;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Reverse Pick List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderReversePickListModel extends SimScreenModel {

    private FulfillmentOrder fulfillmentOrder;

    public void loadFulfillmentOrder() throws Exception {
        if (fulfillmentOrder == null) {
            fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
        }
        if ((Boolean) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED) == Boolean.TRUE) {
            fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(fulfillmentOrder.getId());
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
        }
    }

    public FulfillmentOrder getFulfillmentOrder() {
        return fulfillmentOrder;
    }

    public List<FulfillmentOrderReversePickWrapper> findFulfillmentOrderReversePickVOs() throws Exception {
        List<FulfillmentOrderReversePickWrapper> wrappers = new ArrayList<>();
        for (FulfillmentOrderReversePickVO fulfillmentOrderReversePickVO : ClientServiceFactory.getFulfillmentOrderReversePickServices().findFulfillmentOrderReversePickVOs(fulfillmentOrder.getId())) {
            FulfillmentOrderReversePickWrapper wrapper = ClientWrapperFactory.createFulfillmentOrderReversePickWrapper(fulfillmentOrderReversePickVO);
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    public boolean isCreateFunctionAvailable() {
        boolean reserveOnReceipt = getStoreBoolean(StoreConfigKeys.RESERVE_CUSTOMER_ORDER_INVENTORY_UPON_RECEIVING);
        boolean autoPickOnDelivery = true;
        boolean autoPickOnReceiptDirectDelivery = SimConfigManager.getStoreBoolean(StoreConfigKeys.AUTO_PICK_ON_RECEIVE_DIRECT_DELIVERY, fulfillmentOrder.getStoreId());
        boolean autoPickOnReceiptTransfer = SimConfigManager.getStoreBoolean(StoreConfigKeys.AUTO_PICK_ON_RECEIVE_TRANSFER, fulfillmentOrder.getStoreId());
        boolean autoPickOnReceiptWharehouseDelivery = SimConfigManager.getStoreBoolean(StoreConfigKeys.AUTO_PICK_ON_RECEIVE_WAREHOUSE_DELIVERY, fulfillmentOrder.getStoreId());
        autoPickOnDelivery = autoPickOnReceiptDirectDelivery || autoPickOnReceiptTransfer || autoPickOnReceiptWharehouseDelivery;
        if (reserveOnReceipt && !autoPickOnDelivery) {
            return false;
        }
        return fulfillmentOrder.getStatus() != FulfillmentOrderStatus.COMPLETED && fulfillmentOrder.getStatus() != FulfillmentOrderStatus.CANCELED;
    }

    public boolean isDeleteFunctionAvailable() {
        return true;
    }
    
    public boolean isNotesEditable() {
        FulfillmentOrderStatus status = fulfillmentOrder.getStatus();
        return status == FulfillmentOrderStatus.NEW || status == FulfillmentOrderStatus.IN_PROGRESS;
    }

    public void storeReversePick(FulfillmentOrderReversePickWrapper wrapper) throws Exception {
        FulfillmentOrderReversePick reversePick = ClientServiceFactory.getFulfillmentOrderReversePickServices().readFulfillmentOrderReversePick(wrapper.getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_REVERSE_PICK, reversePick);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
    }

    public void cancelReversePick(FulfillmentOrderReversePickWrapper wrapper) throws Exception {
        if (obtainLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, wrapper.getId().toString())) {
            ClientServiceFactory.getFulfillmentOrderReversePickServices().cancelFulfillmentOrderReversePick(wrapper.getId());
        }
    }

    public void releaseLock(String reversePickId) {
        try {
            releaseLock(ActivityLockType.FULFILLMENT_ORDER_REVERSE_PICK, reversePickId);
        } catch (Exception e) {
            UILog.error(getClass(), e);
        }
    }

    /**
     * Prints the Fulfillment Order Reverse Picks represented by the input FulfillmentOrderReversePickWrapper.
     * @param wrappers The wrappers representing the FulfillmentOrderReversePicks to print.
     */
    public void printReversePicks(List<FulfillmentOrderReversePickWrapper> wrappers) throws Exception {
        Long storeId = getStoreId();
        List<ReportFormat> formats = new ArrayList<>();
        formats.add(ReportFormat.CUSTOMER_ORDER_REVERSE_PICK);
        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, formats);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> requests = new ArrayList<>();
            for (FulfillmentOrderReversePickWrapper wrapper : wrappers) {
                requests.add(BOFactory.createFulfillmentOrderReversePickReportRequest(wrapper.getId()));
            }
            SimClientPrintUtility.printReportRequests(requests, formatPrinters, FulfillmentOrderMessageText.REVERSE_PICK_REPORT_PRINTED);
        }
    }

    /**
     * Returns whether or not the current FulfillmentOrder is in an 'Active' status.
     * @return True if the current FulfillmentOrder is 'In Progress' or 'New' status, otherwise false.
     */
    public boolean isCustomerOrderActive() {
        return fulfillmentOrder.getStatus() == FulfillmentOrderStatus.IN_PROGRESS || fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW;
    }

    /**
     * Creates a reverse pick for the current FulfillmentOrder.
     */
    public void createReversePick() throws Exception {
        boolean nothingToReversePick = true;
        for (FulfillmentOrderLineItem ordLineItem : fulfillmentOrder.getLineItems()) {
            if (ordLineItem.getPickedQuantity().subtract(ordLineItem.getDeliveredQuantity()).isPositive()) {
                nothingToReversePick = false;
            }
        }
        if (nothingToReversePick) {
            throw new BusinessException(FulfillmentOrderMessageText.CUSTOMER_ORDER_DOES_NOT_HAVE_ANY_ITEM_TO_REVERSE_PICK);
        }
        FulfillmentOrderReversePickCreateCommand command = ClientCommandFactory.createFulfillmentOrderReversePickCreateCommand();
        command.setFulfillmentOrder(fulfillmentOrder);
        command.execute();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_REVERSE_PICK, command.getReversePick());
    }

}
