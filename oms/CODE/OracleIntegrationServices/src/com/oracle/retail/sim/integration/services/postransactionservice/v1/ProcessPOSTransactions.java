
package com.oracle.retail.sim.integration.services.postransactionservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.postrncoldesc.v1.PosTrnColDesc;


/**
 * <p>Java class for processPOSTransactions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="processPOSTransactions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PosTrnColDesc/v1}PosTrnColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "processPOSTransactions", propOrder = {
    "posTrnColDesc"
})
public class ProcessPOSTransactions {

    @XmlElement(name = "PosTrnColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PosTrnColDesc/v1")
    protected PosTrnColDesc posTrnColDesc;

    /**
     * Gets the value of the posTrnColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link PosTrnColDesc }
     *     
     */
    public PosTrnColDesc getPosTrnColDesc() {
        return posTrnColDesc;
    }

    /**
     * Sets the value of the posTrnColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosTrnColDesc }
     *     
     */
    public void setPosTrnColDesc(PosTrnColDesc value) {
        this.posTrnColDesc = value;
    }

}
