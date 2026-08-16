
package com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1;

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
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="received_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="damaged_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="received_uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="damaged_uin_col" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "caseSize",
    "receivedQuantity",
    "damagedQuantity",
    "receivedUinCol",
    "damagedUinCol",
    "removedUinCol"
})
@XmlRootElement(name = "StsTsfRcvItmMod")
public class StsTsfRcvItmMod {

    @XmlElement(name = "line_id")
    protected long lineId;
    @XmlElement(name = "case_size", required = true)
    protected BigDecimal caseSize;
    @XmlElement(name = "received_quantity")
    protected BigDecimal receivedQuantity;
    @XmlElement(name = "damaged_quantity")
    protected BigDecimal damagedQuantity;
    @XmlElement(name = "received_uin_col")
    protected List<String> receivedUinCol;
    @XmlElement(name = "damaged_uin_col")
    protected List<String> damagedUinCol;
    @XmlElement(name = "removed_uin_col")
    protected List<String> removedUinCol;

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
     * Gets the value of the receivedUinCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receivedUinCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceivedUinCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getReceivedUinCol() {
        if (receivedUinCol == null) {
            receivedUinCol = new ArrayList<String>();
        }
        return this.receivedUinCol;
    }

    /**
     * Gets the value of the damagedUinCol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the damagedUinCol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDamagedUinCol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDamagedUinCol() {
        if (damagedUinCol == null) {
            damagedUinCol = new ArrayList<String>();
        }
        return this.damagedUinCol;
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
