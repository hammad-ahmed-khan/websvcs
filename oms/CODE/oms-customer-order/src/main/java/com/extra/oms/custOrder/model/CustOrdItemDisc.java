package com.extra.oms.custOrder.model;

import java.math.BigDecimal;


public class CustOrdItemDisc {

    protected long discLineNo;
    protected String rmsPromoType;
    protected Long discRefNo;
    protected String discountType;
    protected BigDecimal unitDiscountAmount;
    protected Long promoCompId;
    protected String employeeId;
    protected String simplePromoInd;
    
	public long getDiscLineNo() {
		return discLineNo;
	}
	public void setDiscLineNo(long discLineNo) {
		this.discLineNo = discLineNo;
	}
	public String getRmsPromoType() {
		return rmsPromoType;
	}
	public void setRmsPromoType(String rmsPromoType) {
		this.rmsPromoType = rmsPromoType;
	}
	public Long getDiscRefNo() {
		return discRefNo;
	}
	public void setDiscRefNo(Long discRefNo) {
		this.discRefNo = discRefNo;
	}
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public BigDecimal getUnitDiscountAmount() {
		return unitDiscountAmount;
	}
	public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
		this.unitDiscountAmount = unitDiscountAmount;
	}
	public Long getPromoCompId() {
		return promoCompId;
	}
	public void setPromoCompId(Long promoCompId) {
		this.promoCompId = promoCompId;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getSimplePromoInd() {
		return simplePromoInd;
	}
	public void setSimplePromoInd(String simplePromoInd) {
		this.simplePromoInd = simplePromoInd;
	}

   
}
