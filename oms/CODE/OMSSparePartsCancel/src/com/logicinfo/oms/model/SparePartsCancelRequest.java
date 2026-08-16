
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SparePartsCancelRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SparePartsCancelRequest">
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
 *         &lt;element name="SequenceId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OMSServiceId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CancellationId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Quantity">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;totalDigits value="12"/>
 *               &lt;fractionDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SparePartsCancelRequest", propOrder = {
    "serviceRequestId",
    "sequenceId",
    "omsServiceId",
    "cancellationId",
    "quantity"
})
public class SparePartsCancelRequest {

    @XmlElement(name = "ServiceRequestId", required = true)
    protected String serviceRequestId;
    @XmlElement(name = "SequenceId", required = true)
    protected String sequenceId;
    @XmlElement(name = "OMSServiceId", required = true)
    protected String omsServiceId;
    @XmlElement(name = "CancellationId", required = true)
    protected String cancellationId;
    @XmlElement(name = "Quantity", required = true)
    protected BigDecimal quantity;

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
     * Gets the value of the sequenceId property.
     *
     */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
     * Sets the value of the sequenceId property.
     * 
     */
    public void setSequenceId(String value) {
        this.sequenceId = value;
    }

    /**
     * Gets the value of the omsServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOMSServiceId() {
        return omsServiceId;
    }

    /**
     * Sets the value of the omsServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOMSServiceId(String value) {
        this.omsServiceId = value;
    }

    /**
     * Gets the value of the cancellationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancellationId() {
        return cancellationId;
    }

    /**
     * Sets the value of the cancellationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancellationId(String value) {
        this.cancellationId = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

}
