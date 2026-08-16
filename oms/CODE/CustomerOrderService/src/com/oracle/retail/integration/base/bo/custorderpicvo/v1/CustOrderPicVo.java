
package com.oracle.retail.integration.base.bo.custorderpicvo.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.oracle.retail.integration.base.bo.custorddeldesc.v1.CustOrdDelDesc;
import com.oracle.retail.integration.base.bo.custorditmpkcolvo.v1.CustOrdItmPkColVo;
import com.oracle.retail.integration.base.bo.paymentcoldesc.v1.PaymentColDesc;


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
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="completed_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cancelled_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
 *         &lt;element name="rounding_adjustment" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="refund_amount_offset_by_sale" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="update_timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmPkColVo/v1}CustOrdItmPkColVo"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdDelDesc/v1}CustOrdDelDesc"/>
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
@XmlType(name = "", propOrder = {
    "customerOrderId",
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
    "roundingAdjustment",
    "refundAmountOffsetBySale",
    "updateTimestamp",
    "custOrdItmPkColVo",
    "custOrdDelDesc",
    "paymentColDesc"
})
@XmlRootElement(name = "CustOrderPicVo")
public class CustOrderPicVo {

    @XmlElement(name = "customer_order_id", required = true)
    protected String customerOrderId;
    @XmlElement(name = "currency_code")
    protected String currencyCode;
    @XmlElement(name = "completed_amount")
    protected BigDecimal completedAmount;
    @XmlElement(name = "cancelled_amount")
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
    @XmlElement(name = "rounding_adjustment")
    protected BigDecimal roundingAdjustment;
    @XmlElement(name = "refund_amount_offset_by_sale")
    protected BigDecimal refundAmountOffsetBySale;
    @XmlElement(name = "update_timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateTimestamp;
    @XmlElement(name = "CustOrdItmPkColVo", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdItmPkColVo/v1", required = true)
    protected CustOrdItmPkColVo custOrdItmPkColVo;
    @XmlElement(name = "CustOrdDelDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdDelDesc/v1", required = true)
    protected CustOrdDelDesc custOrdDelDesc;
    @XmlElement(name = "PaymentColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PaymentColDesc/v1")
    protected PaymentColDesc paymentColDesc;

    /**
     * Gets the value of the customerOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderId() {
        return customerOrderId;
    }

    /**
     * Sets the value of the customerOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderId(String value) {
        this.customerOrderId = value;
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
     * Gets the value of the roundingAdjustment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRoundingAdjustment() {
        return roundingAdjustment;
    }

    /**
     * Sets the value of the roundingAdjustment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRoundingAdjustment(BigDecimal value) {
        this.roundingAdjustment = value;
    }

    /**
     * Gets the value of the refundAmountOffsetBySale property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRefundAmountOffsetBySale() {
        return refundAmountOffsetBySale;
    }

    /**
     * Sets the value of the refundAmountOffsetBySale property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRefundAmountOffsetBySale(BigDecimal value) {
        this.refundAmountOffsetBySale = value;
    }

    /**
     * Gets the value of the updateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Sets the value of the updateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateTimestamp(XMLGregorianCalendar value) {
        this.updateTimestamp = value;
    }

    /**
     * A collection of order items picked up or
     *                                cancelled..
     * 
     * @return
     *     possible object is
     *     {@link CustOrdItmPkColVo }
     *     
     */
    public CustOrdItmPkColVo getCustOrdItmPkColVo() {
        return custOrdItmPkColVo;
    }

    /**
     * Sets the value of the custOrdItmPkColVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrdItmPkColVo }
     *     
     */
    public void setCustOrdItmPkColVo(CustOrdItmPkColVo value) {
        this.custOrdItmPkColVo = value;
    }

    /**
     * Delivery information of the order items picked
     *                                up or cancelled..
     * 
     * @return
     *     possible object is
     *     {@link CustOrdDelDesc }
     *     
     */
    public CustOrdDelDesc getCustOrdDelDesc() {
        return custOrdDelDesc;
    }

    /**
     * Sets the value of the custOrdDelDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrdDelDesc }
     *     
     */
    public void setCustOrdDelDesc(CustOrdDelDesc value) {
        this.custOrdDelDesc = value;
    }

    /**
     * A collection of order payments to add to the
     *                                order.
     * 
     * @return
     *     possible object is
     *     {@link PaymentColDesc }
     *     
     */
    public PaymentColDesc getPaymentColDesc() {
        return paymentColDesc;
    }

    /**
     * Sets the value of the paymentColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentColDesc }
     *     
     */
    public void setPaymentColDesc(PaymentColDesc value) {
        this.paymentColDesc = value;
    }

}
