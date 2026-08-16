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
@NamedQueries( { @NamedQuery(name = "OmsCustOrdReserve.findAll", query = "select o from OmsCustOrdReserve o"),
                 @NamedQuery(name = "OmsCustOrdReserve.findByOmsCustOrdNoAndItem",
                             query = "select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and o.lineNo=:lineNo ORDER BY o.fulfillOrderNo desc"),
                 @NamedQuery(name = "OmsCustOrdReserve.findByOmsCustOrdNo",
                             query = "select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo "),
              @NamedQuery(name = "OmsCustOrdReserve.findByFulOrdNo",
                             query = "select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo and o.fulfillOrderNo=:fulfillOrderNo"),
                     @NamedQuery(name = "OmsCustOrdReserve.findByOmsCustOrdNoAndItemAndResvLoc",
                             query = "select o from OmsCustOrdReserve o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and o.lineNo=:lineNo and o.rmsResvLoc=:rmsResvLoc")})
@Table(name = "OMS_CUST_ORD_RESERVE")
@IdClass(OmsCustOrdReservePK.class)
public class OmsCustOrdReserve implements Serializable {
    @Column(name = "COMBINATION_ID")
    private BigDecimal combinationId;
    @Column(name = "CONF_TS")
    private Timestamp confTs;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Column(name = "INITIATE_TS")
    private Timestamp initiateTs;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    
    @Column(nullable = false)
    private BigDecimal loc;
    @Column(name = "LOC_TYPE", nullable = false, length = 2)
    private String locType;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "PAYMENT_STATUS", length = 1)
    private String paymentStatus;
    @Column(nullable = false)
    private BigDecimal qty;
    @Column(name = "RESV_STATUS", nullable = false, length = 3)
    private String resvStatus;
    @Id
    @Column(name = "RMS_RESV_LOC")
    private BigDecimal rmsResvLoc;
    @Column(name = "RMS_RESV_LOC_TYPE", length = 2)
    private String rmsResvLocType;
    @Column(name = "RMS_RESV_QTY")
    private BigDecimal rmsResvQty;
    @Column(name = "VIRTUAL_WH")
    private BigDecimal virtualWH;

    public OmsCustOrdReserve() {
    }

    public OmsCustOrdReserve(Timestamp confTs, Timestamp createDatetime, String createdBy, BigDecimal fulfillOrderNo,
                             Timestamp initiateTs, String item, BigDecimal lineNo, BigDecimal loc, String locType,
                             BigDecimal omsCustOrdNo, String paymentStatus, BigDecimal qty, String resvStatus,
                             BigDecimal rmsResvLoc, String rmsResvLocType, BigDecimal rmsResvQty,BigDecimal virtualWH) {
        this.confTs = confTs;
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.fulfillOrderNo = fulfillOrderNo;
        this.initiateTs = initiateTs;
        this.item = item;
        this.lineNo = lineNo;
        this.loc = loc;
        this.locType = locType;
        this.omsCustOrdNo = omsCustOrdNo;
        this.paymentStatus = paymentStatus;
        this.qty = qty;
        this.resvStatus = resvStatus;
        this.rmsResvLoc = rmsResvLoc;
        this.rmsResvLocType = rmsResvLocType;
        this.rmsResvQty = rmsResvQty;
        this.virtualWH = virtualWH;
    }

    public Timestamp getConfTs() {
        return confTs;
    }

    public void setConfTs(Timestamp confTs) {
        this.confTs = confTs;
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

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public Timestamp getInitiateTs() {
        return initiateTs;
    }

    public void setInitiateTs(Timestamp initiateTs) {
        this.initiateTs = initiateTs;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getResvStatus() {
        return resvStatus;
    }

    public void setResvStatus(String resvStatus) {
        this.resvStatus = resvStatus;
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

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setVirtualWH(BigDecimal virtualWH) {
        this.virtualWH = virtualWH;
    }

    public BigDecimal getVirtualWH() {
        return virtualWH;
    }
}
