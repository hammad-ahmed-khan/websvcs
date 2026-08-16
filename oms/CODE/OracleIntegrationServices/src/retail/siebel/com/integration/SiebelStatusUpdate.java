
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
 * <p>Java class for SiebelStatusUpdate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SiebelStatusUpdate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="application_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="oms_customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cust_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sub_cust_order_no">
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
 *         &lt;element name="consumer_dly_time" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="close_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="cancel_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CustomerOrderStatusUpdateDetails" type="{http://com.siebel.retail/integration/}SiebelStatusUpdateDetails" maxOccurs="100"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiebelStatusUpdate", propOrder = {
    "applicationId",
    "omsCustomerOrderNo",
    "custOrderNo",
    "subCustOrderNo",
    "custId",
    "consumerDlyTime",
    "closeDatetime",
    "cancelDatetime",
    "customerOrderStatusUpdateDetails"
})
@XmlRootElement
public class SiebelStatusUpdate {

    @XmlElement(name = "application_id", required = true)
    protected String applicationId;
    @XmlElement(name = "oms_customer_order_no", required = true, type = Long.class, nillable = true)
    protected Long omsCustomerOrderNo;
    @XmlElement(name = "cust_order_no", required = true)
    protected String custOrderNo;
    @XmlElement(name = "sub_cust_order_no", required = true)
    protected String subCustOrderNo;
    @XmlElement(name = "cust_id", required = true)
    protected String custId;
    @XmlElement(name = "consumer_dly_time", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar consumerDlyTime;
    @XmlElement(name = "close_datetime", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar closeDatetime;
    @XmlElement(name = "cancel_datetime", nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar cancelDatetime;
    @XmlElement(name = "CustomerOrderStatusUpdateDetails", required = true, nillable = true)
    protected List<SiebelStatusUpdateDetails> customerOrderStatusUpdateDetails;

    /**
     * Gets the value of the applicationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the value of the applicationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationId(String value) {
        this.applicationId = value;
    }

    /**
     * Gets the value of the omsCustomerOrderNo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOmsCustomerOrderNo() {
        return omsCustomerOrderNo;
    }

    /**
     * Sets the value of the omsCustomerOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOmsCustomerOrderNo(Long value) {
        this.omsCustomerOrderNo = value;
    }

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
     *     {@link String }
     *     
     */
    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    /**
     * Sets the value of the subCustOrderNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubCustOrderNo(String value) {
        this.subCustOrderNo = value;
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
     * Gets the value of the closeDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCloseDatetime() {
        return closeDatetime;
    }

    /**
     * Sets the value of the closeDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCloseDatetime(XMLGregorianCalendar value) {
        this.closeDatetime = value;
    }

    /**
     * Gets the value of the cancelDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCancelDatetime() {
        return cancelDatetime;
    }

    /**
     * Sets the value of the cancelDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCancelDatetime(XMLGregorianCalendar value) {
        this.cancelDatetime = value;
    }

    /**
     * Gets the value of the customerOrderStatusUpdateDetails property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrderStatusUpdateDetails property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     * getCustomerOrderStatusUpdateDetails().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link .retail.siebel.com.integration.SiebelStatusUpdateDetails}
     *
     *
     */
    public List<SiebelStatusUpdateDetails> getCustomerOrderStatusUpdateDetails() {
        if (customerOrderStatusUpdateDetails == null) {
            customerOrderStatusUpdateDetails = new ArrayList<SiebelStatusUpdateDetails>();
        }
        return this.customerOrderStatusUpdateDetails;
    }

}
