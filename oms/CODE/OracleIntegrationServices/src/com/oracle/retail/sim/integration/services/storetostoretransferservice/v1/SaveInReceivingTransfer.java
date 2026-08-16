
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1.StsTsfRcvModVo;


/**
 * <p>Java class for saveInReceivingTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveInReceivingTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfRcvModVo/v1}StsTsfRcvModVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveInReceivingTransfer", propOrder = {
    "stsTsfRcvModVo"
})
public class SaveInReceivingTransfer {

    @XmlElement(name = "StsTsfRcvModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfRcvModVo/v1")
    protected StsTsfRcvModVo stsTsfRcvModVo;

    /**
     * Gets the value of the stsTsfRcvModVo property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1.StsTsfRcvModVo}
     *
     */
    public StsTsfRcvModVo getStsTsfRcvModVo() {
        return stsTsfRcvModVo;
    }

    /**
     * Sets the value of the stsTsfRcvModVo property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1.StsTsfRcvModVo}
     *
     */
    public void setStsTsfRcvModVo(StsTsfRcvModVo value) {
        this.stsTsfRcvModVo = value;
    }

}
