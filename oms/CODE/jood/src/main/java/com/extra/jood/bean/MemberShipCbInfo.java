package com.extra.jood.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

public class MemberShipCbInfo {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private long activeMembershipID;
	private long membershipTypeID;
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

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
