
package com.oracle.retail.integration.base.bo.prcovdlinedesc.v1;

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
 *         &lt;element name="override_reason_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unit_overridden_price" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="authorizing_employee_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/PrcOvdLineDesc/v1}enum_entry_method" minOccurs="0"/>
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
    "overrideReasonCode",
    "currencyCode",
    "unitOverriddenPrice",
    "authorizingEmployeeId",
    "entryMethod"
})
@XmlRootElement(name = "PrcOvdLineDesc")
public class PrcOvdLineDesc {

    @XmlElement(name = "override_reason_code")
    protected String overrideReasonCode;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(name = "unit_overridden_price", required = true)
    protected BigDecimal unitOverriddenPrice;
    @XmlElement(name = "authorizing_employee_id")
    protected String authorizingEmployeeId;
    @XmlElement(name = "entry_method")
    protected EnumEntryMethod entryMethod;

    /**
     * Gets the value of the overrideReasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverrideReasonCode() {
        return overrideReasonCode;
    }

    /**
     * Sets the value of the overrideReasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverrideReasonCode(String value) {
        this.overrideReasonCode = value;
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
     * Gets the value of the unitOverriddenPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitOverriddenPrice() {
        return unitOverriddenPrice;
    }

    /**
     * Sets the value of the unitOverriddenPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitOverriddenPrice(BigDecimal value) {
        this.unitOverriddenPrice = value;
    }

    /**
     * Gets the value of the authorizingEmployeeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizingEmployeeId() {
        return authorizingEmployeeId;
    }

    /**
     * Sets the value of the authorizingEmployeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizingEmployeeId(String value) {
        this.authorizingEmployeeId = value;
    }

    /**
     * Gets the value of the entryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EnumEntryMethod }
     *     
     */
    public EnumEntryMethod getEntryMethod() {
        return entryMethod;
    }

    /**
     * Sets the value of the entryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumEntryMethod }
     *     
     */
    public void setEntryMethod(EnumEntryMethod value) {
        this.entryMethod = value;
    }

}
