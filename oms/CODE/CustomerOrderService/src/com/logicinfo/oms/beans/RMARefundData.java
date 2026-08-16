package com.logicinfo.oms.beans;

import java.math.BigDecimal;

public class RMARefundData 
{
    public RMARefundData()
    {
        super();
    }
    public BigDecimal rmaId;
    public BigDecimal lineNo;
    public BigDecimal quantity;
    public String eventId;

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
