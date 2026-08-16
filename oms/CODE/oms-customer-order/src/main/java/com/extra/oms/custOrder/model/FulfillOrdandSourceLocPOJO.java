package com.extra.oms.custOrder.model;

import java.math.BigDecimal;

public class FulfillOrdandSourceLocPOJO {

	private BigDecimal fulfillOrderNo;
	private BigDecimal sourceLoc;

	public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
		this.fulfillOrderNo = fulfillOrderNo;
	}

	public BigDecimal getFulfillOrderNo() {
		return fulfillOrderNo;
	}

	public void setSourceLoc(BigDecimal sourceLoc) {
		this.sourceLoc = sourceLoc;
	}

	public BigDecimal getSourceLoc() {
		return sourceLoc;
	}
}
