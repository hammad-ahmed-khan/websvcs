
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;


/**
 * <p>Java class for readFulfillmentOrderDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="readFulfillmentOrderDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrFordRef/v1}StrFordRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "readFulfillmentOrderDetail", propOrder = {
    "strFordRef"
})
public class ReadFulfillmentOrderDetail {

    @XmlElement(name = "StrFordRef", namespace = "http://www.oracle.com/retail/integration/base/bo/StrFordRef/v1")
    protected StrFordRef strFordRef;

    /**
     * Gets the value of the strFordRef property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef}
     *
     */
    public StrFordRef getStrFordRef() {
        return strFordRef;
    }

    /**
     * Sets the value of the strFordRef property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef}
     *
     */
    public void setStrFordRef(StrFordRef value) {
        this.strFordRef = value;
    }

}
