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
public class ObjectFactory
{
private final static QName _ProcessBackOrderReq_QNAME=
  new QName("http://com.logicinfo.oms/model/","processBackOrderReq");
private final static QName _ProcessBackOrderResp_QNAME=
  new QName("http://com.logicinfo.oms/model/","processBackOrderResp");
private final static QName _ProcessNewOrderResp_QNAME=
  new QName("http://com.logicinfo.oms/model/","processNewOrderResp");
private final static QName _ProcessNewOrderReq_QNAME=new QName("http://com.logicinfo.oms/model/","processNewOrderReq");

/**
 * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: oms.logicinfo.com.model
 *
 */
public ObjectFactory()
{
}

/**
 * Create an instance of {@link BackOrderRequest }
 *
 */
public BackOrderRequest createBackOrderRequest()
{
  return new BackOrderRequest();
}

/**
 * Create an instance of {@link CustomerOrderResponse }
 *
 */
public CustomerOrderResponse createCustomerOrderResponse()
{
  return new CustomerOrderResponse();
}

/**
 * Create an instance of {@link BackOrderResponse }
 *
 */
public BackOrderResponse createBackOrderResponse()
{
  return new BackOrderResponse();
}

/**
 * Create an instance of {@link CustomerOrder }
 *
 */
public CustomerOrder createCustomerOrder()
{
  return new CustomerOrder();
}

/**
 * Create an instance of {@link CustomerOrderTenders }
 *
 */
public CustomerOrderTenders createCustomerOrderTenders()
{
  return new CustomerOrderTenders();
}

/**
 * Create an instance of {@link CustOrdItemDisc }
 *
 */
public CustOrdItemDisc createCustOrdItemDisc()
{
  return new CustOrdItemDisc();
}

/**
 * Create an instance of {@link CustomerOrderItems }
 *
 */
public CustomerOrderItems createCustomerOrderItems()
{
  return new CustomerOrderItems();
}

/**
 * Create an instance of {@link CustomerOrderResponseItemFulfillment }
 *
 */
public CustomerOrderResponseItemFulfillment createCustomerOrderResponseItemFulfillment()
{
  return new CustomerOrderResponseItemFulfillment();
}

/**
 * Create an instance of {@link CustomerOrderAddress }
 *
 */
public CustomerOrderAddress createCustomerOrderAddress()
{
  return new CustomerOrderAddress();
}

/**
 * Create an instance of {@link CustomerOrderResponseItems }
 *
 */
public CustomerOrderResponseItems createCustomerOrderResponseItems()
{
  return new CustomerOrderResponseItems();
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link BackOrderRequest }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="processBackOrderReq")
public JAXBElement<BackOrderRequest> createProcessBackOrderReq(BackOrderRequest value)
{
  return new JAXBElement<BackOrderRequest>(_ProcessBackOrderReq_QNAME,BackOrderRequest.class,null,value);
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link BackOrderResponse }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="processBackOrderResp")
public JAXBElement<BackOrderResponse> createProcessBackOrderResp(BackOrderResponse value)
{
  return new JAXBElement<BackOrderResponse>(_ProcessBackOrderResp_QNAME,BackOrderResponse.class,null,value);
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrderResponse }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="processNewOrderResp")
public JAXBElement<CustomerOrderResponse> createProcessNewOrderResp(CustomerOrderResponse value)
{
  return new JAXBElement<CustomerOrderResponse>(_ProcessNewOrderResp_QNAME,CustomerOrderResponse.class,null,value);
}

/**
 * Create an instance of {@link JAXBElement }{@code <}{@link CustomerOrder }{@code >}}
 *
 */
@XmlElementDecl(namespace="http://com.logicinfo.oms/model/",name="processNewOrderReq")
public JAXBElement<CustomerOrder> createProcessNewOrderReq(CustomerOrder value)
{
  return new JAXBElement<CustomerOrder>(_ProcessNewOrderReq_QNAME,CustomerOrder.class,null,value);
}
}
