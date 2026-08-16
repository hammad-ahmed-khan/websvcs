
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.oracle.retail.integration.base.bo.custordercrivo.v1.CustOrderCriVo;


/**
 * <p>Java class for queryCustomerOrder complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="queryCustomerOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}CustOrderCriVo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryCustomerOrder", propOrder = {
    "custOrderCriVo"
})
public class QueryCustomerOrder {

    @XmlElement(name = "CustOrderCriVo", namespace = "http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1")
    protected CustOrderCriVo custOrderCriVo;

    /**
     * Gets the value of the custOrderCriVo property.
     * 
     * @return
     *     possible object is
     *     {@link CustOrderCriVo }
     *     
     */
    public CustOrderCriVo getCustOrderCriVo() {
        return custOrderCriVo;
    }

    /**
     * Sets the value of the custOrderCriVo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustOrderCriVo }
     *     
     */
    public void setCustOrderCriVo(CustOrderCriVo value) {
        this.custOrderCriVo = value;
    }

}
