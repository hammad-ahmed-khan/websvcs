
package com.oracle.retail.integration.base.bo.fulfilorddesc.v1;

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
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;


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
 *         &lt;element name="customer_order_no" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fulfill_order_no" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="source_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDesc/v1}source_loc_type" minOccurs="0"/>
 *         &lt;element name="source_loc_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fulfill_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDesc/v1}fulfill_loc_type"/>
 *         &lt;element name="fulfill_loc_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="partial_delivery_ind" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDesc/v1}yes_no_ind"/>
 *         &lt;element name="delivery_type" type="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDesc/v1}delivery_type" minOccurs="0"/>
 *         &lt;element name="carrier_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_service_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="consumer_delivery_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="consumer_delivery_time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="delivery_charges" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="delivery_charges_curr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdCustDesc/v1}FulfilOrdCustDesc" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtl/v1}FulfilOrdDtl" maxOccurs="unbounded"/>
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
    "customerOrderNo",
    "fulfillOrderNo",
    "sourceLocType",
    "sourceLocId",
    "fulfillLocType",
    "fulfillLocId",
    "partialDeliveryInd",
    "deliveryType",
    "carrierCode",
    "carrierServiceCode",
    "consumerDeliveryDate",
    "consumerDeliveryTime",
    "deliveryCharges",
    "deliveryChargesCurr",
    "comments",
    "fulfilOrdCustDesc",
    "fulfilOrdDtl"
})
@XmlRootElement(name = "FulfilOrdDesc")
public class FulfilOrdDesc {

    @XmlElement(name = "customer_order_no", required = true)
    protected String customerOrderNo;
    @XmlElement(name = "fulfill_order_no", required = true)
    protected String fulfillOrderNo;
    @XmlElement(name = "source_loc_type")
    protected SourceLocType sourceLocType;
    @XmlElement(name = "source_loc_id")
    protected Long sourceLocId;
    @XmlElement(name = "fulfill_loc_type", required = true)
    protected FulfillLocType fulfillLocType;
    @XmlElement(name = "fulfill_loc_id")
    protected long fulfillLocId;
    @XmlElement(name = "partial_delivery_ind", required = true)
    protected YesNoInd partialDeliveryInd;
    @XmlElement(name = "delivery_type")
    protected DeliveryType deliveryType;
    @XmlElement(name = "carrier_code")
    protected String carrierCode;
    @XmlElement(name = "carrier_service_code")
    protected String carrierServiceCode;
    @XmlElement(name = "consumer_delivery_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDeliveryDate;
    @XmlElement(name = "consumer_delivery_time")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDeliveryTime;
    @XmlElement(name = "delivery_charges")
    protected BigDecimal deliveryCharges;
    @XmlElement(name = "delivery_charges_curr")
    protected String deliveryChargesCurr;
    protected String comments;
    @XmlElement(name = "FulfilOrdCustDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdCustDesc/v1")
    protected FulfilOrdCustDesc fulfilOrdCustDesc;
    @XmlElement(name = "FulfilOrdDtl", namespace = "http://www.oracle.com/retail/integration/base/bo/FulfilOrdDtl/v1", required = true)
    protected List<FulfilOrdDtl> fulfilOrdDtl;

    /**
     * Gets the value of the customerOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    /**
     * Sets the value of the customerOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerOrderNo(String value) {
        this.customerOrderNo = value;
    }

    /**
     * Gets the value of the fulfillOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    /**
     * Sets the value of the fulfillOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFulfillOrderNo(String value) {
        this.fulfillOrderNo = value;
    }

    /**
     * Gets the value of the sourceLocType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType}
     *
     */
    public SourceLocType getSourceLocType() {
        return sourceLocType;
    }

    /**
     * Sets the value of the sourceLocType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType}
     *
     */
    public void setSourceLocType(SourceLocType value) {
        this.sourceLocType = value;
    }

    /**
     * Gets the value of the sourceLocId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSourceLocId() {
        return sourceLocId;
    }

    /**
     * Sets the value of the sourceLocId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSourceLocId(Long value) {
        this.sourceLocId = value;
    }

    /**
     * Gets the value of the fulfillLocType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType}
     *
     */
    public FulfillLocType getFulfillLocType() {
        return fulfillLocType;
    }

    /**
     * Sets the value of the fulfillLocType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType}
     *
     */
    public void setFulfillLocType(FulfillLocType value) {
        this.fulfillLocType = value;
    }

    /**
     * Gets the value of the fulfillLocId property.
     * 
     */
    public long getFulfillLocId() {
        return fulfillLocId;
    }

    /**
     * Sets the value of the fulfillLocId property.
     * 
     */
    public void setFulfillLocId(long value) {
        this.fulfillLocId = value;
    }

    /**
     * Gets the value of the partialDeliveryInd property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd}
     *
     */
    public YesNoInd getPartialDeliveryInd() {
        return partialDeliveryInd;
    }

    /**
     * Sets the value of the partialDeliveryInd property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd}
     *
     */
    public void setPartialDeliveryInd(YesNoInd value) {
        this.partialDeliveryInd = value;
    }

    /**
     * Gets the value of the deliveryType property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType}
     *
     */
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType}
     *
     */
    public void setDeliveryType(DeliveryType value) {
        this.deliveryType = value;
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
     * Gets the value of the deliveryCharges property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDeliveryCharges() {
        return deliveryCharges;
    }

    /**
     * Sets the value of the deliveryCharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDeliveryCharges(BigDecimal value) {
        this.deliveryCharges = value;
    }

    /**
     * Gets the value of the deliveryChargesCurr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryChargesCurr() {
        return deliveryChargesCurr;
    }

    /**
     * Sets the value of the deliveryChargesCurr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryChargesCurr(String value) {
        this.deliveryChargesCurr = value;
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
     * Gets the value of the fulfilOrdCustDesc property.
     *
     * @return
     * possible object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc}
     *
     */
    public FulfilOrdCustDesc getFulfilOrdCustDesc() {
        return fulfilOrdCustDesc;
    }

    /**
     * Sets the value of the fulfilOrdCustDesc property.
     *
     * @param value
     * allowed object is
     * {@link .com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc}
     *
     */
    public void setFulfilOrdCustDesc(FulfilOrdCustDesc value) {
        this.fulfilOrdCustDesc = value;
    }

    /**
     * Gets the value of the fulfilOrdDtl property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fulfilOrdDtl property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getFulfilOrdDtl().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl}
     *
     *
     */
    public List<FulfilOrdDtl> getFulfilOrdDtl() {
        if (fulfilOrdDtl == null) {
            fulfilOrdDtl = new ArrayList<FulfilOrdDtl>();
        }
        return this.fulfilOrdDtl;
    }

}
