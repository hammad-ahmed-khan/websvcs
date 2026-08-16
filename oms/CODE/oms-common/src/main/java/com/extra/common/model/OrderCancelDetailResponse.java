package com.extra.common.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = { "lineNo", "item", "cancelQtySuom", "messageCode", "messageDesc" })
public class OrderCancelDetailResponse {

	@JsonProperty(required = true)
	protected String item;

	@JsonProperty(value = "cancel_qty_suom", required = true)
	protected BigDecimal cancelQtySuom;

	@JsonProperty(value = "line_no")
	protected BigDecimal lineNo;

	@JsonProperty(value = "message_code", required = true)
	protected String messageCode;

	@JsonProperty(value = "message_desc", required = true)
	protected String messageDesc;

	/**
	 * Gets the value of the item property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Sets the value of the item property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setItem(String value) {
		this.item = value;
	}

	/**
	 * Gets the value of the cancelQtySuom property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getCancelQtySuom() {
		return cancelQtySuom;
	}

	/**
	 * Sets the value of the cancelQtySuom property.
	 *
	 * @param value allowed object is {@link BigDecimal }
	 *
	 */
	public void setCancelQtySuom(BigDecimal value) {
		this.cancelQtySuom = value;
	}

	/**
	 * Gets the value of the lineNo property.
	 *
	 */
	public BigDecimal getLineNo() {
		return lineNo;
	}

	/**
	 * Sets the value of the lineNo property.
	 *
	 */
	public void setLineNo(BigDecimal value) {
		this.lineNo = value;
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
