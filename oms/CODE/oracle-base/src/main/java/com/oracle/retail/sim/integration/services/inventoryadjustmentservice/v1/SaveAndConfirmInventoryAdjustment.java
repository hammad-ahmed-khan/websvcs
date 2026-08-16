
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;


/**
 * <p>Java class for saveAndConfirmInventoryAdjustment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveAndConfirmInventoryAdjustment"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrAdjModVo/v1}StrAdjModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveAndConfirmInventoryAdjustment", propOrder = {
    "strAdjModVo"
})
public class SaveAndConfirmInventoryAdjustment {

    @XmlElement(name = "StrAdjModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StrAdjModVo/v1")
    protected StrAdjModVo strAdjModVo;

    /**
     * Gets the value of the strAdjModVo property.
     * 
     * @return
     *     possible object is
     *     {@link StrAdjModVo }
     *     
     */
    public StrAdjModVo getStrAdjModVo() {
        return strAdjModVo;
    }

    /**
     * Sets the value of the strAdjModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link StrAdjModVo }
     *     
     */
    public void setStrAdjModVo(StrAdjModVo value) {
        this.strAdjModVo = value;
    }

}
