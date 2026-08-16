
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forpmodvo.v1.ForpModVo;


/**
 * <p>Java class for updateFulfillmentOrderReversePick complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateFulfillmentOrderReversePick"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpModVo/v1}ForpModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateFulfillmentOrderReversePick", propOrder = {
    "forpModVo"
})
public class UpdateFulfillmentOrderReversePick {

    @XmlElement(name = "ForpModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpModVo/v1")
    protected ForpModVo forpModVo;

    /**
     * Gets the value of the forpModVo property.
     * 
     * @return
     *     possible object is
     *     {@link ForpModVo }
     *     
     */
    public ForpModVo getForpModVo() {
        return forpModVo;
    }

    /**
     * Sets the value of the forpModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForpModVo }
     *     
     */
    public void setForpModVo(ForpModVo value) {
        this.forpModVo = value;
    }

}
