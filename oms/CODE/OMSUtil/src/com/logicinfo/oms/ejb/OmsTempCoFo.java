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
@NamedQueries( { @NamedQuery(name = "OmsTempCoFo.findAll", query = "select o from OmsTempCoFo o"),
                 @NamedQuery(name = "OmsTempCoFo.findSADAD",
                             query = "select o.item,o.sourceLocId,o.orderQty,o.sourceLocationType,o.foConfQty,o.fulfillLocId from OmsTempCoFo o where o.omsCustOrdNo=:custNo"),
                 @NamedQuery(name = "OmsTempCoFo.findByOmsCustOrdNo",
                             query = "select o from OmsTempCoFo o where o.omsCustOrdNo=:omsCustOrdNo"),
                 @NamedQuery(name = "OmsTempCoFo.findDistinctLocs",
                             query = "select DISTINCT o.sourceLocId,o.fulfillLocId from OmsTempCoFo o where o.omsCustOrdNo=:custNo"),
                 @NamedQuery(name = "OmsTempCoFo.findByLocation",
                             query = "select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo and o.sourceLocId=:sourceLocId and o.fulfillLocId=:fulfillLocId"),
                 @NamedQuery(name = "OmsTempCoFo.findByResponseCode",
                             query = "select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo  and o.rmsResponseCode=:rmsResponseCode  "),
                 @NamedQuery(name = "OmsTempCoFo.findByStatus",
                             query = "select o from OmsTempCoFo o where o.omsCustOrdNo=:custNo  and o.status=:status ") })

@Table(name = "OMS_TEMP_CO_FO")
@IdClass(OmsTempCoFoPK.class)
public class OmsTempCoFo implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "FO_CONF_QTY")
    private BigDecimal foConfQty;
    @Column(name = "FULFILL_LOC_ID")
    private BigDecimal fulfillLocId;
    @Column(name = "FULFILL_LOCATION_TYPE", length = 2)
    private String fulfillLocationType;
    @Column(name = "FULFILL_ORDER_NO")
    @SequenceGenerator(name = "omsFulfillOrdNoSeq", sequenceName = "OMS_FUFILL_ORDER_NO_SEQ", allocationSize = 1,
                       initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "omsFulfillOrdNoSeq")
    private BigDecimal fulfillOrderNo;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "ORDER_QTY", nullable = false)
    private BigDecimal orderQty;
    @Column(name = "PROCESSING_APP", length = 15)
    private String processingApp;
    @Column(name = "RMS_ERROR_MSG", length = 200)
    private String rmsErrorMsg;
    @Column(name = "RMS_RESPONSE_CODE", length = 1)
    private String rmsResponseCode;
    @Id
    @Column(name = "SOURCE_LOC_ID", nullable = false)
    private BigDecimal sourceLocId;
    @Column(name = "SOURCE_LOCATION_TYPE", length = 2)
    private String sourceLocationType;
    @Column(length = 15)
    private String status;
    private BigDecimal lineNo;
    private BigDecimal combinationId;
    @Column(name = "VIRTUAL_WH")
    private BigDecimal virtualWH;
    

    public OmsTempCoFo() {
    }

    public OmsTempCoFo(Timestamp createDatetime, BigDecimal foConfQty, BigDecimal fulfillLocId,
                       String fulfillLocationType, BigDecimal fulfillOrderNo, String item, BigDecimal omsCustOrdNo,
                       BigDecimal orderQty, String processingApp, String rmsErrorMsg, String rmsResponseCode,
                       BigDecimal sourceLocId, String sourceLocationType, String status) {
        this.createDatetime = createDatetime;
        this.foConfQty = foConfQty;
        this.fulfillLocId = fulfillLocId;
        this.fulfillLocationType = fulfillLocationType;
        this.fulfillOrderNo = fulfillOrderNo;
        this.item = item;
        this.omsCustOrdNo = omsCustOrdNo;
        this.orderQty = orderQty;
        this.processingApp = processingApp;
        this.rmsErrorMsg = rmsErrorMsg;
        this.rmsResponseCode = rmsResponseCode;
        this.sourceLocId = sourceLocId;
        this.sourceLocationType = sourceLocationType;
        this.status = status;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getFoConfQty() {
        return foConfQty;
    }

    public void setFoConfQty(BigDecimal foConfQty) {
        this.foConfQty = foConfQty;
    }

    public BigDecimal getFulfillLocId() {
        return fulfillLocId;
    }

    public void setFulfillLocId(BigDecimal fulfillLocId) {
        this.fulfillLocId = fulfillLocId;
    }

    public String getFulfillLocationType() {
        return fulfillLocationType;
    }

    public void setFulfillLocationType(String fulfillLocationType) {
        this.fulfillLocationType = fulfillLocationType;
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

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public String getProcessingApp() {
        return processingApp;
    }

    public void setProcessingApp(String processingApp) {
        this.processingApp = processingApp;
    }

    public String getRmsErrorMsg() {
        return rmsErrorMsg;
    }

    public void setRmsErrorMsg(String rmsErrorMsg) {
        this.rmsErrorMsg = rmsErrorMsg;
    }

    public String getRmsResponseCode() {
        return rmsResponseCode;
    }

    public void setRmsResponseCode(String rmsResponseCode) {
        this.rmsResponseCode = rmsResponseCode;
    }

    public BigDecimal getSourceLocId() {
        return sourceLocId;
    }

    public void setSourceLocId(BigDecimal sourceLocId) {
        this.sourceLocId = sourceLocId;
    }

    public String getSourceLocationType() {
        return sourceLocationType;
    }

    public void setSourceLocationType(String sourceLocationType) {
        this.sourceLocationType = sourceLocationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setCombinationId(BigDecimal combinationId) {
        this.combinationId = combinationId;
    }

    public BigDecimal getCombinationId() {
        return combinationId;
    }

    public void setVirtualWH(BigDecimal virtualWH) {
        this.virtualWH = virtualWH;
    }

    public BigDecimal getVirtualWH() {
        return virtualWH;
    }
}
