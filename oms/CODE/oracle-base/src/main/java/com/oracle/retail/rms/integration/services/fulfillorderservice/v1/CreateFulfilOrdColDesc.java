
package com.oracle.retail.rms.integration.services.fulfillorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;


/**
 * <p>Java class for createFulfilOrdColDesc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFulfilOrdColDesc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdColDesc/v1}FulfilOrdColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createFulfilOrdColDesc", propOrder = {
    "fulfilOrdColDesc"
})
public class CreateFulfilOrdColDesc {

    @XmlElement(name = "FulfilOrdColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdColDesc/v1")
    protected FulfilOrdColDesc fulfilOrdColDesc;

    /**
     * Gets the value of the fulfilOrdColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link FulfilOrdColDesc }
     *     
     */
    public FulfilOrdColDesc getFulfilOrdColDesc() {
        return fulfilOrdColDesc;
    }

    /**
     * Sets the value of the fulfilOrdColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link FulfilOrdColDesc }
     *     
     */
    public void setFulfilOrdColDesc(FulfilOrdColDesc value) {
        this.fulfilOrdColDesc = value;
    }

}
