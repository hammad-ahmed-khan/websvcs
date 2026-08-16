
package org.datacontract.schemas._2004._07.extra_services_shipping;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.datacontract.schemas._2004._07.extra_services_shipping package. 
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

    private final static QName _CreateShipmentResponse_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "CreateShipmentResponse");
    private final static QName _ArrayOfProduct_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ArrayOfProduct");
    private final static QName _Product_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Product");
    private final static QName _CustomerLanguage_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Language");
    private final static QName _CustomerFirstName_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "FirstName");
    private final static QName _CustomerCollectorName_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "CollectorName");
    private final static QName _CustomerMobile_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Mobile");
    private final static QName _CustomerLastName_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "LastName");
    private final static QName _CustomerPhone_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Phone");
    private final static QName _CustomerDeliveryMobile_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "DeliveryMobile");
    private final static QName _CustomerEmail_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Email");
    private final static QName _ShipmentOrderShipmentId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ShipmentId");
    private final static QName _ShipmentOrderShipOrderId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ShipOrderId");
    private final static QName _ProductCartonId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "CartonId");
    private final static QName _ProductProductSku_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ProductSku");
    private final static QName _ProductName_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Name");
    private final static QName _ShipmentOrderValue_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "OrderValue");
    private final static QName _ShipmentIsCod_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "IsCod");
    private final static QName _ShipmentIsExpressDelivery_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "IsExpressDelivery");
    private final static QName _ShipmentProducts_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Products");
    private final static QName _ShipmentCustomer_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Customer");
    private final static QName _ShipmentAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Address");
    private final static QName _ShipmentOrderId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "OrderId");
    private final static QName _ShipmentTrackingNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "TrackingNumber");
    private final static QName _ShipmentStoreId_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "StoreId");
    private final static QName _ShipmentCarrier_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "Carrier");
    private final static QName _ShipmentShipmentOrder_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ShipmentOrder");
    private final static QName _AddressCity_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "City");
    private final static QName _AddressShippingAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "ShippingAddress");
    private final static QName _AddressDistrict_QNAME = new QName("http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", "District");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.datacontract.schemas._2004._07.extra_services_shipping
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ShipmentResponse }
     * 
     */
    public ShipmentResponse createShipmentResponse() {
        return new ShipmentResponse();
    }

    /**
     * Create an instance of {@link Product }
     * 
     */
    public Product createProduct() {
        return new Product();
    }

    /**
     * Create an instance of {@link ArrayOfProduct }
     * 
     */
    public ArrayOfProduct createArrayOfProduct() {
        return new ArrayOfProduct();
    }

    /**
     * Create an instance of {@link Shipment }
     * 
     */
    public Shipment createShipment() {
        return new Shipment();
    }

    /**
     * Create an instance of {@link Address }
     * 
     */
    public Address createAddress() {
        return new Address();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomer() {
        return new Customer();
    }

    /**
     * Create an instance of {@link ShipmentOrder }
     * 
     */
    public ShipmentOrder createShipmentOrder() {
        return new ShipmentOrder();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "CreateShipmentResponse")
    public JAXBElement<ShipmentResponse> createCreateShipmentResponse(ShipmentResponse value) {
        return new JAXBElement<ShipmentResponse>(_CreateShipmentResponse_QNAME, ShipmentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProduct }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ArrayOfProduct")
    public JAXBElement<ArrayOfProduct> createArrayOfProduct(ArrayOfProduct value) {
        return new JAXBElement<ArrayOfProduct>(_ArrayOfProduct_QNAME, ArrayOfProduct.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Product }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Product")
    public JAXBElement<Product> createProduct(Product value) {
        return new JAXBElement<Product>(_Product_QNAME, Product.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Language", scope = Customer.class)
    public JAXBElement<String> createCustomerLanguage(String value) {
        return new JAXBElement<String>(_CustomerLanguage_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "FirstName", scope = Customer.class)
    public JAXBElement<String> createCustomerFirstName(String value) {
        return new JAXBElement<String>(_CustomerFirstName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "CollectorName", scope = Customer.class)
    public JAXBElement<String> createCustomerCollectorName(String value) {
        return new JAXBElement<String>(_CustomerCollectorName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Mobile", scope = Customer.class)
    public JAXBElement<String> createCustomerMobile(String value) {
        return new JAXBElement<String>(_CustomerMobile_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "LastName", scope = Customer.class)
    public JAXBElement<String> createCustomerLastName(String value) {
        return new JAXBElement<String>(_CustomerLastName_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Phone", scope = Customer.class)
    public JAXBElement<String> createCustomerPhone(String value) {
        return new JAXBElement<String>(_CustomerPhone_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "DeliveryMobile", scope = Customer.class)
    public JAXBElement<String> createCustomerDeliveryMobile(String value) {
        return new JAXBElement<String>(_CustomerDeliveryMobile_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Email", scope = Customer.class)
    public JAXBElement<String> createCustomerEmail(String value) {
        return new JAXBElement<String>(_CustomerEmail_QNAME, String.class, Customer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ShipmentId", scope = ShipmentOrder.class)
    public JAXBElement<String> createShipmentOrderShipmentId(String value) {
        return new JAXBElement<String>(_ShipmentOrderShipmentId_QNAME, String.class, ShipmentOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ShipOrderId", scope = ShipmentOrder.class)
    public JAXBElement<String> createShipmentOrderShipOrderId(String value) {
        return new JAXBElement<String>(_ShipmentOrderShipOrderId_QNAME, String.class, ShipmentOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "CartonId", scope = Product.class)
    public JAXBElement<String> createProductCartonId(String value) {
        return new JAXBElement<String>(_ProductCartonId_QNAME, String.class, Product.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ProductSku", scope = Product.class)
    public JAXBElement<String> createProductProductSku(String value) {
        return new JAXBElement<String>(_ProductProductSku_QNAME, String.class, Product.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Name", scope = Product.class)
    public JAXBElement<String> createProductName(String value) {
        return new JAXBElement<String>(_ProductName_QNAME, String.class, Product.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "OrderValue", scope = Shipment.class)
    public JAXBElement<BigDecimal> createShipmentOrderValue(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ShipmentOrderValue_QNAME, BigDecimal.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "IsCod", scope = Shipment.class)
    public JAXBElement<Boolean> createShipmentIsCod(Boolean value) {
        return new JAXBElement<Boolean>(_ShipmentIsCod_QNAME, Boolean.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "IsExpressDelivery", scope = Shipment.class)
    public JAXBElement<Boolean> createShipmentIsExpressDelivery(Boolean value) {
        return new JAXBElement<Boolean>(_ShipmentIsExpressDelivery_QNAME, Boolean.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProduct }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Products", scope = Shipment.class)
    public JAXBElement<ArrayOfProduct> createShipmentProducts(ArrayOfProduct value) {
        return new JAXBElement<ArrayOfProduct>(_ShipmentProducts_QNAME, ArrayOfProduct.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Customer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Customer", scope = Shipment.class)
    public JAXBElement<Customer> createShipmentCustomer(Customer value) {
        return new JAXBElement<Customer>(_ShipmentCustomer_QNAME, Customer.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Address }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Address", scope = Shipment.class)
    public JAXBElement<Address> createShipmentAddress(Address value) {
        return new JAXBElement<Address>(_ShipmentAddress_QNAME, Address.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "OrderId", scope = Shipment.class)
    public JAXBElement<Integer> createShipmentOrderId(Integer value) {
        return new JAXBElement<Integer>(_ShipmentOrderId_QNAME, Integer.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "TrackingNumber", scope = Shipment.class)
    public JAXBElement<String> createShipmentTrackingNumber(String value) {
        return new JAXBElement<String>(_ShipmentTrackingNumber_QNAME, String.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "StoreId", scope = Shipment.class)
    public JAXBElement<Integer> createShipmentStoreId(Integer value) {
        return new JAXBElement<Integer>(_ShipmentStoreId_QNAME, Integer.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Carrier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "Carrier", scope = Shipment.class)
    public JAXBElement<Carrier> createShipmentCarrier(Carrier value) {
        return new JAXBElement<Carrier>(_ShipmentCarrier_QNAME, Carrier.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShipmentOrder }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ShipmentOrder", scope = Shipment.class)
    public JAXBElement<ShipmentOrder> createShipmentShipmentOrder(ShipmentOrder value) {
        return new JAXBElement<ShipmentOrder>(_ShipmentShipmentOrder_QNAME, ShipmentOrder.class, Shipment.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "City", scope = Address.class)
    public JAXBElement<String> createAddressCity(String value) {
        return new JAXBElement<String>(_AddressCity_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "ShippingAddress", scope = Address.class)
    public JAXBElement<String> createAddressShippingAddress(String value) {
        return new JAXBElement<String>(_AddressShippingAddress_QNAME, String.class, Address.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/eXtra.Services.Shipping.Dto", name = "District", scope = Address.class)
    public JAXBElement<String> createAddressDistrict(String value) {
        return new JAXBElement<String>(_AddressDistrict_QNAME, String.class, Address.class, value);
    }

}
