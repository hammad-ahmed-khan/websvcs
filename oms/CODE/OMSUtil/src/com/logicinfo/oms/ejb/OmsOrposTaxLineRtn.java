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
@NamedQueries( { @NamedQuery(name = "OmsOrposTaxLineRtn.findAll", 
                             query = "select o from OmsOrposTaxLineRtn o"),
                  @NamedQuery(name = "OmsOrposTaxLineRtn.findAllColumns",
                             query = "select o from OmsOrposTaxLineRtn o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderRtnSeqNo=:custOrderRtnSeqNo and o.lineItemNo=:lineItemNo")
                 })
@Table(name = "OMS_ORPOS_TAX_LINE_RTN")
@IdClass(OmsOrposTaxLineRtnPK.class)
public class OmsOrposTaxLineRtn implements Serializable
{
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_RTN_SEQ_NO", nullable = false)
    private BigDecimal custOrderRtnSeqNo;
    @Column(name = "INCLUSIVE_TAX_FLAG", nullable = false, length = 1)
    private String inclusiveTaxFlag;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "RETURNED_TAX_AMOUNT", nullable = false)
    private BigDecimal returnedTaxAmount;

    public OmsOrposTaxLineRtn() {
    }

    public OmsOrposTaxLineRtn(String currencyCode, BigDecimal custOrderRtnSeqNo, String inclusiveTaxFlag,
                              BigDecimal lineItemNo, BigDecimal lineNo, BigDecimal omsOrposCustOrderId,
                              BigDecimal returnedTaxAmount) {
        this.currencyCode = currencyCode;
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.inclusiveTaxFlag = inclusiveTaxFlag;
        this.lineItemNo = lineItemNo;
        this.lineNo = lineNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.returnedTaxAmount = returnedTaxAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getCustOrderRtnSeqNo() {
        return custOrderRtnSeqNo;
    }

    public void setCustOrderRtnSeqNo(BigDecimal custOrderRtnSeqNo) {
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
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

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    public void setReturnedTaxAmount(BigDecimal returnedTaxAmount) {
        this.returnedTaxAmount = returnedTaxAmount;
    }
}
