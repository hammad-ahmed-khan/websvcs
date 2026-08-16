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
@NamedQueries( { @NamedQuery(name = "OmsOrposTaxLine.findAll", query = "select o from OmsOrposTaxLine o") ,
                  @NamedQuery(name = "OmsOrposTaxLine.findByOmsOrposCustOrdId", query = "select o from OmsOrposTaxLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                                  @NamedQuery(name = "OmsOrposTaxLine.findColumns",
                             query = "select o from OmsOrposTaxLine o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.capturedLineItemNo=:capturedLineItemNo and o.itemId=:itemId")})
@Table(name = "OMS_ORPOS_TAX_LINE")
@IdClass(OmsOrposTaxLinePK.class)
public class OmsOrposTaxLine implements Serializable {
    @Column(name = "CANCELLED_TAX_AMOUNT", nullable = false)
    private BigDecimal cancelledTaxAmount;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "COMPLETED_TAX_AMOUNT", nullable = false)
    private BigDecimal completedTaxAmount;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "INCLUSIVE_TAX_FLAG", nullable = false, length = 1)
    private String inclusiveTaxFlag;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "RETURNED_TAX_AMOUNT", nullable = false)
    private BigDecimal returnedTaxAmount;
    @Column(name = "TAX_AMOUNT", nullable = false)
    private BigDecimal taxAmount;
    @Column(name = "TAX_AUTHORITY_ID", nullable = false)
    private BigDecimal taxAuthorityId;
    @Column(name = "TAX_GROUP_ID", nullable = false)
    private BigDecimal taxGroupId;
    @Column(name = "TAX_HOLIDAY_FLAG", nullable = false, length = 1)
    private String taxHolidayFlag;
    @Column(name = "TAX_MOD_REASON_CODE", length = 20)
    private String taxModReasonCode;
    @Column(name = "TAX_MOD_SCOPE", length = 3)
    private String taxModScope;
    @Column(name = "TAX_MODE", nullable = false, length = 12)
    private String taxMode;
    @Column(name = "TAX_RATE")
    private BigDecimal taxRate;
    @Column(name = "TAX_RULE_NAME", length = 120)
    private String taxRuleName;
    @Column(name = "TAX_TYPE_CODE", nullable = false)
    private BigDecimal taxTypeCode;
    @Column(name = "TAXABLE_AMOUNT", nullable = false)
    private BigDecimal taxableAmount;
    @Column(name = "TAX_AUTHORITY_NAME", length = 120)
    private String taxAuthorityName;

    public OmsOrposTaxLine() {
    }

    public OmsOrposTaxLine(BigDecimal cancelledTaxAmount, BigDecimal capturedLineItemNo, BigDecimal completedTaxAmount,
                           String currencyCode, String inclusiveTaxFlag, String itemId, BigDecimal lineNo,
                           BigDecimal omsOrposCustOrderId, BigDecimal returnedTaxAmount, BigDecimal taxAmount,
                           BigDecimal taxAuthorityId, BigDecimal taxGroupId, String taxHolidayFlag,
                           String taxModReasonCode, String taxModScope, String taxMode, BigDecimal taxRate,
                           String taxRuleName, BigDecimal taxTypeCode, BigDecimal taxableAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
        this.capturedLineItemNo = capturedLineItemNo;
        this.completedTaxAmount = completedTaxAmount;
        this.currencyCode = currencyCode;
        this.inclusiveTaxFlag = inclusiveTaxFlag;
        this.itemId = itemId;
        this.lineNo = lineNo;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.returnedTaxAmount = returnedTaxAmount;
        this.taxAmount = taxAmount;
        this.taxAuthorityId = taxAuthorityId;
        this.taxGroupId = taxGroupId;
        this.taxHolidayFlag = taxHolidayFlag;
        this.taxModReasonCode = taxModReasonCode;
        this.taxModScope = taxModScope;
        this.taxMode = taxMode;
        this.taxRate = taxRate;
        this.taxRuleName = taxRuleName;
        this.taxTypeCode = taxTypeCode;
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getCancelledTaxAmount() {
        return cancelledTaxAmount;
    }

    public void setCancelledTaxAmount(BigDecimal cancelledTaxAmount) {
        this.cancelledTaxAmount = cancelledTaxAmount;
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
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

    public String getInclusiveTaxFlag() {
        return inclusiveTaxFlag;
    }

    public void setInclusiveTaxFlag(String inclusiveTaxFlag) {
        this.inclusiveTaxFlag = inclusiveTaxFlag;
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

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTaxAuthorityId() {
        return taxAuthorityId;
    }

    public void setTaxAuthorityId(BigDecimal taxAuthorityId) {
        this.taxAuthorityId = taxAuthorityId;
    }

    public BigDecimal getTaxGroupId() {
        return taxGroupId;
    }

    public void setTaxGroupId(BigDecimal taxGroupId) {
        this.taxGroupId = taxGroupId;
    }

    public String getTaxHolidayFlag() {
        return taxHolidayFlag;
    }

    public void setTaxHolidayFlag(String taxHolidayFlag) {
        this.taxHolidayFlag = taxHolidayFlag;
    }

    public String getTaxModReasonCode() {
        return taxModReasonCode;
    }

    public void setTaxModReasonCode(String taxModReasonCode) {
        this.taxModReasonCode = taxModReasonCode;
    }

    public String getTaxModScope() {
        return taxModScope;
    }

    public void setTaxModScope(String taxModScope) {
        this.taxModScope = taxModScope;
    }

    public String getTaxMode() {
        return taxMode;
    }

    public void setTaxMode(String taxMode) {
        this.taxMode = taxMode;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxRuleName() {
        return taxRuleName;
    }

    public void setTaxRuleName(String taxRuleName) {
        this.taxRuleName = taxRuleName;
    }

    public BigDecimal getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(BigDecimal taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public void setTaxAuthorityName(String taxAuthorityName) {
        this.taxAuthorityName = taxAuthorityName;
    }

    public String getTaxAuthorityName() {
        return taxAuthorityName;
    }
}
