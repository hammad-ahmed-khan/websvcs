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
@NamedQueries( { @NamedQuery(name = "OmsOrposGiftcardItem.findAll", query = "select o from OmsOrposGiftcardItem o"),
                 @NamedQuery(name = "OmsOrposGiftcardItem.findByOmsOrposCustOrdId", query = "select o from OmsOrposGiftcardItem o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_GIFTCARD_ITEM")
@IdClass(OmsOrposGiftcardItemPK.class)
public class OmsOrposGiftcardItem implements Serializable {
    @Column(name = "AUTHORIZATION_CODE", nullable = false, length = 20)
    private String authorizationCode;
    @Column(name = "AUTHORIZATION_DATETIME")
    private Timestamp authorizationDatetime;
    @Id
    @Column(name = "CAPTURED_LINE_ITEM_NO", nullable = false)
    private BigDecimal capturedLineItemNo;
    @Column(name = "CURRENT_BALANCE", nullable = false)
    private BigDecimal currentBalance;
    @Id
    @Column(name = "GIFT_CARD_NUMBER", nullable = false)
    private BigDecimal giftCardNumber;
    @Id
    @Column(name = "ITEM_ID", nullable = false, length = 25)
    private String itemId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "ORIGINAL_BALANCE")
    private BigDecimal originalBalance;
    @Column(name = "REQUEST_TYPE", nullable = false, length = 6)
    private String requestType;
    @Column(name = "SETTLEMENT_DATA", length = 16)
    private String settlementData;

    public OmsOrposGiftcardItem() {
    }

    public OmsOrposGiftcardItem(String authorizationCode, Timestamp authorizationDatetime,
                                BigDecimal capturedLineItemNo, BigDecimal currentBalance, BigDecimal giftCardNumber,
                                String itemId, BigDecimal omsOrposCustOrderId, BigDecimal originalBalance,
                                String requestType, String settlementData) {
        this.authorizationCode = authorizationCode;
        this.authorizationDatetime = authorizationDatetime;
        this.capturedLineItemNo = capturedLineItemNo;
        this.currentBalance = currentBalance;
        this.giftCardNumber = giftCardNumber;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.originalBalance = originalBalance;
        this.requestType = requestType;
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

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getGiftCardNumber() {
        return giftCardNumber;
    }

    public void setGiftCardNumber(BigDecimal giftCardNumber) {
        this.giftCardNumber = giftCardNumber;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getSettlementData() {
        return settlementData;
    }

    public void setSettlementData(String settlementData) {
        this.settlementData = settlementData;
    }
}
