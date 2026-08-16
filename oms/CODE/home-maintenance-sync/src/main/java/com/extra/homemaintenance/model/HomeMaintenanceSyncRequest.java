package com.extra.homemaintenance.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HomeMaintenanceSyncRequest {
	@JsonProperty("country")
    private String country;

    @JsonProperty("subscritpionId")
    private long subscriptionId;

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

    // Getters & Setters

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
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
    
    
	/*@JsonProperty("mobileNumber")
	private String mobileNumber;

	@JsonProperty("email")
	private String email;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("membershipType")
	private String membershipType;

	@JsonProperty("startTime")
	private String startTime;

	@JsonProperty("endTime")
	private String endTime;

	@JsonProperty("amount")
	private String amount;

	@JsonProperty("membershipId")
	private Integer membershipId;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMembershipType() {
		return membershipType;
	}

	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Integer getMembershipId() {
		return membershipId;
	}

	public void setMembershipId(Integer membershipId) {
		this.membershipId = membershipId;
	}*/
}
