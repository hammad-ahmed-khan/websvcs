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
@NamedQueries( { @NamedQuery(name = "OmsCustOrdLog.findAll", query = "select o from OmsCustOrdLog o"),
                 @NamedQuery(name = "OmsCustOrdLog.findOmsCancelId",
                             query = "select o.omsCancelId from OmsCustOrdLog o where o.omsCancelId=:omsCancelId"),
                 @NamedQuery(name = "OmsCustOrdLog.findlogSeqNo",
                             query = "select o.logSeqNo from OmsCustOrdLog o where o.omsCancelId=:omsCancelId"),
                 @NamedQuery(name = "OmsCustOrdLog.findMaxLogSeqNo",
                             query = "select max(o.logSeqNo) from OmsCustOrdLog o where o.omsCustOrdNo=:omsCustOrdNo")})
@Table(name = "OMS_CUST_ORD_LOG")
public class OmsCustOrdLog implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "EVENT_COMMENTS", length = 200)
    private String eventComments;
    @Column(name = "EVENT_ID", nullable = false, length = 2)
    private String eventId;
    @Id
    @Column(name = "LOG_SEQ_NO", nullable = false)
    @SequenceGenerator(name = "logSeq", sequenceName = "OMS_LOG_SEQ_NO_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logSeq")
    private BigDecimal logSeqNo;
    @Column(name = "OMS_CANCEL_ID")
    private BigDecimal omsCancelId;
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "OMS_DLV_CONF_ID")
    private BigDecimal omsDlvConfId;

    public OmsCustOrdLog() {
    }

    public OmsCustOrdLog(Timestamp createDatetime, String eventComments, String eventId, BigDecimal logSeqNo,
                         BigDecimal omsCancelId, BigDecimal omsCustOrdNo, BigDecimal omsDlvConfId) {
        this.createDatetime = createDatetime;
        this.eventComments = eventComments;
        this.eventId = eventId;
        this.logSeqNo = logSeqNo;
        this.omsCancelId = omsCancelId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsDlvConfId = omsDlvConfId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getEventComments() {
        return eventComments;
    }

    public void setEventComments(String eventComments) {
        this.eventComments = eventComments;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public BigDecimal getLogSeqNo() {
        return logSeqNo;
    }

    public void setLogSeqNo(BigDecimal logSeqNo) {
        this.logSeqNo = logSeqNo;
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

    public BigDecimal getOmsDlvConfId() {
        return omsDlvConfId;
    }

    public void setOmsDlvConfId(BigDecimal omsDlvConfId) {
        this.omsDlvConfId = omsDlvConfId;
    }
}
