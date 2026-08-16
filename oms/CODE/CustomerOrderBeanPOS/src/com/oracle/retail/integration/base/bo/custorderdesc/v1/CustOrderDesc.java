
package com.oracle.retail.integration.base.bo.custorderdesc.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerDesc;
import com.oracle.retail.integration.base.bo.custorddelcoldesc.v1.CustOrdDelColDesc;
import com.oracle.retail.integration.base.bo.custordfulcoldesc.v1.CustOrdFulColDesc;
import com.oracle.retail.integration.base.bo.custorditmcoldesc.v1.CustOrdItmColDesc;
import com.oracle.retail.integration.base.bo.localedesc.v1.LocaleDesc;
import com.oracle.retail.integration.base.bo.paymentcoldesc.v1.PaymentColDesc;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="external_ref_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/LocaleDesc/v1}LocaleDesc"/>
 *         &lt;element name="order_desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="order_status" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1}order_status"/>
 *         &lt;element name="initiate_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1}initiate_loc_type"/>
 *         &lt;element name="initiate_loc_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="initiate_country_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="grand_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="sub_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="tax_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="inclusive_tax_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="shipping_charge_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="discount_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="paid_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="rounding_adjustment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="refund_amount_offset_by_sale" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}CustomerDesc" minOccurs="0"/>
 *         &lt;element name="gift_receipt_assigned" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1}gift_receipt_assigned"/>
 *         &lt;element name="default_gift_registry_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="age_restricted_dob" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="create_timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="update_timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmColDesc/v1}CustOrdItmColDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulColDesc/v1}CustOrdFulColDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdDelColDesc/v1}CustOrdDelColDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentColDesc/v1}PaymentColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "customerOrderId", "shipToStore", "pickLoc", "externalRefId", "currencyCode", "localeDesc", "orderDesc", "orderStatus", "orderCategory", "addrVerifiedInd", "bookingStatus", "initiateLocType", "initiateLocId", "initiateCountryCode", "cashbackInd",
		"orposTransactionNumber", "orposInvoiceNumber", "expressDelvInd", "grandTotal", "subTotal", "taxTotal", "inclusiveTaxTotal", "shippingChargeTotal", "discountTotal", "completedAmount", "cancelledAmount", "returnedAmount",
		"completedDiscountAmount", "cancelledDiscountAmount", "returnedDiscountAmount", "completedTaxAmount", "cancelledTaxAmount", "returnedTaxAmount", "completedInclusiveTaxAmount",
		"cancelledInclusiveTaxAmount", "returnedInclusiveTaxAmount", "paidAmount", "pandaIRN", "delvAuthCode", "roundingAdjustment", "refundAmountOffsetBySale", "customerDesc", "giftReceiptAssigned", "defaultGiftRegistryId",
		"bookingDate", "bookingWindow", "ageRestrictedDob", "createTimestamp", "updateTimestamp", "custOrdItmColDesc", "custOrdFulColDesc", "custOrdDelColDesc", "paymentColDesc" })
@XmlRootElement(name = "CustOrderDesc")
@XmlSeeAlso(CustOrderDesc.class)
public class CustOrderDesc {

	@XmlElement(name = "customer_order_id", required = true)
	protected String customerOrderId;
	@XmlElement(name = "ship_to_store")
	protected String shipToStore = "N"; 
	@XmlElement(name = "pick_loc")
	protected long pickLoc;
	@XmlElement(name = "external_ref_id")
	protected String externalRefId;
	@XmlElement(name = "currency_code", required = true)
	protected String currencyCode;
	@XmlElement(name = "LocaleDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/LocaleDesc/v1", required = true)
	protected LocaleDesc localeDesc;
	@XmlElement(name = "order_desc")
	protected String orderDesc;

	@XmlElement(name = "order_status", required = true)
	protected OrderStatus orderStatus;
	
	@XmlElement(name = "order_category")
	protected String orderCategory;
	
	@XmlElement(name = "addr_verified_ind")
	protected String addrVerifiedInd;
	
	@XmlElement(name = "booking_status")
	private String bookingStatus;

	@XmlElement(name = "initiate_loc_type", required = true)
	protected InitiateLocType initiateLocType;
	@XmlElement(name = "initiate_loc_id")
	protected long initiateLocId;
	@XmlElement(name = "initiate_country_code", required = true)
	protected String initiateCountryCode;

	@XmlElement(name = "cashback_ind")
	protected String cashbackInd;
	
	@XmlElement(name = "orpos_transaction_number")
	protected String orposTransactionNumber;
	
	@XmlElement(name = "pos_invoice_number")
	protected String orposInvoiceNumber;

	@XmlElement(name = "express_delv_ind")
	private String expressDelvInd;

	@XmlElement(name = "grand_total")
	protected BigDecimal grandTotal;
	@XmlElement(name = "sub_total")
	protected BigDecimal subTotal;
	@XmlElement(name = "tax_total")
	protected BigDecimal taxTotal;
	@XmlElement(name = "inclusive_tax_total")
	protected BigDecimal inclusiveTaxTotal;
	@XmlElement(name = "shipping_charge_total")
	protected BigDecimal shippingChargeTotal;
	@XmlElement(name = "discount_total")
	protected BigDecimal discountTotal;
	@XmlElement(name = "completed_amount")
	protected BigDecimal completedAmount;
	@XmlElement(name = "cancelled_amount")
	protected BigDecimal cancelledAmount;
	@XmlElement(name = "returned_amount")
	protected BigDecimal returnedAmount;
	@XmlElement(name = "completed_discount_amount")
	protected BigDecimal completedDiscountAmount;
	@XmlElement(name = "cancelled_discount_amount")
	protected BigDecimal cancelledDiscountAmount;
	@XmlElement(name = "returned_discount_amount")
	protected BigDecimal returnedDiscountAmount;
	@XmlElement(name = "completed_tax_amount")
	protected BigDecimal completedTaxAmount;
	@XmlElement(name = "cancelled_tax_amount")
	protected BigDecimal cancelledTaxAmount;
	@XmlElement(name = "returned_tax_amount")
	protected BigDecimal returnedTaxAmount;
	@XmlElement(name = "completed_inclusive_tax_amount")
	protected BigDecimal completedInclusiveTaxAmount;
	@XmlElement(name = "cancelled_inclusive_tax_amount")
	protected BigDecimal cancelledInclusiveTaxAmount;
	@XmlElement(name = "returned_inclusive_tax_amount")
	protected BigDecimal returnedInclusiveTaxAmount;
	@XmlElement(name = "paid_amount")
	protected BigDecimal paidAmount;

	@XmlElement(name = "panda_irn")
	private String pandaIRN;
	
	@XmlElement(name = "delv_auth_code")
	private Long delvAuthCode;
	
	@XmlElement(name = "rounding_adjustment")
	protected BigDecimal roundingAdjustment;
	@XmlElement(name = "refund_amount_offset_by_sale")
	protected BigDecimal refundAmountOffsetBySale;
	@XmlElement(name = "CustomerDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1")
	protected CustomerDesc customerDesc;
	@XmlElement(name = "gift_receipt_assigned", required = true)
	protected GiftReceiptAssigned giftReceiptAssigned;
	@XmlElement(name = "default_gift_registry_id")
	protected String defaultGiftRegistryId;

	@XmlElement(name = "booking_date", required = false)
	@XmlSchemaType(name = "dateTime")
	private XMLGregorianCalendar bookingDate;
	
	@XmlElement(name = "booking_window", required = false)
	private String bookingWindow;

	@XmlElement(name = "age_restricted_dob")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar ageRestrictedDob;
	@XmlElement(name = "create_timestamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar createTimestamp;
	@XmlElement(name = "update_timestamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar updateTimestamp;
	@XmlElement(name = "CustOrdItmColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdItmColDesc/v1")
	protected CustOrdItmColDesc custOrdItmColDesc;
	@XmlElement(name = "CustOrdFulColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdFulColDesc/v1")
	protected CustOrdFulColDesc custOrdFulColDesc;
	@XmlElement(name = "CustOrdDelColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdDelColDesc/v1")
	protected CustOrdDelColDesc custOrdDelColDesc;
	@XmlElement(name = "PaymentColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PaymentColDesc/v1")
	protected PaymentColDesc paymentColDesc;


	/**
	 * Gets the value of the customerOrderId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustomerOrderId() {
		return customerOrderId;
	}

	/**
	 * Sets the value of the customerOrderId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCustomerOrderId(String value) {
		this.customerOrderId = value;
	}

	/**
	 * Gets the value of the externalRefId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getExternalRefId() {
		return externalRefId;
	}

	/**
	 * Sets the value of the externalRefId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setExternalRefId(String value) {
		this.externalRefId = value;
	}

	/**
	 * Gets the value of the currencyCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * Sets the value of the currencyCode property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCurrencyCode(String value) {
		this.currencyCode = value;
	}

	/**
	 * The order's original receipt locale.
	 * 
	 * @return possible object is {@link LocaleDesc }
	 * 
	 */
	public LocaleDesc getLocaleDesc() {
		return localeDesc;
	}

	/**
	 * Sets the value of the localeDesc property.
	 * 
	 * @param value allowed object is {@link LocaleDesc }
	 * 
	 */
	public void setLocaleDesc(LocaleDesc value) {
		this.localeDesc = value;
	}

	/**
	 * Gets the value of the orderDesc property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderDesc() {
		return orderDesc;
	}

	/**
	 * Sets the value of the orderDesc property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOrderDesc(String value) {
		this.orderDesc = value;
	}

	/**
	 * Gets the value of the orderStatus property.
	 * 
	 * @return possible object is {@link OrderStatus }
	 * 
	 */
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	/**
	 * Sets the value of the orderStatus property.
	 * 
	 * @param value allowed object is {@link OrderStatus }
	 * 
	 */
	public void setOrderStatus(OrderStatus value) {
		this.orderStatus = value;
	}

	/**
	 * Gets the value of the initiateLocType property.
	 * 
	 * @return possible object is {@link InitiateLocType }
	 * 
	 */
	public InitiateLocType getInitiateLocType() {
		return initiateLocType;
	}

	/**
	 * Sets the value of the initiateLocType property.
	 * 
	 * @param value allowed object is {@link InitiateLocType }
	 * 
	 */
	public void setInitiateLocType(InitiateLocType value) {
		this.initiateLocType = value;
	}

	/**
	 * Gets the value of the initiateLocId property.
	 * 
	 */
	public long getInitiateLocId() {
		return initiateLocId;
	}

	/**
	 * Sets the value of the initiateLocId property.
	 * 
	 */
	public void setInitiateLocId(long value) {
		this.initiateLocId = value;
	}

	/**
	 * Gets the value of the initiateCountryCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInitiateCountryCode() {
		return initiateCountryCode;
	}

	/**
	 * Sets the value of the initiateCountryCode property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setInitiateCountryCode(String value) {
		this.initiateCountryCode = value;
	}

	/**
	 * Gets the value of the grandTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getGrandTotal() {
		return grandTotal;
	}

	/**
	 * Sets the value of the grandTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setGrandTotal(BigDecimal value) {
		this.grandTotal = value;
	}

	/**
	 * Gets the value of the subTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getSubTotal() {
		return subTotal;
	}

	/**
	 * Sets the value of the subTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setSubTotal(BigDecimal value) {
		this.subTotal = value;
	}

	/**
	 * Gets the value of the taxTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getTaxTotal() {
		return taxTotal;
	}

	/**
	 * Sets the value of the taxTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setTaxTotal(BigDecimal value) {
		this.taxTotal = value;
	}

	/**
	 * Gets the value of the inclusiveTaxTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getInclusiveTaxTotal() {
		return inclusiveTaxTotal;
	}

	/**
	 * Sets the value of the inclusiveTaxTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setInclusiveTaxTotal(BigDecimal value) {
		this.inclusiveTaxTotal = value;
	}

	/**
	 * Gets the value of the shippingChargeTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getShippingChargeTotal() {
		return shippingChargeTotal;
	}

	/**
	 * Sets the value of the shippingChargeTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setShippingChargeTotal(BigDecimal value) {
		this.shippingChargeTotal = value;
	}

	/**
	 * Gets the value of the discountTotal property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getDiscountTotal() {
		return discountTotal;
	}

	/**
	 * Sets the value of the discountTotal property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setDiscountTotal(BigDecimal value) {
		this.discountTotal = value;
	}

	/**
	 * Gets the value of the completedAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCompletedAmount() {
		return completedAmount;
	}

	/**
	 * Sets the value of the completedAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCompletedAmount(BigDecimal value) {
		this.completedAmount = value;
	}

	/**
	 * Gets the value of the cancelledAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCancelledAmount() {
		return cancelledAmount;
	}

	/**
	 * Sets the value of the cancelledAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCancelledAmount(BigDecimal value) {
		this.cancelledAmount = value;
	}

	/**
	 * Gets the value of the returnedAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getReturnedAmount() {
		return returnedAmount;
	}

	/**
	 * Sets the value of the returnedAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setReturnedAmount(BigDecimal value) {
		this.returnedAmount = value;
	}

	/**
	 * Gets the value of the completedDiscountAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCompletedDiscountAmount() {
		return completedDiscountAmount;
	}

	/**
	 * Sets the value of the completedDiscountAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCompletedDiscountAmount(BigDecimal value) {
		this.completedDiscountAmount = value;
	}

	/**
	 * Gets the value of the cancelledDiscountAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCancelledDiscountAmount() {
		return cancelledDiscountAmount;
	}

	/**
	 * Sets the value of the cancelledDiscountAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCancelledDiscountAmount(BigDecimal value) {
		this.cancelledDiscountAmount = value;
	}

	/**
	 * Gets the value of the returnedDiscountAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getReturnedDiscountAmount() {
		return returnedDiscountAmount;
	}

	/**
	 * Sets the value of the returnedDiscountAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setReturnedDiscountAmount(BigDecimal value) {
		this.returnedDiscountAmount = value;
	}

	/**
	 * Gets the value of the completedTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCompletedTaxAmount() {
		return completedTaxAmount;
	}

	/**
	 * Sets the value of the completedTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCompletedTaxAmount(BigDecimal value) {
		this.completedTaxAmount = value;
	}

	/**
	 * Gets the value of the cancelledTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCancelledTaxAmount() {
		return cancelledTaxAmount;
	}

	/**
	 * Sets the value of the cancelledTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCancelledTaxAmount(BigDecimal value) {
		this.cancelledTaxAmount = value;
	}

	/**
	 * Gets the value of the returnedTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getReturnedTaxAmount() {
		return returnedTaxAmount;
	}

	/**
	 * Sets the value of the returnedTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setReturnedTaxAmount(BigDecimal value) {
		this.returnedTaxAmount = value;
	}

	/**
	 * Gets the value of the completedInclusiveTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCompletedInclusiveTaxAmount() {
		return completedInclusiveTaxAmount;
	}

	/**
	 * Sets the value of the completedInclusiveTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCompletedInclusiveTaxAmount(BigDecimal value) {
		this.completedInclusiveTaxAmount = value;
	}

	/**
	 * Gets the value of the cancelledInclusiveTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getCancelledInclusiveTaxAmount() {
		return cancelledInclusiveTaxAmount;
	}

	/**
	 * Sets the value of the cancelledInclusiveTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setCancelledInclusiveTaxAmount(BigDecimal value) {
		this.cancelledInclusiveTaxAmount = value;
	}

	/**
	 * Gets the value of the returnedInclusiveTaxAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getReturnedInclusiveTaxAmount() {
		return returnedInclusiveTaxAmount;
	}

	/**
	 * Sets the value of the returnedInclusiveTaxAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setReturnedInclusiveTaxAmount(BigDecimal value) {
		this.returnedInclusiveTaxAmount = value;
	}

	/**
	 * Gets the value of the paidAmount property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	/**
	 * Sets the value of the paidAmount property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setPaidAmount(BigDecimal value) {
		this.paidAmount = value;
	}

	/**
	 * Gets the value of the roundingAdjustment property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getRoundingAdjustment() {
		return roundingAdjustment;
	}

	/**
	 * Sets the value of the roundingAdjustment property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setRoundingAdjustment(BigDecimal value) {
		this.roundingAdjustment = value;
	}

	/**
	 * Gets the value of the refundAmountOffsetBySale property.
	 * 
	 * @return possible object is {@link BigDecimal }
	 * 
	 */
	public BigDecimal getRefundAmountOffsetBySale() {
		return refundAmountOffsetBySale;
	}

	/**
	 * Sets the value of the refundAmountOffsetBySale property.
	 * 
	 * @param value allowed object is {@link BigDecimal }
	 * 
	 */
	public void setRefundAmountOffsetBySale(BigDecimal value) {
		this.refundAmountOffsetBySale = value;
	}

	/**
	 * The linked customer for this order.
	 * 
	 * @return possible object is {@link CustomerDesc }
	 * 
	 */
	public CustomerDesc getCustomerDesc() {
		return customerDesc;
	}

	/**
	 * Sets the value of the customerDesc property.
	 * 
	 * @param value allowed object is {@link CustomerDesc }
	 * 
	 */
	public void setCustomerDesc(CustomerDesc value) {
		this.customerDesc = value;
	}

	/**
	 * Gets the value of the giftReceiptAssigned property.
	 * 
	 * @return possible object is {@link GiftReceiptAssigned }
	 * 
	 */
	public GiftReceiptAssigned getGiftReceiptAssigned() {
		return giftReceiptAssigned;
	}

	/**
	 * Sets the value of the giftReceiptAssigned property.
	 * 
	 * @param value allowed object is {@link GiftReceiptAssigned }
	 * 
	 */
	public void setGiftReceiptAssigned(GiftReceiptAssigned value) {
		this.giftReceiptAssigned = value;
	}

	/**
	 * Gets the value of the defaultGiftRegistryId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDefaultGiftRegistryId() {
		return defaultGiftRegistryId;
	}

	/**
	 * Sets the value of the defaultGiftRegistryId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDefaultGiftRegistryId(String value) {
		this.defaultGiftRegistryId = value;
	}

	/**
	 * Gets the value of the ageRestrictedDob property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getAgeRestrictedDob() {
		return ageRestrictedDob;
	}

	/**
	 * Sets the value of the ageRestrictedDob property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setAgeRestrictedDob(XMLGregorianCalendar value) {
		this.ageRestrictedDob = value;
	}

	/**
	 * Gets the value of the createTimestamp property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getCreateTimestamp() {
		return createTimestamp;
	}

	/**
	 * Sets the value of the createTimestamp property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCreateTimestamp(XMLGregorianCalendar value) {
		this.createTimestamp = value;
	}

	/**
	 * Gets the value of the updateTimestamp property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * Sets the value of the updateTimestamp property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setUpdateTimestamp(XMLGregorianCalendar value) {
		this.updateTimestamp = value;
	}

	/**
	 * A collection of order items.
	 * 
	 * @return possible object is {@link CustOrdItmColDesc }
	 * 
	 */
	public CustOrdItmColDesc getCustOrdItmColDesc() {
		return custOrdItmColDesc;
	}

	/**
	 * Sets the value of the custOrdItmColDesc property.
	 * 
	 * @param value allowed object is {@link CustOrdItmColDesc }
	 * 
	 */
	public void setCustOrdItmColDesc(CustOrdItmColDesc value) {
		this.custOrdItmColDesc = value;
	}

	/**
	 * A collection of order fulfillment details. A fulfullment detail defines how a
	 * group of items will be fulfilled: in store pick up or ship.
	 * 
	 * @return possible object is {@link CustOrdFulColDesc }
	 * 
	 */
	public CustOrdFulColDesc getCustOrdFulColDesc() {
		return custOrdFulColDesc;
	}

	/**
	 * Sets the value of the custOrdFulColDesc property.
	 * 
	 * @param value allowed object is {@link CustOrdFulColDesc }
	 * 
	 */
	public void setCustOrdFulColDesc(CustOrdFulColDesc value) {
		this.custOrdFulColDesc = value;
	}

	/**
	 * A collection of order delivery records.
	 * 
	 * @return possible object is {@link CustOrdDelColDesc }
	 * 
	 */
	public CustOrdDelColDesc getCustOrdDelColDesc() {
		return custOrdDelColDesc;
	}

	/**
	 * Sets the value of the custOrdDelColDesc property.
	 * 
	 * @param value allowed object is {@link CustOrdDelColDesc }
	 * 
	 */
	public void setCustOrdDelColDesc(CustOrdDelColDesc value) {
		this.custOrdDelColDesc = value;
	}

	/**
	 * A collection of order payments.
	 * 
	 * @return possible object is {@link PaymentColDesc }
	 * 
	 */
	public PaymentColDesc getPaymentColDesc() {
		return paymentColDesc;
	}

	/**
	 * Sets the value of the paymentColDesc property.
	 * 
	 * @param value allowed object is {@link PaymentColDesc }
	 * 
	 */
	public void setPaymentColDesc(PaymentColDesc value) {
		this.paymentColDesc = value;
	}

	public String getOrposTransactionNumber() {
		return orposTransactionNumber;
	}

	public void setOrposTransactionNumber(String orposTransactionNumber) {
		this.orposTransactionNumber = orposTransactionNumber;
	}

	public XMLGregorianCalendar getBookingDate() {
		return bookingDate;
	}

	public String getBookingWindow() {
		return bookingWindow;
	}

	public void setBookingDate(XMLGregorianCalendar bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setBookingWindow(String bookingWindow) {
		this.bookingWindow = bookingWindow;
	}

	public String getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(String bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getExpressDelvInd() {
		return expressDelvInd;
	}

	public void setExpressDelvInd(String expressDelvInd) {
		this.expressDelvInd = expressDelvInd;
	}

	public String getPandaIRN() {
		return pandaIRN;
	}

	public void setPandaIRN(String pandaIRN) {
		this.pandaIRN = pandaIRN;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public Long getDelvAuthCode() {
		return delvAuthCode;
	}

	public void setDelvAuthCode(Long delvAuthCode) {
		this.delvAuthCode = delvAuthCode;
	}

	public String getOrposInvoiceNumber() {
		return orposInvoiceNumber;
	}

	public void setOrposInvoiceNumber(String orposInvoiceNumber) {
		this.orposInvoiceNumber = orposInvoiceNumber;
	}

	public String getAddrVerifiedInd() {
		return addrVerifiedInd;
	}

	public void setAddrVerifiedInd(String addrVerifiedInd) {
		this.addrVerifiedInd = addrVerifiedInd;
	}

	public String getShipToStore() {
		return shipToStore;
	}

	public void setShipToStore(String shipToStore) {
		this.shipToStore = shipToStore;
	}

	public String getCashbackInd() {
		return cashbackInd;
	}

	public void setCashbackInd(String cashbackInd) {
		this.cashbackInd = cashbackInd;
	}

	public long getPickLoc() {
		return pickLoc;
	}

	public void setPickLoc(long pickLoc) {
		this.pickLoc = pickLoc;
	}
	
	
}
