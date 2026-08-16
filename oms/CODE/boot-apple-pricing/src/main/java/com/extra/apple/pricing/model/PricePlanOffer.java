/**
 * 
 */
package com.extra.apple.pricing.model;

import java.math.BigDecimal;

/**
 * @author aibrahim
 *
 */
public class PricePlanOffer {

	private String priceOfferTitle;

	private BigDecimal offerNumericPrice;

	private String offerTerm;

	private String qualifyingDescriptionTitle;

	private String qualifyingDescription;
	
	private String offerRelation;

	public String getPriceOfferTitle() {
		return priceOfferTitle;
	}

	public void setPriceOfferTitle(String priceOfferTitle) {
		this.priceOfferTitle = priceOfferTitle;
	}

	public BigDecimal getOfferNumericPrice() {
		return offerNumericPrice;
	}

	public void setOfferNumericPrice(BigDecimal offerNumericPrice) {
		this.offerNumericPrice = offerNumericPrice;
	}

	public String getOfferTerm() {
		return offerTerm;
	}

	public void setOfferTerm(String offerTerm) {
		this.offerTerm = offerTerm;
	}

	public String getQualifyingDescriptionTitle() {
		return qualifyingDescriptionTitle;
	}

	public void setQualifyingDescriptionTitle(String qualifyingDescriptionTitle) {
		this.qualifyingDescriptionTitle = qualifyingDescriptionTitle;
	}

	public String getQualifyingDescription() {
		return qualifyingDescription;
	}

	public void setQualifyingDescription(String qualifyingDescription) {
		this.qualifyingDescription = qualifyingDescription;
	}

	public String getOfferRelation() {
		return offerRelation;
	}

	public void setOfferRelation(String offerRelation) {
		this.offerRelation = offerRelation;
	}
}
