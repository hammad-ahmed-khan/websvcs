package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposPayment.findAll", query = "select o from OmsOrposPayment o"), 
                  @NamedQuery(name = "OmsOrposPayment.findByOmsOrposCustOrdId", query = "select o from OmsOrposPayment o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name ="OmsOrposPayment.findMaxPaymentSeqNo", query="select max(o.paymentSeqNo) from OmsOrposPayment o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_PAYMENT")
@IdClass(OmsOrposPaymentPK.class)
public class OmsOrposPayment implements Serializable {
    @Column(name = "ACCOUNT_APR", length = 8)
    private String accountApr;
    @Column(name = "ACCOUNT_APR_TYPE", length = 1)
    private String accountAprType;
    @Column(name = "ACCOUNT_NUMBER", length = 17)
    private String accountNumber;
    @Column(name = "ADDITIONAL_SECURITY_INFO", length = 20)
    private String additionalSecurityInfo;
    @Column(name = "AGENT_NAME", length = 120)
    private String agentName;
    @Column(name = "ALTERNATE_AMOUNT")
    private BigDecimal alternateAmount;
    @Column(name = "ALTERNATE_CURRENCY_CODE", length = 3)
    private String alternateCurrencyCode;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(name = "AUTHORIZATION_CODE", length = 40)
    private String authorizationCode;
    @Column(name = "AUTHORIZATION_DATETIME")
    private Timestamp authorizationDatetime;
    @Column(name = "AUTHORIZATION_METHOD", length = 4)
    private String authorizationMethod;
    @Column(name = "BANK_ID", length = 20)
    private String bankId;
    @Column(name = "CARD_NUMBER", length = 20)
    private String cardNumber;
    @Column(name = "CARD_TOKEN", length = 100)
    private String cardToken;
    @Column(name = "CARD_TYPE", length = 9)
    private String cardType;
    @Column(name = "CERTIFICATE_TYPE", length = 7)
    private String certificateType;
    @Column(name = "CHECK_COUNT")
    private BigDecimal checkCount;
    @Column(name = "CHECK_NUMBER", length = 10)
    private String checkNumber;
    @Column(name = "CHECKTENDER_ENTRY_METHOD", length = 7)
    private String checktenderEntryMethod;
    @Column(name = "CHKTENDER_AUTHORIZATION_CODE", length = 20)
    private String chktenderAuthorizationCode;
    @Column(name = "CHKTENDER_AUTHORIZATION_METHOD", length = 4)
    private String chktenderAuthorizationMethod;
    @Column(name = "COUPON_NUMBER", length = 15)
    private String couponNumber;
    @Column(name = "COUPON_TYPE", length = 12)
    private String couponType;
    @Column(name = "COUPONTENDER_ENTRY_METHOD", length = 7)
    private String coupontenderEntryMethod;
    @Column(name = "CREDIT_FLAG", length = 1)
    private String creditFlag;
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;
    @Column(name = "CUSTOMER_PHONE_NUMBER", length = 30)
    private String customerPhoneNumber;
    @Column(name = "ECHECK_CONVERSION_CODE", length = 9)
    private String echeckConversionCode;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Column(name = "FIRST_NAME", length = 120)
    private String firstName;
    @Column(name = "GIFCARD_AUTHORIZATION_CODE", length = 20)
    private String gifcardAuthorizationCode;
    @Column(name = "GIFCARD_AUTHORIZATION_DATETIME")
    private Timestamp gifcardAuthorizationDatetime;
    @Column(name = "GIFCARD_AUTHORIZATION_METHOD", length = 4)
    private String gifcardAuthorizationMethod;
    @Column(name = "GIFCARD_ENTRY_METHOD", length = 7)
    private String gifcardEntryMethod;
    @Column(name = "GIFCARDSETTLEMENT_DATA", length = 16)
    private String gifcardsettlementData;
    @Column(name = "ISSUE_LOCATION_ID")
    private BigDecimal issueLocationId;
    @Column(name = "ISSUE_LOCATION_TYPE", length = 1)
    private String issueLocationType;
    @Column(name = "LAST_NAME", length = 120)
    private String lastName;
    @Column(name = "MASKED_ACCOUNT_NUMBER", length = 20)
    private String maskedAccountNumber;
    @Column(name = "MICR_NUMBER", length = 80)
    private String micrNumber;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "ORIGINAL_BALANCE")
    private BigDecimal originalBalance;
    @Id
    @Column(name = "PAYMENT_SEQ_NO", nullable = false)
    private BigDecimal paymentSeqNo;
    @Column(name = "PAYMENT_TYPE", nullable = false, length = 13)
    private String paymentType;
    @Column(name = "PERSONAL_ID_COUNTRY", length = 3)
    private String personalIdCountry;
    @Column(name = "PERSONAL_ID_EXPIRATION_DATE")
    private Timestamp personalIdExpirationDate;
    @Column(name = "PERSONAL_ID_ISSUER", length = 20)
    private String personalIdIssuer;
    @Column(name = "PERSONAL_ID_ISSUER_CO_CODE", length = 22)
    private String personalIdIssuerCoCode;
    @Column(name = "PERSONAL_ID_ISSUER_STATE_CODE", length = 5)
    private String personalIdIssuerStateCode;
    @Column(name = "PERSONAL_ID_NUMBER", length = 20)
    private String personalIdNumber;
    @Column(name = "PERSONAL_ID_STATE", length = 3)
    private String personalIdState;
    @Column(name = "PERSONAL_ID_SWIPED_FLAG", length = 1)
    private String personalIdSwipedFlag;
    @Column(name = "PERSONAL_ID_TYPE", length = 20)
    private String personalIdType;
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
    @Column(name = "PURCHASE_ORDER_NUMBER", length = 15)
    private String purchaseOrderNumber;
    @Column(name = "REMAINING_BALANCE")
    private BigDecimal remainingBalance;
    @Column(name = "SERIAL_NUMBER", length = 40)
    private String serialNumber;
    @Column(name = "SETTLEMENT_DATA", length = 16)
    private String settlementData;
    @Column(name = "SIGNATURE_DATA", length = 4000)
    private String signatureData;
    @Column(length = 6)
    private String state;
    @Column(name = "STORE_CREDIT_ID", length = 20)
    private String storeCreditId;
    @Column(name = "STRCRTENDER_CERTIFICATE_TYPE", length = 7)
    private String strcrtenderCertificateType;
    @Column(name = "STRCRTENDER_PERSONAL_ID_TYPE", length = 20)
    private String strcrtenderPersonalIdType;

    public OmsOrposPayment() {
    }

    public OmsOrposPayment(String accountApr, String accountAprType, String accountNumber,
                           String additionalSecurityInfo, String agentName, BigDecimal alternateAmount,
                           String alternateCurrencyCode, BigDecimal amount, String authorizationCode,
                           Timestamp authorizationDatetime, String authorizationMethod, String bankId,
                           String cardNumber, String cardToken, String cardType, String certificateType,
                           BigDecimal checkCount, String checkNumber, String checktenderEntryMethod,
                           String chktenderAuthorizationCode, String chktenderAuthorizationMethod, String couponNumber,
                           String couponType, String coupontenderEntryMethod, String creditFlag, String currencyCode,
                           String customerPhoneNumber, String echeckConversionCode, String entryMethod,
                           String firstName, String gifcardAuthorizationCode, Timestamp gifcardAuthorizationDatetime,
                           String gifcardAuthorizationMethod, String gifcardEntryMethod, String gifcardsettlementData,
                           BigDecimal issueLocationId, String issueLocationType, String lastName,
                           String maskedAccountNumber, String micrNumber, BigDecimal omsOrposCustOrderId,
                           BigDecimal originalBalance, BigDecimal paymentSeqNo, String paymentType,
                           String personalIdCountry, Timestamp personalIdExpirationDate, String personalIdIssuer,
                           String personalIdIssuerCoCode, String personalIdIssuerStateCode, String personalIdNumber,
                           String personalIdState, String personalIdSwipedFlag, String personalIdType,
                           BigDecimal prepaidBalance, String promotionApr, String promotionAprType,
                           String promotionDescription, String promotionDuration, String purchaseOrderNumber,
                           BigDecimal remainingBalance, String serialNumber, String settlementData,
                           String signatureData, String state, String storeCreditId, String strcrtenderCertificateType,
                           String strcrtenderPersonalIdType) {
        this.accountApr = accountApr;
        this.accountAprType = accountAprType;
        this.accountNumber = accountNumber;
        this.additionalSecurityInfo = additionalSecurityInfo;
        this.agentName = agentName;
        this.alternateAmount = alternateAmount;
        this.alternateCurrencyCode = alternateCurrencyCode;
        this.amount = amount;
        this.authorizationCode = authorizationCode;
        this.authorizationDatetime = authorizationDatetime;
        this.authorizationMethod = authorizationMethod;
        this.bankId = bankId;
        this.cardNumber = cardNumber;
        this.cardToken = cardToken;
        this.cardType = cardType;
        this.certificateType = certificateType;
        this.checkCount = checkCount;
        this.checkNumber = checkNumber;
        this.checktenderEntryMethod = checktenderEntryMethod;
        this.chktenderAuthorizationCode = chktenderAuthorizationCode;
        this.chktenderAuthorizationMethod = chktenderAuthorizationMethod;
        this.couponNumber = couponNumber;
        this.couponType = couponType;
        this.coupontenderEntryMethod = coupontenderEntryMethod;
        this.creditFlag = creditFlag;
        this.currencyCode = currencyCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.echeckConversionCode = echeckConversionCode;
        this.entryMethod = entryMethod;
        this.firstName = firstName;
        this.gifcardAuthorizationCode = gifcardAuthorizationCode;
        this.gifcardAuthorizationDatetime = gifcardAuthorizationDatetime;
        this.gifcardAuthorizationMethod = gifcardAuthorizationMethod;
        this.gifcardEntryMethod = gifcardEntryMethod;
        this.gifcardsettlementData = gifcardsettlementData;
        this.issueLocationId = issueLocationId;
        this.issueLocationType = issueLocationType;
        this.lastName = lastName;
        this.maskedAccountNumber = maskedAccountNumber;
        this.micrNumber = micrNumber;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.originalBalance = originalBalance;
        this.paymentSeqNo = paymentSeqNo;
        this.paymentType = paymentType;
        this.personalIdCountry = personalIdCountry;
        this.personalIdExpirationDate = personalIdExpirationDate;
        this.personalIdIssuer = personalIdIssuer;
        this.personalIdIssuerCoCode = personalIdIssuerCoCode;
        this.personalIdIssuerStateCode = personalIdIssuerStateCode;
        this.personalIdNumber = personalIdNumber;
        this.personalIdState = personalIdState;
        this.personalIdSwipedFlag = personalIdSwipedFlag;
        this.personalIdType = personalIdType;
        this.prepaidBalance = prepaidBalance;
        this.promotionApr = promotionApr;
        this.promotionAprType = promotionAprType;
        this.promotionDescription = promotionDescription;
        this.promotionDuration = promotionDuration;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.remainingBalance = remainingBalance;
        this.serialNumber = serialNumber;
        this.settlementData = settlementData;
        this.signatureData = signatureData;
        this.state = state;
        this.storeCreditId = storeCreditId;
        this.strcrtenderCertificateType = strcrtenderCertificateType;
        this.strcrtenderPersonalIdType = strcrtenderPersonalIdType;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAdditionalSecurityInfo() {
        return additionalSecurityInfo;
    }

    public void setAdditionalSecurityInfo(String additionalSecurityInfo) {
        this.additionalSecurityInfo = additionalSecurityInfo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public BigDecimal getAlternateAmount() {
        return alternateAmount;
    }

    public void setAlternateAmount(BigDecimal alternateAmount) {
        this.alternateAmount = alternateAmount;
    }

    public String getAlternateCurrencyCode() {
        return alternateCurrencyCode;
    }

    public void setAlternateCurrencyCode(String alternateCurrencyCode) {
        this.alternateCurrencyCode = alternateCurrencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
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

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public BigDecimal getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(BigDecimal checkCount) {
        this.checkCount = checkCount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public String getChecktenderEntryMethod() {
        return checktenderEntryMethod;
    }

    public void setChecktenderEntryMethod(String checktenderEntryMethod) {
        this.checktenderEntryMethod = checktenderEntryMethod;
    }

    public String getChktenderAuthorizationCode() {
        return chktenderAuthorizationCode;
    }

    public void setChktenderAuthorizationCode(String chktenderAuthorizationCode) {
        this.chktenderAuthorizationCode = chktenderAuthorizationCode;
    }

    public String getChktenderAuthorizationMethod() {
        return chktenderAuthorizationMethod;
    }

    public void setChktenderAuthorizationMethod(String chktenderAuthorizationMethod) {
        this.chktenderAuthorizationMethod = chktenderAuthorizationMethod;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCoupontenderEntryMethod() {
        return coupontenderEntryMethod;
    }

    public void setCoupontenderEntryMethod(String coupontenderEntryMethod) {
        this.coupontenderEntryMethod = coupontenderEntryMethod;
    }

    public String getCreditFlag() {
        return creditFlag;
    }

    public void setCreditFlag(String creditFlag) {
        this.creditFlag = creditFlag;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGifcardAuthorizationCode() {
        return gifcardAuthorizationCode;
    }

    public void setGifcardAuthorizationCode(String gifcardAuthorizationCode) {
        this.gifcardAuthorizationCode = gifcardAuthorizationCode;
    }

    public Timestamp getGifcardAuthorizationDatetime() {
        return gifcardAuthorizationDatetime;
    }

    public void setGifcardAuthorizationDatetime(Timestamp gifcardAuthorizationDatetime) {
        this.gifcardAuthorizationDatetime = gifcardAuthorizationDatetime;
    }

    public String getGifcardAuthorizationMethod() {
        return gifcardAuthorizationMethod;
    }

    public void setGifcardAuthorizationMethod(String gifcardAuthorizationMethod) {
        this.gifcardAuthorizationMethod = gifcardAuthorizationMethod;
    }

    public String getGifcardEntryMethod() {
        return gifcardEntryMethod;
    }

    public void setGifcardEntryMethod(String gifcardEntryMethod) {
        this.gifcardEntryMethod = gifcardEntryMethod;
    }

    public String getGifcardsettlementData() {
        return gifcardsettlementData;
    }

    public void setGifcardsettlementData(String gifcardsettlementData) {
        this.gifcardsettlementData = gifcardsettlementData;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public void setMaskedAccountNumber(String maskedAccountNumber) {
        this.maskedAccountNumber = maskedAccountNumber;
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

    public BigDecimal getOriginalBalance() {
        return originalBalance;
    }

    public void setOriginalBalance(BigDecimal originalBalance) {
        this.originalBalance = originalBalance;
    }

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
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

    public String getPersonalIdState() {
        return personalIdState;
    }

    public void setPersonalIdState(String personalIdState) {
        this.personalIdState = personalIdState;
    }

    public String getPersonalIdSwipedFlag() {
        return personalIdSwipedFlag;
    }

    public void setPersonalIdSwipedFlag(String personalIdSwipedFlag) {
        this.personalIdSwipedFlag = personalIdSwipedFlag;
    }

    public String getPersonalIdType() {
        return personalIdType;
    }

    public void setPersonalIdType(String personalIdType) {
        this.personalIdType = personalIdType;
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

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public String getStrcrtenderCertificateType() {
        return strcrtenderCertificateType;
    }

    public void setStrcrtenderCertificateType(String strcrtenderCertificateType) {
        this.strcrtenderCertificateType = strcrtenderCertificateType;
    }

    public String getStrcrtenderPersonalIdType() {
        return strcrtenderPersonalIdType;
    }

    public void setStrcrtenderPersonalIdType(String strcrtenderPersonalIdType) {
        this.strcrtenderPersonalIdType = strcrtenderPersonalIdType;
    }
}
