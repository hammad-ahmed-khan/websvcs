package com.extra.einvoicing.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

import com.extra.einvoicing.model.EReconDetail;
import com.extra.einvoicing.model.ReconHead;

/**
 * 
 */
public abstract class BaseDAO {

	@Autowired
	protected NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional
	public void persistInvForRecon(List<ReconHead> reconDatas) {
		List<SqlParameterSource> headSource = new ArrayList<SqlParameterSource>();
		List<SqlParameterSource> detailSource = new ArrayList<SqlParameterSource>();
		for (ReconHead recon : reconDatas) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("invoiceNum", recon.getInvoiceNumber());
			param.addValue("store", recon.getStore() != 0 ? recon.getStore() : null);
			param.addValue("orgSys", recon.getOriginSys());
			param.addValue("type", recon.getType());
			param.addValue("ordNo", recon.getOrderNo());
			param.addValue("businessDate", recon.getBusinessDate());
			param.addValue("issueDate", recon.getXmlIssueDate());
			param.addValue("taxAmt", recon.getTaxAmt());
			param.addValue("taxIncxAmt", recon.getTaxInclvAmt());
			param.addValue("invTypeCode", recon.getTypeCode());
			headSource.add(param);
			for (EReconDetail detail : recon.getDetails()) {
				param = new MapSqlParameterSource();
				param.addValue("invoiceNum", recon.getInvoiceNumber());
				param.addValue("lineId", detail.getInvoiceLineId());
				param.addValue("item", detail.getItem());
				param.addValue("qty", detail.getQty());
				param.addValue("vatRate", detail.getVatRate());
				param.addValue("unitAmtExVat", detail.getUnitAmtExTax());
				param.addValue("totAmtExVat", detail.getTotalAmtExTax());
				param.addValue("totalDiscAmt", detail.getDiscAmt());
				param.addValue("totalTaxAmt", detail.getTotalVat());
				param.addValue("taxIncxAmt", detail.getTotalAmt());
				param.addValue("baseQty", detail.getBaseQty());
				detailSource.add(param);
			}
		}
		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_EINV_RECONCIL_HEAD(INVOICE_NUM, STORE, INVOICE_ORIG_SYS, INVOICE_TYPE, ORDER_NO, BUSINESS_DATE, "
				+ "XML_ISSUE_DATE, TAX_AMT, TAX_INCLUSIVE_AMOUNT, INVOICE_TYPE_CODE) "
				+ "VALUES(:invoiceNum, :store, :orgSys, :type, :ordNo, :businessDate, :issueDate, :taxAmt, :taxIncxAmt, :invTypeCode)",
				headSource.toArray(new SqlParameterSource[headSource.size()]));

		jdbcTemplate.batchUpdate(
				"INSERT INTO XX_EINV_RECONCIL_DETAIL(INVOICE_NUM, INVOICE_LINE_ID, ITEM, QTY, VAT_RATE, UNIT_AMT_EXCLX_VAT, TOTAL_AMT_EXCLX_VAT, "
				+ "TOTAL_DISC_AMT, TOTAL_VAT, TOTAL_AMT_INX_VAT, BASE_QTY) "
				+ "VALUES(:invoiceNum, :lineId, :item, :qty, :vatRate, :unitAmtExVat, :totAmtExVat, :totalDiscAmt, :totalTaxAmt, :taxIncxAmt, :baseQty)",
				detailSource.toArray(new SqlParameterSource[detailSource.size()]));
	}

	public Long getICV() {
		return jdbcTemplate.queryForObject("SELECT XX_EINVOICE_COUNTER_SEQ.NEXTVAL FROM DUAL", Collections.emptyMap(), Long.class);
	}

	public String getPIH() {
		try {
			return jdbcTemplate.queryForObject("SELECT HASH_VALUE FROM (SELECT HASH_VALUE FROM XX_EINV_XML_GAZT_REP WHERE HASH_VALUE IS NOT NULL ORDER BY PROCESS_DATE DESC ) WHERE ROWNUM = 1", Collections.emptyMap(), String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
