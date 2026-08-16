/**
 * 
 */
package com.extra.oms.discount.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.extra.oms.discount.model.DiscountInfo;
import com.extra.oms.discount.model.DiscountItem;

/**
 * @author aibrahim
 *
 */
@Repository
public class DiscountDAO {

	@Autowired
	@Qualifier("jndiTemplate")
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void saveDiscountInfo(DiscountInfo discount) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>(discount.getItems().size());
		for (DiscountItem item : discount.getItems()) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("storeId", discount.getStore());
			params.put("businessDate", discount.getBusinessDate());
			params.put("invoiceNo", discount.getInvoiceNo());
			params.put("tranDate", discount.getTranDate());
			params.put("tranType", discount.getTranType());
			params.put("item", item.getItem());
			params.put("quantity", item.getQuantity());
			params.put("unitRetail", item.getUnitRetail());
			params.put("pmpAmount", item.getPmpAmount());
			params.put("extendUnitPrice", item.getExtendUnitPrice());
			params.put("taxAmount", item.getTaxAmount());
			params.put("origTranNo", item.getOrigTranNo());
			params.put("origTranItem", item.getOrigTranItem());
			params.put("origLineNo", item.getOrigTranLineNo());
			params.put("custOrdNo", discount.getCustOrdNo());
			list.add(params);
		}
		@SuppressWarnings("unchecked")
		Map<String, ?>[] paramArr = new HashMap[discount.getItems().size()];
		jdbcTemplate.batchUpdate("INSERT INTO XX_PMP_INV_DETAILS(STORE, BUSINESS_DATE, INVOICE_NO, TRAN_DATE, TRAN_TYPE, ITEM, QTY, UNIT_RETAIL, TAX_AMOUNT, ORIGINAL_TRAN_NO, ORIG_TRAN_ITEM, ORIG_TRAN_ITEM_LINE_NO, CUST_ORDER_NO, PROCESS_FLAG, OMS_CUST_ORD_NO, EXTD_UNIT_PRICE, PMP_AMT) "
				+ "VALUES(:storeId, :businessDate, :invoiceNo, :tranDate, :tranType, :item, :quantity, :unitRetail, :taxAmount, :origTranNo, :origTranItem, :origLineNo, :custOrdNo, 'N', (SELECT OMS_CUST_ORD_NO FROM OMS_CUST_ORD_HEAD WHERE CUST_ORDER_NO = :custOrdNo AND STATUS = 'S'), :extendUnitPrice, :pmpAmount)", list.toArray(paramArr));
	}
}
