package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsRmaModHead.findAll", query = "select o from OmsRmaModHead o"),
                 @NamedQuery(name = "OmsRmaModHead.findByRmaModReqId", query = "select o from OmsRmaModHead o where o.rmaModReqId=:rmaModReqId")})
@Table(name = "OMS_RMA_MOD_HEAD")
public class OmsRmaModHead implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "RMA_ID", nullable = false)
    private BigDecimal rmaId;
    @Id
    @Column(name = "RMA_MOD_REQ_ID", nullable = false, length = 12)
    private String rmaModReqId;
    @Column(nullable = false, length = 15)
    private String status;

    public OmsRmaModHead() {
    }

    public OmsRmaModHead(Timestamp createDatetime, BigDecimal rmaId, String rmaModReqId, String status) {
        this.createDatetime = createDatetime;
        this.rmaId = rmaId;
        this.rmaModReqId = rmaModReqId;
        this.status = status;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }

    public String getRmaModReqId() {
        return rmaModReqId;
    }

    public void setRmaModReqId(String rmaModReqId) {
        this.rmaModReqId = rmaModReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
