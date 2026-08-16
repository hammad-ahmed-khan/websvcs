
package com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1 package. 
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

    private final static QName _CancelFulfillmentOrderDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "cancelFulfillmentOrderDetail");
    private final static QName _CancelFulfillmentOrderDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "cancelFulfillmentOrderDetailResponse");
    private final static QName _CreateFulfillmentOrderDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "createFulfillmentOrderDetail");
    private final static QName _CreateFulfillmentOrderDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "createFulfillmentOrderDetailResponse");
    private final static QName _LookupFulfillmentOrderHeaders_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "lookupFulfillmentOrderHeaders");
    private final static QName _LookupFulfillmentOrderHeadersResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "lookupFulfillmentOrderHeadersResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "pingResponse");
    private final static QName _ReadFulfillmentOrderDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "readFulfillmentOrderDetail");
    private final static QName _ReadFulfillmentOrderDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", "readFulfillmentOrderDetailResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDetail }
     * 
     */
    public CancelFulfillmentOrderDetail createCancelFulfillmentOrderDetail() {
        return new CancelFulfillmentOrderDetail();
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDetailResponse }
     * 
     */
    public CancelFulfillmentOrderDetailResponse createCancelFulfillmentOrderDetailResponse() {
        return new CancelFulfillmentOrderDetailResponse();
    }

    /**
     * Create an instance of {@link CreateFulfillmentOrderDetail }
     * 
     */
    public CreateFulfillmentOrderDetail createCreateFulfillmentOrderDetail() {
        return new CreateFulfillmentOrderDetail();
    }

    /**
     * Create an instance of {@link CreateFulfillmentOrderDetailResponse }
     * 
     */
    public CreateFulfillmentOrderDetailResponse createCreateFulfillmentOrderDetailResponse() {
        return new CreateFulfillmentOrderDetailResponse();
    }

    /**
     * Create an instance of {@link LookupFulfillmentOrderHeaders }
     * 
     */
    public LookupFulfillmentOrderHeaders createLookupFulfillmentOrderHeaders() {
        return new LookupFulfillmentOrderHeaders();
    }

    /**
     * Create an instance of {@link LookupFulfillmentOrderHeadersResponse }
     * 
     */
    public LookupFulfillmentOrderHeadersResponse createLookupFulfillmentOrderHeadersResponse() {
        return new LookupFulfillmentOrderHeadersResponse();
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
     * Create an instance of {@link ReadFulfillmentOrderDetail }
     * 
     */
    public ReadFulfillmentOrderDetail createReadFulfillmentOrderDetail() {
        return new ReadFulfillmentOrderDetail();
    }

    /**
     * Create an instance of {@link ReadFulfillmentOrderDetailResponse }
     * 
     */
    public ReadFulfillmentOrderDetailResponse createReadFulfillmentOrderDetailResponse() {
        return new ReadFulfillmentOrderDetailResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "cancelFulfillmentOrderDetail")
    public JAXBElement<CancelFulfillmentOrderDetail> createCancelFulfillmentOrderDetail(CancelFulfillmentOrderDetail value) {
        return new JAXBElement<CancelFulfillmentOrderDetail>(_CancelFulfillmentOrderDetail_QNAME, CancelFulfillmentOrderDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "cancelFulfillmentOrderDetailResponse")
    public JAXBElement<CancelFulfillmentOrderDetailResponse> createCancelFulfillmentOrderDetailResponse(CancelFulfillmentOrderDetailResponse value) {
        return new JAXBElement<CancelFulfillmentOrderDetailResponse>(_CancelFulfillmentOrderDetailResponse_QNAME, CancelFulfillmentOrderDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "createFulfillmentOrderDetail")
    public JAXBElement<CreateFulfillmentOrderDetail> createCreateFulfillmentOrderDetail(CreateFulfillmentOrderDetail value) {
        return new JAXBElement<CreateFulfillmentOrderDetail>(_CreateFulfillmentOrderDetail_QNAME, CreateFulfillmentOrderDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "createFulfillmentOrderDetailResponse")
    public JAXBElement<CreateFulfillmentOrderDetailResponse> createCreateFulfillmentOrderDetailResponse(CreateFulfillmentOrderDetailResponse value) {
        return new JAXBElement<CreateFulfillmentOrderDetailResponse>(_CreateFulfillmentOrderDetailResponse_QNAME, CreateFulfillmentOrderDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderHeaders }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderHeaders }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "lookupFulfillmentOrderHeaders")
    public JAXBElement<LookupFulfillmentOrderHeaders> createLookupFulfillmentOrderHeaders(LookupFulfillmentOrderHeaders value) {
        return new JAXBElement<LookupFulfillmentOrderHeaders>(_LookupFulfillmentOrderHeaders_QNAME, LookupFulfillmentOrderHeaders.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderHeadersResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderHeadersResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "lookupFulfillmentOrderHeadersResponse")
    public JAXBElement<LookupFulfillmentOrderHeadersResponse> createLookupFulfillmentOrderHeadersResponse(LookupFulfillmentOrderHeadersResponse value) {
        return new JAXBElement<LookupFulfillmentOrderHeadersResponse>(_LookupFulfillmentOrderHeadersResponse_QNAME, LookupFulfillmentOrderHeadersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "ping")
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
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "readFulfillmentOrderDetail")
    public JAXBElement<ReadFulfillmentOrderDetail> createReadFulfillmentOrderDetail(ReadFulfillmentOrderDetail value) {
        return new JAXBElement<ReadFulfillmentOrderDetail>(_ReadFulfillmentOrderDetail_QNAME, ReadFulfillmentOrderDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1", name = "readFulfillmentOrderDetailResponse")
    public JAXBElement<ReadFulfillmentOrderDetailResponse> createReadFulfillmentOrderDetailResponse(ReadFulfillmentOrderDetailResponse value) {
        return new JAXBElement<ReadFulfillmentOrderDetailResponse>(_ReadFulfillmentOrderDetailResponse_QNAME, ReadFulfillmentOrderDetailResponse.class, null, value);
    }

}
