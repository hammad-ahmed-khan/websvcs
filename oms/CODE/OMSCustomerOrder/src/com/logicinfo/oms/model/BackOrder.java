package com.logicinfo.oms.model;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.soap.SOAPException;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsBackOrderDtl;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsCustOrdReserve;
import com.logicinfo.oms.ejb.OmsResUnresvCustOrderLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.ejb.OmsUnapprovedTransfers;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OracleBaseAPIUtil;
import com.logicinfo.oms.util.StoreInventory;
import com.logicinfo.oms.utils.ProjectUtils;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1.FulfilOrdCfmDtl;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;

public class BackOrder {
	public BackOrder() {
		super();
	}

	public final static Logger log = ProjectUtils.getLog();
	Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = null;
	int retryWS = 2;
	BigDecimal virtualWH;
	int numberOfRetry = 1;
	TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = null;
	ArrayList<BigDecimal> transferList = new ArrayList<BigDecimal>();

	public int findBackOrder() throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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
		log.info("starting processing back order");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
		log.info(session.toString());
		List<BigDecimal> omsCustomerOrderList = session.getOmsBackOrderDtlFindOmsCustOrdNoForBackOrder(new Timestamp(new Date().getTime()));
		log.info("No of back orders to process" + omsCustomerOrderList.size());
		for (BigDecimal omsCustOrdNo : omsCustomerOrderList) {
			String orderStatus = "S";
			log.info("Processing back order for omsCustOrdNo=" + omsCustOrdNo);
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			log.info("status for customerOrder =" + omsCustOrdHead.getCustOrderNo() + "Status is " + omsCustOrdHead.getStatus());
			if (omsCustOrdHead.getStatus().equals("S")) {
				log.info("omsCustOrdNo " + omsCustOrdNo + "status is S");
				List<OmsBackOrderDtl> omsBackOrderDtlList = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
				InterfacePersistence interfacePersistence = new InterfacePersistence();
				Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfilmentMap = createFulfilmentList(omsBackOrderDtlList, omsCustOrdNo);
				log.info("fulfilmentMap=" + fulfilmentMap.isEmpty());
				if (fulfilmentMap != null && fulfilmentMap.isEmpty() == false) {
					if (omsCustOrdHead.getOrderCreateReserveInd().equals("R") && omsCustOrdHead.getOrdPaymentStatus().equals("P")) {

						RmsPackage rmsPackageCall = new RmsPackage();
						try {
							persistOmsCustOrdReserveForBackorder(omsCustOrdHead.getOmsCustOrdNo(), fulfilmentMap);
							log.info("++++++++Calling RMSPackage Call+++++++++++" + omsCustOrdNo);
							rmsPackageCallForBackOrder(fulfilmentMap);
							log.info("++++++++End of calling RMSPackage Call+++++++++++" + omsCustOrdNo);
							interfacePersistence.adjustInventoryByItemLocation(fulfilmentMap, omsCustOrdHead.getCustOrderNo());
							for (BigDecimal key : fulfilmentMap.keySet()) {
								ArrayList<OmsTempCoFo> list = fulfilmentMap.get(key);
								for (OmsTempCoFo omsTempCoFo : list) {
									OmsBackOrderDtl omsBackOrderDtl = session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(omsCustOrdNo, omsTempCoFo.getLineNo(), omsTempCoFo.getSourceLocId());
									omsBackOrderDtl.setFulfillQty(omsTempCoFo.getOrderQty().add(omsBackOrderDtl.getFulfillQty()));
									if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0) {
										omsBackOrderDtl.setBackorderStatus("S");
									}
									omsBackOrderDtl.setConfTs(new Timestamp(new java.util.Date().getTime()));
									session.mergeOmsBackOrderDtl(omsBackOrderDtl);
									BigDecimal physicalWH = BigDecimal.ZERO;
									BigDecimal channelId = BigDecimal.ZERO;
									if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
										List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsBackOrderDtl.getSourceLoc());
										for (Object[] result : tempWhObject) {
											physicalWH = new BigDecimal(result[0].toString());
											channelId = new BigDecimal(result[1].toString());
											log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
										}
										log.info("calling RMS back order webservice" + omsBackOrderDtl.getItem() + "qty=" + omsTempCoFo.getOrderQty().negate() + "loc=" + physicalWH.longValue());
										interfacePersistence.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsTempCoFo.getOrderQty().negate(), physicalWH.longValue(), "W", "EA", channelId);
									} else {
										log.info("calling RMS back order webservice" + omsBackOrderDtl.getItem() + "qty=" + omsTempCoFo.getOrderQty().negate() + "loc="
												+ omsBackOrderDtl.getSourceLoc().longValue());
										interfacePersistence.callRMSBackorderWS(omsBackOrderDtl.getItem(), omsTempCoFo.getOrderQty().negate(), omsBackOrderDtl.getSourceLoc().longValue(), "S", "EA",
												channelId);
									}
								}
							}
						} catch (Exception e) {
							log.error("Error " + e);
							log.error("Rollback in case of reserve for omsCustOrdNo=" + omsCustOrdNo);
							log.info("Calling Rollback for RMSPackage Call " + omsCustOrdNo);
							rollbackRmsPackageCallForBackOrder(fulfillDetailMap);
							log.info("End of calling RMSPackage Call " + omsCustOrdNo);
						}
					} else {
						ProcessedObject processedObject = null;
						FulfilOrdCfmCol fulfilOrdCfmCol = null;
						ArrayList<OmsTempCoFo> tempList = null;
						log.info("keyset" + fulfilmentMap.keySet());
						for (BigDecimal key : fulfilmentMap.keySet()) {
							log.info("Processing for key as fulfill order no as" + key);
							tempList = fulfilmentMap.get(key);
							try {
								processedObject = interfacePersistence.callWebservices(omsCustOrdNo, tempList);
								log.info("Call successful ,retuned processed obeject");
								fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
								tempList = processedObject.getOmsTempCoFoList();
								log.info("Processed object size" + tempList.size());
								fulfilmentMap.put(key, tempList);
							} catch (Exception e) {
								log.error("Error in processing fulfillment :" + e.getMessage());
								for (OmsTempCoFo temp : tempList) {
									FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
									log.info("Ordered Qty : " + temp.getOrderQty());
									fulfilOrdDtlRef.setCancelQtySuom(temp.getOrderQty());
									log.info("Item : " + temp.getItem());
									fulfilOrdDtlRef.setItem(temp.getItem());
									FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
									fulfilOrdDtlRef.setTransactionUom("EA");
									fulfilOrdDtlRef.setStandardUom("EA");
									fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
									log.info("<------------------Calling SIM for Rollback - BackOrder for------------------->" + omsCustOrdHead.getCustOrderNo());
									rollbackforSIMForBackOrder(omsCustOrdHead.getCustOrderNo(), fulfilOrdRef);
									log.info("<------------------Rollback Completed------------------->");
								}
							}
							try {
								if (orderStatus.equals("S")) {
									String responseStatus = interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
									log.info("Response received is " + responseStatus);
									log.info("fulfilmentMap.keySet() " + fulfilmentMap.keySet());
									log.info("key " + key);
									if (responseStatus.equals("C")) {
										OmsCoFulfillDetail omsCoFulfillDetail = new OmsCoFulfillDetail();
										persistOmsCoFulFilDetail(key, tempList, omsCoFulfillDetail, omsCustOrdNo);
										if (transferList != null && transferList.size() > 0) {
											oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList, omsCustOrdNo);
										}
									} else if (responseStatus.equals("P")) {
										log.info("inside reponse status P");
										Map<String, BigDecimal> tempMap = new HashMap<String, BigDecimal>();
										List<FulfilOrdCfmDtl> fulfilOrdCfmDtlList = fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getFulfilOrdCfmDtl();
										if (fulfilOrdCfmDtlList != null && fulfilOrdCfmDtlList.size() > 0) {
											for (FulfilOrdCfmDtl fulfilOrdCfmDtl : fulfilOrdCfmDtlList) {
												log.info("fulfilOrdCfmDtl.getItem() " + fulfilOrdCfmDtl.getItem());
												log.info("fulfilOrdCfmDtl.getConfirmQty() " + fulfilOrdCfmDtl.getConfirmQty());
												tempMap.put(fulfilOrdCfmDtl.getItem(), fulfilOrdCfmDtl.getConfirmQty());
											}
										}
										for (OmsTempCoFo tempCoFo : tempList) {
											if (tempMap.containsKey(tempCoFo.getItem()) == true && tempCoFo.getRmsResponseCode() != null && tempCoFo.getRmsResponseCode().equals("P")) {
												log.info("tempCoFo.getOrderQty()---->" + tempCoFo.getOrderQty() + "----------->");
												log.info("tempCoFo.getItem()---->" + tempCoFo.getItem() + "----------->");
												log.info("tempMap.get(tempCoFo.getItem())--->" + tempMap.get(tempCoFo.getItem()) + "------->");
												log.info("remaing Qty " + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
												tempCoFo.setFoConfQty(tempMap.get(tempCoFo.getItem()));
												tempCoFo.setRmsResponseCode("C");
												log.info("key for P to C is " + tempCoFo.getFulfillOrderNo());
											}
										}
										OmsCoFulfillDetail omsCoFulfillDetail = new OmsCoFulfillDetail();
										persistOmsCoFulFilDetail(key, tempList, omsCoFulfillDetail, omsCustOrdNo);
										if (transferList != null && transferList.size() > 0) {
											oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList, omsCustOrdNo);
										}
									} // end of else if for 'P's
									else {
										log.info("Received  response is X for omsCustordNo" + omsCustOrdNo);
										continue;
									}
								} // orderstatus S
							} // end of try
							catch (Exception e) {
								log.error("Failed in persiting into oms_co_fulfill_detail or rtlog" + e.getMessage());
							}
						}
					}
				}
			} // end of if for status
		}
		return omsCustomerOrderList.size();
	}

	public Map<BigDecimal, ArrayList<OmsTempCoFo>> createFulfilmentList(List<OmsBackOrderDtl> omsBackOrderDtlList, BigDecimal omsCustOrdNo)
			throws SOAPException, com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException,
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
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException {
		log.info("inside createFulfilmentList()with list size=" + omsBackOrderDtlList.size());
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int maxFulfillOrderNo = 1;
		boolean fulfilRecordExist = false;
		String orderCreateReserveInd = null;
		sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treeMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		fulfillDetailMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();

		// find the custordInd
		OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
		if (omsCustOrdHead.getStatus().equals("S")) {
			orderCreateReserveInd = omsCustOrdHead.getOrderCreateReserveInd();
			log.info("orderCreateReserveInd " + orderCreateReserveInd);
		}
		OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
		try {
			maxFulfillOrderNo = omsUtilCommons.returnMaxFulFilOrdNoECOM(omsCustOrdHead.getCustOrderNo());
			log.info("maxFulfillOrderNo " + maxFulfillOrderNo);
		} catch (Exception e) {
			log.info("No record exist in ordCust");
			maxFulfillOrderNo = 1;
		}
		BigDecimal sourceLocId = BigDecimal.ZERO;
		for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDtlList) {
			log.info("omscustOrdNo " + omsBackOrderDtl.getOmsCustOrdNo() + " Processing for line no " + omsBackOrderDtl.getLineNo() + "Item " + omsBackOrderDtl.getItem());
			if (omsBackOrderDtl.getSourceQty().intValue() > omsBackOrderDtl.getFulfillQty().intValue()) {
				log.info(" Finding fulfillment for omscustOrdNo " + omsBackOrderDtl.getOmsCustOrdNo() + " Processing for line no " + omsBackOrderDtl.getLineNo() + "Item " + omsBackOrderDtl.getItem()
						+ "sourceLoc " + omsBackOrderDtl.getSourceLoc());
				if (omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() > 0 && omsBackOrderDtl.getBackorderStatus().equals("S") == false) {

					int backOrdFulfillQty = (int) findCurrentInventory(omsBackOrderDtl);
					log.info("omscustOrdNo " + omsBackOrderDtl.getOmsCustOrdNo() + " for sourceLoc +omsBackOrderDtl.getSourceLoc() is" + backOrdFulfillQty);

					if (backOrdFulfillQty > 0) {

						if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
							virtualWH = omsBackOrderDtl.getSourceLoc();
						} else {
							virtualWH = BigDecimal.ZERO;

						}
						if (omsBackOrderDtl.getSourceLocType().equals("WH")
								&& (orderCreateReserveInd.equals("C") || (orderCreateReserveInd.equals("R") && omsCustOrdHead.getOrdPaymentStatus().equals("S")))) {
							log.info("inside if cond for WH & C");
							BigDecimal physicalWH = session.getWhFindPhyWhForVirtualWh(omsBackOrderDtl.getSourceLoc());
							log.info("physicalWH  " + physicalWH);
							sourceLocId = physicalWH;
						} else {
							sourceLocId = omsBackOrderDtl.getSourceLoc();
						}
						OmsTempCoFo omsTempCoFo = createFulfilMapObject(omsCustOrdNo, omsBackOrderDtl.getItem(), sourceLocId, omsBackOrderDtl.getSourceLocType(), omsBackOrderDtl.getFulfillLoc(),
								omsBackOrderDtl.getFulfillLocType(), backOrdFulfillQty, omsBackOrderDtl.getLineNo(), omsBackOrderDtl.getCombinationId(), maxFulfillOrderNo, virtualWH, treeMap, sohMap);
						if (sohMap.get(omsTempCoFo.getFulfillOrderNo()) == null || sohMap.get(omsTempCoFo.getFulfillOrderNo()).size() == 0) {
							ArrayList<OmsTempCoFo> tempList = new ArrayList<OmsTempCoFo>();
							tempList.add(omsTempCoFo);
							sohMap.put(omsTempCoFo.getFulfillOrderNo(), tempList);
						} else {
							ArrayList<OmsTempCoFo> existingList = sohMap.get(omsTempCoFo.getFulfillOrderNo());
							existingList.add(omsTempCoFo);
							sohMap.put(omsTempCoFo.getFulfillOrderNo(), existingList);
						}
						log.info("Added sohMap " + sohMap.keySet());
					} // end of SOH if
				} // end of second if

			} // end of 1st if
		} // end of for loop
		fulfillDetailMap = createFulfillDetailMap(sohMap, 1);
		log.info("fulfilmentMap " + fulfillDetailMap.keySet());
		return fulfillDetailMap;
	} // end of createFulfilmentList

	public Map<BigDecimal, ArrayList<OmsTempCoFo>> getMap() {
		return fulfillDetailMap;
	}

	// This method will create the fulfillment detail map with fulfill_order_no as
	// key and value as items fulfied from same store from map having store_id as
	// key

	public TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> createFulfillDetailMap(Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap, int fulfillOrderNo) {
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();

		for (BigDecimal key : sohMap.keySet()) {
			log.info(sohMap.keySet());
			ArrayList<OmsTempCoFo> list = sohMap.get(key);
			fulfillDetailMap.put(key, list);

			for (OmsTempCoFo temp : list) {
				log.info("Fulfilmetn OrderNo=" + temp.getFulfillOrderNo() + "for item=" + temp.getItem());
			}

			log.info("fulfilDetail map" + fulfillDetailMap.keySet());
		}
		return fulfillDetailMap;
	}

	public long findCurrentInventory(OmsBackOrderDtl omsBackOrderDtl) throws com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storeinventoryservice.v1.ValidationWSFaultException, SOAPException, EntityAlreadyExistsWSFaultException, EntityAlreadyExistsWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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
		log.info("inside findCurrentInventory");
		Map<String, BigDecimal> storeItemMap = new HashMap<String, BigDecimal>();
		Map<String, ItemSOH> itemQtyMap = new HashMap<String, ItemSOH>();
		BigDecimal storeSOH = BigDecimal.ZERO;

		int SOH = 0;
		long L_Cum_Ord_Qty = 0;
		long L_Pending_Qty = 0;
		if (omsBackOrderDtl.getSourceLocType().equals("ST")) {
			SOH = getStoreSOH(omsBackOrderDtl, L_Cum_Ord_Qty, L_Pending_Qty, omsBackOrderDtl.getSourceLoc(), storeItemMap, itemQtyMap, storeSOH);
		} else if (omsBackOrderDtl.getSourceLocType().equals("WH")) {
			SOH = getWHSOH(omsBackOrderDtl, omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).longValue());
		}
		log.info("Stock on hand found for " + omsBackOrderDtl.getItem() + "is " + SOH);
		return SOH;
	}

	void rmsPackageCallForBackOrder(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
		log.info("----------------Begin Start-rmsPackageCall --------------Started");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection con = null;
		CallableStatement pstmt = null;
		int result = 0;
		OmsResUnresvCustOrderLog omsResUnresvCustOrderLog = null;
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
			for (OmsTempCoFo omsTempCoFo : list) {
				if (omsTempCoFo.getSourceLocationType().equals("SU") == false && omsTempCoFo.getSourceLocationType().equals("ST") == false) {
					try {
						log.info("Calling RMS inventory adjustment for item=" + omsTempCoFo.getItem());
						con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
						pstmt = con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
						pstmt.setString(1, omsTempCoFo.getItem());
						log.info("item" + omsTempCoFo.getItem());
						int i_inv_staus = Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS", "OMS_SYSTEM_OPTION"));
						pstmt.setInt(2, i_inv_staus); // read from system parameter table I_inv_status with value 2
						if (omsTempCoFo.getSourceLocationType().equals("ST")) {
							pstmt.setString(3, "S"); // loc type S,W ,source loc type
							log.info("Source loc=" + omsTempCoFo.getSourceLocId());
							pstmt.setInt(4, (omsTempCoFo.getSourceLocId().intValueExact()));
						} else {
							log.info("Source loc=" + omsTempCoFo.getSourceLocId().intValue());
							pstmt.setString(3, "W");
							// Below statements are commented when the source location was passing as status
							// value;
							// pstmt.setInt(4, Integer.valueOf(omsTempCoFo.getStatus()));
							pstmt.setInt(4, omsTempCoFo.getSourceLocId().intValue());
						}
						log.info("order qty" + omsTempCoFo.getOrderQty().intValueExact());
						pstmt.setInt(5, omsTempCoFo.getOrderQty().intValueExact()); // loc qty
						int reason_code = Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE", "OMS_SYSTEM_OPTION"));
						log.info("reason_code" + reason_code);
						pstmt.setInt(6, reason_code); // reason_id put in oms_system_parameter
						OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
						pstmt.setString(7, omsCustOrdHead.getCustOrderNo() + "_BKR");
						pstmt.registerOutParameter(8, Types.INTEGER);
						pstmt.registerOutParameter(9, Types.VARCHAR);
						for (int i = 1; i <= 10; i++) {
							try {
								pstmt.executeUpdate();
								result = pstmt.getInt(8);
								log.info("plsql call ::" + result);
								String err_msg = pstmt.getString(9);
								log.info(err_msg);
								if (result != 1) {
									if (i == 10) {
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
									}
								} else {
									log.info("The Success full call happened at " + i + "Attempt");
									break;
								}
							} catch (Exception e) {
								log.info("--------Error occured while calling RMS Package----------------");
								if (i == 10) {
									log.info(" omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into resv and unresv table");
									omsResUnresvCustOrderLog = new OmsResUnresvCustOrderLog();
									omsResUnresvCustOrderLog.setAdjQty(new BigDecimal((omsTempCoFo.getOrderQty().intValueExact())));
									omsResUnresvCustOrderLog.setCreateTimestamp(new Timestamp(new Date().getTime()));
									omsResUnresvCustOrderLog.setItem(omsTempCoFo.getItem());
									omsResUnresvCustOrderLog.setLocation(new BigDecimal(omsTempCoFo.getSourceLocId().intValueExact()));
									omsResUnresvCustOrderLog.setOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
									omsResUnresvCustOrderLog.setStatus("N");
									try {
										log.info(" Inserting the record omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into resv and unresv table");
										session.persistOmsResUnresvCustOrderLog(omsResUnresvCustOrderLog);
									} catch (Exception e1) {
										log.info(e1);
										log.info(" error occured while insert record omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into persistOmsResUnresvCustOrderLog");
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));

									}
								}
							}
						} // end of for loop....
					} catch (Exception e) {
						log.info("-----------------Error occured-------------");
					} finally {
						try {
							pstmt.close();
							con.close();
						} catch (SQLException e) {
							log.error(e);
						}
					}
				}
			} // else end
		} // for ends

		log.info("----------------End of Start-rmsPackageCall -----------------");
	}

	void rollbackRmsPackageCallForBackOrder(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
		log.info("***Rollback -rmsPackageCall-started***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection con = null;
		CallableStatement pstmt = null;
		int result = 0;
		OmsResUnresvCustOrderLog omsResUnresvCustOrderLog = null;
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
			for (OmsTempCoFo omsTempCoFo : list) {
				if (omsTempCoFo.getSourceLocationType().equals("SU") == false && "ST".equals(omsTempCoFo.getSourceLocationType()) == false) {
					try {
						con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
						pstmt = con.prepareCall("{call OMS_INVADJ_STATUS_UNAVIALINV(?,?,?,?,?,?,?,?,?)}");
						pstmt.setString(1, omsTempCoFo.getItem());
						int i_inv_staus = Integer.parseInt(session.getOmsSystemParametersFindIndValue("RESV_INV_STATUS", "OMS_SYSTEM_OPTION"));
						pstmt.setInt(2, i_inv_staus);
						log.info("Source loc=" + omsTempCoFo.getSourceLocId());
						if (omsTempCoFo.getSourceLocationType().equals("ST")) {
							pstmt.setString(3, "S");
							pstmt.setInt(4, (omsTempCoFo.getSourceLocId().intValueExact()));
						} else {
							// log.info("Source loc="+omsTempCoFo.getStatus());
							pstmt.setString(3, "W");
							// Below statments are commented as as they are passing status value not
							// location value . Which in result
							// throwing null pointer Exception.
							// pstmt.setInt(4, Integer.valueOf(omsTempCoFo.getStatus()));
							pstmt.setInt(4, (omsTempCoFo.getSourceLocId().intValueExact()));
						}
						int qty = -(omsTempCoFo.getOrderQty().intValueExact());
						pstmt.setInt(5, qty);
						int reason_code = Integer.parseInt(session.getOmsSystemParametersFindIndValue("REASON_CODE", "OMS_SYSTEM_OPTION"));
						pstmt.setInt(6, reason_code);
						OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
						pstmt.setString(7, omsCustOrdHead.getCustOrderNo() + "_BKR::");
						pstmt.registerOutParameter(8, Types.INTEGER);
						pstmt.registerOutParameter(9, Types.VARCHAR);
						for (int i = 1; i <= 10; i++) {
							try {
								pstmt.executeUpdate();
								result = pstmt.getInt(8);
								String err_msg = pstmt.getString(9);
								log.info("result is " + result + "err_msg" + err_msg);
								if (result != 1) {
									if (i == 10) {
										log.info("Error occured attempt no is " + i);
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
									}
								} else {
									log.info("<-----------------Success Package call------------------->");
									break;
								}
							} catch (Exception e) {
								log.info("----------------Error occured while calling the package----");
								if (i == 10) {
									log.info(" omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into resv and unresv table");
									omsResUnresvCustOrderLog = new OmsResUnresvCustOrderLog();
									omsResUnresvCustOrderLog.setAdjQty(new BigDecimal(-(omsTempCoFo.getOrderQty().intValueExact())));
									omsResUnresvCustOrderLog.setCreateTimestamp(new Timestamp(new Date().getTime()));
									omsResUnresvCustOrderLog.setItem(omsTempCoFo.getItem());
									omsResUnresvCustOrderLog.setLocation(new BigDecimal(omsTempCoFo.getSourceLocId().intValueExact()));
									omsResUnresvCustOrderLog.setOmsCustOrdNo(omsTempCoFo.getOmsCustOrdNo());
									omsResUnresvCustOrderLog.setStatus("N");
									try {

										log.info(" Inserting the record omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into resv and unresv table");
										session.persistOmsResUnresvCustOrderLog(omsResUnresvCustOrderLog);
									} catch (Exception e1) {
										log.info(e1);
										log.info(" error occured while insert record omsCustordNo" + omsTempCoFo.getOmsCustOrdNo() + "insert into persistOmsResUnresvCustOrderLog");
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
									}
								}
							}
						}

					} catch (Exception e) {
						log.error(e);
					} finally {
						try {
							pstmt.close();
							con.close();
						} catch (SQLException e) {
							log.error(e);
						}
					}
				}
			}
		}
	}

	public static void persistOmsCustOrdReserveForBackorder(BigDecimal custOrdHeadSeqNo, Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) throws SOAPException {
		log.info("***Start-persistOmsCustOrdReserve***");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		log.info("keyset" + fulfillDetailMap.keySet());
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			ArrayList<OmsTempCoFo> list = fulfillDetailMap.get(key);
			for (OmsTempCoFo omsTempCoFo : list) {

				OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(custOrdHeadSeqNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());

				log.info("omsCustOrdNo " + custOrdHeadSeqNo + "Persisting for item=" + omsTempCoFo.getItem() + "order qty=" + omsTempCoFo.getOrderQty() + "Fulfill orde no"
						+ omsTempCoFo.getFulfillOrderNo());
				log.info("line no" + omsTempCoFo.getLineNo() + "Source loc" + omsTempCoFo.getSourceLocId());
				OmsCustOrdReserve omsCustOrdReserve = null;
				boolean found = false;
				try {
					omsCustOrdReserve = session.getOmsCustOrdReserveFindByOmsCustOrdNoAndItemAndResvLoc(custOrdHeadSeqNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo(), omsTempCoFo.getSourceLocId());
					found = true;
				} catch (Exception e) {
					log.info("No record in oms_cust_ord_reserve" + e);
				}
				if (found == true) {
					log.info("Previous value is of resv qty us" + omsCustOrdReserve.getRmsResvQty() + "New value is " + omsCustOrdReserve.getRmsResvQty().add(omsTempCoFo.getOrderQty()));
					omsCustOrdReserve.setRmsResvQty(omsCustOrdReserve.getRmsResvQty().add(omsTempCoFo.getOrderQty()));
					omsCustOrdReserve.setQty(omsCustOrdReserve.getQty().add(omsTempCoFo.getOrderQty()));
					session.mergeOmsCustOrdReserve(omsCustOrdReserve);
				} else {
					log.info("Creating new record in oms_cust_ord_reserve");
					omsCustOrdReserve = new OmsCustOrdReserve();
					omsCustOrdReserve.setItem(omsTempCoFo.getItem());
					omsCustOrdReserve.setLineNo(omsTempCoFo.getLineNo());
					omsCustOrdReserve.setLoc(omsTempCoFo.getFulfillLocId()); // fulfilloc
					omsCustOrdReserve.setOmsCustOrdNo(custOrdHeadSeqNo); // cust head sequence
					omsCustOrdReserve.setFulfillOrderNo(omsTempCoFo.getFulfillOrderNo());
					omsCustOrdReserve.setQty(omsTempCoFo.getOrderQty()); // order qty
					omsCustOrdReserve.setPaymentStatus("P");
					omsCustOrdReserve.setResvStatus("RES");
					omsCustOrdReserve.setLocType(omsTempCoFo.getFulfillLocationType());
					omsCustOrdReserve.setInitiateTs(new Timestamp(new Date().getTime()));
					omsCustOrdReserve.setRmsResvLoc(omsTempCoFo.getSourceLocId()); // source loc
					if (omsTempCoFo.getSourceLocationType().equals("WH")) {
						log.info("Source loc=" + omsTempCoFo.getStatus());
						// Below line is commented to it was throwing null pointer exception
						// omsCustOrdReserve.setRmsResvLoc(new BigDecimal(omsTempCoFo.getStatus()));
						omsCustOrdReserve.setRmsResvLoc(omsTempCoFo.getSourceLocId());

					} else {
						log.info("Source loc=" + omsTempCoFo.getSourceLocId());
						omsCustOrdReserve.setRmsResvLoc(omsTempCoFo.getSourceLocId());
					}
					omsCustOrdReserve.setRmsResvLocType(omsTempCoFo.getSourceLocationType());
					omsCustOrdReserve.setRmsResvQty(omsTempCoFo.getOrderQty()); // order qty
					omsCustOrdReserve.setCombinationId(omsTempCoFo.getCombinationId());
					omsCustOrdReserve.setCreatedBy("User");
					omsCustOrdReserve.setCreateDatetime(new Timestamp(new Date().getTime()));
					session.persistOmsCustOrdReserve(omsCustOrdReserve);
					log.info("persistOmsCustOrdReserve success");
				}
			}
		}
		log.info("success");
	}

	// Added new code for bug 2601

	public BigDecimal getMaxFulFilOrderNoFromOrdCust(String customerOrderNo) throws SOAPException {
		log.info("inside getMaxFulFilOrderNoFromOrdCust " + customerOrderNo);
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		String fulfilOrdNo = null;
		BigDecimal maxFulFilOrdNo = BigDecimal.ZERO;
		log.info("calling getMaxFulFilOrdNofromOrdCust" + customerOrderNo);
		try {
			fulfilOrdNo = session.getMaxFulFilOrdNofromOrdCust(customerOrderNo);
			maxFulFilOrdNo = new BigDecimal(fulfilOrdNo);
			log.info("maxFulFilOrdNo from OrdCust for customerOrderNo " + customerOrderNo + "is" + fulfilOrdNo);
		} catch (Exception e) {
			maxFulFilOrdNo = BigDecimal.ZERO;
			log.info("Exception while getting the maxFulFillOrdNo " + e.getMessage());
		}

		log.info("Returning maxFulFilOrdNo " + maxFulFilOrdNo);
		return maxFulFilOrdNo;
	}

	public void persistOmsCoFulFilDetail(BigDecimal key1, ArrayList<OmsTempCoFo> tempList, OmsCoFulfillDetail omsCoFulfillDetail, BigDecimal omsCustOrdNo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.EntityAlreadyExistsWSFaultException {
		log.info("inside persistOmsCoFulFilDetail");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		log.info("omsCustOrdNo " + omsCustOrdNo + "key " + key1);
		for (OmsTempCoFo omsTemp : tempList) {
			if (key1.intValue() == omsTemp.getFulfillOrderNo().intValue() && omsTemp.getFoConfQty().intValue() > 0) {
				log.info("omsCustOrdNo=" + omsCustOrdNo + "omsTemp.getLineNo()=" + omsTemp.getLineNo() + "omsTemp.getSourceLocId()=" + omsTemp.getSourceLocId() + "item=" + omsTemp.getItem());
				log.info("status" + omsTemp.getStatus() + " virtualWH=" + omsTemp.getVirtualWH() + "omsTemp.getLineNo()" + omsTemp.getLineNo());
				log.info("fulf ord no=" + omsTemp.getFulfillOrderNo());
				OmsBackOrderDtl omsBackOrderDtlTemp = null;
				log.info("omsTemp.getSourceLocationType()=" + omsTemp.getSourceLocationType() + "source loc=" + omsTemp.getSourceLocId());
				if (omsTemp.getSourceLocationType().equals("WH")) {
					omsBackOrderDtlTemp = session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(omsCustOrdNo, omsTemp.getLineNo(), omsTemp.getVirtualWH());
					log.info("inside WH for omsCustOrdNo " + omsCustOrdNo + "and virtualWH " + virtualWH);
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsBackOrderDtlTemp.getFulfillQty()" + omsBackOrderDtlTemp.getFulfillQty());
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsTemp.getOrderQty() " + omsTemp.getFoConfQty());
					log.info("omsCustOrdNo " + omsCustOrdNo + "adding " + omsTemp.getFoConfQty().add(omsBackOrderDtlTemp.getFulfillQty()));
					omsBackOrderDtlTemp.setFulfillQty(omsTemp.getFoConfQty().add(omsBackOrderDtlTemp.getFulfillQty()));
					omsBackOrderDtlTemp.setConfTs(new Timestamp(new java.util.Date().getTime()));
					session.mergeOmsBackOrderDtl(omsBackOrderDtlTemp);
					log.info("Persisted into mergeOmsBackOrderDtl with order qty as " + omsTemp.getOrderQty() + " fulfill orderno=" + omsTemp.getFulfillOrderNo());
				} else {
					omsBackOrderDtlTemp = session.getOmsBackOrderDtlFindByOmsCustOrdNoAndLinNoAndLoc(omsCustOrdNo, omsTemp.getLineNo(), omsTemp.getSourceLocId());
					log.info("inside else for omsCustOrdNo " + omsCustOrdNo);
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsBackOrderDtlTemp.getFulfillQty()" + omsBackOrderDtlTemp.getFulfillQty());
					log.info("omsCustOrdNo " + omsCustOrdNo + "omsTemp.getFoConfQty() " + omsTemp.getFoConfQty());
					log.info("omsCustOrdNo " + omsCustOrdNo + "adding FulfillQty" + omsTemp.getFoConfQty().add(omsBackOrderDtlTemp.getFulfillQty()));
					omsBackOrderDtlTemp.setFulfillQty(omsTemp.getFoConfQty().add(omsBackOrderDtlTemp.getFulfillQty()));
					omsBackOrderDtlTemp.setConfTs(new Timestamp(new java.util.Date().getTime()));
					session.mergeOmsBackOrderDtl(omsBackOrderDtlTemp);
					log.info("Persisted into mergeOmsBackOrderDtl with order qty as " + omsTemp.getOrderQty() + " fulfill orderno=" + omsTemp.getFulfillOrderNo());

				}
				omsCoFulfillDetail.setFulfillOrderNo(omsTemp.getFulfillOrderNo());
				omsCoFulfillDetail.setFulfillLocType(omsTemp.getFulfillLocationType());
				omsCoFulfillDetail.setFulfillLoc(omsTemp.getFulfillLocId());
				omsCoFulfillDetail.setLineNo(omsTemp.getLineNo());
				omsCoFulfillDetail.setItem(omsTemp.getItem());
				omsCoFulfillDetail.setOmsCustOrdNo(omsCustOrdNo);
				log.info("omsCustOrdNo " + omsCustOrdNo + " combinationId" + omsTemp.getCombinationId());
				omsCoFulfillDetail.setCombinationId(omsTemp.getCombinationId());
				omsCoFulfillDetail.setSourceLoc(omsTemp.getSourceLocId());
				omsCoFulfillDetail.setSourceLocType(omsTemp.getSourceLocationType());
				omsCoFulfillDetail.setFulfillReqQty(omsTemp.getFoConfQty());
				omsCoFulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
				omsCoFulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
				omsCoFulfillDetail.setFulfillConfQty(omsTemp.getFoConfQty());
				omsCoFulfillDetail.setFulfillStatus("C");
				omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
				try {
					BigDecimal tsfNo = null;
					OmsCustOrdHead omsCustOrdHead1 = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					if (omsCustOrdHead1.getStatus().equals("S")) {
						log.info("omsCustOrdHead1.getCustOrderNo() " + omsCustOrdHead1.getCustOrderNo());
						tsfNo = session.getOrdcustFindByFulfilOrdNo(omsCustOrdHead1.getCustOrderNo(), omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(),
								omsCoFulfillDetail.getFulfillLoc()).get(0).getTsfNo();
						log.info("omsCustOrdNo " + omsCustOrdNo + " tsfNo " + tsfNo);
					}

					if (tsfNo != null) {
						omsCoFulfillDetail.setTsfNo(tsfNo);
						try {
							OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
							String status = omsUtilCommons.approveTransfers(tsfNo, omsTemp.getSourceLocId());
							if (status.equals("A")) {
								transferList.add(tsfNo);
								omsCoFulfillDetail.setTsfApprovalStatus("A");
								log.info("omsCustOrdNo " + omsCustOrdNo + "Transfer approved succesfully");
							} else {
								if (omsTemp.getSourceLocationType().equals("ST")) {
									log.info("omsCustOrdNo" + omsCustOrdNo + "transferList " + transferList.toString());
									log.info("omsCustOrdNo" + omsCustOrdNo + "tsfNo " + tsfNo);
									if (!transferList.toString().contains(tsfNo.toString())) {
										OmsUnapprovedTransfers omsUnapprovedTransfers = new OmsUnapprovedTransfers();
										omsUnapprovedTransfers.setOmsCustOrdNo(omsCustOrdNo);
										omsUnapprovedTransfers.setCreateDatetime(new Timestamp(new Date().getTime()));
										omsUnapprovedTransfers.setTsfNo(tsfNo);
										omsUnapprovedTransfers.setItem(omsTemp.getItem());
										omsUnapprovedTransfers.setLocation(omsTemp.getSourceLocId());
										omsUnapprovedTransfers.setUnapprovedQty((omsTemp.getOrderQty()));
										session.persistOmsUnapprovedTransfers(omsUnapprovedTransfers);
										log.info("omsCustOrdNo" + omsCustOrdNo + "Persisting into OmsUnapprovedTransfers for item =" + omsTemp.getItem() + " at location " + omsTemp.getSourceLocId()
												+ " with qty=" + omsUnapprovedTransfers.getUnapprovedQty());
									}
								}
							}
						} catch (Exception e) {
							log.error("Failed in approving transfer");
						}
					}

				} catch (Exception e) {
					log.info("Exception in BackOrder while fetching the TSF No " + e.getMessage());
				}
				session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
				log.info("persiting in omsCoFulfillDetail");
				log.info("calling RMS back order webservice from the batch" + omsTemp.getItem() + "qty=" + omsTemp.getFoConfQty().negate() + "loc=" + omsTemp.getSourceLocId().longValue());
				BigDecimal physicalWH = BigDecimal.ZERO;
				BigDecimal channelId = BigDecimal.ZERO;
				if (omsTemp.getSourceLocationType().equals("WH")) {
					if (omsTemp.getSourceLocId().equals("WH")) {
						List<Object[]> tempWhObject = session.getWhFindPhysicalWH(omsTemp.getSourceLocId());
						for (Object[] result : tempWhObject) {

							physicalWH = new BigDecimal(result[0].toString());
							// omsFulfillMatrixExtDetail.setLocation(physicalWH);
							channelId = new BigDecimal(result[1].toString());
							log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0] + "channel id=" + result[1]);
						}

					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "calling RMS back order webservice" + omsTemp.getItem() + "qty=" + omsCoFulfillDetail.getFulfillConfQty().negate() + "loc="
							+ omsTemp.getSourceLocId().longValue());
					interfacePersistence.callRMSBackorderWS(omsTemp.getItem(), omsCoFulfillDetail.getFulfillConfQty().negate(), omsTemp.getSourceLocId().longValue(), "W", "EA", channelId);
					log.info("omsCustOrdNo " + omsCustOrdNo + "successfully called the RMS back order webservice");
				} else {
					log.info("omsCustOrdNo " + omsCustOrdNo + "calling RMS back order webservice" + omsTemp.getItem() + "qty=" + omsCoFulfillDetail.getFulfillConfQty().negate() + "loc="
							+ omsTemp.getSourceLocId().longValue());
					interfacePersistence.callRMSBackorderWS(omsTemp.getItem(), omsCoFulfillDetail.getFulfillConfQty().negate(), omsTemp.getSourceLocId().longValue(), "S", "EA", channelId);
					log.info("omsCustOrdNo " + omsCustOrdNo + "successfully called the RMS back order webservice");
				}
				List<OmsBackOrderDtl> omsBackOrderDetails = session.getOmsBackOrderDtlFindByOmsCustOrdNo(omsCustOrdNo);
				for (OmsBackOrderDtl omsBackOrderDtl : omsBackOrderDetails) {
					if (omsBackOrderDtl.getSourceQty().compareTo(omsBackOrderDtl.getFulfillQty()) == 0) {
						log.info("omsCustOrdNo " + omsCustOrdNo + "updating BackorderStatus to S since the SourceQty is equal to FulfillQty");
						omsBackOrderDtl.setBackorderStatus("S");
						log.info("omsCustOrdNo " + omsCustOrdNo + "setted to S");
					} else {
						log.info("omsCustOrdNo " + omsCustOrdNo + "updating BackorderStatus to N since the SourceQty is not equal to FulfillQty");
						omsBackOrderDtl.setBackorderStatus("N");
						log.info("omsCustOrdNo " + omsCustOrdNo + "setted to N");
					}
					omsBackOrderDtl.setConfTs(new Timestamp(new java.util.Date().getTime()));
					log.info("omsCustOrdNo " + omsCustOrdNo + "calling mergeOmsBackOrderDtl ");
					session.mergeOmsBackOrderDtl(omsBackOrderDtl);
					log.info("omsCustOrdNo " + omsCustOrdNo + "merged omsBackOrderDtl");
				}
			} // end of for loop
				// }
		} // end of method
	}

	public OmsTempCoFo createFulfilMapObject(BigDecimal omsCustOrdNo, String item, BigDecimal sourceLocId, String sourceLocType, BigDecimal fulfillLocId, String fulFillLocType, int availableQty,
			BigDecimal lineNo, BigDecimal combID, int maxFulfillOrderNo, BigDecimal virtualWH, TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> treeMap, Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap) {
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

	public BigDecimal getAvailableQtyFromOmsBackOrderDtl(BigDecimal location, String item) throws SOAPException

	{
		log.info(" inside getAvailableQtyFromOmsBackOrderDtl in backorder class");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal qty = BigDecimal.ZERO;
		qty = session.getOmsBackOrderDtlFindAssignedInvOrders(location, item);
		if (qty != null) {
			return qty;
		}
		return qty;
	}

	public int getStoreSOH(OmsBackOrderDtl omsBackOrderDtl, long L_Cum_Ord_Qty, long L_Pending_Qty, BigDecimal sourceLocId, Map<String, BigDecimal> storeItemMap, Map<String, ItemSOH> itemQtyMap,
			BigDecimal storeSOH) throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
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
		int checkNagativeStock = 0;
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int SOH = 0;
		BigDecimal omsCustOrdNo = omsBackOrderDtl.getOmsCustOrdNo();
		String itemandLoc = omsBackOrderDtl.getItem() + "," + sourceLocId.toString();
		log.info("omsCustOrdNo " + omsCustOrdNo + "Procesing for line No " + omsBackOrderDtl.getLineNo() + "itemand Loc " + itemandLoc);
		// added code for duplicate items - fulfillment logic
		log.info("omsCustOrdNo" + omsCustOrdNo + "itemQtyMap.keySet() " + itemQtyMap.keySet());
		log.info("omsCustOrdNo" + omsCustOrdNo + "storeItemMap.keySet()" + storeItemMap.keySet());
		log.info("omsCustOrdNo" + omsCustOrdNo + "omsCustOrdNo " + omsCustOrdNo + "Started the code for Duplicate Items - fulfillment logic" + " , Ordered Qty "
				+ (omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty())));
		log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
		log.info("omsCustOrdNo" + omsCustOrdNo + "pending qty  " + L_Pending_Qty);
		int requestQty = 0;
		InterfacePersistence interfacePersistence = new InterfacePersistence();

		// check for needed quantity to fulfill
		if (L_Pending_Qty == 0 && L_Cum_Ord_Qty == 0) {
			requestQty = omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue();
			log.info("omsCustOrdNo" + omsCustOrdNo + "requestQty when L_Pending_Qty and l_cum_ord_qty is equal to 0" + requestQty);
		} else if (omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() >= L_Cum_Ord_Qty) {
			requestQty = omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() - (int) L_Cum_Ord_Qty;
			log.info("omsCustOrdNo" + omsCustOrdNo + "custOrdItems.getOrderQtySuom().intValue()>=L_Cum_Ord_Qty" + requestQty);
		} else if (L_Cum_Ord_Qty >= omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue()) {
			requestQty = (int) L_Cum_Ord_Qty - omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue();
			log.info("omsCustOrdNo" + omsCustOrdNo + "L_Cum_Ord_Qty>=omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()).intValue() " + requestQty);
		}
		log.info("============================================================================ line " + omsBackOrderDtl.getLineNo());
		if (itemQtyMap.containsKey(omsBackOrderDtl.getItem())) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside containsKey condition for itemQtyMap for line No " + omsBackOrderDtl.getLineNo());
			if (storeItemMap.containsKey(itemandLoc))
			// if(itemQtyMap.get(custOrdItmDesc.getItemId()).getLocation().compareTo(nextLoc)==0)
			{
				log.info("omsCustOrdNo " + omsCustOrdNo + "inside compareTo condition for item id");
				// SOH=itemQtyMap.get(custOrdItmDesc.getItemId()).getSoh().longValue();
				SOH = storeItemMap.get(itemandLoc).intValue();
				log.info("omsCustOrdNo " + omsCustOrdNo + "itemQtyMap already contain item with SOH=" + SOH);
				ItemSOH itemSOH = itemQtyMap.get(omsBackOrderDtl.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + omsBackOrderDtl.getLineNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + omsBackOrderDtl.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "SOH " + SOH);
				log.info("omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()) " + omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()));
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
					itemQtyMap.put(omsBackOrderDtl.getItem(), itemSOH);
					if (storeItemMap.get(itemandLoc) != null) {
						storeSOH = storeItemMap.get(itemandLoc);
						log.info("storeSOH" + itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
						storeItemMap.put(itemandLoc, itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
					}
					availbleQuantity = (int) SOH;
					log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
				}
				itemQtyMap.put(omsBackOrderDtl.getItem(), itemSOH);
				log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
				log.info("itemSOH.getSoh() " + itemSOH.getSoh());
				long a = itemSOH.getSoh().longValue() - L_Cum_Ord_Qty;
				log.info("omsCustOrdNo " + omsCustOrdNo + "updated the soh to " + itemSOH.getSoh());
				log.info("a " + a);
				if (storeItemMap.get(itemandLoc) != null) {
					storeSOH = storeItemMap.get(itemandLoc);
					log.info("storeSOH" + itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
					storeItemMap.put(itemandLoc, itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
				}

			} // end of 2nd if
			else {
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item exist in map ,but find the SOH with next loc");
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				/// mani mani addded the code back order bug fix for store
				StoreInventory storeInventory = new StoreInventory();
				SOH = storeInventory.getStockForAnItemAndLocationFromSIM(omsBackOrderDtl.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
				checkNagativeStock = SOH;
				log.info("availble qty in store" + SOH);
				// code fix end
				SOH = (int) interfacePersistence.callSIMStoreInventory(omsBackOrderDtl.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
				log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + omsBackOrderDtl.getItem() + "store " + sourceLocId + " SOH" + SOH);

				/// mani addded the code back order bug fix for store
				log.info("back order stock calculation for store value" + SOH);
				if (SOH <= 0 && omsBackOrderDtl.getBackorderStatus() != null) {
					log.info("back order nagative stock block" + SOH);
					log.info("before replacing negative stock into acutal stock " + SOH);
					SOH = checkNagativeStock;
					log.info("After replacing negative stock into acutal stock " + SOH);
				}

				ItemSOH itemSOH = itemQtyMap.get(omsBackOrderDtl.getItem());
				log.info("omsCustOrdNo " + omsCustOrdNo + "=======inside if (storeItemMap.containsKey(itemandLoc))======");
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getLineNo() " + omsBackOrderDtl.getLineNo());
				log.info("omsCustOrdNo " + omsCustOrdNo + "custOrdItems.getItem() " + omsBackOrderDtl.getItem());
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
				itemQtyMap.put(omsBackOrderDtl.getItem(), itemSOH);
				log.info("putting the SOH in map " + itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
				storeItemMap.put(itemandLoc, itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
				log.info("=====================================================================");

			}
			// SOH = interfacePersistece.callSIMStoreInventory(custOrdItmDesc.getItemId(),
			// nextLoc);
		} // end of major 1st if
		else {
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item does not contain in itemQtyMap");
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);

			/// mani mani addded the code back order bug fix for store
			StoreInventory storeInventory = new StoreInventory();
			SOH = storeInventory.getStockForAnItemAndLocationFromSIM(omsBackOrderDtl.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
			checkNagativeStock = SOH;
			log.info("availble qty in store" + SOH);
//code fix end

			SOH = (int) interfacePersistence.callSIMStoreInventory(omsBackOrderDtl.getItem(), sourceLocId, omsCustOrdHead.getApplicationId());
			log.info("omsCustOrdNo " + omsCustOrdNo + "Item " + omsBackOrderDtl.getItem() + "sourceLocId " + sourceLocId + " SOH" + SOH);

			/// mani addded the code back order bug fix for store
			log.info("back order stock calculation for store value" + SOH);
			if (SOH <= 0 && omsBackOrderDtl.getBackorderStatus() != null) {
				log.info("back order nagative stock block" + SOH);
				log.info("before replacing negative stock into acutal stock " + SOH);
				SOH = checkNagativeStock;
				log.info("After replacing negative stock into acutal stock " + SOH);
			}
			// code fix end

			ItemSOH itemSOH = new ItemSOH();
			itemSOH.setLocation(sourceLocId);
			log.info("omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()) " + omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()));
			log.info("requestQty " + requestQty);
			log.info("L_Cum_Ord_Qty " + L_Cum_Ord_Qty);
			long L_Pending_Qty_temp = requestQty - L_Cum_Ord_Qty;
			log.info("L_Pending_Qty_temp " + L_Pending_Qty_temp);
			L_Pending_Qty = L_Pending_Qty_temp;
			log.info("SOH" + SOH);
			log.info("omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty())" + omsBackOrderDtl.getSourceQty().subtract(omsBackOrderDtl.getFulfillQty()));
			if (SOH >= requestQty) {
				log.info("inside SOH>=custOrdItmDesc.getQuantity().intValue() " + new BigDecimal(SOH - requestQty));
				itemSOH.setSoh(new BigDecimal(SOH - requestQty));
				availbleQuantity = requestQty;
				log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
			} else {
				log.info("inside else for SOH>=requestQty condition");
				log.info("itemSoh.getSoh()" + itemSOH.getSoh());
				itemSOH.setSoh(BigDecimal.ZERO);
				log.info("itemSoh.getSoh()" + itemSOH.getSoh());
				availbleQuantity = (int) SOH;
				log.info("omsCustOrdNo " + omsCustOrdNo + "availbleQuantity " + availbleQuantity);
			}
			log.info("putting into itemQtyMap map Location" + itemSOH.getLocation());
			log.info("putting into itemQtyMap map SOH " + itemSOH.getSoh());
			itemQtyMap.put(omsBackOrderDtl.getItem(), itemSOH);
			log.info("putting into storeItem map " + itemSOH.getSoh());
			storeSOH = itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh();
			log.info("storeSOH " + storeSOH);
			storeItemMap.put(itemandLoc, itemQtyMap.get(omsBackOrderDtl.getItem()).getSoh());
			log.info("after putting into the storeMap " + storeItemMap.keySet());
			log.info("omsCustOrdNo " + omsCustOrdNo + "itemQty map does not contain values with SOH=" + itemSOH.getSoh());
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "Completed the try block for Duplicate items -Fulfillment Logic for line No " + omsBackOrderDtl.getLineNo());
		return availbleQuantity;

	}

	public int getWHSOH(OmsBackOrderDtl omsBackOrderDtl, long L_Pending_Qty) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		int availabeQuantity = 0;
		int requestdQty = (int) L_Pending_Qty;
		int SOH = 0;

		virtualWH = omsBackOrderDtl.getSourceLoc();
		BigDecimal sourceLocationId = omsBackOrderDtl.getSourceLoc();
		BigDecimal omsCustOrdNo = omsBackOrderDtl.getOmsCustOrdNo();
		log.info("omsCustOrdNo" + omsCustOrdNo + "Processing for line No " + omsBackOrderDtl.getLineNo() + "and item " + omsBackOrderDtl.getItem() + " in WH");
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
		sourceLocationId = physicalWH;
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH" + omsBackOrderDtl.getFulfillLoc() + " Source loc id is " + sourceLocationId);
		List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);

		int i = 0;
		while (i < locList.size()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
			i++;
		}
		OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
		String applicationId = "E-COMMERCE";
		SOH = omsUtilCommons.checkSOHForWH(omsBackOrderDtl.getItem(), locList, applicationId).intValue();
		log.info("omsCustOrdNo" + omsCustOrdNo + "SOH for item " + omsBackOrderDtl.getItem() + "Line No " + omsBackOrderDtl.getLineNo() + "is " + SOH);
		if (SOH >= requestdQty) {
			availabeQuantity = requestdQty;
		} else if (SOH <= requestdQty) {
			availabeQuantity = SOH;
		}
		// availabeQuantity=
		// availabeQuantity-getAvailableQtyFromOmsBackOrderDtl(sourceLocationId,omsBackOrderDtl.getItem()).intValue();
		return availabeQuantity;
	} // End of method

	public void rollbackforSIMForBackOrder(String customerOrderNo, FulfilOrdRef fulfilOrdRef)
			throws SOAPException, EntityAlreadyExistsWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int id = 0;
		String query = "select * from ful_ord where cust_order_id=? ";
		log.info("query : " + query);

		Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>();
		FulfilOrdColRef fulfilOrdColRef1 = new FulfilOrdColRef();
		fulfilOrdColRef1.setCollectionSize(1);
		fulfilOrdColRef.value = fulfilOrdColRef1;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_SIM_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setString(1, customerOrderNo);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				log.info("equal");
				id = rs.getInt("id");
				fulfilOrdColRef.value = fulfilOrdColRef1;
				fulfilOrdRef.setCustomerOrderNo(customerOrderNo);
				fulfilOrdRef.setFulfillLocId(rs.getLong("STORE_ID"));
				fulfilOrdRef.setFulfillLocType(fulfilOrdRef.getFulfillLocType().fromValue("S"));
				fulfilOrdRef.setFulfillOrderNo(rs.getString("EXTERNAL_ID"));
				log.info("the id from table ful_ord : " + id);
				// fulfilOrdRef = sendFulOrdLine(id,fulfilOrdRef);
				fulfilOrdColRef1.getFulfilOrdRef().add(fulfilOrdRef);
				fulfilOrdColRef.value = fulfilOrdColRef1;
				try {
					log.info("fulfilOrdColRef size=" + fulfilOrdColRef.value.getFulfilOrdRef().size());
					log.info("---" + fulfilOrdColRef.value.getFulfilOrdRef().get(0).getCustomerOrderNo());
					
					CancelFulfillmentOrderDetail detail = new CancelFulfillmentOrderDetail();
					detail.setFulfilOrdColRef(fulfilOrdColRef1);
					OracleBaseAPIUtil.getOracleSIMClient().cancelFulfillmentOrderDetail(detail);
					log.info(">>>");
				} catch (Exception e) {
					log.error("Error in sim cancellation : " + e.getMessage());
				}
			}
		} catch (Exception e1) {
			log.info(e1.getMessage());
			log.info("e1 " + e1);
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e2) {
				log.error(e2.getMessage());
			}
		}

	} // End of RollbackforSIM

	public String getBatchProcessingIndicator() throws Exception {
		log.info("***getBatchProcessingIndicator ***");
		String batchProcessInd = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "select status from OMS_BACK_ORDER_BATCH_CHECK where status='IN_PROGRESS'";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				batchProcessInd = rs.getString("STATUS");

			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}

		return batchProcessInd;
	}

	public int insertBackOrderBachProgress() throws Exception {
		log.info("***insert backorder batch processing indicator ***");
		String batchProcessInd = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ResultSet rsSeq = null;
		int backOrderBatchSequence = 0;
		String sequenceQuery = "select BACK_ORDER_BATCH_SEQ.NEXTVAL as SEQUENCE_NUM from dual";
		String query = "insert into OMS_BACK_ORDER_BATCH_CHECK(BATCH_ID,BATCH_START_TIME,STATUS) values(?,SYSTIMESTAMP,'IN_PROGRESS')";
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			statement = conn.prepareStatement(sequenceQuery);
			rs = statement.executeQuery();
			if (rs.next()) {
				backOrderBatchSequence = rs.getInt("SEQUENCE_NUM");
			}
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.setInt(1, backOrderBatchSequence);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
				OMSUtil.closeDBConnection(conn, statement, rs);

			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}

		return backOrderBatchSequence;
	}

	public String updateBackOrderBatchStatus(String status, int batchId) throws Exception {
		log.info("***update Backorder Batch Indicator ***");
		String batchProcessInd = null;
		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String query = "update OMS_BACK_ORDER_BATCH_CHECK set status='" + status + "',BATCH_END_TIME=systimestamp where status='IN_PROGRESS' and batch_id=" + batchId;
		try {
			conn = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
			preparedStatement = conn.prepareStatement(query);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new SOAPException(e.getMessage());
		} finally {
			try {
				OMSUtil.closeDBConnection(conn, preparedStatement, rs);
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new SOAPException(e.getMessage());
			}
		}

		return batchProcessInd;
	}

}
