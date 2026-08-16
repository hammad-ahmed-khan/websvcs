package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.dao.SparePartDAO;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsSparePartAudit;
import com.logicinfo.oms.ejb.OmsSparePartCancelHdr;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;
import com.logicinfo.oms.model.SparePartsCancelRequest;
import com.logicinfo.oms.model.SparePartsCancelResponse;
import com.logicinfo.oms.model.SparePartsCancellationProcessedFulfillmentObjects;
import com.logicinfo.oms.model.SparePartsCancellationProcessedObjects;
import com.logicinfo.oms.util.NumberUtil;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfCriStatus;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfHdrCriVo;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferService;

public class SparePartsCancelBean extends SparePartsCancelHelperBean {

	private final static Logger log = Logger.getLogger(SparePartsCancelBean.class.getName());

	String webServiceName = "Spare Parts Cancel WebService";
	String transTypeTransfer = "TSF";
	String transTypeAdjustment = "ADJ";
	String cancelTsfEventType = "CANTSF";
	String cancelAdjEventType = "CANADJ";

	OracleIntegrationSevicesSIM simOracleIntegrationSevices;
	SparePartsCancellationProcessedObjects theProcessedObjects;

	private SparePartDAO sparePartDAO = null;

	public SparePartsCancelBean() {
		super();
		simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();
		theProcessedObjects = new SparePartsCancellationProcessedObjects();
		sparePartDAO = new SparePartDAO();
	}

	// This method checks if the input request is already existing with OMS
	// If the new incoming request already exists in the OMS_SPARE_PART_HEADER table
	// then return ERROR.
	// Logic : If the service_request_id and the sequence_id from the input request
	// already exists
	// in the OMS_SPARE_PART_HEADER table, then return ERROR

	public OmsSparePartHeader performBasicValidation(SparePartsCancelRequest input) throws IllegalArgumentException, SOAPException {
		SparePartsCancellationValidator theValidator = new SparePartsCancellationValidator();
		return theValidator.validateCancellationData(input);
	}

	// Step 2: Insert a new record into OMS_SPARE_PART_HEADER based on the data from
	// the input.

	public OmsSparePartCancelHdr createSparePartsCancellationHeaderEntry(SparePartsCancelRequest input) throws SOAPException {
		log.info("  --> Start-Save Spare Parts Header Entry.");
		OmsSparePartCancelHdr theSparePartsCancelRequest;
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			theSparePartsCancelRequest = formOmsSparePartsCancelObject(input);
			log.info("  --> Inserting into OMS_SPARE_PART_CANCEL_HDR table.");
			theSparePartsCancelRequest = session.persistOmsSparePartCancelHdr(theSparePartsCancelRequest);

			log.info("  --> Successfully inserted data into Spare Parts Cancel Header");
		} catch (Exception e) {
			log.error("ERROR while inserting record into OMS_SPARE_PART_CANCEL_HDR :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
		return theSparePartsCancelRequest;
	}

	/**
	 * Step 3: Process the spare parts fulfillment request
	 */
	public void processSparePartsCancellationRequest(SparePartsCancelRequest input, OmsSparePartHeader sparePartHeaderObj) throws SOAPException {
		BigDecimal requestedCancelQty = input.getQuantity();
		BigDecimal unProcessedCancelQty = requestedCancelQty;
		theProcessedObjects.setSparePartsHeaderObj(sparePartHeaderObj);

		// If it transfer the call this piece of code
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<OmsSparePartFulfill> theSparePartFulfillmentDetails = session.getOmsSparePartFulfillFindByRequestAndItem(sparePartHeaderObj.getOmsServiceReqSeqId(), sparePartHeaderObj.getItemId());
		if (sparePartHeaderObj.getCumQuantityTsfReserved().floatValue() > 0 && unProcessedCancelQty.floatValue() > 0) {
			// If status is IT (transfer) the call this piece of code
			unProcessedCancelQty = processFulfillmentOperations(unProcessedCancelQty, sparePartHeaderObj, input, theSparePartFulfillmentDetails, transTypeTransfer, session);
		}

		if (sparePartHeaderObj.getCumQuantityReserved().floatValue() > 0 && unProcessedCancelQty.floatValue() > 0) {
			
			unProcessedCancelQty = processFulfillmentOperations(unProcessedCancelQty, sparePartHeaderObj, input, theSparePartFulfillmentDetails, transTypeAdjustment, session);
		}

	}

	public BigDecimal processFulfillmentOperations(BigDecimal unProcessedCancelQty, OmsSparePartHeader sparePartHeaderObj, SparePartsCancelRequest input, List<OmsSparePartFulfill> theSparePartFulfillmentDetails, String transactionType, OMSUtilSessionEJB session) throws SOAPException {
		SparePartsCancellationProcessedFulfillmentObjects theProcessedFulfillments = null;
		BigDecimal maxCancellableLocationQty;
		BigDecimal cancelLocationQty;
		String itemId = sparePartHeaderObj.getItemId();
		String theReasonCode = null;
		String eventType = null;
		BigDecimal balanceQtyTsfReserved;
		BigDecimal transferId;
		SparePartsCancellationReversalBean sparePartsCancellationReversalBean = new SparePartsCancellationReversalBean();
		try {
			long simTransactionId = 0;

			for (OmsSparePartFulfill theSparePartFulfill : theSparePartFulfillmentDetails) {
				theProcessedFulfillments = null;
				balanceQtyTsfReserved = new BigDecimal(0);
				transferId = null;

				// If the unprocessed Cancel Qty DOES NOT have left over (<=0).
				if (unProcessedCancelQty.floatValue() <= 0) {
					log.info("  --> All cancel requested quantity has been completely applied.");
					break;
				}

				if (transTypeTransfer.equals(transactionType) && (theSparePartFulfill.getSourceLocation().floatValue() == sparePartHeaderObj.getStoreId().floatValue())) {
					continue;
				}

				// status is IP
				if (transTypeAdjustment.equals(transactionType) && (theSparePartFulfill.getSourceLocation().floatValue() != sparePartHeaderObj.getStoreId().floatValue()))
					continue;

				// BigDecimal sourceLocation = new BigDecimal(16006);
				// theSparePartFulfill.setSourceLocation(sourceLocation);

				log.info("  --> Processing Location :" + theSparePartFulfill.getSourceLocation() + " Transfer Reserves Qty :" + theSparePartFulfill.getQuantityTsfReserved() + " Shipped Qty :"
						+ theSparePartFulfill.getQuantityShipped() + " Cancelled Qty :" + theSparePartFulfill.getQuantityCancelled() + "  Reserved Qty :" + theSparePartFulfill.getQuantityReserved()
						+ " Unreserved Qty :" + theSparePartFulfill.getQuantityUnreserved());

				if (transTypeTransfer.equals(transactionType))
					maxCancellableLocationQty = (theSparePartFulfill.getQuantityTsfReserved()
							.subtract(null == theSparePartFulfill.getQuantityCancelled() ? new BigDecimal(0) : theSparePartFulfill.getQuantityCancelled()));
				else
					maxCancellableLocationQty = (theSparePartFulfill.getQuantityReserved()
							.subtract(null == theSparePartFulfill.getQuantityUnreserved() ? new BigDecimal(0) : theSparePartFulfill.getQuantityUnreserved()));

				// If there is no cancellable location quantity the skip processing to next
				// location
				if (maxCancellableLocationQty.floatValue() <= 0)
					continue;

				cancelLocationQty = unProcessedCancelQty.min(maxCancellableLocationQty);
				log.info("      --> Unprocessed Cancel Qty :" + unProcessedCancelQty + " Max Cancellable Qty " + maxCancellableLocationQty + " ==> Cancel Location Qty :" + cancelLocationQty);

				theProcessedFulfillments = new SparePartsCancellationProcessedFulfillmentObjects();
				theProcessedFulfillments.setSparePartsFulfillObj(theSparePartFulfill);
				theProcessedFulfillments.setQuantityProcessed(cancelLocationQty);

				// Check if the transaction type is Transfer and the quantity (partial or full)
				// has been shipped.
				if (transTypeTransfer.equals(transactionType)) {
					eventType = cancelTsfEventType;
					// OMSConstants.QUANTITY_TRANSFERRED;
					// If shipment has been already initiated then just reduce the entire
					// fulfillment quantity from the pending quantity on the header table by the
					// shipped qty.

					if (NumberUtil.defaultIfNull(theSparePartFulfill.getQuantityReceived()).add(NumberUtil.defaultIfNull(theSparePartFulfill.getQuantityCancelled())).subtract(theSparePartFulfill.getQuantityTsfReserved()).intValue() >= 0 ) {
						/*
						 * If all requested items are received or cancelled then ignore this fulfillment process.
						 */
						continue;
					} else if (theSparePartFulfill.getQuantityShipped().floatValue() > 0) {
						log.info("--> Some items shipped for transfer requests. Reducing pending quantity only.");

						theSparePartFulfill = updateSparePartFulfillData(theSparePartFulfill, cancelLocationQty, transactionType, false, true);

						// Update Spare Parts Fulfill Table
						theSparePartFulfill = session.mergeOmsSparePartFulfill(theSparePartFulfill);
						theProcessedFulfillments.setFulfillmentProcessed(true);
						log.info("--> Spare Part Fulfill data updated for cancellation quantity.");


						log.info("Unprocessed Cancel Qty : " + unProcessedCancelQty);
						sparePartHeaderObj = updateSparePartHeaderData(sparePartHeaderObj, cancelLocationQty, transactionType, unProcessedCancelQty, true);
						session.mergeOmsSparePartHeader(sparePartHeaderObj);

						theProcessedFulfillments.setHeaderProcessed(true);
						// Step : Save the values into the SPARE_PART_AUDIT table
						// createSparePartsAuditEntry(sparePartHeaderObj.getItemId(), eventType,
						// cancelLocationQty,
						// theSparePartFulfill.getSourceLocation(),
						// theSparePartFulfill.getOmsServiceReqSeqId());

						// Calculate the unProcessedCancelQty remaining.
						unProcessedCancelQty = unProcessedCancelQty.subtract(cancelLocationQty);
						log.info("--> Unprocessed Cancel Qty =" + unProcessedCancelQty);
						// Add the processed fulfillment obje to the Processed Object.
						// theProcessedObjects.getProcessedFulfillObj().add(theProcessedFulfillments);
						/*
						 * 
						 * Initiate the transfer reversal request
						 */
						sparePartDAO.saveTransferReversalRequest(input, sparePartHeaderObj.getItemId(), sparePartHeaderObj.getStoreId(), cancelLocationQty);
						// Proceed to process the next record
						continue;
					} else {
						log.info(" --> No items shipped yet for transfer requests. Proceeding with cancellations.");
					}
				}

				log.info("--> Sending " + transactionType + " cancellation details to SIM for processing.");

				if (transTypeTransfer.equals(transactionType)) {
					try {
						log.info("Transfer Store Id : " + theSparePartFulfill.getSourceLocation());
						log.info("Transfer Id : " + theSparePartFulfill.getTranId());
						StsTsfHdrColDesc stsTsfHdrColDesc = callSimLookupTransferHeader(theSparePartFulfill.getSourceLocation().longValue(), theSparePartFulfill.getTranId().longValue());

						if (stsTsfHdrColDesc.getStsTsfHdrDesc().get(0).getStatus().equals(com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1.StsTsfStatus.CANCELED_TRANSFER)) {
							log.info("The transfer request has already been cancelled");
						}

						else {
							// Cancel the existing transfer request. This will cancel the entire qty
							// requested and the SIM cancel ws
							// does not take any qty as parameter.
							try {
								simTransactionId = simOracleIntegrationSevices.cancelTransferRequestStore2Store(sparePartHeaderObj.getStoreId(), theSparePartFulfill.getTranId());

								log.info("      --> Sim Transfer Intiated for cancel : Req Id " + simTransactionId);
							} catch (Exception e) {
								log.info("Exception e" + e.getMessage());
							}

							// Determine the balance quantity to transfer after cancellation from the store.
							balanceQtyTsfReserved = theSparePartFulfill.getQuantityTsfReserved().subtract(cancelLocationQty);

							eventType = cancelTsfEventType; // OMSConstants.QUANTITY_TRANSFERRED;
						}
					} catch (Exception e) {
						log.info("Exception occured : " + e.getMessage());
					}
				} else if (transTypeAdjustment.equals(transactionType)) {
					// Step : Get reason code from System Parameters table
					theReasonCode = session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryUnReserveCode, OMSConstants.inventoryAdjReasonCode);

					simTransactionId = simOracleIntegrationSevices.adjustInventoryByItemLocation(itemId, sparePartHeaderObj.getStoreId(), new BigDecimal(theReasonCode), cancelLocationQty,
							input.getServiceRequestId());
					
					String techSubBucket = theSparePartFulfill.getSubBucket();
					if (techSubBucket != null && theSparePartFulfill.getSubBucketQty() != null && theSparePartFulfill.getSubBucketQty().intValue() != 0) {
						// Reverting from available to tech sub bucket
						try {
							BigDecimal reversTecgBkQty = cancelLocationQty.min(theSparePartFulfill.getSubBucketQty());
							simOracleIntegrationSevices.adjustInventoryByItemLocation(itemId, theSparePartFulfill.getSourceLocation(), getSubBucketReasonCode(techSubBucket), reversTecgBkQty,
								input.getServiceRequestId());
							theSparePartFulfill.setSubBucketQty(theSparePartFulfill.getSubBucketQty().subtract(reversTecgBkQty));
						} catch (Exception e) {
							log.warn("Error while Reverting from available to tech sub bucket " + techSubBucket + " for request id " + input.getServiceRequestId(), e);
						}
					}
					log.info("      --> Sim Inv Adjustment Intiated for cancel : Req Id " + simTransactionId);
					eventType = cancelAdjEventType; // OMSConstants.QUANTITY_UNRESERVED;
					/*
					 * 
					 * Initiate the transfer reversal request to original location
					 */
					if (theSparePartFulfill.getTranType().equals("RSV")) {						
						sparePartDAO.saveTransferReversalRequest(input, sparePartHeaderObj.getItemId(), sparePartHeaderObj.getStoreId(), cancelLocationQty);
					}
				}

				theProcessedFulfillments.setSimProcessed(true);

				theSparePartFulfill = updateSparePartFulfillData(theSparePartFulfill, cancelLocationQty, transactionType, false, false);

				// Update Spare Parts Fulfill Table
				theSparePartFulfill = session.mergeOmsSparePartFulfill(theSparePartFulfill);
				theProcessedFulfillments.setFulfillmentProcessed(true);
				log.info("      --> Spare Part Fulfill data updated for cancellation quantity.");

				if (balanceQtyTsfReserved.floatValue() > 0) {
					// Create a new transfer request for the remaining amount.
					// Step : Use SIM StoreToStoreTransfer webservice

					long simTransferReqId = simOracleIntegrationSevices.saveTransferRequestStore2Store(itemId, theSparePartFulfill.getSourceLocation(), sparePartHeaderObj.getStoreId(),
							balanceQtyTsfReserved, input.getServiceRequestId());
					transferId = new BigDecimal(simTransferReqId);

					eventType = OMSConstants.QUANTITY_TRANSFERRED;
					log.info("  --> Transfer Id " + transferId.toString() + " created for Balance Qty " + balanceQtyTsfReserved + " from Store " + theSparePartFulfill.getSourceLocation());
					OmsSparePartFulfill newSparePartFulfill = new OmsSparePartFulfill();
					// Creating a new entry in Spare Parts Fulfill table.
					newSparePartFulfill = theSparePartFulfill;
					newSparePartFulfill.setTranId(transferId);
					newSparePartFulfill = updateSparePartFulfillData(newSparePartFulfill, balanceQtyTsfReserved, transactionType, true, false);
					session.persistOmsSparePartFulfill(newSparePartFulfill);

					log.info("  --> Created Spare Parts Fulfill Entry with Transaction Id " + newSparePartFulfill.getTranId() + " of type " + newSparePartFulfill.getTranType() + " for balance qty "
							+ balanceQtyTsfReserved);

					// Step : Save the values into the SPARE_PART_AUDIT table
					createSparePartsAuditEntry(sparePartHeaderObj.getItemId(), eventType, balanceQtyTsfReserved, newSparePartFulfill.getSourceLocation(), newSparePartFulfill.getOmsServiceReqSeqId());

				}
				// Calculate the unProcessedCancelQty remaining.
				unProcessedCancelQty = unProcessedCancelQty.subtract(cancelLocationQty);

				// Form the data to update Spare Parts Header Table
				sparePartHeaderObj = updateSparePartHeaderData(sparePartHeaderObj, cancelLocationQty, transactionType, unProcessedCancelQty, false);

				// Update Spare Parts Header Table
				sparePartHeaderObj = session.mergeOmsSparePartHeader(sparePartHeaderObj);
				theProcessedFulfillments.setHeaderProcessed(true);

				log.info("      --> Spare Part Header information updated for cancellation quantity. with unProcessedCancelQty=" + unProcessedCancelQty);

				// Step : Save the values into the SPARE_PART_AUDIT table
				createSparePartsAuditEntry(sparePartHeaderObj.getItemId(), eventType, cancelLocationQty, theSparePartFulfill.getSourceLocation(), theSparePartFulfill.getOmsServiceReqSeqId());

				// Add the processed fulfillment obje to the Processed Object.
				theProcessedObjects.getProcessedFulfillObj().add(theProcessedFulfillments);

				log.info("      --> Unprocessed Cancel Qty after " + transactionType + " : " + unProcessedCancelQty);

			} // End Loop

			return unProcessedCancelQty;
		} catch (SOAPException soape) {
			log.error("     --> Error while processing detailed records. " + soape.getMessage());
			log.info("     --> Starting transaction reversals.");

			if (null != theProcessedFulfillments) {
				theProcessedObjects.setSparePartsHeaderObj(sparePartHeaderObj);
				theProcessedObjects.getProcessedFulfillObj().add(theProcessedFulfillments);
			}
			// If any error in processing the detailed records then reverse the
			// transactions.
			sparePartsCancellationReversalBean.reverseSuccessfulTransactions(theProcessedObjects, input);

			throw soape;
		}

	}
	
	private BigDecimal getSubBucketReasonCode(String subBucket) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			statement = connection.prepareStatement("SELECT AVAIL_TO_TECH FROM XX_TECH_REAS_CODE_V WHERE DESCRIPTION = ? ");
			statement.setString(1, subBucket);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
		} catch (Exception e) {
			log.info("Error while loading the sub bucket availablity", e);
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return null;
	}

	private OmsSparePartFulfill updateSparePartFulfillData(OmsSparePartFulfill theSparePartFulfill, BigDecimal locationQty, String transactionType, boolean createTransfer, boolean cancelPartialTSF) {

		if (transTypeTransfer.equals(transactionType)) {
			if (createTransfer) {
				log.info(" updateSparePartFulfillData createTransfer : ");
				// When creating a new transfer, as a result of cancellation, the new transfer
				// record will be created in
				// the fulfill table where the transfer qty is the balance qty after
				// cancellation.
				// The new fulfill record will not have any cancellation qty.
				theSparePartFulfill.setQuantityCancelled(new BigDecimal(0));
				theSparePartFulfill.setQuantityTsfReserved(locationQty);
			} else {
				log.info(" updateSparePartFulfillData createTransfer else : ");
				// When cancelling the spare part request, the original transfer request qty
				// will entirely be cancelled.
				theSparePartFulfill.setQuantityCancelled(cancelPartialTSF ? locationQty : theSparePartFulfill.getQuantityTsfReserved());
			}
		}

		if (transTypeAdjustment.equals(transactionType)) {
			theSparePartFulfill.setQuantityUnreserved((null == theSparePartFulfill.getQuantityUnreserved() ? new BigDecimal(0) : theSparePartFulfill.getQuantityUnreserved()).add(locationQty));
			theSparePartFulfill.setQuantityCancelled((null == theSparePartFulfill.getQuantityCancelled() ? new BigDecimal(0) : theSparePartFulfill.getQuantityCancelled()).add(locationQty));
		}
		theSparePartFulfill.setUpdatedBy(webServiceName);
		theSparePartFulfill.setLastUpdatedDatetime(new Timestamp((new Date()).getTime()));
		return theSparePartFulfill;
	}

	public OmsSparePartHeader updateSparePartHeaderData(OmsSparePartHeader sparePartsHeaderObj, BigDecimal cancelLocationQty, String transactionType, BigDecimal unProcessedCancelQty,
			boolean isShipped) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// if (transTypeAdjustment.equals(transactionType) ||
		// (transTypeTransfer.equals(transactionType) && isShipped)) {
		if (transTypeAdjustment.equals(transactionType)) {
			sparePartsHeaderObj
					.setCumQuantityUnreserved((null == sparePartsHeaderObj.getCumQuantityUnreserved() ? new BigDecimal(0) : sparePartsHeaderObj.getCumQuantityUnreserved()).add(cancelLocationQty));

			// The code below will be called when the items are fulfilled using both TSF and
			// ADJ and the items from TSF
			// are shipped BUT NOT RECEIVED.
			if (null != unProcessedCancelQty && unProcessedCancelQty.floatValue() > 0)
				cancelLocationQty = cancelLocationQty.add(unProcessedCancelQty);
			log.info("cancelLocationQty=" + cancelLocationQty);
		}

		OmsSparePartHeader omsSparePartHeader = session.getOmsSparePartHeaderByIntKey(sparePartsHeaderObj.getOmsServiceReqSeqId());
		BigDecimal cumCancelledQty = omsSparePartHeader.getCumCancelledQty();
		BigDecimal pendingQty = omsSparePartHeader.getPendingQty();
		sparePartsHeaderObj.setPendingQty((null == pendingQty ? new BigDecimal(0) : pendingQty).subtract(cancelLocationQty));

		log.info("CumCancelledQty before updation is " + sparePartsHeaderObj.getCumCancelledQty());
		/*
		 * sparePartsHeaderObj.setCumCancelledQty((null ==
		 * sparePartsHeaderObj.getCumCancelledQty() ? new BigDecimal(0) :
		 * sparePartsHeaderObj.getCumCancelledQty()).add(cancelLocationQty));
		 */

		sparePartsHeaderObj.setCumCancelledQty((null == cumCancelledQty ? new BigDecimal(0) : cumCancelledQty).add(cancelLocationQty));
		log.info("sparePartsHeaderObj Cancelled qty afeter updating=" + sparePartsHeaderObj.getCumCancelledQty());
		// If the entire pending quantity is reduced to ZERO then close the Spare Parts
		// Request.
		if (sparePartsHeaderObj.getPendingQty().floatValue() <= 0) {
			sparePartsHeaderObj.setStatus(statusClosed);
		} else if (NumberUtil.defaultIfNull(sparePartsHeaderObj.getCumQuantityReserved()).subtract(NumberUtil.defaultIfNull(sparePartsHeaderObj.getCumQuantityUnreserved())).subtract(sparePartsHeaderObj.getPendingQty()).intValue() >= 0) {
			sparePartsHeaderObj.setReadyToNotifyFlag("Y");
		}

		sparePartsHeaderObj.setUpdatedBy(webServiceName);
		sparePartsHeaderObj.setLastUpdateDatetime(new Timestamp((new Date()).getTime()));

		session.mergeOmsSparePartHeader(sparePartsHeaderObj);
		log.info("Table merged");
		return sparePartsHeaderObj;
	}

	// Step 5: Save the values into the SPARE_PART_AUDIT table

	public void createSparePartsAuditEntry(String item, String eventType, BigDecimal quantity, BigDecimal sourceLocation, String omsSparePartsServiceId) throws SOAPException {
		log.info("  --> Saving into OMS_SPARE_PART_AUDIT table ");
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsSparePartAudit theOmsSparePartAudit = formOmsSparePartAuditObject(item, eventType, quantity, sourceLocation, omsSparePartsServiceId);

			session.persistOmsSparePartAudit(theOmsSparePartAudit);
			log.info("  --> Successfully created Spare Parts Audit data for Id : " + omsSparePartsServiceId);
		} catch (Exception e) {

			log.error("ERROR while inserting record into OMS_SPARE_PART_AUDIT :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
	}

	public void closeCancellationRequest(OmsSparePartCancelHdr theCancelHeaderRecord) throws SOAPException {
		log.info("  --> Closing the Cancellation request ");
		try {

			OMSUtilSessionEJB session = OMSUtil.doLookup();
			theCancelHeaderRecord.setStatus(statusClosed);
			session.mergeOmsSparePartCancelHdr(theCancelHeaderRecord);

		} catch (Exception e) {

			log.error("ERROR while inserting record into OMS_SPARE_PART_CANCEL_HDR :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		}
	}

	// Create a response and send it back to the invoker

	public SparePartsCancelResponse getServiceResponse(SparePartsCancelRequest input, String errMessage) {
		SparePartsCancelResponse theResponse = new SparePartsCancelResponse();
		theResponse.setOMSServiceId(input.getOMSServiceId());
		theResponse.setSequenceId(input.getSequenceId());
		theResponse.setServiceRequestId(input.getServiceRequestId());
		theResponse.setStatus(null == errMessage ? statusClosed : statusRejected);
		if (null != errMessage) {
			OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
			theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
			theResponse.setMessageCode(theErrorObj.getOmsErrorCode());
		} else {
			theResponse.setMessageDesc("");
			theResponse.setMessageCode("");
		}
		log.info("  --> Response Successfully Created.");
		// log.info(theResponse.toString());
		return theResponse;
	}

	public StsTsfHdrColDesc callSimLookupTransferHeader(long storeId, long tranId) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException, IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfHdrCriVo stsTsfHdrCriVo = new StsTsfHdrCriVo();
		stsTsfHdrCriVo.setStoreId(storeId);
		stsTsfHdrCriVo.setStatus(StsTsfCriStatus.NO_VALUE);
		stsTsfHdrCriVo.setTransferId(tranId);
		StsTsfHdrColDesc stsTsfHdrColDesc = store2storeTransferPortType.lookupTransferHeader(stsTsfHdrCriVo);
		return stsTsfHdrColDesc;
	}
}
