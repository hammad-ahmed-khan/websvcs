/**
 * 
 */
package com.extra.oms.core.bean;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author aibrahim
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemFulfilment {

	private String item;

	private String lineNo;

	private String itemDesc;

	private Long fulfilOrdNo;

	private Long srcLocation;

	private Long fulfilLocation;

	private String srcLocationType;

	private String fulfilLocationType;

	private BigDecimal requestQty;

	private BigDecimal cancelledQty;

	private BigDecimal confirmQty;

	private BigDecimal deliveryQty;

	private BigDecimal itemCancelledQty;

	private String tsfNo;

	private Long srcLocCHId;

	private Integer fulfilLocCHId;

	private boolean valid;

	private String remarks;

	private OrderInfo orderInfo;

	private BigDecimal itemOrderedQty;

	private ItemFulfilment exFulfilment;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public Long getSrcLocation() {
		return srcLocation;
	}

	public void setSrcLocation(Long srcLocation) {
		this.srcLocation = srcLocation;
	}

	public Long getFulfilLocation() {
		return fulfilLocation;
	}

	public void setFulfilLocation(Long fulfilLocation) {
		this.fulfilLocation = fulfilLocation;
	}

	public String getSrcLocationType() {
		return srcLocationType;
	}

	public void setSrcLocationType(String srcLocationType) {
		this.srcLocationType = srcLocationType;
	}

	public String getFulfilLocationType() {
		return fulfilLocationType;
	}

	public void setFulfilLocationType(String fulfilLocationType) {
		this.fulfilLocationType = fulfilLocationType;
	}

	public BigDecimal getRequestQty() {
		return requestQty;
	}

	public void setRequestQty(BigDecimal requestQty) {
		this.requestQty = requestQty;
	}

	public BigDecimal getCancelledQty() {
		return cancelledQty;
	}

	public void setCancelledQty(BigDecimal cancelledQty) {
		this.cancelledQty = cancelledQty;
	}

	public Long getFulfilOrdNo() {
		return fulfilOrdNo;
	}

	public void setFulfilOrdNo(Long fulfilOrdNo) {
		this.fulfilOrdNo = fulfilOrdNo;
	}

	public void setConfirmQty(BigDecimal confirmQty) {
		this.confirmQty = confirmQty;
	}

	public BigDecimal getConfirmQty() {
		return confirmQty;
	}

	public void setDeliveryQty(BigDecimal deliveryQty) {
		this.deliveryQty = deliveryQty;
	}

	public BigDecimal getDeliveryQty() {
		return deliveryQty;
	}

	public String getTsfNo() {
		return tsfNo;
	}

	public void setTsfNo(String tsfNo) {
		this.tsfNo = tsfNo;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public BigDecimal getItemCancelledQty() {
		return itemCancelledQty;
	}

	public void setItemCancelledQty(BigDecimal itemCancelledQty) {
		this.itemCancelledQty = itemCancelledQty;
	}

	public Long getSrcLocCHId() {
		return srcLocCHId;
	}

	public void setSrcLocCHId(Long srcLocCHId) {
		this.srcLocCHId = srcLocCHId;
	}

	public BigDecimal getItemOrderedQty() {
		return itemOrderedQty;
	}

	public void setItemOrderedQty(BigDecimal itemOrderedQty) {
		this.itemOrderedQty = itemOrderedQty;
	}

	public Integer getFulfilLocCHId() {
		return fulfilLocCHId;
	}

	public void setFulfilLocCHId(Integer fulfilLocCHId) {
		this.fulfilLocCHId = fulfilLocCHId;
	}

	public ItemFulfilment getExFulfilment() {
		return exFulfilment;
	}

	public void setExFulfilment(ItemFulfilment exFulfilment) {
		this.exFulfilment = exFulfilment;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
