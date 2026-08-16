
package com.logicinfo.oms.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerOrderResponseItems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrderResponseItems">
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
 *         &lt;element name="order_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tsf_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="po_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="backorder_qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
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
@XmlType(name = "customerOrderResponseItems", propOrder = {
    "lineNo",
    "item",
    "orderQtySuom",
    "fulfillQtySuom",
    "status",
    "statusMessage",
    "customerOrderResponseItemFulfillment"
})
public class CustomerOrderResponseItems {

    @XmlElement(required = true, nillable = true)
    protected String item;
    @XmlElement(name = "order_qty_suom", required = true, nillable = true)
    protected BigDecimal orderQtySuom;
    @XmlElement(name = "fulfill_qty_suom", required = true, nillable = true)
    protected BigDecimal fulfillQtySuom;
    @XmlElement(nillable = true)
    protected List<CustomerOrderResponseItemFulfillment> customerOrderResponseItemFulfillment;
    protected String status;
    @XmlElement(name = "status_message")
    protected String statusMessage;
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
     * Gets the value of the orderQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrderQtySuom() {
        return orderQtySuom;
    }

    /**
     * Sets the value of the orderQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrderQtySuom(BigDecimal value) {
        this.orderQtySuom = value;
    }

    /**
     * Gets the value of the fulfillQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFulfillQtySuom() {
        return fulfillQtySuom;
    }

    /**
     * Sets the value of the fulfillQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFulfillQtySuom(BigDecimal value) {
        this.fulfillQtySuom = value;
    }


    /**
     * Gets the value of the customerOrderResponseItemFulfillment property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrderResponseItemFulfillment property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerOrderResponseItemFulfillment().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerOrderResponseItemFulfillment }
     *
     *
     */
    public List<CustomerOrderResponseItemFulfillment> getCustomerOrderResponseItemFulfillment() {
        if (customerOrderResponseItemFulfillment == null) {
            customerOrderResponseItemFulfillment = new ArrayList<CustomerOrderResponseItemFulfillment>();
        }
        return this.customerOrderResponseItemFulfillment;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets the value of the statusMessage property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Sets the value of the statusMessage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStatusMessage(String value) {
        this.statusMessage = value;
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
