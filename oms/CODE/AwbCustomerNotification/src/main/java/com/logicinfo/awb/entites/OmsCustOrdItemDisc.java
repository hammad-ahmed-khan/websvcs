
package com.logicinfo.awb.entites;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;




public class OmsCustOrdItemDisc implements Serializable {
   
    private Date createDatetime;
    private BigDecimal discLineNo;
    private BigDecimal discRefNo;
    private String discountType;
    private String employeeId;
    private BigDecimal lineNo;
    private BigDecimal omsCustOrdNo;
    private BigDecimal promoCompId;
    private String rmsPromoType;
    private String simplePromoInd;
    private BigDecimal unitDiscountAmount;

    public OmsCustOrdItemDisc() {
    }

    public OmsCustOrdItemDisc(Date createDatetime, BigDecimal discLineNo, BigDecimal discRefNo, String discountType,
                              String employeeId, BigDecimal lineNo, BigDecimal omsCustOrdNo, BigDecimal promoCompId,
                              String rmsPromoType, String simplePromoInd, BigDecimal unitDiscountAmount) {
        this.createDatetime = createDatetime;
        this.discLineNo = discLineNo;
        this.discRefNo = discRefNo;
        this.discountType = discountType;
        this.employeeId = employeeId;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.promoCompId = promoCompId;
        this.rmsPromoType = rmsPromoType;
        this.simplePromoInd = simplePromoInd;
        this.unitDiscountAmount = unitDiscountAmount;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getDiscLineNo() {
        return discLineNo;
    }

    public void setDiscLineNo(BigDecimal discLineNo) {
        this.discLineNo = discLineNo;
    }

    public BigDecimal getDiscRefNo() {
        return discRefNo;
    }

    public void setDiscRefNo(BigDecimal discRefNo) {
        this.discRefNo = discRefNo;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getPromoCompId() {
        return promoCompId;
    }

    public void setPromoCompId(BigDecimal promoCompId) {
        this.promoCompId = promoCompId;
    }

    public String getRmsPromoType() {
        return rmsPromoType;
    }

    public void setRmsPromoType(String rmsPromoType) {
        this.rmsPromoType = rmsPromoType;
    }

    public String getSimplePromoInd() {
        return simplePromoInd;
    }

    public void setSimplePromoInd(String simplePromoInd) {
        this.simplePromoInd = simplePromoInd;
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = unitDiscountAmount;
    }
}
