
package com.oracle.retail.integration.base.bo.fodcremodvo.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="fulfill_order_line_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="added_uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
    "fulfillOrderLineId",
    "quantity",
    "addedUinCol"
})
@XmlRootElement(name = "FodCreItmMod")
public class FodCreItmMod {

    @XmlElement(name = "fulfill_order_line_id")
    protected long fulfillOrderLineId;
    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "added_uin_col")
    protected List<String> addedUinCol;

    /**
     * Gets the value of the fulfillOrderLineId property.
     * 
     */
    public long getFulfillOrderLineId() {
        return fulfillOrderLineId;
    }

    /**
     * Sets the value of the fulfillOrderLineId property.
     * 
     */
    public void setFulfillOrderLineId(long value) {
        this.fulfillOrderLineId = value;
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

    /**
     * Gets the value of the addedUinCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addedUinCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddedUinCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAddedUinCol() {
        if (addedUinCol == null) {
            addedUinCol = new ArrayList<String>();
        }
        return this.addedUinCol;
    }

}
