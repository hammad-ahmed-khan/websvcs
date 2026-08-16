
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;


/**
 * <p>Java class for createFulfillmentOrderDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFulfillmentOrderDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdColDesc/v1}FulfilOrdColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createFulfillmentOrderDetail", propOrder = {
    "fulfilOrdColDesc"
})
public class CreateFulfillmentOrderDetail {

    @XmlElement(name = "FulfilOrdColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdColDesc/v1")
    protected FulfilOrdColDesc fulfilOrdColDesc;

    /**
     * Gets the value of the fulfilOrdColDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc}
     *
     */
    public FulfilOrdColDesc getFulfilOrdColDesc() {
        return fulfilOrdColDesc;
    }

    /**
     * Sets the value of the fulfilOrdColDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc}
     *
     */
    public void setFulfilOrdColDesc(FulfilOrdColDesc value) {
        this.fulfilOrdColDesc = value;
    }

}
