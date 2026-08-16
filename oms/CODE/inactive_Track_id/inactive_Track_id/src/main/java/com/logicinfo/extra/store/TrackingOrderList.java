// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import org.apache.log4j.Logger;

public class TrackingOrderList
{
    private static Logger LOGGER;
    ExtraDBConnection extraDBConnection;
    CallableStatement inActivecommOrderCallableStmt;
    PreparedStatement inActivecommOrderPreparedStmt;
    PreparedStatement inActiveShipmentCarrierPreparedStmt;
    PreparedStatement inActiveWmsShipmentCarrierPreparedStmt;
    PreparedStatement inactiveSimShipmentUpdatepreparedStmt;
    PreparedStatement inactiveWmsShipmentUpdatepreparedStmt;
    PreparedStatement inactiveSimShipmentUpdatepreparedStmtRetry;
    PreparedStatement inactiveWmsShipmentUpdatepreparedStmtRetry;
    
    static {
        TrackingOrderList.LOGGER = Logger.getLogger((Class)TrackingOrderList.class);
    }
    
    public TrackingOrderList() throws SQLException, Exception {
        TrackingOrderList.LOGGER.info((Object)"TrackingOrderList object created::");
        (this.extraDBConnection = new ExtraDBConnection()).loadProperties("sim");
        final Connection simConnection = this.extraDBConnection.getDBConnection();
        this.inActivecommOrderCallableStmt = simConnection.prepareCall("{call INACTIVE_TRACKING_ECOMM_ORDERS(?,?)}");
        this.inActivecommOrderPreparedStmt = simConnection.prepareCall("select DISTINCT cust_order_no, tracking_id , ship_carrier_id ,PROCESS_IND from INACTIVE_ORDER_TRACKING_STATUS WHERE PROCESS_IND=? AND COUNT < 5");
        this.inActiveShipmentCarrierPreparedStmt = simConnection.prepareCall("SELECT ID,CODE, DESCRIPTION, MANIFEST_TYPE FROM shipment_carrier");
        this.inactiveSimShipmentUpdatepreparedStmt = simConnection.prepareStatement("UPDATE INACTIVE_ORDER_TRACKING_STATUS SET PROCESS_IND='Y' , COUNT = COUNT+1, LAST_UPDATETIME=SYSDATE WHERE CUST_ORDER_NO= ? AND TRACKING_ID= ?");
        this.inactiveSimShipmentUpdatepreparedStmtRetry = simConnection.prepareStatement("UPDATE INACTIVE_ORDER_TRACKING_STATUS SET PROCESS_IND='N', COUNT = COUNT+1, LAST_UPDATETIME=SYSDATE WHERE CUST_ORDER_NO= ? AND TRACKING_ID= ?");
        this.extraDBConnection.loadProperties("wms");
        final Connection wmsConnection = this.extraDBConnection.getDBConnection();
        this.inActiveWmsShipmentCarrierPreparedStmt = wmsConnection.prepareCall("select CUST_ORDER_NBR, CARRIER_SHIPMENT_NBR, CARRIER_CODE from xx_awb_status_upload WHERE UPLOAD_STATUS=? AND EVENT_CODE=? AND RETRY_COUNT<5");
        this.inactiveWmsShipmentUpdatepreparedStmt = wmsConnection.prepareStatement("UPDATE  xx_awb_status_upload SET LAST_MODIFIED_DATE=SYSDATE , UPLOAD_STATUS ='Y' , RETRY_COUNT=RETRY_COUNT+1, MODIFIED_BY ='OMSUSER' WHERE EVENT_CODE='Cancel' AND UPLOAD_STATUS='N' AND CUST_ORDER_NBR= ? AND CARRIER_SHIPMENT_NBR= ? AND CARRIER_NAME= ?");
        this.inactiveWmsShipmentUpdatepreparedStmtRetry = wmsConnection.prepareStatement("UPDATE  xx_awb_status_upload SET RETRY_COUNT=RETRY_COUNT+1, LAST_MODIFIED_DATE=SYSDATE , UPLOAD_STATUS ='N' , MODIFIED_BY ='OMSUSER' WHERE EVENT_CODE='Cancel' AND UPLOAD_STATUS='N' AND CUST_ORDER_NBR= ? AND CARRIER_SHIPMENT_NBR= ? AND CARRIER_NAME= ?");
    }
    
    public List<InActiveTrackingOrders> getInactiveOrderListFromSim() throws SQLException {
        TrackingOrderList.LOGGER.info((Object)"inside getInactiveOrderListFromSim method....");
        final List<InActiveTrackingOrders> inActiveTrackingOrdersList = new ArrayList<InActiveTrackingOrders>();
        ResultSet inActiveTrackingOrdersResultSet = null;
        TrackingOrderList.LOGGER.info((Object)"Before Package call....");
        this.inActivecommOrderCallableStmt.registerOutParameter(1, 2);
        this.inActivecommOrderCallableStmt.registerOutParameter(2, 12);
        this.inActivecommOrderCallableStmt.executeUpdate();
        TrackingOrderList.LOGGER.info((Object)"After Package call....");
        final int num = this.inActivecommOrderCallableStmt.getInt(1);
        if (num == 0) {
            this.inActivecommOrderPreparedStmt.setString(1, "N");
            inActiveTrackingOrdersResultSet = this.inActivecommOrderPreparedStmt.executeQuery();
            InActiveTrackingOrders inActiveTrackingOrdersObj = null;
            while (inActiveTrackingOrdersResultSet.next()) {
                inActiveTrackingOrdersObj = new InActiveTrackingOrders();
                inActiveTrackingOrdersObj.setCustomerOrderNo(inActiveTrackingOrdersResultSet.getString("CUST_ORDER_NO"));
                inActiveTrackingOrdersObj.setTrackingId(inActiveTrackingOrdersResultSet.getString("TRACKING_ID"));
                inActiveTrackingOrdersObj.setCarrierId(Integer.valueOf(inActiveTrackingOrdersResultSet.getInt("SHIP_CARRIER_ID")));
                inActiveTrackingOrdersObj.setProcessInd(inActiveTrackingOrdersResultSet.getString("PROCESS_IND"));
                inActiveTrackingOrdersList.add(inActiveTrackingOrdersObj);
            }
            TrackingOrderList.LOGGER.info((Object)("TheinActiveTrackingOrdersList Size is " + inActiveTrackingOrdersList.size()));
            return inActiveTrackingOrdersList;
        }
        TrackingOrderList.LOGGER.info((Object)"no records are available");
        return inActiveTrackingOrdersList;
    }
    
    public List<InActiveTrackingOrders> getInactiveOrderListFromWms() throws SQLException {
        final List<InActiveTrackingOrders> inActiveTrackingOrdersWmsList = new ArrayList<InActiveTrackingOrders>();
        ResultSet inActiveTrackingOrdersWmsResultSet = null;
        InActiveTrackingOrders inActiveTrackingOrdersWmsObj = null;
        this.inActiveWmsShipmentCarrierPreparedStmt.setString(1, "N");
        this.inActiveWmsShipmentCarrierPreparedStmt.setString(2, "Cancel");
        inActiveTrackingOrdersWmsResultSet = this.inActiveWmsShipmentCarrierPreparedStmt.executeQuery();
        while (inActiveTrackingOrdersWmsResultSet.next()) {
            inActiveTrackingOrdersWmsObj = new InActiveTrackingOrders();
            inActiveTrackingOrdersWmsObj.setCustomerOrderNo(inActiveTrackingOrdersWmsResultSet.getString("CUST_ORDER_NBR"));
            inActiveTrackingOrdersWmsObj.setTrackingId(inActiveTrackingOrdersWmsResultSet.getString("CARRIER_SHIPMENT_NBR"));
            inActiveTrackingOrdersWmsObj.setCarrierCode(inActiveTrackingOrdersWmsResultSet.getString("CARRIER_CODE"));
            inActiveTrackingOrdersWmsList.add(inActiveTrackingOrdersWmsObj);
        }
        TrackingOrderList.LOGGER.info((Object)(" Wms inActiveTrackingOrdersWmsList Size is " + inActiveTrackingOrdersWmsList.size()));
        return inActiveTrackingOrdersWmsList;
    }
    
    public Map<Integer, InActiveTrackingOrdersShipmentCarrier> getShipmentCarrierId() throws SQLException {
        TrackingOrderList.LOGGER.info((Object)"Inside getShipmentCarrierId method...");
        ResultSet inActiveTrackingOrdersBasedonidResultSet = null;
        final Map<Integer, InActiveTrackingOrdersShipmentCarrier> shipmentCarrierMap = new HashMap<Integer, InActiveTrackingOrdersShipmentCarrier>();
        inActiveTrackingOrdersBasedonidResultSet = this.inActiveShipmentCarrierPreparedStmt.executeQuery();
        while (inActiveTrackingOrdersBasedonidResultSet.next()) {
            shipmentCarrierMap.put(inActiveTrackingOrdersBasedonidResultSet.getInt("ID"), new InActiveTrackingOrdersShipmentCarrier(inActiveTrackingOrdersBasedonidResultSet.getString("CODE"), inActiveTrackingOrdersBasedonidResultSet.getString("DESCRIPTION")));
        }
        return shipmentCarrierMap;
    }
    
    public void updateProcessIndFlagAndDate(final AwbCancellationRequest awbcancellationRequestObj, final String source) {
        TrackingOrderList.LOGGER.info((Object)"updateProcessIndFlagAndDate method called..");
        try {
            if ("SIM".equals(source)) {
                this.inactiveSimShipmentUpdatepreparedStmt.setString(1, awbcancellationRequestObj.getOrderNo());
                this.inactiveSimShipmentUpdatepreparedStmt.setString(2, awbcancellationRequestObj.getCourierTrackingNo());
                this.inactiveSimShipmentUpdatepreparedStmt.executeUpdate();
            }
            else {
                this.inactiveWmsShipmentUpdatepreparedStmt.setString(1, awbcancellationRequestObj.getOrderNo());
                this.inactiveWmsShipmentUpdatepreparedStmt.setString(2, awbcancellationRequestObj.getCourierTrackingNo());
                this.inactiveWmsShipmentUpdatepreparedStmt.setString(3, awbcancellationRequestObj.getCarrier());
                this.inactiveWmsShipmentUpdatepreparedStmt.executeUpdate();
            }
            TrackingOrderList.LOGGER.info((Object)"inactiveWmsShipment Record successfully updated..");
        }
        catch (SQLException e) {
            TrackingOrderList.LOGGER.error((Object)("TrackingOrderList::updateProcessIndFlagAndDate method SQLException" + e));
        }
        catch (Exception e2) {
            TrackingOrderList.LOGGER.error((Object)("TrackingOrderList::updateProcessIndFlagAndDate method SQLException" + e2));
        }
    }
    
    public void updateProcessIndCountAndDate(final AwbCancellationRequest awbcancellationRequestObj, final String source) {
        TrackingOrderList.LOGGER.info((Object)"updateProcessIndCountAndDate method called..");
        try {
            if ("SIM".equals(source)) {
                this.inactiveSimShipmentUpdatepreparedStmtRetry.setString(1, awbcancellationRequestObj.getOrderNo());
                this.inactiveSimShipmentUpdatepreparedStmtRetry.setString(2, awbcancellationRequestObj.getCourierTrackingNo());
                this.inactiveSimShipmentUpdatepreparedStmtRetry.executeUpdate();
            }
            else {
                this.inactiveWmsShipmentUpdatepreparedStmtRetry.setString(1, awbcancellationRequestObj.getOrderNo());
                this.inactiveWmsShipmentUpdatepreparedStmtRetry.setString(2, awbcancellationRequestObj.getCourierTrackingNo());
                this.inactiveWmsShipmentUpdatepreparedStmtRetry.setString(3, awbcancellationRequestObj.getCarrier());
                this.inactiveWmsShipmentUpdatepreparedStmtRetry.executeUpdate();
            }
            TrackingOrderList.LOGGER.info((Object)"inactiveWmsShipment Record successfully updated..");
        }
        catch (SQLException e) {
            TrackingOrderList.LOGGER.error((Object)("TrackingOrderList::updateProcessIndCountAndDate method SQLException" + e));
        }
        catch (Exception e2) {
            TrackingOrderList.LOGGER.error((Object)("TrackingOrderList::updateProcessIndCountAndDate method SQLException" + e2));
        }
    }
}
