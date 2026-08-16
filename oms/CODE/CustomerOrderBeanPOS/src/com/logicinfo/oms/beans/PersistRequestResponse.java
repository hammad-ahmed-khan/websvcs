package com.logicinfo.oms.beans;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import java.math.BigDecimal;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPException;
import org.apache.log4j.Logger;

public class PersistRequestResponse {

	public PersistRequestResponse() {
		super();
	}

	private final static Logger log = Logger.getLogger(PersistRequestResponse.class.getName());

	public void persistRequestAndResponse(BigDecimal omsOrposCustOrderNo, CustOrderDesc request, CustOrderDesc response) throws SOAPException {
		log.info("***insode persistRequestAndResponse ***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String value = session.getOmsSystemParametersFindIndValue("ORDER_CREATE", "OMS_ORPOS_ORDER");
		System.out.println("Value " + value);
		log.info("omsOrposCustOrderNo " + omsOrposCustOrderNo);
		log.info("CustomerOrderId " + request.getCustomerOrderId());
		log.info("Status " + String.valueOf(response.getOrderStatus()));
		if (session.getOmsSystemParametersFindIndValue("ORDER_CREATE", "OMS_ORPOS_ORDER").equalsIgnoreCase("Y")) {
			Connection conn = null;
			PreparedStatement preparedStatement = null;
			ResultSet rs = null;
			String insertQuery = "Insert into Oms_Orpos_HST values (?,?,?,?,?,?,?)";
			try {
				conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
				preparedStatement = conn.prepareStatement(insertQuery);
				preparedStatement.setBigDecimal(1, omsOrposCustOrderNo);
				preparedStatement.setString(2, request.getCustomerOrderId());
				preparedStatement.setString(3, "Oms_Orpos_Order_Creation");
				preparedStatement.setString(4, String.valueOf(response.getOrderStatus()));
				Blob reqBlobForCreate = conn.createBlob();
				log.info("Converting the String to Blob object for request for service plan  for create");
				if (request != null) {
					reqBlobForCreate.setBytes(1, request.toString().getBytes());
				}
				log.info("setting the to Blob object for request for service plan for create");
				preparedStatement.setBlob(5, reqBlobForCreate);
				log.info("creating Blob object for response for service plan for create");
				Blob respBlobForCreate = conn.createBlob();
				if (response != null) {
					respBlobForCreate.setBytes(1, response.toString().getBytes());
				}
				preparedStatement.setBlob(6, respBlobForCreate);
				preparedStatement.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));
				preparedStatement.executeUpdate();
				log.info("inserted into Oms_Orpos_HST");
				// only when the order is success/filled add promise date
				if (String.valueOf(response.getOrderStatus()).equalsIgnoreCase("FILLED")) {
					fetchItemFromOmsCustOrdItem(request.getCustomerOrderId(), request);
				}
			} catch (Exception e) {
				log.warn("Exception while persisting into the table Oms_Orpos_HST ", e);
			} finally {
				try {
					OMSUtil.closeDBConnection(conn, preparedStatement, rs);
				} catch (Exception e) {
					log.warn(e.getMessage(), e);
				}
			}
		}
	}

	private String getDeliveryLeadTimeForSMALLorBIG(String city, String classification, BigDecimal requestorId, int maxpriority) throws SOAPException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int leadTime = 0;
		String addedTime = null;
		String query = "select * from OMS_CUST_ORDER_DLT where city=? and classification=? and REQUESTOR_ID=? and PRIORITY=? AND STATUS = 'A'";
		log.info("query " + query);
		log.info("city " + city);
		log.info("classification " + classification);
		log.info("initiate_loc_id " + requestorId);
		log.info("maxpriority " + maxpriority);
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city);
			pstmt.setString(2, classification);
			pstmt.setBigDecimal(3, requestorId); // this initiate_loc_id from soap
			pstmt.setInt(4, maxpriority);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getBigDecimal("DELV_LEAD_TIME") != null) {
					leadTime = rs.getBigDecimal("DELV_LEAD_TIME").intValue();
					log.info("lead time from DB " + leadTime);
				} else {
					leadTime = 5;// if date is null set it to 5
					log.info("lead time from DB is null setting it to " + leadTime);
				}
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Using today's date
			c.add(Calendar.DATE, leadTime);
			addedTime = sdf.format(c.getTime());
			log.info("lead time after add with system date " + addedTime);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for deliveryLeadTime " + e.getMessage());
		} finally {
			OMSUtil.closeDBConnection(con, pstmt, rs);
		}
		return addedTime;
	}

	private String getPromiseDateForPREORDERSMALL(String city, String classification, String item) throws SOAPException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Date prom_date = null;
		String query = " select DELV_DATE from OMS_CUST_ORDER_DELV_DATE where city=? and classification=? AND ITEM = ? AND STATUS = 'A'";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city);
			pstmt.setString(2, classification);
			pstmt.setString(3, item);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				prom_date = rs.getDate("DELV_DATE");
			}
			log.info("deliveryDate " + prom_date);
		} catch (Exception e) {
			log.error("Exception while estabilishing the connection for preordersmall ", e);
		} finally {
			OMSUtil.closeDBConnection(con, pstmt, rs);
		}
		return prom_date.toString();
	}

	private int getMaxpriorityFromFulFillMatrix(String city, String classification, long loc_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int max_priority = 0;
		String query = "select max(PRIORITY) from oms_fulfill_matrix_ext_detail where COMBINATION_ID in(select COMBINATION_ID from oms_fulfill_matrix_ext_head where CUSTOMER_CITY=? and SHIP_CLASSIFICATION=? and REQUESTOR_ID=?) ";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city);
			pstmt.setString(2, classification);
			pstmt.setLong(3, loc_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getBigDecimal("max(PRIORITY)") != null) {
					max_priority = rs.getBigDecimal("max(PRIORITY)").intValue();
				}
			}
			if (max_priority == 0) {
				query = "select max(PRIORITY) from oms_fulfill_matrix_ext_detail where COMBINATION_ID in(select COMBINATION_ID from oms_fulfill_matrix_ext_head where CUSTOMER_CITY='ALL' and SHIP_CLASSIFICATION=? and REQUESTOR_ID=?) ";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, classification);
				pstmt.setLong(2, loc_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getBigDecimal("max(PRIORITY)") != null) {
						max_priority = rs.getBigDecimal("max(PRIORITY)").intValue();
					}
				}
			}
			log.info("Max priority " + max_priority);
		} catch (Exception e) {
			log.error("Exception while estabilishing the connection for Max priority ", e);
		} finally {
			OMSUtil.closeDBConnection(con, pstmt, rs);
		}
		return max_priority;
	}

	private void fetchItemFromOmsCustOrdItem(String customerOrderId, CustOrderDesc request) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int maxPriority = 0;
		String promiseDate = null;

		// Fetch the record only when the status is S in head table
		BigDecimal omscustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderId);
		List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByCustOrdNo(omscustOrdNo);
		for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
			if (omsCustOrdItem.getShipClassification().equalsIgnoreCase("SMALL")) {
				maxPriority = getMaxpriorityFromFulFillMatrix(request.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getCity(),
						omsCustOrdItem.getShipClassification(), request.getInitiateLocId());
				String delCity = request.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getCity();
				if (!(delCity.equalsIgnoreCase("319")
						|| delCity.equalsIgnoreCase("PREORDER"))) {
					promiseDate = getDeliveryLeadTimeForSMALLorBIG(request.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getCity(),
							omsCustOrdItem.getShipClassification(), BigDecimal.valueOf(request.getInitiateLocId()), maxPriority);
				} else {
					promiseDate = getPromiseDateForPREORDERSMALL(request.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getCity(),
							omsCustOrdItem.getShipClassification(), omsCustOrdItem.getItem());
				}
			}
			omsCustOrdItem.setExpectedDeliveryDate(Timestamp.valueOf(promiseDate));
			session.mergeOmsCustOrdItem(omsCustOrdItem);
		}

	}
}
