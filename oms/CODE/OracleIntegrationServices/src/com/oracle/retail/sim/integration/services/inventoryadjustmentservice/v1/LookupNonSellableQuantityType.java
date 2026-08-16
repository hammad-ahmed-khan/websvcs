
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.nothing.v1.Nothing;


/**
 * <p>Java class for lookupNonSellableQuantityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupNonSellableQuantityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/Nothing/v1}Nothing" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupNonSellableQuantityType", propOrder = {
    "nothing"
})
public class LookupNonSellableQuantityType {

    @XmlElement(name = "Nothing", namespace = "http://www.oracle.com/retail/integration/base/bo/Nothing/v1")
    protected Nothing nothing;

    /**
     * Gets the value of the nothing property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.nothing.v1.Nothing}
     *
     */
    public Nothing getNothing() {
        return nothing;
    }

    /**
     * Sets the value of the nothing property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.nothing.v1.Nothing}
     *
     */
    public void setNothing(Nothing value) {
        this.nothing = value;
    }

}
