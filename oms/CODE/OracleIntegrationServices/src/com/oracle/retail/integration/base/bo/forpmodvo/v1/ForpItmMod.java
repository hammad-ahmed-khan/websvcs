
package com.oracle.retail.integration.base.bo.forpmodvo.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="fulfillment_order_line_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
    "fulfillmentOrderLineId",
    "quantity"
})
@XmlRootElement(name = "ForpItmMod")
public class ForpItmMod {

    @XmlElement(name = "fulfillment_order_line_id")
    protected long fulfillmentOrderLineId;
    @XmlElement(required = true)
    protected BigDecimal quantity;

    /**
     * Gets the value of the fulfillmentOrderLineId property.
     * 
     */
    public long getFulfillmentOrderLineId() {
        return fulfillmentOrderLineId;
    }

    /**
     * Sets the value of the fulfillmentOrderLineId property.
     * 
     */
    public void setFulfillmentOrderLineId(long value) {
        this.fulfillmentOrderLineId = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

}
