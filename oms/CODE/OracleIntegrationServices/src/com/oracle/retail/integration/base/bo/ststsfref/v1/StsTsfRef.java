
package com.oracle.retail.integration.base.bo.ststsfref.v1;

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
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "storeId"
})
@XmlRootElement(name = "StsTsfRef")
public class StsTsfRef {

    @XmlElement(name = "transfer_id")
    protected long transferId;
    @XmlElement(name = "store_id")
    protected long storeId;

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
     * Gets the value of the storeId property.
     * 
     */
    public long getStoreId() {
        return storeId;
    }

    /**
     * Sets the value of the storeId property.
     * 
     */
    public void setStoreId(long value) {
        this.storeId = value;
    }

}
