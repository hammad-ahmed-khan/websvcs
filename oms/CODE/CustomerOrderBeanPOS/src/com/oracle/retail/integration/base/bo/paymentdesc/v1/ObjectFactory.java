
package com.oracle.retail.integration.base.bo.paymentdesc.v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.integration.base.bo.paymentdesc.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.integration.base.bo.paymentdesc.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PaymentDesc }
     * 
     */
    public PaymentDesc createPaymentDesc() {
        return new PaymentDesc();
    }

    /**
     * Create an instance of {@link CreditDebitTender }
     * 
     */
    public CreditDebitTender createCreditDebitTender() {
        return new CreditDebitTender();
    }

    /**
     * Create an instance of {@link CheckTender }
     * 
     */
    public CheckTender createCheckTender() {
        return new CheckTender();
    }

    /**
     * Create an instance of {@link CouponTender }
     * 
     */
    public CouponTender createCouponTender() {
        return new CouponTender();
    }

    /**
     * Create an instance of {@link GiftCardTender }
     * 
     */
    public GiftCardTender createGiftCardTender() {
        return new GiftCardTender();
    }

    /**
     * Create an instance of {@link GiftCertTender }
     * 
     */
    public GiftCertTender createGiftCertTender() {
        return new GiftCertTender();
    }

    /**
     * Create an instance of {@link MailCheckTender }
     * 
     */
    public MailCheckTender createMailCheckTender() {
        return new MailCheckTender();
    }

    /**
     * Create an instance of {@link PurchaseOrdTender }
     * 
     */
    public PurchaseOrdTender createPurchaseOrdTender() {
        return new PurchaseOrdTender();
    }

    /**
     * Create an instance of {@link StoreCreditTender }
     * 
     */
    public StoreCreditTender createStoreCreditTender() {
        return new StoreCreditTender();
    }

    /**
     * Create an instance of {@link TravelCheckTender }
     * 
     */
    public TravelCheckTender createTravelCheckTender() {
        return new TravelCheckTender();
    }

}
