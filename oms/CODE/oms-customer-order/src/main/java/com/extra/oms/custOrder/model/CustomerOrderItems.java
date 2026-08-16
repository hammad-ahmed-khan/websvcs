
package com.extra.oms.custOrder.model;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

public class CustomerOrderItems {
	protected String item;
	protected String backOrderInd;
	protected String packItemInd;
	protected BigDecimal orderQtySuom;
	protected String standardUom;
	protected BigDecimal unitRetail;
	protected BigDecimal origUnitRetail;
	protected String retailCurrency;
	protected String transactionUom;
	protected String substitutionInd;
	protected String itemComments;
	protected long lineNo;
	protected String shippingClassification;
	protected Long linkLineNo;
	protected List<CustOrdItemDisc> custOrdItemDisc;
	protected List<RSADetails> rsaDetails;
	protected BigDecimal unitVatAmount;
	protected XMLGregorianCalendar expectedDeliveryDateTime;
	protected String rsaItem;
	protected Long combinationId;
	protected Long itemFulfillLoc;
	protected String itemFulfillLocType;
	protected Long itemSourceLoc;
	protected String itemSourceLocType;
	protected Long priority;
	protected BigDecimal unitShippingAmount;
	protected BigDecimal totShippingAmount;
	protected BigDecimal shipChargeLvl;
	protected BigDecimal shipLvlValue;
	protected BigDecimal downPayment;
	protected BigDecimal processingFee;
	protected BigDecimal downPaymentTender;
	protected BigDecimal policyTender;
	protected BigDecimal shippingFeeTender;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getBackOrderInd() {
		return backOrderInd;
	}
	public void setBackOrderInd(String backOrderInd) {
		this.backOrderInd = backOrderInd;
	}
	public String getPackItemInd() {
		return packItemInd;
	}
	public void setPackItemInd(String packItemInd) {
		this.packItemInd = packItemInd;
	}
	public BigDecimal getOrderQtySuom() {
		return orderQtySuom;
	}
	public void setOrderQtySuom(BigDecimal orderQtySuom) {
		this.orderQtySuom = orderQtySuom;
	}
	public String getStandardUom() {
		return standardUom;
	}
	public void setStandardUom(String standardUom) {
		this.standardUom = standardUom;
	}
	public BigDecimal getUnitRetail() {
		return unitRetail;
	}
	public void setUnitRetail(BigDecimal unitRetail) {
		this.unitRetail = unitRetail;
	}
	public BigDecimal getOrigUnitRetail() {
		return origUnitRetail;
	}
	public void setOrigUnitRetail(BigDecimal origUnitRetail) {
		this.origUnitRetail = origUnitRetail;
	}
	public String getRetailCurrency() {
		return retailCurrency;
	}
	public void setRetailCurrency(String retailCurrency) {
		this.retailCurrency = retailCurrency;
	}
	public String getTransactionUom() {
		return transactionUom;
	}
	public void setTransactionUom(String transactionUom) {
		this.transactionUom = transactionUom;
	}
	public String getSubstitutionInd() {
		return substitutionInd;
	}
	public void setSubstitutionInd(String substitutionInd) {
		this.substitutionInd = substitutionInd;
	}
	public String getItemComments() {
		return itemComments;
	}
	public void setItemComments(String itemComments) {
		this.itemComments = itemComments;
	}
	public long getLineNo() {
		return lineNo;
	}
	public void setLineNo(long lineNo) {
		this.lineNo = lineNo;
	}
	public String getShippingClassification() {
		return shippingClassification;
	}
	public void setShippingClassification(String shippingClassification) {
		this.shippingClassification = shippingClassification;
	}
	public Long getLinkLineNo() {
		return linkLineNo;
	}
	public void setLinkLineNo(Long linkLineNo) {
		this.linkLineNo = linkLineNo;
	}
	public List<CustOrdItemDisc> getCustOrdItemDisc() {
		return custOrdItemDisc;
	}
	public void setCustOrdItemDisc(List<CustOrdItemDisc> custOrdItemDisc) {
		this.custOrdItemDisc = custOrdItemDisc;
	}
	public List<RSADetails> getRsaDetails() {
		return rsaDetails;
	}
	public void setRsaDetails(List<RSADetails> rsaDetails) {
		this.rsaDetails = rsaDetails;
	}
	public BigDecimal getUnitVatAmount() {
		return unitVatAmount;
	}
	public void setUnitVatAmount(BigDecimal unitVatAmount) {
		this.unitVatAmount = unitVatAmount;
	}
	public XMLGregorianCalendar getExpectedDeliveryDateTime() {
		return expectedDeliveryDateTime;
	}
	public void setExpectedDeliveryDateTime(XMLGregorianCalendar expectedDeliveryDateTime) {
		this.expectedDeliveryDateTime = expectedDeliveryDateTime;
	}
	public String getRsaItem() {
		return rsaItem;
	}
	public void setRsaItem(String rsaItem) {
		this.rsaItem = rsaItem;
	}
	public Long getCombinationId() {
		return combinationId;
	}
	public void setCombinationId(Long combinationId) {
		this.combinationId = combinationId;
	}
	public Long getItemFulfillLoc() {
		return itemFulfillLoc;
	}
	public void setItemFulfillLoc(Long itemFulfillLoc) {
		this.itemFulfillLoc = itemFulfillLoc;
	}
	public String getItemFulfillLocType() {
		return itemFulfillLocType;
	}
	public void setItemFulfillLocType(String itemFulfillLocType) {
		this.itemFulfillLocType = itemFulfillLocType;
	}
	public Long getItemSourceLoc() {
		return itemSourceLoc;
	}
	public void setItemSourceLoc(Long itemSourceLoc) {
		this.itemSourceLoc = itemSourceLoc;
	}
	public String getItemSourceLocType() {
		return itemSourceLocType;
	}
	public void setItemSourceLocType(String itemSourceLocType) {
		this.itemSourceLocType = itemSourceLocType;
	}
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}
	public BigDecimal getUnitShippingAmount() {
		return unitShippingAmount;
	}
	public void setUnitShippingAmount(BigDecimal unitShippingAmount) {
		this.unitShippingAmount = unitShippingAmount;
	}
	public BigDecimal getTotShippingAmount() {
		return totShippingAmount;
	}
	public void setTotShippingAmount(BigDecimal totShippingAmount) {
		this.totShippingAmount = totShippingAmount;
	}
	public BigDecimal getShipChargeLvl() {
		return shipChargeLvl;
	}
	public void setShipChargeLvl(BigDecimal shipChargeLvl) {
		this.shipChargeLvl = shipChargeLvl;
	}
	public BigDecimal getShipLvlValue() {
		return shipLvlValue;
	}
	public void setShipLvlValue(BigDecimal shipLvlValue) {
		this.shipLvlValue = shipLvlValue;
	}
	public BigDecimal getDownPayment() {
		return downPayment;
	}
	public void setDownPayment(BigDecimal downPayment) {
		this.downPayment = downPayment;
	}
	public BigDecimal getProcessingFee() {
		return processingFee;
	}
	public void setProcessingFee(BigDecimal processingFee) {
		this.processingFee = processingFee;
	}
	public BigDecimal getDownPaymentTender() {
		return downPaymentTender;
	}
	public void setDownPaymentTender(BigDecimal downPaymentTender) {
		this.downPaymentTender = downPaymentTender;
	}
	public BigDecimal getPolicyTender() {
		return policyTender;
	}
	public void setPolicyTender(BigDecimal policyTender) {
		this.policyTender = policyTender;
	}
	public BigDecimal getShippingFeeTender() {
		return shippingFeeTender;
	}
	public void setShippingFeeTender(BigDecimal shippingFeeTender) {
		this.shippingFeeTender = shippingFeeTender;
	}


	
}
