package com.extra.jood.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoodSyncRequest {

	 @JsonProperty("membershipId")
	    private long membershipId;

	    @JsonProperty("Customer_mobile")
	    private String customerMobile;

	    @JsonProperty("Customer_Email")
	    private String customerEmail;

	    @JsonProperty("Customer_first_name")
	    private String customerFirstName;

	    @JsonProperty("Customer_last_name")
	    private String customerLastName;

	    @JsonProperty("Contract_number")
	    private String contractNumber;

	    @JsonProperty("Invoice_number")
	    private String invoiceNumber;

	    @JsonProperty("Invoice_line_number")
	    private String invoiceLineNumber;

	    @JsonProperty("Service_product_line")
	    private String serviceProductLine;

	    @JsonProperty("Package_name")
	    private String packageName;

	    @JsonProperty("Service_SKU")
	    private String serviceSku;

	    @JsonProperty("Contract_period")
	    private int contractPeriod;

	    @JsonProperty("Contract_starting_date")
	    private String contractStartingDate;

	    @JsonProperty("Contract_ending_date")
	    private String contractEndingDate;

	    @JsonProperty("Source_system")
	    private String sourceSystem;

	    @JsonProperty("Contract_status")
	    private String contractStatus;

	    @JsonProperty("Paid_Amount")
	    private BigDecimal paidAmount;

	public long getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(long membershipId) {
		this.membershipId = membershipId;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoiceLineNumber() {
		return invoiceLineNumber;
	}

	public void setInvoiceLineNumber(String invoiceLineNumber) {
		this.invoiceLineNumber = invoiceLineNumber;
	}

	public String getServiceProductLine() {
		return serviceProductLine;
	}

	public void setServiceProductLine(String serviceProductLine) {
		this.serviceProductLine = serviceProductLine;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getServiceSku() {
		return serviceSku;
	}

	public void setServiceSku(String serviceSku) {
		this.serviceSku = serviceSku;
	}

	public int getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(int contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public String getContractStartingDate() {
		return contractStartingDate;
	}

	public void setContractStartingDate(String contractStartingDate) {
		this.contractStartingDate = contractStartingDate;
	}

	public String getContractEndingDate() {
		return contractEndingDate;
	}

	public void setContractEndingDate(String contractEndingDate) {
		this.contractEndingDate = contractEndingDate;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

}
