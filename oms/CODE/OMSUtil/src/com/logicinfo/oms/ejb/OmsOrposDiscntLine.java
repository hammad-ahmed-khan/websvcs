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
@NamedQueries( { @NamedQuery(name = "OmsOrposDiscntLine.findAll", 
                             query = "select o from OmsOrposDiscntLine o"),
                 @NamedQuery(name = "OmsOrposDiscntLine.findLineNoandDiscLineNo",
query = "select o from OmsOrposDiscntLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.lineNo=:lineNo and o.capturedLineItemNo=:capturedLineItemNo")})
@Table(name = "OMS_ORPOS_DISCNT_LINE")
@IdClass(OmsOrposDiscntLinePK.class)
public class OmsOrposDiscntLine implements Serializable {
    @Column(name = "ACCOUNTING_METHOD", nullable = false, length = 8)
    private String accountingMethod;
    @Column(name = "ADVANCED_PRICING_RULE_FLAG", nullable = false, length = 8)
    private String advancedPricingRuleFlag;
    @Column(name = "ASSIGNMENT_BASIS", nullable = false, length = 4)
    private String assignmentBasis;
    @Column(name = "CANCELLED_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal cancelledDiscountAmount;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "COMPLETED_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal completedDiscountAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "DAMAGE_DISCOUNT_FLAG", nullable = false, length = 1)
    private String damageDiscountFlag;
    @Column(name = "DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal discountAmount;
    @Column(name = "DISCOUNT_EMPLOYEE_ID", length = 10)
    private String discountEmployeeId;
    @Column(name = "DISCOUNT_METHOD", nullable = false, length = 3)
    private String discountMethod;
    @Column(name = "DISCOUNT_RATE")
    private BigDecimal discountRate;
    @Column(name = "DISCOUNT_REASON_CODE", length = 20)
    private String discountReasonCode;
    @Column(name = "DISCOUNT_RULE_ID", length = 22)
    private String discountRuleId;
    @Column(name = "DISCOUNT_SCOPE", nullable = false, length = 3)
    private String discountScope;
    @Column(name = "INCLUDED_IN_BESTDEAL_FLAG", nullable = false, length = 1)
    private String includedInBestdealFlag;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PROMOTION_COMPONENT_DETAIL_ID")
    private BigDecimal promotionComponentDetailId;
    @Column(name = "PROMOTION_COMPONENT_ID")
    private BigDecimal promotionComponentId;
    @Column(name = "PROMOTION_ID")
    private BigDecimal promotionId;
    @Column(name = "RETURNED_DISCOUNT_AMOUNT", nullable = false)
    private BigDecimal returnedDiscountAmount;
    @Column(name = "STORE_COUPON_ID")
    private String storeCouponId;
    @Column(name = "UNIT_DISCOUNT_AMOUNT")
    private BigDecimal unitDiscountAmount;

    public OmsOrposDiscntLine() {
    }

    public OmsOrposDiscntLine(String accountingMethod, String advancedPricingRuleFlag, String assignmentBasis,
                              BigDecimal cancelledDiscountAmount, BigDecimal capturedLineItemNo,
                              BigDecimal completedDiscountAmount, String currencyCode, String damageDiscountFlag,
                              BigDecimal discountAmount, String discountEmployeeId, String discountMethod,
                              BigDecimal discountRate, String discountReasonCode, String discountRuleId,
                              String discountScope, String includedInBestdealFlag, String itemId, BigDecimal lineNo,
                              BigDecimal omsCustOrdNo, BigDecimal omsOrposCustOrderId,
                              BigDecimal promotionComponentDetailId, BigDecimal promotionComponentId,
                              BigDecimal promotionId, BigDecimal returnedDiscountAmount, String storeCouponId,
                              BigDecimal unitDiscountAmount) {
        this.accountingMethod = accountingMethod;
        this.advancedPricingRuleFlag = advancedPricingRuleFlag;
        this.assignmentBasis = assignmentBasis;
        this.cancelledDiscountAmount = cancelledDiscountAmount;
        this.capturedLineItemNo = capturedLineItemNo;
        this.completedDiscountAmount = completedDiscountAmount;
        this.currencyCode = currencyCode;
        this.damageDiscountFlag = damageDiscountFlag;
        this.discountAmount = discountAmount;
        this.discountEmployeeId = discountEmployeeId;
        this.discountMethod = discountMethod;
        this.discountRate = discountRate;
        this.discountReasonCode = discountReasonCode;
        this.discountRuleId = discountRuleId;
        this.discountScope = discountScope;
        this.includedInBestdealFlag = includedInBestdealFlag;
        this.itemId = itemId;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.promotionComponentDetailId = promotionComponentDetailId;
        this.promotionComponentId = promotionComponentId;
        this.promotionId = promotionId;
        this.returnedDiscountAmount = returnedDiscountAmount;
        this.storeCouponId = storeCouponId;
        this.unitDiscountAmount = unitDiscountAmount;
    }

    public String getAccountingMethod() {
        return accountingMethod;
    }

    public void setAccountingMethod(String accountingMethod) {
        this.accountingMethod = accountingMethod;
    }

    public String getAdvancedPricingRuleFlag() {
        return advancedPricingRuleFlag;
    }

    public void setAdvancedPricingRuleFlag(String advancedPricingRuleFlag) {
        this.advancedPricingRuleFlag = advancedPricingRuleFlag;
    }

    public String getAssignmentBasis() {
        return assignmentBasis;
    }

    public void setAssignmentBasis(String assignmentBasis) {
        this.assignmentBasis = assignmentBasis;
    }

    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    public void setCancelledDiscountAmount(BigDecimal cancelledDiscountAmount) {
        this.cancelledDiscountAmount = cancelledDiscountAmount;
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
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

    public String getDamageDiscountFlag() {
        return damageDiscountFlag;
    }

    public void setDamageDiscountFlag(String damageDiscountFlag) {
        this.damageDiscountFlag = damageDiscountFlag;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountEmployeeId() {
        return discountEmployeeId;
    }

    public void setDiscountEmployeeId(String discountEmployeeId) {
        this.discountEmployeeId = discountEmployeeId;
    }

    public String getDiscountMethod() {
        return discountMethod;
    }

    public void setDiscountMethod(String discountMethod) {
        this.discountMethod = discountMethod;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public String getDiscountReasonCode() {
        return discountReasonCode;
    }

    public void setDiscountReasonCode(String discountReasonCode) {
        this.discountReasonCode = discountReasonCode;
    }

    public String getDiscountRuleId() {
        return discountRuleId;
    }

    public void setDiscountRuleId(String discountRuleId) {
        this.discountRuleId = discountRuleId;
    }

    public String getDiscountScope() {
        return discountScope;
    }

    public void setDiscountScope(String discountScope) {
        this.discountScope = discountScope;
    }

    public String getIncludedInBestdealFlag() {
        return includedInBestdealFlag;
    }

    public void setIncludedInBestdealFlag(String includedInBestdealFlag) {
        this.includedInBestdealFlag = includedInBestdealFlag;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPromotionComponentDetailId() {
        return promotionComponentDetailId;
    }

    public void setPromotionComponentDetailId(BigDecimal promotionComponentDetailId) {
        this.promotionComponentDetailId = promotionComponentDetailId;
    }

    public BigDecimal getPromotionComponentId() {
        return promotionComponentId;
    }

    public void setPromotionComponentId(BigDecimal promotionComponentId) {
        this.promotionComponentId = promotionComponentId;
    }

    public BigDecimal getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(BigDecimal promotionId) {
        this.promotionId = promotionId;
    }

    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    public void setReturnedDiscountAmount(BigDecimal returnedDiscountAmount) {
        this.returnedDiscountAmount = returnedDiscountAmount;
    }

    public String getStoreCouponId() {
        return storeCouponId;
    }

    public void setStoreCouponId(String storeCouponId) {
        this.storeCouponId = storeCouponId;
    }

    public BigDecimal getUnitDiscountAmount() {
        return unitDiscountAmount;
    }

    public void setUnitDiscountAmount(BigDecimal unitDiscountAmount) {
        this.unitDiscountAmount = unitDiscountAmount;
    }
}
