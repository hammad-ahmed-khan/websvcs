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
@NamedQueries( { @NamedQuery(name = "OmsRmaReqItem.findAll", query = "select o from OmsRmaReqItem o") ,
                 @NamedQuery(name = "OmsRmaReqItem.findByRmaIdAndItem", query = "select o from OmsRmaReqItem o where o.rmaId=:rmaId and o.item=:item and o.lineNo=:lineNo"),
                 @NamedQuery(name = "OmsRmaReqItem.findByRmaId", query = "select o from OmsRmaReqItem o where o.rmaId=:rmaId ")})
@Table(name = "OMS_RMA_REQ_ITEM")
@IdClass(OmsRmaReqItemPK.class)
public class OmsRmaReqItem implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "ITEM_COMMENTS", nullable = false, length = 200)
    private String itemComments;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Column(name = "ORIG_RMA_QTY")
    private BigDecimal origRmaQty;
    @Column(name = "RECEIVED_QTY")
    private BigDecimal receivedQty;
    @Id
    @Column(name = "RMA_ID", nullable = false, length = 20)
    private BigDecimal rmaId;
    @Column(name = "RMA_QTY", nullable = false)
    private BigDecimal rmaQty;
    @Column(nullable = false, length = 1)
    private String status;

    public OmsRmaReqItem() {
    }

    public OmsRmaReqItem(Timestamp createDatetime, String item, String itemComments, Timestamp lastUpdateDatetime,
                         BigDecimal lineNo, BigDecimal origRmaQty, BigDecimal receivedQty, BigDecimal rmaId,
                         BigDecimal rmaQty, String status) {
        this.createDatetime = createDatetime;
        this.item = item;
        this.itemComments = itemComments;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lineNo = lineNo;
        this.origRmaQty = origRmaQty;
        this.receivedQty = receivedQty;
        this.rmaId = rmaId;
        this.rmaQty = rmaQty;
        this.status = status;
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

    public String getItemComments() {
        return itemComments;
    }

    public void setItemComments(String itemComments) {
        this.itemComments = itemComments;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getOrigRmaQty() {
        return origRmaQty;
    }

    public void setOrigRmaQty(BigDecimal origRmaQty) {
        this.origRmaQty = origRmaQty;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }

    public BigDecimal getRmaQty() {
        return rmaQty;
    }

    public void setRmaQty(BigDecimal rmaQty) {
        this.rmaQty = rmaQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
