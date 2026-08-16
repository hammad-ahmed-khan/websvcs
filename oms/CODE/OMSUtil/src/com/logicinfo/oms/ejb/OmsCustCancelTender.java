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
@NamedQueries( { @NamedQuery(name = "OmsCustCancelTender.findAll",
                 query = "select o from OmsCustCancelTender o"),
                 @NamedQuery(name = "OmsCustCancelTender.findbyOmscancelIdOmsCustOrdNumberMaxOfTenderSeqNo",
                 query = "select max(o.tenderSeqNo) from OmsCustCancelTender o where o.omsCancelId=:omsCancelId and o.omsCustOrdNo=:omsCustOrdNo")
                 })
@Table(name = "OMS_CUST_CANCEL_TENDER")
@IdClass(OmsCustCancelTenderPK.class)
public class OmsCustCancelTender implements Serializable {
    @Column(name = "CC_AUTH_NO", length = 16)
    private String ccAuthNo;
    @Column(name = "CC_AUTH_SRC", length = 6)
    private String ccAuthSrc;
    @Column(name = "CC_CARDHOLDER_VERF", length = 6)
    private String ccCardholderVerf;
    @Column(name = "CC_ENTRY_MODE", length = 6)
    private String ccEntryMode;
    @Column(name = "CC_EXP_DATE")
    private Timestamp ccExpDate;
    @Column(name = "CC_NO", length = 40)
    private String ccNo;
    @Column(name = "CC_SPEC_COND", length = 6)
    private String ccSpecCond;
    @Column(name = "CC_TERM_ID", length = 5)
    private String ccTermId;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(name = "OMS_CANCEL_ID", nullable = false)
    private BigDecimal omsCancelId;
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "TENDER_AMT", nullable = false)
    private BigDecimal tenderAmt;
    @Column(name = "TENDER_REF_ID")
    private String tenderRefId;
    @Id
    @Column(name = "TENDER_SEQ_NO", nullable = false)
    private BigDecimal tenderSeqNo;
    @Column(name = "TENDER_TYPE_GROUP", nullable = false, length = 6)
    private String tenderTypeGroup;
    @Column(name = "TENDER_TYPE_ID", nullable = false)
    private BigDecimal tenderTypeId;

    public OmsCustCancelTender() {
    }

    public OmsCustCancelTender(String ccAuthNo, String ccAuthSrc, String ccCardholderVerf, String ccEntryMode,
                               Timestamp ccExpDate, String ccNo, String ccSpecCond, String ccTermId,
                               Timestamp createDatetime, BigDecimal omsCancelId, BigDecimal omsCustOrdNo,
                               BigDecimal tenderAmt, String tenderRefId, BigDecimal tenderSeqNo,
                               String tenderTypeGroup, BigDecimal tenderTypeId) {
        this.ccAuthNo = ccAuthNo;
        this.ccAuthSrc = ccAuthSrc;
        this.ccCardholderVerf = ccCardholderVerf;
        this.ccEntryMode = ccEntryMode;
        this.ccExpDate = ccExpDate;
        this.ccNo = ccNo;
        this.ccSpecCond = ccSpecCond;
        this.ccTermId = ccTermId;
        this.createDatetime = createDatetime;
        this.omsCancelId = omsCancelId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tenderAmt = tenderAmt;
        this.tenderRefId = tenderRefId;
        this.tenderSeqNo = tenderSeqNo;
        this.tenderTypeGroup = tenderTypeGroup;
        this.tenderTypeId = tenderTypeId;
    }

    public String getCcAuthNo() {
        return ccAuthNo;
    }

    public void setCcAuthNo(String ccAuthNo) {
        this.ccAuthNo = ccAuthNo;
    }

    public String getCcAuthSrc() {
        return ccAuthSrc;
    }

    public void setCcAuthSrc(String ccAuthSrc) {
        this.ccAuthSrc = ccAuthSrc;
    }

    public String getCcCardholderVerf() {
        return ccCardholderVerf;
    }

    public void setCcCardholderVerf(String ccCardholderVerf) {
        this.ccCardholderVerf = ccCardholderVerf;
    }

    public String getCcEntryMode() {
        return ccEntryMode;
    }

    public void setCcEntryMode(String ccEntryMode) {
        this.ccEntryMode = ccEntryMode;
    }

    public Timestamp getCcExpDate() {
        return ccExpDate;
    }

    public void setCcExpDate(Timestamp ccExpDate) {
        this.ccExpDate = ccExpDate;
    }

    public String getCcNo() {
        return ccNo;
    }

    public void setCcNo(String ccNo) {
        this.ccNo = ccNo;
    }

    public String getCcSpecCond() {
        return ccSpecCond;
    }

    public void setCcSpecCond(String ccSpecCond) {
        this.ccSpecCond = ccSpecCond;
    }

    public String getCcTermId() {
        return ccTermId;
    }

    public void setCcTermId(String ccTermId) {
        this.ccTermId = ccTermId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTenderAmt() {
        return tenderAmt;
    }

    public void setTenderAmt(BigDecimal tenderAmt) {
        this.tenderAmt = tenderAmt;
    }

    public String getTenderRefId() {
        return tenderRefId;
    }

    public void setTenderRefId(String tenderRefId) {
        this.tenderRefId = tenderRefId;
    }

    public BigDecimal getTenderSeqNo() {
        return tenderSeqNo;
    }

    public void setTenderSeqNo(BigDecimal tenderSeqNo) {
        this.tenderSeqNo = tenderSeqNo;
    }

    public String getTenderTypeGroup() {
        return tenderTypeGroup;
    }

    public void setTenderTypeGroup(String tenderTypeGroup) {
        this.tenderTypeGroup = tenderTypeGroup;
    }

    public BigDecimal getTenderTypeId() {
        return tenderTypeId;
    }

    public void setTenderTypeId(BigDecimal tenderTypeId) {
        this.tenderTypeId = tenderTypeId;
    }
}
