
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strinvgpcrivo.v1.StrInvGpCriVo;


/**
 * <p>Java class for lookupInventoryInTransferZone complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryInTransferZone">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1}StrInvGpCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryInTransferZone", propOrder = {
    "strInvGpCriVo"
})
public class LookupInventoryInTransferZone {

    @XmlElement(name = "StrInvGpCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrInvGpCriVo/v1")
    protected StrInvGpCriVo strInvGpCriVo;

    /**
     * Gets the value of the strInvGpCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strinvgpcrivo.v1.StrInvGpCriVo}
     *
     */
    public StrInvGpCriVo getStrInvGpCriVo() {
        return strInvGpCriVo;
    }

    /**
     * Sets the value of the strInvGpCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strinvgpcrivo.v1.StrInvGpCriVo}
     *
     */
    public void setStrInvGpCriVo(StrInvGpCriVo value) {
        this.strInvGpCriVo = value;
    }

}
