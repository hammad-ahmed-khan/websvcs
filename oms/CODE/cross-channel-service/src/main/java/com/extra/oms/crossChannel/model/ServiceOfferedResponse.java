package com.extra.oms.crossChannel.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceOfferedResponse {

	@JsonProperty("location")
	private String location;

	@JsonProperty("channel")
	private String channel;

	@JsonProperty("order_no")
	private String orderNo;

	@JsonProperty("email")
	private String email;

	@JsonProperty("mobile_number")
	private String mobileNumber;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("last_name")
	private String lastName;

	@JsonProperty("transaction_number")
	private String transactionNumber;

	@JsonProperty("city")
	private String city;

	@JsonProperty("area")
	private String area;

	@JsonProperty("address")
	private String address;

	@JsonProperty("order_date")
	private String orderDate;

	@JsonProperty("country")
	private String country;

	@JsonProperty("total_retail_disount")
	private BigDecimal totalRetailDisount;

	@JsonProperty("total_retail_price")
	private BigDecimal totalRetailPrice;

	@JsonProperty("total_selling_price")
	private BigDecimal totalSellingPrice;

	@JsonProperty("total_jood_discount")
	private BigDecimal totalJoodDiscount;

	@JsonProperty("coupon_discount")
	private BigDecimal couponDiscount;

	@JsonProperty("coupon_codes")
	private List<String> couponCodes;

	@JsonProperty("total_service_price")
	private BigDecimal totalServicePrice;

	@JsonProperty("total_installation_price")
	private BigDecimal totalInstallationPrice;

	@JsonProperty("total_cashBack_price")
	private BigDecimal totalCashBackPrice;

	@JsonProperty("delivery_cost")
	private BigDecimal deliveryCost;
	
	@JsonProperty("sub_cost")
	private BigDecimal subCost;

	@JsonProperty("lines")
	private List<ServiceOfferedLines> serviceOfferedLines;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public BigDecimal getTotalRetailDisount() {
		return totalRetailDisount;
	}

	public void setTotalRetailDisount(BigDecimal totalRetailDisount) {
		this.totalRetailDisount = totalRetailDisount;
	}

	public BigDecimal getTotalRetailPrice() {
		return totalRetailPrice;
	}

	public void setTotalRetailPrice(BigDecimal totalRetailPrice) {
		this.totalRetailPrice = totalRetailPrice;
	}

	public BigDecimal getTotalSellingPrice() {
		return totalSellingPrice;
	}

	public void setTotalSellingPrice(BigDecimal totalSellingPrice) {
		this.totalSellingPrice = totalSellingPrice;
	}

	public BigDecimal getTotalJoodDiscount() {
		return totalJoodDiscount;
	}

	public void setTotalJoodDiscount(BigDecimal totalJoodDiscount) {
		this.totalJoodDiscount = totalJoodDiscount;
	}

	public BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public List<String> getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(List<String> couponCodes) {
		this.couponCodes = couponCodes;
	}

	public BigDecimal getTotalServicePrice() {
		return totalServicePrice;
	}

	public void setTotalServicePrice(BigDecimal totalServicePrice) {
		this.totalServicePrice = totalServicePrice;
	}

	public BigDecimal getTotalInstallationPrice() {
		return totalInstallationPrice;
	}

	public void setTotalInstallationPrice(BigDecimal totalInstallationPrice) {
		this.totalInstallationPrice = totalInstallationPrice;
	}

	public BigDecimal getTotalCashBackPrice() {
		return totalCashBackPrice;
	}

	public void setTotalCashBackPrice(BigDecimal totalCashBackPrice) {
		this.totalCashBackPrice = totalCashBackPrice;
	}

	public BigDecimal getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(BigDecimal deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public List<ServiceOfferedLines> getServiceOfferedLines() {
		return serviceOfferedLines;
	}

	public void setServiceOfferedLines(List<ServiceOfferedLines> serviceOfferedLines) {
		this.serviceOfferedLines = serviceOfferedLines;
	}

	public BigDecimal getSubCost() {
		return subCost;
	}

	public void setSubCost(BigDecimal subCost) {
		this.subCost = subCost;
	}

	
	
	
	

}
