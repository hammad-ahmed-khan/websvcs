package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposDiscntLinePickup.findAll",
                             query = "select o from OmsOrposDiscntLinePickup o"),
                 @NamedQuery(name="OmsOrposDiscntLinePickup.findColumns",
                             query="select o from OmsOrposDiscntLinePickup o where  o.custOrderPicVoSeq=:custOrderPicVoSeq and o.lineItemNo=:lineItemNo")})
@Table(name = "OMS_ORPOS_DISCNT_LINE_PICKUP")
//@IdClass(OmsOrposDiscntLinePickupPK.class)
public class OmsOrposDiscntLinePickup implements Serializable {
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal cancelledDiscountAmount;
    @Column(name = "COMPLETED_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal completedDiscountAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_PIC_VO_SEQ", nullable = false)
    private BigDecimal custOrderPicVoSeq;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Column(name = "REPRICED_DISCOUNT_AMOUNT")
    private BigDecimal repricedDiscountAmount;

    public OmsOrposDiscntLinePickup() {
    }

    public OmsOrposDiscntLinePickup(BigDecimal cancelledDiscountAmount, BigDecimal completedDiscountAmount,
                                    String currencyCode, BigDecimal custOrderPicVoSeq, BigDecimal lineItemNo,
                                    BigDecimal lineNo, BigDecimal repricedDiscountAmount) {
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.completedDiscountAmount = completedDiscountAmount;
        this.currencyCode = currencyCode;
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.lineItemNo = lineItemNo;
        this.lineNo = lineNo;
        this.repricedDiscountAmount = repricedDiscountAmount;
    }

    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    public void setCancelledDiscountAmount(BigDecimal cancelledDiscountAmount) {
        this.cancelledDiscountAmount = cancelledDiscountAmount;
    }

    public BigDecimal getCompletedDiscountAmount() {
        return completedDiscountAmount;
    }

    public void setCompletedDiscountAmount(BigDecimal completedDiscountAmount) {
        this.completedDiscountAmount = completedDiscountAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getCustOrderPicVoSeq() {
        return custOrderPicVoSeq;
    }

    public void setCustOrderPicVoSeq(BigDecimal custOrderPicVoSeq) {
        this.custOrderPicVoSeq = custOrderPicVoSeq;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getRepricedDiscountAmount() {
        return repricedDiscountAmount;
    }

    public void setRepricedDiscountAmount(BigDecimal repricedDiscountAmount) {
        this.repricedDiscountAmount = repricedDiscountAmount;
    }
}
