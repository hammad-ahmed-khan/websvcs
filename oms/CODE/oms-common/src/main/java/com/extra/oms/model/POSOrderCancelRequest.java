package com.extra.oms.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "entityId", "applicationId", "comments", "cancellationRequestorId", "requestDatetimestamp",
		"custOrderNo", "subCustOrderNo", "cancellationId", "refundPreference", "refundAmount", "cancellationDate",
		"reasonCode", "reason", "cancellationItems" })
@JsonIgnoreProperties({ "logSeqId", "omsCancelId" })
public class POSOrderCancelRequest {

	@JsonProperty(value = "entity_id", required = true)
	protected String entityId;

	@JsonProperty(value = "application_id", required = true)
	protected String applicationId;

	protected String comments;

	@JsonProperty(value = "request_datetimestamp", required = true)
	protected Date requestDatetimestamp;

	@JsonProperty(value = "cancellation_id", required = true)
	protected String cancellationId;

	@JsonProperty(value = "cancellation_date", required = true)
	protected Date cancellationDate;

	@JsonProperty(value = "cancellation_items", required = true)
	protected List<OrderCancelDetailRequest> cancellationItems;

	@JsonProperty(value = "cust_order_no", required = true)
	protected String custOrderNo;

	@JsonProperty(value = "sub_cust_order_no")
	protected String subCustOrderNo;

	@JsonProperty(value = "refund_amount", required = true)
	protected BigDecimal refundAmount;

	@JsonProperty(value = "refund_preference", required = true)
	protected String refundPreference;

	@JsonProperty(value = "cancellation_requestor_id", required = true)
	protected String cancellationRequestorId;

	private Long omsCancelId;

	private Long logSeqId;

	@JsonProperty(value = "reason", required = true)
	private String reason;

	@JsonProperty(value = "reason_code", required = true)
	private Integer reasonCode;

	public String getReason() {
		return reason;
	}

	public void setReason(String value) {
		this.reason = value;
	}

	public Integer getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Integer value) {
		this.reasonCode = value;
	}

	/**
	 * Gets the value of the entityId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Sets the value of the entityId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setEntityId(String value) {
		this.entityId = value;
	}

	/**
	 * Gets the value of the applicationId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the value of the applicationId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setApplicationId(String value) {
		this.applicationId = value;
	}

	/**
	 * Gets the value of the comments property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the comments property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setComments(String value) {
		this.comments = value;
	}

	public Date getRequestDatetimestamp() {
		return requestDatetimestamp;
	}

	public void setRequestDatetimestamp(Date value) {
		this.requestDatetimestamp = value;
	}

	/**
	 * Gets the value of the cancellationId property.
	 *
	 */
	public String getCancellationId() {
		return cancellationId;
	}

	/**
	 * Sets the value of the cancellationId property.
	 *
	 */
	public void setCancellationId(String value) {
		this.cancellationId = value;
	}

	public Date getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(Date value) {
		this.cancellationDate = value;
	}

	public List<OrderCancelDetailRequest> getCancellationItems() {
		return this.cancellationItems;
	}

	/**
	 * Gets the value of the custOrderNo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCustOrderNo() {
		return custOrderNo;
	}

	/**
	 * Sets the value of the custOrderNo property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCustOrderNo(String value) {
		this.custOrderNo = value;
	}

	/**
	 * Gets the value of the subCustOrderNo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getSubCustOrderNo() {
		return subCustOrderNo;
	}

	/**
	 * Sets the value of the subCustOrderNo property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setSubCustOrderNo(String value) {
		this.subCustOrderNo = value;
	}

	/**
	 * Gets the value of the refundAmount property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	/**
	 * Gets the value of the refundPreference property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getRefundPreference() {
		return refundPreference;
	}

	/**
	 * Sets the value of the refundAmount property.
	 *
	 * @param value allowed object is {@link BigDecimal }
	 *
	 */
	public void setRefundAmount(BigDecimal value) {
		this.refundAmount = value;
	}

	/**
	 * Sets the value of the refundPreference property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setRefundPreference(String value) {
		this.refundPreference = value;
	}

	/**
	 * Gets the value of the cancellationRequestorId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCancellationRequestorId() {
		return cancellationRequestorId;
	}

	/**
	 * Sets the value of the cancellationRequestorId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCancellationRequestorId(String value) {
		this.cancellationRequestorId = value;
	}

	public Long getOmsCancelId() {
		return omsCancelId;
	}

	public void setOmsCancelId(Long omsCancelId) {
		this.omsCancelId = omsCancelId;
	}

	public void setCancellationItems(List<OrderCancelDetailRequest> cancellationItems) {
		this.cancellationItems = cancellationItems;
	}

	public void setLogSeqId(Long logSeqId) {
		this.logSeqId = logSeqId;
	}

	public Long getLogSeqId() {
		return logSeqId;
	}
}
