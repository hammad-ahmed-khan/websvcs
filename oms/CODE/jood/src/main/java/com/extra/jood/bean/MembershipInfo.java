package com.extra.jood.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * aibrahim
 * 2024
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipInfo {

	private Long activeMembershipID;

	private Long membershipTypeID;

	private boolean renewUpgradeDetails;

	private TransactionInfo transaction;

	public Long getActiveMembershipID() {
		return activeMembershipID;
	}

	public boolean isRenewUpgradeDetails() {
		return renewUpgradeDetails;
	}

	public TransactionInfo getTransaction() {
		return transaction;
	}

	public void setActiveMembershipID(Long activeMembershipID) {
		this.activeMembershipID = activeMembershipID;
	}

	public void setRenewUpgradeDetails(boolean renewUpgradeDetails) {
		this.renewUpgradeDetails = renewUpgradeDetails;
	}

	public void setTransaction(TransactionInfo transaction) {
		this.transaction = transaction;
	}

	public Long getMembershipTypeID() {
		return membershipTypeID;
	}

	public void setMembershipTypeID(Long membershipTypeID) {
		this.membershipTypeID = membershipTypeID;
	}
}
