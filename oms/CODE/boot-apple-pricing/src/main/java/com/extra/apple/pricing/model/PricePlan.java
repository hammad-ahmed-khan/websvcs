/**
 * 
 */
package com.extra.apple.pricing.model;

import java.util.List;

/**
 * @author aibrahim
 *
 */
public class PricePlan {

	private String planName;

    private List<PricePlanOffer> pricePlanOffers;

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public List<PricePlanOffer> getPricePlanOffers() {
		return pricePlanOffers;
	}

	public void setPricePlanOffers(List<PricePlanOffer> pricePlanOffers) {
		this.pricePlanOffers = pricePlanOffers;
	}
}
