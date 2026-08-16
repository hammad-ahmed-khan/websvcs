
package com.oracle.retail.integration.base.bo.fodcremodvo.v1;

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
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FodCreModVo/v1}FodCreItmMod" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "fulfillOrderId",
    "notes",
    "fodCreItmMod"
})
@XmlRootElement(name = "FodCreModVo")
public class FodCreModVo {

    @XmlElement(name = "fulfill_order_id")
    protected long fulfillOrderId;
    protected String notes;
    @XmlElement(name = "FodCreItmMod")
    protected List<FodCreItmMod> fodCreItmMod;

    /**
     * Gets the value of the fulfillOrderId property.
     * 
     */
    public long getFulfillOrderId() {
        return fulfillOrderId;
    }

    /**
     * Sets the value of the fulfillOrderId property.
     * 
     */
    public void setFulfillOrderId(long value) {
        this.fulfillOrderId = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the fodCreItmMod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fodCreItmMod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFodCreItmMod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FodCreItmMod }
     * 
     * 
     */
    public List<FodCreItmMod> getFodCreItmMod() {
        if (fodCreItmMod == null) {
            fodCreItmMod = new ArrayList<FodCreItmMod>();
        }
        return this.fodCreItmMod;
    }

}
