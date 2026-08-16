
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreModVo;


/**
 * <p>Java class for createReversePick complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createReversePick"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpCreModVo/v1}ForpCreModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createReversePick", propOrder = {
    "forpCreModVo"
})
public class CreateReversePick {

    @XmlElement(name = "ForpCreModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpCreModVo/v1")
    protected ForpCreModVo forpCreModVo;

    /**
     * Gets the value of the forpCreModVo property.
     * 
     * @return
     *     possible object is
     *     {@link ForpCreModVo }
     *     
     */
    public ForpCreModVo getForpCreModVo() {
        return forpCreModVo;
    }

    /**
     * Sets the value of the forpCreModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForpCreModVo }
     *     
     */
    public void setForpCreModVo(ForpCreModVo value) {
        this.forpCreModVo = value;
    }

}
