
package com.oracle.retail.integration.base.bo.fulfilorddtlref.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ref_item" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cancel_qty_suom" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="standard_uom" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transaction_uom" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "item",
    "refItem",
    "cancelQtySuom",
    "standardUom",
    "transactionUom"
})
@XmlRootElement(name = "FulfilOrdDtlRef")
public class FulfilOrdDtlRef {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "ref_item")
    protected String refItem;
    @XmlElement(name = "cancel_qty_suom", required = true)
    protected BigDecimal cancelQtySuom;
    @XmlElement(name = "standard_uom", required = true)
    protected String standardUom;
    @XmlElement(name = "transaction_uom", required = true)
    protected String transactionUom;

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
     * Gets the value of the cancelQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCancelQtySuom() {
        return cancelQtySuom;
    }

    /**
     * Sets the value of the cancelQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCancelQtySuom(BigDecimal value) {
        this.cancelQtySuom = value;
    }

    /**
     * Gets the value of the standardUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardUom() {
        return standardUom;
    }

    /**
     * Sets the value of the standardUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardUom(String value) {
        this.standardUom = value;
    }

    /**
     * Gets the value of the transactionUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionUom() {
        return transactionUom;
    }

    /**
     * Sets the value of the transactionUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionUom(String value) {
        this.transactionUom = value;
    }

}
