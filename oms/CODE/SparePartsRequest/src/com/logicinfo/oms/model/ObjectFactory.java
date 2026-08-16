
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

    private final static QName _SparePartsRequest_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsRequest");
    private final static QName _SparePartsResponse_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SparePartsRequest }
     * 
     */
    public SparePartsRequest createSparePartsRequest() {
        return new SparePartsRequest();
    }

    /**
     * Create an instance of {@link SparePartsResponse }
     * 
     */
    public SparePartsResponse createSparePartsResponse() {
        return new SparePartsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsRequest")
    public JAXBElement<SparePartsRequest> createSparePartsRequest(SparePartsRequest value) {
        return new JAXBElement<SparePartsRequest>(_SparePartsRequest_QNAME, SparePartsRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsResponse")
    public JAXBElement<SparePartsResponse> createSparePartsResponse(SparePartsResponse value) {
        return new JAXBElement<SparePartsResponse>(_SparePartsResponse_QNAME, SparePartsResponse.class, null, value);
    }

}
