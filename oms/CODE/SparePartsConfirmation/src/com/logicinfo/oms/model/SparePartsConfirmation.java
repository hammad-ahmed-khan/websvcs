
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SparePartsConfirmation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SparePartsConfirmation">
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
 *         &lt;element name="ServiceConfirmationId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ServiceConfirmationDetail" type="{http://com.logicinfo.oms/services/}ServiceConfirmationDetailType" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SparePartsConfirmation", propOrder = {
    "serviceRequestId",
    "serviceConfirmationId",
    "serviceConfirmationDetail"
})
public class SparePartsConfirmation {

    @XmlElement(name = "ServiceRequestId", required = true)
    protected String serviceRequestId;
    @XmlElement(name = "ServiceConfirmationId", required = true)
    protected String serviceConfirmationId;
    @XmlElement(name = "ServiceConfirmationDetail", required = true)
    protected List<ServiceConfirmationDetailType> serviceConfirmationDetail;

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
     * Gets the value of the serviceConfirmationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceConfirmationId() {
        return serviceConfirmationId;
    }

    /**
     * Sets the value of the serviceConfirmationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceConfirmationId(String value) {
        this.serviceConfirmationId = value;
    }

    /**
     * Gets the value of the serviceConfirmationDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceConfirmationDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceConfirmationDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceConfirmationDetailType }
     * 
     * 
     */
    public List<ServiceConfirmationDetailType> getServiceConfirmationDetail() {
        if (serviceConfirmationDetail == null) {
            serviceConfirmationDetail = new ArrayList<ServiceConfirmationDetailType>();
        }
        return this.serviceConfirmationDetail;
    }

}
