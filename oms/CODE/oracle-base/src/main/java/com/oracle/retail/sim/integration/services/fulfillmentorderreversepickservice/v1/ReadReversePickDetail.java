
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;


/**
 * <p>Java class for readReversePickDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readReversePickDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpRef/v1}ForpRef" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readReversePickDetail", propOrder = {
    "forpRef"
})
public class ReadReversePickDetail {

    @XmlElement(name = "ForpRef", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpRef/v1")
    protected ForpRef forpRef;

    /**
     * Gets the value of the forpRef property.
     * 
     * @return
     *     possible object is
     *     {@link ForpRef }
     *     
     */
    public ForpRef getForpRef() {
        return forpRef;
    }

    /**
     * Sets the value of the forpRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForpRef }
     *     
     */
    public void setForpRef(ForpRef value) {
        this.forpRef = value;
    }

}
