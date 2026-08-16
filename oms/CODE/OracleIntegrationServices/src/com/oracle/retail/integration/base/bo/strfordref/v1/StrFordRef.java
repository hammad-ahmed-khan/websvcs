
package com.oracle.retail.integration.base.bo.strfordref.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="int_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "intFulfillmentOrderId"
})
@XmlRootElement(name = "StrFordRef")
public class StrFordRef {

    @XmlElement(name = "int_fulfillment_order_id")
    protected long intFulfillmentOrderId;

    /**
     * Gets the value of the intFulfillmentOrderId property.
     * 
     */
    public long getIntFulfillmentOrderId() {
        return intFulfillmentOrderId;
    }

    /**
     * Sets the value of the intFulfillmentOrderId property.
     * 
     */
    public void setIntFulfillmentOrderId(long value) {
        this.intFulfillmentOrderId = value;
    }

}
