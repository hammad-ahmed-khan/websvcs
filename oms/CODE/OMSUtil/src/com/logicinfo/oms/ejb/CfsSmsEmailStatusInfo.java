package com.logicinfo.oms.ejb;

import com.logicinfo.oms.beans.OMSUtilCommons;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "CfsSmsEmailStatusInfo.findAll",
query = "select o from CfsSmsEmailStatusInfo o") })
@Table(name = "CFS_SMS_EMAIL_STATUS_INFO")
public class CfsSmsEmailStatusInfo implements Serializable {
@Column(name = "CREATE_DATETIME")
private Timestamp createDatetime;
@Column(name = "CUST_ORDER_NO", length = 48)
private String custOrderNo;
@Column(name = "EMAIL_PROCESS_IND", length = 1)
private String emailProcessInd;
@Column(name = "EMAIL_STATUS_CODE", length = 1)
private String emailStatusCode;
@Id
@Column(nullable = false)
@SequenceGenerator(name = "CfsSmsEmailInfoIdGenerator", sequenceName = "CFS_SMS_EMAIL_STATUS_INFO_SEQ")
@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CfsSmsEmailInfoIdGenerator")
private BigDecimal id;
@Column(name = "LAST_UPDATE_DATETIME")
private Timestamp lastUpdateDatetime;
@Column(name = "ORDER_PICK_STATUS", length = 1)
private String orderPickStatus;
@Column(name = "SMS_PROCESS_IND", length = 1)
private String smsProcessInd;
@Column(name = "SMS_STATUS_CODE", length = 1)
private String smsStatusCode;
@Column(name = "STORE_NO")
private BigDecimal storeNo;


    
public CfsSmsEmailStatusInfo() {
}

public CfsSmsEmailStatusInfo(Timestamp createDatetime, String custOrderNo, String emailProcessInd,
String emailStatusCode, BigDecimal id, Timestamp lastUpdateDatetime,
String orderPickStatus, String smsProcessInd, String smsStatusCode,
BigDecimal storeNo) {    
this.createDatetime = createDatetime;
this.custOrderNo = custOrderNo;
this.emailProcessInd = emailProcessInd;
this.emailStatusCode = emailStatusCode;
this.id = id;
this.lastUpdateDatetime = lastUpdateDatetime;
this.orderPickStatus = orderPickStatus;
this.smsProcessInd = smsProcessInd;
this.smsStatusCode = smsStatusCode;
this.storeNo = storeNo;
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

public String getEmailProcessInd() {
return emailProcessInd;
}

public void setEmailProcessInd(String emailProcessInd) {
this.emailProcessInd = emailProcessInd;
}

public String getEmailStatusCode() {
return emailStatusCode;
}

public void setEmailStatusCode(String emailStatusCode) {
this.emailStatusCode = emailStatusCode;
}

public BigDecimal getId() {
return id;
}

public void setId(BigDecimal id) {
this.id = id;
}

public Timestamp getLastUpdateDatetime() {
return lastUpdateDatetime;
}

public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
this.lastUpdateDatetime = lastUpdateDatetime;
}

public String getOrderPickStatus() {
return orderPickStatus;
}

public void setOrderPickStatus(String orderPickStatus) {
this.orderPickStatus = orderPickStatus;
}

public String getSmsProcessInd() {
return smsProcessInd;
}

public void setSmsProcessInd(String smsProcessInd) {
this.smsProcessInd = smsProcessInd;
}

public String getSmsStatusCode() {
return smsStatusCode;
}

public void setSmsStatusCode(String smsStatusCode) {
this.smsStatusCode = smsStatusCode;
}

public BigDecimal getStoreNo() {
return storeNo;
}

public void setStoreNo(BigDecimal storeNo) {
this.storeNo = storeNo;
}
}