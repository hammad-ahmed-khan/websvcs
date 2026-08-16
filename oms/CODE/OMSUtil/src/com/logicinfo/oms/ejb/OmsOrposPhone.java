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
@NamedQueries( { @NamedQuery(name = "OmsOrposPhone.findAll", query = "select o from OmsOrposPhone o"),
                 @NamedQuery(name = "OmsOrposPhone.findByOmsOrposCustOrdId", query = "select o from OmsOrposPhone o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposPhone.findByOmsOrposContactSeq", query = "select o from OmsOrposPhone o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.contactSeq=:contactSeq")})
@Table(name = "OMS_ORPOS_PHONE")
@IdClass(OmsOrposPhonePK.class)
public class OmsOrposPhone implements Serializable {
    @Id
    @Column(name = "CONTACT_SEQ", nullable = false)
    private BigDecimal contactSeq;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PHONE_EXTENSION", length = 30)
    private String phoneExtension;
    @Column(name = "PHONE_ID")
    private BigDecimal phoneId;
    @Id
    @Column(name = "PHONE_NUMBER", nullable = false, length = 30)
    private String phoneNumber;
    @Column(name = "PHONE_TYPE", length = 6)
    private String phoneType;
    @Column(name = "PRIMARY_PHONE_IND", nullable = false, length = 1)
    private String primaryPhoneInd;

    public OmsOrposPhone() {
    }

    public OmsOrposPhone(BigDecimal contactSeq, BigDecimal omsOrposCustOrderId, String phoneExtension,
                         BigDecimal phoneId, String phoneNumber, String phoneType, String primaryPhoneInd) {
        this.contactSeq = contactSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.phoneExtension = phoneExtension;
        this.phoneId = phoneId;
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
        this.primaryPhoneInd = primaryPhoneInd;
    }

    public BigDecimal getContactSeq() {
        return contactSeq;
    }

    public void setContactSeq(BigDecimal contactSeq) {
        this.contactSeq = contactSeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getPhoneExtension() {
        return phoneExtension;
    }

    public void setPhoneExtension(String phoneExtension) {
        this.phoneExtension = phoneExtension;
    }

    public BigDecimal getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(BigDecimal phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPrimaryPhoneInd() {
        return primaryPhoneInd;
    }

    public void setPrimaryPhoneInd(String primaryPhoneInd) {
        this.primaryPhoneInd = primaryPhoneInd;
    }
}
