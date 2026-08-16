
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;


/**
 * <p>Java class for cancelFulfillmentOrderDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelFulfillmentOrderDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1}FulfilOrdColRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelFulfillmentOrderDetail", propOrder = {
    "fulfilOrdColRef"
})
public class CancelFulfillmentOrderDetail {

    @XmlElement(name = "FulfilOrdColRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1")
    protected FulfilOrdColRef fulfilOrdColRef;

    /**
     * Gets the value of the fulfilOrdColRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef}
     *
     */
    public FulfilOrdColRef getFulfilOrdColRef() {
        return fulfilOrdColRef;
    }

    /**
     * Sets the value of the fulfilOrdColRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef}
     *
     */
    public void setFulfilOrdColRef(FulfilOrdColRef value) {
        this.fulfilOrdColRef = value;
    }

}
