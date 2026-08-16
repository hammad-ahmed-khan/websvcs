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
@NamedQueries( { @NamedQuery(name = "OmsCustOrdItem.findAll", query = "select o from OmsCustOrdItem o"),
                 @NamedQuery(name = "OmsCustOrdItem.getCustItemList",
                             query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustNumber and o.item=:inputItem"),
                 @NamedQuery(name = "OmsCustOrdItem.getCustOrdNo",
                             query = "select o from OmsCustOrdItem o where o.omsCustOrdNo=:omsCustNumber"),
                 @NamedQuery(name = "OmsCustOrdLogLineItem.findItemandLogSeqNo",
                             query = "select o.qty from OmsCustOrdLogItem o where o.lineNo=:lineNo and o.item=:item and o.logSeqNo=:logSeqNo")})
@Table(name = "OMS_CUST_ORD_LOG_ITEM")
@IdClass(OmsCustOrdLogItemPK.class)
public class OmsCustOrdLogItem implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "FULFILL_ORDER_NO")
    private BigDecimal fulfillOrderNo;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LOG_SEQ_NO", nullable = false)
    private BigDecimal logSeqNo;
    @Column(name="QTY",nullable = false)
    private BigDecimal qty;
    
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    public OmsCustOrdLogItem() {
    }

    public OmsCustOrdLogItem(Timestamp createDatetime, BigDecimal fulfillOrderNo, String item, BigDecimal logSeqNo,
                             BigDecimal qty) {
        this.createDatetime = createDatetime;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.logSeqNo = logSeqNo;
        this.qty = qty;
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

    public BigDecimal getLogSeqNo() {
        return logSeqNo;
    }

    public void setLogSeqNo(BigDecimal logSeqNo) {
        this.logSeqNo = logSeqNo;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }
}
