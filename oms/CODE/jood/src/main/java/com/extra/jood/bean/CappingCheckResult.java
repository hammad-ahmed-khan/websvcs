package com.extra.jood.bean;

import java.math.BigDecimal;

public class CappingCheckResult {

	private String status;

	private String levelType;
	
	private String levelIdentifier;

	private String levelDescription;
	
	private BigDecimal capping;

	private BigDecimal consumed;

	private BigDecimal available;

	private BigDecimal requested;

	private BigDecimal rejected;

	private Long lineNumber;
	
	private String redeemptionEligible;

	public String getStatus() {
		return status;
	}

	public String getLevelType() {
		return levelType;
	}

	public String getLevelIdentifier() {
		return levelIdentifier;
	}

	public BigDecimal getCapping() {
		return capping;
	}

	public BigDecimal getConsumed() {
		return consumed;
	}

	public BigDecimal getAvailable() {
		return available;
	}

	public BigDecimal getRequested() {
		return requested;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public void setLevelIdentifier(String levelIdentifier) {
		this.levelIdentifier = levelIdentifier;
	}

	public void setCapping(BigDecimal capping) {
		this.capping = capping;
	}

	public void setConsumed(BigDecimal consumed) {
		this.consumed = consumed;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

	public void setRequested(BigDecimal requested) {
		this.requested = requested;
	}

	public BigDecimal getRejected() {
		return rejected;
	}

	public void setRejected(BigDecimal rejected) {
		this.rejected = rejected;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

	public void setLevelDescription(String levelDescription) {
		this.levelDescription = levelDescription;
	}

	public Long getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getRedeemptionEligible() {
		return redeemptionEligible;
	}

	public void setRedeemptionEligible(String redeemptionEligible) {
		this.redeemptionEligible = redeemptionEligible;
	}
	
	
}
