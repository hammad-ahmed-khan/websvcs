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
@NamedQueries( { @NamedQuery(name = "OmsOrposGiftcardTender.findAll",
                             query = "select o from OmsOrposGiftcardTender o") })
@Table(name = "OMS_ORPOS_GIFTCARD_TENDER")
public class OmsOrposGiftcardTender implements Serializable {
    @Column(name = "AUTHORIZATION_CODE", nullable = false, length = 20)
    private String authorizationCode;
    @Column(name = "AUTHORIZATION_DATETIME")
    private Timestamp authorizationDatetime;
    @Column(name = "AUTHORIZATION_METHOD", length = 4)
    private String authorizationMethod;
    @Id
    @Column(name = "CARD_NUMBER", nullable = false, length = 20)
    private String cardNumber;
    @Column(name = "CREDIT_FLAG", length = 1)
    private String creditFlag;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "ORIGINAL_BALANCE")
    private BigDecimal originalBalance;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "REMAINING_BALANCE")
    private BigDecimal remainingBalance;
    @Column(name = "SETTLEMENT_DATA", length = 16)
    private String settlementData;

    public OmsOrposGiftcardTender() {
    }

    public OmsOrposGiftcardTender(String authorizationCode, Timestamp authorizationDatetime,
                                  String authorizationMethod, String cardNumber, String creditFlag, String entryMethod,
                                  BigDecimal omsOrposCustOrderId, BigDecimal originalBalance, BigDecimal paymentSeqNo,
                                  BigDecimal remainingBalance, String settlementData) {
        this.authorizationCode = authorizationCode;
        this.authorizationDatetime = authorizationDatetime;
        this.authorizationMethod = authorizationMethod;
        this.cardNumber = cardNumber;
        this.creditFlag = creditFlag;
        this.entryMethod = entryMethod;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.originalBalance = originalBalance;
        this.paymentSeqNo = paymentSeqNo;
        this.remainingBalance = remainingBalance;
        this.settlementData = settlementData;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCreditFlag() {
        return creditFlag;
    }

    public void setCreditFlag(String creditFlag) {
        this.creditFlag = creditFlag;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
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

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getSettlementData() {
        return settlementData;
    }

    public void setSettlementData(String settlementData) {
        this.settlementData = settlementData;
    }
}
