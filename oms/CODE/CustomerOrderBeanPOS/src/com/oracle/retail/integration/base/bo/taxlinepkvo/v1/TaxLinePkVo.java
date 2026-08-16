
package com.oracle.retail.integration.base.bo.taxlinepkvo.v1;

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
 *         &lt;element name="completed_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="repriced_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="inclusive_tax_flag" type="{http://www.oracle.com/retail/integration/base/bo/TaxLinePkVo/v1}flag"/>
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
    "completedTaxAmount",
    "cancelledTaxAmount",
    "repricedTaxAmount",
    "inclusiveTaxFlag"
})
@XmlRootElement(name = "TaxLinePkVo")
public class TaxLinePkVo {

    @XmlElement(name = "line_no")
    protected int lineNo;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "completed_tax_amount", required = true)
    protected BigDecimal completedTaxAmount;
    @XmlElement(name = "cancelled_tax_amount", required = true)
    protected BigDecimal cancelledTaxAmount;
    @XmlElement(name = "repriced_tax_amount")
    protected BigDecimal repricedTaxAmount;
    @XmlElement(name = "inclusive_tax_flag", required = true)
    protected Flag inclusiveTaxFlag;

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
     * Gets the value of the inclusiveTaxFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getInclusiveTaxFlag() {
        return inclusiveTaxFlag;
    }

    /**
     * Sets the value of the inclusiveTaxFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setInclusiveTaxFlag(Flag value) {
        this.inclusiveTaxFlag = value;
    }

}
