
package com.oracle.retail.integration.base.bo.stradjrsndesc.v1;

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
 *         &lt;element name="reason_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="reason_code" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="disposition_desc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="to_nonsellable_qty_type_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="from_nonsellable_qty_type_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
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
    "reasonId",
    "reasonCode",
    "description",
    "dispositionDesc",
    "toNonsellableQtyTypeId",
    "fromNonsellableQtyTypeId"
})
@XmlRootElement(name = "StrAdjRsnDesc")
public class StrAdjRsnDesc {

    @XmlElement(name = "reason_id")
    protected long reasonId;
    @XmlElement(name = "reason_code")
    protected int reasonCode;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(name = "disposition_desc", required = true)
    protected String dispositionDesc;
    @XmlElement(name = "to_nonsellable_qty_type_id")
    protected Long toNonsellableQtyTypeId;
    @XmlElement(name = "from_nonsellable_qty_type_id")
    protected Long fromNonsellableQtyTypeId;

    /**
     * Gets the value of the reasonId property.
     * 
     */
    public long getReasonId() {
        return reasonId;
    }

    /**
     * Sets the value of the reasonId property.
     * 
     */
    public void setReasonId(long value) {
        this.reasonId = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     */
    public int getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     */
    public void setReasonCode(int value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the dispositionDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDispositionDesc() {
        return dispositionDesc;
    }

    /**
     * Sets the value of the dispositionDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDispositionDesc(String value) {
        this.dispositionDesc = value;
    }

    /**
     * Gets the value of the toNonsellableQtyTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getToNonsellableQtyTypeId() {
        return toNonsellableQtyTypeId;
    }

    /**
     * Sets the value of the toNonsellableQtyTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setToNonsellableQtyTypeId(Long value) {
        this.toNonsellableQtyTypeId = value;
    }

    /**
     * Gets the value of the fromNonsellableQtyTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFromNonsellableQtyTypeId() {
        return fromNonsellableQtyTypeId;
    }

    /**
     * Sets the value of the fromNonsellableQtyTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFromNonsellableQtyTypeId(Long value) {
        this.fromNonsellableQtyTypeId = value;
    }

}
