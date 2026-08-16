
package com.oracle.retail.integration.base.bo.invbackorddesc.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="loc_type" type="{http://www.oracle.com/retail/integration/base/bo/InvBackOrdDesc/v1}loc_type"/&gt;
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="channel_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="backorder_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="unit_of_measure" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "locType",
    "location",
    "channelId",
    "backorderQty",
    "unitOfMeasure"
})
@XmlRootElement(name = "InvBackOrdDesc")
public class InvBackOrdDesc {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "loc_type", required = true)
    @XmlSchemaType(name = "string")
    protected LocType locType;
    protected long location;
    @XmlElement(name = "channel_id")
    protected Integer channelId;
    @XmlElement(name = "backorder_qty", required = true)
    protected BigDecimal backorderQty;
    @XmlElement(name = "unit_of_measure", required = true)
    protected String unitOfMeasure;

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
     * Gets the value of the locType property.
     * 
     * @return
     *     possible object is
     *     {@link LocType }
     *     
     */
    public LocType getLocType() {
        return locType;
    }

    /**
     * Sets the value of the locType property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocType }
     *     
     */
    public void setLocType(LocType value) {
        this.locType = value;
    }

    /**
     * Gets the value of the location property.
     * 
     */
    public long getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     */
    public void setLocation(long value) {
        this.location = value;
    }

    /**
     * Gets the value of the channelId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getChannelId() {
        return channelId;
    }

    /**
     * Sets the value of the channelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setChannelId(Integer value) {
        this.channelId = value;
    }

    /**
     * Gets the value of the backorderQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBackorderQty() {
        return backorderQty;
    }

    /**
     * Sets the value of the backorderQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBackorderQty(BigDecimal value) {
        this.backorderQty = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

}
