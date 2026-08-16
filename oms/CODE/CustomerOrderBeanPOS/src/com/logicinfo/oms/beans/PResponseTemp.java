package com.logicinfo.oms.beans;

import java.math.BigDecimal;

public class PResponseTemp
{
    public PResponseTemp() 
    {
        super();
    }
    public BigDecimal fulfilOrderNo;
    public String srcLocType;
    public String fulFillLocType;
    public BigDecimal srcLocId;
    public BigDecimal fulfillLocId;
    public BigDecimal quantity;


    public void setFulfilOrderNo(BigDecimal fulfilOrderNo) {
        this.fulfilOrderNo = fulfilOrderNo;
    }

    public BigDecimal getFulfilOrderNo() {
        return fulfilOrderNo;
    }

    public void setSrcLocType(String srcLocType) {
        this.srcLocType = srcLocType;
    }

    public String getSrcLocType() {
        return srcLocType;
    }

    public void setFulFillLocType(String fulFillLocType) {
        this.fulFillLocType = fulFillLocType;
    }

    public String getFulFillLocType() {
        return fulFillLocType;
    }

    public void setSrcLocId(BigDecimal srcLocId) {
        this.srcLocId = srcLocId;
    }

    public BigDecimal getSrcLocId() {
        return srcLocId;
    }

    public void setFulfillLocId(BigDecimal fulfillLocId) {
        this.fulfillLocId = fulfillLocId;
    }

    public BigDecimal getFulfillLocId() {
        return fulfillLocId;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
