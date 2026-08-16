
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;


/**
 * <p>Java class for createCustomerOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createCustomerOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1}CustOrderDesc" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createCustomerOrder", propOrder = {
    "custOrderDesc"
})
public class CreateCustomerOrder {

    @XmlElement(name = "CustOrderDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderDesc/v1")
    protected CustOrderDesc custOrderDesc;

    /**
     * Gets the value of the custOrderDesc property.
     * 
     * @return
     *     possible object is
     *     {@link CustOrderDesc }
     *     
     */
    public CustOrderDesc getCustOrderDesc() {
        return custOrderDesc;
    }

    /**
     * Sets the value of the custOrderDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderDesc }
     *     
     */
    public void setCustOrderDesc(CustOrderDesc value) {
        this.custOrderDesc = value;
    }

}
