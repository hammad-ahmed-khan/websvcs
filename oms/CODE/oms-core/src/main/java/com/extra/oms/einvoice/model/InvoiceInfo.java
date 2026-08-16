package com.extra.oms.einvoice.model;

import java.util.Date;

import com.extra.oms.common.adapter.DateAdapter;
import com.google.gson.annotations.JsonAdapter;

/**
 * @author aibrahim
 *
 */
public class InvoiceInfo {

	private String invoiceNumber;

	private String invoiceId;

	private String orderNumber;

	private String source;

	private String invoiceType;

	@JsonAdapter(DateAdapter.class)
	private Date businessDate;

	@JsonAdapter(DateAdapter.class)
	private Date processDateTime;

	private Character status;

	private String message;

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getSource() {
		return source;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public Date getProcessDateTime() {
		return processDateTime;
	}

	public Character getStatus() {
		return status;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public void setProcessDateTime(Date processDateTime) {
		this.processDateTime = processDateTime;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
