
package com.oracle.retail.rms.integration.services.inventorybackorderservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.rms.integration.services.inventorybackorderservice.v1 package. 
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

    private final static QName _CreateInvBackOrdColDesc_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", "createInvBackOrdColDesc");
    private final static QName _CreateInvBackOrdColDescResponse_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", "createInvBackOrdColDescResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", "ping");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", "pingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.rms.integration.services.inventorybackorderservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateInvBackOrdColDesc }
     * 
     */
    public CreateInvBackOrdColDesc createCreateInvBackOrdColDesc() {
        return new CreateInvBackOrdColDesc();
    }

    /**
     * Create an instance of {@link CreateInvBackOrdColDescResponse }
     * 
     */
    public CreateInvBackOrdColDescResponse createCreateInvBackOrdColDescResponse() {
        return new CreateInvBackOrdColDescResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInvBackOrdColDesc }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateInvBackOrdColDesc }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", name = "createInvBackOrdColDesc")
    public JAXBElement<CreateInvBackOrdColDesc> createCreateInvBackOrdColDesc(CreateInvBackOrdColDesc value) {
        return new JAXBElement<CreateInvBackOrdColDesc>(_CreateInvBackOrdColDesc_QNAME, CreateInvBackOrdColDesc.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateInvBackOrdColDescResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CreateInvBackOrdColDescResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", name = "createInvBackOrdColDescResponse")
    public JAXBElement<CreateInvBackOrdColDescResponse> createCreateInvBackOrdColDescResponse(CreateInvBackOrdColDescResponse value) {
        return new JAXBElement<CreateInvBackOrdColDescResponse>(_CreateInvBackOrdColDescResponse_QNAME, CreateInvBackOrdColDescResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", name = "ping")
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
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

}
