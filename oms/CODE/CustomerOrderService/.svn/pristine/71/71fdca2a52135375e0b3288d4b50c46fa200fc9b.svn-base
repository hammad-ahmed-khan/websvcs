
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

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
 *         &lt;element name="masked_account_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="card_token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="card_type" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}card_type" minOccurs="0"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}entry_method" minOccurs="0"/>
 *         &lt;element name="authorization_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="authorization_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}authorization_method" minOccurs="0"/>
 *         &lt;element name="personal_id_country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personal_id_expiration_date" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="prepaid_balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="account_apr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="account_apr_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion_apr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion_apr_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion_description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotion_duration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="settlement_data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature_data" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="additional_security_info" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "maskedAccountNumber",
    "cardToken",
    "cardType",
    "entryMethod",
    "authorizationCode",
    "authorizationDatetime",
    "authorizationMethod",
    "personalIdCountry",
    "personalIdState",
    "personalIdExpirationDate",
    "prepaidBalance",
    "accountApr",
    "accountAprType",
    "promotionApr",
    "promotionAprType",
    "promotionDescription",
    "promotionDuration",
    "settlementData",
    "signatureData",
    "additionalSecurityInfo"
})
@XmlRootElement(name = "CreditDebitTender")
public class CreditDebitTender {

    @XmlElement(name = "masked_account_number", required = true)
    protected String maskedAccountNumber;
    @XmlElement(name = "card_token")
    protected String cardToken;
    @XmlElement(name = "card_type")
    protected CardType cardType;
    @XmlElement(name = "entry_method")
    protected EntryMethod entryMethod;
    @XmlElement(name = "authorization_code", required = true)
    protected String authorizationCode;
    @XmlElement(name = "authorization_datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authorizationDatetime;
    @XmlElement(name = "authorization_method")
    protected AuthorizationMethod authorizationMethod;
    @XmlElement(name = "personal_id_country")
    protected String personalIdCountry;
    @XmlElement(name = "personal_id_state")
    protected String personalIdState;
    @XmlElement(name = "personal_id_expiration_date")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar personalIdExpirationDate;
    @XmlElement(name = "prepaid_balance")
    protected BigDecimal prepaidBalance;
    @XmlElement(name = "account_apr")
    protected String accountApr;
    @XmlElement(name = "account_apr_type")
    protected String accountAprType;
    @XmlElement(name = "promotion_apr")
    protected String promotionApr;
    @XmlElement(name = "promotion_apr_type")
    protected String promotionAprType;
    @XmlElement(name = "promotion_description")
    protected String promotionDescription;
    @XmlElement(name = "promotion_duration")
    protected String promotionDuration;
    @XmlElement(name = "settlement_data")
    protected String settlementData;
    @XmlElement(name = "signature_data")
    protected List<String> signatureData;
    @XmlElement(name = "additional_security_info")
    protected String additionalSecurityInfo;

    /**
     * Gets the value of the maskedAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    /**
     * Sets the value of the maskedAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaskedAccountNumber(String value) {
        this.maskedAccountNumber = value;
    }

    /**
     * Gets the value of the cardToken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardToken() {
        return cardToken;
    }

    /**
     * Sets the value of the cardToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardToken(String value) {
        this.cardToken = value;
    }

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link CardType }
     *     
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CardType }
     *     
     */
    public void setCardType(CardType value) {
        this.cardType = value;
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
     * Gets the value of the authorizationDatetime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAuthorizationDatetime() {
        return authorizationDatetime;
    }

    /**
     * Sets the value of the authorizationDatetime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAuthorizationDatetime(XMLGregorianCalendar value) {
        this.authorizationDatetime = value;
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
     * Gets the value of the personalIdCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdCountry() {
        return personalIdCountry;
    }

    /**
     * Sets the value of the personalIdCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdCountry(String value) {
        this.personalIdCountry = value;
    }

    /**
     * Gets the value of the personalIdState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalIdState() {
        return personalIdState;
    }

    /**
     * Sets the value of the personalIdState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalIdState(String value) {
        this.personalIdState = value;
    }

    /**
     * Gets the value of the personalIdExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPersonalIdExpirationDate() {
        return personalIdExpirationDate;
    }

    /**
     * Sets the value of the personalIdExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPersonalIdExpirationDate(XMLGregorianCalendar value) {
        this.personalIdExpirationDate = value;
    }

    /**
     * Gets the value of the prepaidBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrepaidBalance() {
        return prepaidBalance;
    }

    /**
     * Sets the value of the prepaidBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrepaidBalance(BigDecimal value) {
        this.prepaidBalance = value;
    }

    /**
     * Gets the value of the accountApr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountApr() {
        return accountApr;
    }

    /**
     * Sets the value of the accountApr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountApr(String value) {
        this.accountApr = value;
    }

    /**
     * Gets the value of the accountAprType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountAprType() {
        return accountAprType;
    }

    /**
     * Sets the value of the accountAprType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountAprType(String value) {
        this.accountAprType = value;
    }

    /**
     * Gets the value of the promotionApr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionApr() {
        return promotionApr;
    }

    /**
     * Sets the value of the promotionApr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionApr(String value) {
        this.promotionApr = value;
    }

    /**
     * Gets the value of the promotionAprType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionAprType() {
        return promotionAprType;
    }

    /**
     * Sets the value of the promotionAprType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionAprType(String value) {
        this.promotionAprType = value;
    }

    /**
     * Gets the value of the promotionDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionDescription() {
        return promotionDescription;
    }

    /**
     * Sets the value of the promotionDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionDescription(String value) {
        this.promotionDescription = value;
    }

    /**
     * Gets the value of the promotionDuration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionDuration() {
        return promotionDuration;
    }

    /**
     * Sets the value of the promotionDuration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionDuration(String value) {
        this.promotionDuration = value;
    }

    /**
     * Gets the value of the settlementData property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSettlementData() {
        return settlementData;
    }

    /**
     * Sets the value of the settlementData property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSettlementData(String value) {
        this.settlementData = value;
    }

    /**
     * Gets the value of the signatureData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatureData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatureData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSignatureData() {
        if (signatureData == null) {
            signatureData = new ArrayList<String>();
        }
        return this.signatureData;
    }

    /**
     * Gets the value of the additionalSecurityInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalSecurityInfo() {
        return additionalSecurityInfo;
    }

    /**
     * Sets the value of the additionalSecurityInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalSecurityInfo(String value) {
        this.additionalSecurityInfo = value;
    }

}
