
package com.extra.services.omsstatus;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.datacontract.schemas._2004._07.extra_services.OrderStatus;


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
 *         &lt;element name="orderStatus" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Oms}OrderStatus" minOccurs="0"/>
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
    "orderStatus"
})
@XmlRootElement(name = "UpdateOrderStatus")
public class UpdateOrderStatus {

    @XmlElementRef(name = "orderStatus", namespace = "http://www.extra.com/Services/OmsStatus", type = JAXBElement.class)
    protected JAXBElement<OrderStatus> orderStatus;

    /**
     * Gets the value of the orderStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     *     
     */
    public JAXBElement<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the value of the orderStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     *     
     */
    public void setOrderStatus(JAXBElement<OrderStatus> value) {
        this.orderStatus = ((JAXBElement<OrderStatus> ) value);
    }

}
