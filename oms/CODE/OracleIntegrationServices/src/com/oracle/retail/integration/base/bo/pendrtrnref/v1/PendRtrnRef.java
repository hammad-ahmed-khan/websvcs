
package com.oracle.retail.integration.base.bo.pendrtrnref.v1;

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
 *         &lt;element name="physical_wh" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="rma_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="line_item_nbr" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="reason_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "physicalWh",
    "rmaNbr",
    "lineItemNbr",
    "reasonCode"
})
@XmlRootElement(name = "PendRtrnRef")
public class PendRtrnRef {

    @XmlElement(name = "physical_wh")
    protected long physicalWh;
    @XmlElement(name = "rma_nbr")
    protected String rmaNbr;
    @XmlElement(name = "line_item_nbr")
    protected long lineItemNbr;
    @XmlElement(name = "reason_code")
    protected String reasonCode;

    /**
     * Gets the value of the physicalWh property.
     * 
     */
    public long getPhysicalWh() {
        return physicalWh;
    }

    /**
     * Sets the value of the physicalWh property.
     * 
     */
    public void setPhysicalWh(long value) {
        this.physicalWh = value;
    }

    /**
     * Gets the value of the rmaNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmaNbr() {
        return rmaNbr;
    }

    /**
     * Sets the value of the rmaNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmaNbr(String value) {
        this.rmaNbr = value;
    }

    /**
     * Gets the value of the lineItemNbr property.
     * 
     */
    public long getLineItemNbr() {
        return lineItemNbr;
    }

    /**
     * Sets the value of the lineItemNbr property.
     * 
     */
    public void setLineItemNbr(long value) {
        this.lineItemNbr = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

}
