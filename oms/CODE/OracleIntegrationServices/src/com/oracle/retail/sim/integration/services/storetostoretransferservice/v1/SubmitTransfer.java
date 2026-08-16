
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;


/**
 * <p>Java class for submitTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfRef/v1}StsTsfRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitTransfer", propOrder = {
    "stsTsfRef"
})
public class SubmitTransfer {

    @XmlElement(name = "StsTsfRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfRef/v1")
    protected StsTsfRef stsTsfRef;

    /**
     * Gets the value of the stsTsfRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef}
     *
     */
    public StsTsfRef getStsTsfRef() {
        return stsTsfRef;
    }

    /**
     * Sets the value of the stsTsfRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef}
     *
     */
    public void setStsTsfRef(StsTsfRef value) {
        this.stsTsfRef = value;
    }

}
