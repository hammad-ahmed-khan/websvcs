
package com.oracle.retail.oms.integration.services.customerorderservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.oms.integration.services.customerorderservice.v1 package. 
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

    private final static QName _ReturnCustomerOrderItems_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "returnCustomerOrderItems");
    private final static QName _PickupCustomerOrderItemsResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "pickupCustomerOrderItemsResponse");
    private final static QName _RequestNewCustomerOrderId_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "requestNewCustomerOrderId");
    private final static QName _QueryCustomerOrderResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "queryCustomerOrderResponse");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "pingResponse");
    private final static QName _RequestNewCustomerOrderIdResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "requestNewCustomerOrderIdResponse");
    private final static QName _QueryCustomerOrder_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "queryCustomerOrder");
    private final static QName _CreateCustomerOrder_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "createCustomerOrder");
    private final static QName _UpdateReceiptResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "updateReceiptResponse");
    private final static QName _CreateCustomerOrderResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "createCustomerOrderResponse");
    private final static QName _ReturnCustomerOrderItemsResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "returnCustomerOrderItemsResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "ping");
    private final static QName _CancelNewCustomerOrderId_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "cancelNewCustomerOrderId");
    private final static QName _PickupCustomerOrderItems_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "pickupCustomerOrderItems");
    private final static QName _UpdateReceipt_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "updateReceipt");
    private final static QName _CancelNewCustomerOrderIdResponse_QNAME = new QName("http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", "cancelNewCustomerOrderIdResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.oms.integration.services.customerorderservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryCustomerOrderResponse }
     * 
     */
    public QueryCustomerOrderResponse createQueryCustomerOrderResponse() {
        return new QueryCustomerOrderResponse();
    }

    /**
     * Create an instance of {@link PickupCustomerOrderItemsResponse }
     * 
     */
    public PickupCustomerOrderItemsResponse createPickupCustomerOrderItemsResponse() {
        return new PickupCustomerOrderItemsResponse();
    }

    /**
     * Create an instance of {@link RequestNewCustomerOrderId }
     * 
     */
    public RequestNewCustomerOrderId createRequestNewCustomerOrderId() {
        return new RequestNewCustomerOrderId();
    }

    /**
     * Create an instance of {@link ReturnCustomerOrderItems }
     * 
     */
    public ReturnCustomerOrderItems createReturnCustomerOrderItems() {
        return new ReturnCustomerOrderItems();
    }

    /**
     * Create an instance of {@link QueryCustomerOrder }
     * 
     */
    public QueryCustomerOrder createQueryCustomerOrder() {
        return new QueryCustomerOrder();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link RequestNewCustomerOrderIdResponse }
     * 
     */
    public RequestNewCustomerOrderIdResponse createRequestNewCustomerOrderIdResponse() {
        return new RequestNewCustomerOrderIdResponse();
    }

    /**
     * Create an instance of {@link UpdateReceiptResponse }
     * 
     */
    public UpdateReceiptResponse createUpdateReceiptResponse() {
        return new UpdateReceiptResponse();
    }

    /**
     * Create an instance of {@link CreateCustomerOrder }
     * 
     */
    public CreateCustomerOrder createCreateCustomerOrder() {
        return new CreateCustomerOrder();
    }

    /**
     * Create an instance of {@link UpdateReceipt }
     * 
     */
    public UpdateReceipt createUpdateReceipt() {
        return new UpdateReceipt();
    }

    /**
     * Create an instance of {@link CancelNewCustomerOrderIdResponse }
     * 
     */
    public CancelNewCustomerOrderIdResponse createCancelNewCustomerOrderIdResponse() {
        return new CancelNewCustomerOrderIdResponse();
    }

    /**
     * Create an instance of {@link PickupCustomerOrderItems }
     * 
     */
    public PickupCustomerOrderItems createPickupCustomerOrderItems() {
        return new PickupCustomerOrderItems();
    }

    /**
     * Create an instance of {@link ReturnCustomerOrderItemsResponse }
     * 
     */
    public ReturnCustomerOrderItemsResponse createReturnCustomerOrderItemsResponse() {
        return new ReturnCustomerOrderItemsResponse();
    }

    /**
     * Create an instance of {@link CreateCustomerOrderResponse }
     * 
     */
    public CreateCustomerOrderResponse createCreateCustomerOrderResponse() {
        return new CreateCustomerOrderResponse();
    }

    /**
     * Create an instance of {@link CancelNewCustomerOrderId }
     * 
     */
    public CancelNewCustomerOrderId createCancelNewCustomerOrderId() {
        return new CancelNewCustomerOrderId();
    }

    /**
     * Create an instance of {@link Ping }
     * 
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link NewpickupCustomerOrderItemsResponse }
     * 
     */
    public NewpickupCustomerOrderItemsResponse createNewpickupCustomerOrderItemsResponse() {
        return new NewpickupCustomerOrderItemsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnCustomerOrderItems }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "returnCustomerOrderItems")
    public JAXBElement<ReturnCustomerOrderItems> createReturnCustomerOrderItems(ReturnCustomerOrderItems value) {
        return new JAXBElement<ReturnCustomerOrderItems>(_ReturnCustomerOrderItems_QNAME, ReturnCustomerOrderItems.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PickupCustomerOrderItemsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "pickupCustomerOrderItemsResponse")
    public JAXBElement<PickupCustomerOrderItemsResponse> createPickupCustomerOrderItemsResponse(PickupCustomerOrderItemsResponse value) {
        return new JAXBElement<PickupCustomerOrderItemsResponse>(_PickupCustomerOrderItemsResponse_QNAME, PickupCustomerOrderItemsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestNewCustomerOrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "requestNewCustomerOrderId")
    public JAXBElement<RequestNewCustomerOrderId> createRequestNewCustomerOrderId(RequestNewCustomerOrderId value) {
        return new JAXBElement<RequestNewCustomerOrderId>(_RequestNewCustomerOrderId_QNAME, RequestNewCustomerOrderId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCustomerOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "queryCustomerOrderResponse")
    public JAXBElement<QueryCustomerOrderResponse> createQueryCustomerOrderResponse(QueryCustomerOrderResponse value) {
        return new JAXBElement<QueryCustomerOrderResponse>(_QueryCustomerOrderResponse_QNAME, QueryCustomerOrderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestNewCustomerOrderIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "requestNewCustomerOrderIdResponse")
    public JAXBElement<RequestNewCustomerOrderIdResponse> createRequestNewCustomerOrderIdResponse(RequestNewCustomerOrderIdResponse value) {
        return new JAXBElement<RequestNewCustomerOrderIdResponse>(_RequestNewCustomerOrderIdResponse_QNAME, RequestNewCustomerOrderIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryCustomerOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "queryCustomerOrder")
    public JAXBElement<QueryCustomerOrder> createQueryCustomerOrder(QueryCustomerOrder value) {
        return new JAXBElement<QueryCustomerOrder>(_QueryCustomerOrder_QNAME, QueryCustomerOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCustomerOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "createCustomerOrder")
    public JAXBElement<CreateCustomerOrder> createCreateCustomerOrder(CreateCustomerOrder value) {
        return new JAXBElement<CreateCustomerOrder>(_CreateCustomerOrder_QNAME, CreateCustomerOrder.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateReceiptResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "updateReceiptResponse")
    public JAXBElement<UpdateReceiptResponse> createUpdateReceiptResponse(UpdateReceiptResponse value) {
        return new JAXBElement<UpdateReceiptResponse>(_UpdateReceiptResponse_QNAME, UpdateReceiptResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateCustomerOrderResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "createCustomerOrderResponse")
    public JAXBElement<CreateCustomerOrderResponse> createCreateCustomerOrderResponse(CreateCustomerOrderResponse value) {
        return new JAXBElement<CreateCustomerOrderResponse>(_CreateCustomerOrderResponse_QNAME, CreateCustomerOrderResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReturnCustomerOrderItemsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "returnCustomerOrderItemsResponse")
    public JAXBElement<ReturnCustomerOrderItemsResponse> createReturnCustomerOrderItemsResponse(ReturnCustomerOrderItemsResponse value) {
        return new JAXBElement<ReturnCustomerOrderItemsResponse>(_ReturnCustomerOrderItemsResponse_QNAME, ReturnCustomerOrderItemsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelNewCustomerOrderId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "cancelNewCustomerOrderId")
    public JAXBElement<CancelNewCustomerOrderId> createCancelNewCustomerOrderId(CancelNewCustomerOrderId value) {
        return new JAXBElement<CancelNewCustomerOrderId>(_CancelNewCustomerOrderId_QNAME, CancelNewCustomerOrderId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PickupCustomerOrderItems }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "pickupCustomerOrderItems")
    public JAXBElement<PickupCustomerOrderItems> createPickupCustomerOrderItems(PickupCustomerOrderItems value) {
        return new JAXBElement<PickupCustomerOrderItems>(_PickupCustomerOrderItems_QNAME, PickupCustomerOrderItems.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateReceipt }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "updateReceipt")
    public JAXBElement<UpdateReceipt> createUpdateReceipt(UpdateReceipt value) {
        return new JAXBElement<UpdateReceipt>(_UpdateReceipt_QNAME, UpdateReceipt.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelNewCustomerOrderIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/oms/integration/services/CustomerOrderService/v1", name = "cancelNewCustomerOrderIdResponse")
    public JAXBElement<CancelNewCustomerOrderIdResponse> createCancelNewCustomerOrderIdResponse(CancelNewCustomerOrderIdResponse value) {
        return new JAXBElement<CancelNewCustomerOrderIdResponse>(_CancelNewCustomerOrderIdResponse_QNAME, CancelNewCustomerOrderIdResponse.class, null, value);
    }

}
