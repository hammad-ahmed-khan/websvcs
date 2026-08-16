
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerOrderRMAItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerOrderRMAItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="return_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="item_comments" minOccurs="0">
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
@XmlType(name = "CustomerOrderRMAItem", propOrder = {
    "item",
    "lineNo",
    "returnQtySuom",
    "itemComments"
})
public class CustomerOrderRMAItem {

    @XmlElement(required = true)
    protected String item;
    @XmlElement(name = "return_qty_suom", required = true)
    protected BigDecimal returnQtySuom;
    @XmlElement(name = "item_comments")
    protected String itemComments;
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
     * Gets the value of the returnQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnQtySuom() {
        return returnQtySuom;
    }

    /**
     * Sets the value of the returnQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnQtySuom(BigDecimal value) {
        this.returnQtySuom = value;
    }

    /**
     * Gets the value of the itemComments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemComments() {
        return itemComments;
    }

    /**
     * Sets the value of the itemComments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemComments(String value) {
        this.itemComments = value;
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
