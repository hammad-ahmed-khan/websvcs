
package org.datacontract.schemas._2004._07.extra_services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for OrderDetailStatus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderDetailStatus"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OrderDetailId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="ProductSku" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Quantity" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="SourceType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SourceId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="FulfillType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FulfillId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="EventId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="EventComment" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="EventReferenceId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="UpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderDetailStatus", propOrder = {
    "orderDetailId",
    "productSku",
    "quantity",
    "sourceType",
    "sourceId",
    "fulfillType",
    "fulfillId",
    "eventId",
    "eventComment",
    "eventReferenceId",
    "updateDate"
})
public class OrderDetailStatus {

    @XmlElement(name = "OrderDetailId")
    protected long orderDetailId;
    @XmlElement(name = "ProductSku", required = true, nillable = true)
    protected String productSku;
    @XmlElement(name = "Quantity")
    protected int quantity;
    @XmlElement(name = "SourceType", required = true, nillable = true)
    protected String sourceType;
    @XmlElement(name = "SourceId")
    protected int sourceId;
    @XmlElement(name = "FulfillType", required = true, nillable = true)
    protected String fulfillType;
    @XmlElement(name = "FulfillId")
    protected int fulfillId;
    @XmlElement(name = "EventId", required = true, nillable = true)
    protected String eventId;
    @XmlElement(name = "EventComment", required = true, nillable = true)
    protected String eventComment;
    @XmlElement(name = "EventReferenceId")
    protected long eventReferenceId;
    @XmlElement(name = "UpdateDate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar updateDate;

    /**
     * Gets the value of the orderDetailId property.
     * 
     */
    public long getOrderDetailId() {
        return orderDetailId;
    }

    /**
     * Sets the value of the orderDetailId property.
     * 
     */
    public void setOrderDetailId(long value) {
        this.orderDetailId = value;
    }

    /**
     * Gets the value of the productSku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductSku() {
        return productSku;
    }

    /**
     * Sets the value of the productSku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductSku(String value) {
        this.productSku = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the sourceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * Sets the value of the sourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceType(String value) {
        this.sourceType = value;
    }

    /**
     * Gets the value of the sourceId property.
     * 
     */
    public int getSourceId() {
        return sourceId;
    }

    /**
     * Sets the value of the sourceId property.
     * 
     */
    public void setSourceId(int value) {
        this.sourceId = value;
    }

    /**
     * Gets the value of the fulfillType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillType() {
        return fulfillType;
    }

    /**
     * Sets the value of the fulfillType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillType(String value) {
        this.fulfillType = value;
    }

    /**
     * Gets the value of the fulfillId property.
     * 
     */
    public int getFulfillId() {
        return fulfillId;
    }

    /**
     * Sets the value of the fulfillId property.
     * 
     */
    public void setFulfillId(int value) {
        this.fulfillId = value;
    }

    /**
     * Gets the value of the eventId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * Sets the value of the eventId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventId(String value) {
        this.eventId = value;
    }

    /**
     * Gets the value of the eventComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventComment() {
        return eventComment;
    }

    /**
     * Sets the value of the eventComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventComment(String value) {
        this.eventComment = value;
    }

    /**
     * Gets the value of the eventReferenceId property.
     * 
     */
    public long getEventReferenceId() {
        return eventReferenceId;
    }

    /**
     * Sets the value of the eventReferenceId property.
     * 
     */
    public void setEventReferenceId(long value) {
        this.eventReferenceId = value;
    }

    /**
     * Gets the value of the updateDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the value of the updateDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setUpdateDate(XMLGregorianCalendar value) {
        this.updateDate = value;
    }

}
