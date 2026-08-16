package org.logicinfo.hybriscancellation.utill;

public class UtillConstantsQuery {

	public static final String Check_Refund_And_cancel_Byid_Query = "select status ,transaction_type from ext_cancel_head where external_cancellation_id=? and transaction_type=? and CUST_ORDER_NO=? and STATUS in('S','F')";
	/* (STATUS='S' or STATUS='F') */
	public static final String Check_Oms_head_custOrd_Payment_Query = "select ORD_PAYMENT_STATUS from oms_cust_ord_head where  CUST_ORDER_NO=? and status='S' ";
	public static final String Check_Oms_head_custOrd_no_Query = "SELECT CUST_ORDER_NO, ORD_PAYMENT_STATUS, STATUS FROM (SELECT CUST_ORDER_NO, ORD_PAYMENT_STATUS, STATUS FROM oms_cust_ord_head WHERE CUST_ORDER_NO = ? ORDER BY CREATE_DATETIME DESC) WHERE ROWNUM = 1";
	public static final String Check_Oms_head_Status_Query = "select status from oms_cust_ord_head where  CUST_ORDER_NO=? and status='S' ";

	public static final String get_tender_amt = "SELECT SUM(TENDER_AMT) FROM OMS_CUST_ORD_TENDER WHERE TENDER_AMT > 0 AND OMS_CUST_ORD_NO IN (SELECT DISTINCT OMS_CUST_ORD_NO FROM OMS_CUST_ORD_HEAD WHERE CUST_ORDER_NO= ? )";

	public static final String insert_Hybris_Head = "insert into ext_cancel_head (CANCEL_HEAD_SEQ_ID,CUST_ORDER_NO,EXTERNAL_CANCELLATION_ID,OMS_CANCELLATION_ID,TRANSACTION_TYPE,CANCEL_REQUESTOR_ID,STATUS,READY_FOR_REFUND,MULE_REFUNDED,PUBLISH_TO_HYBRIS,EXTERNAL_SYS_CANCEL_DATETIME,CANCEL_REQ_DATETIME,LAST_UPDATE_DATETIME)"
			+ "values(?,?,?,?,?,?,?,?,?,?,sysdate,sysdate,sysdate)";

	public static final String insert_Hybris_Item = "insert into ext_cancel_item (CANCEL_HEAD_SEQ_ID,CANCEL_ITEM_SEQ_ID,LINE_NO,ITEM,QTY)"
			+ "values(?,?,?,?,?)";

	public static final String insert_Mule_Table = "Insert into xx_refund_request (REFUND_ID,CANCEL_HEAD_SEQ_ID,CANCEL_TENDER_SEQ_ID,REFUND_CHANNEL,TENDER_TYPE,TENDER_SUBTYPE,DECIMAL_ADJUSTMENT,REFUND_AMOUNT,COUNTRY,CURRENCY,REFUNDABLE,SADAD_BANKID,SADAD_SPTN,SADAD_REFUND_ID,PAYFORT_FORTID,PAYFORT_MERCHANT_REFERENCE,TASHEEL_WALLET_CIVILID,TASHEEL_WALLET_REFNUMBER,TASHEEL_CARDNO,CREATION_DATE)"
			+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	public static final String insert_Hybris_Tender = "insert into ext_cancel_tender (CANCEL_HEAD_SEQ_ID,CANCEL_TENDER_SEQ_ID,PAYMENT_TYPE,TYPE,"
			+ "SUBTYPE,DECIMALADJUSTMENT,REFUND_AMOUNT,REFUNDABLE,SADADBANKID, "
			+ "PAYFORTFORTID,TASHEELWALLETCIVILID,TASHEELWALLETREFNUMBER,TASHEELCARDNO,PAYFORTMERCHANTREFERENCE)"
			+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static final String update_Hybris_Head_Status = "update ext_cancel_head set STATUS='S' where EXTERNAL_CANCELLATION_ID=? and CANCEL_HEAD_SEQ_ID=?";
	public static final String update_Hybris_Head_Error_Message = "update ext_cancel_head set ERROR_MESSAGE=? where CANCEL_HEAD_SEQ_ID=?";
	public static final String update_Hybris_Head_Failed_Status = "update ext_cancel_head set STATUS='F' where EXTERNAL_CANCELLATION_ID=? and CANCEL_HEAD_SEQ_ID=?";

	public static final String generate_Head_seq_Id = "select XX_EXT_CAN_HEAD_ID_SEQ.nextval from dual";
	public static final String generate_item_seq_Id = "select XX_EXT_CAN_ITEM_ID_SEQ.nextval from dual";
	public static final String generate_tender_seq_Id = "select XX_EXT_CAN_TENDER_ID_SEQ.nextval from dual";
	public static final String generate_Oms_Cancel__Id = "select OMSDEV.OMS_CANCEL_ID_SEQ.nextval from dual";
	public static final String generate_Refund_Id = "select XX_RR_REFUND_ID_SEQ.nextval from dual";
	public static final String generate_Sadad_Refund_Id = "select XX_RR_SADAD_REFUND_ID_SEQ.nextval from dual";
	public static final String update_Hybris_Item_Table = "update ext_cancel_item set ERROR_MESSAGE=? where CANCEL_HEAD_SEQ_ID=? and LINE_NO=? and ITEM=?";
	public static final String get_Lineitem_Res_Item_table = "select ITEM , LINE_NO,  ERROR_MESSAGE from  ext_cancel_item where CANCEL_HEAD_SEQ_ID=?";
	public static final String get_Lineitem_Res_Item_table_Validation_level = "select ITEM , LINE_NO,  ERROR_MESSAGE from  ext_cancel_item where CANCEL_HEAD_SEQ_ID =(select CANCEL_HEAD_SEQ_ID from ext_cancel_head where EXTERNAL_CANCELLATION_ID=? and CUST_ORDER_NO=? and status=?)";
	public static final String update_Hybris_Head_Oms_Cancellation_Id = "update ext_cancel_head set OMS_CANCELLATION_ID =(select OMS_CANCEL_ID from OMS_CO_CANCEL_HEAD"
			+ "                                                         where cust_ord_no =? and CANCEL_REQ_ID=?)  where CANCEL_HEAD_SEQ_ID =?";
}
