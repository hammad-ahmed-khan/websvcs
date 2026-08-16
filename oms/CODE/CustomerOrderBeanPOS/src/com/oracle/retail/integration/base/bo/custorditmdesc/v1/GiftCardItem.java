
package com.oracle.retail.integration.base.bo.custorditmdesc.v1;

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
 *         &lt;element name="request_type" type="{http://www.oracle.com/retail/integration/base/bo/CustOrdItmDesc/v1}giftcard_req_type"/>
 *         &lt;element name="authorization_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="authorization_datetime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="original_balance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="current_balance" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
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
    "requestType",
    "authorizationCode",
    "authorizationDatetime",
    "originalBalance",
    "currentBalance",
    "settlementData"
})
@XmlRootElement(name = "GiftCardItem")
public class GiftCardItem {

    @XmlElement(name = "card_number", required = true)
    protected String cardNumber;
    @XmlElement(name = "request_type", required = true)
    protected GiftcardReqType requestType;
    @XmlElement(name = "authorization_code", required = true)
    protected String authorizationCode;
    @XmlElement(name = "authorization_datetime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar authorizationDatetime;
    @XmlElement(name = "original_balance")
    protected BigDecimal originalBalance;
    @XmlElement(name = "current_balance", required = true)
    protected BigDecimal currentBalance;
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
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link GiftcardReqType }
     *     
     */
    public GiftcardReqType getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiftcardReqType }
     *     
     */
    public void setRequestType(GiftcardReqType value) {
        this.requestType = value;
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
     * Gets the value of the currentBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets the value of the currentBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCurrentBalance(BigDecimal value) {
        this.currentBalance = value;
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
