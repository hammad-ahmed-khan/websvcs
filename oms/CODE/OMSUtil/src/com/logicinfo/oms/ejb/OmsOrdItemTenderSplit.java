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
@NamedQueries( { @NamedQuery(name = "OmsOrdItemTenderSplit.findAll",
                             query = "select o from OmsOrdItemTenderSplit o") })
@Table(name = "OMS_ORD_ITEM_TENDER_SPLIT")
@IdClass(OmsOrdItemTenderSplitPK.class)
public class OmsOrdItemTenderSplit implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CREATED_BY", nullable = false, length = 40)
    private String createdBy;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "TENDER_SEQ_NO", nullable = false)
    private BigDecimal tenderSeqNo;
    @Column(name = "TENDER_SPREAD", nullable = false)
    private BigDecimal tenderSpread;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;

    public OmsOrdItemTenderSplit() {
    }

    public OmsOrdItemTenderSplit(Timestamp createDatetime, String createdBy, String item, BigDecimal omsCustOrdNo,
                                 BigDecimal tenderSeqNo, BigDecimal tenderSpread) {
        this.createDatetime = createDatetime;
        this.createdBy = createdBy;
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
        this.tenderSeqNo = tenderSeqNo;
        this.tenderSpread = tenderSpread;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getTenderSeqNo() {
        return tenderSeqNo;
    }

    public void setTenderSeqNo(BigDecimal tenderSeqNo) {
        this.tenderSeqNo = tenderSeqNo;
    }

    public BigDecimal getTenderSpread() {
        return tenderSpread;
    }

    public void setTenderSpread(BigDecimal tenderSpread) {
        this.tenderSpread = tenderSpread;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }
}
