
package org.datacontract.schemas._2004._07.extra_services_shipping;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShipmentResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShipmentResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AirwayBillNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LabelData" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShipmentResponse", propOrder = {
    "airwayBillNo",
    "labelData",
    "errorMessage"
})
public class ShipmentResponse {

    @XmlElement(name = "AirwayBillNo", required = true)
    protected String airwayBillNo;
    @XmlElement(name = "LabelData", required = true)
    protected String labelData;
    @XmlElement(required = true, nillable = true)
    protected String errorMessage;

    /**
     * Gets the value of the airwayBillNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirwayBillNo() {
        return airwayBillNo;
    }

    /**
     * Sets the value of the airwayBillNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirwayBillNo(String value) {
        this.airwayBillNo = value;
    }

    /**
     * Gets the value of the labelData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabelData() {
        return labelData;
    }

    /**
     * Sets the value of the labelData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabelData(String value) {
        this.labelData = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

}
