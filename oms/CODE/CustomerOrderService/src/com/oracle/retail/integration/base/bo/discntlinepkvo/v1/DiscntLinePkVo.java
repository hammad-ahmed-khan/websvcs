
package com.oracle.retail.integration.base.bo.discntlinepkvo.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="line_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="completed_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="repriced_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
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
    "lineNo",
    "currencyCode",
    "completedDiscountAmount",
    "cancelledDiscountAmount",
    "repricedDiscountAmount"
})
@XmlRootElement(name = "DiscntLinePkVo")
public class DiscntLinePkVo {

    @XmlElement(name = "line_no")
    protected int lineNo;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "completed_discount_amount", required = true)
    protected BigDecimal completedDiscountAmount;
    @XmlElement(name = "cancelled_discount_amount", required = true)
    protected BigDecimal cancelledDiscountAmount;
    @XmlElement(name = "repriced_discount_amount")
    protected BigDecimal repricedDiscountAmount;

    /**
     * Gets the value of the lineNo property.
     * 
     */
    public int getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     * 
     */
    public void setLineNo(int value) {
        this.lineNo = value;
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

}
