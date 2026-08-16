
package com.oracle.retail.integration.base.bo.ststsfshpmodvo.v1;

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
 *         &lt;element name="line_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="transfer_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="added_uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="removed_uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "caseSize",
    "transferQuantity",
    "addedUinCol",
    "removedUinCol"
})
@XmlRootElement(name = "StsTsfShpItmMod")
public class StsTsfShpItmMod {

    @XmlElement(name = "line_id")
    protected Long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "case_size")
    protected BigDecimal caseSize;
    @XmlElement(name = "transfer_quantity", required = true)
    protected BigDecimal transferQuantity;
    @XmlElement(name = "added_uin_col")
    protected List<String> addedUinCol;
    @XmlElement(name = "removed_uin_col")
    protected List<String> removedUinCol;

    /**
     * Gets the value of the lineId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLineId() {
        return lineId;
    }

    /**
     * Sets the value of the lineId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLineId(Long value) {
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
     * Gets the value of the transferQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransferQuantity() {
        return transferQuantity;
    }

    /**
     * Sets the value of the transferQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransferQuantity(BigDecimal value) {
        this.transferQuantity = value;
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

    /**
     * Gets the value of the removedUinCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the removedUinCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemovedUinCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRemovedUinCol() {
        if (removedUinCol == null) {
            removedUinCol = new ArrayList<String>();
        }
        return this.removedUinCol;
    }

}
