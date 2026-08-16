package com.db.util;

public class AwbInfoUtil {
    public static final String hearder_info_sms = "select AWB_ID,COURIER_NAME,TRACKING_ID,CUST_ORDER_NO,SMS_STAUS_CODE,DELIVER_PHONE_NO from OMS_AWB_HEADER_INFO where SMS_STAUS_CODE='0' and status='S'";
	public static final String updateSmsFlag_sms = "UPDATE OMS_AWB_HEADER_INFO SET SMS_STAUS_CODE= ? " + " WHERE AWB_ID=?";
	public static final String getFromPhoneNumber_sms = "SELECT PARAMETER_VALUE FROM OMS_CFS_SYSTEM_PARAMETERS where PARAMETER_ID =?";
	public static final String getcustOrderNoLanguageCode = "select  CUSTOMER_LANG from oms_cust_ord_head where status='S' and ord_payment_status='S' and cust_order_no=?";
    public static final String getCourierName="select COURIER_URL from OMS_AWB_COURIER_INFO  where upper(courier_name)=?";
    public static final String getMultipleShipmentcheck="{?= call OMS_CHECK_MULTI_SHIPMENT.f_check_multishipment_Order(?)}";
    public static final String getReminderHeader_info_sms="select AWB_ID,COURIER_NAME,TRACKING_ID,CUST_ORDER_NO,SMS_STAUS_CODE,DELIVER_PHONE_NO from OMS_AWB_HEADER_INFO where SMS_STAUS_CODE='1' and status='S'";
   
  
    public static final String hearder_info_email="select AWB_ID,COURIER_NAME,TRACKING_ID,CUST_ORDER_NO,EMAIL_ID from OMS_AWB_HEADER_INFO where  EMAIL_STATUS_CODE='0' and status='S'";
    public static final String ItemList_email="select * from OMS_AWB_DETAIL_INFO where AWB_ID=?";
    public static final String updateEmailFlag="UPDATE OMS_AWB_HEADER_INFO SET EMAIL_STATUS_CODE= ? " + " WHERE AWB_ID=?";
    public static final String getFromEmail="SELECT PARAMETER_VALUE, PARAMETER_NAME FROM OMS_CFS_SYSTEM_PARAMETERS where PARAMETER_ID =?";
    public static final String itemName_email="select * from ITEM_MASTER where ITEM=?";
    public static final String omsCurd_email="SELECT * FROM OMS_CUST_ORD_HEAD WHERE STATUS ='S' AND ORD_PAYMENT_STATUS='S' AND CUST_ORDER_NO=?";
    public static final String customer_name_email="select * from oms_cust_ord_address where OMS_CUST_ORD_NO=?";
    public static final String getReminderHeader_info_email="select AWB_ID,COURIER_NAME,TRACKING_ID,CUST_ORDER_NO,EMAIL_ID  from OMS_AWB_HEADER_INFO where EMAIL_STATUS_CODE='1' and status='S'";
    
    
  


}
