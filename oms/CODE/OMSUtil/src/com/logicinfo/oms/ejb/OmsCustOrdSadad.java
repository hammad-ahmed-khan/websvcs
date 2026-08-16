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
@NamedQueries( { @NamedQuery(name = "OmsCustOrdSadad.findAll", query = "select o from OmsCustOrdSadad o"),
                 @NamedQuery(name = "OmsCustOrdSadad.findByOmsCustOrdNoAndItem",
                             query = "select o from OmsCustOrdSadad o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item"),
                 @NamedQuery(name = "OmsCustOrdSadad.findByOmsCustOrdNo",
                             query = "select o from OmsCustOrdSadad o where o.omsCustOrdNo=:omsCustOrdNo ") })
@Table(name = "OMS_CUST_ORD_SADAD")
@IdClass(OmsCustOrdSadadPK.class)
public class OmsCustOrdSadad implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(nullable = false)
    private BigDecimal loc;
    @Column(name = "LOC_TYPE", nullable = false, length = 2)
    private String locType;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(nullable = false)
    private BigDecimal qty;
    @Column(name = "RMS_RESV_LOC")
    private BigDecimal rmsResvLoc;
    @Column(name = "RMS_RESV_LOC_TYPE", length = 2)
    private String rmsResvLocType;
    @Column(name = "RMS_RESV_QTY")
    private BigDecimal rmsResvQty;
    @Column(name = "SADAD_CONF_TS")
    private Timestamp sadadConfTs;
    @Column(name = "SADAD_INITIATE_TS")
    private Timestamp sadadInitiateTs;
    @Column(name = "SADAD_PAYMENT_STATUS", length = 1)
    private String sadadPaymentStatus;
    @Column(name = "SADAD_RESV_STATUS", nullable = false, length = 3)
    private String sadadResvStatus;

    public OmsCustOrdSadad() {
    }

    public OmsCustOrdSadad(Timestamp createDatetime, String createdBy, String item, BigDecimal loc, String locType,
                           BigDecimal omsCustOrdNo, BigDecimal qty, BigDecimal rmsResvLoc, String rmsResvLocType,
                           BigDecimal rmsResvQty, Timestamp sadadConfTs, Timestamp sadadInitiateTs,
                           String sadadPaymentStatus, String sadadResvStatus) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.item = item;
        this.loc = loc;
        this.locType = locType;
        this.omsCustOrdNo = omsCustOrdNo;
        this.qty = qty;
        this.rmsResvLoc = rmsResvLoc;
        this.rmsResvLocType = rmsResvLocType;
        this.rmsResvQty = rmsResvQty;
        this.sadadConfTs = sadadConfTs;
        this.sadadInitiateTs = sadadInitiateTs;
        this.sadadPaymentStatus = sadadPaymentStatus;
        this.sadadResvStatus = sadadResvStatus;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLoc() {
        return loc;
    }

    public void setLoc(BigDecimal loc) {
        this.loc = loc;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getRmsResvLoc() {
        return rmsResvLoc;
    }

    public void setRmsResvLoc(BigDecimal rmsResvLoc) {
        this.rmsResvLoc = rmsResvLoc;
    }

    public String getRmsResvLocType() {
        return rmsResvLocType;
    }

    public void setRmsResvLocType(String rmsResvLocType) {
        this.rmsResvLocType = rmsResvLocType;
    }

    public BigDecimal getRmsResvQty() {
        return rmsResvQty;
    }

    public void setRmsResvQty(BigDecimal rmsResvQty) {
        this.rmsResvQty = rmsResvQty;
    }

    public Timestamp getSadadConfTs() {
        return sadadConfTs;
    }

    public void setSadadConfTs(Timestamp sadadConfTs) {
        this.sadadConfTs = sadadConfTs;
    }

    public Timestamp getSadadInitiateTs() {
        return sadadInitiateTs;
    }

    public void setSadadInitiateTs(Timestamp sadadInitiateTs) {
        this.sadadInitiateTs = sadadInitiateTs;
    }

    public String getSadadPaymentStatus() {
        return sadadPaymentStatus;
    }

    public void setSadadPaymentStatus(String sadadPaymentStatus) {
        this.sadadPaymentStatus = sadadPaymentStatus;
    }

    public String getSadadResvStatus() {
        return sadadResvStatus;
    }

    public void setSadadResvStatus(String sadadResvStatus) {
        this.sadadResvStatus = sadadResvStatus;
    }
}
