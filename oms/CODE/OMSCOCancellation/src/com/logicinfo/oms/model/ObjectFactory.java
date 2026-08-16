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
public class ObjectFactory
{
private final static QName _CancelOrder_QNAME=new QName("http://com.logicinfo.oms/model/","cancelOrder");
private final static QName _CancelOrderResponse_QNAME=
  new QName("http://com.logicinfo.oms/model/","cancelOrderResponse");

/**
 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.logicinfo.oms.model
 *
 */
public ObjectFactory()
{
}

/**
 * Create an instance of {@link CustomerOrderCancellation }
 *
 */
public CustomerOrderCancellation createCustomerOrderCancellation()
{
  return new CustomerOrderCancellation();
}

/**
 * Create an instance of {@link CustomerOrderCancellationResponse }
 *
 */
public CustomerOrderCancellationResponse createCustomerOrderCancellationResponse()
{
  return new CustomerOrderCancellationResponse();
}

/**
 * Create an instance of {@link CustomerOrderCancelResponseItems }
 *
 */
public CustomerOrderCancelResponseItems createCustomerOrderCancelResponseItems()
{
  return new CustomerOrderCancelResponseItems();
}

/**
 * Create an instance of {@link CustomerOrderCancellationItems }
 *
 */
public CustomerOrderCancellationItems createCustomerOrderCancellationItems()
{
  return new CustomerOrderCancellationItems();
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrderCancellation }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="cancelOrder")
public JAXBElement<CustomerOrderCancellation> createCancelOrder(CustomerOrderCancellation value)
{
  return new JAXBElement<CustomerOrderCancellation>(_CancelOrder_QNAME,CustomerOrderCancellation.class,null,value);
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrderCancellationResponse }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="cancelOrderResponse")
public JAXBElement<CustomerOrderCancellationResponse> createCancelOrderResponse(CustomerOrderCancellationResponse value)
{
  return new JAXBElement<CustomerOrderCancellationResponse>(_CancelOrderResponse_QNAME,
                                                            CustomerOrderCancellationResponse.class,null,value);
}
}
