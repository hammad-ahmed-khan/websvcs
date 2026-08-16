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
@NamedQueries({ @NamedQuery(name = "OmsCoCancelHead.findAll", query = "select o from OmsCoCancelHead o"),
		@NamedQuery(name = "OmsCoCancelHead.findOmsCancelId", query = "select o.omsCancelId from OmsCoCancelHead o where o.custOrdNo=:custOrdNo and o.subCustOrdNo=:subCustOrdNo and o.cancelReqId=:cancelReqId  "),
		@NamedQuery(name = "OmsCoCancelHead.findByCustOrdNo", query = "select o from OmsCoCancelHead o where o.custOrdNo=:custOrdNo order by o.omsCancelId"),
		@NamedQuery(name = "OmsCoCancelHead.findByomscancelId", query = "select o from OmsCoCancelHead o where o.omsCancelId=:omsCancelId"),
		@NamedQuery(name = "OmsCoCancelHead.findByCustOrdNoforCancelledOrder", query = "select o from OmsCoCancelHead o where o.custOrdNo=:custOrdNo and o.refundCompltInd=:refundCompltInd"),
		@NamedQuery(name = "OmsCoCancelHead.findByCustOrdNoforORPOSRefundOrder", query = "select o from OmsCoCancelHead o where o.custOrdNo=:custOrdNo and o.refundCompltInd=:refundCompltInd and o.refundOption=:refundOption") })

@Table(name = "OMS_CO_CANCEL_HEAD")
@IdClass(OmsCoCancelHeadPK.class)
public class OmsCoCancelHead implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7469786049404439616L;

	@Column(name = "APPLICATION_ID", nullable = false, length = 30)
	private String applicationId;
	@Column(name = "CAN_REQ_DATETIME", nullable = false)
	private Timestamp canReqDatetime;
	@Id
	@Column(name = "CANCEL_REQ_ID", nullable = false)
	private BigDecimal cancelReqId;
	@Column(name = "CANCEL_REQST_LOC_ID", nullable = false)
	private BigDecimal cancelReqstLocId;
	@Column(length = 200)
	private String comments;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Id
	@Column(name = "CUST_ORD_NO", nullable = false, length = 48)
	private String custOrdNo;
	@Column(name = "ENTITY_ID", nullable = false, length = 30)
	private String entityId;
	@Column(name = "LAST_UPDATE_DATETIME")
	private Timestamp lastUpdateDatetime;
	@Column(name = "OMS_CANCEL_ID", nullable = false, unique = true)
	@SequenceGenerator(name = "cancelIdSeq", sequenceName = "OMS_CANCEL_ID_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cancelIdSeq")
	private BigDecimal omsCancelId;
	@Column(name = "REFUND_AMOUNT", nullable = false)
	private BigDecimal refundAmount;
	@Column(name = "REFUND_COMPLT_IND", nullable = false, length = 1)
	private String refundCompltInd;
	@Column(name = "REFUND_OPTION", nullable = false, length = 10)
	private String refundOption;
	@Column(nullable = false, length = 1)
	private String status;
	@Id
	@Column(name = "SUB_CUST_ORD_NO", nullable = false, length = 3)
	private String subCustOrdNo;

	@Column(name = "EIN_PROCESS_FLAG", nullable = true)
	private String eInvProcessFlag;

	public OmsCoCancelHead() {
	}

	public OmsCoCancelHead(String applicationId, Timestamp canReqDatetime, BigDecimal cancelReqId, BigDecimal cancelReqstLocId, String comments, Timestamp createDatetime, String custOrdNo,
			String entityId, Timestamp lastUpdateDatetime, BigDecimal omsCancelId, BigDecimal refundAmount, String refundCompltInd, String refundOption, String status, String subCustOrdNo) {
		this.applicationId = applicationId;
		this.canReqDatetime = canReqDatetime;
		this.cancelReqId = cancelReqId;
		this.cancelReqstLocId = cancelReqstLocId;
		this.comments = comments;
		this.createDatetime = createDatetime;
		this.custOrdNo = custOrdNo;
		this.entityId = entityId;
		this.lastUpdateDatetime = lastUpdateDatetime;
		this.omsCancelId = omsCancelId;
		this.refundAmount = refundAmount;
		this.refundCompltInd = refundCompltInd;
		this.refundOption = refundOption;
		this.status = status;
		this.subCustOrdNo = subCustOrdNo;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Timestamp getCanReqDatetime() {
		return canReqDatetime;
	}

	public void setCanReqDatetime(Timestamp canReqDatetime) {
		this.canReqDatetime = canReqDatetime;
	}

	public BigDecimal getCancelReqId() {
		return cancelReqId;
	}

	public void setCancelReqId(BigDecimal cancelReqId) {
		this.cancelReqId = cancelReqId;
	}

	public BigDecimal getCancelReqstLocId() {
		return cancelReqstLocId;
	}

	public void setCancelReqstLocId(BigDecimal cancelReqstLocId) {
		this.cancelReqstLocId = cancelReqstLocId;
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

	public String getCustOrdNo() {
		return custOrdNo;
	}

	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Timestamp getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public BigDecimal getOmsCancelId() {
		return omsCancelId;
	}

	public void setOmsCancelId(BigDecimal omsCancelId) {
		this.omsCancelId = omsCancelId;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefindAmount(BigDecimal refundAmount) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubCustOrdNo() {
		return subCustOrdNo;
	}

	public void setSubCustOrdNo(String subCustOrdNo) {
		this.subCustOrdNo = subCustOrdNo;
	}

	public String geteInvProcessFlag() {
		return eInvProcessFlag;
	}

	public void seteInvProcessFlag(String eInvProcessFlag) {
		this.eInvProcessFlag = eInvProcessFlag;
	}
}
