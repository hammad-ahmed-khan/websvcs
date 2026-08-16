package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries( { @NamedQuery(name = "Tsfdetail.findAll", query = "select o from Tsfdetail o") ,
                 @NamedQuery(name = "Tsfdetail.findSelectedQty", query = "select o.selectedQty from Tsfdetail o where o.tsfNo=:tsfNo and o.item=:item"),
                 @NamedQuery(name = "Tsfdetail.findQty", query = "select o from Tsfdetail o where o.tsfNo=:tsfNo and o.item=:item"),
                 @NamedQuery(name = "Tsfdetail.findDistroQty",query = "select o.distroQty from Tsfdetail o where o.tsfNo=:tsfNo and o.item=:item"),
                 @NamedQuery(name ="Tsfdetail.findTransferQty", query="select o from Tsfdetail o where o.tsfNo=:tsfNo")
})
@IdClass(TsfdetailPK.class)
public class Tsfdetail implements Serializable {
    @Column(name = "CANCELLED_QTY")
    private BigDecimal cancelledQty;
    @Column(name = "DEFAULT_CHRGS_2_LEG_IND", length = 1)
    private String defaultChrgs2LegInd;
    @Column(name = "DISTRO_QTY")
    private BigDecimal distroQty;
    @Column(name = "FILL_QTY")
    private BigDecimal fillQty;
    @Column(name = "FINISHER_AV_RETAIL")
    private BigDecimal finisherAvRetail;
    @Column(name = "FINISHER_UNITS")
    private BigDecimal finisherUnits;
    @Column(name = "INV_STATUS")
    private BigDecimal invStatus;
    @Column(nullable = false, length = 25)
    private String item;
    @Column(name = "MBR_PROCESSED_IND", length = 1)
    private String mbrProcessedInd;
    @Column(name = "PUBLISH_IND", nullable = false, length = 1)
    private String publishInd;
    @Column(name = "RECEIVED_QTY")
    private BigDecimal receivedQty;
    @Column(name = "RECONCILED_QTY")
    private BigDecimal reconciledQty;
    @Column(name = "RESTOCK_PCT")
    private BigDecimal restockPct;
    @Column(name = "SELECTED_QTY")
    private BigDecimal selectedQty;
    @Column(name = "SHIP_QTY")
    private BigDecimal shipQty;
    @Column(name = "SUPP_PACK_SIZE", nullable = false)
    private BigDecimal suppPackSize;
    @Column(name = "TSF_COST")
    private BigDecimal tsfCost;
    @Id
    @Column(name = "TSF_NO", nullable = false)
    private BigDecimal tsfNo;
    @Column(name = "TSF_PO_LINK_NO")
    private BigDecimal tsfPoLinkNo;
    @Column(name = "TSF_PRICE")
    private BigDecimal tsfPrice;
    @Column(name = "TSF_QTY")
    private BigDecimal tsfQty;
    @Id
    @Column(name = "TSF_SEQ_NO", nullable = false)
    private BigDecimal tsfSeqNo;
    @Column(name = "UPDATED_BY_RMS_IND", nullable = false, length = 1)
    private String updatedByRmsInd;

    public Tsfdetail() {
    }

    public Tsfdetail(BigDecimal cancelledQty, String defaultChrgs2LegInd, BigDecimal distroQty, BigDecimal fillQty,
                     BigDecimal finisherAvRetail, BigDecimal finisherUnits, BigDecimal invStatus, String item,
                     String mbrProcessedInd, String publishInd, BigDecimal receivedQty, BigDecimal reconciledQty,
                     BigDecimal restockPct, BigDecimal selectedQty, BigDecimal shipQty, BigDecimal suppPackSize,
                     BigDecimal tsfCost, BigDecimal tsfNo, BigDecimal tsfPoLinkNo, BigDecimal tsfPrice,
                     BigDecimal tsfQty, BigDecimal tsfSeqNo, String updatedByRmsInd) {
        this.cancelledQty = cancelledQty;
        this.defaultChrgs2LegInd = defaultChrgs2LegInd;
        this.distroQty = distroQty;
        this.fillQty = fillQty;
        this.finisherAvRetail = finisherAvRetail;
        this.finisherUnits = finisherUnits;
        this.invStatus = invStatus;
        this.item = item;
        this.mbrProcessedInd = mbrProcessedInd;
        this.publishInd = publishInd;
        this.receivedQty = receivedQty;
        this.reconciledQty = reconciledQty;
        this.restockPct = restockPct;
        this.selectedQty = selectedQty;
        this.shipQty = shipQty;
        this.suppPackSize = suppPackSize;
        this.tsfCost = tsfCost;
        this.tsfNo = tsfNo;
        this.tsfPoLinkNo = tsfPoLinkNo;
        this.tsfPrice = tsfPrice;
        this.tsfQty = tsfQty;
        this.tsfSeqNo = tsfSeqNo;
        this.updatedByRmsInd = updatedByRmsInd;
    }

    public BigDecimal getCancelledQty() {
        return cancelledQty;
    }

    public void setCancelledQty(BigDecimal cancelledQty) {
        this.cancelledQty = cancelledQty;
    }

    public String getDefaultChrgs2LegInd() {
        return defaultChrgs2LegInd;
    }

    public void setDefaultChrgs2LegInd(String defaultChrgs2LegInd) {
        this.defaultChrgs2LegInd = defaultChrgs2LegInd;
    }

    public BigDecimal getDistroQty() {
        return distroQty;
    }

    public void setDistroQty(BigDecimal distroQty) {
        this.distroQty = distroQty;
    }

    public BigDecimal getFillQty() {
        return fillQty;
    }

    public void setFillQty(BigDecimal fillQty) {
        this.fillQty = fillQty;
    }

    public BigDecimal getFinisherAvRetail() {
        return finisherAvRetail;
    }

    public void setFinisherAvRetail(BigDecimal finisherAvRetail) {
        this.finisherAvRetail = finisherAvRetail;
    }

    public BigDecimal getFinisherUnits() {
        return finisherUnits;
    }

    public void setFinisherUnits(BigDecimal finisherUnits) {
        this.finisherUnits = finisherUnits;
    }

    public BigDecimal getInvStatus() {
        return invStatus;
    }

    public void setInvStatus(BigDecimal invStatus) {
        this.invStatus = invStatus;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMbrProcessedInd() {
        return mbrProcessedInd;
    }

    public void setMbrProcessedInd(String mbrProcessedInd) {
        this.mbrProcessedInd = mbrProcessedInd;
    }

    public String getPublishInd() {
        return publishInd;
    }

    public void setPublishInd(String publishInd) {
        this.publishInd = publishInd;
    }

    public BigDecimal getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(BigDecimal receivedQty) {
        this.receivedQty = receivedQty;
    }

    public BigDecimal getReconciledQty() {
        return reconciledQty;
    }

    public void setReconciledQty(BigDecimal reconciledQty) {
        this.reconciledQty = reconciledQty;
    }

    public BigDecimal getRestockPct() {
        return restockPct;
    }

    public void setRestockPct(BigDecimal restockPct) {
        this.restockPct = restockPct;
    }

    public BigDecimal getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(BigDecimal selectedQty) {
        this.selectedQty = selectedQty;
    }

    public BigDecimal getShipQty() {
        return shipQty;
    }

    public void setShipQty(BigDecimal shipQty) {
        this.shipQty = shipQty;
    }

    public BigDecimal getSuppPackSize() {
        return suppPackSize;
    }

    public void setSuppPackSize(BigDecimal suppPackSize) {
        this.suppPackSize = suppPackSize;
    }

    public BigDecimal getTsfCost() {
        return tsfCost;
    }

    public void setTsfCost(BigDecimal tsfCost) {
        this.tsfCost = tsfCost;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }

    public BigDecimal getTsfPoLinkNo() {
        return tsfPoLinkNo;
    }

    public void setTsfPoLinkNo(BigDecimal tsfPoLinkNo) {
        this.tsfPoLinkNo = tsfPoLinkNo;
    }

    public BigDecimal getTsfPrice() {
        return tsfPrice;
    }

    public void setTsfPrice(BigDecimal tsfPrice) {
        this.tsfPrice = tsfPrice;
    }

    public BigDecimal getTsfQty() {
        return tsfQty;
    }

    public void setTsfQty(BigDecimal tsfQty) {
        this.tsfQty = tsfQty;
    }

    public BigDecimal getTsfSeqNo() {
        return tsfSeqNo;
    }

    public void setTsfSeqNo(BigDecimal tsfSeqNo) {
        this.tsfSeqNo = tsfSeqNo;
    }

    public String getUpdatedByRmsInd() {
        return updatedByRmsInd;
    }

    public void setUpdatedByRmsInd(String updatedByRmsInd) {
        this.updatedByRmsInd = updatedByRmsInd;
    }
}
