package com.logicinfo.awb.entites;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;


public class OmsCustOrdAddress implements Serializable {
	
	private String billAdd1;
	
	private String billAdd2;
	
	private String billAdd3;
	
	private String billCity;
	
	private String billCountry;
	
	private String billFirstName;
	
	private String billLastName;
	
	private String billPost;
	
	private String billState;
	
	private Timestamp createDatetime;
	
	private String custId;
	
	private String deliverAdd1;
	
	private String deliverAdd2;
	
	private String deliverAdd3;
	
	private String deliverCity;
	
	private String deliverCountry;
	
	private String deliverFirstName;
	
	private String deliverLastName;
	
	private String deliverPhoneNo;
	
	private String deliverPost;
	
	private String deliverState;
	
	private BigDecimal omsCustOrdNo;

	public OmsCustOrdAddress() {
	}

	public OmsCustOrdAddress(String billAdd1, String billAdd2, String billAdd3, String billCity, String billCountry,
			String billFirstName, String billLastName, String billPost, String billState, Timestamp createDatetime,
			String custId, String deliverAdd1, String deliverAdd2, String deliverAdd3, String deliverCity,
			String deliverCountry, String deliverFirstName, String deliverLastName, String deliverPhoneNo,
			String deliverPost, String deliverState, BigDecimal omsCustOrdNo) {
		this.billAdd1 = billAdd1;
		this.billAdd2 = billAdd2;
		this.billAdd3 = billAdd3;
		this.billCity = billCity;
		this.billCountry = billCountry;
		this.billFirstName = billFirstName;
		this.billLastName = billLastName;
		this.billPost = billPost;
		this.billState = billState;
		this.createDatetime = createDatetime;
		this.custId = custId;
		this.deliverAdd1 = deliverAdd1;
		this.deliverAdd2 = deliverAdd2;
		this.deliverAdd3 = deliverAdd3;
		this.deliverCity = deliverCity;
		this.deliverCountry = deliverCountry;
		this.deliverFirstName = deliverFirstName;
		this.deliverLastName = deliverLastName;
		this.deliverPhoneNo = deliverPhoneNo;
		this.deliverPost = deliverPost;
		this.deliverState = deliverState;
		this.omsCustOrdNo = omsCustOrdNo;
	}

	public String getBillAdd1() {
		return billAdd1;
	}

	public void setBillAdd1(String billAdd1) {
		this.billAdd1 = billAdd1;
	}

	public String getBillAdd2() {
		return billAdd2;
	}

	public void setBillAdd2(String billAdd2) {
		this.billAdd2 = billAdd2;
	}

	public String getBillAdd3() {
		return billAdd3;
	}

	public void setBillAdd3(String billAdd3) {
		this.billAdd3 = billAdd3;
	}

	public String getBillCity() {
		return billCity;
	}

	public void setBillCity(String billCity) {
		this.billCity = billCity;
	}

	public String getBillCountry() {
		return billCountry;
	}

	public void setBillCountry(String billCountry) {
		this.billCountry = billCountry;
	}

	public String getBillFirstName() {
		return billFirstName;
	}

	public void setBillFirstName(String billFirstName) {
		this.billFirstName = billFirstName;
	}

	public String getBillLastName() {
		return billLastName;
	}

	public void setBillLastName(String billLastName) {
		this.billLastName = billLastName;
	}

	public String getBillPost() {
		return billPost;
	}

	public void setBillPost(String billPost) {
		this.billPost = billPost;
	}

	public String getBillState() {
		return billState;
	}

	public void setBillState(String billState) {
		this.billState = billState;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getDeliverAdd1() {
		return deliverAdd1;
	}

	public void setDeliverAdd1(String deliverAdd1) {
		this.deliverAdd1 = deliverAdd1;
	}

	public String getDeliverAdd2() {
		return deliverAdd2;
	}

	public void setDeliverAdd2(String deliverAdd2) {
		this.deliverAdd2 = deliverAdd2;
	}

	public String getDeliverAdd3() {
		return deliverAdd3;
	}

	public void setDeliverAdd3(String deliverAdd3) {
		this.deliverAdd3 = deliverAdd3;
	}

	public String getDeliverCity() {
		return deliverCity;
	}

	public void setDeliverCity(String deliverCity) {
		this.deliverCity = deliverCity;
	}

	public String getDeliverCountry() {
		return deliverCountry;
	}

	public void setDeliverCountry(String deliverCountry) {
		this.deliverCountry = deliverCountry;
	}

	public String getDeliverFirstName() {
		return deliverFirstName;
	}

	public void setDeliverFirstName(String deliverFirstName) {
		this.deliverFirstName = deliverFirstName;
	}

	public String getDeliverLastName() {
		return deliverLastName;
	}

	public void setDeliverLastName(String deliverLastName) {
		this.deliverLastName = deliverLastName;
	}

	public String getDeliverPhoneNo() {
		return deliverPhoneNo;
	}

	public void setDeliverPhoneNo(String deliverPhoneNo) {
		this.deliverPhoneNo = deliverPhoneNo;
	}

	public String getDeliverPost() {
		return deliverPost;
	}

	public void setDeliverPost(String deliverPost) {
		this.deliverPost = deliverPost;
	}

	public String getDeliverState() {
		return deliverState;
	}

	public void setDeliverState(String deliverState) {
		this.deliverState = deliverState;
	}

	public BigDecimal getOmsCustOrdNo() {
		return omsCustOrdNo;
	}

	public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		this.omsCustOrdNo = omsCustOrdNo;
	}
}
