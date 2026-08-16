
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for customerOrderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrderResponse">
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
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_sub_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="response_message" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "customerOrderResponse", propOrder = {
    "entityId",
    "applicationId",
    "customerOrderNo",
    "customerSubOrderNo",
    "requestDatetimestamp",
    "responseDatetimestamp",
    "omsCustomerOrderNo",
    "messageStatus",
    "messageCode",
    "messageDesc",
    "responseMessage",
    "customerOrderResponseItems"
})
@XmlRootElement
public class CustomerOrderResponse {

    @XmlElement(name = "entity_id", required = true, nillable = true)
    protected String entityId;
    @XmlElement(name = "application_id", required = true, nillable = true)
    protected String applicationId;
    @XmlElement(name = "request_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDatetimestamp;
    @XmlElement(name = "response_datetimestamp", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar responseDatetimestamp;
    @XmlElement(name = "customer_sub_order_no", required = true, nillable = true)
    protected String customerSubOrderNo;
    @XmlElement(name = "response_message", required = true, nillable = true)
    protected String responseMessage;
    @XmlElement(name = "customer_order_response_items", nillable = true)
    protected List<CustomerOrderResponseItems> customerOrderResponseItems;
    @XmlElement(name = "oms_customer_order_no", required = true, type = Long.class, nillable = true)
    protected Long omsCustomerOrderNo;
    @XmlElement(name = "customer_order_no", required = true, nillable = true)
    protected String customerOrderNo;
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
     * Gets the value of the customerSubOrderNo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public String getCustomerSubOrderNo() {
        return customerSubOrderNo;
    }

    /**
     * Sets the value of the customerSubOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerSubOrderNo(String value) {
        this.customerSubOrderNo = value;
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
     * getCustomerOrderResponseItems().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerOrderResponseItems}
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
     * Gets the value of the omsCustomerOrderNo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getOmsCustomerOrderNo() {
        return omsCustomerOrderNo;
    }

    /**
     * Sets the value of the omsCustomerOrderNo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setOmsCustomerOrderNo(Long value) {
        this.omsCustomerOrderNo = value;
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
