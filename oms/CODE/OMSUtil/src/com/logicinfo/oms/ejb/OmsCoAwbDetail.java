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
@NamedQueries({ @NamedQuery(name = "OmsCoAwbDetail.findAll", query = "select o from OmsCoAwbDetail o") })
@Table(name = "OMS_CO_AWB_DETAIL")
@IdClass(OmsCoAwbDetailPK.class)
public class OmsCoAwbDetail implements Serializable {
	@Column(name = "AIRWAY_BILL_NO", nullable = false, length = 40)
	private String airwayBillNo;
	@Id
	@Column(name = "AWB_UPD_REQ_ID", nullable = false, length = 20)
	private String awbUpdReqId;
	@Column(name = "CREATE_DATETIME")
	private Timestamp createDatetime;
	@Id
	@Column(name = "DELIVERY_ID", nullable = false)
	private BigDecimal deliveryId;

	@Id
	@SequenceGenerator(sequenceName = "OMS_AWB_DTL_SEQ", name = "OMS_AWB_DTL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OMS_AWB_DTL_SEQ" )
	@Column(name = "ID", nullable = false)
	private BigDecimal sequenceId;

	@Column(name = "ERROR_CODE", length = 10)
	private String errorCode;
	@Column(name = "ERROR_MESSAGE", length = 100)
	private String errorMessage;

	public OmsCoAwbDetail() {
	}

	public OmsCoAwbDetail(String airwayBillNo, String awbUpdReqId, Timestamp createDatetime, BigDecimal deliveryId, String errorCode, String errorMessage) {
		this.airwayBillNo = airwayBillNo;
		this.awbUpdReqId = awbUpdReqId;
		this.createDatetime = createDatetime;
		this.deliveryId = deliveryId;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getAirwayBillNo() {
		return airwayBillNo;
	}

	public void setAirwayBillNo(String airwayBillNo) {
		this.airwayBillNo = airwayBillNo;
	}

	public String getAwbUpdReqId() {
		return awbUpdReqId;
	}

	public void setAwbUpdReqId(String awbUpdReqId) {
		this.awbUpdReqId = awbUpdReqId;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public BigDecimal getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(BigDecimal deliveryId) {
		this.deliveryId = deliveryId;
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
}
