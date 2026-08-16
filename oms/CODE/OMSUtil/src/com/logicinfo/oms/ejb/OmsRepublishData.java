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
@NamedQueries( { @NamedQuery(name = "OmsRepublishData.findAll", query = "select o from OmsRepublishData o") })
@Table(name = "OMS_REPUBLISH_DATA")
public class OmsRepublishData implements Serializable {
    @Column(name = "APPLICATION_ID", nullable = false, length = 30)
    private String applicationId;
    @Column(name = "ATTEMPT_CNT", nullable = false)
    private BigDecimal attemptCnt;
    @Column(name = "ERROR_MSG", nullable = false, length = 300)
    private String errorMsg;
    @Column(name = "FIRST_ATTEMPT_DATETIME", nullable = false)
    private Timestamp firstAttemptDatetime;
    @Column(name = "LAST_ATTEMPT_TIME", nullable = false)
    private Timestamp lastAttemptTime;
    @Column(name = "REPUBLISH_STATUS", length = 1)
    private String republishStatus;
    @Id
    @SequenceGenerator( name = "omsRepublishDataSeq", sequenceName = "OMS_REPUBLISH_DATASEQ", allocationSize = 1, initialValue = 1 ) 
     @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsRepublishDataSeq" )
    @Column(name = "SEQ_NO", nullable = false)
    private BigDecimal seqNo;
    @Column(name = "TRANSACTION_KEY", nullable = false, length = 30)
    private String transactionKey;
    @Column(name = "WEB_SERVICE_ID", nullable = false)
    private String webServiceId;
    @Column(name = "XML_MSG", nullable = false)
    private String xmlMsg;

    public OmsRepublishData() {
    }

    public OmsRepublishData(String applicationId, BigDecimal attemptCnt, String errorMsg,
                            Timestamp firstAttemptDatetime, Timestamp lastAttemptTime, String republishStatus,
                            BigDecimal seqNo, String transactionKey, String webServiceId, String xmlMsg) {
        this.applicationId = applicationId;
        this.attemptCnt = attemptCnt;
        this.errorMsg = errorMsg;
        this.firstAttemptDatetime = firstAttemptDatetime;
        this.lastAttemptTime = lastAttemptTime;
        this.republishStatus = republishStatus;
        this.seqNo = seqNo;
        this.transactionKey = transactionKey;
        this.webServiceId = webServiceId;
        this.xmlMsg = xmlMsg;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public BigDecimal getAttemptCnt() {
        return attemptCnt;
    }

    public void setAttemptCnt(BigDecimal attemptCnt) {
        this.attemptCnt = attemptCnt;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Timestamp getFirstAttemptDatetime() {
        return firstAttemptDatetime;
    }

    public void setFirstAttemptDatetime(Timestamp firstAttemptDatetime) {
        this.firstAttemptDatetime = firstAttemptDatetime;
    }

    public Timestamp getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(Timestamp lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public String getRepublishStatus() {
        return republishStatus;
    }

    public void setRepublishStatus(String republishStatus) {
        this.republishStatus = republishStatus;
    }

    public BigDecimal getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(BigDecimal seqNo) {
        this.seqNo = seqNo;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public String getWebServiceId() {
        return webServiceId;
    }

    public void setWebServiceId(String webServiceId) {
        this.webServiceId = webServiceId;
    }

    public String getXmlMsg() {
        return xmlMsg;
    }

    public void setXmlMsg(String xmlMsg) {
        this.xmlMsg = xmlMsg;
    }
}
