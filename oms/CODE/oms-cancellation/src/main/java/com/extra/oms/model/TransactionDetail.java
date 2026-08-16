package com.extra.oms.model;

import java.math.BigDecimal;

/**
 * aibrahim 2024
 */
public class TransactionDetail {
	
	private Long memberId;

	private Long lineNumber;

	private String item;

	private int qty;

	private String unitRetailPrice;

	private String unitTotalRetailDiscount;

	private String unitTotalJoodDiscount;

	private String unitSellingPrice;
	
	private String source;
	
	private BigDecimal unitRewardsCB;

	private BigDecimal unitRedeemedCB;

	private String redeemptionEligible;
	
	private BigDecimal joodProgram;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getLineNumber() {
		return lineNumber;
	}

	public String getItem() {
		return item;
	}

	public int getQty() {
		return qty;
	}

	public String getUnitRetailPrice() {
		return unitRetailPrice;
	}

	public String getUnitTotalRetailDiscount() {
		return unitTotalRetailDiscount;
	}

	public String getUnitTotalJoodDiscount() {
		return unitTotalJoodDiscount;
	}

	public String getUnitSellingPrice() {
		return unitSellingPrice;
	}

	public void setLineNumber(Long lineNumber) {
		this.lineNumber = lineNumber;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public void setUnitRetailPrice(String unitRetailPrice) {
		this.unitRetailPrice = unitRetailPrice;
	}

	public void setUnitTotalRetailDiscount(String unitTotalRetailDiscount) {
		this.unitTotalRetailDiscount = unitTotalRetailDiscount;
	}

	public void setUnitTotalJoodDiscount(String unitTotalJoodDiscount) {
		this.unitTotalJoodDiscount = unitTotalJoodDiscount;
	}

	public void setUnitSellingPrice(String unitSellingPrice) {
		this.unitSellingPrice = unitSellingPrice;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public BigDecimal getUnitRewardsCB() {
		return unitRewardsCB;
	}

	public void setUnitRewardsCB(BigDecimal unitRewardsCB) {
		this.unitRewardsCB = unitRewardsCB;
	}

	public BigDecimal getUnitRedeemedCB() {
		return unitRedeemedCB;
	}

	public void setUnitRedeemedCB(BigDecimal unitRedeemedCB) {
		this.unitRedeemedCB = unitRedeemedCB;
	}

	public String getRedeemptionEligible() {
		return redeemptionEligible;
	}

	public void setRedeemptionEligible(String redeemptionEligible) {
		this.redeemptionEligible = redeemptionEligible;
	}

	public BigDecimal getJoodProgram() {
		return joodProgram;
	}

	public void setJoodProgram(BigDecimal joodProgram) {
		this.joodProgram = joodProgram;
	}
	
	
	
}
