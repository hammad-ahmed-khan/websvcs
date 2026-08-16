
package com.oracle.retail.integration.services.exception.v1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.retail.integration.services.exception.v1 package. 
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

    private final static QName _ValidationWSFaultException_QNAME = new QName("http://www.oracle.com/retail/integration/services/exception/v1", "ValidationWSFaultException");
    private final static QName _IllegalArgumentWSFaultException_QNAME = new QName("http://www.oracle.com/retail/integration/services/exception/v1", "IllegalArgumentWSFaultException");
    private final static QName _IllegalStateWSFaultException_QNAME = new QName("http://www.oracle.com/retail/integration/services/exception/v1", "IllegalStateWSFaultException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.retail.integration.services.exception.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IllegalArgumentWSFaultException }
     * 
     */
    public IllegalArgumentWSFaultException createIllegalArgumentWSFaultException() {
        return new IllegalArgumentWSFaultException();
    }

    /**
     * Create an instance of {@link ServiceOpFaultReason }
     * 
     */
    public ServiceOpFaultReason createServiceOpFaultReason() {
        return new ServiceOpFaultReason();
    }

    /**
     * Create an instance of {@link BusinessProblemDetail }
     * 
     */
    public BusinessProblemDetail createBusinessProblemDetail() {
        return new BusinessProblemDetail();
    }

    /**
     * Create an instance of {@link ProblemDetailEntry }
     * 
     */
    public ProblemDetailEntry createProblemDetailEntry() {
        return new ProblemDetailEntry();
    }

    /**
     * Create an instance of {@link IllegalStateWSFaultException }
     * 
     */
    public IllegalStateWSFaultException createIllegalStateWSFaultException() {
        return new IllegalStateWSFaultException();
    }

    /**
     * Create an instance of {@link ValidationWSFaultException }
     * 
     */
    public ValidationWSFaultException createValidationWSFaultException() {
        return new ValidationWSFaultException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidationWSFaultException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/integration/services/exception/v1", name = "ValidationWSFaultException")
    public JAXBElement<ValidationWSFaultException> createValidationWSFaultException(ValidationWSFaultException value) {
        return new JAXBElement<ValidationWSFaultException>(_ValidationWSFaultException_QNAME, ValidationWSFaultException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalArgumentWSFaultException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/integration/services/exception/v1", name = "IllegalArgumentWSFaultException")
    public JAXBElement<IllegalArgumentWSFaultException> createIllegalArgumentWSFaultException(IllegalArgumentWSFaultException value) {
        return new JAXBElement<IllegalArgumentWSFaultException>(_IllegalArgumentWSFaultException_QNAME, IllegalArgumentWSFaultException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IllegalStateWSFaultException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.oracle.com/retail/integration/services/exception/v1", name = "IllegalStateWSFaultException")
    public JAXBElement<IllegalStateWSFaultException> createIllegalStateWSFaultException(IllegalStateWSFaultException value) {
        return new JAXBElement<IllegalStateWSFaultException>(_IllegalStateWSFaultException_QNAME, IllegalStateWSFaultException.class, null, value);
    }

}
