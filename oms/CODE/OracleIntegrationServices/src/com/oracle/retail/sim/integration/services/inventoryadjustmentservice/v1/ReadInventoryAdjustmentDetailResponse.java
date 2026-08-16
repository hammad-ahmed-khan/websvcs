
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjdesc.v1.StrAdjDesc;


/**
 * <p>Java class for readInventoryAdjustmentDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readInventoryAdjustmentDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjDesc/v1}StrAdjDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readInventoryAdjustmentDetailResponse", propOrder = {
    "strAdjDesc"
})
public class ReadInventoryAdjustmentDetailResponse {

    @XmlElement(name = "StrAdjDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjDesc/v1")
    protected StrAdjDesc strAdjDesc;

    /**
     * Gets the value of the strAdjDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjdesc.v1.StrAdjDesc}
     *
     */
    public StrAdjDesc getStrAdjDesc() {
        return strAdjDesc;
    }

    /**
     * Sets the value of the strAdjDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjdesc.v1.StrAdjDesc}
     *
     */
    public void setStrAdjDesc(StrAdjDesc value) {
        this.strAdjDesc = value;
    }

}
