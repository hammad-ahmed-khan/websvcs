
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo;


/**
 * <p>Java class for lookupInventoryInStore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupInventoryInStore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrInvCriVo/v1}StrInvCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupInventoryInStore", propOrder = {
    "strInvCriVo"
})
public class LookupInventoryInStore {

    @XmlElement(name = "StrInvCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrInvCriVo/v1")
    protected StrInvCriVo strInvCriVo;

    /**
     * Gets the value of the strInvCriVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo}
     *
     */
    public StrInvCriVo getStrInvCriVo() {
        return strInvCriVo;
    }

    /**
     * Sets the value of the strInvCriVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strinvcrivo.v1.StrInvCriVo}
     *
     */
    public void setStrInvCriVo(StrInvCriVo value) {
        this.strInvCriVo = value;
    }

}
