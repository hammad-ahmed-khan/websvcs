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
@NamedQueries( { @NamedQuery(name = "OmsOrposCheckTender.findAll", query = "select o from OmsOrposCheckTender o") })
@Table(name = "OMS_ORPOS_CHECK_TENDER")
@IdClass(OmsOrposCheckTenderPK.class)
public class OmsOrposCheckTender implements Serializable {
    @Id
    @Column(name = "ACCOUNT_NUMBER", nullable = false, length = 17)
    private String accountNumber;
    @Column(name = "AUTHORIZATION_CODE", nullable = false, length = 20)
    private String authorizationCode;
    @Column(name = "AUTHORIZATION_METHOD", length = 4)
    private String authorizationMethod;
    @Id
    @Column(name = "BANK_ID", nullable = false, length = 20)
    private String bankId;
    @Column(name = "CHECK_NUMBER", nullable = false, length = 10)
    private String checkNumber;
    @Column(name = "CUSTOMER_PHONE_NUMBER", length = 30)
    private String customerPhoneNumber;
    @Column(name = "ECHECK_CONVERSION_CODE", length = 9)
    private String echeckConversionCode;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Id
    @Column(name = "MICR_NUMBER", nullable = false, length = 80)
    private String micrNumber;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "PERSONAL_ID_ISSUER", length = 20)
    private String personalIdIssuer;
    @Column(name = "PERSONAL_ID_ISSUER_CO_CODE", length = 22)
    private String personalIdIssuerCoCode;
    @Column(name = "PERSONAL_ID_ISSUER_STATE_CODE", length = 5)
    private String personalIdIssuerStateCode;
    @Column(name = "PERSONAL_ID_NUMBER", length = 20)
    private String personalIdNumber;
    @Column(name = "PERSONAL_ID_SWIPED_FLAG", length = 1)
    private String personalIdSwipedFlag;

    public OmsOrposCheckTender() {
    }

    public OmsOrposCheckTender(String accountNumber, String authorizationCode, String authorizationMethod,
                               String bankId, String checkNumber, String customerPhoneNumber,
                               String echeckConversionCode, String entryMethod, String micrNumber,
                               BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo, String personalIdIssuer,
                               String personalIdIssuerCoCode, String personalIdIssuerStateCode,
                               String personalIdNumber, String personalIdSwipedFlag) {
        this.accountNumber = accountNumber;
        this.authorizationCode = authorizationCode;
        this.authorizationMethod = authorizationMethod;
        this.bankId = bankId;
        this.checkNumber = checkNumber;
        this.customerPhoneNumber = customerPhoneNumber;
        this.echeckConversionCode = echeckConversionCode;
        this.entryMethod = entryMethod;
        this.micrNumber = micrNumber;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.personalIdIssuer = personalIdIssuer;
        this.personalIdIssuerCoCode = personalIdIssuerCoCode;
        this.personalIdIssuerStateCode = personalIdIssuerStateCode;
        this.personalIdNumber = personalIdNumber;
        this.personalIdSwipedFlag = personalIdSwipedFlag;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getAuthorizationMethod() {
        return authorizationMethod;
    }

    public void setAuthorizationMethod(String authorizationMethod) {
        this.authorizationMethod = authorizationMethod;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getEcheckConversionCode() {
        return echeckConversionCode;
    }

    public void setEcheckConversionCode(String echeckConversionCode) {
        this.echeckConversionCode = echeckConversionCode;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
    }

    public String getMicrNumber() {
        return micrNumber;
    }

    public void setMicrNumber(String micrNumber) {
        this.micrNumber = micrNumber;
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

    public String getPersonalIdIssuer() {
        return personalIdIssuer;
    }

    public void setPersonalIdIssuer(String personalIdIssuer) {
        this.personalIdIssuer = personalIdIssuer;
    }

    public String getPersonalIdIssuerCoCode() {
        return personalIdIssuerCoCode;
    }

    public void setPersonalIdIssuerCoCode(String personalIdIssuerCoCode) {
        this.personalIdIssuerCoCode = personalIdIssuerCoCode;
    }

    public String getPersonalIdIssuerStateCode() {
        return personalIdIssuerStateCode;
    }

    public void setPersonalIdIssuerStateCode(String personalIdIssuerStateCode) {
        this.personalIdIssuerStateCode = personalIdIssuerStateCode;
    }

    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    public void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    public String getPersonalIdSwipedFlag() {
        return personalIdSwipedFlag;
    }

    public void setPersonalIdSwipedFlag(String personalIdSwipedFlag) {
        this.personalIdSwipedFlag = personalIdSwipedFlag;
    }
}
