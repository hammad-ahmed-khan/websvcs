
package com.extra.oms.custOrder.model;

import java.math.BigDecimal;


public class CustomerOrderResponseItemFulfillment {

   
    protected String item;
    protected BigDecimal orderQtySuom;
    protected BigDecimal fulfillQtySuom;
    protected Long tsfNo;
    protected Long poNo;
    protected String sourceLocType;
    protected Long sourceLoc;
    protected Long fulfillLoc;
    protected String fulfillLocType;
    protected Long rmsResvQty;
    protected Long rmsResvLoc;
    protected String rmsResvLocType;
    protected Long backorderQty;
    protected Long fulfillOrderNo;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public BigDecimal getOrderQtySuom() {
		return orderQtySuom;
	}
	public void setOrderQtySuom(BigDecimal orderQtySuom) {
		this.orderQtySuom = orderQtySuom;
	}
	public BigDecimal getFulfillQtySuom() {
		return fulfillQtySuom;
	}
	public void setFulfillQtySuom(BigDecimal fulfillQtySuom) {
		this.fulfillQtySuom = fulfillQtySuom;
	}
	public Long getTsfNo() {
		return tsfNo;
	}
	public void setTsfNo(Long tsfNo) {
		this.tsfNo = tsfNo;
	}
	public Long getPoNo() {
		return poNo;
	}
	public void setPoNo(Long poNo) {
		this.poNo = poNo;
	}
	public String getSourceLocType() {
		return sourceLocType;
	}
	public void setSourceLocType(String sourceLocType) {
		this.sourceLocType = sourceLocType;
	}
	public Long getSourceLoc() {
		return sourceLoc;
	}
	public void setSourceLoc(Long sourceLoc) {
		this.sourceLoc = sourceLoc;
	}
	public Long getFulfillLoc() {
		return fulfillLoc;
	}
	public void setFulfillLoc(Long fulfillLoc) {
		this.fulfillLoc = fulfillLoc;
	}
	public String getFulfillLocType() {
		return fulfillLocType;
	}
	public void setFulfillLocType(String fulfillLocType) {
		this.fulfillLocType = fulfillLocType;
	}
	public Long getRmsResvQty() {
		return rmsResvQty;
	}
	public void setRmsResvQty(Long rmsResvQty) {
		this.rmsResvQty = rmsResvQty;
	}
	public Long getRmsResvLoc() {
		return rmsResvLoc;
	}
	public void setRmsResvLoc(Long rmsResvLoc) {
		this.rmsResvLoc = rmsResvLoc;
	}
	public String getRmsResvLocType() {
		return rmsResvLocType;
	}
	public void setRmsResvLocType(String rmsResvLocType) {
		this.rmsResvLocType = rmsResvLocType;
	}
	public Long getBackorderQty() {
		return backorderQty;
	}
	public void setBackorderQty(Long backorderQty) {
		this.backorderQty = backorderQty;
	}
	public Long getFulfillOrderNo() {
		return fulfillOrderNo;
	}
	public void setFulfillOrderNo(Long fulfillOrderNo) {
		this.fulfillOrderNo = fulfillOrderNo;
	}

   

}
