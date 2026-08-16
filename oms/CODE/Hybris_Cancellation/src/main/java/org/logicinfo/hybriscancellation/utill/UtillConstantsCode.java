package org.logicinfo.hybriscancellation.utill;

public class UtillConstantsCode {
	// code
	public static final String invaildInput = "404";
	public static final String noDataCode = "100";
	public static final String succesCode = "200";
	public static final String failedCode = "500";

	public static final String rufundMismatchCode = "600";
	public static final String tenderSuccessCode = "700";
	public static final String tenderfailedCode = "505";
	public static final String refundFailedCode = "506";

	public static final String muleTableFailedCode = "507";
	public static final String OmsCustomTablePresistErrorCode = "405";

	// message
	public static final String successMpMessage = "Requested proecessed successfully";
	public static final String successMsg = "Success";
	public static final String refund_and_cancel_Via = "API";
	public static final String failedMsg = "Failed";
	public static final String hybrisHeadStatus = "S";
	public static final String hybrisHeadFailed = "F";
	public static final String success = "S";
	public static final String failure = "F";
	public static final String refundsuccessMsg = "Refund suceess";
	public static final String cancesuccessMsg = "cancellation suceess";
	public static final String refundfailedMsg = "Refund failed cause of order has created successfuly in OMS";
	public static final String canelfailedMsg = "cancellation failed";
	public static final String canel_system_error_Msg = "Cancellation service system error";
	public static final String custOrdError = "Order number does not exist";
	public static final String custOrdFailedError = "Order is failed in OMS ";
	public static final String transactionNoError = "TransactionType and runfundable indicator error";
	public static final String emptyTendervalue = "Tender  data not available for this order no";
	public static final String invalidTenderAmt = "The requested tender amt greater than actual tender amt";
	public static final String hybris_head_error_msg = "Error while inserting hybris head table";
	public static final String hybris_head_update_error_msg = "Error while updating status in hybris head table";
	public static final String hybris_head_update_error_msg_status = "Error while updating Error message in hybris head table";
	public static final String hybris_item_error_msg = "Error while inserting hybris item table";
	public static final String hybris_item_Update_error_msg = "Error while Updating response in  hybris item table";
	public static final String mule_table_error_msg = "Error while inserting mule table but order got Cancelled successfuly in OMS";
	public static final String hybris_tender_error_msg = "Error while inserting hybris tender table";
	public static final String sequence_Error_msg = "Error while generating Head sequence id";
	public static final String oms_Cancel_Id_Error = "Error while generating oms cancel id";
	public static final String sequence_Item_Error_msg = "Error while generating item sequence id";
	public static final String sequence_Tender_Error_msg = "Error while generating tender sequence id";
	public static final String sequence_Mule_Error_msg = "Error while generating Mule sequence id";
	public static final String CoCancellation_Service_Call_Error = "Error while calling CoCancellation Service ";
	public static final String json_Generate_Error = "Error while generating input Json for Cancellation Web service ";
	public static final String convert_to_Json_Res = "Error while converting  Json response to ";
	public static final String request_error = "The given input format is wrong (or) Server is down";
	public static final String Common_Item_Error = "Item cannot cancelled due to other reason";
	public static final String line_level_Res_Error_Msg = "Error while generating line level response";
	public static final String Payment_Error = "Cannot cancel the Order cause order is success but payment is pending in OMS ";
	public static final String reason_Error = " Reason is not avialble ";

	public static final String hybris_head_Oms_Cancel_Id_update_error_msg = "Error while updating oms cancellation id  in hybris head table";

	public static final String update_Hybris_Head_Oms_Cancellation_Id = "update ext_cancel_head set OMS_CANCELLATION_ID =(select OMS_CANCEL_ID from OMS_CO_CANCEL_HEAD"
			+ "                                                         where cust_ord_no =? and CANCEL_REQ_ID=?)  where CANCEL_HEAD_SEQ_ID =?";
	// wsdl url

}
