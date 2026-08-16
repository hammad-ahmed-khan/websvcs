package com.extra.einvoicing.model;

import java.math.BigDecimal;

public class EReconDetail {

	private String invoiceNumber;

	private Long invoiceLineId;

	private BigDecimal qty;

	private BigDecimal baseQty;

	private BigDecimal vatRate;

	private BigDecimal unitAmtExTax;

	private BigDecimal totalAmtExTax;

	private BigDecimal discAmt;

	private BigDecimal totalVat;

	private BigDecimal totalAmt;

	private String item;

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public Long getInvoiceLineId() {
		return invoiceLineId;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public BigDecimal getVatRate() {
		return vatRate;
	}

	public BigDecimal getUnitAmtExTax() {
		return unitAmtExTax;
	}

	public BigDecimal getTotalAmtExTax() {
		return totalAmtExTax;
	}

	public BigDecimal getDiscAmt() {
		return discAmt;
	}

	public BigDecimal getTotalVat() {
		return totalVat;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setInvoiceLineId(Long invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}

	public void setUnitAmtExTax(BigDecimal unitAmtExTax) {
		this.unitAmtExTax = unitAmtExTax;
	}

	public void setTotalAmtExTax(BigDecimal totalAmtExTax) {
		this.totalAmtExTax = totalAmtExTax;
	}

	public void setDiscAmt(BigDecimal discAmt) {
		this.discAmt = discAmt;
	}

	public void setTotalVat(BigDecimal tatalVat) {
		this.totalVat = tatalVat;
	}

	public void setTotalAmt(BigDecimal tatolAmt) {
		this.totalAmt = tatolAmt;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItem() {
		return item;
	}

	public BigDecimal getBaseQty() {
		return baseQty;
	}

	public void setBaseQty(BigDecimal baseQty) {
		this.baseQty = baseQty;
	}
}
