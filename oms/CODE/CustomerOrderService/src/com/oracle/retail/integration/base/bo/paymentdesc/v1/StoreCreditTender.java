
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

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
 *         &lt;element name="certificate_type" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}certificate_type" minOccurs="0"/>
 *         &lt;element name="first_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="last_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="state" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}state"/>
 *         &lt;element name="store_credit_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "certificateType",
    "firstName",
    "lastName",
    "personalIdType",
    "state",
    "storeCreditId"
})
@XmlRootElement(name = "StoreCreditTender")
public class StoreCreditTender {

    @XmlElement(name = "certificate_type")
    protected CertificateType certificateType;
    @XmlElement(name = "first_name")
    protected String firstName;
    @XmlElement(name = "last_name")
    protected String lastName;
    @XmlElement(name = "personal_id_type")
    protected String personalIdType;
    @XmlElement(required = true)
    protected State state;
    @XmlElement(name = "store_credit_id", required = true)
    protected String storeCreditId;

    /**
     * Gets the value of the certificateType property.
     * 
     * @return
     *     possible object is
     *     {@link CertificateType }
     *     
     */
    public CertificateType getCertificateType() {
        return certificateType;
    }

    /**
     * Sets the value of the certificateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificateType }
     *     
     */
    public void setCertificateType(CertificateType value) {
        this.certificateType = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the personalIdType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdType() {
        return personalIdType;
    }

    /**
     * Sets the value of the personalIdType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdType(String value) {
        this.personalIdType = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link State }
     *     
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link State }
     *     
     */
    public void setState(State value) {
        this.state = value;
    }

    /**
     * Gets the value of the storeCreditId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStoreCreditId() {
        return storeCreditId;
    }

    /**
     * Sets the value of the storeCreditId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreCreditId(String value) {
        this.storeCreditId = value;
    }

}
