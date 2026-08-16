
package com.oracle.retail.sim.integration.services.storeinventoryservice.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.sim.integration.services.storeinventoryservice.v1 package. 
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

    private final static QName _LookupInventoryForBuddyStores_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryForBuddyStores");
    private final static QName _PingResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "pingResponse");
    private final static QName _LookupInventoryInStore_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInStore");
    private final static QName _LookupInventoryForBuddyStoresResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryForBuddyStoresResponse");
    private final static QName _LookupAvailableInventory_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupAvailableInventory");
    private final static QName _Ping_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "ping");
    private final static QName _LookupInventoryInStoreResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInStoreResponse");
    private final static QName _LookupInventoryInTransferZoneResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInTransferZoneResponse");
    private final static QName _LookupAvailableInventoryResponse_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupAvailableInventoryResponse");
    private final static QName _LookupInventoryInTransferZone_QNAME = new QName("http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", "lookupInventoryInTransferZone");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.sim.integration.services.storeinventoryservice.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInTransferZone}
     *
     */
    public LookupInventoryInTransferZone createLookupInventoryInTransferZone() {
        return new LookupInventoryInTransferZone();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInTransferZoneResponse}
     *
     */
    public LookupInventoryInTransferZoneResponse createLookupInventoryInTransferZoneResponse() {
        return new LookupInventoryInTransferZoneResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupAvailableInventoryResponse}
     *
     */
    public LookupAvailableInventoryResponse createLookupAvailableInventoryResponse() {
        return new LookupAvailableInventoryResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupAvailableInventory}
     *
     */
    public LookupAvailableInventory createLookupAvailableInventory() {
        return new LookupAvailableInventory();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInStore}
     *
     */
    public LookupInventoryInStore createLookupInventoryInStore() {
        return new LookupInventoryInStore();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryForBuddyStoresResponse}
     *
     */
    public LookupInventoryForBuddyStoresResponse createLookupInventoryForBuddyStoresResponse() {
        return new LookupInventoryForBuddyStoresResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInStoreResponse}
     *
     */
    public LookupInventoryInStoreResponse createLookupInventoryInStoreResponse() {
        return new LookupInventoryInStoreResponse();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.Ping}
     *
     */
    public Ping createPing() {
        return new Ping();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryForBuddyStores}
     *
     */
    public LookupInventoryForBuddyStores createLookupInventoryForBuddyStores() {
        return new LookupInventoryForBuddyStores();
    }

    /**
     * Create an instance of {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.PingResponse}
     *
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryForBuddyStores} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryForBuddyStores")
    public JAXBElement<LookupInventoryForBuddyStores> createLookupInventoryForBuddyStores(LookupInventoryForBuddyStores value) {
        return new JAXBElement<LookupInventoryForBuddyStores>(_LookupInventoryForBuddyStores_QNAME, LookupInventoryForBuddyStores.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.PingResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInStore} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryInStore")
    public JAXBElement<LookupInventoryInStore> createLookupInventoryInStore(LookupInventoryInStore value) {
        return new JAXBElement<LookupInventoryInStore>(_LookupInventoryInStore_QNAME, LookupInventoryInStore.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryForBuddyStoresResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryForBuddyStoresResponse")
    public JAXBElement<LookupInventoryForBuddyStoresResponse> createLookupInventoryForBuddyStoresResponse(LookupInventoryForBuddyStoresResponse value) {
        return new JAXBElement<LookupInventoryForBuddyStoresResponse>(_LookupInventoryForBuddyStoresResponse_QNAME, LookupInventoryForBuddyStoresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupAvailableInventory} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupAvailableInventory")
    public JAXBElement<LookupAvailableInventory> createLookupAvailableInventory(LookupAvailableInventory value) {
        return new JAXBElement<LookupAvailableInventory>(_LookupAvailableInventory_QNAME, LookupAvailableInventory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.Ping} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "ping")
    public JAXBElement<Ping> createPing(Ping value) {
        return new JAXBElement<Ping>(_Ping_QNAME, Ping.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInStoreResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryInStoreResponse")
    public JAXBElement<LookupInventoryInStoreResponse> createLookupInventoryInStoreResponse(LookupInventoryInStoreResponse value) {
        return new JAXBElement<LookupInventoryInStoreResponse>(_LookupInventoryInStoreResponse_QNAME, LookupInventoryInStoreResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInTransferZoneResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryInTransferZoneResponse")
    public JAXBElement<LookupInventoryInTransferZoneResponse> createLookupInventoryInTransferZoneResponse(LookupInventoryInTransferZoneResponse value) {
        return new JAXBElement<LookupInventoryInTransferZoneResponse>(_LookupInventoryInTransferZoneResponse_QNAME, LookupInventoryInTransferZoneResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupAvailableInventoryResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupAvailableInventoryResponse")
    public JAXBElement<LookupAvailableInventoryResponse> createLookupAvailableInventoryResponse(LookupAvailableInventoryResponse value) {
        return new JAXBElement<LookupAvailableInventoryResponse>(_LookupAvailableInventoryResponse_QNAME, LookupAvailableInventoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .com.oracle.retail.sim.integration.services.storeinventoryservice.v1.LookupInventoryInTransferZone} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/sim/integration/services/StoreInventoryService/v1", name = "lookupInventoryInTransferZone")
    public JAXBElement<LookupInventoryInTransferZone> createLookupInventoryInTransferZone(LookupInventoryInTransferZone value) {
        return new JAXBElement<LookupInventoryInTransferZone>(_LookupInventoryInTransferZone_QNAME, LookupInventoryInTransferZone.class, null, value);
    }

}
