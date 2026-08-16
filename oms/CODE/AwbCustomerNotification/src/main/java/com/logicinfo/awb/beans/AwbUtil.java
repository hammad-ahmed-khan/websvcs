package com.logicinfo.awb.beans;

public interface AwbUtil {

	public static final String SIM_QUERY = "SELECT * FROM XX_CARRIER_TRACKING_SIM_V " +
										   "\twhere source=:source"+
										   "\tand upper(courier_name) =:courier_name" + 
										   "\tand tracking_number=:tracking_number";
	public static final String WMS_QUERY = "SELECT * FROM XX_CARRIER_TRACKING_WMS_V" + 
										   "\twhere source=:source"+
										   "\tand upper(courier_name) =:courier_name" +
										   "\tand tracking_number=:tracking_number";

	public static final String headerSeqNo = "SELECT OMS_AWB_HEADER_SEQ.NEXTVAL FROM DUAL";

	public static final String omscustOrdAddressSql = "SELECT * FROM OMS_CUST_ORD_ADDRESS WHERE OMS_CUST_ORD_NO=:omscustOrdNo";
	
	public static final String omscustOrdAddressEmailInfoSql = "SELECT BILL_ADD_2 FROM OMS_CUST_ORD_ADDRESS WHERE OMS_CUST_ORD_NO=:omscustOrdNo";

	public static final String omsCustOrdHeadsql = "SELECT * FROM OMS_CUST_ORD_HEAD WHERE STATUS ='S' AND ORD_PAYMENT_STATUS='S' AND CUST_ORDER_NO=:CUSTORDERNO";

	public static final String insertHeaderSql = "INSERT INTO OMS_AWB_HEADER_INFO "
			+ "(AWB_ID,CUST_ORDER_NO,STATUS,COURIER_NAME,SMS_STAUS_CODE,EMAIL_STATUS_CODE,TRACKING_ID,CREATE_DATETIME,DELIVER_PHONE_NO,EMAIL_ID,SOURCE)"
			+ " VALUES ( ?,?,?,?,?,?,?,?,?,?,?)";

	public static final String omscoFulfilldetailWMSSql = "SELECT * FROM OMS_CO_FULFILL_DETAIL WHERE OMS_CUST_ORD_NO =:OMSCUSTORDNO"
			+ "\t AND ITEM = :ITEM AND TSF_NO =:TSFNO";

	public static final String omscoFulfilldetailSIMSql = "SELECT * FROM OMS_CO_FULFILL_DETAIL WHERE OMS_CUST_ORD_NO=:OMSCUSTORDNO"
			+ "\t AND ITEM =:ITEM AND FULFILL_ORDER_NO=:FULFILLORDERNO";

	public static final String omscustOrdItemUnitRetailsql = "SELECT  UNIT_RETAIL FROM OMS_CUST_ORD_ITEM WHERE "
			+ "\t OMS_CUST_ORD_NO = :OMSCUSTORDNO AND LINE_NO =:LINE_NO AND ITEM=:ITEM";

	public static final String omscustOrdItemDiscPriceSql = "SELECT SUM(UNIT_DISCOUNT_AMOUNT) AMOUNT  FROM OMS_CUST_ORD_ITEM_DISC \t"
			+ "WHERE  OMS_CUST_ORD_NO =:OMSCUST_ORD_NO \t" + " AND LINE_NO =:LINENO" + "\t GROUP BY OMS_CUST_ORD_NO, LINE_NO";

	public static final String omsdetailInfoSeq = "select OMS_AWB_DETAIL_SEQ.nextval from dual";
	
	
	 public static final String omsdetailInsertSql = "INSERT INTO OMS_AWB_DETAIL_INFO(AWB_ID, ID, ITEM_ID,QTY,UNIT_RETAIL) VALUES"
	 		+ "(?,?,?,?,?)";
	 
	 public static final String updateOmsHeaderInfoSuccessStatusSql= "UPDATE OMS_AWB_HEADER_INFO SET STATUS ='S'"
	 		+ "WHERE AWB_ID=:AWBID";
	 
	 
	 public static final String disrubuteQtyfromWMS = "SELECT QTY FROM XX_CARRIER_TRACKING_QTY_WMS_V"
	 		+ "\t WHERE CARRIER_SHIPMENT_NBR=:TRACKINGID AND  OMS_LINK_NO=:OMSlINKNO AND ITEM_ID=:ITEM";
	 
	 public static final String distrubteQtyFromSim = "SELECT sum(QUANTITY)   FROM XX_CARRIER_TRACKING_QTY_SIM_V"
	 		+ "\t WHERE TRACKING_ID=:TRACKINGID AND CUST_ORDER_NO=:ORDERNO AND LINK_NO=:LINKO and item=:item";
	 
	 public static final String updateOmsHeaderInfoSqlFailStatusSql= "UPDATE OMS_AWB_HEADER_INFO SET STATUS ='F'"
		 		+ "WHERE AWB_ID=:AWBID";
			 
	 public static final String checkOmsHeaderInfoRecordExist= "SELECT COUNT(1) CNT FROM OMS_AWB_HEADER_INFO WHERE COURIER_NAME=:COURIERNAME AND TRACKING_ID=:TRACKINGID AND SOURCE=:SOURCE and STATUS='S'";
}

