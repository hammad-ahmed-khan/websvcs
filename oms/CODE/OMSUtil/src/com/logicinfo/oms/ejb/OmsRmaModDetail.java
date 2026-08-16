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
@NamedQueries( { @NamedQuery(name = "OmsRmaModDetail.findAll", query = "select o from OmsRmaModDetail o"),
                 @NamedQuery(name = "OmsRmaModDetail.findByRmaModReqId", query = "select o from OmsRmaModDetail o where o.rmaModReqId=:rmaModReqId")})
@Table(name = "OMS_RMA_MOD_DETAIL")
@IdClass(OmsRmaModDetailPK.class)
public class OmsRmaModDetail implements Serializable {
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "ERROR_CODE", length = 15)
    private String errorCode;
    @Column(name = "ERROR_MESSAGE", length = 200)
    private String errorMessage;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Id
    @Column(name = "LINE_NO", nullable = false)
    private BigDecimal lineNo;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Id
    @Column(name = "RMA_MOD_REQ_ID", nullable = false, length = 12)
    private String rmaModReqId;
    @Column(nullable = false, length = 15)
    private String status;

    public OmsRmaModDetail() {
    }

    public OmsRmaModDetail(Timestamp createDatetime, String errorCode, String errorMessage, String item,
                           BigDecimal lineNo, BigDecimal quantity, String rmaModReqId, String status) {
        this.createDatetime = createDatetime;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.item = item;
        this.lineNo = lineNo;
        this.quantity = quantity;
        this.rmaModReqId = rmaModReqId;
        this.status = status;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getRmaModReqId() {
        return rmaModReqId;
    }

    public void setRmaModReqId(String rmaModReqId) {
        this.rmaModReqId = rmaModReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
