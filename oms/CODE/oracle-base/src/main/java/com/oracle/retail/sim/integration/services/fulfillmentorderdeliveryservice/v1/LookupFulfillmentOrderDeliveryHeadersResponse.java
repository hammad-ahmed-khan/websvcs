
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;


/**
 * <p>Java class for lookupFulfillmentOrderDeliveryHeadersResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupFulfillmentOrderDeliveryHeadersResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodHdrColDesc/v1}FodHdrColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupFulfillmentOrderDeliveryHeadersResponse", propOrder = {
    "fodHdrColDesc"
})
public class LookupFulfillmentOrderDeliveryHeadersResponse {

    @XmlElement(name = "FodHdrColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FodHdrColDesc/v1")
    protected FodHdrColDesc fodHdrColDesc;

    /**
     * Gets the value of the fodHdrColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link FodHdrColDesc }
     *     
     */
    public FodHdrColDesc getFodHdrColDesc() {
        return fodHdrColDesc;
    }

    /**
     * Sets the value of the fodHdrColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link FodHdrColDesc }
     *     
     */
    public void setFodHdrColDesc(FodHdrColDesc value) {
        this.fodHdrColDesc = value;
    }

}
