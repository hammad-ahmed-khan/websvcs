
package com.logicinfo.oms.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.logicinfo.oms.model package. 
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

    private final static QName _InventoryCheckResponse_QNAME = new QName("http://com.logicinfo.oms/model/", "inventoryCheckResponse");
    private final static QName _InventoryCheck_QNAME = new QName("http://com.logicinfo.oms/model/", "inventoryCheck");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InventoryCheckResponse }
     * 
     */
    public InventoryCheckResponse createInventoryCheckResponse() {
        return new InventoryCheckResponse();
    }

    /**
     * Create an instance of {@link InventoryCheck }
     * 
     */
    public InventoryCheck createInventoryCheck() {
        return new InventoryCheck();
    }

    /**
     * Create an instance of {@link CustOrdItmDesc }
     * 
     */
    public CustOrdItmDesc createCustOrdItmDesc() {
        return new CustOrdItmDesc();
    }

    /**
     * Create an instance of {@link CustOrdItmDescResponse }
     * 
     */
    public CustOrdItmDescResponse createCustOrdItmDescResponse() {
        return new CustOrdItmDescResponse();
    }

    /**
     * Create an instance of {@link CustOrdFulDescResponse }
     * 
     */
    public CustOrdFulDescResponse createCustOrdFulDescResponse() {
        return new CustOrdFulDescResponse();
    }

    /**
     * Create an instance of {@link CustOrdFulDesc }
     * 
     */
    public CustOrdFulDesc createCustOrdFulDesc() {
        return new CustOrdFulDesc();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InventoryCheckResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "inventoryCheckResponse")
    public JAXBElement<InventoryCheckResponse> createInventoryCheckResponse(InventoryCheckResponse value) {
        return new JAXBElement<InventoryCheckResponse>(_InventoryCheckResponse_QNAME, InventoryCheckResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InventoryCheck }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "inventoryCheck")
    public JAXBElement<InventoryCheck> createInventoryCheck(InventoryCheck value) {
        return new JAXBElement<InventoryCheck>(_InventoryCheck_QNAME, InventoryCheck.class, null, value);
    }

}
