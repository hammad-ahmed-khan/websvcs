
package com.logicinfo.oms.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RMAModifyDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RMAModifyDetail">
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
 *         &lt;element name="qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
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
@XmlType(name = "RMAModifyDetail", propOrder = {
    "item",
    "lineNo",
    "qty"
})
public class RMAModifyDetail {

    @XmlElement(required = true)
    protected String item;
    protected long qty;
    @XmlElement(name = "line_no")
    protected long lineNo;

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
     * Gets the value of the qty property.
     * 
     */
    public long getQty() {
        return qty;
    }

    /**
     * Sets the value of the qty property.
     * 
     */
    public void setQty(long value) {
        this.qty = value;
    }

    /**
     * Gets the value of the lineNo property.
     *
     */
    public long getLineNo() {
        return lineNo;
    }

    /**
     * Sets the value of the lineNo property.
     *
     */
    public void setLineNo(long value) {
        this.lineNo = value;
    }

}
