
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SparePartsCancelResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SparePartsCancelResponse">
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
 *         &lt;element name="Status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="message_code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="message_desc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
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
@XmlType(name = "SparePartsCancelResponse", propOrder = {
    "serviceRequestId",
    "sequenceId",
    "omsServiceId",
    "status",
    "cancelQty",
    "messageCode",
    "messageDesc"
})
public class SparePartsCancelResponse {

    @XmlElement(name = "ServiceRequestId", required = true)
    protected String serviceRequestId;
    @XmlElement(name = "SequenceId", required = true)
    protected String sequenceId;
    @XmlElement(name = "OMSServiceId", required = true)
    protected String omsServiceId;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "message_code", required = true, nillable = true)
    protected String messageCode;
    @XmlElement(name = "message_desc", required = true, nillable = true)
    protected String messageDesc;
    @XmlElement(name = "cancel_qty", required = true, nillable = true)
    protected BigDecimal cancelQty;

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
     * Gets the value of the messageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Sets the value of the messageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageCode(String value) {
        this.messageCode = value;
    }

    /**
     * Gets the value of the messageDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDesc() {
        return messageDesc;
    }

    /**
     * Sets the value of the messageDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDesc(String value) {
        this.messageDesc = value;
    }

    /**
     * Gets the value of the cancelQty property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getCancelQty() {
        return cancelQty;
    }

    /**
     * Sets the value of the cancelQty property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setCancelQty(BigDecimal value) {
        this.cancelQty = value;
    }

}
