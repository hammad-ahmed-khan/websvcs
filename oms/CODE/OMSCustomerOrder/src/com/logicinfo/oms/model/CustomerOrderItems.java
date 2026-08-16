
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for customerOrderItems complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrderItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Back_Order_Ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Fut_Inv_avl_Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="order_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="standard_uom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="unit_retail">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="orig_unit_retail" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="retail_currency">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="transaction_uom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="substitution_ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Y"/>
 *               &lt;enumeration value="N"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_comments" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrderItems", propOrder = { "item", "lineNo", "linkLineNo", "shippingClassification", "backOrderInd", "packItemInd", "combinationId", "priority", "itemSourceLocType",
		"itemSourceLoc", "itemFulfillLocType", "itemFulfillLoc", "orderQtySuom", "standardUom", "unitRetail", "origUnitRetail", "unitVatAmount", "retailCurrency", "transactionUom", "substitutionInd",
		"itemComments", "expectedDeliveryDateTime", "joodItem", "marketplaceInd", "applyServiceInd", "originalTranOrderNo", "originalTranLineNo", "serviceType", "ordSrId", "delvEffort", "rsaItem", "rsaDetails", "custOrdItemDisc" })
public class CustomerOrderItems {
	@XmlElement(required = true)
	protected String item;
	@XmlElement(name = "Back_Order_Ind", required = true)
	protected String backOrderInd;

	@XmlElement(name = "Pack_Item_Ind")
	protected String packItemInd;

	@XmlElement(name = "order_qty_suom", required = true)
	protected BigDecimal orderQtySuom;
	@XmlElement(name = "standard_uom")
	protected String standardUom;
	@XmlElement(name = "unit_retail", required = true)
	protected BigDecimal unitRetail;
	@XmlElement(name = "orig_unit_retail")
	protected BigDecimal origUnitRetail;
	@XmlElement(name = "retail_currency", required = true)
	protected String retailCurrency;
	@XmlElement(name = "transaction_uom")
	protected String transactionUom;
	@XmlElement(name = "substitution_ind")
	protected String substitutionInd;
	@XmlElement(name = "item_comments")
	protected String itemComments;
	@XmlElement(name = "line_no")
	protected long lineNo;
	@XmlElement(name = "shipping_classification", required = true)
	protected String shippingClassification;
	@XmlElement(name = "link_line_no")
	protected Long linkLineNo;

	@XmlElement(name = "Cust_Ord_Item_Disc")
	protected List<CustOrdItemDisc> custOrdItemDisc;
	@XmlElement(name = "RSADetails")
	protected List<RSADetails> rsaDetails;
	@XmlElement(name = "unit_vat_amount")
	protected BigDecimal unitVatAmount;
	@XmlElement(name = "expected_delivery_dateTime")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar expectedDeliveryDateTime;
	@XmlElement(name = "jood_Item")
	protected String joodItem;
	@XmlElement(name = "marketplace_Ind")
	protected String marketplaceInd;	
	@XmlElement(name = "apply_service_Ind")
	protected String applyServiceInd;
	@XmlElement(name = "original_tran_order_no")
	protected String originalTranOrderNo;
	@XmlElement(name = "original_tran_line_no")
	protected long originalTranLineNo;	
	@XmlElement(name = "service_type")
	protected String serviceType;
	@XmlElement(name = "ord_sr_id")
	protected String ordSrId;
	@XmlElement(name = "delv_effort")
	protected BigDecimal delvEffort;
	@XmlElement(name = "RSAItem")
	protected String rsaItem;
	@XmlElement(name = "combination_id")
	protected Long combinationId;
	@XmlElement(name = "item_fulfill_loc")
	protected Long itemFulfillLoc;
	@XmlElement(name = "item_fulfill_loc_type")
	protected String itemFulfillLocType;
	@XmlElement(name = "item_source_loc")
	protected Long itemSourceLoc;
	@XmlElement(name = "item_source_loc_type")
	protected String itemSourceLocType;
	protected Long priority;


	/**
	 * Gets the value of the item property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Sets the value of the item property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setItem(String value) {
		this.item = value;
	}

	/**
	 * Gets the value of the backOrderInd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBackOrderInd() {
		return backOrderInd;
	}

	/**
	 * Sets the value of the backOrderInd property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setBackOrderInd(String value) {
		this.backOrderInd = value;
	}

	/**
	 * Gets the value of the orderQtySuom property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getOrderQtySuom() {
		return orderQtySuom;
	}

	/**
	 * Sets the value of the orderQtySuom property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setOrderQtySuom(BigDecimal value) {
		this.orderQtySuom = value;
	}

	/**
	 * Gets the value of the standardUom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStandardUom() {
		return standardUom;
	}

	/**
	 * Sets the value of the standardUom property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setStandardUom(String value) {
		this.standardUom = value;
	}

	/**
	 * Gets the value of the unitRetail property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getUnitRetail() {
		return unitRetail;
	}

	/**
	 * Sets the value of the unitRetail property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setUnitRetail(BigDecimal value) {
		this.unitRetail = value;
	}

	/**
	 * Gets the value of the origUnitRetail property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getOrigUnitRetail() {
		return origUnitRetail;
	}

	/**
	 * Sets the value of the origUnitRetail property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setOrigUnitRetail(BigDecimal value) {
		this.origUnitRetail = value;
	}

	/**
	 * Gets the value of the retailCurrency property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRetailCurrency() {
		return retailCurrency;
	}

	/**
	 * Sets the value of the retailCurrency property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRetailCurrency(String value) {
		this.retailCurrency = value;
	}

	/**
	 * Gets the value of the transactionUom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransactionUom() {
		return transactionUom;
	}

	/**
	 * Sets the value of the transactionUom property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setTransactionUom(String value) {
		this.transactionUom = value;
	}

	/**
	 * Gets the value of the substitutionInd property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSubstitutionInd() {
		return substitutionInd;
	}

	/**
	 * Sets the value of the substitutionInd property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setSubstitutionInd(String value) {
		this.substitutionInd = value;
	}

	/**
	 * Gets the value of the itemComments property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getItemComments() {
		return itemComments;
	}

	/**
	 * Sets the value of the itemComments property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setItemComments(String value) {
		this.itemComments = value;
	}

	/**
	 * Gets the value of the lineNo property.
	 *
	 */
	public long getLineNo() {
		return lineNo;
	}

	/**
	 * Gets the value of the shippingClassification property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getShippingClassification() {
		return shippingClassification;
	}

	/**
	 * Sets the value of the lineNo property.
	 *
	 */
	public void setLineNo(long value) {
		this.lineNo = value;
	}

	/**
	 * Sets the value of the shippingClassification property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setShippingClassification(String value) {
		this.shippingClassification = value;
	}

	/**
	 * Gets the value of the linkLineNo property.
	 *
	 */
	public Long getLinkLineNo() {
		return linkLineNo;
	}

	/**
	 * Sets the value of the linkLineNo property.
	 *
	 */
	public void setLinkLineNo(Long value) {
		this.linkLineNo = value;
	}

	/**
	 * Gets the value of the custOrdItemDisc property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the custOrdItemDisc property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCustOrdItemDisc().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CustOrdItemDisc}
	 *
	 *
	 */
	public List<CustOrdItemDisc> getCustOrdItemDisc() {
		if (custOrdItemDisc == null) {
			custOrdItemDisc = new ArrayList<CustOrdItemDisc>();
		}
		return this.custOrdItemDisc;
	}

	public List<RSADetails> getRSADetails() {
		if (rsaDetails == null) {
			rsaDetails = new ArrayList<RSADetails>();
		}
		return this.rsaDetails;
	}

	/**
	 * Gets the value of the unitVatAmount property.
	 *
	 * @return possible object is {@link BigDecimal }
	 *
	 */
	public BigDecimal getUnitVatAmount() {
		return unitVatAmount;
	}

	/**
	 * Sets the value of the unitVatAmount property.
	 *
	 * @param value allowed object is {@link BigDecimal }
	 *
	 */
	public void setUnitVatAmount(BigDecimal value) {
		this.unitVatAmount = value;
	}

	/**
	 * Gets the value of the expectedDeliveryDateTime property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getExpectedDeliveryDateTime() {
		return expectedDeliveryDateTime;
	}

	/**
	 * Sets the value of the expectedDeliveryDateTime property.
	 *
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 *
	 */
	public void setExpectedDeliveryDateTime(XMLGregorianCalendar value) {
		this.expectedDeliveryDateTime = value;
	}

	/**
	 * Gets the value of the combinationId property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getCombinationId() {
		return combinationId;
	}

	/**
	 * Gets the value of the itemFulfillLoc property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getItemFulfillLoc() {
		return itemFulfillLoc;
	}

	/**
	 * Gets the value of the itemFulfillLocType property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getItemFulfillLocType() {
		return itemFulfillLocType;
	}

	/**
	 * Gets the value of the itemSourceLoc property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getItemSourceLoc() {
		return itemSourceLoc;
	}

	/**
	 * Gets the value of the itemSourceLocType property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getItemSourceLocType() {
		return itemSourceLocType;
	}

	/**
	 * Gets the value of the priority property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public Long getPriority() {
		return priority;
	}

	/**
	 * Sets the value of the combinationId property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setCombinationId(Long value) {
		this.combinationId = value;
	}

	/**
	 * Sets the value of the itemFulfillLoc property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setItemFulfillLoc(Long value) {
		this.itemFulfillLoc = value;
	}

	/**
	 * Sets the value of the itemFulfillLocType property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setItemFulfillLocType(String value) {
		this.itemFulfillLocType = value;
	}

	/**
	 * Sets the value of the itemSourceLoc property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setItemSourceLoc(Long value) {
		this.itemSourceLoc = value;
	}

	/**
	 * Sets the value of the itemSourceLocType property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setItemSourceLocType(String value) {
		this.itemSourceLocType = value;
	}

	/**
	 * Sets the value of the priority property.
	 *
	 * @param value allowed object is {@link Long }
	 *
	 */
	public void setPriority(Long value) {
		this.priority = value;
	}

	public void setRsaItem(String rsaItem) {
		this.rsaItem = rsaItem;
	}

	public String getRsaItem() {
		return rsaItem;
	}

	public String getPackItemInd() {
		return packItemInd;
	}

	public void setPackItemInd(String packItemInd) {
		this.packItemInd = packItemInd;
	}

	public String getJoodItem() {
		return joodItem;
	}

	public void setJoodItem(String joodItem) {
		this.joodItem = joodItem;
	}

	public String getMarketplaceInd() {
		return marketplaceInd;
	}

	public void setMarketplaceInd(String marketplaceInd) {
		this.marketplaceInd = marketplaceInd;
	}

	public List<RSADetails> getRsaDetails() {
		return rsaDetails;
	}

	public void setRsaDetails(List<RSADetails> rsaDetails) {
		this.rsaDetails = rsaDetails;
	}

	public String getOriginalTranOrderNo() {
		return originalTranOrderNo;
	}

	public void setOriginalTranOrderNo(String originalTranOrderNo) {
		this.originalTranOrderNo = originalTranOrderNo;
	}

	public long getOriginalTranLineNo() {
		return originalTranLineNo;
	}

	public void setOriginalTranLineNo(long originalTranLineNo) {
		this.originalTranLineNo = originalTranLineNo;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public void setCustOrdItemDisc(List<CustOrdItemDisc> custOrdItemDisc) {
		this.custOrdItemDisc = custOrdItemDisc;
	}
	public String getApplyServiceInd() {
		return applyServiceInd;
	}

	public void setApplyServiceInd(String applyServiceInd) {
		this.applyServiceInd = applyServiceInd;
	}
	public String getOrdSrId() {
		return ordSrId;
	}

	public void setOrdSrId(String ordSrId) {
		this.ordSrId = ordSrId;
	}

	public BigDecimal getDelvEffort() {
		return delvEffort;
	}

	public void setDelvEffort(BigDecimal delvEffort) {
		this.delvEffort = delvEffort;
	}
}
