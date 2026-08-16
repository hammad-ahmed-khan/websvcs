package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsSparePartAudit;
import com.logicinfo.oms.ejb.OmsSparePartFulfill;
import com.logicinfo.oms.ejb.OmsSparePartHeader;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;
import com.logicinfo.oms.model.SparePartsProcessedObject;
import com.logicinfo.oms.model.SparePartsRequest;
import com.logicinfo.oms.model.SparePartsResponse;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class SparePartsRequestBean extends SparePartsRequestHelperBean {
	private final static Logger log = Logger.getLogger(SparePartsRequestBean.class.getName());

	String webServiceName = "Spare Parts Request WebService";
	String statusInProcess = "IP";
	String statusRejected = "RJ";
	String transactionTypeTransfer = "TSF";
	String transactionTypeAdjustment = "ADJ";

	BigDecimal subBucketQty = null;

	OracleIntegrationSevicesSIM simOracleIntegrationSevices;

	public SparePartsRequestBean() {
		super();
	}

	/**
	 * This method checks if the input request is already existing with OMS If the
	 * new incoming request already exists in the OMS_SPARE_PART_HEADER table then
	 * return ERROR. Logic : If the service_request_id and the sequence_id from the
	 * input request already exists in the OMS_SPARE_PART_HEADER table, then return
	 * ERROR
	 * 
	 * @param input
	 * @throws IllegalArgumentException
	 * @throws SOAPException
	 */
	public void performBasicValidation(SparePartsRequest input) throws IllegalArgumentException, SOAPException, BusinessException {
		OMSUtilSessionEJB session;
		BigDecimal combinationId;

		// Step 1 - Check if the session bean can be accessed.
		try {
			session = OMSUtil.doLookup();
		} catch (Exception e) {
			throw new SOAPException("ERROR while looking up OMSUtil Session EJB." + e.getMessage());
		}
		OmsSparePartHeader theSparePartHeadObj = null;
		try {
			if (input.getQuantity().compareTo(BigDecimal.valueOf(99999999)) < 0) {
				// Step 2 -If duplicate record exists then throw error.
				log.info("  --> Checking for duplicate record in Header table.");
				try {
					log.info("inside trys");
					theSparePartHeadObj = session.getOmsSparePartHeaderByExtKeys(input.getServiceRequestId(), input.getSequenceId());
	
					log.info("  --> Duplicate record found.");
	
					if (null != theSparePartHeadObj) {
						String errValueString = " service req id " + input.getServiceRequestId() + " and sequence id " + input.getSequenceId();
						throw new BusinessException(theSparePartHeadObj.getOmsServiceReqSeqId() + "~" + theSparePartHeadObj.getStatus(),
								OMSUtilCommons.formErrorDescription(OMSConstants.ERR_DATA_EXISTS, "1", new String[] { errValueString }));
					}
				} catch (SOAPException se) {
					log.info("Exception" + se);
					if (se.getMessage().equals("No Record")) {
						log.info("  --> NoResultException - No Spare Part Request for Req ID: " + input.getServiceRequestId() + " and Seq Id " + input.getSequenceId());
						log.info("  --> Proceeding with New Request Creation.");
					} else {
						throw se;
					}
				}
			}
			try {
				log.info("Checling item" + input.getItemId());
				session.getItemMasterFindItemStatus(input.getItemId());
			} catch (Exception e) {
				// Invalid item @@value1 ,or item is not approved.
				log.error("Failed in getItemMasterFindItemStatus", e);
				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.INVALID_ITEM, "1", new String[] { input.getItemId() }));
			}

			if (input.getQuantity().equals("1") || input.getQuantity().equals("-1")) {
				// Invalid quantity @@value1
				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.INVALID_QTY, "1", new String[] { input.getQuantity().toString() }));
			}
			// Step 3 - Check if combination id exists for the store id in the Fulfillment
			// Matrix for spares.
			try {
				combinationId = getFulfillmentCombinationId(session, new BigDecimal(input.getStoreId()), "SPARE", "C");
			} catch (SOAPException se) {
				// throw se;
				// Combination id not set for store= @@value1 ,shipment_classification= @@value2
				// and delivery_type= @@value3.
				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.UNAVAIL_COMB_ID, "1", new String[] { String.valueOf(input.getStoreId()), "SPARE", "C" }));
			}

			BigDecimal totalSOH = new BigDecimal(0);
			BigDecimal sohAtLoc = new BigDecimal(0);

			// Step 3 - If the SOH across all stores can fullfill the entire request, then
			// proceed else throw error.
			OMSUtilCommons theCommonFunc = new OMSUtilCommons();
			int count = 0;
			OmsFulfillMatrixExtDetail theFulfillMatrixDetail;
			
			if (input.getSubBucket() != null && !input.getSubBucket().trim().isEmpty()) {				
				subBucketQty = getSubBucketQty(input);
			}
			if (subBucketQty != null && subBucketQty.intValue() > 0) {
				totalSOH = totalSOH.add(subBucketQty);
			}

			while (totalSOH.compareTo(input.getQuantity()) < 0) {
				count++;
				// Step : Get the fulfillment detail based on combination id and priority
				try {
					theFulfillMatrixDetail = getFulfillmentMatrixDetail(session, combinationId, count, input.getItemId());
				} catch (SOAPException se) {
					log.error("--> Insufficient inventory. Requested Qty:" + input.getQuantity() + " and Cumulative Available Qty :" + totalSOH);
					log.info("Throw Exception");
					throw new SOAPException(OMSUtilCommons.formErrorDescription("INSUFFICIENT_INV", "1", new String[] { totalSOH.toString() }));
				}

				log.info("      --> Checking SOH in location id " + theFulfillMatrixDetail.getLocation() + ", priority =" + count);

				// Get the soh at the location
				sohAtLoc = theCommonFunc.getSOHforLocation(input.getItemId(), theFulfillMatrixDetail.getLocation(), OMSConstants.LOCATION_TYPE_ST);

				log.info("      --> SOH retrieved :" + sohAtLoc);

				// Add soh to get the cumulative total
				if (sohAtLoc.compareTo(new BigDecimal(0)) > 0) {
					totalSOH = totalSOH.add(sohAtLoc);
				}
			} // End Looping

			log.info("  --> Sufficient inventory :" + totalSOH + "available to satisfy the requested qty: " + input.getQuantity());
		} catch (SOAPException se) {
			throw se;
		} catch (BusinessException bae) {
			throw bae;
		} catch (Exception e) {
			log.error("Unknown error from Exception" + e.getMessage());
			throw new SOAPException(e.getMessage());
		}
	}

	// Step 2: Insert a new record into OMS_SPARE_PART_HEADER based on the data from
	// the input.

	private BigDecimal getSubBucketQty(SparePartsRequest input) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			statement = connection.prepareStatement("SELECT AVAIL_TECH_SUB FROM XX_TECH_SUB_AVAIL_V WHERE ITEM_ID = ? AND STORE_ID = ? AND TECH_SUB_BUCKET = ? ");
			statement.setString(1, input.getItemId());
			statement.setLong(2, input.getStoreId());
			statement.setString(3, input.getSubBucket().trim());
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
		} catch (Exception e) {
			log.error("Error while loading the sub bucket availablity", e);
			throw e;
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return null;
	}

	/**
	 *
	 * @param input
	 * @throws SOAPException
	 */

	public void createSparePartsHeaderEntry(SparePartsRequest input) throws SOAPException {
		log.info("  --> Start-Save Spare Parts Header Entry.");
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsSparePartHeader theOmsSparePartHeader = formOmsSparePartsHeaderObject(input);

			log.debug(" --> Inserting into OMS_SPARE_PART_HEADER table : " + theOmsSparePartHeader.toString());
			log.info("  --> Inserting into OMS_SPARE_PART_HEADER table.");
			theOmsSparePartHeader = session.persistOmsSparePartHeader(theOmsSparePartHeader);
			OMSSparePartsServiceId = theOmsSparePartHeader.getOmsServiceReqSeqId();
			log.info("  --> Successfully created Spare Parts Header with Id : " + OMSSparePartsServiceId);
		} catch (Exception e) {
			log.error("ERROR while inserting record into OMS_SPARE_PART_HEADER :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_TABLE_INSERT, "1", new String[] { "Spare Part Header" }));
		}
	}

	/**
	 * Step 3: Process the spare parts fulfillment request
	 */
	public void processSparePartsFulfillment(SparePartsRequest input) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons theCommon = new OMSUtilCommons();

		BigDecimal requestedQuantity = input.getQuantity();
		BigDecimal remainingRequestedQuantity = requestedQuantity;
		BigDecimal availableLocationInventory, inventoryAdjustmentQuantity;
		int priority = 1;
		BigDecimal combinationId;
		String item = input.getItemId();
		String eventType;
		BigDecimal quantityDeducted;
		BigDecimal quantityReceived;
		BigDecimal quantityReserved;
		BigDecimal quantityShipped;
		BigDecimal quantityTsfReserved;
		BigDecimal quantityUnreserved;
		BigDecimal transferId;
		String transactionType = null;

		BigDecimal cumulativeQtyReserved = new BigDecimal(0);
		BigDecimal cumulativeQtyTsfReserved = new BigDecimal(0);
		SparePartsProcessedObject theProcessedObj = null;

		simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();
		SparePartsRequestReversalBean sparePartsRequestReversalBean = new SparePartsRequestReversalBean();

		try {
			combinationId = getFulfillmentCombinationId(session, new BigDecimal(input.getStoreId()), "SPARE", "C");
		} catch (SOAPException se) {
			throw se;
		}

		List<SparePartsProcessedObject> successfullyProcessedRecs = new ArrayList<SparePartsProcessedObject>();
		OmsSparePartFulfill theSparePartsFulfillobj;

		try {

			while (remainingRequestedQuantity.doubleValue() > 0) {
				inventoryAdjustmentQuantity = new BigDecimal(0);
				availableLocationInventory = new BigDecimal(0);
				quantityDeducted = new BigDecimal(0);
				quantityReceived = new BigDecimal(0);
				quantityReserved = new BigDecimal(0);
				quantityShipped = new BigDecimal(0);
				quantityTsfReserved = new BigDecimal(0);
				quantityUnreserved = new BigDecimal(0);
				BigDecimal subBukAdjQty = null;
				transferId = new BigDecimal(0);
				eventType = null;
				OmsFulfillMatrixExtDetail theFulfillMatrixDetail = null;
				theProcessedObj = null;
				theSparePartsFulfillobj = null;

				// Step : Get the fulfillment detail based on combination id and priority
				try {
					theFulfillMatrixDetail = getFulfillmentMatrixDetail(session, combinationId, priority, input.getItemId());
				} catch (SOAPException se) {
					throw se;
				}

				// Step : Get the SOH for the location derived from the FulfillMatrix.
				if (null != theFulfillMatrixDetail) {
					log.info("  ===$$$>> Processing Spare Part Item " + input.getItemId() + " request for location " + theFulfillMatrixDetail.getLocation() + " of type "
							+ theFulfillMatrixDetail.getDeliveryFromLocType() + " with priority " + priority);
					availableLocationInventory = theCommon.getSOHforLocation(input.getItemId(), theFulfillMatrixDetail.getLocation(), theFulfillMatrixDetail.getLocationType());
					log.info("  --> Available inventory at the location is " + availableLocationInventory.toString());
				}
				
				if (subBucketQty != null && subBucketQty.intValue() > 0) {
					availableLocationInventory = availableLocationInventory.add(subBucketQty);
				} 

				// If the quantity available at the store is negative for the item then continue
				// to the next store;
				if (availableLocationInventory.compareTo(new BigDecimal(0)) <= 0) {
					priority++;
					continue;
				}
				// Step : Check if availableLocationInventory can satisfy the
				// remainingRequestedQuantity
				inventoryAdjustmentQuantity = availableLocationInventory.min(remainingRequestedQuantity); // Which ever is less will be used for inventory adjustment

				if (subBucketQty != null) {
					quantityReserved = inventoryAdjustmentQuantity;
					transactionType = transactionTypeAdjustment;
				} else if (priority == 1) {
					quantityReserved = inventoryAdjustmentQuantity;
					transactionType = transactionTypeAdjustment;
					log.info("Priority 1" + transactionType);
				} else {
					quantityTsfReserved = inventoryAdjustmentQuantity;
					transactionType = transactionTypeTransfer;
				}

				// Step : Save the values into the SPARE_PART_DETAIL table
				theSparePartsFulfillobj = formOmsSparePartFulfillObject(item, theFulfillMatrixDetail.getLocation(), quantityDeducted, quantityReceived, quantityReserved, quantityShipped,
						quantityTsfReserved, quantityUnreserved, transferId, transactionType);
				log.info("persisted into formOmsSparePartFulfillObject");
				// Initialize the processed object.
				theProcessedObj = new SparePartsProcessedObject();
				theProcessedObj.setSparePartsFulfillObj(theSparePartsFulfillobj);

				// Step : Get the Inventory Adjusted from the subbucket then adjustment same store (priority 1) OR
				// Transferred from another store (Other priorities)
				if (subBucketQty != null) {
					BigDecimal theReasonCode = getSubBucketReasonCode(input.getSubBucket().trim());
					subBukAdjQty = subBucketQty.min(remainingRequestedQuantity);
					simOracleIntegrationSevices.adjustInventoryByItemLocation(item, theFulfillMatrixDetail.getLocation(), theReasonCode,
							subBukAdjQty, input.getServiceRequestId());
					theSparePartsFulfillobj.setSubBucket(input.getSubBucket().trim());
					theSparePartsFulfillobj.setSubBucketQty(subBukAdjQty);
					subBucketQty = null;
				}
				if (priority == 1) {
					// Step : Get reason code from System Parameters table
					String theReasonCode = session.getOmsSystemParametersFindIndValue(OMSConstants.inventoryReserveCode, OMSConstants.inventoryAdjReasonCode);
					log.info("theReasonCode" + theReasonCode);
					// Step : Use SIM Inv Adj webservice
					long simAdjustmentId = simOracleIntegrationSevices.adjustInventoryByItemLocation(item, theFulfillMatrixDetail.getLocation(), new BigDecimal(theReasonCode),
							inventoryAdjustmentQuantity, input.getServiceRequestId());

					log.info("simAdjustmentId" + simAdjustmentId);
					transferId = new BigDecimal(simAdjustmentId);

					eventType = OMSConstants.QUANTITY_RESERVED;
					log.info("  --> Adjustment Id " + transferId.toString() + " created for Qty " + quantityReserved + " from Store " + theFulfillMatrixDetail.getLocation());
					cumulativeQtyReserved = cumulativeQtyReserved.add(quantityReserved);

				} else {
					// Step : Use SIM StoreToStoreTransfer webservice

					long simTransferReqId = simOracleIntegrationSevices.saveTransferRequestStore2Store(item, theFulfillMatrixDetail.getLocation(), new BigDecimal(input.getStoreId()),
							inventoryAdjustmentQuantity, input.getServiceRequestId());
					transferId = new BigDecimal(simTransferReqId);
					log.info("Transfer Id " + transferId);
					log.info("theFulfillMatrixDetail.getLocation() " + theFulfillMatrixDetail.getLocation());
					log.info("StoreId " + input.getStoreId());
					eventType = OMSConstants.QUANTITY_TRANSFERRED;
					log.info("  --> Transfer Id " + transferId.toString() + " created for Qty " + quantityTsfReserved + " from Store " + theFulfillMatrixDetail.getLocation());
					cumulativeQtyTsfReserved = cumulativeQtyTsfReserved.add(quantityTsfReserved);
					// simOracleIntegrationSevices.

					String confirmation = session.getOmsSystemParametersFindIndValue("OMS_SP_TSF_CNF_IND", "OMS_SYSTEM_OPTION");
					String acceptance = session.getOmsSystemParametersFindIndValue("OMS_SP_TSF_ACPT_IND", "OMS_SYSTEM_OPTION");

					log.info(" confirmation " + confirmation);
					log.info(" acceptance " + acceptance);

					if (confirmation.equals("Y")) {
						log.info("calling requestTransferGetLineIdReqQty method ");
						simOracleIntegrationSevices.requestTransferGetLineIdReqQty(transferId, theFulfillMatrixDetail.getLocation(), confirmation, acceptance);
					}

				}

				theProcessedObj.setTransactionId(transferId.toString());
				theProcessedObj.setSimProcessed(true);
				// Create the Spare Parts Fulfill Record in the table.
				theSparePartsFulfillobj.setTranId(transferId);
				session.persistOmsSparePartFulfill(theSparePartsFulfillobj);
				log.info("  --> Created Spare Parts Fulfill Entry with Transaction Id " + theSparePartsFulfillobj.getTranId() + " of type " + theSparePartsFulfillobj.getTranType());

				theProcessedObj.setFulfillProcessed(true);
				theProcessedObj.setSparePartsFulfillObj(theSparePartsFulfillobj); // This will update the transaction id in the fulfill obj.

				// Update the Fulfilltable with the transfer Id and transaction Type.
				theSparePartsFulfillobj.setTranId(transferId);
				session.mergeOmsSparePartFulfill(theSparePartsFulfillobj);

				// Step : Save the values into the SPARE_PART_AUDIT table
				createSparePartsAuditEntry(item, eventType, inventoryAdjustmentQuantity, theFulfillMatrixDetail.getLocation(), OMSSparePartsServiceId);
				theProcessedObj.setAuditProcessed(true);
				log.info("  --> Created Spare Parts Audit Entry.");

				successfullyProcessedRecs.add(theProcessedObj);

				// Step : Reduce the remainingRequestedQuantity by the
				// inventoryAdjustmentQunatity and increment priority.
				remainingRequestedQuantity = remainingRequestedQuantity.subtract(inventoryAdjustmentQuantity);

				priority++;

			} // Looping ends here

		} catch (SOAPException soape) {
			log.error("     --> Error while processing detailed records. Starting transaction reversals." + soape.getMessage());
			if (null != theProcessedObj) {
				successfullyProcessedRecs.add(theProcessedObj);
			}
			// If any error in processing the detailed records then reverse the
			// transactions.
			sparePartsRequestReversalBean.reverseSuccessfulTransactions(successfullyProcessedRecs, input, OMSSparePartsServiceId);

			throw soape;
			// soape.printStackTrace();
		}

		log.info("  --> Finished processing all detailed data. Updating Header table for cumulatives.");
		log.info("  --> Cumulative Reserved : " + cumulativeQtyReserved + " Cumulative Transfer Reserved : " + cumulativeQtyTsfReserved);

		// Step : Update the cumulative quantities in the SPARE_PART_HEADER table
		OmsSparePartHeader omsSparePartHeader = session.getOmsSparePartHeaderByIntKey(OMSSparePartsServiceId);
		omsSparePartHeader.setCumQuantityReserved(null == omsSparePartHeader.getCumQuantityReserved() ? cumulativeQtyReserved : omsSparePartHeader.getCumQuantityReserved().add(cumulativeQtyReserved));
		omsSparePartHeader.setCumQuantityTsfReserved(
				null == omsSparePartHeader.getCumQuantityTsfReserved() ? cumulativeQtyTsfReserved : omsSparePartHeader.getCumQuantityTsfReserved().add(cumulativeQtyTsfReserved));
		session.mergeOmsSparePartHeader(omsSparePartHeader);
		log.info("  --> Cumulatives updated in Spare Part Header table.");

	}

	private BigDecimal getSubBucketReasonCode(String subBucket) throws SOAPException {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			statement = connection.prepareStatement("SELECT TECH_TO_AVAIL FROM XX_TECH_REAS_CODE_V WHERE DESCRIPTION = ? ");
			statement.setString(1, subBucket);
			rs = statement.executeQuery();
			if (rs.next()) {
				return rs.getBigDecimal(1);
			}
		} catch (Exception e) {
			log.error("Error while loading the sub bucket availablity", e);
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNHANDLED_EXCEPTION, "1", new String[] {}));
		} finally {
			OMSUtil.closeDBConnection(connection, statement, rs);
		}
		return null;
	}

	/**
	 * Step 4: Saving Data to OMS_SPARE_PART_FULFILL table.
	 */
	public OmsSparePartFulfill createSparePartsFulfillmentEntry(String item, BigDecimal sourceLocation, BigDecimal quantityDeducted, BigDecimal quantityReceived, BigDecimal quantityReserved,
			BigDecimal quantityShipped, BigDecimal quantityTsfReserved, BigDecimal quantityUnreserved, BigDecimal transferId, String transactionType) throws SOAPException {
		log.info("  -->Saving into OMS_SPARE_PART_FULFILL table ");

		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsSparePartFulfill theOmsSparePartFulfill = formOmsSparePartFulfillObject(item, sourceLocation, quantityDeducted, quantityReceived, quantityReserved, quantityShipped, quantityTsfReserved,
					quantityUnreserved, transferId, transactionType);
			log.debug(" --> Inserting into OMS_SPARE_PART_FULFILL table : " + theOmsSparePartFulfill.toString());
			log.info("  --> Inserting into OMS_SPARE_PART_FULFILL table");
			theOmsSparePartFulfill = session.persistOmsSparePartFulfill(theOmsSparePartFulfill);
			log.info("  --> Successfully created Spare Parts Fulfill data for Id : " + OMSSparePartsServiceId);
			return theOmsSparePartFulfill;
		} catch (Exception e) {

			// sparePartsRequestReversalBean.reverseSuccessfulTransactions(successfullyProcessedRecs,
			// input,OMSSparePartsServiceId);

			log.error("ERROR while inserting record into OMS_SPARE_PART_FULFILL :" + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_TABLE_INSERT, "1", new String[] { "OMS_SPARE_PART_FULFILL" }));
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

	private BigDecimal getFulfillmentCombinationId(OMSUtilSessionEJB session, BigDecimal storeId, String itemType, String fulfillmentType) throws SOAPException {
		BigDecimal returnId;
		try {
			String deliveryZone = null;
			String marketPlaceInd = null;
			String applicationId = null;
			String shipToStore = "N";
			log.info("  --> Finding data in Fulfill Matrix for store id " + storeId + "/SPARE/C ");
			// Step : Get the combination id from the Fulfillment Matrix Header
			returnId = session.getOmsFulfillMatrixExtHeadFindCombinationWoCity(storeId, itemType, fulfillmentType, deliveryZone, marketPlaceInd, applicationId, shipToStore);
			log.info("  --> Combination id " + returnId + " retrieved from fulfillment matrix.");
			return returnId;
		} catch (Exception e) {
			log.error(" --> ERROR while fetching combination id for store id " + storeId + "/SPARE/C " + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_LOC_MTRX_MISSING, "1", new String[] { storeId.toString(), itemType, fulfillmentType }));
		}
	}

	private OmsFulfillMatrixExtDetail getFulfillmentMatrixDetail(OMSUtilSessionEJB session, BigDecimal pCombinationId, int priority, String itemId) throws SOAPException {
		OmsFulfillMatrixExtDetail theFulfillMatrixDetail;
		try {
			theFulfillMatrixDetail = session.getOmsFulfillMatrixExtDetailFindDetail(pCombinationId, new BigDecimal(priority));
		} catch (Exception e) {
			log.error("     --> ERROR while fetching fulfillment Detail for combination id " + pCombinationId + " and priority " + priority + " . -- " + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_FULFILL_LOC_MISSING, "1", new String[] { itemId }));
		}
		return theFulfillMatrixDetail;
	}

	// Create a response and send it back to the invoker

	public SparePartsResponse getServiceResponse(SparePartsRequest input, String errMessage, String statusCode) throws SOAPException {
		// List<SparePartsProcessedObject> successfullyProcessedRecs=null;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		SparePartsResponse theResponse = new SparePartsResponse();
		if (OMSSparePartsServiceId != null) {
			log.info("OMSSparePartsServiceId=" + OMSSparePartsServiceId);
			theResponse.setOMSServiceId(OMSSparePartsServiceId);
		}
		theResponse.setSequenceId(input.getSequenceId());
		theResponse.setServiceRequestId(input.getServiceRequestId());

		if (statusCode != null) {
			String[] omsObjSts = statusCode.split("~");
			theResponse.setOMSServiceId(omsObjSts[0]);
			theResponse.setStatus(omsObjSts[1]);
		} else {
			theResponse.setStatus(null == errMessage ? statusInProcess : statusRejected);
		}
		log.info("input.getServiceRequestId() " + input.getServiceRequestId());
		log.info("OMSSparePartsServiceId " + OMSSparePartsServiceId);

		if (null != errMessage) {
			log.info("inside error message");
			if (input.getServiceRequestId() != null && OMSSparePartsServiceId != null) {
				log.info("Inside if condition doesnot contain null");
				OmsSparePartHeader omsSparePartHeader = session.getOmsSparePartHeaderByKeyComb(OMSSparePartsServiceId, input.getServiceRequestId());
				omsSparePartHeader.setCumCancelledQty(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityReceived(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityReserved(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityDeducted(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityShipped(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityTsfReserved(BigDecimal.ZERO);
				omsSparePartHeader.setCumQuantityUnreserved(BigDecimal.ZERO);
				omsSparePartHeader.setPendingQty(BigDecimal.ZERO);
				omsSparePartHeader.setStatus(statusRejected);
				session.mergeOmsSparePartHeader(omsSparePartHeader);
			}
			if (statusCode == null) { // Duplicate request should not u
				OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(errMessage);
				theResponse.setMessageDesc(theErrorObj.getOmsErrLangDesc());
				theResponse.setMessageCode(theErrorObj.getOmsErrorCode());
			} else {
				theResponse.setMessageDesc("");
				theResponse.setMessageCode("");
			}
		} else {
			List<OmsSparePartFulfill> omsSparePartFulfillList = session.getOmsSparePartFulfillFindByOmsServiceReqId(OMSSparePartsServiceId);
			for (OmsSparePartFulfill omsSparePartFulfill : omsSparePartFulfillList) {
				log.info("omsSparePartFulfill.getSourceLocation()=" + omsSparePartFulfill.getSourceLocation() + "input.getStoreId()=" + input.getStoreId());
				if (omsSparePartFulfill.getSourceLocation().compareTo(new BigDecimal(input.getStoreId())) != 0)
					theResponse.setStatus("IT");

				OmsSparePartHeader omsSparePartHeader = session.getOmsSparePartHeaderByKeyComb(OMSSparePartsServiceId, input.getServiceRequestId());
				/*
				 * Internal Bug - Bug#25 Update the Status in OmsSparePartHeader If it is
				 * Transfer, will update as IT If it is Reservation, will update as IP
				 * 
				 */
				if ((omsSparePartHeader.getCumQuantityReserved().intValue() > 0) && (omsSparePartHeader.getCumQuantityTsfReserved().intValue() == 0)) {
					log.info("Test:" + omsSparePartHeader.getCumQuantityReserved());
					omsSparePartHeader.setStatus("IP");
					log.info("Status: IP");
				} else {
					log.info("Test:" + omsSparePartHeader.getCumQuantityReserved());
					omsSparePartHeader.setStatus("IT");
					log.info("Status: IT");
				}
				// omsSparePartHeader.setStatus("IT");
				session.mergeOmsSparePartHeader(omsSparePartHeader);
			}
			theResponse.setMessageDesc("");
			theResponse.setMessageCode("");
		}
		log.info("  --> Response Successfully Created.");
		return theResponse;
	}
}
