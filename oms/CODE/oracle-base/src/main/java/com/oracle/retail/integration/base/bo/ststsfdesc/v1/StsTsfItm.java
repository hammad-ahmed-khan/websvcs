
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

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
 *         &lt;element name="requested_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="approved_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="transfer_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="received_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="damaged_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
    "requestedQuantity",
    "approvedQuantity",
    "transferQuantity",
    "receivedQuantity",
    "damagedQuantity",
    "caseSize",
    "uinCol"
})
@XmlRootElement(name = "StsTsfItm")
public class StsTsfItm {

    @XmlElement(name = "line_id")
    protected long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "requested_quantity")
    protected BigDecimal requestedQuantity;
    @XmlElement(name = "approved_quantity")
    protected BigDecimal approvedQuantity;
    @XmlElement(name = "transfer_quantity")
    protected BigDecimal transferQuantity;
    @XmlElement(name = "received_quantity")
    protected BigDecimal receivedQuantity;
    @XmlElement(name = "damaged_quantity")
    protected BigDecimal damagedQuantity;
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
     * Gets the value of the requestedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRequestedQuantity() {
        return requestedQuantity;
    }

    /**
     * Sets the value of the requestedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRequestedQuantity(BigDecimal value) {
        this.requestedQuantity = value;
    }

    /**
     * Gets the value of the approvedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getApprovedQuantity() {
        return approvedQuantity;
    }

    /**
     * Sets the value of the approvedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setApprovedQuantity(BigDecimal value) {
        this.approvedQuantity = value;
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
     * Gets the value of the receivedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    /**
     * Sets the value of the receivedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReceivedQuantity(BigDecimal value) {
        this.receivedQuantity = value;
    }

    /**
     * Gets the value of the damagedQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDamagedQuantity() {
        return damagedQuantity;
    }

    /**
     * Sets the value of the damagedQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDamagedQuantity(BigDecimal value) {
        this.damagedQuantity = value;
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
