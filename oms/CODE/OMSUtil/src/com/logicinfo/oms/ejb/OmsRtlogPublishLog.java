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
@NamedQueries( { @NamedQuery(name = "OmsRtlogPublishLog.findAll", query = "select o from OmsRtlogPublishLog o"),
                 @NamedQuery(name = "OmsRtlogPublishLog.findByOmsCustOrderNo", query = "select o from OmsRtlogPublishLog o where o.omsCustOrdNo=:omsCustOrdNo")
                 })
@Table(name = "OMS_RTLOG_PUBLISH_LOG")
public class OmsRtlogPublishLog implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "ERROR_MESSAGE", length = 250)
    private String errorMessage;
    @Column(name = "FULFILL_ORDER_NO")
    private BigDecimal fulfillOrderNo;
    @Column(name ="ITEM", nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    private BigDecimal location;
    @Column(name = "OMS_CANCEL_ID")
    private BigDecimal omsCancelId;
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "OMS_RTLOG_PUB_SEQ_NO", nullable = false)
    @SequenceGenerator(name = "rtLogPubSeq", sequenceName = "OMS_RTLOG_PUB_SEQ_NO_SEQ", allocationSize = 1,
                       initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rtLogPubSeq")
    private BigDecimal omsRtlogPubSeqNo;
    @Column(name = "PUBLISHED_IND", nullable = false, length = 1)
    private String publishedInd;
    @Column(name = "QTY", nullable = false)
    private BigDecimal qty;
    @Column(name = "TRAN_TYPE", nullable = false, length = 3)
    private String tranType;
    
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    public OmsRtlogPublishLog() {
    }

    public OmsRtlogPublishLog(Timestamp createDatetime, String errorMessage, BigDecimal fulfillOrderNo, String item,
                              Timestamp lastUpdateDatetime, BigDecimal location, BigDecimal omsCancelId,
                              BigDecimal omsCustOrdNo, BigDecimal omsRtlogPubSeqNo, String publishedInd,
                              BigDecimal qty, String tranType) {
        this.createDatetime = createDatetime;
        this.errorMessage = errorMessage;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.location = location;
        this.omsCancelId = omsCancelId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsRtlogPubSeqNo = omsRtlogPubSeqNo;
        this.publishedInd = publishedInd;
        this.qty = qty;
        this.tranType = tranType;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
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

    public BigDecimal getOmsRtlogPubSeqNo() {
        return omsRtlogPubSeqNo;
    }

    public void setOmsRtlogPubSeqNo(BigDecimal omsRtlogPubSeqNo) {
        this.omsRtlogPubSeqNo = omsRtlogPubSeqNo;
    }

    public String getPublishedInd() {
        return publishedInd;
    }

    public void setPublishedInd(String publishedInd) {
        this.publishedInd = publishedInd;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }
    
    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }
}
