package org.logicinfo.hybriscancellation.model;

public class HybrisCancellationRefundModel {
	private String paymentType;
	private String type;
	private String subtype;
	private String action;
	private String brandCode;
	private double refundAmount;
	private double decimalAdjustment;
	private String refundable;
	private String sadadBankId;
	private String sadadSPTN;
	private String payfortFortId;
	private String payfortMerchantReference;
	private String tasheelWalletCivilId;
	private String tasheelwalletRefNumber;
	private String tasheelCardNo;

	private String tenderRefundId;

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getDecimalAdjustment() {
		return decimalAdjustment;
	}

	public void setDecimalAdjustment(double decimalAdjustment) {
		this.decimalAdjustment = decimalAdjustment;
	}

	public String getRefundable() {
		return refundable;
	}

	public void setRefundable(String refundable) {
		this.refundable = refundable;
	}

	public String getSadadBankId() {
		return sadadBankId;
	}

	public void setSadadBankId(String sadadBankId) {
		this.sadadBankId = sadadBankId;
	}

	public String getSadadSPTN() {
		return sadadSPTN;
	}

	public void setSadadSPTN(String sadadSPTN) {
		this.sadadSPTN = sadadSPTN;
	}

	public String getPayfortFortId() {
		return payfortFortId;
	}

	public void setPayfortFortId(String payfortFortId) {
		this.payfortFortId = payfortFortId;
	}

	public String getPayfortMerchantReference() {
		return payfortMerchantReference;
	}

	public void setPayfortMerchantReference(String payfortMerchantReference) {
		this.payfortMerchantReference = payfortMerchantReference;
	}

	public String getTasheelWalletCivilId() {
		return tasheelWalletCivilId;
	}

	public void setTasheelWalletCivilId(String tasheelWalletCivilId) {
		this.tasheelWalletCivilId = tasheelWalletCivilId;
	}

	public String getTasheelwalletRefNumber() {
		return tasheelwalletRefNumber;
	}

	public void setTasheelwalletRefNumber(String tasheelwalletRefNumber) {
		this.tasheelwalletRefNumber = tasheelwalletRefNumber;
	}

	public String getTasheelCardNo() {
		return tasheelCardNo;
	}

	public void setTasheelCardNo(String tasheelCardNo) {
		this.tasheelCardNo = tasheelCardNo;
	}

	public HybrisCancellationRefundModel() {

	}

	public String getTenderRefundId() {
		return tenderRefundId;
	}

	public void setTenderRefundId(String tenderRefundId) {
		this.tenderRefundId = tenderRefundId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

}
