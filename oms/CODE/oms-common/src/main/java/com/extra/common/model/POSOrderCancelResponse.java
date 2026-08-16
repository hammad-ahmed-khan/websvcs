package com.extra.common.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "cancellationId","responseDatetimestamp","messageStatus","responseMessage","messageCode",
            "messageDesc","customerOrderCancelResponseItems" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class POSOrderCancelResponse {

	@JsonProperty(value = "response_datetimestamp", required = true)
	protected Date responseDatetimestamp;

	@JsonProperty(value = "cancellation_id", required = true)
	protected String cancellationId;

	@JsonProperty(value = "message_status", required = true)
	protected String messageStatus;

	@JsonProperty(value = "response_message", required = true)
	protected String responseMessage;

	protected List<OrderCancelDetailResponse> customerOrderCancelResponseItems;

	@JsonProperty(value = "message_code", required = true)
	protected String messageCode;

	@JsonProperty(value = "message_desc", required = true)
	protected String messageDesc;

	public Date getResponseDatetimestamp() {
		return responseDatetimestamp;
	}

	public void setResponseDatetimestamp(Date value) {
		this.responseDatetimestamp = value;
	}

	/**
	 * Gets the value of the cancellationId property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public String getCancellationId() {
		return cancellationId;
	}

	/**
	 * Sets the value of the cancellationId property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setCancellationId(String value) {
		this.cancellationId = value;
	}

	/**
	 * Gets the value of the messageStatus property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMessageStatus() {
		return messageStatus;
	}

	/**
	 * Sets the value of the messageStatus property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setMessageStatus(String value) {
		this.messageStatus = value;
	}

	/**
	 * Gets the value of the responseMessage property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Sets the value of the responseMessage property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setResponseMessage(String value) {
		this.responseMessage = value;
	}

	/**
	 * Gets the value of the customerOrderCancelResponseItems property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the customerOrderCancelResponseItems property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCustomerOrderCancelResponseItems().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link OrderCancelDetailResponse }
	 *
	 *
	 */
	public List<OrderCancelDetailResponse> getCustomerOrderCancelResponseItems() {
		if (customerOrderCancelResponseItems == null) {
			customerOrderCancelResponseItems = new ArrayList<OrderCancelDetailResponse>();
		}
		return this.customerOrderCancelResponseItems;
	}

	/**
	 * Gets the value of the messageCode property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMessageCode() {
		return messageCode;
	}

	/**
	 * Gets the value of the messageDesc property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getMessageDesc() {
		return messageDesc;
	}

	/**
	 * Sets the value of the messageCode property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setMessageCode(String value) {
		this.messageCode = value;
	}

	/**
	 * Sets the value of the messageDesc property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setMessageDesc(String value) {
		this.messageDesc = value;
	}
}
