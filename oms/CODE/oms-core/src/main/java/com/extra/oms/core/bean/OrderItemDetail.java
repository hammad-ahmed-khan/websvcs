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
public class OrderItemDetail {

	private String item;

	private Long lineNo;

	private String classification;

	private BigDecimal orderedQty;

	private BigDecimal cancelledQty;

	private BigDecimal deliveredQty;

	private BigDecimal returnedQty;

	private Long fulfilOrdNo;

	private Long srcLocation;

	private Long fulfilLocation;

	private String srcLocationName;

	private String fulfilLocationName;

	private String srcLocationType;

	private String fulfilLocationType;

	private String awbNo;

	private String courierName;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Long getLineNo() {
		return lineNo;
	}

	public void setLineNo(Long lineNo) {
		this.lineNo = lineNo;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public BigDecimal getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(BigDecimal orderedQty) {
		this.orderedQty = orderedQty;
	}

	public BigDecimal getCancelledQty() {
		return cancelledQty;
	}

	public void setCancelledQty(BigDecimal cancelledQty) {
		this.cancelledQty = cancelledQty;
	}

	public BigDecimal getDeliveredQty() {
		return deliveredQty;
	}

	public void setDeliveredQty(BigDecimal deliveredQty) {
		this.deliveredQty = deliveredQty;
	}

	public BigDecimal getReturnedQty() {
		return returnedQty;
	}

	public void setReturnedQty(BigDecimal returnedQty) {
		this.returnedQty = returnedQty;
	}

	public String getAwbNo() {
		return awbNo;
	}

	public void setAwbNo(String awbNo) {
		this.awbNo = awbNo;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
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

	public String getSrcLocationName() {
		return srcLocationName;
	}

	public void setSrcLocationName(String srcLocationName) {
		this.srcLocationName = srcLocationName;
	}

	public String getFulfilLocationName() {
		return fulfilLocationName;
	}

	public void setFulfilLocationName(String fulfilLocationName) {
		this.fulfilLocationName = fulfilLocationName;
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

	public Long getFulfilOrdNo() {
		return fulfilOrdNo;
	}

	public void setFulfilOrdNo(Long fulfilOrdNo) {
		this.fulfilOrdNo = fulfilOrdNo;
	}
}
