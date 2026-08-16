
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;


/**
 * <p>Java class for savePendingTransferRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="savePendingTransferRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfApvModVo/v1}StsTsfApvModVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "savePendingTransferRequest", propOrder = {
    "stsTsfApvModVo"
})
public class SavePendingTransferRequest {

    @XmlElement(name = "StsTsfApvModVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfApvModVo/v1")
    protected StsTsfApvModVo stsTsfApvModVo;

    /**
     * Gets the value of the stsTsfApvModVo property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfApvModVo }
     *     
     */
    public StsTsfApvModVo getStsTsfApvModVo() {
        return stsTsfApvModVo;
    }

    /**
     * Sets the value of the stsTsfApvModVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfApvModVo }
     *     
     */
    public void setStsTsfApvModVo(StsTsfApvModVo value) {
        this.stsTsfApvModVo = value;
    }

}
