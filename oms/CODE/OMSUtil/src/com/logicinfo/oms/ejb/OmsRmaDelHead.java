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
@NamedQueries( { @NamedQuery(name = "OmsRmaDelHead.findAll", query = "select o from OmsRmaDelHead o") ,
                 @NamedQuery(name = "OmsRmaDelHead.findByRmaDelReqId", query = "select o from OmsRmaDelHead o where o.rmaDelReqId=:rmaDelReqId and o.rmaId=:rmaId"),
                 @NamedQuery(name = "OmsRmaDelHead.findByRmaId", query = "select o from OmsRmaDelHead o where o.rmaId=:rmaId and o.status=:status")})
@Table(name = "OMS_RMA_DEL_HEAD")
public class OmsRmaDelHead implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    
    @Column(name = "RMA_DEL_REQ_ID", nullable = false)
    @Id
    private String rmaDelReqId;
    @Column(name = "RMA_ID", nullable = false)
    private BigDecimal rmaId;
    @Column(nullable = false, length = 15)
    private String status;

    public OmsRmaDelHead() {
    }

    public OmsRmaDelHead(Timestamp createDatetime, String rmaDelReqId, BigDecimal rmaId, String status) {
        this.createDatetime = createDatetime;
        this.rmaDelReqId = rmaDelReqId;
        this.rmaId = rmaId;
        this.status = status;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getRmaDelReqId() {
        return rmaDelReqId;
    }

    public void setRmaDelReqId(String rmaDelReqId) {
        this.rmaDelReqId = rmaDelReqId;
    }

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
