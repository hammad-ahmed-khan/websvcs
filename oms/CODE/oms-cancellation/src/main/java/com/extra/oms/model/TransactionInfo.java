package com.extra.oms.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TransactionInfo.java 
 * aibrahim 
 * 2025
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionInfo {

	private long activeMembershipID;

	private String channel;

	private Date date;

	private Long location;

	private String country;

	private String transactionType;

	private String transactionNumber;

	private String orderNumber;

	private String origTranscationNumber;

	private BigDecimal totalRetailPrice;

	private BigDecimal totalRetailDiscount;

	private BigDecimal totalJoodDiscount;

	private BigDecimal totalSellingPrice;

	private BigDecimal totalRewardsCB;

	private BigDecimal totalRedeemedCB;

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

	public void setCountry(String country) {
		this.country = country;
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
}
