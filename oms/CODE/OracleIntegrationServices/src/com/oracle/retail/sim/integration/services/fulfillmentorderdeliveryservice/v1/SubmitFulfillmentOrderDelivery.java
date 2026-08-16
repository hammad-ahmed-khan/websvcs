
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;


/**
 * <p>Java class for submitFulfillmentOrderDelivery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitFulfillmentOrderDelivery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodRef/v1}FodRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitFulfillmentOrderDelivery", propOrder = {
    "fodRef"
})
public class SubmitFulfillmentOrderDelivery {

    @XmlElement(name = "FodRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FodRef/v1")
    protected FodRef fodRef;

    /**
     * Gets the value of the fodRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fodref.v1.FodRef}
     *
     */
    public FodRef getFodRef() {
        return fodRef;
    }

    /**
     * Sets the value of the fodRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fodref.v1.FodRef}
     *
     */
    public void setFodRef(FodRef value) {
        this.fodRef = value;
    }

}
