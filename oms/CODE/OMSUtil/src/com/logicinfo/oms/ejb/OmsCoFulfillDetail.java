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
@NamedQueries( { @NamedQuery(name = "OmsCoFulfillDetail.findAll", query = "select o from OmsCoFulfillDetail o"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findByOmsCustOrdNo",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findFulFillDetails",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb and o.item=:item and o.lineNo=:lineNo  and o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty > 0 ORDER BY o.fulfillOrderNo desc"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findFulfillmentTotals",
                             query = "SELECT o.omsCustOrdNo, o.fulfillOrderNo, SUM(o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty) FROM OmsCoFulfillDetail o WHERE o.omsCustOrdNo=:omsCustOrdNumb AND o.fulfillOrderNo=:fulfillOrdNo  GROUP BY o.omsCustOrdNo, o.fulfillOrderNo"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findFulfillSeqForResv",
                             query = "select o.fulfillOrderNo from OmsCoFulfillDetail o where o.lineNo=:lineNo and o.omsCustOrdNo=:omsCustOrdNo"),
                @NamedQuery(name = "OmsCoFulfillDetail.findFulfillByLineNo",
                             query = "select o from OmsCoFulfillDetail o where o.lineNo=:lineNo and o.omsCustOrdNo=:omsCustOrdNo"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findBySourceLoc",
                             query = "select distinct(o.fulfillLoc) from OmsCoFulfillDetail o where o.sourceLoc=:sourceLoc"),
                                  @NamedQuery(name = "OmsCoFulfillDetail.findBySourceLocAndOmsCustNo",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and  o.sourceLoc=:sourceLoc and o.lineNo=:lineNo"),
                  @NamedQuery(name = "OmsCoFulfillDetail.findByOmsCustOrderNo",
                             query = "select distinct(o.sourceLoc) from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findColumns", query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo"),
                                  @NamedQuery(name = "OmsCoFulfillDetail.findFulFillDetailByItem",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb and o.item=:item  "),
                @NamedQuery(name = "OmsCoFulfillDetail.findMaxFulOrdNo",
                             query = "select max(o.fulfillOrderNo) from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo  "),
                 @NamedQuery(name = "OmsCoFulfillDetail.findOpenFulFillOrders",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:custNumb   and o.fulfillConfQty - o.fulfillDeliverQty - o.fulfillCancelQty > 0 ORDER BY o.sourceLocType"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findByOmsCustOrdNoandLineNoandItemId",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo and o.item=:item and o.sourceLoc!=o.fulfillLoc and o.fulfillLoc='V' and o.tsfNo is not null"),
                 @NamedQuery(name = "OmsCoFulfillDetail.findByItem",
                             query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo and o.item=:item "),
                 @NamedQuery(name = "OmsCoFulfillDetail.findBuOmsCustOrdNoandLineNoandsrcandFullfill",
                 query = "select o from OmsCoFulfillDetail o where o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo and o.sourceLoc=o.fulfillLoc")
                 })
@Table(name = "OMS_CO_FULFILL_DETAIL")
@IdClass(OmsCoFulfillDetailPK.class)
public class OmsCoFulfillDetail implements Serializable {
    @Column(name = "COMBINATION_ID")
    private BigDecimal combinationId;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "FULFILL_CANCEL_QTY", nullable = false)
    private BigDecimal fulfillCancelQty;
    @Column(name = "FULFILL_CONF_QTY", nullable = false)
    private BigDecimal fulfillConfQty;
    @Column(name = "FULFILL_DELIVER_QTY", nullable = false)
    private BigDecimal fulfillDeliverQty;
    @Column(name = "FULFILL_LOC", nullable = false)
    private BigDecimal fulfillLoc;
    @Column(name = "FULFILL_LOC_TYPE", nullable = false, length = 1)
    private String fulfillLocType;
    @Id
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Column(name = "FULFILL_REQ_QTY", nullable = false)
    private BigDecimal fulfillReqQty;
    @Column(name = "FULFILL_STATUS", nullable = false, length = 1)
    private String fulfillStatus;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Column(name = "LINE_NO")
    private BigDecimal lineNo;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "ORIG_ITEM", length = 25)
    private String origItem;
    @Column(name = "SOURCE_LOC")
    private BigDecimal sourceLoc;
    @Column(name = "SOURCE_LOC_TYPE", length = 2)
    private String sourceLocType;
    @Column(name = "TSF_APPROVAL_STATUS", length = 1)
    private String tsfApprovalStatus;
    @Column(name = "TSF_NO")
    private BigDecimal tsfNo;
    public OmsCoFulfillDetail() {
    }

    public OmsCoFulfillDetail(BigDecimal combinationId, Timestamp createDatetime, BigDecimal fulfillCancelQty,
                              BigDecimal fulfillConfQty, BigDecimal fulfillDeliverQty, BigDecimal fulfillLoc,
                              String fulfillLocType, BigDecimal fulfillOrderNo, BigDecimal fulfillReqQty,
                              String fulfillStatus, String item, Timestamp lastUpdateDatetime, BigDecimal lineNo,
                              BigDecimal omsCustOrdNo, String origItem, BigDecimal sourceLoc, String sourceLocType) {
        this.combinationId = combinationId;
        this.createDatetime = createDatetime;
        this.fulfillCancelQty = fulfillCancelQty;
        this.fulfillConfQty = fulfillConfQty;
        this.fulfillDeliverQty = fulfillDeliverQty;
        this.fulfillLoc = fulfillLoc;
        this.fulfillLocType = fulfillLocType;
        this.fulfillOrderNo = fulfillOrderNo;
        this.fulfillReqQty = fulfillReqQty;
        this.fulfillStatus = fulfillStatus;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lineNo = lineNo;
        this.omsCustOrdNo = omsCustOrdNo;
        this.origItem = origItem;
        this.sourceLoc = sourceLoc;
        this.sourceLocType = sourceLocType;
    }

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getFulfillCancelQty() {
        return fulfillCancelQty;
    }

    public void setFulfillCancelQty(BigDecimal fulfillCancelQty) {
        this.fulfillCancelQty = fulfillCancelQty;
    }

    public BigDecimal getFulfillConfQty() {
        return fulfillConfQty;
    }

    public void setFulfillConfQty(BigDecimal fulfillConfQty) {
        this.fulfillConfQty = fulfillConfQty;
    }

    public BigDecimal getFulfillDeliverQty() {
        return fulfillDeliverQty;
    }

    public void setFulfillDeliverQty(BigDecimal fulfillDeliverQty) {
        this.fulfillDeliverQty = fulfillDeliverQty;
    }

    public BigDecimal getFulfillLoc() {
        return fulfillLoc;
    }

    public void setFulfillLoc(BigDecimal fulfillLoc) {
        this.fulfillLoc = fulfillLoc;
    }

    public String getFulfillLocType() {
        return fulfillLocType;
    }

    public void setFulfillLocType(String fulfillLocType) {
        this.fulfillLocType = fulfillLocType;
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public BigDecimal getFulfillReqQty() {
        return fulfillReqQty;
    }

    public void setFulfillReqQty(BigDecimal fulfillReqQty) {
        this.fulfillReqQty = fulfillReqQty;
    }

    public String getFulfillStatus() {
        return fulfillStatus;
    }

    public void setFulfillStatus(String fulfillStatus) {
        this.fulfillStatus = fulfillStatus;
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public String getOrigItem() {
        return origItem;
    }

    public void setOrigItem(String origItem) {
        this.origItem = origItem;
    }

    public BigDecimal getSourceLoc() {
        return sourceLoc;
    }

    public void setSourceLoc(BigDecimal sourceLoc) {
        this.sourceLoc = sourceLoc;
    }

    public String getSourceLocType() {
        return sourceLocType;
    }

    public void setSourceLocType(String sourceLocType) {
        this.sourceLocType = sourceLocType;
    }

    public void setTsfApprovalStatus(String tsfApprovalStatus) {
        this.tsfApprovalStatus = tsfApprovalStatus;
    }

    public String getTsfApprovalStatus() {
        return tsfApprovalStatus;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }
}
