
package com.oracle.retail.integration.base.bo.invavailcrivo.v1;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="items" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1}InvLocation" maxOccurs="unbounded"/>
 *         &lt;element name="store_pickup_ind" type="{http://www.oracle.com/retail/integration/base/bo/InvAvailCriVo/v1}store_pickup_ind"/>
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
    "items",
    "invLocation",
    "storePickupInd"
})
@XmlRootElement(name = "InvAvailCriVo")
public class InvAvailCriVo {

    @XmlElement(required = true)
    protected List<String> items;
    @XmlElement(name = "InvLocation", required = true)
    protected List<InvLocation> invLocation;
    @XmlElement(name = "store_pickup_ind", required = true)
    protected StorePickupInd storePickupInd;

    /**
     * Gets the value of the items property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the items property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItems().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getItems() {
        if (items == null) {
            items = new ArrayList<String>();
        }
        return this.items;
    }

    /**
     * Gets the value of the invLocation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the invLocation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getInvLocation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.InvLocation}
     *
     *
     */
    public List<InvLocation> getInvLocation() {
        if (invLocation == null) {
            invLocation = new ArrayList<InvLocation>();
        }
        return this.invLocation;
    }

    /**
     * Gets the value of the storePickupInd property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.StorePickupInd}
     *
     */
    public StorePickupInd getStorePickupInd() {
        return storePickupInd;
    }

    /**
     * Sets the value of the storePickupInd property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.invavailcrivo.v1.StorePickupInd}
     *
     */
    public void setStorePickupInd(StorePickupInd value) {
        this.storePickupInd = value;
    }

}
