
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjtphdcrivo.v1.StrAdjTpHdCriVo;


/**
 * <p>Java class for lookupInventoryAdjustmentTemplateHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryAdjustmentTemplateHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjTpHdCriVo/v1}StrAdjTpHdCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryAdjustmentTemplateHeader", propOrder = {
    "strAdjTpHdCriVo"
})
public class LookupInventoryAdjustmentTemplateHeader {

    @XmlElement(name = "StrAdjTpHdCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjTpHdCriVo/v1")
    protected StrAdjTpHdCriVo strAdjTpHdCriVo;

    /**
     * Gets the value of the strAdjTpHdCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtphdcrivo.v1.StrAdjTpHdCriVo}
     *
     */
    public StrAdjTpHdCriVo getStrAdjTpHdCriVo() {
        return strAdjTpHdCriVo;
    }

    /**
     * Sets the value of the strAdjTpHdCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjtphdcrivo.v1.StrAdjTpHdCriVo}
     *
     */
    public void setStrAdjTpHdCriVo(StrAdjTpHdCriVo value) {
        this.strAdjTpHdCriVo = value;
    }

}
