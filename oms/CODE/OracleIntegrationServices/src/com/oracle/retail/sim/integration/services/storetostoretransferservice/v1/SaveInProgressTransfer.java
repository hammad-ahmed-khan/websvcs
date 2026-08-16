
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfShpModVo;


/**
 * <p>Java class for saveInProgressTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveInProgressTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfShpModVo/v1}StsTsfShpModVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveInProgressTransfer", propOrder = {
    "stsTsfShpModVo"
})
public class SaveInProgressTransfer {

    @XmlElement(name = "StsTsfShpModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfShpModVo/v1")
    protected StsTsfShpModVo stsTsfShpModVo;

    /**
     * Gets the value of the stsTsfShpModVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfShpModVo}
     *
     */
    public StsTsfShpModVo getStsTsfShpModVo() {
        return stsTsfShpModVo;
    }

    /**
     * Sets the value of the stsTsfShpModVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1.StsTsfShpModVo}
     *
     */
    public void setStsTsfShpModVo(StsTsfShpModVo value) {
        this.stsTsfShpModVo = value;
    }

}
