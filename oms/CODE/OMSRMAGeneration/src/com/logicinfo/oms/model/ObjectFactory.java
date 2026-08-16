
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

    private final static QName _GenerateNewRMAResponse_QNAME = new QName("http://com.logicinfo.oms/model/", "GenerateNewRMAResponse");
    private final static QName _GenerateNewRMA_QNAME = new QName("http://com.logicinfo.oms/model/", "GenerateNewRMA");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustomerOrderRMAResponse }
     * 
     */
    public CustomerOrderRMAResponse createCustomerOrderRMAResponse() {
        return new CustomerOrderRMAResponse();
    }

    /**
     * Create an instance of {@link CustomerOrderRMA }
     * 
     */
    public CustomerOrderRMA createCustomerOrderRMA() {
        return new CustomerOrderRMA();
    }

    /**
     * Create an instance of {@link CustomerOrderRMAItem }
     * 
     */
    public CustomerOrderRMAItem createCustomerOrderRMAItem() {
        return new CustomerOrderRMAItem();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrderRMAResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "GenerateNewRMAResponse")
    public JAXBElement<CustomerOrderRMAResponse> createGenerateNewRMAResponse(CustomerOrderRMAResponse value) {
        return new JAXBElement<CustomerOrderRMAResponse>(_GenerateNewRMAResponse_QNAME, CustomerOrderRMAResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrderRMA }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "GenerateNewRMA")
    public JAXBElement<CustomerOrderRMA> createGenerateNewRMA(CustomerOrderRMA value) {
        return new JAXBElement<CustomerOrderRMA>(_GenerateNewRMA_QNAME, CustomerOrderRMA.class, null, value);
    }

}
