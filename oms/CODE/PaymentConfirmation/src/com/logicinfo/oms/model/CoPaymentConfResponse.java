
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for coPaymentConfResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="coPaymentConfResponse">
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
 *         &lt;element name="comments">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="request_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="response_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oms_customer_ord_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
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
 *         &lt;element name="customer_order_response_items" type="{http://com.logicinfo.oms/model/}customerOrderResponseItems" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coPaymentConfResponse", propOrder = {
    "entityId",
    "applicationId",
    "comments",
    "requestDatetimestamp",
    "responseDatetimestamp",
    "customerOrderNo",
    "omsCustomerOrdNo",
    "messageStatus",
    "messageCode",
    "messageDesc",
    "customerOrderResponseItems"
})
public class CoPaymentConfResponse {

    @XmlElement(name = "entity_id", required = true, nillable = true)
    protected String entityId;
    @XmlElement(name = "application_id", required = true, nillable = true)
    protected String applicationId;
    @XmlElement(required = true, nillable = true)
    protected String comments;
    @XmlElement(name = "request_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDatetimestamp;
    @XmlElement(name = "response_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar responseDatetimestamp;
    @XmlElement(name = "customer_order_no", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "oms_customer_ord_no", required = true, type = Long.class, nillable = true)
    
    protected Long omsCustomerOrdNo;
    @XmlElement(name = "customer_order_response_items", nillable = true)
    protected List<CustomerOrderResponseItems> customerOrderResponseItems;
    @XmlElement(name = "message_code", required = true, nillable = true)
    protected String messageCode;
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
     * Gets the value of the omsCustomerOrdNo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getOmsCustomerOrdNo() {
        return omsCustomerOrdNo;
    }

    /**
     * Sets the value of the omsCustomerOrdNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOmsCustomerOrdNo(Long value) {
        this.omsCustomerOrdNo = value;
    }


    /**
     * Gets the value of the customerOrderResponseItems property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrderResponseItems property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerOrderResponseItems().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerOrderResponseItems }
     *
     *
     */
    public List<CustomerOrderResponseItems> getCustomerOrderResponseItems() {
        if (customerOrderResponseItems == null) {
            customerOrderResponseItems = new ArrayList<CustomerOrderResponseItems>();
        }
        return this.customerOrderResponseItems;
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
