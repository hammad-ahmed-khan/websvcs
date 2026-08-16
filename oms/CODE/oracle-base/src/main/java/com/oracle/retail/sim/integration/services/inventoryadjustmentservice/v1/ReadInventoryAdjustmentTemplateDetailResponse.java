
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjtpldesc.v1.StrAdjTplDesc;


/**
 * <p>Java class for readInventoryAdjustmentTemplateDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readInventoryAdjustmentTemplateDetailResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjTplDesc/v1}StrAdjTplDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readInventoryAdjustmentTemplateDetailResponse", propOrder = {
    "strAdjTplDesc"
})
public class ReadInventoryAdjustmentTemplateDetailResponse {

    @XmlElement(name = "StrAdjTplDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjTplDesc/v1")
    protected StrAdjTplDesc strAdjTplDesc;

    /**
     * Gets the value of the strAdjTplDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjTplDesc }
     *     
     */
    public StrAdjTplDesc getStrAdjTplDesc() {
        return strAdjTplDesc;
    }

    /**
     * Sets the value of the strAdjTplDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjTplDesc }
     *     
     */
    public void setStrAdjTplDesc(StrAdjTplDesc value) {
        this.strAdjTplDesc = value;
    }

}
