
package com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1 package. 
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

    private final static QName _PendReturnDtlModifyResponse_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlModifyResponse");
    private final static QName _PendReturnDtlModify_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlModify");
    private final static QName _PendReturnDtlDeleteResponse_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlDeleteResponse");
    private final static QName _PendReturnDtlCreate_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlCreate");
    private final static QName _PendReturnDtlDelete_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlDelete");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "ping");
    private final static QName _PendReturnDtlCreateResponse_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pendReturnDtlCreateResponse");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", "pingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlModify}
     *
     */
    public PendReturnDtlModify createPendReturnDtlModify() {
        return new PendReturnDtlModify();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlModifyResponse}
     *
     */
    public PendReturnDtlModifyResponse createPendReturnDtlModifyResponse() {
        return new PendReturnDtlModifyResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlCreate}
     *
     */
    public PendReturnDtlCreate createPendReturnDtlCreate() {
        return new PendReturnDtlCreate();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlDeleteResponse}
     *
     */
    public PendReturnDtlDeleteResponse createPendReturnDtlDeleteResponse() {
        return new PendReturnDtlDeleteResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.Ping}
     *
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlDelete}
     *
     */
    public PendReturnDtlDelete createPendReturnDtlDelete() {
        return new PendReturnDtlDelete();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PingResponse}
     *
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlCreateResponse}
     *
     */
    public PendReturnDtlCreateResponse createPendReturnDtlCreateResponse() {
        return new PendReturnDtlCreateResponse();
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlModifyResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlModifyResponse")
    public JAXBElement<PendReturnDtlModifyResponse> createPendReturnDtlModifyResponse(PendReturnDtlModifyResponse value) {
        return new JAXBElement<PendReturnDtlModifyResponse>(_PendReturnDtlModifyResponse_QNAME, PendReturnDtlModifyResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlModify} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlModify")
    public JAXBElement<PendReturnDtlModify> createPendReturnDtlModify(PendReturnDtlModify value) {
        return new JAXBElement<PendReturnDtlModify>(_PendReturnDtlModify_QNAME, PendReturnDtlModify.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlDeleteResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlDeleteResponse")
    public JAXBElement<PendReturnDtlDeleteResponse> createPendReturnDtlDeleteResponse(PendReturnDtlDeleteResponse value) {
        return new JAXBElement<PendReturnDtlDeleteResponse>(_PendReturnDtlDeleteResponse_QNAME, PendReturnDtlDeleteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlCreate} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlCreate")
    public JAXBElement<PendReturnDtlCreate> createPendReturnDtlCreate(PendReturnDtlCreate value) {
        return new JAXBElement<PendReturnDtlCreate>(_PendReturnDtlCreate_QNAME, PendReturnDtlCreate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlDelete} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlDelete")
    public JAXBElement<PendReturnDtlDelete> createPendReturnDtlDelete(PendReturnDtlDelete value) {
        return new JAXBElement<PendReturnDtlDelete>(_PendReturnDtlDelete_QNAME, PendReturnDtlDelete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.Ping} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PendReturnDtlCreateResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pendReturnDtlCreateResponse")
    public JAXBElement<PendReturnDtlCreateResponse> createPendReturnDtlCreateResponse(PendReturnDtlCreateResponse value) {
        return new JAXBElement<PendReturnDtlCreateResponse>(_PendReturnDtlCreateResponse_QNAME, PendReturnDtlCreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.rwms.integration.services.pendingreturnsservice.v1.PingResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/rwms/integration/services/PendingReturnsService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

}
