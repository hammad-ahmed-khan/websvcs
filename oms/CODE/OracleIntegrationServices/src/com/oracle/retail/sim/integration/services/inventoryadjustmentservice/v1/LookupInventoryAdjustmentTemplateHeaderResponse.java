
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjtphdcoldesc.v1.StrAdjTpHdColDesc;


/**
 * <p>Java class for lookupInventoryAdjustmentTemplateHeaderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryAdjustmentTemplateHeaderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjTpHdColDesc/v1}StrAdjTpHdColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryAdjustmentTemplateHeaderResponse", propOrder = {
    "strAdjTpHdColDesc"
})
public class LookupInventoryAdjustmentTemplateHeaderResponse {

    @XmlElement(name = "StrAdjTpHdColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjTpHdColDesc/v1")
    protected StrAdjTpHdColDesc strAdjTpHdColDesc;

    /**
     * Gets the value of the strAdjTpHdColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtphdcoldesc.v1.StrAdjTpHdColDesc}
     *
     */
    public StrAdjTpHdColDesc getStrAdjTpHdColDesc() {
        return strAdjTpHdColDesc;
    }

    /**
     * Sets the value of the strAdjTpHdColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtphdcoldesc.v1.StrAdjTpHdColDesc}
     *
     */
    public void setStrAdjTpHdColDesc(StrAdjTpHdColDesc value) {
        this.strAdjTpHdColDesc = value;
    }

}
