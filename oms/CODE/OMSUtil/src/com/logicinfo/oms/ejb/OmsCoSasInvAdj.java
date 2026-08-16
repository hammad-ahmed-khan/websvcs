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
@NamedQueries( { @NamedQuery(name = "OmsCoSasInvAdj.findAll", query = "select o from OmsCoSasInvAdj o"),
                 @NamedQuery(name = "OmsCoSasInvAdj.findByOmsCustOrdNo",
                             query = "select o from OmsCoSasInvAdj o where  o.omsCustOrdNo=:omsCustOrdNo") })
@Table(name = "OMS_CO_SAS_INV_ADJ")
@IdClass(OmsCoSasInvAdjPK.class)
public class OmsCoSasInvAdj implements Serializable {
    @Id
    @Column(name = "CUST_ORDER_NO", nullable = false, length = 48)
    private String custOrderNo;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LINE_ITEM_NO", nullable = false)
    private BigDecimal lineItemNo;
    @Id
    @Column(name = "LOCATION_ID", nullable = false)
    private BigDecimal locationId;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "RESV_DATETIME", nullable = false)
    private Timestamp resvDatetime;
    @Column(name = "RESV_QTY", nullable = false)
    private BigDecimal resvQty;
    @Column(name = "RESV_REASON_CODE", nullable = false)
    private BigDecimal resvReasonCode;
    @Id
    @Column(name = "SUB_CUST_ORDER_NO", nullable = false, length = 3)
    private String subCustOrderNo;

    public OmsCoSasInvAdj() {
    }

    public OmsCoSasInvAdj(String custOrderNo, String item, BigDecimal lineItemNo, BigDecimal locationId,
                          BigDecimal omsCustOrdNo, Timestamp resvDatetime, BigDecimal resvQty,
                          BigDecimal resvReasonCode, String subCustOrderNo) {
        this.custOrderNo = custOrderNo;
        this.item = item;
        this.lineItemNo = lineItemNo;
        this.locationId = locationId;
        this.omsCustOrdNo = omsCustOrdNo;
        this.resvDatetime = resvDatetime;
        this.resvQty = resvQty;
        this.resvReasonCode = resvReasonCode;
        this.subCustOrderNo = subCustOrderNo;
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(BigDecimal lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public BigDecimal getLocationId() {
        return locationId;
    }

    public void setLocationId(BigDecimal locationId) {
        this.locationId = locationId;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public Timestamp getResvDatetime() {
        return resvDatetime;
    }

    public void setResvDatetime(Timestamp resvDatetime) {
        this.resvDatetime = resvDatetime;
    }

    public BigDecimal getResvQty() {
        return resvQty;
    }

    public void setResvQty(BigDecimal resvQty) {
        this.resvQty = resvQty;
    }

    public BigDecimal getResvReasonCode() {
        return resvReasonCode;
    }

    public void setResvReasonCode(BigDecimal resvReasonCode) {
        this.resvReasonCode = resvReasonCode;
    }

    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(String subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
