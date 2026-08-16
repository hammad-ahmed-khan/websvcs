package com.extra.oms.crossChannel.model;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceOfferedLines {

	@JsonProperty("item")
	private String item;

	@JsonProperty("item_description")
	private String itemDescription;

	@JsonProperty("line_no")
	private int lineNo;

	@JsonProperty("qty")
	private int qty;

	@JsonProperty("retail_discount")
	private double retailDiscount;

	@JsonProperty("retail_price")
	private double retailPrice;

	@JsonProperty("jood_discount")
	private double joodDiscount;

	@JsonProperty("selling_price")
	private double sellingPrice;

	@JsonProperty("vat")
	private int vat;

	@JsonProperty("vat_amount")
	private double vatAmount;

	@JsonProperty("deliver_qty")
	private int deliverQty;

	@JsonProperty("cancel_qty")
	private int cancelQty;

	@JsonProperty("return_qty")
	private int returnQty;

	@JsonProperty("delivery_type")
	private String deliveryType;

	@JsonProperty("link_line_no")
	private String linkLineNo;

	@JsonProperty("delivery_lead_time")
	private String deliveryLeadTime;

	@JsonProperty("item_type")
	private String itemType;

	@JsonProperty("cash_back_amount")
	private double cashBackAmount;

	@JsonProperty("bundle_group_id")
	private String bundleGroupId;

	@JsonProperty("bundle_retail_price")
	private double bundleRetailPrice;

	@JsonProperty("bundle_selling_price")
	private double bundleSellingPrice;

	@JsonProperty("vas_group")
	private String vasGroup;

	@JsonProperty("years")
	private String years;

	@JsonProperty("brand")
	private String brand;

	@JsonProperty("serial_number")
	private String serialNumber;

	@JsonProperty("eligibleServices")
	private List<EligibleServices> eligibleServices;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getRetailDiscount() {
		return retailDiscount;
	}

	public void setRetailDiscount(double retailDiscount) {
		this.retailDiscount = retailDiscount;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getJoodDiscount() {
		return joodDiscount;
	}

	public void setJoodDiscount(double joodDiscount) {
		this.joodDiscount = joodDiscount;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getVat() {
		return vat;
	}

	public void setVat(int vat) {
		this.vat = vat;
	}

	public double getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(double vatAmount) {
		this.vatAmount = vatAmount;
	}

	public int getDeliverQty() {
		return deliverQty;
	}

	public void setDeliverQty(int deliverQty) {
		this.deliverQty = deliverQty;
	}

	public int getCancelQty() {
		return cancelQty;
	}

	public void setCancelQty(int cancelQty) {
		this.cancelQty = cancelQty;
	}

	public int getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(int returnQty) {
		this.returnQty = returnQty;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getLinkLineNo() {
		return linkLineNo;
	}

	public void setLinkLineNo(String linkLineNo) {
		this.linkLineNo = linkLineNo;
	}

	public String getDeliveryLeadTime() {
		return deliveryLeadTime;
	}

	public void setDeliveryLeadTime(String deliveryLeadTime) {
		this.deliveryLeadTime = deliveryLeadTime;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public double getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(double cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getBundleGroupId() {
		return bundleGroupId;
	}

	public void setBundleGroupId(String bundleGroupId) {
		this.bundleGroupId = bundleGroupId;
	}

	public double getBundleRetailPrice() {
		return bundleRetailPrice;
	}

	public void setBundleRetailPrice(double bundleRetailPrice) {
		this.bundleRetailPrice = bundleRetailPrice;
	}

	public double getBundleSellingPrice() {
		return bundleSellingPrice;
	}

	public void setBundleSellingPrice(double bundleSellingPrice) {
		this.bundleSellingPrice = bundleSellingPrice;
	}

	public List<EligibleServices> getEligibleServices() {
		return eligibleServices;
	}

	public void setEligibleServices(List<EligibleServices> eligibleServices) {
		this.eligibleServices = eligibleServices;
	}

	public String getVasGroup() {
		return vasGroup;
	}

	public void setVasGroup(String vasGroup) {
		this.vasGroup = vasGroup;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
