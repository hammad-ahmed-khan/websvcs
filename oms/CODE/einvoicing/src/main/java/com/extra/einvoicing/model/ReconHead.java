package com.extra.einvoicing.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 */
public class ReconHead {

	private Long id;

	private String invoiceNumber;

	private Integer store;

	private String originSys;

	private String type;

	private String orderNo;

	private Date businessDate;

	private Date xmlIssueDate;

	private Date processDate;

	private BigDecimal taxExAmt;

	private BigDecimal taxAmt;

	private BigDecimal taxInclvAmt;

	private String typeCode;

	private List<EReconDetail> details;

	public Long getId() {
		return id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public Integer getStore() {
		return store;
	}

	public String getOriginSys() {
		return originSys;
	}

	public String getType() {
		return type;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public Date getXmlIssueDate() {
		return xmlIssueDate;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public BigDecimal getTaxAmt() {
		return taxAmt;
	}

	public BigDecimal getTaxInclvAmt() {
		return taxInclvAmt;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public void setOriginSys(String originSys) {
		this.originSys = originSys;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public void setXmlIssueDate(Date xmlIssueDate) {
		this.xmlIssueDate = xmlIssueDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}

	public void setTaxInclvAmt(BigDecimal taxInclvAmt) {
		this.taxInclvAmt = taxInclvAmt;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public void setDetails(List<EReconDetail> details) {
		this.details = details;
	}

	public List<EReconDetail> getDetails() {
		return details;
	}

	public BigDecimal getTaxExAmt() {
		return taxExAmt;
	}

	public void setTaxExAmt(BigDecimal taxExAmt) {
		this.taxExAmt = taxExAmt;
	}
}
