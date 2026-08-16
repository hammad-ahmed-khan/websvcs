
package com.oracle.retail.integration.base.bo.customerdesc.v1;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.localedesc.v1.LocaleDesc;


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
 *         &lt;element name="customer_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customer_type" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}customer_type"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1}ContactDesc"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}AddrBook" minOccurs="0"/>
 *         &lt;element name="gender_type" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}gender_type" minOccurs="0"/>
 *         &lt;element name="birth_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="contact_by_mail" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}flag"/>
 *         &lt;element name="contact_by_phone" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}flag"/>
 *         &lt;element name="contact_by_email" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}flag"/>
 *         &lt;element name="receipt_preference" type="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}receipt_preference" minOccurs="0"/>
 *         &lt;element name="employee_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pricing_group_id" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/CustomerDesc/v1}CustomerGrpIdLst" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/LocaleDesc/v1}LocaleDesc" minOccurs="0"/>
 *         &lt;element name="tax_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tax_certificate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reason_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="job_title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "customerId",
    "customerType",
    "contactDesc",
    "addrBook",
    "genderType",
    "birthDate",
    "contactByMail",
    "contactByPhone",
    "contactByEmail",
    "receiptPreference",
    "employeeId",
    "pricingGroupId",
    "customerGrpIdLst",
    "localeDesc",
    "taxId",
    "taxCertificate",
    "reasonCode",
    "jobTitle"
})
@XmlRootElement(name = "CustomerDesc")
public class CustomerDesc {

    @XmlElement(name = "customer_id", required = true)
    protected String customerId;
    @XmlElement(name = "customer_type", required = true)
    protected CustomerType customerType;
    @XmlElement(name = "ContactDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/ContactDesc/v1", required = true)
    protected ContactDesc contactDesc;
    @XmlElement(name = "AddrBook")
    protected AddrBook addrBook;
    @XmlElement(name = "gender_type")
    protected GenderType genderType;
    @XmlElement(name = "birth_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar birthDate;
    @XmlElement(name = "contact_by_mail", required = true)
    protected Flag contactByMail;
    @XmlElement(name = "contact_by_phone", required = true)
    protected Flag contactByPhone;
    @XmlElement(name = "contact_by_email", required = true)
    protected Flag contactByEmail;
    @XmlElement(name = "receipt_preference")
    protected ReceiptPreference receiptPreference;
    @XmlElement(name = "employee_id")
    protected String employeeId;
    @XmlElement(name = "pricing_group_id")
    protected BigDecimal pricingGroupId;
    @XmlElement(name = "CustomerGrpIdLst")
    protected CustomerGrpIdLst customerGrpIdLst;
    @XmlElement(name = "LocaleDesc", namespace = "http://www.oracle.com/retail/integration/base/bo/LocaleDesc/v1")
    protected LocaleDesc localeDesc;
    @XmlElement(name = "tax_id")
    protected String taxId;
    @XmlElement(name = "tax_certificate")
    protected String taxCertificate;
    @XmlElement(name = "reason_code")
    protected String reasonCode;
    @XmlElement(name = "job_title")
    protected String jobTitle;

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
     * Gets the value of the customerType property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerType }
     *     
     */
    public CustomerType getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerType }
     *     
     */
    public void setCustomerType(CustomerType value) {
        this.customerType = value;
    }

    /**
     * Contains the contact information for the customer.
     * 
     * @return
     *     possible object is
     *     {@link ContactDesc }
     *     
     */
    public ContactDesc getContactDesc() {
        return contactDesc;
    }

    /**
     * Sets the value of the contactDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactDesc }
     *     
     */
    public void setContactDesc(ContactDesc value) {
        this.contactDesc = value;
    }

    /**
     * Contains multiple customer addresses.
     * 
     * @return
     *     possible object is
     *     {@link AddrBook }
     *     
     */
    public AddrBook getAddrBook() {
        return addrBook;
    }

    /**
     * Sets the value of the addrBook property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddrBook }
     *     
     */
    public void setAddrBook(AddrBook value) {
        this.addrBook = value;
    }

    /**
     * Gets the value of the genderType property.
     * 
     * @return
     *     possible object is
     *     {@link GenderType }
     *     
     */
    public GenderType getGenderType() {
        return genderType;
    }

    /**
     * Sets the value of the genderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenderType }
     *     
     */
    public void setGenderType(GenderType value) {
        this.genderType = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the contactByMail property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getContactByMail() {
        return contactByMail;
    }

    /**
     * Sets the value of the contactByMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setContactByMail(Flag value) {
        this.contactByMail = value;
    }

    /**
     * Gets the value of the contactByPhone property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getContactByPhone() {
        return contactByPhone;
    }

    /**
     * Sets the value of the contactByPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setContactByPhone(Flag value) {
        this.contactByPhone = value;
    }

    /**
     * Gets the value of the contactByEmail property.
     * 
     * @return
     *     possible object is
     *     {@link Flag }
     *     
     */
    public Flag getContactByEmail() {
        return contactByEmail;
    }

    /**
     * Sets the value of the contactByEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flag }
     *     
     */
    public void setContactByEmail(Flag value) {
        this.contactByEmail = value;
    }

    /**
     * Gets the value of the receiptPreference property.
     * 
     * @return
     *     possible object is
     *     {@link ReceiptPreference }
     *     
     */
    public ReceiptPreference getReceiptPreference() {
        return receiptPreference;
    }

    /**
     * Sets the value of the receiptPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReceiptPreference }
     *     
     */
    public void setReceiptPreference(ReceiptPreference value) {
        this.receiptPreference = value;
    }

    /**
     * Gets the value of the employeeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the value of the employeeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployeeId(String value) {
        this.employeeId = value;
    }

    /**
     * Gets the value of the pricingGroupId property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPricingGroupId() {
        return pricingGroupId;
    }

    /**
     * Sets the value of the pricingGroupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPricingGroupId(BigDecimal value) {
        this.pricingGroupId = value;
    }

    /**
     * Contains a collection of preferred customer discount ids.
     * 
     * @return
     *     possible object is
     *     {@link CustomerGrpIdLst }
     *     
     */
    public CustomerGrpIdLst getCustomerGrpIdLst() {
        return customerGrpIdLst;
    }

    /**
     * Sets the value of the customerGrpIdLst property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerGrpIdLst }
     *     
     */
    public void setCustomerGrpIdLst(CustomerGrpIdLst value) {
        this.customerGrpIdLst = value;
    }

    /**
     * Contains the customer preferred language.
     * 
     * @return
     *     possible object is
     *     {@link LocaleDesc }
     *     
     */
    public LocaleDesc getLocaleDesc() {
        return localeDesc;
    }

    /**
     * Sets the value of the localeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocaleDesc }
     *     
     */
    public void setLocaleDesc(LocaleDesc value) {
        this.localeDesc = value;
    }

    /**
     * Gets the value of the taxId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * Sets the value of the taxId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxId(String value) {
        this.taxId = value;
    }

    /**
     * Gets the value of the taxCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxCertificate() {
        return taxCertificate;
    }

    /**
     * Sets the value of the taxCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxCertificate(String value) {
        this.taxCertificate = value;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonCode(String value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the jobTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * Sets the value of the jobTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTitle(String value) {
        this.jobTitle = value;
    }

}
