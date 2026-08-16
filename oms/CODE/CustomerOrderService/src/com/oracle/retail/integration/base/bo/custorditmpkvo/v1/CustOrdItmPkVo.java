
package com.oracle.retail.integration.base.bo.custorditmpkvo.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.discntlinepkcolvo.v1.DiscntLinePkColVo;
import com.oracle.retail.integration.base.bo.taxlinepkcolvo.v1.TaxLinePkColVo;


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
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="completed_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="completed_repriced_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="unit_of_measure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="completed_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="repriced_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_new_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="repriced_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_new_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="repriced_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_new_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_inc_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_inc_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="repriced_inc_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="completed_new_inc_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="paid_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="serial_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/DiscntLinePkColVo/v1}DiscntLinePkColVo" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/TaxLinePkColVo/v1}TaxLinePkColVo" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmPkVo/v1}NewPricePkItems" minOccurs="0"/>
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
    "fulfillOrderId",
    "completedQuantity",
    "cancelledQuantity",
    "completedRepricedQuantity",
    "unitOfMeasure",
    "currencyCode",
    "completedAmount",
    "cancelledAmount",
    "repricedAmount",
    "completedNewAmount",
    "completedDiscountAmount",
    "cancelledDiscountAmount",
    "repricedDiscountAmount",
    "completedNewDiscountAmount",
    "completedTaxAmount",
    "cancelledTaxAmount",
    "repricedTaxAmount",
    "completedNewTaxAmount",
    "completedIncTaxAmount",
    "cancelledIncTaxAmount",
    "repricedIncTaxAmount",
    "completedNewIncTaxAmount",
    "paidAmount",
    "serialNumber",
    "discntLinePkColVo",
    "taxLinePkColVo",
    "newPricePkItems"
})
@XmlRootElement(name = "CustOrdItmPkVo")
public class CustOrdItmPkVo {

    @XmlElement(name = "line_item_no")
    protected int lineItemNo;
    @XmlElement(name = "fulfill_order_id", required = true)
    protected String fulfillOrderId;
    @XmlElement(name = "completed_quantity", required = true)
    protected BigDecimal completedQuantity;
    @XmlElement(name = "cancelled_quantity", required = true)
    protected BigDecimal cancelledQuantity;
    @XmlElement(name = "completed_repriced_quantity")
    protected BigDecimal completedRepricedQuantity;
    @XmlElement(name = "unit_of_measure")
    protected String unitOfMeasure;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "completed_amount", required = true)
    protected BigDecimal completedAmount;
    @XmlElement(name = "cancelled_amount", required = true)
    protected BigDecimal cancelledAmount;
    @XmlElement(name = "repriced_amount")
    protected BigDecimal repricedAmount;
    @XmlElement(name = "completed_new_amount")
    protected BigDecimal completedNewAmount;
    @XmlElement(name = "completed_discount_amount")
    protected BigDecimal completedDiscountAmount;
    @XmlElement(name = "cancelled_discount_amount")
    protected BigDecimal cancelledDiscountAmount;
    @XmlElement(name = "repriced_discount_amount")
    protected BigDecimal repricedDiscountAmount;
    @XmlElement(name = "completed_new_discount_amount")
    protected BigDecimal completedNewDiscountAmount;
    @XmlElement(name = "completed_tax_amount")
    protected BigDecimal completedTaxAmount;
    @XmlElement(name = "cancelled_tax_amount")
    protected BigDecimal cancelledTaxAmount;
    @XmlElement(name = "repriced_tax_amount")
    protected BigDecimal repricedTaxAmount;
    @XmlElement(name = "completed_new_tax_amount")
    protected BigDecimal completedNewTaxAmount;
    @XmlElement(name = "completed_inc_tax_amount")
    protected BigDecimal completedIncTaxAmount;
    @XmlElement(name = "cancelled_inc_tax_amount")
    protected BigDecimal cancelledIncTaxAmount;
    @XmlElement(name = "repriced_inc_tax_amount")
    protected BigDecimal repricedIncTaxAmount;
    @XmlElement(name = "completed_new_inc_tax_amount")
    protected BigDecimal completedNewIncTaxAmount;
    @XmlElement(name = "paid_amount")
    protected BigDecimal paidAmount;
    @XmlElement(name = "serial_number")
    protected String serialNumber;
    @XmlElement(name = "DiscntLinePkColVo", namespace = "http://www.oracle.com/retail/integration/base/bo/DiscntLinePkColVo/v1")
    protected DiscntLinePkColVo discntLinePkColVo;
    @XmlElement(name = "TaxLinePkColVo", namespace = "http://www.oracle.com/retail/integration/base/bo/TaxLinePkColVo/v1")
    protected TaxLinePkColVo taxLinePkColVo;
    @XmlElement(name = "NewPricePkItems")
    protected NewPricePkItems newPricePkItems;

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
     * Gets the value of the fulfillOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillOrderId() {
        return fulfillOrderId;
    }

    /**
     * Sets the value of the fulfillOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillOrderId(String value) {
        this.fulfillOrderId = value;
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
     * Gets the value of the completedRepricedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedRepricedQuantity() {
        return completedRepricedQuantity;
    }

    /**
     * Sets the value of the completedRepricedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedRepricedQuantity(BigDecimal value) {
        this.completedRepricedQuantity = value;
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
     * Gets the value of the repricedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepricedAmount() {
        return repricedAmount;
    }

    /**
     * Sets the value of the repricedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepricedAmount(BigDecimal value) {
        this.repricedAmount = value;
    }

    /**
     * Gets the value of the completedNewAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedNewAmount() {
        return completedNewAmount;
    }

    /**
     * Sets the value of the completedNewAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedNewAmount(BigDecimal value) {
        this.completedNewAmount = value;
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
     * Gets the value of the repricedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepricedDiscountAmount() {
        return repricedDiscountAmount;
    }

    /**
     * Sets the value of the repricedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepricedDiscountAmount(BigDecimal value) {
        this.repricedDiscountAmount = value;
    }

    /**
     * Gets the value of the completedNewDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedNewDiscountAmount() {
        return completedNewDiscountAmount;
    }

    /**
     * Sets the value of the completedNewDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedNewDiscountAmount(BigDecimal value) {
        this.completedNewDiscountAmount = value;
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
     * Gets the value of the repricedTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepricedTaxAmount() {
        return repricedTaxAmount;
    }

    /**
     * Sets the value of the repricedTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepricedTaxAmount(BigDecimal value) {
        this.repricedTaxAmount = value;
    }

    /**
     * Gets the value of the completedNewTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedNewTaxAmount() {
        return completedNewTaxAmount;
    }

    /**
     * Sets the value of the completedNewTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedNewTaxAmount(BigDecimal value) {
        this.completedNewTaxAmount = value;
    }

    /**
     * Gets the value of the completedIncTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedIncTaxAmount() {
        return completedIncTaxAmount;
    }

    /**
     * Sets the value of the completedIncTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedIncTaxAmount(BigDecimal value) {
        this.completedIncTaxAmount = value;
    }

    /**
     * Gets the value of the cancelledIncTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelledIncTaxAmount() {
        return cancelledIncTaxAmount;
    }

    /**
     * Sets the value of the cancelledIncTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelledIncTaxAmount(BigDecimal value) {
        this.cancelledIncTaxAmount = value;
    }

    /**
     * Gets the value of the repricedIncTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRepricedIncTaxAmount() {
        return repricedIncTaxAmount;
    }

    /**
     * Sets the value of the repricedIncTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRepricedIncTaxAmount(BigDecimal value) {
        this.repricedIncTaxAmount = value;
    }

    /**
     * Gets the value of the completedNewIncTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCompletedNewIncTaxAmount() {
        return completedNewIncTaxAmount;
    }

    /**
     * Sets the value of the completedNewIncTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCompletedNewIncTaxAmount(BigDecimal value) {
        this.completedNewIncTaxAmount = value;
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
     * A collection of discounts applied to item units
     *                                picked up or cancelled in this transaction for
     *                                this order line item.
     * 
     * @return
     *     possible object is
     *     {@link DiscntLinePkColVo }
     *     
     */
    public DiscntLinePkColVo getDiscntLinePkColVo() {
        return discntLinePkColVo;
    }

    /**
     * Sets the value of the discntLinePkColVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiscntLinePkColVo }
     *     
     */
    public void setDiscntLinePkColVo(DiscntLinePkColVo value) {
        this.discntLinePkColVo = value;
    }

    /**
     * A collection of tax applied to item units picked
     *                                up or cancelled in this transaction for this
     *                                order line item.
     * 
     * @return
     *     possible object is
     *     {@link TaxLinePkColVo }
     *     
     */
    public TaxLinePkColVo getTaxLinePkColVo() {
        return taxLinePkColVo;
    }

    /**
     * Sets the value of the taxLinePkColVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxLinePkColVo }
     *     
     */
    public void setTaxLinePkColVo(TaxLinePkColVo value) {
        this.taxLinePkColVo = value;
    }

    /**
     * Picked up order items with new price.
     * 
     * @return
     *     possible object is
     *     {@link NewPricePkItems }
     *     
     */
    public NewPricePkItems getNewPricePkItems() {
        return newPricePkItems;
    }

    /**
     * Sets the value of the newPricePkItems property.
     * 
     * @param value
     *     allowed object is
     *     {@link NewPricePkItems }
     *     
     */
    public void setNewPricePkItems(NewPricePkItems value) {
        this.newPricePkItems = value;
    }

}
