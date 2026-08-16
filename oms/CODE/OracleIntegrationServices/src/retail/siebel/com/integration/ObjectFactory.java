
package retail.siebel.com.integration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the retail.siebel.com.integration package. 
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

    private final static QName _SiebelStatusUpdateInfo_QNAME = new QName("http://com.siebel.retail/integration/", "SiebelStatusUpdateInfo");
    private final static QName _SiebelStatusUpdateResponse_QNAME = new QName("http://com.siebel.retail/integration/", "SiebelStatusUpdateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: retail.siebel.com.integration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link .retail.siebel.com.integration.SiebelStatusUpdateResponse}
     *
     */
    public SiebelStatusUpdateResponse createSiebelStatusUpdateResponse() {
        return new SiebelStatusUpdateResponse();
    }

    /**
     * Create an instance of {@link .retail.siebel.com.integration.SiebelStatusUpdateInfo}
     *
     */
    public SiebelStatusUpdateInfo createSiebelStatusUpdateInfo() {
        return new SiebelStatusUpdateInfo();
    }

    /**
     * Create an instance of {@link .retail.siebel.com.integration.SiebelStatusUpdateDetails}
     *
     */
    public SiebelStatusUpdateDetails createSiebelStatusUpdateDetails() {
        return new SiebelStatusUpdateDetails();
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .retail.siebel.com.integration.SiebelStatusUpdateInfo} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://com.siebel.retail/integration/", name = "SiebelStatusUpdateInfo")
    public JAXBElement<SiebelStatusUpdateInfo> createSiebelStatusUpdateInfo(SiebelStatusUpdateInfo value) {
        return new JAXBElement<SiebelStatusUpdateInfo>(_SiebelStatusUpdateInfo_QNAME, SiebelStatusUpdateInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement} {@code <} {@link .retail.siebel.com.integration.SiebelStatusUpdateResponse} {@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://com.siebel.retail/integration/", name = "SiebelStatusUpdateResponse")
    public JAXBElement<SiebelStatusUpdateResponse> createSiebelStatusUpdateResponse(SiebelStatusUpdateResponse value) {
        return new JAXBElement<SiebelStatusUpdateResponse>(_SiebelStatusUpdateResponse_QNAME, SiebelStatusUpdateResponse.class, null, value);
    }

}
