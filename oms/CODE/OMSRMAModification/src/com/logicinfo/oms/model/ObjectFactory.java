
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

    private final static QName _ModifyRMA_QNAME = new QName("http://com.logicinfo.oms/model/", "ModifyRMA");
    private final static QName _ModifyRMAResponse_QNAME = new QName("http://com.logicinfo.oms/model/", "ModifyRMAResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RMAModifyRequest }
     * 
     */
    public RMAModifyRequest createRMAModifyRequest() {
        return new RMAModifyRequest();
    }

    /**
     * Create an instance of {@link RMAModifyResponse }
     * 
     */
    public RMAModifyResponse createRMAModifyResponse() {
        return new RMAModifyResponse();
    }

    /**
     * Create an instance of {@link RMAModifyDetailResponse }
     * 
     */
    public RMAModifyDetailResponse createRMAModifyDetailResponse() {
        return new RMAModifyDetailResponse();
    }

    /**
     * Create an instance of {@link RMAModifyDetail }
     * 
     */
    public RMAModifyDetail createRMAModifyDetail() {
        return new RMAModifyDetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RMAModifyRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "ModifyRMA")
    public JAXBElement<RMAModifyRequest> createModifyRMA(RMAModifyRequest value) {
        return new JAXBElement<RMAModifyRequest>(_ModifyRMA_QNAME, RMAModifyRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RMAModifyResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "ModifyRMAResponse")
    public JAXBElement<RMAModifyResponse> createModifyRMAResponse(RMAModifyResponse value) {
        return new JAXBElement<RMAModifyResponse>(_ModifyRMAResponse_QNAME, RMAModifyResponse.class, null, value);
    }

}
