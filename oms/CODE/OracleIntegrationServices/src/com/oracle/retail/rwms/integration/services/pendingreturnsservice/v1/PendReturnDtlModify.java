
package com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDesc;


/**
 * <p>Java class for pendReturnDtlModify complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pendReturnDtlModify">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PendRtrnDesc/v1}PendRtrnDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pendReturnDtlModify", propOrder = {
    "pendRtrnDesc"
})
public class PendReturnDtlModify {

    @XmlElement(name = "PendRtrnDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/PendRtrnDesc/v1")
    protected PendRtrnDesc pendRtrnDesc;

    /**
     * Gets the value of the pendRtrnDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDesc}
     *
     */
    public PendRtrnDesc getPendRtrnDesc() {
        return pendRtrnDesc;
    }

    /**
     * Sets the value of the pendRtrnDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDesc}
     *
     */
    public void setPendRtrnDesc(PendRtrnDesc value) {
        this.pendRtrnDesc = value;
    }

}
