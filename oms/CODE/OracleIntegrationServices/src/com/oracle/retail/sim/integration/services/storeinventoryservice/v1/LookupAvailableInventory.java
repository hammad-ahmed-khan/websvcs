
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvAvailCriVo;


/**
 * <p>Java class for lookupAvailableInventory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupAvailableInventory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1}InvAvailCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupAvailableInventory", propOrder = {
    "invAvailCriVo"
})
public class LookupAvailableInventory {

    @XmlElement(name = "InvAvailCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1")
    protected InvAvailCriVo invAvailCriVo;

    /**
     * Gets the value of the invAvailCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvAvailCriVo}
     *
     */
    public InvAvailCriVo getInvAvailCriVo() {
        return invAvailCriVo;
    }

    /**
     * Sets the value of the invAvailCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvAvailCriVo}
     *
     */
    public void setInvAvailCriVo(InvAvailCriVo value) {
        this.invAvailCriVo = value;
    }

}
