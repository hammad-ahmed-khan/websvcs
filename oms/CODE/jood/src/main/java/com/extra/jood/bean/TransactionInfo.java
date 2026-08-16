package com.extra.jood.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TransactionInfo.java 
 * aibrahim 
 * 2024
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInfo {

	private long activeMembershipID;

	private String channel;

	private Date date;

	private Long location;

	private String country;

	private CustomerInfo customer;

	private String transactionType;

	private String transactionNumber;

	private String orderNumber;

	private String origTranscationNumber;

	private BigDecimal totalRetailPrice;

	private BigDecimal totalRetailDiscount;

	private BigDecimal totalJoodDiscount;

	private BigDecimal totalSellingPrice;
	
	private String cashbackCheck;

	private BigDecimal totalRewardsCB;

	private BigDecimal totalRedeemedCB;
	
	private String isFirstPurchaseAvail;
	
	private long membershipProgram;

	@JsonProperty("lines")
	private List<TransactionDetail> transactionLineItems;

	public long getActiveMembershipID() {
		return activeMembershipID;
	}

	public String getChannel() {
		return channel;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public String getOrigTranscationNumber() {
		return origTranscationNumber;
	}

	public BigDecimal getTotalRetailPrice() {
		return totalRetailPrice;
	}

	public BigDecimal getTotalRetailDiscount() {
		return totalRetailDiscount;
	}

	public BigDecimal getTotalJoodDiscount() {
		return totalJoodDiscount;
	}

	public BigDecimal getTotalSellingPrice() {
		return totalSellingPrice;
	}

	public List<TransactionDetail> getTransactionLineItems() {
		return transactionLineItems;
	}

	public void setActiveMembershipID(long activeMembershipID) {
		this.activeMembershipID = activeMembershipID;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setOrigTranscationNumber(String origTranscationNumber) {
		this.origTranscationNumber = origTranscationNumber;
	}

	public void setTotalRetailPrice(BigDecimal totalRetailPrice) {
		this.totalRetailPrice = totalRetailPrice;
	}

	public void setTotalRetailDiscount(BigDecimal totalRetailDiscount) {
		this.totalRetailDiscount = totalRetailDiscount;
	}

	public void setTotalJoodDiscount(BigDecimal totalJoodDiscount) {
		this.totalJoodDiscount = totalJoodDiscount;
	}

	public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
		this.totalSellingPrice = totalSellingPrice;
	}

	public void setTransactionLineItems(List<TransactionDetail> transactionLineItems) {
		this.transactionLineItems = transactionLineItems;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getLocation() {
		return location;
	}

	public void setLocation(Long location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public CustomerInfo getCustomer() {
		return customer;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCustomer(CustomerInfo customer) {
		this.customer = customer;
	}

	public BigDecimal getTotalRewardsCB() {
		return totalRewardsCB;
	}

	public BigDecimal getTotalRedeemedCB() {
		return totalRedeemedCB;
	}

	public void setTotalRewardsCB(BigDecimal totalRewardsCB) {
		this.totalRewardsCB = totalRewardsCB;
	}

	public void setTotalRedeemedCB(BigDecimal totalRedeemedCB) {
		this.totalRedeemedCB = totalRedeemedCB;
	}

	public String getCashbackCheck() {
		return cashbackCheck;
	}

	public void setCashbackCheck(String cashbackCheck) {
		this.cashbackCheck = cashbackCheck;
	}

	public String getIsFirstPurchaseAvail() {
		return isFirstPurchaseAvail;
	}

	public void setIsFirstPurchaseAvail(String isFirstPurchaseAvail) {
		this.isFirstPurchaseAvail = isFirstPurchaseAvail;
	}

	public long getMembershipProgram() {
		return membershipProgram;
	}

	public void setMembershipProgram(long membershipProgram) {
		this.membershipProgram = membershipProgram;
	}


	
	
}
