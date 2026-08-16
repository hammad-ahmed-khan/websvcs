package com.extra.common.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OmsCoFulfillDetail {

    private BigDecimal combinationId;

    private Timestamp createDatetime;

    private BigDecimal fulfillCancelQty;

    private BigDecimal fulfillConfQty;

    private BigDecimal fulfillDeliverQty;

    private BigDecimal fulfillLoc;

    private String fulfillLocType;

    private BigDecimal fulfillOrderNo;

    private BigDecimal fulfillReqQty;

    private String fulfillStatus;

    private String item;

    private Timestamp lastUpdateDatetime;

    private BigDecimal lineNo;

    private BigDecimal omsCustOrdNo;

    private String origItem;

    private BigDecimal sourceLoc;

    private String sourceLocType;

    private String tsfApprovalStatus;

    private BigDecimal tsfNo;

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getFulfillCancelQty() {
        return fulfillCancelQty;
    }

    public void setFulfillCancelQty(BigDecimal fulfillCancelQty) {
        this.fulfillCancelQty = fulfillCancelQty;
    }

    public BigDecimal getFulfillConfQty() {
        return fulfillConfQty;
    }

    public void setFulfillConfQty(BigDecimal fulfillConfQty) {
        this.fulfillConfQty = fulfillConfQty;
    }

    public BigDecimal getFulfillDeliverQty() {
        return fulfillDeliverQty;
    }

    public void setFulfillDeliverQty(BigDecimal fulfillDeliverQty) {
        this.fulfillDeliverQty = fulfillDeliverQty;
    }

    public BigDecimal getFulfillLoc() {
        return fulfillLoc;
    }

    public void setFulfillLoc(BigDecimal fulfillLoc) {
        this.fulfillLoc = fulfillLoc;
    }

    public String getFulfillLocType() {
        return fulfillLocType;
    }

    public void setFulfillLocType(String fulfillLocType) {
        this.fulfillLocType = fulfillLocType;
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public BigDecimal getFulfillReqQty() {
        return fulfillReqQty;
    }

    public void setFulfillReqQty(BigDecimal fulfillReqQty) {
        this.fulfillReqQty = fulfillReqQty;
    }

    public String getFulfillStatus() {
        return fulfillStatus;
    }

    public void setFulfillStatus(String fulfillStatus) {
        this.fulfillStatus = fulfillStatus;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public String getOrigItem() {
        return origItem;
    }

    public void setOrigItem(String origItem) {
        this.origItem = origItem;
    }

    public BigDecimal getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(BigDecimal sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public String getSourceLocType() {
        return sourceLocType;
    }

    public void setSourceLocType(String sourceLocType) {
        this.sourceLocType = sourceLocType;
    }

    public void setTsfApprovalStatus(String tsfApprovalStatus) {
        this.tsfApprovalStatus = tsfApprovalStatus;
    }

    public String getTsfApprovalStatus() {
        return tsfApprovalStatus;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }
}
