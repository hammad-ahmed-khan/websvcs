package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrderRtn.findAll",
                             query = "select o from OmsOrposCustOrderRtn o"),
                 @NamedQuery(name = "OmsOrposCustOrderRtn.findCustOrdRtnSeq",
                             query = "select o.custOrderRtnSeqNo  from OmsOrposCustOrderRtn o where o.customerOrderId=:customerOrderId"),
                 @NamedQuery(name = "OmsOrposCustOrderRtn.max",
                             query = "select max(o.custOrderRtnSeqNo) from OmsOrposCustOrderRtn o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposCustOrderRtn.findAllColumns",
                             query = "select o from OmsOrposCustOrderRtn o where o.customerOrderId=:customerOrderId")
                 })
@Table(name = "OMS_ORPOS_CUST_ORDER_RTN")
@IdClass(OmsOrposCustOrderRtnPK.class)
public class OmsOrposCustOrderRtn implements Serializable
{
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_RTN_SEQ_NO", nullable = false)
    @SequenceGenerator( name = "omsOrposCustOrdRtnSeq", sequenceName = "CUST_ORDER_RTN_SEQNO", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposCustOrdRtnSeq" )
    private BigDecimal custOrderRtnSeqNo;
    @Column(name = "CUSTOMER_ORDER_ID", nullable = false, length = 48)
           
    private String customerOrderId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "RETURNED_AMOUNT")
    private BigDecimal returnedAmount;
    @Column(name = "RETURNED_DISCOUNT_AMOUNT")
    private BigDecimal returnedDiscountAmount;
    @Column(name = "RETURNED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal returnedInclusiveTaxAmount;
    @Column(name = "RETURNED_TAX_AMOUNT")
    private BigDecimal returnedTaxAmount;
    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
    private Timestamp updateTimestamp;

    public OmsOrposCustOrderRtn() {
    }

    public OmsOrposCustOrderRtn(String currencyCode, BigDecimal custOrderRtnSeqNo, String customerOrderId,
                                BigDecimal omsOrposCustOrderId, BigDecimal returnedAmount,
                                BigDecimal returnedDiscountAmount, BigDecimal returnedInclusiveTaxAmount,
                                BigDecimal returnedTaxAmount, Timestamp updateTimestamp) {
        this.currencyCode = currencyCode;
        this.custOrderRtnSeqNo = custOrderRtnSeqNo;
        this.customerOrderId = customerOrderId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.returnedAmount = returnedAmount;
        this.returnedDiscountAmount = returnedDiscountAmount;
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
        this.returnedTaxAmount = returnedTaxAmount;
        this.updateTimestamp = updateTimestamp;
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

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
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

    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    public void setReturnedTaxAmount(BigDecimal returnedTaxAmount) {
        this.returnedTaxAmount = returnedTaxAmount;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
