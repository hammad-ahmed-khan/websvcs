package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@NamedQueries( { @NamedQuery(name = "OrdcustDetail.findAll", query = "select o from OrdcustDetail o"),
                  @NamedQuery(name = "OrdcustDetail.findByOrdCustNo", query = "select o from OrdcustDetail o where o.ordcustNo=:ordcustNo")
                 })
@Table(name = "ORDCUST_DETAIL")
public class OrdcustDetail implements Serializable {
    @Column(length = 2000)
    private String comments;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
   
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Column(nullable = false, unique = true, length = 25)
        @Id
    private String item;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATE_DATETIME", nullable = false)
    private Date lastUpdateDatetime;
    @Column(name = "LAST_UPDATE_ID", nullable = false, length = 30)
    private String lastUpdateId;
    @Column(name = "ORDCUST_NO", nullable = false, unique = true)
        @Id
    private BigDecimal ordcustNo;
    @Column(name = "ORIGINAL_ITEM", unique = true, length = 25)
    private String originalItem;
    @Column(name = "QTY_CANCELLED_SUOM")
    private BigDecimal qtyCancelledSuom;
    @Column(name = "QTY_ORDERED_SUOM", nullable = false)
    private BigDecimal qtyOrderedSuom;
    @Column(name = "REF_ITEM", length = 25)
    private String refItem;
    @Column(name = "RETAIL_CURRENCY_CODE", length = 3)
    private String retailCurrencyCode;
    @Column(name = "STANDARD_UOM", nullable = false, length = 4)
    private String standardUom;
    @Column(name = "SUBSTITUTE_ALLOWED_IND", nullable = false, length = 1)
    private String substituteAllowedInd;
    @Column(name = "TRANSACTION_UOM", nullable = false, length = 4)
    private String transactionUom;
    @Column(name = "UNIT_RETAIL")
    private BigDecimal unitRetail;

    public OrdcustDetail() {
    }

    public OrdcustDetail(String comments, Date createDatetime, String createId, String item, Date lastUpdateDatetime,
                         String lastUpdateId, BigDecimal ordcustNo, String originalItem, BigDecimal qtyCancelledSuom,
                         BigDecimal qtyOrderedSuom, String refItem, String retailCurrencyCode, String standardUom,
                         String substituteAllowedInd, String transactionUom, BigDecimal unitRetail) {
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdateId = lastUpdateId;
        this.ordcustNo = ordcustNo;
        this.originalItem = originalItem;
        this.qtyCancelledSuom = qtyCancelledSuom;
        this.qtyOrderedSuom = qtyOrderedSuom;
        this.refItem = refItem;
        this.retailCurrencyCode = retailCurrencyCode;
        this.standardUom = standardUom;
        this.substituteAllowedInd = substituteAllowedInd;
        this.transactionUom = transactionUom;
        this.unitRetail = unitRetail;
       
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
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

    public Date getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Date lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public BigDecimal getOrdcustNo() {
        return ordcustNo;
    }

    public void setOrdcustNo(BigDecimal ordcustNo) {
        this.ordcustNo = ordcustNo;
    }

    public String getOriginalItem() {
        return originalItem;
    }

    public void setOriginalItem(String originalItem) {
        this.originalItem = originalItem;
    }

    public BigDecimal getQtyCancelledSuom() {
        return qtyCancelledSuom;
    }

    public void setQtyCancelledSuom(BigDecimal qtyCancelledSuom) {
        this.qtyCancelledSuom = qtyCancelledSuom;
    }

    public BigDecimal getQtyOrderedSuom() {
        return qtyOrderedSuom;
    }

    public void setQtyOrderedSuom(BigDecimal qtyOrderedSuom) {
        this.qtyOrderedSuom = qtyOrderedSuom;
    }

    public String getRefItem() {
        return refItem;
    }

    public void setRefItem(String refItem) {
        this.refItem = refItem;
    }

    public String getRetailCurrencyCode() {
        return retailCurrencyCode;
    }

    public void setRetailCurrencyCode(String retailCurrencyCode) {
        this.retailCurrencyCode = retailCurrencyCode;
    }

    public String getStandardUom() {
        return standardUom;
    }

    public void setStandardUom(String standardUom) {
        this.standardUom = standardUom;
    }

    public String getSubstituteAllowedInd() {
        return substituteAllowedInd;
    }

    public void setSubstituteAllowedInd(String substituteAllowedInd) {
        this.substituteAllowedInd = substituteAllowedInd;
    }

    public String getTransactionUom() {
        return transactionUom;
    }

    public void setTransactionUom(String transactionUom) {
        this.transactionUom = transactionUom;
    }

    public BigDecimal getUnitRetail() {
        return unitRetail;
    }

    public void setUnitRetail(BigDecimal unitRetail) {
        this.unitRetail = unitRetail;
    }


    public String toString() {
        return super.toString();
    }
}
