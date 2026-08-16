
package com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.locoffulfilordcustdesc.v1.LocOfFulfilOrdCustDesc;


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
 *         &lt;element name="customer_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_phonetic_first" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_phonetic_last" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_preferred_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_company_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_add1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_add2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_add3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_country_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_post" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_jurisdiction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deliver_phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_phonetic_first" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_phonetic_last" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_preferred_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_company_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_add1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_add2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_add3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_country_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_post" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_jurisdiction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bill_phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/LocOfFulfilOrdCustDesc/v1}LocOfFulfilOrdCustDesc" minOccurs="0"/>
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
    "customerNo",
    "deliverFirstName",
    "deliverPhoneticFirst",
    "deliverLastName",
    "deliverPhoneticLast",
    "deliverPreferredName",
    "deliverCompanyName",
    "deliverAdd1",
    "deliverAdd2",
    "deliverAdd3",
    "deliverCounty",
    "deliverCity",
    "deliverState",
    "deliverCountryId",
    "deliverPost",
    "deliverJurisdiction",
    "deliverPhone",
    "billFirstName",
    "billPhoneticFirst",
    "billLastName",
    "billPhoneticLast",
    "billPreferredName",
    "billCompanyName",
    "billAdd1",
    "billAdd2",
    "billAdd3",
    "billCounty",
    "billCity",
    "billState",
    "billCountryId",
    "billPost",
    "billJurisdiction",
    "billPhone",
    "locOfFulfilOrdCustDesc"
})
@XmlRootElement(name = "FulfilOrdCustDesc")
public class FulfilOrdCustDesc {

    @XmlElement(name = "customer_no")
    protected String customerNo;
    @XmlElement(name = "deliver_first_name")
    protected String deliverFirstName;
    @XmlElement(name = "deliver_phonetic_first")
    protected String deliverPhoneticFirst;
    @XmlElement(name = "deliver_last_name")
    protected String deliverLastName;
    @XmlElement(name = "deliver_phonetic_last")
    protected String deliverPhoneticLast;
    @XmlElement(name = "deliver_preferred_name")
    protected String deliverPreferredName;
    @XmlElement(name = "deliver_company_name")
    protected String deliverCompanyName;
    @XmlElement(name = "deliver_add1")
    protected String deliverAdd1;
    @XmlElement(name = "deliver_add2")
    protected String deliverAdd2;
    @XmlElement(name = "deliver_add3")
    protected String deliverAdd3;
    @XmlElement(name = "deliver_county")
    protected String deliverCounty;
    @XmlElement(name = "deliver_city")
    protected String deliverCity;
    @XmlElement(name = "deliver_state")
    protected String deliverState;
    @XmlElement(name = "deliver_country_id")
    protected String deliverCountryId;
    @XmlElement(name = "deliver_post")
    protected String deliverPost;
    @XmlElement(name = "deliver_jurisdiction")
    protected String deliverJurisdiction;
    @XmlElement(name = "deliver_phone")
    protected String deliverPhone;
    @XmlElement(name = "bill_first_name")
    protected String billFirstName;
    @XmlElement(name = "bill_phonetic_first")
    protected String billPhoneticFirst;
    @XmlElement(name = "bill_last_name")
    protected String billLastName;
    @XmlElement(name = "bill_phonetic_last")
    protected String billPhoneticLast;
    @XmlElement(name = "bill_preferred_name")
    protected String billPreferredName;
    @XmlElement(name = "bill_company_name")
    protected String billCompanyName;
    @XmlElement(name = "bill_add1")
    protected String billAdd1;
    @XmlElement(name = "bill_add2")
    protected String billAdd2;
    @XmlElement(name = "bill_add3")
    protected String billAdd3;
    @XmlElement(name = "bill_county")
    protected String billCounty;
    @XmlElement(name = "bill_city")
    protected String billCity;
    @XmlElement(name = "bill_state")
    protected String billState;
    @XmlElement(name = "bill_country_id")
    protected String billCountryId;
    @XmlElement(name = "bill_post")
    protected String billPost;
    @XmlElement(name = "bill_jurisdiction")
    protected String billJurisdiction;
    @XmlElement(name = "bill_phone")
    protected String billPhone;
    @XmlElement(name = "LocOfFulfilOrdCustDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/LocOfFulfilOrdCustDesc/v1")
    protected LocOfFulfilOrdCustDesc locOfFulfilOrdCustDesc;

    /**
     * Gets the value of the customerNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * Sets the value of the customerNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNo(String value) {
        this.customerNo = value;
    }

    /**
     * Gets the value of the deliverFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverFirstName() {
        return deliverFirstName;
    }

    /**
     * Sets the value of the deliverFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverFirstName(String value) {
        this.deliverFirstName = value;
    }

    /**
     * Gets the value of the deliverPhoneticFirst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPhoneticFirst() {
        return deliverPhoneticFirst;
    }

    /**
     * Sets the value of the deliverPhoneticFirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPhoneticFirst(String value) {
        this.deliverPhoneticFirst = value;
    }

    /**
     * Gets the value of the deliverLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverLastName() {
        return deliverLastName;
    }

    /**
     * Sets the value of the deliverLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverLastName(String value) {
        this.deliverLastName = value;
    }

    /**
     * Gets the value of the deliverPhoneticLast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPhoneticLast() {
        return deliverPhoneticLast;
    }

    /**
     * Sets the value of the deliverPhoneticLast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPhoneticLast(String value) {
        this.deliverPhoneticLast = value;
    }

    /**
     * Gets the value of the deliverPreferredName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPreferredName() {
        return deliverPreferredName;
    }

    /**
     * Sets the value of the deliverPreferredName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPreferredName(String value) {
        this.deliverPreferredName = value;
    }

    /**
     * Gets the value of the deliverCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCompanyName() {
        return deliverCompanyName;
    }

    /**
     * Sets the value of the deliverCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCompanyName(String value) {
        this.deliverCompanyName = value;
    }

    /**
     * Gets the value of the deliverAdd1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd1() {
        return deliverAdd1;
    }

    /**
     * Sets the value of the deliverAdd1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd1(String value) {
        this.deliverAdd1 = value;
    }

    /**
     * Gets the value of the deliverAdd2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd2() {
        return deliverAdd2;
    }

    /**
     * Sets the value of the deliverAdd2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd2(String value) {
        this.deliverAdd2 = value;
    }

    /**
     * Gets the value of the deliverAdd3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd3() {
        return deliverAdd3;
    }

    /**
     * Sets the value of the deliverAdd3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd3(String value) {
        this.deliverAdd3 = value;
    }

    /**
     * Gets the value of the deliverCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCounty() {
        return deliverCounty;
    }

    /**
     * Sets the value of the deliverCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCounty(String value) {
        this.deliverCounty = value;
    }

    /**
     * Gets the value of the deliverCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCity() {
        return deliverCity;
    }

    /**
     * Sets the value of the deliverCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCity(String value) {
        this.deliverCity = value;
    }

    /**
     * Gets the value of the deliverState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverState() {
        return deliverState;
    }

    /**
     * Sets the value of the deliverState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverState(String value) {
        this.deliverState = value;
    }

    /**
     * Gets the value of the deliverCountryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCountryId() {
        return deliverCountryId;
    }

    /**
     * Sets the value of the deliverCountryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCountryId(String value) {
        this.deliverCountryId = value;
    }

    /**
     * Gets the value of the deliverPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPost() {
        return deliverPost;
    }

    /**
     * Sets the value of the deliverPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPost(String value) {
        this.deliverPost = value;
    }

    /**
     * Gets the value of the deliverJurisdiction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverJurisdiction() {
        return deliverJurisdiction;
    }

    /**
     * Sets the value of the deliverJurisdiction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverJurisdiction(String value) {
        this.deliverJurisdiction = value;
    }

    /**
     * Gets the value of the deliverPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPhone() {
        return deliverPhone;
    }

    /**
     * Sets the value of the deliverPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPhone(String value) {
        this.deliverPhone = value;
    }

    /**
     * Gets the value of the billFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillFirstName() {
        return billFirstName;
    }

    /**
     * Sets the value of the billFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillFirstName(String value) {
        this.billFirstName = value;
    }

    /**
     * Gets the value of the billPhoneticFirst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPhoneticFirst() {
        return billPhoneticFirst;
    }

    /**
     * Sets the value of the billPhoneticFirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPhoneticFirst(String value) {
        this.billPhoneticFirst = value;
    }

    /**
     * Gets the value of the billLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillLastName() {
        return billLastName;
    }

    /**
     * Sets the value of the billLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillLastName(String value) {
        this.billLastName = value;
    }

    /**
     * Gets the value of the billPhoneticLast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPhoneticLast() {
        return billPhoneticLast;
    }

    /**
     * Sets the value of the billPhoneticLast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPhoneticLast(String value) {
        this.billPhoneticLast = value;
    }

    /**
     * Gets the value of the billPreferredName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPreferredName() {
        return billPreferredName;
    }

    /**
     * Sets the value of the billPreferredName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPreferredName(String value) {
        this.billPreferredName = value;
    }

    /**
     * Gets the value of the billCompanyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCompanyName() {
        return billCompanyName;
    }

    /**
     * Sets the value of the billCompanyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCompanyName(String value) {
        this.billCompanyName = value;
    }

    /**
     * Gets the value of the billAdd1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd1() {
        return billAdd1;
    }

    /**
     * Sets the value of the billAdd1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd1(String value) {
        this.billAdd1 = value;
    }

    /**
     * Gets the value of the billAdd2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd2() {
        return billAdd2;
    }

    /**
     * Sets the value of the billAdd2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd2(String value) {
        this.billAdd2 = value;
    }

    /**
     * Gets the value of the billAdd3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd3() {
        return billAdd3;
    }

    /**
     * Sets the value of the billAdd3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd3(String value) {
        this.billAdd3 = value;
    }

    /**
     * Gets the value of the billCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCounty() {
        return billCounty;
    }

    /**
     * Sets the value of the billCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCounty(String value) {
        this.billCounty = value;
    }

    /**
     * Gets the value of the billCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCity() {
        return billCity;
    }

    /**
     * Sets the value of the billCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCity(String value) {
        this.billCity = value;
    }

    /**
     * Gets the value of the billState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillState() {
        return billState;
    }

    /**
     * Sets the value of the billState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillState(String value) {
        this.billState = value;
    }

    /**
     * Gets the value of the billCountryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCountryId() {
        return billCountryId;
    }

    /**
     * Sets the value of the billCountryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCountryId(String value) {
        this.billCountryId = value;
    }

    /**
     * Gets the value of the billPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPost() {
        return billPost;
    }

    /**
     * Sets the value of the billPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPost(String value) {
        this.billPost = value;
    }

    /**
     * Gets the value of the billJurisdiction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillJurisdiction() {
        return billJurisdiction;
    }

    /**
     * Sets the value of the billJurisdiction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillJurisdiction(String value) {
        this.billJurisdiction = value;
    }

    /**
     * Gets the value of the billPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPhone() {
        return billPhone;
    }

    /**
     * Sets the value of the billPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPhone(String value) {
        this.billPhone = value;
    }

    /**
     * Gets the value of the locOfFulfilOrdCustDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.locoffulfilordcustdesc.v1.LocOfFulfilOrdCustDesc}
     *
     */
    public LocOfFulfilOrdCustDesc getLocOfFulfilOrdCustDesc() {
        return locOfFulfilOrdCustDesc;
    }

    /**
     * Sets the value of the locOfFulfilOrdCustDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.locoffulfilordcustdesc.v1.LocOfFulfilOrdCustDesc}
     *
     */
    public void setLocOfFulfilOrdCustDesc(LocOfFulfilOrdCustDesc value) {
        this.locOfFulfilOrdCustDesc = value;
    }

}
