package com.extra.oms.discount.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author aibrahim
 *
 */
public class DiscountInfo {

	private Integer store;

	@JsonProperty(value = "business_date")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date businessDate;

	@JsonProperty(value = "invoice_no")
	private String invoiceNo;

	@JsonProperty(value = "tran_date")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date tranDate;

	@JsonProperty(value = "tran_type")
	private String tranType;

	@JsonProperty(value = "cust_order_no")
	private String custOrdNo;

	private List<DiscountItem> items;

	public Integer getStore() {
		return store;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public Date getTranDate() {
		return tranDate;
	}

	public String getTranType() {
		return tranType;
	}

	public String getCustOrdNo() {
		return custOrdNo;
	}

	public List<DiscountItem> getItems() {
		return items;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
	}

	public void setItems(List<DiscountItem> items) {
		this.items = items;
	}
}
