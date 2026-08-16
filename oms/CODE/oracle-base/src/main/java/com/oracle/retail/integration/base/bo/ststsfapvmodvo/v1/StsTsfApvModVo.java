
package com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1;

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
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfApvModVo/v1}StsTsfApvItmMod" maxOccurs="unbounded"/&gt;
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
    "transferId",
    "stsTsfApvItmMod"
})
@XmlRootElement(name = "StsTsfApvModVo")
public class StsTsfApvModVo {

    @XmlElement(name = "transfer_id")
    protected long transferId;
    @XmlElement(name = "StsTsfApvItmMod", required = true)
    protected List<StsTsfApvItmMod> stsTsfApvItmMod;

    /**
     * Gets the value of the transferId property.
     * 
     */
    public long getTransferId() {
        return transferId;
    }

    /**
     * Sets the value of the transferId property.
     * 
     */
    public void setTransferId(long value) {
        this.transferId = value;
    }

    /**
     * Gets the value of the stsTsfApvItmMod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stsTsfApvItmMod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStsTsfApvItmMod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StsTsfApvItmMod }
     * 
     * 
     */
    public List<StsTsfApvItmMod> getStsTsfApvItmMod() {
        if (stsTsfApvItmMod == null) {
            stsTsfApvItmMod = new ArrayList<StsTsfApvItmMod>();
        }
        return this.stsTsfApvItmMod;
    }

}
