
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for CustomerOrderRMA complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerOrderRMA">
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
 *               &lt;enumeration value="SIEBEL_CRM"/>
 *               &lt;enumeration value="ECOMMERCE"/>
 *               &lt;enumeration value="ORPOS"/>
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
 *         &lt;element name="request_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="rma_request_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="physical_wh">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="return_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="CustomerOrderRMAItem" type="{http://com.logicinfo.oms/model/}CustomerOrderRMAItem" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerOrderRMA", propOrder = {
    "entityId",
    "applicationId",
    "comments",
    "requestDatetimestamp",
    "customerOrderNo",
    "subCustomerOrderNo",
    "rmaRequestId",
    "physicalWh",
    "returnDate",
    "refundPreference",
    "refundAmount",
    "reasonCode",
    "reason",
    "customerOrderRMAItem"
})
public class CustomerOrderRMA {

    @XmlElement(name = "entity_id", required = true)
    protected String entityId;
    @XmlElement(name = "application_id", required = true)
    protected String applicationId;
    protected String comments;
    @XmlElement(name = "request_datetimestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDatetimestamp;
    @XmlElement(name = "customer_order_no", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "rma_request_id", required = true)
    protected String rmaRequestId;
    @XmlElement(name = "physical_wh")
    protected long physicalWh;
    @XmlElement(name = "return_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar returnDate;
    @XmlElement(name = "CustomerOrderRMAItem", required = true)
    protected List<CustomerOrderRMAItem> customerOrderRMAItem;
    @XmlElement(name = "sub_customer_order_no")
    protected String subCustomerOrderNo;
    @XmlElement(name = "refund_amount", required = true)
    protected BigDecimal refundAmount;
    @XmlElement(name = "refund_preference", required = true)
    protected String refundPreference;
    @XmlElement(name = "reason_code", required = false)
    protected Integer reasonCode = 0;
    @XmlElement(name = "reason", required = false)
    protected String reason = "";


    public Integer getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(Integer reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
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
     * Gets the value of the customerOrderNo property.
     *
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * Sets the value of the customerOrderNo property.
     * 
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
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
     * Gets the value of the physicalWh property.
     * 
     */
    public long getPhysicalWh() {
        return physicalWh;
    }

    /**
     * Sets the value of the physicalWh property.
     * 
     */
    public void setPhysicalWh(long value) {
        this.physicalWh = value;
    }

    /**
     * Gets the value of the returnDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReturnDate() {
        return returnDate;
    }

    /**
     * Sets the value of the returnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReturnDate(XMLGregorianCalendar value) {
        this.returnDate = value;
    }

    /**
     * Gets the value of the customerOrderRMAItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrderRMAItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerOrderRMAItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerOrderRMAItem }
     * 
     * 
     */
    public List<CustomerOrderRMAItem> getCustomerOrderRMAItem() {
        if (customerOrderRMAItem == null) {
            customerOrderRMAItem = new ArrayList<CustomerOrderRMAItem>();
        }
        return this.customerOrderRMAItem;
    }

    /**
     * Gets the value of the subCustomerOrderNo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSubCustomerOrderNo() {
        return subCustomerOrderNo;
    }

    /**
     * Sets the value of the subCustomerOrderNo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSubCustomerOrderNo(String value) {
        this.subCustomerOrderNo = value;
    }

    /**
     * Gets the value of the refundAmount property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * Gets the value of the refundPreference property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRefundPreference() {
        return refundPreference;
    }

    /**
     * Sets the value of the refundAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setRefundAmount(BigDecimal value) {
        this.refundAmount = value;
    }

    /**
     * Sets the value of the refundPreference property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRefundPreference(String value) {
        this.refundPreference = value;
    }

}
