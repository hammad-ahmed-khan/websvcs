
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
 * &lt;complexType name="readReversePickDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpRef/v1}ForpRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.forpref.v1.ForpRef}
     *
     */
    public ForpRef getForpRef() {
        return forpRef;
    }

    /**
     * Sets the value of the forpRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.forpref.v1.ForpRef}
     *
     */
    public void setForpRef(ForpRef value) {
        this.forpRef = value;
    }

}
