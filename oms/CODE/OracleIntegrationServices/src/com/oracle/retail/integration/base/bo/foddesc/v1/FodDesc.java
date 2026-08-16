
package com.oracle.retail.integration.base.bo.foddesc.v1;

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
 *         &lt;element name="int_fulfill_order_delivery_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ext_fulfill_order_delivery_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://www.oracle.com/retail/integration/base/bo/FodDesc/v1}fod_status"/>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="create_user_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dispatch_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dispatch_user_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodDesc/v1}FodBol"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodDesc/v1}FodItm" maxOccurs="unbounded" minOccurs="0"/>
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
    "extFulfillOrderDeliveryId",
    "fulfillOrderId",
    "storeId",
    "status",
    "createDate",
    "createUserName",
    "dispatchDate",
    "dispatchUserName",
    "fodBol",
    "fodItm"
})
@XmlRootElement(name = "FodDesc")
public class FodDesc {

    @XmlElement(name = "int_fulfill_order_delivery_id")
    protected long intFulfillOrderDeliveryId;
    @XmlElement(name = "ext_fulfill_order_delivery_id")
    protected String extFulfillOrderDeliveryId;
    @XmlElement(name = "fulfill_order_id")
    protected long fulfillOrderId;
    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(required = true)
    protected FodStatus status;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "create_user_name", required = true)
    protected String createUserName;
    @XmlElement(name = "dispatch_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dispatchDate;
    @XmlElement(name = "dispatch_user_name")
    protected String dispatchUserName;
    @XmlElement(name = "FodBol", required = true)
    protected FodBol fodBol;
    @XmlElement(name = "FodItm")
    protected List<FodItm> fodItm;

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
     * Gets the value of the extFulfillOrderDeliveryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtFulfillOrderDeliveryId() {
        return extFulfillOrderDeliveryId;
    }

    /**
     * Sets the value of the extFulfillOrderDeliveryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtFulfillOrderDeliveryId(String value) {
        this.extFulfillOrderDeliveryId = value;
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

    /**
     * Gets the value of the status property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodStatus}
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
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodStatus}
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
     * Gets the value of the createUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * Sets the value of the createUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateUserName(String value) {
        this.createUserName = value;
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

    /**
     * Gets the value of the dispatchUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispatchUserName() {
        return dispatchUserName;
    }

    /**
     * Sets the value of the dispatchUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispatchUserName(String value) {
        this.dispatchUserName = value;
    }

    /**
     * Gets the value of the fodBol property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodBol}
     *
     */
    public FodBol getFodBol() {
        return fodBol;
    }

    /**
     * Sets the value of the fodBol property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodBol}
     *
     */
    public void setFodBol(FodBol value) {
        this.fodBol = value;
    }

    /**
     * Gets the value of the fodItm property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fodItm property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getFodItm().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodItm}
     *
     *
     */
    public List<FodItm> getFodItm() {
        if (fodItm == null) {
            fodItm = new ArrayList<FodItm>();
        }
        return this.fodItm;
    }

}
