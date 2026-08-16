
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="card_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="authorization_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}authorization_method" minOccurs="0"/>
 *         &lt;element name="credit_flag" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}credit_flag"/>
 *         &lt;element name="entry_method" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}entry_method" minOccurs="0"/>
 *         &lt;element name="original_balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="remaining_balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="settlement_data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "cardNumber",
    "authorizationCode",
    "authorizationDatetime",
    "authorizationMethod",
    "creditFlag",
    "entryMethod",
    "originalBalance",
    "remainingBalance",
    "settlementData"
})
@XmlRootElement(name = "GiftCardTender")
public class GiftCardTender {

    @XmlElement(name = "card_number", required = true)
    protected String cardNumber;
    @XmlElement(name = "authorization_code", required = true)
    protected String authorizationCode;
    @XmlElement(name = "authorization_datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authorizationDatetime;
    @XmlElement(name = "authorization_method")
    protected AuthorizationMethod authorizationMethod;
    @XmlElement(name = "credit_flag", required = true)
    protected CreditFlag creditFlag;
    @XmlElement(name = "entry_method")
    protected EntryMethod entryMethod;
    @XmlElement(name = "original_balance")
    protected BigDecimal originalBalance;
    @XmlElement(name = "remaining_balance")
    protected BigDecimal remainingBalance;
    @XmlElement(name = "settlement_data")
    protected String settlementData;

    /**
     * Gets the value of the cardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Sets the value of the cardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardNumber(String value) {
        this.cardNumber = value;
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
     * Gets the value of the creditFlag property.
     * 
     * @return
     *     possible object is
     *     {@link CreditFlag }
     *     
     */
    public CreditFlag getCreditFlag() {
        return creditFlag;
    }

    /**
     * Sets the value of the creditFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditFlag }
     *     
     */
    public void setCreditFlag(CreditFlag value) {
        this.creditFlag = value;
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
     * Gets the value of the originalBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOriginalBalance() {
        return originalBalance;
    }

    /**
     * Sets the value of the originalBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOriginalBalance(BigDecimal value) {
        this.originalBalance = value;
    }

    /**
     * Gets the value of the remainingBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * Sets the value of the remainingBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRemainingBalance(BigDecimal value) {
        this.remainingBalance = value;
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

}
