package com.extra.einvoicing.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.extra.einvoicing.model.InvoiceInfo;
import com.extra.einvoicing.util.InvoiceUtil;

/**
 * @author aibrahim
 *
 */
@Repository
public class B2CInvoicingDAO extends BaseDAO {

	private static final Logger _LOG = LoggerFactory.getLogger(B2CInvoicingDAO.class);

	private static final String CAN_RET_SELECT_QUERY = "SELECT V.UNIQUE_INV_ID, V.CUST_ORDER_NO, V.CAN_RET_ID, V.OMS_CUST_ORD_NO, NULL RESTOCK_AMOUNT, V.SRC_LOC_CRN CRN,"
			+ "V.CUST_FIRST_NAME || V.CUST_LAST_NAME VENDOR_NAME, V.ITEM, V.LINE_NO, V.CANCEL_CONF_QTY QTY, "
			+ "V.UNIT_RETAIL, V.UNIT_VAT_AMOUNT, V.TAXRATE, V.TOTAL_DISOUNT, V.PMP_QTY, V.PMP_AMT, V.PMP_TAX_TOTAL, V.TOTAL_PMP_AMOUNT, V.CREATE_DATETIME, V.ORIGINAL_INVOICE_NO, 'C' TYPE, "
			+ "COALESCE(I.ITEM_DESC_SECONDARY, I.SHORT_DESC) ITEM_DSC FROM XX_EINV_CAN_DETAILS_CLR_V V, ITEM_MASTER I WHERE PROCESS_FLAG = 'N' AND I.ITEM = V.ITEM "
			+ "UNION ALL "
			+ "SELECT V.UNIQUE_INV_ID, V.CUST_ORDER_NO, V.CAN_RET_ID, V.OMS_CUST_ORD_NO, V.RESTOCK_AMOUNT, V.SRC_LOC_CRN CRN,"
			+ "V.CUST_FIRST_NAME || V.CUST_LAST_NAME VENDOR_NAME, V.ITEM, V.LINE_NO, V.RECEIVED_QTY QTY, "
			+ "V.UNIT_RETAIL, V.UNIT_VAT_AMOUNT, V.TAXRATE, V.TOTAL_DISOUNT, V.PMP_QTY, V.PMP_AMT, V.PMP_TAX_TOTAL, V.TOTAL_PMP_AMOUNT, V.CREATE_DATETIME, V.ORIGINAL_INVOICE_NO, 'R' TYPE, "
			+ "COALESCE(I.ITEM_DESC_SECONDARY, I.SHORT_DESC) ITEM_DSC FROM XX_EINV_RMA_DETAILS_CLR_V V, ITEM_MASTER I WHERE PROCESS_FLAG = 'N' AND I.ITEM = V.ITEM ";

	private static final String SIEBEL_INVOICE_SELECT_QUERY = "SELECT INVOICE_CREATION_DATE, UNIQUE_INVOICE_NO, INVOICE_LINE_NO, SKU_DESCR ITEM, QTY, PRICE_EXCL_VAT, "
			+ "PRICE_INC_VAT, VAT_AMT, TOTAL_LINE_EXCL_VAT, TOTAL_LINE, VAT_PERCENTAGE, TRAN_TYPE, FIRST_NAME || ' ' || LAST_NAME VENDOR_NAME, ADDRESS1, PHONE, EMAIL, "
			+ "ORIGINAL_INVOICE_NO, (SELECT CRN FROM XX_EINV_STORE_CRN WHERE STORE = ORGANIZATION_ID) CRN, SKU FROM XX_EINV_SPARE_PART_STG WHERE PROCESS_IND = 'N'";

	private static final String SELECT_ITEM_VAT_SQL = "SELECT MAX(VAT_RATE) VAT_RATE, A.ITEM FROM VAT_ITEM A WHERE VAT_REGION = 1101 AND A.ITEM IN (:items) "
			+ "AND ACTIVE_DATE = (SELECT MAX(ACTIVE_DATE) FROM VAT_ITEM WHERE ITEM = A.ITEM AND VAT_REGION = A.VAT_REGION) "
			+ "GROUP BY A.ITEM";

	public List<InvoiceInfo<Long>> getCancelReturnDetails() {
		
		return jdbcTemplate.query(CAN_RET_SELECT_QUERY, (ResultSetExtractor<List<InvoiceInfo<Long>>>)(rs) -> {
			Map<Long, InvoiceInfo<Long>> invoiceMap = new HashMap<>();
			while(rs.next()) {
				Long invIdentifier = rs.getLong("UNIQUE_INV_ID");
				InvoiceInfo<Long> invoice = invoiceMap.get(invIdentifier);
				try {
					if (invoice == null) {
						invoice = new InvoiceInfo<Long>();
						invoice.setInvoiceId(invIdentifier);
						invoice.setInvoiceType(InvoiceUtil.createB2CInvoice(rs));
						invoice.setInvoiceIdentifier(invIdentifier);
						invoice.setOrderNo(rs.getString("CUST_ORDER_NO"));
						invoiceMap.put(invIdentifier, invoice);
					} else {
						invoice.getInvoiceType().getLineCountNumeric().setValue(invoice.getInvoiceType().getLineCountNumeric().getValue().add(BigDecimal.ONE));
					}
					InvoiceUtil.createB2CLineItem(invoice.getInvoiceType(), rs);
				} catch (Exception e) {
					_LOG.error("Error while getting the POS transaction records", e);
					throw new RuntimeException(e);
				}
			}
			return invoiceMap.values().stream().collect(Collectors.toList());
		});
	}

	public <T> void updateOMSXMLGeneration(List<InvoiceInfo<T>> processedInvs, List<InvoiceInfo<T>> xmlInvoices) {
		List<SqlParameterSource> params = new ArrayList<>(processedInvs.size());
		try {
			for (InvoiceInfo<T> invoiceInfo : processedInvs) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("status", invoiceInfo.getClearanceStatus());
				param.addValue("message", invoiceInfo.getErrorMessage());
				param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
				param.addValue("icv", invoiceInfo.getIcv());
				params.add(param);
			}
			jdbcTemplate.batchUpdate("UPDATE XX_EINV_CAN_RET_OMS SET PROCESS_FLAG = :status, PROCESS_DATE = SYSDATE, ERROR_MSG = :message, ICV = :icv WHERE UNIQUE_INV_ID = :invoiceNum",
				params.toArray(new SqlParameterSource[processedInvs.size()]));
			_LOG.info("Status updatd to database successfully for b2c forms");
			if (!xmlInvoices.isEmpty()) {
				params.clear();
				for (InvoiceInfo<T> invoiceInfo : xmlInvoices) {
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
					param.addValue("bDate", invoiceInfo.getInvoiceType().getIssueDate().getValue().toGregorianCalendar().getTime());
					param.addValue("xmlMsg", invoiceInfo.getClearedXml() != null ? invoiceInfo.getClearedXml() : invoiceInfo.getErrorMessage());
					param.addValue("status", invoiceInfo.getClearanceStatus().equals('Y') ? "N" : "F");
					param.addValue("type", invoiceInfo.getInvoiceType().getInvoiceTypeCode().getValue());
					param.addValue("ordNo", invoiceInfo.getOrderNo());
					param.addValue("hash", invoiceInfo.getHash());
					params.add(param);
				}
				jdbcTemplate.batchUpdate("INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, ORDER_NO, HASH_VALUE, VENDOR_ID) VALUES(:invoiceNum, 'OMS', 'B2C', :bDate, :xmlMsg, SYSDATE, :status, :type, :ordNo, :hash, 'extra')", params.toArray(new SqlParameterSource[xmlInvoices.size()]));
			}
		} catch (Exception e) {
			_LOG.error("Error while inserting the status to the database", e);
		}
	}

	public <T> void updateSiebelXML(List<InvoiceInfo<T>> processedInvs, List<InvoiceInfo<T>> xmlInvoices) {
		List<SqlParameterSource> params = new ArrayList<>(processedInvs.size());
		try {
			for (InvoiceInfo<T> invoiceInfo : processedInvs) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("status", invoiceInfo.getClearanceStatus());
				param.addValue("message", invoiceInfo.getErrorMessage());
				param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
				param.addValue("icv", invoiceInfo.getIcv());
				params.add(param);
			}
			jdbcTemplate.batchUpdate("UPDATE XX_EINV_SPARE_PART_STG SET PROCESS_IND = :status, PROCESS_DATE = SYSDATE, ERROR_MSG = :message, ICV = :icv WHERE UNIQUE_INVOICE_NO = :invoiceNum",
					params.toArray(new SqlParameterSource[processedInvs.size()]));
			_LOG.info("Status updatd to database successfully for b2c forms");
			if (!xmlInvoices.isEmpty()) {
				params.clear();
				for (InvoiceInfo<T> invoiceInfo : xmlInvoices) {
					MapSqlParameterSource param = new MapSqlParameterSource();
					param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
					param.addValue("bDate", invoiceInfo.getInvoiceType().getIssueDate().getValue().toGregorianCalendar().getTime());
					param.addValue("xmlMsg", invoiceInfo.getClearedXml() != null ? invoiceInfo.getClearedXml() : invoiceInfo.getErrorMessage());
					param.addValue("status", invoiceInfo.getClearanceStatus().equals('Y') ? "N" : "F");
					param.addValue("type", invoiceInfo.getInvoiceType().getInvoiceTypeCode().getValue());
					param.addValue("ordNo", invoiceInfo.getOrderNo());
					param.addValue("hash", invoiceInfo.getHash());
					params.add(param);
				}
				jdbcTemplate.batchUpdate("INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, ORDER_NO, HASH_VALUE, VENDOR_ID) VALUES(:invoiceNum, 'SEIBEL', 'B2C', :bDate, :xmlMsg, SYSDATE, :status, :type, :ordNo, :hash, 'extra')", params.toArray(new SqlParameterSource[xmlInvoices.size()]));
			}
		} catch (Exception e) {
			_LOG.error("Error while inserting the status to the database", e);
		}
	}

	public List<InvoiceInfo<String>> getSignedInvoices() {
		return jdbcTemplate.query("SELECT INVOICE_NUM, BUSINESS_DATE, XML_GEN, STORE, INVOICE_ORIG_SYS, INVOICE_TYPE, ORDER_NO, VALIDATE_XML FROM XX_EINV_XML_GAZT_REP WHERE GAZT_RPT_FLAG = 'N' AND RESA_VLD_FLAG = 'Y' AND VENDOR_ID = 'extra'", (RowMapper<InvoiceInfo<String>>)(rs, rNum) -> {
			InvoiceInfo<String> invoice = new InvoiceInfo<>();
			invoice.setInvoiceIdentifier(rs.getString(1));
			invoice.setBusinessDate(rs.getDate(2));
			invoice.setClearedXml(rs.getString(3));
			invoice.setStore(rs.getInt(4));
			invoice.setOriginSystem(rs.getString(5));
			invoice.setType(rs.getString(6));
			invoice.setOrderNo(rs.getString(7));
			invoice.setValidateXML(rs.getString(8));
			return invoice;
		});
	}

	public void updateXMLReporting(List<InvoiceInfo<String>> processedInvs) {
		List<SqlParameterSource> params = new ArrayList<>(processedInvs.size());
		try {
			for (InvoiceInfo<String> invoiceInfo : processedInvs) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("status", invoiceInfo.getClearanceStatus());
				param.addValue("message", invoiceInfo.getErrorMessage());
				param.addValue("invoiceNum", invoiceInfo.getInvoiceIdentifier());
				params.add(param);
			}
			jdbcTemplate.batchUpdate("UPDATE XX_EINV_XML_GAZT_REP SET GAZT_RPT_FLAG = :status, PROCESS_DATE = SYSDATE, ERROR_FLAG = :message WHERE INVOICE_NUM = :invoiceNum AND GAZT_RPT_FLAG = 'N' ",
				params.toArray(new SqlParameterSource[processedInvs.size()]));
			_LOG.info("Reporting Status updatd to database successfully for b2c forms");
		} catch (Exception e) {
			_LOG.error("Error while updating the status to the database", e);
		}
	}

	public List<InvoiceInfo<String>> getSiebelInvoices() {
		return jdbcTemplate.query(SIEBEL_INVOICE_SELECT_QUERY, (ResultSetExtractor<List<InvoiceInfo<String>>>)(rs) -> {
			Map<String, InvoiceInfo<String>> invoiceMap = new HashMap<>();
			while(rs.next()) {
				String invIdentifier = rs.getString("UNIQUE_INVOICE_NO");
				InvoiceInfo<String> invoice = invoiceMap.get(invIdentifier);
				try {
					if (invoice == null) {
						invoice = new InvoiceInfo<String>();
						invoice.setInvoiceType(InvoiceUtil.createSeibelInvoice(rs));
						invoice.setInvoiceIdentifier(invIdentifier);
						invoiceMap.put(invIdentifier, invoice);
					} else {
						invoice.getInvoiceType().getLineCountNumeric().setValue(invoice.getInvoiceType().getLineCountNumeric().getValue().add(BigDecimal.ONE));
					}
					InvoiceUtil.createSeibelLineItem(invoice.getInvoiceType(), rs, !rs.getString("TRAN_TYPE").equals("RETURN") ? BigDecimal.ONE : BigDecimal.ONE.negate());
				} catch (Exception e) {
					_LOG.error("Error while getting the POS transaction records", e);
					throw new RuntimeException(e);
				}
			}
			return invoiceMap.values().stream().collect(Collectors.toList());
		});
	}

	public Map<String, BigDecimal> getItemVatRates(Set<String> items) {
		return jdbcTemplate.query(SELECT_ITEM_VAT_SQL, Collections.singletonMap("items", items), new ResultSetExtractor<Map<String, BigDecimal>>() {

			@Override
			public Map<String, BigDecimal> extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<String, BigDecimal> itemVatRates = new HashMap<>();
				while (rs.next()) {
					itemVatRates.put(rs.getString(2), rs.getBigDecimal(1));
				}
				return itemVatRates;
			}
		});
	}
}
