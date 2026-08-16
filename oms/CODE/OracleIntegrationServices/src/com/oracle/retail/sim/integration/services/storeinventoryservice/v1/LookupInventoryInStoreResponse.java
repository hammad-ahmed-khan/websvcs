
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc;


/**
 * <p>Java class for lookupInventoryInStoreResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryInStoreResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1}StrInvColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryInStoreResponse", propOrder = {
    "strInvColDesc"
})
public class LookupInventoryInStoreResponse {

    @XmlElement(name = "StrInvColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrInvColDesc/v1")
    protected StrInvColDesc strInvColDesc;

    /**
     * Gets the value of the strInvColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc}
     *
     */
    public StrInvColDesc getStrInvColDesc() {
        return strInvColDesc;
    }

    /**
     * Sets the value of the strInvColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcoldesc.v1.StrInvColDesc}
     *
     */
    public void setStrInvColDesc(StrInvColDesc value) {
        this.strInvColDesc = value;
    }

}
