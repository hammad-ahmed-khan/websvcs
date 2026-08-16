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
@NamedQueries({
		@NamedQuery(name = "OmsFulfillMatrixExtHead.findAll", query = "select o from OmsFulfillMatrixExtHead o"),
		@NamedQuery(name = "OmsFulfillMatrixExtHead.findCombination", query = "select o.combinationId from OmsFulfillMatrixExtHead o where o.requestorId=:reqID  and (o.scv = :deliverZone OR o.scv = 'ALL') and UPPER(o.shipClassification)=:shipClassification and UPPER(o.customerCity)=:custCity and o.modeOfDelivery=:modeOfDelv and o.marketPlaceInd = COALESCE(:marketPlaceInd, 'N') and o.applicationId = :applicationId and o.shipToStore = :shipToStore"),
		@NamedQuery(name = "OmsFulfillMatrixExtHead.findCombinationWoCity", query = "select o.combinationId from OmsFulfillMatrixExtHead o where o.requestorId=:reqID and (o.scv = :deliverZone OR o.scv = 'ALL') and UPPER(o.shipClassification)=:shipClassification and o.modeOfDelivery=:modeOfDelv and o.marketPlaceInd = COALESCE(:marketPlaceInd, 'N') and o.applicationId = :applicationId and o.shipToStore = :shipToStore") })

@Table(name = "OMS_FULFILL_MATRIX_EXT_HEAD")
public class OmsFulfillMatrixExtHead implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "CITY_TYPE", length = 6)
	private String cityType;
	@Id
	@Column(name = "COMBINATION_ID", nullable = false)
	private BigDecimal combinationId;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CREATED_BY", nullable = false, length = 40)
	private String createdBy;
	@Column(name = "CUSTOMER_CITY", length = 120)
	private String customerCity;
	@Column(name = "LAST_UPDATE_DATETIME")
	private Timestamp lastUpdateDatetime;
	@Column(name = "MODE_OF_DELIVERY", nullable = false, length = 10)
	private String modeOfDelivery;
	@Column(name = "REQUESTOR_ID", nullable = false)
	private BigDecimal requestorId;
	@Column(name = "REQUESTOR_TYPE", nullable = false, length = 20)
	private String requestorType;
	@Column(name = "SHIP_CLASSIFICATION", nullable = false, length = 6)
	private String shipClassification;
	@Column(name = "UPDATED_BY", length = 40)
	private String updatedBy;
	@Column(name = "SCV", length = 40)
	private String scv;
	@Column(name = "MARKETPLACE_IND", length = 40)
	private String marketPlaceInd;
	@Column(name = "APPLICATION_ID", length = 40)
	private String applicationId;
	@Column(name = "SHIP_TO_STORE", length = 40)
	private String shipToStore;
	public OmsFulfillMatrixExtHead() {
	}

	public OmsFulfillMatrixExtHead(String cityType, BigDecimal combinationId, Timestamp createDatetime,
			String createdBy, String customerCity, Timestamp lastUpdateDatetime, String modeOfDelivery,
			BigDecimal requestorId, String requestorType, String shipClassification, String updatedBy, String scv, String marketPlaceInd) {
		this.cityType = cityType;
		this.combinationId = combinationId;
		this.createDatetime = createDatetime;
		this.createdBy = createdBy;
		this.customerCity = customerCity;
		this.lastUpdateDatetime = lastUpdateDatetime;
		this.modeOfDelivery = modeOfDelivery;
		this.requestorId = requestorId;
		this.requestorType = requestorType;
		this.shipClassification = shipClassification;
		this.updatedBy = updatedBy;
		this.scv = scv;
		this.marketPlaceInd = marketPlaceInd;
	}

	public String getCityType() {
		return cityType;
	}

	public void setCityType(String cityType) {
		this.cityType = cityType;
	}

	public BigDecimal getCombinationId() {
		return combinationId;
	}

	public void setCombinationId(BigDecimal combinationId) {
		this.combinationId = combinationId;
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

	public String getCustomerCity() {
		return customerCity;
	}

	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}

	public Timestamp getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public String getModeOfDelivery() {
		return modeOfDelivery;
	}

	public void setModeOfDelivery(String modeOfDelivery) {
		this.modeOfDelivery = modeOfDelivery;
	}

	public BigDecimal getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(BigDecimal requestorId) {
		this.requestorId = requestorId;
	}

	public String getRequestorType() {
		return requestorType;
	}

	public void setRequestorType(String requestorType) {
		this.requestorType = requestorType;
	}

	public String getShipClassification() {
		return shipClassification;
	}

	public void setShipClassification(String shipClassification) {
		this.shipClassification = shipClassification;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getScv() {
		return scv;
	}

	public void setScv(String scv) {
		this.scv = scv;
	}

	public String getMarketPlaceInd() {
		return marketPlaceInd;
	}

	public void setMarketPlaceInd(String marketPlaceInd) {
		this.marketPlaceInd = marketPlaceInd;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getShipToStore() {
		return shipToStore;
	}

	public void setShipToStore(String shipToStore) {
		this.shipToStore = shipToStore;
	}

	

	
}
