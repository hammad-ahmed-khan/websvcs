package com.extra.oms.einvoicingxml.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HybrisOmsXmlRequest {

	@JsonProperty(value = "order_req_id")
	private Long OrderRequestorId;
	@JsonProperty(value = "invoice_no")
	private String InvoiceNumber;
	@JsonProperty(value = "order_Id")
	private String OrderId;
	@JsonProperty(value = "tran_type")
	private String TransactionType;
	@JsonProperty(value = "tran_date")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date TransactionDate;
	@JsonProperty(value = "generated_xml")
	private String GeneratedXml;

	public Long getOrderRequestorId() {
		return OrderRequestorId;
	}

	public void setOrderRequestorId(Long orderRequestorId) {
		OrderRequestorId = orderRequestorId;
	}

	public String getInvoiceNumber() {
		return InvoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public Date getTransactionDate() {
		return TransactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		TransactionDate = transactionDate;
	}

	public String getGeneratedXml() {
		return GeneratedXml;
	}

	public void setGeneratedXml(String generatedXml) {
		GeneratedXml = generatedXml;
	}

}
