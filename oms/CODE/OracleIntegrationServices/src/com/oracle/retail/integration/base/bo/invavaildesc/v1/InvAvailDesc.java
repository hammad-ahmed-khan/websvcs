
package com.oracle.retail.integration.base.bo.invavaildesc.v1;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="loc_type" type="{http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1}loc_type"/>
 *         &lt;element name="channel_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="available_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unit_of_measure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="available_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="pack_calculate_ind" type="{http://www.oracle.com/retail/integration/base/bo/InvAvailDesc/v1}pack_calculate_ind" minOccurs="0"/>
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
    "location",
    "locType",
    "channelId",
    "availableQty",
    "unitOfMeasure",
    "availableDate",
    "packCalculateInd"
})
@XmlRootElement(name = "InvAvailDesc")
public class InvAvailDesc {

    @XmlElement(required = true)
    protected String item;
    protected long location;
    @XmlElement(name = "loc_type", required = true)
    protected LocType locType;
    @XmlElement(name = "channel_id")
    protected Integer channelId;
    @XmlElement(name = "available_qty", required = true)
    protected BigDecimal availableQty;
    @XmlElement(name = "unit_of_measure", required = true)
    protected String unitOfMeasure;
    @XmlElement(name = "available_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar availableDate;
    @XmlElement(name = "pack_calculate_ind")
    protected PackCalculateInd packCalculateInd;

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
     * Gets the value of the locType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invavaildesc.v1.LocType}
     *
     */
    public LocType getLocType() {
        return locType;
    }

    /**
     * Sets the value of the locType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invavaildesc.v1.LocType}
     *
     */
    public void setLocType(LocType value) {
        this.locType = value;
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
     * Gets the value of the availableQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvailableQty() {
        return availableQty;
    }

    /**
     * Sets the value of the availableQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvailableQty(BigDecimal value) {
        this.availableQty = value;
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

    /**
     * Gets the value of the availableDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAvailableDate() {
        return availableDate;
    }

    /**
     * Sets the value of the availableDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAvailableDate(XMLGregorianCalendar value) {
        this.availableDate = value;
    }

    /**
     * Gets the value of the packCalculateInd property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invavaildesc.v1.PackCalculateInd}
     *
     */
    public PackCalculateInd getPackCalculateInd() {
        return packCalculateInd;
    }

    /**
     * Sets the value of the packCalculateInd property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invavaildesc.v1.PackCalculateInd}
     *
     */
    public void setPackCalculateInd(PackCalculateInd value) {
        this.packCalculateInd = value;
    }

}
