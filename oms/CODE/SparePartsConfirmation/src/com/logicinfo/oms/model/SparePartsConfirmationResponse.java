
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SparePartsConfirmationResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SparePartsConfirmationResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceRequestId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SparePartsConfirmationResponseDetail" type="{http://com.logicinfo.oms/services/}ServiceConfDetailsResponseType" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SparePartsConfirmationResponse", propOrder = {
    "serviceRequestId",
    "status",
    "sparePartsConfirmationResponseDetail"
})
public class SparePartsConfirmationResponse {

    @XmlElement(name = "ServiceRequestId", required = true, nillable = true)
    protected String serviceRequestId;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "SparePartsConfirmationResponseDetail")
    protected List<ServiceConfDetailsResponseType> sparePartsConfirmationResponseDetail;

    /**
     * Gets the value of the serviceRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceRequestId() {
        return serviceRequestId;
    }

    /**
     * Sets the value of the serviceRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceRequestId(String value) {
        this.serviceRequestId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the sparePartsConfirmationResponseDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sparePartsConfirmationResponseDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSparePartsConfirmationResponseDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceConfDetailsResponseType }
     * 
     * 
     */
    public List<ServiceConfDetailsResponseType> getSparePartsConfirmationResponseDetail() {
        if (sparePartsConfirmationResponseDetail == null) {
            sparePartsConfirmationResponseDetail = new ArrayList<ServiceConfDetailsResponseType>();
        }
        return this.sparePartsConfirmationResponseDetail;
    }

}
