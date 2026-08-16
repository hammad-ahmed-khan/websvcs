
package com.oracle.retail.integration.base.bo.forpref.v1;

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
 *         &lt;element name="reverse_pick_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "reversePickId"
})
@XmlRootElement(name = "ForpRef")
public class ForpRef {

    @XmlElement(name = "reverse_pick_id")
    protected long reversePickId;

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

}
