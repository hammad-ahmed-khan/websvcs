
package com.oracle.retail.integration.base.bo.taxlinedesc.v1;

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
 *         &lt;element name="tax_authority_id" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="tax_group_id" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="tax_type_code" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="tax_holiday_flag" type="{http://www.oracle.com/retail/integration/base/bo/TaxLineDesc/v1}flag"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="taxable_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="completed_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cancelled_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="returned_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="inclusive_tax_flag" type="{http://www.oracle.com/retail/integration/base/bo/TaxLineDesc/v1}flag"/>
 *         &lt;element name="tax_mode" type="{http://www.oracle.com/retail/integration/base/bo/TaxLineDesc/v1}enum_tax_mode"/>
 *         &lt;element name="tax_mod_reason_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tax_mod_scope" type="{http://www.oracle.com/retail/integration/base/bo/TaxLineDesc/v1}enum_tax_mod_scope" minOccurs="0"/>
 *         &lt;element name="tax_rate" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="tax_rule_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tax_authority_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "taxAuthorityId",
    "taxGroupId",
    "taxTypeCode",
    "taxHolidayFlag",
    "currencyCode",
    "taxableAmount",
    "taxAmount",
    "completedTaxAmount",
    "cancelledTaxAmount",
    "returnedTaxAmount",
    "inclusiveTaxFlag",
    "taxMode",
    "taxModReasonCode",
    "taxModScope",
    "taxRate",
    "taxRuleName",
    "taxAuthorityName"
})
@XmlRootElement(name = "TaxLineDesc")
public class TaxLineDesc {

    @XmlElement(name = "line_no")
    protected int lineNo;
    @XmlElement(name = "tax_authority_id", required = true)
    protected BigDecimal taxAuthorityId;
    @XmlElement(name = "tax_group_id", required = true)
    protected BigDecimal taxGroupId;
    @XmlElement(name = "tax_type_code", required = true)
    protected BigDecimal taxTypeCode;
    @XmlElement(name = "tax_holiday_flag", required = true)
    protected Flag taxHolidayFlag;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "taxable_amount", required = true)
    protected BigDecimal taxableAmount;
    @XmlElement(name = "tax_amount", required = true)
    protected BigDecimal taxAmount;
    @XmlElement(name = "completed_tax_amount", required = true)
    protected BigDecimal completedTaxAmount;
    @XmlElement(name = "cancelled_tax_amount", required = true)
    protected BigDecimal cancelledTaxAmount;
    @XmlElement(name = "returned_tax_amount", required = true)
    protected BigDecimal returnedTaxAmount;
    @XmlElement(name = "inclusive_tax_flag", required = true)
    protected Flag inclusiveTaxFlag;
    @XmlElement(name = "tax_mode", required = true)
    protected EnumTaxMode taxMode;
    @XmlElement(name = "tax_mod_reason_code")
    protected String taxModReasonCode;
    @XmlElement(name = "tax_mod_scope")
    protected EnumTaxModScope taxModScope;
    @XmlElement(name = "tax_rate")
    protected BigDecimal taxRate;
    @XmlElement(name = "tax_rule_name")
    protected String taxRuleName;
    @XmlElement(name = "tax_authority_name")
    protected String taxAuthorityName;

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
     * Gets the value of the taxAuthorityId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxAuthorityId() {
        return taxAuthorityId;
    }

    /**
     * Sets the value of the taxAuthorityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxAuthorityId(BigDecimal value) {
        this.taxAuthorityId = value;
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
     * Gets the value of the taxTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxTypeCode() {
        return taxTypeCode;
    }

    /**
     * Sets the value of the taxTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxTypeCode(BigDecimal value) {
        this.taxTypeCode = value;
    }

    /**
     * Gets the value of the taxHolidayFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getTaxHolidayFlag() {
        return taxHolidayFlag;
    }

    /**
     * Sets the value of the taxHolidayFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setTaxHolidayFlag(Flag value) {
        this.taxHolidayFlag = value;
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
     * Gets the value of the taxableAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    /**
     * Sets the value of the taxableAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxableAmount(BigDecimal value) {
        this.taxableAmount = value;
    }

    /**
     * Gets the value of the taxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the value of the taxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxAmount(BigDecimal value) {
        this.taxAmount = value;
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

    /**
     * Gets the value of the taxMode property.
     * 
     * @return
     *     possible object is
     *     {@link EnumTaxMode }
     *     
     */
    public EnumTaxMode getTaxMode() {
        return taxMode;
    }

    /**
     * Sets the value of the taxMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTaxMode }
     *     
     */
    public void setTaxMode(EnumTaxMode value) {
        this.taxMode = value;
    }

    /**
     * Gets the value of the taxModReasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxModReasonCode() {
        return taxModReasonCode;
    }

    /**
     * Sets the value of the taxModReasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxModReasonCode(String value) {
        this.taxModReasonCode = value;
    }

    /**
     * Gets the value of the taxModScope property.
     * 
     * @return
     *     possible object is
     *     {@link EnumTaxModScope }
     *     
     */
    public EnumTaxModScope getTaxModScope() {
        return taxModScope;
    }

    /**
     * Sets the value of the taxModScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumTaxModScope }
     *     
     */
    public void setTaxModScope(EnumTaxModScope value) {
        this.taxModScope = value;
    }

    /**
     * Gets the value of the taxRate property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * Sets the value of the taxRate property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTaxRate(BigDecimal value) {
        this.taxRate = value;
    }

    /**
     * Gets the value of the taxRuleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxRuleName() {
        return taxRuleName;
    }

    /**
     * Sets the value of the taxRuleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxRuleName(String value) {
        this.taxRuleName = value;
    }

    /**
     * Gets the value of the taxAuthorityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxAuthorityName() {
        return taxAuthorityName;
    }

    /**
     * Sets the value of the taxAuthorityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxAuthorityName(String value) {
        this.taxAuthorityName = value;
    }

}
