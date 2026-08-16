
package com.logicinfo.oms.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for customerOrder complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
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
 *               &lt;maxLength value="30"/>
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
 *         &lt;element name="customer_order_no">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_sub_order_no" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *               &lt;minExclusive value="0"/>
 *               &lt;totalDigits value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="customer_id">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
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
 *         &lt;element name="service_request_type">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="VAS_ESP"/>
 *               &lt;enumeration value="VAS_PCEW"/>
 *               &lt;enumeration value="VAS_DAMM"/>
 *               &lt;enumeration value="ASC_BRAND"/>
 *               &lt;enumeration value="NO_WARRANTY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="consumer_delivery_date" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="consumer_delivery_time" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="payment_status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PAID"/>
 *               &lt;enumeration value="PENDING"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="create_date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="pick_loc" type="{http://www.w3.org/2001/XMLSchema}unsignedInt"/>
 *         &lt;element name="customer_order_address" type="{http://com.logicinfo.oms/model/}customerOrderAddress" minOccurs="0"/>
 *         &lt;element name="customer_order_items" type="{http://com.logicinfo.oms/model/}customerOrderItems" maxOccurs="100"/>
 *         &lt;element name="customer_order_tenders" type="{http://com.logicinfo.oms/model/}customerOrderTenders" maxOccurs="10"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrder", propOrder = { "entityId", "applicationId", "comments", "orderRequestorId",
		"requestDatetimestamp", "orderType", "orderCreateReserveInd", "customerOrderNo", "customerSubOrderNo",
		"customerId", "customerPhoneNo", "customerLang", "deliveryType", "firstName", "lastName",
		"consumerDeliveryDate", "consumerDeliveryTime", "payInStoreInd", "createDate", "pickLoc", "cartNumber",
		"contractFlag", "source", "consumerDeliverySlot", "deliveryModeType", "orderCategory", "deliveryAuthCode", "addrVerifiedInd", "omsReference", "omsMedium", "ordType", "lockerId", "customerOrderAddress",
		"customerOrderItems", "customerOrderTenders" })
@XmlRootElement
public class CustomerOrder {

	@XmlElement(name = "entity_id", required = true)
	protected String entityId;
	@XmlElement(name = "application_id", required = true)
	protected String applicationId;
	protected String comments;
	@XmlElement(name = "request_datetimestamp", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar requestDatetimestamp;
	@XmlElement(name = "order_type", required = true)
	protected String orderType;
	@XmlElement(name = "customer_order_no", required = true)
	protected String customerOrderNo;
	@XmlElement(name = "customer_sub_order_no")
	protected String customerSubOrderNo;
	@XmlElement(name = "customer_id", required = true)
	protected String customerId;
	@XmlElement(name = "delivery_type", required = true)
	protected String deliveryType;
	@XmlElement(name = "consumer_delivery_date", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar consumerDeliveryDate;
	@XmlElement(name = "consumer_delivery_time", required = true)
	@XmlSchemaType(name = "time")
	protected XMLGregorianCalendar consumerDeliveryTime;
	@XmlElement(name = "create_date", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar createDate;
	@XmlElement(name = "pick_loc")
	@XmlSchemaType(name = "unsignedInt")
	protected Long pickLoc;
	@XmlElement(name = "customer_order_address")
	protected CustomerOrderAddress customerOrderAddress;
	@XmlElement(name = "customer_order_items", required = true)
	protected List<CustomerOrderItems> customerOrderItems;
	@XmlElement(name = "customer_order_tenders")
	protected List<CustomerOrderTenders> customerOrderTenders;
	@XmlElement(name = "order_requestor_id")
	protected long orderRequestorId;
	@XmlElement(name = "customer_lang", required = true)
	protected String customerLang;
	@XmlElement(name = "customer_phone_no", required = true)
	protected String customerPhoneNo;
	@XmlElement(name = "order_create_reserve_ind", required = true)
	protected String orderCreateReserveInd;
	@XmlElement(name = "pay_in_store_ind", required = true)
	protected String payInStoreInd;
	@XmlElement(name = "first_name", required = true)
	protected String firstName;
	@XmlElement(name = "last_name", required = true)
	protected String lastName;
	@XmlElement(name = "cart_number")
	protected String cartNumber;
	@XmlElement(name = "contract_flag")
	protected String contractFlag;
	@XmlElement(name = "ord_src")
	protected String source;
	@XmlElement(name = "consumer_delivery_slot")
	protected String consumerDeliverySlot;
	@XmlElement(name = "delivery_mode_type")
	protected String deliveryModeType;
	@XmlElement(name = "order_category")
	protected String orderCategory;
	@XmlElement(name = "delivery_auth_code")
	protected Long deliveryAuthCode;
	@XmlElement(name = "addr_verified_ind")
	protected String addrVerifiedInd;
	@XmlElement(name = "oms_reference")
	protected String omsReference;
	@XmlElement(name = "oms_medium")
	protected String omsMedium;
	@XmlElement(name = "ord_type")
	protected String ordType;
	@XmlElement(name = "locker_id")
	protected String lockerId;
	/**
	 * Gets the value of the entityId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Sets the value of the entityId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setEntityId(String value) {
		this.entityId = value;
	}

	/**
	 * Gets the value of the applicationId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Sets the value of the applicationId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setApplicationId(String value) {
		this.applicationId = value;
	}

	/**
	 * Gets the value of the comments property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * Sets the value of the comments property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setComments(String value) {
		this.comments = value;
	}

	/**
	 * Gets the value of the requestDatetimestamp property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getRequestDatetimestamp() {
		return requestDatetimestamp;
	}

	/**
	 * Sets the value of the requestDatetimestamp property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setRequestDatetimestamp(XMLGregorianCalendar value) {
		this.requestDatetimestamp = value;
	}

	/**
	 * Gets the value of the orderType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * Sets the value of the orderType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setOrderType(String value) {
		this.orderType = value;
	}

	/**
	 * Gets the value of the customerOrderNo property.
	 *
	 */
	public String getCustomerOrderNo() {
		return customerOrderNo;
	}

	/**
	 * Sets the value of the customerOrderNo property.
	 * 
	 */
	public void setCustomerOrderNo(String value) {
		this.customerOrderNo = value;
	}

	/**
	 * Gets the value of the customerSubOrderNo property.
	 *
	 * @return possible object is {@link Long }
	 *
	 */
	public String getCustomerSubOrderNo() {
		return customerSubOrderNo;
	}

	/**
	 * Sets the value of the customerSubOrderNo property.
	 * 
	 * @param value allowed object is {@link Long }
	 * 
	 */
	public void setCustomerSubOrderNo(String value) {
		this.customerSubOrderNo = value;
	}

	/**
	 * Gets the value of the customerId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the value of the customerId property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setCustomerId(String value) {
		this.customerId = value;
	}

	/**
	 * Gets the value of the deliveryType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeliveryType() {
		return deliveryType;
	}

	/**
	 * Sets the value of the deliveryType property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDeliveryType(String value) {
		this.deliveryType = value;
	}

	/**
	 * Gets the value of the consumerDeliveryDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getConsumerDeliveryDate() {
		return consumerDeliveryDate;
	}

	/**
	 * Sets the value of the consumerDeliveryDate property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setConsumerDeliveryDate(XMLGregorianCalendar value) {
		this.consumerDeliveryDate = value;
	}

	/**
	 * Gets the value of the consumerDeliveryTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getConsumerDeliveryTime() {
		return consumerDeliveryTime;
	}

	/**
	 * Sets the value of the consumerDeliveryTime property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setConsumerDeliveryTime(XMLGregorianCalendar value) {
		this.consumerDeliveryTime = value;
	}

	/**
	 * Gets the value of the createDate property.
	 *
	 * @return possible object is {@link XMLGregorianCalendar }
	 *
	 */
	public XMLGregorianCalendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the value of the createDate property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCreateDate(XMLGregorianCalendar value) {
		this.createDate = value;
	}

	/**
	 * Gets the value of the pickLoc property.
	 *
	 */
	public Long getPickLoc() {
		return pickLoc;
	}

	/**
	 * Sets the value of the pickLoc property.
	 * 
	 */
	public void setPickLoc(Long value) {
		this.pickLoc = value;
	}

	/**
	 * Gets the value of the customerOrderAddress property.
	 *
	 * @return possible object is {@link CustomerOrderAddress}
	 *
	 */
	public CustomerOrderAddress getCustomerOrderAddress() {
		return customerOrderAddress;
	}

	/**
	 * Sets the value of the customerOrderAddress property.
	 *
	 * @param value allowed object is {@link CustomerOrderAddress}
	 *
	 */
	public void setCustomerOrderAddress(CustomerOrderAddress value) {
		this.customerOrderAddress = value;
	}

	/**
	 * Gets the value of the customerOrderItems property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the customerOrderItems property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCustomerOrderItems().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CustomerOrderItems}
	 *
	 *
	 */
	public List<CustomerOrderItems> getCustomerOrderItems() {
		if (customerOrderItems == null) {
			customerOrderItems = new ArrayList<CustomerOrderItems>();
		}
		return this.customerOrderItems;
	}

	/**
	 * Gets the value of the customerOrderTenders property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the customerOrderTenders property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCustomerOrderTenders().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link CustomerOrderTenders}
	 *
	 *
	 */
	public List<CustomerOrderTenders> getCustomerOrderTenders() {
		if (customerOrderTenders == null) {
			customerOrderTenders = new ArrayList<CustomerOrderTenders>();
		}
		return this.customerOrderTenders;
	}

	/**
	 * Gets the value of the orderRequestorId property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public long getOrderRequestorId() {
		return orderRequestorId;
	}

	/**
	 * Sets the value of the orderRequestorId property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setOrderRequestorId(long value) {
		this.orderRequestorId = value;
	}

	/**
	 * Gets the value of the customerLang property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCustomerLang() {
		return customerLang;
	}

	/**
	 * Gets the value of the customerPhoneNo property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCustomerPhoneNo() {
		return customerPhoneNo;
	}

	/**
	 * Gets the value of the orderCreateReserveInd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getOrderCreateReserveInd() {
		return orderCreateReserveInd;
	}

	/**
	 * Gets the value of the payInStoreInd property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getPayInStoreInd() {
		return payInStoreInd;
	}

	/**
	 * Sets the value of the customerLang property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCustomerLang(String value) {
		this.customerLang = value;
	}

	/**
	 * Sets the value of the customerPhoneNo property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCustomerPhoneNo(String value) {
		this.customerPhoneNo = value;
	}

	/**
	 * Sets the value of the orderCreateReserveInd property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setOrderCreateReserveInd(String value) {
		this.orderCreateReserveInd = value;
	}

	/**
	 * Sets the value of the payInStoreInd property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setPayInStoreInd(String value) {
		this.payInStoreInd = value;
	}

	/**
	 * Gets the value of the firstName property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Gets the value of the lastName property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the value of the firstName property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setFirstName(String value) {
		this.firstName = value;
	}

	/**
	 * Sets the value of the lastName property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setLastName(String value) {
		this.lastName = value;
	}

	/**
	 * Gets the value of the cartNumber property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getCartNumber() {
		return cartNumber;
	}

	/**
	 * Sets the value of the cartNumber property.
	 *
	 * @param value allowed object is {@link String }
	 *
	 */
	public void setCartNumber(String value) {
		this.cartNumber = value;
	}

	public String getContractFlag() {
		return contractFlag;
	}

	public void setContractFlag(String contractFlag) {
		this.contractFlag = contractFlag;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getConsumerDeliverySlot() {
		return consumerDeliverySlot;
	}

	public void setConsumerDeliverySlot(String consumerDeliverySlot) {
		this.consumerDeliverySlot = consumerDeliverySlot;
	}

	public String getDeliveryModeType() {
		return deliveryModeType;
	}

	public void setDeliveryModeType(String deliveryModeType) {
		this.deliveryModeType = deliveryModeType;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public Long getDeliveryAuthCode() {
		return deliveryAuthCode;
	}

	public void setDeliveryAuthCode(Long deliveryAuthCode) {
		this.deliveryAuthCode = deliveryAuthCode;
	}

	public String getAddrVerifiedInd() {
		return addrVerifiedInd;
	}

	public void setAddrVerifiedInd(String addrVerifiedInd) {
		this.addrVerifiedInd = addrVerifiedInd;
	}

	public String getOmsReference() {
		return omsReference;
	}

	public void setOmsReference(String omsReference) {
		this.omsReference = omsReference;
	}

	public String getOmsMedium() {
		return omsMedium;
	}

	public void setOmsMedium(String omsMedium) {
		this.omsMedium = omsMedium;
	}

	public String getOrdType() {
		return ordType;
	}

	public void setOrdType(String ordType) {
		this.ordType = ordType;
	}

	public String getLockerId() {
		return lockerId;
	}

	public void setLockerId(String lockerId) {
		this.lockerId = lockerId;
	}

	

}
