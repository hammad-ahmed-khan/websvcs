package com.extra.ecom.reconcilliation.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author aibrahim
 *
 */
@JsonInclude(content = Include.NON_NULL)
public class CatelogueResponse {

	@JsonProperty(value = "catalogCode")
	private String catelougeCode;

	private List<String> availProductCodes;

	private List<String> unavailProductCodes;

	private List<String> availPriceCodes;
	
	private List<String> unavailPriceCodes;

	private List<ProductDetail> productDetails;

	private List<PromotionDetail> availPromoDetails;

	private List<PromotionDetail> unavailPromoDetails;

	public String getCatelougeCode() {
		return catelougeCode;
	}

	public List<String> getAvailProductCodes() {
		return availProductCodes;
	}

	public List<String> getUnavailProductCodes() {
		return unavailProductCodes;
	}

	public List<String> getAvailPriceCodes() {
		return availPriceCodes;
	}

	public List<String> getUnavailPriceCodes() {
		return unavailPriceCodes;
	}

	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public List<PromotionDetail> getAvailPromoDetails() {
		return availPromoDetails;
	}

	public List<PromotionDetail> getUnavailPromoDetails() {
		return unavailPromoDetails;
	}

	public void setCatelougeCode(String catelougeCode) {
		this.catelougeCode = catelougeCode;
	}

	public void setAvailProductCodes(List<String> availProductCodes) {
		this.availProductCodes = availProductCodes;
	}

	public void setUnavailProductCodes(List<String> unavailProductCodes) {
		this.unavailProductCodes = unavailProductCodes;
	}

	public void setAvailPriceCodes(List<String> availPriceCodes) {
		this.availPriceCodes = availPriceCodes;
	}

	public void setUnavailPriceCodes(List<String> unavailPriceCodes) {
		this.unavailPriceCodes = unavailPriceCodes;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}

	public void setAvailPromoDetails(List<PromotionDetail> availPromoDetails) {
		this.availPromoDetails = availPromoDetails;
	}

	public void setUnavailPromoDetails(List<PromotionDetail> unavailPromoDetails) {
		this.unavailPromoDetails = unavailPromoDetails;
	}
}
