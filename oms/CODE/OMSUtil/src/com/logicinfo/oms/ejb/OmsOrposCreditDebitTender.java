package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCreditDebitTender.findAll",
                             query = "select o from OmsOrposCreditDebitTender o") })
@Table(name = "OMS_ORPOS_CREDIT_DEBIT_TENDER")
public class OmsOrposCreditDebitTender implements Serializable {
    @Column(name = "ACCOUNT_APR", length = 8)
    private String accountApr;
    @Column(name = "ACCOUNT_APR_TYPE", length = 1)
    private String accountAprType;
    @Column(name = "ADDITIONAL_SECURITY_INFO", length = 20)
    private String additionalSecurityInfo;
    @Column(name = "AUTHORIZATION_CODE", nullable = false, length = 40)
    private String authorizationCode;
    @Column(name = "AUTHORIZATION_DATETIME")
    private Timestamp authorizationDatetime;
    @Column(name = "AUTHORIZATION_METHOD", length = 4)
    private String authorizationMethod;
    @Column(name = "CARD_TOKEN", length = 100)
    private String cardToken;
    @Column(name = "CARD_TYPE", length = 9)
    private String cardType;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Id
    @Column(name = "MASKED_ACCOUNT_NUMBER", nullable = false, length = 20)
    private String maskedAccountNumber;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "PERSONAL_ID_COUNTRY", length = 3)
    private String personalIdCountry;
    @Column(name = "PERSONAL_ID_EXPIRATION_DATE")
    private Timestamp personalIdExpirationDate;
    @Column(name = "PERSONAL_ID_STATE", length = 3)
    private String personalIdState;
    @Column(name = "PREPAID_BALANCE")
    private BigDecimal prepaidBalance;
    @Column(name = "PROMOTION_APR", length = 8)
    private String promotionApr;
    @Column(name = "PROMOTION_APR_TYPE", length = 1)
    private String promotionAprType;
    @Column(name = "PROMOTION_DESCRIPTION", length = 60)
    private String promotionDescription;
    @Column(name = "PROMOTION_DURATION", length = 40)
    private String promotionDuration;
    @Column(name = "SETTLEMENT_DATA", length = 16)
    private String settlementData;
    @Column(name = "SIGNATURE_DATA", length = 200)
    private String signatureData;

    public OmsOrposCreditDebitTender() {
    }

    public OmsOrposCreditDebitTender(String accountApr, String accountAprType, String additionalSecurityInfo,
                                     String authorizationCode, Timestamp authorizationDatetime,
                                     String authorizationMethod, String cardToken, String cardType, String entryMethod,
                                     String maskedAccountNumber, BigDecimal omsOrposCustOrderId,
                                     BigDecimal paymentSeqNo, String personalIdCountry,
                                     Timestamp personalIdExpirationDate, String personalIdState,
                                     BigDecimal prepaidBalance, String promotionApr, String promotionAprType,
                                     String promotionDescription, String promotionDuration, String settlementData,
                                     String signatureData) {
        this.accountApr = accountApr;
        this.accountAprType = accountAprType;
        this.additionalSecurityInfo = additionalSecurityInfo;
        this.authorizationCode = authorizationCode;
        this.authorizationDatetime = authorizationDatetime;
        this.authorizationMethod = authorizationMethod;
        this.cardToken = cardToken;
        this.cardType = cardType;
        this.entryMethod = entryMethod;
        this.maskedAccountNumber = maskedAccountNumber;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.personalIdCountry = personalIdCountry;
        this.personalIdExpirationDate = personalIdExpirationDate;
        this.personalIdState = personalIdState;
        this.prepaidBalance = prepaidBalance;
        this.promotionApr = promotionApr;
        this.promotionAprType = promotionAprType;
        this.promotionDescription = promotionDescription;
        this.promotionDuration = promotionDuration;
        this.settlementData = settlementData;
        this.signatureData = signatureData;
    }

    public String getAccountApr() {
        return accountApr;
    }

    public void setAccountApr(String accountApr) {
        this.accountApr = accountApr;
    }

    public String getAccountAprType() {
        return accountAprType;
    }

    public void setAccountAprType(String accountAprType) {
        this.accountAprType = accountAprType;
    }

    public String getAdditionalSecurityInfo() {
        return additionalSecurityInfo;
    }

    public void setAdditionalSecurityInfo(String additionalSecurityInfo) {
        this.additionalSecurityInfo = additionalSecurityInfo;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public Timestamp getAuthorizationDatetime() {
        return authorizationDatetime;
    }

    public void setAuthorizationDatetime(Timestamp authorizationDatetime) {
        this.authorizationDatetime = authorizationDatetime;
    }

    public String getAuthorizationMethod() {
        return authorizationMethod;
    }

    public void setAuthorizationMethod(String authorizationMethod) {
        this.authorizationMethod = authorizationMethod;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public void setMaskedAccountNumber(String maskedAccountNumber) {
        this.maskedAccountNumber = maskedAccountNumber;
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

    public String getPersonalIdCountry() {
        return personalIdCountry;
    }

    public void setPersonalIdCountry(String personalIdCountry) {
        this.personalIdCountry = personalIdCountry;
    }

    public Timestamp getPersonalIdExpirationDate() {
        return personalIdExpirationDate;
    }

    public void setPersonalIdExpirationDate(Timestamp personalIdExpirationDate) {
        this.personalIdExpirationDate = personalIdExpirationDate;
    }

    public String getPersonalIdState() {
        return personalIdState;
    }

    public void setPersonalIdState(String personalIdState) {
        this.personalIdState = personalIdState;
    }

    public BigDecimal getPrepaidBalance() {
        return prepaidBalance;
    }

    public void setPrepaidBalance(BigDecimal prepaidBalance) {
        this.prepaidBalance = prepaidBalance;
    }

    public String getPromotionApr() {
        return promotionApr;
    }

    public void setPromotionApr(String promotionApr) {
        this.promotionApr = promotionApr;
    }

    public String getPromotionAprType() {
        return promotionAprType;
    }

    public void setPromotionAprType(String promotionAprType) {
        this.promotionAprType = promotionAprType;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public String getPromotionDuration() {
        return promotionDuration;
    }

    public void setPromotionDuration(String promotionDuration) {
        this.promotionDuration = promotionDuration;
    }

    public String getSettlementData() {
        return settlementData;
    }

    public void setSettlementData(String settlementData) {
        this.settlementData = settlementData;
    }

    public String getSignatureData() {
        return signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }
}
