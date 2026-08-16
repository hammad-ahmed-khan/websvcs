
package com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1 package. 
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

    private final static QName _CancelInventoryAdjustment_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "cancelInventoryAdjustment");
    private final static QName _CancelInventoryAdjustmentResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "cancelInventoryAdjustmentResponse");
    private final static QName _ConfirmInventoryAdjustment_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "confirmInventoryAdjustment");
    private final static QName _ConfirmInventoryAdjustmentResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "confirmInventoryAdjustmentResponse");
    private final static QName _LookupInventoryAdjustmentHeader_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentHeader");
    private final static QName _LookupInventoryAdjustmentHeaderResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentHeaderResponse");
    private final static QName _LookupInventoryAdjustmentReason_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentReason");
    private final static QName _LookupInventoryAdjustmentReasonResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentReasonResponse");
    private final static QName _LookupInventoryAdjustmentTemplateHeader_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentTemplateHeader");
    private final static QName _LookupInventoryAdjustmentTemplateHeaderResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupInventoryAdjustmentTemplateHeaderResponse");
    private final static QName _LookupNonSellableQuantityType_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupNonSellableQuantityType");
    private final static QName _LookupNonSellableQuantityTypeResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "lookupNonSellableQuantityTypeResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "pingResponse");
    private final static QName _ReadInventoryAdjustmentDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "readInventoryAdjustmentDetail");
    private final static QName _ReadInventoryAdjustmentDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "readInventoryAdjustmentDetailResponse");
    private final static QName _ReadInventoryAdjustmentTemplateDetail_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "readInventoryAdjustmentTemplateDetail");
    private final static QName _ReadInventoryAdjustmentTemplateDetailResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "readInventoryAdjustmentTemplateDetailResponse");
    private final static QName _SaveAndConfirmInventoryAdjustment_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "saveAndConfirmInventoryAdjustment");
    private final static QName _SaveAndConfirmInventoryAdjustmentResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "saveAndConfirmInventoryAdjustmentResponse");
    private final static QName _SaveInventoryAdjustment_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "saveInventoryAdjustment");
    private final static QName _SaveInventoryAdjustmentResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", "saveInventoryAdjustmentResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CancelInventoryAdjustment }
     * 
     */
    public CancelInventoryAdjustment createCancelInventoryAdjustment() {
        return new CancelInventoryAdjustment();
    }

    /**
     * Create an instance of {@link CancelInventoryAdjustmentResponse }
     * 
     */
    public CancelInventoryAdjustmentResponse createCancelInventoryAdjustmentResponse() {
        return new CancelInventoryAdjustmentResponse();
    }

    /**
     * Create an instance of {@link ConfirmInventoryAdjustment }
     * 
     */
    public ConfirmInventoryAdjustment createConfirmInventoryAdjustment() {
        return new ConfirmInventoryAdjustment();
    }

    /**
     * Create an instance of {@link ConfirmInventoryAdjustmentResponse }
     * 
     */
    public ConfirmInventoryAdjustmentResponse createConfirmInventoryAdjustmentResponse() {
        return new ConfirmInventoryAdjustmentResponse();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentHeader }
     * 
     */
    public LookupInventoryAdjustmentHeader createLookupInventoryAdjustmentHeader() {
        return new LookupInventoryAdjustmentHeader();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentHeaderResponse }
     * 
     */
    public LookupInventoryAdjustmentHeaderResponse createLookupInventoryAdjustmentHeaderResponse() {
        return new LookupInventoryAdjustmentHeaderResponse();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentReason }
     * 
     */
    public LookupInventoryAdjustmentReason createLookupInventoryAdjustmentReason() {
        return new LookupInventoryAdjustmentReason();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentReasonResponse }
     * 
     */
    public LookupInventoryAdjustmentReasonResponse createLookupInventoryAdjustmentReasonResponse() {
        return new LookupInventoryAdjustmentReasonResponse();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentTemplateHeader }
     * 
     */
    public LookupInventoryAdjustmentTemplateHeader createLookupInventoryAdjustmentTemplateHeader() {
        return new LookupInventoryAdjustmentTemplateHeader();
    }

    /**
     * Create an instance of {@link LookupInventoryAdjustmentTemplateHeaderResponse }
     * 
     */
    public LookupInventoryAdjustmentTemplateHeaderResponse createLookupInventoryAdjustmentTemplateHeaderResponse() {
        return new LookupInventoryAdjustmentTemplateHeaderResponse();
    }

    /**
     * Create an instance of {@link LookupNonSellableQuantityType }
     * 
     */
    public LookupNonSellableQuantityType createLookupNonSellableQuantityType() {
        return new LookupNonSellableQuantityType();
    }

    /**
     * Create an instance of {@link LookupNonSellableQuantityTypeResponse }
     * 
     */
    public LookupNonSellableQuantityTypeResponse createLookupNonSellableQuantityTypeResponse() {
        return new LookupNonSellableQuantityTypeResponse();
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
     * Create an instance of {@link ReadInventoryAdjustmentDetail }
     * 
     */
    public ReadInventoryAdjustmentDetail createReadInventoryAdjustmentDetail() {
        return new ReadInventoryAdjustmentDetail();
    }

    /**
     * Create an instance of {@link ReadInventoryAdjustmentDetailResponse }
     * 
     */
    public ReadInventoryAdjustmentDetailResponse createReadInventoryAdjustmentDetailResponse() {
        return new ReadInventoryAdjustmentDetailResponse();
    }

    /**
     * Create an instance of {@link ReadInventoryAdjustmentTemplateDetail }
     * 
     */
    public ReadInventoryAdjustmentTemplateDetail createReadInventoryAdjustmentTemplateDetail() {
        return new ReadInventoryAdjustmentTemplateDetail();
    }

    /**
     * Create an instance of {@link ReadInventoryAdjustmentTemplateDetailResponse }
     * 
     */
    public ReadInventoryAdjustmentTemplateDetailResponse createReadInventoryAdjustmentTemplateDetailResponse() {
        return new ReadInventoryAdjustmentTemplateDetailResponse();
    }

    /**
     * Create an instance of {@link SaveAndConfirmInventoryAdjustment }
     * 
     */
    public SaveAndConfirmInventoryAdjustment createSaveAndConfirmInventoryAdjustment() {
        return new SaveAndConfirmInventoryAdjustment();
    }

    /**
     * Create an instance of {@link SaveAndConfirmInventoryAdjustmentResponse }
     * 
     */
    public SaveAndConfirmInventoryAdjustmentResponse createSaveAndConfirmInventoryAdjustmentResponse() {
        return new SaveAndConfirmInventoryAdjustmentResponse();
    }

    /**
     * Create an instance of {@link SaveInventoryAdjustment }
     * 
     */
    public SaveInventoryAdjustment createSaveInventoryAdjustment() {
        return new SaveInventoryAdjustment();
    }

    /**
     * Create an instance of {@link SaveInventoryAdjustmentResponse }
     * 
     */
    public SaveInventoryAdjustmentResponse createSaveInventoryAdjustmentResponse() {
        return new SaveInventoryAdjustmentResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelInventoryAdjustment }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelInventoryAdjustment }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "cancelInventoryAdjustment")
    public JAXBElement<CancelInventoryAdjustment> createCancelInventoryAdjustment(CancelInventoryAdjustment value) {
        return new JAXBElement<CancelInventoryAdjustment>(_CancelInventoryAdjustment_QNAME, CancelInventoryAdjustment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelInventoryAdjustmentResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CancelInventoryAdjustmentResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "cancelInventoryAdjustmentResponse")
    public JAXBElement<CancelInventoryAdjustmentResponse> createCancelInventoryAdjustmentResponse(CancelInventoryAdjustmentResponse value) {
        return new JAXBElement<CancelInventoryAdjustmentResponse>(_CancelInventoryAdjustmentResponse_QNAME, CancelInventoryAdjustmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmInventoryAdjustment }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConfirmInventoryAdjustment }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "confirmInventoryAdjustment")
    public JAXBElement<ConfirmInventoryAdjustment> createConfirmInventoryAdjustment(ConfirmInventoryAdjustment value) {
        return new JAXBElement<ConfirmInventoryAdjustment>(_ConfirmInventoryAdjustment_QNAME, ConfirmInventoryAdjustment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmInventoryAdjustmentResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ConfirmInventoryAdjustmentResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "confirmInventoryAdjustmentResponse")
    public JAXBElement<ConfirmInventoryAdjustmentResponse> createConfirmInventoryAdjustmentResponse(ConfirmInventoryAdjustmentResponse value) {
        return new JAXBElement<ConfirmInventoryAdjustmentResponse>(_ConfirmInventoryAdjustmentResponse_QNAME, ConfirmInventoryAdjustmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentHeader }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentHeader }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentHeader")
    public JAXBElement<LookupInventoryAdjustmentHeader> createLookupInventoryAdjustmentHeader(LookupInventoryAdjustmentHeader value) {
        return new JAXBElement<LookupInventoryAdjustmentHeader>(_LookupInventoryAdjustmentHeader_QNAME, LookupInventoryAdjustmentHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentHeaderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentHeaderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentHeaderResponse")
    public JAXBElement<LookupInventoryAdjustmentHeaderResponse> createLookupInventoryAdjustmentHeaderResponse(LookupInventoryAdjustmentHeaderResponse value) {
        return new JAXBElement<LookupInventoryAdjustmentHeaderResponse>(_LookupInventoryAdjustmentHeaderResponse_QNAME, LookupInventoryAdjustmentHeaderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentReason }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentReason }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentReason")
    public JAXBElement<LookupInventoryAdjustmentReason> createLookupInventoryAdjustmentReason(LookupInventoryAdjustmentReason value) {
        return new JAXBElement<LookupInventoryAdjustmentReason>(_LookupInventoryAdjustmentReason_QNAME, LookupInventoryAdjustmentReason.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentReasonResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentReasonResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentReasonResponse")
    public JAXBElement<LookupInventoryAdjustmentReasonResponse> createLookupInventoryAdjustmentReasonResponse(LookupInventoryAdjustmentReasonResponse value) {
        return new JAXBElement<LookupInventoryAdjustmentReasonResponse>(_LookupInventoryAdjustmentReasonResponse_QNAME, LookupInventoryAdjustmentReasonResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentTemplateHeader }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentTemplateHeader }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentTemplateHeader")
    public JAXBElement<LookupInventoryAdjustmentTemplateHeader> createLookupInventoryAdjustmentTemplateHeader(LookupInventoryAdjustmentTemplateHeader value) {
        return new JAXBElement<LookupInventoryAdjustmentTemplateHeader>(_LookupInventoryAdjustmentTemplateHeader_QNAME, LookupInventoryAdjustmentTemplateHeader.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentTemplateHeaderResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupInventoryAdjustmentTemplateHeaderResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupInventoryAdjustmentTemplateHeaderResponse")
    public JAXBElement<LookupInventoryAdjustmentTemplateHeaderResponse> createLookupInventoryAdjustmentTemplateHeaderResponse(LookupInventoryAdjustmentTemplateHeaderResponse value) {
        return new JAXBElement<LookupInventoryAdjustmentTemplateHeaderResponse>(_LookupInventoryAdjustmentTemplateHeaderResponse_QNAME, LookupInventoryAdjustmentTemplateHeaderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupNonSellableQuantityType }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupNonSellableQuantityType }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupNonSellableQuantityType")
    public JAXBElement<LookupNonSellableQuantityType> createLookupNonSellableQuantityType(LookupNonSellableQuantityType value) {
        return new JAXBElement<LookupNonSellableQuantityType>(_LookupNonSellableQuantityType_QNAME, LookupNonSellableQuantityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LookupNonSellableQuantityTypeResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link LookupNonSellableQuantityTypeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "lookupNonSellableQuantityTypeResponse")
    public JAXBElement<LookupNonSellableQuantityTypeResponse> createLookupNonSellableQuantityTypeResponse(LookupNonSellableQuantityTypeResponse value) {
        return new JAXBElement<LookupNonSellableQuantityTypeResponse>(_LookupNonSellableQuantityTypeResponse_QNAME, LookupNonSellableQuantityTypeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "ping")
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
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "readInventoryAdjustmentDetail")
    public JAXBElement<ReadInventoryAdjustmentDetail> createReadInventoryAdjustmentDetail(ReadInventoryAdjustmentDetail value) {
        return new JAXBElement<ReadInventoryAdjustmentDetail>(_ReadInventoryAdjustmentDetail_QNAME, ReadInventoryAdjustmentDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "readInventoryAdjustmentDetailResponse")
    public JAXBElement<ReadInventoryAdjustmentDetailResponse> createReadInventoryAdjustmentDetailResponse(ReadInventoryAdjustmentDetailResponse value) {
        return new JAXBElement<ReadInventoryAdjustmentDetailResponse>(_ReadInventoryAdjustmentDetailResponse_QNAME, ReadInventoryAdjustmentDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentTemplateDetail }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentTemplateDetail }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "readInventoryAdjustmentTemplateDetail")
    public JAXBElement<ReadInventoryAdjustmentTemplateDetail> createReadInventoryAdjustmentTemplateDetail(ReadInventoryAdjustmentTemplateDetail value) {
        return new JAXBElement<ReadInventoryAdjustmentTemplateDetail>(_ReadInventoryAdjustmentTemplateDetail_QNAME, ReadInventoryAdjustmentTemplateDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentTemplateDetailResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ReadInventoryAdjustmentTemplateDetailResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "readInventoryAdjustmentTemplateDetailResponse")
    public JAXBElement<ReadInventoryAdjustmentTemplateDetailResponse> createReadInventoryAdjustmentTemplateDetailResponse(ReadInventoryAdjustmentTemplateDetailResponse value) {
        return new JAXBElement<ReadInventoryAdjustmentTemplateDetailResponse>(_ReadInventoryAdjustmentTemplateDetailResponse_QNAME, ReadInventoryAdjustmentTemplateDetailResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAndConfirmInventoryAdjustment }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SaveAndConfirmInventoryAdjustment }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "saveAndConfirmInventoryAdjustment")
    public JAXBElement<SaveAndConfirmInventoryAdjustment> createSaveAndConfirmInventoryAdjustment(SaveAndConfirmInventoryAdjustment value) {
        return new JAXBElement<SaveAndConfirmInventoryAdjustment>(_SaveAndConfirmInventoryAdjustment_QNAME, SaveAndConfirmInventoryAdjustment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveAndConfirmInventoryAdjustmentResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SaveAndConfirmInventoryAdjustmentResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "saveAndConfirmInventoryAdjustmentResponse")
    public JAXBElement<SaveAndConfirmInventoryAdjustmentResponse> createSaveAndConfirmInventoryAdjustmentResponse(SaveAndConfirmInventoryAdjustmentResponse value) {
        return new JAXBElement<SaveAndConfirmInventoryAdjustmentResponse>(_SaveAndConfirmInventoryAdjustmentResponse_QNAME, SaveAndConfirmInventoryAdjustmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveInventoryAdjustment }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SaveInventoryAdjustment }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "saveInventoryAdjustment")
    public JAXBElement<SaveInventoryAdjustment> createSaveInventoryAdjustment(SaveInventoryAdjustment value) {
        return new JAXBElement<SaveInventoryAdjustment>(_SaveInventoryAdjustment_QNAME, SaveInventoryAdjustment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SaveInventoryAdjustmentResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link SaveInventoryAdjustmentResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1", name = "saveInventoryAdjustmentResponse")
    public JAXBElement<SaveInventoryAdjustmentResponse> createSaveInventoryAdjustmentResponse(SaveInventoryAdjustmentResponse value) {
        return new JAXBElement<SaveInventoryAdjustmentResponse>(_SaveInventoryAdjustmentResponse_QNAME, SaveInventoryAdjustmentResponse.class, null, value);
    }

}
