package com.logicinfo.oms.model;

import java.math.BigDecimal;

public class FulfillOrdandSourceLocPOJO {
    public FulfillOrdandSourceLocPOJO() {
        super();
    }
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
