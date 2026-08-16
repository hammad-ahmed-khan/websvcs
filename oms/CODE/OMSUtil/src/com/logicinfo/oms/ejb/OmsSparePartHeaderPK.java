package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsSparePartHeaderPK implements Serializable {
    public String sequenceId;
    public String serviceRequestId;

    public OmsSparePartHeaderPK() {
    }

    public OmsSparePartHeaderPK(String sequenceId, String serviceRequestId) {
        this.sequenceId = sequenceId;
        this.serviceRequestId = serviceRequestId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsSparePartHeaderPK) {
            final OmsSparePartHeaderPK otherOmsSparePartHeaderPK = (OmsSparePartHeaderPK)other;
            final boolean areEqual =
                (otherOmsSparePartHeaderPK.sequenceId.equals(sequenceId) && otherOmsSparePartHeaderPK.serviceRequestId.equals(serviceRequestId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }
}
