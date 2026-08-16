/**
 * 
 */
package com.extra.oms.discount.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author aibrahim
 *
 */
public class DiscountItem {

	private String item;

	@JsonProperty(value = "qty")
	private BigDecimal quantity;

	@JsonProperty(value = "unit_retail")
	private BigDecimal unitRetail;

	@JsonProperty(value = "extd_unit_price")
	private BigDecimal extendUnitPrice;

	@JsonProperty(value = "tax_amount")
	private BigDecimal taxAmount;

	@JsonProperty(value = "pmp_amt")
	private BigDecimal pmpAmount;

	@JsonProperty(value = "original_tran_no")
	private String origTranNo;

	@JsonProperty(value = "orig_tran_item")
	private String origTranItem;

	@JsonProperty(value = "orig_tran_item_line_no")
	private Integer origTranLineNo;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitRetail() {
		return unitRetail;
	}

	public void setUnitRetail(BigDecimal unitRetail) {
		this.unitRetail = unitRetail;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getOrigTranNo() {
		return origTranNo;
	}

	public void setOrigTranNo(String origTranNo) {
		this.origTranNo = origTranNo;
	}

	public String getOrigTranItem() {
		return origTranItem;
	}

	public void setOrigTranItem(String origTranItem) {
		this.origTranItem = origTranItem;
	}

	public Integer getOrigTranLineNo() {
		return origTranLineNo;
	}

	public void setOrigTranLineNo(Integer origTranLineNo) {
		this.origTranLineNo = origTranLineNo;
	}

	public BigDecimal getExtendUnitPrice() {
		return extendUnitPrice;
	}

	public void setExtendUnitPrice(BigDecimal extendUnitPrice) {
		this.extendUnitPrice = extendUnitPrice;
	}

	public BigDecimal getPmpAmount() {
		return pmpAmount;
	}

	public void setPmpAmount(BigDecimal pmpAmount) {
		this.pmpAmount = pmpAmount;
	}
}
