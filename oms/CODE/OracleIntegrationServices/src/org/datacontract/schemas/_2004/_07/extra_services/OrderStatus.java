
package org.datacontract.schemas._2004._07.extra_services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for OrderStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderStatus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EnitityId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApplicationId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SubOrderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OmsOrderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="OrderDetailStatuses" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Oms}ArrayOfOrderDetailStatus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderStatus", propOrder = {
    "enitityId",
    "applicationId",
    "orderId",
    "subOrderId",
    "omsOrderId",
    "deliveryDate",
    "updateDate",
    "orderDetailStatuses"
})
public class OrderStatus {

    @XmlElement(name = "EnitityId", required = true, nillable = true)
    protected String enitityId;
    @XmlElement(name = "ApplicationId", required = true, nillable = true)
    protected String applicationId;
    @XmlElement(name = "OrderId", required = true, nillable = true)
    protected String orderId;
    @XmlElementRef(name = "SubOrderId", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", type = JAXBElement.class)
    protected JAXBElement<String> subOrderId;
    @XmlElement(name = "OmsOrderId", required = true, nillable = true)
    protected String omsOrderId;
    @XmlElement(name = "DeliveryDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deliveryDate;
    @XmlElement(name = "UpdateDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDate;
    @XmlElement(name = "OrderDetailStatuses", required = true, nillable = true)
    protected ArrayOfOrderDetailStatus orderDetailStatuses;

    /**
     * Gets the value of the enitityId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnitityId() {
        return enitityId;
    }

    /**
     * Sets the value of the enitityId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnitityId(String value) {
        this.enitityId = value;
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
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the subOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubOrderId() {
        return subOrderId;
    }

    /**
     * Sets the value of the subOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubOrderId(JAXBElement<String> value) {
        this.subOrderId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the omsOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOmsOrderId() {
        return omsOrderId;
    }

    /**
     * Sets the value of the omsOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOmsOrderId(String value) {
        this.omsOrderId = value;
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
     * Gets the value of the updateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

    /**
     * Gets the value of the orderDetailStatuses property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOrderDetailStatus }
     *     
     */
    public ArrayOfOrderDetailStatus getOrderDetailStatuses() {
        return orderDetailStatuses;
    }

    /**
     * Sets the value of the orderDetailStatuses property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOrderDetailStatus }
     *     
     */
    public void setOrderDetailStatuses(ArrayOfOrderDetailStatus value) {
        this.orderDetailStatuses = value;
    }

}
