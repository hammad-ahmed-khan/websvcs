package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsSimCancelTsfDetail;
import com.logicinfo.oms.ejb.OmsTaxDesc;
import com.logicinfo.oms.ejb.OmsTsfCancelledQtySumm;
import com.logicinfo.oms.ejb.OmsUnapprovedTransfers;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.ejb.OrdcustDetail;
import com.logicinfo.oms.integration.OracleIntegrationSevicesSIM;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodHdrDesc;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvItmMod;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfItm;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfCriStatus;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfHdrCriVo;
import com.oracle.retail.integration.base.bo.ststsfhdrdesc.v1.StsTsfHdrDesc;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqItmMod;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqModVo;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderService;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.InventoryBackOrderPortType;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.InventoryBackOrderService;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryService;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderService;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferService;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

public class OMSUtilCommons {
	private final static Logger log = Logger.getLogger(OMSUtilCommons.class.getName());

	public OMSUtilCommons() {
		super();
	}

	public String[] splitCustomerOrder(String inputCustomerOrdNo) {
		int transIdx = inputCustomerOrdNo.lastIndexOf("-");
		String posCustomerOrdNO[] = new String[2];
		posCustomerOrdNO[0] = inputCustomerOrdNo.substring(0, transIdx);
		posCustomerOrdNO[1] = inputCustomerOrdNo.substring(transIdx + 1, inputCustomerOrdNo.length());
		log.info("CustomerOrderNo " + posCustomerOrdNO[0]);
		log.info("Transaction Number " + posCustomerOrdNO[1]);
		return posCustomerOrdNO;
	}

	public void persistintoOmsSimCancelTsfDetail(BigDecimal omsCancelId, BigDecimal omsCustOrdNo, BigDecimal fulfilOrdNo, BigDecimal transferId, BigDecimal sourceLoc, String item, BigDecimal lineNo,
			BigDecimal omsCancelQty) throws Exception {
		log.info("starts persiting into  OmsSimCancelTsfDetail table");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsSimCancelTsfDetail omsSimCancelTsfDetail = new OmsSimCancelTsfDetail();
		log.info(" Object created for OmsSimCancelTsfDetail ");
		omsSimCancelTsfDetail.setOmsCustOrdNo(omsCustOrdNo);
		log.info("setted omsCustOrdNo " + omsCustOrdNo);
		omsSimCancelTsfDetail.setOmsCancelId(omsCancelId);
		log.info("setted omsCancelId " + omsCancelId);
		omsSimCancelTsfDetail.setFulfillOrderNo(fulfilOrdNo);
		log.info("setted fulfilOrdNo " + fulfilOrdNo);
		omsSimCancelTsfDetail.setItem(item);
		log.info("setted item " + item);
		omsSimCancelTsfDetail.setLineNo(lineNo);
		log.info("setted lineNo " + lineNo);
		omsSimCancelTsfDetail.setTransferId(transferId);
		log.info("setted transferId " + transferId);
		omsSimCancelTsfDetail.setSoStatusCancelQty(BigDecimal.ZERO);
		omsSimCancelTsfDetail.setOmsCancelQty(omsCancelQty);
		log.info("setted omsCancelQty " + omsCancelQty);
		omsSimCancelTsfDetail.setSourceLoc(sourceLoc);
		log.info("setted sourceLoc " + sourceLoc);
		omsSimCancelTsfDetail.setCreateDatime(new Timestamp(new Date().getTime()));
		log.info("setted create DateTime");
		session.persistOmsSimCancelTsfDetail(omsSimCancelTsfDetail);
		log.info("successfully persisted into omsSimCancelTsfDetail ");
	}

	public String getBOIndicator(BigDecimal loc, String item) throws Exception {
		log.info("***Start getBOIndicator ***");
		String boIndicator = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check from DAS schema for BACK_ORDER_IND
		String query = "select BACK_ORDER_IND from ITEM_LOC_TRAITS where LOC= ? and ITEM =?";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, loc.intValue());
			preparedStatement.setString(2, item);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				boIndicator = rs.getString("BACK_ORDER_IND");
				if (boIndicator == null) {
					boIndicator = "N";
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}

		return boIndicator;
	}

	public BigDecimal checkSOH(String item, BigDecimal loc) throws SOAPException {
		log.info("***Start checkSOH***");
		BigDecimal SOH = new BigDecimal(0);
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check SOH in DAS schema
		String query = "select avail_qty from XX_OMS_INVAVAIL_V where item= ? and loc= ?";

		try {
			log.info("item " + item);
			log.info("loc " + loc);
			log.info("OMSConstants.DS_DAS_STRING" + OMSConstants.DS_DAS_STRING);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
			preparedStatement = conn.prepareStatement(query);

			// log.info(Integer.parseInt(item));
			// preparedStatement.setLong(1, Long.parseLong(item));
			preparedStatement.setString(1, item);

			// log.info(loc.intValue());
			preparedStatement.setInt(2, loc.intValue());

			rs = preparedStatement.executeQuery();
			// log.info("before rs");

			while (rs.next()) {
				SOH = rs.getBigDecimal("avail_qty");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return SOH;
	}

	public BigDecimal checkSOHForWH(String item, List<BigDecimal> locs, String applicationId) throws SOAPException {
		log.info("***Start checkSOH***");
		BigDecimal SOH = new BigDecimal(0);
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = null;
		// Check SOH in OMS schema
		StringBuffer locations = new StringBuffer("");

		for (int j = 0; j < locs.size();) {

			locations.append(locs.get(j));
			if (++j < locs.size()) {

				locations.append(",");
			}
		}
		log.info(locations.toString());
		if ("E-COMMERCE".equals(applicationId)) { 
			query = "select item, sum(AVAIL_QTY_AFTER_BACK_ORDER) total_avail_qty " + "from XX_OMS_INVAVAIL_V_BACK " + "where item='" + item + "' " + "and avail_qty >0 and loc IN("
					+ locations.toString() + ") " + "group by item";
			log.info(query);
		} else {
			query = "select item, sum(AVAIL_QTY_AFTER_BACK_ORDER) total_avail_qty " + "from XX_OMS_POS_INVAVAIL_V " + "where item='" + item + "' " + "and avail_qty >0 and loc IN("
					+ locations.toString() + ") " + "group by item";
			log.info(query);
		}

		try {
			log.info("loc " + locations);
			log.info("item " + item);
			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("conn " + conn);
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			// log.info(rs.getBigDecimal(2));
			while (rs.next()) {
				SOH = rs.getBigDecimal("total_avail_qty");
			}
			// added code for 2603
			log.info("Before Calculation : " + SOH);
			SOH = sohCalculationforWH(SOH, item, locs);
			log.info("After Calculation : " + SOH);
		} catch (Exception e) {
			log.error(e.getMessage());
			// throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}
		return SOH;
	}

	public Integer getPackItemAvailQty(String item, BigDecimal locationId) {
		log.info("***Checking availablity for pack item ***");
		int stockOnHand = 0;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		String query = "SELECT AVAIL_QTY FROM GET_PACK_QTY_V WHERE PACK_NO = ? AND LOC = ? ";
		log.info(query);

		try {
			log.info("item " + item + " location " + locationId);
			log.info("connecting to OMS Schema");
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setLong(2, locationId.longValue());
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				stockOnHand = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Error while getting the pack item availablity for item ->" + item, e);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.warn("Error while closing the connection", e);
			}
		}
		return stockOnHand;
	}

	public static String formErrorDescription(String errorCode, String errorLang, String[] errorValues) {
		String returnErrorString = errorCode + "|" + errorLang + "|";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String valueToken;

		String query = "select oms_err_lang_desc " + "from oms_error_codes " + "where oms_error_code =? " + "and lang_code =?";
		String errString = null;
		// log.info(query + " " + errorCode + " " + errorLang);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, errorCode);
			preparedStatement.setString(2, errorLang);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				errString = rs.getString("oms_err_lang_desc");
			}

			returnErrorString = returnErrorString + errString;
			if (errString.contains("@@value")) {
				int c = 0;
				for (int i = 0; i < errorValues.length; i++) {
					// log.info(" Processing " + errorValues[i]);
					valueToken = "@@value" + (++c);
					returnErrorString = returnErrorString.replaceFirst(valueToken, errorValues[i]);
					log.info("returnErrorString=" + returnErrorString);
				}
			}
		} catch (SQLException se) {
			log.error(" --> No error description found for error code " + errorCode + " and Language " + errorLang);
			log.error(" --> SQL Error is " + se.getMessage());
			returnErrorString = returnErrorString + "No error description found.";
		} catch (Exception e) {
			log.error(" --> Unknown Error while fetching description for error code " + errorCode + " and Language " + errorLang);
			returnErrorString = returnErrorString + "No error description found.";
		} finally {
			OMSUtil.closeDBConnection(conn, preparedStatement, rs);
		}
		return returnErrorString;

	}

	public BigDecimal getSOHforLocation(String item, BigDecimal pLocationID, String pLocationType) throws SOAPException {
		BigDecimal locationSOH;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons theCommon = new OMSUtilCommons();
		String applicationId = "ORPOS";
		OracleIntegrationSevicesSIM simOracleIntegrationSevices = new OracleIntegrationSevicesSIM();
		try {
			// Validation : Sometimes the locationType can be passed in as 'S' instead of
			// 'ST'
			// and 'W' instead of 'WH'. So we need to convert it from 1 char location types
			// to 2 char location types

			if (pLocationType.equals("S"))
				pLocationType = "ST";
			if (pLocationType.equals("W"))
				pLocationType = "WH";

			// Step : If the location type is WH, get the SOH for WH from the DAS schema
			if (OMSConstants.LOCATION_TYPE_WH.equals(pLocationType)) {
				List<BigDecimal> virtualWHs = session.getWhFindByPhysicalWH(pLocationID);
				locationSOH = theCommon.checkSOHForWH(item, virtualWHs, applicationId);

			} else if (OMSConstants.LOCATION_TYPE_ST.equals(pLocationType)) {
				// Step : If the location type is ST, get the SOH for Store using the SIM
				// webservice
				// log.info(" --> Calling SIM webservice for finding SOH for item =" + item + "
				// in store=" + pLocationID);
				locationSOH = simOracleIntegrationSevices.getSOHByItemLocation(item, pLocationID);
				long SOH = sohCalculation(locationSOH, item, pLocationID).longValue();
				locationSOH = new BigDecimal(SOH);
				log.info("Final SOH after calculation : " + locationSOH);
			} else {

				throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_CO_UNKNOWN_LOC_TYPE, "1", new String[] {}));
			}
			return locationSOH;
		} catch (Exception e) {

			log.error(" --> ERROR while getting SOH at " + pLocationType + " - " + pLocationID.longValue() + " : " + e.getMessage());
			throw new SOAPException(OMSUtilCommons.formErrorDescription(OMSConstants.ERR_UNAVL_INV, "1", new String[] { pLocationType, pLocationID.toString() }));
		}
	}

	/**
	 * @param webserviceId
	 * @return
	 * @throws SOAPException
	 */
	public static String getWebServiceURL(String webserviceId) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String wsdlUrl = session.getOmsWebserviceUriDetailFindByWebserviceName(webserviceId);
		return wsdlUrl;
	}

	public String findUINType(String item, BigDecimal loc) throws SOAPException {
		log.info("***Start findUINType***");
		String uinType = "";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check SOH in DAS schema

		String query = "select decode(UIN_TYPE,NULL,'N','Y') from item_loc where ITEM= ? and LOC= ?";

		try {

			conn = OMSUtil.createDBConnection(OMSConstants.DS_DAS_STRING);
			preparedStatement = conn.prepareStatement(query);

			// log.info(Integer.parseInt(item));
			preparedStatement.setString(1, (item));

			// log.info(loc.intValue());
			preparedStatement.setInt(2, loc.intValue());

			rs = preparedStatement.executeQuery();
			// log.info("before rs");

			while (rs.next()) {
				uinType = rs.getString(1);
				log.info("uinType====" + uinType);
				// uinType = rs.getString("UIN_TYPE");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return uinType;
	}

	public String getShippingClassificationType() {
		return "I";
	}

	public String findShipmentClassification(String item, BigDecimal store, String shippingClassification) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection con = null;
		CallableStatement pstmt = null;
		int result = 0;
		String shipClassification = "";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);

			BigDecimal channelId = session.getStoreFindChannelId(store);
			pstmt = con.prepareCall("{?=call OMS_SHIP_CLASSIFICATION(?,?,?,?)}");
			pstmt.registerOutParameter(1, Types.VARCHAR);
			pstmt.setString(2, item);

			pstmt.setInt(3, channelId.intValue());
			pstmt.setInt(4, store.intValue());
			pstmt.setString(5, shippingClassification);
			pstmt.executeUpdate();

			shipClassification = pstmt.getString(1);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to find the shipping classification");
			String errString = OMSUtilCommons.formErrorDescription("UNAVL_SHIP_CLASSIFICATION", "1", new String[] {});
			log.error(errString);
			throw new SOAPException(errString);

		} finally {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return shipClassification;
	}

	public BigDecimal processFulfillmentMatrixGetCombID(BigDecimal reqId, String itemType, String custCity, String modeOfDelv, String deliverZone, String marketPlaceInd, String applicationId, String ShipToStore)
			throws SOAPException {
		log.info("***Start processFulfillmentMatrixGetCombID***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal combinationID = null;
		// Fetching combinationId
		try {
			log.info("Finding combination for " + "reqId" + reqId + "itemType" + itemType + "custCity" + custCity + "modeOfDelv" + modeOfDelv);
			combinationID = session.getOmsFulfillMatrixExtHeadFindCombination(reqId, itemType, custCity, modeOfDelv, deliverZone, marketPlaceInd, applicationId, ShipToStore);
			log.info("Comb_id when the city name exists-----" + combinationID);
		} catch (Exception e) {
			log.error("Combination ID unavailable for the given record , rollbacking");
			// omsCustomerOrderBean.rollback();

			// included the code for CITY_NAME = "ALL" option
			try {
				log.info("Inside the try block for checking the city name 'ALL' option");
				combinationID = session.getOmsFulfillMatrixExtHeadFindCombination(reqId, itemType, "ALL", modeOfDelv, deliverZone, marketPlaceInd, applicationId, ShipToStore);
				log.info("Successfully fetched the combination_id when city doesnt exists in first try block-----" + combinationID);
			} catch (Exception f) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Combination ID unavailable for the given record"));
			}
			// throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Combination
			// ID unavailable for the given record"));
		}
		// ProjectUtils.setCombinationID(combinationID);
		return combinationID;
	}

	public BigDecimal processFulfillmentMatrixGetCombIDWoCity(BigDecimal reqId, String itemType, String modeOfDelv, String deliverZone, String marketPlaceInd, String applicationId, String ShipToStore)
			throws SOAPException {
		log.info("***Start processFulfillmentMatrixGetCombIDWoCity***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal combinationID = null;

		try {
			log.info("reqId" + reqId + "itemType" + itemType + "modeOfDelv" + modeOfDelv);
			combinationID = session.getOmsFulfillMatrixExtHeadFindCombinationWoCity(reqId, itemType, modeOfDelv, deliverZone, marketPlaceInd, applicationId, ShipToStore);
			log.info(combinationID);
		} catch (Exception e) {
			e.printStackTrace();
			// omsCustomerOrderBean.rollback();
			// Combination ID unavailable for the given record
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("LOC_MTRX_MISSING"));
		}
		// ProjectUtils.setCombinationID(combinationID);
		return combinationID;
	}

	public int checkOpenDeliveryForQuantity_Picked(String custOrderId, BigDecimal omsCustOrdNo, OmsCustOrdItem input)
			throws SOAPException, com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("--> inside checkOpenDeliveryForQuantity_Picked for oms_cust_ord_no=" + omsCustOrdNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
		int handeOverToCourierQty = 0;
		log.info("inside for of item, omsCustOrderNo=" + omsCustOrdNo + "inputItem.getLineNo()" + input.getLineNo() + "inputItem.getItem()" + input.getItem());
		log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
		// omsCoFulfillDetailList =
		// session.getOmsCoFulfillDetailFindByItem(omsCustOrdNo,input.getLineNo(),
		// input.getItem());
		omsCoFulfillDetailList = session.getOmsCoFulfillDetailfindByOmsCustOrdNoLineNoandItemsrcandFul(omsCustOrdNo, input.getLineNo());
		log.info("omsCustOrdHead " + omsCustOrdHead.getCustOrderNo() + "omsCoFulfillDetailList size " + omsCoFulfillDetailList.size());
		int quantity_picked = 0;
		// log.info("omsCoFulfillDetailList size"+omsCoFulfillDetailList.size());
		int openQty = 0;
		for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
			log.info("checkOpenDeliveryForQuantity_Picked,inside fulfill order loop---" + omsCustOrdHead.getDeliveryType() + "----" + omsCoFulfillDetail.getSourceLocType());
			openQty = openQty + omsCoFulfillDetail.getFulfillReqQty().intValue() - (omsCoFulfillDetail.getFulfillDeliverQty().intValue() + omsCoFulfillDetail.getFulfillCancelQty().intValue());

			OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
			// OpenDeliveryBean openDeliveryBean=
			// omsUtilCommmons.findAvailableQtyForCancellation(omsCoFulfillDetail,
			// custOrderId);
			// log.info("openDeliveryBean.getQuantity()"+openDeliveryBean.getQuantity());
			OpenDeliveryBean openDeliveryBean1 = omsUtilCommmons.findQuantityPickedFromBoL(omsCoFulfillDetail, custOrderId);
			log.info("Quantity picked " + openDeliveryBean1.getQuantity());
			quantity_picked = quantity_picked + openDeliveryBean1.getQuantity();
			// quantity_picked=openDeliveryBean1.getQuantity();

		} // end of OmsCoFulfillDetail loop

		return quantity_picked;
	}

	public OpenDeliveryBean findAvailableQtyForCancellation(OmsCoFulfillDetail omsCoFulfillDetail, String custOrderId) {
		log.info("inside findAvailableQtyForCancellation ");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int external_Id = 0;
		String item = null;
		int quantity = 0;
		OpenDeliveryBean openDeliveryBean = new OpenDeliveryBean();
		log.info("omsCoFulfillDetail.getFulfillOrderNo() " + omsCoFulfillDetail.getFulfillOrderNo());
		log.info("Item " + omsCoFulfillDetail.getItem());
		log.info("custOrderId " + custOrderId);

		String query = " select a.external_id,b.item_id,d.quantity,e.id from ful_ord a, ful_ord_line_item b, ful_ord_dlv c, ful_ord_dlv_line_item d, shipment_bol e, SHIPMENT_CARRIER_SERVICE f  "
				+ " where b.ful_ord_id = a.id and a.cust_order_id=? " + " and b.item_id=? " + " and c.ful_ord_id = a.id " + " and d.ful_ord_dlv_id = c.id " + " and d.ful_ord_line_item_id = b.id "
				+ " and e.id = c.shipment_bol_id " + " and c.status != '4' " + // 4 means delivery not in cancel status
				" and a.external_id=? " + " and e.SHIP_CARRIER_SERVICE_ID = f.ID " + " and e.ship_carrier_id=f.SHIPMENT_CARRIER_ID " + " and f.DESCRIPTION='Handed to Carrier' "; // 2 = 'Handed to
																																													// Carrier'

		log.info("query " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderId);
			preparedStatement.setString(2, omsCoFulfillDetail.getItem());
			preparedStatement.setInt(3, omsCoFulfillDetail.getFulfillOrderNo().intValue());
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				external_Id = rs.getInt(1);
				log.info("external_Id " + external_Id);
				openDeliveryBean.setFulfillOrdNo(external_Id);
				log.info("omsCoFulfillDetail.getFulfillOrderNo() " + omsCoFulfillDetail.getFulfillOrderNo().intValue());
				item = rs.getString(2);
				log.info("item " + item);
				openDeliveryBean.setItem(item);
				if (item.equals(omsCoFulfillDetail.getItem()) && (external_Id == omsCoFulfillDetail.getFulfillOrderNo().intValue())) {
					log.info("equal");
					quantity = quantity + rs.getInt(3);
					log.info("quantity " + quantity);
					openDeliveryBean.setQuantity(quantity);
				}

			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return openDeliveryBean;
	}

	public void callRMSBackorderWS(String item, BigDecimal backOrdQty, long location, String locType, String unitOfMeasure, BigDecimal channelId)
			throws com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException, SOAPException {
		log.info("inside backorder");
		InventoryBackOrderService inventoryBackOrderService = new InventoryBackOrderService();
		InventoryBackOrderPortType inventoryBackOrderPortType = inventoryBackOrderService.getInventoryBackOrderPort();
		InvBackOrdColDesc invBackOrdColDesc = new InvBackOrdColDesc();
		invBackOrdColDesc.setCollectionSize(1);
		InvBackOrdDesc invBackOrdDesc = new InvBackOrdDesc();
		invBackOrdDesc.setItem(item);
		log.info("backOrdQty " + backOrdQty);
		invBackOrdDesc.setBackorderQty(backOrdQty);
		log.info("setted the backOrdQty ");
		if (locType.equals("ST")) {
			invBackOrdDesc.setLocType(invBackOrdDesc.getLocType().S);
		} else if (locType.equals("WH")) {
			invBackOrdDesc.setLocType(invBackOrdDesc.getLocType().W);

		}
		if (channelId.intValue() > 0) {
			invBackOrdDesc.setChannelId(channelId.intValue());
			log.info("Setting channel id to " + channelId + " for calling back order WS");
		}
		log.info("location " + location);
		invBackOrdDesc.setLocation(location);
		invBackOrdDesc.setUnitOfMeasure(unitOfMeasure);
		log.info("adding into invBackOrdDesc");
		invBackOrdColDesc.getInvBackOrdDesc().add(invBackOrdDesc);
		log.info("calling createInvBackOrdColDesc");
		try {
			inventoryBackOrderPortType.createInvBackOrdColDesc(invBackOrdColDesc);
		} catch (Exception e) {
			log.info("Exception in BO " + e.getMessage());
			// Added code for 3244 bug item_loc_soh lock
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			// OmsCustOrdHead
			// omsCustOrdHead=session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdItem.getOmsCustOrdNo());
			OmsRepublishData omsRepublishData = new OmsRepublishData();
			omsRepublishData.setApplicationId("E-COMMERCE");
			omsRepublishData.setErrorMsg("UNABLE TO CALL RMS BackOrder  WEB SERVICE.");
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			omsRepublishData.setTransactionKey((item + "-" + location));
			BigDecimal webserviceid = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_BACK_ORDER");
			omsRepublishData.setWebServiceId(webserviceid.toString());
			String start = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1\" xmlns:v11=\"http://www.oracle.com/retail/integration/base/bo/InvBackOrdColDesc/v1\" xmlns:v12=\"http://www.oracle.com/retail/integration/base/bo/InvBackOrdDesc/v1\">\n"
					+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <v1:createInvBackOrdColDesc>\n" + "          <v11:InvBackOrdColDesc>\n" + "            <v12:InvBackOrdDesc>";
			String xml = null;
			if (locType.equals("ST")) {
				xml = "<v12:item>" + item + "</v12:item>\n" + "               <v12:loc_type>" + invBackOrdDesc.getLocType().S + "</v12:loc_type>\n" + "               <v12:location>" + location
						+ "</v12:location>\n" + "               <v12:backorder_qty>" + backOrdQty + "</v12:backorder_qty>\n" + "               <v12:unit_of_measure>" + unitOfMeasure
						+ "</v12:unit_of_measure>";
			} else {
				xml = "<v12:item>" + item + "</v12:item>\n" + "               <v12:loc_type>" + invBackOrdDesc.getLocType().W + "</v12:loc_type>\n" + "               <v12:location>" + location
						+ "</v12:location>\n" + "               <v12:channel_id>" + channelId + "</v12:channel_id>\n" + "               <v12:backorder_qty>" + backOrdQty + "</v12:backorder_qty>\n"
						+ "               <v12:unit_of_measure>" + unitOfMeasure + "</v12:unit_of_measure>";
			}
			String end = " </v12:InvBackOrdDesc>\n" + "            <v11:collection_size>1</v11:collection_size>\n" + "         </v11:InvBackOrdColDesc>\n" + "      </v1:createInvBackOrdColDesc>\n"
					+ "   </soapenv:Body>\n" + "</soapenv:Envelope>";
			omsRepublishData.setXmlMsg(start + xml + end);
			log.info("XML " + start + xml + end);
			try {
				session.persistOmsRepublishData(omsRepublishData);
				log.info("persisting in omsRepublish data for RMS BackOrder");
			} catch (Exception f) {
				log.error("persisting in omsRepublish data failed" + f.getMessage());
			}
		}
		log.info("called createInvBackOrdColDesc ");
	}

	public void callSimSaveTransferRequest(long transferId, long lineId, String itemId, BigDecimal requestedQuantity, long sendingStore, long receivingStore)
			throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfReqModVo stsTsfReqModVo = new StsTsfReqModVo();
		stsTsfReqModVo.setTransferId(transferId);
		stsTsfReqModVo.setSendingStoreId(sendingStore);
		stsTsfReqModVo.setReceivingStoreId(receivingStore);
		List<StsTsfReqItmMod> stsTsfReqItmModList = stsTsfReqModVo.getStsTsfReqItmMod();
		StsTsfReqItmMod stsTsfReqItmMod = new StsTsfReqItmMod();
		stsTsfReqItmMod.setLineId(lineId);
		stsTsfReqItmMod.setItemId(itemId);
		stsTsfReqItmMod.setCaseSize(BigDecimal.ONE);
		stsTsfReqItmMod.setRequestedQuantity(requestedQuantity);
		stsTsfReqItmModList.add(stsTsfReqItmMod);
		StsTsfRef stsTsfRef = store2storeTransferPortType.saveTransferRequest(stsTsfReqModVo);

	}

	public void callSimSavePendingTransferRequest(long transferId, long lineId, String itemId, BigDecimal approvedQuantity)
			throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfApvModVo stsTsfApvModVo = new StsTsfApvModVo();
		stsTsfApvModVo.setTransferId(transferId);
		List<StsTsfApvItmMod> stsTsfApvItmModList = stsTsfApvModVo.getStsTsfApvItmMod();
		StsTsfApvItmMod stsTsfApvItmMod = new StsTsfApvItmMod();
		stsTsfApvItmMod.setLineId(lineId);

		stsTsfApvItmMod.setCaseSize(BigDecimal.ONE);
		stsTsfApvItmMod.setApprovedQuantity(approvedQuantity);
		stsTsfApvItmModList.add(stsTsfApvItmMod);
		store2storeTransferPortType.savePendingTransferRequest(stsTsfApvModVo);
	}

	public void callSimApproveTransfer(long transferId, long storeId) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfRef stsTsfRef = new StsTsfRef();
		stsTsfRef.setTransferId(transferId);
		stsTsfRef.setStoreId(storeId);
		InvocationSuccess invocationSuccess = store2storeTransferPortType.approveTransfer(stsTsfRef);
	}

	public StsTsfHdrColDesc callSimLookupTransferHeader(long storeId, long transferNo) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException, IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfHdrCriVo stsTsfHdrCriVo = new StsTsfHdrCriVo();
		stsTsfHdrCriVo.setStoreId(storeId);
		stsTsfHdrCriVo.setStatus(StsTsfCriStatus.NO_VALUE);
		stsTsfHdrCriVo.setExternalId(transferNo);
		StsTsfHdrColDesc stsTsfHdrColDesc = store2storeTransferPortType.lookupTransferHeader(stsTsfHdrCriVo);
		return stsTsfHdrColDesc;
	}

	public StsTsfDesc callSimReadTransferDetail(long storeId, long transferId) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfRef stsTsfRef = new StsTsfRef();
		stsTsfRef.setStoreId(storeId);
		stsTsfRef.setTransferId(transferId);
		StsTsfDesc stsTsfDesc = store2storeTransferPortType.readTransferDetail(stsTsfRef);
		return stsTsfDesc;
	}

	public long cancelApprovedTransferRequestForStore2Store(BigDecimal transferId, BigDecimal storeId)
			throws SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		log.info("inside cancelApprovedTransferRequestForStore2Store");
		StsTsfRef stsTsfRef = null;
		try {
			StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
			StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();

			stsTsfRef = new StsTsfRef();

			log.info("store_Id " + storeId);
			log.info("transferId " + transferId);
			stsTsfRef.setStoreId(storeId.longValue());
			stsTsfRef.setTransferId(transferId.longValue());

			InvocationSuccess invocationSuccess = store2storeTransferPortType.cancelTransfer(stsTsfRef);
			log.info("invocationSuccess " + invocationSuccess.getSuccessMessage());

			// return stsTsfRef.getTransferId();

		} catch (Exception e) {
			log.info("inside the catch block for cancelTransferRequestStore2Store " + e.getMessage());
		}
		return stsTsfRef.getTransferId();
	}

	public long rejectTransferRequestStore2Store(BigDecimal transferId, BigDecimal storeId)
			throws SOAPException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();

		StsTsfRef stsTsfRef = new StsTsfRef();
		try {
			log.info("store_Id " + storeId);
			log.info("transferId " + transferId);
			stsTsfRef.setStoreId(storeId.longValue());
			stsTsfRef.setTransferId(transferId.longValue());

			InvocationSuccess invocationSuccess = store2storeTransferPortType.rejectTransfer(stsTsfRef);
			log.info("invocationSuccess " + invocationSuccess.getSuccessMessage());

			// return stsTsfRef.getTransferId();

		} catch (Exception e) {
			log.info("inside the catch block for rejectTransferRequestStore2Store " + e.getMessage());
		}
		return stsTsfRef.getTransferId();
	}

	public String approveTransfers(BigDecimal tsfNo, BigDecimal sourceLoc) throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException, SOAPException {
		String status = "F";
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		try {
			String waitTime = session.getOmsSystemParametersFindIndValue("TSF_APPROVAL_WAIT_TIME", "OMS_SYSTEM_OPTION");
			log.info("waitTime" + waitTime);
			Thread.sleep(Long.valueOf(waitTime));
		} catch (InterruptedException e) {
			log.info("Exception while approving thread " + e.getMessage());
		}
		try {
			StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
			StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
			StsTsfHdrCriVo stsTsfHdrCriVo = new StsTsfHdrCriVo();
			stsTsfHdrCriVo.setStoreId(sourceLoc.longValue());
			stsTsfHdrCriVo.setStatus(StsTsfCriStatus.ACTIVE);
			stsTsfHdrCriVo.setExternalId(tsfNo.longValue());
			log.info("calling lookupTransferHeader");
			StsTsfHdrColDesc stsTsfHdrColDesc = store2storeTransferPortType.lookupTransferHeader(stsTsfHdrCriVo);
			log.info("stsTsfHdrColDesc.getCollectionSize()" + stsTsfHdrColDesc.getCollectionSize() + "sourceLoc=" + sourceLoc + "tsfNo=" + tsfNo);
			if (stsTsfHdrColDesc.getCollectionSize() != 0) {
				List<StsTsfHdrDesc> stsTsfHdrDescList = stsTsfHdrColDesc.getStsTsfHdrDesc();
				StsTsfHdrDesc StsTsfHdrDesc = stsTsfHdrDescList.get(0);
				StsTsfRef stsTsfRef = new StsTsfRef();
				stsTsfRef.setTransferId(StsTsfHdrDesc.getTransferId());
				stsTsfRef.setStoreId(StsTsfHdrDesc.getSendingStoreId());
				log.info("calling readTransferDetail");
				StsTsfDesc stsTsfDesc = store2storeTransferPortType.readTransferDetail(stsTsfRef);

				StsTsfApvModVo stsTsfApvModVo = new StsTsfApvModVo();
				stsTsfApvModVo.setTransferId(stsTsfDesc.getTransferId());
				List<StsTsfApvItmMod> stsTsfApvItmLsit = stsTsfApvModVo.getStsTsfApvItmMod();

				for (StsTsfItm stsTsfItm : stsTsfDesc.getStsTsfItm()) {
					StsTsfApvItmMod stsTsfApvItmMod = new StsTsfApvItmMod();
					stsTsfApvItmMod.setLineId(stsTsfItm.getLineId());
					stsTsfApvItmMod.setApprovedQuantity(stsTsfItm.getRequestedQuantity());
					stsTsfApvItmLsit.add(stsTsfApvItmMod);
					stsTsfApvModVo.getStsTsfApvItmMod().add(stsTsfApvItmMod);
				}
				log.info("calling savePendingTransferRequest");
				InvocationSuccess invocationSuccess = store2storeTransferPortType.savePendingTransferRequest(stsTsfApvModVo);
				log.info("calling approveTransfer");
				InvocationSuccess invocationSuccess1 = store2storeTransferPortType.approveTransfer(stsTsfRef);
				if (invocationSuccess1.getSuccessMessage().equals("Service Operation Complete")) {
					status = "A";
					log.info("Message Success Transfer approved");
				} else {
					log.info("transfer not approved");
				}

			}
		} catch (Exception e) {
			log.info("Failed in approve transfer");
		}
		return status;
	}

	public OpenDeliveryBean findQuantityPickedFromBoL(OmsCoFulfillDetail omsCoFulfillDetail, String custOrderId) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int external_Id = 0;
		String item = null;
		int quantity_Picked = 0;
		OpenDeliveryBean openDeliveryBean = new OpenDeliveryBean();

		String query = "select b.quantity_picked-b.quantity_delivered-b.quantity_canceled from ful_ord a, ful_ord_line_item b  "
				+ "where b.ful_ord_id = a.id and a.cust_order_id=? and b.item_id=? and a.external_id=?";

		log.info("query " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderId);
			preparedStatement.setString(2, omsCoFulfillDetail.getItem());
			preparedStatement.setInt(3, omsCoFulfillDetail.getFulfillOrderNo().intValue());
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				quantity_Picked = rs.getInt(1);
				log.info("quantity_Picked " + quantity_Picked);
				openDeliveryBean.setQuantity(quantity_Picked);

			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return openDeliveryBean;
	}

	public int checkOpenDeliveryForQuantity_PickedandDevelired(String custOrderId, BigDecimal omsCustOrdNo, OmsCustOrdItem input)
			throws SOAPException, com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("--> inside checkOpenDeliveryForQuantity_Picked for oms_cust_ord_no=" + omsCustOrdNo);
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		int handeOverToCourierQty = 0;
		log.info("inside for of item, omsCustOrderNo=" + omsCustOrdNo + "inputItem.getLineNo()" + input.getLineNo() + "inputItem.getItem()" + input.getItem());
		// omsCoFulfillDetailList =
		// session.getOmsCoFulfillDetailFindByItem(omsCustOrdNo,input.getLineNo(),
		// input.getItem());
		int quantity_picked = 0;

		try {
			List<OmsCoFulfillDetail> omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByOmsCustOrdNo(omsCustOrdNo);

			log.info("omsCoFulfillDetailList size " + omsCoFulfillDetailList.size());
			int openQty = 0;
			for (OmsCoFulfillDetail omsCoFulfillDetail : omsCoFulfillDetailList) {
				log.info("omsCoFulfillDetail.getLineNo() " + omsCoFulfillDetail.getLineNo());
				log.info("input.getLineNo() " + input.getLineNo());
				log.info("omsCoFulfillDetail.getSourceLoc() " + omsCoFulfillDetail.getSourceLoc());
				log.info("omsCoFulfillDetail.getFulfillLoc() " + omsCoFulfillDetail.getFulfillLoc());
				if (omsCoFulfillDetail.getLineNo().intValue() == input.getLineNo().intValue() && omsCoFulfillDetail.getSourceLoc().intValue() == omsCoFulfillDetail.getFulfillLoc().intValue()) {
					log.info("inside if condition for lineNo ,srcLoc and FullfillLoc");
					log.info("checkOpenDeliveryForQuantity_Picked,inside fulfill order loop---" + omsCustOrdHead.getDeliveryType() + "----" + omsCoFulfillDetail.getSourceLocType());
					openQty = openQty + omsCoFulfillDetail.getFulfillReqQty().intValue() - (omsCoFulfillDetail.getFulfillDeliverQty().intValue() + omsCoFulfillDetail.getFulfillCancelQty().intValue());

					OMSUtilCommons omsUtilCommmons = new OMSUtilCommons();
					// OpenDeliveryBean openDeliveryBean=
					// omsUtilCommmons.findAvailableQtyForCancellation(omsCoFulfillDetail,
					// custOrderId);
					// log.info("openDeliveryBean.getQuantity()"+openDeliveryBean.getQuantity());
					OpenDeliveryBean openDeliveryBean1 = findQuantityPickedandDeliveredFromBoL(omsCoFulfillDetail, custOrderId);
					log.info("Quantity picked " + openDeliveryBean1.getQuantity());
					quantity_picked = quantity_picked + openDeliveryBean1.getQuantity();
				}

			} // end of OmsCoFulfillDetail loop
		} catch (Exception e) {
			log.info("inside catch block " + e.getMessage());
		}

		return quantity_picked;
	}

	public OpenDeliveryBean findQuantityPickedandDeliveredFromBoL(OmsCoFulfillDetail omsCoFulfillDetail, String custOrderId) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int external_Id = 0;
		String item = null;
		int quantity_Picked = 0;
		OpenDeliveryBean openDeliveryBean = new OpenDeliveryBean();

		String query = "select b.quantity_picked  from ful_ord a, ful_ord_line_item b  " + "where b.ful_ord_id = a.id and a.cust_order_id=? and b.item_id=? and a.external_id=?";

		log.info("query " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderId);
			preparedStatement.setString(2, omsCoFulfillDetail.getItem());
			preparedStatement.setInt(3, omsCoFulfillDetail.getFulfillOrderNo().intValue());
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				quantity_Picked = rs.getInt(1);
				log.info("quantity_Picked " + quantity_Picked);
				openDeliveryBean.setQuantity(quantity_Picked);

			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return openDeliveryBean;
	}

	public int fetchingSIMFullfillOrderNo(String custOrderNo) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int external_Id = 0;
		String query = "select max(to_number(external_id))  from ful_ord where cust_order_id=? ";

		log.info("query " + query);
		try {

			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				external_Id = rs.getInt(1);
				log.info(" max external_Id " + external_Id);
			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return external_Id;
	}

	public int fetchingRMSFullfillOrderNo(String custOrderNo) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int fulfill_order_no = 0;
		String query = "select max(to_number(FULFILL_ORDER_NO))  from ordcust where customer_order_no=? ";

		log.info("query " + query);
		try {

			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, custOrderNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				fulfill_order_no = rs.getInt(1);
				log.info(" max fulfill_order_no " + fulfill_order_no);
			}

		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		return fulfill_order_no;
	}

	public BigDecimal getUnitRetailBySumofUnitDiscounts(BigDecimal omsCustOrdNo, BigDecimal lineNo, BigDecimal unitRetail) throws SOAPException {
		log.info("Inside getUnitRetailBySumofUnitDiscounts() ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal sumOfUnitDiscounts = BigDecimal.ZERO;
		try {
			log.info("calling getOmsCustOrdItemDiscfindByOmsCustordNoLineNoUnitDiscnt");
			log.info("*** omsCustOrdNo : " + omsCustOrdNo);
			log.info("*** lineNo : " + lineNo);
			log.info("*** unitRetail : " + unitRetail);
			sumOfUnitDiscounts = session.getOmsCustOrdItemDiscfindByOmsCustordNoLineNoUnitDiscnt(omsCustOrdNo, lineNo);
			if (sumOfUnitDiscounts != null) {
				log.info("*** sumOfUnitDiscounts : " + sumOfUnitDiscounts);
				unitRetail = unitRetail.subtract(sumOfUnitDiscounts);
				log.info("*** unitRetail :inside try block " + unitRetail);
			}
		} catch (Exception e) {
			sumOfUnitDiscounts = BigDecimal.ZERO;
			log.info("*** sumOfUnitDiscounts : " + sumOfUnitDiscounts);
			unitRetail = unitRetail.subtract(sumOfUnitDiscounts);
			log.info("*** unitRetail : inside catch block " + unitRetail);

		}
		log.info("Returning unit retail " + unitRetail);
		return unitRetail;
	}

	public boolean cancellingSingleTransferForMultipleItems(String custOrdNo, BigDecimal cancelQty, BigDecimal omsCustOrderNo, BigDecimal tsfNo, BigDecimal srcLoc, BigDecimal fulfilLoc)
			throws SOAPException {
		// coded for 2471 defect
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info(" inside cancellingSingleTransferForMultipleItems ");
		log.info("custOrdNo " + custOrdNo);
		log.info("omsCustOrderNo " + omsCustOrderNo);
		log.info("tsfNo " + tsfNo);
		log.info("cancelQty " + cancelQty);
		boolean flag = false;
		BigDecimal totalTransferQty = BigDecimal.ZERO;
		OmsTsfCancelledQtySumm omsTsfCancelledQtySumm = null;

		try {
			totalTransferQty = getOmsCoFulFillDetailSumofOrdQtyfindbyTsfNoandOmsCustOrdNo(omsCustOrderNo, tsfNo);
			log.info("totalCancelQty No. of tsfNo in " + tsfNo + "is " + totalTransferQty);
			omsTsfCancelledQtySumm = session.getOmsTsfCancelledQtySummfindByCustOrdNoTsfnoandOmsCustOrdNo(custOrdNo, tsfNo, omsCustOrderNo);
			log.info("record exist in OmsTsfCancelledQtySumm for tsfNo " + omsTsfCancelledQtySumm.getTsfNo());

		} catch (Exception e) {
			log.info("No record exist in OmsTsfCancelledQtySumm ");
		}

		if (omsTsfCancelledQtySumm == null) {
			log.info("persisting records into OmsTsfCancelledQtySumm");
			omsTsfCancelledQtySumm = new OmsTsfCancelledQtySumm();
			omsTsfCancelledQtySumm.setCustOrderNo(custOrdNo);
			log.info("setted customerOrder " + custOrdNo);
			omsTsfCancelledQtySumm.setOmsCustOrdNo(omsCustOrderNo);
			log.info("setted omsCustOrderNo " + omsCustOrderNo);
			omsTsfCancelledQtySumm.setTsfNo(tsfNo);
			log.info("setted tsfNo " + tsfNo);
			omsTsfCancelledQtySumm.setCumTsfQty(totalTransferQty);
			log.info("setted CumTsfQty " + totalTransferQty);
			omsTsfCancelledQtySumm.setCreateDatetime(new Timestamp(new Date().getTime()));
			log.info("setteed timestamp");
			omsTsfCancelledQtySumm.setFulfillloc(fulfilLoc);
			log.info("setted fulfilLoc " + fulfilLoc);
			omsTsfCancelledQtySumm.setSourceloc(srcLoc);
			log.info("setted srcLoc " + srcLoc);
			omsTsfCancelledQtySumm.setTsfApprovalStatus("A");
			log.info("setted TsfApprovalStatus A");
			try {
				session.persistOmsTsfCancelledQtySumm(omsTsfCancelledQtySumm);
				log.info("successfully persisted in omsTsfCancelledQtySumm ");
			} catch (Exception e) {
				log.info("Exception while persisting in to omsTsfCancelledQtySumm " + e.getMessage());
			}

		}
		omsTsfCancelledQtySumm = session.getOmsTsfCancelledQtySummfindByCustOrdNoTsfnoandOmsCustOrdNo(custOrdNo, tsfNo, omsCustOrderNo);
		log.info("omsTsfCancelledQtySumm.getCumTsfQty() " + omsTsfCancelledQtySumm.getCumTsfQty());
		log.info("cancelQty " + cancelQty);
		BigDecimal remaingQty = omsTsfCancelledQtySumm.getCumTsfQty().subtract(cancelQty);
		omsTsfCancelledQtySumm.setCumTsfQty(remaingQty);
		omsTsfCancelledQtySumm.setUpdateDatetime(new Timestamp(new Date().getTime()));
		session.mergeOmsTsfCancelledQtySumm(omsTsfCancelledQtySumm);
		log.info("updated the CumTsfQty to  " + remaingQty);
		if (remaingQty.intValue() <= 0) {
			log.info("Deleting the transfer from  omsTsfCancelledQtySumm table " + tsfNo);
			// omsTsfCancelledQtySumm.setTsfApprovalStatus("C");
			// omsTsfCancelledQtySumm.setCloseDatetime(new Timestamp(new Date().getTime()));
			// session.mergeOmsTsfCancelledQtySumm(omsTsfCancelledQtySumm);
			flag = true;
			session.removeOmsTsfCancelledQtySumm(omsTsfCancelledQtySumm);
		} else {
			log.info("still transfer qty is not 0 so setting the flag false for tsfNo " + tsfNo);
			flag = false;
		}

		log.info("returning flag " + flag);

		return flag;
	}

	public BigDecimal getOmsCoFulFillDetailSumofOrdQtyfindbyTsfNoandOmsCustOrdNo(BigDecimal omsCustOrdNo, BigDecimal tsfNo) throws Exception {
		log.info("inside getOmsCoFulFillDetailSumofOrdQtyfindbyTsfNoandOmsCustOrdNo ");
		log.info("omsCustOrderNo " + omsCustOrdNo);
		log.info("tsfNo " + tsfNo);
		BigDecimal totalTransferQty = BigDecimal.ZERO;
		String query = " select sum(FULFILL_CONF_QTY-(FULFILL_DELIVER_QTY+FULFILL_CANCEL_QTY)) from oms_co_fulfill_detail where oms_cust_ord_no=? and TSF_NO=? ";
		log.info("query " + query);

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setBigDecimal(1, omsCustOrdNo);
			preparedStatement.setBigDecimal(2, tsfNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				log.info("equal");
				totalTransferQty = rs.getBigDecimal(1);
				log.info(" totalTransferQty " + totalTransferQty);
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
		return totalTransferQty;
	}

	// Added code for bug 2603

	public Long sohCalculation(BigDecimal soh, String item, BigDecimal location) throws SOAPException {
		log.info("<-------------Inside sohCalculation method-------------->");
		log.info("<-------------SOH : " + soh + "-------------->");
		log.info("<---------------item : " + item + "--------------->");
		log.info("<----------------location : " + location + "----------------->");
		BigDecimal sumofQuantity = BigDecimal.ZERO;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("calling getQuantityOmsPaymentSyncBasedOnItemAndLoc to get the sumofQuantity ");
		sumofQuantity = session.getQuantityOmsPaymentSyncBasedOnItemAndLoc(item, location);
		if (sumofQuantity == null) {
			sumofQuantity = BigDecimal.ZERO;
		}

		log.info("<---------sumofQuantity--------->" + sumofQuantity);
		try {
			log.info("<------Stock on hand before Calcualtion ");
			if (soh.intValue() != 0 && sumofQuantity.intValue() != 0) {
				soh = soh.subtract(sumofQuantity);
				log.info("<------Stock on hand after Calculation : " + soh + "--------->");
			} else if (sumofQuantity.intValue() == 0) {
				// soh = BigDecimal.ZERO;
				log.info("sumofQuantity is 0 hence returning the actual SOH of SIM");
			} else if (soh.intValue() <= 0) {
				soh = BigDecimal.ZERO;
				log.info("SOH is less than 0, hence setting it to 0");
			}
			BigDecimal unapproveQty = BigDecimal.ZERO;
			try {
				List<OmsUnapprovedTransfers> omsUnapprovedTransfersList = session.getOmsUnapprovedTransfersFindByQtyByLoc(item, location);
				for (OmsUnapprovedTransfers omsUnapprovedTransfers : omsUnapprovedTransfersList) {
					unapproveQty = unapproveQty.add(omsUnapprovedTransfers.getUnapprovedQty());
				}
				log.info("unapproveQty=" + unapproveQty);
			} catch (Exception e) {
				log.info("");
			}
			soh = soh.subtract(unapproveQty);
			if (soh.intValue() < 0) {
				soh = BigDecimal.ZERO;
			}
		} catch (Exception e) {
			log.info("Inside Catch Block : " + e.getMessage());
			soh = BigDecimal.ZERO;
		}
		log.info("<----------------Final Value of SOH : " + soh + "--------------------------->");
		return soh.longValue();
	}

	public BigDecimal sohCalculationforWH(BigDecimal soh, String item, List<BigDecimal> location) throws SOAPException {
		log.info("<-------------------inside sohCalculationforWH method---------------------->");
		log.info("SOH " + soh);
		log.info("item " + item);
		log.info("location " + location.toString());
		BigDecimal sumofQuantity = BigDecimal.ZERO;
		BigDecimal sumofLocationQty = BigDecimal.ZERO;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			log.info("calling getQuantityOmsPaymentSyncBasedOnItemAndLoc to get the sumofLocationQty ");
			for (BigDecimal locs : location) {
				sumofLocationQty = session.getQuantityOmsPaymentSyncBasedOnItemAndLoc(item, locs);
				if (sumofLocationQty == null) {
					sumofLocationQty = BigDecimal.ZERO;
				}
				log.info("sumofLocationQty : " + sumofLocationQty + "in location" + locs);
				sumofQuantity = sumofLocationQty.add(sumofQuantity);
				log.info("****in for loop : " + sumofQuantity);
			}
			log.info("<---------sumofQuantity--------->" + sumofQuantity);
			log.info("<------Stock on hand before Calcualtion ");

			if (soh.intValue() != 0 && sumofQuantity.intValue() != 0) {
				soh = soh.subtract(sumofQuantity);
				log.info("<------Stock on hand after Calculation : " + soh + "--------->");
			} else if (sumofQuantity.intValue() == 0) {
				// soh = BigDecimal.ZERO;
				log.info("sumofQuantity is 0 hence returning the actual SOH of DAS");
			} else if (soh.intValue() <= 0) {
				soh = BigDecimal.ZERO;
				log.info("SOH is less than 0, hence setting it to 0");
			}
		} catch (Exception e) {
			log.info("Inside Catch Block : " + e.getMessage());
			soh = BigDecimal.ZERO;
		}
		log.info("<----------------Final Value of SOH : " + soh + "--------------------------->");

		return soh;

	}

	public BigDecimal getMaxFulFilOrderNoFromRMSOrdCust(String customerOrderNo) throws SOAPException {
		log.info("inside getMaxFulFilOrderNoFromOrdCust " + customerOrderNo);
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String fulfilOrdNo = null;
		BigDecimal maxFulFilOrdNo = BigDecimal.ZERO;
		log.info("calling getMaxFulFilOrdNofromOrdCust" + customerOrderNo);
		try {
			fulfilOrdNo = session.getMaxFulFilOrdNofromOrdCust(customerOrderNo);
			maxFulFilOrdNo = new BigDecimal(fulfilOrdNo);
			log.info("maxFulFilOrdNo from OrdCust for customerOrderNo " + customerOrderNo + "is" + fulfilOrdNo);
		} catch (Exception e) {
			maxFulFilOrdNo = BigDecimal.ZERO;
			log.info("Exception while getting the maxFulFillOrdNo " + e.getMessage());
		}

		log.info("Returning maxFulFilOrdNo " + maxFulFilOrdNo);
		return maxFulFilOrdNo;
	}

	public BigDecimal getMaxFulFilOrderNoFromSIMFulOrd(String customerOrderNo) throws SOAPException {
		log.info("inside getMaxFulFilOrderNoFromOrdCust " + customerOrderNo);
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int fulfilOrdNo = 0;
		BigDecimal simMaxFulFilOrdNo = BigDecimal.ZERO;
		log.info("calling fetchingSIMFullfillOrderNo" + customerOrderNo);
		try {
			fulfilOrdNo = fetchingSIMFullfillOrderNo(customerOrderNo);
			simMaxFulFilOrdNo = new BigDecimal(fulfilOrdNo);
			log.info("simMaxFulFilOrdNo from fulOrd for customerOrderNo " + customerOrderNo + "is" + simMaxFulFilOrdNo);
		} catch (Exception e) {
			simMaxFulFilOrdNo = BigDecimal.ZERO;
			log.info("Exception while getting the simMaxFulFilOrdNo " + e.getMessage());
		}

		log.info("Returning simMaxFulFilOrdNo " + simMaxFulFilOrdNo);
		return simMaxFulFilOrdNo;
	}

	public int returnMaxFulFilOrdNoECOM(String customerOrderNo) throws SOAPException {
		int maxfulFilOrdNo = 0;
		log.info("inside returnMaxFulFilOrdNo " + customerOrderNo);
		int simMaxFulFilOrdNo = 0;
		int rmsMaxFulFilOrdNo = fetchingRMSFullfillOrderNo(customerOrderNo);
		log.info("simMaxFulFilOrdNo " + simMaxFulFilOrdNo);
		log.info("rmsMaxFulFilOrdNo " + rmsMaxFulFilOrdNo);
		maxfulFilOrdNo = Math.max(simMaxFulFilOrdNo, rmsMaxFulFilOrdNo);
		log.info("maxfulFilOrdNo value from SIM and RMS is " + maxfulFilOrdNo);
		if (maxfulFilOrdNo == 0) {
			log.info("maxfulFilOrdNo is equal to 0 ,assigning it to 1");
			maxfulFilOrdNo = 1;
		} else if (maxfulFilOrdNo > 0) {
			maxfulFilOrdNo = maxfulFilOrdNo + 1;
		}
		log.info("Before returning the maxfulFilOrdNo " + maxfulFilOrdNo);
		return maxfulFilOrdNo;
	}

	public int returnMaxFulFilOrdNo(String customerOrderNo) throws SOAPException {
		int maxfulFilOrdNo = 0;
		log.info("inside returnMaxFulFilOrdNo " + customerOrderNo);
		int simMaxFulFilOrdNo = getMaxFulFilOrderNoFromSIMFulOrd(customerOrderNo).intValue();
		int rmsMaxFulFilOrdNo = fetchingRMSFullfillOrderNo(customerOrderNo);
		log.info("simMaxFulFilOrdNo " + simMaxFulFilOrdNo);
		log.info("rmsMaxFulFilOrdNo " + rmsMaxFulFilOrdNo);
		maxfulFilOrdNo = Math.max(simMaxFulFilOrdNo, rmsMaxFulFilOrdNo);
		log.info("maxfulFilOrdNo value from SIM and RMS is " + maxfulFilOrdNo);
		if (maxfulFilOrdNo == 0) {
			log.info("maxfulFilOrdNo is equal to 0 ,assigning it to 1");
			maxfulFilOrdNo = 1;
		} else if (maxfulFilOrdNo > 0) {
			maxfulFilOrdNo = maxfulFilOrdNo + 1;
		}
		log.info("Before returning the maxfulFilOrdNo " + maxfulFilOrdNo);
		return maxfulFilOrdNo;
	}

	// OMS fulfill cancel QTy updateion block

	public void updateCancelQtyinOMS(FulfilOrdRef fulfilOrdRef) {
		log.info("OMS Cnacel Qty update Block");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String storeCheck = "";

		if (fulfilOrdRef.getFulfillLocType().toString().equals("S")) {
			log.info("OMS Cnacel Qty update for ST");
			storeCheck = "";

		} else {
			log.info("OMS Cnacel Qty update for WH");
			storeCheck = " SOURCE_LOC_TYPE=?  and SOURCE_LOC=? and  ";
		}

		String query = "update oms_co_fulfill_detail set FULFILL_CANCEL_QTY=? \n"
				+ " where OMS_CUST_ORD_NO in(SELECT OMS_CUST_ORD_NO FROM oms_cust_ord_head where  cust_order_no = ? and status='S') \n" + " and FULFILL_ORDER_NO =? and ITEM=? and " + storeCheck
				+ " FULFILL_LOC_TYPE=? and FULFILL_LOC=? ";
		log.info("query : " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);

			for (int i = 0; i < fulfilOrdRef.getFulfilOrdDtlRef().size(); i++) {
				int j = 0;
				log.info("can_qty" + fulfilOrdRef.getFulfilOrdDtlRef().get(i).getCancelQtySuom().intValue());
				log.info("order No" + fulfilOrdRef.getCustomerOrderNo());
				log.info("Fulfill No" + fulfilOrdRef.getFulfillOrderNo());
				log.info("Fulfill No" + fulfilOrdRef.getFulfilOrdDtlRef().get(i).getItem());
				log.info("FulfillLocType" + fulfilOrdRef.getFulfillLocType().toString());
				log.info("LocId" + fulfilOrdRef.getFulfillLocId());
				preparedStatement.setInt(++j, fulfilOrdRef.getFulfilOrdDtlRef().get(i).getCancelQtySuom().intValue());
				preparedStatement.setString(++j, fulfilOrdRef.getCustomerOrderNo());
				preparedStatement.setString(++j, fulfilOrdRef.getFulfillOrderNo());
				preparedStatement.setString(++j, fulfilOrdRef.getFulfilOrdDtlRef().get(i).getItem());
				if (!fulfilOrdRef.getFulfillLocType().toString().equals("S")) {
					log.info("cancel if source location is WH");
					preparedStatement.setString(++j, fulfilOrdRef.getSourceLocType().toString());
					preparedStatement.setLong(++j, fulfilOrdRef.getSourceLocId());
				}
				preparedStatement.setString(++j, fulfilOrdRef.getFulfillLocType().toString());
				preparedStatement.setLong(++j, fulfilOrdRef.getFulfillLocId());
				int result = preparedStatement.executeUpdate();

				if (result > 0) {
					log.info("Cnacel Qty update successfully in OMS" + result);
				} else {
					log.info("Error while updateing cancel qty in OMS ");
				}

			}

		} catch (Exception e1) {
			log.info("Error while updateing cancel qty in OMS " + e1.getMessage());
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}

	} // OMS fulfill cancel QTy updateion block

	// Added method for Rollback

	public void rollback(String customerOrderNo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, BusinessException {
		log.info("Inside the rollback method for RMS WebService");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<Ordcust> ordcustList = session.getOrdcustFindByCustomerOrderNoandStatus(customerOrderNo);
		log.info("<--------- List of Ordcust Table : " + ordcustList.size() + " --------->");
		log.info("<--------- List Contains : " + ordcustList.toString() + " ---------->");
		FulfillOrderService fulfillOrderService = null;
		try {
			fulfillOrderService = new FulfillOrderService();
		} catch (Exception e) {
			log.error("Error while creating the proxy object for RMS base webservice. ", e);
			throw new BusinessException("RMS_UNAVL");
		}
		FulfillOrderPortType fulfillOrderPortType = fulfillOrderService.getFulfillOrderPort();
		int rmsAttemptCount = 1;
		if (ordcustList.size() > 0) {
			for (Ordcust list : ordcustList) {
				rmsAttemptCount = 1;
				FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
				fulfilOrdColRef.setCollectionSize(1);
				FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
				// call ordcustdetail table
				log.info("Calling OrdCust_Detail Table .... " + list.getOrdcustNo());
				// fulfilOrdRef=callOrdCustDetail(list.getOrdcustNo(),fulfilOrdRef);

				List<OrdcustDetail> ordcustDetailList = session.getOrdcustDetailFindByOrdCustNo(list.getOrdcustNo());
				log.info("Size of ordcustDetailList : " + ordcustDetailList.size());

				for (OrdcustDetail ordcustDetail : ordcustDetailList) {
					FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
					log.info("Cancel Quantity in fulfilOrdDtlRef.getCancelQtySuom() :" + fulfilOrdDtlRef.getCancelQtySuom());
					if (fulfilOrdDtlRef.getCancelQtySuom() == null) {
						log.info("Inside IF loop");
						fulfilOrdDtlRef.setCancelQtySuom(ordcustDetail.getQtyOrderedSuom());
						log.info("OrdCustNo : " + ordcustDetail.getOrdcustNo() + "<-------Cancel Qty : " + ordcustDetail.getQtyOrderedSuom() + " --------->");
						fulfilOrdDtlRef.setItem(ordcustDetail.getItem());
						log.info("OrdCustNo : " + ordcustDetail.getOrdcustNo() + "<-------- Item : " + ordcustDetail.getItem() + "--------->");
						// fulfilOrdDtlRef.setRefItem(value);
						fulfilOrdDtlRef.setTransactionUom(ordcustDetail.getTransactionUom());
						fulfilOrdDtlRef.setStandardUom(ordcustDetail.getStandardUom());
						fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
					}
				}
				log.info("Table OrdCust_Detail Processed ....");

				// Added code for SIM unapproved transfer
				if (list.getTsfNo() != null) {
					log.info("Transfer Number from Ordcust Table : " + list.getTsfNo());
					rollbackForTransfer(list.getTsfNo().toString());
					log.info("Transfer Table Processed ....");
				}
				log.info("customerOrderNo : " + customerOrderNo + "+<----------CustomerOrderNo : " + customerOrderNo + " --------------->");
				fulfilOrdRef.setCustomerOrderNo(customerOrderNo);
				log.info("customerOrderNo : " + customerOrderNo + "+<---------FulfilLoc Id : " + list.getFulfillLocId().longValue() + " ------------>");
				fulfilOrdRef.setFulfillLocId(list.getFulfillLocId().longValue());
				log.info("customerOrderNo : " + customerOrderNo + "<------FulfilLocType : " + list.getFulfillLocType() + " ----------->");
				fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(list.getFulfillLocType()));
				log.info("customerOrderNo : " + customerOrderNo + "<-------Fulfil Order Number : " + list.getFulfillOrderNo() + " --------->");
				fulfilOrdRef.setFulfillOrderNo(String.valueOf(list.getFulfillOrderNo()));

				if (list.getSourceLocId() != null) {
					log.info("customerOrderNo : " + customerOrderNo + "<-----SourceLoc Id : " + list.getSourceLocId().longValue() + " --------->");
					fulfilOrdRef.setSourceLocId(list.getSourceLocId().longValue());
					log.info("customerOrderNo : " + customerOrderNo + "<---------Source Loc Type : " + list.getSourceLocType() + " --------->");
					fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(list.getSourceLocType()));
				}

				log.info("list.getOrdcustNo() " + list.getOrdcustNo());
				fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
				log.error("Double Fufillemnt  changes block for RMS" + fulfilOrdRef.getSourceLocType());
				for (int i = 0; i < rmsAttemptCount; i++) {
					try {
						InvocationSuccess result = fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
						log.info("Result from RMS ---> " + result.getSuccessMessage());
						rmsAttemptCount = 0;
						if (fulfilOrdRef.getSourceLocType().toString().equals("WH")) {
							updateCancelQtyinOMS(fulfilOrdRef);
						}
					} catch (Exception e) {
						log.info("Exception " + e.getMessage());
						log.info("RMS Failed Attemt Count " + rmsAttemptCount);
						if (rmsAttemptCount == 6) {
							rmsAttemptCount = 0;

						} else {
							rmsAttemptCount = rmsAttemptCount + 1;
						}

					}
				}
				log.info(">>> RMS Completed");
				try {
					rollbackforSIMOrder(customerOrderNo, fulfilOrdRef);
					log.info(">>> SIM Completed");
				} catch (Exception e) {
					log.warn("customerOrderNo " + customerOrderNo + "Exception while calling SIM rollback ", e);
					if (e instanceof BusinessException) {
						throw (BusinessException) e;
					}
				}
			}
		}
		/*
		 * else if(ordcustList.size()==0) {
		 * log.info("No records in RMS for customerOrderNo"
		 * +customerOrderNo+"cancelling in SIM "); try { FulfilOrdRef fulfilOrdRef = new
		 * FulfilOrdRef(); rollbackforSIM(customerOrderNo,fulfilOrdRef);
		 * log.info("Rollback is success for SIM cancellation for customerOrderNo "
		 * +customerOrderNo); } catch(Exception e) {
		 * log.info("Exception in cancellation for customerOrderNo"+customerOrderNo+
		 * e.getMessage()); }
		 * 
		 * }
		 */

		// added code for SIM rollback for 3121 bug
		try {
			log.info("customerOrderNo " + customerOrderNo + "Irrespective of cancellation calling SIM");
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
			rollbackforSIM(customerOrderNo, fulfilOrdRef);
			log.info("Rollback is success for SIM cancellation for customerOrderNo " + customerOrderNo);
		} catch (Exception e) {
			log.warn("Exception in cancellation for customerOrderNo" + customerOrderNo, e);
			if (e instanceof BusinessException) {
				throw (BusinessException) e;
			}
		}

	} // End of rollbackRMS Method

	public void rollbackforSIM(String customerOrderNo, FulfilOrdRef fulfilOrdRef)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		log.info("<----------------------->");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int id = 0;
		String query = "select * from ful_ord where cust_order_id=? and status in(0,1)";
		log.info("query : " + query);

		StoreFulfillmentOrderService storeFulfillmentOrderService = null;
		try {
			storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		} catch (Exception e) {
			log.error("Error while creating the proxy object for RMS base webservice. ", e);
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
		}
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, customerOrderNo);
			rs = preparedStatement.executeQuery();
			FulfilOrdColRef fulfilOrdColRef1 = null;
			while (rs.next()) {

				fulfilOrdColRef1 = new FulfilOrdColRef();
				log.info("Address of fulfilOrdColRef1 " + fulfilOrdColRef1);
				log.info("Address Ful Ord Ref --->" + fulfilOrdColRef1.getFulfilOrdRef() + "Size is" + fulfilOrdColRef1.getFulfilOrdRef().size());
				fulfilOrdColRef1.setCollectionSize(1);
				fulfilOrdColRef.value = fulfilOrdColRef1;
				log.info("equal");
				id = rs.getInt("id");
				// fulfilOrdColRef.value = fulfilOrdColRef1;
				log.info("customerOrderNo " + customerOrderNo);
				fulfilOrdRef.setCustomerOrderNo(customerOrderNo);
				fulfilOrdRef.setFulfillLocId(rs.getLong("STORE_ID"));
				log.info("rs.getLong(STORE_ID) " + rs.getLong("STORE_ID"));
				fulfilOrdRef.setFulfillLocType(FulfillLocType.S);
				log.info("fulfill LocType is S");
				fulfilOrdRef.setFulfillOrderNo(rs.getString("EXTERNAL_ID"));
				log.info("fulfillorderNo " + rs.getString("EXTERNAL_ID"));
				log.info("the id from table ful_ord : " + id);
				log.info("<---The address of ful ord ref address Before---->" + fulfilOrdRef + "<-->");
				fulfilOrdRef = sendFulOrdLineItemSIM(id, fulfilOrdRef);
				log.info("<---The address of ful ord ref address After---->" + fulfilOrdRef + "<-->");
				log.info("----------fulfilOrdRef----------- " + fulfilOrdRef.getFulfilOrdDtlRef().size());
				for (FulfilOrdDtlRef fulfilOrdDtl : fulfilOrdRef.getFulfilOrdDtlRef()) {
					log.info("***********Dtl item " + fulfilOrdDtl.getItem());
					log.info("*********Dtl CancelQty " + fulfilOrdDtl.getCancelQtySuom());
				}
				log.info("fulfilOrdRef " + fulfilOrdRef.getCustomerOrderNo());
				fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
				try {
					log.info("fulfilOrdColRef size=" + fulfilOrdColRef.value.getFulfilOrdRef().size());
					log.info("fulfilOrdRef.getFulfilOrdDtlRef().size()---- " + fulfilOrdRef.getFulfilOrdDtlRef().size());
					log.info("---" + fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());
					storeFulfillmentOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);
					log.info(">>>");
					log.info("<---------After calling SIM------------>");
					for (FulfilOrdDtlRef fulfilOrdDtl : fulfilOrdColRef.value.getFulfilOrdRef().get(0).getFulfilOrdDtlRef()) {
						log.info("-----------Dtl item " + fulfilOrdDtl.getItem());
						log.info("----------Dtl CancelQty " + fulfilOrdDtl.getCancelQtySuom());
					}
				} catch (Exception e) {
					log.error("Error in sim cancellation");
				}

			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}

	}

	public FulfilOrdRef sendFulOrdLine(int id, FulfilOrdRef fulfilOrdRef) {
		log.info("Inside sendFulOrdLine method for table ful_ord_line_item");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "select * from ful_ord_line_item where ful_ord_id = ?";
		log.info("query : " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			while (rs.next()) {
				log.info("equal");
				id = rs.getInt("FUL_ORD_ID");
				log.info("the id from table ful_ord_line_item : " + id);
				// add fulfilOrdDtlRef
				if (fulfilOrdDtlRef.getCancelQtySuom().intValue() == 0 || fulfilOrdDtlRef.getCancelQtySuom() == null) {
					fulfilOrdDtlRef.setCancelQtySuom(rs.getBigDecimal("QUANTITY_ORDERED"));
					log.info("<-------Cancel Qty : " + rs.getBigDecimal("QUANTITY_ORDERED") + " --------->");
					fulfilOrdDtlRef.setItem(rs.getString("ITEM_ID"));
					log.info("<------ Item : " + rs.getString("ITEM_ID") + "--------->");
					// fulfilOrdDtlRef.setRefItem(value);
					fulfilOrdDtlRef.setTransactionUom("EA");
					fulfilOrdDtlRef.setStandardUom("EA");
					fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
				}
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		log.info("<----- Returning data from table ful_ord_line_item ------>");
		return fulfilOrdRef;
	} // End of sendFulOrdLine

	// Added code for Rollback in SIM unapproved Transfer

	public void rollbackForTransfer(String external_id) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "select * from transfer where EXTERNAL_ID = ? ";
		log.info("query : " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, external_id);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				external_id = rs.getString("EXTERNAL_ID");
				log.info("the id from table transfer : " + external_id);
				int id = rs.getInt("ID");
				log.info("ID from the transfer table : " + id);
				int create_store_id = rs.getInt("CREATE_STORE_ID");
				log.info("Create Store ID from transfer table : " + create_store_id);
				// LookupTransferHeader WS
				log.info("<--------- Calling callSimLookupTransferHeader WS ---------->");
				callSimLookupTransferHeader(create_store_id, id);
				// ReadTransferDetail WS
				log.info("<--------- Calling callSimReadTransferDetail WS ---------->");
				callSimReadTransferDetail(create_store_id, id);
				// CancelRequest WS
				log.info("<--------- Calling rejectTransferRequestStore2Store WS ---------->");
				rejectTransferRequestStore2Store(new BigDecimal(id), new BigDecimal(create_store_id));
				log.info("The quantity is cancelled from transfer table .... ");
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		log.info("<----- Returning data from table transfer ------>");

	} // End of method rollbackForTransfer

	public FodHdrColDesc isDeliveryCreated(long intFulfillmentOrderId) throws com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException

	{

		FulfillmentOrderDeliveryService fulfillmentOrderDeliveryService = new FulfillmentOrderDeliveryService();
		FulfillmentOrderDeliveryPortType fulfillmentOrderDeliveryPortType = fulfillmentOrderDeliveryService.getFulfillmentOrderDeliveryPort();
		StrFordRef strFordRef = new StrFordRef();
		strFordRef.setIntFulfillmentOrderId(intFulfillmentOrderId);
		FodHdrColDesc fodHdrColDesc = fulfillmentOrderDeliveryPortType.lookupFulfillmentOrderDeliveryHeaders(strFordRef);
		fodHdrColDesc.getFodHdrDesc();
		return fodHdrColDesc;
	}

	public void cancelDelivery(FodHdrColDesc fodHdrColDesc) throws com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException {
		FulfillmentOrderDeliveryService fulfillmentOrderDeliveryService = new FulfillmentOrderDeliveryService();
		FulfillmentOrderDeliveryPortType fulfillmentOrderDeliveryPortType = fulfillmentOrderDeliveryService.getFulfillmentOrderDeliveryPort();

		for (FodHdrDesc fodHdrDesc : fodHdrColDesc.getFodHdrDesc()) {
			if (fodHdrDesc.getStatus().toString().equals("CANCELED") == false && fodHdrDesc.getStatus().toString().equals("COMPLETED") == false) {
				FodRef fodRef = new FodRef();
				fodRef.setDeliveryId(fodHdrDesc.getIntFulfillOrderDeliveryId());
				InvocationSuccess invocationSuccess = fulfillmentOrderDeliveryPortType.cancelFulfillmentOrderDelivery(fodRef);
				log.info("Delivery id " + fodHdrDesc.getIntFulfillOrderDeliveryId() + " is cancelled successfully");
			}
		}

	}

	public void cancelPendingTransfer(long transferId, long lineId, String itemId, BigDecimal requestedQuantity, long sendingStore, long receivingStore)
			throws com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ValidationWSFaultException {
		StoreToStoreTransferService store2storeTransferService = new StoreToStoreTransferService();
		StoreToStoreTransferPortType store2storeTransferPortType = store2storeTransferService.getStoreToStoreTransferPort();
		StsTsfReqModVo stsTsfReqModVo = new StsTsfReqModVo();
		stsTsfReqModVo.setTransferId(transferId);
		stsTsfReqModVo.setSendingStoreId(sendingStore);
		stsTsfReqModVo.setReceivingStoreId(receivingStore);
		stsTsfReqModVo.getRemovedLineIdCol().add(lineId);
		StsTsfRef stsTsfRef = store2storeTransferPortType.saveTransferRequest(stsTsfReqModVo);

	}

	public void detectQuantityfromUnapprovedTransfers(BigDecimal omscustOrdNo, BigDecimal transferNo, String item, BigDecimal location, BigDecimal quantity) throws SOAPException {
		log.info("omscustOrdNo " + omscustOrdNo + "inside detectQuantityfromUnapprovedTransfers");
		log.info("omscustOrdNo " + omscustOrdNo + "transferNo " + transferNo + "item " + item + "location " + location + "quantity " + quantity);
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal pendingQty = BigDecimal.ZERO;
		try {
			OmsUnapprovedTransfers omsUnapprovedTransfers = session.getOmsUnapprovedTransfersFindByOmsCustOrderNo(transferNo, item, location);
			pendingQty = omsUnapprovedTransfers.getUnapprovedQty();
			log.info("pendingQty before subtracting " + pendingQty);
			pendingQty = pendingQty.subtract(quantity);
			log.info("pendingQty after subtracting " + pendingQty);
			if (pendingQty.intValue() <= 0) {
				log.info("pending qty is equal to 0 delete the records from omsUnapprovedTransfers");
				session.removeOmsUnapprovedTransfers(omsUnapprovedTransfers);
			} else {
				log.info("pending qty is not equal to 0 update the records from omsUnapprovedTransfers");
				omsUnapprovedTransfers.setUnapprovedQty(pendingQty);
				omsUnapprovedTransfers.setLastUpdateDatetime(new Timestamp(new Date().getTime()));
				session.mergeOmsUnapprovedTransfers(omsUnapprovedTransfers);
			}
		} catch (Exception e) {
			log.info("Exception occured calling getOmsUnapprovedTransfersFindByOmsCustOrderNo");
		}

	}

	public void deleteApprovedTsffromUnapprovedTsfTable(ArrayList<BigDecimal> transferList, BigDecimal omsCustOrdNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		if (transferList != null && transferList.size() > 0) {
			for (BigDecimal tsfNo : transferList) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "transferNo " + tsfNo);
				try {
					List<OmsUnapprovedTransfers> omsUnapprovedTransfersList = session.getOmsUnapprovedTransfersfindByTsfNoandOmsCustOrdNo(tsfNo, omsCustOrdNo);
					if (omsUnapprovedTransfersList != null && omsUnapprovedTransfersList.size() > 0) {
						for (OmsUnapprovedTransfers omsUnapprovedTransfers : omsUnapprovedTransfersList) {
							log.info("Deleting the record from omsUnapprovedTransfers for omsCustOrdNo" + omsCustOrdNo + "transferNo " + tsfNo);
							session.removeOmsUnapprovedTransfers(omsUnapprovedTransfers);
						}
					}
				} catch (Exception e) {
					log.info("Exception while deleting the records from omsUnapprovedTransfers");
				}
			}
		}
	}

	public void rollbackforSIMOrder(String customerOrderNo, FulfilOrdRef fulfilOrdRef)
			throws SOAPException, EntityAlreadyExistsWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int id = 0;
		int simAttemptCount = 1;
		String query = "select * from ful_ord where cust_order_id=? and status in(0,1) and EXTERNAL_ID=?";
		log.info("query : " + query);

		StoreFulfillmentOrderService storeFulfillmentOrderService = null;
		try {
			storeFulfillmentOrderService = new StoreFulfillmentOrderService();
		} catch (Exception e) {
			log.error("Error while creating the proxy object for RMS base webservice. ", e);
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
		}
		StoreFulfillmentOrderPortType storeFulfillmentOrderPortType = storeFulfillmentOrderService.getStoreFulfillmentOrderPort();
		Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
		FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
		fulfilOrdColRef1.setCollectionSize(1);
		fulfilOrdColRef.value = fulfilOrdColRef1;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, customerOrderNo);
			preparedStatement.setString(2, fulfilOrdRef.getFulfillOrderNo());
			log.info("fulfilOrdRef.getFulfillOrderNo() " + fulfilOrdRef.getFulfillOrderNo());
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				simAttemptCount = 1;
				log.info("equal");
				id = rs.getInt("id");
				fulfilOrdColRef.value = fulfilOrdColRef1;
				fulfilOrdRef.setCustomerOrderNo(customerOrderNo);
				fulfilOrdRef.setFulfillLocId(rs.getLong("STORE_ID"));
				fulfilOrdRef.setFulfillLocType(fulfilOrdRef.getFulfillLocType().fromValue("S"));
				fulfilOrdRef.setFulfillOrderNo(rs.getString("EXTERNAL_ID"));
				log.info("the id from table ful_ord : " + id);
				fulfilOrdRef = sendFulOrdLine(id, fulfilOrdRef);
				fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
				fulfilOrdColRef.value = fulfilOrdColRef1;

				log.error("Double Fufillemnt  changes block for SIM");
				for (int i = 0; i < simAttemptCount; i++) {
					try {
						log.info("fulfilOrdColRef size=" + fulfilOrdColRef.value.getFulfilOrdRef().size());
						log.info("---" + fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());
						storeFulfillmentOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);
						simAttemptCount = 0;
						if (fulfilOrdRef.getFulfillLocType().toString().equals("S")) {
							updateCancelQtyinOMS(fulfilOrdRef);
						}
						log.info(">>>");
					} catch (Exception e) {
						log.error("Error in sim cancellation : " + e.getMessage());
						log.info("SIM Failed Attemt Count " + simAttemptCount);
						if (simAttemptCount == 6) {
							simAttemptCount = 0;
						} else {
							simAttemptCount = simAttemptCount + 1;
						}
					}
				}
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}

	} // End of RollbackforSIM

	public FulfilOrdRef sendFulOrdLineItemSIM(int id, FulfilOrdRef fulfilOrdRefHeader) {
		log.info("Inside sendFulOrdLine method for table ful_ord_line_item");
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "select * from ful_ord_line_item where ful_ord_id = ? ";
		log.info("query : " + query);
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			// List<FulfilOrdDtlRef> fulfilOrdDtlRefList = new ArrayList<FulfilOrdDtlRef>();
			while (rs.next()) {
				FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
				log.info("equal");
				id = rs.getInt("FUL_ORD_ID");
				log.info("the id from table ful_ord_line_item : " + id);
				// add fulfilOrdDtlRef
				int i = 0;
				fulfilOrdDtlRef.setCancelQtySuom(rs.getBigDecimal("QUANTITY_ORDERED"));
				log.info("<-------Cancel Qty : " + rs.getBigDecimal("QUANTITY_ORDERED") + " --------->");
				fulfilOrdDtlRef.setItem(rs.getString("ITEM_ID"));
				log.info("<------ Item : " + rs.getString("ITEM_ID") + "--------->");
				fulfilOrdDtlRef.setTransactionUom("EA");
				fulfilOrdDtlRef.setStandardUom("EA");
				// fulfilOrdDtlRefList.add(fulfilOrdDtlRef);
				fulfilOrdRefHeader.getFulfilOrdDtlRef().add(i, fulfilOrdDtlRef);
				i++;
				// fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);

			}
			log.info("Size of list from fulOrdLine Item is " + fulfilOrdRefHeader.getFulfilOrdDtlRef().size());
			for (FulfilOrdDtlRef fulfilOrdDtl : fulfilOrdRefHeader.getFulfilOrdDtlRef()) {
				log.info("Dtl item " + fulfilOrdDtl.getItem());
				log.info("Dtl CancelQty " + fulfilOrdDtl.getCancelQtySuom());

			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}
		log.info("<----- Returning data from table ful_ord_line_item ------>");
		return fulfilOrdRefHeader;
	} // End of sendFulOrdLine

	public static String getLanguageCodeOfOmsCustomerOrderNo(BigDecimal OmsCustomerOrderNo) throws SOAPException {
		String languageCode = "1";
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			// Error occured while fetching the language code for a given customer order no.
			languageCode = session.getOmsCustOrdHeadFindLanguage(OmsCustomerOrderNo);
		} catch (Exception e) {
			return languageCode;
		}
		return languageCode;
	}

	public static Boolean checkErrorCodeExistOrNot(String errorCode, String languageCode) throws SOAPException {
		log.info("<----------Error Code ---" + errorCode + "----- Language Code<---->" + languageCode);
		OmsErrorCodes omsErrorCodes = null;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		try {
			omsErrorCodes = session.getOmsErrorCodesFindByErrorCode(errorCode, languageCode);
		} catch (Exception e) {
			// Exception occuered means Record doesn't exist for the given languge we need
			// to Search in Basic langauge.
			return false;
		}
		// This means Error Records Exist for the given specific language
		return true;
	}

	public static OmsErrorCodes getOmsErrorCodesObject(String errorCode, String languageCode) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsErrorCodes omsErrorCode = null;
		try {
			omsErrorCode = session.getOmsErrorCodesFindByErrorCode(errorCode, languageCode);
		} catch (Exception e) {
			// Below piece of code gets exceuted when it is unable to fetch record in
			// omsErrorCodes table for a given
			// error Code and langauge code
			omsErrorCode = session.getOmsErrorCodesFindByErrorCode(errorCode, OmsErrorCodesConstant.baseLanguageCodeValue);
		}
		return omsErrorCode;
	}

	public static String getLanguageCodeByCustomerOrderNo(String customerOrderNo) throws SOAPException {
		log.info("<--- Given Customer Order No ----->" + customerOrderNo + "<----->");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsCustomerOrderNo = null;
		String langugeCode = "1";
		try {
			// fetch the omscustOrdNo of a given customer order No......
			log.info("<---------------Fetch the  omscustOrderNo------>");
			omsCustomerOrderNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
		} catch (Exception e) {
			log.info("Unable to fetch the omscustOrdNo  for given customer order No---->");
			log.info("Return the language code value is" + langugeCode);
			return langugeCode;
		}

		try {
			log.info("<---Fetch the language Code from oms cust Ord head table -------->");
			// fetch the language code of a given customer order No..
			langugeCode = getLanguageCodeOfOmsCustomerOrderNo(omsCustomerOrderNo);
		} catch (Exception e) {
			log.info(" Before returning the language code value is -->" + langugeCode);
			return langugeCode;
		}
		return langugeCode;
	}

	public Map<BigDecimal, BigDecimal> getCancelRequestQtyForALineNo(String customerOrderNo) {
		String sqlQuery = "select ocitem.LINE_NO ,  sum( ocitem.CANCEL_REQ_QTY)\n" + "from OMS_CO_CANCEL_HEAD  ochead,  OMS_CO_CANCEL_ITEM ocitem \n"
				+ "where ochead.OMS_CANCEL_ID = ocitem.OMS_CANCEL_ID \n" + "and ochead.CUST_ORD_NO=? \n" + "and ochead.REFUND_OPTION='ORPOS'\n" + "and ochead.REFUND_COMPLT_IND='N'\n"
				+ "and ocitem.CANCEL_CONF_QTY>0\n" + "group by ocitem.LINE_NO ";
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Map<BigDecimal, BigDecimal> ResultSetMap = new HashMap<BigDecimal, BigDecimal>();
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(sqlQuery);
			preparedStatement.setString(1, customerOrderNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				ResultSetMap.put(rs.getBigDecimal(1), rs.getBigDecimal(2));
			}

		} catch (Exception e) {
			log.info(" Exception Message " + e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());

			}
		}

		return ResultSetMap;

	}

	public Map<BigDecimal, BigDecimal> getCancelRequestQtyForALineNoFromRMA(String customerOrderNo) {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Map<BigDecimal, BigDecimal> resultSetItemMap = new HashMap<BigDecimal, BigDecimal>();
		log.info("<----------Customer Order No --->" + customerOrderNo);
		String sqlQuery = "select   ormitem.LINE_NO  , sum(ormitem.RMA_QTY)\n" + " from  OMS_RMA_REQ omreq  , OMS_RMA_REQ_ITEM ormitem \n" + " where omreq.RMA_ID = ormitem.RMA_ID \n"
				+ " and omreq.CUST_ORDER_NO = ?\n" + " and omreq.REFUND_OPTION='ORPOS'\n" + " and omreq.REFUND_COMPLT_IND ='N'\n" + " and ormitem.ORIG_RMA_QTY>0\n" + " and omreq.RETURN_STATUS='Y'\n"
				+ " group by ormitem.LINE_NO";
		log.info("Sql Query -->" + sqlQuery + "<--->");
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("<--- Connection established successfully ---->");
			preparedStatement = conn.prepareStatement(sqlQuery);
			log.info("<------------Prepare of sql Query ------>");
			preparedStatement.setString(1, customerOrderNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				log.info("<----------------- Inside while loop--------------->");
				resultSetItemMap.put(rs.getBigDecimal(1), rs.getBigDecimal(2));
			}
		} catch (Exception e) {
			log.info(" error occured -->" + e);
			log.info(" Message is " + e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
		return resultSetItemMap;
	}

	/* Start Vat Changes @tsultana */

	public String getTaxableIndicator(String item, BigDecimal loc) throws Exception {
		log.info("***Start getTaxableInd ***");
		String taxableInd = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Check from DAS schema for TAXABLE_IND
		String query = "select TAXABLE_IND from ITEM_LOC where ITEM =? and LOC=?";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setBigDecimal(2, loc);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				taxableInd = rs.getString("TAXABLE_IND");
				if (taxableInd == null) {
					taxableInd = "N";
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return taxableInd;
	}

	public BigDecimal getVatRegion(BigDecimal loc) throws Exception {
		log.info("***Start Vat Region ***");
		BigDecimal vatRegion = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// Get Vat_region for the location
		String query = "select VAT_REGION from STORE where STORE=?";
		log.info("Sql Query -->" + query + "<--->");
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setBigDecimal(1, loc);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vatRegion = rs.getBigDecimal("VAT_REGION");
				log.info("Vat Region" + vatRegion);
				if (vatRegion != null) {
					return vatRegion;
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return vatRegion;
	}

	public String getVatCode(String item, BigDecimal vatRegion, Timestamp orderCreateDate) throws Exception {
		log.info("***Start getVatCode ***");
		String vatCode = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "select VAT_CODE from VAT_ITEM where ITEM =? and VAT_REGION=? and active_date =(SELECT MAX(active_date) from VAT_ITEM where ITEM =? and VAT_REGION=? and active_date <= ?)";
		log.info("Sql Query -->" + query + "<--->");
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, item);
			preparedStatement.setBigDecimal(2, vatRegion);
			preparedStatement.setString(3, item);
			preparedStatement.setBigDecimal(4, vatRegion);
			preparedStatement.setTimestamp(5, orderCreateDate);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vatCode = rs.getString("VAT_CODE");
				log.info("Vat Code" + vatCode);
				if (vatCode == null) {
					vatCode = "N";
				}

			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}
		return vatCode;
	}

	public OmsTaxDesc getOmsTaxDetails(String vatCode, BigDecimal vatRegion, Timestamp orderCreateDate) throws Exception {
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		OmsTaxDesc omsTaxDesc = new OmsTaxDesc();
		log.info("<----------Vat Code --->" + vatCode);
		String sqlQuery = "select TAX_AUTHORITY_ID, TAX_GROUP_ID,TAX_TYPE_CODE, TAX_HOLIDAY_FLAG," + "INCLUSIVE_TAX_FLAG,TAX_MODE, TAX_MOD_REASON_CODE,TAX_MOD_SCOPE, TAX_RATE, TAX_RULE_NAME, "
				+ "TAX_AUTHORITY_NAME from OMS_TAX_DESC where VAT_CODE = ? and VAT_REGION = ? and ACTIVE_DATE = (SELECT MAX(active_date) from OMS_TAX_DESC where VAT_CODE =? and VAT_REGION=? and active_date <= ?)";

		log.info("Sql Query -->" + sqlQuery + "<--->");
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("<--- Connection established successfully ---->");
			preparedStatement = conn.prepareStatement(sqlQuery);
			log.info("<------------Prepare of sql Query ------>");
			preparedStatement.setString(1, vatCode);
			preparedStatement.setBigDecimal(2, vatRegion);
			preparedStatement.setString(3, vatCode);
			preparedStatement.setBigDecimal(4, vatRegion);
			preparedStatement.setTimestamp(5, orderCreateDate);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				log.info("<----------------- Inside while loop--------------->");
				omsTaxDesc.setTaxAuthorityId(rs.getBigDecimal("TAX_AUTHORITY_ID"));
				omsTaxDesc.setTaxGroupId(rs.getBigDecimal("TAX_GROUP_ID"));
				omsTaxDesc.setTaxTypeCode(rs.getBigDecimal("TAX_TYPE_CODE"));
				omsTaxDesc.setTaxHolidayFlag(rs.getString("TAX_HOLIDAY_FLAG"));
				omsTaxDesc.setInclusiveTaxFlag(rs.getString("INCLUSIVE_TAX_FLAG"));
				omsTaxDesc.setTaxMode(rs.getString("TAX_MODE"));
				omsTaxDesc.setTaxModReasonCode(rs.getString("TAX_MOD_REASON_CODE"));
				omsTaxDesc.setTaxModScope(rs.getString("TAX_MOD_SCOPE"));
				omsTaxDesc.setTaxRate(rs.getBigDecimal("TAX_RATE"));
				omsTaxDesc.setTaxRuleName(rs.getString("TAX_RULE_NAME"));
				omsTaxDesc.setTaxAuthorityName(rs.getString("TAX_AUTHORITY_NAME"));
				return omsTaxDesc;
			}
		} catch (Exception e) {
			log.info(" error occured -->" + e);
			log.info(" Mesasge is " + e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}
		return omsTaxDesc;
	}
	/* End Vat Changes @tsultana */

	/* below code to get the order is express delivey order */

	public Boolean getExpressDelvdetails(String OrderNo) {

		log.info("***Start getExpressDelvdetails ***" + OrderNo);
		Boolean flag = Boolean.FALSE;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("<--- Connection established successfully ---->");
			preparedStatement = conn.prepareStatement(OMSConstants.OMS_GET_EXPRESS_DLV);

			log.info("<------------After Prepare statement------>");
			preparedStatement.setString(1, OrderNo);

			rs = preparedStatement.executeQuery();
			log.info("<------------After executing the query------>");
			while (rs.next()) {
				if ("1".equals(rs.getString(1))) {
					log.info("<------------Inside result set if condition------>");
					flag = Boolean.TRUE;
					log.info("<------------after setting true flag------>");
				}
			}
		} catch (Exception e) {
			log.info(" error occured -->" + e);
			log.info(" Mesasge is " + e.getMessage());

		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		log.info("***End of  getExpressDelvdetails ***" + OrderNo);
		return flag;
	}

	/* If tender has Coupon */

	public Boolean checkTenderType(BigDecimal OmsCustno) {

		log.info("***Start Boolean checkTenderType ***" + OmsCustno);

		Connection conn = null;
		Boolean flag = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String tender_type = "";

		String orderTender = "select TENDER_TYPE_GROUP from oms_cust_ord_tender where oms_cust_ord_no = ?" + "and TENDER_TYPE_GROUP = 'COUPON' ";

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("<--- Connectionn established successfully ---->");
			preparedStatement = conn.prepareStatement(orderTender);

			log.info("<------------Afterr Prepared statement------>");
			preparedStatement.setBigDecimal(1, OmsCustno);

			rs = preparedStatement.executeQuery();

			log.info("<------------Afterr executing the query------>");

			if (!rs.isBeforeFirst()) {
				flag = false;
			} else {
				flag = true;
			}
			log.info(" flag value:: " + flag);
		} catch (Exception e) {
			log.info(" error occured -->" + e);
			log.info(" Mesasge is " + e.getMessage());

		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);

			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

		log.info("***End of  getCouponBalance ***" + OmsCustno);
		return flag;
	}

	/* Code to get Coupon Balance */

	public Integer getCouponBalance(BigDecimal OmsCustno, String custOrderId) throws SQLException {

		log.info("***Starttt getCouponBalance ***" + OmsCustno);

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;
		PreparedStatement preparedStatement5 = null;

		ResultSet rs1 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		log.info("***Inside getCouponBalance1 ***" + OmsCustno);
		Integer CancelAmt = 0, CouponTender = 0, CouponTenderBal = 0, ReturnQty = 0, ReturnAmt = 0, returnVal = 0;
		// Integer CouponTender = 0; Integer CouponTenderBal =0; Integer ReturnQty = 0;
		Integer lineNo = 0;
		Integer unitRtl = 0;
		Integer discVal = 0;
		log.info("***Inside getCouponBalance2 ***" + OmsCustno);
		String orderTender = "select tender_amt from oms_cust_ord_tender where tender_type_group = 'COUPON' " + "and oms_cust_ord_no = ?";
		String cancelTender1 = "select sum(REFUND_AMOUNT) from OMS_CO_CANCEL_HEAD where CUST_ORD_NO = ? " + "and REFUND_COMPLT_IND = 'Y'";
		String returnTender = "select sum(REFUND_AMOUNT) from oms_rma_req where CUST_ORDER_NO = ? " + "and REFUND_COMPLT_IND = 'Y'";
		String returnDet = "select sum(return_quantity), line_item_no from oms_orpos_master_audit where event_id = 'RT' " + "and ORDER_ID = ?" + "group by line_item_no";
		log.info("***Inside getCouponBalance3 ***" + OmsCustno);
		try {

			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("<--- Connectionnn established successfully ---->");
			preparedStatement = conn.prepareStatement(orderTender);
			preparedStatement1 = conn.prepareStatement(cancelTender1);
			preparedStatement2 = conn.prepareStatement(returnTender);
			preparedStatement3 = conn.prepareStatement(returnDet, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			// preparedStatement3 = conn.prepareStatement(returnDet);
			log.info("<------------Afterrr Prepared statement------>");

			preparedStatement.setBigDecimal(1, OmsCustno);
			preparedStatement1.setString(1, custOrderId);
			preparedStatement2.setString(1, custOrderId);
			preparedStatement3.setString(1, custOrderId);
			log.info("<------------Afterrr setting parameter in Prepared statement------>");

			rs = preparedStatement.executeQuery();
			rs2 = preparedStatement1.executeQuery();
			rs1 = preparedStatement2.executeQuery();
			rs3 = preparedStatement3.executeQuery();
			log.info("<------------Afterrr executing the Prepared statement------>");

			if (rs.isBeforeFirst()) {
				if (rs.next()) {
					CouponTender = rs.getInt(1);
				}
			}
			if (rs2.isBeforeFirst()) {
				if (rs2.next()) {
					CancelAmt = rs2.getInt(1);
				}
			}
			if (rs1.isBeforeFirst()) {
				if (rs1.next()) {
					ReturnAmt = rs1.getInt(1);
				}
			}
			int rowCount = 0, i = 0;
			int[][] retDetails = new int[2][2];
			if (rs3.isBeforeFirst()) {
				if (rs3.last()) {
					log.info("<------------Insidee orpos master audit Resultset------>");
					rowCount = rs3.getRow();
					rs3.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first
										// element
				}
				retDetails = new int[rowCount][2];
				while (rs3.next()) {
					log.info("<------------Inside orpos master audit while------>");
					ReturnQty = 1;
					// lineNo = rs3.getInt(2);
					retDetails[i][0] = rs3.getInt(1);
					retDetails[i][1] = rs3.getInt(2);
					i++;
				}
			}
			int[] unitRtlList = new int[rowCount];
			int[] discValList = new int[rowCount];
			String returnUnitRtl = "";
			String discAmt = "";
			for (i = 0; i < rowCount; i++) {
				log.info("<------------Fetching unit retail from Item table ------>");
				returnUnitRtl = "select unit_retail from oms_cust_ord_item where oms_cust_ord_no = ? " + "and line_no = '" + retDetails[i][1] + "'";
				preparedStatement4 = conn.prepareStatement(returnUnitRtl);
				preparedStatement4.setBigDecimal(1, OmsCustno);
				rs4 = preparedStatement4.executeQuery();
				if (rs4.isBeforeFirst()) {
					if (rs4.next()) {
						unitRtlList[i] = rs4.getInt(1);
					}
				}
			}
			for (i = 0; i < rowCount; i++) {
				log.info("<------------Fetching Discount details ------>");
				discAmt = "select sum(unit_discount_amount) from OMS_CUST_ORD_ITEM_DISC where oms_cust_ord_no = ? and line_no =  '" + retDetails[i][1] + "'";
				preparedStatement5 = conn.prepareStatement(discAmt);
				preparedStatement5.setBigDecimal(1, OmsCustno);
				rs5 = preparedStatement5.executeQuery();
				discValList[i] = 0;
				if (rs5.isBeforeFirst()) {
					if (rs5.next()) {
						discValList[i] = rs5.getInt(1);
					}
				}
			}

			if (!rs1.isBeforeFirst() && (!rs2.isBeforeFirst())) {
				log.info("<------------No cancel and Return data exists------>");
				CouponTenderBal = CouponTender;
			} else if (rs2.isBeforeFirst() || rs1.isBeforeFirst()) {
				log.info("<------------Cancel and Return data exists------>");
				if (CancelAmt >= CouponTender) {
					CouponTenderBal = 0;
				} else {
					CouponTenderBal = CouponTender - CancelAmt;
				}
				if (ReturnAmt >= CouponTenderBal) {
					CouponTenderBal = 0;
				} else {
					CouponTenderBal = CouponTenderBal - ReturnAmt;
				}
				log.info("<------------Inside Cancel and Return data Ends------>" + CouponTenderBal);
			}

			if ((ReturnQty > 0) || CouponTenderBal > 0) {
				log.info("<------------ORPOS Return data exists------>" + CouponTenderBal + ":::" + ReturnQty);
				for (i = 0; i < rowCount; i++) {
					returnVal += (unitRtlList[i] - discValList[i]) * (retDetails[i][0]);
				}
				if (returnVal >= CouponTenderBal) {
					CouponTenderBal = 0;
				} else {
					CouponTenderBal = CouponTenderBal - returnVal;
				}
				log.info("<------------ORPOS Return data Ends------>" + CouponTenderBal);
			}
			log.info("Final coupon tender minus amount: " + CouponTenderBal);
			return CouponTenderBal;
		} catch (Exception e) {
			log.info(" error occured -->" + e);
			log.info(" Mesasge is " + e.getMessage());

		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
				OMSUtil.closeDBConnection(conn, preparedStatement1, rs2);
				OMSUtil.closeDBConnection(conn, preparedStatement2, rs1);
				OMSUtil.closeDBConnection(conn, preparedStatement3, rs3);
				OMSUtil.closeDBConnection(conn, preparedStatement4, rs4);
				OMSUtil.closeDBConnection(conn, preparedStatement5, rs5);
			} catch (Exception e2) {
				log.error(e2);
			}
		}
		log.info("***End of  getCouponBalance ***" + OmsCustno);
		return CouponTenderBal;
	}

} // End of class OMSUtilCommons
