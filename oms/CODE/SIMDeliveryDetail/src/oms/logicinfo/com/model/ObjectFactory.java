
package oms.logicinfo.com.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the oms.logicinfo.com.model package. 
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

    private final static QName _ProcessOrderReq_QNAME = new QName("http://com.logicinfo.oms/model/", "processOrderReq");
    private final static QName _ProcessOrderResp_QNAME = new QName("http://com.logicinfo.oms/model/", "processOrderResp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oms.logicinfo.com.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SIMDeliveryDetails }
     * 
     */
    public SIMDeliveryDetail createSIMDeliveryDetails() {
        return new SIMDeliveryDetail();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link CustomerOrderResponseAddress }
     * 
     */
    public CustomerOrderResponseAddress createCustomerOrderResponseAddress() {
        return new CustomerOrderResponseAddress();
    }

    /**
     * Create an instance of {@link CustomerOrderResponseItems }
     * 
     */
    public CustomerOrderResponseItems createCustomerOrderResponseItems() {
        return new CustomerOrderResponseItems();
    }

    /**
     * Create an instance of {@link SIMDeliveryDetailsResponse }
     * 
     */
    public SimDeliveryDetailResponse createSIMDeliveryDetailsResponse() {
        return new SimDeliveryDetailResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SIMDeliveryDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "processOrderReq")
    public JAXBElement<SIMDeliveryDetail> createProcessOrderReq(SIMDeliveryDetail value) {
        return new JAXBElement<SIMDeliveryDetail>(_ProcessOrderReq_QNAME, SIMDeliveryDetail.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SIMDeliveryDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://com.logicinfo.oms/model/", name = "processOrderResp")
    public JAXBElement<SimDeliveryDetailResponse> createProcessOrderResp(SimDeliveryDetailResponse value) {
        return new JAXBElement<SimDeliveryDetailResponse>(_ProcessOrderResp_QNAME, SimDeliveryDetailResponse.class, null, value);
    }

}
