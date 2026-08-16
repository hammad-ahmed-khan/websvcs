
package com.oracle.retail.integration.base.bo.strinvdesc.v1;

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
 *         &lt;element name="store_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="is_ranged" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="is_estimated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="uom_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="case_size" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="stock_on_hand_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="back_room_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="shop_floor_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="delivery_bay_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="available_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unavailable_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="nonsell_total_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="on_order_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="in_transit_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="cust_reserved_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="transfer_reserved_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="vendor_return_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="allocation_total_qty" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/StrInvDesc/v1}StrInvNslQty" maxOccurs="unbounded" minOccurs="0"/>
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
    "storeId",
    "isRanged",
    "isEstimated",
    "uomCode",
    "caseSize",
    "stockOnHandQty",
    "backRoomQty",
    "shopFloorQty",
    "deliveryBayQty",
    "availableQty",
    "unavailableQty",
    "nonsellTotalQty",
    "onOrderQty",
    "inTransitQty",
    "custReservedQty",
    "transferReservedQty",
    "vendorReturnQty",
    "allocationTotalQty",
    "strInvNslQty"
})
@XmlRootElement(name = "StrInvDesc")
public class StrInvDesc {

    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(name = "store_id")
    protected long storeId;
    @XmlElement(name = "is_ranged")
    protected boolean isRanged;
    @XmlElement(name = "is_estimated")
    protected boolean isEstimated;
    @XmlElement(name = "uom_code", required = true)
    protected String uomCode;
    @XmlElement(name = "case_size", required = true)
    protected BigDecimal caseSize;
    @XmlElement(name = "stock_on_hand_qty", required = true)
    protected BigDecimal stockOnHandQty;
    @XmlElement(name = "back_room_qty", required = true)
    protected BigDecimal backRoomQty;
    @XmlElement(name = "shop_floor_qty", required = true)
    protected BigDecimal shopFloorQty;
    @XmlElement(name = "delivery_bay_qty", required = true)
    protected BigDecimal deliveryBayQty;
    @XmlElement(name = "available_qty", required = true)
    protected BigDecimal availableQty;
    @XmlElement(name = "unavailable_qty", required = true)
    protected BigDecimal unavailableQty;
    @XmlElement(name = "nonsell_total_qty", required = true)
    protected BigDecimal nonsellTotalQty;
    @XmlElement(name = "on_order_qty", required = true)
    protected BigDecimal onOrderQty;
    @XmlElement(name = "in_transit_qty", required = true)
    protected BigDecimal inTransitQty;
    @XmlElement(name = "cust_reserved_qty", required = true)
    protected BigDecimal custReservedQty;
    @XmlElement(name = "transfer_reserved_qty", required = true)
    protected BigDecimal transferReservedQty;
    @XmlElement(name = "vendor_return_qty", required = true)
    protected BigDecimal vendorReturnQty;
    @XmlElement(name = "allocation_total_qty", required = true)
    protected BigDecimal allocationTotalQty;
    @XmlElement(name = "StrInvNslQty")
    protected List<StrInvNslQty> strInvNslQty;

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

    /**
     * Gets the value of the isRanged property.
     * 
     */
    public boolean isIsRanged() {
        return isRanged;
    }

    /**
     * Sets the value of the isRanged property.
     * 
     */
    public void setIsRanged(boolean value) {
        this.isRanged = value;
    }

    /**
     * Gets the value of the isEstimated property.
     * 
     */
    public boolean isIsEstimated() {
        return isEstimated;
    }

    /**
     * Sets the value of the isEstimated property.
     * 
     */
    public void setIsEstimated(boolean value) {
        this.isEstimated = value;
    }

    /**
     * Gets the value of the uomCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUomCode() {
        return uomCode;
    }

    /**
     * Sets the value of the uomCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUomCode(String value) {
        this.uomCode = value;
    }

    /**
     * Gets the value of the caseSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCaseSize() {
        return caseSize;
    }

    /**
     * Sets the value of the caseSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCaseSize(BigDecimal value) {
        this.caseSize = value;
    }

    /**
     * Gets the value of the stockOnHandQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getStockOnHandQty() {
        return stockOnHandQty;
    }

    /**
     * Sets the value of the stockOnHandQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setStockOnHandQty(BigDecimal value) {
        this.stockOnHandQty = value;
    }

    /**
     * Gets the value of the backRoomQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBackRoomQty() {
        return backRoomQty;
    }

    /**
     * Sets the value of the backRoomQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBackRoomQty(BigDecimal value) {
        this.backRoomQty = value;
    }

    /**
     * Gets the value of the shopFloorQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getShopFloorQty() {
        return shopFloorQty;
    }

    /**
     * Sets the value of the shopFloorQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setShopFloorQty(BigDecimal value) {
        this.shopFloorQty = value;
    }

    /**
     * Gets the value of the deliveryBayQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDeliveryBayQty() {
        return deliveryBayQty;
    }

    /**
     * Sets the value of the deliveryBayQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDeliveryBayQty(BigDecimal value) {
        this.deliveryBayQty = value;
    }

    /**
     * Gets the value of the availableQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAvailableQty() {
        return availableQty;
    }

    /**
     * Sets the value of the availableQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAvailableQty(BigDecimal value) {
        this.availableQty = value;
    }

    /**
     * Gets the value of the unavailableQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getUnavailableQty() {
        return unavailableQty;
    }

    /**
     * Sets the value of the unavailableQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setUnavailableQty(BigDecimal value) {
        this.unavailableQty = value;
    }

    /**
     * Gets the value of the nonsellTotalQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNonsellTotalQty() {
        return nonsellTotalQty;
    }

    /**
     * Sets the value of the nonsellTotalQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNonsellTotalQty(BigDecimal value) {
        this.nonsellTotalQty = value;
    }

    /**
     * Gets the value of the onOrderQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOnOrderQty() {
        return onOrderQty;
    }

    /**
     * Sets the value of the onOrderQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOnOrderQty(BigDecimal value) {
        this.onOrderQty = value;
    }

    /**
     * Gets the value of the inTransitQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInTransitQty() {
        return inTransitQty;
    }

    /**
     * Sets the value of the inTransitQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInTransitQty(BigDecimal value) {
        this.inTransitQty = value;
    }

    /**
     * Gets the value of the custReservedQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCustReservedQty() {
        return custReservedQty;
    }

    /**
     * Sets the value of the custReservedQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCustReservedQty(BigDecimal value) {
        this.custReservedQty = value;
    }

    /**
     * Gets the value of the transferReservedQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTransferReservedQty() {
        return transferReservedQty;
    }

    /**
     * Sets the value of the transferReservedQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTransferReservedQty(BigDecimal value) {
        this.transferReservedQty = value;
    }

    /**
     * Gets the value of the vendorReturnQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVendorReturnQty() {
        return vendorReturnQty;
    }

    /**
     * Sets the value of the vendorReturnQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVendorReturnQty(BigDecimal value) {
        this.vendorReturnQty = value;
    }

    /**
     * Gets the value of the allocationTotalQty property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAllocationTotalQty() {
        return allocationTotalQty;
    }

    /**
     * Sets the value of the allocationTotalQty property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAllocationTotalQty(BigDecimal value) {
        this.allocationTotalQty = value;
    }

    /**
     * Gets the value of the strInvNslQty property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the strInvNslQty property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getStrInvNslQty().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.strinvdesc.v1.StrInvNslQty}
     *
     *
     */
    public List<StrInvNslQty> getStrInvNslQty() {
        if (strInvNslQty == null) {
            strInvNslQty = new ArrayList<StrInvNslQty>();
        }
        return this.strInvNslQty;
    }

}
