package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsFulfillMatrixExtDetailPK implements Serializable {
    public BigDecimal combinationId;
    public BigDecimal priority;

    public OmsFulfillMatrixExtDetailPK() {
    }

    public OmsFulfillMatrixExtDetailPK(BigDecimal combinationId, BigDecimal priority) {
        this.combinationId = combinationId;
        this.priority = priority;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsFulfillMatrixExtDetailPK) {
            final OmsFulfillMatrixExtDetailPK otherOmsFulfillMatrixExtDetailPK = (OmsFulfillMatrixExtDetailPK)other;
            final boolean areEqual =
                (otherOmsFulfillMatrixExtDetailPK.combinationId.equals(combinationId) && otherOmsFulfillMatrixExtDetailPK.priority.equals(priority));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public BigDecimal getPriority() {
        return priority;
    }

    public void setPriority(BigDecimal priority) {
        this.priority = priority;
    }
}
