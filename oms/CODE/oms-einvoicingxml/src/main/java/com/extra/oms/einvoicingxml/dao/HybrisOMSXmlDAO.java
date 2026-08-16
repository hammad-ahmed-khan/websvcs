package com.extra.oms.einvoicingxml.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Base64Utils;

import com.extra.oms.einvoicingxml.controller.XmlController;
import com.extra.oms.einvoicingxml.model.HybrisOmsXmlRequest;

@Repository
public class HybrisOMSXmlDAO {

	private static final Logger LOG = Logger.getLogger(XmlController.class);

	@Autowired
	@Qualifier("jndiTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void saveXmlInfo(HybrisOmsXmlRequest hybomsxml) {
		LOG.info("Inside HybrisOMSXmlDAO class - saveXmlInfo method");
		MapSqlParameterSource params = new MapSqlParameterSource();
		try {
			params.addValue("orderRequestorid", hybomsxml.getOrderRequestorId());
			params.addValue("invoicenumber", hybomsxml.getInvoiceNumber());
			params.addValue("transactiontype", hybomsxml.getTransactionType());
			params.addValue("transactiondate", hybomsxml.getTransactionDate());
			params.addValue("ordNo", hybomsxml.getOrderId());
			params.addValue("generatedxml", new String(Base64Utils.decodeFromString(hybomsxml.getGeneratedXml())));

			LOG.info("Inside saveXmlInfo method - Before insertion");

			jdbcTemplate.update("INSERT INTO XX_EINV_XML_GAZT_REP(INVOICE_NUM, STORE, INVOICE_ORIG_SYS, INVOICE_TYPE, BUSINESS_DATE, XML_GEN, PROCESS_DATE, GAZT_RPT_FLAG, INVOICE_DESC, ERROR_FLAG, ORDER_NO) "
					+ "VALUES(:invoicenumber, :orderRequestorid, 'ECOM', 'B2C',  :transactiondate,  :generatedxml, null, 'N', :transactiontype, null, :ordNo)", params);

			LOG.info("Inside saveXmlInfo method - After insertion");

		} catch (Exception e) {
			LOG.error("Inside Catch: " + hybomsxml.getOrderId(), e);
			throw e;
		}
	}
}
