package com.extra.ecom.reconcilliation.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author aibrahim
 *
 */
@JsonInclude(content = Include.NON_NULL)
public class Catelogue {

	@JsonProperty(value = "catalogCode")
	private String catelougeCode;

	private List<String> productCodes;

	@JsonProperty(value = "promo_details")
	private List<PromotionDetail> promotionDetails;

	@JsonIgnore
	private List<String> rowIds;

	public String getCatelougeCode() {
		return catelougeCode;
	}

	public List<String> getProductCodes() {
		return productCodes;
	}

	public List<PromotionDetail> getPromotionDetails() {
		return promotionDetails;
	}

	public void setCatelougeCode(String catelougeCode) {
		this.catelougeCode = catelougeCode;
	}

	public void setProductCodes(List<String> productCodes) {
		this.productCodes = productCodes;
	}

	public void setPromotionDetails(List<PromotionDetail> promotionDetails) {
		this.promotionDetails = promotionDetails;
	}

	public List<String> getRowIds() {
		return rowIds;
	}

	public void setRowIds(List<String> rowIds) {
		this.rowIds = rowIds;
	}
}
