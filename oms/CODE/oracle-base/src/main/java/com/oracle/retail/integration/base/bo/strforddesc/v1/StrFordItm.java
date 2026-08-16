
package com.oracle.retail.integration.base.bo.strforddesc.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="substitute_item_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="preferred_uom" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="standard_uom" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="order_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="picked_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="delivered_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="canceled_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="remaining_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="reserved_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="allow_substitution" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="last_update_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "description",
    "substituteItemId",
    "preferredUom",
    "standardUom",
    "orderQty",
    "pickedQty",
    "deliveredQty",
    "canceledQty",
    "remainingQty",
    "reservedQty",
    "allowSubstitution",
    "createDate",
    "lastUpdateDate",
    "comments"
})
@XmlRootElement(name = "StrFordItm")
public class StrFordItm {

    @XmlElement(name = "line_id")
    protected long lineId;
    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    protected String description;
    @XmlElement(name = "substitute_item_id")
    protected String substituteItemId;
    @XmlElement(name = "preferred_uom", required = true)
    protected String preferredUom;
    @XmlElement(name = "standard_uom", required = true)
    protected String standardUom;
    @XmlElement(name = "order_qty", required = true)
    protected BigDecimal orderQty;
    @XmlElement(name = "picked_qty", required = true)
    protected BigDecimal pickedQty;
    @XmlElement(name = "delivered_qty", required = true)
    protected BigDecimal deliveredQty;
    @XmlElement(name = "canceled_qty", required = true)
    protected BigDecimal canceledQty;
    @XmlElement(name = "remaining_qty", required = true)
    protected BigDecimal remainingQty;
    @XmlElement(name = "reserved_qty", required = true)
    protected BigDecimal reservedQty;
    @XmlElement(name = "allow_substitution")
    protected boolean allowSubstitution;
    @XmlElement(name = "create_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDate;
    @XmlElement(name = "last_update_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdateDate;
    protected String comments;

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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the substituteItemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubstituteItemId() {
        return substituteItemId;
    }

    /**
     * Sets the value of the substituteItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubstituteItemId(String value) {
        this.substituteItemId = value;
    }

    /**
     * Gets the value of the preferredUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreferredUom() {
        return preferredUom;
    }

    /**
     * Sets the value of the preferredUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreferredUom(String value) {
        this.preferredUom = value;
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
     * Gets the value of the orderQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrderQty() {
        return orderQty;
    }

    /**
     * Sets the value of the orderQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrderQty(BigDecimal value) {
        this.orderQty = value;
    }

    /**
     * Gets the value of the pickedQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPickedQty() {
        return pickedQty;
    }

    /**
     * Sets the value of the pickedQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPickedQty(BigDecimal value) {
        this.pickedQty = value;
    }

    /**
     * Gets the value of the deliveredQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDeliveredQty() {
        return deliveredQty;
    }

    /**
     * Sets the value of the deliveredQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDeliveredQty(BigDecimal value) {
        this.deliveredQty = value;
    }

    /**
     * Gets the value of the canceledQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCanceledQty() {
        return canceledQty;
    }

    /**
     * Sets the value of the canceledQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCanceledQty(BigDecimal value) {
        this.canceledQty = value;
    }

    /**
     * Gets the value of the remainingQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemainingQty() {
        return remainingQty;
    }

    /**
     * Sets the value of the remainingQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemainingQty(BigDecimal value) {
        this.remainingQty = value;
    }

    /**
     * Gets the value of the reservedQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReservedQty() {
        return reservedQty;
    }

    /**
     * Sets the value of the reservedQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReservedQty(BigDecimal value) {
        this.reservedQty = value;
    }

    /**
     * Gets the value of the allowSubstitution property.
     * 
     */
    public boolean isAllowSubstitution() {
        return allowSubstitution;
    }

    /**
     * Sets the value of the allowSubstitution property.
     * 
     */
    public void setAllowSubstitution(boolean value) {
        this.allowSubstitution = value;
    }

    /**
     * Gets the value of the createDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDate() {
        return createDate;
    }

    /**
     * Sets the value of the createDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDate(XMLGregorianCalendar value) {
        this.createDate = value;
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdateDate(XMLGregorianCalendar value) {
        this.lastUpdateDate = value;
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
