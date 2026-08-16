package com.logicinfo.oms.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "OmsBackOrderDtl.findAll", query = "select o from OmsBackOrderDtl o"),
		@NamedQuery(name = "OmsBackOrderDtl.findBackOrders", query = "select o from OmsBackOrderDtl o where  o.backorderStatus<>'S' and o.fulInvAvlDate<=:fulInvAvlDate "),
		@NamedQuery(name = "OmsBackOrderDtl.findByOmsCustOrdNo", query = "select o from OmsBackOrderDtl o where  o.omsCustOrdNo=:omsCustOrdNo ORDER BY o.fulfillOrderNo desc"),
		@NamedQuery(name = "OmsBackOrderDtl.findByOmsCustOrdNoAndLinNo", query = "select o from OmsBackOrderDtl o where  o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo and o.sourceQty  > o.fulfillQty"),
		@NamedQuery(name = "OmsBackOrderDtl.findAssignedInvOrders", query = "select SUM(o.sourceQty) from OmsBackOrderDtl o where o.sourceLoc=:sourceLoc and o.item=:item and  o.backorderStatus<>'S' "),
		@NamedQuery(name = "OmsBackOrderDtl.findbyomsCustOrderNoItemLineNo", query = "select o from OmsBackOrderDtl o where o.omsCustOrdNo=:omsCustOrdNo and o.item=:item and o.lineNo=:lineNo"),
		@NamedQuery(name = "OmsBackOrderDtl.findByOmsCustOrdNoAndLinNoAndLoc", query = "select o from OmsBackOrderDtl o where  o.omsCustOrdNo=:omsCustOrdNo and o.lineNo=:lineNo and o.sourceLoc=:sourceLoc")

})
@NamedNativeQueries({	
	@NamedNativeQuery(name = "OmsBackOrderDtl.findOmsCustOrdNoForBackOrder", query = "SELECT DISTINCT(O.OMS_CUST_ORD_NO) FROM OMS_BACK_ORDER_DTL O WHERE O.BACKORDER_STATUS <> 'S' AND BAT_PK_STATUS = 'N' ORDER BY O.OMS_CUST_ORD_NO")
})
@Table(name = "OMS_BACK_ORDER_DTL")
@IdClass(OmsBackOrderDtlPK.class)
public class OmsBackOrderDtl implements Serializable {
	@Column(name = "BACKORDER_STATUS", nullable = false, length = 3)
	private String backorderStatus;
	@Column(name = "CONF_TS")
	private Timestamp confTs;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CREATED_BY", nullable = false, length = 40)
	private String createdBy;
	@Column(name = "FUL_INV_AVL_DATE", nullable = false)
	private Timestamp fulInvAvlDate;
	@Column(name = "FULFILL_LOC", nullable = false)
	private BigDecimal fulfillLoc;
	@Column(name = "FULFILL_LOC_TYPE", nullable = false, length = 2)
	private String fulfillLocType;
	@Column(name = "FULFILL_ORDER_NO")
	private BigDecimal fulfillOrderNo;
	@Column(name = "FULFILL_QTY", nullable = false)
	private BigDecimal fulfillQty;
	@Column(name = "INITIATE_TS")
	private Timestamp initiateTs;
	@Id
	@Column(nullable = false, length = 25)
	private String item;
	@Id
	@Column(name = "LINE_NO", nullable = false)
	private BigDecimal lineNo;
	@Id
	@Column(name = "OMS_CUST_ORD_NO", nullable = false)
	private BigDecimal omsCustOrdNo;

	@Id
	@Column(name = "SOURCE_LOC", nullable = false)
	private BigDecimal sourceLoc;
	@Column(name = "SOURCE_LOC_TYPE", length = 2)
	private String sourceLocType;
	@Column(name = "SOURCE_QTY")
	private BigDecimal sourceQty;
	@Column(name = "COMBINATION_ID")
	private BigDecimal combinationId;

	public OmsBackOrderDtl() {
	}

	public OmsBackOrderDtl(String backorderStatus, Timestamp confTs, Timestamp createDatetime, String createdBy, Timestamp fulInvAvlDate, BigDecimal fulfillLoc, String fulfillLocType,
			BigDecimal fulfillOrderNo, BigDecimal fulfillQty, Timestamp initiateTs, String item, BigDecimal lineNo, BigDecimal omsCustOrdNo, BigDecimal sourceLoc, String sourceLocType,
			BigDecimal sourceQty, BigDecimal combinationId) {
		this.backorderStatus = backorderStatus;
		this.confTs = confTs;
		this.createDatetime = createDatetime;
		this.createdBy = createdBy;
		this.fulInvAvlDate = fulInvAvlDate;
		this.fulfillLoc = fulfillLoc;
		this.fulfillLocType = fulfillLocType;
		this.fulfillOrderNo = fulfillOrderNo;
		this.fulfillQty = fulfillQty;
		this.initiateTs = initiateTs;
		this.item = item;
		this.lineNo = lineNo;
		this.omsCustOrdNo = omsCustOrdNo;

		this.sourceLoc = sourceLoc;
		this.sourceLocType = sourceLocType;
		this.sourceQty = sourceQty;
		this.combinationId = combinationId;
	}

	public String getBackorderStatus() {
		return backorderStatus;
	}

	public void setBackorderStatus(String backorderStatus) {
		this.backorderStatus = backorderStatus;
	}

	public Timestamp getConfTs() {
		return confTs;
	}

	public void setConfTs(Timestamp confTs) {
		this.confTs = confTs;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getFulInvAvlDate() {
		return fulInvAvlDate;
	}

	public void setFulInvAvlDate(Timestamp fulInvAvlDate) {
		this.fulInvAvlDate = fulInvAvlDate;
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

	public BigDecimal getFulfillQty() {
		return fulfillQty;
	}

	public void setFulfillQty(BigDecimal fulfillQty) {
		this.fulfillQty = fulfillQty;
	}

	public Timestamp getInitiateTs() {
		return initiateTs;
	}

	public void setInitiateTs(Timestamp initiateTs) {
		this.initiateTs = initiateTs;
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

	public BigDecimal getOmsCustOrdNo() {
		return omsCustOrdNo;
	}

	public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		this.omsCustOrdNo = omsCustOrdNo;
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

	public BigDecimal getSourceQty() {
		return sourceQty;
	}

	public void setSourceQty(BigDecimal sourceQty) {
		this.sourceQty = sourceQty;
	}

	public void setCombinationId(BigDecimal combinationId) {
		this.combinationId = combinationId;
	}

	public BigDecimal getCombinationId() {
		return combinationId;
	}
}
