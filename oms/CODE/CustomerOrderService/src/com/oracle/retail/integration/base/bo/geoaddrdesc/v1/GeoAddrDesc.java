
package com.oracle.retail.integration.base.bo.geoaddrdesc.v1;

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
 *         &lt;element name="address_alias" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address_1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="address_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address_3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address_4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address_5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postal_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jurisdiction_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "addressAlias",
    "address1",
    "address2",
    "address3",
    "address4",
    "address5",
    "city",
    "county",
    "stateCode",
    "stateName",
    "countryCode",
    "countryName",
    "postalCode",
    "jurisdictionCode"
})
@XmlRootElement(name = "GeoAddrDesc")
public class GeoAddrDesc {

    @XmlElement(name = "address_alias")
    protected String addressAlias;
    @XmlElement(name = "address_1", required = true)
    protected String address1;
    @XmlElement(name = "address_2")
    protected String address2;
    @XmlElement(name = "address_3")
    protected String address3;
    @XmlElement(name = "address_4")
    protected String address4;
    @XmlElement(name = "address_5")
    protected String address5;
    protected String city;
    protected String county;
    @XmlElement(name = "state_code")
    protected String stateCode;
    @XmlElement(name = "state_name")
    protected String stateName;
    @XmlElement(name = "country_code")
    protected String countryCode;
    @XmlElement(name = "country_name")
    protected String countryName;
    @XmlElement(name = "postal_code")
    protected String postalCode;
    @XmlElement(name = "jurisdiction_code")
    protected String jurisdictionCode;

    /**
     * Gets the value of the addressAlias property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressAlias() {
        return addressAlias;
    }

    /**
     * Sets the value of the addressAlias property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressAlias(String value) {
        this.addressAlias = value;
    }

    /**
     * Gets the value of the address1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the value of the address1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress1(String value) {
        this.address1 = value;
    }

    /**
     * Gets the value of the address2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets the value of the address2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress2(String value) {
        this.address2 = value;
    }

    /**
     * Gets the value of the address3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress3() {
        return address3;
    }

    /**
     * Sets the value of the address3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress3(String value) {
        this.address3 = value;
    }

    /**
     * Gets the value of the address4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress4() {
        return address4;
    }

    /**
     * Sets the value of the address4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress4(String value) {
        this.address4 = value;
    }

    /**
     * Gets the value of the address5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress5() {
        return address5;
    }

    /**
     * Sets the value of the address5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress5(String value) {
        this.address5 = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounty() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounty(String value) {
        this.county = value;
    }

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCode(String value) {
        this.stateCode = value;
    }

    /**
     * Gets the value of the stateName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Sets the value of the stateName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateName(String value) {
        this.stateName = value;
    }

    /**
     * Gets the value of the countryCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the value of the countryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryName(String value) {
        this.countryName = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the jurisdictionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJurisdictionCode() {
        return jurisdictionCode;
    }

    /**
     * Sets the value of the jurisdictionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJurisdictionCode(String value) {
        this.jurisdictionCode = value;
    }

}
