package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrderHead.findAll",
                             query = "select o from OmsOrposCustOrderHead o"),
                 @NamedQuery(name = "OmsOrposCustOrderHead.findOmsOrposCustOrdId",
                             query = "select o.omsOrposCustOrderId from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status='S'"),
                 @NamedQuery(name = "OmsOrposCustOrderHead.findOrderHeadDetailsByOrderId",
                             query = "select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status='S'"),
                  @NamedQuery(name = "OmsOrposCustOrderHead.findOrderHeadDetailsByCustomerId",
                             query = "select o from OmsOrposCustOrderHead o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                   @NamedQuery(name="OmsOrposCustOrderHead.findColumns",
                             query = "select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status='S'"),
                 @NamedQuery(name="OmsOrposCustOrderHead.findBycustomerOrderId",
                             query = "select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status='S'"),
                 @NamedQuery(name="OmsOrposCustOrderHead.findBycustomerOrderIdandstatus",
                             query="select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status=:status"),
                 @NamedQuery(name="OmsOrposCustOrderHead.findBycustomerOrderIdandstatusList",
                             query="select o from OmsOrposCustOrderHead o where o.customerOrderId=:customerOrderId and o.status=:status"),
                 @NamedQuery(name="OmsOrposCustOrderHead.findByomsOrposCustOrderId",
                             query="select o from OmsOrposCustOrderHead o where o.omsOrposCustOrderId=:omsOrposCustOrderId")
                 })
@Table(name = "OMS_ORPOS_CUST_ORDER_HEAD")
public class OmsOrposCustOrderHead implements Serializable {
    @Column(name = "AGE_RESTRICTED_DOB")
    private Timestamp ageRestrictedDob;
    @Column(name = "CANCELLED_AMOUNT")
    private BigDecimal cancelledAmount;
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT")
    private BigDecimal cancelledDiscountAmount;
    @Column(name = "CANCELLED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal cancelledInclusiveTaxAmount;
    @Column(name = "CANCELLED_TAX_AMOUNT")
    private BigDecimal cancelledTaxAmount;
    @Column(name = "COMPLETED_AMOUNT")
    private BigDecimal completedAmount;
    @Column(name = "COMPLETED_DISCOUNT_AMOUNT")
    private BigDecimal completedDiscountAmount;
    @Column(name = "COMPLETED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal completedInclusiveTaxAmount;
    @Column(name = "COMPLETED_TAX_AMOUNT")
    private BigDecimal completedTaxAmount;
    @Column(name = "CREATE_TIMESTAMP", nullable = false)
    private Timestamp createTimestamp;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 2)
    private String currencyCode;
    @Column(name = "CUSTOMER_ORDER_ID", nullable = false, length = 48)
    private String customerOrderId;
    @Column(name = "DEFAULT_GIFT_REGISTRY_ID", length = 14)
    private String defaultGiftRegistryId;
    @Column(name = "DISCOUNT_TOTAL")
    private BigDecimal discountTotal;
    @Column(name = "EXTERNAL_REF_ID", length = 120)
    private String externalRefId;
    @Column(name = "GIFT_RECEIPT_ASSIGNED", length = 1)
    private String giftReceiptAssigned;
    @Column(name = "GRAND_TOTAL")
    private BigDecimal grandTotal;
    @Column(name = "INCLUSIVE_TAX_TOTAL")
    private BigDecimal inclusiveTaxTotal;
    @Column(name = "INITIATE_COUNTRY_CODE", nullable = false)
    private String initiateCountryCode;
    @Column(name = "INITIATE_LOC_ID", nullable = false)
    private BigDecimal initiateLocId;
    @Column(name = "INITIATE_LOC_TYPE", length = 1)
    private String initiateLocType;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    @SequenceGenerator( name = "omsOrposCustOrdSeq", sequenceName = "OMS_ORPOS_CUST_ORDER_SEQ", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposCustOrdSeq" )    
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "ORDER_DESC", length = 250)
    private String orderDesc;
    @Column(name = "ORDER_STATUS", length = 9)
    private String orderStatus;
    @Column(name = "PAID_AMOUNT")
    private BigDecimal paidAmount;
    @Column(name = "REFUND_AMOUNT_OFFSET_BY_SALE")
    private BigDecimal refundAmountOffsetBySale;
    @Column(name = "RETURNED_AMOUNT")
    private BigDecimal returnedAmount;
    @Column(name = "RETURNED_DISCOUNT_AMOUNT")
    private BigDecimal returnedDiscountAmount;
    @Column(name = "RETURNED_INCLUSIVE_TAX_AMOUNT")
    private BigDecimal returnedInclusiveTaxAmount;
    @Column(name = "RETURNED_TAX_AMOUNT")
    private BigDecimal returnedTaxAmount;
    @Column(name = "ROUNDING_ADJUSTMENT")
    private BigDecimal roundingAdjustment;
    @Column(name = "SHIPPING_CHARGE_TOTAL")
    private BigDecimal shippingChargeTotal;
    @Column(length = 1)
    private String status;
    @Column(name = "SUB_TOTAL")
    private BigDecimal subTotal;
    @Column(name = "TAX_TOTAL")
    private BigDecimal taxTotal;
    @Column(name = "UPDATE_TIMESTAMP", nullable = false)
    private Timestamp updateTimestamp;

    public OmsOrposCustOrderHead() {
    }

    public OmsOrposCustOrderHead(Timestamp ageRestrictedDob, BigDecimal cancelledAmount,
                                 BigDecimal cancelledDiscountAmount, BigDecimal cancelledInclusiveTaxAmount,
                                 BigDecimal cancelledTaxAmount, BigDecimal completedAmount,
                                 BigDecimal completedDiscountAmount, BigDecimal completedInclusiveTaxAmount,
                                 BigDecimal completedTaxAmount, Timestamp createTimestamp, String currencyCode,
                                 String customerOrderId, String defaultGiftRegistryId, BigDecimal discountTotal,
                                 String externalRefId, String giftReceiptAssigned, BigDecimal grandTotal,
                                 BigDecimal inclusiveTaxTotal, String initiateCountryCode,
                                 BigDecimal initiateLocId, String initiateLocType, BigDecimal omsOrposCustOrderId,
                                 String orderDesc, String orderStatus, BigDecimal paidAmount,
                                 BigDecimal refundAmountOffsetBySale, BigDecimal returnedAmount,
                                 BigDecimal returnedDiscountAmount, BigDecimal returnedInclusiveTaxAmount,
                                 BigDecimal returnedTaxAmount, BigDecimal roundingAdjustment,
                                 BigDecimal shippingChargeTotal, String status, BigDecimal subTotal,
                                 BigDecimal taxTotal, Timestamp updateTimestamp) {
        this.ageRestrictedDob = ageRestrictedDob;
        this.cancelledAmount = cancelledAmount;
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.cancelledInclusiveTaxAmount = cancelledInclusiveTaxAmount;
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.completedAmount = completedAmount;
        this.completedDiscountAmount = completedDiscountAmount;
        this.completedInclusiveTaxAmount = completedInclusiveTaxAmount;
        this.completedTaxAmount = completedTaxAmount;
        this.createTimestamp = createTimestamp;
        this.currencyCode = currencyCode;
        this.customerOrderId = customerOrderId;
        this.defaultGiftRegistryId = defaultGiftRegistryId;
        this.discountTotal = discountTotal;
        this.externalRefId = externalRefId;
        this.giftReceiptAssigned = giftReceiptAssigned;
        this.grandTotal = grandTotal;
        this.inclusiveTaxTotal = inclusiveTaxTotal;
        this.initiateCountryCode = initiateCountryCode;
        this.initiateLocId = initiateLocId;
        this.initiateLocType = initiateLocType;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.orderDesc = orderDesc;
        this.orderStatus = orderStatus;
        this.paidAmount = paidAmount;
        this.refundAmountOffsetBySale = refundAmountOffsetBySale;
        this.returnedAmount = returnedAmount;
        this.returnedDiscountAmount = returnedDiscountAmount;
        this.returnedInclusiveTaxAmount = returnedInclusiveTaxAmount;
        this.returnedTaxAmount = returnedTaxAmount;
        this.roundingAdjustment = roundingAdjustment;
        this.shippingChargeTotal = shippingChargeTotal;
        this.status = status;
        this.subTotal = subTotal;
        this.taxTotal = taxTotal;
        this.updateTimestamp = updateTimestamp;
    }

    public Timestamp getAgeRestrictedDob() {
        return ageRestrictedDob;
    }

    public void setAgeRestrictedDob(Timestamp ageRestrictedDob) {
        this.ageRestrictedDob = ageRestrictedDob;
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

    public BigDecimal getCancelledInclusiveTaxAmount() {
        return cancelledInclusiveTaxAmount;
    }

    public void setCancelledInclusiveTaxAmount(BigDecimal cancelledInclusiveTaxAmount) {
        this.cancelledInclusiveTaxAmount = cancelledInclusiveTaxAmount;
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

    public BigDecimal getCompletedInclusiveTaxAmount() {
        return completedInclusiveTaxAmount;
    }

    public void setCompletedInclusiveTaxAmount(BigDecimal completedInclusiveTaxAmount) {
        this.completedInclusiveTaxAmount = completedInclusiveTaxAmount;
    }

    public BigDecimal getCompletedTaxAmount() {
        return completedTaxAmount;
    }

    public void setCompletedTaxAmount(BigDecimal completedTaxAmount) {
        this.completedTaxAmount = completedTaxAmount;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getDefaultGiftRegistryId() {
        return defaultGiftRegistryId;
    }

    public void setDefaultGiftRegistryId(String defaultGiftRegistryId) {
        this.defaultGiftRegistryId = defaultGiftRegistryId;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public String getGiftReceiptAssigned() {
        return giftReceiptAssigned;
    }

    public void setGiftReceiptAssigned(String giftReceiptAssigned) {
        this.giftReceiptAssigned = giftReceiptAssigned;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    public BigDecimal getInclusiveTaxTotal() {
        return inclusiveTaxTotal;
    }

    public void setInclusiveTaxTotal(BigDecimal inclusiveTaxTotal) {
        this.inclusiveTaxTotal = inclusiveTaxTotal;
    }

    public String getInitiateCountryCode() {
        return initiateCountryCode;
    }

    public void setInitiateCountryCode(String initiateCountryCode) {
        this.initiateCountryCode = initiateCountryCode;
    }

    public BigDecimal getInitiateLocId() {
        return initiateLocId;
    }

    public void setInitiateLocId(BigDecimal initiateLocId) {
        this.initiateLocId = initiateLocId;
    }

    public String getInitiateLocType() {
        return initiateLocType;
    }

    public void setInitiateLocType(String initiateLocType) {
        this.initiateLocType = initiateLocType;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public BigDecimal getRoundingAdjustment() {
        return roundingAdjustment;
    }

    public void setRoundingAdjustment(BigDecimal roundingAdjustment) {
        this.roundingAdjustment = roundingAdjustment;
    }

    public BigDecimal getShippingChargeTotal() {
        return shippingChargeTotal;
    }

    public void setShippingChargeTotal(BigDecimal shippingChargeTotal) {
        this.shippingChargeTotal = shippingChargeTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
}
