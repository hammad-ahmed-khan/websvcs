
package com.oracle.retail.integration.base.bo.custordfuldesc.v1;

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
 *         &lt;element name="seq_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fulfill_order_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fulfill_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}fulfill_loc_type" minOccurs="0"/>
 *         &lt;element name="fulfill_loc_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="delivery_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}delivery_type"/>
 *         &lt;element name="partial_delivery_ind" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}partial_delivery_ind" minOccurs="0"/>
 *         &lt;element name="ship_to_fulfill_loc_flag" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}ship_to_fulfill_loc_flag" minOccurs="0"/>
 *         &lt;element name="carrier_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_service_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consumer_delivery_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="consumer_delivery_time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}DeliveryDestDtl"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustOrdFulDesc/v1}BillingDestDtl" minOccurs="0"/>
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
    "seqNo",
    "fulfillOrderId",
    "fulfillLocType",
    "fulfillLocId",
    "deliveryType",
    "partialDeliveryInd",
    "shipToFulfillLocFlag",
    "carrierCode",
    "carrierServiceCode",
    "consumerDeliveryDate",
    "consumerDeliveryTime",
    "comments",
    "deliveryDestDtl",
    "billingDestDtl"
})
@XmlRootElement(name = "CustOrdFulDesc")
public class CustOrdFulDesc {

    @XmlElement(name = "seq_no")
    protected int seqNo;
    @XmlElement(name = "fulfill_order_id")
    protected String fulfillOrderId;
    @XmlElement(name = "fulfill_loc_type")
    protected FulfillLocType fulfillLocType;
    @XmlElement(name = "fulfill_loc_id")
    protected Long fulfillLocId;
    @XmlElement(name = "delivery_type", required = true)
    protected DeliveryType deliveryType;
    @XmlElement(name = "partial_delivery_ind")
    protected PartialDeliveryInd partialDeliveryInd;
    @XmlElement(name = "ship_to_fulfill_loc_flag")
    protected ShipToFulfillLocFlag shipToFulfillLocFlag;
    @XmlElement(name = "carrier_code")
    protected String carrierCode;
    @XmlElement(name = "carrier_service_code")
    protected String carrierServiceCode;
    @XmlElement(name = "consumer_delivery_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDeliveryDate;
    @XmlElement(name = "consumer_delivery_time")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDeliveryTime;
    protected String comments;
    @XmlElement(name = "DeliveryDestDtl", required = true)
    protected DeliveryDestDtl deliveryDestDtl;
    @XmlElement(name = "BillingDestDtl")
    protected BillingDestDtl billingDestDtl;

    /**
     * Gets the value of the seqNo property.
     * 
     */
    public int getSeqNo() {
        return seqNo;
    }

    /**
     * Sets the value of the seqNo property.
     * 
     */
    public void setSeqNo(int value) {
        this.seqNo = value;
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
     * Gets the value of the fulfillLocType property.
     * 
     * @return
     *     possible object is
     *     {@link FulfillLocType }
     *     
     */
    public FulfillLocType getFulfillLocType() {
        return fulfillLocType;
    }

    /**
     * Sets the value of the fulfillLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link FulfillLocType }
     *     
     */
    public void setFulfillLocType(FulfillLocType value) {
        this.fulfillLocType = value;
    }

    /**
     * Gets the value of the fulfillLocId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFulfillLocId() {
        return fulfillLocId;
    }

    /**
     * Sets the value of the fulfillLocId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFulfillLocId(Long value) {
        this.fulfillLocId = value;
    }

    /**
     * Gets the value of the deliveryType property.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryType }
     *     
     */
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryType }
     *     
     */
    public void setDeliveryType(DeliveryType value) {
        this.deliveryType = value;
    }

    /**
     * Gets the value of the partialDeliveryInd property.
     * 
     * @return
     *     possible object is
     *     {@link PartialDeliveryInd }
     *     
     */
    public PartialDeliveryInd getPartialDeliveryInd() {
        return partialDeliveryInd;
    }

    /**
     * Sets the value of the partialDeliveryInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartialDeliveryInd }
     *     
     */
    public void setPartialDeliveryInd(PartialDeliveryInd value) {
        this.partialDeliveryInd = value;
    }

    /**
     * Gets the value of the shipToFulfillLocFlag property.
     * 
     * @return
     *     possible object is
     *     {@link ShipToFulfillLocFlag }
     *     
     */
    public ShipToFulfillLocFlag getShipToFulfillLocFlag() {
        return shipToFulfillLocFlag;
    }

    /**
     * Sets the value of the shipToFulfillLocFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShipToFulfillLocFlag }
     *     
     */
    public void setShipToFulfillLocFlag(ShipToFulfillLocFlag value) {
        this.shipToFulfillLocFlag = value;
    }

    /**
     * Gets the value of the carrierCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierCode() {
        return carrierCode;
    }

    /**
     * Sets the value of the carrierCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierCode(String value) {
        this.carrierCode = value;
    }

    /**
     * Gets the value of the carrierServiceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierServiceCode() {
        return carrierServiceCode;
    }

    /**
     * Sets the value of the carrierServiceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierServiceCode(String value) {
        this.carrierServiceCode = value;
    }

    /**
     * Gets the value of the consumerDeliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsumerDeliveryDate() {
        return consumerDeliveryDate;
    }

    /**
     * Sets the value of the consumerDeliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsumerDeliveryDate(XMLGregorianCalendar value) {
        this.consumerDeliveryDate = value;
    }

    /**
     * Gets the value of the consumerDeliveryTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsumerDeliveryTime() {
        return consumerDeliveryTime;
    }

    /**
     * Sets the value of the consumerDeliveryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsumerDeliveryTime(XMLGregorianCalendar value) {
        this.consumerDeliveryTime = value;
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
     * The delivery destination of order items.
     * 
     * @return
     *     possible object is
     *     {@link DeliveryDestDtl }
     *     
     */
    public DeliveryDestDtl getDeliveryDestDtl() {
        return deliveryDestDtl;
    }

    /**
     * Sets the value of the deliveryDestDtl property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeliveryDestDtl }
     *     
     */
    public void setDeliveryDestDtl(DeliveryDestDtl value) {
        this.deliveryDestDtl = value;
    }

    /**
     * The billing destination of order items.
     * 
     * @return
     *     possible object is
     *     {@link BillingDestDtl }
     *     
     */
    public BillingDestDtl getBillingDestDtl() {
        return billingDestDtl;
    }

    /**
     * Sets the value of the billingDestDtl property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillingDestDtl }
     *     
     */
    public void setBillingDestDtl(BillingDestDtl value) {
        this.billingDestDtl = value;
    }

}
