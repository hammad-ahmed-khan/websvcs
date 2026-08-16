
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc;


/**
 * <p>Java class for readTransferDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readTransferDetailResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}StsTsfDesc" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readTransferDetailResponse", propOrder = {
    "stsTsfDesc"
})
public class ReadTransferDetailResponse {

    @XmlElement(name = "StsTsfDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1")
    protected StsTsfDesc stsTsfDesc;

    /**
     * Gets the value of the stsTsfDesc property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfDesc }
     *     
     */
    public StsTsfDesc getStsTsfDesc() {
        return stsTsfDesc;
    }

    /**
     * Sets the value of the stsTsfDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfDesc }
     *     
     */
    public void setStsTsfDesc(StsTsfDesc value) {
        this.stsTsfDesc = value;
    }

}
