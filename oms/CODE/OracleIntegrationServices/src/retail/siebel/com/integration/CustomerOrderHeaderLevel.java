
package retail.siebel.com.integration;

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
 * <p>Java class for CustomerOrderHeaderLevel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerOrderHeaderLevel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cust_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sub_cust_order_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oms_cust_ord_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cust_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cust_order_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="order_requestor_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_phone_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="delivery_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="pick_loc" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="payment_status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="7"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="consumer_dly_time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="comment" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="create_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="ItemLevelDetails" type="{http://com.siebel.retail/integration/}ItemLevelDetails" maxOccurs="100"/>
 *         &lt;element name="CustomerDetails" type="{http://com.siebel.retail/integration/}CustomerDetails" minOccurs="0"/>
 *         &lt;element name="TenderDetails" type="{http://com.siebel.retail/integration/}TenderDetails" maxOccurs="100" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerOrderHeaderLevel", propOrder = {
    "custOrderNo",
    "subCustOrderNo",
    "omsCustOrdNo",
    "custId",
    "custOrderType",
    "orderRequestorId",
    "customerPhoneNo",
    "deliveryType",
    "firstName",
    "lastName",
    "pickLoc",
    "paymentStatus",
    "consumerDlyTime",
    "comment",
    "createDatetime",
    "itemLevelDetails",
    "customerDetails",
    "tenderDetails"
})
@XmlRootElement
public class CustomerOrderHeaderLevel {

    @XmlElement(name = "cust_order_no", required = true)
    protected String custOrderNo;
    @XmlElement(name = "sub_cust_order_no")
    protected Long subCustOrderNo;
    @XmlElement(name = "oms_cust_ord_no", required = true)
    protected String omsCustOrdNo;
    @XmlElement(name = "cust_id", required = true)
    protected String custId;
    @XmlElement(name = "cust_order_type", required = true)
    protected String custOrderType;
    @XmlElement(name = "order_requestor_id", required = true)
    protected String orderRequestorId;
    @XmlElement(name = "customer_phone_no", required = true)
    protected String customerPhoneNo;
    @XmlElement(name = "delivery_type", required = true)
    protected String deliveryType;
    @XmlElement(name = "first_name", required = true)
    protected String firstName;
    @XmlElement(name = "last_name", required = true)
    protected String lastName;
    @XmlElement(name = "pick_loc", nillable = true)
    protected Long pickLoc;
    @XmlElement(name = "payment_status", required = true)
    protected String paymentStatus;
    @XmlElement(name = "consumer_dly_time", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDlyTime;
    @XmlElement(nillable = true)
    protected String comment;
    @XmlElement(name = "create_datetime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createDatetime;
    @XmlElement(name = "ItemLevelDetails", required = true, nillable = true)
    protected List<ItemLevelDetails> itemLevelDetails;
    @XmlElement(name = "CustomerDetails", nillable = true)
    protected CustomerDetails customerDetails;
    @XmlElement(name = "TenderDetails", nillable = true)
    protected List<TenderDetails> tenderDetails;

    /**
     * Gets the value of the custOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustOrderNo() {
        return custOrderNo;
    }

    /**
     * Sets the value of the custOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustOrderNo(String value) {
        this.custOrderNo = value;
    }

    /**
     * Gets the value of the subCustOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSubCustOrderNo() {
        return subCustOrderNo;
    }

    /**
     * Sets the value of the subCustOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSubCustOrderNo(Long value) {
        this.subCustOrderNo = value;
    }

    /**
     * Gets the value of the omsCustOrdNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    /**
     * Sets the value of the omsCustOrdNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOmsCustOrdNo(String value) {
        this.omsCustOrdNo = value;
    }

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
     * Gets the value of the custOrderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustOrderType() {
        return custOrderType;
    }

    /**
     * Sets the value of the custOrderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustOrderType(String value) {
        this.custOrderType = value;
    }

    /**
     * Gets the value of the orderRequestorId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderRequestorId() {
        return orderRequestorId;
    }

    /**
     * Sets the value of the orderRequestorId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderRequestorId(String value) {
        this.orderRequestorId = value;
    }

    /**
     * Gets the value of the customerPhoneNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    /**
     * Sets the value of the customerPhoneNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerPhoneNo(String value) {
        this.customerPhoneNo = value;
    }

    /**
     * Gets the value of the deliveryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryType() {
        return deliveryType;
    }

    /**
     * Sets the value of the deliveryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryType(String value) {
        this.deliveryType = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the pickLoc property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPickLoc() {
        return pickLoc;
    }

    /**
     * Sets the value of the pickLoc property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPickLoc(Long value) {
        this.pickLoc = value;
    }

    /**
     * Gets the value of the paymentStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets the value of the paymentStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentStatus(String value) {
        this.paymentStatus = value;
    }

    /**
     * Gets the value of the consumerDlyTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getConsumerDlyTime() {
        return consumerDlyTime;
    }

    /**
     * Sets the value of the consumerDlyTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setConsumerDlyTime(XMLGregorianCalendar value) {
        this.consumerDlyTime = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the createDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreateDatetime() {
        return createDatetime;
    }

    /**
     * Sets the value of the createDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreateDatetime(XMLGregorianCalendar value) {
        this.createDatetime = value;
    }

    /**
     * Gets the value of the itemLevelDetails property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the itemLevelDetails property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getItemLevelDetails().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .retail.siebel.com.integration.ItemLevelDetails}
     *
     *
     */
    public List<ItemLevelDetails> getItemLevelDetails() {
        if (itemLevelDetails == null) {
            itemLevelDetails = new ArrayList<ItemLevelDetails>();
        }
        return this.itemLevelDetails;
    }

    /**
     * Gets the value of the customerDetails property.
     *
     * @return
     * possible object is
     * {@link .retail.siebel.com.integration.CustomerDetails}
     *
     */
    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    /**
     * Sets the value of the customerDetails property.
     *
     * @param value
     * allowed object is
     * {@link .retail.siebel.com.integration.CustomerDetails}
     *
     */
    public void setCustomerDetails(CustomerDetails value) {
        this.customerDetails = value;
    }

    /**
     * Gets the value of the tenderDetails property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tenderDetails property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getTenderDetails().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .retail.siebel.com.integration.TenderDetails}
     *
     *
     */
    public List<TenderDetails> getTenderDetails() {
        if (tenderDetails == null) {
            tenderDetails = new ArrayList<TenderDetails>();
        }
        return this.tenderDetails;
    }

}
