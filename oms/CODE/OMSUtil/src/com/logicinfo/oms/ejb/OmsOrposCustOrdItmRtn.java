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
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrdItmRtn.findAll",
                             query = "select o from OmsOrposCustOrdItmRtn o"),
                 @NamedQuery(name = "OmsOrposCustOrdItmRtn.findColumns",
                             query = "select o from OmsOrposCustOrdItmRtn o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.custOrderRtnSeqNo=:custOrderRtnSeqNo"),
                 @NamedQuery(name="OmsOrposCustOrdItmRtn.findByOmsOrposCustOrdIdAndLineItemNo",
                             query="select o from OmsOrposCustOrdItmRtn o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.lineItemNo=:lineItemNo")
                 })
@Table(name = "OMS_ORPOS_CUST_ORD_ITM_RTN")
@IdClass(OmsOrposCustOrdItmRtnPK.class)
public class OmsOrposCustOrdItmRtn implements Serializable {
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_RTN_SEQ_NO", nullable = false)
    private BigDecimal custOrderRtnSeqNo;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "RETURNED_AMOUNT", nullable = false)
    private BigDecimal returnedAmount;
    @Column(name = "RETURNED_DISCOUNT_AMOUNT")
    private BigDecimal returnedDiscountAmount;
    @Column(name = "RETURNED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal returnedInclusiveTaxAmount;
    @Column(name = "RETURNED_QUANTITY", nullable = false)
    private BigDecimal returnedQuantity;
    @Column(name = "RETURNED_TAX_AMOUNT")
    private BigDecimal returnedTaxAmount;
    @Column(name = "UNIT_OF_MEASURE", length = 4)
    private String unitOfMeasure;

    public OmsOrposCustOrdItmRtn() {
    }

    public OmsOrposCustOrdItmRtn(String currencyCode, BigDecimal custOrderRtnSeqNo, BigDecimal lineItemNo,
                                 BigDecimal omsOrposCustOrderId, BigDecimal returnedAmount,
                                 BigDecimal returnedDiscountAmount, BigDecimal returnedInclusiveTaxAmount,
                                 BigDecimal returnedQuantity, BigDecimal returnedTaxAmount, String unitOfMeasure) {
        this.currencyCode = currencyCode;
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.lineItemNo = lineItemNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.returnedAmount = returnedAmount;
        this.returnedDiscountAmount = returnedDiscountAmount;
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
        this.returnedQuantity = returnedQuantity;
        this.returnedTaxAmount = returnedTaxAmount;
        this.unitOfMeasure = unitOfMeasure;
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

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(BigDecimal returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    public void setReturnedDiscountAmount(BigDecimal returnedDiscountAmount) {
        this.returnedDiscountAmount = returnedDiscountAmount;
    }

    public BigDecimal getReturnedInclusiveTaxAmount() {
        return returnedInclusiveTaxAmount;
    }

    public void setReturnedInclusiveTaxAmount(BigDecimal returnedInclusiveTaxAmount) {
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
    }

    public BigDecimal getReturnedQuantity() {
        return returnedQuantity;
    }

    public void setReturnedQuantity(BigDecimal returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }

    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    public void setReturnedTaxAmount(BigDecimal returnedTaxAmount) {
        this.returnedTaxAmount = returnedTaxAmount;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
