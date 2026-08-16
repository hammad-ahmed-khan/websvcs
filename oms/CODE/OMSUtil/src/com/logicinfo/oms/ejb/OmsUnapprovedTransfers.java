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
@NamedQueries( { @NamedQuery(name = "OmsUnapprovedTransfers.findAll",
                             query = "select o from OmsUnapprovedTransfers o"),
                             @NamedQuery(name = "OmsUnapprovedTransfers.findQtyByItemAndLoc",
                             query = "select o from OmsUnapprovedTransfers o where o.tsfNo=:tsfNo and   o.item=:item and  o.location=:location"),
                             @NamedQuery(name = "OmsUnapprovedTransfers.findQtyByLoc",
                             query = "select o from OmsUnapprovedTransfers o where  o.item=:item and  o.location=:location"),
                             @NamedQuery(name = "OmsUnapprovedTransfers.findBytsfNoandOmsCustOrdNo",
                             query = "select o from OmsUnapprovedTransfers o where  o.tsfNo=:tsfNo and  o.omsCustOrdNo=:omsCustOrdNo")
                 })
@Table(name = "OMS_UNAPPROVED_TRANSFERS")
@IdClass(OmsUnapprovedTransfersPK.class)
public class OmsUnapprovedTransfers implements Serializable {
    @Column(name = "CREATE_DATETIME")
    private Timestamp createDatetime;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(nullable = false)
    private BigDecimal location;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "TSF_NO", nullable = false)
    private BigDecimal tsfNo;
    @Column(name = "UNAPPROVED_QTY", nullable = false)
    private BigDecimal unapprovedQty;

    public OmsUnapprovedTransfers() {
    }

    public OmsUnapprovedTransfers(Timestamp createDatetime, String item, Timestamp lastUpdateDatetime,
                                  BigDecimal location, BigDecimal omsCustOrdNo, BigDecimal tsfNo,
                                  BigDecimal unapprovedqty) {
        this.createDatetime = createDatetime;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.location = location;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tsfNo = tsfNo;
        this.unapprovedQty = unapprovedqty;
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

    public BigDecimal getLocation() {
        return location;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public BigDecimal getUnapprovedQty() {
        return unapprovedQty;
    }

    public void setUnapprovedQty(BigDecimal unapprovedQty) {
        this.unapprovedQty = unapprovedQty;
    }
}
