
package com.oracle.retail.integration.base.bo.stradjdesc.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="line_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="reason_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "reasonId",
    "quantity",
    "caseSize",
    "uinCol"
})
@XmlRootElement(name = "StrAdjItm")
public class StrAdjItm {

    @XmlElement(name = "line_id")
    protected long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "reason_id")
    protected long reasonId;
    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "case_size", required = true)
    protected BigDecimal caseSize;
    @XmlElement(name = "uin_col")
    protected List<String> uinCol;

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
     * Gets the value of the reasonId property.
     * 
     */
    public long getReasonId() {
        return reasonId;
    }

    /**
     * Sets the value of the reasonId property.
     * 
     */
    public void setReasonId(long value) {
        this.reasonId = value;
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
