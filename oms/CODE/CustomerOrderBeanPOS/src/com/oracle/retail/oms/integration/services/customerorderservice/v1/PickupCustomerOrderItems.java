
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custorderpicvo.v1.CustOrderPicVo;


/**
 * <p>Java class for pickupCustomerOrderItems complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pickupCustomerOrderItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderPicVo/v1}CustOrderPicVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pickupCustomerOrderItems", propOrder = {
    "custOrderPicVo"
})
public class PickupCustomerOrderItems {

    @XmlElement(name = "CustOrderPicVo", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderPicVo/v1")
    protected CustOrderPicVo custOrderPicVo;

    /**
     * Gets the value of the custOrderPicVo property.
     * 
     * @return
     *     possible object is
     *     {@link CustOrderPicVo }
     *     
     */
    public CustOrderPicVo getCustOrderPicVo() {
        return custOrderPicVo;
    }

    /**
     * Sets the value of the custOrderPicVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderPicVo }
     *     
     */
    public void setCustOrderPicVo(CustOrderPicVo value) {
        this.custOrderPicVo = value;
    }

}
