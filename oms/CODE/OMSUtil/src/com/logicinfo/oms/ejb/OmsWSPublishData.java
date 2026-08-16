package com.logicinfo.oms.ejb;

import java.io.Serializable;

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
@NamedQueries( { @NamedQuery(name = "OmsWSPublishData.findAll", query = "select ows from OmsWSPublishData ows ") })
@Table(name = "OMS_PUBLISH_WS_DATA")
public class OmsWSPublishData implements Serializable {

    private static final long serialVersionUID = -4464653926907002473L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQUENCE_ID_GENERATOR")
    @SequenceGenerator(sequenceName = "OMS_REPUBLISH_DATASEQ", allocationSize = 1, name = "SEQUENCE_ID_GENERATOR")
    @Column(name = "SEQ_NO")
    private Long sequenecNo;

    @Column(name = "APPLICATION_ID", nullable = false, length = 30)
    private String applicationId;

    @Column(name = "FIRST_ATTEMPT_DATETIME", nullable = false)
    private Timestamp firstAttemptDateTime;

    @Column(name = "TRANSACTION_KEY", nullable = false, length = 50)
    private String transactionKey;

    @Column(name = "XML_MSG", nullable = false)
    private String message;

    @Column(name = "WEB_SERVICE_ID", nullable = false)
    private String webServiceId;

    @Column(name = "ATTEMPT_CNT", nullable = false, length = 2)
    private Integer attemtCnt;

    @Column(name = "ERROR_MSG")
    private String errorMessage;

    @Column(name = "LAST_ATTEMPT_TIME", nullable = false)
    private Timestamp lastAttemptDateTme;

    @Column(name = "REPUBLISH_STATUS")
    private Character publishSatus = 'N';

    public OmsWSPublishData() {
        super();
    }

    public Long getSequenecNo() {
        return sequenecNo;
    }

    public void setSequenecNo(Long sequenecNo) {
        this.sequenecNo = sequenecNo;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Timestamp getFirstAttemptDateTime() {
        return firstAttemptDateTime;
    }

    public void setFirstAttemptDateTime(Timestamp firstAttemptDateTime) {
        this.firstAttemptDateTime = firstAttemptDateTime;
    }

    public String getTransactionKey() {
        return transactionKey;
    }

    public void setTransactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWebServiceId() {
        return webServiceId;
    }

    public void setWebServiceId(String webServiceId) {
        this.webServiceId = webServiceId;
    }

    public Integer getAttemtCnt() {
        return attemtCnt;
    }

    public void setAttemtCnt(Integer attemtCnt) {
        this.attemtCnt = attemtCnt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Timestamp getLastAttemptDateTme() {
        return lastAttemptDateTme;
    }

    public void setLastAttemptDateTme(Timestamp lastAttemptDateTme) {
        this.lastAttemptDateTme = lastAttemptDateTme;
    }

    public Character getPublishSatus() {
        return this.publishSatus;
    }

    public Character setPublishSatus(Character publishSatus) {
        return this.publishSatus = publishSatus;
    }
}
