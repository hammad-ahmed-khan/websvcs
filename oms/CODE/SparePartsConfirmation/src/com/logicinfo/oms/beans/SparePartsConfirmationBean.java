package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsSparePartAudit;
import com.logicinfo.oms.ejb.OmsSparePartConfirmDtl;
import com.logicinfo.oms.ejb.OmsSparePartConfirmHdr;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;
import com.logicinfo.oms.model.ServiceConfDetailsResponseType;
import com.logicinfo.oms.model.ServiceConfirmationDetailType;
import com.logicinfo.oms.model.SparePartsConfirmation;
import com.logicinfo.oms.model.SparePartsConfirmationProcessedObject;
import com.logicinfo.oms.model.SparePartsConfirmationResponse;
import com.logicinfo.oms.model.SparePartsFulfillProcessedObject;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import java.sql.*;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;
import java.util.HashMap;

public class SparePartsConfirmationBean {
	private final static Logger log = Logger.getLogger(SparePartsConfirmationBean.class.getName());
	String statusRejected = "RJ";
	String statusClosed = "CL";
	final String webServiceName = "Spare Parts Confirm WebService";
	boolean testing = true;

	OracleIntegrationSevicesSIM simOracleIntegrationSevices;
	// List<SparePartsConfirmationProcessedObject> successfullyProcessedObjList =
	// new ArrayList<SparePartsConfirmationProcessedObject>();

	SparePartsConfirmationResponse theResponse;

	public SparePartsConfirmationBean() {
		super();
		theResponse = new SparePartsConfirmationResponse();
	}

	// This method checks if the spaces are not passed as Customer Number or
	// Customer Order No

	public void performBasicValidation(SparePartsConfirmation input) throws IllegalArgumentException, SOAPException {
		String errInputArgument = "";

		try {
			// Step 1 - Check if the KEY arguments passed are not blanks.
			log.info("  --> Verifying Service Request Id " + input.getServiceRequestId());

			if (null != input.getServiceRequestId() && input.getServiceRequestId().trim().equals(""))
				errInputArgument = errInputArgument + ("".equals(errInputArgument) ? "" : ", ") + "Service Request Id";

			if (!"".equals(errInputArgument)) {
				log.error(" --> ERROR : " + errInputArgument + ": Illegal input passed.");
				throw new IllegalArgumentException(errInputArgument + ": Illegal input passed.");
			}
			OMSUtilSessionEJB session = null;
			try {
				session = OMSUtil.doLookup();
			} catch (Exception e) {
				log.error("Cannot locate EJB --- " + e.getMessage());
				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
			}
			log.info("  --> Verifying duplicate request in Header for Service Req Id " + input.getServiceRequestId() + " and Confirmation id " + input.getServiceConfirmationId());

			// Check for duplicate record.
			OmsSparePartConfirmHdr theConfirmHdr = null;
			try {
				theConfirmHdr = session.getOmsSparePartConfirmHdrFindByKey(input.getServiceRequestId(), input.getServiceConfirmationId());

			} catch (Exception e) {
				log.info(" --> No Confirm Hdr information found.");
			}

			if (null != theConfirmHdr && null != theConfirmHdr.getServiceRequestId()) {

				log.info("  --> Duplicate request found in Header for Service Req Id " + theConfirmHdr.getServiceRequestId() + " and Confirmation id " + theConfirmHdr.getServiceConfirmId());

				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_EXISTS, "1", new String[] { input.getServiceRequestId() }));
			} else {
				log.info("    --> NoResultException - No Service Request for Req ID: " + input.getServiceRequestId());
				log.info("    --> Proceeding with New Request Creation.");
			}

		} catch (SOAPException se) {
			throw new SOAPException(se.getMessage());
		} catch (Exception e) {
			log.error("ERROR during validation step. " + e.getMessage());
			throw new SOAPException(e.getMessage());
		}
	}

	// This method will ALWAYS create an entry into the
	// OMS_CO_SparePartsConfirmation_HEAD table.

	public void createSparePartsConfirmationHeader(SparePartsConfirmation input) throws SOAPException {
		log.info("  --> Start-Save OMS_SPARE_PART_CONFIRM_HDR");

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			OmsSparePartConfirmHdr theServiceConfirmationHeaderObj = formServiceConfirmationHeaderObject(input);
			theServiceConfirmationHeaderObj.setStatus(statusClosed);
			log.info("   --> Inserting into OMS_SPARE_PART_CONFIRM_HDR table");
			theServiceConfirmationHeaderObj = session.persistOmsSparePartConfirmHdr(theServiceConfirmationHeaderObj);
			log.info("  --> Successfully created Spare Part Confirm Head info");
		} catch (Exception e) {
			log.error("Error saving data into OMS_SPARE_PART_CONFIRM_HDR table. " + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
	}

	// Updated by Santosh Suman
	// Added new parameter reasonCodeMap
	public SparePartsConfirmationResponse processAndCreateSparePartsConfirmationDetails(SparePartsConfirmation input, HashMap<String, String> reasonCodeIdMap) throws SOAPException {
		log.info("  --> Start-Save OMS SPARE PART CONFIRM Detail");

		OMSUtilSessionEJB session = OMSUtil.doLookup();

		simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();

		boolean errorOccured = false;
		String errMessage;

		try {

			List<ServiceConfirmationDetailType> theDetailsList = input.getServiceConfirmationDetail();
			theResponse.setServiceRequestId(input.getServiceRequestId());

			// Loop through all detail records in the request XML.

			for (ServiceConfirmationDetailType theDetail : theDetailsList) {
				errMessage = null;
				OmsSparePartHeader theSparePartsHeadObj = null;
				// Prepare the SparePartsConfirmation Detail Object to be saved to the database
				// table OMS_SPARE_PART_CONFIRN_DTL.
				OmsSparePartConfirmDtl theServiceConfirmationDetailObj = formServiceConfirmationDetailObject(input, theDetail);

				try {
					try {
						// Validation : Service Request Id and Item Id combination exists in Spare Parts
						// Header table.
						theSparePartsHeadObj = session.getOmsSparePartHeaderByKeyComb(theDetail.getOmsServiceReqSeqId(), input.getServiceRequestId());
						// Update the OmsServiceRequestId in Confirmation Detail Object from the Spare
						// Parts Header Object.
						theServiceConfirmationDetailObj.setOmsServiceReqSeqId(theSparePartsHeadObj.getOmsServiceReqSeqId());

						SparePartsConfirmationValidator theValidator = new SparePartsConfirmationValidator();

						// Validation : If the requested confirmation qty is more than Pending qty then
						// throw error.
						log.info("  --> Performing Pending Quantity Check.");
						theValidator.isPendingQtyMoreThanConfirmationQty(theSparePartsHeadObj, theDetail);

						log.info("  --> Performing Cum Reserve Quantity Check.");
						// Validation : If Cumulative Reserve Qty is more than requested confirmation
						// qty is more than then throw error.
						theValidator.isCumReservedQtyMoreThanConfirmationQty(theSparePartsHeadObj, theDetail);

					} catch (SOAPException se) {
						log.info("-----000000");
						if (se.getMessage().equals("No Record")) {
							log.info("      --> NoResultException - No Spare Part Request for Req ID: " + input.getServiceRequestId() + " and Item " + theDetail.getItemID());

							throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_NOT_EXISTS, "1", new String[] { "Spare Part Request with Req ID: "
									+ input.getServiceRequestId() + "omsServiceReqId:" + theSparePartsHeadObj.getOmsServiceReqSeqId() + " and Item " + theDetail.getItemID() }));
						} else {
							log.error("     --> Unknown error while validating data." + se.getMessage());
							/*
							 * throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.
							 * ERR_UNHANDLED_EXCEPTION, "1", new String[] { }));
							 */
							throw new SOAPException(se.getMessage());
						}
					}

					// Updated by Santosh Suman
					// added New Parameter reasonCodeIdMap.
					updateSIMandSparePartsRequestTable(theSparePartsHeadObj, theServiceConfirmationDetailObj, input.getServiceRequestId(), input.getServiceConfirmationId(), reasonCodeIdMap);
				} catch (SOAPException se) {
					log.info("1");
					errorOccured = true;
					errMessage = se.getMessage();
				}
				log.info("***********");
				formServiceResponse(errMessage, theSparePartsHeadObj.getServiceRequestId(), theSparePartsHeadObj.getSequenceId(), theServiceConfirmationDetailObj);
			} // End For Loop
				// If any of the detail record has been errored out mark the status in response
				// header as 'Failed' else 'Success'
			theResponse.setStatus((errorOccured ? statusRejected : statusClosed));
			updateStatusInHeaderRecord(input, (errorOccured ? statusRejected : statusClosed));
			log.info("++++++++++++++++++");
		} catch (SOAPException se) {
			log.info("2");
			updateStatusInHeaderRecord(input, statusRejected);
			throw new SOAPException(se.getMessage());
		} catch (Exception e) {
			log.info("3" + e);
			updateStatusInHeaderRecord(input, statusRejected);
			log.error(" --> Error saving data into OMS_SPARE_PART_CONFIRM_DTL table. " + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
		return theResponse;
	}

	// Update By Santosh Suman
	public void updateSIMandSparePartsRequestTable(OmsSparePartHeader theSparePartsHeaderObj, OmsSparePartConfirmDtl theConfirmationDetailObj, String theServiceRequestId, String theConfirmationId,
			HashMap<String, String> reasonCodeIdMap) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		String reasonCode = null;
		String eventType = "CNF";
		SparePartsConfirmationProcessedObject theProcessedObj = new SparePartsConfirmationProcessedObject();
		SparePartsConfirmationReversalBean reversalBean = new SparePartsConfirmationReversalBean();
		try {
			// Add to the Processed obj, the quantity of the confirmation detail request.
			log.info("Inside try block ");
			theProcessedObj.setQuantityProcessed(theConfirmationDetailObj.getConfirmQty());
			theProcessedObj.setServiceRequestId(theSparePartsHeaderObj.getServiceRequestId());
			// Save the Service Confirmation Detail Object.

			theConfirmationDetailObj = session.persistOmsSparePartConfirmDtl(theConfirmationDetailObj);

			log.info("      --> Successfully created Service Detail info for Service Confirmation Id :" + theConfirmationDetailObj.getServiceConfirmId());

			// Flag the event in the Processed Obj.
			theProcessedObj.setSparePartsConfirmDtlObj(theConfirmationDetailObj);

			// Get the Response code for Reducing Inv Adjustment
			reasonCode = session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryUnReserveCode, OMSConstants.inventoryAdjReasonCode);
			log.info("------> Reason code as mentioned for reducing inventory :  " + reasonCode);
			// Call SIM Inv Adjustment WS to Reducing Inv Adjustment

			try {
				simOracleIntegrationSevices.adjustInventoryByItemLocation(theSparePartsHeaderObj.getItemId(), theSparePartsHeaderObj.getStoreId(), new BigDecimal(reasonCode),
						theConfirmationDetailObj.getConfirmQty(), theSparePartsHeaderObj.getServiceRequestId());
				log.info("      --> Item unreserved in SIM for qty " + theConfirmationDetailObj.getConfirmQty());
				// Flag the event
				theProcessedObj.setSimUnreserveProcessed(true);
			} catch (Exception e) {
				log.info("--------> Inserting into Republish Data Table  <----------");
				String XMLMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1\" "
						+ "xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/StrAdjModVo/v1\">\n" + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n"
						+ "      <v1:saveAndConfirmInventoryAdjustment>\n" + "         <!--Optional:-->\n" + "         <v11:StrAdjModVo>\n" + "            <v11:store_id>"
						+ theSparePartsHeaderObj.getStoreId() + "</v11:store_id>\n" + "            <v11:comments>" + theSparePartsHeaderObj.getServiceRequestId() + "</v11:comments>\n"
						+ "            <v11:StrAdjItmMod>\n" + "               <v11:item_id>" + theSparePartsHeaderObj.getItemId() + "</v11:item_id>\n" + "               <v11:reason_id>" + reasonCode
						+ "</v11:reason_id>\n" + "               <v11:quantity>" + theConfirmationDetailObj.getConfirmQty() + "</v11:quantity>\n" + "               <v11:case_size>1</v11:case_size>\n"
						+ "            </v11:StrAdjItmMod>\n" + "         </v11:StrAdjModVo>\n" + "      </v1:saveAndConfirmInventoryAdjustment>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId("SPARE");
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIM INVENTORY ADJUSTMENT WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(theSparePartsHeaderObj.getServiceRequestId());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIM_INVENTORY_ADJ");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				// theConfirmationDetailObj.getConfirmQty();
				omsRepublishData.setXmlMsg(XMLMsg);

				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisted in omsRepublish data");
			}

			// Get the Response code for Deducting Qunatity. Now the reason code is passed
			// by Siebel to OMS
			// reasonCode =
			// session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryDeductCode,
			// OMSConstants.inventoryAdjReasonCode);
			reasonCode = theConfirmationDetailObj.getReasonCode();
			log.info("Reason code as mentioned to deduct the inventory from confirmation request : " + reasonCode);

			// Added by Santosh Suman
			String reasonId = reasonCodeIdMap.get(theSparePartsHeaderObj.getOmsServiceReqSeqId());
			log.info("--> Test Map data " + theSparePartsHeaderObj.getOmsServiceReqSeqId() + " reasondId " + reasonId);

			// Call SIM Inv Adjustment WS to Deducting/Reducing Inventory
			try {
				// Updated By Santosh Suman
				simOracleIntegrationSevices.adjustInventoryByItemLocation(theSparePartsHeaderObj.getItemId(), theSparePartsHeaderObj.getStoreId(), new BigDecimal(reasonId),
						theConfirmationDetailObj.getConfirmQty(), theSparePartsHeaderObj.getServiceRequestId());
				log.info("--> Item deducted in SIM");
				// Flag the event
				theProcessedObj.setSimDeductProcessed(true);
			} catch (Exception e) {
				log.info("--------> Inserting into Republish Data Table  <----------");
				String XMLMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1\" "
						+ "xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/StrAdjModVo/v1\">\n" + "   <soapenv:Header/>\n" + "   <soapenv:Body>\n"
						+ "      <v1:saveAndConfirmInventoryAdjustment>\n" + "         <!--Optional:-->\n" + "         <v11:StrAdjModVo>\n" + "            <v11:store_id>"
						+ theSparePartsHeaderObj.getStoreId() + "</v11:store_id>\n" + "            <v11:comments>" + theSparePartsHeaderObj.getServiceRequestId() + "</v11:comments>\n"
						+ "            <v11:StrAdjItmMod>\n" + "               <v11:item_id>" + theSparePartsHeaderObj.getItemId() + "</v11:item_id>\n" + "               <v11:reason_id>" + reasonId
						+ "</v11:reason_id>\n" + "               <v11:quantity>" + theConfirmationDetailObj.getConfirmQty() + "</v11:quantity>\n" + "               <v11:case_size>1</v11:case_size>\n"
						+ "            </v11:StrAdjItmMod>\n" + "         </v11:StrAdjModVo>\n" + "      </v1:saveAndConfirmInventoryAdjustment>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>";

				OmsRepublishData omsRepublishData = new OmsRepublishData();
				omsRepublishData.setApplicationId("SPARE");
				omsRepublishData.setErrorMsg("UNABLE TO CALL SIM INVENTORY ADJUSTMENT WEB SERVICE.");
				omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
				omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
				omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
				omsRepublishData.setRepublishStatus("F");
				omsRepublishData.setTransactionKey(theSparePartsHeaderObj.getServiceRequestId());
				BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("SIM_INVENTORY_ADJ");
				omsRepublishData.setWebServiceId(webserviceid.toString());
				// theConfirmationDetailObj.getConfirmQty();
				omsRepublishData.setXmlMsg(XMLMsg);

				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisted in omsRepublish data");
			}

			// if(testing)
			// throw new SOAPException("Test Reversal");
		} catch (Exception e) {
			log.error(e);
			log.error("       --> Error in SIM update. Reason Code " + reasonCode + ". " + e.getMessage());
			log.info("     --> Starting transaction reversals.");
			String errorCode, errString;
			if (e.getMessage().contains("VALIDATION_ERROR"))
				errorCode = OMSConstants.ERR_INVALID_INPUT;
			else
				errorCode = OMSConstants.ERR_UNHANDLED_EXCEPTION;

			errString = "Item " + theSparePartsHeaderObj.getItemId() + " OR Reason Code " + reasonCode;
			// successfullyProcessedObjList.add(theProcessedObj);
			// If any error in processing the detailed records then reverse the
			// transactions.
			reversalBean.reverseSuccessfulTransactions(theProcessedObj, theConfirmationDetailObj, theServiceRequestId, theConfirmationId);
			throw new SOAPException(OMSUtilCommons.formErrorDescription(errorCode, "1", new String[] { errString }));

		}

		// ============================================================================================================

		try {

			// Update OMS_SPARE_PART_FULFILL
			List<OmsSparePartFulfill> theFulfillRecs = session.getOmsSparePartFulfillFindByRequestAndItem(theSparePartsHeaderObj.getOmsServiceReqSeqId(), theSparePartsHeaderObj.getItemId());
			BigDecimal maxConfirmableQty;
			BigDecimal pendingFulfillmentQty = new BigDecimal(0);
			SparePartsFulfillProcessedObject theFulfillProcessedObj;
			BigDecimal qtyUnreserved, qtyCancelled;

			for (OmsSparePartFulfill theFulfillmentRec : theFulfillRecs) {
				theFulfillProcessedObj = new SparePartsFulfillProcessedObject();
				// In the Spare Part confirmation process only the Requesting store data needs
				// to be processed. So Skip the other locations.
				if (theFulfillmentRec.getSourceLocation().longValue() != theSparePartsHeaderObj.getStoreId().longValue())
					continue;

				qtyUnreserved = (null == theFulfillmentRec.getQuantityUnreserved() ? new BigDecimal(0) : theFulfillmentRec.getQuantityUnreserved());
				qtyCancelled = (null == theFulfillmentRec.getQuantityCancelled() ? new BigDecimal(0) : theFulfillmentRec.getQuantityCancelled());

				// Check the 'confirmable' quantity on each of the records.
				maxConfirmableQty = (theFulfillmentRec.getQuantityReserved().subtract(qtyCancelled)).subtract(qtyUnreserved);
				if (maxConfirmableQty.compareTo(new BigDecimal(0)) <= 0)
					continue;

				theFulfillmentRec = setSparePartFulfillQuantity(theFulfillmentRec, maxConfirmableQty);
				session.mergeOmsSparePartFulfill(theFulfillmentRec);
				log.info("  --> Updated Spare Part Fulfill for location " + theFulfillmentRec.getSourceLocation() + " with quantity " + maxConfirmableQty);
				theFulfillProcessedObj.setSparePartsFulfillRec(theFulfillmentRec);
				theFulfillProcessedObj.setQuantityConfirmed(maxConfirmableQty);

				pendingFulfillmentQty = theConfirmationDetailObj.getConfirmQty().subtract(maxConfirmableQty);

				theProcessedObj.getProcessedFulfilledRecs().add(theFulfillProcessedObj);
				// Flag the event.
				theProcessedObj.setFulfillmentProcessed(true);

			} // End looping

			if (pendingFulfillmentQty.compareTo(new BigDecimal(0)) > 0) {
				log.warn("      --> Warning : " + pendingFulfillmentQty + " Quantity still pending for confirmation for Service Request Id " + theSparePartsHeaderObj.getServiceRequestId()
						+ " and item id " + theSparePartsHeaderObj.getItemId() + " from store " + theSparePartsHeaderObj.getStoreId());
			}

			// Update OMS_SPARE_PART_HDR
			theSparePartsHeaderObj = setSparePartHeaderQuantity(theSparePartsHeaderObj, theConfirmationDetailObj.getConfirmQty());
			session.mergeOmsSparePartHeader(theSparePartsHeaderObj);
			log.info("  --> Updated Spare Part Header Cumulative Quantity");
			// Flag the event.
			theProcessedObj.setSpHeaderProcessed(true);

			// Step : Save the values into the SPARE_PART_AUDIT table
			createSparePartsAuditEntry(theConfirmationDetailObj.getItem(), eventType, theConfirmationDetailObj.getConfirmQty(), theSparePartsHeaderObj.getStoreId(),
					theSparePartsHeaderObj.getOmsServiceReqSeqId());

			// Flag the event.
			theProcessedObj.setAuditProcessed(true);
			// successfullyProcessedObjList.add(theProcessedObj);

		} catch (Exception e) {
			log.error("       --> Error during update of SPARE PART HEADER OR FULFILL TABLE." + e.getMessage());
			log.info("     --> Starting transaction reversals.");
			// successfullyProcessedObjList.add(theProcessedObj);
			// If any error in processing the detailed records then reverse the
			// transactions.
			reversalBean.reverseSuccessfulTransactions(theProcessedObj, theConfirmationDetailObj, theServiceRequestId, theConfirmationId);
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}

	}
	// Step 5: Save the values into the SPARE_PART_AUDIT table

	public void createSparePartsAuditEntry(String item, String eventType, BigDecimal quantity, BigDecimal sourceLocation, String omsSparePartsServiceId) throws SOAPException {
		log.info("  -->Saving into OMS_SPARE_PART_AUDIT table ");
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsSparePartAudit theOmsSparePartAudit = formOmsSparePartAuditObject(item, eventType, quantity, sourceLocation, omsSparePartsServiceId);
			log.debug(" --> Inserting into OMS_SPARE_PART_AUDIT table : " + theOmsSparePartAudit.toString());
			session.persistOmsSparePartAudit(theOmsSparePartAudit);
			log.info("  --> Successfully created Spare Parts Audit data for Id : " + omsSparePartsServiceId);
		} catch (Exception e) {

			log.error("ERROR while inserting record into OMS_SPARE_PART_AUDIT :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
	}

	public OmsSparePartHeader setSparePartHeaderQuantity(OmsSparePartHeader theSPHeaderObj, BigDecimal qtyConfirmed) {

		BigDecimal cumQtyUnreserved, cumQtyDeducted;

		cumQtyUnreserved = (null == theSPHeaderObj.getCumQuantityUnreserved() ? new BigDecimal(0) : theSPHeaderObj.getCumQuantityUnreserved());
		cumQtyDeducted = (null == theSPHeaderObj.getCumQuantityDeducted() ? new BigDecimal(0) : theSPHeaderObj.getCumQuantityDeducted());

		theSPHeaderObj.setPendingQty(theSPHeaderObj.getPendingQty().subtract(qtyConfirmed));
		theSPHeaderObj.setCumQuantityUnreserved(cumQtyUnreserved.add(qtyConfirmed));
		theSPHeaderObj.setCumQuantityDeducted(cumQtyDeducted.add(qtyConfirmed));
		if (theSPHeaderObj.getPendingQty().compareTo(new BigDecimal(0)) == 0)
			theSPHeaderObj.setStatus(statusClosed);

		theSPHeaderObj.setUpdatedBy(webServiceName);
		theSPHeaderObj.setLastUpdateDatetime(new Timestamp((new Date()).getTime()));
		return theSPHeaderObj;

	}

	public OmsSparePartFulfill setSparePartFulfillQuantity(OmsSparePartFulfill theSPFulfillRec, BigDecimal qtyConfirmed) {

		BigDecimal qtyUnreserved, qtyDeducted;

		qtyUnreserved = (null == theSPFulfillRec.getQuantityUnreserved() ? new BigDecimal(0) : theSPFulfillRec.getQuantityUnreserved());
		qtyDeducted = (null == theSPFulfillRec.getQuantityDeducted() ? new BigDecimal(0) : theSPFulfillRec.getQuantityDeducted());

		theSPFulfillRec.setQuantityUnreserved(qtyUnreserved.add(qtyConfirmed));
		theSPFulfillRec.setQuantityDeducted(qtyDeducted.add(qtyConfirmed));
		theSPFulfillRec.setUpdatedBy(webServiceName);
		theSPFulfillRec.setLastUpdatedDatetime(new Timestamp((new Date()).getTime()));
		return theSPFulfillRec;
	}

	// Create a response and send it back to the invoker

	public SparePartsConfirmationResponse formServiceResponse(String errMessage, String serviceReqId, String seqId, OmsSparePartConfirmDtl theServiceConfirmationDetailObj) {

		log.info("inside formService Response");
		OmsErrorCodes theErrorObj = new OmsErrorCodes();
		theResponse.setServiceRequestId(serviceReqId);
		theResponse.setStatus("RJ");
		log.info("Staus is set");
		log.info("errMessage=" + errMessage);
		if (null != errMessage) {
			theErrorObj = OMSUtil.parseErrorString(errMessage);
		} else {
			theErrorObj.setOmsErrorCode("");
			theErrorObj.setOmsErrLangDesc("");
		}

		ServiceConfDetailsResponseType theResponseDetails = new ServiceConfDetailsResponseType();

		theResponseDetails.setMsgCode(theErrorObj.getOmsErrorCode());
		theResponseDetails.setMsgDesc(theErrorObj.getOmsErrLangDesc());
		if (null != theServiceConfirmationDetailObj) {
			theResponseDetails.setSequenceId(seqId);
			theResponseDetails.setOMSServiceId(new Long(theServiceConfirmationDetailObj.getOmsServiceReqSeqId()).longValue());
			theResponseDetails.setReasonCode(theServiceConfirmationDetailObj.getReasonCode());
			theResponseDetails.setItemID(theServiceConfirmationDetailObj.getItem());
		}

		theResponse.getSparePartsConfirmationResponseDetail().add(theResponseDetails);

		log.info("  --> Response Successfully Created.");
		return theResponse;
	}

	public SparePartsConfirmationResponse getServiceResponse(SparePartsConfirmation sparePartsConfirmation, String errMessage) {
		log.info("inside getServiceResponse");

		if (theResponse.getServiceRequestId() == null || theResponse.getStatus() == null) {
			log.info("response is null");
			OmsErrorCodes theErrorObj = new OmsErrorCodes();
			theResponse.setServiceRequestId(sparePartsConfirmation.getServiceRequestId());
			theResponse.setStatus("RJ");
			log.info("Staus is set");
			log.info("errMessage=" + errMessage);
			if (null != errMessage) {
				theErrorObj = OMSUtil.parseErrorString(errMessage);
			} else {
				theErrorObj.setOmsErrorCode("");
				theErrorObj.setOmsErrLangDesc("");
			}

			ServiceConfDetailsResponseType theResponseDetails = new ServiceConfDetailsResponseType();

			theResponseDetails.setMsgCode(theErrorObj.getOmsErrorCode());
			theResponseDetails.setMsgDesc(theErrorObj.getOmsErrLangDesc());
			/*
			 * if (null != theServiceConfirmationDetailObj) {
			 * theResponseDetails.setSequenceId(new Long(seqId.longValue()));
			 * theResponseDetails.setOMSServiceId(new
			 * Long(theServiceConfirmationDetailObj.getOmsServiceReqSeqId()).longValue());
			 * theResponseDetails.setReasonCode(theServiceConfirmationDetailObj.
			 * getReasonCode());
			 * theResponseDetails.setItemID(theServiceConfirmationDetailObj.getItem()); }
			 */

			theResponse.getSparePartsConfirmationResponseDetail().add(theResponseDetails);

		}
		return theResponse;
	}

	private OmsSparePartConfirmHdr formServiceConfirmationHeaderObject(SparePartsConfirmation inputData) {

		OmsSparePartConfirmHdr theServiceConfirmHeadObj = new OmsSparePartConfirmHdr();

		// Service Confirmation Header Info
		theServiceConfirmHeadObj.setServiceRequestId(inputData.getServiceRequestId());
		theServiceConfirmHeadObj.setServiceConfirmId(inputData.getServiceConfirmationId());
		theServiceConfirmHeadObj.setCreateDatetime(new Timestamp(new Date().getTime()));
		theServiceConfirmHeadObj.setCreatedBy(webServiceName);
		return theServiceConfirmHeadObj;
	}

	private void updateStatusInHeaderRecord(SparePartsConfirmation input, String status) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsSparePartConfirmHdr theHeaderObj = session.getOmsSparePartConfirmHdrFindByKey(input.getServiceRequestId(), input.getServiceConfirmationId());
		theHeaderObj.setStatus(status);
		session.mergeOmsSparePartConfirmHdr(theHeaderObj);
	}

	private OmsSparePartConfirmDtl formServiceConfirmationDetailObject(SparePartsConfirmation inputData, ServiceConfirmationDetailType theDetailData) {

		OmsSparePartConfirmDtl theServiceConfirmDetailObj = new OmsSparePartConfirmDtl();

		// Service Confirmation Detail Info
		theServiceConfirmDetailObj.setConfirmQty(theDetailData.getQuantity());
		theServiceConfirmDetailObj.setItem(theDetailData.getItemID());
		theServiceConfirmDetailObj.setServiceConfirmId(inputData.getServiceConfirmationId());
		theServiceConfirmDetailObj.setReasonCode(theDetailData.getReasonCode());
		theServiceConfirmDetailObj.setOmsServiceReqSeqId(theDetailData.getOmsServiceReqSeqId());
		// theServiceConfirmDetailObj.setOmsServiceReqSeqId(omsServiceRequestId);
		return theServiceConfirmDetailObj;
	}

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

	/**
	 * Added By Santosh Suman
	 * 
	 * Validate the reason code check the given reason code is presnt in the table
	 * or not. Also return the reason code id to the caller method. return type
	 * HashMap
	 */
	public HashMap<String, String> validateGetReasonCodeId(SparePartsConfirmation sparePartsConfirmation) throws IllegalArgumentException, SOAPException {
		log.info("Validate reason code");
		Connection connection = null;
		PreparedStatement prepStatement = null;
		ResultSet rs = null;
		String reasonCode = null;
		String reasonId = null;
		HashMap<String, String> reasonCodeMap = null;

		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("connection of reason code : " + connection);
			reasonCodeMap = new HashMap<String, String>();
			String getQuery = "SELECT id FROM INV_ADJUST_REASON WHERE code = ? ";
			prepStatement = connection.prepareStatement(getQuery);
			List<ServiceConfirmationDetailType> serviceConfirmationDetail = sparePartsConfirmation.getServiceConfirmationDetail();
			log.info("reason code : " + serviceConfirmationDetail.get(0).getReasonCode());

			for (int j = 0; j < serviceConfirmationDetail.size(); ++j) {
				reasonCode = serviceConfirmationDetail.get(j).getReasonCode();
				prepStatement.setString(1, reasonCode);
				rs = prepStatement.executeQuery();
				if (rs.next()) {
					reasonId = rs.getString("id");
					reasonCodeMap.put(serviceConfirmationDetail.get(j).getOmsServiceReqSeqId(), reasonId);
					log.info(serviceConfirmationDetail.size() + " Reason code: " + reasonCode + " Reason id: " + reasonId);
				} else {
					log.info("Cound not find reason id for given Reason code: " + reasonCode);
					throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_NOT_EXISTS, "1", new String[] { "Reason Code: " + reasonCode }));
				}
			}
		} catch (SOAPException se) {
			throw new SOAPException(se.getMessage());

		} catch (Exception e) {
			log.info("Issue while validating reason code " + e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(connection, prepStatement, rs);
				prepStatement.close();
				rs.close();
				connection.close();
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return reasonCodeMap;
	}
	/***/

}
