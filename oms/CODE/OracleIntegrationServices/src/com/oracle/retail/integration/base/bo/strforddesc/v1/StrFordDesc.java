
package com.oracle.retail.integration.base.bo.strforddesc.v1;

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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="int_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ext_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="order_type" type="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}str_ford_type"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}str_ford_status"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="release_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="delivery_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="delivery_type" type="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}str_ford_delivery_type"/>
 *         &lt;element name="delivery_carrier_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="delivery_service_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="allow_partial_delivery" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}StrFordCust" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}StrFordItm" maxOccurs="unbounded" minOccurs="0"/>
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
    "intFulfillmentOrderId",
    "extFulfillmentOrderId",
    "customerOrderId",
    "orderType",
    "status",
    "createDate",
    "releaseDate",
    "deliveryDate",
    "deliveryType",
    "deliveryCarrierCode",
    "deliveryServiceCode",
    "allowPartialDelivery",
    "comments",
    "strFordCust",
    "strFordItm"
})
@XmlRootElement(name = "StrFordDesc")
public class StrFordDesc {

    @XmlElement(name = "int_fulfillment_order_id")
    protected long intFulfillmentOrderId;
    @XmlElement(name = "ext_fulfillment_order_id", required = true)
    protected String extFulfillmentOrderId;
    @XmlElement(name = "customer_order_id", required = true)
    protected String customerOrderId;
    @XmlElement(name = "order_type", required = true)
    protected StrFordType orderType;
    @XmlElement(required = true)
    protected StrFordStatus status;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "release_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar releaseDate;
    @XmlElement(name = "delivery_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deliveryDate;
    @XmlElement(name = "delivery_type", required = true)
    protected StrFordDeliveryType deliveryType;
    @XmlElement(name = "delivery_carrier_code")
    protected String deliveryCarrierCode;
    @XmlElement(name = "delivery_service_code")
    protected String deliveryServiceCode;
    @XmlElement(name = "allow_partial_delivery")
    protected boolean allowPartialDelivery;
    protected String comments;
    @XmlElement(name = "StrFordCust")
    protected StrFordCust strFordCust;
    @XmlElement(name = "StrFordItm")
    protected List<StrFordItm> strFordItm;

    /**
     * Gets the value of the intFulfillmentOrderId property.
     * 
     */
    public long getIntFulfillmentOrderId() {
        return intFulfillmentOrderId;
    }

    /**
     * Sets the value of the intFulfillmentOrderId property.
     * 
     */
    public void setIntFulfillmentOrderId(long value) {
        this.intFulfillmentOrderId = value;
    }

    /**
     * Gets the value of the extFulfillmentOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtFulfillmentOrderId() {
        return extFulfillmentOrderId;
    }

    /**
     * Sets the value of the extFulfillmentOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtFulfillmentOrderId(String value) {
        this.extFulfillmentOrderId = value;
    }

    /**
     * Gets the value of the customerOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderId() {
        return customerOrderId;
    }

    /**
     * Sets the value of the customerOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderId(String value) {
        this.customerOrderId = value;
    }

    /**
     * Gets the value of the orderType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordType}
     *
     */
    public StrFordType getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordType}
     *
     */
    public void setOrderType(StrFordType value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordStatus}
     *
     */
    public StrFordStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordStatus}
     *
     */
    public void setStatus(StrFordStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the releaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the value of the releaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReleaseDate(XMLGregorianCalendar value) {
        this.releaseDate = value;
    }

    /**
     * Gets the value of the deliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the value of the deliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeliveryDate(XMLGregorianCalendar value) {
        this.deliveryDate = value;
    }

    /**
     * Gets the value of the deliveryType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDeliveryType}
     *
     */
    public StrFordDeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDeliveryType}
     *
     */
    public void setDeliveryType(StrFordDeliveryType value) {
        this.deliveryType = value;
    }

    /**
     * Gets the value of the deliveryCarrierCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryCarrierCode() {
        return deliveryCarrierCode;
    }

    /**
     * Sets the value of the deliveryCarrierCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryCarrierCode(String value) {
        this.deliveryCarrierCode = value;
    }

    /**
     * Gets the value of the deliveryServiceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryServiceCode() {
        return deliveryServiceCode;
    }

    /**
     * Sets the value of the deliveryServiceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryServiceCode(String value) {
        this.deliveryServiceCode = value;
    }

    /**
     * Gets the value of the allowPartialDelivery property.
     * 
     */
    public boolean isAllowPartialDelivery() {
        return allowPartialDelivery;
    }

    /**
     * Sets the value of the allowPartialDelivery property.
     * 
     */
    public void setAllowPartialDelivery(boolean value) {
        this.allowPartialDelivery = value;
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
     * Gets the value of the strFordCust property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordCust}
     *
     */
    public StrFordCust getStrFordCust() {
        return strFordCust;
    }

    /**
     * Sets the value of the strFordCust property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordCust}
     *
     */
    public void setStrFordCust(StrFordCust value) {
        this.strFordCust = value;
    }

    /**
     * Gets the value of the strFordItm property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strFordItm property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getStrFordItm().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordItm}
     *
     *
     */
    public List<StrFordItm> getStrFordItm() {
        if (strFordItm == null) {
            strFordItm = new ArrayList<StrFordItm>();
        }
        return this.strFordItm;
    }

}
