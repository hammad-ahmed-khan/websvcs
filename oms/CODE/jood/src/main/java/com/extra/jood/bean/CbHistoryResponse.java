package com.extra.jood.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CbHistoryResponse {

	private long activeMembershipID;
	private long membershipTypeID;
	private long mobileNo;
	private long totalAvailableCashback;
	private long totalTransactions;
	
	private List<OrderDetail> orderDetails;
	private ResponseInfo responseHeader;

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

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public long getTotalAvailableCashback() {
		return totalAvailableCashback;
	}

	public void setTotalAvailableCashback(long totalAvailableCashback) {
		this.totalAvailableCashback = totalAvailableCashback;
	}

	public List<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public ResponseInfo getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(ResponseInfo responseHeader) {
		this.responseHeader = responseHeader;
	}

	public long getTotalTransactions() {
		return totalTransactions;
	}

	public void setTotalTransactions(long totalTransactions) {
		this.totalTransactions = totalTransactions;
	}

}
