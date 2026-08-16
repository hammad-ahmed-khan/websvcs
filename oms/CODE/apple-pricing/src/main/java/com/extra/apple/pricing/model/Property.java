/**
 * 
 */
package com.extra.apple.pricing.model;

/**
 * @author aibrahim
 *
 */
public class Property {

	private String consumerOffer;

	private String valueProposition;

	private transient String partnerTC;

	public String getConsumerOffer() {
		return consumerOffer;
	}

	public void setConsumerOffer(String consumerOffer) {
		this.consumerOffer = consumerOffer;
	}

	public String getValueProposition() {
		return valueProposition;
	}

	public void setValueProposition(String valueProposition) {
		this.valueProposition = valueProposition;
	}

	public String getPartnerTC() {
		return partnerTC;
	}

	public void setPartnerTC(String partnerTC) {
		this.partnerTC = partnerTC;
	}
}
