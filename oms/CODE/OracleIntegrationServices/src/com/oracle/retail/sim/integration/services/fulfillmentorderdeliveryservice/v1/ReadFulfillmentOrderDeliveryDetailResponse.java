
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.foddesc.v1.FodDesc;


/**
 * <p>Java class for readFulfillmentOrderDeliveryDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readFulfillmentOrderDeliveryDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodDesc/v1}FodDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readFulfillmentOrderDeliveryDetailResponse", propOrder = {
    "fodDesc"
})
public class ReadFulfillmentOrderDeliveryDetailResponse {

    @XmlElement(name = "FodDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FodDesc/v1")
    protected FodDesc fodDesc;

    /**
     * Gets the value of the fodDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodDesc}
     *
     */
    public FodDesc getFodDesc() {
        return fodDesc;
    }

    /**
     * Sets the value of the fodDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.foddesc.v1.FodDesc}
     *
     */
    public void setFodDesc(FodDesc value) {
        this.fodDesc = value;
    }

}
