package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsSparePartConfirmHdrPK implements Serializable {
    public String serviceConfirmId;
    public String serviceRequestId;

    public OmsSparePartConfirmHdrPK() {
    }

    public OmsSparePartConfirmHdrPK(String serviceConfirmId, String serviceRequestId) {
        this.serviceConfirmId = serviceConfirmId;
        this.serviceRequestId = serviceRequestId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsSparePartConfirmHdrPK) {
            final OmsSparePartConfirmHdrPK otherOmsSparePartConfirmHdrPK = (OmsSparePartConfirmHdrPK)other;
            final boolean areEqual =
                (otherOmsSparePartConfirmHdrPK.serviceConfirmId.equals(serviceConfirmId) && otherOmsSparePartConfirmHdrPK.serviceRequestId.equals(serviceRequestId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getServiceConfirmId() {
        return serviceConfirmId;
    }

    public void setServiceConfirmId(String serviceConfirmId) {
        this.serviceConfirmId = serviceConfirmId;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }
}
