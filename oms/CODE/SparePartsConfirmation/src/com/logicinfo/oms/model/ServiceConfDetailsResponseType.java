
package com.logicinfo.oms.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceConfDetailsResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceConfDetailsResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SequenceId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OMSServiceId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ItemID">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ReasonCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MsgCode">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MsgDesc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceConfDetailsResponseType", propOrder = {
    "sequenceId",
    "omsServiceId",
    "itemID",
    "reasonCode",
    "msgCode",
    "msgDesc"
})
public class ServiceConfDetailsResponseType {

    @XmlElement(name = "SequenceId", required = true, nillable = true)
    protected String sequenceId;
    @XmlElement(name = "OMSServiceId", required = true, type = Long.class, nillable = true)
    protected Long omsServiceId;
    @XmlElement(name = "ItemID", required = true, nillable = true)
    protected String itemID;
    @XmlElement(name = "ReasonCode", required = true, nillable = true)
    protected String reasonCode;
    @XmlElement(name = "MsgCode", required = true, nillable = true)
    protected String msgCode;
    @XmlElement(name = "MsgDesc", required = true, nillable = true)
    protected String msgDesc;

    /**
     * Gets the value of the sequenceId property.
     *
     */
    public String getSequenceId() {
        return sequenceId;
    }

    /**
     * Sets the value of the sequenceId property.
     * 
     */
    public void setSequenceId(String value) {
        this.sequenceId = value;
    }

    /**
     * Gets the value of the omsServiceId property.
     *
     */
    public Long getOMSServiceId() {
        return omsServiceId;
    }

    /**
     * Sets the value of the omsServiceId property.
     * 
     */
    public void setOMSServiceId(Long value) {
        this.omsServiceId = value;
    }

    /**
     * Gets the value of the itemID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemID() {
        return itemID;
    }

    /**
     * Sets the value of the itemID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemID(String value) {
        this.itemID = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the msgCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgCode() {
        return msgCode;
    }

    /**
     * Sets the value of the msgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgCode(String value) {
        this.msgCode = value;
    }

    /**
     * Gets the value of the msgDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgDesc() {
        return msgDesc;
    }

    /**
     * Sets the value of the msgDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgDesc(String value) {
        this.msgDesc = value;
    }

}
