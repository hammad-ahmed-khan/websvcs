
package com.extra.services.omsstatus;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.datacontract.schemas._2004._07.extra_services.OrderStatus;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.extra.services.omsstatus package. 
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

    private final static QName _UpdateOrderStatusOrderStatus_QNAME = new QName("http://www.extra.com/Services/OmsStatus", "orderStatus");
    private final static QName _UpdateOrderStatusResponseUpdateOrderStatusResult_QNAME = new QName("http://www.extra.com/Services/OmsStatus", "UpdateOrderStatusResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.extra.services.omsstatus
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UpdateOrderStatus }
     * 
     */
    public UpdateOrderStatus createUpdateOrderStatus() {
        return new UpdateOrderStatus();
    }

    /**
     * Create an instance of {@link UpdateOrderStatusResponse }
     * 
     */
    public UpdateOrderStatusResponse createUpdateOrderStatusResponse() {
        return new UpdateOrderStatusResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.extra.com/Services/OmsStatus", name = "orderStatus", scope = UpdateOrderStatus.class)
    public JAXBElement<OrderStatus> createUpdateOrderStatusOrderStatus(OrderStatus value) {
        return new JAXBElement<OrderStatus>(_UpdateOrderStatusOrderStatus_QNAME, OrderStatus.class, UpdateOrderStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://www.extra.com/Services/OmsStatus", name = "UpdateOrderStatusResult", scope = UpdateOrderStatusResponse.class)
    public JAXBElement<String> createUpdateOrderStatusResponseUpdateOrderStatusResult(String value) {
        return new JAXBElement<String>(_UpdateOrderStatusResponseUpdateOrderStatusResult_QNAME, String.class, UpdateOrderStatusResponse.class, value);
    }

}
