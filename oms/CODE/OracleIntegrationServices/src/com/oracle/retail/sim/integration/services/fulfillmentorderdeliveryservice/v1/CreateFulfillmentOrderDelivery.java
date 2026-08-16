
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo;


/**
 * <p>Java class for createFulfillmentOrderDelivery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createFulfillmentOrderDelivery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodCreModVo/v1}FodCreModVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createFulfillmentOrderDelivery", propOrder = {
    "fodCreModVo"
})
public class CreateFulfillmentOrderDelivery {

    @XmlElement(name = "FodCreModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/FodCreModVo/v1")
    protected FodCreModVo fodCreModVo;

    /**
     * Gets the value of the fodCreModVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo}
     *
     */
    public FodCreModVo getFodCreModVo() {
        return fodCreModVo;
    }

    /**
     * Sets the value of the fodCreModVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fodcremodvo.v1.FodCreModVo}
     *
     */
    public void setFodCreModVo(FodCreModVo value) {
        this.fodCreModVo = value;
    }

}
