
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forphdrcoldesc.v1.ForpHdrColDesc;


/**
 * <p>Java class for lookupReversePickHeadersResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupReversePickHeadersResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpHdrColDesc/v1}ForpHdrColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupReversePickHeadersResponse", propOrder = {
    "forpHdrColDesc"
})
public class LookupReversePickHeadersResponse {

    @XmlElement(name = "ForpHdrColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpHdrColDesc/v1")
    protected ForpHdrColDesc forpHdrColDesc;

    /**
     * Gets the value of the forpHdrColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ForpHdrColDesc }
     *     
     */
    public ForpHdrColDesc getForpHdrColDesc() {
        return forpHdrColDesc;
    }

    /**
     * Sets the value of the forpHdrColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForpHdrColDesc }
     *     
     */
    public void setForpHdrColDesc(ForpHdrColDesc value) {
        this.forpHdrColDesc = value;
    }

}
