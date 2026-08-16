
package com.oracle.retail.integration.base.bo.emaildesc.v1;

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
 *         &lt;element name="email_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="email_type" type="{http://www.oracle.com/retail/integration/base/bo/EmailDesc/v1}email_type" minOccurs="0"/>
 *         &lt;element name="primary_email_ind" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email_address" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "emailId",
    "emailType",
    "primaryEmailInd",
    "emailAddress"
})
@XmlRootElement(name = "EmailDesc")
public class EmailDesc {

    @XmlElement(name = "email_id")
    protected BigDecimal emailId;
    @XmlElement(name = "email_type")
    protected EmailType emailType;
    @XmlElement(name = "primary_email_ind", required = true)
    protected String primaryEmailInd;
    @XmlElement(name = "email_address", required = true)
    protected String emailAddress;

    /**
     * Gets the value of the emailId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEmailId() {
        return emailId;
    }

    /**
     * Sets the value of the emailId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEmailId(BigDecimal value) {
        this.emailId = value;
    }

    /**
     * Gets the value of the emailType property.
     * 
     * @return
     *     possible object is
     *     {@link EmailType }
     *     
     */
    public EmailType getEmailType() {
        return emailType;
    }

    /**
     * Sets the value of the emailType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmailType }
     *     
     */
    public void setEmailType(EmailType value) {
        this.emailType = value;
    }

    /**
     * Gets the value of the primaryEmailInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryEmailInd() {
        return primaryEmailInd;
    }

    /**
     * Sets the value of the primaryEmailInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryEmailInd(String value) {
        this.primaryEmailInd = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress(String value) {
        this.emailAddress = value;
    }

}
