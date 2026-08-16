
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

    private final static QName _SparePartsCancelRequest_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsCancelRequest");
    private final static QName _SparePartsCancelResponse_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsCancelResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SparePartsCancelRequest }
     * 
     */
    public SparePartsCancelRequest createSparePartsCancelRequest() {
        return new SparePartsCancelRequest();
    }

    /**
     * Create an instance of {@link SparePartsCancelResponse }
     * 
     */
    public SparePartsCancelResponse createSparePartsCancelResponse() {
        return new SparePartsCancelResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsCancelRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsCancelRequest")
    public JAXBElement<SparePartsCancelRequest> createSparePartsCancelRequest(SparePartsCancelRequest value) {
        return new JAXBElement<SparePartsCancelRequest>(_SparePartsCancelRequest_QNAME, SparePartsCancelRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsCancelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsCancelResponse")
    public JAXBElement<SparePartsCancelResponse> createSparePartsCancelResponse(SparePartsCancelResponse value) {
        return new JAXBElement<SparePartsCancelResponse>(_SparePartsCancelResponse_QNAME, SparePartsCancelResponse.class, null, value);
    }

}
