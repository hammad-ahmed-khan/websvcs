
package com.oracle.retail.integration.base.bo.pendrtrndesc.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="line_item_nbr" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="expected_unit_qty" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PendRtrnDesc/v1}PendRtrnDtlRsnCodeDesc" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PendRtrnDesc/v1}PendRtrnDtlActCodeDesc" maxOccurs="unbounded"/>
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
    "itemId",
    "lineItemNbr",
    "expectedUnitQty",
    "pendRtrnDtlRsnCodeDesc",
    "pendRtrnDtlActCodeDesc"
})
@XmlRootElement(name = "PendRtrnDtlDesc")
public class PendRtrnDtlDesc {

    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "line_item_nbr")
    protected long lineItemNbr;
    @XmlElement(name = "expected_unit_qty")
    protected BigDecimal expectedUnitQty;
    @XmlElement(name = "PendRtrnDtlRsnCodeDesc", required = true)
    protected List<PendRtrnDtlRsnCodeDesc> pendRtrnDtlRsnCodeDesc;
    @XmlElement(name = "PendRtrnDtlActCodeDesc", required = true)
    protected List<PendRtrnDtlActCodeDesc> pendRtrnDtlActCodeDesc;

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
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
     * Gets the value of the expectedUnitQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getExpectedUnitQty() {
        return expectedUnitQty;
    }

    /**
     * Sets the value of the expectedUnitQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setExpectedUnitQty(BigDecimal value) {
        this.expectedUnitQty = value;
    }

    /**
     * Gets the value of the pendRtrnDtlRsnCodeDesc property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pendRtrnDtlRsnCodeDesc property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getPendRtrnDtlRsnCodeDesc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlRsnCodeDesc}
     *
     *
     */
    public List<PendRtrnDtlRsnCodeDesc> getPendRtrnDtlRsnCodeDesc() {
        if (pendRtrnDtlRsnCodeDesc == null) {
            pendRtrnDtlRsnCodeDesc = new ArrayList<PendRtrnDtlRsnCodeDesc>();
        }
        return this.pendRtrnDtlRsnCodeDesc;
    }

    /**
     * Gets the value of the pendRtrnDtlActCodeDesc property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pendRtrnDtlActCodeDesc property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getPendRtrnDtlActCodeDesc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.pendrtrndesc.v1.PendRtrnDtlActCodeDesc}
     *
     *
     */
    public List<PendRtrnDtlActCodeDesc> getPendRtrnDtlActCodeDesc() {
        if (pendRtrnDtlActCodeDesc == null) {
            pendRtrnDtlActCodeDesc = new ArrayList<PendRtrnDtlActCodeDesc>();
        }
        return this.pendRtrnDtlActCodeDesc;
    }

}
