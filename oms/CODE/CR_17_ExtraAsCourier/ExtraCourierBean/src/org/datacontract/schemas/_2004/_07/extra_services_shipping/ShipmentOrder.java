
package org.datacontract.schemas._2004._07.extra_services_shipping;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShipmentOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShipOrderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ShipmentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentOrder", propOrder = {
    "shipOrderId",
    "shipmentId"
})
public class ShipmentOrder {

    @XmlElementRef(name = "ShipOrderId", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class)
    protected JAXBElement<String> shipOrderId;
    @XmlElementRef(name = "ShipmentId", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class)
    protected JAXBElement<String> shipmentId;

    /**
     * Gets the value of the shipOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getShipOrderId() {
        return shipOrderId;
    }

    /**
     * Sets the value of the shipOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setShipOrderId(JAXBElement<String> value) {
        this.shipOrderId = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the shipmentId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getShipmentId() {
        return shipmentId;
    }

    /**
     * Sets the value of the shipmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setShipmentId(JAXBElement<String> value) {
        this.shipmentId = ((JAXBElement<String> ) value);
    }

}
