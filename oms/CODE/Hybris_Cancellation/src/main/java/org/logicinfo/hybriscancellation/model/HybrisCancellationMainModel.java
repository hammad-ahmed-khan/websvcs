package org.logicinfo.hybriscancellation.model;

import java.util.List;

public class HybrisCancellationMainModel {

	private String orderCode;
	private String initiatedFromStoreId;
	private String cancellationId;
	private String cancellationDate;
	private String country;
	private String currency;
	private String isRefundOnly;
	private String transactionType;
	private String marketPlaceOrder;
	private long headSeqId;
	private long tenderSeqId;
	private long OmsCancelId;
	private String reason;
	private Integer reasonCode;

	private List<Long> tenderSeqIdList;
	private List<HybrisCancellationLineModel> lines;
	private List<HybrisCancellationRefundModel> refunds;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public long getOmsCancelId() {
		return OmsCancelId;
	}

	public void setOmsCancelId(long omsCancelId) {
		OmsCancelId = omsCancelId;
	}

	public long getTenderSeqId() {
		return tenderSeqId;
	}

	public void setTenderSeqId(long tenderSeqId) {
		this.tenderSeqId = tenderSeqId;
	}

	public long getHeadSeqId() {
		return headSeqId;
	}

	public void setHeadSeqId(long headSeqId) {
		this.headSeqId = headSeqId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getInitiatedFromStoreId() {
		return initiatedFromStoreId;
	}

	public void setInitiatedFromStoreId(String initiatedFromStoreId) {
		this.initiatedFromStoreId = initiatedFromStoreId;
	}

	public String getCancellationId() {
		return cancellationId;
	}

	public void setCancellationId(String cancellationId) {
		this.cancellationId = cancellationId;
	}

	public String getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(String cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsRefundOnly() {
		return isRefundOnly;
	}

	public void setIsRefundOnly(String isRefundOnly) {
		this.isRefundOnly = isRefundOnly;
	}

	public List<HybrisCancellationLineModel> getLines() {
		return lines;
	}

	public void setLines(List<HybrisCancellationLineModel> lines) {
		this.lines = lines;
	}

	public List<HybrisCancellationRefundModel> getRefunds() {
		return refunds;
	}

	public void setRefunds(List<HybrisCancellationRefundModel> refunds) {
		this.refunds = refunds;
	}

	public HybrisCancellationMainModel() {
		System.out.println("Default con");
	}

	public List<Long> getTenderSeqIdList() {
		return tenderSeqIdList;
	}

	public void setTenderSeqIdList(List<Long> tenderSeqIdList) {
		this.tenderSeqIdList = tenderSeqIdList;
	}

	public String getMarketPlaceOrder() {
		return marketPlaceOrder;
	}

	public void setMarketPlaceOrder(String marketPlaceOrder) {
		this.marketPlaceOrder = marketPlaceOrder;
	}

	

}