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
@NamedQueries( { @NamedQuery(name = "OmsOrposTaxLinePickup.findAll",
                             query = "select o from OmsOrposTaxLinePickup o"),
                 @NamedQuery(name="OmsOrposTaxLinePickup.findColumns",
                             query="select o from OmsOrposTaxLinePickup o where   o.custOrderPicVoSeq=:custOrderPicVoSeq and o.lineItemNo=:lineItemNo")})
@Table(name = "OMS_ORPOS_TAX_LINE_PICKUP")
@IdClass(OmsOrposTaxLinePickupPK.class)
public class OmsOrposTaxLinePickup implements Serializable {
    @Column(name = "CANCELLED_TAX_AMOUNT", nullable = false)
    private BigDecimal cancelledTaxAmount;
    @Column(name = "COMPLETED_TAX_AMOUNT", nullable = false)
    private BigDecimal completedTaxAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_PIC_VO_SEQ", nullable = false)
    private BigDecimal custOrderPicVoSeq;
    @Column(name = "INCLUSIVE_TAX_FLAG", nullable = false, length = 1)
    private String inclusiveTaxFlag;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Column(name = "REPRICED_TAX_AMOUNT")
    private BigDecimal repricedTaxAmount;

    public OmsOrposTaxLinePickup() {
    }

    public OmsOrposTaxLinePickup(BigDecimal cancelledTaxAmount, BigDecimal completedTaxAmount, String currencyCode,
                                 BigDecimal custOrderPicVoSeq, String inclusiveTaxFlag, BigDecimal lineItemNo,
                                 BigDecimal lineNo, BigDecimal repricedTaxAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.completedTaxAmount = completedTaxAmount;
        this.currencyCode = currencyCode;
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.inclusiveTaxFlag = inclusiveTaxFlag;
        this.lineItemNo = lineItemNo;
        this.lineNo = lineNo;
        this.repricedTaxAmount = repricedTaxAmount;
    }

    public BigDecimal getCancelledTaxAmount() {
        return cancelledTaxAmount;
    }

    public void setCancelledTaxAmount(BigDecimal cancelledTaxAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
    }

    public BigDecimal getCompletedTaxAmount() {
        return completedTaxAmount;
    }

    public void setCompletedTaxAmount(BigDecimal completedTaxAmount) {
        this.completedTaxAmount = completedTaxAmount;
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

    public String getInclusiveTaxFlag() {
        return inclusiveTaxFlag;
    }

    public void setInclusiveTaxFlag(String inclusiveTaxFlag) {
        this.inclusiveTaxFlag = inclusiveTaxFlag;
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

    public BigDecimal getRepricedTaxAmount() {
        return repricedTaxAmount;
    }

    public void setRepricedTaxAmount(BigDecimal repricedTaxAmount) {
        this.repricedTaxAmount = repricedTaxAmount;
    }
}