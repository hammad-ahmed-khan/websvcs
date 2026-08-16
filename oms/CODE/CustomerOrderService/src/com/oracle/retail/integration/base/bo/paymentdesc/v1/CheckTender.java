
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="bank_id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="account_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="micr_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="check_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}authorization_method" minOccurs="0"/>
 *         &lt;element name="personal_id_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_issuer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_issuer_co_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_issuer_state_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_swiped_flag" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}personal_id_swiped_flag" minOccurs="0"/>
 *         &lt;element name="customer_phone_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}entry_method" minOccurs="0"/>
 *         &lt;element name="echeck_conversion_code" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}echeck_conversion_code" minOccurs="0"/>
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
    "bankId",
    "accountNumber",
    "micrNumber",
    "checkNumber",
    "authorizationCode",
    "authorizationMethod",
    "personalIdNumber",
    "personalIdIssuer",
    "personalIdIssuerCoCode",
    "personalIdIssuerStateCode",
    "personalIdSwipedFlag",
    "customerPhoneNumber",
    "entryMethod",
    "echeckConversionCode"
})
@XmlRootElement(name = "CheckTender")
public class CheckTender {

    @XmlElement(name = "bank_id", required = true)
    protected String bankId;
    @XmlElement(name = "account_number", required = true)
    protected String accountNumber;
    @XmlElement(name = "micr_number", required = true)
    protected String micrNumber;
    @XmlElement(name = "check_number", required = true)
    protected String checkNumber;
    @XmlElement(name = "authorization_code", required = true)
    protected String authorizationCode;
    @XmlElement(name = "authorization_method")
    protected AuthorizationMethod authorizationMethod;
    @XmlElement(name = "personal_id_number")
    protected String personalIdNumber;
    @XmlElement(name = "personal_id_issuer")
    protected String personalIdIssuer;
    @XmlElement(name = "personal_id_issuer_co_code")
    protected String personalIdIssuerCoCode;
    @XmlElement(name = "personal_id_issuer_state_code")
    protected String personalIdIssuerStateCode;
    @XmlElement(name = "personal_id_swiped_flag")
    protected PersonalIdSwipedFlag personalIdSwipedFlag;
    @XmlElement(name = "customer_phone_number")
    protected String customerPhoneNumber;
    @XmlElement(name = "entry_method")
    protected EntryMethod entryMethod;
    @XmlElement(name = "echeck_conversion_code")
    protected EcheckConversionCode echeckConversionCode;

    /**
     * Gets the value of the bankId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * Sets the value of the bankId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBankId(String value) {
        this.bankId = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the micrNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMicrNumber() {
        return micrNumber;
    }

    /**
     * Sets the value of the micrNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMicrNumber(String value) {
        this.micrNumber = value;
    }

    /**
     * Gets the value of the checkNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckNumber() {
        return checkNumber;
    }

    /**
     * Sets the value of the checkNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckNumber(String value) {
        this.checkNumber = value;
    }

    /**
     * Gets the value of the authorizationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * Sets the value of the authorizationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthorizationCode(String value) {
        this.authorizationCode = value;
    }

    /**
     * Gets the value of the authorizationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link AuthorizationMethod }
     *     
     */
    public AuthorizationMethod getAuthorizationMethod() {
        return authorizationMethod;
    }

    /**
     * Sets the value of the authorizationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthorizationMethod }
     *     
     */
    public void setAuthorizationMethod(AuthorizationMethod value) {
        this.authorizationMethod = value;
    }

    /**
     * Gets the value of the personalIdNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    /**
     * Sets the value of the personalIdNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdNumber(String value) {
        this.personalIdNumber = value;
    }

    /**
     * Gets the value of the personalIdIssuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdIssuer() {
        return personalIdIssuer;
    }

    /**
     * Sets the value of the personalIdIssuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdIssuer(String value) {
        this.personalIdIssuer = value;
    }

    /**
     * Gets the value of the personalIdIssuerCoCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdIssuerCoCode() {
        return personalIdIssuerCoCode;
    }

    /**
     * Sets the value of the personalIdIssuerCoCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdIssuerCoCode(String value) {
        this.personalIdIssuerCoCode = value;
    }

    /**
     * Gets the value of the personalIdIssuerStateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdIssuerStateCode() {
        return personalIdIssuerStateCode;
    }

    /**
     * Sets the value of the personalIdIssuerStateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdIssuerStateCode(String value) {
        this.personalIdIssuerStateCode = value;
    }

    /**
     * Gets the value of the personalIdSwipedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link PersonalIdSwipedFlag }
     *     
     */
    public PersonalIdSwipedFlag getPersonalIdSwipedFlag() {
        return personalIdSwipedFlag;
    }

    /**
     * Sets the value of the personalIdSwipedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonalIdSwipedFlag }
     *     
     */
    public void setPersonalIdSwipedFlag(PersonalIdSwipedFlag value) {
        this.personalIdSwipedFlag = value;
    }

    /**
     * Gets the value of the customerPhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Sets the value of the customerPhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerPhoneNumber(String value) {
        this.customerPhoneNumber = value;
    }

    /**
     * Gets the value of the entryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link EntryMethod }
     *     
     */
    public EntryMethod getEntryMethod() {
        return entryMethod;
    }

    /**
     * Sets the value of the entryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntryMethod }
     *     
     */
    public void setEntryMethod(EntryMethod value) {
        this.entryMethod = value;
    }

    /**
     * Gets the value of the echeckConversionCode property.
     * 
     * @return
     *     possible object is
     *     {@link EcheckConversionCode }
     *     
     */
    public EcheckConversionCode getEcheckConversionCode() {
        return echeckConversionCode;
    }

    /**
     * Sets the value of the echeckConversionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link EcheckConversionCode }
     *     
     */
    public void setEcheckConversionCode(EcheckConversionCode value) {
        this.echeckConversionCode = value;
    }

}
