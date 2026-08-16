
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1.StrAdjHdrCriVo;


/**
 * <p>Java class for lookupInventoryAdjustmentHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryAdjustmentHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjHdrCriVo/v1}StrAdjHdrCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryAdjustmentHeader", propOrder = {
    "strAdjHdrCriVo"
})
public class LookupInventoryAdjustmentHeader {

    @XmlElement(name = "StrAdjHdrCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjHdrCriVo/v1")
    protected StrAdjHdrCriVo strAdjHdrCriVo;

    /**
     * Gets the value of the strAdjHdrCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1.StrAdjHdrCriVo}
     *
     */
    public StrAdjHdrCriVo getStrAdjHdrCriVo() {
        return strAdjHdrCriVo;
    }

    /**
     * Sets the value of the strAdjHdrCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.stradjhdrcrivo.v1.StrAdjHdrCriVo}
     *
     */
    public void setStrAdjHdrCriVo(StrAdjHdrCriVo value) {
        this.strAdjHdrCriVo = value;
    }

}
