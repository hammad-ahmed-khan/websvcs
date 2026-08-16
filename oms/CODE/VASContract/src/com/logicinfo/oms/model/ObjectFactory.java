
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

    private final static QName _VASContractResp_QNAME = new QName("http://com.logicinfo.oms/model/", "VASContractResp");
    private final static QName _ProcessVASContractReq_QNAME = new QName("http://com.logicinfo.oms/model/", "processVASContractReq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VASContractResponse }
     * 
     */
    public VASContractResponse createVASContractResponse() {
        return new VASContractResponse();
    }

    /**
     * Create an instance of {@link VASContract }
     * 
     */
    public VASContract createVASContract() {
        return new VASContract();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VASContractResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "VASContractResp")
    public JAXBElement<VASContractResponse> createVASContractResp(VASContractResponse value) {
        return new JAXBElement<VASContractResponse>(_VASContractResp_QNAME, VASContractResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VASContract }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "processVASContractReq")
    public JAXBElement<VASContract> createProcessVASContractReq(VASContract value) {
        return new JAXBElement<VASContract>(_ProcessVASContractReq_QNAME, VASContract.class, null, value);
    }

}
