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
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrdItmPickup.findAll",
                             query = "select o from OmsOrposCustOrdItmPickup o"),
                 @NamedQuery(name="OmsOrposCustOrdItmPickup.findAllColumns",
                             query = "select o from OmsOrposCustOrdItmPickup o where  o.custOrderPicVoSeq=:custOrderPicVoSeq"),
                 @NamedQuery(name = "OmsOrposCustOrdItmPickup.findColumns",
                             query = "select o from OmsOrposCustOrdItmPickup o where o.custOrderPicVoSeq=:custOrderPicVoSeq and o.fulfillOrderId=:fulfillOrderId")})
@Table(name = "OMS_ORPOS_CUST_ORD_ITM_PICKUP")
@IdClass(OmsOrposCustOrdItmPickupPK.class)
public class OmsOrposCustOrdItmPickup implements Serializable {
    @Column(name = "CANCELLED_AMOUNT", nullable = false)
    private BigDecimal cancelledAmount;
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT")
    private BigDecimal cancelledDiscountAmount;
    @Column(name = "CANCELLED_INC_TAX_AMOUNT")
    private BigDecimal cancelledIncTaxAmount;
    @Column(name = "CANCELLED_QUANTITY", nullable = false)
    private BigDecimal cancelledQuantity;
    @Column(name = "CANCELLED_TAX_AMOUNT")
    private BigDecimal cancelledTaxAmount;
    @Column(name = "COMPLETED_AMOUNT", nullable = false)
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
    @Column(name = "COMPLETED_QUANTITY", nullable = false)
    private BigDecimal completedQuantity;
    @Column(name = "COMPLETED_REPRICED_QUANTITY")
    private BigDecimal completedRepricedQuantity;
    @Column(name = "COMPLETED_TAX_AMOUNT")
    private BigDecimal completedTaxAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Id
    @Column(name = "CUST_ORDER_PIC_VO_SEQ", nullable = false)
    private BigDecimal custOrderPicVoSeq;
    @Column(name = "FULFILL_ORDER_ID", nullable = false, length = 48)
    private String fulfillOrderId;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;
    @Column(name = "REPRICED_AMOUNT")
    private BigDecimal repricedAmount;
    @Column(name = "REPRICED_DISCOUNT_AMOUNT")
    private BigDecimal repricedDiscountAmount;
    @Column(name = "REPRICED_INC_TAX_AMOUNT")
    private BigDecimal repricedIncTaxAmount;
    @Column(name = "REPRICED_TAX_AMOUNT")
    private BigDecimal repricedTaxAmount;
    @Column(name = "SERIAL_NUMBER", length = 40)
    private String serialNumber;
    @Column(name = "UNIT_OF_MEASURE", length = 4)
    private String unitOfMeasure;

    public OmsOrposCustOrdItmPickup() {
    }

    public OmsOrposCustOrdItmPickup(BigDecimal cancelledAmount, BigDecimal cancelledDiscountAmount,
                                    BigDecimal cancelledIncTaxAmount, BigDecimal cancelledQuantity,
                                    BigDecimal cancelledTaxAmount, BigDecimal completedAmount,
                                    BigDecimal completedDiscountAmount, BigDecimal completedIncTaxAmount,
                                    BigDecimal completedNewAmount, BigDecimal completedNewDiscountAmount,
                                    BigDecimal completedNewIncTaxAmount, BigDecimal completedNewTaxAmount,
                                    BigDecimal completedQuantity, BigDecimal completedRepricedQuantity,
                                    BigDecimal completedTaxAmount, String currencyCode, BigDecimal custOrderPicVoSeq,
                                    String fulfillOrderId, BigDecimal lineItemNo, BigDecimal paidAmount,
                                    BigDecimal repricedAmount, BigDecimal repricedDiscountAmount,
                                    BigDecimal repricedIncTaxAmount, BigDecimal repricedTaxAmount, String serialNumber,
                                    String unitOfMeasure) {
        this.cancelledAmount = cancelledAmount;
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.cancelledIncTaxAmount = cancelledIncTaxAmount;
        this.cancelledQuantity = cancelledQuantity;
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.completedAmount = completedAmount;
        this.completedDiscountAmount = completedDiscountAmount;
        this.completedIncTaxAmount = completedIncTaxAmount;
        this.completedNewAmount = completedNewAmount;
        this.completedNewDiscountAmount = completedNewDiscountAmount;
        this.completedNewIncTaxAmount = completedNewIncTaxAmount;
        this.completedNewTaxAmount = completedNewTaxAmount;
        this.completedQuantity = completedQuantity;
        this.completedRepricedQuantity = completedRepricedQuantity;
        this.completedTaxAmount = completedTaxAmount;
        this.currencyCode = currencyCode;
        this.custOrderPicVoSeq = custOrderPicVoSeq;
        this.fulfillOrderId = fulfillOrderId;
        this.lineItemNo = lineItemNo;
        this.paidAmount = paidAmount;
        this.repricedAmount = repricedAmount;
        this.repricedDiscountAmount = repricedDiscountAmount;
        this.repricedIncTaxAmount = repricedIncTaxAmount;
        this.repricedTaxAmount = repricedTaxAmount;
        this.serialNumber = serialNumber;
        this.unitOfMeasure = unitOfMeasure;
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

    public BigDecimal getCancelledQuantity() {
        return cancelledQuantity;
    }

    public void setCancelledQuantity(BigDecimal cancelledQuantity) {
        this.cancelledQuantity = cancelledQuantity;
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

    public BigDecimal getCompletedQuantity() {
        return completedQuantity;
    }

    public void setCompletedQuantity(BigDecimal completedQuantity) {
        this.completedQuantity = completedQuantity;
    }

    public BigDecimal getCompletedRepricedQuantity() {
        return completedRepricedQuantity;
    }

    public void setCompletedRepricedQuantity(BigDecimal completedRepricedQuantity) {
        this.completedRepricedQuantity = completedRepricedQuantity;
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

    public String getFulfillOrderId() {
        return fulfillOrderId;
    }

    public void setFulfillOrderId(String fulfillOrderId) {
        this.fulfillOrderId = fulfillOrderId;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }
}
