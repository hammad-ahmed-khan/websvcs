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
@NamedQueries( { @NamedQuery(name = "OmsOrposGiftcertTender.findAll",
                             query = "select o from OmsOrposGiftcertTender o") })
@Table(name = "OMS_ORPOS_GIFTCERT_TENDER")
public class OmsOrposGiftcertTender implements Serializable {
    @Column(name = "CERTIFICATE_TYPE", length = 7)
    private String certificateType;
    @Id
    @Column(name = "GIFTCERT_SERIAL_NUMBER", nullable = false, length = 40)
    private String giftcertSerialNumber;
    @Column(name = "ISSUE_LOCATION_ID")
    private BigDecimal issueLocationId;
    @Column(name = "ISSUE_LOCATION_TYPE", length = 1)
    private String issueLocationType;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;

    public OmsOrposGiftcertTender() {
    }

    public OmsOrposGiftcertTender(String certificateType, String giftcertSerialNumber, BigDecimal issueLocationId,
                                  String issueLocationType, BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo) {
        this.certificateType = certificateType;
        this.giftcertSerialNumber = giftcertSerialNumber;
        this.issueLocationId = issueLocationId;
        this.issueLocationType = issueLocationType;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getGiftcertSerialNumber() {
        return giftcertSerialNumber;
    }

    public void setGiftcertSerialNumber(String giftcertSerialNumber) {
        this.giftcertSerialNumber = giftcertSerialNumber;
    }

    public BigDecimal getIssueLocationId() {
        return issueLocationId;
    }

    public void setIssueLocationId(BigDecimal issueLocationId) {
        this.issueLocationId = issueLocationId;
    }

    public String getIssueLocationType() {
        return issueLocationType;
    }

    public void setIssueLocationType(String issueLocationType) {
        this.issueLocationType = issueLocationType;
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
}
