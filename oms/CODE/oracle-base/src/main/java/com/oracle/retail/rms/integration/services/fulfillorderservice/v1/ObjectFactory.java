
package com.oracle.retail.rms.integration.services.fulfillorderservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.rms.integration.services.fulfillorderservice.v1 package. 
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

    private final static QName _CancelFulfilOrdColRef_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "cancelFulfilOrdColRef");
    private final static QName _CancelFulfilOrdColRefResponse_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "cancelFulfilOrdColRefResponse");
    private final static QName _CreateFulfilOrdColDesc_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "createFulfilOrdColDesc");
    private final static QName _CreateFulfilOrdColDescResponse_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "createFulfilOrdColDescResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", "pingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.rms.integration.services.fulfillorderservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CancelFulfilOrdColRef }
     * 
     */
    public CancelFulfilOrdColRef createCancelFulfilOrdColRef() {
        return new CancelFulfilOrdColRef();
    }

    /**
     * Create an instance of {@link CancelFulfilOrdColRefResponse }
     * 
     */
    public CancelFulfilOrdColRefResponse createCancelFulfilOrdColRefResponse() {
        return new CancelFulfilOrdColRefResponse();
    }

    /**
     * Create an instance of {@link CreateFulfilOrdColDesc }
     * 
     */
    public CreateFulfilOrdColDesc createCreateFulfilOrdColDesc() {
        return new CreateFulfilOrdColDesc();
    }

    /**
     * Create an instance of {@link CreateFulfilOrdColDescResponse }
     * 
     */
    public CreateFulfilOrdColDescResponse createCreateFulfilOrdColDescResponse() {
        return new CreateFulfilOrdColDescResponse();
    }

    /**
     * Create an instance of {@link Ping }
     * 
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfilOrdColRef }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfilOrdColRef }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "cancelFulfilOrdColRef")
    public JAXBElement<CancelFulfilOrdColRef> createCancelFulfilOrdColRef(CancelFulfilOrdColRef value) {
        return new JAXBElement<CancelFulfilOrdColRef>(_CancelFulfilOrdColRef_QNAME, CancelFulfilOrdColRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfilOrdColRefResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfilOrdColRefResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "cancelFulfilOrdColRefResponse")
    public JAXBElement<CancelFulfilOrdColRefResponse> createCancelFulfilOrdColRefResponse(CancelFulfilOrdColRefResponse value) {
        return new JAXBElement<CancelFulfilOrdColRefResponse>(_CancelFulfilOrdColRefResponse_QNAME, CancelFulfilOrdColRefResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfilOrdColDesc }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfilOrdColDesc }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "createFulfilOrdColDesc")
    public JAXBElement<CreateFulfilOrdColDesc> createCreateFulfilOrdColDesc(CreateFulfilOrdColDesc value) {
        return new JAXBElement<CreateFulfilOrdColDesc>(_CreateFulfilOrdColDesc_QNAME, CreateFulfilOrdColDesc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfilOrdColDescResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfilOrdColDescResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "createFulfilOrdColDescResponse")
    public JAXBElement<CreateFulfilOrdColDescResponse> createCreateFulfilOrdColDescResponse(CreateFulfilOrdColDescResponse value) {
        return new JAXBElement<CreateFulfilOrdColDescResponse>(_CreateFulfilOrdColDescResponse_QNAME, CreateFulfilOrdColDescResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

}
