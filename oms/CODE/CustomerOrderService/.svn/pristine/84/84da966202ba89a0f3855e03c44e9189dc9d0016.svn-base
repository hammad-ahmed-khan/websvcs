
package com.logicinfo.oms.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMADeleteDetailResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMADeleteDetailResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="message_code">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="message_desc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="200"/>
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
@XmlType(name = "RMADeleteDetailResponse", propOrder = {
    "lineNo",
    "item",
    "messageCode",
    "messageDesc"
})
public class RMADeleteDetailResponse {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "message_code", required = true, nillable = true)
    protected String messageCode;
    @XmlElement(name = "message_desc", required = true, nillable = true)
    protected String messageDesc;
    @XmlElement(name = "line_no")
    protected Long lineNo;

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
     * Gets the value of the messageCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Sets the value of the messageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageCode(String value) {
        this.messageCode = value;
    }

    /**
     * Gets the value of the messageDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDesc() {
        return messageDesc;
    }

    /**
     * Sets the value of the messageDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDesc(String value) {
        this.messageDesc = value;
    }

    /**
     * Gets the value of the lineNo property.
     *
     */
    public Long getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     *
     */
    public void setLineNo(Long value) {
        this.lineNo = value;
    }

}
