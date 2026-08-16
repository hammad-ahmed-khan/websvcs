
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef;


/**
 * <p>Java class for cancelInventoryAdjustment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelInventoryAdjustment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjRef/v1}StrAdjRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelInventoryAdjustment", propOrder = {
    "strAdjRef"
})
public class CancelInventoryAdjustment {

    @XmlElement(name = "StrAdjRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjRef/v1")
    protected StrAdjRef strAdjRef;

    /**
     * Gets the value of the strAdjRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef}
     *
     */
    public StrAdjRef getStrAdjRef() {
        return strAdjRef;
    }

    /**
     * Sets the value of the strAdjRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef}
     *
     */
    public void setStrAdjRef(StrAdjRef value) {
        this.strAdjRef = value;
    }

}
