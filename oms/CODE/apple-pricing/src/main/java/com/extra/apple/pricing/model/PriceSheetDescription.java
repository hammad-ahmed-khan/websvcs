/**
 * 
 */
package com.extra.apple.pricing.model;

import java.util.List;

/**
 * @author aibrahim
 *
 */
public class PriceSheetDescription {

	private List<String> mpns;

	private ProductKey productKey;

	private Property properties;

	private List<PricePlan> pricePlans;

	public List<String> getMpns() {
		return mpns;
	}

	public void setMpns(List<String> mpns) {
		this.mpns = mpns;
	}

	public ProductKey getProductKey() {
		return productKey;
	}

	public void setProductKey(ProductKey productKey) {
		this.productKey = productKey;
	}

	public List<PricePlan> getPricePlans() {
		return pricePlans;
	}

	public void setPricePlans(List<PricePlan> pricePlans) {
		this.pricePlans = pricePlans;
	}

	public Property getProperties() {
		return properties;
	}

	public void setProperties(Property properties) {
		this.properties = properties;
	}
}
