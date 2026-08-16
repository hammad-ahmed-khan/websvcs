package com.logicinfo.oms.ejb;


import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "OmsSimCancelTsfDetail.findAll",
                             query = "select o from OmsSimCancelTsfDetail o") })
@Table(name = "OMS_SIM_CANCEL_TSF_DETAIL")
public class OmsSimCancelTsfDetail implements Serializable {
    @Column(name = "CREATE_DATIME")
    private Timestamp createDatime;
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Column(name = "OMS_CANCEL_ID", nullable = false)
    private BigDecimal omsCancelId;
    @Column(name = "OMS_CANCEL_QTY", nullable = false)
    private BigDecimal omsCancelQty;
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Id
    @Column(name = "OMS_SIM_TSF_CANCEL_SEQ", nullable = false)
    @SequenceGenerator(name = "simTsfSeqNo", sequenceName = "OMS_SIM_CAN_TSF_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "simTsfSeqNo")
    private BigDecimal omsSimTsfCancelSeq;
   
    @Column(name = "SO_STATUS_CANCEL_QTY", nullable = false)
    private BigDecimal soStatusCancelQty;
    @Column(name = "SOURCE_LOC", nullable = false)
    private BigDecimal sourceLoc;
    @Column(name = "TRANSFER_ID", nullable = false)
    private BigDecimal transferId;

    public OmsSimCancelTsfDetail() {
    }

    public OmsSimCancelTsfDetail(Timestamp createDatime, BigDecimal fulfillOrderNo, String item,
                                 Timestamp lastUpdateDatetime, BigDecimal lineNo, BigDecimal omsCancelId,
                                 BigDecimal omsCancelQty, BigDecimal omsCustOrdNo, BigDecimal omsSimTsfCancelSeq,
                                 BigDecimal soStatusCancelQty, BigDecimal sourceLoc,
                                 BigDecimal transferId) {
        this.createDatime = createDatime;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lineNo = lineNo;
        this.omsCancelId = omsCancelId;
        this.omsCancelQty = omsCancelQty;
        this.omsCustOrdNo = omsCustOrdNo;
        this.omsSimTsfCancelSeq = omsSimTsfCancelSeq;
       
        this.soStatusCancelQty = soStatusCancelQty;
        this.sourceLoc = sourceLoc;
        this.transferId = transferId;
    }

    public Timestamp getCreateDatime() {
        return createDatime;
    }

    public void setCreateDatime(Timestamp createDatime) {
        this.createDatime = createDatime;
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

    public BigDecimal getOmsCancelQty() {
        return omsCancelQty;
    }

    public void setOmsCancelQty(BigDecimal omsCancelQty) {
        this.omsCancelQty = omsCancelQty;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getOmsSimTsfCancelSeq() {
        return omsSimTsfCancelSeq;
    }

    public void setOmsSimTsfCancelSeq(BigDecimal omsSimTsfCancelSeq) {
        this.omsSimTsfCancelSeq = omsSimTsfCancelSeq;
    }

   

    public BigDecimal getSoStatusCancelQty() {
        return soStatusCancelQty;
    }

    public void setSoStatusCancelQty(BigDecimal soStatusCancelQty) {
        this.soStatusCancelQty = soStatusCancelQty;
    }

    public BigDecimal getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(BigDecimal sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public BigDecimal getTransferId() {
        return transferId;
    }

    public void setTransferId(BigDecimal transferId) {
        this.transferId = transferId;
    }
}
