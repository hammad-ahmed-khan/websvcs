
package com.oracle.retail.integration.base.bo.pickupcustomerorderitemdetailsref.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custordcancelitemdetailscol.v1.CustOrdCancelItemDetailsCol;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;


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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1}CustOrderRef" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdCancelItemDetailsCol/v1}CustOrdCancelItemDetailsCol" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "PickupCustomerOrderItemDetailsRef")
@XmlType(name = "", propOrder = {
    "custOrderRef",
    "pickupStatus",
    "custOrdCancelItemDetailsCol"
})
public class PickupCustomerOrderItemDetailsRef {

    @XmlElement(name = "CustOrderRef", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1")
    protected CustOrderRef custOrderRef;
    @XmlElement(name = "CustOrdCancelItemDetailsCol", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdCancelItemDetailsCol/v1")
    protected CustOrdCancelItemDetailsCol custOrdCancelItemDetailsCol;
    @XmlElement(name = "pickup_status", required = true)
    protected String pickupStatus;

    /**
     * A collection of customer order id references.
     *
     * @return
     *     possible object is
     *     {@link CustOrderRef }
     *
     */
    public CustOrderRef getCustOrderRef() {
        return custOrderRef;
    }

    /**
     * Sets the value of the custOrderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderRef }
     *     
     */
    public void setCustOrderRef(CustOrderRef value) {
        this.custOrderRef = value;
    }


    /**
     * A collection of customer order items references.
     *
     * @return
     *     possible object is
     *     {@link CustOrdCancelItemDetailsCol }
     *
     */
    public CustOrdCancelItemDetailsCol getCustOrdCancelItemDetailsCol() {
        return custOrdCancelItemDetailsCol;
    }

    /**
     * Sets the value of the custOrdCancelItemDetailsCol property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrdCancelItemDetailsCol }
     *     
     */
    public void setCustOrdCancelItemDetailsCol(CustOrdCancelItemDetailsCol value) {
        this.custOrdCancelItemDetailsCol = value;
    }

    /**
     * Gets the value of the pickupStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPickupStatus() {
        return pickupStatus;
    }

    /**
     * Sets the value of the pickupStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPickupStatus(String value) {
        this.pickupStatus = value;
    }

}
