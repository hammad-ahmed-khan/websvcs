package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposEmailPK implements Serializable {
    public BigDecimal contactSeq;
    public String emailAddress;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposEmailPK() {
    }

    public OmsOrposEmailPK(BigDecimal contactSeq, String emailAddress, BigDecimal omsOrposCustOrderId) {
        this.contactSeq = contactSeq;
        this.emailAddress = emailAddress;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposEmailPK) {
            final OmsOrposEmailPK otherOmsOrposEmailPK = (OmsOrposEmailPK)other;
            final boolean areEqual =
                (otherOmsOrposEmailPK.contactSeq.equals(contactSeq) && otherOmsOrposEmailPK.emailAddress.equals(emailAddress) &&
                 otherOmsOrposEmailPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
