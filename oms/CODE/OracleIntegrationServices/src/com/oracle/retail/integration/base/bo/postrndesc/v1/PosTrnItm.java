
package com.oracle.retail.integration.base.bo.postrndesc.v1;

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
 *         &lt;element name="item_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="unit_of_measure" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reason_code" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="drop_ship" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reservation_type" type="{http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1}PosTrnOrdResvType"/>
 *         &lt;element name="transaction_code" type="{http://www.oracle.com/retail/integration/base/bo/PosTrnDesc/v1}PosTrnItmTranCode"/>
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
    "quantity",
    "unitOfMeasure",
    "uin",
    "reasonCode",
    "dropShip",
    "comments",
    "fulfillOrderId",
    "reservationType",
    "transactionCode"
})
@XmlRootElement(name = "PosTrnItm")
public class PosTrnItm {

    @XmlElement(name = "item_id", required = true)
    protected String itemId;
    @XmlElement(required = true)
    protected BigDecimal quantity;
    @XmlElement(name = "unit_of_measure", required = true)
    protected String unitOfMeasure;
    protected String uin;
    @XmlElement(name = "reason_code")
    protected Integer reasonCode;
    @XmlElement(name = "drop_ship")
    protected boolean dropShip;
    protected String comments;
    @XmlElement(name = "fulfill_order_id")
    protected String fulfillOrderId;
    @XmlElement(name = "reservation_type", required = true)
    protected PosTrnOrdResvType reservationType;
    @XmlElement(name = "transaction_code", required = true)
    protected PosTrnItmTranCode transactionCode;

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
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantity(BigDecimal value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

    /**
     * Gets the value of the uin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUin() {
        return uin;
    }

    /**
     * Sets the value of the uin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUin(String value) {
        this.uin = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReasonCode(Integer value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the dropShip property.
     * 
     */
    public boolean isDropShip() {
        return dropShip;
    }

    /**
     * Sets the value of the dropShip property.
     * 
     */
    public void setDropShip(boolean value) {
        this.dropShip = value;
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

    /**
     * Gets the value of the fulfillOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillOrderId() {
        return fulfillOrderId;
    }

    /**
     * Sets the value of the fulfillOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillOrderId(String value) {
        this.fulfillOrderId = value;
    }

    /**
     * Gets the value of the reservationType property.
     * 
     * @return
     *     possible object is
     *     {@link PosTrnOrdResvType }
     *     
     */
    public PosTrnOrdResvType getReservationType() {
        return reservationType;
    }

    /**
     * Sets the value of the reservationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosTrnOrdResvType }
     *     
     */
    public void setReservationType(PosTrnOrdResvType value) {
        this.reservationType = value;
    }

    /**
     * Gets the value of the transactionCode property.
     * 
     * @return
     *     possible object is
     *     {@link PosTrnItmTranCode }
     *     
     */
    public PosTrnItmTranCode getTransactionCode() {
        return transactionCode;
    }

    /**
     * Sets the value of the transactionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PosTrnItmTranCode }
     *     
     */
    public void setTransactionCode(PosTrnItmTranCode value) {
        this.transactionCode = value;
    }

}
