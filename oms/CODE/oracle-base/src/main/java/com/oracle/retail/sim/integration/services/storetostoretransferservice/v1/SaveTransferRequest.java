
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1.StsTsfReqModVo;


/**
 * <p>Java class for saveTransferRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="saveTransferRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfReqModVo/v1}StsTsfReqModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "saveTransferRequest", propOrder = {
    "stsTsfReqModVo"
})
public class SaveTransferRequest {

    @XmlElement(name = "StsTsfReqModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfReqModVo/v1")
    protected StsTsfReqModVo stsTsfReqModVo;

    /**
     * Gets the value of the stsTsfReqModVo property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfReqModVo }
     *     
     */
    public StsTsfReqModVo getStsTsfReqModVo() {
        return stsTsfReqModVo;
    }

    /**
     * Sets the value of the stsTsfReqModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfReqModVo }
     *     
     */
    public void setStsTsfReqModVo(StsTsfReqModVo value) {
        this.stsTsfReqModVo = value;
    }

}
