
package com.oracle.retail.integration.base.bo.foddesc.v1;

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
 *         &lt;element name="fulfill_order_delivery_line_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="fulfill_order_line_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
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
    "fulfillOrderDeliveryLineId",
    "fulfillOrderLineId",
    "quantity",
    "caseSize",
    "uinCol"
})
@XmlRootElement(name = "FodItm")
public class FodItm {

    @XmlElement(name = "fulfill_order_delivery_line_id")
    protected long fulfillOrderDeliveryLineId;
    @XmlElement(name = "fulfill_order_line_id")
    protected long fulfillOrderLineId;
    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "case_size", required = true)
    protected BigDecimal caseSize;
    @XmlElement(name = "uin_col")
    protected List<String> uinCol;

    /**
     * Gets the value of the fulfillOrderDeliveryLineId property.
     * 
     */
    public long getFulfillOrderDeliveryLineId() {
        return fulfillOrderDeliveryLineId;
    }

    /**
     * Sets the value of the fulfillOrderDeliveryLineId property.
     * 
     */
    public void setFulfillOrderDeliveryLineId(long value) {
        this.fulfillOrderDeliveryLineId = value;
    }

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
     * Gets the value of the caseSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCaseSize() {
        return caseSize;
    }

    /**
     * Sets the value of the caseSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCaseSize(BigDecimal value) {
        this.caseSize = value;
    }

    /**
     * Gets the value of the uinCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uinCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUinCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getUinCol() {
        if (uinCol == null) {
            uinCol = new ArrayList<String>();
        }
        return this.uinCol;
    }

}
