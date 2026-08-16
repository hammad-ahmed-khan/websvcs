
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

    private final static QName _SparePartsConfirmation_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsConfirmation");
    private final static QName _SparePartsConfirmationResponse_QNAME = new QName("http://com.logicinfo.oms/services/", "SparePartsConfirmationResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SparePartsConfirmation }
     * 
     */
    public SparePartsConfirmation createSparePartsConfirmation() {
        return new SparePartsConfirmation();
    }

    /**
     * Create an instance of {@link SparePartsConfirmationResponse }
     * 
     */
    public SparePartsConfirmationResponse createSparePartsConfirmationResponse() {
        return new SparePartsConfirmationResponse();
    }

    /**
     * Create an instance of {@link ServiceConfDetailsResponseType }
     * 
     */
    public ServiceConfDetailsResponseType createServiceConfDetailsResponseType() {
        return new ServiceConfDetailsResponseType();
    }

    /**
     * Create an instance of {@link ServiceConfirmationDetailType }
     * 
     */
    public ServiceConfirmationDetailType createServiceConfirmationDetailType() {
        return new ServiceConfirmationDetailType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsConfirmation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsConfirmation")
    public JAXBElement<SparePartsConfirmation> createSparePartsConfirmation(SparePartsConfirmation value) {
        return new JAXBElement<SparePartsConfirmation>(_SparePartsConfirmation_QNAME, SparePartsConfirmation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SparePartsConfirmationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/services/", name = "SparePartsConfirmationResponse")
    public JAXBElement<SparePartsConfirmationResponse> createSparePartsConfirmationResponse(SparePartsConfirmationResponse value) {
        return new JAXBElement<SparePartsConfirmationResponse>(_SparePartsConfirmationResponse_QNAME, SparePartsConfirmationResponse.class, null, value);
    }

}
