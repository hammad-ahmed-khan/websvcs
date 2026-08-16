package com.extra.einvoicing.model;

import java.util.Date;

import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

public class InvoiceInfo<T> {

	private Long invoiceId;

	private T invoiceIdentifier;

	private String customerEmail;

	private InvoiceType invoiceType;

	private String clearedXml;

	private Character clearanceStatus;

	private String errorMessage;

	private String type;

	private String orderNo;

	private Long icv;

	private Date businessDate;

	private Date processDate;

	private String originSystem;

	private Integer store;

	private String typeCode;

	private String hash;

	private String validateXML;

	public T getInvoiceIdentifier() {
		return invoiceIdentifier;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceIdentifier(T invoiceNumber) {
		this.invoiceIdentifier = invoiceNumber;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getClearedXml() {
		return clearedXml;
	}

	public Character getClearanceStatus() {
		return clearanceStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setClearedXml(String clearedXml) {
		this.clearedXml = clearedXml;
	}

	public void setClearanceStatus(Character clearanceStatus) {
		this.clearanceStatus = clearanceStatus;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getIcv() {
		return icv;
	}

	public void setIcv(Long icv) {
		this.icv = icv;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public String getOriginSystem() {
		return this.originSystem;
	}

	public Integer getStore() {
		return this.store;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setOriginSystem(String originSystem) {
		this.originSystem = originSystem;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getValidateXML() {
		return validateXML;
	}

	public void setValidateXML(String validateXML) {
		this.validateXML = validateXML;
	}
}
