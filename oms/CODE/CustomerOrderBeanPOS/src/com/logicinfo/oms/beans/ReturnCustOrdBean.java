package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdLog;
import com.logicinfo.oms.ejb.OmsCustOrdLogItem;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItmRtn;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsOrposCustOrderRtn;
import com.logicinfo.oms.ejb.OmsOrposDiscntLine;
import com.logicinfo.oms.ejb.OmsOrposDiscntLineRtn;
import com.logicinfo.oms.ejb.OmsOrposMasterAudit;
import com.logicinfo.oms.ejb.OmsOrposTaxLine;
import com.logicinfo.oms.ejb.OmsOrposTaxLineRtn;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OmsOrderStatusUpdateHeader;
import com.oracle.retail.integration.base.bo.custordercolref.v1.CustOrderColRef;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;
import com.oracle.retail.integration.base.bo.custorderrtncolvo.v1.CustOrderRtnColVo;
import com.oracle.retail.integration.base.bo.custorderrtnvo.v1.CustOrderRtnVo;
import com.oracle.retail.integration.base.bo.custorditmrtcolvo.v1.CustOrdItmRtColVo;
import com.oracle.retail.integration.base.bo.custorditmrtvo.v1.CustOrdItmRtVo;
import com.oracle.retail.integration.base.bo.discntlinertcolvo.v1.DiscntLineRtColVo;
import com.oracle.retail.integration.base.bo.discntlinertvo.v1.DiscntLineRtVo;
import com.oracle.retail.integration.base.bo.taxlinertcolvo.v1.TaxLineRtColVo;
import com.oracle.retail.integration.base.bo.taxlinertvo.v1.TaxLineRtVo;

public class ReturnCustOrdBean {
	BigDecimal omsOrposCustOrdId = null;
	BigDecimal omsOrposCustOrdRtnSeq = null;
	public String status = "S";

	public ReturnCustOrdBean() {
		super();
	}

	private final static Logger log = Logger.getLogger(ReturnCustOrdBean.class.getName());
	public String customerOrderNo = "";
	public String transactionNumber = "";

	public void getCustomerOrderNoandTransactionNo(String customerOrderId) {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		if (customerOrderId.contains("-")) {
			log.info("insisde if block input customerOrderNo contains dellimeter");
			String posCustOrdNo[] = oMSUtilCommons.splitCustomerOrder(customerOrderId);
			customerOrderNo = posCustOrdNo[0];
			transactionNumber = posCustOrdNo[1];
			log.info("customerOrderNo " + customerOrderNo);
			log.info("transactionNumber " + transactionNumber);

		} else {
			customerOrderNo = customerOrderId;
			log.info("customerOrderNo " + customerOrderNo);
			log.info("transactionNumber " + transactionNumber);

		}
	}

	public String checkForShippingChargeNonInventoryItem(CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		log.info("inside checkForShippingChargeNonInventoryItem ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// String
		// customerNo=custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId();
		List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
		String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
		List<OmsCustOrdItem> omsCustOrditemList = null;
		for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
			CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
			List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
			for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
				omsCustOrditemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, new BigDecimal(custOrdItmRtVo.getLineItemNo()));
				for (OmsCustOrdItem omsCustOrdItem : omsCustOrditemList) {
					BigDecimal itemDept = session.getItemMasterFindDept(omsCustOrdItem.getItem());
					String inventoryIndn = session.getItemMasterFindInventoryInd(omsCustOrdItem.getItem(), itemDept);
					if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
						status = "ERROR_114"; // returning an shipping charge & non-inventory item
						break;
					}

				}
			}
		}

		return status;

	}

	public String restrictItem(CustOrderRtnColVo custOrderRtnColVo) {
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			List<String> items = new ArrayList<String>();
			List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
			BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);

			List<OmsCustOrdItem> omsCustOrditemList = null;
			for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
				CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
				List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
				for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
					omsCustOrditemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, new BigDecimal(custOrdItmRtVo.getLineItemNo()));
				}
			}
			for (OmsCustOrdItem omsCustOrdItem : omsCustOrditemList) {
				String item = omsCustOrdItem.getItem();
				items.add(item);
			}
			int rows = fetchItem(items);
			if (rows > 0) {
				status = "ERROR_114";
			}
		} catch (SOAPException e) {
			log.error("connection filed -", e);
		}
		return status;
	}

	public String validateDeliveryCharge(CustOrderRtnColVo custOrderRtnColVo) {
		log.info("started validation for delivery charge");
		BigDecimal omsCustOrdNo = null;
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
			omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
			Map<BigDecimal, BigDecimal> requestReturnQtyMap = new HashMap<BigDecimal, BigDecimal>();
			List<BigDecimal> reqLineNos = new ArrayList<BigDecimal>();

			for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
				CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
				List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
				for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
					reqLineNos.add(new BigDecimal(custOrdItmRtVo.getLineItemNo()));
					requestReturnQtyMap.put(new BigDecimal(custOrdItmRtVo.getLineItemNo()), custOrdItmRtVo.getReturnedQuantity());
				}
			}

			if (hasPendingQuantity(omsCustOrdNo, reqLineNos, requestReturnQtyMap)) {
				status = "ERROR_114";
				log.info("status-" + status);
			}
			log.info("completed validation for delivery charge" + status);

		} catch (Exception e) {
			log.error("error while getting pending qty for oms cust order no " + omsCustOrdNo, e);
		}
		return status;
	}

	private boolean hasPendingQuantity(BigDecimal omsCustOrdNo, List<BigDecimal> reqLineNos, Map<BigDecimal, BigDecimal> requestReturnLineMap) {
		log.info("Entering pending qty method");
		List<String> hardcodedItems = new ArrayList<String>();
		boolean hasSmallPendingqty = false;
		boolean hasSmallDeliveryItem = false;
		boolean hasBigPendingqty = false;
		boolean hasBigDeliveryItem = false;
		String smallDlvItem = "100054959";
		String bigDlvItem = "100054961";
		hardcodedItems.add(smallDlvItem);
		hardcodedItems.add(bigDlvItem); // Shipping Charge Small and Shipping Charge Big
		String lineNosPlaceholders = reqLineNos.isEmpty() ? "NULL" : reqLineNos.toString().replace("[", "").replace("]", "");
		StringBuilder qryBuilder = new StringBuilder();

		String query = "SELECT ITEM, LINE_NO, (QTY_ORDERED_SUOM - (QTY_CANCELLED + QTY_RETURNED)) PENDING, SHIP_CLASSIFICATION " + "FROM OMS_CUST_ORD_ITEM " + "WHERE OMS_CUST_ORD_NO = ? "
				+ "AND SHIP_CLASSIFICATION IN ( " + " SELECT DISTINCT SHIP_CLASSIFICATION " + "    FROM OMS_CUST_ORD_ITEM " + "    WHERE OMS_CUST_ORD_NO = ? " + "    AND LINE_NO IN ("
				+ lineNosPlaceholders + "))";

		log.info("Executing query: " + query);

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setBigDecimal(1, omsCustOrdNo);
			preparedStatement.setBigDecimal(2, omsCustOrdNo);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String item = rs.getString("ITEM");
				BigDecimal lineNo = rs.getBigDecimal("LINE_NO");
				BigDecimal pendingQty = rs.getBigDecimal("PENDING");
				String classification = rs.getString("SHIP_CLASSIFICATION");

				if (requestReturnLineMap.containsKey(lineNo)) {
					BigDecimal requestedQty = requestReturnLineMap.get(lineNo);
					pendingQty = pendingQty.subtract(requestedQty);
				}
				if (!hasSmallPendingqty && "SMALL".equals(classification) && pendingQty.intValue() > 0) {
					hasSmallPendingqty = true;
				} else if (!hasBigPendingqty && "BIG".equals(classification) && pendingQty.intValue() > 0) {
					hasBigPendingqty = true;
				}
				if (!hasSmallDeliveryItem && smallDlvItem.equals(item) && requestReturnLineMap.containsKey(lineNo)) {
					hasSmallDeliveryItem = true;
				} else if (!hasBigDeliveryItem && bigDlvItem.equals(item) && requestReturnLineMap.containsKey(lineNo)) {
					hasBigDeliveryItem = true;
				}
				log.info("total qty " + pendingQty);
			}
		} catch (Exception e) {
			log.error("Exception occurred while fetching item count: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception closeEx) {
				log.error("Exception occurred while closing DB connection: " + closeEx.getMessage(), closeEx);
			}
		}
		return (hasSmallPendingqty && hasSmallDeliveryItem) || (hasBigPendingqty && hasBigDeliveryItem);
	}

	public int fetchItem(List<String> items) {
		log.info("Entering fetchItem method");
		int rows = 0;

		if (items == null || items.isEmpty()) {
			log.warn("Item list is empty or null");
			return rows;
		}

		StringBuilder placeholders = new StringBuilder();
		for (int i = 0; i < items.size(); i++) {
			placeholders.append("?");
			if (i < items.size() - 1) {
				placeholders.append(",");
			}
		}

		String query = "SELECT COUNT(*) AS total FROM XX_RET_RESTRICT_SKU WHERE item IN (" + placeholders + ")";
		log.info("Executing query: " + query);

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);

			for (int i = 0; i < items.size(); i++) {
				preparedStatement.setString(i + 1, items.get(i));
			}

			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				rows = rs.getInt("total");
			}

		} catch (Exception e) {
			log.error("Exception occurred while fetching item count: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception closeEx) {
				log.error("Exception occurred while closing DB connection: " + closeEx.getMessage(), closeEx);
			}
		}

		log.info("Total rows fetched: " + rows);
		return rows;
	}

	public int checkCustomerOrderIdOmsOrposCustomerOrderId(CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		log.info("inside checkCustomerOrderIdOmsOrposCustomerOrderId ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal omsOrposCustOrdId = null;
		int collectionSize = 0;

		// String
		// customerNo=custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId();
		// log.info("customerNo "+customerNo);

		try {
			log.info("checking whether customerNo " + customerOrderNo + " is present in OmsOrposCustOrderHead");
			log.info("customerNo " + customerOrderNo);
			omsOrposCustOrdId = session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(customerOrderNo);
			log.info("omsOrposCustOrdId " + omsOrposCustOrdId);
			// log.info("omsOrposCustOrderHead.getStatus()
			// "+omsOrposCustOrderHead.getStatus());
			if (omsOrposCustOrdId != null) {
				log.info("customerNo " + customerOrderNo + " exist in OmsOrposCustOrderHead");

				collectionSize = saveCustOrderRtnColVo(omsOrposCustOrdId, custOrderRtnColVo);
				try {
					update__Oms_cust_ord_item(customerOrderNo, custOrderRtnColVo);
					List<OmsOrposCustOrdItm> omsOrposCustOrdItmList = session.getOmsOrposCustOrdItmFindColumns(omsOrposCustOrdId);
					log.info("omsOrposCustOrdItmList.size() " + omsOrposCustOrdItmList.size());
					if (omsOrposCustOrdItmList.size() > 0) {

						update_OmsOrposCustOrderHead(customerOrderNo);

					} else {
						// log.info("Again updating OMS table for E-COMMERCE order");
						// update__Oms_cust_ord_item(customerNo,custOrderRtnColVo);

					}
					log.info("collectionSize " + collectionSize);
				} // end of try
				catch (Exception ex) {
					// log.info("record not present in OmsOrposCustOrdItm table for
					// omsOrposCustOrdId "+omsOrposCustOrdId);
					// log.info("exception "+ex);
				} // end of catch

			} // end of if

		} // end of try

		catch (Exception e) {
			log.info("customerNo " + customerOrderNo + " not present in ORPOS table ,persisting into persistIntoOmsOrposCustOrdHead");
			BigDecimal omsOrposCustOrdId1 = persistIntoOmsOrposCustOrdHead(custOrderRtnColVo);
			log.info("omsOrposCustOrdId1 " + omsOrposCustOrdId1);
			log.info("calling saveCustOrderRtnColVo method ");
			collectionSize = saveCustOrderRtnColVo(omsOrposCustOrdId1, custOrderRtnColVo);
			log.info("collectionSize " + collectionSize);
			log.info("updating OMS table for E-COMMERCE order for new record");
			update__Oms_cust_ord_item(customerOrderNo, custOrderRtnColVo);
		}

		return collectionSize;
	}

	public int saveCustOrderRtnColVo(BigDecimal omsOrposCustOrdId, CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		log.info("inside saveCustOrderRtnColVo ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsOrposCustOrderHead omsOrposCustOrderHead = new OmsOrposCustOrderHead();
		log.info("omsOrposCustOrdId " + omsOrposCustOrdId);
		int collectionSize = 0;
		List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
		for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
			OmsOrposCustOrderRtn omsOrposCustOrderRtn = new OmsOrposCustOrderRtn();
			// omsOrposCustOrdId=
			// session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderRtnVo.getCustomerOrderId());

			omsOrposCustOrderRtn.setOmsOrposCustOrderId(omsOrposCustOrdId);
			omsOrposCustOrderRtn.setCustomerOrderId(checkNullValueForString(customerOrderNo));
			omsOrposCustOrderRtn.setCurrencyCode(checkNullValueForString(custOrderRtnVo.getCurrencyCode()));
			omsOrposCustOrderRtn.setReturnedAmount(checkNullValueForNumber(custOrderRtnVo.getReturnedAmount()));
			omsOrposCustOrderRtn.setReturnedDiscountAmount(checkNullValueForNumber(custOrderRtnVo.getReturnedDiscountAmount()));
			omsOrposCustOrderRtn.setReturnedTaxAmount(checkNullValueForNumber(custOrderRtnVo.getReturnedTaxAmount()));
			omsOrposCustOrderRtn.setReturnedInclusiveTaxAmount(checkNullValueForNumber(custOrderRtnVo.getReturnedInclusiveTaxAmount()));
			omsOrposCustOrderRtn.setUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
			session.persistOmsOrposCustOrderRtn(omsOrposCustOrderRtn);
			log.info("Successfully persisted in OmsOrposCustOrderRtn");
			List<BigDecimal> omsOrposCustOrdRtnSeq = session.getOmsOrposCustOrderRtnFindCustOrderRtnSeq(customerOrderNo);
			CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
			collectionSize = saveCustOrdItmRtColVo(custOrdItmRtColVo, omsOrposCustOrdId);

		} // end of CustOrderRtnVo
		return collectionSize;
	} // end of saveCustOrderRtnColVo

	public int saveCustOrdItmRtColVo(CustOrdItmRtColVo custOrdItmRtColVo, BigDecimal omsOrposCustOrdId) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
		int collectionSize = custOrdItmRtVoList.size();
		BigDecimal maxCustOrdRtnSeqNo = session.getMaxOmsOrposCustOrderRtn(omsOrposCustOrdId);
		log.info("return seqNo=" + maxCustOrdRtnSeqNo);
		for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
			OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtn = new OmsOrposCustOrdItmRtn();

			omsOrposCustOrdItmRtn.setOmsOrposCustOrderId(omsOrposCustOrdId);
			omsOrposCustOrdItmRtn.setCustOrderRtnSeqNo(maxCustOrdRtnSeqNo);
			omsOrposCustOrdItmRtn.setLineItemNo(checkNullValueForNumber(new BigDecimal(custOrdItmRtVo.getLineItemNo())));
			omsOrposCustOrdItmRtn.setReturnedQuantity(checkNullValueForNumber(custOrdItmRtVo.getReturnedQuantity()));
			omsOrposCustOrdItmRtn.setUnitOfMeasure(checkNullValueForString(custOrdItmRtVo.getUnitOfMeasure()));
			omsOrposCustOrdItmRtn.setCurrencyCode(checkNullValueForString(custOrdItmRtVo.getCurrencyCode()));
			omsOrposCustOrdItmRtn.setReturnedAmount(checkNullValueForNumber(custOrdItmRtVo.getReturnedAmount()));
			omsOrposCustOrdItmRtn.setReturnedDiscountAmount(checkNullValueForNumber(custOrdItmRtVo.getReturnedDiscountAmount()));
			omsOrposCustOrdItmRtn.setReturnedTaxAmount(checkNullValueForNumber(custOrdItmRtVo.getReturnedTaxAmount()));
			omsOrposCustOrdItmRtn.setReturnedInclusiveTaxAmount(checkNullValueForNumber(custOrdItmRtVo.getReturnedInclusiveTaxAmount()));
			session.persistOmsOrposCustOrdItmRtn(omsOrposCustOrdItmRtn);
			log.info("Successfully persisted in OmsOrposCustOrdItmRtn");

			if (custOrdItmRtVo.getDiscntLineRtColVo() != null) {
				DiscntLineRtColVo discntLineRtColVo = custOrdItmRtVo.getDiscntLineRtColVo();
				if (discntLineRtColVo != null) {
					for (DiscntLineRtVo discntLineRtVo : discntLineRtColVo.getDiscntLineRtVo()) {
						OmsOrposDiscntLineRtn omsOrposDiscntLineRtn = new OmsOrposDiscntLineRtn();
						omsOrposDiscntLineRtn.setOmsOrposCustOrderId(omsOrposCustOrdId);
						omsOrposDiscntLineRtn.setCustOrderRtnSeqNo(maxCustOrdRtnSeqNo);
						omsOrposDiscntLineRtn.setLineItemNo(new BigDecimal(custOrdItmRtVo.getLineItemNo()));
						omsOrposDiscntLineRtn.setLineNo(checkNullValueForNumber(new BigDecimal(discntLineRtVo.getLineNo())));
						omsOrposDiscntLineRtn.setCurrencyCode(checkNullValueForString(discntLineRtVo.getCurrencyCode()));
						omsOrposDiscntLineRtn.setReturnedDiscountAmount(checkNullValueForNumber(discntLineRtVo.getReturnedDiscountAmount()));
						session.persistOmsOrposDiscntLineRtn(omsOrposDiscntLineRtn);
						log.info("Successfully persisted in OmsOrposDiscntLineRtn");

					} // end of DiscntLineRtVo

				} // end of if discntLineRtColVo

			} // end of if custOrdItmRtVo

			if (custOrdItmRtVo.getTaxLineRtColVo() != null) {
				TaxLineRtColVo taxLineRtColVo = custOrdItmRtVo.getTaxLineRtColVo();
				if (taxLineRtColVo != null) {
					for (TaxLineRtVo taxLineRtVo : taxLineRtColVo.getTaxLineRtVo()) {
						OmsOrposTaxLineRtn omsOrposTaxLineRtn = new OmsOrposTaxLineRtn();
						omsOrposTaxLineRtn.setOmsOrposCustOrderId(omsOrposCustOrdId);
						omsOrposTaxLineRtn.setCustOrderRtnSeqNo(maxCustOrdRtnSeqNo);
						omsOrposTaxLineRtn.setLineItemNo(new BigDecimal(custOrdItmRtVo.getLineItemNo()));
						omsOrposTaxLineRtn.setLineNo(checkNullValueForNumber(new BigDecimal(taxLineRtVo.getLineNo())));
						omsOrposTaxLineRtn.setCurrencyCode(checkNullValueForString(taxLineRtVo.getCurrencyCode()));
						omsOrposTaxLineRtn.setReturnedTaxAmount(checkNullValueForNumber(taxLineRtVo.getReturnedTaxAmount()));
						omsOrposTaxLineRtn.setInclusiveTaxFlag(checkNullValueForString(taxLineRtVo.getInclusiveTaxFlag().value()));
						/* Start Tax enablement tsultana */
						session.persistOmsOrposTaxLineRtn(omsOrposTaxLineRtn);
						log.info("Successfully persisted in OmsOrposTaxLineRtn");
						/* End Tax Enablement tsultana */
					} // end of TaxLineRtVo

				} // end of if

			}
		}
		return collectionSize;
	}
	// updating OMS_ORPOS_CUST_ORDER_HEAD

	public void update_OmsOrposCustOrderHead(String customerOrderId) throws SOAPException {
		log.info("Updating OmsOrposCustOrderHead");
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		// Intializing variables for OmsOrposCustOrderHead table
		BigDecimal omsOrposCustOrderId = null;
		BigDecimal head_paid_Amount = null;
		BigDecimal head_Returned_Amount = null;
		BigDecimal head_Returned_Discount_Amount = null;
		BigDecimal head_Returned_Tax_Amount = null;
		BigDecimal head_Returned_Inclusive_Tax_Amount = null;

		// Intializing variables for OmsOrposCustOrderRtn table
		BigDecimal rtn_CustOrderRtnseq = null;
		BigDecimal rtn_Returned_Amount = null;
		BigDecimal rtn_Returned_Discount_Amount = null;
		BigDecimal rtn_Returned_Tax_Amount = null;
		BigDecimal rtn_Returned_Inclusive_Tax_Amount = null;

		List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList = session.getOmsOrposCustOrderHeadfindColumns(customerOrderId);
		for (OmsOrposCustOrderHead omsOrposCustOrderHead : omsOrposCustOrderHeadList) {
			log.info("inside omsOrposCustOrderHead loop");
			omsOrposCustOrderId = omsOrposCustOrderHead.getOmsOrposCustOrderId();
			head_paid_Amount = omsOrposCustOrderHead.getPaidAmount();
			head_Returned_Amount = omsOrposCustOrderHead.getReturnedAmount();
			head_Returned_Discount_Amount = omsOrposCustOrderHead.getReturnedDiscountAmount();
			head_Returned_Tax_Amount = omsOrposCustOrderHead.getReturnedTaxAmount();
			head_Returned_Inclusive_Tax_Amount = omsOrposCustOrderHead.getReturnedInclusiveTaxAmount();

			// Assinging Null value to zero
			if (head_paid_Amount == null) {
				head_paid_Amount = new BigDecimal(0);
			}
			if (head_Returned_Amount == null) {
				head_Returned_Amount = new BigDecimal(0);
			}
			if (head_Returned_Discount_Amount == null) {
				head_Returned_Discount_Amount = new BigDecimal(0);
			}
			if (head_Returned_Tax_Amount == null) {
				head_Returned_Tax_Amount = new BigDecimal(0);
			}
			if (head_Returned_Inclusive_Tax_Amount == null) {
				head_Returned_Inclusive_Tax_Amount = new BigDecimal(0);
			}
			// BigDecimal
			// custOrderRtnSeqNo=session.getMaxOmsOrposCustOrderRtn(omsOrposCustOrdId);
			// log.info("custOrderRtnSeqNo "+custOrderRtnSeqNo);
			List<OmsOrposCustOrderRtn> omsOrposCustOrderRtn = session.getOmsOrposCustOrderRtnFindAllColumns(customerOrderId);
			for (OmsOrposCustOrderRtn omsOrposCustOrderRtnLoop : omsOrposCustOrderRtn) {
				log.info("inside omsOrposCustOrderRtnLoop ");
				rtn_CustOrderRtnseq = omsOrposCustOrderRtnLoop.getCustOrderRtnSeqNo();
				log.info("rtn_CustOrderRtnseq " + rtn_CustOrderRtnseq);
				rtn_Returned_Amount = omsOrposCustOrderRtnLoop.getReturnedAmount();
				log.info("rtn_Returned_Amount " + rtn_Returned_Amount);
				rtn_Returned_Discount_Amount = omsOrposCustOrderRtnLoop.getReturnedDiscountAmount();
				rtn_Returned_Tax_Amount = omsOrposCustOrderRtnLoop.getReturnedTaxAmount();
				rtn_Returned_Inclusive_Tax_Amount = omsOrposCustOrderRtnLoop.getReturnedInclusiveTaxAmount();

				// Assinging Null value to zero
				if (rtn_Returned_Amount == null) {
					rtn_Returned_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Discount_Amount == null) {
					rtn_Returned_Discount_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Tax_Amount == null) {
					rtn_Returned_Tax_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Inclusive_Tax_Amount == null) {
					rtn_Returned_Inclusive_Tax_Amount = new BigDecimal(0);
				}

				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial value for OmsOrposCustOrderHead!!!!!!!!!!!!!!!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("paid_Amount " + head_paid_Amount);
				log.info("Returned_Amount " + head_Returned_Amount);
				log.info("Returned_Discount_Amount " + head_Returned_Discount_Amount);
				log.info("Returned_Tax_Amount " + head_Returned_Tax_Amount);
				log.info("Returned_Inclusive_Tax_Amount " + head_Returned_Inclusive_Tax_Amount);

				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial value for OmsOrposCustOrderRtn Audit table values!!!!!!!!!!!!!!!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("rtn_Returned_Amount " + rtn_Returned_Amount);
				log.info("rtn_Returned_Discount_Amount " + rtn_Returned_Discount_Amount);
				log.info("rtn_Returned_Tax_Amount " + rtn_Returned_Tax_Amount);
				log.info("rtn_Returned_Inclusive_Tax_Amount " + rtn_Returned_Inclusive_Tax_Amount);

				// Subtract Paid_Amount - Returned_Amount=check_Amount(from
				// oms_orpos_cust_ord_Rtn table)
				BigDecimal check_Amount = head_paid_Amount.subtract(head_Returned_Amount);
				log.info("check_Amount :" + check_Amount);

				// Comparing check_Amount with Returned_Amount( oms_orpos_cust_ord_Rtn table)
				int value_Amount = check_Amount.compareTo(rtn_Returned_Amount);
				log.info(" value_Amount " + value_Amount);
				// Adding itm_Returned_Amount (from oms_orpos_cust_ord_Head table)+
				// rtn_Returned_Amount (from oms_orpos_cust_ord_Rtn table) =add_Returned_Amount
				BigDecimal add_Returned_Amount = head_Returned_Amount.add(rtn_Returned_Amount);
				log.info("Returned_Amount " + add_Returned_Amount);

				// comparing add_Returned_Amount and rtn_Returned_Amount(Request
				// Returned_Amount)
				int amount_result = add_Returned_Amount.compareTo(rtn_Returned_Amount);
				log.info("Amount_result " + amount_result);
				log.info("Updated Values for OmsOrposCustOrderHead");
				if (value_Amount == 0 || value_Amount == 1) {
					if (amount_result == 0 || amount_result == 1) {
						log.info("Returned_Amount " + add_Returned_Amount);
						omsOrposCustOrderHead.setReturnedAmount(add_Returned_Amount);
					} else if (amount_result == -1) {
						log.info("Requested Returned Amount is more than the original Returned Amount(Already Returned)");

					}
				} else if (value_Amount == -1) {
					log.info("Requested Returned Amount is more than the original Paid Amount");
					// throw new SOAPException("Requested Returned Amount is more than the original
					// Paid Amount");
				}
				omsOrposCustOrderHead.setReturnedDiscountAmount(rtn_Returned_Discount_Amount.add(head_Returned_Discount_Amount));
				omsOrposCustOrderHead.setReturnedTaxAmount(rtn_Returned_Tax_Amount.add(head_Returned_Tax_Amount));
				omsOrposCustOrderHead.setReturnedInclusiveTaxAmount(rtn_Returned_Inclusive_Tax_Amount.add(head_Returned_Inclusive_Tax_Amount));

				log.info("Returned_Discount_Amount " + rtn_Returned_Discount_Amount.add(head_Returned_Discount_Amount));
				log.info("Returned_Tax_Amount " + rtn_Returned_Tax_Amount.add(head_Returned_Tax_Amount));
				log.info("Returned_Inclusive_Tax_Amount " + rtn_Returned_Inclusive_Tax_Amount.add(head_Returned_Inclusive_Tax_Amount));
				session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
				log.info("Successfully updated OmsOrposCustOrderHead");
				update_OmsOrposCustOrderItm(omsOrposCustOrderId, rtn_CustOrderRtnseq, customerOrderId);
			}
		}
	}

	public void update_OmsOrposCustOrderItm(BigDecimal omsOrposCustOrderId, BigDecimal rtn_CustOrderRtnseq, String customerOrderId) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();

		// Intializing variables for OmsOrposCustOrderItm table
		String itm_ItemId = null;
		BigDecimal itm_LineItem_No = null;
		BigDecimal itm_CapturedLine_Item_No = null;
		BigDecimal itm_Quantity = null;
		BigDecimal itm_Paid_Amount = null;
		BigDecimal itm_Returned_Quantity = null;
		BigDecimal itm_Returned_Amount = null;
		BigDecimal itm_Returned_Discount_Amount = null;
		BigDecimal itm_Returned_Tax_Amount = null;
		BigDecimal itm_Returned_Inclusive_Tax = null;
		BigDecimal oms_item_Qty_Returned = null;
		// Intializing variables for OmsOrposCustOrderItmRt table
		BigDecimal rtn_LineItem_No = null;
		BigDecimal rtn_Returned_Quantity = null;
		BigDecimal rtn_Returned_Amount = null;
		BigDecimal rtn_Returned_Discount_Amount = null;
		BigDecimal rtn_Returned_Tax_Amount = null;
		BigDecimal rtn_Returned_Inclusive_Tax = null;
		OmsCustOrdItem omsCustOrdItem = null;
		log.info("!!!!!!!!!!!!!!!!!!!!!!!!OmsOrposCustOrdItm!!!!!!!!!!!!!!!!!!!!!!!!");
		List<OmsOrposCustOrdItm> omsOrposCustOrderItmList = session.getOmsOrposCustOrdItmFindColumns(omsOrposCustOrderId);
		for (OmsOrposCustOrdItm omsOrposCustOrdItm : omsOrposCustOrderItmList) {
			itm_LineItem_No = omsOrposCustOrdItm.getLineItemNo();
			itm_ItemId = omsOrposCustOrdItm.getItemId();
			itm_CapturedLine_Item_No = omsOrposCustOrdItm.getCapturedLineItemNo();
			itm_Quantity = omsOrposCustOrdItm.getQuantity();
			itm_Paid_Amount = omsOrposCustOrdItm.getPaidAmount();
			itm_Returned_Quantity = omsOrposCustOrdItm.getReturnedQuantity();
			itm_Returned_Amount = omsOrposCustOrdItm.getReturnedAmount();
			itm_Returned_Discount_Amount = omsOrposCustOrdItm.getReturnedDiscountAmount();
			itm_Returned_Tax_Amount = omsOrposCustOrdItm.getReturnedTaxAmount();
			itm_Returned_Inclusive_Tax = omsOrposCustOrdItm.getReturnedInclusiveTaxAmount();

			// Assinging Null value to zero
			if (itm_Paid_Amount == null) {
				log.info("itm_Paid_Amount is null intializing it to 0");
				itm_Paid_Amount = new BigDecimal(0);
			}
			if (itm_Returned_Quantity == null) {
				log.info("itm_Returned_Quantity is null intializing it to 0");
				itm_Returned_Quantity = new BigDecimal(0);
			}
			if (itm_Returned_Amount == null) {
				log.info("itm_Returned_Amount is null intializing it to 0");
				itm_Returned_Amount = new BigDecimal(0);
			}
			if (itm_Returned_Tax_Amount == null) {
				log.info("itm_Returned_Tax_Amount is null intializing it to 0");
				itm_Returned_Tax_Amount = new BigDecimal(0);
			}
			if (itm_Returned_Inclusive_Tax == null) {
				log.info("itm_Returned_Inclusive_Tax is null intializing it to 0");
				itm_Returned_Inclusive_Tax = new BigDecimal(0);
			}
			if (itm_Returned_Discount_Amount == null) {
				log.info("itm_Returned_Discount_Amount is null intializing it to 0");
				itm_Returned_Discount_Amount = new BigDecimal(0);
			}

			List<OmsOrposCustOrdItmRtn> omsOrposCustOrdItmRtnList = session.getOmsOrposCustOrdItmRtnfindColumns(omsOrposCustOrderId, rtn_CustOrderRtnseq);
			for (OmsOrposCustOrdItmRtn omsOrposCustOrdItmRtLoop : omsOrposCustOrdItmRtnList) {
				rtn_CustOrderRtnseq = omsOrposCustOrdItmRtLoop.getCustOrderRtnSeqNo();
				rtn_LineItem_No = omsOrposCustOrdItmRtLoop.getLineItemNo();
				rtn_Returned_Quantity = omsOrposCustOrdItmRtLoop.getReturnedQuantity();
				// log.info("rtn_Returned_Quantity "+rtn_Returned_Quantity);
				rtn_Returned_Amount = omsOrposCustOrdItmRtLoop.getReturnedAmount();
				rtn_Returned_Discount_Amount = omsOrposCustOrdItmRtLoop.getReturnedDiscountAmount();
				rtn_Returned_Tax_Amount = omsOrposCustOrdItmRtLoop.getReturnedTaxAmount();
				rtn_Returned_Inclusive_Tax = omsOrposCustOrdItmRtLoop.getReturnedInclusiveTaxAmount();
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerOrderId, "S");
				omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdHead.getOmsCustOrdNo(), itm_ItemId, rtn_LineItem_No);
				oms_item_Qty_Returned = omsCustOrdItem.getQtyReturned();
				log.info("Qty_Returned " + oms_item_Qty_Returned);
				// Assinging Null value to zero
				if (rtn_Returned_Quantity == null) {
					log.info("return Quantity is null intializing it to zero");
					rtn_Returned_Quantity = new BigDecimal(0);
				}
				if (rtn_Returned_Amount == null) {
					rtn_Returned_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Discount_Amount == null) {
					rtn_Returned_Discount_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Tax_Amount == null) {
					rtn_Returned_Tax_Amount = new BigDecimal(0);
				}
				if (rtn_Returned_Inclusive_Tax == null) {
					rtn_Returned_Inclusive_Tax = new BigDecimal(0);
				}

				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from Cust Order Itm!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("Line_Item_No " + itm_LineItem_No);
				log.info("itm_ItemId  " + itm_ItemId);
				log.info("itm_CapturedLine_Item_No " + itm_CapturedLine_Item_No);
				log.info("itm_paid_Amount " + itm_Paid_Amount);
				log.info("itm_Quantity " + itm_Quantity);
				log.info("itm_Returned_Quantity " + itm_Returned_Quantity);
				log.info("itm_Returned_Amount " + itm_Returned_Amount);
				log.info("itm_Returned_Discount_Amount " + itm_Returned_Discount_Amount);
				log.info("itm_Returned_Tax_Amount " + itm_Returned_Tax_Amount);
				log.info("itm_Returned_Inclusive_Tax " + itm_Returned_Inclusive_Tax);

				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from Cust Order Return Itm - Audit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("rtn_CustOrderRtnseq " + rtn_CustOrderRtnseq);
				log.info("rtn_LineItem_No " + rtn_LineItem_No);
				log.info("rtn_Returned_Quantity " + rtn_Returned_Quantity);
				log.info("rtn_Returned_Amount " + rtn_Returned_Amount);
				log.info("rtn_Returned_Discount_Amount " + rtn_Returned_Discount_Amount);
				log.info("rtn_Returned_Tax_Amount " + rtn_Returned_Tax_Amount);
				log.info("rtn_Returned_Inclusive_Tax " + rtn_Returned_Inclusive_Tax);

				log.info("+++++++++++++++++++++Updated Values OmsOrposCustOrdItm +++++++++++++++++++++++++++++++++++");

				// //comparing itm_Line_No from oms_orpos_cust_ord_Itm table and rtn_LineItem_No
				// from oms_orpos_cust_ord_Itmrt
				// int value_line_num=itm_LineItem_No.compareTo(rtn_LineItem_No);
				//
				// //Subtracting Quantity - Returned Quantity=check_Quantity (from
				// oms_orpos_cust_ord_Itm table)
				// BigDecimal check_Quantity= itm_Quantity.add(itm_Returned_Quantity);
				// System.out.println("Quantity - Returned Quantity "+check_Quantity);
				//
				// //Comparing check_Quantity(Quantity - Returned Quantity from
				// oms_orpos_cust_ord_Itm table) with Returned Quantity (
				// oms_orpos_cust_ord_ItmRt table)
				// int value_Quantity=check_Quantity.compareTo(rtn_Returned_Quantity);
				//
				// //Adding itm_Returned_Quantity (from oms_orpos_cust_ord_Itm table)+
				// rtn_Returned_Quantity (from oms_orpos_cust_ord_Itmrt table)
				// =add_Returned_Quantity
				// BigDecimal
				// add_Returned_Quantity=rtn_Returned_Quantity.add(itm_Returned_Quantity);
				// log.info("add_Returned_Quantity
				// +++++++++++++++++++++++"+add_Returned_Quantity);
				// //comparing add_Returned_Quantity and rtn_Returned_Quantity(Request
				// Returned_Quantity)
				// int quantity_result=add_Returned_Quantity.compareTo(rtn_Returned_Quantity);
				//
				// //Subtract Paid_Amount - Returned_Amount=check_Amount(from
				// oms_orpos_cust_ord_Itm table)
				// BigDecimal check_Amount= itm_Paid_Amount.add(itm_Returned_Amount);
				// log.info("check_Amount "+check_Amount);
				//
				// //Comparing check_Amount with Returned_Amount( oms_orpos_cust_ord_ItmRt
				// table)
				// int value_Amount=check_Amount.compareTo(rtn_Returned_Amount);
				//
				// //Adding itm_Returned_Amount (from oms_orpos_cust_ord_Itm table)+
				// rtn_Returned_Amount (from oms_orpos_cust_ord_Itmrt table)
				// =add_Returned_Quantity
				// BigDecimal add_Returned_Amount=rtn_Returned_Amount.add(itm_Returned_Amount);
				//
				// //comparing add_Returned_Amount and rtn_Returned_Amount(Request
				// Returned_Amount)
				// int amount_result=add_Returned_Amount.compareTo(rtn_Returned_Amount);
				//
				// //Comparining line_item_no (itm_Line_No,rtn_LineItem_No)
				if (itm_LineItem_No.intValue() == rtn_LineItem_No.intValue()) {

					log.info("Returned_Quantity " + rtn_Returned_Quantity.add(itm_Returned_Quantity));
					omsOrposCustOrdItm.setReturnedQuantity(rtn_Returned_Quantity.add(itm_Returned_Quantity));
					log.info("Returned_Amount " + rtn_Returned_Amount.add(itm_Returned_Amount));
					omsOrposCustOrdItm.setReturnedAmount(rtn_Returned_Amount.add(itm_Returned_Amount));
					log.info(" Returned_Discount_Amount " + rtn_Returned_Discount_Amount.add(itm_Returned_Discount_Amount));
					omsOrposCustOrdItm.setReturnedDiscountAmount(rtn_Returned_Discount_Amount.add(itm_Returned_Discount_Amount));
					log.info(" Returned_Tax_Amount " + rtn_Returned_Tax_Amount.add(itm_Returned_Tax_Amount));
					omsOrposCustOrdItm.setReturnedTaxAmount(rtn_Returned_Tax_Amount.add(itm_Returned_Tax_Amount));
					log.info(" Returned_Inclusive_Tax " + rtn_Returned_Inclusive_Tax.add(itm_Returned_Inclusive_Tax));
					omsOrposCustOrdItm.setInclusiveTaxTotal(rtn_Returned_Inclusive_Tax.add(itm_Returned_Inclusive_Tax));
					session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);

					update_OmsOrposDiscntLine(omsOrposCustOrderId, itm_CapturedLine_Item_No, rtn_CustOrderRtnseq, rtn_LineItem_No);
					// update_OmsOrposTaxLine(omsOrposCustOrderId,itm_CapturedLine_Item_No,rtn_CustOrderRtnseq,rtn_LineItem_No);

				} // end of if

			} // end of for-loop
		} // end of for-loop
			// omsCustOrdItem.setQtyReturned(oms_item_Qty_Returned.add(rtn_Returned_Quantity));
			// log.info("updating return Qty in OMS_CUST_ORD_ITEM
			// "+oms_item_Qty_Returned.add(rtn_Returned_Quantity));
			// session.mergeOmsCustOrdItem(omsCustOrdItem);
	}

	public void update_OmsOrposDiscntLine(BigDecimal omsOrposCustOrderId, BigDecimal itm_CapturedLine_Item_No, BigDecimal rtn_CustOrderRtnseq, BigDecimal rtn_LineItem_No) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// Intializing variables for OmsOrposDiscintLine table
		String itm_dis_item_id = null;
		BigDecimal dis_Line_No = null;
		BigDecimal dis_Returned_Discount_Amount = null;

		// Intializing variables for OmsOrposDiscintLineRt table
		BigDecimal disrtn_Line_No = null;
		BigDecimal disrtn_Returned_Discount_Amount = null;

		List<OmsOrposDiscntLine> omsOrposDiscntLineList = session.getOmsOrposDiscntLineFindAllColumns(omsOrposCustOrderId, itm_CapturedLine_Item_No);

		for (OmsOrposDiscntLine omsOrposDiscntLine : omsOrposDiscntLineList) {
			itm_dis_item_id = omsOrposDiscntLine.getItemId();
			dis_Line_No = omsOrposDiscntLine.getLineNo();
			dis_Returned_Discount_Amount = omsOrposDiscntLine.getReturnedDiscountAmount();

			if (dis_Returned_Discount_Amount == null) {
				dis_Returned_Discount_Amount = new BigDecimal(0);
			}
			log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from OmsOrposDiscntLine !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
			log.info("item_id " + itm_dis_item_id);
			log.info("CapturedLine_Item_No " + itm_CapturedLine_Item_No);
			log.info("Line_No " + dis_Line_No);
			log.info("Returned_Discount_Amount " + dis_Returned_Discount_Amount);

			OmsOrposDiscntLineRtn omsOrposDiscntLineRt = new OmsOrposDiscntLineRtn();
			List<OmsOrposDiscntLineRtn> omsOrposDiscntLineRtList = session.getOmsOrposDiscntLineRtnFindAllColumns(omsOrposCustOrderId, rtn_CustOrderRtnseq, rtn_LineItem_No);

			for (OmsOrposDiscntLineRtn omsOrposDiscntLineRtnLoop : omsOrposDiscntLineRtList) {
				disrtn_Line_No = omsOrposDiscntLineRtnLoop.getLineNo();
				disrtn_Returned_Discount_Amount = omsOrposDiscntLineRtnLoop.getReturnedDiscountAmount();

				if (disrtn_Returned_Discount_Amount == null) {
					disrtn_Returned_Discount_Amount = new BigDecimal(0);
				}

				int value_discnt = disrtn_Line_No.compareTo(dis_Line_No);
				int value_Rtn_Dis_Amt = dis_Returned_Discount_Amount.compareTo(disrtn_Returned_Discount_Amount);
				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from OmsOrposDiscntLineRt -Audit !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("rtn_CustOrderRtnseq " + rtn_CustOrderRtnseq);
				log.info("disrtn_LineItem_Id " + rtn_LineItem_No);
				log.info("disrtn_Line_No " + disrtn_Line_No);
				log.info("disrtn_Returned_Discount_Amount " + disrtn_Returned_Discount_Amount);

				log.info("Updating the OmsOrposDiscntLine");

				if (value_discnt == 0) {
					if (value_Rtn_Dis_Amt == 0 || value_Rtn_Dis_Amt == 1) {
						omsOrposDiscntLine.setReturnedDiscountAmount(disrtn_Returned_Discount_Amount.add(dis_Returned_Discount_Amount));
						log.info("Returned Discount Amount " + disrtn_Returned_Discount_Amount.add(dis_Returned_Discount_Amount));
					} else if (value_Rtn_Dis_Amt == -1) {
						log.info("Requested Returned discount Amount is greater");
					}
				}
				session.mergeOmsOrposDiscntLine(omsOrposDiscntLine);

			}

		}
	}

	public void update_OmsOrposTaxLine(BigDecimal omsOrposCustOrderId, BigDecimal itm_CapturedLine_Item_No, BigDecimal rtn_CustOrderRtnseq, BigDecimal rtn_LineItem_No) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// Intializing variables for OmsOrposTaxLine table
		String tax_item_id = null;
		BigDecimal tax_Line_No = null;
		BigDecimal tax_Returned_Tax_Amount = null;

		// Intializing variables for OmsOrposDiscintLineRt table
		BigDecimal taxrtn_Line_No = null;
		BigDecimal taxrtn_Returned_Tax_Amount = null;

		List<OmsOrposTaxLine> omsOrposTaxLineList = session.getOmsOrposTaxLineFindAllColumns(omsOrposCustOrderId, itm_CapturedLine_Item_No);
		for (OmsOrposTaxLine omsOrposTaxLine : omsOrposTaxLineList) {
			tax_item_id = omsOrposTaxLine.getItemId();
			tax_Line_No = omsOrposTaxLine.getLineNo();
			tax_Returned_Tax_Amount = omsOrposTaxLine.getReturnedTaxAmount();
			if (tax_Returned_Tax_Amount == null) {

				tax_Returned_Tax_Amount = new BigDecimal(0);
			}
			log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from OmsOrposTaxLine!!!!!!!!!!!!!!! ");
			log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
			log.info("item_id " + tax_item_id);
			log.info("CapturedLine_Item_No " + itm_CapturedLine_Item_No);
			log.info("Line_No " + tax_Line_No);
			log.info("Returned_Tax_Amount " + tax_Returned_Tax_Amount);

			OmsOrposTaxLineRtn omsOrposTaxLineRt = new OmsOrposTaxLineRtn();
			List<OmsOrposTaxLineRtn> omsOrposTaxLineRtnList = session.getOmsOrposTaxLineRtnFindAllColumns(omsOrposCustOrderId, rtn_CustOrderRtnseq, rtn_LineItem_No);
			for (OmsOrposTaxLineRtn omsOrposTaxLineRtnLoop : omsOrposTaxLineRtnList) {
				taxrtn_Line_No = omsOrposTaxLineRtnLoop.getLineNo();
				taxrtn_Returned_Tax_Amount = omsOrposTaxLineRtnLoop.getReturnedTaxAmount();

				if (taxrtn_Returned_Tax_Amount == null) {

					taxrtn_Returned_Tax_Amount = new BigDecimal(0);
				}

				int value_tax = taxrtn_Line_No.compareTo(tax_Line_No);
				int value_Rtn_Tax_Amt = tax_Returned_Tax_Amount.compareTo(tax_Returned_Tax_Amount);
				log.info("!!!!!!!!!!!!!!!!!!!!!!!!Intial Value from OmsOrposTaxLineRt-Audit !!!!!!!!!!!");
				log.info("omsOrposCustOrderId " + omsOrposCustOrderId);
				log.info("rtn_CustOrderRtnseq " + rtn_CustOrderRtnseq);
				log.info("rtn_LineItem_Id " + rtn_LineItem_No);
				log.info("taxrtn_Line_No " + taxrtn_Line_No);
				log.info("taxrtn_Returned_Tax_Amount " + taxrtn_Returned_Tax_Amount);

				log.info("=========================Updating omsOrposTaxLine values================== ");
				if (value_tax == 0) {
					if (value_Rtn_Tax_Amt == 0 || value_Rtn_Tax_Amt == 1) {
						omsOrposTaxLine.setReturnedTaxAmount(tax_Returned_Tax_Amount.add(taxrtn_Returned_Tax_Amount));
						log.info("Adding Discnt line Returned Amount " + tax_Returned_Tax_Amount.add(taxrtn_Returned_Tax_Amount));
					} else if (value_Rtn_Tax_Amt == -1) {
						log.info("Requested Returned Tax Amount is greater");
					}
				}
				session.mergeOmsOrposTaxLine(omsOrposTaxLine);

			}
		}

	}

	public String checkNullValueForString(String value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public BigDecimal checkNullValueForNumber(BigDecimal value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	public BigDecimal persistIntoOmsOrposCustOrdHead(CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("inside persistIntoOmsOrposCustOrdHead method");
		BigDecimal omsOrposCustOrderId = null;
		OmsOrposCustOrderHead omsOrposCustOrderHead = new OmsOrposCustOrderHead();
		// String
		// customerNo=custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId();
		log.info("inside persistIntoOmsOrposCustOrdHead method11111  " + customerOrderNo);
		List<OmsCustOrdHead> omsCustOrdHead = session.getOmsCustOrdHeadFindColumns(customerOrderNo);
		log.info("INITIATE_LOC_ID " + omsCustOrdHead.get(0).getOrderRequestorId());
		log.info("omsCustOrdHead status " + omsCustOrdHead.get(0).getStatus());
		// String
		// country_id=session.getAddrFindByAddrKeyValue1andaddr_type(omsCustOrdHead.get(0).getOrderRequestorId(),"01");
		log.info("customerNo " + customerOrderNo);
		omsOrposCustOrderHead.setCustomerOrderId(customerOrderNo);
		log.info("custOrderRtnColVo.getCustOrderRtnVo().get(0).getCurrencyCode() " + custOrderRtnColVo.getCustOrderRtnVo().get(0).getCurrencyCode());
		omsOrposCustOrderHead.setCurrencyCode(checkNullValueForString(custOrderRtnColVo.getCustOrderRtnVo().get(0).getCurrencyCode()));
		omsOrposCustOrderHead.setInitiateLocType("S");
		omsOrposCustOrderHead.setInitiateLocId(checkNullValueForNumber(omsCustOrdHead.get(0).getOrderRequestorId()));
		omsOrposCustOrderHead.setInitiateCountryCode("SA");
		omsOrposCustOrderHead.setStatus(omsCustOrdHead.get(0).getStatus());
		omsOrposCustOrderHead.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
		omsOrposCustOrderHead.setUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
		session.persistOmsOrposCustOrderHead(omsOrposCustOrderHead);
		log.info("Successfully persisted in omsOrposCustOrderHead");
		List<OmsOrposCustOrderHead> omsOrposCustOrderHeadList = session.getOmsOrposCustOrderHeadfindColumns(customerOrderNo);
		omsOrposCustOrderId = omsOrposCustOrderHeadList.get(0).getOmsOrposCustOrderId();
		log.info("omsOrposCustOrdId " + omsOrposCustOrderId);

		return omsOrposCustOrderId;

	}

	public String checkforOrderIdandTransactionNo(String customerOrderNo, String transactionNumber, CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("===========inside checkforOrderIdandTransactionNo===============");
		List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
		List<OmsOrposMasterAudit> omsOrposMasterAudit = null;
		try {
			log.info("customerOrderNo " + customerOrderNo);
			log.info("transactionNumber " + transactionNumber);
			omsOrposMasterAudit = session.getOmsOrposMasterAuditfindByOrderIdandOrposTransactionNumber(customerOrderNo, transactionNumber);
			// adding code of return quantity validation bug 3086
			BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
			List<OmsCustOrdItem> omsCustOrdItemList = null;

			for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
				CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
				List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
				for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
					omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdNo, new BigDecimal(custOrdItmRtVo.getLineItemNo()));
					for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
						log.info("transactionNumber" + transactionNumber + "Line No " + omsCustOrdItem.getLineNo());
						log.info("transactionNumber" + transactionNumber + "Delivered Qty " + omsCustOrdItem.getCumQtyDelivered());
						log.info("transactionNumber" + transactionNumber + "Returned Qty " + omsCustOrdItem.getQtyReturned());
						if (omsCustOrdItem.getQtyReturned().intValue() == omsCustOrdItem.getCumQtyDelivered().intValue()) {
							status = "ERROR_115";
							break;
						}
					}
				}
			}
			if (!status.equals("ERROR_115")) {
				if (omsOrposMasterAudit != null && omsOrposMasterAudit.size() > 0) {
					log.info("transactionNumber " + transactionNumber + "omsOrposMasterAudit.size()" + omsOrposMasterAudit.size());
					status = "ERROR_115";
					log.info("transactionNumber " + transactionNumber + "setting status to " + status);
				} else {

					status = "S";
				}
			}
			log.info("transactionNumber " + transactionNumber + "status " + status);
		} catch (Exception e) {
			if (omsOrposMasterAudit == null && omsOrposMasterAudit.size() == 0) {
				log.info("No record exist in the table in the OmsOrposMasterAudit Table : ");
				status = "S";
				log.info("Inside catch block setting status to " + status);
			}
		}
		log.info("returning status in checkforOrderIdandTransactionNo " + status);
		return status;
	}

	public CustOrderColRef createResponse(CustOrderRtnColVo custOrderRtnColVo) {
		CustOrderColRef custOrderColRef = new CustOrderColRef();
		CustOrderRef custOrderRef = new CustOrderRef();
		try {
			OMSUtilSessionEJB session = OMSUtil.doLookup();

			log.info("calling getCustomerOrderNoandTransactionNo method");
			getCustomerOrderNoandTransactionNo(custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId());
			log.info("inside Create Response method for Return");
			log.info("Order no " + customerOrderNo + " trans no " + transactionNumber);

			status = checkforOrderIdandTransactionNo(customerOrderNo, transactionNumber, custOrderRtnColVo);
			log.info("status " + status);

			if (status.equals("S")) {
//			status = checkForShippingChargeNonInventoryItem(custOrderRtnColVo);
				status = restrictItem(custOrderRtnColVo);
				status = validateDeliveryCharge(custOrderRtnColVo);
				log.info("status " + status);

			}
			if (status.equals("ERROR_114") || status.equals("ERROR_115")) {
				custOrderRef.setOrderId(customerOrderNo);
				custOrderColRef.setCollectionSize(0);
				custOrderColRef.getCustOrderRef().add(0, custOrderRef);
				custOrderColRef.setStatus(status);
				log.info("Response Created Successfully ! for " + status);
			}

			else {

				int collectionSize = checkCustomerOrderIdOmsOrposCustomerOrderId(custOrderRtnColVo);

				custOrderRef.setOrderId(customerOrderNo);
				custOrderColRef.setCollectionSize(collectionSize);
				custOrderColRef.getCustOrderRef().add(0, custOrderRef);

				custOrderColRef.setStatus(status);

				log.info("Response Created Successfully !");
			}
		} catch (SOAPException e) {
			log.error("error while getting connection", e);
		}
		return custOrderColRef;
	}

	public void update__Oms_cust_ord_item(String customerNo, CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("upadting OMS_CUST_ORD_ITEM Table");
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadfindByCustOrdNoAndStatus(customerNo, "S");
		log.info("customerNo " + customerNo);
		List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
		log.info("custOrderRtnVoList.size() " + custOrderRtnVoList.size());
		OmsCustOrdLog omsCustOrdLog = new OmsCustOrdLog();
		omsCustOrdLog.setOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
		omsCustOrdLog.setEventId("RTN");
		omsCustOrdLog.setEventComments("Returned From POS");
		omsCustOrdLog.setCreateDatetime(new Timestamp(new Date().getTime()));
		omsCustOrdLog.setOmsCancelId(null);
		omsCustOrdLog.setOmsDlvConfId(null);
		session.persistOmsCustOrdLog(omsCustOrdLog);
		log.info("successfully persisted in OmsCustOrdLog ");
		BigDecimal maxLogSeqNo = session.getOmsCustOrdLogFindMaxLogSeqNo(omsCustOrdHead.getOmsCustOrdNo());
		OmsCustOrdLogItem omsCustOrdLogItem = new OmsCustOrdLogItem();
		List<OmsCoFulfillDetail> omsCoFulfillDetail = null;
		List<OmsCoFulfillDetail> omsCoFulfillDetailList = null;
		List<CustOrdItmRtVo> custOrdItmRtVoList = null;
		Map<String, BigDecimal> posReturnItm = new HashMap<String, BigDecimal>();
		for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
			CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
			custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
			log.info("custOrdItmRtVoList.size()" + custOrdItmRtVoList.size());
			for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {

				log.info("omsCustOrdHead.getOmsCustOrdNo() " + omsCustOrdHead.getOmsCustOrdNo());
				log.info("custOrdItmRtVo.getLineItemNo() " + custOrdItmRtVo.getLineItemNo());
				List<OmsCustOrdItem> omsCustOrdItemList = session.getOmsCustOrdItemFindByOmsCustOrdNoAndLineNo(omsCustOrdHead.getOmsCustOrdNo(), new BigDecimal(custOrdItmRtVo.getLineItemNo()));
				log.info(" omsCustOrdItemList.size()" + omsCustOrdItemList.size());
				for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
					log.info("inside omsCustOrdItem loop");
					if (omsCustOrdItem.getLineNo().intValue() == custOrdItmRtVo.getLineItemNo()) {
						String lineNoItem = omsCustOrdItem.getLineNo().toString().concat(",").concat(omsCustOrdItem.getItem());
						log.info("lineNoItem " + lineNoItem);
						posReturnItm.put(lineNoItem, custOrdItmRtVo.getReturnedQuantity());
						log.info("both lineNo are equal");
						BigDecimal oms_item_Qty_Returned = omsCustOrdItem.getQtyReturned();
						log.info("omsCustOrdItem.getQtyReturned() " + oms_item_Qty_Returned);
						omsCustOrdItem.setQtyReturned(oms_item_Qty_Returned.add(custOrdItmRtVo.getReturnedQuantity()));
						log.info("updating return Qty in OMS_CUST_ORD_ITEM " + oms_item_Qty_Returned.add(custOrdItmRtVo.getReturnedQuantity()));
						session.mergeOmsCustOrdItem(omsCustOrdItem);

						omsCoFulfillDetailList = session.getOmsCoFulfillDetailFindByItem(omsCustOrdHead.getOmsCustOrdNo(), new BigDecimal(custOrdItmRtVo.getLineItemNo()), omsCustOrdItem.getItem());
						log.info("omsCoFulfillDetailList.size() " + omsCoFulfillDetailList.size());
						if (omsCoFulfillDetailList.size() > 0) {
							for (OmsCoFulfillDetail omsCoFulfillDetail1 : omsCoFulfillDetailList) {

								// code commented for the report issue.
								// if(omsCoFulfillDetail1.getSourceLoc().intValue()==omsCoFulfillDetail1.getFulfillLoc().intValue())
								// {
								log.info("Inserting inside cust_ord_log_item table");
								omsCustOrdLogItem.setLineNo(omsCustOrdItem.getLineNo());
								omsCustOrdLogItem.setItem(omsCustOrdItem.getItem());
								omsCustOrdLogItem.setLogSeqNo(maxLogSeqNo);
								omsCustOrdLogItem.setQty(custOrdItmRtVo.getReturnedQuantity());
								omsCustOrdLogItem.setFulfillOrderNo(omsCoFulfillDetail1.getFulfillOrderNo());
								omsCustOrdLogItem.setCreateDatetime(new Timestamp(new Date().getTime()));
								session.persistOmsCustOrdLogItem(omsCustOrdLogItem);
								log.info("successfully persisted in omsCustOrdLogItem ");

								// }
								/*
								 * else { log.info("inside else block");
								 * omsCustOrdLogItem.setLineNo(omsCustOrdItem.getLineNo());
								 * omsCustOrdLogItem.setItem(omsCustOrdItem.getItem());
								 * omsCustOrdLogItem.setLogSeqNo(maxLogSeqNo);
								 * omsCustOrdLogItem.setQty(custOrdItmRtVo.getReturnedQuantity());
								 * omsCustOrdLogItem.setFulfillOrderNo(omsCoFulfillDetail1.getFulfillOrderNo());
								 * omsCustOrdLogItem.setCreateDatetime(new Timestamp(new Date().getTime()));
								 * session.persistOmsCustOrdLogItem(omsCustOrdLogItem);
								 * log.info("successfully persisted in omsCustOrdLogItem "); }
								 */
							}
						}

					}
				}
			}
		}

		InterfacePersistence interfacePersistence = new InterfacePersistence();
		log.info("calling siebel for return");
		try {
			POSTransactionBean pOSTransactionBean = new POSTransactionBean();
			log.info("calling ReturnFromPos ");
			PosTransactionMsg posTransactionMsgforRepublish = pOSTransactionBean.retunFromPOS(customerOrderNo, transactionNumber, posReturnItm);

			if (posTransactionMsgforRepublish.getPos_transacation_call() == Boolean.FALSE) {

				String xmlMsg = posTransactionMsgforRepublish.getPosTranscationXMLMessage(posTransactionMsgforRepublish.getPosTransactionDesc());
				log.info("The Retrun XML IS " + xmlMsg);
				posTransactionMsgforRepublish.insertRecordIntoRepublishDataForPos_Transaction(xmlMsg, customerNo);

			}
			log.info("called ReturnFromPos");
			// interfacePersistence.callSeibelWebserviceForReturn(custOrdItmRtVoList,customerNo,omsCustOrdHead.getOmsCustOrdNo());
			OmsOrderStatusUpdateHeader omsOrderStatusUpdateHeaderReturn = interfacePersistence.getOmsOrderStatusUpdateHeaderForReturn(custOrdItmRtVoList, customerNo, omsCustOrdHead.getOmsCustOrdNo());

			/*
			 * OmsStatusUpdateForReturnPickupCancellation
			 * omsStatusUpdateForReturnPickupCancellationWSCall = new
			 * OmsStatusUpdateForReturnPickupCancellation();
			 * omsStatusUpdateForReturnPickupCancellationWSCall
			 * .callOmsStatusUpdateWebserviceForReturnAndCancellationAndPickup(
			 * omsOrderStatusUpdateHeaderReturn);
			 */

			interfacePersistence.saveOmsWSeibelRequest(omsOrderStatusUpdateHeaderReturn, session);

		} catch (Exception e) {
			log.error("Exception occured while pushing siebel return ", e);
		}
	}

	public void reconcilationReturnOrder(CustOrderRtnColVo custOrderRtnColVo) throws SOAPException {
		log.info("Inside reconcilationReturnOrder ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		// String
		// customerNo=custOrderRtnColVo.getCustOrderRtnVo().get(0).getCustomerOrderId();
		List<CustOrderRtnVo> custOrderRtnVoList = custOrderRtnColVo.getCustOrderRtnVo();
		BigDecimal omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(customerOrderNo);
		List<OmsCustOrdItem> omsCustOrditemList = null;
		if (transactionNumber != null && !transactionNumber.equals("") && !transactionNumber.isEmpty()) {
			for (CustOrderRtnVo custOrderRtnVo : custOrderRtnVoList) {
				CustOrdItmRtColVo custOrdItmRtColVo = custOrderRtnVo.getCustOrdItmRtColVo();
				List<CustOrdItmRtVo> custOrdItmRtVoList = custOrdItmRtColVo.getCustOrdItmRtVo();
				for (CustOrdItmRtVo custOrdItmRtVo : custOrdItmRtVoList) {
					OmsOrposMasterAudit omsOrposMasterAudit = new OmsOrposMasterAudit();

					log.info("customerOrderNo " + customerOrderNo);
					omsOrposMasterAudit.setOrderId(customerOrderNo);

					omsOrposMasterAudit.setLineItemNo(new BigDecimal(custOrdItmRtVo.getLineItemNo()));
					log.info("omsCustOrdNo " + omsCustOrdNo);
					omsOrposMasterAudit.setOmsCustOrderNo(omsCustOrdNo);
					log.info("transactionNumber " + transactionNumber);
					omsOrposMasterAudit.setOrposTransactionNumber(transactionNumber);
					log.info("custOrdItmRtVo.getReturnedQuantity() " + custOrdItmRtVo.getReturnedQuantity());
					omsOrposMasterAudit.setReturnQuantity(custOrdItmRtVo.getReturnedQuantity());
					omsOrposMasterAudit.setCancelReqId(new BigDecimal(-1));
					omsOrposMasterAudit.setRmaReqId(new BigDecimal(-1));
					omsOrposMasterAudit.setEventId("RT");
					omsOrposMasterAudit.setStatus("N");
					omsOrposMasterAudit.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
					try {
						session.persistOmsOrposMasterAudit(omsOrposMasterAudit);
						log.info("transactionNo " + transactionNumber + "successfully persisted OmsOrposMasterAudit");
					} catch (Exception e) {
						log.info("transactionNo " + transactionNumber + "Exception while persisting into master audit table " + e.getMessage());
					}
				}
			}
		} else {
			log.info("transactionNumber is null");
			log.info("transactionNumber " + transactionNumber);
		}

	}

}
