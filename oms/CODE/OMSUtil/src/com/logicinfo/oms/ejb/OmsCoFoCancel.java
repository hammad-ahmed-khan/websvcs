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
@NamedQueries( { @NamedQuery(name = "OmsCoFoCancel.findAll", query = "select o from OmsCoFoCancel o"),
                 @NamedQuery(name = "OmsCoFoCancel.findByOmsCancelId", query = "select o from OmsCoFoCancel o where o.omsCancelId=:omsCancelId")})
@Table(name = "OMS_CO_FO_CANCEL")
@IdClass(OmsCoFoCancelPK.class)
public class OmsCoFoCancel implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "FO_CANCELLED_OTY", nullable = false)
    private BigDecimal foCancelledOty;
    @Id
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_CANCEL_ID", nullable = false)
    private BigDecimal omsCancelId;
    @Column(name = "WS_RESPONSE", length = 3)
    private String wsResponse;

    public OmsCoFoCancel() {
    }

    public OmsCoFoCancel(Timestamp createDatetime, BigDecimal foCancelledOty, BigDecimal fulfillOrderNo, String item,
                         BigDecimal lineNo, BigDecimal omsCancelId, String wsResponse) {
        this.createDatetime = createDatetime;
        this.foCancelledOty = foCancelledOty;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.lineNo = lineNo;
        this.omsCancelId = omsCancelId;
        this.wsResponse = wsResponse;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getFoCancelledOty() {
        return foCancelledOty;
    }

    public void setFoCancelledOty(BigDecimal foCancelledOty) {
        this.foCancelledOty = foCancelledOty;
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public String getWsResponse() {
        return wsResponse;
    }

    public void setWsResponse(String wsResponse) {
        this.wsResponse = wsResponse;
    }
}
