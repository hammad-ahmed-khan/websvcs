
package com.oracle.retail.integration.base.bo.ststsfreqmodvo.v1;

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
 *         &lt;element name="line_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="requested_quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
    "requestedQuantity"
})
@XmlRootElement(name = "StsTsfReqItmMod")
public class StsTsfReqItmMod {

    @XmlElement(name = "line_id")
    protected Long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "case_size")
    protected BigDecimal caseSize;
    @XmlElement(name = "requested_quantity", required = true)
    protected BigDecimal requestedQuantity;

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

}
