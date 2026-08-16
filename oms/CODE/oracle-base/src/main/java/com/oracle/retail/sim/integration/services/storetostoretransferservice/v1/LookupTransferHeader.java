
package com.oracle.retail.sim.integration.services.storetostoretransferservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfHdrCriVo;


/**
 * <p>Java class for lookupTransferHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lookupTransferHeader"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfHdrCriVo/v1}StsTsfHdrCriVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lookupTransferHeader", propOrder = {
    "stsTsfHdrCriVo"
})
public class LookupTransferHeader {

    @XmlElement(name = "StsTsfHdrCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/StsTsfHdrCriVo/v1")
    protected StsTsfHdrCriVo stsTsfHdrCriVo;

    /**
     * Gets the value of the stsTsfHdrCriVo property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfHdrCriVo }
     *     
     */
    public StsTsfHdrCriVo getStsTsfHdrCriVo() {
        return stsTsfHdrCriVo;
    }

    /**
     * Sets the value of the stsTsfHdrCriVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfHdrCriVo }
     *     
     */
    public void setStsTsfHdrCriVo(StsTsfHdrCriVo value) {
        this.stsTsfHdrCriVo = value;
    }

}
