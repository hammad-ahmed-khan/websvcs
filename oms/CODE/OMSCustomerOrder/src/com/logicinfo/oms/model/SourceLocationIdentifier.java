package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.soap.SOAPException;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.BackOrderAllocatedInventory;
import com.logicinfo.oms.beans.ItemLoc;
import com.logicinfo.oms.beans.ItemUnavailabilityStatus;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsRepublishData;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.PreOrderItemLocationSync;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

public class SourceLocationIdentifier {
	public SourceLocationIdentifier() {
		super();
	}

	BigDecimal omsCustOrdNo;
	public final static Logger log = Logger.getLogger(SourceLocationIdentifier.class.getName());
	Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = null;
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;
	Map<String, FulfillOrdandSourceLocPOJO> itemCounterMap = new ConcurrentHashMap<String, FulfillOrdandSourceLocPOJO>();
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treeMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
	Map<String, BigDecimal> storeItemMap = new HashMap<String, BigDecimal>();
	Map<String, ItemSOH> itemQtyMap = new HashMap<String, ItemSOH>();
	// unavailableItemsList available list is made local variable for each thread
	// otherwise we will get
	// Orders are created without successful fulfillment...
	List<ItemAvailability> unavailableItemsList = null;
	BigDecimal combId = BigDecimal.ZERO;
	BigDecimal nextLoc = BigDecimal.ONE;
	BigDecimal sourceLocId = BigDecimal.ZERO;
	BigDecimal fulfillLocId = BigDecimal.ZERO;
	BigDecimal virtualWH = BigDecimal.ZERO;
	String deliveryLocType = null;
	String sourceLocType = null;
	BigDecimal storeSOH = BigDecimal.ZERO;

	InterfacePersistence interfacePersistence = new InterfacePersistence();

	public void processSplitOrders(BigDecimal omsCustOrdNo, CustomerOrder input, int priority, BigDecimal combID, BigDecimal fulfillLocId)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "***OMS_SPLIT_ORDER = Y processSplitOrders starts***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		int maxFulfillOrderNo = oMSUtilCommons.returnMaxFulFilOrdNoECOM(input.getCustomerOrderNo());
		for (CustomerOrderItems custOrdItems : input.getCustomerOrderItems()) {
			long SOH = 0;
			log.info("omsCustOrdNo " + omsCustOrdNo + "-------------------Processing for item=" + custOrdItems.getItem() + "Line_no" + custOrdItems.getLineNo() + "---------------");
			priority = 1;
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(custOrdItems.getItem());
			String inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItems.getItem(), itemDept);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item Dept= " + itemDept + "InventoryIndicator " + inventoryIndn);
			combId = findCombinationId(input, custOrdItems);
			log.info("omsCustOrdNo " + omsCustOrdNo + "combId=" + combId);

			if (shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y") && ((null == custOrdItems.getCombinationId())))

			{
				long L_Cum_Ord_Qty = 0L;
				long L_Pending_Qty = 0L;
				while (L_Cum_Ord_Qty < custOrdItems.getOrderQtySuom().longValue()) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);
					FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
					int availableQty = 0;
					try {
						try {
							fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrix(combId, priority);
							log.info("Fulfil Location ********" + fulfillMatrixExtDetailResult.getDeliveryFromLoc());
							log.info("Combination ID ********" + fulfillMatrixExtDetailResult.getCombinationId());
							log.info("Priority ********" + fulfillMatrixExtDetailResult.getPriority());
							log.info("Source Loc ********" + fulfillMatrixExtDetailResult.getLocation());
						} catch (Exception e) {

							if (custOrdItems.getBackOrderInd().equals("Y")) {
								log.info("omsCustOrder" + omsCustOrdNo + " Checking backorder as physical inventory not available");
								SOH = processBackOrderItem(custOrdItems, input, new BigDecimal(L_Pending_Qty));
								L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
								L_Pending_Qty = custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).longValue();
								log.info("omsCustOrder" + omsCustOrdNo + " L_Pending_Qty=" + L_Pending_Qty);
								if (L_Pending_Qty > 0) {
									ItemAvailability itemAvailability = new ItemAvailability();
									itemAvailability.setItem(custOrdItems.getItem());
									itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
									itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
									itemAvailability.setErrorMessage("UNAVL_INV");
									itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));

									// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
									unavailableItemsList.add(itemAvailability);
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									omsCustOrdHead.setStatus("F");
									session.mergeOmsCustOrdHead(omsCustOrdHead);
									OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
									omsCustOrdItem.setStatus("F");
									session.mergeOmsCustOrdItem(omsCustOrdItem);
									break;
									// throw new
									// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
								}
							} else {
								ItemAvailability itemAvailability = new ItemAvailability();
								try {
									OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(e.getMessage());
									log.info("omsCustOrdNo " + omsCustOrdNo + "error message" + theErrorObj.getOmsErrLangDesc());
									itemAvailability.setErrorMessage(theErrorObj.getOmsErrLangDesc());
								} catch (Exception h) {
									itemAvailability.setErrorMessage("FAILED");
								}
								log.info("omsCustOrdNo " + omsCustOrdNo + "Putting into item Availablity" + e.getMessage());

								itemAvailability.setItem(custOrdItems.getItem());
								itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
								itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));
								itemAvailability.setErrorMessage("UNAVL_INV");
								// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
								unavailableItemsList.add(itemAvailability);
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
								omsCustOrdItem.setStatus("F");
								session.mergeOmsCustOrdItem(omsCustOrdItem);
								break;
								// throw new
								// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
							}
						}
						log.info("omsCustOrdNo " + omsCustOrdNo + " priority=" + priority);
						sourceLocId = fulfillMatrixExtDetailResult.getLocation();
						sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
						fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
						deliveryLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();

						log.info("omsCustOrdNo " + omsCustOrdNo + " sourceLocId:" + sourceLocId + " sourceLocType=" + sourceLocType + " fulfillLocId=" + fulfillLocId + " deliveryLocType="
								+ deliveryLocType);

						if (sourceLocType.equals("ST")) {
							if ("Y".equals(custOrdItems.getPackItemInd())) {
								availableQty = getStorePackAvailablity(custOrdItems, L_Cum_Ord_Qty, L_Pending_Qty, sourceLocId);
							} else {
								availableQty = getStoreSOH(custOrdItems, L_Cum_Ord_Qty, L_Pending_Qty, sourceLocId);
							}

							virtualWH = BigDecimal.ZERO;
						}
						if (sourceLocType.equals("WH")) {
							if (priority == 1) {
								L_Pending_Qty = custOrdItems.getOrderQtySuom().longValue();
							}
							if ("Y".equals(custOrdItems.getPackItemInd())) {
								availableQty = getWHPackItemAvailablity(input, custOrdItems, sourceLocId, L_Pending_Qty);
							} else {
								availableQty = getWHSOH(custOrdItems, input, sourceLocId, fulfillLocId, L_Pending_Qty);
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + " virtualWH in WH " + virtualWH);
						} else if (sourceLocType.equals("SU") || sourceLocId.intValue() < 0) {
							if (priority == 1) {
								L_Pending_Qty = custOrdItems.getOrderQtySuom().longValue();
							}
							if (custOrdItems.getBackOrderInd().equals("Y")) {
								// call BackOrder Method
								SOH = processBackOrderItem(custOrdItems, input, new BigDecimal(L_Pending_Qty));
								log.info("omsCustOrdNo" + omsCustOrdNo + " qty fulfilled from BO=" + SOH);
								L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
							}
							if (L_Cum_Ord_Qty != custOrdItems.getOrderQtySuom().intValue()) {
								availableQty = processPO(custOrdItems, input, fulfillLocId, deliveryLocType, L_Pending_Qty, SOH);
								log.info("omsCustOrdNo" + omsCustOrdNo + "availableQty from PO" + availableQty + " and L_Cum_Ord_Qty before going for PO is " + L_Cum_Ord_Qty);
							}
							virtualWH = BigDecimal.ZERO;
						}
						if (availableQty > 0) {
							// create a list and add to sohMap
							OmsTempCoFo omsTempCoFo = createFulfilMapObject(omsCustOrdNo, custOrdItems.getItem(), sourceLocId, sourceLocType, fulfillLocId, deliveryLocType, availableQty,
									new BigDecimal(custOrdItems.getLineNo()), combId, maxFulfillOrderNo, virtualWH);
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
						L_Pending_Qty = custOrdItems.getOrderQtySuom().intValue() - L_Cum_Ord_Qty;

						log.info("omsCustOrdNo " + omsCustOrdNo + " ordered qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + custOrdItems.getOrderQtySuom());
						log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Cum_Ord_Qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + L_Cum_Ord_Qty);
						log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Pending_Qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + L_Pending_Qty);

						priority++;

					} // end of try
					catch (javax.xml.ws.soap.SOAPFaultException g) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "g.getMessage() " + g.getMessage());
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(g.getMessage()));

					} catch (javax.xml.ws.WebServiceException f) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in calling SIM exception is  " + f);
						OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
						omsCustOrdHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCustOrdHead);
						OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
						omsCustOrdItem.setStatus("F");
						session.mergeOmsCustOrdItem(omsCustOrdItem);
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("WSDL_UNAVL"));
					} catch (Exception e) {
						log.info("inside the Exception " + e);

						ItemAvailability itemAvailability = new ItemAvailability();
						try {
							OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(e.getMessage());
							log.info("omsCustOrdNo " + omsCustOrdNo + "error message" + theErrorObj.getOmsErrLangDesc());
							itemAvailability.setErrorMessage(theErrorObj.getOmsErrLangDesc());
						} catch (Exception h) {
							itemAvailability.setErrorMessage("FAILED");
						}
						log.info("omsCustOrdNo " + omsCustOrdNo + "Putting into item Availablity" + e.getMessage());

						itemAvailability.setItem(custOrdItems.getItem());
						itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
						itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
						itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));
						itemAvailability.setErrorMessage("UNAVL_INV");
						// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
						unavailableItemsList.add(itemAvailability);
						OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
						omsCustOrdHead.setStatus("F");
						session.mergeOmsCustOrdHead(omsCustOrdHead);
						OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
						omsCustOrdItem.setStatus("F");
						session.mergeOmsCustOrdItem(omsCustOrdItem);
						throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));

					}
				} // end of while block
			} // end of if

			// New Changes for Carrera -- Adding else block if combination id, priority and
			// locations are given
			else if (custOrdItems.getCombinationId() != null && custOrdItems.getPriority() != null && custOrdItems.getItemFulfillLoc() != null && shipingChargeDept.equals(itemDept.toString()) == false
					&& inventoryIndn.equals("Y")) {
				log.info("In the ELSE IF PART FOR CARRERA *** Combination ID, Priority and Locations are given");
				long L_Cum_Ord_Qty = 0L;
				long L_Pending_Qty = 0L;
				log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);
				FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
				int availableQty = 0;
				try {
					try {
						fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrixForCarrera(new BigDecimal(custOrdItems.getCombinationId()), custOrdItems.getPriority().intValue());
						log.info("Fulfil Location ********" + fulfillMatrixExtDetailResult.getDeliveryFromLoc());
						log.info("Combination ID ********" + fulfillMatrixExtDetailResult.getCombinationId());
						log.info("Priority ********" + fulfillMatrixExtDetailResult.getPriority());
						log.info("Source Loc ********" + fulfillMatrixExtDetailResult.getLocation());

						findNextfulfillLoc.checkFulfilment(custOrdItems, fulfillMatrixExtDetailResult);
						// log.info("FLAG VALUE : " +flag);

					} catch (Exception e) {
						log.info("Inside catch block");
						if (custOrdItems.getBackOrderInd().equals("Y")) {
							log.info("omsCustOrder" + omsCustOrdNo + "Checking backorder as physical inventory not available");
							SOH = processBackOrderItemforCarrera(custOrdItems, input, new BigDecimal(custOrdItems.getOrderQtySuom().longValue()));
							log.info("SOH of processBackOrderItem : " + SOH);
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
							L_Pending_Qty = custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).longValue();
							log.info("omsCustOrder" + omsCustOrdNo + " L_Pending_Qty=" + L_Pending_Qty);
							if (L_Pending_Qty > 0) {
								ItemAvailability itemAvailability = new ItemAvailability();
								itemAvailability.setItem(custOrdItems.getItem());
								itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
								itemAvailability.setErrorMessage("UNAVL_INV");
								itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));

								// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
								unavailableItemsList.add(itemAvailability);
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
								omsCustOrdItem.setStatus("F");
								session.mergeOmsCustOrdItem(omsCustOrdItem);
								// break;
								// throw new
								// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
							}
						} else {
							ItemAvailability itemAvailability = new ItemAvailability();
							try {
								OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(e.getMessage());
								log.info("omsCustOrdNo " + omsCustOrdNo + "error message" + theErrorObj.getOmsErrLangDesc());
								itemAvailability.setErrorMessage(theErrorObj.getOmsErrLangDesc());
							} catch (Exception h) {
								itemAvailability.setErrorMessage("FAILED");
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + "Putting into item Availablity" + e.getMessage());

							itemAvailability.setItem(custOrdItems.getItem());
							itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
							itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
							itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));
							itemAvailability.setErrorMessage("UNAVL_INV");
							// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
							unavailableItemsList.add(itemAvailability);
							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							omsCustOrdHead.setStatus("F");
							session.mergeOmsCustOrdHead(omsCustOrdHead);
							OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
							omsCustOrdItem.setStatus("F");
							session.mergeOmsCustOrdItem(omsCustOrdItem);
							break;
							// throw new
							// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
						}
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + " priority=" + priority + "based of carrera");
					sourceLocId = new BigDecimal(custOrdItems.getItemSourceLoc());
					sourceLocType = custOrdItems.getItemSourceLocType();
					fulfillLocId = new BigDecimal(custOrdItems.getItemFulfillLoc());
					deliveryLocType = custOrdItems.getItemFulfillLocType();

					log.info("omsCustOrdNo " + omsCustOrdNo + " sourceLocId:" + sourceLocId + " sourceLocType=" + sourceLocType + " fulfillLocId=" + fulfillLocId + " deliveryLocType="
							+ deliveryLocType);

					if (sourceLocType.equals("ST")) {
						if ("Y".equals(custOrdItems.getPackItemInd())) {
							availableQty = getStorePackAvailablity(custOrdItems, L_Cum_Ord_Qty, L_Pending_Qty, sourceLocId);
						} else {
							availableQty = getStoreSOH(custOrdItems, L_Cum_Ord_Qty, L_Pending_Qty, sourceLocId);
						}
						virtualWH = BigDecimal.ZERO;
					}
					if (sourceLocType.equals("WH")) {
						if (priority == 1) {
							L_Pending_Qty = custOrdItems.getOrderQtySuom().longValue();
						}
						if ("Y".equals(custOrdItems.getPackItemInd())) {
							availableQty = getWHPackItemAvailablity(input, custOrdItems, sourceLocId, L_Pending_Qty);
						} else {
							availableQty = getWHSOH(custOrdItems, input, sourceLocId, fulfillLocId, L_Pending_Qty);
						}
						log.info("omsCustOrdNo " + omsCustOrdNo + " virtualWH in WH " + virtualWH);
					} else if (sourceLocType.equals("SU") || sourceLocId.intValue() < 0) {
						if (priority == 1) {
							L_Pending_Qty = custOrdItems.getOrderQtySuom().longValue();
						}
						if (custOrdItems.getBackOrderInd().equals("Y")) {
							// call BackOrder Method
							SOH = processBackOrderItemforCarrera(custOrdItems, input, new BigDecimal(custOrdItems.getOrderQtySuom().longValue()));
							log.info("omsCustOrdNo" + omsCustOrdNo + " qty fulfilled from BO=" + SOH);
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
						}
						if (L_Cum_Ord_Qty != custOrdItems.getOrderQtySuom().intValue()) {
							availableQty = processPO(custOrdItems, input, fulfillLocId, deliveryLocType, L_Pending_Qty, SOH);
							log.info("omsCustOrdNo" + omsCustOrdNo + "availableQty from PO" + availableQty + " and L_Cum_Ord_Qty before going for PO is " + L_Cum_Ord_Qty);
						}
						virtualWH = BigDecimal.ZERO;
					}
					// mani changes old code ---- if ( availableQty > 0 )
					// new changes if( availableQty >=custOrdItems.getOrderQtySuom().intValue())

					if (availableQty >= custOrdItems.getOrderQtySuom().intValue()) {
						// create a list and add to sohMap
						log.info("Available Qty is greater than 0 loop");
						OmsTempCoFo omsTempCoFo = createFulfilMapObject(omsCustOrdNo, custOrdItems.getItem(), sourceLocId, sourceLocType, fulfillLocId, deliveryLocType, availableQty,
								new BigDecimal(custOrdItems.getLineNo()), new BigDecimal(custOrdItems.getCombinationId()), maxFulfillOrderNo, virtualWH);
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
						log.info("Available Qty is equal to 0");
						availableQty = 0;

						if (custOrdItems.getBackOrderInd().equals("Y")) {
							log.info("omsCustOrder" + omsCustOrdNo + "Checking backorder as physical inventory not available");
							SOH = processBackOrderItemforCarrera(custOrdItems, input, new BigDecimal(custOrdItems.getOrderQtySuom().longValue()));
							log.info("SOH of processBackOrderItem : " + SOH);
							L_Cum_Ord_Qty = L_Cum_Ord_Qty + SOH;
							L_Pending_Qty = custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).longValue();
							log.info("omsCustOrder Carrera" + omsCustOrdNo + " L_Pending_Qty=" + L_Pending_Qty);
							log.info("OrderedQty Carrera: " + custOrdItems.getOrderQtySuom().longValue());
							log.info("L_Cum_Ord_Qty Carrera" + L_Cum_Ord_Qty);
							if (L_Pending_Qty > 0) {
								ItemAvailability itemAvailability = new ItemAvailability();
								itemAvailability.setItem(custOrdItems.getItem());
								itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
								itemAvailability.setErrorMessage("UNAVL_INV");
								itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));

								// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
								unavailableItemsList.add(itemAvailability);
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
								omsCustOrdItem.setStatus("F");
								session.mergeOmsCustOrdItem(omsCustOrdItem);
								// break;
								// throw new
								// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
							}
						} else {
							ItemAvailability itemAvailability = new ItemAvailability();
							try {
								OmsErrorCodes theErrorObj = OMSUtil.parseErrorString("UNAVL_INV");
								log.info("omsCustOrdNo " + omsCustOrdNo + "error message" + theErrorObj.getOmsErrLangDesc());
								itemAvailability.setErrorMessage(theErrorObj.getOmsErrLangDesc());
							} catch (Exception h) {
								itemAvailability.setErrorMessage("FAILED");
							}
							itemAvailability.setItem(custOrdItems.getItem());
							itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
							itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
							itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));
							itemAvailability.setErrorMessage("UNAVL_INV");
							// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
							unavailableItemsList.add(itemAvailability);
							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							omsCustOrdHead.setStatus("F");
							session.mergeOmsCustOrdHead(omsCustOrdHead);
							OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
							omsCustOrdItem.setStatus("F");
							session.mergeOmsCustOrdItem(omsCustOrdItem);
							break;
							// throw new
							// SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
						}

					}
					L_Cum_Ord_Qty = L_Cum_Ord_Qty + availableQty;
					L_Pending_Qty = custOrdItems.getOrderQtySuom().intValue() - L_Cum_Ord_Qty;
					log.info("omsCustOrdNo " + omsCustOrdNo + " ordered qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + custOrdItems.getOrderQtySuom());
					log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Cum_Ord_Qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + L_Cum_Ord_Qty);
					log.info("omsCustOrdNo " + omsCustOrdNo + " Overrall L_Pending_Qty for lineNo " + custOrdItems.getLineNo() + "and item" + custOrdItems.getItem() + "is" + L_Pending_Qty);

					// priority++;

				} // end of try
				catch (javax.xml.ws.soap.SOAPFaultException g) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "g.getMessage() " + g.getMessage());
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault(g.getMessage()));

				} catch (javax.xml.ws.WebServiceException f) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in calling SIM exception is  " + f);
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
					omsCustOrdItem.setStatus("F");
					session.mergeOmsCustOrdItem(omsCustOrdItem);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("WSDL_UNAVL"));
				} catch (Exception e) {
					log.info("inside the Exception " + e);

					ItemAvailability itemAvailability = new ItemAvailability();
					try {
						OmsErrorCodes theErrorObj = OMSUtil.parseErrorString(e.getMessage());
						log.info("omsCustOrdNo " + omsCustOrdNo + "error message" + theErrorObj.getOmsErrLangDesc());
						itemAvailability.setErrorMessage(theErrorObj.getOmsErrLangDesc());
					} catch (Exception h) {
						itemAvailability.setErrorMessage("FAILED");
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "Putting into item Availablity" + e.getMessage());

					itemAvailability.setItem(custOrdItems.getItem());
					itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
					itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
					itemAvailability.setErrorMessage("UNAVL_INV");
					itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty));

					// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
					unavailableItemsList.add(itemAvailability);
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
					omsCustOrdItem.setStatus("F");
					session.mergeOmsCustOrdItem(omsCustOrdItem);
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));

				}
			}
		} // end of for
		if (sohMap != null && sohMap.size() > 0 && sohMap.keySet() != null) {
			fulfillDetailMap = createFulfillDetailMap(sohMap, 1);
		}
	} // end of method

	private int getWHPackItemAvailablity(CustomerOrder input, CustomerOrderItems custOrdItems, BigDecimal sourceLocationId, long L_Pending_Qty) throws SOAPException {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		BigDecimal physicalWH = BigDecimal.ZERO;
		if (input.getOrderCreateReserveInd().equals("C")) {
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			List<Object[]> tempWhObject = session.getWhFindPhysicalWH(sourceLocationId);
			for (Object[] result : tempWhObject) {
				physicalWH = new BigDecimal(result[0].toString());
				log.info("omsCustOrdNo" + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
			}
			sourceLocId = physicalWH;
		}
		
		int soh = oMSUtilCommons.getPackItemAvailQty(custOrdItems.getItem(), sourceLocationId);
		int requestQty = (int) L_Pending_Qty;
		return requestQty > soh ? soh : requestQty;
	}

	private int getStorePackAvailablity(CustomerOrderItems custOrdItems, long L_Cum_Ord_Qty, long L_Pending_Qty, BigDecimal sourceLocId) {
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		int soh = oMSUtilCommons.getPackItemAvailQty(custOrdItems.getItem(), sourceLocId);
		String itemandLoc = custOrdItems.getItem() + "," + sourceLocId.toString();
		log.info("Pack Item omsCustOrdNo " + omsCustOrdNo + "Procesing for line No " + custOrdItems.getLineNo() + "itemand Loc" + itemandLoc);
		int requestQty = 0;
		// check for needed quantity to fulfill
		if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
			requestQty = custOrdItems.getOrderQtySuom().intValue();
			log.info("Pack Item omsCustOrdNo" + omsCustOrdNo + "requestQty when L_Pending_Qty and l_cum_ord_qty is equal to 0" + requestQty);
		} else if (custOrdItems.getOrderQtySuom().intValue() >= L_Cum_Ord_Qty) {
			requestQty = custOrdItems.getOrderQtySuom().intValue() - (int) L_Cum_Ord_Qty;
			log.info("Pack Item omsCustOrdNo" + omsCustOrdNo + "custOrdItems.getOrderQtySuom().intValue()>=L_Cum_Ord_Qty" + requestQty);
		} else if (L_Cum_Ord_Qty >= custOrdItems.getOrderQtySuom().intValue()) {
			requestQty = (int) L_Cum_Ord_Qty - custOrdItems.getOrderQtySuom().intValue();
			log.info("Pack Item omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty>=custOrdItems.getOrderQtySuom().intValue() " + requestQty);
		}
		return requestQty > soh ? soh : requestQty;
	}

	public int getStoreSOH(CustomerOrderItems custOrdItems, long L_Cum_Ord_Qty, long L_Pending_Qty, BigDecimal sourceLocId)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		int availbleQuantity = 0;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int SOH = 0;
		log.info("omsCustOrdNo " + omsCustOrdNo + " omsCustOrdNo " + omsCustOrdNo + " sourceLocId " + sourceLocId);
		String itemandLoc = custOrdItems.getItem() + "," + sourceLocId.toString();
		log.info("omsCustOrdNo " + omsCustOrdNo + "Procesing for line No " + custOrdItems.getLineNo() + "itemand Loc" + itemandLoc);
		// added code for duplicate items - fulfillment logic
		log.info("omsCustOrdNo" + omsCustOrdNo + "itemQtyMap.keySet() " + itemQtyMap.keySet());
		log.info("omsCustOrdNo" + omsCustOrdNo + "storeItemMap.keySet()" + storeItemMap.keySet());
		log.info("omsCustOrdNo" + omsCustOrdNo + "omsCustOrdNo " + omsCustOrdNo + "Started the code for Duplicate Items - fulfillment logic" + " , Ordered Qty " + custOrdItems.getOrderQtySuom());
		log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
		log.info("omsCustOrdNo" + omsCustOrdNo + "pending qty  " + L_Pending_Qty);
		int requestQty = 0;
		// check for needed quantity to fulfill
		if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
			requestQty = custOrdItems.getOrderQtySuom().intValue();
			log.info("omsCustOrdNo" + omsCustOrdNo + "requestQty when L_Pending_Qty and l_cum_ord_qty is equal to 0" + requestQty);
		} else if (custOrdItems.getOrderQtySuom().intValue() >= L_Cum_Ord_Qty) {
			requestQty = custOrdItems.getOrderQtySuom().intValue() - (int) L_Cum_Ord_Qty;
			log.info("omsCustOrdNo" + omsCustOrdNo + "custOrdItems.getOrderQtySuom().intValue()>=L_Cum_Ord_Qty" + requestQty);
		} else if (L_Cum_Ord_Qty >= custOrdItems.getOrderQtySuom().intValue()) {
			requestQty = (int) L_Cum_Ord_Qty - custOrdItems.getOrderQtySuom().intValue();
			log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty>=custOrdItems.getOrderQtySuom().intValue() " + requestQty);
		}
		log.info("============================================================================ line " + custOrdItems.getLineNo());
		if (itemQtyMap.containsKey(custOrdItems.getItem())) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside containsKey condition for itemQtyMap for line No " + custOrdItems.getLineNo());
			if (storeItemMap.containsKey(itemandLoc))
			// if(itemQtyMap.get(custOrdItmDesc.getItemId()).getLocation().compareTo(nextLoc)==0)
			{
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside compareTo condition for item id");
				// SOH=itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh().longValue();
				SOH = storeItemMap.get(itemandLoc).intValue();
				log.info("omsCustOrdNo " + omsCustOrdNo + "itemQtyMap already contain item with SOH=" + SOH);
				ItemSOH itemSOH = itemQtyMap.get(custOrdItems.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + custOrdItems.getLineNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
				log.info("custOrdItems.getOrderQtySuom() " + custOrdItems.getOrderQtySuom());
				if (SOH >= requestQty) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "SOH >0 and locatin is " + sourceLocId);
					itemSOH.setLocation(sourceLocId);
					itemSOH.setSoh(new BigDecimal(SOH - requestQty));
					log.info("getting SOH inside inside if (storeItemMap.containsKey(itemandLoc)) " + itemSOH.getSoh());
					availbleQuantity = requestQty;
					log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);

				} // end of inner if
				else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "SOH is less than quantity requested,finding in store" + sourceLocId);
					itemSOH.setSoh(BigDecimal.ZERO);
					log.info("omsCustOrdNo" + omsCustOrdNo + "SOH inside else" + itemSOH.getSoh());
					itemQtyMap.put(custOrdItems.getItem(), itemSOH);
					if (storeItemMap.get(itemandLoc) != null) {
						storeSOH = storeItemMap.get(itemandLoc);
						log.info("storeSOH" + itemQtyMap.get(custOrdItems.getItem()).getSoh());
						storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
					}
					availbleQuantity = (int) SOH;
					log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
				}
				itemQtyMap.put(custOrdItems.getItem(), itemSOH);
				log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
				log.info("itemSOH.getSoh() " + itemSOH.getSoh());
				long a = itemSOH.getSoh().longValue() - L_Cum_Ord_Qty;
				log.info("omsCustOrdNo " + omsCustOrdNo + "updated the soh to " + itemSOH.getSoh());
				log.info("a " + a);
				if (storeItemMap.get(itemandLoc) != null) {
					storeSOH = storeItemMap.get(itemandLoc);
					log.info("storeSOH" + itemQtyMap.get(custOrdItems.getItem()).getSoh());
					storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
				}

			} // end of 2nd if
			else {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item exist in map ,but find the SOH with next loc");
				Date d1 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "while calling SIM SOH" + d1);
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				SOH = (int) interfacePersistence.callSIMStoreInventory(custOrdItems.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
				Date d2 = new Date();
				log.info("omsCustOrdNo" + omsCustOrdNo + "after calling SIM SOH" + (d2.getTime() - d1.getTime()) + " in milliseconds");
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItems.getItem() + "store " + sourceLocId + " SOH" + SOH);
				ItemSOH itemSOH = itemQtyMap.get(custOrdItems.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + custOrdItems.getLineNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
				log.info("requestQty " + requestQty);
				if (SOH >= requestQty) {
					log.info("settting when SOH>=custOrdItems.getOrderQtySuom().longValue() ");
					itemSOH.setSoh(new BigDecimal(SOH - requestQty));
					availbleQuantity = requestQty;
					log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);

				} else {
					log.info("SOH is set to  0");
					itemSOH.setSoh(BigDecimal.ZERO);
					availbleQuantity = (int) SOH;
					log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);

				}
				log.info("setted itemSOH " + itemSOH.getSoh());
				itemSOH.setLocation(sourceLocId);
				itemQtyMap.put(custOrdItems.getItem(), itemSOH);
				log.info("putting the SOH in map " + itemQtyMap.get(custOrdItems.getItem()).getSoh());
				storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
				log.info("=====================================================================");

			}
		} // end of major 1st if
		else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not contain in itemQtyMap");
			Date d1 = new Date();
			log.info("omsCustOrdNo" + omsCustOrdNo + "while calling SIM SOH" + d1);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			SOH = (int) interfacePersistence.callSIMStoreInventory(custOrdItems.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
			Date d2 = new Date();
			log.info("omsCustOrdNo" + omsCustOrdNo + "after calling SIM SOH" + (d2.getTime() - d1.getTime()) + " in milliseconds");
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItems.getItem() + "sourceLocId " + sourceLocId + " SOH" + SOH);
			ItemSOH itemSOH = new ItemSOH();
			itemSOH.setLocation(sourceLocId);
			log.info("custOrdItems.getOrderQtySuom() " + custOrdItems.getOrderQtySuom());
			log.info("requestQty " + requestQty);
			log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
			long L_Pending_Qty_temp = requestQty - L_Cum_Ord_Qty;
			log.info("L_Pending_Qty_temp " + L_Pending_Qty_temp);
			L_Pending_Qty = L_Pending_Qty_temp;
			log.info("omsCustOrdNo " + omsCustOrdNo + " SOH " + SOH);
			log.info("omsCustOrdNo " + omsCustOrdNo + " custOrdItmDesc.getQuantity() " + custOrdItems.getOrderQtySuom());
			if (SOH >= requestQty) {
				log.info("omsCustOrdNo " + omsCustOrdNo + " inside SOH>=custOrdItmDesc.getQuantity().intValue() " + new BigDecimal(SOH - requestQty));
				itemSOH.setSoh(new BigDecimal(SOH - requestQty));
				availbleQuantity = requestQty;
				log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
			} else {
				log.info("omsCustOrdNo " + omsCustOrdNo + " inside else for SOH>=requestQty condition");
				itemSOH.setSoh(BigDecimal.ZERO);
				availbleQuantity = (int) SOH;
				log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + " putting into itemQtyMap map Location" + itemSOH.getLocation());
			log.info("omsCustOrdNo " + omsCustOrdNo + " putting into itemQtyMap map SOH " + itemSOH.getSoh());
			itemQtyMap.put(custOrdItems.getItem(), itemSOH);
			log.info("omsCustOrdNo " + omsCustOrdNo + " putting into storeItem map " + itemSOH.getSoh());
			storeSOH = itemQtyMap.get(custOrdItems.getItem()).getSoh();
			log.info("omsCustOrdNo " + omsCustOrdNo + " storeSOH " + storeSOH);
			storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
			log.info("omsCustOrdNo " + omsCustOrdNo + " after putting into the storeMap " + storeItemMap.keySet());
			log.info("omsCustOrdNo " + omsCustOrdNo + "itemQty map does not contain values with SOH=" + itemSOH.getSoh());
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Completed the try block for Duplicate items -Fulfillment Logic for line No " + custOrdItems.getLineNo());

		return availbleQuantity;

	}

	public BigDecimal findCombinationId(CustomerOrder input, CustomerOrderItems customerOrderItems)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		String shipClassifcation = null;
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		BigDecimal combinationId = BigDecimal.ZERO;
		FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
		String deliverZone = null;
		String marketPlaceInd = null;
		String applicationId = "E-COMMERCE";
		String shipToStore = "N";
		if("ODDSMALL".equals(input.getDeliveryModeType())){
			 deliverZone = input.getCustomerOrderAddress().getDeliver_zone();
		}
		if(customerOrderItems.getMarketplaceInd() != null) {
			marketPlaceInd = customerOrderItems.getMarketplaceInd();
			log.info("marketPlaceInd -  " + marketPlaceInd);
		}
		try {
			if (input.getDeliveryType().equals("S") || input.getDeliveryType().equals("SS") || input.getDeliveryType().equals("SC")) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Ship to customer");

				shipClassifcation = oMSUtilCommons.findShipmentClassification(customerOrderItems.getItem(), new BigDecimal(input.getOrderRequestorId()),
						customerOrderItems.getShippingClassification().toUpperCase());

				combinationId = findNextfulfillLoc.processFulfillmentMatrixGetCombID(new BigDecimal(input.getOrderRequestorId()), shipClassifcation.toUpperCase(),
						input.getCustomerOrderAddress().getDeliverCity().toUpperCase(), input.getDeliveryType(), deliverZone, marketPlaceInd, applicationId, shipToStore);

				log.info("omsCustOrdNo " + omsCustOrdNo + "*********shipClassifcation" + shipClassifcation);
			} else {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Customer pick up");
				shipClassifcation = oMSUtilCommons.findShipmentClassification(customerOrderItems.getItem(), new BigDecimal(input.getPickLoc()),
						customerOrderItems.getShippingClassification().toUpperCase());
				log.info("omsCustOrdNo " + omsCustOrdNo + "*********shipClassifcation" + shipClassifcation);
				combinationId = findNextfulfillLoc.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(input.getPickLoc()), shipClassifcation.toUpperCase(), input.getDeliveryType(), deliverZone, marketPlaceInd, applicationId, shipToStore);
			}
		} catch (Exception e) {
			throw new SOAPException("UNAVL_COMB_ID");
		}
		return combinationId;
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
		log.info("ARRAY LIST TEMP CO FO ***************: " + omsTempCoFo);

		log.info("MAP OBJECT ********************" + sohMap);
		return omsTempCoFo;
	}

	public int getWHSOH(CustomerOrderItems custOrdItems, CustomerOrder input, BigDecimal sourceLocationId, BigDecimal fulfillLocId, long L_Pending_Qty) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int availableQuantity = 0;
		int requestdQty = (int) L_Pending_Qty;
		int SOH = 0;
		virtualWH = sourceLocationId;
		log.info("omsCustOrdNo" + omsCustOrdNo + "Processing for line No " + custOrdItems.getLineNo() + "and item " + custOrdItems.getItem() + " in WH");
		// If Source_loc_type =�WH� then query WH table in RMS
		// select physical_wh,channel_id from wh where wh='101';
		List<Object[]> tempWhObject = session.getWhFindPhysicalWH(sourceLocationId);
		BigDecimal physicalWH = BigDecimal.ZERO;
		BigDecimal channelId = BigDecimal.ZERO;
		for (Object[] result : tempWhObject) {

			physicalWH = new BigDecimal(result[0].toString());
			channelId = new BigDecimal(result[1].toString());
			log.info("omsCustOrdNo" + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
		}
		if (input.getOrderCreateReserveInd().equals("C")) {
			sourceLocId = physicalWH;
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + " fulfillLocId for WH" + fulfillLocId + " Source loc id is " + sourceLocId);
		List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);

		int i = 0;
		while (i < locList.size()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
			i++;
		}
		OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
		log.info("virtualWH " + virtualWH);
		log.info("sourceLocId " + sourceLocId);
		String applicationId = "E-COMMERCE";
		SOH = omsUtilCommons.checkSOHForWH(custOrdItems.getItem(), locList, applicationId).intValue();  
		log.info("omsCustOrdNo" + omsCustOrdNo + " SOH for item " + custOrdItems.getItem() + "Line No " + custOrdItems.getLineNo() + "is " + SOH);

		if (SOH < 0) {
			SOH = 0;
		}

		if (SOH >= requestdQty) {
			availableQuantity = requestdQty;
		} else if (SOH <= requestdQty) {
			availableQuantity = SOH;
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + " SOH for item " + custOrdItems.getItem() + " and Line No " + custOrdItems.getLineNo() + " after calculation returning the availableQuantity is "
				+ availableQuantity);
		return availableQuantity;
	}

	public int processBackOrderItem(CustomerOrderItems custOrdItems, CustomerOrder input, BigDecimal L_Pending_Qty) throws SOAPException {
		Date date1 = new Date();
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + " calling process backorder for  OMSCustOrdNo " + omsCustOrdNo + " " + date1);
		log.info("<----------------Begin of processBackOrderItem method ---------------------------->");
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "processBackOrderItem");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		int fulfilledQty = 0;
		int boFulfilledQty = 0;
		try {
			BigDecimal currentPendingQty = BigDecimal.ZERO;
			List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combId);

			BigDecimal alloctedInventory = BigDecimal.ZERO;
			CustFutureInvPosition custFutureInvPosition = null;
			currentPendingQty = L_Pending_Qty;
			log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty=" + L_Pending_Qty);

			int i = 0;
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilledQty=" + fulfilledQty + "omsFulfillMatrixExtDetailList.size()" + omsFulfillMatrixExtDetailList.size());

			while (fulfilledQty != L_Pending_Qty.intValue() && i < omsFulfillMatrixExtDetailList.size()) {
				String boIndicator = "N";
				OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail = omsFulfillMatrixExtDetailList.get(i);
				BigDecimal boCombId = omsFulfillMatrixExtDetail.getCombinationId();
				log.info("omsCustOrdNo " + omsCustOrdNo + " boCombId " + boCombId);
				boolean itemLocked = false;
				i++;
				try {

					// log.info("omsCustOrdNo "+omsCustOrdNo +"alloctedInventory "+alloctedInventory
					// +"for location "+omsFulfillMatrixExtDetail.getLocation());
					CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
					BigDecimal physicalWH = BigDecimal.ZERO;
					BigDecimal channelId = BigDecimal.ZERO;
					try {
						log.info("try block-1");
						if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
							sourceLocType = "W";
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());

							for (Object[] result : tempWhObject) {

								physicalWH = new BigDecimal(result[0].toString());
								// omsFulfillMatrixExtDetail.setLocation(physicalWH);
								channelId = new BigDecimal(result[1].toString());
								log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
							}

						}
						/// code For Synchronization
					} catch (Exception g) {
						log.info("omsCustOrdNo : " + omsCustOrdNo + " try block-1");
						log.info("omsCustOrdNo : " + omsCustOrdNo + " Exception occured" + g.getMessage());
					}
						
					itemLocked = PreOrderItemLocationSync.obtainLock(custOrdItems.getItem(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());

					BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();
					// alloctedInventory =
					// session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),
					// custOrdItems.getItem());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Thread.current thread " + Thread.currentThread().getName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Before calling getAllocatedInventoryfromBackOrderDTL method alloctedInventory " + alloctedInventory);
//					alloctedInventory = backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(omsFulfillMatrixExtDetail.getLocation(), custOrdItems.getItem());
//					if (alloctedInventory == null) {
//						alloctedInventory = BigDecimal.ZERO;
//					}
					
					log.info("omsCustOrdNo " + omsCustOrdNo + "alloctedInventory " + alloctedInventory);
					try {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Finding bo indicator for location" + omsFulfillMatrixExtDetail.getLocation());
						boIndicator = oMSUtilCommons.getBOIndicator(omsFulfillMatrixExtDetail.getLocation(), custOrdItems.getItem());

						log.info("omsCustOrdNo " + omsCustOrdNo + "fetched indicator value  from table is +++++++++++===" + boIndicator);
						if (boIndicator == null || boIndicator.isEmpty()) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "boIndicator is null ,setting it to N");
							boIndicator = "N";
						} else if (boIndicator.equals("Y")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "inside indicator Y");
							custFutureInvPosition = checkItemLocSOH.findFutInvDateAndQty(custOrdItems.getItem(), omsFulfillMatrixExtDetail.getLocation().longValue(), alloctedInventory,
									L_Pending_Qty, omsFulfillMatrixExtDetail.getLocationType(), custOrdItems.getExpectedDeliveryDateTime() != null ? custOrdItems.getExpectedDeliveryDateTime().toGregorianCalendar().getTime() : null);
						
							log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition values fetched" + custFutureInvPosition.getExpectedDate() + "qty="
									+ custFutureInvPosition.getExpectedQty());
						}

					} catch (Exception e) {
						log.info("omsCustOrdNo : " + omsCustOrdNo + " Exception occured while checking BO indicator and future inventory, and the error message is " + e.getMessage());
						boIndicator = "N";
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());

					log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetail.getLocation().longValue() " + omsFulfillMatrixExtDetail.getLocation().longValue());

					if (boIndicator.equals("Y")) {
						if (custFutureInvPosition.getExpectedDate() != null) {

//							int qtyToBeFulfilled = custFutureInvPosition.getExpectedQty().intValue() - alloctedInventory.intValue(); 
							int qtyToBeFulfilled = custFutureInvPosition.getExpectedQty().intValue();
							log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToBeFulfilled=" + qtyToBeFulfilled);
							if (qtyToBeFulfilled > 0) {

								fulfilledQty = fulfilledQty + qtyToBeFulfilled;

								OmsBackOrderDtl omsBackOrderDtl = new OmsBackOrderDtl();
								omsBackOrderDtl.setSourceLoc(omsFulfillMatrixExtDetail.getLocation());
								omsBackOrderDtl.setSourceLocType(omsFulfillMatrixExtDetail.getLocationType());
								omsBackOrderDtl.setFulfillLocType(omsFulfillMatrixExtDetail.getDeliveryFromLocType());
								omsBackOrderDtl.setFulfillLoc(omsFulfillMatrixExtDetail.getDeliveryFromLoc());
								omsBackOrderDtl.setFulInvAvlDate(new Timestamp(custFutureInvPosition.getExpectedDate().getTime()));
								omsBackOrderDtl.setCreateDatetime(new Timestamp(new Date().getTime()));
								omsBackOrderDtl.setItem(custOrdItems.getItem());
								omsBackOrderDtl.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								omsBackOrderDtl.setOmsCustOrdNo(omsCustOrdNo);
								omsBackOrderDtl.setCombinationId(omsFulfillMatrixExtDetail.getCombinationId());
								BigDecimal qtyToPersist = null;
								if (qtyToBeFulfilled <= currentPendingQty.intValue()) {
									currentPendingQty = new BigDecimal(currentPendingQty.intValue() - qtyToBeFulfilled);
									log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToPersist=" + qtyToPersist);
									qtyToPersist = new BigDecimal(qtyToBeFulfilled);

								} else {

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

									InterfacePersistence interfacePersistence = new InterfacePersistence();
									if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc=" + physicalWH);

										// Added try catch block in case of any issue in calling the backorder WS
										try {
											interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, physicalWH.longValue(), "W", custOrdItems.getStandardUom(), channelId);
										} catch (Exception e) {
											log.info("omsCustOrdNo : " + omsCustOrdNo + " Error occured while call ws Back Order web service call For wareHouse");
											// put into republish batch to publish the record
											insertIntoRepublishBackOrder(custOrdItems.getItem(), physicalWH.longValue(), "W", channelId, custOrdItems.getStandardUom(), qtyToPersist);
											// abc(
											// custOrdItems.getItem(),physicalWH.longValue(),"W",channelId,custOrdItems.getStandardUom(),qtyToPersist);
										}
									} else {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc="
												+ omsFulfillMatrixExtDetail.getLocation());
										// Added try catch block in case of any issue in calling the backorder WS
										try {
											interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, omsFulfillMatrixExtDetail.getLocation().longValue(), "S",
													custOrdItems.getStandardUom(), channelId);
										} catch (Exception e) {
											insertIntoRepublishBackOrder(custOrdItems.getItem(), physicalWH.longValue(), "W", channelId, custOrdItems.getStandardUom(), qtyToPersist);
											log.info("omsCustOrdNo : " + omsCustOrdNo + " Error occured while call ws Back Order web service call For store");
										}
									}
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Item---->" + custOrdItems.getItem() + "<---->");
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Location---->" + omsFulfillMatrixExtDetail.getLocation() + "<---->");
									// Boolean result = itemlocObj.getItemlocTreemap().containsKey( itemloc );
									// 27-Oct-20 - Madhu/Renuga - modified this code to handle pre order issue
									// added as part of thread stuck issue
									try {
										log.info("omsCustOrdNo : " + omsCustOrdNo + " Before inserting into back order dtl table");
										session.persistOmsBackOrderDtl(omsBackOrderDtl);
										log.info("omsCustOrdNo : " + omsCustOrdNo + " Successfully inserted into back order dtl table");
									} catch (Exception ex) {
										log.error("omsCustOrdNo : " + omsCustOrdNo + " Exception occured while persisting to back order dtl table, and the error is ", ex);
										throw new Exception(ex.getMessage());
									}
								} // end of qtyToPersist > 0

								log.info("omsCustOrdNo " + omsCustOrdNo + "Back order successful");
							}
							// break;
						}
					}
					log.info("<------------------End of Back order indicator If Part------------------------>");
					log.info("<-----omsCustOrdNo-------->" + omsCustOrdNo + "<-----Need to Remove Item-------------->" + custOrdItems.getItem() + " and location" + ""
							+ omsFulfillMatrixExtDetail.getLocation() + "from Temp  map matrix--------------->");
					ItemLoc itemloc = new ItemLoc();
					itemloc.setItem(custOrdItems.getItem());
					itemloc.setLocation(omsFulfillMatrixExtDetail.getLocation());
				} catch (Exception f) {
					log.error("omsCustOrdNo " + omsCustOrdNo + " ", f);
				} finally {
					try {
						if (itemLocked) {
							PreOrderItemLocationSync.releaseLock(custOrdItems.getItem(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());
						}
					} catch (Exception e) {
						log.warn("Error while relaesing the lock " + custOrdItems.getItem() + "~" + omsFulfillMatrixExtDetail.getLocation() + " for the oms cust ord no# " + omsCustOrdNo, e);
					}
				}

			} // end of While

		} catch (Exception h) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in back order " + h);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
		}
		Date date2 = new Date();
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + " completed processsing backorder for  OMSCustOrdNo " + omsCustOrdNo + " " + date2);
		log.info("CustomerOrder No is " + input.getCustomerOrderNo() + "Time consumed in calling process backorder for " + omsCustOrdNo + " OMSCustOrdNo " + (date1.getTime() - date2.getTime())
				+ "milli seconds");
		log.info("omsCustOrdNo " + omsCustOrdNo + " boFulfilledQty=" + boFulfilledQty);
		return boFulfilledQty;

	}

	public int processPO(CustomerOrderItems custOrdItems, CustomerOrder input, BigDecimal fulfillLocId, String deliveryLocType, long L_Pending_Qty, long SOH) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Boolean flag = Boolean.FALSE;
		int availableQuantity = 0;

		CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
		log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
		long partialPoQty = L_Pending_Qty - SOH;
		log.info("omsCustOrdNo " + omsCustOrdNo + "isPartialPO=true and partialPoQty=" + partialPoQty);
		String item_status = checkItemLocSOH.findItemStatus(custOrdItems.getItem(), fulfillLocId);
		log.info(" Item Status is " + item_status);
		if (item_status.equals("A") == false) {
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
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
			log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + fulfillLocId.longValue());
			sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), fulfillLocId.longValue());
		} else {
			log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + input.getOrderRequestorId());
			/**
			 * If delivery location type is 'V' Virtual store. then we are passing item and
			 * order requestor id(it is a location). The functions return the primary
			 * supplier of the given item and location (order requestor id). After that We
			 * are checking the direct_ship_ind indicator value 'Y' or not by Passing item
			 * and supplier. If it is 'Y' then the above supplier will be used to create the
			 * order Otherwise we will throw an Error.
			 */
			sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), input.getOrderRequestorId());
		}
		log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItems.getItem() + "...." + "Location" + sourceLocId.longValue());
		flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItems.getItem(), sourceLocId.longValue(), input.getOrderRequestorId());
		// nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(),
		// "Y"));
		if (flag == Boolean.FALSE) {
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
		}
		flag = checkItemLocSOH.checkSupplierTraitsofaGivenSupplier(sourceLocId.longValue());
		if (flag == Boolean.FALSE) {
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + sourceLocId);
		if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
		} else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
			if (item_status.equals("A") == false) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
			}
			log.info("Delivery Location Type Value is" + deliveryLocType);
			if ("S".equals(deliveryLocType)) {
				log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + fulfillLocId);
				sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), fulfillLocId.longValue());
			} else {
				log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + input.getOrderRequestorId());
				sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), input.getOrderRequestorId());

			}
			log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItems.getItem() + "...." + "Location" + sourceLocId.longValue());
			flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItems.getItem(), sourceLocId.longValue(), input.getOrderRequestorId());
			if (flag == Boolean.FALSE) {
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + sourceLocId);
			if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
			}

			// isPO = true;
		}
		availableQuantity = (int) partialPoQty;
		return availableQuantity;

	} // end of PO method

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> createFulfillDetailMap(Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap, int fulfillOrderNo) {
		log.info("omsCustOrdNo " + omsCustOrdNo + "inside createFulfillDetailMap method ");
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		for (BigDecimal key : sohMap.keySet()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());
			ArrayList<OmsTempCoFo> list = sohMap.get(key);
			fulfillDetailMap.put(key, list);

			for (OmsTempCoFo temp : list) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Fulfilmetn OrderNo=" + temp.getFulfillOrderNo() + "for item=" + temp.getItem());
			}

		}
		return fulfillDetailMap;
	}

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> getMap() {
		return fulfillDetailMap;
	}

	public ItemUnavailabilityStatus OrderPickUpModule(CustomerOrder input, BigDecimal omsCustOrderNo)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException {
		String status = "PASS";
		log.info("omsCustOrdNo " + omsCustOrdNo + "***OrderPickUpModule started***");
		omsCustOrdNo = omsCustOrderNo;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		unavailableItemsList = new ArrayList<ItemAvailability>();
		ItemUnavailabilityStatus itemUnavailabilityStatus = new ItemUnavailabilityStatus();
		itemUnavailabilityStatus.setStatus(status);
		itemUnavailabilityStatus.setUnavailableInvtemsList(unavailableItemsList);

		BigDecimal fulfillLocId = BigDecimal.ZERO;
		if (input.getPickLoc() != null) {
			nextLoc = new BigDecimal(input.getPickLoc());
			sourceLocId = new BigDecimal(input.getPickLoc());
			fulfillLocId = new BigDecimal(input.getPickLoc());
		}
		int priority = 1;
		BigDecimal combID = BigDecimal.ZERO;

		processSplitOrders(omsCustOrdNo, input, priority, combID, fulfillLocId);

		if (unavailableItemsList.size() > 0) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "unavailableItemsList size" + unavailableItemsList.size());
			List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
			for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
				log.info("Roll backing back order for " + omsBackOrderDtl.getItem());
				InterfacePersistence interfacePersistence = new InterfacePersistence();
				BigDecimal physicalWH = BigDecimal.ZERO;
				BigDecimal channelId = BigDecimal.ZERO;
				if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
					List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());

					for (Object[] result : tempWhObject) {

						physicalWH = new BigDecimal(result[0].toString());
						// omsFulfillMatrixExtDetail.setLocation(physicalWH);
						channelId = new BigDecimal(result[1].toString());
						log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + omsBackOrderDtl.getItem() + "qty=" + omsBackOrderDtl.getSourceQty().negate() + "loc=" + physicalWH);

					interfacePersistence.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsBackOrderDtl.getSourceQty().negate(), physicalWH.longValue(), "W", "EA", channelId);
				} else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + omsBackOrderDtl.getItem() + "qty=" + omsBackOrderDtl.getSourceQty().negate() + "loc="
							+ omsBackOrderDtl.getSourceLoc());

					interfacePersistence.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsBackOrderDtl.getSourceQty().negate(), omsBackOrderDtl.getSourceLoc().longValue(), "S", "EA", channelId);
				}

				omsBackOrderDtl.setSourceQty(BigDecimal.ZERO);
				omsBackOrderDtl.setBackorderStatus("S");
				session.mergeOmsBackOrderDtl(omsBackOrderDtl);

			}
			status = "FAIL";
			itemUnavailabilityStatus.setStatus(status);
			itemUnavailabilityStatus.setUnavailableInvtemsList(unavailableItemsList);
			log.info("Size is ----------------" + itemUnavailabilityStatus.getUnavailableInvtemsList().size());
		}

		return itemUnavailabilityStatus;

	}

	public CustomerOrderResponse sendFailedResposne(CustomerOrder input) throws SOAPException {
		OMSCustomerOrderBean bean = new OMSCustomerOrderBean();
		return bean.createResponseForFailedItems(input, omsCustOrdNo, unavailableItemsList);

	}

	public List<ItemAvailability> getUnavailabeItems() {
		return unavailableItemsList;
	}

	// Method added to insert data in republish data table

	private void insertIntoRepublishBackOrder(String item, Long location, String locType, BigDecimal channelId, String unitOfMeasure, BigDecimal backorderqty) throws SOAPException {
		try {
			log.info("-------------------------Begin of insertInoBackOrderRepublish()-----------------------------------");
			BackOrderRepublish brp = new BackOrderRepublish();
			String xmlMessage = brp.backOrderXML(item, location, locType, channelId, unitOfMeasure, backorderqty.longValue());
			log.info(" from backOrderXML --- xmlMessage = " + xmlMessage);
			log.info("-----------Before getting connection inside function insertInoBackOrderRepublish()----------");
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			OmsCustOrdHead omsCustOrdhead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			log.info("omsCustOrdhead ----- = " + omsCustOrdhead);
			BigDecimal webserviceId = session.getOmsWebserviceUriDetailFindWebServiceId("RMS_BACK_ORDER");

			OmsRepublishData omsRepublishData = new OmsRepublishData();

			omsRepublishData.setApplicationId(omsCustOrdhead.getApplicationId());
			log.info("Application_id =  " + omsCustOrdhead.getApplicationId());
			omsRepublishData.setTransactionKey(omsCustOrdhead.getCustOrderNo());
			omsRepublishData.setFirstAttemptDatetime(new Timestamp(new Date().getTime()));
			omsRepublishData.setErrorMsg("UNABLE TO CALL SEIBEL WEB SERVICE");
			omsRepublishData.setAttemptCnt(BigDecimal.ZERO);
			omsRepublishData.setLastAttemptTime(new Timestamp(new Date().getTime()));
			omsRepublishData.setRepublishStatus("F");
			log.info("omsRepublishData --- Status = F  ");
			log.info("setting XmlMsg into omsRepublishData----");
			omsRepublishData.setXmlMsg(xmlMessage);
			omsRepublishData.setWebServiceId(webserviceId.intValue() + "");
			session.persistOmsRepublishData(omsRepublishData);
			log.info("persisted into  persistOmsRepublishData(omsRepublishData)----- ");
		} catch (Exception e) {
			log.info("----------------------------------------------------------------------------------------------------");
		}
	}

	// Start process back order item for carrera

	public int processBackOrderItemforCarrera(CustomerOrderItems custOrdItems, CustomerOrder input, BigDecimal L_Pending_Qty) throws SOAPException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "processBackOrderItem for Carrera");
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "combination ID from item request " + custOrdItems.getCombinationId());
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "SourceLocType from item request " + custOrdItems.getItemSourceLoc());
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "SourceLocation from item request " + custOrdItems.getItemSourceLocType());
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "FulFillLocType from item request " + custOrdItems.getItemFulfillLoc());
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "FulFillLocation from item request " + custOrdItems.getItemFulfillLocType());
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "priority from item request " + custOrdItems.getPriority());

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		int fulfilledQty = 0;
		int boFulfilledQty = 0;
		try {
			BigDecimal currentPendingQty = BigDecimal.ZERO;
			List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(new BigDecimal(custOrdItems.getCombinationId()));
			BigDecimal alloctedInventory = null;
			CustFutureInvPosition custFutureInvPosition = null;
			currentPendingQty = L_Pending_Qty;
			boolean itemLocked = false;
			log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty=" + L_Pending_Qty);
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilledQty=" + fulfilledQty + "omsFulfillMatrixExtDetailList.size()" + omsFulfillMatrixExtDetailList.size());

			if (fulfilledQty != L_Pending_Qty.intValue() && omsFulfillMatrixExtDetailList.size() > 0) {
				String boIndicator = "N";
				BigDecimal boCombId = new BigDecimal(custOrdItems.getCombinationId());
				log.info("omsCustOrdNo " + omsCustOrdNo + " boCombId " + boCombId);

				try {
					CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
					BigDecimal physicalWH = BigDecimal.ZERO;
					BigDecimal channelId = BigDecimal.ZERO;
					try {
						if (custOrdItems.getItemSourceLocType().equalsIgnoreCase("WH")) {
							sourceLocType = "W";
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(new BigDecimal(custOrdItems.getItemSourceLoc()));

							for (Object[] result : tempWhObject) {

								physicalWH = new BigDecimal(result[0].toString());
								// omsFulfillMatrixExtDetail.setLocation(physicalWH);
								channelId = new BigDecimal(result[1].toString());
								log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
							}

						}
						/// code For Synchronization
						log.info("<------omsCustOrdNo-------->" + omsCustOrdNo + "<-----Creating  Item Loc Object------------------------------>");
					} catch (Exception g) {
						log.error("Exception occured" + g);
					}

					itemLocked = PreOrderItemLocationSync.obtainLock(custOrdItems.getItem(), new BigDecimal(custOrdItems.getItemSourceLoc()), omsCustOrdNo.toPlainString());
					
					log.info("<---------------------- End Of While--------------------------------->");
					BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();
					// alloctedInventory =
					// session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),
					// custOrdItems.getItem());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Thread.current thread " + Thread.currentThread().getName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Before calling getAllocatedInventoryfromBackOrderDTL method alloctedInventory " + alloctedInventory);
					alloctedInventory = backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(new BigDecimal(custOrdItems.getItemSourceLoc()), custOrdItems.getItem());
					if (alloctedInventory == null) {
						alloctedInventory = BigDecimal.ZERO;
					}
					
					log.info("omsCustOrdNo " + omsCustOrdNo + "alloctedInventory " + alloctedInventory);
					try {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Finding bo indicator for location" + custOrdItems.getItemSourceLoc());
						boIndicator = oMSUtilCommons.getBOIndicator(new BigDecimal(custOrdItems.getItemSourceLoc()), custOrdItems.getItem());

						log.info("omsCustOrdNo " + omsCustOrdNo + "fetched indicator value  from table is +++++++++++===" + boIndicator);
						if (boIndicator == null || boIndicator.isEmpty()) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "boIndicator is null ,setting it to N");
							boIndicator = "N";
						} else if (boIndicator.equals("Y")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "inside indicator Y");
							custFutureInvPosition = checkItemLocSOH.findFutInvDateAndQty(custOrdItems.getItem(), custOrdItems.getItemSourceLoc(), alloctedInventory, L_Pending_Qty,
									custOrdItems.getItemSourceLocType(), custOrdItems.getExpectedDeliveryDateTime() != null ? custOrdItems.getExpectedDeliveryDateTime().toGregorianCalendar().getTime() : null);
							log.info("omsCustOrdNo " + omsCustOrdNo + "custFutureInvPosition values fetched" + custFutureInvPosition.getExpectedDate() + "qty="
									+ custFutureInvPosition.getExpectedQty());
						}

					} catch (Exception e) {
						boIndicator = "N";
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());

					log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItemSourceLoc() " + custOrdItems.getItemSourceLoc());

					if (boIndicator.equals("Y")) {
						if (custFutureInvPosition.getExpectedDate() != null) {

							int qtyToBeFulfilled = custFutureInvPosition.getExpectedQty().intValue() - alloctedInventory.intValue();
							log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToBeFulfilled=" + qtyToBeFulfilled);
							if (qtyToBeFulfilled > 0) {

								fulfilledQty = fulfilledQty + qtyToBeFulfilled;

								OmsBackOrderDtl omsBackOrderDtl = new OmsBackOrderDtl();
								omsBackOrderDtl.setSourceLoc(new BigDecimal(custOrdItems.getItemSourceLoc()));
								omsBackOrderDtl.setSourceLocType(custOrdItems.getItemSourceLocType());
								omsBackOrderDtl.setFulfillLocType(custOrdItems.getItemFulfillLocType());
								omsBackOrderDtl.setFulfillLoc(new BigDecimal(custOrdItems.getItemFulfillLoc()));
								omsBackOrderDtl.setFulInvAvlDate(new Timestamp(custFutureInvPosition.getExpectedDate().getTime()));
								omsBackOrderDtl.setCreateDatetime(new Timestamp(new Date().getTime()));
								omsBackOrderDtl.setItem(custOrdItems.getItem());
								omsBackOrderDtl.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								omsBackOrderDtl.setOmsCustOrdNo(omsCustOrdNo);
								omsBackOrderDtl.setCombinationId(new BigDecimal(custOrdItems.getCombinationId()));
								BigDecimal qtyToPersist = null;
								if (qtyToBeFulfilled <= currentPendingQty.intValue()) {
									currentPendingQty = new BigDecimal(currentPendingQty.intValue() - qtyToBeFulfilled);
									log.info("omsCustOrdNo " + omsCustOrdNo + "qtyToPersist=" + qtyToPersist);
									qtyToPersist = new BigDecimal(qtyToBeFulfilled);

								} else {

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

									InterfacePersistence interfacePersistence = new InterfacePersistence();
									if (custOrdItems.getItemSourceLocType().equals("WH")) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc=" + physicalWH);

										// Added try catch block in case of any issue in calling the backorder WS
										try {
											interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, physicalWH.longValue(), "W", custOrdItems.getStandardUom(), channelId);
										} catch (Exception e) {
											log.error("Error occured while call ws Back Order web service call For wareHouse");
											// put into republish batch to publish the record
											insertIntoRepublishBackOrder(custOrdItems.getItem(), physicalWH.longValue(), "W", channelId, custOrdItems.getStandardUom(), qtyToPersist);
											// abc(
											// custOrdItems.getItem(),physicalWH.longValue(),"W",channelId,custOrdItems.getStandardUom(),qtyToPersist);
										}
									} else {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc="
												+ custOrdItems.getItemSourceLoc());
										// Added try catch block in case of any issue in calling the backorder WS
										try {
											interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, custOrdItems.getItemSourceLoc().longValue(), "S",
													custOrdItems.getStandardUom(), channelId);
										} catch (Exception e) {
											insertIntoRepublishBackOrder(custOrdItems.getItem(), physicalWH.longValue(), "W", channelId, custOrdItems.getStandardUom(), qtyToPersist);
											log.error("Error occured while call ws Back Order web service call For store");
										}
									}
									log.info("<------- omsCustOrdNo----> " + omsCustOrdNo + "<-------Inserting Record into OmsBackOrderDtl Table ------------------------> ");
									log.info("<-----omsCustOrdNo ----->" + omsCustOrdNo + "<-----Executing Weblogic Thread --------------------->" + Thread.currentThread().getName() + "<--------->");
									ItemLoc itemloc = new ItemLoc();
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Item---->" + custOrdItems.getItem() + "<---->");
									itemloc.setItem(custOrdItems.getItem());
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Location---->" + new BigDecimal(custOrdItems.getItemSourceLoc()) + "<---->");
									itemloc.setLocation(new BigDecimal(custOrdItems.getItemSourceLoc()));
									session.persistOmsBackOrderDtl(omsBackOrderDtl);
								} // end of qtyToPersist > 0

								log.info("omsCustOrdNo " + omsCustOrdNo + "Back order successful");
							}
							// break;
						}
					}
					log.info("<------------------End of Back order indicator If Part------------------------>");
					log.info("<-----omsCustOrdNo-------->" + omsCustOrdNo + "<-----Need to Remove Item-------------->" + custOrdItems.getItem() + " and location" + ""
							+ new BigDecimal(custOrdItems.getItemSourceLoc()) + "from Temp  map matrix--------------->");
					
				} catch (Exception f) {
					log.info("omsCustOrdNo " + omsCustOrdNo + f);
					log.info("<--------omsCustOrdNo----> " + omsCustOrdNo + "<------If Some Error Occured then it should allow other request to Procced ---->" + f.getMessage() + "<---->");
					log.info("<------------Checking Item and Location Combination exist in OmsitemLocSync Table----------------------->");
					log.info("<-----omsCustOrdNo------>" + omsCustOrdNo + "<-------Item ----->" + custOrdItems.getItem() + "Location is ------------>" + new BigDecimal(custOrdItems.getItemSourceLoc())
							+ "<----->");
				} finally {
					try {
						if (itemLocked) {
							PreOrderItemLocationSync.releaseLock(custOrdItems.getItem(), new BigDecimal(custOrdItems.getItemSourceLoc()), omsCustOrdNo.toPlainString());
						}
					} catch (Exception e) {
						log.warn("Error while relaesing the lock " + custOrdItems.getItem() + "~" + new BigDecimal(custOrdItems.getItemSourceLoc()) + " for the oms cust ord no# " + omsCustOrdNo, e);
					}
				}

			} // end of if (replaced from while to if for carrera)
			else {
				log.info(" combination id doesnot exist in matrix detail for omsCustOrdNo " + omsCustOrdNo);
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				omsCustOrdHead.setStatus("F");
				session.mergeOmsCustOrdHead(omsCustOrdHead);
				throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_COMBID"));
			}

		} catch (Exception h) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in back order " + h);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + " boFulfilledQty=" + boFulfilledQty);
		return boFulfilledQty;

	}

	public BigDecimal getAvailableQtyFromOmsBackOrderDtl(BigDecimal location, String item, long lineNo) throws SOAPException

	{
		log.info("omsCustOrdNo " + omsCustOrdNo + " inside getAvailableQtyFromOmsBackOrderDtl");
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
		log.info("omsCustOrdNo " + omsCustOrdNo + " BO allocated inventory for the location" + location + " item " + item + " and lineNo " + lineNo + " is " + qty);

		return qty;
	}
} // end class
