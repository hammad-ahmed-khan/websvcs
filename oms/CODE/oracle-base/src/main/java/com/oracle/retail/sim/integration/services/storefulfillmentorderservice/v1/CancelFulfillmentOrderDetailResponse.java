
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;


/**
 * <p>Java class for cancelFulfillmentOrderDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelFulfillmentOrderDetailResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1}FulfilOrdColRef" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelFulfillmentOrderDetailResponse", propOrder = {
    "fulfilOrdColRef"
})
public class CancelFulfillmentOrderDetailResponse {

    @XmlElement(name = "FulfilOrdColRef", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdColRef/v1")
    protected FulfilOrdColRef fulfilOrdColRef;

    /**
     * Gets the value of the fulfilOrdColRef property.
     * 
     * @return
     *     possible object is
     *     {@link FulfilOrdColRef }
     *     
     */
    public FulfilOrdColRef getFulfilOrdColRef() {
        return fulfilOrdColRef;
    }

    /**
     * Sets the value of the fulfilOrdColRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link FulfilOrdColRef }
     *     
     */
    public void setFulfilOrdColRef(FulfilOrdColRef value) {
        this.fulfilOrdColRef = value;
    }

}
