
package com.oracle.retail.sim.integration.services.postransactionservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.postransactionservice.v1 package. 
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

    private final static QName _ProcessPOSTransactionsResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", "processPOSTransactionsResponse");
    private final static QName _ProcessPOSTransactions_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", "processPOSTransactions");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", "pingResponse");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", "ping");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.postransactionservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProcessPOSTransactions }
     * 
     */
    public ProcessPOSTransactions createProcessPOSTransactions() {
        return new ProcessPOSTransactions();
    }

    /**
     * Create an instance of {@link ProcessPOSTransactionsResponse }
     * 
     */
    public ProcessPOSTransactionsResponse createProcessPOSTransactionsResponse() {
        return new ProcessPOSTransactionsResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessPOSTransactionsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", name = "processPOSTransactionsResponse")
    public JAXBElement<ProcessPOSTransactionsResponse> createProcessPOSTransactionsResponse(ProcessPOSTransactionsResponse value) {
        return new JAXBElement<ProcessPOSTransactionsResponse>(_ProcessPOSTransactionsResponse_QNAME, ProcessPOSTransactionsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProcessPOSTransactions }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", name = "processPOSTransactions")
    public JAXBElement<ProcessPOSTransactions> createProcessPOSTransactions(ProcessPOSTransactions value) {
        return new JAXBElement<ProcessPOSTransactions>(_ProcessPOSTransactions_QNAME, ProcessPOSTransactions.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Ping }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/POSTransactionService/v1", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

}
