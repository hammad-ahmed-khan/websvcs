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
@NamedQueries( { @NamedQuery(name = "OmsInvAdjItem.findAll", query = "select o from OmsInvAdjItem o"),
                 @NamedQuery(name = "OmsInvAdjItem.findByOmsAdjReqId",
                             query = "select o from OmsInvAdjItem o where o.omsAdjReqId=:reqId") })
@Table(name = "OMS_INV_ADJ_ITEM")
@IdClass(OmsInvAdjItemPK.class)
public class OmsInvAdjItem implements Serializable {
    @Column(name = "ADJ_QTY_SUOM", nullable = false)
    private BigDecimal adjQtySuom;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "OMS_ADJ_REQ_ID", nullable = false)
    private BigDecimal omsAdjReqId;
    @Column(length = 12)
    private String status;

    public OmsInvAdjItem() {
    }

    public OmsInvAdjItem(BigDecimal adjQtySuom, Timestamp createDatetime, String item, Timestamp lastUpdateDatetime,
                         BigDecimal omsAdjReqId, String status) {
        this.adjQtySuom = adjQtySuom;
        this.createDatetime = createDatetime;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsAdjReqId = omsAdjReqId;
        this.status = status;
    }

    public BigDecimal getAdjQtySuom() {
        return adjQtySuom;
    }

    public void setAdjQtySuom(BigDecimal adjQtySuom) {
        this.adjQtySuom = adjQtySuom;
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

    public BigDecimal getOmsAdjReqId() {
        return omsAdjReqId;
    }

    public void setOmsAdjReqId(BigDecimal omsAdjReqId) {
        this.omsAdjReqId = omsAdjReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
