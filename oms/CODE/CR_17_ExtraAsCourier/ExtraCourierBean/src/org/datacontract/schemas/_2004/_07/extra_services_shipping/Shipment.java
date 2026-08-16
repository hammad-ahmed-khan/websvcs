
package org.datacontract.schemas._2004._07.extra_services_shipping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="Address" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto}Address"/>
 *         &lt;element name="Carrier" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto}Carrier" minOccurs="0"/>
 *         &lt;element name="Customer" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto}Customer" minOccurs="0"/>
 *         &lt;element name="IsCod" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsExpressDelivery" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="OrderId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="OrderValue" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Products" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto}ArrayOfProduct" minOccurs="0"/>
 *         &lt;element name="ShipmentOrder" type="{http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto}ShipmentOrder" minOccurs="0"/>
 *         &lt;element name="StoreId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="TrackingNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "addressAndCarrierAndCustomer"
})
@XmlRootElement(name = "Shipment")
public class Shipment {

    @XmlElementRefs({
        @XmlElementRef(name = "Products", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "OrderValue", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "Address", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "ShipmentOrder", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "Carrier", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "Customer", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "IsCod", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "TrackingNumber", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "IsExpressDelivery", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "StoreId", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class),
        @XmlElementRef(name = "OrderId", namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> addressAndCarrierAndCustomer;

    /**
     * Gets the value of the addressAndCarrierAndCustomer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressAndCarrierAndCustomer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressAndCarrierAndCustomer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ArrayOfProduct }{@code >}
     * {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}
     * {@link JAXBElement }{@code <}{@link ShipmentOrder }{@code >}
     * {@link JAXBElement }{@code <}{@link Address }{@code >}
     * {@link JAXBElement }{@code <}{@link Carrier }{@code >}
     * {@link JAXBElement }{@code <}{@link Customer }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getAddressAndCarrierAndCustomer() {
        if (addressAndCarrierAndCustomer == null) {
            addressAndCarrierAndCustomer = new ArrayList<JAXBElement<?>>();
        }
        return this.addressAndCarrierAndCustomer;
    }

}
