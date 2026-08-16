
package retail.siebel.com.integration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cust_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_last_name" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_add_1" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_add_2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_add_3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_city" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_state">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_country">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_post" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="deliver_phone_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="15"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_add_1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_add_2" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_add_3" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_city">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_state">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_country" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bill_post" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
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
@XmlType(name = "CustomerDetails", propOrder = {
    "custId",
    "deliverFirstName",
    "deliverLastName",
    "deliverAdd1",
    "deliverAdd2",
    "deliverAdd3",
    "deliverCity",
    "deliverState",
    "deliverCountry",
    "deliverPost",
    "deliverPhoneNo",
    "billFirstName",
    "billLastName",
    "billAdd1",
    "billAdd2",
    "billAdd3",
    "billCity",
    "billState",
    "billCountry",
    "billPost"
})
@XmlRootElement
public class CustomerDetails {

    @XmlElement(name = "cust_id", required = true)
    protected String custId;
    @XmlElement(name = "deliver_first_name", required = true, nillable = true)
    protected String deliverFirstName;
    @XmlElement(name = "deliver_last_name", nillable = true)
    protected String deliverLastName;
    @XmlElement(name = "deliver_add_1", nillable = true)
    protected String deliverAdd1;
    @XmlElement(name = "deliver_add_2", nillable = true)
    protected String deliverAdd2;
    @XmlElement(name = "deliver_add_3", nillable = true)
    protected String deliverAdd3;
    @XmlElement(name = "deliver_city", nillable = true)
    protected String deliverCity;
    @XmlElement(name = "deliver_state", required = true, nillable = true)
    protected String deliverState;
    @XmlElement(name = "deliver_country", required = true, nillable = true)
    protected String deliverCountry;
    @XmlElement(name = "deliver_post", nillable = true)
    protected String deliverPost;
    @XmlElement(name = "deliver_phone_no")
    protected String deliverPhoneNo;
    @XmlElement(name = "bill_first_name", required = true, nillable = true)
    protected String billFirstName;
    @XmlElement(name = "bill_last_name", required = true, nillable = true)
    protected String billLastName;
    @XmlElement(name = "bill_add_1", required = true, nillable = true)
    protected String billAdd1;
    @XmlElement(name = "bill_add_2", nillable = true)
    protected String billAdd2;
    @XmlElement(name = "bill_add_3", nillable = true)
    protected String billAdd3;
    @XmlElement(name = "bill_city", required = true, nillable = true)
    protected String billCity;
    @XmlElement(name = "bill_state", required = true, nillable = true)
    protected String billState;
    @XmlElement(name = "bill_country", nillable = true)
    protected String billCountry;
    @XmlElement(name = "bill_post", nillable = true)
    protected String billPost;

    /**
     * Gets the value of the custId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustId() {
        return custId;
    }

    /**
     * Sets the value of the custId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustId(String value) {
        this.custId = value;
    }

    /**
     * Gets the value of the deliverFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverFirstName() {
        return deliverFirstName;
    }

    /**
     * Sets the value of the deliverFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverFirstName(String value) {
        this.deliverFirstName = value;
    }

    /**
     * Gets the value of the deliverLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverLastName() {
        return deliverLastName;
    }

    /**
     * Sets the value of the deliverLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverLastName(String value) {
        this.deliverLastName = value;
    }

    /**
     * Gets the value of the deliverAdd1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd1() {
        return deliverAdd1;
    }

    /**
     * Sets the value of the deliverAdd1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd1(String value) {
        this.deliverAdd1 = value;
    }

    /**
     * Gets the value of the deliverAdd2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd2() {
        return deliverAdd2;
    }

    /**
     * Sets the value of the deliverAdd2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd2(String value) {
        this.deliverAdd2 = value;
    }

    /**
     * Gets the value of the deliverAdd3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverAdd3() {
        return deliverAdd3;
    }

    /**
     * Sets the value of the deliverAdd3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverAdd3(String value) {
        this.deliverAdd3 = value;
    }

    /**
     * Gets the value of the deliverCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCity() {
        return deliverCity;
    }

    /**
     * Sets the value of the deliverCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCity(String value) {
        this.deliverCity = value;
    }

    /**
     * Gets the value of the deliverState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverState() {
        return deliverState;
    }

    /**
     * Sets the value of the deliverState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverState(String value) {
        this.deliverState = value;
    }

    /**
     * Gets the value of the deliverCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverCountry() {
        return deliverCountry;
    }

    /**
     * Sets the value of the deliverCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverCountry(String value) {
        this.deliverCountry = value;
    }

    /**
     * Gets the value of the deliverPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPost() {
        return deliverPost;
    }

    /**
     * Sets the value of the deliverPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPost(String value) {
        this.deliverPost = value;
    }

    /**
     * Gets the value of the deliverPhoneNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliverPhoneNo() {
        return deliverPhoneNo;
    }

    /**
     * Sets the value of the deliverPhoneNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliverPhoneNo(String value) {
        this.deliverPhoneNo = value;
    }

    /**
     * Gets the value of the billFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillFirstName() {
        return billFirstName;
    }

    /**
     * Sets the value of the billFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillFirstName(String value) {
        this.billFirstName = value;
    }

    /**
     * Gets the value of the billLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillLastName() {
        return billLastName;
    }

    /**
     * Sets the value of the billLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillLastName(String value) {
        this.billLastName = value;
    }

    /**
     * Gets the value of the billAdd1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd1() {
        return billAdd1;
    }

    /**
     * Sets the value of the billAdd1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd1(String value) {
        this.billAdd1 = value;
    }

    /**
     * Gets the value of the billAdd2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd2() {
        return billAdd2;
    }

    /**
     * Sets the value of the billAdd2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd2(String value) {
        this.billAdd2 = value;
    }

    /**
     * Gets the value of the billAdd3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillAdd3() {
        return billAdd3;
    }

    /**
     * Sets the value of the billAdd3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillAdd3(String value) {
        this.billAdd3 = value;
    }

    /**
     * Gets the value of the billCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCity() {
        return billCity;
    }

    /**
     * Sets the value of the billCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCity(String value) {
        this.billCity = value;
    }

    /**
     * Gets the value of the billState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillState() {
        return billState;
    }

    /**
     * Sets the value of the billState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillState(String value) {
        this.billState = value;
    }

    /**
     * Gets the value of the billCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillCountry() {
        return billCountry;
    }

    /**
     * Sets the value of the billCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillCountry(String value) {
        this.billCountry = value;
    }

    /**
     * Gets the value of the billPost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBillPost() {
        return billPost;
    }

    /**
     * Sets the value of the billPost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBillPost(String value) {
        this.billPost = value;
    }

}
