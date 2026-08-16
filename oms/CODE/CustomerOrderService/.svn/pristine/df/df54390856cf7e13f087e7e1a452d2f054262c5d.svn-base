
package com.oracle.retail.integration.base.bo.custordfuldesc.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.geoaddrdesc.v1.GeoAddrDesc;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1}ContactDesc"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/GeoAddrDesc/v1}GeoAddrDesc" minOccurs="0"/>
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
    "contactDesc",
    "geoAddrDesc"
})
@XmlRootElement(name = "DeliveryDestDtl")
public class DeliveryDestDtl {

    @XmlElement(name = "ContactDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1", required = true)
    protected ContactDesc contactDesc;
    @XmlElement(name = "GeoAddrDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/GeoAddrDesc/v1")
    protected GeoAddrDesc geoAddrDesc;

    /**
     * The contact info of the person to pickup the
     *                                items if customer pickup is specified; or the
     *                                person to ship the items to if ship direct is
     *                                specfied.
     * 
     * @return
     *     possible object is
     *     {@link ContactDesc }
     *     
     */
    public ContactDesc getContactDesc() {
        return contactDesc;
    }

    /**
     * Sets the value of the contactDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactDesc }
     *     
     */
    public void setContactDesc(ContactDesc value) {
        this.contactDesc = value;
    }

    /**
     * The address to ship the order items to if ship
     *                                direct is specifed.
     * 
     * @return
     *     possible object is
     *     {@link GeoAddrDesc }
     *     
     */
    public GeoAddrDesc getGeoAddrDesc() {
        return geoAddrDesc;
    }

    /**
     * Sets the value of the geoAddrDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeoAddrDesc }
     *     
     */
    public void setGeoAddrDesc(GeoAddrDesc value) {
        this.geoAddrDesc = value;
    }

}
