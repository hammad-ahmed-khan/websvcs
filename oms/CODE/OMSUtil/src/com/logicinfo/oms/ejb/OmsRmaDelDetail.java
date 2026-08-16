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
@NamedQueries( { @NamedQuery(name = "OmsRmaDelDetail.findAll", query = "select o from OmsRmaDelDetail o") ,
                 @NamedQuery(name = "OmsRmaDelDetail.findByRmaDelReqId", query = "select o from OmsRmaDelDetail o  where o.rmaDelReqId=:rmaDelReqId")})
@Table(name = "OMS_RMA_DEL_DETAIL")
@IdClass(OmsRmaDelDetailPK.class)
public class OmsRmaDelDetail implements Serializable {
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
    @Id
    @Column(name = "RMA_DEL_REQ_ID", nullable = false)
    private String rmaDelReqId;
    @Column(nullable = false, length = 15)
    private String status;

    public OmsRmaDelDetail() {
    }

    public OmsRmaDelDetail(Timestamp createDatetime, String errorCode, String errorMessage, String item,
                           BigDecimal lineNo, String rmaDelReqId, String status) {
        this.createDatetime = createDatetime;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.item = item;
        this.lineNo = lineNo;
        this.rmaDelReqId = rmaDelReqId;
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

    public String getRmaDelReqId() {
        return rmaDelReqId;
    }

    public void setRmaDelReqId(String rmaDelReqId) {
        this.rmaDelReqId = rmaDelReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
