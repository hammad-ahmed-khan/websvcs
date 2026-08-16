
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.discntlinecoldesc.v1.DiscntLineColDesc;
import com.oracle.retail.integration.base.bo.prcovdlinedesc.v1.PrcOvdLineDesc;
import com.oracle.retail.integration.base.bo.promolinedesc.v1.PromoLineDesc;
import com.oracle.retail.integration.base.bo.taxlinecoldesc.v1.TaxLineColDesc;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="line_item_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="captured_line_item_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="item_upc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="item_description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="available_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="completed_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="returned_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unit_of_measure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="item_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="unit_sell_price" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unit_regular_price" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="completed_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="returned_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="paid_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="discount_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="tax_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="inclusive_tax_total" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PrcOvdLineDesc/v1}PrcOvdLineDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PromoLineDesc/v1}PromoLineDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/DiscntLineColDesc/v1}DiscntLineColDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/TaxLineColDesc/v1}TaxLineColDesc" minOccurs="0"/>
 *         &lt;element name="department_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="restrictive_age" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="discountable_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="damage_discountable_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="employee_discountable_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="item_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}item_type"/>
 *         &lt;element name="manufacturer_upc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="merchandise_hierarchy_group_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="product_group" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}product_group" minOccurs="0"/>
 *         &lt;element name="restocking_fee_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="return_eligible_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="serialized_item_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="validate_serial_number_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag" minOccurs="0"/>
 *         &lt;element name="allow_new_serial_number_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag" minOccurs="0"/>
 *         &lt;element name="size_required_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="tax_group_id" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="taxable_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="serial_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="size_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gift_registry_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gift_receipted_item_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="shipping_charge_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}flag"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}entry_method" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}AlterationItem" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}GiftCardItem" minOccurs="0"/>
 *         &lt;element name="fulfillment_seq_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "lineItemNo",
    "capturedLineItemNo",
    "itemId",
    "itemUpc",
    "itemDescription",
    "quantity",
    "availableQuantity",
    "completedQuantity",
    "cancelledQuantity",
    "returnedQuantity",
    "unitOfMeasure",
    "currencyCode",
    "itemTotal",
    "unitSellPrice",
    "unitRegularPrice",
    "completedAmount",
    "cancelledAmount",
    "returnedAmount",
    "paidAmount",
    "discountTotal",
    "completedDiscountAmount",
    "cancelledDiscountAmount",
    "returnedDiscountAmount",
    "taxTotal",
    "completedTaxAmount",
    "cancelledTaxAmount",
    "returnedTaxAmount",
    "inclusiveTaxTotal",
    "completedInclusiveTaxAmount",
    "cancelledInclusiveTaxAmount",
    "returnedInclusiveTaxAmount",
    "prcOvdLineDesc",
    "promoLineDesc",
    "discntLineColDesc",
    "taxLineColDesc",
    "departmentId",
    "restrictiveAge",
    "discountableFlag",
    "damageDiscountableFlag",
    "employeeDiscountableFlag",
    "itemType",
    "manufacturerUpc",
    "merchandiseHierarchyGroupId",
    "productGroup",
    "restockingFeeFlag",
    "returnEligibleFlag",
    "serializedItemFlag",
    "validateSerialNumberFlag",
    "allowNewSerialNumberFlag",
    "sizeRequiredFlag",
    "taxGroupId",
    "taxableFlag",
    "weight",
    "serialNumber",
    "sizeCode",
    "giftRegistryId",
    "giftReceiptedItemFlag",
    "shippingChargeFlag",
    "entryMethod",
    "alterationItem",
    "giftCardItem",
    "fulfillmentSeqNo"
})
@XmlRootElement(name = "CustOrdItmDesc")
public class CustOrdItmDesc {

    @XmlElement(name = "line_item_no")
    protected int lineItemNo;
    @XmlElement(name = "captured_line_item_no")
    protected int capturedLineItemNo;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "item_upc", required = true)
    protected String itemUpc;
    @XmlElement(name = "item_description", required = true)
    protected String itemDescription;
    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "available_quantity", required = true)
    protected BigDecimal availableQuantity;
    @XmlElement(name = "completed_quantity", required = true)
    protected BigDecimal completedQuantity;
    @XmlElement(name = "cancelled_quantity", required = true)
    protected BigDecimal cancelledQuantity;
    @XmlElement(name = "returned_quantity", required = true)
    protected BigDecimal returnedQuantity;
    @XmlElement(name = "unit_of_measure")
    protected String unitOfMeasure;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "item_total")
    protected BigDecimal itemTotal;
    @XmlElement(name = "unit_sell_price", required = true)
    protected BigDecimal unitSellPrice;
    @XmlElement(name = "unit_regular_price", required = true)
    protected BigDecimal unitRegularPrice;
    @XmlElement(name = "completed_amount", required = true)
    protected BigDecimal completedAmount;
    @XmlElement(name = "cancelled_amount", required = true)
    protected BigDecimal cancelledAmount;
    @XmlElement(name = "returned_amount", required = true)
    protected BigDecimal returnedAmount;
    @XmlElement(name = "paid_amount", required = true)
    protected BigDecimal paidAmount;
    @XmlElement(name = "discount_total")
    protected BigDecimal discountTotal;
    @XmlElement(name = "completed_discount_amount")
    protected BigDecimal completedDiscountAmount;
    @XmlElement(name = "cancelled_discount_amount")
    protected BigDecimal cancelledDiscountAmount;
    @XmlElement(name = "returned_discount_amount")
    protected BigDecimal returnedDiscountAmount;
    @XmlElement(name = "tax_total")
    protected BigDecimal taxTotal;
    @XmlElement(name = "completed_tax_amount")
    protected BigDecimal completedTaxAmount;
    @XmlElement(name = "cancelled_tax_amount")
    protected BigDecimal cancelledTaxAmount;
    @XmlElement(name = "returned_tax_amount")
    protected BigDecimal returnedTaxAmount;
    @XmlElement(name = "inclusive_tax_total")
    protected BigDecimal inclusiveTaxTotal;
    @XmlElement(name = "completed_inclusive_tax_amount")
    protected BigDecimal completedInclusiveTaxAmount;
    @XmlElement(name = "cancelled_inclusive_tax_amount")
    protected BigDecimal cancelledInclusiveTaxAmount;
    @XmlElement(name = "returned_inclusive_tax_amount")
    protected BigDecimal returnedInclusiveTaxAmount;
    @XmlElement(name = "PrcOvdLineDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PrcOvdLineDesc/v1")
    protected PrcOvdLineDesc prcOvdLineDesc;
    @XmlElement(name = "PromoLineDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PromoLineDesc/v1")
    protected PromoLineDesc promoLineDesc;
    @XmlElement(name = "DiscntLineColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/DiscntLineColDesc/v1")
    protected DiscntLineColDesc discntLineColDesc;
    @XmlElement(name = "TaxLineColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/TaxLineColDesc/v1")
    protected TaxLineColDesc taxLineColDesc;
    @XmlElement(name = "department_id", required = true)
    protected String departmentId;
    @XmlElement(name = "restrictive_age", required = true)
    protected BigDecimal restrictiveAge;
    @XmlElement(name = "discountable_flag", required = true)
    protected Flag discountableFlag;
    @XmlElement(name = "damage_discountable_flag", required = true)
    protected Flag damageDiscountableFlag;
    @XmlElement(name = "employee_discountable_flag", required = true)
    protected Flag employeeDiscountableFlag;
    @XmlElement(name = "item_type", required = true)
    protected ItemType itemType;
    @XmlElement(name = "manufacturer_upc")
    protected String manufacturerUpc;
    @XmlElement(name = "merchandise_hierarchy_group_id", required = true)
    protected String merchandiseHierarchyGroupId;
    @XmlElement(name = "product_group")
    protected ProductGroup productGroup;
    @XmlElement(name = "restocking_fee_flag", required = true)
    protected Flag restockingFeeFlag;
    @XmlElement(name = "return_eligible_flag", required = true)
    protected Flag returnEligibleFlag;
    @XmlElement(name = "serialized_item_flag", required = true)
    protected Flag serializedItemFlag;
    @XmlElement(name = "validate_serial_number_flag")
    protected Flag validateSerialNumberFlag;
    @XmlElement(name = "allow_new_serial_number_flag")
    protected Flag allowNewSerialNumberFlag;
    @XmlElement(name = "size_required_flag", required = true)
    protected Flag sizeRequiredFlag;
    @XmlElement(name = "tax_group_id", required = true)
    protected BigDecimal taxGroupId;
    @XmlElement(name = "taxable_flag", required = true)
    protected Flag taxableFlag;
    protected BigDecimal weight;
    @XmlElement(name = "serial_number")
    protected String serialNumber;
    @XmlElement(name = "size_code")
    protected String sizeCode;
    @XmlElement(name = "gift_registry_id")
    protected String giftRegistryId;
    @XmlElement(name = "gift_receipted_item_flag", required = true)
    protected Flag giftReceiptedItemFlag;
    @XmlElement(name = "shipping_charge_flag", required = true)
    protected Flag shippingChargeFlag;
    @XmlElement(name = "entry_method")
    protected EntryMethod entryMethod;
    @XmlElement(name = "AlterationItem")
    protected AlterationItem alterationItem;
    @XmlElement(name = "GiftCardItem")
    protected GiftCardItem giftCardItem;
    @XmlElement(name = "fulfillment_seq_no")
    protected int fulfillmentSeqNo;

    /**
     * Gets the value of the lineItemNo property.
     * 
     */
    public int getLineItemNo() {
        return lineItemNo;
    }

    /**
     * Sets the value of the lineItemNo property.
     * 
     */
    public void setLineItemNo(int value) {
        this.lineItemNo = value;
    }

    /**
     * Gets the value of the capturedLineItemNo property.
     * 
     */
    public int getCapturedLineItemNo() {
        return capturedLineItemNo;
    }

    /**
     * Sets the value of the capturedLineItemNo property.
     * 
     */
    public void setCapturedLineItemNo(int value) {
        this.capturedLineItemNo = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
    }

    /**
     * Gets the value of the itemUpc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemUpc() {
        return itemUpc;
    }

    /**
     * Sets the value of the itemUpc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemUpc(String value) {
        this.itemUpc = value;
    }

    /**
     * Gets the value of the itemDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Sets the value of the itemDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemDescription(String value) {
        this.itemDescription = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the availableQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvailableQuantity() {
        return availableQuantity;
    }

    /**
     * Sets the value of the availableQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvailableQuantity(BigDecimal value) {
        this.availableQuantity = value;
    }

    /**
     * Gets the value of the completedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedQuantity() {
        return completedQuantity;
    }

    /**
     * Sets the value of the completedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedQuantity(BigDecimal value) {
        this.completedQuantity = value;
    }

    /**
     * Gets the value of the cancelledQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledQuantity() {
        return cancelledQuantity;
    }

    /**
     * Sets the value of the cancelledQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledQuantity(BigDecimal value) {
        this.cancelledQuantity = value;
    }

    /**
     * Gets the value of the returnedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedQuantity() {
        return returnedQuantity;
    }

    /**
     * Sets the value of the returnedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedQuantity(BigDecimal value) {
        this.returnedQuantity = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the itemTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    /**
     * Sets the value of the itemTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setItemTotal(BigDecimal value) {
        this.itemTotal = value;
    }

    /**
     * Gets the value of the unitSellPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitSellPrice() {
        return unitSellPrice;
    }

    /**
     * Sets the value of the unitSellPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitSellPrice(BigDecimal value) {
        this.unitSellPrice = value;
    }

    /**
     * Gets the value of the unitRegularPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitRegularPrice() {
        return unitRegularPrice;
    }

    /**
     * Sets the value of the unitRegularPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitRegularPrice(BigDecimal value) {
        this.unitRegularPrice = value;
    }

    /**
     * Gets the value of the completedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedAmount() {
        return completedAmount;
    }

    /**
     * Sets the value of the completedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedAmount(BigDecimal value) {
        this.completedAmount = value;
    }

    /**
     * Gets the value of the cancelledAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledAmount() {
        return cancelledAmount;
    }

    /**
     * Sets the value of the cancelledAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledAmount(BigDecimal value) {
        this.cancelledAmount = value;
    }

    /**
     * Gets the value of the returnedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    /**
     * Sets the value of the returnedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedAmount(BigDecimal value) {
        this.returnedAmount = value;
    }

    /**
     * Gets the value of the paidAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    /**
     * Sets the value of the paidAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPaidAmount(BigDecimal value) {
        this.paidAmount = value;
    }

    /**
     * Gets the value of the discountTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    /**
     * Sets the value of the discountTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDiscountTotal(BigDecimal value) {
        this.discountTotal = value;
    }

    /**
     * Gets the value of the completedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedDiscountAmount() {
        return completedDiscountAmount;
    }

    /**
     * Sets the value of the completedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedDiscountAmount(BigDecimal value) {
        this.completedDiscountAmount = value;
    }

    /**
     * Gets the value of the cancelledDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledDiscountAmount() {
        return cancelledDiscountAmount;
    }

    /**
     * Sets the value of the cancelledDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledDiscountAmount(BigDecimal value) {
        this.cancelledDiscountAmount = value;
    }

    /**
     * Gets the value of the returnedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    /**
     * Sets the value of the returnedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedDiscountAmount(BigDecimal value) {
        this.returnedDiscountAmount = value;
    }

    /**
     * Gets the value of the taxTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    /**
     * Sets the value of the taxTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxTotal(BigDecimal value) {
        this.taxTotal = value;
    }

    /**
     * Gets the value of the completedTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedTaxAmount() {
        return completedTaxAmount;
    }

    /**
     * Sets the value of the completedTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedTaxAmount(BigDecimal value) {
        this.completedTaxAmount = value;
    }

    /**
     * Gets the value of the cancelledTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledTaxAmount() {
        return cancelledTaxAmount;
    }

    /**
     * Sets the value of the cancelledTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledTaxAmount(BigDecimal value) {
        this.cancelledTaxAmount = value;
    }

    /**
     * Gets the value of the returnedTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    /**
     * Sets the value of the returnedTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedTaxAmount(BigDecimal value) {
        this.returnedTaxAmount = value;
    }

    /**
     * Gets the value of the inclusiveTaxTotal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInclusiveTaxTotal() {
        return inclusiveTaxTotal;
    }

    /**
     * Sets the value of the inclusiveTaxTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInclusiveTaxTotal(BigDecimal value) {
        this.inclusiveTaxTotal = value;
    }

    /**
     * Gets the value of the completedInclusiveTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedInclusiveTaxAmount() {
        return completedInclusiveTaxAmount;
    }

    /**
     * Sets the value of the completedInclusiveTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedInclusiveTaxAmount(BigDecimal value) {
        this.completedInclusiveTaxAmount = value;
    }

    /**
     * Gets the value of the cancelledInclusiveTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledInclusiveTaxAmount() {
        return cancelledInclusiveTaxAmount;
    }

    /**
     * Sets the value of the cancelledInclusiveTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledInclusiveTaxAmount(BigDecimal value) {
        this.cancelledInclusiveTaxAmount = value;
    }

    /**
     * Gets the value of the returnedInclusiveTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedInclusiveTaxAmount() {
        return returnedInclusiveTaxAmount;
    }

    /**
     * Sets the value of the returnedInclusiveTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedInclusiveTaxAmount(BigDecimal value) {
        this.returnedInclusiveTaxAmount = value;
    }

    /**
     * The manual price override applied to the item.
     * 
     * @return
     *     possible object is
     *     {@link PrcOvdLineDesc }
     *     
     */
    public PrcOvdLineDesc getPrcOvdLineDesc() {
        return prcOvdLineDesc;
    }

    /**
     * Sets the value of the prcOvdLineDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrcOvdLineDesc }
     *     
     */
    public void setPrcOvdLineDesc(PrcOvdLineDesc value) {
        this.prcOvdLineDesc = value;
    }

    /**
     * The price promtion applied to the item.
     * 
     * @return
     *     possible object is
     *     {@link PromoLineDesc }
     *     
     */
    public PromoLineDesc getPromoLineDesc() {
        return promoLineDesc;
    }

    /**
     * Sets the value of the promoLineDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link PromoLineDesc }
     *     
     */
    public void setPromoLineDesc(PromoLineDesc value) {
        this.promoLineDesc = value;
    }

    /**
     * A collection of discount line items for the item.
     * 
     * @return
     *     possible object is
     *     {@link DiscntLineColDesc }
     *     
     */
    public DiscntLineColDesc getDiscntLineColDesc() {
        return discntLineColDesc;
    }

    /**
     * Sets the value of the discntLineColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiscntLineColDesc }
     *     
     */
    public void setDiscntLineColDesc(DiscntLineColDesc value) {
        this.discntLineColDesc = value;
    }

    /**
     * A collection of tax line items of the item
     * 
     * @return
     *     possible object is
     *     {@link TaxLineColDesc }
     *     
     */
    public TaxLineColDesc getTaxLineColDesc() {
        return taxLineColDesc;
    }

    /**
     * Sets the value of the taxLineColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxLineColDesc }
     *     
     */
    public void setTaxLineColDesc(TaxLineColDesc value) {
        this.taxLineColDesc = value;
    }

    /**
     * Gets the value of the departmentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     * Sets the value of the departmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartmentId(String value) {
        this.departmentId = value;
    }

    /**
     * Gets the value of the restrictiveAge property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRestrictiveAge() {
        return restrictiveAge;
    }

    /**
     * Sets the value of the restrictiveAge property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRestrictiveAge(BigDecimal value) {
        this.restrictiveAge = value;
    }

    /**
     * Gets the value of the discountableFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getDiscountableFlag() {
        return discountableFlag;
    }

    /**
     * Sets the value of the discountableFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setDiscountableFlag(Flag value) {
        this.discountableFlag = value;
    }

    /**
     * Gets the value of the damageDiscountableFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getDamageDiscountableFlag() {
        return damageDiscountableFlag;
    }

    /**
     * Sets the value of the damageDiscountableFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setDamageDiscountableFlag(Flag value) {
        this.damageDiscountableFlag = value;
    }

    /**
     * Gets the value of the employeeDiscountableFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getEmployeeDiscountableFlag() {
        return employeeDiscountableFlag;
    }

    /**
     * Sets the value of the employeeDiscountableFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setEmployeeDiscountableFlag(Flag value) {
        this.employeeDiscountableFlag = value;
    }

    /**
     * Gets the value of the itemType property.
     * 
     * @return
     *     possible object is
     *     {@link ItemType }
     *     
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * Sets the value of the itemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ItemType }
     *     
     */
    public void setItemType(ItemType value) {
        this.itemType = value;
    }

    /**
     * Gets the value of the manufacturerUpc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManufacturerUpc() {
        return manufacturerUpc;
    }

    /**
     * Sets the value of the manufacturerUpc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManufacturerUpc(String value) {
        this.manufacturerUpc = value;
    }

    /**
     * Gets the value of the merchandiseHierarchyGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchandiseHierarchyGroupId() {
        return merchandiseHierarchyGroupId;
    }

    /**
     * Sets the value of the merchandiseHierarchyGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchandiseHierarchyGroupId(String value) {
        this.merchandiseHierarchyGroupId = value;
    }

    /**
     * Gets the value of the productGroup property.
     * 
     * @return
     *     possible object is
     *     {@link ProductGroup }
     *     
     */
    public ProductGroup getProductGroup() {
        return productGroup;
    }

    /**
     * Sets the value of the productGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProductGroup }
     *     
     */
    public void setProductGroup(ProductGroup value) {
        this.productGroup = value;
    }

    /**
     * Gets the value of the restockingFeeFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRestockingFeeFlag() {
        return restockingFeeFlag;
    }

    /**
     * Sets the value of the restockingFeeFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRestockingFeeFlag(Flag value) {
        this.restockingFeeFlag = value;
    }

    /**
     * Gets the value of the returnEligibleFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getReturnEligibleFlag() {
        return returnEligibleFlag;
    }

    /**
     * Sets the value of the returnEligibleFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setReturnEligibleFlag(Flag value) {
        this.returnEligibleFlag = value;
    }

    /**
     * Gets the value of the serializedItemFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getSerializedItemFlag() {
        return serializedItemFlag;
    }

    /**
     * Sets the value of the serializedItemFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setSerializedItemFlag(Flag value) {
        this.serializedItemFlag = value;
    }

    /**
     * Gets the value of the validateSerialNumberFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getValidateSerialNumberFlag() {
        return validateSerialNumberFlag;
    }

    /**
     * Sets the value of the validateSerialNumberFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setValidateSerialNumberFlag(Flag value) {
        this.validateSerialNumberFlag = value;
    }

    /**
     * Gets the value of the allowNewSerialNumberFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getAllowNewSerialNumberFlag() {
        return allowNewSerialNumberFlag;
    }

    /**
     * Sets the value of the allowNewSerialNumberFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setAllowNewSerialNumberFlag(Flag value) {
        this.allowNewSerialNumberFlag = value;
    }

    /**
     * Gets the value of the sizeRequiredFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getSizeRequiredFlag() {
        return sizeRequiredFlag;
    }

    /**
     * Sets the value of the sizeRequiredFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setSizeRequiredFlag(Flag value) {
        this.sizeRequiredFlag = value;
    }

    /**
     * Gets the value of the taxGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxGroupId() {
        return taxGroupId;
    }

    /**
     * Sets the value of the taxGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxGroupId(BigDecimal value) {
        this.taxGroupId = value;
    }

    /**
     * Gets the value of the taxableFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getTaxableFlag() {
        return taxableFlag;
    }

    /**
     * Sets the value of the taxableFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setTaxableFlag(Flag value) {
        this.taxableFlag = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeight(BigDecimal value) {
        this.weight = value;
    }

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

    /**
     * Gets the value of the sizeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSizeCode() {
        return sizeCode;
    }

    /**
     * Sets the value of the sizeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSizeCode(String value) {
        this.sizeCode = value;
    }

    /**
     * Gets the value of the giftRegistryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGiftRegistryId() {
        return giftRegistryId;
    }

    /**
     * Sets the value of the giftRegistryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGiftRegistryId(String value) {
        this.giftRegistryId = value;
    }

    /**
     * Gets the value of the giftReceiptedItemFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getGiftReceiptedItemFlag() {
        return giftReceiptedItemFlag;
    }

    /**
     * Sets the value of the giftReceiptedItemFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setGiftReceiptedItemFlag(Flag value) {
        this.giftReceiptedItemFlag = value;
    }

    /**
     * Gets the value of the shippingChargeFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getShippingChargeFlag() {
        return shippingChargeFlag;
    }

    /**
     * Sets the value of the shippingChargeFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setShippingChargeFlag(Flag value) {
        this.shippingChargeFlag = value;
    }

    /**
     * Gets the value of the entryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EntryMethod }
     *     
     */
    public EntryMethod getEntryMethod() {
        return entryMethod;
    }

    /**
     * Sets the value of the entryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryMethod }
     *     
     */
    public void setEntryMethod(EntryMethod value) {
        this.entryMethod = value;
    }

    /**
     * Additional info if the order item is an
     *                                alteration service item
     * 
     * @return
     *     possible object is
     *     {@link AlterationItem }
     *     
     */
    public AlterationItem getAlterationItem() {
        return alterationItem;
    }

    /**
     * Sets the value of the alterationItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlterationItem }
     *     
     */
    public void setAlterationItem(AlterationItem value) {
        this.alterationItem = value;
    }

    /**
     * Additional info if the order item is a gift card
     *                                item.
     * 
     * @return
     *     possible object is
     *     {@link GiftCardItem }
     *     
     */
    public GiftCardItem getGiftCardItem() {
        return giftCardItem;
    }

    /**
     * Sets the value of the giftCardItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiftCardItem }
     *     
     */
    public void setGiftCardItem(GiftCardItem value) {
        this.giftCardItem = value;
    }

    /**
     * Gets the value of the fulfillmentSeqNo property.
     * 
     */
    public int getFulfillmentSeqNo() {
        return fulfillmentSeqNo;
    }

    /**
     * Sets the value of the fulfillmentSeqNo property.
     * 
     */
    public void setFulfillmentSeqNo(int value) {
        this.fulfillmentSeqNo = value;
    }

}
