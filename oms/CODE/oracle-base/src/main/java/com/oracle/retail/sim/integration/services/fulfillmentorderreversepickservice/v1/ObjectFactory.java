
package com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1 package. 
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

    private final static QName _ConfirmReversePick_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "confirmReversePick");
    private final static QName _ConfirmReversePickResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "confirmReversePickResponse");
    private final static QName _CreateReversePick_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "createReversePick");
    private final static QName _CreateReversePickResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "createReversePickResponse");
    private final static QName _DeleteReversePick_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "deleteReversePick");
    private final static QName _DeleteReversePickResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "deleteReversePickResponse");
    private final static QName _LookupReversePickHeaders_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "lookupReversePickHeaders");
    private final static QName _LookupReversePickHeadersResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "lookupReversePickHeadersResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "pingResponse");
    private final static QName _ReadReversePickDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "readReversePickDetail");
    private final static QName _ReadReversePickDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "readReversePickDetailResponse");
    private final static QName _UpdateFulfillmentOrderReversePick_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "updateFulfillmentOrderReversePick");
    private final static QName _UpdateFulfillmentOrderReversePickResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", "updateFulfillmentOrderReversePickResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfirmReversePick }
     * 
     */
    public ConfirmReversePick createConfirmReversePick() {
        return new ConfirmReversePick();
    }

    /**
     * Create an instance of {@link ConfirmReversePickResponse }
     * 
     */
    public ConfirmReversePickResponse createConfirmReversePickResponse() {
        return new ConfirmReversePickResponse();
    }

    /**
     * Create an instance of {@link CreateReversePick }
     * 
     */
    public CreateReversePick createCreateReversePick() {
        return new CreateReversePick();
    }

    /**
     * Create an instance of {@link CreateReversePickResponse }
     * 
     */
    public CreateReversePickResponse createCreateReversePickResponse() {
        return new CreateReversePickResponse();
    }

    /**
     * Create an instance of {@link DeleteReversePick }
     * 
     */
    public DeleteReversePick createDeleteReversePick() {
        return new DeleteReversePick();
    }

    /**
     * Create an instance of {@link DeleteReversePickResponse }
     * 
     */
    public DeleteReversePickResponse createDeleteReversePickResponse() {
        return new DeleteReversePickResponse();
    }

    /**
     * Create an instance of {@link LookupReversePickHeaders }
     * 
     */
    public LookupReversePickHeaders createLookupReversePickHeaders() {
        return new LookupReversePickHeaders();
    }

    /**
     * Create an instance of {@link LookupReversePickHeadersResponse }
     * 
     */
    public LookupReversePickHeadersResponse createLookupReversePickHeadersResponse() {
        return new LookupReversePickHeadersResponse();
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
     * Create an instance of {@link ReadReversePickDetail }
     * 
     */
    public ReadReversePickDetail createReadReversePickDetail() {
        return new ReadReversePickDetail();
    }

    /**
     * Create an instance of {@link ReadReversePickDetailResponse }
     * 
     */
    public ReadReversePickDetailResponse createReadReversePickDetailResponse() {
        return new ReadReversePickDetailResponse();
    }

    /**
     * Create an instance of {@link UpdateFulfillmentOrderReversePick }
     * 
     */
    public UpdateFulfillmentOrderReversePick createUpdateFulfillmentOrderReversePick() {
        return new UpdateFulfillmentOrderReversePick();
    }

    /**
     * Create an instance of {@link UpdateFulfillmentOrderReversePickResponse }
     * 
     */
    public UpdateFulfillmentOrderReversePickResponse createUpdateFulfillmentOrderReversePickResponse() {
        return new UpdateFulfillmentOrderReversePickResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmReversePick }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConfirmReversePick }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "confirmReversePick")
    public JAXBElement<ConfirmReversePick> createConfirmReversePick(ConfirmReversePick value) {
        return new JAXBElement<ConfirmReversePick>(_ConfirmReversePick_QNAME, ConfirmReversePick.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmReversePickResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConfirmReversePickResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "confirmReversePickResponse")
    public JAXBElement<ConfirmReversePickResponse> createConfirmReversePickResponse(ConfirmReversePickResponse value) {
        return new JAXBElement<ConfirmReversePickResponse>(_ConfirmReversePickResponse_QNAME, ConfirmReversePickResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReversePick }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateReversePick }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "createReversePick")
    public JAXBElement<CreateReversePick> createCreateReversePick(CreateReversePick value) {
        return new JAXBElement<CreateReversePick>(_CreateReversePick_QNAME, CreateReversePick.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateReversePickResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateReversePickResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "createReversePickResponse")
    public JAXBElement<CreateReversePickResponse> createCreateReversePickResponse(CreateReversePickResponse value) {
        return new JAXBElement<CreateReversePickResponse>(_CreateReversePickResponse_QNAME, CreateReversePickResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteReversePick }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteReversePick }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "deleteReversePick")
    public JAXBElement<DeleteReversePick> createDeleteReversePick(DeleteReversePick value) {
        return new JAXBElement<DeleteReversePick>(_DeleteReversePick_QNAME, DeleteReversePick.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteReversePickResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DeleteReversePickResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "deleteReversePickResponse")
    public JAXBElement<DeleteReversePickResponse> createDeleteReversePickResponse(DeleteReversePickResponse value) {
        return new JAXBElement<DeleteReversePickResponse>(_DeleteReversePickResponse_QNAME, DeleteReversePickResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupReversePickHeaders }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupReversePickHeaders }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "lookupReversePickHeaders")
    public JAXBElement<LookupReversePickHeaders> createLookupReversePickHeaders(LookupReversePickHeaders value) {
        return new JAXBElement<LookupReversePickHeaders>(_LookupReversePickHeaders_QNAME, LookupReversePickHeaders.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupReversePickHeadersResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupReversePickHeadersResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "lookupReversePickHeadersResponse")
    public JAXBElement<LookupReversePickHeadersResponse> createLookupReversePickHeadersResponse(LookupReversePickHeadersResponse value) {
        return new JAXBElement<LookupReversePickHeadersResponse>(_LookupReversePickHeadersResponse_QNAME, LookupReversePickHeadersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "ping")
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
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadReversePickDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadReversePickDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "readReversePickDetail")
    public JAXBElement<ReadReversePickDetail> createReadReversePickDetail(ReadReversePickDetail value) {
        return new JAXBElement<ReadReversePickDetail>(_ReadReversePickDetail_QNAME, ReadReversePickDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadReversePickDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadReversePickDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "readReversePickDetailResponse")
    public JAXBElement<ReadReversePickDetailResponse> createReadReversePickDetailResponse(ReadReversePickDetailResponse value) {
        return new JAXBElement<ReadReversePickDetailResponse>(_ReadReversePickDetailResponse_QNAME, ReadReversePickDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderReversePick }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderReversePick }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "updateFulfillmentOrderReversePick")
    public JAXBElement<UpdateFulfillmentOrderReversePick> createUpdateFulfillmentOrderReversePick(UpdateFulfillmentOrderReversePick value) {
        return new JAXBElement<UpdateFulfillmentOrderReversePick>(_UpdateFulfillmentOrderReversePick_QNAME, UpdateFulfillmentOrderReversePick.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderReversePickResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UpdateFulfillmentOrderReversePickResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/FulfillmentOrderReversePickService/v1", name = "updateFulfillmentOrderReversePickResponse")
    public JAXBElement<UpdateFulfillmentOrderReversePickResponse> createUpdateFulfillmentOrderReversePickResponse(UpdateFulfillmentOrderReversePickResponse value) {
        return new JAXBElement<UpdateFulfillmentOrderReversePickResponse>(_UpdateFulfillmentOrderReversePickResponse_QNAME, UpdateFulfillmentOrderReversePickResponse.class, null, value);
    }

}
