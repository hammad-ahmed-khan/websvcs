
package com.oracle.retail.integration.base.bo.custordercrivo.v1;

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
 *         &lt;element name="request_item_header_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element name="request_item_discount_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element name="request_item_tax_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
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
    "requestItemHeaderFlag",
    "requestItemDiscountFlag",
    "requestItemTaxFlag"
})
@XmlRootElement(name = "OrderItemRequestor")
public class OrderItemRequestor {

    @XmlElement(name = "request_item_header_flag", required = true)
    protected Flag requestItemHeaderFlag;
    @XmlElement(name = "request_item_discount_flag", required = true)
    protected Flag requestItemDiscountFlag;
    @XmlElement(name = "request_item_tax_flag", required = true)
    protected Flag requestItemTaxFlag;

    /**
     * Gets the value of the requestItemHeaderFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestItemHeaderFlag() {
        return requestItemHeaderFlag;
    }

    /**
     * Sets the value of the requestItemHeaderFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestItemHeaderFlag(Flag value) {
        this.requestItemHeaderFlag = value;
    }

    /**
     * Gets the value of the requestItemDiscountFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestItemDiscountFlag() {
        return requestItemDiscountFlag;
    }

    /**
     * Sets the value of the requestItemDiscountFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestItemDiscountFlag(Flag value) {
        this.requestItemDiscountFlag = value;
    }

    /**
     * Gets the value of the requestItemTaxFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestItemTaxFlag() {
        return requestItemTaxFlag;
    }

    /**
     * Sets the value of the requestItemTaxFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestItemTaxFlag(Flag value) {
        this.requestItemTaxFlag = value;
    }

}
