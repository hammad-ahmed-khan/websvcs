
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custordercoldesc.v1.CustOrderColDesc;


/**
 * <p>Java class for queryCustomerOrderResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryCustomerOrderResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderColDesc/v1}CustOrderColDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryCustomerOrderResponse", propOrder = {
    "custOrderColDesc"
})
public class QueryCustomerOrderResponse {

    @XmlElement(name = "CustOrderColDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderColDesc/v1")
    protected CustOrderColDesc custOrderColDesc;

    /**
     * Gets the value of the custOrderColDesc property.
     * 
     * @return
     *     possible object is
     *     {@link CustOrderColDesc }
     *     
     */
    public CustOrderColDesc getCustOrderColDesc() {
        return custOrderColDesc;
    }

    /**
     * Sets the value of the custOrderColDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderColDesc }
     *     
     */
    public void setCustOrderColDesc(CustOrderColDesc value) {
        this.custOrderColDesc = value;
    }

}
