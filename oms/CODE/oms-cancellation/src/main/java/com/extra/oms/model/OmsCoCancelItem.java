package com.extra.oms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.extra.common.model.OmsCustOrdItem;

public class OmsCoCancelItem {

    private BigDecimal cancelConfQty = BigDecimal.ZERO;

    private BigDecimal cancelReqQty = BigDecimal.ZERO;

    private String comments;

    private Timestamp createDatetime;

    private String item;

    private Timestamp lastUpdateDatetime;

    private BigDecimal lineNo;

    private Long omsCancelId;
   
    private OmsCustOrdItem ordItem;
   
    private List<OmsCoFoCancel> fulfils;

    public OmsCustOrdItem getOrdItem() {
		return ordItem;
	}

	public void setOrdItem(OmsCustOrdItem ordItem) {
		this.ordItem = ordItem;
	}

	public List<OmsCoFoCancel> getFulfils() {
		return fulfils;
	}

	public void setFulfils(List<OmsCoFoCancel> fulfils) {
		this.fulfils = fulfils;
	}

	public BigDecimal getCancelConfQty() {
        return cancelConfQty;
    }

    public void setCancelConfQty(BigDecimal cancelConfQty) {
        this.cancelConfQty = cancelConfQty;
    }

    public BigDecimal getCancelReqQty() {
        return cancelReqQty;
    }

    public void setCancelReqQty(BigDecimal cancelReqQty) {
        this.cancelReqQty = cancelReqQty;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
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

    public Long getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(Long omsCancelId) {
        this.omsCancelId = omsCancelId;
    }
}
