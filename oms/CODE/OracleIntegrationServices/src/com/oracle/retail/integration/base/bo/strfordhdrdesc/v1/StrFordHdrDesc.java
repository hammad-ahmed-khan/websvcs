
package com.oracle.retail.integration.base.bo.strfordhdrdesc.v1;

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
 *         &lt;element name="ext_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="order_type" type="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrDesc/v1}str_ford_type"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrDesc/v1}str_ford_status"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="release_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="number_of_skus" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "numberOfSkus"
})
@XmlRootElement(name = "StrFordHdrDesc")
public class StrFordHdrDesc {

    @XmlElement(name = "int_fulfillment_order_id")
    protected long intFulfillmentOrderId;
    @XmlElement(name = "ext_fulfillment_order_id")
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
    @XmlElement(name = "number_of_skus")
    protected int numberOfSkus;

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
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrdesc.v1.StrFordType}
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
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrdesc.v1.StrFordType}
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
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrdesc.v1.StrFordStatus}
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
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrdesc.v1.StrFordStatus}
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
     * Gets the value of the numberOfSkus property.
     * 
     */
    public int getNumberOfSkus() {
        return numberOfSkus;
    }

    /**
     * Sets the value of the numberOfSkus property.
     * 
     */
    public void setNumberOfSkus(int value) {
        this.numberOfSkus = value;
    }

}
