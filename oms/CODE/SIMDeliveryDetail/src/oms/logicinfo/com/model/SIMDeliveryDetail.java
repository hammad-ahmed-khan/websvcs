
package oms.logicinfo.com.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SIMDeliveryDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SIMDeliveryDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numberOfOrders">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="orderStatus">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="READY_TO_SHIP"/>
 *               &lt;enumeration value="UNDELIVERED"/>
 *               &lt;enumeration value="ALL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="orderSource">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="E-COMMERCE"/>
 *               &lt;enumeration value="ORPOS"/>
 *               &lt;enumeration value="ALL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="retrieveCustomerDetails">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="True"/>
 *               &lt;enumeration value="False"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="retrieveProductDetails">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="True"/>
 *               &lt;enumeration value="False"/>
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
@XmlType(name = "SIMDeliveryDetail", propOrder = {
    "numberOfOrders",
    "orderStatus",
    "orderSource",
    "retrieveCustomerDetails",
    "retrieveProductDetails"
})
public class SIMDeliveryDetail {

    protected long numberOfOrders;
    @XmlElement(required = true)
    protected String orderStatus;
    @XmlElement(required = true)
    protected String orderSource;
    @XmlElement(required = true)
    protected String retrieveCustomerDetails;
    @XmlElement(required = true)
    protected String retrieveProductDetails;

    /**
     * Gets the value of the numberOfOrders property.
     * 
     */
    public long getNumberOfOrders() {
        return numberOfOrders;
    }

    /**
     * Sets the value of the numberOfOrders property.
     * 
     */
    public void setNumberOfOrders(long value) {
        this.numberOfOrders = value;
    }

    /**
     * Gets the value of the orderStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * Sets the value of the orderStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderStatus(String value) {
        this.orderStatus = value;
    }

    /**
     * Gets the value of the orderSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderSource() {
        return orderSource;
    }

    /**
     * Sets the value of the orderSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderSource(String value) {
        this.orderSource = value;
    }

    /**
     * Gets the value of the retrieveCustomerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrieveCustomerDetails() {
        return retrieveCustomerDetails;
    }

    /**
     * Sets the value of the retrieveCustomerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrieveCustomerDetails(String value) {
        this.retrieveCustomerDetails = value;
    }

    /**
     * Gets the value of the retrieveProductDetails property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetrieveProductDetails() {
        return retrieveProductDetails;
    }

    /**
     * Sets the value of the retrieveProductDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetrieveProductDetails(String value) {
        this.retrieveProductDetails = value;
    }

}
