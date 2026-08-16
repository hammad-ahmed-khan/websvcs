/**
 * 
 */
package com.extra.einvoicing.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.extra.einvoicing.config.ApplicationProperty;
import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.util.InvoiceUtil;

/**
 * @author aibrahim
 *
 */
@Repository
public class B2BInvoicingDAO extends BaseDAO {

	private static final Logger _LOG = LoggerFactory.getLogger(B2BInvoicingDAO.class);

	@Autowired
	private ApplicationProperty applicationProperty;

	private static final String B2B_POS_SELECT_QUERY = "SELECT T.RECEIPT_NUM, T.TRAN_SEQ_NO, T.INVOICE_NO, T.BUSINESS_DATE, H.TRAN_TYPE, 'SAR', FIRST_NAME || ' ' || LAST_NAME VENDOR_NAME, EMAIL_ADDRESS EMAIL, ADDRESS1 TO_ADDRESS_STREET_NAME, CITY TO_ADDRESS_CITY, 'SA' TO_ADDRESS_COUNTRY, "
			+ "I.ITEM, i.ITEM_SEQ_NO, i.QTY, I.UNIT_RETAIL, NVL(TX.IGTAX_RATE, 0) IGTAX_RATE, TX.TOTAL_IGTAX_AMT, D.QTY DISC_QTY, (SELECT CRN FROM XX_EINV_STORE_CRN WHERE STORE = TO_CHAR(T.STORE)) CRN,"
			+ "D.UNIT_DISCOUNT_AMT, D.DISC_TYPE, ORG_INVOICE_NO, (SELECT C.CODE_DESC FROM CODE_DETAIL C WHERE C.CODE = I.OVERRIDE_REASON) REASON, COALESCE(IM.ITEM_DESC_SECONDARY, IM.SHORT_DESC) ITEM_DSC FROM XX_POS_B2B_EINV T, SA_TRAN_HEAD H, SA_TRAN_ITEM I, SA_TRAN_IGTAX TX, SA_TRAN_DISC D, ITEM_MASTER IM "
			+ "WHERE H.TRAN_SEQ_NO = T.TRAN_SEQ_NO AND T.TRAN_SEQ_NO = I.TRAN_SEQ_NO "
			+ "AND H.TRAN_SEQ_NO = I.TRAN_SEQ_NO AND I.TRAN_SEQ_NO = TX.TRAN_SEQ_NO AND I.ITEM_SEQ_NO = TX.ITEM_SEQ_NO AND IM.ITEM = I.ITEM AND D.TRAN_SEQ_NO (+)= I.TRAN_SEQ_NO "
			+ "AND D.ITEM_SEQ_NO (+)= I.ITEM_SEQ_NO AND T.PROCESS_FLAG = 'N'";

	private final ResultSetExtractor<List<InvoiceInfo<String>>> EBS_INVOICE_RESULT_SET = (rs) -> {
		Map<Long, InvoiceInfo<String>> invoiceMap = new HashMap<>();
		Set<Long> failedInvs = new HashSet<>();
		while (rs.next()) {
			String invoiceNumber = rs.getString("INVOICE_NUM");
			Long invoiceId = rs.getLong("INVOICE_ID");
			InvoiceInfo<String> invoice = invoiceMap.get(invoiceId);
			try {
				if (!failedInvs.contains(invoiceId)) {
					if (invoice == null) {
						invoice = new InvoiceInfo<String>();
						invoice.setInvoiceId(invoiceId);
						invoice.setCustomerEmail(rs.getString("EMAIL_ADDRESS"));
						invoice.setType(rs.getString("TYPE"));
						invoice.setInvoiceType(InvoiceUtil.createEBSInvoice(rs));
						invoice.setInvoiceIdentifier(invoiceNumber);
						invoiceMap.put(invoiceId, invoice);
	
					} else {
						invoice.getInvoiceType().getLineCountNumeric().setValue(invoice.getInvoiceType().getLineCountNumeric().getValue().add(BigDecimal.ONE));
					}
					InvoiceUtil.createEBSLineItem(invoice.getInvoiceType(), rs);
				}
			} catch (Exception e) {
				_LOG.error("Error while getting the EBS transaction records", e);
				try {
					failedInvs.add(invoiceId);
					invoiceMap.remove(invoiceId);
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("icv", invoice.getIcv());
					param.addValue("invoiceId", invoiceId);	
					param.addValue("message", e.getMessage());	
					jdbcTemplate.update("UPDATE XX_B2B_EBS_EINV_REP SET ICV = :icv, XML_REPORT_FLAG = 'F', ERROR_MSG = :message WHERE INVOICE_ID = :invoiceId ", param);
				} catch (Exception e2) {
					_LOG.warn("Error while updating the EBS error status" + invoiceNumber, e2);
				}
			}
		}
		return invoiceMap.values().stream().collect(Collectors.toList());
	};

	private final ResultSetExtractor<List<InvoiceInfo<String>>> POS_INVOICE_RESULT_SET = (rs) -> {
		Map<String, InvoiceInfo<String>> invoiceMap = new HashMap<>();
		Set<String> failedInvs = new HashSet<>();
		while (rs.next()) {
			String invoiceNumber = rs.getString("INVOICE_NO");
			InvoiceInfo<String> invoice = invoiceMap.get(invoiceNumber);
			String tranType = rs.getString("TRAN_TYPE");
			try {
				if (!failedInvs.contains(invoiceNumber)) {
					if (invoice == null) {
						invoice = new InvoiceInfo<String>();
						invoice.setInvoiceId(rs.getLong("TRAN_SEQ_NO"));
						invoice.setCustomerEmail(applicationProperty.getB2bPOSEmailTo());
						invoice.setInvoiceType(InvoiceUtil.createPOSInvoice(rs));
						invoice.setInvoiceIdentifier(invoiceNumber);
						invoice.setType("POS");
						invoiceMap.put(invoiceNumber, invoice);
	
					} else {
						invoice.getInvoiceType().getLineCountNumeric().setValue(invoice.getInvoiceType().getLineCountNumeric().getValue().add(BigDecimal.ONE));
					}
					InvoiceUtil.createPOSLineItem(invoice.getInvoiceType(), rs, tranType.equals("SALE") ? BigDecimal.ONE : BigDecimal.ONE.negate());
				}
			} catch (Exception e) {
				_LOG.error("Error while getting the POS transaction record for " + invoiceNumber, e);
				try {
					failedInvs.add(invoiceNumber);
					invoiceMap.remove(invoiceNumber);
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("icv", invoice.getIcv());
					param.addValue("invoiceId", invoice.getInvoiceId());	
					jdbcTemplate.update("UPDATE XX_POS_B2B_EINV SET ICV = :icv, PROCESS_FLAG = 'F' WHERE TRAN_SEQ_NO = :invoiceId ", param);
				} catch (Exception e2) {
					_LOG.warn("Error while upating the error status" + invoiceNumber, e2);
				}
			}
		}
		return invoiceMap.values().stream().collect(Collectors.toList());
	};

	public List<InvoiceInfo<String>> getEBSInvoices() {
		return jdbcTemplate.query("SELECT * FROM XX_B2B_EBS_EINV_REP WHERE XML_REPORT_FLAG = 'N'", EBS_INVOICE_RESULT_SET);
	}

	public <T> void updateInvoiceStatus(InvoiceInfo<T> invoiceInfo) {

		try {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("status", invoiceInfo.getClearanceStatus());
			param.addValue("message", invoiceInfo.getErrorMessage());
			param.addValue("invoiceId", invoiceInfo.getInvoiceId());	
			param.addValue("icv", invoiceInfo.getIcv());
			jdbcTemplate.update("UPDATE XX_B2B_EBS_EINV_REP SET XML_REPORT_FLAG = :status, XML_PROCESS_DATE = SYSDATE, ERROR_MSG = :message, ICV = :icv WHERE INVOICE_ID = :invoiceId",
					param);
			_LOG.debug("Status updatd to database successfully");
		} catch (Exception e) {
			_LOG.error("Error while updating the status to the database", e);
		}
	}

	public <T> void saveClearedInvoice(InvoiceInfo<T> invoiceInfo, String orgSystem) {
		try {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
			param.addValue("bDate", invoiceInfo.getInvoiceType().getIssueDate().getValue().toGregorianCalendar().getTime());
			param.addValue("xmlMsg", invoiceInfo.getClearedXml() != null ? invoiceInfo.getClearedXml() : invoiceInfo.getErrorMessage());
			param.addValue("status", invoiceInfo.getClearanceStatus());
			param.addValue("orgSystem", orgSystem);
			param.addValue("hash", invoiceInfo.getHash());
			param.addValue("type", invoiceInfo.getInvoiceType().getInvoiceTypeCode().getValue());
			jdbcTemplate.update("INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, HASH_VALUE) VALUES(:invoiceNum, :orgSystem, 'B2B', :bDate, :xmlMsg, SYSDATE, :status, :type, :hash)", param);
		} catch (Exception e) {
			_LOG.error("Error while inserting the status to the database", e);
		}
	}

	public <T> void updatePOSInvoice(InvoiceInfo<T> invoiceInfo) {
		try {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("status", invoiceInfo.getClearanceStatus());
			param.addValue("message", invoiceInfo.getErrorMessage());
			param.addValue("invoiceNum", invoiceInfo.getInvoiceId());
			param.addValue("icv", invoiceInfo.getIcv());
			jdbcTemplate.update("UPDATE XX_POS_B2B_EINV SET PROCESS_FLAG = :status, PROCESS_DATE = SYSDATE, ERROR_MSG = :message, ICV = :icv WHERE TRAN_SEQ_NO = :invoiceNum",
					param);
			_LOG.info("Status updatd to database successfully");
		} catch (Exception e) {
			_LOG.error("Error while updating the status to the database", e);
		}
	}

	public List<InvoiceInfo<String>> getB2BInvoices() {
		return jdbcTemplate.query(B2B_POS_SELECT_QUERY, Collections.singletonMap("posStoreIds", applicationProperty.getB2bStoreIds()) , POS_INVOICE_RESULT_SET);
	}

	public <T> void updateEBSEmailStatus(InvoiceInfo<T> invoiceInfo, String status) {
		try {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("invoiceId", invoiceInfo.getInvoiceId());
			param.addValue("eMailStatus", status);
			jdbcTemplate.update(
					"UPDATE XX_B2B_EBS_EINV_REP SET EMAIL_STATUS = :eMailStatus WHERE INVOICE_ID = :invoiceId", param);
			_LOG.debug("EBS Email Status updatd to database successfully", status);
		} catch (Exception e) {
			_LOG.error("Error while updating the EBS Email status to the database", e);
		}
	}

	public <T> void updatePOSEmailStatus(InvoiceInfo<T> invoiceInfo, String status) {
		try {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("invoiceNum", invoiceInfo.getInvoiceId());
			param.addValue("eMailStatus", status);
			jdbcTemplate.update(
					"UPDATE XX_POS_B2B_EINV SET EMAIL_STATUS = :eMailStatus WHERE TRAN_SEQ_NO = :invoiceNum", param);
			_LOG.debug("POS Email Status updatd to database successfully", status);
		} catch (Exception e) {
			_LOG.error("Error while updating the POS Email status to the database", e);
		}
	}

	public List<InvoiceInfo<String>> getEBSEmailDetails() {
		List<InvoiceInfo<String>> invoiceInfos = new ArrayList<>();
		String query = "SELECT P.INVOICE_ID, P.INVOICE_NUM, G.XML_GEN, P.EMAIL_ADDRESS, \"type\", EMAIL_STATUS "
				+ "FROM XX_B2B_EBS_EINV_REP P, XX_EINV_XML_GAZT_REP G WHERE EMAIL_STATUS = :emailStatus AND G.INVOICE_NUM = TO_CHAR(P.INVOICE_NUM) AND G.GAZT_RPT_FLAG = :gaztRptFlag AND G.BUSINESS_DATE = P.INVOICE_DATE";

		Map<String, Object> params = new HashMap<>();
		params.put("emailStatus", "F");
		params.put("gaztRptFlag", "Y");

		try {
			invoiceInfos = jdbcTemplate.query(query, params, resultSet -> {
				Map<Long, InvoiceInfo<String>> invs = new HashMap<>();
				while (resultSet.next()) {
					Long invId = resultSet.getLong("INVOICE_ID");
					if (!invs.containsKey(invId)) {
						InvoiceInfo<String> invoiceInfo = new InvoiceInfo<>();
						invoiceInfo.setInvoiceId(invId);
						invoiceInfo.setInvoiceIdentifier(resultSet.getString("INVOICE_NUM"));
						invoiceInfo.setClearedXml(resultSet.getString("XML_GEN"));
						invoiceInfo.setCustomerEmail(resultSet.getString("EMAIL_ADDRESS"));
						invoiceInfo.setType(resultSet.getString("TYPE"));
						invs.put(invId, invoiceInfo);
					}
				}
				return new ArrayList<>(invs.values());
			});

		} catch (Exception e) {
			_LOG.error("Error while getting the EBS transaction records", e);
		}
		return invoiceInfos;
	}

	public List<InvoiceInfo<String>> getPOSEmailDetails() {
		List<InvoiceInfo<String>> invoiceInfos = new ArrayList<>();
		String query = "SELECT P.TRAN_SEQ_NO, P.INVOICE_NO, G.XML_GEN, EMAIL_STATUS FROM XX_POS_B2B_EINV P, XX_EINV_XML_GAZT_REP G WHERE  EMAIL_STATUS = :emailStatus AND G.INVOICE_NUM = TO_CHAR(P.INVOICE_NO) AND G.GAZT_RPT_FLAG = :gaztRptFlag";
		Map<String, Object> params = new HashMap<>();
		params.put("emailStatus", "F");
		params.put("gaztRptFlag", "Y");
		try {
			invoiceInfos = jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
			InvoiceInfo<String> invoiceInfo = new InvoiceInfo<>();
			invoiceInfo.setInvoiceId(resultSet.getLong("TRAN_SEQ_NO"));
			invoiceInfo.setCustomerEmail(applicationProperty.getB2bPOSEmailTo());
			invoiceInfo.setInvoiceIdentifier(resultSet.getString("INVOICE_NO"));
			invoiceInfo.setType("POS");
			invoiceInfo.setClearedXml(resultSet.getString("XML_GEN"));
			return invoiceInfo;
		});

		} catch (Exception e) {
			_LOG.error("Error while getting the EBS transaction records for sending email", e);
		}
		return invoiceInfos;
	}
}
