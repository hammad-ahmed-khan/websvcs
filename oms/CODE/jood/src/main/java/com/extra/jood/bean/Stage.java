package com.extra.jood.bean;

import java.math.BigDecimal;
import java.util.List;

public class Stage {

	private String stage;
	
	private long membershipProgram;
	
	private String isFirstPurchaseAvail;
	
	private BigDecimal totalCbAvail;
	
	private BigDecimal totalCbEarned;

	private BigDecimal totalCbRedeemed;


	private List<CappingCheckResult> cappingCheckResults;

	public String getStage() {
		return stage;
	}

	public List<CappingCheckResult> getCappingCheckResults() {
		return cappingCheckResults;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public void setCappingCheckResults(List<CappingCheckResult> cappingCheckResults) {
		this.cappingCheckResults = cappingCheckResults;
	}

	public BigDecimal getTotalCbAvail() {
		return totalCbAvail;
	}

	public void setTotalCbAvail(BigDecimal totalCbAvail) {
		this.totalCbAvail = totalCbAvail;
	}

	public BigDecimal getTotalCbEarned() {
		return totalCbEarned;
	}

	public void setTotalCbEarned(BigDecimal totalCbEarned) {
		this.totalCbEarned = totalCbEarned;
	}

	public BigDecimal getTotalCbRedeemed() {
		return totalCbRedeemed;
	}

	public void setTotalCbRedeemed(BigDecimal totalCbRedeemed) {
		this.totalCbRedeemed = totalCbRedeemed;
	}

	public long getMembershipProgram() {
		return membershipProgram;
	}

	public void setMembershipProgram(long membershipProgram) {
		this.membershipProgram = membershipProgram;
	}

	public String getIsFirstPurchaseAvail() {
		return isFirstPurchaseAvail;
	}

	public void setIsFirstPurchaseAvail(String isFirstPurchaseAvail) {
		this.isFirstPurchaseAvail = isFirstPurchaseAvail;
	}
	
	
	
}
