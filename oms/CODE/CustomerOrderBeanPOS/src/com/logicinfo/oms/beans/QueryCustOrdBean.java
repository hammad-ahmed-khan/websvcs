package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

//import com.oracle.retail.integration.base.bo.paymentdesc.v1.EntryMethod;

import com.logicinfo.oms.ejb.Addr;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.util.OMSUtil;
import com.oracle.retail.integration.base.bo.contactdesc.v1.ContactDesc;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerDesc;
import com.oracle.retail.integration.base.bo.customerdesc.v1.CustomerType;
import com.oracle.retail.integration.base.bo.custordercoldesc.v1.CustOrderColDesc;
import com.oracle.retail.integration.base.bo.custordercrivo.v1.CustOrderCriVo;
import com.oracle.retail.integration.base.bo.custordercrivo.v1.OrderCriteria;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.GiftReceiptAssigned;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.InitiateLocType;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.OrderStatus;
import com.oracle.retail.integration.base.bo.localedesc.v1.LocaleDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException;

//import org.apache.log.Logger;

public class QueryCustOrdBean {
	public QueryCustOrdBean() {
		super();
	}

	BigDecimal omsOrposCustOrderId, omsCustOrdNo;
	String custId;
	String firstName;
	String lastName;
	String phoneNo;
	BigDecimal grandTotal = BigDecimal.ZERO;
	/*
	 * custOrderCriVo
	 * 
	 */
	private final static Logger log = Logger.getLogger(QueryCustOrdBean.class.getName());

	public CustOrderColDesc queryCustomerOrder(CustOrderCriVo custOrderCriVo)
			throws SOAPException, IllegalArgumentWSFaultException, IllegalArgumentWSFaultException,
			IllegalStateWSFaultException, ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException {
		log.info("started queryCustomerOrder");
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		OrderCriteria orderCriteria = custOrderCriVo.getOrderCriteria();
		log.info("Before checking SearchType");
		String searchType = orderCriteria.getSearchType().value();
		log.info("after search type:" + searchType);
		if (searchType.equals("ORDERID")) {
			log.info("inside search type of ORDERID");
			// omsOrposCustOrderId=session.getOmsOrposCustOrderHeadFindOmsOrposCustOrdId(custOrderCriVo.getOrderCriteria().getCustomerOrderId());
			// OmsOrposCustOrderHead omsOrposCustOrderHead=
			// session.getOmsOrposCustOrderHeadDetailsByOrderId((custOrderCriVo.getOrderCriteria().getCustomerOrderId()));
			ResponseProcessing responseProcessing = new ResponseProcessing();
			return responseProcessing.createResponse(custOrderCriVo);
		} else if (searchType.equals("CUSTOMER")) {
			if (custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerId() != null) {
				custId = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerId();
				return createResponseByCustomerId(custOrderCriVo, custId);
			} else {
				if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName()
								.equals("?")))) {
					firstName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName();
					return createResponseByCustomerFirstName(custOrderCriVo, firstName);
				}

				else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName()
								.equals("?")))) {
					lastName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName();
					return createResponseByCustomerLastName(custOrderCriVo, lastName);
				} else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber()
								.equals("?")))) {
					phoneNo = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber();
					return createResponseByCustomerPhoneNo(custOrderCriVo, phoneNo);
				} else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName() != ""
						&& custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName()
								.equals("?")))
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName()
								.equals("?")))) {
					firstName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName();
					lastName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName();
					return createResponseByCustomerFirstAndLastName(custOrderCriVo, firstName, lastName);
				} else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName() != ""
						&& custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName()
								.equals("?")))
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber()
								.equals("?")))) {
					firstName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName();
					phoneNo = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber();
					return createResponseByCustomerFirstNameAndPhoneNo(custOrderCriVo, firstName, phoneNo);
				} else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName() != ""
						&& custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName()
								.equals("?")))
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber()
								.equals("?")))) {
					firstName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName();
					phoneNo = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber();
					return createResponseByCustomerLastNameAndPhoneNo(custOrderCriVo, lastName, phoneNo);
				} else if ((custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName() != ""
						&& custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName() != ""
						&& custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber() != "")
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getFirstName()
								.equals("?")))
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName()
								.equals("?")))
						&& (!(custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber()
								.equals("?")))) {
					firstName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName();
					lastName = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getLastName();
					phoneNo = custOrderCriVo.getOrderCriteria().getCustomerCri().getCustomerInfo().getPhoneNumber();
					return createResponseByCustomerFirstLastNameAndPhoneNo(custOrderCriVo, firstName, lastName,
							phoneNo);
				}
			}

		} else if (searchType.equals("CHARGECARD")) {
			log.info("inside searchType of Chargecard");
			String tenderRefId = custOrderCriVo.getOrderCriteria().getCreditDebitCri().getCardToken();
			log.info("tenderRefId:" + tenderRefId);
			String item = custOrderCriVo.getOrderCriteria().getCreditDebitCri().getItemId();
			log.info("item:" + item);
			XMLGregorianCalendar startDate = custOrderCriVo.getOrderCriteria().getCreditDebitCri().getDateRange()
					.getStartDate();
			log.info("startDate:" + startDate);
			XMLGregorianCalendar endDate = custOrderCriVo.getOrderCriteria().getCreditDebitCri().getDateRange()
					.getEndDate();
			log.info("endDate:" + endDate);
			// CONVERT STARTDATE AND ENDDATE INTO TIMESTAMP
			Timestamp startTime = new Timestamp(toDate(startDate).getTime());
			log.info("startTime:" + startTime);
			Timestamp endTime = new Timestamp(toDate(endDate).getTime());
			log.info("endTime:" + endTime);
			return createResponseByCardToken(custOrderCriVo, tenderRefId, item, startTime, endTime);
		}
		return custOrderColDesc;

	}

	public CustOrderColDesc createResponseByCustomerId(CustOrderCriVo input, String custId) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCustomerId()");
		List<BigDecimal> omsCustOrdNoList = (List<BigDecimal>) session.getOmsCustOrdHeadFindByCustId(custId);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerFirstName(CustOrderCriVo input, String firstName)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCustomerInfo()");
		log.info("first Name" + firstName);
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverFirstName(firstName);
		log.info("omsCustOrdNoList: " + omsCustOrdNoList);
		// setResponses(orderStatus, sourceLocList, omsCustOrdItemList,
		// custOrderDescList, custOrderDesc, omsCustOrdHead, omsCustOrdNoList);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;

	}

	public CustOrderColDesc createResponseByCustomerLastName(CustOrderCriVo input, String lastName)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCustomerLastName()");
		log.info("last Name" + lastName);
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverLastName(lastName);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerPhoneNo(CustOrderCriVo input, String phoneNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCustomerPhoneNo()");
		log.info("Phone No" + phoneNo);
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverPhoneNo(phoneNo);
		log.info("omsCustOrdNoList:" + omsCustOrdNoList);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerFirstAndLastName(CustOrderCriVo input, String firstName,
			String lastName) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCustomerFirstAndLastName()");
		log.info("first Name" + firstName);
		log.info("last Name" + lastName);
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverFirstNameAndLastName(firstName,
				lastName);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerFirstNameAndPhoneNo(CustOrderCriVo input, String firstName,
			String phoneNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverFirstNameAndPhoneNo(firstName,
				phoneNo);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerLastNameAndPhoneNo(CustOrderCriVo input, String lastName,
			String phoneNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdAddressFindByDeliverLastNameAndPhoneNo(lastName,
				phoneNo);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCustomerFirstLastNameAndPhoneNo(CustOrderCriVo input, String firstName,
			String lastName, String phoneNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		List<BigDecimal> omsCustOrdNoList = session
				.getOmsCustOrdAddressFindByDeliverFirstNameLastNameAndPhoneNo(firstName, lastName, phoneNo);
		setResponses(omsCustOrdNoList, custOrderDescList);
		return custOrderColDesc;
	}

	public CustOrderColDesc createResponseByCardToken(CustOrderCriVo input, String tenderRefId, String item,
			Timestamp startTime, Timestamp endTime) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		CustOrderColDesc custOrderColDesc = new CustOrderColDesc();
		custOrderColDesc.setCollectionSize(4);
		List<CustOrderDesc> custOrderDescList = custOrderColDesc.getCustOrderDesc();
		log.info("inside createResponseByCardToken()");
		log.info("tenderRefId" + tenderRefId);
		List<BigDecimal> omsCustOrdNoList1 = session.getOmsCustOrdTenderFindByTenderRefId(tenderRefId);
		log.info("after getOmsCustOrdTenderFindByTenderRefId()" + omsCustOrdNoList1);
		// OmsCustOrdItem omsCustOrdItem;
		for (BigDecimal omsCustOrdNo1 : omsCustOrdNoList1) {
			log.info("inside omsCustOrdNoList for loop");
			log.info("item:" + item);
			log.info("startTime:" + startTime);
			log.info("endTime:" + endTime);
			List<BigDecimal> omsCustOrdNoList = session.getOmsCustOrdItemFindByItemAndLastUpdatedDateTime(item,
					startTime, endTime, omsCustOrdNo1);
			log.info("after getOmsCustOrdItemFindByItemAndLastUpdatedDateTime()" + omsCustOrdNoList1);
			setResponses(omsCustOrdNoList, custOrderDescList);
		}
		return custOrderColDesc;
	}

	public void setResponses(List<BigDecimal> omsCustOrdNoList, List<CustOrderDesc> custOrderDescList)
			throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("Inside setResponses method");
		for (BigDecimal omsCustOrdNo : omsCustOrdNoList) {
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			CustOrderDesc custOrderDesc = new CustOrderDesc();
			OrderStatus orderStatus = custOrderDesc.getOrderStatus();
			log.info("before sourceLocList");
			List<BigDecimal> sourceLocList = session
					.getOmsCoFulfillDetailFindByOmsCustOrderNo(omsCustOrdHead.getOmsCustOrdNo());
			log.info("sourceLocList--->" + sourceLocList);
			List<OmsCustOrdItem> omsCustOrdItemList = session
					.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
			log.info("omsCustOrdItemList-->" + omsCustOrdItemList);
			if (sourceLocList.size() != 0) {
				BigDecimal sourceLoc = sourceLocList.get(0);
				List<BigDecimal> fulfillLoc = session.getOmsCoFulfillDetailFindBySourceLoc(sourceLoc);
				for (BigDecimal fulloc : fulfillLoc) {
					/*
					 * 30-Dec-2021 for status seeting as wrong.
					 */
					if (fulloc != null && sourceLoc != null) {
						try {
							for (OmsCustOrdItem omsCustOrdItem : omsCustOrdItemList) {
								// OrderStatus
								orderStatus = OrderStatus.PARTIAL;
								BigDecimal deliveredQty = omsCustOrdItem.getCumQtyDelivered();
								BigDecimal cancelledQty = omsCustOrdItem.getQtyCancelled();
								BigDecimal orderedQuantity = omsCustOrdItem.getQtyOrderedSuom();
								BigDecimal sumOfDeliveredAndCanceled = deliveredQty.add(cancelledQty);
								int result = sumOfDeliveredAndCanceled.compareTo(orderedQuantity);
								if (result == 0) {
									if (deliveredQty.intValue() == 0) {
										orderStatus = OrderStatus.CANCELED;
									} else {
										orderStatus = OrderStatus.COMPLETED;
									}
								} else {
									if (sumOfDeliveredAndCanceled.intValue() < orderedQuantity.longValue()) {
										if (sumOfDeliveredAndCanceled.intValue() > 0) {
											orderStatus = OrderStatus.PARTIAL;
										} else {
											orderStatus = OrderStatus.FILLED;
										}
									}
								}
							}
							custOrderDescList.add(custOrderDesc);
						} catch (NumberFormatException e) {
							log.info(e.getMessage());
						}

					} else {
						orderStatus = OrderStatus.NEW;
					}
				}
			} else {
				orderStatus = OrderStatus.NEW;
			}
			if (orderStatus == OrderStatus.COMPLETED || orderStatus == OrderStatus.CANCELED) {
				log.info("CANCELED/COMPLETED");
				String paraName = "COMPLETED_ORDER_RANGE";
				String paraId = "QUERY_COMPLETED_ORDER";
				String diff = session.getOmsSystemParametersFindIndValue(paraName, paraId);
				long differenceOfDays = Long.parseLong(diff);
				// convert lastUpdatedateTime into Date format
				Timestamp lastUpdateDateTime = omsCustOrdHead.getLastUpdateDatetime();
				log.info("LastUpdateDatetime:" + lastUpdateDateTime);
				long timestamp = lastUpdateDateTime.getTime();
				Date updatedDate = new Date(timestamp);
				// Current Date
				Timestamp stamp = new Timestamp(System.currentTimeMillis());
				Date currentdate = new Date(stamp.getTime());
				log.info("Current Date:" + currentdate);
				// Calculate difference of days
				// in milliseconds
				long diffOfDays = currentdate.getTime() - updatedDate.getTime();
				long diffDays = diffOfDays / (24 * 60 * 60 * 1000);
				if (diffDays > differenceOfDays) {
					log.info("less than 15");
					log.info("Filtered Order No: " + omsCustOrdHead.getCustOrderNo());
					continue;
				}
			}
			if (omsCustOrdHead.getCustOrderNo() != null) {
				custOrderDesc.setCustomerOrderId(omsCustOrdHead.getCustOrderNo());
				log.info("omsCustOrdHead.getCustOrderNo()" + omsCustOrdHead.getCustOrderNo());
			}
			if (omsCustOrdHead.getOrderRequestorId() != null) {
				custOrderDesc.setInitiateLocId(omsCustOrdHead.getOrderRequestorId().longValue());
				log.info("before setInitiateLocType");
			}
			custOrderDesc.setOrderStatus(orderStatus);
			// Initiate_country_code
			List<Addr> addrList = session.getAddrFindByAddrKeyValue1(omsCustOrdHead.getOrderRequestorId().toString());
			log.info("before getAddrFindByKeyValue1()");

			for (Addr addr : addrList) {
				custOrderDesc.setInitiateCountryCode(addr.getCountryId());
				log.info("Initiate country code: " + addr.getCountryId());
			}

			// InitiateLocType
			if (omsCustOrdHead.getCustOrderType() != null) {
				String ordType = omsCustOrdHead.getCustOrderType();
				log.info("getCustOrderType:" + ordType);
				if (ordType.equalsIgnoreCase("STR")) {
					custOrderDesc.getInitiateLocType();
					custOrderDesc.setInitiateLocType(InitiateLocType.fromValue("S"));
					log.info("after setInitiateLocType");
				} else if (ordType.equalsIgnoreCase("B2B")) {
					custOrderDesc.getInitiateLocType();
					custOrderDesc.setInitiateLocType(InitiateLocType.fromValue("O"));
					log.info("setting InitiateLocType as S");
				}
			}
			if (omsCustOrdHead.getCreateDatetime() != null) {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = null;
				try {
					datatypeFactory = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException f) {
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrderDesc.toString()));
				}
				XMLGregorianCalendar createTimestamp = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				Calendar createTimestampResp = Calendar.getInstance();
				createTimestampResp.setTimeInMillis(omsCustOrdHead.getCreateDatetime().getTime());
				createTimestamp.setMonth(createTimestampResp.get(Calendar.MONTH) + 1);
				createTimestamp.setYear(createTimestampResp.get(Calendar.YEAR));
				createTimestamp.setDay(createTimestampResp.get(Calendar.DAY_OF_MONTH));
				custOrderDesc.setCreateTimestamp(createTimestamp);
				log.info("setCreateTimestamp: " + createTimestamp);
			}
			if (omsCustOrdHead.getLastUpdateDatetime() != null) {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = null;
				try {
					datatypeFactory = DatatypeFactory.newInstance();
				} catch (DatatypeConfigurationException f) {
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(custOrderDesc.toString()));
				}
				XMLGregorianCalendar updateTimestamp = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				Calendar updateTimestampResp = Calendar.getInstance();
				updateTimestampResp.setTimeInMillis(omsCustOrdHead.getLastUpdateDatetime().getTime());
				updateTimestamp.setMonth(updateTimestampResp.get(Calendar.MONTH) + 1);
				updateTimestamp.setYear(updateTimestampResp.get(Calendar.YEAR));
				updateTimestamp.setDay(updateTimestampResp.get(Calendar.DAY_OF_MONTH));
				custOrderDesc.setUpdateTimestamp(updateTimestamp);
				log.info("setUpdateTimestamp: " + updateTimestamp);
			}
			custOrderDesc.setGiftReceiptAssigned(GiftReceiptAssigned.N);
			log.info("after setGiftReceiptAssigned");
			LocaleDesc localDesc = new LocaleDesc();
			for (Addr addr1 : addrList) {
				if (addr1.getCountryId() != null) {
					localDesc.setCountry(addr1.getCountryId());
				}
			}
			if (omsCustOrdHead.getCustomerLang() != null) {
				localDesc.setLang(omsCustOrdHead.getCustomerLang());
			}
			custOrderDesc.setLocaleDesc(localDesc);
			CustomerDesc customerDesc = new CustomerDesc();
			if (omsCustOrdHead.getCustId() != null) {
				customerDesc.setCustomerId(omsCustOrdHead.getCustId());
			}
			customerDesc.setCustomerType(CustomerType.REGULAR);
			ContactDesc contactDesc = new ContactDesc();
			log.info("after contactDesc");
			log.info("getOmsCustOrdNo: " + omsCustOrdHead.getOmsCustOrdNo());
			try {
				OmsCustOrdAddress omsCustOrdAddress = session
						.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdHead.getOmsCustOrdNo());
				log.info("after omsCustOrdAddress:" + omsCustOrdAddress);
				if (omsCustOrdAddress.getDeliverFirstName() != null) {
					contactDesc.setFirstName(omsCustOrdAddress.getDeliverFirstName());
					log.info("First Name:" + omsCustOrdAddress.getDeliverFirstName());
				}
				if (omsCustOrdAddress.getDeliverLastName() != null) {
					contactDesc.setLastName(omsCustOrdAddress.getDeliverLastName());
					log.info("Last Name:" + omsCustOrdAddress.getDeliverLastName());
				}
			} catch (Exception e) {
				log.error("Address doesnt exist");
			}
			contactDesc.setMiddleName("");
			contactDesc.setCompanyName("");
			customerDesc.setContactDesc(contactDesc);
			custOrderDesc.setCustomerDesc(customerDesc);
			for (OmsCustOrdItem omsCustOrdItem1 : omsCustOrdItemList) {
				log.info("omsCustOrdItem omsCustOrdNo" + omsCustOrdItem1.getOmsCustOrdNo());
				if (omsCustOrdItem1.getRetailCurr() != null) {
					custOrderDesc.setCurrencyCode(omsCustOrdItem1.getRetailCurr());
					log.info("after omsCustOrdItemList" + omsCustOrdItem1.getRetailCurr());
				}
				custOrderDescList.add(custOrderDesc);
			}
		}

	}

	public BigDecimal checkNullValueForNumber(BigDecimal value) {
		if (value == null) {
			return null;
		} else {
			return value;
		}
	}

	private static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}
}
