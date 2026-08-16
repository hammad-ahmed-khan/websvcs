
package com.oracle.retail.integration.base.bo.pendrtrndesc.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="action_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unit_qty" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="disposition_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sellable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "actionCode",
    "unitQty",
    "dispositionCode",
    "sellable",
    "comments"
})
@XmlRootElement(name = "PendRtrnDtlActCodeDesc")
public class PendRtrnDtlActCodeDesc {

    @XmlElement(name = "action_code")
    protected String actionCode;
    @XmlElement(name = "unit_qty")
    protected BigDecimal unitQty;
    @XmlElement(name = "disposition_code")
    protected String dispositionCode;
    protected String sellable;
    protected String comments;

    /**
     * Gets the value of the actionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * Sets the value of the actionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionCode(String value) {
        this.actionCode = value;
    }

    /**
     * Gets the value of the unitQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitQty() {
        return unitQty;
    }

    /**
     * Sets the value of the unitQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitQty(BigDecimal value) {
        this.unitQty = value;
    }

    /**
     * Gets the value of the dispositionCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispositionCode() {
        return dispositionCode;
    }

    /**
     * Sets the value of the dispositionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispositionCode(String value) {
        this.dispositionCode = value;
    }

    /**
     * Gets the value of the sellable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSellable() {
        return sellable;
    }

    /**
     * Sets the value of the sellable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSellable(String value) {
        this.sellable = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

}
