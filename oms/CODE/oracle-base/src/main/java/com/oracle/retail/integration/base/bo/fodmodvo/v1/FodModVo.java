
package com.oracle.retail.integration.base.bo.fodmodvo.v1;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="int_fulfill_order_delivery_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodModVo/v1}FodItmMod" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "intFulfillOrderDeliveryId",
    "notes",
    "fodItmMod"
})
@XmlRootElement(name = "FodModVo")
public class FodModVo {

    @XmlElement(name = "int_fulfill_order_delivery_id")
    protected long intFulfillOrderDeliveryId;
    @XmlElement(nillable = true)
    protected List<String> notes;
    @XmlElement(name = "FodItmMod")
    protected List<FodItmMod> fodItmMod;

    /**
     * Gets the value of the intFulfillOrderDeliveryId property.
     * 
     */
    public long getIntFulfillOrderDeliveryId() {
        return intFulfillOrderDeliveryId;
    }

    /**
     * Sets the value of the intFulfillOrderDeliveryId property.
     * 
     */
    public void setIntFulfillOrderDeliveryId(long value) {
        this.intFulfillOrderDeliveryId = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the notes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNotes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNotes() {
        if (notes == null) {
            notes = new ArrayList<String>();
        }
        return this.notes;
    }

    /**
     * Gets the value of the fodItmMod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fodItmMod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFodItmMod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FodItmMod }
     * 
     * 
     */
    public List<FodItmMod> getFodItmMod() {
        if (fodItmMod == null) {
            fodItmMod = new ArrayList<FodItmMod>();
        }
        return this.fodItmMod;
    }

}
