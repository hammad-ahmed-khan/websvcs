
package com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1;

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
 *         &lt;element name="transfer_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StsTsfRcvModVo/v1}StsTsfRcvItmMod" maxOccurs="unbounded"/>
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
    "transferId",
    "stsTsfRcvItmMod"
})
@XmlRootElement(name = "StsTsfRcvModVo")
public class StsTsfRcvModVo {

    @XmlElement(name = "transfer_id")
    protected long transferId;
    @XmlElement(name = "StsTsfRcvItmMod", required = true)
    protected List<StsTsfRcvItmMod> stsTsfRcvItmMod;

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
     * Gets the value of the stsTsfRcvItmMod property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stsTsfRcvItmMod property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getStsTsfRcvItmMod().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.ststsfrcvmodvo.v1.StsTsfRcvItmMod}
     *
     *
     */
    public List<StsTsfRcvItmMod> getStsTsfRcvItmMod() {
        if (stsTsfRcvItmMod == null) {
            stsTsfRcvItmMod = new ArrayList<StsTsfRcvItmMod>();
        }
        return this.stsTsfRcvItmMod;
    }

}
