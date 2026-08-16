
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
 * &lt;complexType name="readTransferDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}StsTsfDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc}
     *
     */
    public StsTsfDesc getStsTsfDesc() {
        return stsTsfDesc;
    }

    /**
     * Sets the value of the stsTsfDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfdesc.v1.StsTsfDesc}
     *
     */
    public void setStsTsfDesc(StsTsfDesc value) {
        this.stsTsfDesc = value;
    }

}
