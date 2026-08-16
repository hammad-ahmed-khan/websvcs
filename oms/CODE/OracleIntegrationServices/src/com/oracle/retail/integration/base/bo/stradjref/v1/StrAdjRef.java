
package com.oracle.retail.integration.base.bo.stradjref.v1;

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
 *         &lt;element name="adjustment_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
    "adjustmentId"
})
@XmlRootElement(name = "StrAdjRef")
public class StrAdjRef {

    @XmlElement(name = "adjustment_id")
    protected long adjustmentId;

    /**
     * Gets the value of the adjustmentId property.
     * 
     */
    public long getAdjustmentId() {
        return adjustmentId;
    }

    /**
     * Sets the value of the adjustmentId property.
     * 
     */
    public void setAdjustmentId(long value) {
        this.adjustmentId = value;
    }

}
