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
@NamedQueries( { @NamedQuery(name = "OmsPaymentSync.findAll", query = "select o from OmsPaymentSync o"),
                 @NamedQuery(name="OmsPaymentSync.findQuantityBasedItemAndLoc",query="select sum(o.quantity) from OmsPaymentSync o where o.item=:item and o.location=:location"),
                 @NamedQuery(name="OmsPaymentSync.findByOmsCustOrdNo",query="select o from OmsPaymentSync o where o.omsCustOrdNo=:omsCustOrdNo")
                })
@Table(name = "OMS_PAYMENT_SYNC")
@IdClass(OmsPaymentSyncPK.class)
public class OmsPaymentSync implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATE_ID", length = 25)
    private String createId;
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Id
    @Column(nullable = false)
    private BigDecimal location;
    @Column(name = "LOCATION_TYPE", nullable = false, length = 2)
    private String locationType;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(nullable = false)
    private BigDecimal quantity;

    public OmsPaymentSync() {
    }

    public OmsPaymentSync(Timestamp createDatetime, String createId, String item, BigDecimal lineNo,
                          BigDecimal location, String locationType, BigDecimal omsCustOrdNo, BigDecimal quantity) {
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.item = item;
        this.lineNo = lineNo;
        this.location = location;
        this.locationType = locationType;
        this.omsCustOrdNo = omsCustOrdNo;
        this.quantity = quantity;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
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

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
