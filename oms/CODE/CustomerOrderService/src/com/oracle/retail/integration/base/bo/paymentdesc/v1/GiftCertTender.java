
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
 *         &lt;element name="issue_location_type" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}issue_location_type" minOccurs="0"/>
 *         &lt;element name="issue_location_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="serial_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "issueLocationType",
    "issueLocationId",
    "serialNumber"
})
@XmlRootElement(name = "GiftCertTender")
public class GiftCertTender {

    @XmlElement(name = "certificate_type")
    protected CertificateType certificateType;
    @XmlElement(name = "issue_location_type")
    protected IssueLocationType issueLocationType;
    @XmlElement(name = "issue_location_id")
    protected Long issueLocationId;
    @XmlElement(name = "serial_number", required = true)
    protected String serialNumber;

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
     * Gets the value of the issueLocationType property.
     * 
     * @return
     *     possible object is
     *     {@link IssueLocationType }
     *     
     */
    public IssueLocationType getIssueLocationType() {
        return issueLocationType;
    }

    /**
     * Sets the value of the issueLocationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link IssueLocationType }
     *     
     */
    public void setIssueLocationType(IssueLocationType value) {
        this.issueLocationType = value;
    }

    /**
     * Gets the value of the issueLocationId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIssueLocationId() {
        return issueLocationId;
    }

    /**
     * Sets the value of the issueLocationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIssueLocationId(Long value) {
        this.issueLocationId = value;
    }

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerialNumber(String value) {
        this.serialNumber = value;
    }

}
