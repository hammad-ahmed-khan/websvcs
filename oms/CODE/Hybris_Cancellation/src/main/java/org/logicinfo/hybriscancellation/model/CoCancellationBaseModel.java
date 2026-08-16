package org.logicinfo.hybriscancellation.model;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class CoCancellationBaseModel {

	protected XMLGregorianCalendar responseDatetimestamp;

	protected String cancellationId;

	protected String messageStatus;

	protected String responseMessage;
	protected List<CoCancellationItemModel> customerOrderCancelResponseItems;

	protected String messageCode;

	protected String messageDesc;

	public XMLGregorianCalendar getResponseDatetimestamp() {
		return responseDatetimestamp;
	}

	public void setResponseDatetimestamp(XMLGregorianCalendar responseDatetimestamp) {
		this.responseDatetimestamp = responseDatetimestamp;
	}

	public String getCancellationId() {
		return cancellationId;
	}

	public void setCancellationId(String cancellationId) {
		this.cancellationId = cancellationId;
	}

	public String getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(String messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public List<CoCancellationItemModel> getCustomerOrderCancelResponseItems() {
		return customerOrderCancelResponseItems;
	}

	public void setCustomerOrderCancelResponseItems(List<CoCancellationItemModel> customerOrderCancelResponseItems) {
		this.customerOrderCancelResponseItems = customerOrderCancelResponseItems;
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

}
