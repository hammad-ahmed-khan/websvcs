package com.logicinfo.oms.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "OmsCustOrdAddress.findAll", query = "select o from OmsCustOrdAddress o"),
		@NamedQuery(name = "OmsCustOrdAddress.findByOmsCustOrdNo", query = "select o from OmsCustOrdAddress o where o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverFirstName", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverFirstName=:deliverFirstName"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverLastName", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverLastName=:deliverLastName"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverPhoneNo", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverPhoneNo=:deliverPhoneNo"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverFirstNameAndLastName", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverFirstName=:deliverFirstName and o.deliverLastName=:deliverLastName"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverFirstNameAndPhoneNo", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverFirstName=:deliverFirstName and o.deliverPhoneNo=:deliverPhoneNo"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverLastNameAndPhoneNo", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverLastName=:deliverLastName  and o.deliverPhoneNo=:deliverPhoneNo"),
		@NamedQuery(name = "OmsCustOrdAddress.findByDeliverFirstNameLastNameAndPhoneNo", query = "select o.omsCustOrdNo from OmsCustOrdAddress o where o.deliverFirstName=:deliverFirstName and o.deliverLastName=:deliverLastName and o.deliverPhoneNo=:deliverPhoneNo") })
@Table(name = "OMS_CUST_ORD_ADDRESS")
public class OmsCustOrdAddress implements Serializable {
	@Column(name = "BILL_ADD_1", length = 240)
	private String billAdd1;
	@Column(name = "BILL_ADD_2", length = 240)
	private String billAdd2;
	@Column(name = "BILL_ADD_3", length = 240)
	private String billAdd3;
	@Column(name = "BILL_CITY", length = 120)
	private String billCity;
	@Column(name = "BILL_COUNTRY", length = 3)
	private String billCountry;
	@Column(name = "BILL_FIRST_NAME", length = 120)
	private String billFirstName;
	@Column(name = "BILL_LAST_NAME", length = 120)
	private String billLastName;
	@Column(name = "BILL_POST", length = 30)
	private String billPost;
	@Column(name = "BILL_STATE", length = 3)
	private String billState;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CUST_ID", nullable = false, length = 10)
	private String custId;
	@Column(name = "DELIVER_ADD_1", length = 240)
	private String deliverAdd1;
	@Column(name = "DELIVER_ADD_2", length = 240)
	private String deliverAdd2;
	@Column(name = "DELIVER_ADD_3", length = 240)
	private String deliverAdd3;
	@Column(name ="DELV_SHORT_ADDR")
	private String delvShortAddr;
	@Column(name = "DELIVER_CITY", length = 120)
	private String deliverCity;
	@Column(name = "DELIVER_COUNTRY", length = 3)
	private String deliverCountry;
	@Column(name = "DELIVER_FIRST_NAME", length = 120)
	private String deliverFirstName;
	@Column(name = "DELIVER_LAST_NAME", length = 120)
	private String deliverLastName;
	@Column(name = "DELIVER_PHONE_NO", length = 15)
	private String deliverPhoneNo;
	@Column(name = "LATITUDE")
	private BigDecimal latitude;
	@Column(name = "LONGITUDE")
	private BigDecimal longitude;
	@Column(name = "DELIVERY_ZONE")
	private String zone;
	@Column(name = "DELIVER_POST", length = 30)
	private String deliverPost;
	@Column(name = "DELIVER_STATE", length = 3)
	private String deliverState;
	@Column(name = "COMPANY_NAME")
	private String companyName;
	@Column(name = "VAT_REG_NUMBER ")
	private String vatRegNumber;
	@Column(name = "CR_NUMBER ")
	private String crNumber;
	@Id
	@Column(name = "OMS_CUST_ORD_NO", nullable = false)
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

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}
	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getDelvShortAddr() {
		return delvShortAddr;
	}

	public void setDelvShortAddr(String delvShortAddr) {
		this.delvShortAddr = delvShortAddr;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getVatRegNumber() {
		return vatRegNumber;
	}

	public void setVatRegNumber(String vatRegNumber) {
		this.vatRegNumber = vatRegNumber;
	}

	public String getCrNumber() {
		return crNumber;
	}

	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}
	
}
