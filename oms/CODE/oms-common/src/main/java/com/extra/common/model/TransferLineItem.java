package com.extra.common.model;

/**
 * @author aibrahim
 *
 */
public class TransferLineItem {

	private Long id;

	private Long lineId;

	private String fulfilOrdNo;

	private Long transferNo;

	private String item;

	public Long getId() {
		return id;
	}

	public Long getLineId() {
		return lineId;
	}

	public String getFulfilOrdNo() {
		return fulfilOrdNo;
	}

	public Long getTransferNo() {
		return transferNo;
	}

	public String getItem() {
		return item;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public void setFulfilOrdNo(String fulfilOrdNo) {
		this.fulfilOrdNo = fulfilOrdNo;
	}

	public void setTransferNo(Long transferNo) {
		this.transferNo = transferNo;
	}

	public void setItem(String item) {
		this.item = item;
	}
}
