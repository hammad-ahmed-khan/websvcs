
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjtplref.v1.StrAdjTplRef;


/**
 * <p>Java class for readInventoryAdjustmentTemplateDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readInventoryAdjustmentTemplateDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjTplRef/v1}StrAdjTplRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readInventoryAdjustmentTemplateDetail", propOrder = {
    "strAdjTplRef"
})
public class ReadInventoryAdjustmentTemplateDetail {

    @XmlElement(name = "StrAdjTplRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjTplRef/v1")
    protected StrAdjTplRef strAdjTplRef;

    /**
     * Gets the value of the strAdjTplRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtplref.v1.StrAdjTplRef}
     *
     */
    public StrAdjTplRef getStrAdjTplRef() {
        return strAdjTplRef;
    }

    /**
     * Sets the value of the strAdjTplRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtplref.v1.StrAdjTplRef}
     *
     */
    public void setStrAdjTplRef(StrAdjTplRef value) {
        this.strAdjTplRef = value;
    }

}
