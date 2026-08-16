
package com.oracle.retail.integration.base.bo.receiptdesc.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="unit_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="receipt_xactn_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receipt_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="receipt_nbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dest_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="container_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="distro_nbr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="distro_doc_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="to_disposition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_disposition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="to_wip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_wip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="to_trouble" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="from_trouble" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="user_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dummy_carton_ind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tampered_carton_ind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unit_cost" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="shipped_qty" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="weight_uom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gross_cost" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ReceiptDesc/v1}ReceiptDtlUin" maxOccurs="unbounded" minOccurs="0"/>
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
    "unitQty",
    "receiptXactnType",
    "receiptDate",
    "receiptNbr",
    "destId",
    "containerId",
    "distroNbr",
    "distroDocType",
    "toDisposition",
    "fromDisposition",
    "toWip",
    "fromWip",
    "toTrouble",
    "fromTrouble",
    "userId",
    "dummyCartonInd",
    "tamperedCartonInd",
    "unitCost",
    "shippedQty",
    "weight",
    "weightUom",
    "grossCost",
    "receiptDtlUin"
})
@XmlRootElement(name = "ReceiptDtl")
public class ReceiptDtl {

    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "unit_qty", required = true)
    protected BigDecimal unitQty;
    @XmlElement(name = "receipt_xactn_type", required = true)
    protected String receiptXactnType;
    @XmlElement(name = "receipt_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receiptDate;
    @XmlElement(name = "receipt_nbr", required = true)
    protected String receiptNbr;
    @XmlElement(name = "dest_id")
    protected String destId;
    @XmlElement(name = "container_id")
    protected String containerId;
    @XmlElement(name = "distro_nbr")
    protected String distroNbr;
    @XmlElement(name = "distro_doc_type")
    protected String distroDocType;
    @XmlElement(name = "to_disposition")
    protected String toDisposition;
    @XmlElement(name = "from_disposition")
    protected String fromDisposition;
    @XmlElement(name = "to_wip")
    protected String toWip;
    @XmlElement(name = "from_wip")
    protected String fromWip;
    @XmlElement(name = "to_trouble")
    protected String toTrouble;
    @XmlElement(name = "from_trouble")
    protected String fromTrouble;
    @XmlElement(name = "user_id")
    protected String userId;
    @XmlElement(name = "dummy_carton_ind")
    protected String dummyCartonInd;
    @XmlElement(name = "tampered_carton_ind")
    protected String tamperedCartonInd;
    @XmlElement(name = "unit_cost")
    protected BigDecimal unitCost;
    @XmlElement(name = "shipped_qty")
    protected BigDecimal shippedQty;
    protected BigDecimal weight;
    @XmlElement(name = "weight_uom")
    protected String weightUom;
    @XmlElement(name = "gross_cost")
    protected BigDecimal grossCost;
    @XmlElement(name = "ReceiptDtlUin")
    protected List<ReceiptDtlUin> receiptDtlUin;

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
     * Gets the value of the receiptXactnType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptXactnType() {
        return receiptXactnType;
    }

    /**
     * Sets the value of the receiptXactnType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptXactnType(String value) {
        this.receiptXactnType = value;
    }

    /**
     * Gets the value of the receiptDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceiptDate() {
        return receiptDate;
    }

    /**
     * Sets the value of the receiptDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceiptDate(XMLGregorianCalendar value) {
        this.receiptDate = value;
    }

    /**
     * Gets the value of the receiptNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptNbr() {
        return receiptNbr;
    }

    /**
     * Sets the value of the receiptNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptNbr(String value) {
        this.receiptNbr = value;
    }

    /**
     * Gets the value of the destId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestId() {
        return destId;
    }

    /**
     * Sets the value of the destId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestId(String value) {
        this.destId = value;
    }

    /**
     * Gets the value of the containerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * Sets the value of the containerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainerId(String value) {
        this.containerId = value;
    }

    /**
     * Gets the value of the distroNbr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistroNbr() {
        return distroNbr;
    }

    /**
     * Sets the value of the distroNbr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistroNbr(String value) {
        this.distroNbr = value;
    }

    /**
     * Gets the value of the distroDocType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistroDocType() {
        return distroDocType;
    }

    /**
     * Sets the value of the distroDocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistroDocType(String value) {
        this.distroDocType = value;
    }

    /**
     * Gets the value of the toDisposition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToDisposition() {
        return toDisposition;
    }

    /**
     * Sets the value of the toDisposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToDisposition(String value) {
        this.toDisposition = value;
    }

    /**
     * Gets the value of the fromDisposition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromDisposition() {
        return fromDisposition;
    }

    /**
     * Sets the value of the fromDisposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromDisposition(String value) {
        this.fromDisposition = value;
    }

    /**
     * Gets the value of the toWip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToWip() {
        return toWip;
    }

    /**
     * Sets the value of the toWip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToWip(String value) {
        this.toWip = value;
    }

    /**
     * Gets the value of the fromWip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromWip() {
        return fromWip;
    }

    /**
     * Sets the value of the fromWip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromWip(String value) {
        this.fromWip = value;
    }

    /**
     * Gets the value of the toTrouble property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToTrouble() {
        return toTrouble;
    }

    /**
     * Sets the value of the toTrouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToTrouble(String value) {
        this.toTrouble = value;
    }

    /**
     * Gets the value of the fromTrouble property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFromTrouble() {
        return fromTrouble;
    }

    /**
     * Sets the value of the fromTrouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFromTrouble(String value) {
        this.fromTrouble = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the dummyCartonInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDummyCartonInd() {
        return dummyCartonInd;
    }

    /**
     * Sets the value of the dummyCartonInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDummyCartonInd(String value) {
        this.dummyCartonInd = value;
    }

    /**
     * Gets the value of the tamperedCartonInd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTamperedCartonInd() {
        return tamperedCartonInd;
    }

    /**
     * Sets the value of the tamperedCartonInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTamperedCartonInd(String value) {
        this.tamperedCartonInd = value;
    }

    /**
     * Gets the value of the unitCost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnitCost() {
        return unitCost;
    }

    /**
     * Sets the value of the unitCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnitCost(BigDecimal value) {
        this.unitCost = value;
    }

    /**
     * Gets the value of the shippedQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getShippedQty() {
        return shippedQty;
    }

    /**
     * Sets the value of the shippedQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setShippedQty(BigDecimal value) {
        this.shippedQty = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeight(BigDecimal value) {
        this.weight = value;
    }

    /**
     * Gets the value of the weightUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeightUom() {
        return weightUom;
    }

    /**
     * Sets the value of the weightUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeightUom(String value) {
        this.weightUom = value;
    }

    /**
     * Gets the value of the grossCost property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGrossCost() {
        return grossCost;
    }

    /**
     * Sets the value of the grossCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGrossCost(BigDecimal value) {
        this.grossCost = value;
    }

    /**
     * Collection of UIN(s) associated to the item.Gets the value of the receiptDtlUin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiptDtlUin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiptDtlUin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiptDtlUin }
     * 
     * 
     */
    public List<ReceiptDtlUin> getReceiptDtlUin() {
        if (receiptDtlUin == null) {
            receiptDtlUin = new ArrayList<ReceiptDtlUin>();
        }
        return this.receiptDtlUin;
    }

}
