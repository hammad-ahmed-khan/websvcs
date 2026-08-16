package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "OmsTaxDesc.findAll", query = "select o from OmsTaxDesc o") })
@Table(name = "OMS_TAX_DESC")
@IdClass(OmsTaxDescPK.class)

public class OmsTaxDesc implements Serializable {
    @Column(name = "ACTIVE_DATE", nullable = false)
    private Timestamp activeDate;
    @Column(length = 250)
    private String comments;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Column(name = "INCLUSIVE_TAX_FLAG", nullable = false, length = 1)
    private String inclusiveTaxFlag;
    @Column(name = "LAST_UPDATE_DATETIME", nullable = false)
    private Timestamp lastUpdateDatetime;
    @Column(name = "LAST_UPDATE_ID", nullable = false, length = 30)
    private String lastUpdateId;
    @Column(name = "TAX_AUTHORITY_ID", nullable = false)
    private BigDecimal taxAuthorityId;
    @Column(name = "TAX_AUTHORITY_NAME", length = 120)
    private String taxAuthorityName;
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
    @Id
    @Column(name = "VAT_CODE", nullable = false, length = 120)
    private String vatCode;
    @Id
    @Column(name = "VAT_REGION", nullable = false)
    private BigDecimal vatRegion;

    public OmsTaxDesc() {
    }

    public OmsTaxDesc(Timestamp activeDate, String comments, Timestamp createDatetime, String createId,
                      String inclusiveTaxFlag, Timestamp lastUpdateDatetime, String lastUpdateId,
                      BigDecimal taxAuthorityId, String taxAuthorityName, BigDecimal taxGroupId, String taxHolidayFlag,
                      String taxModReasonCode, String taxModScope, String taxMode, BigDecimal taxRate,
                      String taxRuleName, BigDecimal taxTypeCode, String vatCode, BigDecimal vatRegion) {
        this.activeDate = activeDate;
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.inclusiveTaxFlag = inclusiveTaxFlag;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdateId = lastUpdateId;
        this.taxAuthorityId = taxAuthorityId;
        this.taxAuthorityName = taxAuthorityName;
        this.taxGroupId = taxGroupId;
        this.taxHolidayFlag = taxHolidayFlag;
        this.taxModReasonCode = taxModReasonCode;
        this.taxModScope = taxModScope;
        this.taxMode = taxMode;
        this.taxRate = taxRate;
        this.taxRuleName = taxRuleName;
        this.taxTypeCode = taxTypeCode;
        this.vatCode = vatCode;
        this.vatRegion = vatRegion;
    }

    public Timestamp getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Timestamp activeDate) {
        this.activeDate = activeDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getInclusiveTaxFlag() {
        return inclusiveTaxFlag;
    }

    public void setInclusiveTaxFlag(String inclusiveTaxFlag) {
        this.inclusiveTaxFlag = inclusiveTaxFlag;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public BigDecimal getTaxAuthorityId() {
        return taxAuthorityId;
    }

    public void setTaxAuthorityId(BigDecimal taxAuthorityId) {
        this.taxAuthorityId = taxAuthorityId;
    }

    public String getTaxAuthorityName() {
        return taxAuthorityName;
    }

    public void setTaxAuthorityName(String taxAuthorityName) {
        this.taxAuthorityName = taxAuthorityName;
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

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public BigDecimal getVatRegion() {
        return vatRegion;
    }

    public void setVatRegion(BigDecimal vatRegion) {
        this.vatRegion = vatRegion;
    }
}