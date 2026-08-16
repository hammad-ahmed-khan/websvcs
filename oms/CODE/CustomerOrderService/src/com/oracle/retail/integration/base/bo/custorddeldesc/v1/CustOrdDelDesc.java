
package com.oracle.retail.integration.base.bo.custorddeldesc.v1;

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
 *         &lt;element name="seq_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="delivery_loc_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdDelDesc/v1}enum_delivery_loc_type"/>
 *         &lt;element name="delivery_loc_id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="delivery_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdDelDesc/v1}enum_delivery_type"/>
 *         &lt;element name="delivery_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="delivery_time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="customer_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customer_signature" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="carrier_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_service_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_tracking_no" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "deliveryLocType",
    "deliveryLocId",
    "deliveryType",
    "deliveryDate",
    "deliveryTime",
    "customerId",
    "customerSignature",
    "carrierCode",
    "carrierServiceCode",
    "carrierTrackingNo"
})
@XmlRootElement(name = "CustOrdDelDesc")
public class CustOrdDelDesc {

    @XmlElement(name = "seq_no")
    protected int seqNo;
    @XmlElement(name = "delivery_loc_type", required = true)
    protected EnumDeliveryLocType deliveryLocType;
    @XmlElement(name = "delivery_loc_id")
    protected long deliveryLocId;
    @XmlElement(name = "delivery_type", required = true)
    protected EnumDeliveryType deliveryType;
    @XmlElement(name = "delivery_date", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deliveryDate;
    @XmlElement(name = "delivery_time")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deliveryTime;
    @XmlElement(name = "customer_id")
    protected String customerId;
    @XmlElement(name = "customer_signature")
    protected List<String> customerSignature;
    @XmlElement(name = "carrier_code")
    protected String carrierCode;
    @XmlElement(name = "carrier_service_code")
    protected String carrierServiceCode;
    @XmlElement(name = "carrier_tracking_no")
    protected String carrierTrackingNo;

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
     * Gets the value of the deliveryLocType property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDeliveryLocType }
     *     
     */
    public EnumDeliveryLocType getDeliveryLocType() {
        return deliveryLocType;
    }

    /**
     * Sets the value of the deliveryLocType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDeliveryLocType }
     *     
     */
    public void setDeliveryLocType(EnumDeliveryLocType value) {
        this.deliveryLocType = value;
    }

    /**
     * Gets the value of the deliveryLocId property.
     * 
     */
    public long getDeliveryLocId() {
        return deliveryLocId;
    }

    /**
     * Sets the value of the deliveryLocId property.
     * 
     */
    public void setDeliveryLocId(long value) {
        this.deliveryLocId = value;
    }

    /**
     * Gets the value of the deliveryType property.
     * 
     * @return
     *     possible object is
     *     {@link EnumDeliveryType }
     *     
     */
    public EnumDeliveryType getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnumDeliveryType }
     *     
     */
    public void setDeliveryType(EnumDeliveryType value) {
        this.deliveryType = value;
    }

    /**
     * Gets the value of the deliveryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the value of the deliveryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeliveryDate(XMLGregorianCalendar value) {
        this.deliveryDate = value;
    }

    /**
     * Gets the value of the deliveryTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Sets the value of the deliveryTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDeliveryTime(XMLGregorianCalendar value) {
        this.deliveryTime = value;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerId(String value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the customerSignature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerSignature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCustomerSignature() {
        if (customerSignature == null) {
            customerSignature = new ArrayList<String>();
        }
        return this.customerSignature;
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
     * Gets the value of the carrierTrackingNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierTrackingNo() {
        return carrierTrackingNo;
    }

    /**
     * Sets the value of the carrierTrackingNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierTrackingNo(String value) {
        this.carrierTrackingNo = value;
    }

}
