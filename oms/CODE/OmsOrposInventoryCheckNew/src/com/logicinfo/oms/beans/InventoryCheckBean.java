package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.model.CustOrdFulDesc;
import com.logicinfo.oms.model.CustOrdFulDescResponse;
import com.logicinfo.oms.model.CustOrdItmDesc;
import com.logicinfo.oms.model.CustOrdItmDescResponse;
import com.logicinfo.oms.model.InventoryCheck;
import com.logicinfo.oms.model.InventoryCheckResponse;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;

import retail.siebel.com.integration.InboundordercreationbpelprocessClientEp;

public class InventoryCheckBean {
	/**
	 * Injectable field for service WebServiceClient
	 **/
	@WebServiceRef
	InboundordercreationbpelprocessClientEp inboundordercreationbpelprocessClientEp;

	public InventoryCheckBean() {
		super();
	}

	BigDecimal combID;
	int priority;
	long SOH;
	private final static Logger log = Logger.getLogger(InventoryCheckBean.class.getName());

	public InventoryCheckResponse checkInventory(InventoryCheck inventoryCheck) throws SOAPException {
		log.info("inside checkInventory method");
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Map<String, Map<BigDecimal, Long>> itemQtyMap = new HashMap<String, Map<BigDecimal, Long>>();
		Map<String, Long> masterLocMap = new HashMap<String, Long>();
		Map<String, Long> futureLocMap = new HashMap<String, Long>();
		FindNextFulfillLoc findNextfulfillLoc = new FindNextFulfillLoc();
		BigDecimal nextLoc;
		String shipClassification = null;
		String promiseDate = null;
		String shipToStore = "N";
		InventoryCheckResponse inventoryCheckResponse = new InventoryCheckResponse();
		List<CustOrdFulDescResponse> custOrdFulDescResponseList = inventoryCheckResponse.getCustOrdFulDescResponse();
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
		log.info("shipingChargeDept " + shipingChargeDept);
		for (CustOrdFulDesc custOrdFulDesc : inventoryCheck.getCustOrdFulDesc()) {

			inventoryCheckResponse.setInitiateLocId(inventoryCheck.getInitiateLocId());
			String countryCode = null;
			if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("1") || String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("8")) {
				countryCode = "SA";
			} else if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("2")) {
				countryCode = "BH";
			} else if (String.valueOf(inventoryCheck.getInitiateLocId()).startsWith("3")) {
				countryCode = "OM";
			}
			CustOrdFulDescResponse custOrdFulDescResponse = new CustOrdFulDescResponse();
			custOrdFulDescResponse.setDeliveryType(custOrdFulDesc.getDeliveryType());
			if (custOrdFulDesc.getDeliveryType().equals("C")) {
				custOrdFulDescResponse.setPickLoc(custOrdFulDesc.getPickLoc());
			}
			custOrdFulDescResponse.setShipCity(custOrdFulDesc.getShipCity());
			List<CustOrdItmDescResponse> custOrdItmDescResponseList = custOrdFulDescResponse.getCustOrdItmDescResponse();
			int count = 1;
			long SOH = 0;
			for (CustOrdItmDesc custOrdItmDesc : custOrdFulDesc.getCustOrdItmDesc()) {
				int promiseDateFlag = 0;
				boolean isPOAvailable = false;
				count = 1;
				log.info("Item=" + custOrdItmDesc.getItemId() + "Lineno =" + custOrdItmDesc.getLineNo());
				priority = 1;
				CustOrdItmDescResponse custOrdItmDescResponse = new CustOrdItmDescResponse();
				long L_Cum_Ord_Qty = 0L;
				long L_Pending_Qty = custOrdItmDesc.getRequestedQty().longValue();
				BigDecimal itemDept = null;
				String inventoryIndn = null;
				String itemSku = null;
				String deliverZone = null;
				String marketPlaceInd = null;
				String applicationId = "ORPOS";
				try {
					itemSku = custOrdItmDesc.getItemId();
					itemDept = session.getItemMasterFindDept(custOrdItmDesc.getItemId());
					log.info("Item dept fetched from item_master " + itemDept);
					if (itemDept != null) {
						inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItmDesc.getItemId(), itemDept);
						log.info(" inventory Indicator= " + inventoryIndn);
					}
					if ((shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y")) || (itemDept.equals(BigDecimal.valueOf(8801) /* For Gift Card Voucher */))) {
						try {
							if (custOrdFulDesc.getDeliveryType().equals("S")) {
								log.info("Condition : Ship to customer");
								// Find the combination id from fulfillmentMatrix
								String refValue = "";
								try {
									refValue = session.getOmsReferenceDataFindRefValue("STATE_CITY_LINK", countryCode, custOrdFulDesc.getShipCity().toUpperCase());
									log.info("City from oms_reference_data is " + refValue);
								} catch (Exception e) {
									log.info("ref value is null");
								}
								try {
									// Added WALL BRACKET condition
									String[] classificationInd = null;
									String ShipIndicator = null;
									String wallBracketItem = findWallBracketSku(custOrdItmDesc.getItemId());
									log.info("comparing two items WB Item-" + wallBracketItem + " Normal Item-" + custOrdItmDesc.getItemId());
									if (wallBracketItem != null && wallBracketItem.equals(custOrdItmDesc.getItemId())) {
										log.info("Inside setting wall bracket item shipClassification -" + wallBracketItem);
										String otherItemShipClassification = null;

										for (CustOrdItmDesc item : custOrdFulDesc.getCustOrdItmDesc()) {
											if (findWallBracketSku(item.getItemId()) == null) {
												String[] classification = getOmsShipClassification(item.getItemId(), inventoryCheck.getInitiateLocId()).split("-");
												otherItemShipClassification = classification[0];
												ShipIndicator = classification[1];
												break;
											}
										}

										if (otherItemShipClassification != null) {
											shipClassification = otherItemShipClassification;
											log.info("wall bracket shipClassification -" + shipClassification);
										} else {
											classificationInd = getOmsShipClassification(custOrdItmDesc.getItemId(), inventoryCheck.getInitiateLocId()).split("-");
											shipClassification = classificationInd[0];
											ShipIndicator = classificationInd[1];
										}

									} else {
										classificationInd = getOmsShipClassification(custOrdItmDesc.getItemId(), inventoryCheck.getInitiateLocId()).split("-");
										shipClassification = classificationInd[0];
										ShipIndicator = classificationInd[1];
										log.info("normal item shipClassification -" + shipClassification);
									}
									// shipClassification=oMSUtilCommons.findShipmentClassification(custOrdItmDesc.getItemId(),new
									// BigDecimal(inventoryCheck.getInitiateLocId()),null);
									String delCity = custOrdFulDesc.getShipCity().toUpperCase();
									log.info("shipClassification from getOmsShipClassification " + shipClassification);
									if (promiseDateFlag == 0) {
										BigDecimal locId = BigDecimal.ZERO;
										// log
										// ||shipClassification.equalsIgnoreCase("BIG")
										log.info("PromiseDateFlag 0 :" + delCity + " : " + delCity.equalsIgnoreCase("PREORDER") + " : " + shipClassification.equalsIgnoreCase("SMALL"));
										// if ((!"PREORDER".equals(refValue)) &&
										// (shipClassification.equalsIgnoreCase("SMALL"))) {
										if (shipClassification.equalsIgnoreCase("SMALL")) {
											log.info("PromiseDateFlag 1");
											int maxPriority = getMaxpriorityFromFulFillMatrix(delCity, shipClassification, inventoryCheck.getInitiateLocId());
											if (String.valueOf(inventoryCheck.getInitiateLocId()).length() == 4) {
												log.info("PromiseDateFlag 2");
												locId = new BigDecimal(String.valueOf(inventoryCheck.getInitiateLocId()).substring(0, 2));
											} else {
												log.info("PromiseDateFlag 3");
												locId = new BigDecimal(inventoryCheck.getInitiateLocId());
											}
											log.info("locId " + locId);
											if ("Y".equals(custOrdFulDesc.getExpressDelv())) {
												SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
												Calendar date = Calendar.getInstance();
												Calendar cutOffTime = (Calendar) date.clone();
												cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
												cutOffTime.set(Calendar.MINUTE, 0);
												cutOffTime.set(Calendar.SECOND, 0);
												cutOffTime.set(Calendar.MILLISECOND, 0);
												if (!date.before(cutOffTime)) {
													date.add(Calendar.DATE, 1);
												}
												promiseDate = sdf.format(date.getTime());
											} else if ("Y".equalsIgnoreCase(ShipIndicator)) {
												promiseDate = getPromiseDateForPREORDERSMALL(refValue, shipClassification, locId, itemSku);
											} else {
												promiseDate = getDeliveryLeadTimeForSMALLorBIG(delCity, shipClassification, locId, maxPriority);
											}
										}
										log.info("PromiseDateFlag 5");
										promiseDateFlag++;
									}
								} catch (Exception e) {
									custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_113");
									log.error(" Error -- ", e);
									List<CustOrdFulDescResponse> custOrdFulDescResponseList1 = inventoryCheckResponse.getCustOrdFulDescResponse();
									custOrdFulDescResponseList1.add(custOrdFulDescResponse);
									log.info(" returning inventoryCheckResponse ");
									return inventoryCheckResponse;
								}

								log.info("PromiseDateFlag 6");
								combID = oMSUtilCommons.processFulfillmentMatrixGetCombID(new BigDecimal(inventoryCheck.getInitiateLocId()), shipClassification,
										inventoryCheck.getCustOrdFulDesc().get(0).getShipCity().toUpperCase(), custOrdFulDesc.getDeliveryType(), deliverZone, marketPlaceInd, applicationId,
										shipToStore);
								log.info("PromiseDateFlag 7");
							} else {
								log.info("Condition : Customer pick up");
								// Find the combination id from fulfillmentMatrix
								BigDecimal locId = null;
								if (String.valueOf(inventoryCheck.getInitiateLocId()).length() == 4) {
									locId = new BigDecimal(String.valueOf(inventoryCheck.getInitiateLocId()).substring(0, 2));
								} else {
									locId = new BigDecimal(inventoryCheck.getInitiateLocId());
								}
								String delCity = custOrdFulDesc.getShipCity().toUpperCase();
								int maxPriority = getMaxpriorityFromFulFillMatrix(delCity, shipClassification, locId.longValue());
								promiseDate = getDeliveryLeadTimeForSMALLorBIG(delCity, shipClassification, locId, maxPriority);
								log.info("promiseDate :" + promiseDate);
								combID = oMSUtilCommons.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(custOrdFulDesc.getPickLoc()), shipClassification, custOrdFulDesc.getDeliveryType(),
										deliverZone, marketPlaceInd, applicationId, shipToStore);
							}
						} catch (Exception e) {
							log.info("Combination id is not available");
							custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_102");
							List<CustOrdFulDescResponse> custOrdFulDescResponseList1 = inventoryCheckResponse.getCustOrdFulDescResponse();
							custOrdFulDescResponseList1.add(custOrdFulDescResponse);
							log.info(" returning inventoryCheckResponse ");
							return inventoryCheckResponse;
						}
						int j = 1;
						List<OmsFulfillMatrixExtDetail> list = session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
						int recordCount = 0;
						long otherLocQty = 0;
						long masterLocQty = 0;
						long masterTHQty = 0;
						while (recordCount <= list.size()) {
							int whThrsldQty = 0;
							int stThrsldQty = 0;
							log.info("------------------Iteration=" + j + "with qty fulfilled till now is " + L_Cum_Ord_Qty + "-----------");
							j++;
							recordCount++;
							OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
							try {
								fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrix(combID, priority);
							} catch (Exception e) {
								log.info("Inside catch will find back order back order");
								// Check for back order
								// if (L_Cum_Ord_Qty < custOrdItmDesc.getRequestedQty().longValue())
								// {
								log.info("isPOAvailable=" + isPOAvailable);
								if (isPOAvailable == false) {
									log.info("Finding backorder with qty" + L_Pending_Qty);
									List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
									BackOrderResponse backOrderResponse = interfacePersistence.backOrder(omsFulfillMatrixExtDetailList, custOrdItmDesc, L_Pending_Qty, shipClassification, countryCode);
									log.info("got response from back order");
									if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
										log.info("backOrderResponse is not null");
										custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);
										log.info("L_Pending_Qty " + L_Pending_Qty);
										log.info("backOrderResponse.getFutureAvlQty() " + backOrderResponse.getFutureAvlQty());
										log.info("Setting fut qty to " + backOrderResponse.getFutureAvlQty());
										if (futureLocMap.get(custOrdItmDesc.getItemId()) != null) {
											custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(futureLocMap.get(custOrdItmDesc.getItemId()), 0)));
											if (futureLocMap.get(custOrdItmDesc.getItemId()) - L_Pending_Qty < 0) {
												futureLocMap.put(custOrdItmDesc.getItemId(), 0L);
											} else {
												futureLocMap.put(custOrdItmDesc.getItemId(), futureLocMap.get(custOrdItmDesc.getItemId()) - L_Pending_Qty);
											}
											log.info("Adding in futureLocMap for item 1 in if" + custOrdItmDesc.getItemId() + "with qty" + futureLocMap.get(custOrdItmDesc.getItemId()));
										} else {
											custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(backOrderResponse.getFutureAvlQty(), 0)));
											if (backOrderResponse.getFutureAvlQty() - L_Pending_Qty < 0) {
												futureLocMap.put(custOrdItmDesc.getItemId(), 0L);
											} else {
												futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty() - L_Pending_Qty);
											}
											log.info("Adding in futureLocMap for item 1 in else" + custOrdItmDesc.getItemId() + "with qty" + futureLocMap.get(custOrdItmDesc.getItemId()));
										}
									}
								}
								break;
							}
							nextLoc = fulfillMatrixExtDetailResult.getLocation();
							try {
								if (itemQtyMap.containsKey(custOrdItmDesc.getItemId())) {
									log.info("Item " + custOrdItmDesc.getItemId() + " already exist");
									int nextLocExist = 0;
									log.info("" + itemQtyMap.get(custOrdItmDesc.getItemId()).size());
									Map<BigDecimal, Long> temp = itemQtyMap.get(custOrdItmDesc.getItemId());
									log.info("---" + temp.keySet());
									if (temp.containsKey(nextLoc)) {
										SOH = temp.get(nextLoc);
										log.info("location " + nextLoc + " exist with SOH=" + SOH);
										nextLocExist = 1;
										if (nextLoc.intValue() < 0) {
											nextLocExist = 0;
											// custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
										}
										// break;
									}
									if (nextLocExist == 1) {
										// SOH = itemQtyMap.get(custOrdItmDesc.getItemId()).get(nextLoc);
									} else {
										log.info("Item exist but finding the location witn next location " + nextLoc);
										if (fulfillMatrixExtDetailResult.getLocationType().equals("ST")) {
											SOH = interfacePersistence.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc);

											if (shipClassification.equalsIgnoreCase("SMALL")) {
												stThrsldQty = interfacePersistence.stThrsldQty(countryCode + "_ST_POS_SMALL");
											} else {
												stThrsldQty = interfacePersistence.stThrsldQty(countryCode + "_ST_POS_BIG");
											}
											log.info("SOH - " + SOH);
										} else if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {

											if (shipClassification.equalsIgnoreCase("SMALL")) {
												whThrsldQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_SMALL", countryCode + "_WH_SMALL_POS_THRESHOLD"));
											} else {
												whThrsldQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_BIG", countryCode + "_WH_BIG_POS_THRESHOLD"));
											}
											SOH = interfacePersistence.findWHInventory(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getLocation());
										} else if (fulfillMatrixExtDetailResult.getLocationType().equals("SU")) {
											isPOAvailable = true;
											String item_status = findNextfulfillLoc.findItemStatus(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
											if (item_status.equals("A") == false) {
												throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
											}
											log.info("Before going for PO checking back order");
											log.info("Finding backorder with qty" + L_Pending_Qty);
											List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
											BackOrderResponse backOrderResponse = null;
											log.info("^^^^^^^^^^^^^^^^^^" + futureLocMap.get(custOrdItmDesc.getItemId()));
											if (futureLocMap.get(custOrdItmDesc.getItemId()) == null) {
												backOrderResponse = interfacePersistence.backOrder(omsFulfillMatrixExtDetailList, custOrdItmDesc, L_Pending_Qty, shipClassification, countryCode);
												log.info("got back order responmse");
												log.info("backOrderResponse.getFutureAvlQty()=" + backOrderResponse.getFutureAvlQty());
												if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
													log.info("backOrderResponse is not null");
													if (backOrderResponse.getFutureAvlQty() > 0) {
														custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);
														log.info("L_Pending_Qty " + L_Pending_Qty);
														log.info("backOrderResponse.getFutureAvlQty() " + backOrderResponse.getFutureAvlQty());
														log.info("Setting fut qty to " + backOrderResponse.getFutureAvlQty());
														custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(backOrderResponse.getFutureAvlQty(), 0)));
													}
													log.info("L_Pending_Qty " + L_Pending_Qty);
													log.info("backOrderResponse.getFutureAvlQty() " + backOrderResponse.getFutureAvlQty());
													futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty() - L_Pending_Qty);
													log.info("Adding in futureLocMap for item 2" + custOrdItmDesc.getItemId() + "with qty" + (backOrderResponse.getFutureAvlQty() - L_Pending_Qty));
												}
												if (L_Pending_Qty >= backOrderResponse.getFutureAvlQty()) {
													// PO condition
													custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(L_Pending_Qty, 0)));
												}
											} else {
												long avaialbleFutureQty = futureLocMap.get(custOrdItmDesc.getItemId());
												log.info("avaialbleFutureQty=" + avaialbleFutureQty + "L_Pending_Qty=" + L_Pending_Qty);
												if (L_Pending_Qty > avaialbleFutureQty) {
													log.info("PO condition");
													custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(L_Pending_Qty, 0)));
													futureLocMap.put(custOrdItmDesc.getItemId(), L_Pending_Qty);
													log.info("Setting futureLocMap to" + L_Pending_Qty);
												} else {
													custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(avaialbleFutureQty, 0)));
													futureLocMap.put(custOrdItmDesc.getItemId(), futureLocMap.get(custOrdItmDesc.getItemId()) - L_Pending_Qty);
													log.info("Setting futureLocMap to" + (futureLocMap.get(custOrdItmDesc.getItemId()) - L_Pending_Qty));
												}
											}
										}
									}
								} else {
									log.info("Item does not exists");
									if (fulfillMatrixExtDetailResult.getLocationType().equals("ST")) {
										SOH = interfacePersistence.callSIMStoreInventory(custOrdItmDesc.getItemId(), nextLoc);

										if (shipClassification.equalsIgnoreCase("SMALL")) {
											stThrsldQty = interfacePersistence.stThrsldQty(countryCode + "_ST_POS_SMALL");
										} else {
											stThrsldQty = interfacePersistence.stThrsldQty(countryCode + "_ST_POS_BIG");
										}
										log.info("SOH - " + SOH);
									} else if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
										if (shipClassification.equalsIgnoreCase("SMALL")) {
											whThrsldQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_SMALL", countryCode + "_WH_SMALL_POS_THRESHOLD"));
										} else {
											whThrsldQty = Integer.parseInt(session.getOmsSystemParametersFindIndValue(countryCode + "_WH_POS_BIG", countryCode + "_WH_BIG_POS_THRESHOLD"));
										}
										SOH = interfacePersistence.findWHInventory(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getLocation());
									} else if (fulfillMatrixExtDetailResult.getLocationType().equals("SU")) {
										String item_status = findNextfulfillLoc.findItemStatus(custOrdItmDesc.getItemId(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
										if (item_status.equals("A") == false) {
											throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
										}
										log.info("Before going for PO checking back order");
										log.info("Finding backorder with qty" + L_Pending_Qty);
										List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combID);
										BackOrderResponse backOrderResponse = interfacePersistence.backOrder(omsFulfillMatrixExtDetailList, custOrdItmDesc, L_Pending_Qty, shipClassification,
												countryCode);
										log.info("got response from back order");
										log.info("backOrderResponse.getFutureAvlQty()=" + backOrderResponse.getFutureAvlQty());
										if (backOrderResponse != null && backOrderResponse.getFutureAvlQty() > 0) {
											log.info("backOrderResponse is not null");
											if (backOrderResponse.getFutInvAvlDate() != null) {
												custOrdItmDescResponse.setInTransitQty(BigDecimal.ONE);
												log.info("L_Pending_Qty " + L_Pending_Qty);
												log.info("backOrderResponse.getFutureAvlQty() " + backOrderResponse.getFutureAvlQty());
												log.info("Setting fut qty to " + backOrderResponse.getFutureAvlQty());
												custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(backOrderResponse.getFutureAvlQty(), 0)));
												futureLocMap.put(custOrdItmDesc.getItemId(), backOrderResponse.getFutureAvlQty() - custOrdItmDesc.getRequestedQty().longValue());
												log.info("Adding in futureLocMap for item 3" + custOrdItmDesc.getItemId() + "with qty"
														+ (backOrderResponse.getFutureAvlQty() - custOrdItmDesc.getRequestedQty().longValue()));
											}
											if (L_Pending_Qty > backOrderResponse.getFutureAvlQty()) {
												custOrdItmDescResponse.setFutAvlQuantity(new BigDecimal(Math.max(L_Pending_Qty, 0)));
											}
											if (futureLocMap.isEmpty() == false) {
												if (L_Pending_Qty > futureLocMap.get(custOrdItmDesc.getItemId())) {
													custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
												}
											}
										} else {
											custOrdItmDescResponse.setFutAvlQuantity(custOrdItmDesc.getRequestedQty());
										}
									}
								}
								log.info("count=" + count);
								if (count == 1) {
									log.info("+++++++++++++++++++Setting master loc to " + SOH);
									masterLocQty = SOH;
									masterTHQty = stThrsldQty + whThrsldQty;
									if (masterLocMap.get(custOrdItmDesc.getItemId()) == null) {
										masterLocMap.put(custOrdItmDesc.getItemId(), SOH);
									}
									count++;
								} else {
									otherLocQty = otherLocQty + (Math.max(SOH - whThrsldQty - stThrsldQty, 0));
								}
							} catch (Exception e) {
								log.error("Error=" + e);
							}
							log.info("L_Pending_Qty=" + L_Pending_Qty + "   L_Cum_Ord_Qty=" + L_Cum_Ord_Qty + "   SOH=" + SOH);
							Map<BigDecimal, Long> itemLocMap = new HashMap<BigDecimal, Long>();
							if (itemQtyMap.get(custOrdItmDesc.getItemId()) != null) {
								itemLocMap = itemQtyMap.get(custOrdItmDesc.getItemId());
							}
							log.info("itemLocMap.keySet=" + itemLocMap.keySet());
							if (SOH > L_Pending_Qty) {
								log.info("SOH >= L_Pending_Qty setting SOH to " + (SOH - custOrdItmDesc.getRequestedQty().longValue()) + "for location+" + nextLoc);
								itemLocMap.put(nextLoc, SOH - L_Pending_Qty);
								// itemSOH.setSoh(new
								// BigDecimal(SOH-custOrdItmDesc.getRequestedQty().longValue()) );
							} else {
								itemLocMap.put(nextLoc, 0L);
							}
							itemQtyMap.put(custOrdItmDesc.getItemId(), itemLocMap);
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty);
							L_Pending_Qty = custOrdItmDesc.getRequestedQty().longValue() - L_Cum_Ord_Qty;
							priority++;
							log.info("***!");
							custOrdItmDescResponse.setItemId(custOrdItmDesc.getItemId());
							if (custOrdFulDesc.getDeliveryType().equals("S")) {
								custOrdItmDescResponse.setLoc(inventoryCheck.getInitiateLocId());
							} else {
								custOrdItmDescResponse.setLoc(custOrdFulDesc.getPickLoc());
							}
							log.info("setting response");
							custOrdItmDescResponse.setLineNo(custOrdItmDesc.getLineNo());
							custOrdItmDescResponse.setRequestedQty(custOrdItmDesc.getRequestedQty());
							custOrdItmDescResponse.setPromiseDeliveryDate(promiseDate);
							if (nextLoc.intValue() > 0) {
								custOrdItmDescResponse.setAvailableQty(new BigDecimal(Math.max(otherLocQty, 0)));
							}
							log.info("Setting available qty to " + otherLocQty + "for item " + custOrdItmDesc.getItemId() + "line no " + custOrdItmDesc.getLineNo());
							try {
								log.info("Stock on hand " + masterLocQty + "for item " + custOrdItmDesc.getItemId() + "line no " + custOrdItmDesc.getLineNo());
								custOrdItmDescResponse.setStockOnHandQty(new BigDecimal(Math.max(masterLocQty - masterTHQty, 0)));
							} catch (Exception e) {
								log.info("faied in finding soh qty");
								custOrdItmDescResponse.setStockOnHandQty(BigDecimal.ZERO);
							}
						} // while loop ending
						custOrdItmDescResponseList.add(custOrdItmDescResponse);
					} else {
						custOrdItmDescResponse.setItemId(custOrdItmDesc.getItemId());
						custOrdItmDescResponse.setLineNo(custOrdItmDesc.getLineNo());
						custOrdItmDescResponse.setRequestedQty(custOrdItmDesc.getRequestedQty());
						custOrdItmDescResponse.setAvailableQty(BigDecimal.ZERO);
						custOrdItmDescResponse.setStockOnHandQty(custOrdItmDesc.getRequestedQty());
						custOrdFulDescResponse.setPickLoc(inventoryCheck.getInitiateLocId());
						custOrdItmDescResponseList.add(custOrdItmDescResponse);
					}
				} catch (Exception e) {
					log.error("Error", e);
					custOrdFulDescResponse.setErrorMessage("OMS_ORPOS_ERROR_107");
				}
			} // end of item loop
			custOrdFulDescResponseList.add(custOrdFulDescResponse);
		} // end of fulfillmetn detail loop
		return inventoryCheckResponse;
	}

	private String getOmsShipClassification(String item, Long requestorId) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String shipClassification = null;
		String query = " SELECT GET_CLASSIFICATION_BO (?, ?) SHIP FROM DUAL ";
		log.info("query shipClassification " + query);
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item);
			pstmt.setLong(2, requestorId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString("ship") != null) {
					shipClassification = rs.getString("ship");
				}
			}
			log.info("shipClassification " + shipClassification);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for shipClassification " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				log.warn("Error while resultset...");
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while statement...");
			}
			try {
				con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection...");
			}
		}
		return shipClassification;
	}

	private String findWallBracketSku(String item) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		String query = "SELECT ITEM FROM XX_WALL_BRACKET_SKU WHERE ITEM = ?";
		log.info("Query for WALL_BRACKET: " + query);

		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, item);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String fetchedItem = rs.getString("ITEM");
				if (fetchedItem != null && !fetchedItem.trim().isEmpty()) {
					result = fetchedItem;
				}
			}
			log.info("Result from WALL_BRACKET query: " + result);

		} catch (Exception e) {
			log.error("Exception while establishing the connection for shipClassification: " + e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				log.warn("Error while closing result set: " + e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while closing statement: " + e.getMessage());
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection: " + e.getMessage());
			}
		}

		return result;
	}

	private String getDeliveryLeadTimeForSMALLorBIG(String city, String classification, BigDecimal requestorId, int maxpriority) throws SOAPException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int leadTime = 0;
		String addedTime = null;
		String query = "select * from OMS_CUST_ORDER_DLT where city=? and classification=? and REQUESTOR_ID=? and PRIORITY=? AND STATUS = 'A'";
		log.info("query " + query);
		log.info("city " + city);
		log.info("classification " + classification);
		log.info("initiate_loc_id " + requestorId);
		log.info("maxpriority " + maxpriority);

		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city);
			pstmt.setString(2, classification);
			pstmt.setBigDecimal(3, requestorId); // this initiate_loc_id from soap
			pstmt.setInt(4, maxpriority);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getBigDecimal("DELV_LEAD_TIME") != null) {
					leadTime = rs.getBigDecimal("DELV_LEAD_TIME").intValue();
					log.info("lead time from DB " + leadTime);
				} else {
					leadTime = 5;
					log.info("lead time from DB is null setting it to " + leadTime);
				}
			} else {
				leadTime = 5;
				log.info("lead time no value in db. setting it to default " + leadTime);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cutOffTime = Calendar.getInstance();
			cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
			cutOffTime.set(Calendar.MINUTE, 0);
			cutOffTime.set(Calendar.SECOND, 0);
			cutOffTime.set(Calendar.MILLISECOND, 0);

			Calendar c = Calendar.getInstance();
			if (c.after(cutOffTime)) {
				++leadTime;
			}
			c.add(Calendar.DATE, leadTime);
			int fromDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			int toDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (leadTime >= 7 || fromDayOfWeek == Calendar.FRIDAY || Calendar.FRIDAY == toDayOfWeek || (fromDayOfWeek < Calendar.FRIDAY && Calendar.FRIDAY < toDayOfWeek)
					|| (fromDayOfWeek < Calendar.FRIDAY && toDayOfWeek < fromDayOfWeek)) {
				log.info("friday coming in between toady and lead date...");
				c.add(Calendar.DATE, 1);
			}
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
				log.info("lead time is again comes on friday after adjustment, add one more day");
				c.add(Calendar.DATE, 1);
			}
			addedTime = sdf.format(c.getTime());
			log.info("lead time after add with system date " + addedTime);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for deliveryLeadTime " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				log.warn("Error while resultset...");
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while statement...");
			}
			try {
				con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection...");
			}
		}
		return addedTime;
	}

	private String getPromiseDateForPREORDERSMALL(String city, String classification, BigDecimal requestorId, String itemSku) throws SOAPException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Date prom_date = null;
		String addedTime = null;
		String query = " select DELV_DATE from OMS_CUST_ORDER_DELV_DATE where upper(city)=? and upper(classification)=? and REQUESTOR_ID=? and ITEM =? AND STATUS = 'A'";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city.toUpperCase());
			pstmt.setString(2, classification.toUpperCase());
			pstmt.setBigDecimal(3, requestorId);
			pstmt.setString(4, itemSku);
			rs = pstmt.executeQuery();
			log.info("inside getPromiseDateForPREORDERSMALL" + ":" + city + ":" + classification + ":" + requestorId + ":" + itemSku);
			if (rs.next()) {

				Calendar cutOffTime = Calendar.getInstance();
				cutOffTime.set(Calendar.HOUR_OF_DAY, 14);
				cutOffTime.set(Calendar.MINUTE, 0);
				cutOffTime.set(Calendar.SECOND, 0);
				cutOffTime.set(Calendar.MILLISECOND, 0);

				prom_date = rs.getDate("DELV_DATE");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTime(prom_date);

				// c.add(Calendar.DATE,leadTime);
				addedTime = sdf.format(c.getTime());
			}
			log.info("deliveryDate " + prom_date + ":" + addedTime);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for preordersmall " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				log.warn("Error while resultset...");
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while statement...");
			}
			try {
				con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection...");
			}
		}
		return addedTime;
	}

	private int getMaxpriorityFromFulFillMatrix(String city, String classification, long loc_id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int max_priority = 0;
		String query = "select max(PRIORITY) from oms_fulfill_matrix_ext_detail where COMBINATION_ID in(select COMBINATION_ID from oms_fulfill_matrix_ext_head where CUSTOMER_CITY=? and SHIP_CLASSIFICATION=? and REQUESTOR_ID=?) ";
		try {
			con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, city);
			pstmt.setString(2, classification);
			pstmt.setLong(3, loc_id);
			rs = pstmt.executeQuery();
			// log
			log.info("Inside getMaxpriorityFromFulFillMatrix");
			if (rs.next()) {
				if (rs.getBigDecimal("max(PRIORITY)") != null) {
					max_priority = rs.getBigDecimal("max(PRIORITY)").intValue();
				}
			}
			if (max_priority == 0) {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (SQLException e) {
					log.warn("Error while resultset...");
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					log.warn("Error while statement...");
				}
				query = "select max(PRIORITY) from oms_fulfill_matrix_ext_detail where COMBINATION_ID in(select COMBINATION_ID from oms_fulfill_matrix_ext_head where CUSTOMER_CITY='ALL' and SHIP_CLASSIFICATION=? and REQUESTOR_ID=?) ";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, classification);
				pstmt.setLong(2, loc_id);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					if (rs.getBigDecimal("max(PRIORITY)") != null) {
						max_priority = rs.getBigDecimal("max(PRIORITY)").intValue();
					}
				}
			}
			log.info("Max priority " + max_priority);
		} catch (Exception e) {
			log.info("Exception while estabilishing the connection for Max priority " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				log.warn("Error while resultset...");
			}
			try {
				pstmt.close();
			} catch (SQLException e) {
				log.warn("Error while statement...");
			}
			try {
				con.close();
			} catch (SQLException e) {
				log.warn("Error while closing connection...");
			}
		}
		return max_priority;
	}

	public BigDecimal getAvailableQtyFromOmsBackOrderDtl(BigDecimal location, String item, long lineNo) throws SOAPException {
		BigDecimal qty = BigDecimal.ZERO;
		BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();
		try {
			qty = backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(location, item);
			if (qty == null) {
				qty = BigDecimal.ZERO;
			}
		} catch (Exception e) {
			qty = BigDecimal.ZERO;
		}
		log.info(" BO allocated inventory for the location" + location + " item " + item + " and lineNo " + lineNo + " is " + qty);
		return qty;
	}
}
