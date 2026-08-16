package com.extra.jood.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Transaction {

	private String transactionNumber;
	private String orderNumber;
	private String channel;
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yy hh.mm.ss.SSS a", locale = "en")
	private Date date;	
	private long location;
	private String country;

	private long totalRetailPrice;
	private long totalRetailDiscount;
	private long totalJoodDiscount;
	private long totalSellingPrice;

	private long totalRewardsCB;
	private long totalRedeemedCB;
	private long totalAvailableCB;
	private String expiryDate; 

	private List<Lines> lines;

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getLocation() {
		return location;
	}

	public void setLocation(long location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getTotalRetailPrice() {
		return totalRetailPrice;
	}

	public void setTotalRetailPrice(long totalRetailPrice) {
		this.totalRetailPrice = totalRetailPrice;
	}

	public long getTotalRetailDiscount() {
		return totalRetailDiscount;
	}

	public void setTotalRetailDiscount(long totalRetailDiscount) {
		this.totalRetailDiscount = totalRetailDiscount;
	}

	public long getTotalJoodDiscount() {
		return totalJoodDiscount;
	}

	public void setTotalJoodDiscount(long totalJoodDiscount) {
		this.totalJoodDiscount = totalJoodDiscount;
	}

	public long getTotalSellingPrice() {
		return totalSellingPrice;
	}

	public void setTotalSellingPrice(long totalSellingPrice) {
		this.totalSellingPrice = totalSellingPrice;
	}

	public long getTotalRewardsCB() {
		return totalRewardsCB;
	}

	public void setTotalRewardsCB(long totalRewardsCB) {
		this.totalRewardsCB = totalRewardsCB;
	}

	public long getTotalRedeemedCB() {
		return totalRedeemedCB;
	}

	public void setTotalRedeemedCB(long totalRedeemedCB) {
		this.totalRedeemedCB = totalRedeemedCB;
	}

	public long getTotalAvailableCB() {
		return totalAvailableCB;
	}

	public void setTotalAvailableCB(long totalAvailableCB) {
		this.totalAvailableCB = totalAvailableCB;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public List<Lines> getLines() {
		return lines;
	}

	public void setLines(List<Lines> lines) {
		this.lines = lines;
	}

	
}
