
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import java.math.BigDecimal;
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
 *         &lt;element name="seq_no" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="payment_type" type="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}payment_type"/>
 *         &lt;element name="currency_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="alternate_currency_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alternate_amount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}CreditDebitTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}CheckTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}CouponTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}GiftCardTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}GiftCertTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}MailCheckTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}PurchaseOrdTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}StoreCreditTender" minOccurs="0"/>
 *         &lt;element ref="{http://www.oracle.com/retail/integration/base/bo/PaymentDesc/v1}TravelCheckTender" minOccurs="0"/>
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
    "seqNo",
    "paymentType",
    "currencyCode",
    "amount",
    "alternateCurrencyCode",
    "alternateAmount",
    "creditDebitTender",
    "checkTender",
    "couponTender",
    "giftCardTender",
    "giftCertTender",
    "mailCheckTender",
    "purchaseOrdTender",
    "storeCreditTender",
    "travelCheckTender"
})
@XmlRootElement(name = "PaymentDesc")
public class PaymentDesc {

    @XmlElement(name = "seq_no")
    protected int seqNo;
    @XmlElement(name = "payment_type", required = true)
    protected PaymentType paymentType;
    @XmlElement(name = "currency_code", required = true)
    protected String currencyCode;
    @XmlElement(required = true)
    protected BigDecimal amount;
    @XmlElement(name = "alternate_currency_code")
    protected String alternateCurrencyCode;
    @XmlElement(name = "alternate_amount")
    protected BigDecimal alternateAmount;
    @XmlElement(name = "CreditDebitTender")
    protected CreditDebitTender creditDebitTender;
    @XmlElement(name = "CheckTender")
    protected CheckTender checkTender;
    @XmlElement(name = "CouponTender")
    protected CouponTender couponTender;
    @XmlElement(name = "GiftCardTender")
    protected GiftCardTender giftCardTender;
    @XmlElement(name = "GiftCertTender")
    protected GiftCertTender giftCertTender;
    @XmlElement(name = "MailCheckTender")
    protected MailCheckTender mailCheckTender;
    @XmlElement(name = "PurchaseOrdTender")
    protected PurchaseOrdTender purchaseOrdTender;
    @XmlElement(name = "StoreCreditTender")
    protected StoreCreditTender storeCreditTender;
    @XmlElement(name = "TravelCheckTender")
    protected TravelCheckTender travelCheckTender;

    /**
     * Gets the value of the seqNo property.
     * 
     */
    public int getSeqNo() {
        return seqNo;
    }

    /**
     * Sets the value of the seqNo property.
     * 
     */
    public void setSeqNo(int value) {
        this.seqNo = value;
    }

    /**
     * Gets the value of the paymentType property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentType }
     *     
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * Sets the value of the paymentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentType }
     *     
     */
    public void setPaymentType(PaymentType value) {
        this.paymentType = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the alternateCurrencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateCurrencyCode() {
        return alternateCurrencyCode;
    }

    /**
     * Sets the value of the alternateCurrencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateCurrencyCode(String value) {
        this.alternateCurrencyCode = value;
    }

    /**
     * Gets the value of the alternateAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAlternateAmount() {
        return alternateAmount;
    }

    /**
     * Sets the value of the alternateAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAlternateAmount(BigDecimal value) {
        this.alternateAmount = value;
    }

    /**
     * This records a credit/debit card payment.
     * 
     * @return
     *     possible object is
     *     {@link CreditDebitTender }
     *     
     */
    public CreditDebitTender getCreditDebitTender() {
        return creditDebitTender;
    }

    /**
     * Sets the value of the creditDebitTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditDebitTender }
     *     
     */
    public void setCreditDebitTender(CreditDebitTender value) {
        this.creditDebitTender = value;
    }

    /**
     * The records a check payment.
     * 
     * @return
     *     possible object is
     *     {@link CheckTender }
     *     
     */
    public CheckTender getCheckTender() {
        return checkTender;
    }

    /**
     * Sets the value of the checkTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckTender }
     *     
     */
    public void setCheckTender(CheckTender value) {
        this.checkTender = value;
    }

    /**
     * This records a coupon payment.
     * 
     * @return
     *     possible object is
     *     {@link CouponTender }
     *     
     */
    public CouponTender getCouponTender() {
        return couponTender;
    }

    /**
     * Sets the value of the couponTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link CouponTender }
     *     
     */
    public void setCouponTender(CouponTender value) {
        this.couponTender = value;
    }

    /**
     * This records a gift card payment.
     * 
     * @return
     *     possible object is
     *     {@link GiftCardTender }
     *     
     */
    public GiftCardTender getGiftCardTender() {
        return giftCardTender;
    }

    /**
     * Sets the value of the giftCardTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiftCardTender }
     *     
     */
    public void setGiftCardTender(GiftCardTender value) {
        this.giftCardTender = value;
    }

    /**
     * This records a gift certificate payment
     * 
     * @return
     *     possible object is
     *     {@link GiftCertTender }
     *     
     */
    public GiftCertTender getGiftCertTender() {
        return giftCertTender;
    }

    /**
     * Sets the value of the giftCertTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link GiftCertTender }
     *     
     */
    public void setGiftCertTender(GiftCertTender value) {
        this.giftCertTender = value;
    }

    /**
     * This records a refund payment by a check mailed to the customer.
     * 
     * @return
     *     possible object is
     *     {@link MailCheckTender }
     *     
     */
    public MailCheckTender getMailCheckTender() {
        return mailCheckTender;
    }

    /**
     * Sets the value of the mailCheckTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link MailCheckTender }
     *     
     */
    public void setMailCheckTender(MailCheckTender value) {
        this.mailCheckTender = value;
    }

    /**
     * This records a purchase order payment.
     * 
     * @return
     *     possible object is
     *     {@link PurchaseOrdTender }
     *     
     */
    public PurchaseOrdTender getPurchaseOrdTender() {
        return purchaseOrdTender;
    }

    /**
     * Sets the value of the purchaseOrdTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link PurchaseOrdTender }
     *     
     */
    public void setPurchaseOrdTender(PurchaseOrdTender value) {
        this.purchaseOrdTender = value;
    }

    /**
     * This records a store credit payment.
     * 
     * @return
     *     possible object is
     *     {@link StoreCreditTender }
     *     
     */
    public StoreCreditTender getStoreCreditTender() {
        return storeCreditTender;
    }

    /**
     * Sets the value of the storeCreditTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link StoreCreditTender }
     *     
     */
    public void setStoreCreditTender(StoreCreditTender value) {
        this.storeCreditTender = value;
    }

    /**
     * This records a travelers check payment.
     * 
     * @return
     *     possible object is
     *     {@link TravelCheckTender }
     *     
     */
    public TravelCheckTender getTravelCheckTender() {
        return travelCheckTender;
    }

    /**
     * Sets the value of the travelCheckTender property.
     * 
     * @param value
     *     allowed object is
     *     {@link TravelCheckTender }
     *     
     */
    public void setTravelCheckTender(TravelCheckTender value) {
        this.travelCheckTender = value;
    }

}
