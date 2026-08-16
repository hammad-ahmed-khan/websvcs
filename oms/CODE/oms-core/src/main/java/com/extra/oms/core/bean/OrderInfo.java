/**
 * 
 */
package com.extra.oms.core.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.extra.oms.common.adapter.DateAdapter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.JsonAdapter;

/**
 * @author aibrahim
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInfo {

	private String orderNumber;

	private String omsOrderNumber;

	private String customerName;

	private String customerMobile;

	private String countryName;

	private Long picLocation;

	private String picLocationName;

	private String paymentType;

	private String paymentStatusCode;

	private String paymentStatus;

	private String deliveryType;

	private BigDecimal tenderAmount;

	private String status;

	@JsonAdapter(DateAdapter.class)
	private Date createdDate;

	@JsonAdapter(DateAdapter.class)
	private Date closedDate;

	private boolean hasCancellation;

	private boolean hasReturn;

	private List<OrderItemDetail> items;

	public boolean isHasCancellation() {
		return hasCancellation;
	}

	public void setHasCancellation(boolean hasCancellation) {
		this.hasCancellation = hasCancellation;
	}

	public boolean isHasReturn() {
		return hasReturn;
	}

	public void setHasReturn(boolean hasReturn) {
		this.hasReturn = hasReturn;
	}

	private CustomerAddressInfo addressInfo; 

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOmsOrderNumber() {
		return omsOrderNumber;
	}

	public void setOmsOrderNumber(String omsOrderNumber) {
		this.omsOrderNumber = omsOrderNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getPicLocation() {
		return picLocation;
	}

	public void setPicLocation(Long picLocation) {
		this.picLocation = picLocation;
	}

	public String getPicLocationName() {
		return picLocationName;
	}

	public void setPicLocationName(String picLocationName) {
		this.picLocationName = picLocationName;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public BigDecimal getTenderAmount() {
		return tenderAmount;
	}

	public void setTenderAmount(BigDecimal tenderAmount) {
		this.tenderAmount = tenderAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}

	public String getPaymentStatusCode() {
		return paymentStatusCode;
	}

	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}

	public CustomerAddressInfo getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(CustomerAddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	public List<OrderItemDetail> getItems() {
		return items;
	}

	public void setItems(List<OrderItemDetail> items) {
		this.items = items;
	}
}
