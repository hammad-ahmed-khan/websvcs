package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.logicinfo.oms.ejb.OmsSparePartAudit;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.model.SparePartsCancelRequest;

public class SparePartsCancelHelperBean {

	final String webServiceName = "Spare Parts Cancel WebService";
	final String statusInProcess = "IP";
	final String statusRejected = "RJ";
	final String statusClosed = "CL";

	public SparePartsCancelHelperBean() {
		super();
	}

	public OmsSparePartCancelHdr formOmsSparePartsCancelObject(SparePartsCancelRequest inputData) {

		OmsSparePartCancelHdr theSparePartsCancelRequestObj = new OmsSparePartCancelHdr();
		theSparePartsCancelRequestObj.setServiceReqSeqId(inputData.getServiceRequestId());
		theSparePartsCancelRequestObj.setSequenceId(inputData.getSequenceId());
		theSparePartsCancelRequestObj.setOmsServiceReqSeqId(inputData.getOMSServiceId());
		theSparePartsCancelRequestObj.setCancellationId(inputData.getCancellationId());
		theSparePartsCancelRequestObj.setCancelQty(inputData.getQuantity());

		theSparePartsCancelRequestObj.setStatus(statusInProcess);
		theSparePartsCancelRequestObj.setCreatedBy(webServiceName);
		theSparePartsCancelRequestObj.setCreateDatetime(new Timestamp((new Date()).getTime()));

		return theSparePartsCancelRequestObj;
	}

	/*
	 * public OmsSparePartFulfill formOmsSparePartFulfillObject(String item,
	 * BigDecimal sourceLocation, BigDecimal quantityDeducted, BigDecimal
	 * quantityReceived, BigDecimal quantityReserved, BigDecimal quantityShipped,
	 * BigDecimal quantityTsfReserved, BigDecimal quantityUnreserved, BigDecimal
	 * transferId) {
	 * 
	 * OmsSparePartFulfill theOmsSparePartFulfillObject = new OmsSparePartFulfill();
	 * theOmsSparePartFulfillObject.setOmsServiceReqSeqId(OMSSparePartsServiceId);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityDeducted);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityReceived);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityReserved);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityShipped);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityTsfReserved);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(quantityUnreserved);
	 * theOmsSparePartFulfillObject.setQuantityDeducted(transferId);
	 * 
	 * theOmsSparePartFulfillObject.setItemId(item);
	 * theOmsSparePartFulfillObject.setSourceLocation(sourceLocation);
	 * 
	 * theOmsSparePartFulfillObject.setCreatedBy(webServiceName);
	 * theOmsSparePartFulfillObject.setCreateDatetime(new Timestamp((new
	 * Date()).getTime())); theOmsSparePartFulfillObject.setLastUpdatedDatetime(new
	 * Timestamp((new Date()).getTime()));
	 * 
	 * return theOmsSparePartFulfillObject; }
	 */
	public OmsSparePartAudit formOmsSparePartAuditObject(String itemId, String eventType, BigDecimal quantity, BigDecimal sourceLocation, String omsServiceRequestId) {

		OmsSparePartAudit theOmsSparePartAuditObject = new OmsSparePartAudit();

		theOmsSparePartAuditObject.setOmsServiceReqSeqId(omsServiceRequestId);
		theOmsSparePartAuditObject.setItemId(itemId);
		theOmsSparePartAuditObject.setQuantity(quantity);
		theOmsSparePartAuditObject.setSourceLocation(sourceLocation);
		theOmsSparePartAuditObject.setEventType(eventType);

		theOmsSparePartAuditObject.setCreatedBy(webServiceName);
		theOmsSparePartAuditObject.setCreateDatetime(new Timestamp((new Date()).getTime()));

		return theOmsSparePartAuditObject;
	}
}
