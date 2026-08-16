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
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsErrorCodes;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.PreOrderItemLocationSync;
import com.logicinfo.oms.utils.ProjectUtils;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;

public class SourceLocIdentify {
	public SourceLocIdentify() {
		super();
	}

	BigDecimal omsCustOrdNo;
	public final static Logger log = ProjectUtils.getLog();
	Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = null;
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;
	static List<ItemAvailability> unavailableItemsList = null;
	boolean isPO = false;
	boolean isBackOrderChecked = false;

	public String OrderPickUpModule(CustomerOrder input, BigDecimal omsCustOrderNo)
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
		long SOH = 0L;
		unavailableItemsList = new ArrayList<ItemAvailability>();
		String fulFillLocType = "S";
		BigDecimal nextLoc = BigDecimal.ONE;
		BigDecimal sourceLocId = BigDecimal.ZERO;
		BigDecimal fulfillLocId = BigDecimal.ZERO;
		if (input.getPickLoc() != null) {
			nextLoc = new BigDecimal(input.getPickLoc());
			sourceLocId = new BigDecimal(input.getPickLoc());
			fulfillLocId = new BigDecimal(input.getPickLoc());
		}
		int priority = 1;
		String sourceLocType = "ST";
		BigDecimal combID = BigDecimal.ZERO;
		// Step 1 : If OMS_SPLIT_ORDER_IND=N
		if (session.getOmsSystemParametersFindIndValue("OMS_SPLIT_ORDER_IND", "OMS_SYSTEM_OPTION").equals("N")) {
			processNonSplitOrder(omsCustOrdNo, input, SOH, fulFillLocType, nextLoc, sourceLocId, sourceLocType, priority, combID, fulfillLocId);
		}
		// Step 2 : If OMS_SPLIT_ORDER_IND=Y
		else {
			// processSplitOrders(omsCustOrdNo, omsCustOrdItemList, omsCustOrdAddress,
			// omsCustOrdHead, SOH, fulFillLocType, nextLoc, sourceLocId, sourceLocType,
			// priority, combID, fulfillLocId);
			processSplitOrders(omsCustOrdNo, input, SOH, fulFillLocType, nextLoc, sourceLocId, sourceLocType, priority, combID, fulfillLocId);
		}
		// log.info("omsCustOrdNo "+omsCustOrdNo +"failed
		// item"+sohList.get(0).getItem()+"avilable
		// qty"+sohList.get(0).getAvailableQty());
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

		}
		return status;
	}

	public CustomerOrderResponse sendFailedResposne(CustomerOrder input) throws SOAPException {
		OMSCustomerOrderBean bean = new OMSCustomerOrderBean();
		return bean.createResponseForFailedItems(input, omsCustOrdNo, unavailableItemsList);

	}

	public List<ItemAvailability> getUnavailabeItems() {
		return unavailableItemsList;
	}

	public BigDecimal IdentifyVirutalLoc(String locationType, BigDecimal locID) throws SOAPException {
		BigDecimal virtualId = BigDecimal.ZERO;
		log.info("omsCustOrdNo " + omsCustOrdNo + "***Start-IdentifyVirutalLoc***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCustOrdNo " + omsCustOrdNo + "locID :" + locID);
		log.info("omsCustOrdNo " + omsCustOrdNo + "locationType :" + locationType);
		virtualId = session.getOmsVirtualStrMatrixFindVirtualLocID(locID, locationType);
		log.info("omsCustOrdNo " + omsCustOrdNo + "virtualId:" + virtualId);
		log.info("omsCustOrdNo " + omsCustOrdNo + "***end-IdentifyVirutalLoc***");
		return virtualId;
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------

	public void processNonSplitOrder(BigDecimal omsCustOrdNo, CustomerOrder input, long SOH, String fulFillLocType, BigDecimal nextLoc, BigDecimal sourceLocId, String sourceLocType, int priority,
			BigDecimal combID, BigDecimal fulfillLocId) throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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

		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("omsCustOrdNo " + omsCustOrdNo + "***OMS_SPLIT_ORDER = N processNonSplitOrder starts***");
		OmsFulfillMatrixExtDetail fulfillMatrixExtDetailResult = null;
		SourceLocIdentify sourceLocIdentify = new SourceLocIdentify();
		String deliverZone = null;
		String applicationId = "E-COMMERCE";
		String shipToStore = "N";
		sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		Map<String, BigDecimal> itemCounterMap = new HashMap<String, BigDecimal>();
		if ("ODDSMALL".equals(input.getDeliveryModeType())) {
			deliverZone = input.getCustomerOrderAddress().getDeliver_zone();
		}
		for (CustomerOrderItems custOrdItems : input.getCustomerOrderItems()) {
			// Step 1: If particular item is not marked as back order proceed else do
			// nothing
			priority = 1;
			if (custOrdItems.getBackOrderInd().equals("N")) {
				CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
				while (!nextLoc.equals(0)) {
					FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
					try {
						if (input.getDeliveryType().equals("S") || input.getDeliveryType().equals("SS") || input.getDeliveryType().equals("SC")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Condition : Ship to customer");
							// Find the combination id from fulfillmentMatrix
				 			combID = findNextfulfillLoc.processFulfillmentMatrixGetCombID(new BigDecimal(input.getOrderRequestorId()), custOrdItems.getShippingClassification(),
									input.getCustomerOrderAddress().getDeliverCity().toUpperCase(), input.getDeliveryType(), deliverZone, custOrdItems.getMarketplaceInd(), applicationId, shipToStore);
						} else {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Condition : Customer pick up");
							// Find the combination id from fulfillmentMatrix
							combID = findNextfulfillLoc.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(input.getPickLoc()), custOrdItems.getShippingClassification(), input.getDeliveryType(),
									deliverZone, custOrdItems.getMarketplaceInd(), applicationId, shipToStore);
						}
						// Find the next fulfill location based on combId and priority
						fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrix(combID, priority);
						nextLoc = fulfillMatrixExtDetailResult.getLocation();

						if (nextLoc.intValue() < 0) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Next location is -1,PO can be raised,finding supplier");
							String item_status = checkItemLocSOH.findItemStatus(custOrdItems.getItem(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
							if (item_status.equals("A") == false) {
								// Status of item is not approved,cannot fulfill the order.
								throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITEM_NOT_APPR"));
							}
							nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(), "Y"));

							isPO = true;
							// break;
						}
						sourceLocId = nextLoc;
						log.info("omsCustOrdNo " + omsCustOrdNo + "Source location is " + sourceLocId);
					} catch (Exception e) {
						log.error(e.getMessage());
						// move to next item an save SOH of this
						ItemAvailability itemAvailability = new ItemAvailability();
						itemAvailability.setItem(custOrdItems.getItem());
						itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
						itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
						itemAvailability.setAvailableQty(new BigDecimal(SOH));
						itemAvailability.setErrorMessage(e.getMessage());
						log.info("omsCustOrdNo " + omsCustOrdNo + "Putting into unavailableItemsList");
						unavailableItemsList.add(itemAvailability);
						OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
						omsCustOrdHead.setStatus("F"); // Failed
						session.mergeOmsCustOrdHead(omsCustOrdHead);
						break;
					}
					sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
					log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocType " + sourceLocType);
					fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc(); // added
					log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId " + fulfillLocId);

					if (fulfillMatrixExtDetailResult.getDeliveryFromLocType().equals("WH")) {
						// Find virtual loc id
						fulfillLocId = sourceLocIdentify.IdentifyVirutalLoc(fulfillMatrixExtDetailResult.getDeliveryFromLocType(), sourceLocId);
						fulFillLocType = "V";

					}
					if (sourceLocType.equals("ST")) {
						// Find SOH from SIM
						log.info("omsCustOrdNo " + omsCustOrdNo + "Calling SIM webservice for finding SOH for item =" + custOrdItems.getItem() + " in store=" + nextLoc);
						InterfacePersistence interfacePersistece = new InterfacePersistence();
						try {
							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							SOH = interfacePersistece.callSIMStoreInventory(custOrdItems.getItem(), nextLoc, omsCustOrdHead.getApplicationId());
						} catch (Exception e) {
							throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
						}
					} else if (sourceLocType.equals("WH")) {
						// Step 2: Check stock on hand in DAS schema in case of WH
						log.info("omsCustOrdNo " + omsCustOrdNo + "Finding SOH for item=" + custOrdItems.getItem() + "for WH with Physical Loc=" + sourceLocId);
						List<BigDecimal> locList = session.getWhFindByPhysicalWH(sourceLocId);
						int i = 0;
						while (i < locList.size()) {
							log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
							i++;
						}
						SOH = checkItemLocSOH.checkSOHForWH(custOrdItems.getItem(), locList);
					}
					if (itemCounterMap.containsKey(custOrdItems.getItem())) {
						itemCounterMap.get(custOrdItems.getItem()).add(BigDecimal.ONE);
					} else {
						itemCounterMap.put(custOrdItems.getItem(), BigDecimal.ONE);
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "item=" + custOrdItems.getItem() + "SOH=" + SOH + "next loc=" + nextLoc);
					OmsTempCoFo omsTempCoFo = new OmsTempCoFo();
					omsTempCoFo.setOmsCustOrdNo(omsCustOrdNo);
					omsTempCoFo.setFulfillOrderNo(itemCounterMap.get(custOrdItems.getItem()));
					omsTempCoFo.setItem(custOrdItems.getItem());
					omsTempCoFo.setOrderQty(custOrdItems.getOrderQtySuom());
					omsTempCoFo.setSourceLocId(sourceLocId);
					log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocType " + sourceLocType);

					omsTempCoFo.setSourceLocationType(sourceLocType);
					omsTempCoFo.setFulfillLocId(fulfillLocId);
					omsTempCoFo.setFulfillLocationType(fulFillLocType);
					omsTempCoFo.setFoConfQty(BigDecimal.ZERO);
					omsTempCoFo.setRmsResponseCode("");
					omsTempCoFo.setRmsErrorMsg("");
					log.info("omsCustOrdNo " + omsCustOrdNo + "Item:" + omsTempCoFo.getItem() + "-OrderedQty=" + omsTempCoFo.getOrderQty() + "-FulfilOrdNo=" + omsTempCoFo.getFulfillOrderNo());
					omsTempCoFo.setCreateDatetime(new Timestamp(new Date().getTime()));
					omsTempCoFo.setStatus("N"); // New
					if (SOH >= custOrdItems.getOrderQtySuom().longValue() || isPO == true) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "inside SOH>ord_qty");
						if (sohMap.get(sourceLocId) == null || sohMap.get(sourceLocId).size() == 0) {
							// log.info("omsCustOrdNo "+omsCustOrdNo +"Creating map with new key+" +
							// sourceLocId);
							ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
							tempList.add(omsTempCoFo);
							sohMap.put(sourceLocId, tempList);
						} else {
							// log.info("omsCustOrdNo "+omsCustOrdNo +"Adding temp record with existing
							// key=" + sourceLocId);
							ArrayList<OmsTempCoFo> existingList = sohMap.get(sourceLocId);
							existingList.add(omsTempCoFo);
							sohMap.put(sourceLocId, existingList);
						}
						isPO = false;

						break;
					} else {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Condition :SOH < custOrdItems.getOrderQtySuom().longValue()");

						priority++;
					}

				} // while (!nextLoc.equals(0)) ends

			} // custOrdItems.getBackOrderInd().equals("N") ends
			else {
				// Its a back order
				// interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(),
				// custOrdItems.getOrderQtySuom(),
				// input.getOrderRequestorId(), "S",custOrdItems.getStandardUom());
			}
		} // for loop item ends here
		fulfillDetailMap = createFulfillDetailMap(sohMap, 1); // start the fulfilmentOrdeNo from 1

	}

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> getMap() {
		return fulfillDetailMap;
	}

	public void processSplitOrders(BigDecimal omsCustOrdNo, CustomerOrder input, long SOH, String fulFillLocType, BigDecimal nextLoc, BigDecimal sourceLocId, String sourceLocType, int priority,
			BigDecimal combID, BigDecimal fulfillLocId) throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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
		Map<String, BigDecimal> storeItemMap = new HashMap<String, BigDecimal>();
		Map<String, ItemSOH> itemQtyMap = new HashMap<String, ItemSOH>();
		Map<String, FulfillOrdandSourceLocPOJO> itemCounterMap = new ConcurrentHashMap<String, FulfillOrdandSourceLocPOJO>();
		int maxFulfillOrderNo = oMSUtilCommons.returnMaxFulFilOrdNoECOM(input.getCustomerOrderNo());
		int intialNo = maxFulfillOrderNo;
		log.info("================maxFulfillOrderNo=====================" + maxFulfillOrderNo);
		BigDecimal storeSOH = BigDecimal.ZERO;
		BigDecimal virtualWH = BigDecimal.ZERO;
		String deliveryLocType = null;
		Boolean flag = Boolean.FALSE;
		String applicationId = "E-COMMERCE";
		String shipToStore = "N";
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treemap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		for (CustomerOrderItems custOrdItems : input.getCustomerOrderItems()) {
			boolean isPartialPO = false;
			int partialPoQty = 0;
			isBackOrderChecked = false;
			// Step 1: If particular item is not marked as back order proceed else do
			// nothing
			log.info("omsCustOrdNo " + omsCustOrdNo + "-------------------Processing for item=" + custOrdItems.getItem() + "Line_no" + custOrdItems.getLineNo() + "---------------");
			priority = 1;
			String shipingChargeDept = session.getOmsSystemParametersFindIndValue("SHIPPING_CHARGE_DEPT", "OMS_SYSTEM_OPTION");
			BigDecimal itemDept = session.getItemMasterFindDept(custOrdItems.getItem());
			/*
			 * OmsCustOrdItem omsCustOrdItem1=
			 * session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(),new
			 * BigDecimal(custOrdItems.getLineNo())); omsCustOrdItem1.setDept(itemDept);
			 * session.mergeOmsCustOrdItem(omsCustOrdItem1);
			 */

			String inventoryIndn = session.getItemMasterFindInventoryInd(custOrdItems.getItem(), itemDept);
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item Dept= " + itemDept + "InventoryIndicator " + inventoryIndn);
			if (shipingChargeDept.equals(itemDept.toString()) == false && inventoryIndn.equals("Y")) {
				// if (custOrdItems.getBackOrderInd().equals("N"))
				// {

				CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
				// sourceLocId = new BigDecimal(input.getPickLoc());

				long L_Cum_Ord_Qty = 0L;
				long L_Pending_Qty = 0L;
				String deliverZone = null;
				if ("ODDSMALL".equals(input.getDeliveryModeType())) {
					deliverZone = input.getCustomerOrderAddress().getDeliver_zone();
				}
				// Step 2:Find the fulfiment location
				isPO = false;
				while (L_Cum_Ord_Qty < custOrdItems.getOrderQtySuom().longValue() && isPO == false) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);
					FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();

					String shipClassifcation;
					try {
						// If delivery type is Ship to Customer
						if (input.getDeliveryType().equals("S") || input.getDeliveryType().equals("SS") || input.getDeliveryType().equals("SC")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Ship to customer");

							shipClassifcation = oMSUtilCommons.findShipmentClassification(custOrdItems.getItem(), new BigDecimal(input.getOrderRequestorId()),
									custOrdItems.getShippingClassification().toUpperCase());

							combID = findNextfulfillLoc.processFulfillmentMatrixGetCombID(new BigDecimal(input.getOrderRequestorId()), shipClassifcation.toUpperCase(),
									input.getCustomerOrderAddress().getDeliverCity().toUpperCase(), input.getDeliveryType(), deliverZone, custOrdItems.getMarketplaceInd(), applicationId, shipToStore);

							log.info("omsCustOrdNo " + omsCustOrdNo + "*********shipClassifcation" + shipClassifcation);
						} else {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Customer pick up");
							shipClassifcation = oMSUtilCommons.findShipmentClassification(custOrdItems.getItem(), new BigDecimal(input.getPickLoc()),
									custOrdItems.getShippingClassification().toUpperCase());
							log.info("omsCustOrdNo " + omsCustOrdNo + "*********shipClassifcation" + shipClassifcation);
							combID = findNextfulfillLoc.processFulfillmentMatrixGetCombIDWoCity(new BigDecimal(input.getPickLoc()), shipClassifcation.toUpperCase(), input.getDeliveryType(),
									deliverZone, custOrdItems.getMarketplaceInd(), applicationId, shipToStore);
						}
						fulfillMatrixExtDetailResult = findNextfulfillLoc.processFulfillmentMatrix(combID, priority);
						nextLoc = fulfillMatrixExtDetailResult.getLocation();
						sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
						deliveryLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
						BigDecimal deilveryfromLocation = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
						if (nextLoc.intValue() < 0) {
							long L_Pending_Qty_temp;
							long L_Cum_Ord_Qty_temp;
							if (custOrdItems.getBackOrderInd().equals("Y")) {
								isBackOrderChecked = true;
								SOH = processBackOrderItem(custOrdItems, combID, input, custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)));
								L_Pending_Qty_temp = custOrdItems.getOrderQtySuom().longValue() - L_Cum_Ord_Qty;
								L_Cum_Ord_Qty_temp = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty);
								log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty_temp=" + L_Pending_Qty_temp + "L_Cum_Ord_Qty_temp=" + L_Cum_Ord_Qty_temp + "SOH=" + SOH);
								// if(L_Cum_Ord_Qty_temp < custOrdItems.getOrderQtySuom().longValue()) {
								if (custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).intValue() > SOH) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
									isPartialPO = true;
									partialPoQty = (custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).subtract(new BigDecimal(SOH)).intValue());
									log.info("omsCustOrdNo " + omsCustOrdNo + "isPartialPO=true and partialPoQty=" + partialPoQty);
									String item_status = checkItemLocSOH.findItemStatus(custOrdItems.getItem(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
									log.info(" Item Status is " + item_status);
									if (item_status.equals("A") == false) {
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
									}
									log.info("DeliveryLocType value is " + deliveryLocType);
									// nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(),
									// "Y"));
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
										log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + deilveryfromLocation.longValue());
										nextLoc = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), deilveryfromLocation.longValue());
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
										nextLoc = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), input.getOrderRequestorId());
									}
									log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItems.getItem() + "...." + "Location" + nextLoc.longValue());
									flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItems.getItem(), nextLoc.longValue(), input.getOrderRequestorId());
									// nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(),
									// "Y"));
									if (flag == Boolean.FALSE) {
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
									}

									fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
									sourceLocId = nextLoc;
									fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
									sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
									log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + nextLoc);
									if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(nextLoc)) != 0) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
									}

									// break;
									isPO = true;
								}
							} else {
								log.info("omsCustOrdNo " + omsCustOrdNo + "Its a PO,Find the supplier");
								String item_status = checkItemLocSOH.findItemStatus(custOrdItems.getItem(), fulfillMatrixExtDetailResult.getDeliveryFromLoc());
								if (item_status.equals("A") == false) {
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
								}
								log.info("Delivery Location Type Value is" + deliveryLocType);
								if ("S".equals(deliveryLocType)) {
									log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + deilveryfromLocation.longValue());
									nextLoc = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), deilveryfromLocation.longValue());

								} else {
									log.info("Calling getPrimarySupplierFromItemLocation Method  Item" + custOrdItems.getItem() + "...." + "Location" + input.getOrderRequestorId());
									nextLoc = checkItemLocSOH.getPrimarySupplierFromItemLocation(custOrdItems.getItem(), input.getOrderRequestorId());

								}
								log.info("Calling checkDirectShipIndicatoryofaGivenSupplier Method  Item" + custOrdItems.getItem() + "...." + "Location" + nextLoc.longValue());
								flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(custOrdItems.getItem(), nextLoc.longValue(), input.getOrderRequestorId());
								if (flag == Boolean.FALSE) {
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SUPP_NOT_FOUND"));
								}
								// nextLoc = new BigDecimal(checkItemLocSOH.findSupplier(custOrdItems.getItem(),
								// "Y"));

								fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();
								sourceLocId = nextLoc;
								fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
								sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
								log.info("omsCustOrdNo " + omsCustOrdNo + "next log for fing org unit=" + nextLoc);
								if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(nextLoc)) != 0) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
								}

								// break;
								isPO = true;
							}
						}
						sourceLocId = nextLoc;
						log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId:" + nextLoc + "sourceLocType=" + sourceLocType);
						sourceLocType = fulfillMatrixExtDetailResult.getLocationType();
						fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc(); // added
						log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId========================" + fulfillLocId);
						fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();

						if (fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {

							log.info("omsCustOrdNo" + omsCustOrdNo + "Processing for line No " + custOrdItems.getLineNo() + "and item " + custOrdItems.getItem() + " in WH");
							// If Source_loc_type =�WH� then query WH table in RMS
							// select physical_wh,channel_id from wh where wh='101';
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(fulfillMatrixExtDetailResult.getLocation());
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
							// else
							virtualWH = fulfillMatrixExtDetailResult.getLocation();
							fulfillLocId = fulfillMatrixExtDetailResult.getDeliveryFromLoc();

							log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH" + fulfillLocId);
							fulFillLocType = fulfillMatrixExtDetailResult.getDeliveryFromLocType();
							List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);

							int i = 0;
							while (i < locList.size()) {
								log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
								i++;
							}
							OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
							SOH = omsUtilCommons.checkSOHForWH(custOrdItems.getItem(), locList, applicationId).longValue();
							log.info("omsCustOrdNo" + omsCustOrdNo + "SOH for item " + custOrdItems.getItem() + "Line No " + custOrdItems.getLineNo() + "is " + SOH);
						}
					} catch (Exception e) {
						// move to next item
						log.info("inside catch wil check back order ");
						log.info("omsCustOrdNo " + omsCustOrdNo + "isBackOrderChecked=" + isBackOrderChecked);
						if (custOrdItems.getBackOrderInd().equals("Y") && isBackOrderChecked == false) {
							int qty = 0;
							qty = processBackOrderItem(custOrdItems, combID, input, custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)));
							if (qty < custOrdItems.getOrderQtySuom().subtract(new BigDecimal(L_Cum_Ord_Qty)).intValue()) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "Rollbacking back order");
								ItemAvailability itemAvailability = new ItemAvailability();
								itemAvailability.setItem(custOrdItems.getItem());
								itemAvailability.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
								itemAvailability.setOrderQty(custOrdItems.getOrderQtySuom());
								itemAvailability.setAvailableQty(new BigDecimal(L_Cum_Ord_Qty + qty));

								// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
								unavailableItemsList.add(itemAvailability);
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
								omsCustOrdItem.setStatus("F");
								session.mergeOmsCustOrdItem(omsCustOrdItem);

							}
							break;
						} else {
							log.info("Rollbacking ");
							log.error(e);
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
							itemAvailability.setAvailableQty(new BigDecimal(SOH));

							// log.info("omsCustOrdNo "+omsCustOrdNo +"Putting into SOH map");
							unavailableItemsList.add(itemAvailability);
							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							omsCustOrdHead.setStatus("F");

							session.mergeOmsCustOrdHead(omsCustOrdHead);
							OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
							omsCustOrdItem.setStatus("F");
							session.mergeOmsCustOrdItem(omsCustOrdItem);

							break;
						}
					}
					priority++;
					// Step 3 :Check qunatities for particular location in DAS schema

					if (sourceLocType.equals("ST")) {
						// Find SOH from SIM
						nextLoc = fulfillMatrixExtDetailResult.getLocation();
						String itemandLoc = custOrdItems.getItem() + "," + nextLoc.toString();
						InterfacePersistence interfacePersistece = new InterfacePersistence();
						try {
							log.info("============================================================================ line " + custOrdItems.getLineNo());
							// added code for duplicate items - fulfillment logic
							log.info("itemQtyMap.keySet() " + itemQtyMap.keySet());
							log.info("storeItemMap.keySet()" + storeItemMap.keySet());
							log.info("itemand Loc" + itemandLoc);
							log.info("omsCustOrdNo " + omsCustOrdNo + "Started the code for Duplicate Items - fulfillment logic");
							log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for LineNo============= " + custOrdItems.getLineNo());
							log.info("omsCustOrdNo" + omsCustOrdNo + "Procesing for Item " + custOrdItems.getItem());
							log.info("omsCustOrdNo" + omsCustOrdNo + "Ordered Qty " + custOrdItems.getOrderQtySuom());
							log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
							log.info("omsCustOrdNo" + omsCustOrdNo + "pending qty  " + L_Pending_Qty);
							int requestQty = 0;
							// check for needed quantity to fulfill
							if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
								requestQty = custOrdItems.getOrderQtySuom().intValue();
								log.info("requestQty when pending and l_cum_ord_qty " + requestQty);
							} else if (custOrdItems.getOrderQtySuom().intValue() >= L_Cum_Ord_Qty) {
								requestQty = custOrdItems.getOrderQtySuom().intValue() - (int) L_Cum_Ord_Qty;
								log.info("custOrdItems.getOrderQtySuom().intValue()>=L_Cum_Ord_Qty" + requestQty);
							} else if (L_Cum_Ord_Qty >= custOrdItems.getOrderQtySuom().intValue()) {
								requestQty = (int) L_Cum_Ord_Qty - custOrdItems.getOrderQtySuom().intValue();
								log.info("L_Cum_Ord_Qty>=custOrdItems.getOrderQtySuom().intValue() " + requestQty);
							}
							log.info("============================================================================ line " + custOrdItems.getLineNo());
							if (itemQtyMap.containsKey(custOrdItems.getItem())) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "inside containsKey condition for itemQtyMap for line No " + custOrdItems.getLineNo());
								if (storeItemMap.containsKey(itemandLoc))
								// if(itemQtyMap.get(custOrdItmDesc.getItemId()).getLocation().compareTo(nextLoc)==0)
								{
									log.info("omsCustOrdNo " + omsCustOrdNo + "inside compareTo condition for item id");
									// SOH=itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh().longValue();
									SOH = storeItemMap.get(itemandLoc).longValue();
									log.info("omsCustOrdNo " + omsCustOrdNo + "itemQtyMap already contain item with SOH=" + SOH);
									ItemSOH itemSOH = itemQtyMap.get(custOrdItems.getItem());
									log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
									log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + custOrdItems.getLineNo());
									log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());
									log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
									log.info("custOrdItems.getOrderQtySuom() " + custOrdItems.getOrderQtySuom());
									if (SOH >= requestQty) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "SOH >0 and locatin is " + nextLoc);
										itemSOH.setLocation(nextLoc);
										itemSOH.setSoh(new BigDecimal(SOH - requestQty));
										log.info("getting SOH inside inside if (storeItemMap.containsKey(itemandLoc)) " + itemSOH.getSoh());
									} // end of inner if
									else {
										log.info("omsCustOrdNo " + omsCustOrdNo + "SOH is less than quantity requested,finding in store" + nextLoc);
										itemSOH.setSoh(BigDecimal.ZERO);
										log.info("omsCustOrdNo" + omsCustOrdNo + "SOH inside else" + itemSOH.getSoh());
										itemQtyMap.put(custOrdItems.getItem(), itemSOH);
										if (storeItemMap.get(itemandLoc) != null) {
											storeSOH = storeItemMap.get(itemandLoc);
											log.info("storeSOH" + itemQtyMap.get(custOrdItems.getItem()).getSoh());
											storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
										}
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
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									SOH = interfacePersistece.callSIMStoreInventory(custOrdItems.getItem(), nextLoc, omsCustOrdHead.getApplicationId());
									log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItems.getItem() + "store " + nextLoc + " SOH" + SOH);
									ItemSOH itemSOH = itemQtyMap.get(custOrdItems.getItem());
									log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
									log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + custOrdItems.getLineNo());
									log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());
									log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
									log.info("requestQty " + requestQty);
									if (SOH >= requestQty) {
										log.info("settting when SOH>=custOrdItems.getOrderQtySuom().longValue() ");
										itemSOH.setSoh(new BigDecimal(SOH - requestQty));
									} else {
										log.info("SOH is set to  0");
										itemSOH.setSoh(BigDecimal.ZERO);
									}
									log.info("setted itemSOH " + itemSOH.getSoh());
									itemSOH.setLocation(nextLoc);
									itemQtyMap.put(custOrdItems.getItem(), itemSOH);
									log.info("putting the SOH in map " + itemQtyMap.get(custOrdItems.getItem()).getSoh());
									storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
									log.info("=====================================================================");

								}
								// SOH = interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(),
								// nextLoc);
							} // end of major 1st if
							else {
								log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not contain in itemQtyMap");
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								SOH = interfacePersistece.callSIMStoreInventory(custOrdItems.getItem(), nextLoc, omsCustOrdHead.getApplicationId());
								log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + custOrdItems.getItem() + "nextloc " + nextLoc + " SOH" + SOH);
								ItemSOH itemSOH = new ItemSOH();
								itemSOH.setLocation(nextLoc);
								log.info("custOrdItems.getOrderQtySuom() " + custOrdItems.getOrderQtySuom());
								log.info("requestQty " + requestQty);
								log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
								long L_Pending_Qty_temp = requestQty - L_Cum_Ord_Qty;
								log.info("L_Pending_Qty_temp " + L_Pending_Qty_temp);
								L_Pending_Qty = L_Pending_Qty_temp;
								log.info("SOH" + SOH);
								log.info("custOrdItmDesc.getQuantity() " + custOrdItems.getOrderQtySuom());
								if (SOH >= requestQty) {
									log.info("inside SOH>=custOrdItmDesc.getQuantity().intValue() " + new BigDecimal(SOH - requestQty));
									itemSOH.setSoh(new BigDecimal(SOH - requestQty));
								} else {
									log.info("inside else for SOH>=requestQty condition");
									log.info("itemSoh.getSoh()" + itemSOH.getSoh());
									itemSOH.setSoh(BigDecimal.ZERO);
									log.info("itemSoh.getSoh()" + itemSOH.getSoh());
								}
								log.info("putting into itemQtyMap map Location" + itemSOH.getLocation());
								log.info("putting into itemQtyMap map SOH " + itemSOH.getSoh());
								itemQtyMap.put(custOrdItems.getItem(), itemSOH);
								log.info("putting into storeItem map " + itemSOH.getSoh());
								storeSOH = itemQtyMap.get(custOrdItems.getItem()).getSoh();
								log.info("storeSOH " + storeSOH);
								storeItemMap.put(itemandLoc, itemQtyMap.get(custOrdItems.getItem()).getSoh());
								log.info("after putting into the storeMap " + storeItemMap.keySet());
								log.info("omsCustOrdNo " + omsCustOrdNo + "itemQty map does not contain values with SOH=" + itemSOH.getSoh());
							}
							log.info("omsCustOrdNo " + omsCustOrdNo + "Completed the try block for Duplicate items -Fulfillment Logic for line No " + custOrdItems.getLineNo());
						} // end of try

						catch (javax.xml.ws.WebServiceException f) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Failed in calling SIM exception is  " + f);
							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							omsCustOrdHead.setStatus("F");
							session.mergeOmsCustOrdHead(omsCustOrdHead);
							OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
							omsCustOrdItem.setStatus("F");
							session.mergeOmsCustOrdItem(omsCustOrdItem);
							throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SIM_UNAVL"));
						} catch (Exception e) {

							OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
							omsCustOrdHead.setStatus("F");
							session.mergeOmsCustOrdHead(omsCustOrdHead);
							OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, custOrdItems.getItem(), new BigDecimal(custOrdItems.getLineNo()));
							omsCustOrdItem.setStatus("F");
							session.mergeOmsCustOrdItem(omsCustOrdItem);
							throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
						}
					}
					// if(SOH>0 || isPO==true || isBackOrderChecked==false)
					log.info("SOH= " + SOH + "isPO " + isPO + " isBackOrderChecked= " + isBackOrderChecked + " isPartialPO= " + isPartialPO);

					if ((isBackOrderChecked == false && (SOH > 0 || isPO == true)) || isPartialPO == true) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "Setting into item counter map");
						if (itemCounterMap.containsKey(custOrdItems.getItem())) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Item already exist in itemCounterMap.");
							// itemCounterMap.put(custOrdItems.getItem(),
							// itemCounterMap.get(custOrdItems.getItem()).add(BigDecimal.ONE));///---uncommmented
							// 1dec as duplicate item not working
							// itemCounterMap.put(custOrdItems.getItem(),
							// itemCounterMap.get(custOrdItems.getItem()).add(BigDecimal.ONE));
							FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO = new FulfillOrdandSourceLocPOJO();
							fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
							log.info("omsCustOrdNo " + omsCustOrdNo + "-----");
							maxFulfillOrderNo = itemCounterMap.get(custOrdItems.getItem()).getFulfillOrderNo().add(BigDecimal.ONE).intValue();
							// maxFulfillOrderNo=sohmapdetail.lastKey().intValue();
							log.info("omsCustOrdNo " + omsCustOrdNo + "!-------");
							log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfillOrderNo" + maxFulfillOrderNo);

							boolean mapFound = false;
							for (BigDecimal key1 : sohMap.keySet()) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "key is " + key1);
								ArrayList<OmsTempCoFo> list = sohMap.get(key1);
								if (mapFound == true) {
									break;
								}
								log.info("list size=" + list.size());
								for (OmsTempCoFo temp : list) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "looping over map");
									if (temp.getSourceLocId().compareTo(sourceLocId) == 0 && temp.getItem().equals(custOrdItems.getItem()) == false) {
										fulfillOrdandSourceLocPOJO.setFulfillOrderNo(key1);
										log.info("omsCustOrdNo " + omsCustOrdNo + "mapFound=true;");
										mapFound = true;
										break;
									}
								}

							}
							boolean fulOrdNoFound = false;
							while (fulOrdNoFound == false) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "fulOrdNoFound" + fulOrdNoFound + "maxFulfillOrderNo=" + maxFulfillOrderNo + "key set" + sohMap.keySet());
								log.info("sohMap.get(maxFulfillOrderNo)" + sohMap.containsKey(new BigDecimal(maxFulfillOrderNo)));

								if (sohMap.containsKey(new BigDecimal(maxFulfillOrderNo))) {
									log.info("Size is" + sohMap.get(new BigDecimal(maxFulfillOrderNo)).size());
									log.info("sohMap.get(maxFulfillOrderNo)" + sohMap.get(new BigDecimal(maxFulfillOrderNo)).get(0).getSourceLocId() + " current loc is " + sourceLocId);
									if (sohMap.get(new BigDecimal(maxFulfillOrderNo)).get(0).getSourceLocId().compareTo(sourceLocId) == 0) {
										log.info("sourceLocType=" + sourceLocType);
										if (sourceLocType.equals("WH"))
											maxFulfillOrderNo++;
										fulOrdNoFound = true;
									} else {

										maxFulfillOrderNo++;
										log.info("in else maxFulfillOrderNo=" + maxFulfillOrderNo);
									}
								} else {
									log.info("No record exist in SohMap with ful ord no " + maxFulfillOrderNo);
									break;
								}
							}
							// fulfillOrdandSourceLocPOJO.setFulfillOrderNo(itemCounterMap.get(custOrdItems.getItem()).getFulfillOrderNo().add(BigDecimal.ONE));
							fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
							itemCounterMap.put(custOrdItems.getItem(), fulfillOrdandSourceLocPOJO);

						} else {
							log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not exist in itemCounterMap" + custOrdItems.getItem() + "fulfil order no=1");
							if (itemCounterMap.size() == 0) {
								FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO = new FulfillOrdandSourceLocPOJO();
								fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
								// fulfillOrdandSourceLocPOJO.setFulfillOrderNo(BigDecimal.ONE);
								fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
								itemCounterMap.put(custOrdItems.getItem(), fulfillOrdandSourceLocPOJO);
							} else {
								for (String key : itemCounterMap.keySet()) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Setting the FulfillOrdandSourceLocPOJO, key is " + key);
									FulfillOrdandSourceLocPOJO pojo = itemCounterMap.get(key);
									if (pojo.getSourceLoc().compareTo(sourceLocId) == 0 && sourceLocType.equals("WH") == false) {
										FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO = new FulfillOrdandSourceLocPOJO();
										fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
										fulfillOrdandSourceLocPOJO.setFulfillOrderNo(pojo.getFulfillOrderNo());
										itemCounterMap.put(custOrdItems.getItem(), fulfillOrdandSourceLocPOJO);
										break;
									} else {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Source and fulfil loc is not same");
										if (sourceLocType.equals("WH") == false && sohMap.size() > 0) {
											log.info("omsCustOrdNo " + omsCustOrdNo + "pojo.getFulfillOrderNo()" + pojo.getFulfillOrderNo());
											if (pojo.getFulfillOrderNo().intValue() >= 1) {
												int i = pojo.getFulfillOrderNo().intValue();
												int initialFulOrdNO = intialNo;
												while (i >= initialFulOrdNO) {
													log.info("omsCustOrdNo " + omsCustOrdNo + "inside while");
													List<OmsTempCoFo> tempSohMapList = sohMap.get(new BigDecimal(initialFulOrdNO));
													log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());
													log.info("omsCustOrdNo " + omsCustOrdNo + "---");
													boolean found = false;
													log.info("omsCustOrdNo " + omsCustOrdNo + "tempSohMapList size" + tempSohMapList.size());
													for (OmsTempCoFo omsTempCoFo : tempSohMapList) {
														if (custOrdItems.getItem().equals(omsTempCoFo.getItem())) {
															found = true;
															break;
														}
													}
													log.info("omsCustOrdNo " + omsCustOrdNo + "found+" + found);
													if (found == false) {
														FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO = new FulfillOrdandSourceLocPOJO();
														fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
														// fulfillOrdandSourceLocPOJO.setFulfillOrderNo(pojo.getFulfillOrderNo());
														boolean mapFound = false;
														int highestFulOrdNo = 1;
														// int highestFulOrdNo=0;
														for (BigDecimal key1 : sohMap.keySet()) {
															log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());

															if (key1.intValue() > highestFulOrdNo) {
																highestFulOrdNo = key1.intValue();
															}
															ArrayList<OmsTempCoFo> list = sohMap.get(key1);
															if (mapFound == true) {
																break;
															}
															for (OmsTempCoFo temp : list) {
																if (temp.getSourceLocId().compareTo(sourceLocId) == 0) {
																	fulfillOrdandSourceLocPOJO.setFulfillOrderNo(key1);
																	mapFound = true;
																	break;
																}
															}
														}
														if (mapFound == false) {
															fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(highestFulOrdNo + 1));
															maxFulfillOrderNo = highestFulOrdNo + 1;
															log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfillOrderNo " + maxFulfillOrderNo);
														}
														log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilorder no is " + fulfillOrdandSourceLocPOJO.getFulfillOrderNo());
														itemCounterMap.put(custOrdItems.getItem(), fulfillOrdandSourceLocPOJO);

														break;
													}
													i--;
												}
											}
										} else {
											FulfillOrdandSourceLocPOJO fulfillOrdandSourceLocPOJO = new FulfillOrdandSourceLocPOJO();
											fulfillOrdandSourceLocPOJO.setSourceLoc(sourceLocId);
											fulfillOrdandSourceLocPOJO.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo + 1));
											maxFulfillOrderNo = maxFulfillOrderNo + 1;
											log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfillOrderNo=" + maxFulfillOrderNo);
											itemCounterMap.put(custOrdItems.getItem(), fulfillOrdandSourceLocPOJO);
											break;
										}
									}
								}
							}
							// itemCounterMap.put(custOrdItems.getItem(), BigDecimal.ONE);

						}
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "SOH" + SOH);

					L_Pending_Qty = custOrdItems.getOrderQtySuom().longValue() - L_Cum_Ord_Qty;
					log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty" + L_Pending_Qty);
					log.info("omsCustOrdNo " + omsCustOrdNo + "priority " + priority);
					OmsTempCoFo omsTempCoFo = new OmsTempCoFo();
					omsTempCoFo.setOmsCustOrdNo(omsCustOrdNo);
					omsTempCoFo.setItem(custOrdItems.getItem());
					omsTempCoFo.setCombinationId(combID);
					omsTempCoFo.setLineNo(new BigDecimal(custOrdItems.getLineNo()));
					// omsTempCoFo.setOrderQty(custOrdItems.getOrderQtySuom());//----uncommented for
					// PO
					omsTempCoFo.setSourceLocId(sourceLocId);
					omsTempCoFo.setSourceLocationType(sourceLocType);
					if (sourceLocType.equals("WH")) {
						omsTempCoFo.setVirtualWH(virtualWH);
					} else {
						omsTempCoFo.setVirtualWH(BigDecimal.ZERO);
					}
					omsTempCoFo.setFulfillLocId(fulfillLocId);
					omsTempCoFo.setFulfillLocationType(fulFillLocType);
					if (itemCounterMap.get(custOrdItems.getItem()) != null) {
						omsTempCoFo.setFulfillOrderNo(itemCounterMap.get(custOrdItems.getItem()).getFulfillOrderNo());
					}
					omsTempCoFo.setFoConfQty(BigDecimal.ZERO);
					omsTempCoFo.setRmsResponseCode("");
					omsTempCoFo.setRmsErrorMsg("");
					omsTempCoFo.setCreateDatetime(new Timestamp(new Date().getTime()));
					omsTempCoFo.setStatus("N");
					log.info("omsCustOrdNo " + omsCustOrdNo + "Item:" + omsTempCoFo.getItem() + "-OrderedQty=" + omsTempCoFo.getOrderQty() + "-FulfilOrdNo=" + omsTempCoFo.getFulfillOrderNo());
					log.info("omsCustOrdNo " + omsCustOrdNo + "isBackOrderChecked=" + isBackOrderChecked);
					if ((SOH > 0 || isPO == true)) {

						log.info("omsCustOrdNo " + omsCustOrdNo + "inside SOH > 0 and isPO=" + isPO);
						if (isBackOrderChecked == false || isPartialPO == true) {
							// Customer pick up
							if (input.getDeliveryType().equals("C") || input.getDeliveryType().equals("CC")) {

								omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH, L_Pending_Qty)));
								// if(isPO==true) omsTempCoFo.setOrderQty(custOrdItems.getOrderQtySuom());
								if (isPO == true)
									omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
								if (isPartialPO == true)
									omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));
								// omsTempCoFo.setFulfillLocId(new BigDecimal(input.getPickLoc()));
								log.info("omsCustOrdNo " + omsCustOrdNo + "Ordered qty" + omsTempCoFo.getOrderQty() + "isPO=" + isPO + "isPartialPO" + isPartialPO);
								omsTempCoFo.setFulfillLocId(fulfillLocId);
								if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null || sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "in map ******* creating new key with ful ord no=" + omsTempCoFo.getFulfillOrderNo());
									ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
									tempList.add(omsTempCoFo);
									sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
								} else {
									ArrayList<OmsTempCoFo> existingList = sohMap.get(omsTempCoFo.getFulfillOrderNo());
									existingList.add(omsTempCoFo);
									sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
								}

							}
							// Ship to customer
							else { // 2
									// WH
								if (fulfillMatrixExtDetailResult.getDeliveryFromLocType().equals("WH") || fulfillMatrixExtDetailResult.getLocationType().equals("WH")) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "fulfil loc id as virtual loc is=" + fulfillLocId);
									omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH, L_Pending_Qty)));
									// if(isPO==true) omsTempCoFo.setOrderQty(custOrdItems.getOrderQtySuom());
									if (isPO == true)
										omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
									if (isPartialPO == true)
										omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));

									log.info("omsCustOrdNo " + omsCustOrdNo + "Ordered qty" + omsTempCoFo.getOrderQty() + "isPO=" + isPO + "isPartialPO" + isPartialPO);
									omsTempCoFo.setFulfillLocId(fulfillLocId);

									if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null || sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "in map ******* ");
										ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
										tempList.add(omsTempCoFo);
										sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
									} else {

										log.info("WHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
										maxFulfillOrderNo = treemap.lastKey().intValue();
										log.info("omsCustOrdNo " + omsCustOrdNo + " Before increamenting the maxFulfillOrderNo in WH else block " + maxFulfillOrderNo);
										maxFulfillOrderNo = maxFulfillOrderNo + 1;
										log.info("maxFulfillOrderNo WHHHHHHHHHHHHHHHHHHHHHHHHHHHH" + maxFulfillOrderNo);

										log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCoFo.getLineNo() " + omsTempCoFo.getLineNo());
										log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCo.getItem()" + omsTempCoFo.getItem());
										omsTempCoFo.setFulfillOrderNo(new BigDecimal(maxFulfillOrderNo));
										log.info("omsCustOrdNo " + omsCustOrdNo + "omsTempCo.fulfilOrdNo() " + omsTempCoFo.getFulfillOrderNo());
										ArrayList<OmsTempCoFo> existingList = new ArrayList<OmsTempCoFo>();
										existingList.add(omsTempCoFo);
										log.info("existingList size " + existingList.size());
										sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);

										// checking the existing list
										ArrayList<OmsTempCoFo> exist = sohMap.get(omsTempCoFo.getFulfillOrderNo());
										for (OmsTempCoFo tem : exist) {
											log.info("omsCustOrdNo" + omsCustOrdNo + "lineNo " + tem.getLineNo());
											log.info("omsCustOrdNo" + omsCustOrdNo + "item " + tem.getItem());
											log.info("omsCustOrdNo" + omsCustOrdNo + "item " + tem.getOrderQty());
											log.info("omsCustOrdNo" + omsCustOrdNo + "srcLoctyp+tem" + tem.getSourceLocationType());
											log.info("omsCustOrdNo" + omsCustOrdNo + "item " + tem.getSourceLocId());

										}

									}
								} else { // 1
											// Store
									log.info("Adding into list for ST");
									omsTempCoFo.setOrderQty(new BigDecimal(Math.min(SOH, L_Pending_Qty)));
									if (isPO == true)
										omsTempCoFo.setOrderQty(new BigDecimal(L_Pending_Qty));
									if (isPartialPO == true)
										omsTempCoFo.setOrderQty(new BigDecimal(partialPoQty));

									log.info("omsCustOrdNo " + omsCustOrdNo + "Ordered qty" + omsTempCoFo.getOrderQty() + "isPO=" + isPO + "isPartialPO" + isPartialPO);
									log.info("omsCustOrdNo " + omsCustOrdNo + "----------------------*******************");
									omsTempCoFo.setFulfillLocId(fulfillLocId);

									log.info("sohMap.keySet() " + sohMap.keySet());
									int oldFulFilOrdNo = omsTempCoFo.getFulfillOrderNo().intValue();
									log.info("oldFulFilOrdNo " + oldFulFilOrdNo);
									log.info("omsTempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
									log.info("==============MaxfulfilOrderNo ==============" + maxFulfillOrderNo);
									omsTempCoFo = STfulFilOrderNo(omsTempCoFo, sohMap);
									int newFulFilOrdNo = omsTempCoFo.getFulfillOrderNo().intValue();
									log.info("newFulFilOrdNo " + newFulFilOrdNo);
									if (oldFulFilOrdNo != newFulFilOrdNo) {
										maxFulfillOrderNo = maxFulfillOrderNo - 1;
									}
									log.info("After checking the new and old fulfilOrdNo " + maxFulfillOrderNo);
									if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null || sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "in map *******" + omsTempCoFo.getFulfillOrderNo());
										ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
										tempList.add(omsTempCoFo);
										sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
									} else {
										ArrayList<OmsTempCoFo> existingList = sohMap.get(omsTempCoFo.getFulfillOrderNo());
										existingList.add(omsTempCoFo);
										sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
									}
								} // else1 end
							} // else2

						}
						log.info("omsCustOrdNo " + omsCustOrdNo + "Item:" + omsTempCoFo.getItem() + "-OrderedQty=" + omsTempCoFo.getOrderQty() + "-FulfilOrdNo=" + omsTempCoFo.getFulfillOrderNo());

						log.info("omsCustOrdNo " + omsCustOrdNo + "persistOmsTempCoFo success");
						log.info("omsCustOrdNo " + omsCustOrdNo + "min : SOH " + SOH + "L_Pending_Qty" + L_Pending_Qty);
						L_Cum_Ord_Qty = L_Cum_Ord_Qty + Math.min(SOH, L_Pending_Qty);
						log.info("omsCustOrdNo " + omsCustOrdNo + "L_Cum_Ord_Qty : " + L_Cum_Ord_Qty);

						// } //if (SOH > 0) end
					} // end of src_loc_type WH
				} // while (L_Cum_Ord_Qty < custOrdItems.getOrderQtySuom().longValue()) end
			} // if (custOrdItems.getBackOrderInd().equals("N"))

			if (sohMap != null && sohMap.size() > 0) {
				treemap.putAll(sohMap);
				log.info("omsCustOrdNo " + omsCustOrdNo + "maxfulfilordNo " + treemap.lastKey());
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + " Added into treeMap " + treemap.keySet());

		} // for
		log.info("sohMap keyset " + sohMap.keySet());
		if (sohMap != null && sohMap.size() > 0) {
			fulfillDetailMap = createFulfillDetailMap(sohMap, 1);
		}

	}

	public int processBackOrderItem(CustomerOrderItems custOrdItems, BigDecimal combID, CustomerOrder input, BigDecimal L_Pending_Qty) throws SOAPException {
		log.info("<----------------Begin of processBackOrderItem method ---------------------------->");
		log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo" + omsCustOrdNo + "processBackOrderItem");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		int fulfilledQty = 0;
		try {
			BigDecimal currentPendingQty = BigDecimal.ZERO;
			List<OmsFulfillMatrixExtDetail> omsFulfillMatrixExtDetailList = session.getOmsFulfillMatrixExtDetailFindByCombId(combID);

			BigDecimal alloctedInventory = null;
			CustFutureInvPosition custFutureInvPosition = null;
			currentPendingQty = L_Pending_Qty;
			boolean itemLocked = false;
			log.info("omsCustOrdNo " + omsCustOrdNo + "L_Pending_Qty=" + L_Pending_Qty);

			int i = 0;
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilledQty=" + fulfilledQty + "omsFulfillMatrixExtDetailList.size()" + omsFulfillMatrixExtDetailList.size());
			while (fulfilledQty != L_Pending_Qty.intValue() && i < omsFulfillMatrixExtDetailList.size()) {
				String boIndicator = "N";
				OmsFulfillMatrixExtDetail omsFulfillMatrixExtDetail = omsFulfillMatrixExtDetailList.get(i);
				BigDecimal boCombId = omsFulfillMatrixExtDetail.getCombinationId();
				log.info("omsCustOrdNo " + omsCustOrdNo + " boCombId " + boCombId);

				i++;
				try {

					// log.info("omsCustOrdNo "+omsCustOrdNo +"alloctedInventory "+alloctedInventory
					// +"for location "+omsFulfillMatrixExtDetail.getLocation());
					CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
					BigDecimal physicalWH = BigDecimal.ZERO;
					BigDecimal channelId = BigDecimal.ZERO;
					try {
						if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsFulfillMatrixExtDetail.getLocation());

							for (Object[] result : tempWhObject) {

								physicalWH = new BigDecimal(result[0].toString());
								// omsFulfillMatrixExtDetail.setLocation(physicalWH);
								channelId = new BigDecimal(result[1].toString());
								log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
							}

						}
					} catch (Exception g) {

					}
					itemLocked = PreOrderItemLocationSync.obtainLock(custOrdItems.getItem(), omsFulfillMatrixExtDetail.getLocation(), omsCustOrdNo.toPlainString());
					log.info("<---------------------- End Of While--------------------------------->");
					BackOrderAllocatedInventory backOrderAllocatedInventory = new BackOrderAllocatedInventory();
//                        alloctedInventory =
//                                session.getOmsBackOrderDtlFindAssignedInvOrders(omsFulfillMatrixExtDetail.getLocation(),
//                                                                                custOrdItems.getItem());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Thread.current thread " + Thread.currentThread().getName());
					log.info("omsCustOrdNo " + omsCustOrdNo + "Before calling getAllocatedInventoryfromBackOrderDTL method alloctedInventory " + alloctedInventory);
					alloctedInventory = backOrderAllocatedInventory.getAllocatedInventoryfromBackOrderDTL(omsFulfillMatrixExtDetail.getLocation(), custOrdItems.getItem());
					if (alloctedInventory == null) {
						alloctedInventory = BigDecimal.ZERO;
					}
					
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
						boIndicator = "N";
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + custOrdItems.getItem());

					log.info("omsCustOrdNo " + omsCustOrdNo + "omsFulfillMatrixExtDetail.getLocation().longValue() " + omsFulfillMatrixExtDetail.getLocation().longValue());

					if (boIndicator.equals("Y")) {
						if (custFutureInvPosition.getExpectedDate() != null) {

							int qtyToBeFulfilled = custFutureInvPosition.getExpectedQty().intValue() - alloctedInventory.intValue();
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

								omsBackOrderDtl.setFulfillQty(BigDecimal.ZERO);
								omsBackOrderDtl.setBackorderStatus("N");
								omsBackOrderDtl.setCreatedBy("OMSUSER");
								if (qtyToPersist.intValue() > 0) {
									omsBackOrderDtl.setSourceQty(qtyToPersist);

									InterfacePersistence interfacePersistence = new InterfacePersistence();
									if (omsFulfillMatrixExtDetail.getLocationType().equals("WH")) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc=" + physicalWH);

										interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, physicalWH.longValue(), "W", custOrdItems.getStandardUom(), channelId);
									} else {
										log.info("omsCustOrdNo " + omsCustOrdNo + "Calling back order WS with item=" + custOrdItems.getItem() + "qty=" + qtyToPersist + "loc="
												+ omsFulfillMatrixExtDetail.getLocation());

										interfacePersistence.callRMSBackorderWS(custOrdItems.getItem(), qtyToPersist, omsFulfillMatrixExtDetail.getLocation().longValue(), "S",
												custOrdItems.getStandardUom(), channelId);
									}
									log.info("<------- omsCustOrdNo----> " + omsCustOrdNo + "<-------Inserting Record into OmsBackOrderDtl Table ------------------------> ");
									log.info("<-----omsCustOrdNo ----->" + omsCustOrdNo + "<-----Executing Weblogic Thread --------------------->" + Thread.currentThread().getName() + "<--------->");
									ItemLoc itemloc = new ItemLoc();
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Item---->" + custOrdItems.getItem() + "<---->");
									itemloc.setItem(custOrdItems.getItem());
									log.info("<---omsCustOrdNo----->" + omsCustOrdNo + "<----- Location---->" + omsFulfillMatrixExtDetail.getLocation() + "<---->");
									itemloc.setLocation(omsFulfillMatrixExtDetail.getLocation());
									session.persistOmsBackOrderDtl(omsBackOrderDtl);
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
					log.info("omsCustOrdNo " + omsCustOrdNo + f);
					log.info("<--------omsCustOrdNo----> " + omsCustOrdNo + "<------If Some Error Occured then it should allow other request to Procced ---->" + f.getMessage() + "<---->");
					log.info("<------------Checking Item and Location Combination exist in OmsitemLocSync Table----------------------->");
					log.info("<-----omsCustOrdNo------>" + omsCustOrdNo + "<-------Item ----->" + custOrdItems.getItem() + "Location is ------------>" + omsFulfillMatrixExtDetail.getLocation()
							+ "<----->");
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
		return fulfilledQty;

	}

	// This method will create the fulfillment detail map with fulfill_order_no as
	// key and value as items fulfied from same store from map having store_id as
	// key

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

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> createFulfillDetailMapNew(Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap, int fulfillOrderNo) {
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		for (BigDecimal key : sohMap.keySet()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + sohMap.keySet());
			ArrayList<OmsTempCoFo> list = sohMap.get(key);
			if (list.get(0).getSourceLocationType().equals("ST")) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "1");
				ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
				for (OmsTempCoFo temp : list) {
					OmsTempCoFo omsTempCoFo = temp;
					omsTempCoFo.setFulfillOrderNo(new BigDecimal(fulfillOrderNo));
					tempList.add(omsTempCoFo);
					if (fulfillDetailMap.containsKey(key))
						fulfillDetailMap.put(new BigDecimal(fulfillOrderNo), tempList);
					// fulfillDetailMap.put(new BigDecimal(fulfillOrderNo), list);
				}
				fulfillOrderNo++;
			} else {
				log.info("omsCustOrdNo " + omsCustOrdNo + "2");
				ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
				for (OmsTempCoFo temp : list) {
					OmsTempCoFo omsTempCoFo = temp;
					omsTempCoFo.setFulfillOrderNo(new BigDecimal(fulfillOrderNo));
					tempList.add(omsTempCoFo);
					fulfillDetailMap.put(new BigDecimal(fulfillOrderNo), tempList);
					fulfillOrderNo++;
				}
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilDetail map" + fulfillDetailMap.keySet());
		}
		return fulfillDetailMap;
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

}
