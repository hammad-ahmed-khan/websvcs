package com.extra.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsCustOrdReserve {

    private BigDecimal combinationId;

    private Timestamp confTs;

    private Timestamp createDatetime;

    private String createdBy;

    private BigDecimal fulfillOrderNo;

    private Timestamp initiateTs;

    private String item;

    private BigDecimal lineNo;
    
    private BigDecimal loc;

    private String locType;

    private BigDecimal omsCustOrdNo;

    private String paymentStatus;

    private BigDecimal qty;

    private String resvStatus;

    private BigDecimal rmsResvLoc;

    private String rmsResvLocType;

    private BigDecimal rmsResvQty;

    private BigDecimal virtualWH;

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
