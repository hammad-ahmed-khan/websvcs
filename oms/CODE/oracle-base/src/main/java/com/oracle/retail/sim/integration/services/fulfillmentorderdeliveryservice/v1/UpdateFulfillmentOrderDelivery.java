
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.fodmodvo.v1.FodModVo;


/**
 * <p>Java class for updateFulfillmentOrderDelivery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateFulfillmentOrderDelivery"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodModVo/v1}FodModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateFulfillmentOrderDelivery", propOrder = {
    "fodModVo"
})
public class UpdateFulfillmentOrderDelivery {

    @XmlElement(name = "FodModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/FodModVo/v1")
    protected FodModVo fodModVo;

    /**
     * Gets the value of the fodModVo property.
     * 
     * @return
     *     possible object is
     *     {@link FodModVo }
     *     
     */
    public FodModVo getFodModVo() {
        return fodModVo;
    }

    /**
     * Sets the value of the fodModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link FodModVo }
     *     
     */
    public void setFodModVo(FodModVo value) {
        this.fodModVo = value;
    }

}
