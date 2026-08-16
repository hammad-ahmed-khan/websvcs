package com.extra.apple.pricing.model;

import java.util.Date;
import java.util.List;

/**
 * @author aibrahim
 *
 */
public class PriceSheet {

	private Long priceSheetId;

	private Long publishId;

	private String lob;

	private List<Long> stores;

	private List<Long> storeGroups;

	private Date startDate;

	private Date endDate;

	private boolean immediate;

	private List<PriceSheetDescription> priceSheetDescriptions;

	public Long getPriceSheetId() {
		return priceSheetId;
	}

	public void setPriceSheetId(Long priceSheetId) {
		this.priceSheetId = priceSheetId;
	}

	public List<PriceSheetDescription> getPriceSheetDescriptions() {
		return priceSheetDescriptions;
	}

	public void setPriceSheetDescriptions(List<PriceSheetDescription> priceSheetDescriptions) {
		this.priceSheetDescriptions = priceSheetDescriptions;
	}

	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	public List<Long> getStores() {
		return stores;
	}

	public void setStores(List<Long> stores) {
		this.stores = stores;
	}

	public List<Long> getStoreGroups() {
		return storeGroups;
	}

	public void setStoreGroups(List<Long> storeGroups) {
		this.storeGroups = storeGroups;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isImmediate() {
		return immediate;
	}

	public void setImmediate(boolean immediate) {
		this.immediate = immediate;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}
}
