
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo;


/**
 * <p>Java class for lookupFulfillmentOrderHeaders complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupFulfillmentOrderHeaders">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrCriVo/v1}StrFordHdrCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupFulfillmentOrderHeaders", propOrder = {
    "strFordHdrCriVo"
})
public class LookupFulfillmentOrderHeaders {

    @XmlElement(name = "StrFordHdrCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrFordHdrCriVo/v1")
    protected StrFordHdrCriVo strFordHdrCriVo;

    /**
     * Gets the value of the strFordHdrCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo}
     *
     */
    public StrFordHdrCriVo getStrFordHdrCriVo() {
        return strFordHdrCriVo;
    }

    /**
     * Sets the value of the strFordHdrCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo}
     *
     */
    public void setStrFordHdrCriVo(StrFordHdrCriVo value) {
        this.strFordHdrCriVo = value;
    }

}
