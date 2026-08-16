
package com.oracle.retail.integration.base.bo.forpdesc.v1;

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
 *         &lt;element name="line_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="fulfillment_order_line_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="standard_uom" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="suggested_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="unpicked_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
    "lineId",
    "itemId",
    "fulfillmentOrderLineId",
    "standardUom",
    "suggestedQuantity",
    "unpickedQuantity",
    "caseSize"
})
@XmlRootElement(name = "ForpItm")
public class ForpItm {

    @XmlElement(name = "line_id")
    protected long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "fulfillment_order_line_id")
    protected long fulfillmentOrderLineId;
    @XmlElement(name = "standard_uom", required = true)
    protected String standardUom;
    @XmlElement(name = "suggested_quantity")
    protected BigDecimal suggestedQuantity;
    @XmlElement(name = "unpicked_quantity", required = true)
    protected BigDecimal unpickedQuantity;
    @XmlElement(name = "case_size", required = true)
    protected BigDecimal caseSize;

    /**
     * Gets the value of the lineId property.
     * 
     */
    public long getLineId() {
        return lineId;
    }

    /**
     * Sets the value of the lineId property.
     * 
     */
    public void setLineId(long value) {
        this.lineId = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
    }

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
     * Gets the value of the suggestedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSuggestedQuantity() {
        return suggestedQuantity;
    }

    /**
     * Sets the value of the suggestedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSuggestedQuantity(BigDecimal value) {
        this.suggestedQuantity = value;
    }

    /**
     * Gets the value of the unpickedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnpickedQuantity() {
        return unpickedQuantity;
    }

    /**
     * Sets the value of the unpickedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnpickedQuantity(BigDecimal value) {
        this.unpickedQuantity = value;
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

}
