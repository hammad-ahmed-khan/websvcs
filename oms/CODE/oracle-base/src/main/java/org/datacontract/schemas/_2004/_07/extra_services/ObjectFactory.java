
package org.datacontract.schemas._2004._07.extra_services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.extra_services package. 
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

    private final static QName _OrderStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", "OrderStatus");
    private final static QName _ArrayOfOrderDetailStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", "ArrayOfOrderDetailStatus");
    private final static QName _OrderDetailStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", "OrderDetailStatus");
    private final static QName _OrderStatusSubOrderId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", "SubOrderId");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.extra_services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrderStatus }
     * 
     */
    public OrderStatus createOrderStatus() {
        return new OrderStatus();
    }

    /**
     * Create an instance of {@link ArrayOfOrderDetailStatus }
     * 
     */
    public ArrayOfOrderDetailStatus createArrayOfOrderDetailStatus() {
        return new ArrayOfOrderDetailStatus();
    }

    /**
     * Create an instance of {@link OrderDetailStatus }
     * 
     */
    public OrderDetailStatus createOrderDetailStatus() {
        return new OrderDetailStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderStatus }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", name = "OrderStatus")
    public JAXBElement<OrderStatus> createOrderStatus(OrderStatus value) {
        return new JAXBElement<OrderStatus>(_OrderStatus_QNAME, OrderStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderDetailStatus }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ArrayOfOrderDetailStatus }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", name = "ArrayOfOrderDetailStatus")
    public JAXBElement<ArrayOfOrderDetailStatus> createArrayOfOrderDetailStatus(ArrayOfOrderDetailStatus value) {
        return new JAXBElement<ArrayOfOrderDetailStatus>(_ArrayOfOrderDetailStatus_QNAME, ArrayOfOrderDetailStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrderDetailStatus }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link OrderDetailStatus }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", name = "OrderDetailStatus")
    public JAXBElement<OrderDetailStatus> createOrderDetailStatus(OrderDetailStatus value) {
        return new JAXBElement<OrderDetailStatus>(_OrderDetailStatus_QNAME, OrderDetailStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Oms", name = "SubOrderId", scope = OrderStatus.class)
    public JAXBElement<String> createOrderStatusSubOrderId(String value) {
        return new JAXBElement<String>(_OrderStatusSubOrderId_QNAME, String.class, OrderStatus.class, value);
    }

}
