package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsOrposGeoaddrPK implements Serializable {
    public BigDecimal geoaddrSeq;
    public BigDecimal omsOrposCustOrderId;

    public OmsOrposGeoaddrPK() {
    }

    public OmsOrposGeoaddrPK(BigDecimal geoaddrSeq, BigDecimal omsOrposCustOrderId) {
        this.geoaddrSeq = geoaddrSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsOrposGeoaddrPK) {
            final OmsOrposGeoaddrPK otherOmsOrposGeoaddrPK = (OmsOrposGeoaddrPK)other;
            final boolean areEqual =
                (otherOmsOrposGeoaddrPK.geoaddrSeq.equals(geoaddrSeq) && otherOmsOrposGeoaddrPK.omsOrposCustOrderId.equals(omsOrposCustOrderId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getGeoaddrSeq() {
        return geoaddrSeq;
    }

    public void setGeoaddrSeq(BigDecimal geoaddrSeq) {
        this.geoaddrSeq = geoaddrSeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
