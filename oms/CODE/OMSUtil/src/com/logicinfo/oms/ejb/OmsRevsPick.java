package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsRevsPick.findAll", query = "select o from OmsRevsPick o") })
@Table(name = "OMS_REVS_PICK")
@IdClass(OmsRevsPickPK.class)
public class OmsRevsPick implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(nullable = false)
    private BigDecimal loc;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "OMS_REV_PIC_SEQ", nullable = false)
    @SequenceGenerator( name = "omsRevsPickSeq", sequenceName = "OMS_REVS_PICK_SEQ", allocationSize = 1, initialValue = 1 ) 
     @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsRevsPickSeq" )
    private BigDecimal omsRevPicSeq;
    @Column(name = "PU_QTY", nullable = false)
    private BigDecimal puQty;
    @Column(name = "REV_PIC_QTY", nullable = false)
    private BigDecimal revPicQty;

    public OmsRevsPick() {
    }

    public OmsRevsPick(Timestamp createDatetime, BigDecimal fulfillOrderNo, String item, Timestamp lastUpdateDatetime,
                       BigDecimal loc, BigDecimal omsCustOrdNo, BigDecimal omsRevPicSeq, BigDecimal puQty,
                       BigDecimal revPicQty) {
        this.createDatetime = createDatetime;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.loc = loc;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsRevPicSeq = omsRevPicSeq;
        this.puQty = puQty;
        this.revPicQty = revPicQty;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
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

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getLoc() {
        return loc;
    }

    public void setLoc(BigDecimal loc) {
        this.loc = loc;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getOmsRevPicSeq() {
        return omsRevPicSeq;
    }

    public void setOmsRevPicSeq(BigDecimal omsRevPicSeq) {
        this.omsRevPicSeq = omsRevPicSeq;
    }

    public BigDecimal getPuQty() {
        return puQty;
    }

    public void setPuQty(BigDecimal puQty) {
        this.puQty = puQty;
    }

    public BigDecimal getRevPicQty() {
        return revPicQty;
    }

    public void setRevPicQty(BigDecimal revPicQty) {
        this.revPicQty = revPicQty;
    }
}
