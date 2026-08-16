
package com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1 package. 
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

    private final static QName _CancelFulfillmentOrderDelivery_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "cancelFulfillmentOrderDelivery");
    private final static QName _CancelFulfillmentOrderDeliveryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "cancelFulfillmentOrderDeliveryResponse");
    private final static QName _CancelFulfillmentOrderDeliverySubmission_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "cancelFulfillmentOrderDeliverySubmission");
    private final static QName _CancelFulfillmentOrderDeliverySubmissionResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "cancelFulfillmentOrderDeliverySubmissionResponse");
    private final static QName _CreateFulfillmentOrderDelivery_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "createFulfillmentOrderDelivery");
    private final static QName _CreateFulfillmentOrderDeliveryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "createFulfillmentOrderDeliveryResponse");
    private final static QName _DispatchFulfillmentOrderDelivery_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "dispatchFulfillmentOrderDelivery");
    private final static QName _DispatchFulfillmentOrderDeliveryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "dispatchFulfillmentOrderDeliveryResponse");
    private final static QName _LookupFulfillmentOrderDeliveryHeaders_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "lookupFulfillmentOrderDeliveryHeaders");
    private final static QName _LookupFulfillmentOrderDeliveryHeadersResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "lookupFulfillmentOrderDeliveryHeadersResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "pingResponse");
    private final static QName _ReadFulfillmentOrderDeliveryDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "readFulfillmentOrderDeliveryDetail");
    private final static QName _ReadFulfillmentOrderDeliveryDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "readFulfillmentOrderDeliveryDetailResponse");
    private final static QName _SubmitFulfillmentOrderDelivery_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "submitFulfillmentOrderDelivery");
    private final static QName _SubmitFulfillmentOrderDeliveryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "submitFulfillmentOrderDeliveryResponse");
    private final static QName _UpdateFulfillmentOrderDelivery_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "updateFulfillmentOrderDelivery");
    private final static QName _UpdateFulfillmentOrderDeliveryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", "updateFulfillmentOrderDeliveryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDelivery }
     * 
     */
    public CancelFulfillmentOrderDelivery createCancelFulfillmentOrderDelivery() {
        return new CancelFulfillmentOrderDelivery();
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDeliveryResponse }
     * 
     */
    public CancelFulfillmentOrderDeliveryResponse createCancelFulfillmentOrderDeliveryResponse() {
        return new CancelFulfillmentOrderDeliveryResponse();
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDeliverySubmission }
     * 
     */
    public CancelFulfillmentOrderDeliverySubmission createCancelFulfillmentOrderDeliverySubmission() {
        return new CancelFulfillmentOrderDeliverySubmission();
    }

    /**
     * Create an instance of {@link CancelFulfillmentOrderDeliverySubmissionResponse }
     * 
     */
    public CancelFulfillmentOrderDeliverySubmissionResponse createCancelFulfillmentOrderDeliverySubmissionResponse() {
        return new CancelFulfillmentOrderDeliverySubmissionResponse();
    }

    /**
     * Create an instance of {@link CreateFulfillmentOrderDelivery }
     * 
     */
    public CreateFulfillmentOrderDelivery createCreateFulfillmentOrderDelivery() {
        return new CreateFulfillmentOrderDelivery();
    }

    /**
     * Create an instance of {@link CreateFulfillmentOrderDeliveryResponse }
     * 
     */
    public CreateFulfillmentOrderDeliveryResponse createCreateFulfillmentOrderDeliveryResponse() {
        return new CreateFulfillmentOrderDeliveryResponse();
    }

    /**
     * Create an instance of {@link DispatchFulfillmentOrderDelivery }
     * 
     */
    public DispatchFulfillmentOrderDelivery createDispatchFulfillmentOrderDelivery() {
        return new DispatchFulfillmentOrderDelivery();
    }

    /**
     * Create an instance of {@link DispatchFulfillmentOrderDeliveryResponse }
     * 
     */
    public DispatchFulfillmentOrderDeliveryResponse createDispatchFulfillmentOrderDeliveryResponse() {
        return new DispatchFulfillmentOrderDeliveryResponse();
    }

    /**
     * Create an instance of {@link LookupFulfillmentOrderDeliveryHeaders }
     * 
     */
    public LookupFulfillmentOrderDeliveryHeaders createLookupFulfillmentOrderDeliveryHeaders() {
        return new LookupFulfillmentOrderDeliveryHeaders();
    }

    /**
     * Create an instance of {@link LookupFulfillmentOrderDeliveryHeadersResponse }
     * 
     */
    public LookupFulfillmentOrderDeliveryHeadersResponse createLookupFulfillmentOrderDeliveryHeadersResponse() {
        return new LookupFulfillmentOrderDeliveryHeadersResponse();
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
     * Create an instance of {@link ReadFulfillmentOrderDeliveryDetail }
     * 
     */
    public ReadFulfillmentOrderDeliveryDetail createReadFulfillmentOrderDeliveryDetail() {
        return new ReadFulfillmentOrderDeliveryDetail();
    }

    /**
     * Create an instance of {@link ReadFulfillmentOrderDeliveryDetailResponse }
     * 
     */
    public ReadFulfillmentOrderDeliveryDetailResponse createReadFulfillmentOrderDeliveryDetailResponse() {
        return new ReadFulfillmentOrderDeliveryDetailResponse();
    }

    /**
     * Create an instance of {@link SubmitFulfillmentOrderDelivery }
     * 
     */
    public SubmitFulfillmentOrderDelivery createSubmitFulfillmentOrderDelivery() {
        return new SubmitFulfillmentOrderDelivery();
    }

    /**
     * Create an instance of {@link SubmitFulfillmentOrderDeliveryResponse }
     * 
     */
    public SubmitFulfillmentOrderDeliveryResponse createSubmitFulfillmentOrderDeliveryResponse() {
        return new SubmitFulfillmentOrderDeliveryResponse();
    }

    /**
     * Create an instance of {@link UpdateFulfillmentOrderDelivery }
     * 
     */
    public UpdateFulfillmentOrderDelivery createUpdateFulfillmentOrderDelivery() {
        return new UpdateFulfillmentOrderDelivery();
    }

    /**
     * Create an instance of {@link UpdateFulfillmentOrderDeliveryResponse }
     * 
     */
    public UpdateFulfillmentOrderDeliveryResponse createUpdateFulfillmentOrderDeliveryResponse() {
        return new UpdateFulfillmentOrderDeliveryResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDelivery }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDelivery }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "cancelFulfillmentOrderDelivery")
    public JAXBElement<CancelFulfillmentOrderDelivery> createCancelFulfillmentOrderDelivery(CancelFulfillmentOrderDelivery value) {
        return new JAXBElement<CancelFulfillmentOrderDelivery>(_CancelFulfillmentOrderDelivery_QNAME, CancelFulfillmentOrderDelivery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliveryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliveryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "cancelFulfillmentOrderDeliveryResponse")
    public JAXBElement<CancelFulfillmentOrderDeliveryResponse> createCancelFulfillmentOrderDeliveryResponse(CancelFulfillmentOrderDeliveryResponse value) {
        return new JAXBElement<CancelFulfillmentOrderDeliveryResponse>(_CancelFulfillmentOrderDeliveryResponse_QNAME, CancelFulfillmentOrderDeliveryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliverySubmission }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliverySubmission }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "cancelFulfillmentOrderDeliverySubmission")
    public JAXBElement<CancelFulfillmentOrderDeliverySubmission> createCancelFulfillmentOrderDeliverySubmission(CancelFulfillmentOrderDeliverySubmission value) {
        return new JAXBElement<CancelFulfillmentOrderDeliverySubmission>(_CancelFulfillmentOrderDeliverySubmission_QNAME, CancelFulfillmentOrderDeliverySubmission.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliverySubmissionResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelFulfillmentOrderDeliverySubmissionResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "cancelFulfillmentOrderDeliverySubmissionResponse")
    public JAXBElement<CancelFulfillmentOrderDeliverySubmissionResponse> createCancelFulfillmentOrderDeliverySubmissionResponse(CancelFulfillmentOrderDeliverySubmissionResponse value) {
        return new JAXBElement<CancelFulfillmentOrderDeliverySubmissionResponse>(_CancelFulfillmentOrderDeliverySubmissionResponse_QNAME, CancelFulfillmentOrderDeliverySubmissionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDelivery }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDelivery }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "createFulfillmentOrderDelivery")
    public JAXBElement<CreateFulfillmentOrderDelivery> createCreateFulfillmentOrderDelivery(CreateFulfillmentOrderDelivery value) {
        return new JAXBElement<CreateFulfillmentOrderDelivery>(_CreateFulfillmentOrderDelivery_QNAME, CreateFulfillmentOrderDelivery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDeliveryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateFulfillmentOrderDeliveryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "createFulfillmentOrderDeliveryResponse")
    public JAXBElement<CreateFulfillmentOrderDeliveryResponse> createCreateFulfillmentOrderDeliveryResponse(CreateFulfillmentOrderDeliveryResponse value) {
        return new JAXBElement<CreateFulfillmentOrderDeliveryResponse>(_CreateFulfillmentOrderDeliveryResponse_QNAME, CreateFulfillmentOrderDeliveryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchFulfillmentOrderDelivery }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DispatchFulfillmentOrderDelivery }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "dispatchFulfillmentOrderDelivery")
    public JAXBElement<DispatchFulfillmentOrderDelivery> createDispatchFulfillmentOrderDelivery(DispatchFulfillmentOrderDelivery value) {
        return new JAXBElement<DispatchFulfillmentOrderDelivery>(_DispatchFulfillmentOrderDelivery_QNAME, DispatchFulfillmentOrderDelivery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DispatchFulfillmentOrderDeliveryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DispatchFulfillmentOrderDeliveryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "dispatchFulfillmentOrderDeliveryResponse")
    public JAXBElement<DispatchFulfillmentOrderDeliveryResponse> createDispatchFulfillmentOrderDeliveryResponse(DispatchFulfillmentOrderDeliveryResponse value) {
        return new JAXBElement<DispatchFulfillmentOrderDeliveryResponse>(_DispatchFulfillmentOrderDeliveryResponse_QNAME, DispatchFulfillmentOrderDeliveryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderDeliveryHeaders }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderDeliveryHeaders }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "lookupFulfillmentOrderDeliveryHeaders")
    public JAXBElement<LookupFulfillmentOrderDeliveryHeaders> createLookupFulfillmentOrderDeliveryHeaders(LookupFulfillmentOrderDeliveryHeaders value) {
        return new JAXBElement<LookupFulfillmentOrderDeliveryHeaders>(_LookupFulfillmentOrderDeliveryHeaders_QNAME, LookupFulfillmentOrderDeliveryHeaders.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderDeliveryHeadersResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupFulfillmentOrderDeliveryHeadersResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "lookupFulfillmentOrderDeliveryHeadersResponse")
    public JAXBElement<LookupFulfillmentOrderDeliveryHeadersResponse> createLookupFulfillmentOrderDeliveryHeadersResponse(LookupFulfillmentOrderDeliveryHeadersResponse value) {
        return new JAXBElement<LookupFulfillmentOrderDeliveryHeadersResponse>(_LookupFulfillmentOrderDeliveryHeadersResponse_QNAME, LookupFulfillmentOrderDeliveryHeadersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "ping")
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
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDeliveryDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDeliveryDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "readFulfillmentOrderDeliveryDetail")
    public JAXBElement<ReadFulfillmentOrderDeliveryDetail> createReadFulfillmentOrderDeliveryDetail(ReadFulfillmentOrderDeliveryDetail value) {
        return new JAXBElement<ReadFulfillmentOrderDeliveryDetail>(_ReadFulfillmentOrderDeliveryDetail_QNAME, ReadFulfillmentOrderDeliveryDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDeliveryDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadFulfillmentOrderDeliveryDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "readFulfillmentOrderDeliveryDetailResponse")
    public JAXBElement<ReadFulfillmentOrderDeliveryDetailResponse> createReadFulfillmentOrderDeliveryDetailResponse(ReadFulfillmentOrderDeliveryDetailResponse value) {
        return new JAXBElement<ReadFulfillmentOrderDeliveryDetailResponse>(_ReadFulfillmentOrderDeliveryDetailResponse_QNAME, ReadFulfillmentOrderDeliveryDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitFulfillmentOrderDelivery }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitFulfillmentOrderDelivery }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "submitFulfillmentOrderDelivery")
    public JAXBElement<SubmitFulfillmentOrderDelivery> createSubmitFulfillmentOrderDelivery(SubmitFulfillmentOrderDelivery value) {
        return new JAXBElement<SubmitFulfillmentOrderDelivery>(_SubmitFulfillmentOrderDelivery_QNAME, SubmitFulfillmentOrderDelivery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubmitFulfillmentOrderDeliveryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SubmitFulfillmentOrderDeliveryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "submitFulfillmentOrderDeliveryResponse")
    public JAXBElement<SubmitFulfillmentOrderDeliveryResponse> createSubmitFulfillmentOrderDeliveryResponse(SubmitFulfillmentOrderDeliveryResponse value) {
        return new JAXBElement<SubmitFulfillmentOrderDeliveryResponse>(_SubmitFulfillmentOrderDeliveryResponse_QNAME, SubmitFulfillmentOrderDeliveryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderDelivery }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderDelivery }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "updateFulfillmentOrderDelivery")
    public JAXBElement<UpdateFulfillmentOrderDelivery> createUpdateFulfillmentOrderDelivery(UpdateFulfillmentOrderDelivery value) {
        return new JAXBElement<UpdateFulfillmentOrderDelivery>(_UpdateFulfillmentOrderDelivery_QNAME, UpdateFulfillmentOrderDelivery.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderDeliveryResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderDeliveryResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderDeliveryService/v1", name = "updateFulfillmentOrderDeliveryResponse")
    public JAXBElement<UpdateFulfillmentOrderDeliveryResponse> createUpdateFulfillmentOrderDeliveryResponse(UpdateFulfillmentOrderDeliveryResponse value) {
        return new JAXBElement<UpdateFulfillmentOrderDeliveryResponse>(_UpdateFulfillmentOrderDeliveryResponse_QNAME, UpdateFulfillmentOrderDeliveryResponse.class, null, value);
    }

}
