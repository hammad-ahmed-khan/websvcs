
package com.extra.oms.custOrder.model;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class CustomerOrder {

	protected String entityId;
	protected String applicationId;
	protected String comments;
	protected XMLGregorianCalendar requestDatetimestamp;
	protected String orderType;
	protected String customerOrderNo;
	protected String customerSubOrderNo;
	protected String customerId;
	protected String deliveryType;
	protected XMLGregorianCalendar consumerDeliveryDate;
	protected XMLGregorianCalendar consumerDeliveryTime;
	protected XMLGregorianCalendar createDate;
	protected Long pickLoc;
	protected CustomerOrderAddress customerOrderAddress;
	protected List<CustomerOrderItems> customerOrderItems;
	protected List<CustomerOrderTenders> customerOrderTenders;
	protected long orderRequestorId;
	protected String customerLang;
	protected String customerPhoneNo;
	protected String orderCreateReserveInd;
	protected String payInStoreInd;
	protected String firstName;
	protected String lastName;
	public String cartNumber;
	protected String contractFlag;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public XMLGregorianCalendar getRequestDatetimestamp() {
		return requestDatetimestamp;
	}

	public void setRequestDatetimestamp(XMLGregorianCalendar requestDatetimestamp) {
		this.requestDatetimestamp = requestDatetimestamp;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getCustomerSubOrderNo() {
		return customerSubOrderNo;
	}

	public void setCustomerSubOrderNo(String customerSubOrderNo) {
		this.customerSubOrderNo = customerSubOrderNo;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public XMLGregorianCalendar getConsumerDeliveryDate() {
		return consumerDeliveryDate;
	}

	public void setConsumerDeliveryDate(XMLGregorianCalendar consumerDeliveryDate) {
		this.consumerDeliveryDate = consumerDeliveryDate;
	}

	public XMLGregorianCalendar getConsumerDeliveryTime() {
		return consumerDeliveryTime;
	}

	public void setConsumerDeliveryTime(XMLGregorianCalendar consumerDeliveryTime) {
		this.consumerDeliveryTime = consumerDeliveryTime;
	}

	public XMLGregorianCalendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(XMLGregorianCalendar createDate) {
		this.createDate = createDate;
	}

	public Long getPickLoc() {
		return pickLoc;
	}

	public void setPickLoc(Long pickLoc) {
		this.pickLoc = pickLoc;
	}

	public CustomerOrderAddress getCustomerOrderAddress() {
		return customerOrderAddress;
	}

	public void setCustomerOrderAddress(CustomerOrderAddress customerOrderAddress) {
		this.customerOrderAddress = customerOrderAddress;
	}

	public List<CustomerOrderItems> getCustomerOrderItems() {
		return customerOrderItems;
	}

	public void setCustomerOrderItems(List<CustomerOrderItems> customerOrderItems) {
		this.customerOrderItems = customerOrderItems;
	}

	public List<CustomerOrderTenders> getCustomerOrderTenders() {
		return customerOrderTenders;
	}

	public void setCustomerOrderTenders(List<CustomerOrderTenders> customerOrderTenders) {
		this.customerOrderTenders = customerOrderTenders;
	}

	public long getOrderRequestorId() {
		return orderRequestorId;
	}

	public void setOrderRequestorId(long orderRequestorId) {
		this.orderRequestorId = orderRequestorId;
	}

	public String getCustomerLang() {
		return customerLang;
	}

	public void setCustomerLang(String customerLang) {
		this.customerLang = customerLang;
	}

	public String getCustomerPhoneNo() {
		return customerPhoneNo;
	}

	public void setCustomerPhoneNo(String customerPhoneNo) {
		this.customerPhoneNo = customerPhoneNo;
	}

	public String getOrderCreateReserveInd() {
		return orderCreateReserveInd;
	}

	public void setOrderCreateReserveInd(String orderCreateReserveInd) {
		this.orderCreateReserveInd = orderCreateReserveInd;
	}

	public String getPayInStoreInd() {
		return payInStoreInd;
	}

	public void setPayInStoreInd(String payInStoreInd) {
		this.payInStoreInd = payInStoreInd;
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

	public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
	}

	public String getContractFlag() {
		return contractFlag;
	}

	public void setContractFlag(String contractFlag) {
		this.contractFlag = contractFlag;
	}

}
