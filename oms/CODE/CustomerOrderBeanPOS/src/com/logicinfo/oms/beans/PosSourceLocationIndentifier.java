package com.logicinfo.oms.beans;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsOrposCustOrdItm;
import com.logicinfo.oms.ejb.OmsOrposCustOrderHead;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.PreOrderItemLocationSync;
import com.oracle.retail.integration.base.bo.custorderdesc.v1.CustOrderDesc;
import com.oracle.retail.integration.base.bo.custorditmdesc.v1.CustOrdItmDesc;
import com.oracle.retail.integration.base.bo.discntlinedesc.v1.DiscntLineDesc;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException;

public class PosSourceLocationIndentifier {
	public PosSourceLocationIndentifier() {
		super();
	}

	private final static Logger log = Logger.getLogger(com.logicinfo.oms.beans.PosSourceLocationIndentifier.class.getName());
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;
	Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = null;
	Map<String, FulfillOrdandSourceLocPOJO> itemCounterMap = new ConcurrentHashMap<String, FulfillOrdandSourceLocPOJO>();
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treeMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
	Map<String, BigDecimal> storeItemMap = new HashMap<String, BigDecimal>();
	BigDecimal omsCustOrdNo;
	int nonInvenandShipping = 0;
	int invalidItem = 0;
	boolean status = false;
	BigDecimal combId = BigDecimal.ZERO;
	BigDecimal sourceLocId = BigDecimal.ZERO;
	BigDecimal fulfillLocId = BigDecimal.ZERO;
	BigDecimal virtualWH = BigDecimal.ZERO;
	String deliveryLocType = null;
	String sourceLocType = null;
	BigDecimal storeSOH = BigDecimal.ZERO;
	InterfacePersistence interfacePersistece = new InterfacePersistence();
	ArrayList<String> errorcodes = new ArrayList<String>();

	Map<String, Integer> whItemStockMap = new HashMap<String, Integer>();
	Map<Long, BigDecimal> phyWhMap = new HashMap<Long, BigDecimal>();

	public Map<BigDecimal, ArrayList<OmsTempCoFo>> processSplitOrders(BigDecimal omsCustOrdNo, CustOrderDesc custOrderDesc, BigDecimal omsOrposCustOrderId)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		this.omsCustOrdNo = omsCustOrdNo;
		log.info("omsCustOrdNo " + omsCustOrdNo + "processSplitOrders starts ");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		int maxFulfillOrderNo = oMSUtilCommons.returnMaxFulFilOrdNoECOM(custOrderDesc.getCustomerOrderId());
		log.info("omsCustOrdNo " + omsCustOrdNo + " maxFulfillOrderNo " + maxFulfillOrderNo);
		;
		FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
		sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		log.info("omsCustOrdNo " + omsCustOrdNo + "log version for omsCustOrdNo" + omsCustOrdNo);
		// added code for duplicate items - fulfillment logic
		Map<String, CustOrdItmDesc> groupedItemMap = groupItem(omsCustOrdNo, custOrderDesc);
		OmsPersistence omsPersistence1 = new OmsPersistence();
		Map<String, String> itemPreOrdIndMap = omsPersistence1.persistOmsCustOrdItem(custOrderDesc, groupedItemMap);
//		omsPersistence1.persistOmsCustOrdItem(custOrderDesc, groupedItemMap);
		List<OmsCustOrdItem> omsCustOrdItem1 = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
		List<OmsCustOrdItem> omsCustOrdItem2 = session.getOmsCustOrdItemFindByOmsCustOrdNo(omsCustOrdNo);
		log.info("omsCustOrdNo " + omsCustOrdNo + " groupedItemMap.keySet() " + groupedItemMap.keySet());
		log.info("omsCustOrdNo " + omsCustOrdNo + " Before entring For each loop groupedItemMap.size() " + groupedItemMap.size());
		int itemLevel = 0;
		for (String groupItemKey : groupedItemMap.keySet()) {
			CustOrdItmDesc custOrdItmDesc = groupedItemMap.get(groupItemKey);
			log.info("omsCustOrdNo " + omsCustOrdNo + "groupedItemMap.size() " + groupedItemMap.size());
			itemLevel++;
			log.info("omsCustOrdNo " + omsCustOrdNo + "itemLevel " + itemLevel);
			OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(), new BigDecimal(custOrdItmDesc.getLineItemNo()));
			String shipClassifcation = omsCustOrdItem.getShipClassification();
			long L_Cum_Ord_Qty = 0L;
			long L_Pending_Qty = 0L;
			if (custOrdItmDesc.getShippingChargeFlag().value().equals("Y") == false) {
				int priority = 1;
				// code for Shipping Charges
				log.info("omsCustOrdNo " + omsCustOrdNo + "Code for shipping charges starts");
				String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
				// Fetching the item department based on the input item sent from the request
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item : " + custOrdItmDesc.getItemId());
				BigDecimal itemDept = null;
				try {
					itemDept = session.getItemMasterFindDept(custOrdItmDesc.getItemId());
					log.info("omsCustOrdNo " + omsCustOrdNo + "item dept is set " + itemDept);
				} catch (Exception e) {
					invalidItem++;
					if (invalidItem == omsCustOrdItem1.size()) {
						custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_107"); // invalid item
						log.info("omsCustOrdNo " + omsCustOrdNo + "invalidItem " + invalidItem);
						log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdItem1.size() " + omsCustOrdItem1.size());
						fulfillDetailMap = null;
						sohMap = null;
						break;
					}
				}
				String inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItmDesc.getItemId(), itemDept);
				log.info("omsCustOrdNo " + omsCustOrdNo + " shipingChargeDept= " + shipingChargeDept + " inventoryIndn= " + inventoryIndn);
				if (shipingChargeDept.equals(itemDept.toString()) == true || inventoryIndn.equals("N")) {
					nonInvenandShipping++;
					if (nonInvenandShipping == omsCustOrdItem2.size()) {

						log.info("omsCustOrdNo " + omsCustOrdNo + "setting error code for OMS_ORPOS_ERROR_112");
						custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_112"); // shipping charge & non-inventory
						log.info("omsCustOrdNo " + omsCustOrdNo + "nonInvenandShipping " + nonInvenandShipping);
						log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdItem2.size() " + omsCustOrdItem2.size());
						fulfillDetailMap = null;
						sohMap = null;
						break;
					}

				}

				// code for Shipping Charges - Checking for the department based on the item

				log.info("omsCustOrdNo " + omsCustOrdNo + " shipClassifcation=" + shipClassifcation);
				if (shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y")) {

					log.info("omsCustOrdNo " + omsCustOrdNo + "Shipping loop started");
					log.info("omsCustOrdNo " + omsCustOrdNo + "item:" + custOrdItmDesc.getItemId());

					// find combination Id
					String shipClz = "Y".equals(itemPreOrdIndMap.get(custOrdItmDesc.getItemId())) ? ("PRE ORDER " + shipClassifcation) : shipClassifcation;
					try {
						combId = findCombinationId(custOrderDesc, shipClz);
					} catch (Exception e) {
						combId = findCombinationId(custOrderDesc, shipClz);
						log.info("Exception occured while fetch the combination Id ");
						if (errorcodes.size() > 0) {
							break;
						}
					}

					while (L_Cum_Ord_Qty < custOrdItmDesc.getQuantity().longValue()) {
						try {
							fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrix(combId, priority);
							sourceLocId = fulfillMatrixExtDetailResult.getLocation();
							deliveryLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
							sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
							fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
							String deliveryType = custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value();
							String shipToStore = custOrderDesc.getShipToStore();
							int availableQty = 0;
							boolean allowFlow = ("C".equals(deliveryType) && "Y".equals(shipToStore)) || ("S".equals(deliveryType) && "N".equals(shipToStore))
									|| ("C".equals(deliveryType) && "N".equals(shipToStore) && sourceLocId != null && custOrderDesc.getPickLoc() == sourceLocId.longValue());

							log.info("omsCustOrdNo " + omsCustOrdNo + " sourceLocId:" + sourceLocId + " sourceLocType= " + sourceLocType + " fulfillLocId= " + fulfillLocId + " deliveryLocType= "
									+ deliveryLocType);
							if (allowFlow) {
								if (sourceLocType.equals("ST")) {
									availableQty = getStoreSOH(custOrdItmDesc, custOrderDesc, L_Cum_Ord_Qty, L_Pending_Qty, omsOrposCustOrderId);
									virtualWH = BigDecimal.ZERO;
								}
								if (sourceLocType.equals("WH")) {
									virtualWH = sourceLocId;
									if (priority == 1) {
										L_Pending_Qty = custOrdItmDesc.getQuantity().longValue();
									}
									availableQty = getWHSOH(custOrdItmDesc, L_Pending_Qty, sourceLocType, deliveryLocType, custOrderDesc.getInitiateLocId());
									log.info("omsCustOrdNo " + omsCustOrdNo + " virtualWH in WH " + virtualWH);
								}
								if (sourceLocType.equals("SU") || sourceLocId.intValue() < 0) {
									if (priority == 1) {
										L_Pending_Qty = custOrdItmDesc.getQuantity().longValue();
									}
									int SOH = 0;
									BackOrderFulfillDetail backOrderFulfillDetail = processBackOrderItem(custOrderDesc, custOrdItmDesc, new BigDecimal(L_Pending_Qty), omsCustOrdNo,
											omsOrposCustOrderId);
									SOH = backOrderFulfillDetail.getSOH();
									log.info("omsCustOrdNo" + omsCustOrdNo + " qty fulfilled from BO= " + SOH);
									L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
									log.info("omsCustOrdNo" + omsCustOrdNo + " = " + SOH);
									if (L_Cum_Ord_Qty != custOrdItmDesc.getQuantity().intValue()) {
										availableQty = processPO(custOrdItmDesc, custOrderDesc, L_Pending_Qty, SOH);
									}
									virtualWH = BigDecimal.ZERO;
								}
							}
							if (availableQty > 0) {
								OmsTempCoFo omsTempCoFo = null;
								// create a list and add to sohMap - "W".equals(deliveryLocType) ?
								// fulfillMatrixExtDetailResult.getLocation() :
								if ("N".equals(custOrderDesc.getShipToStore())) {
									omsTempCoFo = createFulfilMapObject(omsCustOrdNo, custOrdItmDesc.getItemId(),
											"W".equals(deliveryLocType) ? fulfillMatrixExtDetailResult.getLocation() : sourceLocId, sourceLocType, fulfillLocId, deliveryLocType, availableQty,
											new BigDecimal(custOrdItmDesc.getLineItemNo()), combId, maxFulfillOrderNo, virtualWH);

								} else {
									omsTempCoFo = createFulfilMapObject(omsCustOrdNo, custOrdItmDesc.getItemId(),
											"S".equals(deliveryLocType) ? fulfillMatrixExtDetailResult.getLocation() : sourceLocId, sourceLocType, fulfillLocId, deliveryLocType, availableQty,
											new BigDecimal(custOrdItmDesc.getLineItemNo()), combId, maxFulfillOrderNo, virtualWH);
								}
								log.info("omsCustOrdNo " + omsCustOrdNo + "VirtualWH " + omsTempCoFo.getVirtualWH());
								log.info("omsCustOrdNo " + omsCustOrdNo + "putting into sohMap for lineNo " + omsTempCoFo.getLineNo() + "FulFilOrderNo is " + omsTempCoFo.getFulfillOrderNo());
								if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null || sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
									ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
									tempList.add(omsTempCoFo);
									sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
								} else {
									ArrayList<OmsTempCoFo> existingList = sohMap.get(omsTempCoFo.getFulfillOrderNo());
									existingList.add(omsTempCoFo);
									sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
								}
							} else {
								availableQty = 0;
							}
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + availableQty;
							L_Pending_Qty = custOrdItmDesc.getQuantity().intValue() - L_Cum_Ord_Qty;
							log.info("omsCustOrdNo " + omsCustOrdNo + " ordered qty for lineNo " + custOrdItmDesc.getLineItemNo() + "and item" + custOrdItmDesc.getItemId() + "is"
									+ custOrdItmDesc.getQuantity());
							log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Cum_Ord_Qty for lineNo " + custOrdItmDesc.getLineItemNo() + "and item" + custOrdItmDesc.getItemId() + "is"
									+ L_Cum_Ord_Qty);
							log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Pending_Qty for lineNo " + custOrdItmDesc.getLineItemNo() + "and item" + custOrdItmDesc.getItemId() + "is"
									+ L_Pending_Qty);
							priority++;

						} // end of try
						catch (Exception e) {
							int qty = 0;
							log.info("omsCustOrdNo " + omsCustOrdNo + " omsOrposCustOrderId " + omsOrposCustOrderId + "inside catch block ");
							BackOrderFulfillDetail backOrderFulfillDetail = new BackOrderFulfillDetail();
							log.info("----------------> The BackOrderFulfillDetail Address ------------------->" + backOrderFulfillDetail + "<-------------->");
							log.info("custOrderDesc.getOrderDesc() " + custOrderDesc.getOrderDesc());
							if (errorcodes.size() == 0) {
								qty = processBackOrderItem(custOrderDesc, custOrdItmDesc, new BigDecimal(L_Pending_Qty), omsCustOrdNo, omsOrposCustOrderId).getSOH();
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + " over all ordered qty in backOrder for lineNo " + custOrdItmDesc.getLineItemNo() + " is" + qty);
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + qty;
							L_Pending_Qty = custOrdItmDesc.getQuantity().intValue() - L_Cum_Ord_Qty;
							log.info("omsCustOrdNo " + omsCustOrdNo + " over all L_Cum_Ord_Qty for lineNo " + custOrdItmDesc.getLineItemNo() + "is " + L_Cum_Ord_Qty);
							log.info("omsCustOrdNo " + omsCustOrdNo + " over all L_Pending_Qty for lineNo " + custOrdItmDesc.getLineItemNo() + "is " + L_Pending_Qty);
							if (L_Pending_Qty > 0) {
								log.error("omsCustOrdNo " + omsCustOrdNo + "failed in source loc in finding comb_id" + e);
								log.info("omsCustOrdNo " + omsCustOrdNo + custOrderDesc.getOrderDesc());
								log.info("omsCustOrdNo " + omsCustOrdNo + "combId " + combId);
								if (combId.intValue() == 0) {
									log.info("omsCustOrdNo " + omsCustOrdNo + " setting the error code for OMS_ORPOS_ERROR_102 when combination id not available");
									custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_102"); // failed in comb_id
								} else if (errorcodes.size() == 0) {
									log.info("omsCustOrdNo " + omsCustOrdNo + " setting the error code for OMS_ORPOS_ERROR_103 when inventory is not available");
									custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_103");// inventory unavailble
								}
								log.info("omsCustOrdNo " + omsCustOrdNo + custOrderDesc.getOrderDesc());
								OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
								log.info("omsCustOrdNo " + omsCustOrdNo + "retrived status value from omsOrposCustOrderHead " + omsOrposCustOrderHead.getStatus());
								omsOrposCustOrderHead.setStatus("F");
								session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(), new BigDecimal(custOrdItmDesc.getLineItemNo()));
								log.info("omsCustOrdNo " + omsCustOrdNo + "==========================Cancelled Quantity in OMS table=============================== "
										+ omsCustOrdItem.getQtyOrderedSuom());
								omsCustOrdItem.setQtyCancelled(omsCustOrdItem.getQtyOrderedSuom());
								omsCustOrdItem.setStatus("F");
								session.mergeOmsCustOrdItem(omsCustOrdItem);
								OmsOrposCustOrdItm omsOrposCustOrdItm = session.getOmsOrposCustOrdItmFindByOmsOrposCustOrdIdAndItem(omsOrposCustOrderId, custOrdItmDesc.getItemId(),
										new BigDecimal(custOrdItmDesc.getLineItemNo()));
								log.info("omsCustOrdNo " + omsCustOrdNo + "==========================Cancelled Quantity in ORPOS table=============================== "
										+ omsCustOrdItem.getQtyOrderedSuom());
								omsOrposCustOrdItm.setCancelledQuantity(omsCustOrdItem.getQtyOrderedSuom());
								session.mergeOmsOrposCustOrdItm(omsOrposCustOrdItm);
								log.info("omsCustOrdNo " + omsCustOrdNo + "----");
								status = true;
							} // end of if block in catch
							break;
						} // end of catch block
					} // end of while

				} // shipingChargeDept.equals(itemDept.toString()) == false &&
					// inventoryIndn.equals("Y")
				if (status == true) {
					sohMap.clear();
					fulfillDetailMap.clear();
					OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
					omsOrposCustOrderHead.setStatus("F");
					session.mergeOmsOrposCustOrderHead(omsOrposCustOrderHead);
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					break;
				}
			} // end of if for shipping charge or non-inventory
		} // end of for
		log.info("omsCustOrdNo " + omsCustOrdNo + "reached end of for loop processing next Item");
		if (sohMap != null && sohMap.size() > 0 && sohMap.keySet() != null) {
			fulfillDetailMap = createFulfillDetailMap(omsCustOrdNo, sohMap, 1);
		}
		return fulfillDetailMap;

	}// end of method

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> createFulfillDetailMap(BigDecimal omsCustOrdNo, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap, int fulfillOrderNo) {
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside createFulfillDetailMap");
		for (BigDecimal key : sohMap.keySet()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());
			log.info("omsCustOrdNo " + omsCustOrdNo + "key=" + key);
			ArrayList<OmsTempCoFo> list = sohMap.get(key);
			log.info("omsCustOrdNo " + omsCustOrdNo + "------" + list.size());
			try {
				fulfillDetailMap.put(key, list);
			} catch (Exception e) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside catch block when creating fulfildetailMap");
			}
		}
		return fulfillDetailMap;
	}

	public BigDecimal findCombinationId(CustOrderDesc custOrderDesc, String shipClassifcation) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String refValue = null;
		String deliverZone = null;
		String marketPlaceInd = "N";
		String modeOfDelv = custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryType().value();
		String shipToStore = null;
		String applicationId = "ORPOS";
		FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
		shipToStore = custOrderDesc.getShipToStore();
		log.info("modeOfDelv- " + modeOfDelv + "shipToStore-" + shipToStore);
		if ("S".equals(modeOfDelv)) {
			refValue = session.getOmsReferenceDataFindRefValue("STATE_CITY_LINK", custOrderDesc.getInitiateCountryCode(),
					custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getDeliveryDestDtl().getGeoAddrDesc().getStateCode());
			// changed the code for Shipping Classification - instead of BIG (replaced with
			// Uda value)
			// the refValue is the state name returned from the OmsReferenceData table for
			// ISO code translation
			log.info("omsCustOrdNo " + omsCustOrdNo + "refValue " + refValue.toUpperCase());
			log.info("shipToStore -" + shipToStore);

//			if ("Y".equals(shipToStore)) {
//				modeOfDelv = "C";
//			} else {
//				modeOfDelv = "S";
//			}

			combId = findNextfulfillLoc.processFulfillmentMatrixGetCombID(new BigDecimal(custOrderDesc.getInitiateLocId()), shipClassifcation, refValue.toUpperCase(), modeOfDelv, deliverZone,
					marketPlaceInd, applicationId, shipToStore);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Combination Id " + combId);

		} else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Customer pick up");
			log.info("Finding combination for " + "reqId " + custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getFulfillLocId() + "itemType: " + shipClassifcation + "modeOfDelv: "
					+ modeOfDelv + "marketPlaceInd: " + marketPlaceInd + "shipToStore: " + shipToStore);

			// changed the code for Shipping Classification - instead of BIG (replaced with
			// Uda value)
			combId = findNextfulfillLoc.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(custOrderDesc.getInitiateLocId()), shipClassifcation, modeOfDelv, deliverZone, marketPlaceInd,
					applicationId, shipToStore);
			log.info("omsCustOrdNo " + omsCustOrdNo + " combId= " + combId);

		}
		return combId;
	}

	public int getStoreSOH(CustOrdItmDesc custOrdItmDesc, CustOrderDesc custOrderDesc, long L_Cum_Ord_Qty, long L_Pending_Qty, BigDecimal omsOrposCustOrderId)
			throws EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException, SOAPException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException

	{
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int availableQuantity = 0;

		String locKey = sourceLocType + "~" + sourceLocId + "~" + custOrdItmDesc.getItemId();

		try {
			log.info("============================================================================ line " + custOrdItmDesc.getLineItemNo());
			// added code for duplicate items - fulfillment logic
			log.info("storeItemMap.keySet()" + storeItemMap.keySet());
			log.info("itemand Loc" + locKey);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Started the code for Duplicate Items - fulfillment logic");
			log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for LineNo============= " + custOrdItmDesc.getLineItemNo());
			log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for Item " + custOrdItmDesc.getItemId());
			log.info("omsCustOrdNo" + omsCustOrdNo + "Ordered Qty " + custOrdItmDesc.getQuantity());
			log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
			log.info("omsCustOrdNo" + omsCustOrdNo + "pending qty  " + L_Pending_Qty);
			int requestQty = 0;
			long SOH = 0;
			// check for needed quantity to fulfill
			if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
				requestQty = custOrdItmDesc.getQuantity().intValue();
				log.info("requestQty when pending and l_cum_ord_qty " + requestQty);
			} else if (custOrdItmDesc.getQuantity().intValue() >= L_Cum_Ord_Qty) {
				requestQty = custOrdItmDesc.getQuantity().intValue() - (int) L_Cum_Ord_Qty;
				log.info("requestQty when custOrdItmDesc.getQuantity().intValue() " + requestQty);
			} else if (L_Cum_Ord_Qty >= custOrdItmDesc.getQuantity().intValue()) {
				requestQty = (int) L_Cum_Ord_Qty - custOrdItmDesc.getQuantity().intValue();
				log.info("requestQty when L_Cum_Ord_Qty>=custOrdItmDesc.getQuantity() " + requestQty);
			}
			log.info("============================================================================ line " + custOrdItmDesc.getLineItemNo());
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside containsKey condition for itemQtyMap for line No " + custOrdItmDesc.getLineItemNo());
			if (storeItemMap.containsKey(locKey))
			// if(itemQtyMap.get(custOrdItmDesc.getItemId()).getLocation().compareTo(nextLoc)==0)
			{
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside compareTo condition for item id");
				// SOH=itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh().longValue();
				SOH = storeItemMap.get(locKey).longValue();
				log.info("omsCustOrdNo " + omsCustOrdNo + "itemQtyMap already contain item with SOH=" + SOH);
				log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + custOrdItmDesc.getLineItemNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItmDesc.getItemId());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
				log.info("custOrdItems.getOrderQtySuom() " + custOrdItmDesc.getItemId());

				if (SOH >= requestQty) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "SOH >0 and locatin is " + sourceLocId);
					SOH = SOH - requestQty;
					availableQuantity = requestQty;
				} // end of inner if
				else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "SOH is less than quantity requested,finding in store" + sourceLocId);
					availableQuantity = (int) SOH;
					SOH = 0;
				}
				log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
				storeItemMap.put(locKey, new BigDecimal(SOH));
			} // end of major 1st if
			else {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not contain in itemQtyMap");
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				SOH = interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(), sourceLocId, omsCustOrdHead.getApplicationId());

				log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItmDesc.getItemId() + "sourceLocId " + sourceLocId + " SOH" + SOH);
				log.info("custOrdItems.getOrderQtySuom() " + custOrdItmDesc.getQuantity());
				log.info("requestQty " + requestQty);
				log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
				long L_Pending_Qty_temp = requestQty - L_Cum_Ord_Qty;
				log.info("L_Pending_Qty_temp " + L_Pending_Qty_temp);
				L_Pending_Qty = L_Pending_Qty_temp;
				log.info("SOH" + SOH);
				log.info("custOrdItmDesc.getQuantity() " + custOrdItmDesc.getQuantity());
				if (SOH >= requestQty) {
					log.info("inside SOH>=custOrdItmDesc.getQuantity().intValue() " + new BigDecimal(SOH - requestQty));
					availableQuantity = requestQty;
					SOH = SOH - requestQty;
				} else {
					log.info("inside else for SOH>=requestQty condition");
					availableQuantity = (int) SOH;
					SOH = 0;
				}
				log.info("storeSOH " + storeSOH);
				storeItemMap.put(locKey, new BigDecimal(SOH));
				log.info("after putting into the storeMap " + storeItemMap.keySet());
			}
		} catch (javax.xml.ws.WebServiceException f) {
			custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_111"); // RMS/SIM WS is down
			log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in calling SIM exception is  " + f);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			OmsOrposCustOrderHead omsOrposCustOrderHead = session.getOmsOrposCustOrderHeadFindByomsOrposCustOrderId(omsOrposCustOrderId);
			omsOrposCustOrderHead.setStatus("F");
			OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(), new BigDecimal(custOrdItmDesc.getLineItemNo()));
			omsCustOrdItem.setStatus("F");
			session.mergeOmsCustOrdItem(omsCustOrdItem);
			status = true;

			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Completed the try block for Duplicate items -Fulfillment Logic");

		return availableQuantity;
	}

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> getMap(BigDecimal omsCustOrdNo) {
		log.info(omsCustOrdNo + "inside getMap " + fulfillDetailMap.keySet() + "-------------->");
		return fulfillDetailMap;
	}

	public int getWHSOH(CustOrdItmDesc custOrdItmDesc, long L_Pending_Qty, String sourceLocType, String deliveryLocType, long intiateLocId) throws SOAPException {

		String locKey = sourceLocType + "~" + sourceLocId + "~" + custOrdItmDesc.getItemId();
		int availableQuantity = 0;

		int requestedQuantity = (int) L_Pending_Qty;

		Integer whStock = whItemStockMap.get(locKey);
		if (whStock == null) {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			log.info("omsCustOrdNo " + omsCustOrdNo + "Started the code for Virtual Warehouse scenario");
			List<Object[]> tempWhObject = session.getWhFindPhysicalWH(sourceLocId);

			// virtualWH = sourceLocId;
			BigDecimal physicalWH = BigDecimal.ZERO;
			BigDecimal channelId = BigDecimal.ZERO;
			for (Object[] result : tempWhObject) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside loop");
				log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0]);
				physicalWH = new BigDecimal(result[0].toString());
				channelId = new BigDecimal(result[1].toString());
				log.info("omsCustOrdNo " + omsCustOrdNo + "channel id=" + result[1]);
			}

			phyWhMap.put(sourceLocId.longValue(), physicalWH);

			// virtualWH = sourceLocId;

			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH" + fulfillLocId);
			log.info("omsCustOrdNo " + omsCustOrdNo + " physicalWH " + physicalWH);
			log.info("omsCustOrdNo " + omsCustOrdNo + " channelId " + channelId);
			List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);
			int i = 0;
			while (i < locList.size()) {
				log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
				i++;
			}
			String applicationId = "ORPOS";
			OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
			whStock = omsUtilCommons.checkSOHForWH(custOrdItmDesc.getItemId(), locList, applicationId).intValue();
			log.info("omsCustOrdNo " + omsCustOrdNo + " Item " + custOrdItmDesc.getItemId() + "locList" + locList.toString() + "SOH " + whStock);
		}
		if ("WH".equals(sourceLocType) && "S".equals(deliveryLocType) && String.valueOf(intiateLocId).startsWith("8")) {
			// sourceLocId remains unchanged; no assignment needed
		} else {
			sourceLocId = phyWhMap.get(sourceLocId.longValue());
		}
		if (whStock >= requestedQuantity) {
			availableQuantity = requestedQuantity;
			whStock = whStock - requestedQuantity;
		} else if (whStock <= requestedQuantity) {
			availableQuantity = whStock;
			whStock = 0;
		}
		whItemStockMap.put(locKey, whStock);
		log.info("omsCustOrdNo " + omsCustOrdNo + " availableQuantity in case of WH is " + availableQuantity);
		return availableQuantity;

	}// end of WHSOH

	public int processPO(CustOrdItmDesc custOrdItmDesc, CustOrderDesc custOrderDesc, long L_Pending_Qty, int SOH) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Boolean flag = Boolean.FALSE;
		int availableQuantity = 0;

		CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
		log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
		long partialPoQty = L_Pending_Qty - SOH;
		log.info("omsCustOrdNo " + omsCustOrdNo + "isPartialPO=true and partialPoQty=" + partialPoQty);
		String item_status = checkItemLocSOH.findItemStatus(custOrdItmDesc.getItemId(), fulfillLocId);
		log.info(" Item Status is " + item_status);
		if (item_status.equals("A") == false) {
			custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_108"); // Status of item is not approved,cannot fulfill the order
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
		}
		log.info("DeliveryLocType value is " + deliveryLocType);
		/**
		 * Eariler we are finding the primary supplier of an item having direct_Ship_ind
		 * 'Y' or not. If the primary supplier of the given item having direct_ship_ind
		 * 'N' then we are getting an EJB Exception.
		 *
		 */
		if ("S".equals(deliveryLocType)) {
			/**
			 * If delivery location type is physical store 'S'. then we are passing item and
			 * delivery location. The function returns primary supplier of the given item
			 * and at particular location. After that We are checking the direct_ship_ind
			 * indicator value 'Y' or not by Passing item and supplier. If it is 'Y' then
			 * the above supplier will be used to create the order Otherwise we will throw
			 * an Error.
			 */
			log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location" + fulfillLocId.longValue());
			sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(), fulfillLocId.longValue());
		} else {
			log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location" + custOrderDesc.getInitiateLocId());
			/**
			 * If delivery location type is 'V' Virtual store. then we are passing item and
			 * order requestor id(it is a location). The functions return the primary
			 * supplier of the given item and location (order requestor id). After that We
			 * are checking the direct_ship_ind indicator value 'Y' or not by Passing item
			 * and supplier. If it is 'Y' then the above supplier will be used to create the
			 * order Otherwise we will throw an Error.
			 */
			sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(), custOrderDesc.getInitiateLocId());
		}
		log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location" + sourceLocId.longValue());
		flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItmDesc.getItemId(), sourceLocId.longValue(), custOrderDesc.getInitiateLocId());
		// nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(),
		// "Y"));
		if (flag == Boolean.FALSE) {
			status = true;
			custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_108"); // Status of item is not approved,cannot fulfill the order
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
			errorcodes.add("OMS_ORPOS_ERROR_108");
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + sourceLocId);
		if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
			status = true;
			custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_104"); // ORG_UNIT_UNMATCHED
			// throw new
			// SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
			errorcodes.add("OMS_ORPOS_ERROR_104");
		} else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
			if (item_status.equals("A") == false) {
				status = true;
				custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_108"); // Status of item is not approved,cannot fulfill the order
				errorcodes.add("OMS_ORPOS_ERROR_108");
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
			}
			log.info("Delivery Location Type Value is" + deliveryLocType);
			if ("S".equals(deliveryLocType)) {
				log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location" + fulfillLocId);
				sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(), fulfillLocId.longValue());
			} else {
				log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location " + custOrderDesc.getInitiateLocId());
				sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItmDesc.getItemId(), custOrderDesc.getInitiateLocId());

			}
			log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItmDesc.getItemId() + "...." + "Location" + sourceLocId.longValue());
			flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItmDesc.getItemId(), sourceLocId.longValue(), custOrderDesc.getInitiateLocId());
			if (flag == Boolean.FALSE) {
				status = true;
				custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_109"); // supplier doesnot exist
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
				errorcodes.add("OMS_ORPOS_ERROR_109");
				log.info("Added error code for OMS_ORPOS_ERROR_109 SUPP_NOT_FOUND ");

			}

			log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + sourceLocId);
			if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
				status = true;
				custOrderDesc.setOrderDesc("OMS_ORPOS_ERROR_104"); // ORG_UNIT_UNMATCHED
				errorcodes.add("OMS_ORPOS_ERROR_104");
				log.info("Added error code for OMS_ORPOS_ERROR_104 ORG_UNIT_UNMATCHED ");
				// throw new
				// SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
			}

		}

		availableQuantity = (int) partialPoQty;
		return availableQuantity;

	}// end of PO

	public BackOrderFulfillDetail processBackOrderItem(CustOrderDesc custOrderDesc, CustOrdItmDesc custOrdItmDesc, BigDecimal L_Pending_Qty, BigDecimal omsCustOrdNo, BigDecimal omsOrposCustOrderId)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		log.info("<----------------------------Begin of  processBackOrderItem method ----------------------------------> ");
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside backorder");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		BackOrderFulfillDetail backOrderFulfillDetail = new BackOrderFulfillDetail();
		log.info(" Checking the  backOrderFulfillDetail Object address is" + backOrderFulfillDetail + "--------------------------------->");
		log.info("omsCustOrdNo " + omsCustOrdNo + " L_Pending_Qty " + L_Pending_Qty);
		int fulfilledQty = 0;
		BigDecimal channelId = BigDecimal.ZERO;
		int boFulfilledQty = 0;
		try {
			BigDecimal currentPendingQty = BigDecimal.ZERO;
			List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combId);
			int fulfilledqty = 0;
			log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetailList.size() " + omsFulfillMatrixExtDetailList.size());
			CustFutureInvPosition custFutureInvPosition = null;
			currentPendingQty = L_Pending_Qty;
			boolean itemLocked = false;
			int i = 0;
			Date startDate = new Date();
			while (fulfilledqty != L_Pending_Qty.intValue() && i < omsFulfillMatrixExtDetailList.size()) {

				String boIndicator = "N";
				log.info("omsCustOrdNo " + omsCustOrdNo + "i " + i);
				OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail = omsFulfillMatrixExtDetailList.get(i);
				BigDecimal boCombId = omsFulfillMatrixExtDetail.getCombinationId();
				log.info("omsCustOrdNo " + omsCustOrdNo + " boCombId " + boCombId);
				i++;

				try {
					CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
					BigDecimal physicalWH = BigDecimal.ZERO;
					log.info("omsCustOrdNo " + omsCustOrdNo + "****************************************************************");
					try {
						if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());

							for (Object[] result : tempWhObject) {
								physicalWH = new BigDecimal(result[0].toString());
								// omsFulfillMatrixExtDetail.setLocation(physicalWH);
								channelId = new BigDecimal(result[1].toString());
								log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + "physicalWH " + physicalWH);
						}

						ItemLoc i1 = null;
						log.info("<-------------->omsCustOrdNo " + omsCustOrdNo + "<---------Current Thread------>" + Thread.currentThread() + "<------>");
						i1 = new ItemLoc();
						log.info(" <---omsCustOrdNo--->" + omsCustOrdNo + "<-----Item is---->" + custOrdItmDesc.getItemId() + "<------->");
						i1.setItem(custOrdItmDesc.getItemId());
						log.info("<----omsCustOrdNo--->" + omsCustOrdNo + "<--Location is----->" + omsFulfillMatrixExtDetail.getLocation() + "<------->");
						i1.setLocation(omsFulfillMatrixExtDetail.getLocation());

						log.info("Obtaining ItemLoc Singleton Object------------------------------------->");
						// <--------------- No need of supplier in tree
						// mapObject---------------------------------->
					} catch (Exception e) {
						log.warn("Exception occured -- " + omsCustOrdNo, e);
					}
					itemLocked = PreOrderItemLocationSync.obtainLock(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());
					// } // end of if and else

					// alloctedInventory
					// =session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),custOrdItmDesc.getItemId());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Thread.current thread " + Thread.currentThread().getName());
					// Thread.sleep(2000);

					log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItmDesc.getItemId());
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetail.getLocation().longValue() " + omsFulfillMatrixExtDetail.getLocation().longValue());

					try {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Finding bo indicator for location" + omsFulfillMatrixExtDetail.getLocation());
						boIndicator = oMSUtilCommons.getBOIndicator(omsFulfillMatrixExtDetail.getLocation(), custOrdItmDesc.getItemId());

						log.info("omsCustOrdNo " + omsCustOrdNo + "fetched indicator value  from table is +++++++++++===" + boIndicator);
						if (boIndicator == null || boIndicator.isEmpty()) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "boIndicator is null ,setting it to N");
							boIndicator = "N";
						} else if (boIndicator.equals("Y")) {
							custFutureInvPosition = checkItemLocSOH.findFutInvDateAndQty(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation().longValue(), L_Pending_Qty);
						}
					} catch (Exception e) {
						boIndicator = "N";
					}

					if (boIndicator.equals("Y")) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition values fetched" + custFutureInvPosition.getExpectedDate() + "qty=" + custFutureInvPosition.getExpectedQty());

						log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition qty " + custFutureInvPosition.getExpectedQty() + " :date" + custFutureInvPosition.getExpectedDate());

						int qtyToBeFulfilled = custFutureInvPosition.getExpectedQty().intValue();
						log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToBeFulfilled " + qtyToBeFulfilled);
						log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilledQty " + fulfilledQty);
						if (qtyToBeFulfilled > 0) {

							fulfilledQty = fulfilledQty + qtyToBeFulfilled;
							backOrderFulfillDetail.setSOH(fulfilledQty);

							OmsBackOrderDtl omsBackOrderDtl = new OmsBackOrderDtl();
							omsBackOrderDtl.setSourceLoc(omsFulfillMatrixExtDetail.getLocation());
							omsBackOrderDtl.setSourceLocType(omsFulfillMatrixExtDetail.getLocationType());
							omsBackOrderDtl.setFulfillLocType(omsFulfillMatrixExtDetail.getDeliveryFromLocType());
							omsBackOrderDtl.setFulfillLoc(omsFulfillMatrixExtDetail.getDeliveryFromLoc());
							if (custOrderDesc.getCustOrdFulColDesc() != null && custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc() != null
									&& custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().size() > 0) {
								XMLGregorianCalendar delvDate = custOrderDesc.getCustOrdFulColDesc().getCustOrdFulDesc().get(0).getConsumerDeliveryDate();
								if (delvDate != null) {
									omsBackOrderDtl.setFulInvAvlDate(new Timestamp(delvDate.toGregorianCalendar().getTime().getTime()));
								}
							}

							omsBackOrderDtl.setCreateDatetime(new Timestamp(new Date().getTime()));
							omsBackOrderDtl.setItem(custOrdItmDesc.getItemId());
							omsBackOrderDtl.setLineNo(new BigDecimal(custOrdItmDesc.getLineItemNo()));
							omsBackOrderDtl.setCombinationId(omsFulfillMatrixExtDetail.getCombinationId());
							omsBackOrderDtl.setOmsCustOrdNo(omsCustOrdNo);
							BigDecimal qtyToPersist = null;
							log.info("omsCustOrdNo " + omsCustOrdNo + "currentPendingQty " + currentPendingQty);
							if (qtyToBeFulfilled <= currentPendingQty.intValue()) {
								currentPendingQty = new BigDecimal(currentPendingQty.intValue() - qtyToBeFulfilled);
								log.info("omsCustOrdNo " + omsCustOrdNo + "currentPendingQty " + currentPendingQty);
								log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToPersist=" + qtyToPersist);
								qtyToPersist = new BigDecimal(qtyToBeFulfilled);
							} else {
								// omsBackOrderDtl.setSourceQty(custOrdItmDesc.getQuantity());
								qtyToPersist = currentPendingQty;
								log.info("omsCustOrdNo " + omsCustOrdNo + "else qtyToPersist=" + qtyToPersist);
								currentPendingQty = BigDecimal.ZERO;
							}
							boFulfilledQty = boFulfilledQty + qtyToPersist.intValue();

							omsBackOrderDtl.setFulfillQty(BigDecimal.ZERO);
							omsBackOrderDtl.setBackorderStatus("N");
							omsBackOrderDtl.setCreatedBy("OMSUSER");

							if (qtyToPersist.intValue() > 0) {
								omsBackOrderDtl.setSourceQty(qtyToPersist);

								// session.persistOmsBackOrderDtl(omsBackOrderDtl);

								backOrderFulfillDetail.setSourceLoc(omsFulfillMatrixExtDetail.getLocation());
								backOrderFulfillDetail.setSourceLocType(omsFulfillMatrixExtDetail.getLocationType());
								backOrderFulfillDetail.setFulfillLoc(omsFulfillMatrixExtDetail.getDeliveryFromLoc());
								backOrderFulfillDetail.setFulfillLocType(omsFulfillMatrixExtDetail.getDeliveryFromLocType());
								InterfacePersistence interfacePersistence = new InterfacePersistence();
								OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItmDesc.getItemId(), new BigDecimal(custOrdItmDesc.getLineItemNo()));
								if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItmDesc.getItemId() + "qty=" + qtyToPersist + "loc=" + physicalWH);
									interfacePersistence.callRMSBackorderWS(custOrdItmDesc.getItemId(), qtyToPersist, physicalWH.longValue(), "W", omsCustOrdItem.getStandardUom(), channelId);
								} else {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItmDesc.getItemId() + "qty=" + qtyToPersist + "loc="
											+ omsFulfillMatrixExtDetail.getLocation());
									interfacePersistence.callRMSBackorderWS(custOrdItmDesc.getItemId(), qtyToPersist, omsFulfillMatrixExtDetail.getLocation().longValue(), "S",
											omsCustOrdItem.getStandardUom(), channelId);

								}
								log.info("<------- omsCustOrdNo----> " + omsCustOrdNo + "<-------Inserting Record into OmsBackOrderDtl Table ------------------------> ");
								log.info("<-----omsCustOrdNo ----->" + omsCustOrdNo + "<-----Executing Weblogic Thread --------------------->" + Thread.currentThread().getName() + "<--------->");
								session.persistOmsBackOrderDtl(omsBackOrderDtl);
								// Remove the pending Object from temp map object

								Date endDate = new Date();
								log.info(" 1.End Date------------------------>" + endDate);
								log.info("<----omsCustOrdNo --->" + omsCustOrdNo + "<-----Time Difference is---->" + (endDate.getTime() - startDate.getTime()) / 1000 + "Seconds" + "Thread Name--->"
										+ Thread.currentThread().getName() + "<----->");
								log.info("omsCustOrdNo " + omsCustOrdNo + "Back order successful");
								omsCustOrdItem.setBackorderInd("Y");
								// omsCustOrdItem.setStatus("S");
								log.info("omsCustOrdNo " + omsCustOrdNo + "setted BackOrder Indicator to Y");
								session.mergeOmsCustOrdItem(omsCustOrdItem);
							}
						}
					}
					log.info("<----------omsCustOrdNo--->" + omsCustOrdNo + "<----------------------- End of Bo Indicator Part--------------------------------------------------->");
					log.info("<-----omsCustOrdNo-------->" + omsCustOrdNo + "<-----Need to Remove Item-------------->" + custOrdItmDesc.getItemId() + " and location" + ""
							+ omsFulfillMatrixExtDetail.getLocation() + "from Temp  map matrix--------------->");
					ItemLoc itemloc = new ItemLoc();
					itemloc.setItem(custOrdItmDesc.getItemId());
					itemloc.setLocation(omsFulfillMatrixExtDetail.getLocation());
				} catch (Exception f) {
					log.info("<--------omsCustOrdNo----> " + omsCustOrdNo + "<------If Some Error Occured then it should allow other request to Procced ---->");
					log.error("Exception occured inside while calling backorder ", f);
				} finally {
					try {
						if (itemLocked) {
							PreOrderItemLocationSync.releaseLock(custOrdItmDesc.getItemId(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());
						}
					} catch (Exception e) {
						log.warn("Error while relaesing the lock " + custOrdItmDesc.getItemId() + "~" + omsFulfillMatrixExtDetail.getLocation() + " for the oms cust ord no# " + omsCustOrdNo, e);
					}
				}
			}

		} catch (Exception h) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in back order " + h);

		}
		backOrderFulfillDetail.setSOH(boFulfilledQty);

		return backOrderFulfillDetail;

	}

	public Map<String, CustOrdItmDesc> groupItem(BigDecimal omsCustOrdNo, CustOrderDesc custOrderDesc) throws SOAPException {
		Map<String, CustOrdItmDesc> groupedItemMap = new HashMap<String, CustOrdItmDesc>();
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String refValue = "";
		try {
			refValue = session.getOmsReferenceDataFindRefValue("OMS_SYSTEM_OPTION", "STORE_GROUPING_PARAM", String.valueOf(custOrderDesc.getInitiateLocId()));
			log.info("omsCustOrdNo " + omsCustOrdNo + "refValue from try block OmsReferenceData " + refValue);
		} catch (Exception e) {
			refValue = session.getOmsSystemParametersFindIndValue("DEFAULT_GROUPING_PARAM", "OMS_SYSTEM_OPTION");
			log.info("omsCustOrdNo " + omsCustOrdNo + "refValue from catch block OmsSystemParameters " + refValue);
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Grouping flag is " + refValue);
		log.info("omsCustOrdNo " + omsCustOrdNo + "custOrderDesc.getGiftReceiptAssigned().value() " + custOrderDesc.getGiftReceiptAssigned().value());
		if (custOrderDesc.getGiftReceiptAssigned().value().equals("Y")) {
			refValue = "N";
		}
		for (CustOrdItmDesc custOrdItmDesc : custOrderDesc.getCustOrdItmColDesc().getCustOrdItmDesc()) {
			String discountKey = ",";
			log.info("Discount Mapping");
			log.info("refValue " + refValue);
			if (custOrdItmDesc.getDiscntLineColDesc() != null) {
				log.info(" inside if condition for custOrdItmDesc.getDiscntLineColDesc() ");
				for (DiscntLineDesc discntLineDesc : custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc()) {
					log.info("inside for loop for custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc() ");
					discountKey = discountKey.concat(discntLineDesc.getDiscountReasonCode());
					log.info("Discount Keys : " + discountKey);
				}
			}
			log.info("outside the if condition for custOrdItmDesc.getDiscntLineColDesc().getDiscntLineDesc()");
			log.info("omsCustOrdNo " + omsCustOrdNo + " and " + "Line No " + custOrdItmDesc.getLineItemNo() + " Item " + custOrdItmDesc.getItemId() + "RMS Promo Type=" + discountKey);
			if (refValue.equals("Y")) {
				String key = custOrdItmDesc.getItemId() + "," + custOrdItmDesc.getUnitSellPrice() + discountKey;
				log.info("KEY : " + key);
				if (groupedItemMap.containsKey(key)) {
					CustOrdItmDesc custOrdItmDescTemp = new CustOrdItmDesc();
					custOrdItmDescTemp = groupedItemMap.get(key);
					if (custOrdItmDesc.getUnitSellPrice().compareTo(custOrdItmDescTemp.getUnitSellPrice()) == 0) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItmDescTemp.getQuantity()=" + custOrdItmDescTemp.getQuantity() + "for lineno" + custOrdItmDesc.getLineItemNo());
						custOrdItmDescTemp.setQuantity(custOrdItmDescTemp.getQuantity().add(custOrdItmDesc.getQuantity()));
						log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItmDescTemp.getInclusiveTaxTotal() " + custOrdItmDescTemp.getInclusiveTaxTotal() + "for lineno"
								+ custOrdItmDesc.getLineItemNo());
						custOrdItmDescTemp.setInclusiveTaxTotal(custOrdItmDescTemp.getInclusiveTaxTotal().add(custOrdItmDesc.getInclusiveTaxTotal()));
						log.info("omsCustOrdNo " + omsCustOrdNo + "While grouping qty is now " + custOrdItmDescTemp.getQuantity());
						log.info("omsCustOrdNo " + omsCustOrdNo + "While grouping tax is now " + custOrdItmDescTemp.getInclusiveTaxTotal());

						groupedItemMap.put(key, custOrdItmDescTemp);
					} else {
						log.info("omsCustOrdNo " + omsCustOrdNo + " in else block for unit sell price " + key);
						groupedItemMap.put(key, custOrdItmDesc);
					}
				} else {
					log.info("omsCustOrdNo " + omsCustOrdNo + " in else block not contain key " + key);
					groupedItemMap.put(key, custOrdItmDesc);
				}
			} else {
				String key = String.valueOf(custOrdItmDesc.getLineItemNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + " else block for  refValue key is " + key);
				groupedItemMap.put(key, custOrdItmDesc);
			}
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "groupedItemMap " + groupedItemMap.keySet());
		return groupedItemMap;
	}// end of group Item

	public OmsTempCoFo createFulfilMapObject(BigDecimal omsCustOrdNo, String item, BigDecimal sourceLocId, String sourceLocType, BigDecimal fulfillLocId, String fulFillLocType, int availableQty,
			BigDecimal lineNo, BigDecimal combID, int maxFulfillOrderNo, BigDecimal virtualWH) {
		log.info("omsCustOrdNo " + omsCustOrdNo + " inside createFulfilMapObject ");
		OmsTempCoFo omsTempCoFo = new OmsTempCoFo();

		if (sohMap != null && sohMap.size() > 0 && sohMap.keySet() != null) {
			treeMap.putAll(sohMap);
			maxFulfillOrderNo = treeMap.lastKey().intValue();
			log.info("omsCustOrdNo " + omsCustOrdNo + "maxfulFilOrderNo from treeMap " + maxFulfillOrderNo);
			maxFulfillOrderNo = maxFulfillOrderNo + 1;
			log.info("omsCustOrdNo " + omsCustOrdNo + "maxfulFilOrderNo after adding " + maxFulfillOrderNo);
			omsTempCoFo.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));

		} else {
			omsTempCoFo.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo)); 
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Source loc=" + sourceLocId + " source loc type=" + sourceLocType + "fulfill loc=" + fulfillLocId + " fulFillLocType=" + fulFillLocType);
		omsTempCoFo.setOmsCustOrdNo(omsCustOrdNo);
		omsTempCoFo.setLineNo(lineNo);
		omsTempCoFo.setItem(item);
		omsTempCoFo.setOrderQty(new BigDecimal(availableQty));
		omsTempCoFo.setFoConfQty(BigDecimal.ZERO);
		omsTempCoFo.setSourceLocId(sourceLocId);
		omsTempCoFo.setSourceLocationType(sourceLocType);
		omsTempCoFo.setFulfillLocId(fulfillLocId);
		omsTempCoFo.setFulfillLocationType(fulFillLocType);
		omsTempCoFo.setCombinationId(combID);
		omsTempCoFo.setRmsResponseCode("");
		omsTempCoFo.setRmsErrorMsg("");
		omsTempCoFo.setVirtualWH(virtualWH);
		if (!sourceLocType.equals("WH")) {
			omsTempCoFo = STfulFilOrderNo(omsTempCoFo, sohMap);
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + " fulfilOrderNo for lineNO " + omsTempCoFo.getLineNo() + "Item " + omsTempCoFo.getItem() + "SourceLocId " + sourceLocId + "is "
				+ omsTempCoFo.getFulfillOrderNo());
		return omsTempCoFo;
	}

	public OmsTempCoFo STfulFilOrderNo(OmsTempCoFo omsTempCoFo, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap) {
		// Added for 2609 bug
		log.info("inside STfulFilOrderNo");
		BigDecimal stfulfilOrderNO = omsTempCoFo.getFulfillOrderNo();
		log.info("sohMap.keySet() " + sohMap.keySet());
		log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
		ArrayList<String> itemlist = new ArrayList<String>();
		ArrayList<OmsTempCoFo> list = new ArrayList<OmsTempCoFo>();
		for (BigDecimal k : sohMap.keySet()) {
			log.info("======Key============== " + k);
			list = sohMap.get(k);
			for (OmsTempCoFo temp : list) {
				if (temp.getSourceLocId().intValue() == omsTempCoFo.getSourceLocId().intValue() && temp.getFulfillLocId().intValue() == omsTempCoFo.getFulfillLocId().intValue()) {
					log.info("temp.getSourceLocId() " + temp.getSourceLocId());
					log.info("temp.getFulfillLocId() " + temp.getFulfillLocId());
					log.info("omsTempCoFo.getSourceLocId() " + omsTempCoFo.getSourceLocId());
					log.info("omsTempCoFo.getFulfillocId() " + omsTempCoFo.getFulfillLocId());
					log.info("temp.getItem() " + temp.getItem());
					log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());
					log.info("temp fulfilorderNo " + temp.getFulfillOrderNo());
					log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
					stfulfilOrderNO = temp.getFulfillOrderNo();
					log.info("Adding item " + temp.getItem());
					itemlist.add(temp.getItem().toString());
					log.info("stfulfilOrderNO " + stfulfilOrderNO);
				}
			}
		}

		log.info("itemlist " + itemlist.toString());
		log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());

		if (itemlist.contains(omsTempCoFo.getItem())) {
			log.info("return actual fulfillOrderNo " + omsTempCoFo.getFulfillOrderNo());
		} else {
			omsTempCoFo.setFulfillOrderNo(stfulfilOrderNO);
			log.info("set the stfulfilOrderNO " + stfulfilOrderNO);

		}

		log.info("while returning the fulfilOrderNo is " + omsTempCoFo.getFulfillOrderNo());
		return omsTempCoFo;
	}

}// end of class
