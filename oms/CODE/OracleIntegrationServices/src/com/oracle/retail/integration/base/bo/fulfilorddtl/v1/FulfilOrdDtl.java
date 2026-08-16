
package com.oracle.retail.integration.base.bo.fulfilorddtl.v1;

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
 *         &lt;element name="order_qty_suom" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="standard_uom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transaction_uom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="substitute_ind" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="unit_retail" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="retail_curr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "orderQtySuom",
    "standardUom",
    "transactionUom",
    "substituteInd",
    "unitRetail",
    "retailCurr",
    "comments"
})
@XmlRootElement(name = "FulfilOrdDtl")
public class FulfilOrdDtl {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "ref_item")
    protected String refItem;
    @XmlElement(name = "order_qty_suom", required = true)
    protected BigDecimal orderQtySuom;
    @XmlElement(name = "standard_uom", required = true)
    protected String standardUom;
    @XmlElement(name = "transaction_uom", required = true)
    protected String transactionUom;
    @XmlElement(name = "substitute_ind", required = true)
    protected String substituteInd;
    @XmlElement(name = "unit_retail")
    protected BigDecimal unitRetail;
    @XmlElement(name = "retail_curr")
    protected String retailCurr;
    protected String comments;

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
     * Gets the value of the orderQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrderQtySuom() {
        return orderQtySuom;
    }

    /**
     * Sets the value of the orderQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrderQtySuom(BigDecimal value) {
        this.orderQtySuom = value;
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

    /**
     * Gets the value of the substituteInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstituteInd() {
        return substituteInd;
    }

    /**
     * Sets the value of the substituteInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstituteInd(String value) {
        this.substituteInd = value;
    }

    /**
     * Gets the value of the unitRetail property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitRetail() {
        return unitRetail;
    }

    /**
     * Sets the value of the unitRetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitRetail(BigDecimal value) {
        this.unitRetail = value;
    }

    /**
     * Gets the value of the retailCurr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetailCurr() {
        return retailCurr;
    }

    /**
     * Sets the value of the retailCurr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetailCurr(String value) {
        this.retailCurr = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

}
