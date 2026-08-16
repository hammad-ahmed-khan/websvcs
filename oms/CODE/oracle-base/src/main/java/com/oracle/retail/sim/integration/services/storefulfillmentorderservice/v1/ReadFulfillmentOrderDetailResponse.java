
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;


/**
 * <p>Java class for readFulfillmentOrderDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readFulfillmentOrderDetailResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1}StrFordDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readFulfillmentOrderDetailResponse", propOrder = {
    "strFordDesc"
})
public class ReadFulfillmentOrderDetailResponse {

    @XmlElement(name = "StrFordDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StrFordDesc/v1")
    protected StrFordDesc strFordDesc;

    /**
     * Gets the value of the strFordDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StrFordDesc }
     *     
     */
    public StrFordDesc getStrFordDesc() {
        return strFordDesc;
    }

    /**
     * Sets the value of the strFordDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrFordDesc }
     *     
     */
    public void setStrFordDesc(StrFordDesc value) {
        this.strFordDesc = value;
    }

}
