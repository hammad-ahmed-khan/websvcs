package com.extra.oms.bean;

import java.math.BigDecimal;

public class Address {
	private String address1;
	private String address2;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String buildingNumber;
	private Integer postCode;
	private String additionalNumber;
	private Integer cityId;
	private Integer regionId;
	private Long pkAddressID;
	private Long districtID;
	private String regionName;
	private String regionNameAr;
	private String city;
	private String streetEn;
	private String streetAr;
	private String district;
	private String shortAddress;

	public String getAddress1() {
		return this.address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public BigDecimal getLatitude() {
		return this.latitude;
	}

	public BigDecimal getLongitude() {
		return this.longitude;
	}

	public String getBuildingNumber() {
		return this.buildingNumber;
	}

	public Integer getPostCode() {
		return this.postCode;
	}

	public String getAdditionalNumber() {
		return this.additionalNumber;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public Integer getRegionId() {
		return this.regionId;
	}

	public Long getPkAddressID() {
		return this.pkAddressID;
	}

	public Long getDistrictID() {
		return this.districtID;
	}

	public String getRegionName() {
		return this.regionName;
	}

	public String getRegionNameAr() {
		return this.regionNameAr;
	}

	public String getCity() {
		return this.city;
	}

	public String getStreetEn() {
		return this.streetEn;
	}

	public String getStreetAr() {
		return this.streetAr;
	}

	public String getDistrict() {
		return this.district;
	}

	public String getShortAddress() {
		return this.shortAddress;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public void setPostCode(Integer postCode) {
		this.postCode = postCode;
	}

	public void setAdditionalNumber(String additionalNumber) {
		this.additionalNumber = additionalNumber;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public void setPkAddressID(Long pkAddressID) {
		this.pkAddressID = pkAddressID;
	}

	public void setDistrictID(Long districtID) {
		this.districtID = districtID;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public void setRegionNameAr(String regionNameAr) {
		this.regionNameAr = regionNameAr;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreetEn(String streetEn) {
		this.streetEn = streetEn;
	}

	public void setStreetAr(String streetAr) {
		this.streetAr = streetAr;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setShortAddress(String shortAddress) {
		this.shortAddress = shortAddress;
	}
}
