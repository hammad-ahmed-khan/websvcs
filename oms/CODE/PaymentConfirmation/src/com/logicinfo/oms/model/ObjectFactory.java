
package com.logicinfo.oms.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.logicinfo.oms.model package. 
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

    private final static QName _PaymentConfResponse_QNAME = new QName("http://com.logicinfo.oms/model/", "PaymentConfResponse");
    private final static QName _PaymentConf_QNAME = new QName("http://com.logicinfo.oms/model/", "PaymentConf");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CoPaymentConf }
     * 
     */
    public CoPaymentConf createCoPaymentConf() {
        return new CoPaymentConf();
    }

    /**
     * Create an instance of {@link CoPaymentConfResponse }
     * 
     */
    public CoPaymentConfResponse createCoPaymentConfResponse() {
        return new CoPaymentConfResponse();
    }

    /**
     * Create an instance of {@link CustomerOrderResponseItemFulfillment }
     * 
     */
    public CustomerOrderResponseItemFulfillment createCustomerOrderResponseItemFulfillment() {
        return new CustomerOrderResponseItemFulfillment();
    }

    /**
     * Create an instance of {@link CustomerOrderResponseItems }
     * 
     */
    public CustomerOrderResponseItems createCustomerOrderResponseItems() {
        return new CustomerOrderResponseItems();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CoPaymentConfResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "PaymentConfResponse")
    public JAXBElement<CoPaymentConfResponse> createPaymentConfResponse(CoPaymentConfResponse value) {
        return new JAXBElement<CoPaymentConfResponse>(_PaymentConfResponse_QNAME, CoPaymentConfResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CoPaymentConf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "PaymentConf")
    public JAXBElement<CoPaymentConf> createPaymentConf(CoPaymentConf value) {
        return new JAXBElement<CoPaymentConf>(_PaymentConf_QNAME, CoPaymentConf.class, null, value);
    }

}
