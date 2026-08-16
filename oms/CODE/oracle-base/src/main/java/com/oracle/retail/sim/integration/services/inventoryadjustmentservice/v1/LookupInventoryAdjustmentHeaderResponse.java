
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjhdrcoldesc.v1.StrAdjHdrColDesc;


/**
 * <p>Java class for lookupInventoryAdjustmentHeaderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryAdjustmentHeaderResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjHdrColDesc/v1}StrAdjHdrColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryAdjustmentHeaderResponse", propOrder = {
    "strAdjHdrColDesc"
})
public class LookupInventoryAdjustmentHeaderResponse {

    @XmlElement(name = "StrAdjHdrColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjHdrColDesc/v1")
    protected StrAdjHdrColDesc strAdjHdrColDesc;

    /**
     * Gets the value of the strAdjHdrColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjHdrColDesc }
     *     
     */
    public StrAdjHdrColDesc getStrAdjHdrColDesc() {
        return strAdjHdrColDesc;
    }

    /**
     * Sets the value of the strAdjHdrColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjHdrColDesc }
     *     
     */
    public void setStrAdjHdrColDesc(StrAdjHdrColDesc value) {
        this.strAdjHdrColDesc = value;
    }

}
