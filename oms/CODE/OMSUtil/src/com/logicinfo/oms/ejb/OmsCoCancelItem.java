package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsCoCancelItem.findAll", query = "select o from OmsCoCancelItem o"),
                 @NamedQuery(name = "OmsCoCancelItem.findByOmsCancelId",
                             query = "select o from OmsCoCancelItem o where o.omsCancelId=:omsCancelId  "),
                 @NamedQuery(name = "OmsCoCancelItem.getCustItemList",
                             query = "select o from OmsCoCancelItem o where o.omsCancelId=:omsCancelId and o.item=:inputItem"),
                 @NamedQuery(name = "OmsCoCancelItem.findByomsCancelIdandLineNo",
                  query = "select o from OmsCoCancelItem o where o.omsCancelId=:omsCancelId and o.lineNo=:lineNo")})
@Table(name = "OMS_CO_CANCEL_ITEM")
@IdClass(OmsCoCancelItemPK.class)
public class OmsCoCancelItem implements Serializable {
    @Column(name = "CANCEL_CONF_QTY")
    private BigDecimal cancelConfQty;
    @Column(name = "CANCEL_REQ_QTY", nullable = false)
    private BigDecimal cancelReqQty;
    @Column(length = 200)
    private String comments;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_CANCEL_ID", nullable = false)
    private BigDecimal omsCancelId;

    public OmsCoCancelItem() {
    }

    public OmsCoCancelItem(BigDecimal cancelConfQty, BigDecimal cancelReqQty, String comments,
                           Timestamp createDatetime, String item, Timestamp lastUpdateDatetime, BigDecimal lineNo,
                           BigDecimal omsCancelId) {
        this.cancelConfQty = cancelConfQty;
        this.cancelReqQty = cancelReqQty;
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lineNo = lineNo;
        this.omsCancelId = omsCancelId;
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

    public BigDecimal getOmsCancelId() {
        return omsCancelId;
    }

    public void setOmsCancelId(BigDecimal omsCancelId) {
        this.omsCancelId = omsCancelId;
    }
}
