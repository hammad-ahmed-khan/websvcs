// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

import java.util.Iterator;
import java.util.Map;
import java.util.List;
import com.logicinfo.extra.util.CarrierMapping;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class InActiveCancellationImpl
{
    public static void main(final String[] args) throws SQLException, Exception {
        final Logger LOGGER = Logger.getLogger((Class)InActiveCancellationImpl.class);
        TrackingOrderList trackingOrderListObj = null;
        List<InActiveTrackingOrders> inActiveTrackingSimOrdersList = new ArrayList<InActiveTrackingOrders>();
        List<InActiveTrackingOrders> inActiveTrackingWmsOrdersList = new ArrayList<InActiveTrackingOrders>();
        Map<Integer, InActiveTrackingOrdersShipmentCarrier> shipmentCarrierMap = null;
        CarrierMapping cmap=new CarrierMapping();
        try {
            trackingOrderListObj = new TrackingOrderList();
            System.out.println("Inside Inactive Cancellation");
        }
        catch (Exception e) {
            LOGGER.error((Object)("InActiveCancellationImpl::Exception in main method" + e));
        }
        if (trackingOrderListObj != null) {
            try {
                inActiveTrackingSimOrdersList = (List<InActiveTrackingOrders>)trackingOrderListObj.getInactiveOrderListFromSim();
                inActiveTrackingWmsOrdersList = (List<InActiveTrackingOrders>)trackingOrderListObj.getInactiveOrderListFromWms();
                System.out.println("Inside Inactive Cancellation try");
            }
            catch (Exception ex) {
                LOGGER.error((Object)("InActiveCancellationImpl::Exception in main method" + ex));
            }
        }
        if (inActiveTrackingSimOrdersList.size() >= 0) {
            try {
                System.out.println("Inside Inactive Cancellation > 0");
                shipmentCarrierMap = (Map<Integer, InActiveTrackingOrdersShipmentCarrier>)trackingOrderListObj.getShipmentCarrierId();
            }
            catch (SQLException et) {
                LOGGER.error((Object)("InActiveCancellationImpl::Exception in main method at inActiveTrackingSimOrdersList size checking" + et));
            }
        }
        for (final InActiveTrackingOrders inactiveSimOrderObj : inActiveTrackingSimOrdersList) {
            try {
                System.out.println("Inside Inactive Cancellation - FOR" + inactiveSimOrderObj.getCustomerOrderNo());
                LOGGER.info((Object)("Customer order no : " + inactiveSimOrderObj.getCustomerOrderNo() + "Tracking id : " + inactiveSimOrderObj.getTrackingId() + "Carrier name : " + shipmentCarrierMap.get(inactiveSimOrderObj.getCarrierId()).getDescription()));
                System.out.println("Inside Inactive Cancellation - FOR1");
                System.out.println("Customer order no : " + inactiveSimOrderObj.getCustomerOrderNo() + "Tracking id : " + inactiveSimOrderObj.getTrackingId() + "Carrier name : " + shipmentCarrierMap.get(inactiveSimOrderObj.getCarrierId()).getDescription());
                System.out.println("Inside Inactive Cancellation - FOR2");
                String carrierDescription = shipmentCarrierMap.get(inactiveSimOrderObj.getCarrierId()).getCode();
                System.out.println("Inside Inactive Cancellation - FOR3");
                carrierDescription = cmap.getCarrierDescription(carrierDescription);
                if (carrierDescription == null) {
                    carrierDescription = CarrierMapping.getcamelCaseCarrierName(shipmentCarrierMap.get(inactiveSimOrderObj.getCarrierId()).getDescription());
                }
                final AwbCancellationRequest awbcancellationRequestObj = new AwbCancellationRequest(inactiveSimOrderObj.getCustomerOrderNo(), inactiveSimOrderObj.getTrackingId(), carrierDescription);
                ShipmentCancellationClient.callInactiveOrdrdelete(awbcancellationRequestObj, "SIM");
            }
            catch (Exception e2) {
                System.out.println("Inside Inactive Cancellation - Exception");
                LOGGER.error((Object)("InActiveCancellationImpl::Exception in main method" + e2));
            }
        }
        for (final InActiveTrackingOrders inactiveWmsOrderObj : inActiveTrackingWmsOrdersList) {
            final AwbCancellationRequest awbcancellationRequestObj2 = new AwbCancellationRequest(inactiveWmsOrderObj.getCustomerOrderNo(), inactiveWmsOrderObj.getTrackingId(), cmap.getCarrierDescription(inactiveWmsOrderObj.getCarrierCode()));
            ShipmentCancellationClient.callInactiveOrdrdelete(awbcancellationRequestObj2, "WMS");
        }
    }
}
