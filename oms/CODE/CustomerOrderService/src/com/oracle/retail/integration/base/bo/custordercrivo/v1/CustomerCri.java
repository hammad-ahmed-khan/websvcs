
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
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}CustomerInfo" minOccurs="0"/>
 *         &lt;element name="customer_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "customerInfo",
    "customerId"
})
@XmlRootElement(name = "CustomerCri")
public class CustomerCri {

    @XmlElement(name = "CustomerInfo")
    protected CustomerInfo customerInfo;
    @XmlElement(name = "customer_id")
    protected String customerId;

    /**
     * Either customer info or customer id must be set
     *                                for order search by customer.
     * 
     * @return
     *     possible object is
     *     {@link CustomerInfo }
     *     
     */
    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    /**
     * Sets the value of the customerInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerInfo }
     *     
     */
    public void setCustomerInfo(CustomerInfo value) {
        this.customerInfo = value;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

}
