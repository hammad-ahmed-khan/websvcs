package oracle.retail.sim.client.screen.fulfillmentorderdelivery;

import java.util.ArrayList;
import java.util.List;

import extra.retail.sim.webservice.fulfillmentorderdelivery.client.IMEICancelDeliveryWebserviceClient;
import extra.retail.sim.webservice.fulfillmentorderdelivery.model.IMEICancelDelivery;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.activitylock.ActivityLockType;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryCreateCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order Delivery List Model
 * <p>
 * Provides business logic to the Customer Order Delivery List Screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderDeliveryListModel extends SimScreenModel {

    private FulfillmentOrder fulfillmentOrder;

    /**
     * Returns a List of CustomerOrderDeliveryWrappers representing all the deliveries for the
     * current FulfillmentOrder.
     * @return A List of CustomerOrderDeliveryWrappers representing all the deliveries for the
     * current FulfillmentOrder.
     */
    public List<FulfillmentOrderDeliveryWrapper> findFulfillmentOrderDeliveryVOs() throws Exception {

        List<FulfillmentOrderDeliveryWrapper> wrappers = new ArrayList<>();
        for (FulfillmentOrderDeliveryVO fulfillmentOrderDeliveryVO : ClientServiceFactory.getFulfillmentOrderDeliveryServices().findFulfillmentOrderDeliveryVOs(getFulfillmentOrderId())) {
            FulfillmentOrderDeliveryWrapper wrapper = ClientWrapperFactory.createFulfillmentOrderDeliveryWrapper(fulfillmentOrderDeliveryVO);
            wrappers.add(wrapper);
        }
        return wrappers;
    }

    /**
     * Returns the current FulfillmentOrder, if it was null or modified it also refreshes it in memory.
     * @return The current FulfillmentOrder.
     */
    public FulfillmentOrder getFulfillmentOrder() throws Exception {
        if (fulfillmentOrder == null) {
            fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
        }
        if ((Boolean) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED) == Boolean.TRUE) {
            fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(fulfillmentOrder.getId());
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
        }
        return fulfillmentOrder;
    }

    /**
     * Returns the ID of the current FulfillmentOrder.
     * @return The ID of the current FulfillmentOrder.
     */
    public Long getFulfillmentOrderId() throws Exception {
        return getFulfillmentOrder().getId();
    }

    /**
     * Stores the input CustomerOrderDeliveryWrapper in memory.
     * @param wrapper The CustomerOrderDeliveryWrapper to store in memory.
     */
    public void storeDelivery(FulfillmentOrderDeliveryWrapper wrapper) throws Exception {
        Long deliveryId = wrapper.getDeliveryId();
        FulfillmentOrderDelivery delivery = ClientServiceFactory.getFulfillmentOrderDeliveryServices().readFulfillmentOrderDelivery(deliveryId);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_DELIVERY, delivery);
    }

    /**
     * Returns whether or not the user can edit a delivery for the current FulfillmentOrder.
     * @return True if the user can edit a delivery for the current FulfillmentOrder.
     */
    public boolean isDeliveryReadOnly() {
        if (!isCustomerOrderActive()) {
            return true;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_PICKUP) && fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.PICKUP) {
            return true;
        }
        if (!hasPermission(PermissionKey.PC_EDIT_CUSTOMER_ORDER_DELIVERY_FOR_SHIPMENT) && fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.SHIPMENT) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not the current FulfillmentOrder is in an 'Active' status.
     * @return True if the current FulfillmentOrder is 'In Progress' or 'New' status, otherwise false.
     */
    public boolean isCustomerOrderActive() {
        if (fulfillmentOrder.getStatus() == FulfillmentOrderStatus.IN_PROGRESS || fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW) {
            return true;
        }
        return false;
    }

    /**
     * Returns whether or not the user can create a new FulfillmentOrderDelivery for the current FulfillmentOrder.
     * @return True if the user can create a new FulfillmentOrderDelivery for the current FulfillmentOrder, otherwise false.
     */
    public boolean isCreateFunctionAvailable() {
        if (fulfillmentOrder.getOrderType() != FulfillmentOrderType.WEB_ORDER) {
            return false;
        }
        if (!isCustomerOrderActive()) {
            return false;
        }
        if (hasPermission(PermissionKey.PC_CREATE_CUSTOMER_ORDER_DELIVERY_FOR_PICKUP) && fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.PICKUP) {
            return true;
        }
        if (hasPermission(PermissionKey.PC_CREATE_CUSTOMER_ORDER_DELIVERY_FOR_SHIPMENT) && fulfillmentOrder.getDeliveryType() == FulfillmentOrderDeliveryType.SHIPMENT) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the current FulfillmentOrder has been concurrently modified and if so, refreshes it in memory.
     * @return True once the operation is complete.
     */
    public boolean validateForCreate() throws Exception {
        if (!ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrderTimestamp(fulfillmentOrder.getId()).equals(fulfillmentOrder.getUpdateDate())) {
            fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(fulfillmentOrder.getId());
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
        }
        return true;
    }

    /**
     * Returns whether or not the user can delete a FulfillmentOrderDelivery for the current FulfillmentOrder.
     * @return True if the user can delete a FulfillmentOrderDelivery for the current FulfillmentOrder.
     */
    public boolean isDeleteFunctionAvailable() {
        if (!isCustomerOrderActive()) {
            return false;
        }
        if (!fulfillmentOrder.isWebOrder()) {
            return false;
        }
        if (hasPermission(PermissionKey.PC_DELETE_CUSTOMER_ORDER_DELIVERY)) {
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if the notes are currently editable.
     */
    public boolean isNotesEditable() {
        FulfillmentOrderStatus status = fulfillmentOrder.getStatus();
        return status == FulfillmentOrderStatus.NEW || status == FulfillmentOrderStatus.IN_PROGRESS;
    }

    /**
     * Creates a delivery for the current FulfillmentOrder.
     */
    public void createDelivery() throws Exception {
        FulfillmentOrderDeliveryCreateCommand command = ClientCommandFactory.createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand();
        command.setFulfillmentOrder(fulfillmentOrder);
        command.execute();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_DELIVERY, command.getDelivery());
    }

    /**
     * Checks if the current fulfillment order has been concurrently modified, if so it refreshes the data.
     * If the Fulfillment Order was in 'New' status, saves it 'In Progress' status.
     */
    public void markFulfillmentOrderAsInProgress() throws Exception {
        if (fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW) {
            ClientServiceFactory.getFulfillmentOrderServices().markFulfillmentOrderInProgress(fulfillmentOrder.getId());
            RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED, Boolean.TRUE);
        }
    }

    /**
     * Attempts to cancel the input CustomerOrderDeliveryWrappers.
     * @param wrappers The CustomerOrderDeliveryWrappers representing deliveries to cancel.
     */
    public void cancelDeliveries(List<FulfillmentOrderDeliveryWrapper> wrappers) throws Exception {
        for (FulfillmentOrderDeliveryWrapper wrapper : wrappers) {
            try {
                if (obtainLock(ActivityLockType.FULFILLMENT_ORDER_DELIVERY, wrapper.getDeliveryId().toString())) {
                    ClientServiceFactory.getFulfillmentOrderDeliveryServices().cancelFulfillmentOrderDelivery(wrapper.getDeliveryId());
                    Long fulfillmentOrderId = wrapper.getFulfillmentOrderDeliveryVO().getFulfillmentOrderId();
                    Long deliveryId = wrapper.getDeliveryId();
                    IMEICancelDeliveryWebserviceClient client=new IMEICancelDeliveryWebserviceClient();
                    IMEICancelDelivery cancelModel=new IMEICancelDelivery();
                    cancelModel.setDeliveryId(deliveryId);
                    cancelModel.setFulfillmentOrderId(fulfillmentOrderId);
                    String cancelIMEIDelivery = client.cancelIMEIDelivery(cancelModel);
                    if (!cancelIMEIDelivery.contains("\"code\":200")) {
        				throw new Exception(cancelIMEIDelivery);
        			}
                    
                }
            } finally {
                releaseLock(ActivityLockType.FULFILLMENT_ORDER_DELIVERY, wrapper.getDeliveryId().toString());
            }
        }
    }

    /**
     * Prints the Fulfillment Order Deliveries represented by the input CustomerOrderDeliveryWrappers.
     * @param wrappers The wrappers representing the FulfillmentOrderDeliveries to print.
     */
    public void printDeliveries(List<FulfillmentOrderDeliveryWrapper> wrappers) throws Exception {
        Long storeId = getStoreId();
        List<ReportFormat> formats = new ArrayList<>();
        formats.add(ReportFormat.CUSTOMER_ORDER_DELIVERY);
        formats.add(ReportFormat.CUSTOMER_ORDER_DELIVERY_BOL);

        List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, formats);
        if (formatPrinters != null && formatPrinters.size() > 0) {
            List<ReportRequest> requests = new ArrayList<ReportRequest>();
            for (FulfillmentOrderDeliveryWrapper wrapper : wrappers) {
                requests.add(BOFactory.createFulfillmentOrderDeliveryReportRequest(wrapper.getDeliveryId()));
            }
            SimClientPrintUtility.printReportRequests(requests, formatPrinters, FulfillmentOrderMessageText.DELIVERY_REPORT_PRINTED);
        }
    }

}
