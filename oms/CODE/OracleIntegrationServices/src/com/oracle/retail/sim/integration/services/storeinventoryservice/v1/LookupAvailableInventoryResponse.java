
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.invavailcoldesc.v1.InvAvailColDesc;


/**
 * <p>Java class for lookupAvailableInventoryResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupAvailableInventoryResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1}InvAvailColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupAvailableInventoryResponse", propOrder = {
    "invAvailColDesc"
})
public class LookupAvailableInventoryResponse {

    @XmlElement(name = "InvAvailColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/InvAvailColDesc/v1")
    protected InvAvailColDesc invAvailColDesc;

    /**
     * Gets the value of the invAvailColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcoldesc.v1.InvAvailColDesc}
     *
     */
    public InvAvailColDesc getInvAvailColDesc() {
        return invAvailColDesc;
    }

    /**
     * Sets the value of the invAvailColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcoldesc.v1.InvAvailColDesc}
     *
     */
    public void setInvAvailColDesc(InvAvailColDesc value) {
        this.invAvailColDesc = value;
    }

}
