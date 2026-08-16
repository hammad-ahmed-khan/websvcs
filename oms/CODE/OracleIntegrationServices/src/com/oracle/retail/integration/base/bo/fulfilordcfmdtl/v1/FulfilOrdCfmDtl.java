
package com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1;

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
 *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ref_item" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="confirm_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="confirm_qty_uom" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "item",
    "refItem",
    "confirmQty",
    "confirmQtyUom"
})
@XmlRootElement(name = "FulfilOrdCfmDtl")
public class FulfilOrdCfmDtl {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "ref_item")
    protected String refItem;
    @XmlElement(name = "confirm_qty", required = true)
    protected BigDecimal confirmQty;
    @XmlElement(name = "confirm_qty_uom", required = true)
    protected String confirmQtyUom;

    /**
     * Gets the value of the item property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the refItem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefItem() {
        return refItem;
    }

    /**
     * Sets the value of the refItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefItem(String value) {
        this.refItem = value;
    }

    /**
     * Gets the value of the confirmQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getConfirmQty() {
        return confirmQty;
    }

    /**
     * Sets the value of the confirmQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setConfirmQty(BigDecimal value) {
        this.confirmQty = value;
    }

    /**
     * Gets the value of the confirmQtyUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConfirmQtyUom() {
        return confirmQtyUom;
    }

    /**
     * Sets the value of the confirmQtyUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConfirmQtyUom(String value) {
        this.confirmQtyUom = value;
    }

}
