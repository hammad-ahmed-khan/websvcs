
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjref.v1.StrAdjRef;


/**
 * <p>Java class for readInventoryAdjustmentDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readInventoryAdjustmentDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjRef/v1}StrAdjRef" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readInventoryAdjustmentDetail", propOrder = {
    "strAdjRef"
})
public class ReadInventoryAdjustmentDetail {

    @XmlElement(name = "StrAdjRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjRef/v1")
    protected StrAdjRef strAdjRef;

    /**
     * Gets the value of the strAdjRef property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjRef }
     *     
     */
    public StrAdjRef getStrAdjRef() {
        return strAdjRef;
    }

    /**
     * Sets the value of the strAdjRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjRef }
     *     
     */
    public void setStrAdjRef(StrAdjRef value) {
        this.strAdjRef = value;
    }

}
