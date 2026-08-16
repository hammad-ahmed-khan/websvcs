
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
 *         &lt;element name="request_order_header_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element name="request_order_item_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}enum_request_order_item_flag"/>
 *         &lt;element name="request_order_fulfillment_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element name="request_order_delivery_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element name="request_order_payment_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}flag"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}OrderItemRequestor" minOccurs="0"/>
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
    "requestOrderHeaderFlag",
    "requestOrderItemFlag",
    "requestOrderFulfillmentFlag",
    "requestOrderDeliveryFlag",
    "requestOrderPaymentFlag",
    "orderItemRequestor"
})
@XmlRootElement(name = "OrderRequestor")
public class OrderRequestor {

    @XmlElement(name = "request_order_header_flag", required = true)
    protected Flag requestOrderHeaderFlag;
    @XmlElement(name = "request_order_item_flag", required = true)
    protected EnumRequestOrderItemFlag requestOrderItemFlag;
    @XmlElement(name = "request_order_fulfillment_flag", required = true)
    protected Flag requestOrderFulfillmentFlag;
    @XmlElement(name = "request_order_delivery_flag", required = true)
    protected Flag requestOrderDeliveryFlag;
    @XmlElement(name = "request_order_payment_flag", required = true)
    protected Flag requestOrderPaymentFlag;
    @XmlElement(name = "OrderItemRequestor")
    protected OrderItemRequestor orderItemRequestor;

    /**
     * Gets the value of the requestOrderHeaderFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestOrderHeaderFlag() {
        return requestOrderHeaderFlag;
    }

    /**
     * Sets the value of the requestOrderHeaderFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestOrderHeaderFlag(Flag value) {
        this.requestOrderHeaderFlag = value;
    }

    /**
     * Gets the value of the requestOrderItemFlag property.
     * 
     * @return
     *     possible object is
     *     {@link EnumRequestOrderItemFlag }
     *     
     */
    public EnumRequestOrderItemFlag getRequestOrderItemFlag() {
        return requestOrderItemFlag;
    }

    /**
     * Sets the value of the requestOrderItemFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumRequestOrderItemFlag }
     *     
     */
    public void setRequestOrderItemFlag(EnumRequestOrderItemFlag value) {
        this.requestOrderItemFlag = value;
    }

    /**
     * Gets the value of the requestOrderFulfillmentFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestOrderFulfillmentFlag() {
        return requestOrderFulfillmentFlag;
    }

    /**
     * Sets the value of the requestOrderFulfillmentFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestOrderFulfillmentFlag(Flag value) {
        this.requestOrderFulfillmentFlag = value;
    }

    /**
     * Gets the value of the requestOrderDeliveryFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestOrderDeliveryFlag() {
        return requestOrderDeliveryFlag;
    }

    /**
     * Sets the value of the requestOrderDeliveryFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestOrderDeliveryFlag(Flag value) {
        this.requestOrderDeliveryFlag = value;
    }

    /**
     * Gets the value of the requestOrderPaymentFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getRequestOrderPaymentFlag() {
        return requestOrderPaymentFlag;
    }

    /**
     * Sets the value of the requestOrderPaymentFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setRequestOrderPaymentFlag(Flag value) {
        this.requestOrderPaymentFlag = value;
    }

    /**
     * This element defines what needs to be returned
     *                                for an order item. It is required only if the
     *                                return_order_item_flag is "FIRST" or "ALL".
     * 
     * @return
     *     possible object is
     *     {@link OrderItemRequestor }
     *     
     */
    public OrderItemRequestor getOrderItemRequestor() {
        return orderItemRequestor;
    }

    /**
     * Sets the value of the orderItemRequestor property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderItemRequestor }
     *     
     */
    public void setOrderItemRequestor(OrderItemRequestor value) {
        this.orderItemRequestor = value;
    }

}
