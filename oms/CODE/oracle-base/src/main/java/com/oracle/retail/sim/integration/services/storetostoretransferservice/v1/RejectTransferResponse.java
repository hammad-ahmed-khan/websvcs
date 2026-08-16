
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;


/**
 * <p>Java class for rejectTransferResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rejectTransferResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1}InvocationSuccess" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rejectTransferResponse", propOrder = {
    "invocationSuccess"
})
public class RejectTransferResponse {

    @XmlElement(name = "InvocationSuccess", namespace = "http://www.oracle.com/retail/integration/base/bo/InvocationSuccess/v1")
    protected InvocationSuccess invocationSuccess;

    /**
     * Gets the value of the invocationSuccess property.
     * 
     * @return
     *     possible object is
     *     {@link InvocationSuccess }
     *     
     */
    public InvocationSuccess getInvocationSuccess() {
        return invocationSuccess;
    }

    /**
     * Sets the value of the invocationSuccess property.
     * 
     * @param value
     *     allowed object is
     *     {@link InvocationSuccess }
     *     
     */
    public void setInvocationSuccess(InvocationSuccess value) {
        this.invocationSuccess = value;
    }

}
