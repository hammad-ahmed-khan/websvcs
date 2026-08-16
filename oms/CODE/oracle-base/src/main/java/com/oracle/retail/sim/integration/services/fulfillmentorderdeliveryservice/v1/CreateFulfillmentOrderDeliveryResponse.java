
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;


/**
 * <p>Java class for createFulfillmentOrderDeliveryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFulfillmentOrderDeliveryResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodRef/v1}FodRef" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createFulfillmentOrderDeliveryResponse", propOrder = {
    "fodRef"
})
public class CreateFulfillmentOrderDeliveryResponse {

    @XmlElement(name = "FodRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FodRef/v1")
    protected FodRef fodRef;

    /**
     * Gets the value of the fodRef property.
     * 
     * @return
     *     possible object is
     *     {@link FodRef }
     *     
     */
    public FodRef getFodRef() {
        return fodRef;
    }

    /**
     * Sets the value of the fodRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link FodRef }
     *     
     */
    public void setFodRef(FodRef value) {
        this.fodRef = value;
    }

}
