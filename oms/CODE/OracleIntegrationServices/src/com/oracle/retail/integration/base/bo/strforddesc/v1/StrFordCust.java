
package com.oracle.retail.integration.base.bo.strforddesc.v1;

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
 *         &lt;element name="customer_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="delivery_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_phonetic_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_phonetic_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_preferred_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_company_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_address1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_address2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_address3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_postal_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_phonetic_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_phonetic_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_preferred_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_company_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_address1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_address2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_address3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_postal_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="billing_phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "customerId",
    "deliveryFirstName",
    "deliveryPhoneticFirstName",
    "deliveryLastName",
    "deliveryPhoneticLastName",
    "deliveryPreferredName",
    "deliveryCompanyName",
    "deliveryAddress1",
    "deliveryAddress2",
    "deliveryAddress3",
    "deliveryCounty",
    "deliveryCity",
    "deliveryState",
    "deliveryPostalCode",
    "deliveryCountry",
    "deliveryPhone",
    "billingFirstName",
    "billingPhoneticFirstName",
    "billingLastName",
    "billingPhoneticLastName",
    "billingPreferredName",
    "billingCompanyName",
    "billingAddress1",
    "billingAddress2",
    "billingAddress3",
    "billingCounty",
    "billingCity",
    "billingState",
    "billingPostalCode",
    "billingCountry",
    "billingPhone"
})
@XmlRootElement(name = "StrFordCust")
public class StrFordCust {

    @XmlElement(name = "customer_id", required = true)
    protected String customerId;
    @XmlElement(name = "delivery_first_name")
    protected String deliveryFirstName;
    @XmlElement(name = "delivery_phonetic_first_name")
    protected String deliveryPhoneticFirstName;
    @XmlElement(name = "delivery_last_name")
    protected String deliveryLastName;
    @XmlElement(name = "delivery_phonetic_last_name")
    protected String deliveryPhoneticLastName;
    @XmlElement(name = "delivery_preferred_name")
    protected String deliveryPreferredName;
    @XmlElement(name = "delivery_company_name")
    protected String deliveryCompanyName;
    @XmlElement(name = "delivery_address1")
    protected String deliveryAddress1;
    @XmlElement(name = "delivery_address2")
    protected String deliveryAddress2;
    @XmlElement(name = "delivery_address3")
    protected String deliveryAddress3;
    @XmlElement(name = "delivery_county")
    protected String deliveryCounty;
    @XmlElement(name = "delivery_city")
    protected String deliveryCity;
    @XmlElement(name = "delivery_state")
    protected String deliveryState;
    @XmlElement(name = "delivery_postal_code")
    protected String deliveryPostalCode;
    @XmlElement(name = "delivery_country")
    protected String deliveryCountry;
    @XmlElement(name = "delivery_phone")
    protected String deliveryPhone;
    @XmlElement(name = "billing_first_name")
    protected String billingFirstName;
    @XmlElement(name = "billing_phonetic_first_name")
    protected String billingPhoneticFirstName;
    @XmlElement(name = "billing_last_name")
    protected String billingLastName;
    @XmlElement(name = "billing_phonetic_last_name")
    protected String billingPhoneticLastName;
    @XmlElement(name = "billing_preferred_name")
    protected String billingPreferredName;
    @XmlElement(name = "billing_company_name")
    protected String billingCompanyName;
    @XmlElement(name = "billing_address1")
    protected String billingAddress1;
    @XmlElement(name = "billing_address2")
    protected String billingAddress2;
    @XmlElement(name = "billing_address3")
    protected String billingAddress3;
    @XmlElement(name = "billing_county")
    protected String billingCounty;
    @XmlElement(name = "billing_city")
    protected String billingCity;
    @XmlElement(name = "billing_state")
    protected String billingState;
    @XmlElement(name = "billing_postal_code")
    protected String billingPostalCode;
    @XmlElement(name = "billing_country")
    protected String billingCountry;
    @XmlElement(name = "billing_phone")
    protected String billingPhone;

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the deliveryFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryFirstName() {
        return deliveryFirstName;
    }

    /**
     * Sets the value of the deliveryFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryFirstName(String value) {
        this.deliveryFirstName = value;
    }

    /**
     * Gets the value of the deliveryPhoneticFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryPhoneticFirstName() {
        return deliveryPhoneticFirstName;
    }

    /**
     * Sets the value of the deliveryPhoneticFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryPhoneticFirstName(String value) {
        this.deliveryPhoneticFirstName = value;
    }

    /**
     * Gets the value of the deliveryLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryLastName() {
        return deliveryLastName;
    }

    /**
     * Sets the value of the deliveryLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryLastName(String value) {
        this.deliveryLastName = value;
    }

    /**
     * Gets the value of the deliveryPhoneticLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryPhoneticLastName() {
        return deliveryPhoneticLastName;
    }

    /**
     * Sets the value of the deliveryPhoneticLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryPhoneticLastName(String value) {
        this.deliveryPhoneticLastName = value;
    }

    /**
     * Gets the value of the deliveryPreferredName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryPreferredName() {
        return deliveryPreferredName;
    }

    /**
     * Sets the value of the deliveryPreferredName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryPreferredName(String value) {
        this.deliveryPreferredName = value;
    }

    /**
     * Gets the value of the deliveryCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCompanyName() {
        return deliveryCompanyName;
    }

    /**
     * Sets the value of the deliveryCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCompanyName(String value) {
        this.deliveryCompanyName = value;
    }

    /**
     * Gets the value of the deliveryAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryAddress1() {
        return deliveryAddress1;
    }

    /**
     * Sets the value of the deliveryAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryAddress1(String value) {
        this.deliveryAddress1 = value;
    }

    /**
     * Gets the value of the deliveryAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryAddress2() {
        return deliveryAddress2;
    }

    /**
     * Sets the value of the deliveryAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryAddress2(String value) {
        this.deliveryAddress2 = value;
    }

    /**
     * Gets the value of the deliveryAddress3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryAddress3() {
        return deliveryAddress3;
    }

    /**
     * Sets the value of the deliveryAddress3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryAddress3(String value) {
        this.deliveryAddress3 = value;
    }

    /**
     * Gets the value of the deliveryCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCounty() {
        return deliveryCounty;
    }

    /**
     * Sets the value of the deliveryCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCounty(String value) {
        this.deliveryCounty = value;
    }

    /**
     * Gets the value of the deliveryCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCity() {
        return deliveryCity;
    }

    /**
     * Sets the value of the deliveryCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCity(String value) {
        this.deliveryCity = value;
    }

    /**
     * Gets the value of the deliveryState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryState() {
        return deliveryState;
    }

    /**
     * Sets the value of the deliveryState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryState(String value) {
        this.deliveryState = value;
    }

    /**
     * Gets the value of the deliveryPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    /**
     * Sets the value of the deliveryPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryPostalCode(String value) {
        this.deliveryPostalCode = value;
    }

    /**
     * Gets the value of the deliveryCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCountry() {
        return deliveryCountry;
    }

    /**
     * Sets the value of the deliveryCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCountry(String value) {
        this.deliveryCountry = value;
    }

    /**
     * Gets the value of the deliveryPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    /**
     * Sets the value of the deliveryPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryPhone(String value) {
        this.deliveryPhone = value;
    }

    /**
     * Gets the value of the billingFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingFirstName() {
        return billingFirstName;
    }

    /**
     * Sets the value of the billingFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingFirstName(String value) {
        this.billingFirstName = value;
    }

    /**
     * Gets the value of the billingPhoneticFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPhoneticFirstName() {
        return billingPhoneticFirstName;
    }

    /**
     * Sets the value of the billingPhoneticFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPhoneticFirstName(String value) {
        this.billingPhoneticFirstName = value;
    }

    /**
     * Gets the value of the billingLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingLastName() {
        return billingLastName;
    }

    /**
     * Sets the value of the billingLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingLastName(String value) {
        this.billingLastName = value;
    }

    /**
     * Gets the value of the billingPhoneticLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPhoneticLastName() {
        return billingPhoneticLastName;
    }

    /**
     * Sets the value of the billingPhoneticLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPhoneticLastName(String value) {
        this.billingPhoneticLastName = value;
    }

    /**
     * Gets the value of the billingPreferredName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPreferredName() {
        return billingPreferredName;
    }

    /**
     * Sets the value of the billingPreferredName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPreferredName(String value) {
        this.billingPreferredName = value;
    }

    /**
     * Gets the value of the billingCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCompanyName() {
        return billingCompanyName;
    }

    /**
     * Sets the value of the billingCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCompanyName(String value) {
        this.billingCompanyName = value;
    }

    /**
     * Gets the value of the billingAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAddress1() {
        return billingAddress1;
    }

    /**
     * Sets the value of the billingAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAddress1(String value) {
        this.billingAddress1 = value;
    }

    /**
     * Gets the value of the billingAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAddress2() {
        return billingAddress2;
    }

    /**
     * Sets the value of the billingAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAddress2(String value) {
        this.billingAddress2 = value;
    }

    /**
     * Gets the value of the billingAddress3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingAddress3() {
        return billingAddress3;
    }

    /**
     * Sets the value of the billingAddress3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingAddress3(String value) {
        this.billingAddress3 = value;
    }

    /**
     * Gets the value of the billingCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCounty() {
        return billingCounty;
    }

    /**
     * Sets the value of the billingCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCounty(String value) {
        this.billingCounty = value;
    }

    /**
     * Gets the value of the billingCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCity() {
        return billingCity;
    }

    /**
     * Sets the value of the billingCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCity(String value) {
        this.billingCity = value;
    }

    /**
     * Gets the value of the billingState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingState() {
        return billingState;
    }

    /**
     * Sets the value of the billingState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingState(String value) {
        this.billingState = value;
    }

    /**
     * Gets the value of the billingPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPostalCode() {
        return billingPostalCode;
    }

    /**
     * Sets the value of the billingPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPostalCode(String value) {
        this.billingPostalCode = value;
    }

    /**
     * Gets the value of the billingCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingCountry() {
        return billingCountry;
    }

    /**
     * Sets the value of the billingCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingCountry(String value) {
        this.billingCountry = value;
    }

    /**
     * Gets the value of the billingPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillingPhone() {
        return billingPhone;
    }

    /**
     * Sets the value of the billingPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillingPhone(String value) {
        this.billingPhone = value;
    }

}
