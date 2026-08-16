package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposPhonePK implements Serializable {
    public BigDecimal contactSeq;
    public BigDecimal omsOrposCustOrderId;
    public String phoneNumber;

    public OmsOrposPhonePK() {
    }

    public OmsOrposPhonePK(BigDecimal contactSeq, BigDecimal omsOrposCustOrderId, String phoneNumber) {
        this.contactSeq = contactSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.phoneNumber = phoneNumber;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposPhonePK) {
            final OmsOrposPhonePK otherOmsOrposPhonePK = (OmsOrposPhonePK)other;
            final boolean areEqual =
                (otherOmsOrposPhonePK.contactSeq.equals(contactSeq) && otherOmsOrposPhonePK.omsOrposCustOrderId.equals(omsOrposCustOrderId) &&
                 otherOmsOrposPhonePK.phoneNumber.equals(phoneNumber));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
