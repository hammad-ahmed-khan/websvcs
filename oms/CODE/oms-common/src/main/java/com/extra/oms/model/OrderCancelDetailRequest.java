package com.extra.oms.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderCancelDetailRequest {

	@JsonProperty(required = true)
	protected String item;

	@JsonProperty(value = "cancel_qty_suom", required = true)
	protected BigDecimal cancelQtySuom;

	@JsonProperty(value = "item_comments", required = true)
	protected String itemComments;

	@JsonProperty(value = "line_no")
	protected BigDecimal lineNo;

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
	 * Gets the value of the itemComments property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getItemComments() {
		return itemComments;
	}

	/**
	 * Sets the value of the itemComments property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setItemComments(String value) {
		this.itemComments = value;
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
}
