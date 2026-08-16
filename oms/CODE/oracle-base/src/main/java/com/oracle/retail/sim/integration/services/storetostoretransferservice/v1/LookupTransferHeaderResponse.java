
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfhdrcoldesc.v1.StsTsfHdrColDesc;


/**
 * <p>Java class for lookupTransferHeaderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupTransferHeaderResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfHdrColDesc/v1}StsTsfHdrColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupTransferHeaderResponse", propOrder = {
    "stsTsfHdrColDesc"
})
public class LookupTransferHeaderResponse {

    @XmlElement(name = "StsTsfHdrColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfHdrColDesc/v1")
    protected StsTsfHdrColDesc stsTsfHdrColDesc;

    /**
     * Gets the value of the stsTsfHdrColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfHdrColDesc }
     *     
     */
    public StsTsfHdrColDesc getStsTsfHdrColDesc() {
        return stsTsfHdrColDesc;
    }

    /**
     * Sets the value of the stsTsfHdrColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfHdrColDesc }
     *     
     */
    public void setStsTsfHdrColDesc(StsTsfHdrColDesc value) {
        this.stsTsfHdrColDesc = value;
    }

}
