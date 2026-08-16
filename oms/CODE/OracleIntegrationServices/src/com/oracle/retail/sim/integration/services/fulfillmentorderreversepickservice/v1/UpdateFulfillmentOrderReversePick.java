
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
 * &lt;complexType name="updateFulfillmentOrderReversePick">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpModVo/v1}ForpModVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
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
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.forpmodvo.v1.ForpModVo}
     *
     */
    public ForpModVo getForpModVo() {
        return forpModVo;
    }

    /**
     * Sets the value of the forpModVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.forpmodvo.v1.ForpModVo}
     *
     */
    public void setForpModVo(ForpModVo value) {
        this.forpModVo = value;
    }

}
