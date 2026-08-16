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
@NamedQueries( { @NamedQuery(name = "OmsRmaReq.findAll", query = "select o from OmsRmaReq o"),
                 @NamedQuery(name = "OmsRmaReq.findRmaId",
                             query = "select o.rmaId from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.rmaReqId=:rmaReqId and o.status!='F'"),
                 @NamedQuery(name = "OmsRmaReq.findByOmsCustOrdNoAndRmaId",
                             query = "select o from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.rmaReqId=:rmaReqId and o.status!='F' "),
                 @NamedQuery(name = "OmsRmaReq.findByOmsRmaId",
                             query = "select o from OmsRmaReq o where o.rmaId=:rmaId "),
                 @NamedQuery(name = "OmsRmaReq.findByOmscustOrdNo",
                             query = "select o from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.status!='F'"),
                 @NamedQuery(name = "OmsRmaReq.findByOmscustOrdNoforRmaReturn",
                             query = "select o from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.refundCompltInd='Y' and o.status!='F'"),
                 @NamedQuery(name = "OmsRmaReq.findByOmscustOrdNoforORPOSRefund",
                             query = "select o from OmsRmaReq o where o.omsCustOrdNo=:omsCustOrdNo and o.returnStatus='Y' and o.refundCompltInd='N' and o.refundOption='ORPOS' and  o.status!='F'")
                 })

@Table(name = "OMS_RMA_REQ")
@IdClass(OmsRmaReqPK.class)
public class OmsRmaReq implements Serializable {
    @Column(nullable = false, length = 200)
    private String comments;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CUST_ORDER_NO", nullable = false, length = 48)
    private String custOrderNo;
    @Column(name = "LAST_UPDATE_DATETIME")
    private Timestamp lastUpdateDatetime;
    @Id
    @Column(name = "OMS_CUST_ORD_NO", nullable = false)
    private BigDecimal omsCustOrdNo;
    @Column(name = "ORIG_REFUND_AMT")
    private BigDecimal origRefundAmt;
    @Column(name = "REFUND_AMOUNT", nullable = false)
    private BigDecimal refundAmount;
    @Column(name = "REFUND_COMPLT_IND", nullable = false, length = 1)
    private String refundCompltInd;
    @Column(name = "REFUND_OPTION", nullable = false, length = 10)
    private String refundOption;
    @Column(name = "RESTOCK_AMOUNT", length = 12)
    private BigDecimal restockAmount;
    @Column(name = "RESTOCK_PERCENTAGE")
    private BigDecimal restockPercentage;
    @Column(name = "RETURN_DATE_TIME", nullable = false)
    private Timestamp returnDateTime;
    @Column(name = "RETURN_LOC_ID", nullable = false)
    private BigDecimal returnLocId;
    @Column(name = "RETURN_STATUS", nullable = false, length = 1)
    private String returnStatus;
    @Column(name = "RMA_ID", nullable = false, unique = true)
    @SequenceGenerator(name = "rmaIdSeq", sequenceName = "OMS_RMA_ID_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "rmaIdSeq" )
    private BigDecimal rmaId;
    @Id
    @Column(name = "RMA_REQ_ID", nullable = false)
    private String rmaReqId;
    @Column(nullable = false, length = 15)
    private String status;
    @Column(name = "SUB_CUST_ORDER_NO", nullable = false, length = 3)
    private String subCustOrderNo;
    @Column(name = "REASON_CODE", nullable = false)
    private Integer reasonCode;
    @Column(name = "REASON", nullable = false)
    private String reason;
    


    public OmsRmaReq() {
    }

    public OmsRmaReq(String comments, Timestamp createDatetime, String custOrderNo, Timestamp lastUpdateDatetime,
                     BigDecimal omsCustOrdNo, BigDecimal origRefundAmt, BigDecimal refundAmount,
                     String refundCompltInd, String refundOption, BigDecimal restockAmount, BigDecimal restockPercentage,
                     Timestamp returnDateTime, BigDecimal returnLocId, String returnStatus, BigDecimal rmaId,
                     String rmaReqId, String status, String subCustOrderNo) {
        this.comments = comments;
        this.createDatetime = createDatetime;
        this.custOrderNo = custOrderNo;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsCustOrdNo = omsCustOrdNo;
        this.origRefundAmt = origRefundAmt;
        this.refundAmount = refundAmount;
        this.refundCompltInd = refundCompltInd;
        this.refundOption = refundOption;
        this.restockAmount = restockAmount;
        this.restockPercentage = restockPercentage;
        this.returnDateTime = returnDateTime;
        this.returnLocId = returnLocId;
        this.returnStatus = returnStatus;
        this.rmaId = rmaId;
        this.rmaReqId = rmaReqId;
        this.status = status;
        this.subCustOrderNo = subCustOrderNo;
    }

    public Integer getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public BigDecimal getOrigRefundAmt() {
        return origRefundAmt;
    }

    public void setOrigRefundAmt(BigDecimal origRefundAmt) {
        this.origRefundAmt = origRefundAmt;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundCompltInd() {
        return refundCompltInd;
    }

    public void setRefundCompltInd(String refundCompltInd) {
        this.refundCompltInd = refundCompltInd;
    }

    public String getRefundOption() {
        return refundOption;
    }

    public void setRefundOption(String refundOption) {
        this.refundOption = refundOption;
    }

    public BigDecimal getRestockAmount() {
        return restockAmount;
    }

    public void setRestockAmount(BigDecimal restockAmount) {
        this.restockAmount = restockAmount;
    }

    public BigDecimal getRestockPercentage() {
        return restockPercentage;
    }

    public void setRestockPercentage(BigDecimal restockPercentage) {
        this.restockPercentage = restockPercentage;
    }

    public Timestamp getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(Timestamp returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public BigDecimal getReturnLocId() {
        return returnLocId;
    }

    public void setReturnLocId(BigDecimal returnLocId) {
        this.returnLocId = returnLocId;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public BigDecimal getRmaId() {
        return rmaId;
    }

    public void setRmaId(BigDecimal rmaId) {
        this.rmaId = rmaId;
    }

    public String getRmaReqId() {
        return rmaReqId;
    }

    public void setRmaReqId(String rmaReqId) {
        this.rmaReqId = rmaReqId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(String subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
