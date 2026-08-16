
package com.extra.oms.custOrder.model;

import java.sql.Date;
import java.util.List;


public class CustomerOrderResponse {

    protected String entityId;
   
    protected String applicationId;
   
    protected Date requestDatetimestamp;
    
    protected Date responseDatetimestamp;
  
    protected String customerSubOrderNo;

    protected String responseMessage;

    protected List<CustomerOrderResponseItems> customerOrderResponseItems;

    protected Long omsCustomerOrderNo;

    protected String customerOrderNo;

    protected String messageCode;

    protected String messageDesc;

    protected String messageStatus;

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

	public Date getRequestDatetimestamp() {
		return requestDatetimestamp;
	}

	public void setRequestDatetimestamp(Date requestDatetimestamp) {
		this.requestDatetimestamp = requestDatetimestamp;
	}

	public Date getResponseDatetimestamp() {
		return responseDatetimestamp;
	}

	public void setResponseDatetimestamp(Date responseDatetimestamp) {
		this.responseDatetimestamp = responseDatetimestamp;
	}

	public String getCustomerSubOrderNo() {
		return customerSubOrderNo;
	}

	public void setCustomerSubOrderNo(String customerSubOrderNo) {
		this.customerSubOrderNo = customerSubOrderNo;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<CustomerOrderResponseItems> getCustomerOrderResponseItems() {
		return customerOrderResponseItems;
	}

	public void setCustomerOrderResponseItems(List<CustomerOrderResponseItems> customerOrderResponseItems) {
		this.customerOrderResponseItems = customerOrderResponseItems;
	}

	public Long getOmsCustomerOrderNo() {
		return omsCustomerOrderNo;
	}

	public void setOmsCustomerOrderNo(Long omsCustomerOrderNo) {
		this.omsCustomerOrderNo = omsCustomerOrderNo;
	}

	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	public void setCustomerOrderNo(String customerOrderNo) {
		this.customerOrderNo = customerOrderNo;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageDesc() {
		return messageDesc;
	}

	public void setMessageDesc(String messageDesc) {
		this.messageDesc = messageDesc;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

  


}
