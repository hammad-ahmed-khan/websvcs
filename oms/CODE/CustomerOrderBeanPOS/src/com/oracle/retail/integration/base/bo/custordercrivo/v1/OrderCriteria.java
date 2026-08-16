
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
 *         &lt;element name="search_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}enum_search_type"/>
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}CustomerCri" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}CreditDebitCri" minOccurs="0"/>
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
    "searchType",
    "customerOrderId",
    "customerCri",
    "creditDebitCri"
})
@XmlRootElement(name = "OrderCriteria")
public class OrderCriteria {

    @XmlElement(name = "search_type", required = true)
    protected EnumSearchType searchType;
    @XmlElement(name = "customer_order_id")
    protected String customerOrderId;
    @XmlElement(name = "CustomerCri")
    protected CustomerCri customerCri;
    @XmlElement(name = "CreditDebitCri")
    protected CreditDebitCri creditDebitCri;

    /**
     * Gets the value of the searchType property.
     * 
     * @return
     *     possible object is
     *     {@link EnumSearchType }
     *     
     */
    public EnumSearchType getSearchType() {
        return searchType;
    }

    /**
     * Sets the value of the searchType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumSearchType }
     *     
     */
    public void setSearchType(EnumSearchType value) {
        this.searchType = value;
    }

    /**
     * Gets the value of the customerOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderId() {
        return customerOrderId;
    }

    /**
     * Sets the value of the customerOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderId(String value) {
        this.customerOrderId = value;
    }

    /**
     * Based on the search type, either a customer order
     *                                id, a customer criteria, or a credit debit card
     *                                criteria must be specified for order search.
     * 
     * @return
     *     possible object is
     *     {@link CustomerCri }
     *     
     */
    public CustomerCri getCustomerCri() {
        return customerCri;
    }

    /**
     * Sets the value of the customerCri property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerCri }
     *     
     */
    public void setCustomerCri(CustomerCri value) {
        this.customerCri = value;
    }

    /**
     * Based on the search type, either a customer order
     *                                id, a customer criteria, or a credit debit card
     *                                criteria must be specified for order search.
     * 
     * @return
     *     possible object is
     *     {@link CreditDebitCri }
     *     
     */
    public CreditDebitCri getCreditDebitCri() {
        return creditDebitCri;
    }

    /**
     * Sets the value of the creditDebitCri property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditDebitCri }
     *     
     */
    public void setCreditDebitCri(CreditDebitCri value) {
        this.creditDebitCri = value;
    }

}
