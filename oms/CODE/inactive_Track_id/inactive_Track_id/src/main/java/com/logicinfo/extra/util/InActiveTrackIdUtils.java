// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.util;

public class InActiveTrackIdUtils
{
    public static final String executeprocedure = "{call INACTIVE_TRACKING_ECOMM_ORDERS(?,?)}";
    public static final String fetchrecordsfromsim = "select DISTINCT cust_order_no, tracking_id , ship_carrier_id ,PROCESS_IND from INACTIVE_ORDER_TRACKING_STATUS WHERE PROCESS_IND=? AND COUNT < 5";
    public static final String fetchrecordsbasedonid = "SELECT ID,CODE, DESCRIPTION, MANIFEST_TYPE FROM shipment_carrier";
    public static final String fetchwmsallinactivetrackingrecords = "select CUST_ORDER_NBR, CARRIER_SHIPMENT_NBR, CARRIER_CODE from xx_awb_status_upload WHERE UPLOAD_STATUS=? AND EVENT_CODE=? AND RETRY_COUNT<5";
    public static final String updatesimheadertable = "UPDATE INACTIVE_ORDER_TRACKING_STATUS SET PROCESS_IND='Y' , COUNT = COUNT+1, LAST_UPDATETIME=SYSDATE WHERE CUST_ORDER_NO= ? AND TRACKING_ID= ?";
    public static final String updatewmsheadertable = "UPDATE  xx_awb_status_upload SET LAST_MODIFIED_DATE=SYSDATE , UPLOAD_STATUS ='Y' , RETRY_COUNT=RETRY_COUNT+1, MODIFIED_BY ='OMSUSER' WHERE EVENT_CODE='Cancel' AND UPLOAD_STATUS='N' AND CUST_ORDER_NBR= ? AND CARRIER_SHIPMENT_NBR= ? AND CARRIER_NAME= ?";
    public static final String updatesimheadertable1 = "UPDATE INACTIVE_ORDER_TRACKING_STATUS SET PROCESS_IND='N', COUNT = COUNT+1, LAST_UPDATETIME=SYSDATE WHERE CUST_ORDER_NO= ? AND TRACKING_ID= ?";
    public static final String updatewmsheadertable1 = "UPDATE  xx_awb_status_upload SET RETRY_COUNT=RETRY_COUNT+1, LAST_MODIFIED_DATE=SYSDATE , UPLOAD_STATUS ='N' , MODIFIED_BY ='OMSUSER' WHERE EVENT_CODE='Cancel' AND UPLOAD_STATUS='N' AND CUST_ORDER_NBR= ? AND CARRIER_SHIPMENT_NBR= ? AND CARRIER_NAME= ?";
}
