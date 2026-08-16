
package com.oracle.retail.integration.base.bo.strinvdesc.v1;

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
 *         &lt;element name="nonsell_qty_type_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "nonsellQtyTypeId",
    "quantity"
})
@XmlRootElement(name = "StrInvNslQty")
public class StrInvNslQty {

    @XmlElement(name = "nonsell_qty_type_id")
    protected long nonsellQtyTypeId;
    @XmlElement(required = true)
    protected BigDecimal quantity;

    /**
     * Gets the value of the nonsellQtyTypeId property.
     * 
     */
    public long getNonsellQtyTypeId() {
        return nonsellQtyTypeId;
    }

    /**
     * Sets the value of the nonsellQtyTypeId property.
     * 
     */
    public void setNonsellQtyTypeId(long value) {
        this.nonsellQtyTypeId = value;
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
