
package com.oracle.retail.integration.base.bo.ststsfdesc.v1;

import java.math.BigDecimal;
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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bill_of_lading_motive_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="requested_pickup_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="carrier_role" type="{http://www.oracle.com/retail/integration/base/bo/StsTsfDesc/v1}sts_tsf_carrier_role"/&gt;
 *         &lt;element name="carrier_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="carrier_service_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="carrier_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="carrier_address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="carton_type_id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="package_weight" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="package_weight_uom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="alternate_ship_to_address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tracking_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "billOfLadingMotiveId",
    "requestedPickupDate",
    "carrierRole",
    "carrierCode",
    "carrierServiceCode",
    "carrierName",
    "carrierAddress",
    "cartonTypeId",
    "packageWeight",
    "packageWeightUom",
    "alternateShipToAddress",
    "trackingNumber"
})
@XmlRootElement(name = "StsTsfBol")
public class StsTsfBol {

    @XmlElement(name = "bill_of_lading_motive_id")
    protected String billOfLadingMotiveId;
    @XmlElement(name = "requested_pickup_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestedPickupDate;
    @XmlElement(name = "carrier_role", required = true)
    @XmlSchemaType(name = "string")
    protected StsTsfCarrierRole carrierRole;
    @XmlElement(name = "carrier_code")
    protected String carrierCode;
    @XmlElement(name = "carrier_service_code")
    protected String carrierServiceCode;
    @XmlElement(name = "carrier_name")
    protected String carrierName;
    @XmlElement(name = "carrier_address")
    protected String carrierAddress;
    @XmlElement(name = "carton_type_id")
    protected Long cartonTypeId;
    @XmlElement(name = "package_weight")
    protected BigDecimal packageWeight;
    @XmlElement(name = "package_weight_uom")
    protected String packageWeightUom;
    @XmlElement(name = "alternate_ship_to_address")
    protected String alternateShipToAddress;
    @XmlElement(name = "tracking_number")
    protected String trackingNumber;

    /**
     * Gets the value of the billOfLadingMotiveId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillOfLadingMotiveId() {
        return billOfLadingMotiveId;
    }

    /**
     * Sets the value of the billOfLadingMotiveId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillOfLadingMotiveId(String value) {
        this.billOfLadingMotiveId = value;
    }

    /**
     * Gets the value of the requestedPickupDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestedPickupDate() {
        return requestedPickupDate;
    }

    /**
     * Sets the value of the requestedPickupDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestedPickupDate(XMLGregorianCalendar value) {
        this.requestedPickupDate = value;
    }

    /**
     * Gets the value of the carrierRole property.
     * 
     * @return
     *     possible object is
     *     {@link StsTsfCarrierRole }
     *     
     */
    public StsTsfCarrierRole getCarrierRole() {
        return carrierRole;
    }

    /**
     * Sets the value of the carrierRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link StsTsfCarrierRole }
     *     
     */
    public void setCarrierRole(StsTsfCarrierRole value) {
        this.carrierRole = value;
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
     * Gets the value of the carrierName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierName() {
        return carrierName;
    }

    /**
     * Sets the value of the carrierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierName(String value) {
        this.carrierName = value;
    }

    /**
     * Gets the value of the carrierAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierAddress() {
        return carrierAddress;
    }

    /**
     * Sets the value of the carrierAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierAddress(String value) {
        this.carrierAddress = value;
    }

    /**
     * Gets the value of the cartonTypeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCartonTypeId() {
        return cartonTypeId;
    }

    /**
     * Sets the value of the cartonTypeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCartonTypeId(Long value) {
        this.cartonTypeId = value;
    }

    /**
     * Gets the value of the packageWeight property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPackageWeight() {
        return packageWeight;
    }

    /**
     * Sets the value of the packageWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPackageWeight(BigDecimal value) {
        this.packageWeight = value;
    }

    /**
     * Gets the value of the packageWeightUom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackageWeightUom() {
        return packageWeightUom;
    }

    /**
     * Sets the value of the packageWeightUom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackageWeightUom(String value) {
        this.packageWeightUom = value;
    }

    /**
     * Gets the value of the alternateShipToAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateShipToAddress() {
        return alternateShipToAddress;
    }

    /**
     * Sets the value of the alternateShipToAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateShipToAddress(String value) {
        this.alternateShipToAddress = value;
    }

    /**
     * Gets the value of the trackingNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrackingNumber() {
        return trackingNumber;
    }

    /**
     * Sets the value of the trackingNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrackingNumber(String value) {
        this.trackingNumber = value;
    }

}
