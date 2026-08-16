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
@NamedQueries( { @NamedQuery(name = "OmsStsTrfItem.findAll", query = "select o from OmsStsTrfItem o"),
                 @NamedQuery(name = "OmsStsTrfItem.findByOmsTrfReqId",
                             query = "select o from OmsStsTrfItem o where o.omsTrfReqId=:reqId") })
@Table(name = "OMS_STS_TRF_ITEM")
@IdClass(OmsStsTrfItemPK.class)
public class OmsStsTrfItem implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "OMS_TRF_REQ_ID", nullable = false)
    private BigDecimal omsTrfReqId;
    @Column(length = 12)
    private String status;
    @Column(name = "TRANSFER_QTY_SUOM", nullable = false)
    private BigDecimal transferQtySuom;

    public OmsStsTrfItem() {
    }

    public OmsStsTrfItem(Timestamp createDatetime, String item, Timestamp lastUpdateDatetime, BigDecimal omsTrfReqId,
                         String status, BigDecimal transferQtySuom) {
        this.createDatetime = createDatetime;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsTrfReqId = omsTrfReqId;
        this.status = status;
        this.transferQtySuom = transferQtySuom;
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

    public BigDecimal getOmsTrfReqId() {
        return omsTrfReqId;
    }

    public void setOmsTrfReqId(BigDecimal omsTrfReqId) {
        this.omsTrfReqId = omsTrfReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTransferQtySuom() {
        return transferQtySuom;
    }

    public void setTransferQtySuom(BigDecimal transferQtySuom) {
        this.transferQtySuom = transferQtySuom;
    }
}
