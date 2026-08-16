package com.extra.jood.bean;

public class CbHistoryRequest {

	private long activeMembershipID;
	private long membershipTypeID;
	private long mobileNo;
	private String orderNo;
	private long offSet;
	private long size;

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

	public long getOffSet() {
		return offSet;
	}

	public void setOffSet(long offSet) {
		this.offSet = offSet;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	
}
