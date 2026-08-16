
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.forpdesc.v1.ForpDesc;


/**
 * <p>Java class for readReversePickDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readReversePickDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpDesc/v1}ForpDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readReversePickDetailResponse", propOrder = {
    "forpDesc"
})
public class ReadReversePickDetailResponse {

    @XmlElement(name = "ForpDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ForpDesc/v1")
    protected ForpDesc forpDesc;

    /**
     * Gets the value of the forpDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.forpdesc.v1.ForpDesc}
     *
     */
    public ForpDesc getForpDesc() {
        return forpDesc;
    }

    /**
     * Sets the value of the forpDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.forpdesc.v1.ForpDesc}
     *
     */
    public void setForpDesc(ForpDesc value) {
        this.forpDesc = value;
    }

}
