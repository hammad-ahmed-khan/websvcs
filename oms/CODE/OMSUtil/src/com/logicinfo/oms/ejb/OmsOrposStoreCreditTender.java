package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposStoreCreditTender.findAll",
                             query = "select o from OmsOrposStoreCreditTender o") })
@Table(name = "OMS_ORPOS_STORE_CREDIT_TENDER")
public class OmsOrposStoreCreditTender implements Serializable {
    @Column(name = "CERTIFICATE_TYPE", length = 7)
    private String certificateType;
    @Column(name = "FIRST_NAME", length = 120)
    private String firstName;
    @Column(name = "LAST_NAME", length = 120)
    private String lastName;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "PERSONAL_ID_TYPE", length = 20)
    private String personalIdType;
    @Column(length = 6)
    private String state;
    @Id
    @Column(name = "STORE_CREDIT_ID", nullable = false, length = 20)
    private String storeCreditId;

    public OmsOrposStoreCreditTender() {
    }

    public OmsOrposStoreCreditTender(String certificateType, String firstName, String lastName,
                                     BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo, String personalIdType,
                                     String state, String storeCreditId) {
        this.certificateType = certificateType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.personalIdType = personalIdType;
        this.state = state;
        this.storeCreditId = storeCreditId;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getPersonalIdType() {
        return personalIdType;
    }

    public void setPersonalIdType(String personalIdType) {
        this.personalIdType = personalIdType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStoreCreditId() {
        return storeCreditId;
    }

    public void setStoreCreditId(String storeCreditId) {
        this.storeCreditId = storeCreditId;
    }
}
