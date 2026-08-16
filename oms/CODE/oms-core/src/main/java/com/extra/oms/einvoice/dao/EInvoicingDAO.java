package com.extra.oms.einvoice.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.extra.oms.common.util.NumberUtil;
import com.extra.oms.core.dao.BaseDAO;
import com.extra.oms.einvoice.model.InvoiceInfo;

/**
 * @author aibrahim
 *
 */
@Repository
public class EInvoicingDAO extends BaseDAO {

	@Autowired
	@Qualifier("rmsJndiTemplate")
	protected NamedParameterJdbcTemplate rmsJndiTemplate;

	public List<InvoiceInfo> getInvoices(Map<String, String> searchParams) {
		StringBuilder queryBuilder = new StringBuilder("SELECT INVOICE_NUM, ORDER_NO, INVOICE_ORIG_SYS, BUSINESS_DATE, PROCESS_DATE, INVOICE_TYPE, GAZT_RPT_FLAG FROM XX_EINV_XML_GAZT_REP");
		String paramValue = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if ((paramValue = searchParams.get("invoiceNumber")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" INVOICE_NUM = :invoiceNumber ");
			paramMap.put("invoiceNumber", paramValue.trim());
		}
		if ((paramValue = searchParams.get("source")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" INVOICE_ORIG_SYS = :source ");
			paramMap.put("source", paramValue.trim());
		}
		if ((paramValue = searchParams.get("invoiceType")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" INVOICE_TYPE = :invoiceType ");
			paramMap.put("invoiceType", paramValue.trim());
		}
		if ((paramValue = searchParams.get("status")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" GAZT_RPT_FLAG = :status ");
			paramMap.put("status", paramValue.trim());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if ((paramValue = searchParams.get("fromDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (PROCESS_DATE >= :fromDate OR BUSINESS_DATE >= :fromDate) ");
			try {
				paramMap.put("fromDate", NumberUtil.getStartDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ((paramValue = searchParams.get("toDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (PROCESS_DATE <= :toDate OR BUSINESS_DATE <= :toDate) ");
			try {
				paramMap.put("toDate", NumberUtil.getEndDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return rmsJndiTemplate.query(queryBuilder.toString(), paramMap, new RowMapper<InvoiceInfo>() {

			@Override
			public InvoiceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InvoiceInfo invoice = new InvoiceInfo();
				invoice.setInvoiceNumber(rs.getString("INVOICE_NUM"));
				invoice.setOrderNumber(rs.getString("ORDER_NO"));
				invoice.setSource(rs.getString("INVOICE_ORIG_SYS"));
				invoice.setBusinessDate(rs.getTimestamp("BUSINESS_DATE"));
				invoice.setProcessDateTime(rs.getTimestamp("PROCESS_DATE"));
				invoice.setInvoiceType(rs.getString("INVOICE_TYPE"));
				invoice.setStatus(rs.getString("GAZT_RPT_FLAG").charAt(0));
				return invoice;
			}
		});
	}

	public String getInvoiceXML(String invoiceNumber) {
		return rmsJndiTemplate.queryForObject("SELECT XML_GEN FROM XX_EINV_XML_GAZT_REP WHERE INVOICE_NUM = :invoiceNumber ", Collections.singletonMap("invoiceNumber", invoiceNumber), String.class);
	}

	public String getError(String invoiceNumber) {
		return rmsJndiTemplate.queryForObject("SELECT ERROR_FLAG FROM XX_EINV_XML_GAZT_REP WHERE INVOICE_NUM = :invoiceNumber ", Collections.singletonMap("invoiceNumber", invoiceNumber), String.class);
	}

	public List<InvoiceInfo> getEBSInvoices(Map<String, String> searchParams) {
		StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT INVOICE_ID, \"type\", INVOICE_NUM, INVOICE_DATE, VENDOR_NAME, ORIGINAL_INVOICE, XML_REPORT_FLAG, XML_PROCESS_DATE, TO_CHAR(ERROR_MSG) AS ERROR FROM XX_B2B_EBS_EINV_REP ");
		String paramValue = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if ((paramValue = searchParams.get("invoiceNumber")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" INVOICE_NUM = :invoiceNumber ");
			paramMap.put("invoiceNumber", paramValue.trim());
		}
		if ((paramValue = searchParams.get("type")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" \"type\" = :type ");
			paramMap.put("type", paramValue.trim());
		}
		if ((paramValue = searchParams.get("status")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" XML_REPORT_FLAG = :status ");
			paramMap.put("status", paramValue.trim());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if ((paramValue = searchParams.get("fromDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (XML_PROCESS_DATE >= :fromDate OR INVOICE_DATE >= :fromDate) ");
			try {
				paramMap.put("fromDate", NumberUtil.getStartDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ((paramValue = searchParams.get("toDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (XML_PROCESS_DATE <= :toDate OR INVOICE_DATE <= :toDate) ");
			try {
				paramMap.put("toDate", NumberUtil.getEndDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return rmsJndiTemplate.query(queryBuilder.toString(), paramMap, new RowMapper<InvoiceInfo>() {

			@Override
			public InvoiceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InvoiceInfo invoice = new InvoiceInfo();
				invoice.setInvoiceId(rs.getString("INVOICE_ID"));
				invoice.setInvoiceNumber(rs.getString("INVOICE_NUM"));
				invoice.setOrderNumber(rs.getString("ORIGINAL_INVOICE"));
				invoice.setSource(rs.getString("VENDOR_NAME"));
				invoice.setBusinessDate(rs.getTimestamp("INVOICE_DATE"));
				invoice.setProcessDateTime(rs.getTimestamp("XML_PROCESS_DATE"));
				invoice.setInvoiceType(rs.getString("TYPE"));
				invoice.setStatus(rs.getString("XML_REPORT_FLAG").charAt(0));
				invoice.setMessage(rs.getString("ERROR"));
				return invoice;
			}
		});
	}

	public List<InvoiceInfo> getB2BPOSInvoices(Map<String, String> searchParams) {
		StringBuilder queryBuilder = new StringBuilder("SELECT TRAN_SEQ_NO, INVOICE_NO, PROCESS_FLAG, BUSINESS_DATE, PROCESS_DATE, ERROR_MSG FROM XX_POS_B2B_EINV ");
		String paramValue = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if ((paramValue = searchParams.get("invoiceNumber")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" INVOICE_NO = :invoiceNumber ");
			paramMap.put("invoiceNumber", paramValue.trim());
		}
		if ((paramValue = searchParams.get("status")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" PROCESS_FLAG = :status ");
			paramMap.put("status", paramValue.trim());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		if ((paramValue = searchParams.get("fromDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (PROCESS_DATE >= :fromDate OR BUSINESS_DATE >= :fromDate) ");
			try {
				paramMap.put("fromDate", NumberUtil.getStartDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if ((paramValue = searchParams.get("toDate")) != null && !paramValue.trim().isEmpty()) {
			appendWhere(queryBuilder);
			queryBuilder.append(" (PROCESS_DATE <= :toDate OR BUSINESS_DATE <= :toDate) ");
			try {
				paramMap.put("toDate", NumberUtil.getEndDay(dateFormat.parse(paramValue.trim())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return rmsJndiTemplate.query(queryBuilder.toString(), paramMap, new RowMapper<InvoiceInfo>() {

			@Override
			public InvoiceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				InvoiceInfo invoice = new InvoiceInfo();
				invoice.setInvoiceId(rs.getString("TRAN_SEQ_NO"));
				invoice.setInvoiceNumber(rs.getString("INVOICE_NO"));
				invoice.setBusinessDate(rs.getTimestamp("BUSINESS_DATE"));
				invoice.setProcessDateTime(rs.getTimestamp("PROCESS_DATE"));
				invoice.setStatus(rs.getString("PROCESS_FLAG").charAt(0));
				invoice.setMessage(rs.getString("ERROR_MSG"));
				return invoice;
			}
		});
	}
}
