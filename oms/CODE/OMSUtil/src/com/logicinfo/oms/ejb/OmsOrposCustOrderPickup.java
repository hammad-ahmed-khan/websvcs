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
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrderPickup.findAll",
                             query = "select o from OmsOrposCustOrderPickup o"),
                 @NamedQuery(name = "OmsOrposCustOrderPickup.findCustOrderPicVoSeq",
                             query = "select o.custOrderPicVoSeq from OmsOrposCustOrderPickup o where o.customerOrderId=:customerOrderId"),
                 @NamedQuery(name = "OmsOrposCustOrderPickup.findAllColumns",
                             query = "select o from OmsOrposCustOrderPickup o where o.custOrderPicVoSeq=:custOrderPicVoSeq"),
                 @NamedQuery(name = "OmsOrposCustOrderPickup.max",
                             query = "select max(o.custOrderPicVoSeq) from OmsOrposCustOrderPickup o where o.customerOrderId=:customerOrderId")})
@Table(name = "OMS_ORPOS_CUST_ORDER_PICKUP")
@IdClass(OmsOrposCustOrderPickupPK.class)
public class OmsOrposCustOrderPickup implements Serializable {
    @Column(name = "CANCELLED_AMOUNT")
    private BigDecimal cancelledAmount;
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT")
    private BigDecimal cancelledDiscountAmount;
    @Column(name = "CANCELLED_INC_TAX_AMOUNT")
    private BigDecimal cancelledIncTaxAmount;
    @Column(name = "CANCELLED_TAX_AMOUNT")
    private BigDecimal cancelledTaxAmount;
    @Column(name = "COMPLETED_AMOUNT")
    private BigDecimal completedAmount;
    @Column(name = "COMPLETED_DISCOUNT_AMOUNT")
    private BigDecimal completedDiscountAmount;
    @Column(name = "COMPLETED_INC_TAX_AMOUNT")
    private BigDecimal completedIncTaxAmount;
    @Column(name = "COMPLETED_NEW_AMOUNT")
    private BigDecimal completedNewAmount;
    @Column(name = "COMPLETED_NEW_DISCOUNT_AMOUNT")
    private BigDecimal completedNewDiscountAmount;
    @Column(name = "COMPLETED_NEW_INC_TAX_AMOUNT")
    private BigDecimal completedNewIncTaxAmount;
    @Column(name = "COMPLETED_NEW_TAX_AMOUNT")
    private BigDecimal completedNewTaxAmount;
    @Column(name = "COMPLETED_TAX_AMOUNT")
    private BigDecimal completedTaxAmount;
    @Column(name = "CURRENCY_CODE", length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_PIC_VO_SEQ", nullable = false, unique = true)
    @SequenceGenerator( name = "omsOrposCustOrdPicVoSeq", sequenceName = "OMS_OR_CU_ORD_PIC_VO_SEQ", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposCustOrdPicVoSeq" )   
    private BigDecimal custOrderPicVoSeq;
    @Id
    @Column(name = "CUSTOMER_ORDER_ID", nullable = false, length = 48)
    private String customerOrderId;
    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;
    @Column(name = "REFUND_AMOUNT_OFFSET_BY_SALE")
    private BigDecimal refundAmountOffsetBySale;
    @Column(name = "REPRICED_AMOUNT")
    private BigDecimal repricedAmount;
    @Column(name = "REPRICED_DISCOUNT_AMOUNT")
    private BigDecimal repricedDiscountAmount;
    @Column(name = "REPRICED_INC_TAX_AMOUNT")
    private BigDecimal repricedIncTaxAmount;
    @Column(name = "REPRICED_TAX_AMOUNT")
    private BigDecimal repricedTaxAmount;
    @Column(name = "ROUNDING_ADJUSTMENT")
    private BigDecimal roundingAdjustment;
    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
    private Timestamp updateTimestamp;

    public OmsOrposCustOrderPickup() {
    }

    public OmsOrposCustOrderPickup(BigDecimal cancelledAmount, BigDecimal cancelledDiscountAmount,
                                   BigDecimal cancelledIncTaxAmount, BigDecimal cancelledTaxAmount,
                                   BigDecimal completedAmount, BigDecimal completedDiscountAmount,
                                   BigDecimal completedIncTaxAmount, BigDecimal completedNewAmount,
                                   BigDecimal completedNewDiscountAmount, BigDecimal completedNewIncTaxAmount,
                                   BigDecimal completedNewTaxAmount, BigDecimal completedTaxAmount,
                                   String currencyCode, BigDecimal custOrderPicVoSeq, String customerOrderId,
                                   BigDecimal paidAmount, BigDecimal refundAmountOffsetBySale,
                                   BigDecimal repricedAmount, BigDecimal repricedDiscountAmount,
                                   BigDecimal repricedIncTaxAmount, BigDecimal repricedTaxAmount,
                                   BigDecimal roundingAdjustment, Timestamp updateTimestamp) {
        this.cancelledAmount = cancelledAmount;
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.cancelledIncTaxAmount = cancelledIncTaxAmount;
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.completedAmount = completedAmount;
        this.completedDiscountAmount = completedDiscountAmount;
        this.completedIncTaxAmount = completedIncTaxAmount;
        this.completedNewAmount = completedNewAmount;
        this.completedNewDiscountAmount = completedNewDiscountAmount;
        this.completedNewIncTaxAmount = completedNewIncTaxAmount;
        this.completedNewTaxAmount = completedNewTaxAmount;
        this.completedTaxAmount = completedTaxAmount;
        this.currencyCode = currencyCode;
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.customerOrderId = customerOrderId;
        this.paidAmount = paidAmount;
        this.refundAmountOffsetBySale = refundAmountOffsetBySale;
        this.repricedAmount = repricedAmount;
        this.repricedDiscountAmount = repricedDiscountAmount;
        this.repricedIncTaxAmount = repricedIncTaxAmount;
        this.repricedTaxAmount = repricedTaxAmount;
        this.roundingAdjustment = roundingAdjustment;
        this.updateTimestamp = updateTimestamp;
    }

    public BigDecimal getCancelledAmount() {
        return cancelledAmount;
    }

    public void setCancelledAmount(BigDecimal cancelledAmount) {
        this.cancelledAmount = cancelledAmount;
    }

    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    public void setCancelledDiscountAmount(BigDecimal cancelledDiscountAmount) {
        this.cancelledDiscountAmount = cancelledDiscountAmount;
    }

    public BigDecimal getCancelledIncTaxAmount() {
        return cancelledIncTaxAmount;
    }

    public void setCancelledIncTaxAmount(BigDecimal cancelledIncTaxAmount) {
        this.cancelledIncTaxAmount = cancelledIncTaxAmount;
    }

    public BigDecimal getCancelledTaxAmount() {
        return cancelledTaxAmount;
    }

    public void setCancelledTaxAmount(BigDecimal cancelledTaxAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
    }

    public BigDecimal getCompletedAmount() {
        return completedAmount;
    }

    public void setCompletedAmount(BigDecimal completedAmount) {
        this.completedAmount = completedAmount;
    }

    public BigDecimal getCompletedDiscountAmount() {
        return completedDiscountAmount;
    }

    public void setCompletedDiscountAmount(BigDecimal completedDiscountAmount) {
        this.completedDiscountAmount = completedDiscountAmount;
    }

    public BigDecimal getCompletedIncTaxAmount() {
        return completedIncTaxAmount;
    }

    public void setCompletedIncTaxAmount(BigDecimal completedIncTaxAmount) {
        this.completedIncTaxAmount = completedIncTaxAmount;
    }

    public BigDecimal getCompletedNewAmount() {
        return completedNewAmount;
    }

    public void setCompletedNewAmount(BigDecimal completedNewAmount) {
        this.completedNewAmount = completedNewAmount;
    }

    public BigDecimal getCompletedNewDiscountAmount() {
        return completedNewDiscountAmount;
    }

    public void setCompletedNewDiscountAmount(BigDecimal completedNewDiscountAmount) {
        this.completedNewDiscountAmount = completedNewDiscountAmount;
    }

    public BigDecimal getCompletedNewIncTaxAmount() {
        return completedNewIncTaxAmount;
    }

    public void setCompletedNewIncTaxAmount(BigDecimal completedNewIncTaxAmount) {
        this.completedNewIncTaxAmount = completedNewIncTaxAmount;
    }

    public BigDecimal getCompletedNewTaxAmount() {
        return completedNewTaxAmount;
    }

    public void setCompletedNewTaxAmount(BigDecimal completedNewTaxAmount) {
        this.completedNewTaxAmount = completedNewTaxAmount;
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

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getRefundAmountOffsetBySale() {
        return refundAmountOffsetBySale;
    }

    public void setRefundAmountOffsetBySale(BigDecimal refundAmountOffsetBySale) {
        this.refundAmountOffsetBySale = refundAmountOffsetBySale;
    }

    public BigDecimal getRepricedAmount() {
        return repricedAmount;
    }

    public void setRepricedAmount(BigDecimal repricedAmount) {
        this.repricedAmount = repricedAmount;
    }

    public BigDecimal getRepricedDiscountAmount() {
        return repricedDiscountAmount;
    }

    public void setRepricedDiscountAmount(BigDecimal repricedDiscountAmount) {
        this.repricedDiscountAmount = repricedDiscountAmount;
    }

    public BigDecimal getRepricedIncTaxAmount() {
        return repricedIncTaxAmount;
    }

    public void setRepricedIncTaxAmount(BigDecimal repricedIncTaxAmount) {
        this.repricedIncTaxAmount = repricedIncTaxAmount;
    }

    public BigDecimal getRepricedTaxAmount() {
        return repricedTaxAmount;
    }

    public void setRepricedTaxAmount(BigDecimal repricedTaxAmount) {
        this.repricedTaxAmount = repricedTaxAmount;
    }

    public BigDecimal getRoundingAdjustment() {
        return roundingAdjustment;
    }

    public void setRoundingAdjustment(BigDecimal roundingAdjustment) {
        this.roundingAdjustment = roundingAdjustment;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
