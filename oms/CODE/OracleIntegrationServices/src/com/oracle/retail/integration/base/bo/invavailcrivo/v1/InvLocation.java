
package com.oracle.retail.integration.base.bo.invavailcrivo.v1;

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
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="loc_type" type="{http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1}loc_type"/>
 *         &lt;element name="channel_id" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "location",
    "locType",
    "channelId"
})
@XmlRootElement(name = "InvLocation")
public class InvLocation {

    protected long location;
    @XmlElement(name = "loc_type", required = true)
    protected LocType locType;
    @XmlElement(name = "channel_id")
    protected Integer channelId;

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
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.LocType}
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
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.LocType}
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

}
