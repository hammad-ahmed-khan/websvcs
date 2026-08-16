package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "OmsCustOrdHead.findAll", query = "select o from OmsCustOrdHead o"),
		@NamedQuery(name = "OmsCustOrdHead.findDuplicate", query = "select o.omsCustOrdNo from OmsCustOrdHead o where o.applicationId=:appId and o.custOrderNo=:custOrdNumb and o.status=:status"),
		@NamedQuery(name = "OmsCustOrdHead.findPaymentStatus", query = "select o.payInStore from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdHead.findByOmsCustOrdNo", query = "select o from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdHead.findByExternalCustOrdNo", query = "select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo and o.applicationId=:applicationId and o.status='S'"),
		@NamedQuery(name = "OmsCustOrdHead.findByExternalCustOrdNoByFailedStatus", query = "select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo and o.applicationId=:applicationId and o.status='F'"),
		@NamedQuery(name = "OmsCustOrdHead.findByStatus", query = "select o.omsCustOrdNo from OmsCustOrdHead o where o.applicationId=:appId and o.custOrderNo=:custOrdNumb and o.subCustOrderNo=:subCustOrderNo and o.status IN ('A','S') "),
		@NamedQuery(name = "OmsCustOrdHead.findLanguage", query = "select o.customerLang from OmsCustOrdHead o where  o.omsCustOrdNo=:omsCustOrdNo"),
		@NamedQuery(name = "OmsCustOrdHead.findByCustId", query = "select o.omsCustOrdNo from OmsCustOrdHead o where  o.custId=:custId and o.status='S'"),
		@NamedQuery(name = "OmsCustOrdHead.findColumns", query = "select o from OmsCustOrdHead o where o.custOrderNo=:custOrderNo and o.status='S'"),
		@NamedQuery(name = "OmsCustOrdHead.findOmsCustOrdNo", query = "select o.omsCustOrdNo from OmsCustOrdHead o where o.custOrderNo=:custOrderNo and o.status='S'"),
		@NamedQuery(name = "OmsCustOrdHead.findByCustOrdNoAndSubCustOrdNo", query = "select o.omsCustOrdNo from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.subCustOrderNo=:subCustOrderNo and o.status='S'"),
		@NamedQuery(name = "OmsCustOrdHead.findByCustOrdNoAndStatus", query = "select o from OmsCustOrdHead o where  o.custOrderNo=:custOrderNo and o.status=:status")

})
@Table(name = "OMS_CUST_ORD_HEAD")
public class OmsCustOrdHead implements Serializable {

	private static final long serialVersionUID = 4460718134818704591L;

	@Column(name = "APPLICATION_ID", nullable = false, length = 30)
	private String applicationId;
	@Column(name = "CANCEL_DATETIME")
	private Timestamp cancelDatetime;
	@Column(name = "CLOSE_DATETIME")
	private Timestamp closeDatetime;
	@Column(length = 2000)
	private String comments;
	@Column(name = "CONSUMER_DLY_TIME")
	private Timestamp consumerDlyTime;
	@Column(name = "CREATE_DATETIME", nullable = false)
	private Timestamp createDatetime;
	@Column(name = "CUST_FIRST_NAME", nullable = false, length = 120)
	private String custFirstName;
	@Column(name = "CUST_ID", nullable = false, length = 14)
	private String custId;
	@Column(name = "CUST_LAST_NAME", nullable = false, length = 120)
	private String custLastName;
	@Column(name = "CUST_ORDER_NO", nullable = false, length = 48)
	private String custOrderNo;
	@Column(name = "CUST_ORDER_TYPE", nullable = false, length = 3)
	private String custOrderType;
	@Column(name = "CUST_PHONE_NO", nullable = false, length = 15)
	private String custPhoneNo;
	@Column(name = "CUSTOMER_LANG", nullable = false, length = 3)
	private String customerLang;
	@Column(name = "DELIVERY_TYPE", nullable = false, length = 1)
	private String deliveryType;
	@Column(name = "ENTITY_ID", nullable = false, length = 30)
	private String entityId;
	@Column(name = "LAST_UPDATE_DATETIME")
	private Timestamp lastUpdateDatetime;
	@Id
	@Column(name = "OMS_CUST_ORD_NO", nullable = false)
	@SequenceGenerator(name = "omsCustOrdNoSeq", sequenceName = "OMS_CUST_ORD_NO_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "omsCustOrdNoSeq")
	private BigDecimal omsCustOrdNo;
	@Column(name = "ORD_PAYMENT_STATUS", nullable = false, length = 1)
	private String ordPaymentStatus;
	@Column(name = "ORDER_CREATE_RESERVE_IND", length = 1)
	private String orderCreateReserveInd;
	@Column(name = "ORDER_REQUESTOR_ID", nullable = false)
	private BigDecimal orderRequestorId;
	@Column(name = "PAY_IN_STORE", nullable = false, length = 1)
	private String payInStore;
	@Column(name = "PICK_LOC")
	private BigDecimal pickLoc;
	@Column(length = 1)
	private String status;
	@Column(name = "SUB_CUST_ORDER_NO", nullable = false, length = 3)
	private String subCustOrderNo;
	@Column(name = "CART_NUMBER", nullable = true)
	private String cartNumber;
	@Column(name = "ORD_SRC", nullable = true)
	private String source;
	@Column(name = "DELIVERY_SLOT")
	protected String consumerDeliverySlot;
	@Column(name = "DELIVERY_MODE")
	protected String deliveryMode;
	@Column(name = "ORDER_CATEGORY")
	protected String orderCategory;
	@Column(name = "DELV_AUTH_CODE")
	protected BigDecimal deliveryAuthCode;
	@Column(name = "ADDR_VERIFIED_IND")
	protected String addrVerifiedInd;
	@Column(name = "OMS_REFERENCE")
	protected String omsReference;
	@Column(name = "OMS_MEDIUM")
	protected String omsMedium;
	@Column(name = "ORD_TYPE")
	protected String ordType;
	@Column(name = "LOCKER_ID")
	protected String lockerId;
	@Column(name = "SHIP_TO_STORE")
	protected String shipToStore;

	public OmsCustOrdHead() {
	}

	public OmsCustOrdHead(String applicationId, Timestamp cancelDatetime, Timestamp closeDatetime, String comments, Timestamp consumerDlyTime, Timestamp createDatetime, String custFirstName,
			String custId, String custLastName, String custOrderNo, String custOrderType, String custPhoneNo, String customerLang, String deliveryType, String entityId, Timestamp lastUpdateDatetime,
			BigDecimal omsCustOrdNo, String ordPaymentStatus, String orderCreateReserveInd, BigDecimal orderRequestorId, String payInStore, BigDecimal pickLoc, String status, String subCustOrderNo) {
		this.applicationId = applicationId;
		this.cancelDatetime = cancelDatetime;
		this.closeDatetime = closeDatetime;
		this.comments = comments;
		this.consumerDlyTime = consumerDlyTime;
		this.createDatetime = createDatetime;
		this.custFirstName = custFirstName;
		this.custId = custId;
		this.custLastName = custLastName;
		this.custOrderNo = custOrderNo;
		this.custOrderType = custOrderType;
		this.custPhoneNo = custPhoneNo;
		this.customerLang = customerLang;
		this.deliveryType = deliveryType;
		this.entityId = entityId;
		this.lastUpdateDatetime = lastUpdateDatetime;
		this.omsCustOrdNo = omsCustOrdNo;
		this.ordPaymentStatus = ordPaymentStatus;
		this.orderCreateReserveInd = orderCreateReserveInd;
		this.orderRequestorId = orderRequestorId;
		this.payInStore = payInStore;
		this.pickLoc = pickLoc;
		this.status = status;
		this.subCustOrderNo = subCustOrderNo;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public Timestamp getCancelDatetime() {
		return cancelDatetime;
	}

	public void setCancelDatetime(Timestamp cancelDatetime) {
		this.cancelDatetime = cancelDatetime;
	}

	public Timestamp getCloseDatetime() {
		return closeDatetime;
	}

	public void setCloseDatetime(Timestamp closeDatetime) {
		this.closeDatetime = closeDatetime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getConsumerDlyTime() {
		return consumerDlyTime;
	}

	public void setConsumerDlyTime(Timestamp consumerDlyTime) {
		this.consumerDlyTime = consumerDlyTime;
	}

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}

	public String getCustOrderNo() {
		return custOrderNo;
	}

	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}

	public String getCustOrderType() {
		return custOrderType;
	}

	public void setCustOrderType(String custOrderType) {
		this.custOrderType = custOrderType;
	}

	public String getCustPhoneNo() {
		return custPhoneNo;
	}

	public void setCustPhoneNo(String custPhoneNo) {
		this.custPhoneNo = custPhoneNo;
	}

	public String getCustomerLang() {
		return customerLang;
	}

	public void setCustomerLang(String customerLang) {
		this.customerLang = customerLang;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Timestamp getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	public BigDecimal getOmsCustOrdNo() {
		return omsCustOrdNo;
	}

	public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
		this.omsCustOrdNo = omsCustOrdNo;
	}

	public String getOrdPaymentStatus() {
		return ordPaymentStatus;
	}

	public void setOrdPaymentStatus(String ordPaymentStatus) {
		this.ordPaymentStatus = ordPaymentStatus;
	}

	public String getOrderCreateReserveInd() {
		return orderCreateReserveInd;
	}

	public void setOrderCreateReserveInd(String orderCreateReserveInd) {
		this.orderCreateReserveInd = orderCreateReserveInd;
	}

	public BigDecimal getOrderRequestorId() {
		return orderRequestorId;
	}

	public void setOrderRequestorId(BigDecimal orderRequestorId) {
		this.orderRequestorId = orderRequestorId;
	}

	public String getPayInStore() {
		return payInStore;
	}

	public void setPayInStore(String payInStore) {
		this.payInStore = payInStore;
	}

	public BigDecimal getPickLoc() {
		return pickLoc;
	}

	public void setPickLoc(BigDecimal pickLoc) {
		this.pickLoc = pickLoc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubCustOrderNo() {
		return subCustOrderNo;
	}

	public void setSubCustOrderNo(String subCustOrderNo) {
		this.subCustOrderNo = subCustOrderNo;
	}

	public String getCartNumber() {
		return cartNumber;
	}

	public void setCartNumber(String cartNumber) {
		this.cartNumber = cartNumber;
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

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public BigDecimal getDeliveryAuthCode() {
		return deliveryAuthCode;
	}

	public void setDeliveryAuthCode(BigDecimal deliveryAuthCode) {
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

	public String getShipToStore() {
		return shipToStore;
	}

	public void setShipToStore(String shipToStore) {
		this.shipToStore = shipToStore;
	}

}
