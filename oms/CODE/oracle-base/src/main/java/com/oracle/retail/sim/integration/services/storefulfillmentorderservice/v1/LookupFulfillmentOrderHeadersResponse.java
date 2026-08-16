
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;


/**
 * <p>Java class for lookupFulfillmentOrderHeadersResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupFulfillmentOrderHeadersResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordHdrColDesc/v1}StrFordHdrColDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupFulfillmentOrderHeadersResponse", propOrder = {
    "strFordHdrColDesc"
})
public class LookupFulfillmentOrderHeadersResponse {

    @XmlElement(name = "StrFordHdrColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrFordHdrColDesc/v1")
    protected StrFordHdrColDesc strFordHdrColDesc;

    /**
     * Gets the value of the strFordHdrColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StrFordHdrColDesc }
     *     
     */
    public StrFordHdrColDesc getStrFordHdrColDesc() {
        return strFordHdrColDesc;
    }

    /**
     * Sets the value of the strFordHdrColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrFordHdrColDesc }
     *     
     */
    public void setStrFordHdrColDesc(StrFordHdrColDesc value) {
        this.strFordHdrColDesc = value;
    }

}
