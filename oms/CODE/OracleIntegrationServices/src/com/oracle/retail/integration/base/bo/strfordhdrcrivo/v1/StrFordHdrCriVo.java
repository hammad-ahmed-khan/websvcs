
package com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1;

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
 *         &lt;element name="release_from_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="release_to_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrCriVo/v1}StrFordCriStatus"/>
 *         &lt;element name="int_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ext_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bin_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="order_type" type="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrCriVo/v1}StrFordCriType"/>
 *         &lt;element name="customer_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "releaseFromDate",
    "releaseToDate",
    "status",
    "intFulfillmentOrderId",
    "extFulfillmentOrderId",
    "customerOrderId",
    "binId",
    "orderType",
    "customerName",
    "itemId",
    "storeId"
})
@XmlRootElement(name = "StrFordHdrCriVo")
public class StrFordHdrCriVo {

    @XmlElement(name = "release_from_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar releaseFromDate;
    @XmlElement(name = "release_to_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar releaseToDate;
    @XmlElement(required = true)
    protected StrFordCriStatus status;
    @XmlElement(name = "int_fulfillment_order_id")
    protected Long intFulfillmentOrderId;
    @XmlElement(name = "ext_fulfillment_order_id")
    protected String extFulfillmentOrderId;
    @XmlElement(name = "customer_order_id")
    protected String customerOrderId;
    @XmlElement(name = "bin_id")
    protected String binId;
    @XmlElement(name = "order_type", required = true)
    protected StrFordCriType orderType;
    @XmlElement(name = "customer_name")
    protected String customerName;
    @XmlElement(name = "item_id")
    protected String itemId;
    @XmlElement(name = "store_id")
    protected long storeId;

    /**
     * Gets the value of the releaseFromDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReleaseFromDate() {
        return releaseFromDate;
    }

    /**
     * Sets the value of the releaseFromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReleaseFromDate(XMLGregorianCalendar value) {
        this.releaseFromDate = value;
    }

    /**
     * Gets the value of the releaseToDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReleaseToDate() {
        return releaseToDate;
    }

    /**
     * Sets the value of the releaseToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReleaseToDate(XMLGregorianCalendar value) {
        this.releaseToDate = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriStatus}
     *
     */
    public StrFordCriStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriStatus}
     *
     */
    public void setStatus(StrFordCriStatus value) {
        this.status = value;
    }

    /**
     * Gets the value of the intFulfillmentOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIntFulfillmentOrderId() {
        return intFulfillmentOrderId;
    }

    /**
     * Sets the value of the intFulfillmentOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIntFulfillmentOrderId(Long value) {
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
     * Gets the value of the binId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBinId() {
        return binId;
    }

    /**
     * Sets the value of the binId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBinId(String value) {
        this.binId = value;
    }

    /**
     * Gets the value of the orderType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriType}
     *
     */
    public StrFordCriType getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriType}
     *
     */
    public void setOrderType(StrFordCriType value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
    }

    /**
     * Gets the value of the storeId property.
     * 
     */
    public long getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     */
    public void setStoreId(long value) {
        this.storeId = value;
    }

}
