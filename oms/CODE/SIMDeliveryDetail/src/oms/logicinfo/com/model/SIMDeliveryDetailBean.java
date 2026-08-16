package oms.logicinfo.com.model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.ItemSuppCountryDim;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCustOrdAddress;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

public class SIMDeliveryDetailBean {
	private final static Logger log = Logger.getLogger(SIMDeliveryDetailBean.class.getName());

	public SIMDeliveryDetailBean() {
		super();
	}

	public void validateInput(SIMDeliveryDetail input) {
		log.info("Inside validation for SIMDeliveryDetail");
	}

	public SimDeliveryDetailResponse processOrdersDetails(SIMDeliveryDetail input) throws SOAPException, Exception {

		log.info("Inside processOrdersDetails method of SIMDeliveryDetail");
		log.info("Number of orders to be fetched : " + input.getNumberOfOrders());

		// Order status should be either ReadyToShip or Undelivered
		log.info("Order Status : " + input.getOrderStatus());

		// Order Source should be E-Commerce as of now
		log.info("Order Source : " + input.getOrderSource());

		// Retrieve customer details in case the value is set to true
		log.info("Customer Details : " + input.getRetrieveCustomerDetails());

		// Retrieve the product details in case the value is set to true
		log.info("Product Details : " + input.getRetrieveProductDetails());
		SimDeliveryDetailResponse SIMDeliveryDetailResponse = new SimDeliveryDetailResponse();

		if (input.getOrderStatus() != null && input.getOrderStatus().equalsIgnoreCase("READY_TO_SHIP")) {
			log.info("Inside READY TO SHIP order status");
			log.info("calling processReadyToShipDetails");
			try {
				SIMDeliveryDetailResponse = generateResponse(input, "SUCCESS");

			} catch (Throwable e) {
				log.warn("Error while processing ready to ship", e);
			}
		} else if (input.getOrderStatus() != null && input.getOrderStatus().equalsIgnoreCase("UNDELIVERED")) {
			log.info("Inside UNDELIVERED order status");
			log.info("calling processUndeliveredDetails");
			SIMDeliveryDetailResponse = generateResponseForUndelivered(input, "SUCCESS");
		} else {
			// throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("Request
			// Containes Incorrect Data"));
			throw new Exception("Request Contains Incorrect Data");
		}
		return SIMDeliveryDetailResponse;
	}

	/**
	 * @param input
	 * @param status
	 * @return
	 */
	public SimDeliveryDetailResponse generateResponse(SIMDeliveryDetail input, String status) throws SOAPException, SQLException, Exception {
		log.info("Status : Success");
		log.info("Inside generateResponse method of READYTO SHIP ORDERS");
		SimDeliveryDetailResponse SIMDeliveryDetailResponse = new SimDeliveryDetailResponse();
		Order orders = null;
		BigDecimal omsCustOrdNo = null;
		CustomerOrderResponseAddress customerOrderResponseAddress = new CustomerOrderResponseAddress();
		String query3 = null;
		Connection simConnection = null;
		Connection omsConnection = null;
		Connection connection = null;
		PreparedStatement prepStatement = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStmnt = null;
		ResultSet rs = null;
		String carrierCity = null;
		String extraOMSCity = null;
		String extracarrier = null;
		String omsCity = null;
		String carrier = null;
		String query5 = null;
		CustomerOrderResponseItems customerOrderResponseItems = null;

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdAddress omsCustOrdAddress = new OmsCustOrdAddress();
		SIMDeliveryDetailCommon SIMDeliveryDetailCommon = new SIMDeliveryDetailCommon();
		log.info("created object for SIMDeliveryDetailResponse");

		ArrayList<OrderInformation> OrderInformationList = SIMDeliveryDetailCommon.processReadyToShipDetails(input.getNumberOfOrders());

		int i = 0;
		String numToken = "[\\p{Digit}&&[123456789]]+";

		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			simConnection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("conn : " + simConnection);

			log.info("connecting to OMS Schema");
			log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
			omsConnection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("omsConnection : " + omsConnection);
			if (input.getNumberOfOrders() != 0 || input.equals(numToken)) {
				Long j = input.getNumberOfOrders();
				int number = j.intValue();
				log.info("Requested number of order is :" + number);

				for (OrderInformation orderInformation : OrderInformationList) {
					if (i != number) {
						omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(orderInformation.getOrderNo());

						BigDecimal tenderTypeID = new BigDecimal(0);
						BigDecimal tenderAmt = new BigDecimal(0);
						try {
							tenderTypeID = session.getOmsCustOrdTenderFindTenderTypeIdByOmsCustOrdNo(omsCustOrdNo);
							log.info("tenderTypeID : " + tenderTypeID);
							tenderAmt = session.getOmsCustOrdTenderSumOfTenderAmt(omsCustOrdNo);
							log.info("tenderAmt : " + tenderAmt);
						} catch (Exception e) {
							log.info(e.getMessage());
							log.info("Catch in ready to ship");
						}
						orders = new Order();
						orders.setOrderNo(orderInformation.getOrderNo());
						orders.setStoreId(orderInformation.getStoreId());
						// name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
						orders.setCarrier(orderInformation.getCarrier());
						// carrier = orderInformation.getCarrier().substring(0, 1).toUpperCase() +
						// orderInformation.getCarrier().substring(1).toLowerCase();
						carrier = orderInformation.getCarrier();
						log.info("Ready to Ship ---- CARRIER ORDER INFORMATION: " + orderInformation.getCarrier());
						log.info("Ready to Ship ---- CARRIER :  " + orderInformation.getCarrier().substring(0, 1).toUpperCase() + orderInformation.getCarrier().substring(1).toLowerCase());
						orders.setOrderValue(tenderAmt);
						orders.setShipmentId(String.valueOf(0));
						log.info("Before COD setter" + tenderTypeID + ":::" + tenderTypeID.signum());
						if (tenderTypeID != null) {
							if (tenderTypeID.equals(new BigDecimal(106))) {
								orders.setIsCod("Y");
								log.info("Inside 106 " + orders.getIsCod());
							} else {
								orders.setIsCod("N");
								log.info("Inside 106 else " + orders.getIsCod());
							}
						} else {
							orders.setIsCod("N");
						}
						OMSUtilCommons utilObj = new OMSUtilCommons();
						orders.setIsExpressDelivery("N");
						log.info(" Checking the order is " + orderInformation.getOrderNo() + "Express order or not ");
						Boolean expressResult = utilObj.getExpressDelvdetails(orderInformation.getOrderNo());
						log.info(" Checking the order is " + orderInformation.getOrderNo() + "Express order Result  " + expressResult);
						if (expressResult == Boolean.TRUE) {
							orders.setIsExpressDelivery("Y");
						} else {
							log.info(" inside express delivery else");
							orders.setIsExpressDelivery("N");
						}
						log.info(" after else");
						log.info(" after total weight");
						if (input.getRetrieveProductDetails().equalsIgnoreCase("TRUE")) {
							log.info(" inside input.getRetrieveProductDetails().equalsIgnoreCase");
							for (ItemListBean ItemListBean : orderInformation.getItemListBeanDetail()) {
								log.info(" inside ItemListBean");
								log.info("session variable 1: " + session);
								List<ItemSuppCountryDim> itemSuppCountryDim = session.getItemSuppCountryDimFindByItemId(ItemListBean.getItem());
								log.info(" 1");
								String itemName = session.getItemMasterFindItemDesc(ItemListBean.getItem());
								log.info(" 2");
								customerOrderResponseItems = new CustomerOrderResponseItems();
								log.info(" 3");
								customerOrderResponseItems.setLineNo(ItemListBean.getLineNo().intValue());
								log.info(" 4");
								customerOrderResponseItems.setProductSku(ItemListBean.getItem());
								log.info(" 5");
								customerOrderResponseItems.setName(itemName);
								log.info(" 6");
								customerOrderResponseItems.setQuantity(ItemListBean.getQuantity());
								log.info(" 7");
								customerOrderResponseItems.setDeliveryId(ItemListBean.getDeliveryId());
								log.info(" 8");

								if (ItemListBean.getUnitCostValue() != null) {
									log.info("9");
									customerOrderResponseItems.setTotalValue(ItemListBean.getUnitCostValue());
									log.info(" 10");
								} else {
									log.info(" 11");
									customerOrderResponseItems.setTotalValue(BigDecimal.ZERO);
									log.info(" 12");
								}
								log.info(" 13");
								if (ItemListBean.getUnitCostCurrency() != null) {
									log.info(" 14");
									customerOrderResponseItems.setRetailCurrencyCode(ItemListBean.getUnitCostCurrency());
									log.info(" 15");
								} else {
									log.info(" 16");
									customerOrderResponseItems.setRetailCurrencyCode("");
									log.info(" 17");
								}
								log.info(" 18");
								if (!itemSuppCountryDim.isEmpty() && null != itemSuppCountryDim.get(0).getWeight()) {
									log.info(" 19");
									customerOrderResponseItems.setTotalWeight(itemSuppCountryDim.get(0).getWeight().multiply(new BigDecimal(ItemListBean.getQuantity())));
								}

								else {
									log.info(" 20");
									customerOrderResponseItems.setTotalWeight(BigDecimal.ZERO);
								}
								log.info(" 21");
								customerOrderResponseItems.setWeightUom("KG");
								log.info(" 22");
								orders.getCustomerOrderItems().add(customerOrderResponseItems);
								log.info(" 23");

							}
							log.info(" 24");
						}
						log.info(" 25");
						log.info("session:" + session);

						String ordcustrec = "";
						try {
							log.info("Inside Try for RTS");
							query3 = "update ordcust set BILL_PHONE = (select BILL_PHONE from ordcust where CUSTOMER_ORDER_NO = '" + orderInformation.getOrderNo()
									+ "' and rownum=1 and BILL_PHONE IS NOT NULL) where CUSTOMER_ORDER_NO = '" + orderInformation.getOrderNo() + "' AND BILL_PHONE IS NULL";

							log.info(query3);
							log.info("after update ordcust query in RTS");

							preparedStmnt = omsConnection.prepareStatement(query3);
							log.info("order_no" + orderInformation.getOrderNo());
							preparedStmnt.executeUpdate();
							log.info("update successful for ordcust");

							ordcustrec = session.getOrdcustFindBillPhoneByCustomerOrderNo(orderInformation.getOrderNo());
							log.info("@@@@@@@@@@@@@Value of Bill Phone : " + ordcustrec);
						} catch (Exception e) {
							log.info("**********Exception*****");
							log.info(e.getMessage());
						}
						log.info("after session:" + session);
						// log.info("ordcustrec.getClass().getName()"+ordcustrec.getClass().getName());

						if (input.getRetrieveCustomerDetails().equalsIgnoreCase("TRUE")) {
							log.info("26");
							customerOrderResponseAddress = new CustomerOrderResponseAddress();
							log.info(" 27");
							omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(orderInformation.getOrderNo());
							log.info(" 28");
							omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
							log.info(" 29");
							customerOrderResponseAddress.setCollectorName(omsCustOrdAddress.getDeliverFirstName());
							log.info(" 30");
							customerOrderResponseAddress.setCustomerId(Long.parseLong(omsCustOrdAddress.getCustId()));
							log.info(" 31");
							if ("ELITE".equals(orders.getCarrier())) {								
								customerOrderResponseAddress.setDeliveryMobile(omsCustOrdAddress.getDeliverPhoneNo().replaceAll("^(00)?", ""));
							} else {
								customerOrderResponseAddress.setDeliveryMobile(omsCustOrdAddress.getDeliverPhoneNo());
							}
							log.info(" 32");
							if (null != ordcustrec) {
								log.info("33");
								customerOrderResponseAddress.setPhone(ordcustrec);
								log.info(" 34");
							}

							else {
								log.info(" 35");
								customerOrderResponseAddress.setPhone("");
								log.info(" 36");
							}
							customerOrderResponseAddress.setFirstName(omsCustOrdAddress.getBillFirstName());
							log.info(" 37");
							customerOrderResponseAddress.setLastName(omsCustOrdAddress.getBillLastName());
							log.info(" 38");

							String lang = session.getOmsCustOrdHeadFindLanguage(omsCustOrdNo);
							log.info(" 39");
							customerOrderResponseAddress.setLanguage(lang);
							log.info(" 40");
							customerOrderResponseAddress.setCustomerName(omsCustOrdAddress.getBillFirstName() + " " + omsCustOrdAddress.getBillLastName());
							log.info(" 41");

							if (null != omsCustOrdAddress.getBillAdd2()) {
								log.info("42");
								customerOrderResponseAddress.setEmail(omsCustOrdAddress.getBillAdd2());
								log.info(" 43");
							} else if (orderInformation.getStoreId() != null && orderInformation.getStoreId().toString().startsWith("3")) {
								/* For OMAN Orders */
								customerOrderResponseAddress.setEmail("dummy@extra.com");
							} else {
								log.info(" 44");
								customerOrderResponseAddress.setEmail("");
								log.info(" 45");
							}

							omsCity = omsCustOrdAddress.getDeliverCity();
							extraOMSCity = omsCity.toLowerCase();
							extracarrier = carrier.toLowerCase();

							log.info("String 1(extraOMSCity) ==========" + extraOMSCity);
							log.info("String 2 (carrier)==========" + extracarrier);
							query5 = "select CARRIER_CITY from xx_vxref_carrier_city where lower(EXTRA_CITY)= ? and lower(CARRIER_CODE) = ? ";
							try {
								log.info("connecting to SIM Schema");
								log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
								// connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
								log.info("connection of Delivery City : " + connection);
								prepStatement = simConnection.prepareStatement(query5);
								prepStatement.setString(1, extraOMSCity);
								prepStatement.setString(2, extracarrier);
								rs = prepStatement.executeQuery();
								if (rs.next()) {
									carrierCity = rs.getString("CARRIER_CITY");
								} else {
									carrierCity = null;
								}
								log.info("CARRIER_CITY FROM SIM TABLE xx_vxref_carrier_city : " + carrierCity);
								// customerOrderResponseAddress.setCity(carrierCity);
								if (null != carrierCity) {
									customerOrderResponseAddress.setCity(carrierCity);
								} else
									customerOrderResponseAddress.setCity(omsCustOrdAddress.getDeliverCity());

							} catch (Exception e) {
								log.error(e.getMessage());
							}

//                            deliveryCity = checkDeliveryCarrierCity(simConnection  ,omsCustOrdAddress.getDeliverCity(), orderInformation.getCarrier().substring(0, 1).toUpperCase() +
//                                          orderInformation.getCarrier().substring(1).toLowerCase());
//                            log.info("Delivery City with NEW Change : "+deliveryCity);
//                            if(deliveryCity.equalsIgnoreCase(""))
//                            {
//                            customerOrderResponseAddress.setCity(omsCustOrdAddress.getDeliverCity());
//                            }
//                            else {
//                                customerOrderResponseAddress.setCity(deliveryCity);
//                            }
//                            log.info("Ready_to_ship -- 46 --- OMS City "+omsCustOrdAddress.getDeliverCity());
							customerOrderResponseAddress.setCountryId(omsCustOrdAddress.getDeliverCountry());
							log.info(" 47");
							if (null != omsCustOrdAddress.getDeliverAdd2()) {
								log.info(" 48");
								customerOrderResponseAddress.setDistrict(omsCustOrdAddress.getDeliverAdd2());
							} else {
								log.info(" 49");
								customerOrderResponseAddress.setDistrict("");
							}
							log.info(" 50");
							// mani changes
							// customerOrderResponseAddress.setShippingAddress(omsCustOrdAddress.getDeliverAdd1());
							if (null != omsCustOrdAddress.getDeliverAdd3()) {
								log.info(" 50 If");
								customerOrderResponseAddress.setShippingAddress(omsCustOrdAddress.getDeliverAdd1() + " " + omsCustOrdAddress.getDeliverAdd3());
							} else {
								log.info(" 50 else");
								customerOrderResponseAddress.setShippingAddress(omsCustOrdAddress.getDeliverAdd1());
							}
							log.info(" 51");
							orders.setCustomerOrderAddress(customerOrderResponseAddress);
							log.info(" 52");
						}
						log.info(" 53");
						i++;
						log.info(" 54");
						SIMDeliveryDetailResponse.getOrder().add(orders);
						log.info(" 55");
					}

				}

			} // End of if for --- numeric check
			else {
				new Exception("Request Contains Incorrect Data");
			}
		} catch (Exception e) {
			log.info("Exception occured while connecting to data base : " + e.getMessage());
			log.error(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(simConnection, preparedStatement, rs);
				OMSUtil.closeDBConnection(omsConnection, preparedStmnt, rs);

			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}
		return SIMDeliveryDetailResponse;
	} // End of generate response method

	public SimDeliveryDetailResponse generateResponseForUndelivered(SIMDeliveryDetail input, String status) throws SOAPException, SQLException, Exception {

		log.info("Status : Success");
		log.info("Inside generateResponse method of UNDELIVERED ORDERS");
		SimDeliveryDetailResponse SIMDeliveryDetailResponse = new SimDeliveryDetailResponse();
		Order orders = null;
		String query2 = null;
		String query4 = null;
		String query5 = null;
		Connection simConnection = null;
		Connection omsConnection = null;
		PreparedStatement prepStatement = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement prepareStmt = null;
		ResultSet rs = null;
		BigDecimal omsCustOrdNo = null;
		String carrierCity = null;
		String extraOMSCity = null;
		String omsCity = null;
		String carrier = null;
		CustomerOrderResponseAddress customerOrderResponseAddress = new CustomerOrderResponseAddress();
		// List<Order> responseorderList = SIMDeliveryDetailResponse.getOrder();
		CustomerOrderResponseItems customerOrderResponseItems = null;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsCustOrdAddress omsCustOrdAddress = new OmsCustOrdAddress();
		SIMDeliveryDetailCommon SIMDeliveryDetailCommon = new SIMDeliveryDetailCommon();
		log.info("created object for SIMDeliveryDetailResponse");

		ArrayList<OrderInformation> OrderInformationList = SIMDeliveryDetailCommon.processUndeliveredDetails(input.getNumberOfOrders());
		int i = 0;
		String numToken = "[\\p{Digit}&&[123456789]]+";

		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			simConnection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("conn : " + simConnection);

			omsConnection = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			log.info("conn : " + omsConnection);

			if (input.getNumberOfOrders() != 0 || input.equals(numToken)) {
				Long j = input.getNumberOfOrders();
				int number = j.intValue();
				log.info("Requested number of order is :" + number);

				for (OrderInformation orderInformation : OrderInformationList) {
					if (i != number) {
						log.info("Current order number in loop is : " + orderInformation.getOrderNo());
						log.info("*");
						omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(orderInformation.getOrderNo());
						log.info("------After setting omscustorderno---------" + omsCustOrdNo);
						BigDecimal tenderTypeID = new BigDecimal(0);
						BigDecimal tenderAmt = new BigDecimal(0);
						try {
							tenderTypeID = session.getOmsCustOrdTenderFindTenderTypeIdByOmsCustOrdNo(omsCustOrdNo);
							log.info("------tenderTypeID ---------" + tenderTypeID);
							tenderAmt = session.getOmsCustOrdTenderSumOfTenderAmt(omsCustOrdNo);
							log.info("------Before creation of object for Orders ---------" + tenderAmt);
						} catch (Exception e) {
							log.info(e.getMessage());
							log.info("inside catch");
						}

						orders = new Order();
						log.info("------After creation of object for Orders----------------");
						orders.setOrderNo(orderInformation.getOrderNo());
						log.info("Order Number : " + orderInformation.getOrderNo());
						orders.setStoreId(orderInformation.getStoreId());
						log.info("Store Number : " + orderInformation.getStoreId());
						// orders.setCarrier(orderInformation.getCarrier());
						orders.setCarrier(orderInformation.getCarrier());
						// carrier = orderInformation.getCarrier().substring(0, 1).toUpperCase() +
						// orderInformation.getCarrier().substring(1).toLowerCase();
						carrier = orderInformation.getCarrier();
						log.info("Ready to Ship ---- CARRIER ORDER INFORMATION: " + orderInformation.getCarrier());
						log.info("Carrier : " + orderInformation.getCarrier().substring(0, 1).toUpperCase() + orderInformation.getCarrier().substring(1).toLowerCase());
						log.info("UNDELIVERED --- Carrier : " + orderInformation.getCarrier().substring(0, 1).toUpperCase() + orderInformation.getCarrier().substring(1).toLowerCase());
						orders.setShipmentId(orderInformation.getShipmentId());

						orders.setOrderValue(tenderAmt);
						log.info("after setting order value");

						if (!tenderTypeID.equals(null)) {
							if (tenderTypeID.equals("106")) {
								orders.setIsCod("Y");

							} else {
								orders.setIsCod("N");
							}
						} else {
							orders.setIsCod("N");
						}
						log.info("after setting order value fro tender id");

						if (input.getRetrieveProductDetails().equalsIgnoreCase("TRUE")) {
							log.info("***Get Retrieve Product Details: TRUE");
							for (ItemListBean ItemListBean : orderInformation.getItemListBeanDetail()) {
								log.info("***Get Retrieve Product Details: Inside For");
								log.info("***Get Retrieve Product Details: Inside For" + ItemListBean.getCustomerOrderId());
								List<ItemSuppCountryDim> itemSuppCountryDim = session.getItemSuppCountryDimFindByItemId(ItemListBean.getItem());
								log.info("***Get Retrieve Product Details: itemSuppCountryDim" + itemSuppCountryDim.size());
								String itemName = session.getItemMasterFindItemDesc(ItemListBean.getItem());
								log.info("Value of itemSuppCountryDim :");
								customerOrderResponseItems = new CustomerOrderResponseItems();
								log.info("$$$$Order Number : " + orders.getOrderNo());
								customerOrderResponseItems.setProductSku(ItemListBean.getItem());
								log.info("$$$$After Product Sku : " + orders.getOrderNo());
								customerOrderResponseItems.setLineNo(ItemListBean.getLineNo().intValue());
								log.info("$$$$Order Line No : " + orders.getOrderNo());
								customerOrderResponseItems.setName(itemName);
								log.info("$$$$Item : " + ItemListBean.getItem());
								customerOrderResponseItems.setQuantity(ItemListBean.getQuantity());
								log.info("$$$$Quantity : " + ItemListBean.getQuantity());
								customerOrderResponseItems.setDeliveryId(ItemListBean.getDeliveryId());

								// Updating Ful_ORD_DV table
								query2 = "update Ful_Ord_Dlv set update_date = sysdate where ID IN ('" + ItemListBean.getDeliveryId() + "')";
								log.info(query2);
								log.info("after update ful_ord_dlv query");

								PreparedStatement preparedStmt = simConnection.prepareStatement(query2);

								preparedStmt.executeUpdate();
								log.info("update successful");

								if (ItemListBean.getUnitCostValue() != null) {
									customerOrderResponseItems.setTotalValue(ItemListBean.getUnitCostValue());
								} else {
									customerOrderResponseItems.setTotalValue(BigDecimal.ZERO);
								}
								if (ItemListBean.getUnitCostCurrency() != null) {
									customerOrderResponseItems.setRetailCurrencyCode(ItemListBean.getUnitCostCurrency());
								} else {
									customerOrderResponseItems.setRetailCurrencyCode("");
								}

								log.info("$$$$$Unit value cost : " + ItemListBean.getUnitCostValue());
								// log.info("--------> total weight : " +itemSuppCountryDim.get(0));
								if (!itemSuppCountryDim.isEmpty()) {
									customerOrderResponseItems.setTotalWeight(itemSuppCountryDim.get(0).getWeight().multiply(new BigDecimal(ItemListBean.getQuantity())));
								}
								customerOrderResponseItems.setWeightUom("KG");
								orders.getCustomerOrderItems().add(customerOrderResponseItems);
							}
						}

						String ordcustrec = "";
						try {
							log.info("inside try for undelivered");
							query4 = "update ordcust set BILL_PHONE = (select BILL_PHONE from ordcust where CUSTOMER_ORDER_NO = '" + orderInformation.getOrderNo()
									+ "' and rownum=1 and BILL_PHONE IS NOT NULL) where CUSTOMER_ORDER_NO = '" + orderInformation.getOrderNo() + "' AND BILL_PHONE IS NULL";

							log.info(query4);
							log.info("after update ordcust query in UNDELIVERED");
							log.info("connecting to OMS Schema");
							log.info("OMSConstants.DS_OMS_STRING " + OMSConstants.DS_OMS_STRING);
							prepareStmt = omsConnection.prepareStatement(query4);
							prepareStmt.executeUpdate();

							log.info("update successful for ordcust in UNDELIVERED");

							ordcustrec = session.getOrdcustFindBillPhoneByCustomerOrderNo(orderInformation.getOrderNo());
							log.info("inside try for undelivered after executing ordcustrec");
						} catch (Exception e) {
							log.info(e.getMessage());
							log.info("@@@@@@@@@@@@@Value of Bill Phone : " + ordcustrec);
						}
						if (input.getRetrieveCustomerDetails().equalsIgnoreCase("TRUE")) {
							log.info("Getting address detail from OMSCustOrdAddress table");
							customerOrderResponseAddress = new CustomerOrderResponseAddress();
							omsCustOrdNo = session.getOmsCustOrdHeadFindOmsCustOrdNo(orderInformation.getOrderNo());

							omsCustOrdAddress = session.getOmsCustOrdAddressFindByOmsCustOrdNo(omsCustOrdNo);
							customerOrderResponseAddress.setCollectorName(omsCustOrdAddress.getDeliverFirstName());
							customerOrderResponseAddress.setCustomerId(Long.parseLong(omsCustOrdAddress.getCustId()));
							if ("ELITE".equals(orders.getCarrier())) {								
								customerOrderResponseAddress.setDeliveryMobile(omsCustOrdAddress.getDeliverPhoneNo().replaceAll("^(00)?", ""));
							} else {
								customerOrderResponseAddress.setDeliveryMobile(omsCustOrdAddress.getDeliverPhoneNo());
							}

							if (null != ordcustrec) {
								customerOrderResponseAddress.setPhone(ordcustrec);
							} else {
								customerOrderResponseAddress.setPhone("");
							}
							customerOrderResponseAddress.setCustomerName(omsCustOrdAddress.getBillFirstName() + " " + omsCustOrdAddress.getBillLastName());
							if (null != omsCustOrdAddress.getBillAdd2()) {
								customerOrderResponseAddress.setEmail(omsCustOrdAddress.getBillAdd2());
							} else {
								customerOrderResponseAddress.setEmail("");
							}

							omsCity = omsCustOrdAddress.getDeliverCity();
							extraOMSCity = omsCity.substring(0, 1).toUpperCase() + omsCity.substring(1).toLowerCase();
							log.info("String 1(extraOMSCity) ==========" + extraOMSCity);
							log.info("String 2 (carrier)==========" + carrier);
							query5 = "select CARRIER_CITY from xx_vxref_carrier_city where EXTRA_CITY= ? and CARRIER_CODE = ? ";
							try {
								log.info("connecting to SIM Schema");
								log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
								// connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
								prepStatement = simConnection.prepareStatement(query5);
								prepStatement.setString(1, extraOMSCity);
								prepStatement.setString(2, carrier);
								rs = prepStatement.executeQuery();
								while (rs.next()) {
									carrierCity = rs.getString("CARRIER_CITY");

								}
								log.info("CARRIER_CITY FROM SIM TABLE xx_vxref_carrier_city : " + carrierCity);
								customerOrderResponseAddress.setCity(carrierCity);
								if (null != carrierCity) {
									customerOrderResponseAddress.setCity(carrierCity);
								} else
									customerOrderResponseAddress.setCity(omsCustOrdAddress.getDeliverCity());

							} catch (Exception e) {
								log.error(e.getMessage());
							}
							// deliveryCity = checkDeliveryCarrierCity(extraOMSCity, carrier);
							// log.info("Delivery City with NEW Change : "+deliveryCity);
//                            if(deliveryCity.equalsIgnoreCase(""))
//                            {
//                            customerOrderResponseAddress.setCity(omsCustOrdAddress.getDeliverCity());
//                            }
//                            else 
//                            {
//                                customerOrderResponseAddress.setCity(deliveryCity);  
//                            }
							customerOrderResponseAddress.setCountryId(omsCustOrdAddress.getDeliverCountry());

							if (null != omsCustOrdAddress.getDeliverAdd2()) {
								customerOrderResponseAddress.setDistrict(omsCustOrdAddress.getDeliverAdd2());
							} else {
								customerOrderResponseAddress.setDistrict("");
							}
							customerOrderResponseAddress.setShippingAddress(omsCustOrdAddress.getDeliverAdd1());
							orders.setCustomerOrderAddress(customerOrderResponseAddress);
						}
						i++;
						SIMDeliveryDetailResponse.getOrder().add(orders);
					}
				}

			} // End of if for --- numeric check
			else {
				new Exception("Request Contains Incorrect Data");
			}
		} catch (Exception e) {
			log.info("Exception occured while connecting to data base : " + e.getMessage());
			log.error(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(simConnection, preparedStatement, rs);
				OMSUtil.closeDBConnection(omsConnection, prepareStmt, rs);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// throw new SOAPException(e.getMessage());
			}
			try {
				if (prepStatement != null && !prepStatement.isClosed()) {
					prepStatement.close();
				}
			} catch (Exception e) {
			}
		}
		return SIMDeliveryDetailResponse;
	} // End of generateResponseForUndelivered

	// public void error(String errorMessage) {
	// SIMDeliveryDetailResponse
	// }

	private String checkDeliveryCarrierCity(Connection connection, String omsCity, String carrier) throws Exception {
		log.info("Calling checkDeliveryCarrierCity method");
		// Connection connection = null;
		PreparedStatement prepStatement = null;
		String query4 = null;
		ResultSet rs = null;
		String carrierCity = null;
		String extraOMSCity = omsCity.substring(0, 1).toUpperCase() + omsCity.substring(1).toLowerCase();
		log.info("String 1(extraOMSCity) ==========" + extraOMSCity);
		log.info("String 2 (carrier)==========" + carrier);
		query4 = "select CARRIER_CITY from xx_vxref_carrier_city where EXTRA_CITY= ? and CARRIER_CODE = ? ";
		try {
			log.info("connecting to SIM Schema");
			log.info("OMSConstants.DS_SIM_STRING " + OMSConstants.DS_SIM_STRING);
			connection = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			log.info("connection of Delivery City : " + connection);
			prepStatement = connection.prepareStatement(query4);
			prepStatement.setString(1, extraOMSCity);
			prepStatement.setString(2, carrier);
			rs = prepStatement.executeQuery();
			while (rs.next()) {
				carrierCity = rs.getString("CARRIER_CITY");

			}
			log.info("CARRIER_CITY FROM SIM TABLE xx_vxref_carrier_city : " + carrierCity);
		} catch (Exception e) {
			log.info("Exception e " + e.getMessage());
		} finally {
			try {
				// OMSUtil.closeDBConnection(connection, prepStatement, rs);
				prepStatement.close();
				rs.close();

			} catch (Exception e) {
				log.error(e.getMessage());
				// throw new SOAPException(e.getMessage());
			}
		}

		return carrierCity;
	}
} // End of class SIMDeliveryDetailBean
