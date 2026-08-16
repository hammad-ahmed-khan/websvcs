
package com.oracle.retail.rms.integration.services.inventorybackorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;


/**
 * <p>Java class for createInvBackOrdColDesc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createInvBackOrdColDesc">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvBackOrdColDesc/v1}InvBackOrdColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createInvBackOrdColDesc", propOrder = {
    "invBackOrdColDesc"
})
public class CreateInvBackOrdColDesc {

    @XmlElement(name = "InvBackOrdColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/InvBackOrdColDesc/v1")
    protected InvBackOrdColDesc invBackOrdColDesc;

    /**
     * Gets the value of the invBackOrdColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc}
     *
     */
    public InvBackOrdColDesc getInvBackOrdColDesc() {
        return invBackOrdColDesc;
    }

    /**
     * Sets the value of the invBackOrdColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc}
     *
     */
    public void setInvBackOrdColDesc(InvBackOrdColDesc value) {
        this.invBackOrdColDesc = value;
    }

}
