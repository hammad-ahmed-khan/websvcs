
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;


/**
 * <p>Java class for cancelFulfillmentOrderDeliveryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelFulfillmentOrderDeliveryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1}InvocationSuccess" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelFulfillmentOrderDeliveryResponse", propOrder = {
    "invocationSuccess"
})
public class CancelFulfillmentOrderDeliveryResponse {

    @XmlElement(name = "InvocationSuccess", namespace = "http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1")
    protected InvocationSuccess invocationSuccess;

    /**
     * Gets the value of the invocationSuccess property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess}
     *
     */
    public InvocationSuccess getInvocationSuccess() {
        return invocationSuccess;
    }

    /**
     * Sets the value of the invocationSuccess property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess}
     *
     */
    public void setInvocationSuccess(InvocationSuccess value) {
        this.invocationSuccess = value;
    }

}
