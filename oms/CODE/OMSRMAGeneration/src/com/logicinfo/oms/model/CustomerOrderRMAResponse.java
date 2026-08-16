
package com.logicinfo.oms.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CustomerOrderRMAResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerOrderRMAResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entity_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="application_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comments" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="request_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="response_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="rma_request_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMA_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="response_message">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
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
@XmlType(name = "CustomerOrderRMAResponse", propOrder = {
    "entityId",
    "applicationId",
    "customerOrderNo",
    "comments",
    "messageStatus",
    "messageDesc",
    "requestDatetimestamp",
    "responseDatetimestamp",
    "rmaRequestId",
    "rmaNo",
    "responseMessage"
})
public class CustomerOrderRMAResponse {

    @XmlElement(name = "entity_id", required = true, nillable = true)
    protected String entityId;
    @XmlElement(name = "application_id", required = true, nillable = true)
    protected String applicationId;
    protected String comments;
    @XmlElement(name = "request_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDatetimestamp;
    @XmlElement(name = "response_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar responseDatetimestamp;
    @XmlElement(name = "rma_request_id", required = true, nillable = true)
    protected String rmaRequestId;
    @XmlElement(name = "RMA_no", required = true, type = Long.class, nillable = true)
    protected Long rmaNo;
    @XmlElement(name = "response_message", required = true, nillable = true)
    protected String responseMessage;
    @XmlElement(name = "customer_order_no", required = true, nillable = true)
    protected String customerOrderNo;
    @XmlElement(name = "message_desc", required = true, nillable = true)
    protected String messageDesc;
    @XmlElement(name = "message_status", required = true, nillable = true)
    protected String messageStatus;

    /**
     * Gets the value of the entityId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * Sets the value of the entityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityId(String value) {
        this.entityId = value;
    }

    /**
     * Gets the value of the applicationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the value of the applicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationId(String value) {
        this.applicationId = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the requestDatetimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestDatetimestamp() {
        return requestDatetimestamp;
    }

    /**
     * Sets the value of the requestDatetimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestDatetimestamp(XMLGregorianCalendar value) {
        this.requestDatetimestamp = value;
    }

    /**
     * Gets the value of the responseDatetimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getResponseDatetimestamp() {
        return responseDatetimestamp;
    }

    /**
     * Sets the value of the responseDatetimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setResponseDatetimestamp(XMLGregorianCalendar value) {
        this.responseDatetimestamp = value;
    }

    /**
     * Gets the value of the rmaRequestId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmaRequestId() {
        return rmaRequestId;
    }

    /**
     * Sets the value of the rmaRequestId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmaRequestId(String value) {
        this.rmaRequestId = value;
    }

    /**
     * Gets the value of the rmaNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRMANo() {
        return rmaNo;
    }

    /**
     * Sets the value of the rmaNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRMANo(Long value) {
        this.rmaNo = value;
    }

    /**
     * Gets the value of the responseMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

    /**
     * Gets the value of the customerOrderNo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * Sets the value of the customerOrderNo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
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
     * Gets the value of the messageStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMessageStatus() {
        return messageStatus;
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
     * Sets the value of the messageStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMessageStatus(String value) {
        this.messageStatus = value;
    }

}
