
package com.extra.oms.custOrder.model;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;


public class CustomerOrderTenders {

    protected String tenderType;
    protected BigDecimal tenderAmount;
    protected String ccNo;
    protected String ccAuthNo;
    protected String ccRefId;
    protected String ccAuthSrc;
    protected String ccCardholderVerf;
    protected XMLGregorianCalendar ccExpDate;
    protected String ccEntryMode;
    protected String ccTermId;
    protected String ccSpecCond;
    protected long tenderTypeId;
    protected Long tenderRefId;
    protected String pymtStatusInd;
    
	public String getTenderType() {
		return tenderType;
	}
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	public BigDecimal getTenderAmount() {
		return tenderAmount;
	}
	public void setTenderAmount(BigDecimal tenderAmount) {
		this.tenderAmount = tenderAmount;
	}
	public String getCcNo() {
		return ccNo;
	}
	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}
	public String getCcAuthNo() {
		return ccAuthNo;
	}
	public void setCcAuthNo(String ccAuthNo) {
		this.ccAuthNo = ccAuthNo;
	}
	public String getCcRefId() {
		return ccRefId;
	}
	public void setCcRefId(String ccRefId) {
		this.ccRefId = ccRefId;
	}
	public String getCcAuthSrc() {
		return ccAuthSrc;
	}
	public void setCcAuthSrc(String ccAuthSrc) {
		this.ccAuthSrc = ccAuthSrc;
	}
	public String getCcCardholderVerf() {
		return ccCardholderVerf;
	}
	public void setCcCardholderVerf(String ccCardholderVerf) {
		this.ccCardholderVerf = ccCardholderVerf;
	}
	public XMLGregorianCalendar getCcExpDate() {
		return ccExpDate;
	}
	public void setCcExpDate(XMLGregorianCalendar ccExpDate) {
		this.ccExpDate = ccExpDate;
	}
	public String getCcEntryMode() {
		return ccEntryMode;
	}
	public void setCcEntryMode(String ccEntryMode) {
		this.ccEntryMode = ccEntryMode;
	}
	public String getCcTermId() {
		return ccTermId;
	}
	public void setCcTermId(String ccTermId) {
		this.ccTermId = ccTermId;
	}
	public String getCcSpecCond() {
		return ccSpecCond;
	}
	public void setCcSpecCond(String ccSpecCond) {
		this.ccSpecCond = ccSpecCond;
	}
	public long getTenderTypeId() {
		return tenderTypeId;
	}
	public void setTenderTypeId(long tenderTypeId) {
		this.tenderTypeId = tenderTypeId;
	}
	public Long getTenderRefId() {
		return tenderRefId;
	}
	public void setTenderRefId(Long tenderRefId) {
		this.tenderRefId = tenderRefId;
	}
	public String getPymtStatusInd() {
		return pymtStatusInd;
	}
	public void setPymtStatusInd(String pymtStatusInd) {
		this.pymtStatusInd = pymtStatusInd;
	}

   
}
