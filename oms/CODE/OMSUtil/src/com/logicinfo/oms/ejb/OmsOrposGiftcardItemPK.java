package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposGiftcardItemPK implements Serializable {
    public BigDecimal capturedLineItemNo;
    public BigDecimal giftCardNumber;
    public String itemId;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposGiftcardItemPK() {
    }

    public OmsOrposGiftcardItemPK(BigDecimal capturedLineItemNo, BigDecimal giftCardNumber, String itemId,
                                  BigDecimal omsOrposCustOrderId) {
        this.capturedLineItemNo = capturedLineItemNo;
        this.giftCardNumber = giftCardNumber;
        this.itemId = itemId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposGiftcardItemPK) {
            final OmsOrposGiftcardItemPK otherOmsOrposGiftcardItemPK = (OmsOrposGiftcardItemPK)other;
            final boolean areEqual =
                (otherOmsOrposGiftcardItemPK.capturedLineItemNo.equals(capturedLineItemNo) && otherOmsOrposGiftcardItemPK.giftCardNumber.equals(giftCardNumber) &&
                 otherOmsOrposGiftcardItemPK.itemId.equals(itemId) &&
                 otherOmsOrposGiftcardItemPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    public void setCapturedLineItemNo(BigDecimal capturedLineItemNo) {
        this.capturedLineItemNo = capturedLineItemNo;
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
}
