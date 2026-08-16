
package com.oracle.retail.integration.base.bo.forpmodvo.v1;

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
 *         &lt;element name="reverse_pick_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ForpModVo/v1}ForpItmMod" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "reversePickId",
    "forpItmMod"
})
@XmlRootElement(name = "ForpModVo")
public class ForpModVo {

    @XmlElement(name = "reverse_pick_id")
    protected long reversePickId;
    @XmlElement(name = "ForpItmMod")
    protected List<ForpItmMod> forpItmMod;

    /**
     * Gets the value of the reversePickId property.
     * 
     */
    public long getReversePickId() {
        return reversePickId;
    }

    /**
     * Sets the value of the reversePickId property.
     * 
     */
    public void setReversePickId(long value) {
        this.reversePickId = value;
    }

    /**
     * Gets the value of the forpItmMod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the forpItmMod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getForpItmMod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ForpItmMod }
     * 
     * 
     */
    public List<ForpItmMod> getForpItmMod() {
        if (forpItmMod == null) {
            forpItmMod = new ArrayList<ForpItmMod>();
        }
        return this.forpItmMod;
    }

}
