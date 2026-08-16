
package com.logicinfo.oms.model;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerOrderResponseItemFulfillment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrderResponseItemFulfillment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="item">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="25"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="order_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minExclusive value="0"/>
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_qty_suom">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;fractionDigits value="04"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="tsf_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="po_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="source_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fulfill_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMS_resv_qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMS_resv_loc">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="RMS_resv_loc_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="backorder_qty">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrderResponseItemFulfillment", propOrder = {
    "fulfillOrderNo",
    "item",
    "orderQtySuom",
    "fulfillQtySuom",
    "tsfNo",
    "poNo",
    "sourceLocType",
    "sourceLoc",
    "fulfillLoc",
    "fulfillLocType",
    "rmsResvQty",
    "rmsResvLoc",
    "rmsResvLocType",
    "backorderQty"
})
public class CustomerOrderResponseItemFulfillment {

    @XmlElement(required = true, nillable = true)
    protected String item;
    @XmlElement(name = "order_qty_suom", required = true, nillable = true)
    protected BigDecimal orderQtySuom;
    @XmlElement(name = "fulfill_qty_suom", required = true, nillable = true)
    protected BigDecimal fulfillQtySuom;
    @XmlElement(name = "tsf_no", required = true, type = Long.class, nillable = true)
    protected Long tsfNo;
    @XmlElement(name = "po_no", required = true, type = Long.class, nillable = true)
    protected Long poNo;
    @XmlElement(name = "source_loc_type", required = true, nillable = true)
    protected String sourceLocType;
    @XmlElement(name = "source_loc", required = true, type = Long.class, nillable = true)
    protected Long sourceLoc;
    @XmlElement(name = "fulfill_loc", required = true, type = Long.class, nillable = true)
    protected Long fulfillLoc;
    @XmlElement(name = "fulfill_loc_type", required = true, nillable = true)
    protected String fulfillLocType;
    @XmlElement(name = "RMS_resv_qty", required = true, type = Long.class, nillable = true)
    protected Long rmsResvQty;
    @XmlElement(name = "RMS_resv_loc", required = true, type = Long.class, nillable = true)
    protected Long rmsResvLoc;
    @XmlElement(name = "RMS_resv_loc_type", required = true, nillable = true)
    protected String rmsResvLocType;
    @XmlElement(name = "backorder_qty", required = true, type = Long.class, nillable = true)
    protected Long backorderQty;
    @XmlElement(name = "fulfill_order_no", required = true, type = Long.class, nillable = true)
    protected Long fulfillOrderNo;

    /**
     * Gets the value of the item property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getItem() {
        return item;
    }

    /**
     * Sets the value of the item property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem(String value) {
        this.item = value;
    }

    /**
     * Gets the value of the orderQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrderQtySuom() {
        return orderQtySuom;
    }

    /**
     * Sets the value of the orderQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrderQtySuom(BigDecimal value) {
        this.orderQtySuom = value;
    }

    /**
     * Gets the value of the fulfillQtySuom property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFulfillQtySuom() {
        return fulfillQtySuom;
    }

    /**
     * Sets the value of the fulfillQtySuom property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFulfillQtySuom(BigDecimal value) {
        this.fulfillQtySuom = value;
    }

    /**
     * Gets the value of the tsfNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTsfNo() {
        return tsfNo;
    }

    /**
     * Sets the value of the tsfNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTsfNo(Long value) {
        this.tsfNo = value;
    }

    /**
     * Gets the value of the poNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPoNo() {
        return poNo;
    }

    /**
     * Sets the value of the poNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPoNo(Long value) {
        this.poNo = value;
    }

    /**
     * Gets the value of the sourceLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceLocType() {
        return sourceLocType;
    }

    /**
     * Sets the value of the sourceLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceLocType(String value) {
        this.sourceLocType = value;
    }

    /**
     * Gets the value of the sourceLoc property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSourceLoc() {
        return sourceLoc;
    }

    /**
     * Sets the value of the sourceLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSourceLoc(Long value) {
        this.sourceLoc = value;
    }

    /**
     * Gets the value of the fulfillLoc property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFulfillLoc() {
        return fulfillLoc;
    }

    /**
     * Sets the value of the fulfillLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFulfillLoc(Long value) {
        this.fulfillLoc = value;
    }

    /**
     * Gets the value of the fulfillLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillLocType() {
        return fulfillLocType;
    }

    /**
     * Sets the value of the fulfillLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillLocType(String value) {
        this.fulfillLocType = value;
    }

    /**
     * Gets the value of the rmsResvQty property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRMSResvQty() {
        return rmsResvQty;
    }

    /**
     * Sets the value of the rmsResvQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRMSResvQty(Long value) {
        this.rmsResvQty = value;
    }

    /**
     * Gets the value of the rmsResvLoc property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRMSResvLoc() {
        return rmsResvLoc;
    }

    /**
     * Sets the value of the rmsResvLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRMSResvLoc(Long value) {
        this.rmsResvLoc = value;
    }

    /**
     * Gets the value of the rmsResvLocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRMSResvLocType() {
        return rmsResvLocType;
    }

    /**
     * Sets the value of the rmsResvLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRMSResvLocType(String value) {
        this.rmsResvLocType = value;
    }

    /**
     * Gets the value of the backorderQty property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBackorderQty() {
        return backorderQty;
    }

    /**
     * Sets the value of the backorderQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBackorderQty(Long value) {
        this.backorderQty = value;
    }

    /**
     * Gets the value of the fulfillOrderNo property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getFulfillOrderNo() {
        return fulfillOrderNo;
    }


    /**
     * Sets the value of the fulfillOrderNo property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setFulfillOrderNo(Long value) {
        this.fulfillOrderNo = value;
    }


}
