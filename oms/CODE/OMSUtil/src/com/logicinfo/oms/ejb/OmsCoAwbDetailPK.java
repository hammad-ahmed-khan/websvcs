package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class OmsCoAwbDetailPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6389074300398822624L;

	public String awbUpdReqId;

	public BigDecimal deliveryId;

	private BigDecimal sequenceId;

	public OmsCoAwbDetailPK() {
	}

	public OmsCoAwbDetailPK(String awbUpdReqId, BigDecimal deliveryId) {
		this.awbUpdReqId = awbUpdReqId;
		this.deliveryId = deliveryId;
	}

	public boolean equals(Object other) {
		if (other instanceof OmsCoAwbDetailPK) {
			final OmsCoAwbDetailPK otherOmsCoAwbDetailPK = (OmsCoAwbDetailPK) other;
			final boolean areEqual = (otherOmsCoAwbDetailPK.awbUpdReqId.equals(awbUpdReqId) && otherOmsCoAwbDetailPK.deliveryId.equals(deliveryId) && otherOmsCoAwbDetailPK.getSequenceId().equals(sequenceId));
			return areEqual;
		}
		return false;
	}

	public int hashCode() {
		return super.hashCode();
	}

	public String getAwbUpdReqId() {
		return awbUpdReqId;
	}

	public void setAwbUpdReqId(String awbUpdReqId) {
		this.awbUpdReqId = awbUpdReqId;
	}

	public BigDecimal getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
	}

	public BigDecimal getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(BigDecimal sequenceId) {
		this.sequenceId = sequenceId;
	}
}
