
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
 *         &lt;element name="search_loc_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="search_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}enum_search_loc_type"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}OrderCriteria"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrderCriVo/v1}OrderRequestor"/>
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
    "searchLocId",
    "searchLocType",
    "orderCriteria",
    "orderRequestor"
})
@XmlRootElement(name = "CustOrderCriVo")
public class CustOrderCriVo {

    @XmlElement(name = "search_loc_id")
    protected long searchLocId;
    @XmlElement(name = "search_loc_type", required = true)
    protected EnumSearchLocType searchLocType;
    @XmlElement(name = "OrderCriteria", required = true)
    protected OrderCriteria orderCriteria;
    @XmlElement(name = "OrderRequestor", required = true)
    protected OrderRequestor orderRequestor;

    /**
     * Gets the value of the searchLocId property.
     * 
     */
    public long getSearchLocId() {
        return searchLocId;
    }

    /**
     * Sets the value of the searchLocId property.
     * 
     */
    public void setSearchLocId(long value) {
        this.searchLocId = value;
    }

    /**
     * Gets the value of the searchLocType property.
     * 
     * @return
     *     possible object is
     *     {@link EnumSearchLocType }
     *     
     */
    public EnumSearchLocType getSearchLocType() {
        return searchLocType;
    }

    /**
     * Sets the value of the searchLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumSearchLocType }
     *     
     */
    public void setSearchLocType(EnumSearchLocType value) {
        this.searchLocType = value;
    }

    /**
     * This element defines the query criteria for order
     *                                search
     * 
     * @return
     *     possible object is
     *     {@link OrderCriteria }
     *     
     */
    public OrderCriteria getOrderCriteria() {
        return orderCriteria;
    }

    /**
     * Sets the value of the orderCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderCriteria }
     *     
     */
    public void setOrderCriteria(OrderCriteria value) {
        this.orderCriteria = value;
    }

    /**
     * This element defines what needs to be returned in
     *                                an order search result.
     * 
     * @return
     *     possible object is
     *     {@link OrderRequestor }
     *     
     */
    public OrderRequestor getOrderRequestor() {
        return orderRequestor;
    }

    /**
     * Sets the value of the orderRequestor property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderRequestor }
     *     
     */
    public void setOrderRequestor(OrderRequestor value) {
        this.orderRequestor = value;
    }

}
