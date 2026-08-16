
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjrsncoldesc.v1.StrAdjRsnColDesc;


/**
 * <p>Java class for lookupInventoryAdjustmentReasonResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryAdjustmentReasonResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjRsnColDesc/v1}StrAdjRsnColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryAdjustmentReasonResponse", propOrder = {
    "strAdjRsnColDesc"
})
public class LookupInventoryAdjustmentReasonResponse {

    @XmlElement(name = "StrAdjRsnColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjRsnColDesc/v1")
    protected StrAdjRsnColDesc strAdjRsnColDesc;

    /**
     * Gets the value of the strAdjRsnColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjRsnColDesc }
     *     
     */
    public StrAdjRsnColDesc getStrAdjRsnColDesc() {
        return strAdjRsnColDesc;
    }

    /**
     * Sets the value of the strAdjRsnColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjRsnColDesc }
     *     
     */
    public void setStrAdjRsnColDesc(StrAdjRsnColDesc value) {
        this.strAdjRsnColDesc = value;
    }

}
