
package oms.logicinfo.com.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="OrderNo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="42"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OrderValue">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="05"/>
 *               &lt;totalDigits value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="IsCod">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Y"/>
 *               &lt;enumeration value="N"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Carrier">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Aramex"/>
 *               &lt;enumeration value="Smsa"/>
 *               &lt;enumeration value="Fetchr"/>
 *               &lt;enumeration value="Ups"/>
 *               &lt;enumeration value="Dhl"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="store_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="shipment_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_order_address" type="{http://com.logicinfo.oms/model/}customerOrderResponseAddress" minOccurs="0"/>
 *         &lt;element name="customer_order_items" type="{http://com.logicinfo.oms/model/}customerOrderResponseItems" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Order")
@XmlType(name = "", propOrder = {
    "orderNo",
    "orderValue",
    "isCod",
    "isExpressDelivery",
    "carrier",
    "storeId",
    "shipmentId",
    "customerOrderAddress",
    "customerOrderItems"
})
public class Order {

    @XmlElement(name = "OrderNo", required = true, nillable = true)
    protected String orderNo;
    @XmlElement(name = "OrderValue", required = true, nillable = true)
    protected BigDecimal orderValue;
    @XmlElement(name = "IsCod", required = true)
    protected String isCod;
    @XmlElement(name = "Carrier", required = true)
    protected String carrier;
    @XmlElement(name = "store_id", required = true, type = Integer.class, nillable = true)
    protected Integer storeId;
    @XmlElement(name = "shipment_id", required = true, nillable = true)
    protected String shipmentId;
    @XmlElement(name = "customer_order_address")
    protected CustomerOrderResponseAddress customerOrderAddress;
    @XmlElement(name = "customer_order_items", required = true)
    protected List<CustomerOrderResponseItems> customerOrderItems;
    @XmlElement(name = "IsExpressDelivery", required = true)
    protected String isExpressDelivery;

    /**
     * Gets the value of the orderNo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * Sets the value of the orderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNo(String value) {
        this.orderNo = value;
    }

    /**
     * Gets the value of the orderValue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrderValue() {
        return orderValue;
    }

    /**
     * Sets the value of the orderValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrderValue(BigDecimal value) {
        this.orderValue = value;
    }

    /**
     * Gets the value of the isCod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsCod() {
        return isCod;
    }

    /**
     * Sets the value of the isCod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsCod(String value) {
        this.isCod = value;
    }

    /**
     * Gets the value of the carrier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * Sets the value of the carrier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrier(String value) {
        this.carrier = value;
    }

    /**
     * Gets the value of the storeId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStoreId(Integer value) {
        this.storeId = value;
    }

    /**
     * Gets the value of the shipmentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShipmentId() {
        return shipmentId;
    }

    /**
     * Sets the value of the shipmentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShipmentId(String value) {
        this.shipmentId = value;
    }

    /**
     * Gets the value of the customerOrderAddress property.
     *
     * @return
     * possible object is
     * {@link CustomerOrderResponseAddress}
     *
     */
    public CustomerOrderResponseAddress getCustomerOrderAddress() {
        return customerOrderAddress;
    }

    /**
     * Sets the value of the customerOrderAddress property.
     *
     * @param value
     * allowed object is
     * {@link CustomerOrderResponseAddress}
     *
     */
    public void setCustomerOrderAddress(CustomerOrderResponseAddress value) {
        this.customerOrderAddress = value;
    }

    /**
     * Gets the value of the customerOrderItems property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrderItems property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getCustomerOrderItems().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerOrderResponseItems}
     *
     *
     */
    public List<CustomerOrderResponseItems> getCustomerOrderItems() {
        if (customerOrderItems == null) {
            customerOrderItems = new ArrayList<CustomerOrderResponseItems>();
        }
        return this.customerOrderItems;
    }

    /**
     * Gets the value of the isExpressDelivery property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIsExpressDelivery() {
        return isExpressDelivery;
    }

    /**
     * Sets the value of the isExpressDelivery property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIsExpressDelivery(String value) {
        this.isExpressDelivery = value;
    }

}
