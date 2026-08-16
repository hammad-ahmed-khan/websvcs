
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

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
 *         &lt;element name="coupon_type" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}coupon_type" minOccurs="0"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}entry_method" minOccurs="0"/>
 *         &lt;element name="coupon_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "couponType",
    "entryMethod",
    "couponNumber"
})
@XmlRootElement(name = "CouponTender")
public class CouponTender {

    @XmlElement(name = "coupon_type")
    protected CouponType couponType;
    @XmlElement(name = "entry_method")
    protected EntryMethod entryMethod;
    @XmlElement(name = "coupon_number", required = true)
    protected String couponNumber;

    /**
     * Gets the value of the couponType property.
     * 
     * @return
     *     possible object is
     *     {@link CouponType }
     *     
     */
    public CouponType getCouponType() {
        return couponType;
    }

    /**
     * Sets the value of the couponType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CouponType }
     *     
     */
    public void setCouponType(CouponType value) {
        this.couponType = value;
    }

    /**
     * Gets the value of the entryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EntryMethod }
     *     
     */
    public EntryMethod getEntryMethod() {
        return entryMethod;
    }

    /**
     * Sets the value of the entryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryMethod }
     *     
     */
    public void setEntryMethod(EntryMethod value) {
        this.entryMethod = value;
    }

    /**
     * Gets the value of the couponNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCouponNumber() {
        return couponNumber;
    }

    /**
     * Sets the value of the couponNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCouponNumber(String value) {
        this.couponNumber = value;
    }

}
