
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;


/**
 * <p>Java class for cancelNewCustomerOrderIdResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cancelNewCustomerOrderIdResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1}CustOrderRef" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelNewCustomerOrderIdResponse", propOrder = {
    "custOrderRef"
})
public class CancelNewCustomerOrderIdResponse {

    @XmlElement(name = "CustOrderRef", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderRef/v1")
    protected CustOrderRef custOrderRef;

    /**
     * Gets the value of the custOrderRef property.
     * 
     * @return
     *     possible object is
     *     {@link CustOrderRef }
     *     
     */
    public CustOrderRef getCustOrderRef() {
        return custOrderRef;
    }

    /**
     * Sets the value of the custOrderRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderRef }
     *     
     */
    public void setCustOrderRef(CustOrderRef value) {
        this.custOrderRef = value;
    }

}
