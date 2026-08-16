package com.extra.jood.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipCbResponse {

	private long activeMembershipID;
	private long membershipTypeID;
	private long membershipProgram;
	private String totalPurchasesThreshold;
	private String totalPurchases;
	private String remainingLimit;
	private String totalAvailableCashback;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yy hh.mm.ss.SSS a", locale = "en")
	private Date lastTransactionDate;
	private ResponseInfo responseHeader;

	private Transaction transaction;

	// Getters and Setters
	public long getActiveMembershipID() {
		return activeMembershipID;
	}

	public void setActiveMembershipID(long activeMembershipID) {
		this.activeMembershipID = activeMembershipID;
	}

	public long getMembershipTypeID() {
		return membershipTypeID;
	}

	public void setMembershipTypeID(long membershipTypeID) {
		this.membershipTypeID = membershipTypeID;
	}

	public String getTotalPurchasesThreshold() {
		return totalPurchasesThreshold;
	}

	public void setTotalPurchasesThreshold(String totalPurchasesThreshold) {
		this.totalPurchasesThreshold = totalPurchasesThreshold;
	}

	public String getTotalPurchases() {
		return totalPurchases;
	}

	public void setTotalPurchases(String totalPurchases) {
		this.totalPurchases = totalPurchases;
	}

	public String getRemainingLimit() {
		return remainingLimit;
	}

	public void setRemainingLimit(String remainingLimit) {
		this.remainingLimit = remainingLimit;
	}

	public String getTotalAvailableCashback() {
		return totalAvailableCashback;
	}

	public void setTotalAvailableCashback(String totalAvailableCashback) {
		this.totalAvailableCashback = totalAvailableCashback;
	}

	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public ResponseInfo getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseInfo responseHeader) {
		this.responseHeader = responseHeader;
	}

	public long getMembershipProgram() {
		return membershipProgram;
	}

	public void setMembershipProgram(long membershipProgram) {
		this.membershipProgram = membershipProgram;
	}

}
