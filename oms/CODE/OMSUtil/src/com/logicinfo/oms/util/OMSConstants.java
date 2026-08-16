package com.logicinfo.oms.util;

public class OMSConstants {


    public final static String DS_SIM_STRING = "jdbc/sim";
    public final static String DS_OMS_STRING = "jdbc/oms";
    public final static String DS_RMS_STRING = "jdbc/rms";
    public final static String DS_DAS_STRING = "jdbc/das";

    public final static String LOCATION_TYPE_WH = "WH";
    public final static String LOCATION_TYPE_ST = "ST";

    public final static String STATUS_IN_PROGRESS = "IP";
    public final static String STATUS_REJECTED = "RJ";

    public final static String inventoryReserveCode = "SAS_INV_RESV_CODE";
    public final static String inventoryAdjReasonCode = "OMS_INV_ADJ_RSN_CODE";
    public final static String inventoryUnReserveCode = "SAS_INV_UNRESV_CODE";
    public final static String inventoryDeductCode = "SAS_INV_DEDUCT_CODE";
    public final static String QUANTITY_TRANSFERRED = "TRSFRD";
    public final static String QUANTITY_RESERVED = "RSV";
    public final static String QUANTITY_UNRESERVED = "UNRSV";
    public final static String QUANTITY_SHIPPED = "SHIP";
    public final static String QUANTITY_RECEIVED = "RCVD";
    public final static String QUANTITY_DEDUCTED = "DEDUCE";
    public final static String INVALID_DELV_TYP = "INVALID_DELV_TYP";
    public final static String CLOSED_ORDER = "CLOSED_ORDER";
    public final static String INVALID_ITEM = "INVALID_ITEM";
    public final static String UNAVAIL_COMB_ID = "UNAVAIL_COMB_ID";
    public final static String INVALID_QTY = "INVALID_QTY";
    public final static String QUANTITY_REJECTED ="REJECT";
    public final static int MAX_NO_OF_RETRY=2;
    //----------------------- OMS ERROR CODES -----------------------------------------
    public final static String ERR_UNKNOWN_CODE = "UNKNOWN_ERROR";
    public final static String ERR_UNHANDLED_EXCEPTION = "ERR_UNHANDLED_EXCEPTION";
    
    public final static String ERR_DETAIL_REC_COUNT = "ERR_DETAIL_REC_COUNT";

    public final static String ERR_TABLE_INSERT = "ERR_TABLE_INSERT";
    public final static String ERR_TABLE_UPDATE = "ERR_TABLE_UPDATE";
    public final static String ERR_TABLE_DELETE = "ERR_TABLE_DELETE";

    public final static String ERR_DATA_NOT_EXISTS = "ERR_DATA_NOT_EXISTS";
    public final static String ERR_DATA_EXISTS = "ERR_DATA_EXISTS";

    public final static String ERR_REQ_QTY_MORE_PEND_QTY = "REQ_QTY_MORE_PEND_QTY";

    public final static String ERR_HD_REQ_FULFILLED = "HOME_DEL_REQ_FULFILLED";

    public final static String ERR_LOC_MTRX_MISSING = "LOC_MTRX_MISSING";
    public final static String ERR_FULFILL_LOC_MISSING = "FULFILL_LOC_MISSING";
    public final static String ERR_CO_UNKNOWN_LOC_TYPE = "CO_UNKNOWN_LOC_TYPE";
    public final static String ERR_UNAVL_INV = "UNAVL_INV";
    public final static String ERR_CO_NOT_EXISTS = "CO_NOT_EXISTS";

    public final static String ERR_DELIV_NOT_IN_PROGRESS = "DELIV_NOT_IN_PROGRESS";
    public final static String ERR_INVALID_INPUT ="INVALID_INPUT";
    public final static String ERR_INSUFFICIENT_INV  ="INSUFFICIENT_INV ";
    public final static String OMS_ORDER_STATUS_HEADER = "<soapenv:Envelope xmlns:soapenv="+"\"http://schemas.xmlsoap.org/soap/envelope/\""+"\t xmlns:oms=\""+
                                                         "http://www.extra.com/Services/OmsStatus\""+"\txmlns:ext=\""+"http://schemas.datacontract.org/2004/07/eXtra.Services.Oms\""+
                                                         "><soapenv:Header/><soapenv:Body>";
    public final static String OMS_GET_EXPRESS_DLV = "SELECT COUNT(1) COUNT  FROM OMS_CUST_ORD_ITEM  OI  , OMS_CUST_ORD_HEAD OH , ITEM_MASTER IM \n" + 
    "WHERE OH.OMS_CUST_ORD_NO =OI.OMS_CUST_ORD_NO \n" + 
    "AND OH.APPLICATION_ID <>'ORPOS' \n" + 
    "AND OI.ITEM = IM.ITEM \n" + 
    "AND OH.STATUS='S' \n" + 
    "AND OH.ORD_PAYMENT_STATUS='S'\n" + 
    "AND IM.INVENTORY_IND='N'\n" + 
    "AND IM.DEPT='4701'\n" + 
    "AND UPPER(IM.DESC_UP)= UPPER('Express Delivery')\n" + 
    "AND OH.CUST_ORDER_NO = ?";

    public OMSConstants() {
        super();
    }


}
