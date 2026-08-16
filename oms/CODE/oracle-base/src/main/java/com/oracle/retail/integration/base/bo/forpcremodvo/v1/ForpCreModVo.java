
package com.oracle.retail.integration.base.bo.forpcremodvo.v1;

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
 *         &lt;element name="int_fulfillment_order_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpCreModVo/v1}ForpCreItmMod" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "intFulfillmentOrderId",
    "forpCreItmMod"
})
@XmlRootElement(name = "ForpCreModVo")
public class ForpCreModVo {

    @XmlElement(name = "int_fulfillment_order_id")
    protected long intFulfillmentOrderId;
    @XmlElement(name = "ForpCreItmMod")
    protected List<ForpCreItmMod> forpCreItmMod;

    /**
     * Gets the value of the intFulfillmentOrderId property.
     * 
     */
    public long getIntFulfillmentOrderId() {
        return intFulfillmentOrderId;
    }

    /**
     * Sets the value of the intFulfillmentOrderId property.
     * 
     */
    public void setIntFulfillmentOrderId(long value) {
        this.intFulfillmentOrderId = value;
    }

    /**
     * Gets the value of the forpCreItmMod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the forpCreItmMod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForpCreItmMod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ForpCreItmMod }
     * 
     * 
     */
    public List<ForpCreItmMod> getForpCreItmMod() {
        if (forpCreItmMod == null) {
            forpCreItmMod = new ArrayList<ForpCreItmMod>();
        }
        return this.forpCreItmMod;
    }

}
