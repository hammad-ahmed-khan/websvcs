
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="addr_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="primary_addr_ind" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}prim_addr_type"/>
 *         &lt;element name="addr_type" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}addr_type" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/GeoAddrDesc/v1}GeoAddrDesc"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1}ContactDesc" minOccurs="0"/>
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
    "addrId",
    "primaryAddrInd",
    "addrType",
    "geoAddrDesc",
    "contactDesc"
})
@XmlRootElement(name = "AddrBookEntry")
public class AddrBookEntry {

    @XmlElement(name = "addr_id")
    protected BigDecimal addrId;
    @XmlElement(name = "primary_addr_ind", required = true)
    protected PrimAddrType primaryAddrInd;
    @XmlElement(name = "addr_type")
    protected AddrType addrType;
    @XmlElement(name = "GeoAddrDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/GeoAddrDesc/v1", required = true)
    protected GeoAddrDesc geoAddrDesc;
    @XmlElement(name = "ContactDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1")
    protected ContactDesc contactDesc;

    /**
     * Gets the value of the addrId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAddrId() {
        return addrId;
    }

    /**
     * Sets the value of the addrId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAddrId(BigDecimal value) {
        this.addrId = value;
    }

    /**
     * Gets the value of the primaryAddrInd property.
     * 
     * @return
     *     possible object is
     *     {@link PrimAddrType }
     *     
     */
    public PrimAddrType getPrimaryAddrInd() {
        return primaryAddrInd;
    }

    /**
     * Sets the value of the primaryAddrInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimAddrType }
     *     
     */
    public void setPrimaryAddrInd(PrimAddrType value) {
        this.primaryAddrInd = value;
    }

    /**
     * Gets the value of the addrType property.
     * 
     * @return
     *     possible object is
     *     {@link AddrType }
     *     
     */
    public AddrType getAddrType() {
        return addrType;
    }

    /**
     * Sets the value of the addrType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddrType }
     *     
     */
    public void setAddrType(AddrType value) {
        this.addrType = value;
    }

    /**
     * Contains a customer address
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

    /**
     * Contains the contact information for an address. If no contact information provided
     * 		     				at this level, then the contact information provided at the customer header level will be used.
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

}
