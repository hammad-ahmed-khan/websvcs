
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;


/**
 * <p>Java class for cancelTransferSubmission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelTransferSubmission"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfRef/v1}StsTsfRef" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelTransferSubmission", propOrder = {
    "stsTsfRef"
})
public class CancelTransferSubmission {

    @XmlElement(name = "StsTsfRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfRef/v1")
    protected StsTsfRef stsTsfRef;

    /**
     * Gets the value of the stsTsfRef property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfRef }
     *     
     */
    public StsTsfRef getStsTsfRef() {
        return stsTsfRef;
    }

    /**
     * Sets the value of the stsTsfRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfRef }
     *     
     */
    public void setStsTsfRef(StsTsfRef value) {
        this.stsTsfRef = value;
    }

}
