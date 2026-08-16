
package com.oracle.retail.integration.base.bo.fodhdrdesc.v1;

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
 *         &lt;element name="int_fulfill_order_delivery_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/FodHdrDesc/v1}fod_status"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="dispatch_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
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
    "intFulfillOrderDeliveryId",
    "fulfillOrderId",
    "status",
    "createDate",
    "dispatchDate"
})
@XmlRootElement(name = "FodHdrDesc")
public class FodHdrDesc {

    @XmlElement(name = "int_fulfill_order_delivery_id")
    protected long intFulfillOrderDeliveryId;
    @XmlElement(name = "fulfill_order_id")
    protected long fulfillOrderId;
    @XmlElement(required = true)
    protected FodStatus status;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "dispatch_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dispatchDate;

    /**
     * Gets the value of the intFulfillOrderDeliveryId property.
     * 
     */
    public long getIntFulfillOrderDeliveryId() {
        return intFulfillOrderDeliveryId;
    }

    /**
     * Sets the value of the intFulfillOrderDeliveryId property.
     * 
     */
    public void setIntFulfillOrderDeliveryId(long value) {
        this.intFulfillOrderDeliveryId = value;
    }

    /**
     * Gets the value of the fulfillOrderId property.
     * 
     */
    public long getFulfillOrderId() {
        return fulfillOrderId;
    }

    /**
     * Sets the value of the fulfillOrderId property.
     * 
     */
    public void setFulfillOrderId(long value) {
        this.fulfillOrderId = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodStatus}
     *
     */
    public FodStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodStatus}
     *
     */
    public void setStatus(FodStatus value) {
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
     * Gets the value of the dispatchDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDispatchDate() {
        return dispatchDate;
    }

    /**
     * Sets the value of the dispatchDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDispatchDate(XMLGregorianCalendar value) {
        this.dispatchDate = value;
    }

}
