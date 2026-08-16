
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for VASContractResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VASContractResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="x_requestor_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="x_application_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="x_response_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="message">
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
@XmlType(name = "VASContractResponse", propOrder = {
    "xRequestorId",
    "xApplicationId",
    "xResponseDatetime",
    "statusCode",
    "status",
    "message"
})
public class VASContractResponse {

    @XmlElement(name = "x_requestor_id", required = true)
    protected String xRequestorId;
    @XmlElement(name = "x_application_id", required = true)
    protected String xApplicationId;
    @XmlElement(name = "x_response_datetime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar xResponseDatetime;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(required = true)
    protected String message;
    @XmlElement(name = "status_code", required = true)
    protected BigDecimal statusCode;

    /**
     * Gets the value of the xRequestorId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getXRequestorId() {
        return xRequestorId;
    }

    /**
     * Sets the value of the xRequestorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXRequestorId(String value) {
        this.xRequestorId = value;
    }

    /**
     * Gets the value of the xApplicationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXApplicationId() {
        return xApplicationId;
    }

    /**
     * Sets the value of the xApplicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXApplicationId(String value) {
        this.xApplicationId = value;
    }

    /**
     * Gets the value of the xResponseDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getXResponseDatetime() {
        return xResponseDatetime;
    }

    /**
     * Sets the value of the xResponseDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setXResponseDatetime(XMLGregorianCalendar value) {
        this.xResponseDatetime = value;
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
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the statusCode property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setStatusCode(BigDecimal value) {
        this.statusCode = value;
    }

}
