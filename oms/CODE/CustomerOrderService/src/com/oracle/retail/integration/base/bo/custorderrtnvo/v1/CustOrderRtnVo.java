
package com.oracle.retail.integration.base.bo.custorderrtnvo.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.oracle.retail.integration.base.bo.custorditmrtcolvo.v1.CustOrdItmRtColVo;


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
 *         &lt;element name="customer_order_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returned_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_discount_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="returned_inclusive_tax_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="update_timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmRtColVo/v1}CustOrdItmRtColVo"/>
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
    "customerOrderId",
    "currencyCode",
    "returnedAmount",
    "returnedDiscountAmount",
    "returnedTaxAmount",
    "returnedInclusiveTaxAmount",
    "updateTimestamp",
    "custOrdItmRtColVo"
})
@XmlRootElement(name = "CustOrderRtnVo")
public class CustOrderRtnVo {

    @XmlElement(name = "customer_order_id", required = true)
    protected String customerOrderId;
    @XmlElement(name = "currency_code")
    protected String currencyCode;
    @XmlElement(name = "returned_amount")
    protected BigDecimal returnedAmount;
    @XmlElement(name = "returned_discount_amount")
    protected BigDecimal returnedDiscountAmount;
    @XmlElement(name = "returned_tax_amount")
    protected BigDecimal returnedTaxAmount;
    @XmlElement(name = "returned_inclusive_tax_amount")
    protected BigDecimal returnedInclusiveTaxAmount;
    @XmlElement(name = "update_timestamp", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateTimestamp;
    @XmlElement(name = "CustOrdItmRtColVo", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrdItmRtColVo/v1", required = true)
    protected CustOrdItmRtColVo custOrdItmRtColVo;

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
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the returnedAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedAmount() {
        return returnedAmount;
    }

    /**
     * Sets the value of the returnedAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedAmount(BigDecimal value) {
        this.returnedAmount = value;
    }

    /**
     * Gets the value of the returnedDiscountAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedDiscountAmount() {
        return returnedDiscountAmount;
    }

    /**
     * Sets the value of the returnedDiscountAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedDiscountAmount(BigDecimal value) {
        this.returnedDiscountAmount = value;
    }

    /**
     * Gets the value of the returnedTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedTaxAmount() {
        return returnedTaxAmount;
    }

    /**
     * Sets the value of the returnedTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedTaxAmount(BigDecimal value) {
        this.returnedTaxAmount = value;
    }

    /**
     * Gets the value of the returnedInclusiveTaxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getReturnedInclusiveTaxAmount() {
        return returnedInclusiveTaxAmount;
    }

    /**
     * Sets the value of the returnedInclusiveTaxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setReturnedInclusiveTaxAmount(BigDecimal value) {
        this.returnedInclusiveTaxAmount = value;
    }

    /**
     * Gets the value of the updateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Sets the value of the updateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateTimestamp(XMLGregorianCalendar value) {
        this.updateTimestamp = value;
    }

    /**
     * A collection of order items to update
     *                                incrementally.
     * 
     * @return
     *     possible object is
     *     {@link CustOrdItmRtColVo }
     *     
     */
    public CustOrdItmRtColVo getCustOrdItmRtColVo() {
        return custOrdItmRtColVo;
    }

    /**
     * Sets the value of the custOrdItmRtColVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrdItmRtColVo }
     *     
     */
    public void setCustOrdItmRtColVo(CustOrdItmRtColVo value) {
        this.custOrdItmRtColVo = value;
    }

}
