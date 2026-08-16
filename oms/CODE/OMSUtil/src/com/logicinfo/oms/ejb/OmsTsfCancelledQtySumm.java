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
@NamedQueries( { @NamedQuery(name = "OmsTsfCancelledQtySumm.findAll",
                             query = "select o from OmsTsfCancelledQtySumm o"),
                 @NamedQuery(name = "OmsTsfCancelledQtySumm.findByCustOrdNoTsfnoandOmsCustOrdNo",
                             query = "select o from OmsTsfCancelledQtySumm o where o.custOrderNo=:custOrderNo and o.tsfNo=:tsfNo and o.omsCustOrdNo=:omsCustOrdNo")
                 })
@Table(name = "OMS_TSF_CANCELLED_QTY_SUMM")
@IdClass(OmsTsfCancelledQtySummPK.class)
public class OmsTsfCancelledQtySumm implements Serializable {
    @Column(name = "CLOSE_DATETIME")
    private Timestamp closeDatetime;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CUM_TSF_QTY", nullable = false)
    private BigDecimal cumTsfQty;
    @Id
    @Column(name = "CUST_ORDER_NO", nullable = false, length = 48)
    private String custOrderNo;
    @Column(nullable = false)
    private BigDecimal fulfillloc;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(nullable = false)
    private BigDecimal sourceloc;
    @Column(name = "TSF_APPROVAL_STATUS", nullable = false, length = 1)
    private String tsfApprovalStatus;
    @Id
    @Column(name = "TSF_NO", nullable = false)
    private BigDecimal tsfNo;
    @Column(name = "UPDATE_DATETIME")
    private Timestamp updateDatetime;

    public OmsTsfCancelledQtySumm() {
    }

    public OmsTsfCancelledQtySumm(Timestamp closeDatetime, Timestamp createDatetime, BigDecimal cumTsfQty,
                                  String custOrderNo, BigDecimal fulfillloc, BigDecimal omsCustOrdNo,
                                  BigDecimal sourceloc, String tsfApprovalStatus, BigDecimal tsfNo,
                                  Timestamp updateDatetime) {
        this.closeDatetime = closeDatetime;
        this.createDatetime = createDatetime;
        this.cumTsfQty = cumTsfQty;
        this.custOrderNo = custOrderNo;
        this.fulfillloc = fulfillloc;
        this.omsCustOrdNo = omsCustOrdNo;
        this.sourceloc = sourceloc;
        this.tsfApprovalStatus = tsfApprovalStatus;
        this.tsfNo = tsfNo;
        this.updateDatetime = updateDatetime;
    }

    public Timestamp getCloseDatetime() {
        return closeDatetime;
    }

    public void setCloseDatetime(Timestamp closeDatetime) {
        this.closeDatetime = closeDatetime;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getCumTsfQty() {
        return cumTsfQty;
    }

    public void setCumTsfQty(BigDecimal cumTsfQty) {
        this.cumTsfQty = cumTsfQty;
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public BigDecimal getFulfillloc() {
        return fulfillloc;
    }

    public void setFulfillloc(BigDecimal fulfillloc) {
        this.fulfillloc = fulfillloc;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getSourceloc() {
        return sourceloc;
    }

    public void setSourceloc(BigDecimal sourceloc) {
        this.sourceloc = sourceloc;
    }

    public String getTsfApprovalStatus() {
        return tsfApprovalStatus;
    }

    public void setTsfApprovalStatus(String tsfApprovalStatus) {
        this.tsfApprovalStatus = tsfApprovalStatus;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public Timestamp getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Timestamp updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
