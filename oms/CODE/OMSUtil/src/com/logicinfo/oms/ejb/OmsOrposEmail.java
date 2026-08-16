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
@NamedQueries( { @NamedQuery(name = "OmsOrposEmail.findAll", query = "select o from OmsOrposEmail o") ,
                 @NamedQuery(name = "OmsOrposEmail.findByOmsOrposCustOrdId", query = "select o from OmsOrposEmail o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposEmail.findByOmsOrposContactSeq", query = "select o from OmsOrposEmail o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.contactSeq=:contactSeq")
                 })
@Table(name = "OMS_ORPOS_EMAIL")
@IdClass(OmsOrposEmailPK.class)
public class OmsOrposEmail implements Serializable {
    @Id
    @Column(name = "CONTACT_SEQ", nullable = false)
    private BigDecimal contactSeq;
    @Id
    @Column(name = "EMAIL_ADDRESS", nullable = false, length = 64)
    private String emailAddress;
    @Column(name = "EMAIL_ID")
    private BigDecimal emailId;
    @Column(name = "EMAIL_TYPE", length = 5)
    private String emailType;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PRIMARY_EMAIL_IND", nullable = false, length = 1)
    private String primaryEmailInd;

    public OmsOrposEmail() {
    }

    public OmsOrposEmail(BigDecimal contactSeq, String emailAddress, BigDecimal emailId, String emailType,
                         BigDecimal omsOrposCustOrderId, String primaryEmailInd) {
        this.contactSeq = contactSeq;
        this.emailAddress = emailAddress;
        this.emailId = emailId;
        this.emailType = emailType;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.primaryEmailInd = primaryEmailInd;
    }

    public BigDecimal getContactSeq() {
        return contactSeq;
    }

    public void setContactSeq(BigDecimal contactSeq) {
        this.contactSeq = contactSeq;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public BigDecimal getEmailId() {
        return emailId;
    }

    public void setEmailId(BigDecimal emailId) {
        this.emailId = emailId;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getPrimaryEmailInd() {
        return primaryEmailInd;
    }

    public void setPrimaryEmailInd(String primaryEmailInd) {
        this.primaryEmailInd = primaryEmailInd;
    }
}
