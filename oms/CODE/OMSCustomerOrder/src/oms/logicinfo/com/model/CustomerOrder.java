package oms.logicinfo.com.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
/**
 * <p>Java class for customerOrder complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="customerOrder">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entity_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="application_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="E-COMMERCE"/>
 *               &lt;enumeration value="SIEBEL_CRM"/>
 *               &lt;enumeration value="ORPOS"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="comments" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="240"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="order_requestor_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="12"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="request_datetimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="order_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="B2B"/>
 *               &lt;enumeration value="SAS"/>
 *               &lt;enumeration value="CO"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="order_create_reserve_ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="C"/>
 *               &lt;enumeration value="R"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_sub_order_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_phone_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_lang">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="3"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="delivery_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="S"/>
 *               &lt;enumeration value="C"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="first_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="last_name">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="120"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="consumer_delivery_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="consumer_delivery_time" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="pay_in_store_ind">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Y"/>
 *               &lt;enumeration value="N"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="pick_loc" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cart_number" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_order_address" type="{http://com.logicinfo.oms/model/}customerOrderAddress" minOccurs="0"/>
 *         &lt;element name="customer_order_items" type="{http://com.logicinfo.oms/model/}customerOrderItems" maxOccurs="100"/>
 *         &lt;element name="customer_order_tenders" type="{http://com.logicinfo.oms/model/}customerOrderTenders" maxOccurs="10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="customerOrder",
         propOrder={ "entityId","applicationId","comments","orderRequestorId","requestDatetimestamp","orderType",
                     "orderCreateReserveInd","customerOrderNo","customerSubOrderNo","customerId","customerPhoneNo",
                     "customerLang","deliveryType","firstName","lastName","consumerDeliveryDate",
                     "consumerDeliveryTime","payInStoreInd","createDate","pickLoc","cartNumber","customerOrderAddress",
                     "customerOrderItems","customerOrderTenders" })
public class CustomerOrder
{
@XmlElement(name="entity_id",required=true)
protected String entityId;
@XmlElement(name="application_id",required=true)
protected String applicationId;
protected String comments;
@XmlElement(name="order_requestor_id")
protected long orderRequestorId;
@XmlElement(name="request_datetimestamp",required=true)
@XmlSchemaType(name="dateTime")
protected XMLGregorianCalendar requestDatetimestamp;
@XmlElement(name="order_type",required=true)
protected String orderType;
@XmlElement(name="order_create_reserve_ind",required=true)
protected String orderCreateReserveInd;
@XmlElement(name="customer_order_no",required=true)
protected String customerOrderNo;
@XmlElement(name="customer_sub_order_no")
protected String customerSubOrderNo;
@XmlElement(name="customer_id",required=true)
protected String customerId;
@XmlElement(name="customer_phone_no",required=true)
protected String customerPhoneNo;
@XmlElement(name="customer_lang",required=true)
protected String customerLang;
@XmlElement(name="delivery_type",required=true)
protected String deliveryType;
@XmlElement(name="first_name",required=true)
protected String firstName;
@XmlElement(name="last_name",required=true)
protected String lastName;
@XmlElement(name="consumer_delivery_date",required=true)
@XmlSchemaType(name="date")
protected XMLGregorianCalendar consumerDeliveryDate;
@XmlElement(name="consumer_delivery_time",required=true)
@XmlSchemaType(name="time")
protected XMLGregorianCalendar consumerDeliveryTime;
@XmlElement(name="pay_in_store_ind",required=true)
protected String payInStoreInd;
@XmlElement(name="create_date",required=true)
@XmlSchemaType(name="dateTime")
protected XMLGregorianCalendar createDate;
@XmlElement(name="pick_loc")
protected Long pickLoc;
@XmlElement(name="cart_number")
protected String cartNumber;
@XmlElement(name="customer_order_address")
protected CustomerOrderAddress customerOrderAddress;
@XmlElement(name="customer_order_items",required=true)
protected List<CustomerOrderItems> customerOrderItems;
@XmlElement(name="customer_order_tenders")
protected List<CustomerOrderTenders> customerOrderTenders;

/**
 * Gets the value of the entityId property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getEntityId()
{
  return entityId;
}

/**
 * Sets the value of the entityId property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setEntityId(String value)
{
  this.entityId=value;
}

/**
 * Gets the value of the applicationId property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getApplicationId()
{
  return applicationId;
}

/**
 * Sets the value of the applicationId property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setApplicationId(String value)
{
  this.applicationId=value;
}

/**
 * Gets the value of the comments property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getComments()
{
  return comments;
}

/**
 * Sets the value of the comments property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setComments(String value)
{
  this.comments=value;
}

/**
 * Gets the value of the orderRequestorId property.
 *
 */
public long getOrderRequestorId()
{
  return orderRequestorId;
}

/**
 * Sets the value of the orderRequestorId property.
 *
 */
public void setOrderRequestorId(long value)
{
  this.orderRequestorId=value;
}

/**
 * Gets the value of the requestDatetimestamp property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getRequestDatetimestamp()
{
  return requestDatetimestamp;
}

/**
 * Sets the value of the requestDatetimestamp property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setRequestDatetimestamp(XMLGregorianCalendar value)
{
  this.requestDatetimestamp=value;
}

/**
 * Gets the value of the orderType property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getOrderType()
{
  return orderType;
}

/**
 * Sets the value of the orderType property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setOrderType(String value)
{
  this.orderType=value;
}

/**
 * Gets the value of the orderCreateReserveInd property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getOrderCreateReserveInd()
{
  return orderCreateReserveInd;
}

/**
 * Sets the value of the orderCreateReserveInd property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setOrderCreateReserveInd(String value)
{
  this.orderCreateReserveInd=value;
}

/**
 * Gets the value of the customerOrderNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCustomerOrderNo()
{
  return customerOrderNo;
}

/**
 * Sets the value of the customerOrderNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCustomerOrderNo(String value)
{
  this.customerOrderNo=value;
}

/**
 * Gets the value of the customerSubOrderNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCustomerSubOrderNo()
{
  return customerSubOrderNo;
}

/**
 * Sets the value of the customerSubOrderNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCustomerSubOrderNo(String value)
{
  this.customerSubOrderNo=value;
}

/**
 * Gets the value of the customerId property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCustomerId()
{
  return customerId;
}

/**
 * Sets the value of the customerId property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCustomerId(String value)
{
  this.customerId=value;
}

/**
 * Gets the value of the customerPhoneNo property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCustomerPhoneNo()
{
  return customerPhoneNo;
}

/**
 * Sets the value of the customerPhoneNo property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCustomerPhoneNo(String value)
{
  this.customerPhoneNo=value;
}

/**
 * Gets the value of the customerLang property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCustomerLang()
{
  return customerLang;
}

/**
 * Sets the value of the customerLang property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCustomerLang(String value)
{
  this.customerLang=value;
}

/**
 * Gets the value of the deliveryType property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getDeliveryType()
{
  return deliveryType;
}

/**
 * Sets the value of the deliveryType property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setDeliveryType(String value)
{
  this.deliveryType=value;
}

/**
 * Gets the value of the firstName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getFirstName()
{
  return firstName;
}

/**
 * Sets the value of the firstName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setFirstName(String value)
{
  this.firstName=value;
}

/**
 * Gets the value of the lastName property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getLastName()
{
  return lastName;
}

/**
 * Sets the value of the lastName property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setLastName(String value)
{
  this.lastName=value;
}

/**
 * Gets the value of the consumerDeliveryDate property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getConsumerDeliveryDate()
{
  return consumerDeliveryDate;
}

/**
 * Sets the value of the consumerDeliveryDate property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setConsumerDeliveryDate(XMLGregorianCalendar value)
{
  this.consumerDeliveryDate=value;
}

/**
 * Gets the value of the consumerDeliveryTime property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getConsumerDeliveryTime()
{
  return consumerDeliveryTime;
}

/**
 * Sets the value of the consumerDeliveryTime property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setConsumerDeliveryTime(XMLGregorianCalendar value)
{
  this.consumerDeliveryTime=value;
}

/**
 * Gets the value of the payInStoreInd property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getPayInStoreInd()
{
  return payInStoreInd;
}

/**
 * Sets the value of the payInStoreInd property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setPayInStoreInd(String value)
{
  this.payInStoreInd=value;
}

/**
 * Gets the value of the createDate property.
 *
 * @return
 *     possible object is
 *     {@link XMLGregorianCalendar }
 *
 */
public XMLGregorianCalendar getCreateDate()
{
  return createDate;
}

/**
 * Sets the value of the createDate property.
 *
 * @param value
 *     allowed object is
 *     {@link XMLGregorianCalendar }
 *
 */
public void setCreateDate(XMLGregorianCalendar value)
{
  this.createDate=value;
}

/**
 * Gets the value of the pickLoc property.
 *
 * @return
 *     possible object is
 *     {@link Long }
 *
 */
public Long getPickLoc()
{
  return pickLoc;
}

/**
 * Sets the value of the pickLoc property.
 *
 * @param value
 *     allowed object is
 *     {@link Long }
 *
 */
public void setPickLoc(Long value)
{
  this.pickLoc=value;
}

/**
 * Gets the value of the cartNumber property.
 *
 * @return
 *     possible object is
 *     {@link String }
 *
 */
public String getCartNumber()
{
  return cartNumber;
}

/**
 * Sets the value of the cartNumber property.
 *
 * @param value
 *     allowed object is
 *     {@link String }
 *
 */
public void setCartNumber(String value)
{
  this.cartNumber=value;
}

/**
 * Gets the value of the customerOrderAddress property.
 *
 * @return
 *     possible object is
 *     {@link CustomerOrderAddress }
 *
 */
public CustomerOrderAddress getCustomerOrderAddress()
{
  return customerOrderAddress;
}

/**
 * Sets the value of the customerOrderAddress property.
 *
 * @param value
 *     allowed object is
 *     {@link CustomerOrderAddress }
 *
 */
public void setCustomerOrderAddress(CustomerOrderAddress value)
{
  this.customerOrderAddress=value;
}

/**
 * Gets the value of the customerOrderItems property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the customerOrderItems property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustomerOrderItems().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustomerOrderItems }
 *
 *
 */
public List<CustomerOrderItems> getCustomerOrderItems()
{
  if(customerOrderItems==null)
  {
    customerOrderItems=new ArrayList<CustomerOrderItems>();
  }
  return this.customerOrderItems;
}

/**
 * Gets the value of the customerOrderTenders property.
 *
 * <p>
 * This accessor method returns a reference to the live list,
 * not a snapshot. Therefore any modification you make to the
 * returned list will be present inside the JAXB object.
 * This is why there is not a <CODE>set</CODE> method for the customerOrderTenders property.
 *
 * <p>
 * For example, to add a new item, do as follows:
 * <pre>
 *    getCustomerOrderTenders().add(newItem);
 * </pre>
 *
 *
 * <p>
 * Objects of the following type(s) are allowed in the list
 * {@link CustomerOrderTenders }
 *
 *
 */
public List<CustomerOrderTenders> getCustomerOrderTenders()
{
  if(customerOrderTenders==null)
  {
    customerOrderTenders=new ArrayList<CustomerOrderTenders>();
  }
  return this.customerOrderTenders;
}
}
