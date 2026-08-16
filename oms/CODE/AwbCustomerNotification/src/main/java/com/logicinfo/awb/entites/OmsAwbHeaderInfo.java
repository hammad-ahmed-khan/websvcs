package com.logicinfo.awb.entites;

import java.io.Serializable;
import java.sql.Timestamp;

public class OmsAwbHeaderInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer awbId;
	
	private String trackingId;

	private String courierName;

	private String custOrderNo;

	private String emailStatusCode;

	private String smsStausCode;

	private String status;

	private Timestamp lastUpdateDatetime;

	private Timestamp createDatetime;

	private String custPhoneNo;

	private String emailId;
	
	private String source;
	
	public OmsAwbHeaderInfo() {
	}

	public Integer getAwbId() {
		return awbId;
	}

	public void setAwbId(Integer awbId) {
		this.awbId = awbId;
	}

	public String getCourierName() {
		return this.courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getCustOrderNo() {
		return this.custOrderNo;
	}

	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}

	public String getEmailStatusCode() {
		return this.emailStatusCode;
	}

	public void setEmailStatusCode(String emailStatusCode) {
		this.emailStatusCode = emailStatusCode;
	}

	public String getSmsStausCode() {
		return this.smsStausCode;
	}

	public void setSmsStausCode(String smsStausCode) {
		this.smsStausCode = smsStausCode;
	}

	public String getStatus() {
		return this.status;
	}

	public Timestamp getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCustPhoneNo() {
		return custPhoneNo;
	}

	public void setCustPhoneNo(String custPhoneNo) {
		this.custPhoneNo = custPhoneNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}