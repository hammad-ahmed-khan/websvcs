package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsSparePartCancelHdrPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5237744480901868717L;

	public String cancellationId;
    public String sequenceId;
    public String serviceReqSeqId;

    public OmsSparePartCancelHdrPK() {
    }

    public OmsSparePartCancelHdrPK(String cancellationId, String sequenceId, String serviceReqSeqId) {
        this.cancellationId = cancellationId;
        this.sequenceId = sequenceId;
        this.serviceReqSeqId = serviceReqSeqId;
    }

    public boolean equals(Object other)
    {
        if (other instanceof OmsSparePartCancelHdrPK)
        {
            final OmsSparePartCancelHdrPK otherOmsSparePartCancelHdrPK = (OmsSparePartCancelHdrPK)other;
            final boolean areEqual =(otherOmsSparePartCancelHdrPK.cancellationId.equals(cancellationId) && otherOmsSparePartCancelHdrPK.sequenceId.equals(sequenceId) &&
                 otherOmsSparePartCancelHdrPK.serviceReqSeqId.equals(serviceReqSeqId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(String cancellationId) {
        this.cancellationId = cancellationId;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getServiceReqSeqId() {
        return serviceReqSeqId;
    }

    public void setServiceReqSeqId(String serviceReqSeqId) {
        this.serviceReqSeqId = serviceReqSeqId;
    }
}
