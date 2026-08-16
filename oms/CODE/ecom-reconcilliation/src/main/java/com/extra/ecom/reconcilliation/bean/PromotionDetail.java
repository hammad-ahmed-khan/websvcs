package com.extra.ecom.reconcilliation.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author aibrahim
 *
 */
public class PromotionDetail {

	private Long id;

	@JsonProperty(value = "comp_id")
	private Long compId;

	@JsonProperty(value = "dtl_id")
	private Long detailId;

	public Long getId() {
		return id;
	}

	public Long getCompId() {
		return compId;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCompId(Long compId) {
		this.compId = compId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}
}
