package com.logicinfo.oms.model;

import com.logicinfo.oms.beans.OMSUtilCommons;
import com.logicinfo.oms.beans.PartialResponse;
import com.logicinfo.oms.beans.ReturnPartialResponseObject;
import com.logicinfo.oms.ejb.OMSUtilSessionEJB;
import com.logicinfo.oms.ejb.OmsCoFulfillDetail;
import com.logicinfo.oms.ejb.OmsCustOrdHead;
import com.logicinfo.oms.ejb.OmsCustOrdItem;
import com.logicinfo.oms.ejb.OmsFulfillMatrixExtDetail;
import com.logicinfo.oms.ejb.OmsRtlogPublishLog;
import com.logicinfo.oms.ejb.OmsTempCoFo;
import com.logicinfo.oms.ejb.OmsUnapprovedTransfers;
import com.logicinfo.oms.ejb.Ordcust;
import com.logicinfo.oms.ejb.OrdcustDetail;
import com.logicinfo.oms.util.BusinessException;
import com.logicinfo.oms.util.OMSConstants;
import com.logicinfo.oms.util.OMSUtil;
import com.logicinfo.oms.util.OracleBaseAPIUtil;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.ConfirmType;
import com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1.FulfilOrdCfmDtl;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderService;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
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

public class NonSADADPayment extends OMSCustomerOrderBean {
	public NonSADADPayment() {
		super();
	}

	OMSUtilCommons oMSUtilCommons = new OMSUtilCommons();
	private final static Logger log = Logger.getLogger(NonSADADPayment.class.getName());
	BigDecimal fulfilmentOrderNo = null;
	FulfilOrdCfmCol fulfilOrdCfmCol = null;
	ConfirmType confirmType = null;
	int retryWS = 2;
	boolean flag = false;
	int currentFulFilOrderNo = 0;
	int maxFulfilOrderNo = 0;
	FulfilOrdCfmCol pfulfilOrdCfmCol = null;
	ConcurrentHashMap<BigDecimal, FulfilOrdCfmCol> pfulfilOrdCfmCols = new ConcurrentHashMap<BigDecimal, FulfilOrdCfmCol>();
	ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> xfulfilOrdCfmCols = new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
	ArrayList<BigDecimal> transferList = new ArrayList<BigDecimal>();

	public void processNonSadad(TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap1, BigDecimal omsCustOrdNo, CustomerOrder input)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.ValidationWSFaultException, EntityAlreadyExistsWSFaultException, BusinessException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "***Start : processNonSadad");
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfilDetailMap size=" + fulfillDetailMap1.size() + "for omsCustOrdNo=" + omsCustOrdNo);
		if (fulfillDetailMap1.size() > 0) {
			ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap = new ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
			fulfillDetailMap.putAll(fulfillDetailMap1);
			OMSUtilSessionEJB session = OMSUtil.doLookup();
			ProcessedObject processedObject = null;
			InterfacePersistence interfacePersistence = new InterfacePersistence();
			ArrayList<OmsTempCoFo> tempList = null;
			TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> newFulfillDetailMap = null;
			TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> tempMap1 = new TreeMap<BigDecimal, ArrayList<OmsTempCoFo>>();
			tempMap1.putAll(fulfillDetailMap);
			for (int k = 1; k <= tempMap1.lastKey().intValue(); k++) {
				BigDecimal key = new BigDecimal(k);
				try {
					log.info("omsCustOrdNo " + omsCustOrdNo + "key :" + key);
					tempList = fulfillDetailMap.get(key);
					if (tempList == null) {
						continue;
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "inside try of process Non Sadad");
					log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillDetailMap.get(key) " + fulfillDetailMap.get(key));
					log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillDetailMap.keySet() " + fulfillDetailMap.keySet());
					log.info("omsCustOrdNo " + omsCustOrdNo + "checking RmsResponseCode is equal to null ");
					if (tempList.get(0).getRmsResponseCode().isEmpty() || tempList.get(0).getRmsResponseCode() == null) {
						// call of Web services of RMS and SIM
						for (int _count = 1; _count <= 5; _count++) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "------Before calling the Web Service call --- Count Value is" + _count);
							try {
								log.info("omsCustOrdNo " + omsCustOrdNo + "-------------------------Calling  the web serivce -------------------");
								processedObject = interfacePersistence.callWebservices(omsCustOrdNo, tempList);
								log.info("omsCustOrdNo " + omsCustOrdNo + "--------------------Successfull Happened Count Value is" + _count);
								break;
							} catch (Exception e) {
								log.error("omsCustOrdNo " + omsCustOrdNo + "#################------------ Catch Exception----------------############", e);
								log.info("omsCustOrdNo " + omsCustOrdNo + "<----Exception  Occured---- count-->" + _count + "---" + e.getMessage() + "-----------");
								if (_count == 5) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "-------------------------Error Message----------" + e.getMessage());
									throw e;
								}
							}
						}
						log.info("omsCustOrdNo " + omsCustOrdNo + "Call successful ,retuned processed object");
						log.info("omsCustOrdNo " + omsCustOrdNo + "Returned processed object transfer : " + processedObject.getTsf_no());
						fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
						currentFulFilOrderNo = processedObject.getCurrentFulFilOrderNo();
						tempList = processedObject.getOmsTempCoFoList();
						log.info("omsCustOrdNo " + omsCustOrdNo + "Processed object size" + tempList.size());
						// Update the value of map with processing app (SIM/RMS)
						fulfillDetailMap.put(key, tempList);
						maxFulfilOrderNo = tempMap1.lastKey().intValue();
						log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfilOrderNo after calling RMS " + maxFulfilOrderNo);
					} else if (tempList.get(0).getRmsResponseCode().equals("X")) {
						XResponseProcessingObj xResponseProcessingObj = processXResponse(omsCustOrdNo, tempList, maxFulfilOrderNo, fulfillDetailMap, newFulfillDetailMap, input);
						fulfillDetailMap = xResponseProcessingObj.getFulfillDetailMap();
						tempMap1.putAll(fulfillDetailMap);
						maxFulfilOrderNo = tempMap1.lastKey().intValue();
						log.info("omsCustOrdNo " + omsCustOrdNo + "maxFulfilOrderNo after calling X response " + maxFulfilOrderNo);
					} else {
						log.info("omsCustOrdNo " + omsCustOrdNo + "inside else block tempList.get(0).getRmsResponseCode() " + tempList.get(0).getRmsResponseCode());
						log.info("omsCustOrdNo " + omsCustOrdNo + "RmsResponseCode is not equal X or P");
					}
				} catch (javax.xml.ws.WebServiceException f) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "WSDL unanavailbale error " + f.getMessage());
					for (OmsTempCoFo omsTempCoFo : tempList) {
						OmsCustOrdItem omsCustOrdItem = session.getOmsCustOrdItemFindByItem(omsCustOrdNo, omsTempCoFo.getItem(), omsTempCoFo.getLineNo());
						omsCustOrdItem.setStatus("F");
						session.mergeOmsCustOrdItem(omsCustOrdItem);
					}
					log.info("omsCustOrdNo " + omsCustOrdNo + "Merging omsCustORdHead");
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					// OMSCustomerOrderBean bean = new OMSCustomerOrderBean();
					log.info("omsCustOrdNo " + omsCustOrdNo + "Rollbacking");
					// bean.rollback(fulfillDetailMap);
					try {
						// added by madhu
						NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
						nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
					} catch (Exception ex) {
						log.info("Exception occured during roll back");
					}
					rollbackWHandST(input.getCustomerOrderNo());
					log.info("omsCustOrdNo " + omsCustOrdNo + "throwing RMS_UNAVL");
					if (f instanceof SOAPFaultException) {
						throw ((SOAPFaultException) f);
					}
					throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("RMS_UNAVL"));
				} catch (Exception e) {
					if (processedObject != null && processedObject.getTsf_no() != null) {
						break;
					}
					log.error("Error in processing fulfillment :" + e.getMessage());
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					try {
						// added by madhu
						NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
						nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
					} catch (Exception ex) {
						log.warn("Exception During rolling back fulfillment", ex);
					}
					rollbackWHandST(input.getCustomerOrderNo());
					if (e instanceof BusinessException) {
						throw (BusinessException) e;
					} else {
						throw new RuntimeException(e.getMessage(), e);
					}
				}
				String responseStatus = "";
				if (processedObject != null && processedObject.getTsf_no() != null) {
					responseStatus = "C";
				} else {
					responseStatus = interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
				}
				// String responseStatus =
				// interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
				log.info("omsCustOrdNo " + omsCustOrdNo + "Response received is" + responseStatus);
				while (responseStatus.equals("C") == false && !"".equals(responseStatus)) {
					if (responseStatus.equals("X")) {
						log.info("Received X response.");
						log.info("omsCustOrdNo " + omsCustOrdNo + "Received X response.");
						log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
						log.info("currentFulfilOrderNo loop " + currentFulFilOrderNo);
						log.info("fulfillDetailMap.keySet() before calling X Response method " + fulfillDetailMap.keySet());
						try {
							XResponseProcessingObj xResponseProcessingObj = processXResponse(omsCustOrdNo, tempList, maxFulfilOrderNo, fulfillDetailMap, newFulfillDetailMap, input);
							fulfillDetailMap = xResponseProcessingObj.getFulfillDetailMap();
							tempMap1.putAll(fulfillDetailMap);
							maxFulfilOrderNo = tempMap1.lastKey().intValue();
							log.info("maxFulfilOrderNo after calling X response " + maxFulfilOrderNo);
							log.info("Printing fulfillDetailMap.keySet() after calling X response ");
							for (BigDecimal tempKey : fulfillDetailMap.keySet()) {
								log.info("tempKey " + tempKey);
								tempList = fulfillDetailMap.get(tempKey);
								for (OmsTempCoFo omsTemp : tempList) {
									if (omsTemp.getRmsResponseCode() != null && !"".equals(omsTemp.getRmsResponseCode())) {
										log.info("omsCustOrdNo " + omsCustOrdNo + "tempList.size() inside newFulfillDetailMap " + tempList.size());
										log.info("omsCustOrdNo " + omsCustOrdNo + "Fulfill order No in tempList loop" + omsTemp.getFulfillOrderNo());
										log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo " + omsTemp.getOmsCustOrdNo());
										log.info("omsCustOrdNo " + omsCustOrdNo + " lineNo= " + omsTemp.getLineNo());
										log.info("omsCustOrdNo " + omsCustOrdNo + " item= " + omsTemp.getItem());
										log.info("omsCustOrdNo " + omsCustOrdNo + " Quantity Ordered= " + omsTemp.getOrderQty());
										log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfilmetn OrderNo=" + omsTemp.getFulfillOrderNo());
										log.info("omsCustOrdNo " + omsCustOrdNo + " source loc id= " + omsTemp.getSourceLocId());
										log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfill loc id= " + omsTemp.getFulfillLocId());
										log.info("omsCustOrdNo " + omsCustOrdNo + " Quantity Confirmed= " + omsTemp.getFoConfQty());
										log.info("omsCustOrdNo " + omsCustOrdNo + " RmsResponseCode=" + omsTemp.getRmsResponseCode());
										responseStatus = omsTemp.getRmsResponseCode();
										log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
									}
								}
							}
							// Code for P/X, process P responses after X response
							for (BigDecimal pkey : pfulfilOrdCfmCols.keySet()) {
								log.info("<--- Process One by One P map in X--pkey--->" + pkey + "<----->");
								tempList = findUnProcessed_P_MapObject(fulfillDetailMap);
								if (tempList != null) {
									BigDecimal tempkey = getUnProceesedPResponsekey(fulfillDetailMap);
									ArrayList<OmsTempCoFo> unfulfilledItems = new ArrayList<OmsTempCoFo>();
									log.info("<---The value of temp list is---->" + tempkey + "<--->");
									log.info("<---Calling  ProceessPResponseFulFillmentOrder  method ----->");
									unfulfilledItems = ProceessPResponseFulFillmentOrder(tempList, fulfillDetailMap, omsCustOrdNo, tempkey);
									if (unfulfilledItems.size() > 0) {
										PResponseProcessingObj pResponseProcessingObj = null;
										try {
											pResponseProcessingObj = processPResponse(omsCustOrdNo, unfulfilledItems, fulfilOrdCfmCol, maxFulfilOrderNo, fulfillDetailMap, input);
										} catch (Exception e) {
											log.info("<---Error Occured while unfulfilledItems are processed--->");
										}
										log.info("pResponseProcessingObj.getNewFulfillMap()" + pResponseProcessingObj.getNewFulfillMap());
										newFulfillDetailMap = pResponseProcessingObj.getNewFulfillMap();
										log.info("newFulfillDetailMap.keySet()after calling processPresponse " + newFulfillDetailMap.keySet());
										fulfillDetailMap.putAll(newFulfillDetailMap);
										tempMap1.putAll(fulfillDetailMap);
										maxFulfilOrderNo = tempMap1.lastKey().intValue();
									} // end of unfulfilledItems size
								} // end of if condition..........
							} // end of if to handle multiple P responses for a single FulFillment order
							log.info("<---Calling findUnProcessed_X_MapObject Method----->");
							for (BigDecimal xKey : xfulfilOrdCfmCols.keySet()) {
								log.info("<--- Processing X map One to One xKey--->" + xKey + "<--->");
								tempList = findUnProcessed_X_MapObject(fulfillDetailMap);
								if (tempList != null) {
									log.info("<---Processing  un Processed  X Response temp list Object---->");
									try {
										xResponseProcessingObj = processXResponse(omsCustOrdNo, tempList, maxFulfilOrderNo, fulfillDetailMap, newFulfillDetailMap, input);
										fulfillDetailMap = xResponseProcessingObj.getFulfillDetailMap();
										tempMap1.putAll(fulfillDetailMap);
										maxFulfilOrderNo = tempMap1.lastKey().intValue();
										log.info("<---maxFulfilOrderNo--->" + maxFulfilOrderNo + "<----->");
										responseStatus = interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
									} catch (Exception e) {
										log.error("Failed in processing X response" + e);
										// code for updating status of head table
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
									}
								}
							} // end of for loop to handle multiple X response for a single FulFillment order.
						} catch (Exception e) {
							log.error("<-----Failed in processing X response----->" + e + "<---->");
							// code for updating status of head table
							// OMSCustomerOrderBean bean = new OMSCustomerOrderBean();
							try {
								NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
								nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
							} catch (Exception f) {
								log.error("Failed in cancellation while rollbacking with error " + f.getMessage());
							}
							rollbackWHandST(input.getCustomerOrderNo());
							throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
						}
					} else if (responseStatus.equals("P")) {
						log.info("<------Inside P response equals------------->");
						log.info("omsCustOrdNo " + omsCustOrdNo + "Received P response.");
						log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
						log.info("currentFulfilOrderNo loop " + currentFulFilOrderNo);
						log.info("fulfillDetailMap.keySet() " + fulfillDetailMap.keySet());
						List<FulfilOrdCfmDtl> fulfilOrdCfmDtlList = null;
						try {
							if (fulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty() && fulfilOrdCfmCol.getCollectionSize() == 0) {
								log.info("<----------fulfilOrdCfmCol variable is null -------------->");
								log.info("<---------------------Response code is C-----------------------------------> ");
							} else {
								fulfilOrdCfmDtlList = this.fulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getFulfilOrdCfmDtl();
							}
						} catch (Exception e) {
							log.info("-------->Error Occurred------->" + e.getMessage() + "------------->");
						}
						ArrayList<OmsTempCoFo> unfulfilledItems = new ArrayList<OmsTempCoFo>();
						Map<String, BigDecimal> tempMap = new HashMap<String, BigDecimal>();
						if (fulfilOrdCfmDtlList != null && fulfilOrdCfmDtlList.size() > 0) {
							for (FulfilOrdCfmDtl fulfilOrdCfmDtl : fulfilOrdCfmDtlList) {
								log.info("<---Item map contains-->" + fulfilOrdCfmDtl.getItem() + "----" + fulfilOrdCfmDtl.getConfirmQty());
								tempMap.put(fulfilOrdCfmDtl.getItem(), fulfilOrdCfmDtl.getConfirmQty());
							}
							for (BigDecimal mapkey : fulfillDetailMap.keySet()) {
								tempList = fulfillDetailMap.get(mapkey);
								for (OmsTempCoFo tempCoFo : tempList) {
									if (tempMap.containsKey(tempCoFo.getItem()) == true && tempCoFo.getRmsResponseCode() != null && tempCoFo.getRmsResponseCode().equals("P")) {
										log.info("<-----Key contains in tempmap----->");
										log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
										OmsTempCoFo unfulfilledTemp = new OmsTempCoFo();
										log.info("tempCoFo.getOrderQty() " + tempCoFo.getOrderQty());
										log.info("remaing Qty " + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
										if ((tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())).intValue() > 0)) {
											// maxFulfilOrderNo=maxFulfilOrderNo+1;
											log.info("maxFulfilOrderNo after adding " + maxFulfilOrderNo);
											unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
											unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
											unfulfilledTemp.setItem(tempCoFo.getItem());
											unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
											log.info("tempCoFo.getOrderQty() " + tempCoFo.getOrderQty());
											log.info("remaing Qty " + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
											unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
											unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
											unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
											unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
											unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
											unfulfilledTemp.setRmsResponseCode("P");
											log.info("unfulfilledTemp.getSourceLocId() " + unfulfilledTemp.getSourceLocId());
											log.info("unfulfilledTemp.getCombinationId()" + unfulfilledTemp.getCombinationId());
											log.info("unfulfilledTemp.getRmsResponseCode()" + unfulfilledTemp.getRmsResponseCode());
											log.info("unfulfilledTemp.getOrderQty() " + unfulfilledTemp.getOrderQty());
											tempCoFo.setOrderQty(tempMap.get(tempCoFo.getItem()));
											tempCoFo.setFoConfQty(tempMap.get(tempCoFo.getItem()));
											if (unfulfilledTemp.getItem().equals(tempCoFo.getItem())) {
												tempCoFo.setRmsResponseCode("C");
												log.info("tempCoFo.getRmsResponseCode()" + tempCoFo.getRmsResponseCode());
												log.info("tempCoFo.orderQty" + tempMap.get(tempCoFo.getItem()));
												log.info("tempCoFo.ConfQty " + tempMap.get(tempCoFo.getItem()));
											}
											unfulfilledItems.add(unfulfilledTemp);
										} else {
											tempCoFo.setRmsResponseCode("C");
											tempCoFo.setFoConfQty(tempCoFo.getOrderQty());
										}
									} else if (tempCoFo.getRmsResponseCode() != null && tempCoFo.getRmsResponseCode().equals("P")) {
										log.info("<----key doesnot contain in in tempmap--->");
										log.info("passing currentFulfilOrderNo in fullfilldetailMap " + currentFulFilOrderNo);
										tempList = fulfillDetailMap.get(new BigDecimal(currentFulFilOrderNo));
										log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
										log.info("temp.getOrderQty() " + tempCoFo.getOrderQty());
										log.info("temp.getRmsResponseCode() " + tempCoFo.getRmsResponseCode());
										if (tempCoFo.getOrderQty().intValue() > 0) {
											OmsTempCoFo unfulfilledTemp = new OmsTempCoFo();
											log.info("maxFulfilOrderNo after adding " + maxFulfilOrderNo);
											unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
											unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
											unfulfilledTemp.setItem(tempCoFo.getItem());
											unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
											unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty());
											unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
											unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
											unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
											unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
											unfulfilledTemp.setRmsResponseCode("P");
											log.info("unfulfilledTemp.getSourceLocId() " + unfulfilledTemp.getSourceLocId());
											log.info("unfulfilledTemp.getCombinationId()" + unfulfilledTemp.getCombinationId());
											log.info("unfulfilledTemp.getRmsResponseCode()" + unfulfilledTemp.getRmsResponseCode());
											log.info("unfulfilledTemp.getOrderQty() " + unfulfilledTemp.getOrderQty());
											tempCoFo.setOrderQty(tempCoFo.getOrderQty());
											tempCoFo.setFoConfQty(new BigDecimal(0));
											if (unfulfilledTemp.getItem().equals(tempCoFo.getItem())) {
												tempCoFo.setRmsResponseCode("C");
												log.info("temp.getRmsResponseCode()" + tempCoFo.getRmsResponseCode());
											}
											unfulfilledItems.add(unfulfilledTemp);
										} else {
											tempCoFo.setRmsResponseCode("C");
											tempCoFo.setFoConfQty(new BigDecimal(0));
										}
									}
								}
							} // end of if loop iterarting fulfillDetail map
						} // end of if condition...
						log.info("<-------unfulfilledItems----->" + unfulfilledItems.size() + "<------>");
						PResponseProcessingObj pResponseProcessingObj = null;
						if (unfulfilledItems.size() > 0) {
							pResponseProcessingObj = processPResponse(omsCustOrdNo, unfulfilledItems, fulfilOrdCfmCol, maxFulfilOrderNo, fulfillDetailMap, input);
							newFulfillDetailMap = pResponseProcessingObj.getNewFulfillMap();
							log.info("newFulfillDetailMap.keySet() after calling processPresponse " + newFulfillDetailMap.keySet());
							fulfillDetailMap.putAll(newFulfillDetailMap);
							tempMap1.putAll(fulfillDetailMap);
							maxFulfilOrderNo = tempMap1.lastKey().intValue();
							log.info("maxFulfilOrderNo after calling P response " + maxFulfilOrderNo);
							log.info("After calling the P response method the  fulfillDetailMap is " + fulfillDetailMap.keySet());
							// code for P/X, process nested P response, one by one , one fulfillOrderNo will
							// be called once and it will contain the single item
							log.info("<----------- Finding in a map templist which are un processed-------->");
							for (BigDecimal pkey : pfulfilOrdCfmCols.keySet()) {
								log.info("<---- Process P map One by One-----pkey-------------->" + pkey + "<----->");
								tempList = findUnProcessed_P_MapObject(fulfillDetailMap);
								if (tempList != null) {
									// tempList contains an Response Code 'P'
									BigDecimal tempkey = getUnProceesedPResponsekey(fulfillDetailMap);
									log.info("<--->The value of temp list is-->" + key + "<--->");
									log.info("Calling  ProceessPResponseFulFillmentOrder  method --------------------->");
									unfulfilledItems = ProceessPResponseFulFillmentOrder(tempList, fulfillDetailMap, omsCustOrdNo, tempkey);
									log.info("<------------unfulfilledItems  are --------------------------------------------->");
									if (unfulfilledItems.size() > 0) {
										try {
											pResponseProcessingObj = processPResponse(omsCustOrdNo, unfulfilledItems, fulfilOrdCfmCol, maxFulfilOrderNo, fulfillDetailMap, input);
										} catch (Exception e) {
											log.info(" ********************Error Occured while processing unfulfilledItems **********  ");
										}
										log.info("pResponseProcessingObj.getNewFulfillMap()" + pResponseProcessingObj.getNewFulfillMap());
										newFulfillDetailMap = pResponseProcessingObj.getNewFulfillMap();
										log.info("newFulfillDetailMap.keySet() after calling processPresponse " + newFulfillDetailMap.keySet());
										fulfillDetailMap.putAll(newFulfillDetailMap);
										tempMap1.putAll(fulfillDetailMap);
										maxFulfilOrderNo = tempMap1.lastKey().intValue();
									}
								} // end of P Response.
								log.info("<------------EndProcess P Map----------------------------------------->");
							} // processed all P response For a single FulFillment order No
								// Process nested X response for a single fulfilment order
							log.info("<-------------Calling findUnProcessed_X_MapObject method --------------->");
							for (BigDecimal xkey : xfulfilOrdCfmCols.keySet()) {
								log.info("<----Processing X map One By One---xkey ------->" + xkey + "<----->");
								tempList = findUnProcessed_X_MapObject(fulfillDetailMap);
								if (tempList != null) {
									try {
										log.info("<------omsCustOrdNo-------->" + omsCustOrdNo + "<----------------->");
										XResponseProcessingObj xResponseProcessingObj = processXResponse(omsCustOrdNo, tempList, maxFulfilOrderNo, fulfillDetailMap, newFulfillDetailMap, input);
										fulfillDetailMap = xResponseProcessingObj.getFulfillDetailMap();
										tempMap1.putAll(fulfillDetailMap);
										maxFulfilOrderNo = tempMap1.lastKey().intValue();
									} catch (Exception e) {
										throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM_ERROR"));
									}
								}
								log.info("<----------------------------------------------------------------------->");
							} // multiple X Response are handled under single FulFillment order processed.
						} // .........end of If part when unfulfilledItems size is greater than zero....
						else {
							break;
						}
					}
				} // while loop end
				for (BigDecimal tempKey : fulfillDetailMap.keySet()) {
					tempList = fulfillDetailMap.get(tempKey);
					for (OmsTempCoFo omsTemp : tempList) {
						if (omsTemp.getRmsResponseCode() != null && !"".equals(omsTemp.getRmsResponseCode())) {
							log.info("tempKey " + tempKey);
							log.info("omsCustOrdNo " + omsCustOrdNo + "tempList.size() inside newFulfillDetailMap " + tempList.size());
							log.info("omsCustOrdNo " + omsCustOrdNo + "Fulfill order No in tempList loop" + omsTemp.getFulfillOrderNo());
							log.info("omsCustOrdNo " + omsCustOrdNo + "omsCustOrdNo " + omsTemp.getOmsCustOrdNo());
							log.info("omsCustOrdNo " + omsCustOrdNo + " lineNo= " + omsTemp.getLineNo());
							log.info("omsCustOrdNo " + omsCustOrdNo + " item= " + omsTemp.getItem());
							log.info("omsCustOrdNo " + omsCustOrdNo + " Quantity Ordered= " + omsTemp.getOrderQty());
							log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfilmetn OrderNo=" + omsTemp.getFulfillOrderNo());
							log.info("omsCustOrdNo " + omsCustOrdNo + " source loc id= " + omsTemp.getSourceLocId());
							log.info("omsCustOrdNo " + omsCustOrdNo + " Fulfill loc id= " + omsTemp.getFulfillLocId());
							log.info("omsCustOrdNo " + omsCustOrdNo + " Quantity Confirmed= " + omsTemp.getFoConfQty());
							log.info("omsCustOrdNo " + omsCustOrdNo + " RmsResponseCode=" + omsTemp.getRmsResponseCode());
							responseStatus = omsTemp.getRmsResponseCode();
						}
					}
				}
			} // for loop end
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			String extCustOrdNo = omsCustOrdHead.getCustOrderNo();
			if (omsCustOrdHead.getSubCustOrderNo().equals("1")) {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo();
			} else {
				extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + omsCustOrdHead.getSubCustOrderNo();
				if (omsCustOrdHead.getSubCustOrderNo().trim().length() < 3) {
					if (omsCustOrdHead.getSubCustOrderNo().trim().length() == 2) {
						extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "-0" + omsCustOrdHead.getSubCustOrderNo().trim();
					} else {
						extCustOrdNo = omsCustOrdHead.getCustOrderNo().trim() + "-00" + omsCustOrdHead.getSubCustOrderNo().trim();
					}
				}
			}
			OmsCoFulfillDetail omsCoFulfillDetail = null;
			log.info("omsCustOrdNo " + omsCustOrdNo + "extCustOrdNo=" + extCustOrdNo);
			for (BigDecimal key : fulfillDetailMap.keySet()) {
				tempList = fulfillDetailMap.get(key);
				for (OmsTempCoFo omsTemp : tempList) {
					omsCoFulfillDetail = new OmsCoFulfillDetail();
					if (input.getOrderType().equals("SAS") == false && omsTemp.getOrderQty().intValue() > 0 && omsTemp.getFoConfQty().intValue() > 0) {
						omsCoFulfillDetail.setFulfillOrderNo(omsTemp.getFulfillOrderNo());
						omsCoFulfillDetail.setFulfillLocType(omsTemp.getFulfillLocationType());
						omsCoFulfillDetail.setFulfillLoc(omsTemp.getFulfillLocId());
						omsCoFulfillDetail.setLineNo(omsTemp.getLineNo());
						omsCoFulfillDetail.setItem(omsTemp.getItem());
						omsCoFulfillDetail.setOmsCustOrdNo(omsCustOrdNo);
						omsCoFulfillDetail.setSourceLoc(omsTemp.getSourceLocId());
						omsCoFulfillDetail.setSourceLocType(omsTemp.getSourceLocationType());
						omsCoFulfillDetail.setFulfillReqQty(omsTemp.getOrderQty());
						omsCoFulfillDetail.setCombinationId(omsTemp.getCombinationId());
						omsCoFulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
						omsCoFulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
						omsCoFulfillDetail.setFulfillConfQty(omsTemp.getFoConfQty());
						omsCoFulfillDetail.setFulfillStatus("C");
						omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
						try {
							BigDecimal tsfNo = null;
							log.info("Getting Transfer No for Reservation");
							log.info("extCustOrdNo " + extCustOrdNo);
							log.info("omsCoFulfillDetail.getFulfillOrderNo() " + omsCoFulfillDetail.getFulfillOrderNo());
							log.info("omsCoFulfillDetail.getSourceLoc() " + omsCoFulfillDetail.getSourceLoc());
							log.info("omsCoFulfillDetail.getFulfillLoc() " + omsCoFulfillDetail.getFulfillLoc());
							tsfNo = session
									.getOrdcustFindByFulfilOrdNo(extCustOrdNo, omsCoFulfillDetail.getFulfillOrderNo().toString(), omsCoFulfillDetail.getSourceLoc(), omsCoFulfillDetail.getFulfillLoc())
									.get(0).getTsfNo();
							if (tsfNo != null && "ST".equals(omsTemp.getSourceLocationType())) {
								log.info("tsfNo " + tsfNo);
								omsCoFulfillDetail.setTsfNo(tsfNo);
								try {
									String status = interfacePersistence.approveTransfers(tsfNo, omsTemp.getSourceLocId());
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
												omsUnapprovedTransfers.setTsfNo(tsfNo);
												omsUnapprovedTransfers.setItem(omsTemp.getItem());
												omsUnapprovedTransfers.setLocation(omsTemp.getSourceLocId());
												omsUnapprovedTransfers.setUnapprovedQty((omsTemp.getOrderQty()));
												omsUnapprovedTransfers.setOmsCustOrdNo(omsCustOrdNo);
												omsUnapprovedTransfers.setCreateDatetime(new Timestamp(new Date().getTime()));
												session.persistOmsUnapprovedTransfers(omsUnapprovedTransfers);
												log.info("omsCustOrdNo" + omsCustOrdNo + "Persisting into OmsUnapprovedTransfers for item =" + omsTemp.getItem() + " at location "
														+ omsTemp.getSourceLocId() + " with qty=" + omsUnapprovedTransfers.getUnapprovedQty());
											}
										}
									}
								} catch (Exception e) {
									log.error("Failed in approving transfer");
								}
							} else {
								log.info("----------------WareHouse Transfer-------------------------------");
								omsCoFulfillDetail.setTsfNo(tsfNo);
								omsCoFulfillDetail.setTsfApprovalStatus("A");
							}
						} catch (Exception e) {
							log.error("---------");
						}
						Date d1 = new Date();
						session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
						Date d2 = new Date();
						log.info("omsCustOrdNo" + omsCustOrdNo + "after persisting into omsCoFulfillDetail" + (d2.getTime() - d1.getTime()) + " in milliseconds");
					}
				}
				log.info("omsCustOrdNo " + omsCustOrdNo + "Persisting success in oms_co_fulfill_detail");
			}
			// delete the approved transfer from unapproved transfer table
			if (transferList != null && transferList.size() > 0) {
				oMSUtilCommons.deleteApprovedTsffromUnapprovedTsfTable(transferList, omsCustOrdNo);
			}
			log.info("omsCustOrdNo " + omsCustOrdNo + "***End : processNonSadad");
			tempMap1.putAll(fulfillDetailMap);
		}
	}

	public XResponseProcessingObj processXResponse(BigDecimal omsCustOrdNo, ArrayList<OmsTempCoFo> tempList, int maxFulfilOrderNo,
			ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap, TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> newFulfillMap, CustomerOrder input)
			throws EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			SOAPException {
		log.info("<-----------------Inside processXResponse method---------------------------->");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		XResponseProcessingObj xResponseProcessingObj = new XResponseProcessingObj();
		ProcessedObject processedObject = null;
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		String responseStatus = "";
		BigDecimal combinationId = null;
		Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		ReturnPartialResponseObject returnPartialResponse = new ReturnPartialResponseObject();
		BigDecimal virtualWH = BigDecimal.ZERO;
		try {
			log.info("tempList.size() for Xresponse " + tempList.size());
			String storeMapValue = null;
			String srcLocfulfiLoc = null;
			TreeMap<BigDecimal, String> storeFulFillMap = new TreeMap<BigDecimal, String>();
			for (BigDecimal mapkey : fulfillDetailMap.keySet()) {
				tempList = fulfillDetailMap.get(mapkey);
				log.info("mapkey for X response for fulfillDetailMap.keySet() " + mapkey);
				for (OmsTempCoFo omsTempCoFo : tempList) {
					log.info("tempList.size() " + tempList.size());
					log.info("checking whether the omsTempCoFo.getRmsResponseCode() is equal to X ");
					log.info("omsTempCoFo.getRmsResponseCode() " + omsTempCoFo.getRmsResponseCode());
					if (omsTempCoFo.getRmsResponseCode() != null && omsTempCoFo.getRmsResponseCode().equals("X")) {
						log.info("tempCoFo.getLineNo()" + omsTempCoFo.getLineNo());
						log.info("tempCoFo.getItem() " + omsTempCoFo.getItem());
						log.info("tempCoFo.getFulfillOrderNo() " + omsTempCoFo.getFulfillOrderNo());
						log.info("tempCoFo.getRmsResponseCode() " + omsTempCoFo.getRmsResponseCode());
						log.info("tempCoFo.getOrderQty() " + omsTempCoFo.getOrderQty());
						log.info("tempCoFo.getSourceLocId() " + omsTempCoFo.getSourceLocId());
						log.info("tempCoFo.getSourceLocationType() " + omsTempCoFo.getSourceLocationType());
						log.info("tempCoFo.getFulfillLocId() " + omsTempCoFo.getFulfillLocId());
						log.info("tempCoFo.getFulfillLocationType() " + omsTempCoFo.getFulfillLocationType());
						log.info("omsTempCoFo.getCombinationId() " + omsTempCoFo.getCombinationId());
						String item = omsTempCoFo.getItem();
						BigDecimal lineNo = omsTempCoFo.getLineNo();
						long SOH = 0;
						long pendingQty = 0;
						long availQty = 0;
						long orderQty = 0;
						log.info("item " + item);
						log.info("lineNo " + lineNo);
						log.info("omsCustOrdNo " + omsCustOrdNo);
						orderQty = omsTempCoFo.getOrderQty().intValue();
						PartialResponse PartialResponse = new PartialResponse();
						OmsFulfillMatrixExtDetail matrixDetail = null;
						combinationId = omsTempCoFo.getCombinationId();
						log.info("combinationId " + combinationId);
						log.info("omsTempCoFo.getSourceLocId() " + omsTempCoFo.getSourceLocId());
						log.info("omsTempCoFo.getVirtualWH() " + omsTempCoFo.getVirtualWH());
						log.info("omsTempCoFo.getSourceLocationType()" + omsTempCoFo.getSourceLocationType());
						if (omsTempCoFo.getSourceLocationType().equals("WH")) {
							virtualWH = omsTempCoFo.getVirtualWH();
							matrixDetail = session.getOmsFulfillMatrixExtDetailFindPriority(combinationId, virtualWH);
						} else {
							matrixDetail = session.getOmsFulfillMatrixExtDetailFindPriority(combinationId, omsTempCoFo.getSourceLocId());
						}
						FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
						int priority = matrixDetail.getPriority().add(BigDecimal.ONE).intValue();
						log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
						log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());
						while (orderQty > 0) {
							log.info("priority " + priority);
							log.info("omsCustOrdNo " + omsCustOrdNo + "Finding next location with combination id " + combinationId + "and priority " + (priority));
							matrixDetail = findNextfulfillLoc.processFulfillmentMatrix(combinationId, priority);
							BigDecimal sourceLocId = matrixDetail.getLocation();
							BigDecimal fulfillLocId = matrixDetail.getDeliveryFromLoc();
							String srcLocType = matrixDetail.getLocationType();
							String fulFillLocType = matrixDetail.getDeliveryFromLocType();
							if (matrixDetail.getLocationType().equals("ST")) {
								// Find SOH from SIM
								log.info("omsCustOrdNo " + omsCustOrdNo + "Calling SIM webservice for finding SOH for item =" + omsTempCoFo.getItem() + " in store=" + sourceLocId);
								try {
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									SOH = interfacePersistence.callSIMStoreInventory(item, sourceLocId, omsCustOrdHead.getApplicationId());
									returnPartialResponse = PartialResponse.STLocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo,
											combinationId, fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty(), srcLocfulfiLoc, storeMapValue, storeFulFillMap);
									orderQty = returnPartialResponse.getOrderQty();
									maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
									sohMap = returnPartialResponse.getSohMap();
									pendingQty = returnPartialResponse.getPendingQty();
									omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
									storeFulFillMap = returnPartialResponse.getStoreFulFillMap();
									log.info("=========returnPartialResponse objects for ST=================");
									log.info("orderQty " + orderQty);
									log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
									log.info("pendingQty " + pendingQty);
									log.info("sohMap " + sohMap.keySet());
									log.info("storeFulFillMap " + storeFulFillMap.keySet());
									log.info("=========returnPartialResponse objects for ST=================");
								} catch (Exception e) {
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
								}
							}
							if (matrixDetail.getLocationType().equals("WH")) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "inside WH ");
								log.info("omsCustOrdNo " + omsCustOrdNo + "Started the code for Virtual Warehouse scenario");
								List<Object[]> tempWhObject = session.getWhFindPhysicalWH(matrixDetail.getLocation());
								BigDecimal physicalWH = BigDecimal.ZERO;
								BigDecimal channelId = BigDecimal.ZERO;
								for (Object[] result : tempWhObject) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "inside loop");
									log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0]);
									physicalWH = new BigDecimal(result[0].toString());
									channelId = new BigDecimal(result[1].toString());
									log.info("omsCustOrdNo " + omsCustOrdNo + "channel id=" + result[1]);
								}
								sourceLocId = physicalWH;
								log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId " + sourceLocId);
								fulfillLocId = matrixDetail.getDeliveryFromLoc();
								virtualWH = matrixDetail.getLocation();
								log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH " + fulfillLocId);
								fulFillLocType = matrixDetail.getDeliveryFromLocType();
								log.info("omsCustOrdNo " + omsCustOrdNo + "fulFillLocType " + fulFillLocType);
								log.info("omsCustOrdNo " + omsCustOrdNo + " physicalWH " + physicalWH);
								log.info("omsCustOrdNo " + omsCustOrdNo + " channelId " + channelId);
								List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);
								int i = 0;
								while (i < locList.size()) {
									log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
									i++;
								}
								OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
								// added a code to make Thread sleep to handle sync between and RMS and DAS
								// schema.
								try {
									Thread.sleep(5000);
								} catch (InterruptedException ie) {
									log.info("<---- Error occured in making current threaad sleep------>");
								}
								String applicationId = "E-COMMERCE";
								SOH = omsUtilCommons.checkSOHForWH(omsTempCoFo.getItem(), locList, applicationId).longValue();
								returnPartialResponse = PartialResponse.WHLocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo,
										combinationId, fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty());
								orderQty = returnPartialResponse.getOrderQty();
								maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
								sohMap = returnPartialResponse.getSohMap();
								pendingQty = returnPartialResponse.getPendingQty();
								omsTempCoFo.setOrderQty(new BigDecimal(pendingQty)); // removed comment as a part of P/X Response
								log.info("=========returnPartialResponse objects for WH=================");
								log.info("orderQty " + orderQty);
								log.info("pendingQty " + pendingQty);
								log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
								log.info("sohMap " + sohMap.keySet());
								log.info("=========returnPartialResponse objects for WH=================");
							}
							if (matrixDetail.getLocationType().equals("SU")) {
								CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
								String item_status = checkItemLocSOH.findItemStatus(omsTempCoFo.getItem(), fulfillLocId);
								if (item_status.equals("A") == false) {
									log.info("omsCustOrdNo " + omsCustOrdNo + " inside item_status false condition");
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									omsCustOrdHead.setStatus("F");
									session.mergeOmsCustOrdHead(omsCustOrdHead);
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
									// break;
								}
								// sourceLocId = new
								// BigDecimal(checkItemLocSOH.findSupplier(omsTempCoFo.getItem(), "Y"));
								log.info("Delivery Location type" + matrixDetail.getDeliveryFromLocType());
								if ("S".equals(matrixDetail.getDeliveryFromLocType())) {
									log.info(" Calling getPrimarySupplierFromItemLocation method by passing item" + omsTempCoFo.getItem() + "location" + matrixDetail.getDeliveryFromLoc().longValue());
									sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(), matrixDetail.getDeliveryFromLoc().longValue());
								} else {
									log.info(" Calling getPrimarySupplierFromItemLocation method by passing item" + omsTempCoFo.getItem() + "location" + input.getOrderRequestorId());
									sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(), input.getOrderRequestorId());
								}
								log.info(" calling checkDirectShipIndicatoryofaGivenSupplier by passing item =" + omsTempCoFo.getItem() + "and supplier is" + sourceLocId.longValue());
								Boolean flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(omsTempCoFo.getItem(), sourceLocId.longValue(), input.getOrderRequestorId());
								log.info("Value of flag is" + flag);
								log.info("sourceLocId " + sourceLocId);
								log.info("omsCustOrdNo " + omsCustOrdNo + "supplier value from findSupplier method " + sourceLocId);
								if (flag == Boolean.FALSE) {
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									omsCustOrdHead.setStatus("F");
									session.mergeOmsCustOrdHead(omsCustOrdHead);
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SRC_LOC_ID==0"));
								}
								if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
									log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
									OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
									omsCustOrdHead.setStatus("F");
									session.mergeOmsCustOrdHead(omsCustOrdHead);
									throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
								}
								SOH = orderQty;
								log.info("========SOH========= in supplier " + SOH);
								availQty = SOH;
								returnPartialResponse = PartialResponse.SULocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo,
										combinationId, fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty(), srcLocfulfiLoc, storeMapValue, storeFulFillMap);
								orderQty = returnPartialResponse.getOrderQty();
								maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
								sohMap = returnPartialResponse.getSohMap();
								pendingQty = returnPartialResponse.getPendingQty();
								omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
								log.info("=========returnPartialResponse objects for SU=================");
								log.info("orderQty " + orderQty);
								log.info("pendingQty " + pendingQty);
								log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
								log.info("sohMap " + sohMap.keySet());
								log.info("=========returnPartialResponse objects for SU=================");
							}
							SourceLocIdentify sourceLocIdentify = new SourceLocIdentify();
							newFulfillMap = sourceLocIdentify.createFulfillDetailMap(sohMap, maxFulfilOrderNo);
							log.info("newFulfillMap.keySet() " + newFulfillMap.keySet());
							if (orderQty == 0) {
								log.info("Removing FulfillOrderNo from Map" + omsTempCoFo.getFulfillOrderNo());
								fulfillDetailMap.remove(omsTempCoFo.getFulfillOrderNo());
								break;
							}
							priority++;
						}
					} else {
						log.info("<-----------Doesnot contain X--------------------->");
					}
				}
			}
			log.info("newFulfillMap " + newFulfillMap.keySet());
			for (BigDecimal key : newFulfillMap.keySet()) {
				log.info("newFulfillMap.keySet() " + newFulfillMap.keySet());
				log.info("key is " + key);
				tempList = newFulfillMap.get(key);
				log.info("tempList.size()" + tempList.size());
				log.info("omsCustOrdNo " + omsCustOrdNo + "after getting X response and checking for next location calling the callWebservices method");
				try {
					log.info("tempList.get(0).getRmsResponseCode() " + tempList.get(0).getRmsResponseCode());
					if (tempList.get(0).getRmsResponseCode() == null || tempList.get(0).getRmsResponseCode().isEmpty()) {
						log.info("In class non saddad --- callwebservice method in line 910");
						processedObject = interfacePersistence.callWebservices(omsCustOrdNo, tempList);
						fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
						currentFulFilOrderNo = processedObject.getCurrentFulFilOrderNo();
						log.info("currentFulfilOrderNo inside xresponse after calling RMS " + currentFulFilOrderNo);
						log.info("omsCustOrdNo " + omsCustOrdNo + "+++++++++++++++++ fulfilOrdCfmCol " + fulfilOrdCfmCol.getCollectionSize());
						tempList = processedObject.getOmsTempCoFoList();
						newFulfillMap.put(key, tempList);
						log.info("omsCustOrdNo " + omsCustOrdNo + "After getting from XResponseProcessingObj fulfilOrdCfmCol " + fulfilOrdCfmCol.getCollectionSize());
						xResponseProcessingObj.setFulfilOrdCfmCol(fulfilOrdCfmCol);
						responseStatus = interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
						log.info("responseStatus " + responseStatus);
						if ("P".equals(responseStatus)) {
							pfulfilOrdCfmCols.put(key, fulfilOrdCfmCol);
						} else if ("X".equals(responseStatus)) {
							xfulfilOrdCfmCols.put(key, tempList);
						}
						fulfillDetailMap.put(key, tempList);
						xResponseProcessingObj.setFulfillDetailMap(fulfillDetailMap);
					}
				} catch (Exception e) {
					log.info("exception " + e.getMessage());
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Rollbacking");
					try {
						NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
						nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
					} catch (Exception ex) {
						log.info("Exception occurred during rollback");
					}
					rollbackWHandST(input.getCustomerOrderNo());
					log.info("omsCustOrdNo " + omsCustOrdNo + "throwing RMS_UNAVL");
				}
			}
		} catch (Exception e) {
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			/*
			 * OMSCustomerOrderBean oMSCustomerOrderBean = new OMSCustomerOrderBean();
			 * oMSCustomerOrderBean.rollback(fulfillDetailMap);
			 */
			try {
				NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
				nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
			} catch (Exception ex) {
				log.warn("Exception Occurred during roll back", ex);
			}
			rollbackWHandST(input.getCustomerOrderNo());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM ERROR"));
		}
		return xResponseProcessingObj;
	}

	public PResponseProcessingObj processPResponse(BigDecimal omsCustOrdNo, ArrayList<OmsTempCoFo> tempList, FulfilOrdCfmCol fulfilOrdCfmCol, int maxFulfilOrderNo,
			ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap, CustomerOrder input)
			throws EntityAlreadyExistsWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			SOAPException {
		log.info("<-------------------- Inside processPResponse method------------------>");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		BigDecimal combinationId = null;
		ProcessedObject processedObject = null;
		String responseStatus = "";
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		TreeMap<BigDecimal, ArrayList<OmsTempCoFo>> newFulfillMap = null;
		Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap = new HashMap<BigDecimal, ArrayList<OmsTempCoFo>>();
		PResponseProcessingObj pResponseProcessingObj = new PResponseProcessingObj();
		ReturnPartialResponseObject returnPartialResponse = new ReturnPartialResponseObject();
		BigDecimal virtualWH = BigDecimal.ZERO;
		try {
			log.info("tempList.size() for presponse" + tempList.size());
			String storeMapValue = null;
			String srcLocfulfiLoc = null;
			TreeMap<BigDecimal, String> storeFulFillMap = new TreeMap<BigDecimal, String>();
			for (OmsTempCoFo omsTempCoFo : tempList) {
				OmsFulfillMatrixExtDetail matrixDetail = null;
				combinationId = omsTempCoFo.getCombinationId();
				log.info("combinationId " + combinationId);
				log.info("omsTempCoFo.getSourceLocId() " + omsTempCoFo.getSourceLocId());
				log.info("omsTempCoFo.getVirtualWH() " + omsTempCoFo.getVirtualWH());
				log.info("omsTempCoFo.getSourceLocationType()" + omsTempCoFo.getSourceLocationType());
				if (omsTempCoFo.getSourceLocationType().equals("WH")) {
					virtualWH = omsTempCoFo.getVirtualWH();
					matrixDetail = session.getOmsFulfillMatrixExtDetailFindPriority(combinationId, virtualWH);
				} else {
					matrixDetail = session.getOmsFulfillMatrixExtDetailFindPriority(combinationId, omsTempCoFo.getSourceLocId());
				}
				FindNextfulfillLoc findNextfulfillLoc = new FindNextfulfillLoc();
				int priority = matrixDetail.getPriority().add(BigDecimal.ONE).intValue();
				log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
				log.info("omsTempCoFo.getItem() " + omsTempCoFo.getItem());
				log.info("omsTempCoFo.getRmsResponseCode() " + omsTempCoFo.getRmsResponseCode());
				long orderQty = omsTempCoFo.getOrderQty().intValue();
				if (omsTempCoFo.getRmsResponseCode().equals("P")) {
					while (orderQty > 0) {
						log.info("orderQty " + orderQty);
						log.info("priority " + priority);
						log.info("omsCustOrdNo " + omsCustOrdNo + "Finding next location with combination id " + combinationId + "and priority " + (priority));
						matrixDetail = findNextfulfillLoc.processFulfillmentMatrix(combinationId, priority);
						BigDecimal sourceLocId = matrixDetail.getLocation();
						BigDecimal fulfillLocId = matrixDetail.getDeliveryFromLoc();
						String srcLocType = matrixDetail.getLocationType();
						String fulFillLocType = matrixDetail.getDeliveryFromLocType();
						String item = omsTempCoFo.getItem();
						BigDecimal lineNo = omsTempCoFo.getLineNo();
						long SOH = 0;
						long pendingQty = 0;
						long availQty = 0;
						log.info("item " + item);
						log.info("lineNo " + lineNo);
						log.info("omsCustOrdNo " + omsCustOrdNo);
						PartialResponse PartialResponse = new PartialResponse();
						InterfacePersistence interfacePersistece = new InterfacePersistence();
						if (matrixDetail.getLocationType().equals("ST")) {
							// Find SOH from SIM
							log.info("omsCustOrdNo " + omsCustOrdNo + "Calling SIM webservice for finding SOH for item =" + omsTempCoFo.getItem() + " in store=" + sourceLocId);
							try {
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								SOH = interfacePersistece.callSIMStoreInventory(item, sourceLocId, omsCustOrdHead.getApplicationId());
								returnPartialResponse = PartialResponse.STLocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo,
										combinationId, fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty(), srcLocfulfiLoc, storeMapValue, storeFulFillMap);
								orderQty = returnPartialResponse.getOrderQty();
								maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
								sohMap = returnPartialResponse.getSohMap();
								pendingQty = returnPartialResponse.getPendingQty();
								omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
								storeFulFillMap = returnPartialResponse.getStoreFulFillMap();
								log.info("=========returnPartialResponse objects for ST=================");
								log.info("orderQty " + orderQty);
								log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
								log.info("pendingQty " + pendingQty);
								log.info("sohMap " + sohMap.keySet());
								log.info("storeFulFillMap " + storeFulFillMap.keySet());
								log.info("=========returnPartialResponse objects for ST=================");
							} catch (Exception e) {
								throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("UNAVL_INV"));
							}
						}
						if (matrixDetail.getLocationType().equals("WH")) {
							log.info("omsCustOrdNo " + omsCustOrdNo + "inside WH ");
							log.info("omsCustOrdNo " + omsCustOrdNo + "Started the code for Virtual Warehouse scenario");
							List<Object[]> tempWhObject = session.getWhFindPhysicalWH(matrixDetail.getLocation());
							BigDecimal physicalWH = BigDecimal.ZERO;
							BigDecimal channelId = BigDecimal.ZERO;
							for (Object[] result : tempWhObject) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "inside loop");
								log.info("omsCustOrdNo " + omsCustOrdNo + "WH=" + result[0]);
								physicalWH = new BigDecimal(result[0].toString());
								channelId = new BigDecimal(result[1].toString());
								log.info("omsCustOrdNo " + omsCustOrdNo + "channel id=" + result[1]);
							}
							sourceLocId = physicalWH;
							log.info("omsCustOrdNo " + omsCustOrdNo + "sourceLocId " + sourceLocId);
							fulfillLocId = matrixDetail.getDeliveryFromLoc();
							virtualWH = matrixDetail.getLocation();
							log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillLocId for WH " + fulfillLocId);
							fulFillLocType = matrixDetail.getDeliveryFromLocType();
							log.info("omsCustOrdNo " + omsCustOrdNo + "fulFillLocType " + fulFillLocType);
							log.info("omsCustOrdNo " + omsCustOrdNo + " physicalWH " + physicalWH);
							log.info("omsCustOrdNo " + omsCustOrdNo + " channelId " + channelId);
							List<BigDecimal> locList = session.getWhFindVirtualWh(physicalWH, channelId);
							int i = 0;
							while (i < locList.size()) {
								log.info("omsCustOrdNo " + omsCustOrdNo + locList.get(i));
								i++;
							}
							// added code to sync rms das schema for ware house
							OMSUtilCommons omsUtilCommons = new OMSUtilCommons();
							try {
								Thread.sleep(5000);
							} catch (InterruptedException ie) {
								log.info("<---Error Occured while making current Thread sleep----->");
							}
							String applicationId = "E-COMMERCE";
							SOH = omsUtilCommons.checkSOHForWH(omsTempCoFo.getItem(), locList, applicationId).longValue();
							returnPartialResponse = PartialResponse.WHLocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo, combinationId,
									fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty());
							orderQty = returnPartialResponse.getOrderQty();
							maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
							sohMap = returnPartialResponse.getSohMap();
							pendingQty = returnPartialResponse.getPendingQty();
							omsTempCoFo.setOrderQty(new BigDecimal(pendingQty)); // removed comment as a part of P/X
							log.info("=========returnPartialResponse objects for WH=================");
							log.info("orderQty " + orderQty);
							log.info("pendingQty " + pendingQty);
							log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
							log.info("sohMap " + sohMap.keySet());
							log.info("=========returnPartialResponse objects for WH=================");
						}
						if (matrixDetail.getLocationType().equals("SU")) {
							log.info("orderQty in supplier " + orderQty);
							CheckItemLocSOH checkItemLocSOH = new CheckItemLocSOH();
							String item_status = checkItemLocSOH.findItemStatus(omsTempCoFo.getItem(), fulfillLocId);
							if (item_status.equals("A") == false) {
								log.info("omsCustOrdNo " + omsCustOrdNo + " inside item_status false condition");
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ITM_NOT_APP"));
								// break;
							}
							// sourceLocId = new
							// BigDecimal(checkItemLocSOH.findSupplier(omsTempCoFo.getItem(), "Y"));
							log.info("Delivery Location type" + matrixDetail.getDeliveryFromLocType());
							if ("S".equals(matrixDetail.getDeliveryFromLoc())) {
								log.info(" Calling getPrimarySupplierFromItemLocation method by passing item" + omsTempCoFo.getItem() + "location" + matrixDetail.getDeliveryFromLoc().longValue());
								sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(), matrixDetail.getDeliveryFromLoc().longValue());
							} else {
								log.info(" Calling getPrimarySupplierFromItemLocation method by passing item" + omsTempCoFo.getItem() + "location" + input.getOrderRequestorId());
								sourceLocId = checkItemLocSOH.getPrimarySupplierFromItemLocation(omsTempCoFo.getItem(), input.getOrderRequestorId());
							}
							log.info(" calling checkDirectShipIndicatoryofaGivenSupplier by passing item =" + omsTempCoFo.getItem() + "and supplier is" + sourceLocId.longValue());
							Boolean flag = checkItemLocSOH.checkDirectShipIndicatoryofaGivenSupplier(omsTempCoFo.getItem(), sourceLocId.longValue(), input.getOrderRequestorId());
							log.info("Value of flag is" + flag);
							log.info("sourceLocId " + sourceLocId);
							log.info("omsCustOrdNo " + omsCustOrdNo + "supplier value from findSupplier method " + sourceLocId);
							if (flag == Boolean.FALSE) {
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SRC_LOC_ID==0"));
							}
							if (session.getStoreFindOrgUnit(fulfillLocId).compareTo(session.getPartnerOrgUnitFindOrgUnitId(sourceLocId)) != 0) {
								log.info("omsCustOrdNo " + omsCustOrdNo + "Org unit not matched");
								OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
								omsCustOrdHead.setStatus("F");
								session.mergeOmsCustOrdHead(omsCustOrdHead);
								throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("ORG_UNIT_UNMATCHED"));
							}
							SOH = orderQty;
							log.info("========SOH========= in supplier " + SOH);
							availQty = SOH;
							returnPartialResponse = PartialResponse.SULocation(item, sourceLocId, availQty, pendingQty, omsCustOrdNo, SOH, orderQty, maxFulfilOrderNo, sohMap, lineNo, combinationId,
									fulFillLocType, srcLocType, fulfillLocId, virtualWH, omsTempCoFo.getOrderQty(), srcLocfulfiLoc, storeMapValue, storeFulFillMap);
							orderQty = returnPartialResponse.getOrderQty();
							maxFulfilOrderNo = returnPartialResponse.getMaxFulfilOrderNo();
							sohMap = returnPartialResponse.getSohMap();
							pendingQty = returnPartialResponse.getPendingQty();
							omsTempCoFo.setOrderQty(new BigDecimal(pendingQty));
							log.info("=========returnPartialResponse objects for SU=================");
							log.info("orderQty " + orderQty);
							log.info("pendingQty " + pendingQty);
							log.info("maxFulfilOrderNo " + maxFulfilOrderNo);
							log.info("sohMap " + sohMap.keySet());
							log.info("=========returnPartialResponse objects for SU=================");
						}
						SourceLocIdentify sourceLocIdentify = new SourceLocIdentify();
						newFulfillMap = sourceLocIdentify.createFulfillDetailMap(sohMap, maxFulfilOrderNo);
						log.info("newFulfillMap.keySet() " + newFulfillMap.keySet());
						if (orderQty == 0) {
							break;
						}
						priority++;
					}
				}
			}
			log.info("newFulfillMap " + newFulfillMap.keySet());
			for (BigDecimal key : newFulfillMap.keySet()) {
				log.info("newFulfillMap.keySet() " + newFulfillMap.keySet());
				log.info("key " + key);
				tempList = newFulfillMap.get(key);
				log.info("tempList.size()" + tempList.size());
				log.info("omsCustOrdNo " + omsCustOrdNo + "after getting P response and checking for next location calling the callWebservices method");
				try {
					log.info("In class non saddad --- callwebservice method in line 1200");
					processedObject = interfacePersistence.callWebservices(omsCustOrdNo, tempList);
				} catch (Exception e) {
					log.info("omsCustOrdNo " + omsCustOrdNo + "Exception occured " + e.getMessage());
					OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
					omsCustOrdHead.setStatus("F");
					session.mergeOmsCustOrdHead(omsCustOrdHead);
					log.info("omsCustOrdNo " + omsCustOrdNo + "Rollbacking");
					try {
						NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
						nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
					} catch (Exception ex) {
						log.warn("Exception occurred during roll back", ex);
					}
					rollbackWHandST(input.getCustomerOrderNo());
					log.info("omsCustOrdNo " + omsCustOrdNo + "throwing RMS_UNAVL");
				}
				this.fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
				currentFulFilOrderNo = processedObject.getCurrentFulFilOrderNo();
				log.info("currentFulfilOrderNo inside presponse " + currentFulFilOrderNo);
				log.info("omsCustOrdNo " + omsCustOrdNo + "+++++++++++++++++ fulfilOrdCfmCol " + this.fulfilOrdCfmCol.getCollectionSize());
				tempList = processedObject.getOmsTempCoFoList();
				newFulfillMap.put(key, tempList);
				log.info("omsCustOrdNo " + omsCustOrdNo + "After getting from PResponseProcessingObj fulfilOrdCfmCol " + this.fulfilOrdCfmCol.getCollectionSize());
				pResponseProcessingObj.setFulfilOrdCfmCol(this.fulfilOrdCfmCol);
				pResponseProcessingObj.setNewFulfillMap(newFulfillMap);
				responseStatus = interfacePersistence.processWebserviceResponse(this.fulfilOrdCfmCol, tempList);
				log.info("responseStatus " + responseStatus);
				// added code handle nested p and X respone.
				if ("P".equals(responseStatus)) {
					this.pfulfilOrdCfmCols.put(key, this.fulfilOrdCfmCol);
				} else if ("X".equals(responseStatus)) {
					this.xfulfilOrdCfmCols.put(key, tempList);
				}
			}
		} catch (Exception e) {
			OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
			omsCustOrdHead.setStatus("F");
			session.mergeOmsCustOrdHead(omsCustOrdHead);
			/*
			 * OMSCustomerOrderBean oMSCustomerOrderBean = new OMSCustomerOrderBean();
			 * oMSCustomerOrderBean.rollback(fulfillDetailMap);
			 */
			try {
				NonSADADPayment nonSaddadaPayment = new NonSADADPayment();
				nonSaddadaPayment.rollbackExceptDCtoDC(input.getCustomerOrderNo());
			} catch (Exception ex) {
				log.info("Exception during roll back");
			}
			rollbackWHandST(input.getCustomerOrderNo());
			throw new SOAPFaultException(OMSUtil.getInstance().newSoapFault("SYSTEM ERROR"));
		}
		return pResponseProcessingObj;
	}

	public void processNonSadadBackOrder(Map<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap, BigDecimal omsCustOrdNo)
			throws SOAPException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException,
			com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException, com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException,
			EntityAlreadyExistsWSFaultException {
		log.info("omsCustOrdNo " + omsCustOrdNo + "***Start : processNonSadad in Back Order");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		ProcessedObject processedObject = null;
		// List<OmsTempCoFo> tempCOList =
		// session.getOmsTempCoFoFindByOmsCustOrdNo(omsCustOrdNo);
		// log.info("omsCustOrdNo "+omsCustOrdNo +"temp size" + tempCOList.size());
		InterfacePersistence interfacePersistence = new InterfacePersistence();
		ArrayList<OmsTempCoFo> tempList = null;
		log.info("omsCustOrdNo " + omsCustOrdNo + "fulfillDetailMap.keySet()" + fulfillDetailMap.keySet());
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "inside ");
			tempList = fulfillDetailMap.get(key);
			// for (OmsTempCoFo tempList : tempCOList) {
			log.info("omsCustOrdNo " + omsCustOrdNo + "tempList size  is " + tempList.size());
			try {
				log.info("In class non saddad --- callwebservice method in line 1274");
				processedObject = interfacePersistence.callWebservices(omsCustOrdNo, tempList);
				fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
				log.info("omsCustOrdNo " + omsCustOrdNo + "Call successful ,retuned processed obeject");
				fulfilOrdCfmCol = processedObject.getFulfilOrdCfmCol();
				tempList = processedObject.getOmsTempCoFoList();
				log.info("omsCustOrdNo " + omsCustOrdNo + "Processed object size" + tempList.size());
				// Update the value of map with processing app (SIM/RMS)
				fulfillDetailMap.put(key, tempList);
			} catch (Exception e) {
				log.error("Error in processing fulfillment :" + e.getMessage());
				if (e.getMessage().contains("locked")) {
					// If SIM and RMS tables are locked update status in oms tables
					log.info("omsCustOrdNo " + omsCustOrdNo + "Table is locked");
					for (OmsTempCoFo omsTempCoFo : tempList) {
						omsTempCoFo.setStatus("L");
						// session.mergeOmsTempCoFo(omsTempCoFo);
					}
				} else {
					for (OmsTempCoFo omsTempCoFo : tempList) {
						omsTempCoFo.setStatus("F");
						// session.mergeOmsTempCoFo(omsTempCoFo);
					}
				}
				// In either of the above two cases RETRY
			}
			interfacePersistence.processWebserviceResponse(fulfilOrdCfmCol, tempList);
			// for loop end
		}
		OmsCoFulfillDetail omsCoFulfillDetail = new OmsCoFulfillDetail();
		OmsRtlogPublishLog omsRtlogPublishLog = new OmsRtlogPublishLog();
		for (BigDecimal key : fulfillDetailMap.keySet()) {
			tempList = fulfillDetailMap.get(key);
			for (OmsTempCoFo omsTemp : tempList) {
				omsCoFulfillDetail.setFulfillOrderNo(omsTemp.getFulfillOrderNo());
				omsCoFulfillDetail.setFulfillLocType(omsTemp.getFulfillLocationType());
				omsCoFulfillDetail.setFulfillLoc(omsTemp.getFulfillLocId());
				omsCoFulfillDetail.setItem(omsTemp.getItem());
				omsCoFulfillDetail.setOmsCustOrdNo(omsCustOrdNo);
				omsCoFulfillDetail.setSourceLoc(omsTemp.getSourceLocId());
				omsCoFulfillDetail.setSourceLocType(omsTemp.getSourceLocationType());
				omsCoFulfillDetail.setFulfillReqQty(omsTemp.getOrderQty());
				omsCoFulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
				omsCoFulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
				omsCoFulfillDetail.setFulfillConfQty(omsTemp.getFoConfQty());
				omsCoFulfillDetail.setFulfillStatus("C");
				omsCoFulfillDetail.setCreateDatetime(new Timestamp(new Date().getTime()));
				session.persistOmsCoFulfillDetail(omsCoFulfillDetail);
				OmsCustOrdHead omsCustOrdHead = session.getOmsCustOrdHeadFindByOmsCustOrdNo(omsCustOrdNo);
				// check payment status
				if (omsCustOrdHead.getPayInStore().equals("N")) {
					// omsRtlogPublishLog.setFulfillOrderNo(omsTemp.getFulfillOrderNo());
					omsRtlogPublishLog.setLocation((omsCustOrdHead.getOrderRequestorId()));
					omsRtlogPublishLog.setItem(omsTemp.getItem());
					omsRtlogPublishLog.setPublishedInd("N");
					omsRtlogPublishLog.setOmsCustOrdNo(omsCustOrdNo);
					omsRtlogPublishLog.setTranType("ORI");
					omsRtlogPublishLog.setQty(omsTemp.getOrderQty());
					omsRtlogPublishLog.setCreateDatetime(new Timestamp(new Date().getTime()));
					session.persistOmsRtlogPublishLog(omsRtlogPublishLog);
				}
			}
		}
		log.info("omsCustOrdNo " + omsCustOrdNo + "***End : processNonSadad");
	}

// for P/X processing. code for consecutive P response
	private ArrayList<OmsTempCoFo> findUnProcessed_P_MapObject(ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) {
		log.info("<----------------- Begin findUnProcessed_P_MapObject method------------------->");
		ArrayList<OmsTempCoFo> tempList = null;
		ArrayList<OmsTempCoFo> tempList1 = null;
		for (BigDecimal tempKey : fulfillDetailMap.keySet()) {
			log.info("<--- temp key --->" + tempKey + "<----->");
			tempList = fulfillDetailMap.get(tempKey);
			for (OmsTempCoFo omsTemp : tempList) {
				if (omsTemp.getRmsResponseCode() != null && !"".equals(omsTemp.getRmsResponseCode())) {
					if ("P".equals(omsTemp.getRmsResponseCode())) {
						log.info("<---->" + omsTemp.getRmsResponseCode() + "<----->");
						tempList1 = tempList;
						return tempList1;
					}
				}
			}
		}
		log.info("<--- Before returning findUnProcessed_P_MapObject---->" + tempList1 + "<------>");
		return tempList1;
	} // end of findUnProcessed_P_MapObject method....
// Method added as part of P/X processing, add it to OMSUtil

	private ArrayList<OmsTempCoFo> findUnProcessed_X_MapObject(ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) {
		log.info("<------ Begin findUnProcessed_X_MapObject method--------->");
		ArrayList<OmsTempCoFo> tempList = null;
		ArrayList<OmsTempCoFo> tempList1 = null;
		for (BigDecimal tempKey : fulfillDetailMap.keySet()) {
			log.info("<----- temp key ------->" + tempKey + "<----->");
			tempList = fulfillDetailMap.get(tempKey);
			for (OmsTempCoFo omsTemp : tempList) {
				if (omsTemp.getRmsResponseCode() != null && !"".equals(omsTemp.getRmsResponseCode())) {
					if ("X".equals(omsTemp.getRmsResponseCode())) {
						log.info("<---------------->" + omsTemp.getRmsResponseCode() + "<--------------------->");
						tempList1 = tempList;
						return tempList1;
					}
				} // end of if condition
			} // end of inner loop
		} // end of outer loop
		log.info("<----- Before returning findUnProcessed_X_MapObject----->" + tempList1 + "<---->");
		return tempList1;
	} // end of function
// added as part P/X ,add it to OMSUtil

	private BigDecimal getUnProceesedPResponsekey(ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap) {
		log.info("<----Begin of getUnProceesedPResponsekey method------>");
		BigDecimal unProcessedPResponsekey = null;
		ArrayList<OmsTempCoFo> tempList = null;
		for (BigDecimal tempKey : fulfillDetailMap.keySet()) {
			log.info("<---- temp key ---->" + tempKey + "<----->");
			tempList = fulfillDetailMap.get(tempKey);
			for (OmsTempCoFo omsTemp : tempList) {
				if (omsTemp.getRmsResponseCode() != null && !"".equals(omsTemp.getRmsResponseCode())) {
					if ("P".equals(omsTemp.getRmsResponseCode())) {
						unProcessedPResponsekey = tempKey;
						log.info("<----Map Object With-------->" + tempKey + "<----------Need to Procees....-->");
						return unProcessedPResponsekey;
					}
				}
			}
		}
		log.info("<------End of getUnProceesedPResponsekey method---------->");
		return unProcessedPResponsekey;
	}

//Added method for P/X handling, creats the map for unfilled P response objects
	private ArrayList<OmsTempCoFo> ProceessPResponseFulFillmentOrder(ArrayList<OmsTempCoFo> tempList, ConcurrentHashMap<BigDecimal, ArrayList<OmsTempCoFo>> fulfillDetailMap, BigDecimal omsCustOrdNo,
			BigDecimal key) {
		log.info(" <------Begin of ProceessPResponseFulFillmentOrder---------->");
		ArrayList<OmsTempCoFo> unfulfilledItems = new ArrayList<OmsTempCoFo>();
		Map<String, BigDecimal> tempMap = new HashMap<String, BigDecimal>();
		List<FulfilOrdCfmDtl> fulfilOrdCfmDtlList = null;
		log.info("<----The key is ------>" + key + "<------>");
		this.pfulfilOrdCfmCol = this.pfulfilOrdCfmCols.get(key);
		try {
			if (this.pfulfilOrdCfmCol.getFulfilOrdCfmDesc().isEmpty() && this.pfulfilOrdCfmCol.getCollectionSize() == 0) {
				log.info("<----------*************Response code is C**********************---------------> ");
			} else {
				log.info("<2.------------------------------------------------------------->");
				fulfilOrdCfmDtlList = this.pfulfilOrdCfmCol.getFulfilOrdCfmDesc().get(0).getFulfilOrdCfmDtl();
			}
		} catch (Exception e) {
			log.info("-------->Error Occurred------->" + e.getMessage() + "------------->");
		}
		if (fulfilOrdCfmDtlList != null && fulfilOrdCfmDtlList.size() > 0) {
			for (FulfilOrdCfmDtl fulfilOrdCfmDtl : fulfilOrdCfmDtlList) {
				log.info("<--------------P.fulfilOrdCfmDtl.getItem()------------>" + fulfilOrdCfmDtl.getItem() + "----fulfilOrdCfmDtl.getConfirmQty()-----" + fulfilOrdCfmDtl.getConfirmQty() + "--->");
				tempMap.put(fulfilOrdCfmDtl.getItem(), fulfilOrdCfmDtl.getConfirmQty());
			}
			// for (BigDecimal mapkey : fulfillDetailMap.keySet())
			log.info("<------ Process The list with Key------>" + key + "<-------->");
			if (key != null) {
				tempList = fulfillDetailMap.get(key);
				log.info("<---------------------------  mapkey-------------->" + key + "<------>");
				for (OmsTempCoFo tempCoFo : tempList) {
					if (tempMap.containsKey(tempCoFo.getItem()) == true && tempCoFo.getRmsResponseCode() != null && tempCoFo.getRmsResponseCode().equals("P")) {
						log.info("<---Map-->" + key + "<------ Item --- --->" + tempCoFo.getItem() + "<-------------------->");
						OmsTempCoFo unfulfilledTemp = new OmsTempCoFo();
						log.info("tempCoFo.getOrderQty()---->" + tempCoFo.getOrderQty() + "----------->");
						log.info("tempCoFo.getItem()---->" + tempCoFo.getItem() + "----------->");
						log.info("tempMap.get(tempCoFo.getItem())--->" + tempMap.get(tempCoFo.getItem()) + "------->");
						log.info("remaing Qty " + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
						if ((tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())).intValue() > 0)) {
							log.info("omsCustOrdNo----->" + omsCustOrdNo + "------->");
							unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
							unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
							log.info("tempCoFo.getItem()----->" + tempCoFo.getItem() + "----->");
							unfulfilledTemp.setItem(tempCoFo.getItem());
							log.info("tempCoFo.getLineNo()" + tempCoFo.getLineNo() + "------>");
							unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
							log.info("tempCoFo.getOrderQty() " + tempCoFo.getOrderQty() + "----->");
							log.info("tempCoFo.getItem()" + tempCoFo.getItem() + "----->");
							log.info("tempMap.get(tempCoFo.getItem())" + tempMap.get(tempCoFo.getItem()) + "----->");
							log.info("remaing Qty " + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
							log.info("<---------New Order Quantity---------->" + tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())) + "--------->");
							unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty().subtract(tempMap.get(tempCoFo.getItem())));
							log.info("<-------------tempCoFo.getSourceLocId()--------->" + tempCoFo.getSourceLocId());
							unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
							log.info("tempCoFo.getSourceLocationType()" + tempCoFo.getSourceLocationType() + "-------->");
							unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
							log.info("tempCoFo.getVirtualWH()" + tempCoFo.getVirtualWH() + "----------->");
							unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
							log.info("tempCoFo.getCombinationId()" + tempCoFo.getCombinationId() + "<-------------->");
							unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
							unfulfilledTemp.setRmsResponseCode("P");
							log.info("Setting Response Code to P");
							log.info("unfulfilledTemp.getSourceLocId() " + unfulfilledTemp.getSourceLocId() + "------->");
							log.info("unfulfilledTemp.getCombinationId()" + unfulfilledTemp.getCombinationId() + "------->");
							log.info("unfulfilledTemp.getRmsResponseCode()" + unfulfilledTemp.getRmsResponseCode() + "------->");
							log.info("unfulfilledTemp.getOrderQty() " + unfulfilledTemp.getOrderQty() + "------>");
							tempCoFo.setOrderQty(tempMap.get(tempCoFo.getItem()));
							log.info("tempMap.get(tempCoFo.getItem())---->" + tempMap.get(tempCoFo.getItem()) + "---->");
							tempCoFo.setFoConfQty(tempMap.get(tempCoFo.getItem()));
							log.info("<----- Confrim Qauntity------->" + tempCoFo.getFoConfQty() + "<------>");
							if (unfulfilledTemp.getItem().equals(tempCoFo.getItem())) {
								tempCoFo.setRmsResponseCode("C");
								log.info("<---------tempCoFo.getRmsResponseCode()" + tempCoFo.getRmsResponseCode() + "-------->");
								log.info("<--------tempCoFo.orderQty----->" + tempMap.get(tempCoFo.getItem()) + "-------->");
								log.info("<---------tempCoFo.ConfQty------->" + tempMap.get(tempCoFo.getItem()) + "--------->");
							}
							unfulfilledItems.add(unfulfilledTemp);
						} else {
							tempCoFo.setRmsResponseCode("C");
							tempCoFo.setFoConfQty(tempCoFo.getOrderQty());
						}
					} // end of templist response code is 'P'.
					else if (tempCoFo.getRmsResponseCode() != null && tempCoFo.getRmsResponseCode().equals("P")) {
						// tempmap we are not considering
						log.info("<-------key doesnot contain in in tempmap-------------------->");
						log.info("passing currentFulfilOrderNo in fullfilldetailMap " + currentFulFilOrderNo + "<------>");
						tempList = fulfillDetailMap.get(new BigDecimal(currentFulFilOrderNo));
						// for (OmsTempCoFo temp : tempList) { // Removed from handle
						log.info("maxFulfilOrderNo " + maxFulfilOrderNo + "<-------->");
						log.info("temp.getOrderQty() " + tempCoFo.getOrderQty() + "<------>");
						log.info("temp.getItem()" + tempCoFo.getItem() + "<---->");
						log.info("remaing Qty " + tempCoFo.getOrderQty() + "<------>");
						log.info("temp.getRmsResponseCode() " + tempCoFo.getRmsResponseCode() + "<----->");
						if (tempCoFo.getOrderQty().intValue() > 0) {
							log.info(" <---------Creating an Object Of unfulfilledTemp------------->");
							OmsTempCoFo unfulfilledTemp = new OmsTempCoFo();
							log.info("<---omsCustOrdNo--->" + omsCustOrdNo + "<--maxFulfilOrderNo-->" + maxFulfilOrderNo + "Item" + tempCoFo.getItem());
							unfulfilledTemp.setOmsCustOrdNo(omsCustOrdNo);
							unfulfilledTemp.setFulfillOrderNo(new BigDecimal(maxFulfilOrderNo));
							unfulfilledTemp.setItem(tempCoFo.getItem());
							log.info("Line No" + tempCoFo.getLineNo() + "Order Qunatity" + tempCoFo.getOrderQty() + "Source Loc Id" + tempCoFo.getSourceLocId());
							unfulfilledTemp.setLineNo(tempCoFo.getLineNo());
							unfulfilledTemp.setOrderQty(tempCoFo.getOrderQty());
							unfulfilledTemp.setSourceLocId(tempCoFo.getSourceLocId());
							log.info("Source Location Type" + tempCoFo.getSourceLocationType() + " Virtual WH" + tempCoFo.getVirtualWH() + "CombinationId" + tempCoFo.getCombinationId());
							unfulfilledTemp.setSourceLocationType(tempCoFo.getSourceLocationType());
							unfulfilledTemp.setVirtualWH(tempCoFo.getVirtualWH());
							unfulfilledTemp.setCombinationId(tempCoFo.getCombinationId());
							unfulfilledTemp.setRmsResponseCode("P");
							log.info("<---OrderQty-->" + tempCoFo.getOrderQty());
							tempCoFo.setOrderQty(tempCoFo.getOrderQty());
							tempCoFo.setFoConfQty(new BigDecimal(0));
							if (unfulfilledTemp.getItem().equals(tempCoFo.getItem())) {
								tempCoFo.setRmsResponseCode("C");
								log.info("temp.getRmsResponseCode()" + tempCoFo.getRmsResponseCode() + "<------>");
							}
							log.info("<--------Unfulfilled Items add into------->");
							unfulfilledItems.add(unfulfilledTemp);
						} else {
							log.info("We are setting Response Code into C-------->");
							tempCoFo.setRmsResponseCode("C");
							tempCoFo.setFoConfQty(new BigDecimal(0));
						}
						// } // end of for loop
					} // end of else if part.
				} // end of inner loop
			} // map key ................... end of outer loop
		} // end if statement of fulfilOrdCfmDtlList
		log.info("<------------------ Before returning ProceessPResponseFulFillmentOrder --------------------->");
		return unfulfilledItems;
	}

	private void rollbackWHandST(String customerOrderNo) throws SOAPException {
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		List<Ordcust> ordcustList = session.getOrdcustFindByCustomerOrderNoandStatus(customerOrderNo);
		if (ordcustList.size() > 0) {
			for (Ordcust list : ordcustList) {
				if (list.getSourceLocType() != null && list.getFulfillLocType() != null) {
					if (list.getSourceLocType().equals("WH") && list.getFulfillLocType().equals("W")) {
						List<OrdcustDetail> ordcustDetailList = session.getOrdcustDetailFindByOrdCustNo(list.getOrdcustNo());
						log.info("Size of ordcustDetailList : " + ordcustDetailList.size());
						Connection con = null;
						CallableStatement pstmt = null;
						ResultSet rs = null;
						try {
							con = OMSUtil.createDBConnection(OMSConstants.DS_OMS_STRING);
							// pstmt = "{call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?) }";
							pstmt = con.prepareCall("{? = call XTRA_OMS_TSF_CAN_SQL.OMS_APRV_TSF_CAN(?, ?, ?, ?)}");
							for (OrdcustDetail ordcustDetail : ordcustDetailList) {
								pstmt.registerOutParameter(1, Types.VARCHAR);
								pstmt.registerOutParameter(2, Types.VARCHAR);
								pstmt.setBigDecimal(3, list.getTsfNo());
								pstmt.setString(4, ordcustDetail.getItem());
								pstmt.setBigDecimal(5, ordcustDetail.getQtyOrderedSuom());
								pstmt.execute();
								// rs = stmt.getString(1);
								String errorTextflag = pstmt.getString(2);
								log.info("FLAG : " + errorTextflag);
								String errorReturned = pstmt.getString(1);
								log.info("FLAG : " + errorReturned);
								if (errorReturned.equals("Success") && errorTextflag.equals("Success")) {
									log.info("Package call is successful");
								} else {
									log.info("Package for Rollback of Cancellation Stub... Not working");
								}
							} // end of loop OrdcustDetail
						} catch (Exception e) {
							log.error(e);
						} finally {
							try {
								OMSUtil.closeDBConnection(con, pstmt, rs);
							} catch (Exception e2) {
								log.error(e2.getMessage());
							}
						}
					}
				}
			}
		}
	}

//Added method for Rollback
	public void rollbackExceptDCtoDC(String customerOrderNo)
			throws SOAPException, EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException {
		log.info("Inside the rollback method for RMS WebService");
		OMSUtilSessionEJB session = OMSUtil.doLookup();
		OMSUtilCommons omsutilcommons = new OMSUtilCommons();
		List<Ordcust> ordcustList = session.getOrdcustFindByCustomerOrderNoandStatus(customerOrderNo);
		log.info("<--------- List of Ordcust Table : " + ordcustList.size() + " --------->");
		log.info("<--------- List Contains : " + ordcustList.toString() + " ---------->");
		FulfillOrderService fulfillOrderService = new FulfillOrderService();
		FulfillOrderPortType fulfillOrderPortType = fulfillOrderService.getFulfillOrderPort();
		if (ordcustList.size() > 0) {
			for (Ordcust list : ordcustList) {
				if (!list.getFulfillLocType().equals("W")) {
					FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
					fulfilOrdColRef.setCollectionSize(1);
					FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
					// call ordcustdetail table
					log.info("Calling OrdCust_Detail Table .... " + list.getOrdcustNo());
					// fulfilOrdRef=callOrdCustDetail(list.getOrdcustNo(),fulfilOrdRef);
					List<OrdcustDetail> ordcustDetailList = session.getOrdcustDetailFindByOrdCustNo(list.getOrdcustNo());
					log.info("Size of ordcustDetailList : " + ordcustDetailList.size());
					for (OrdcustDetail ordcustDetail : ordcustDetailList) {
						FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
						log.info("Cancel Quantity in fulfilOrdDtlRef.getCancelQtySuom() :" + fulfilOrdDtlRef.getCancelQtySuom());
						if (fulfilOrdDtlRef.getCancelQtySuom() == null) {
							log.info("Inside IF loop");
							fulfilOrdDtlRef.setCancelQtySuom(ordcustDetail.getQtyOrderedSuom());
							log.info("OrdCustNo : " + ordcustDetail.getOrdcustNo() + "<-------Cancel Qty : " + ordcustDetail.getQtyOrderedSuom() + " --------->");
							fulfilOrdDtlRef.setItem(ordcustDetail.getItem());
							log.info("OrdCustNo : " + ordcustDetail.getOrdcustNo() + "<-------- Item : " + ordcustDetail.getItem() + "--------->");
							// fulfilOrdDtlRef.setRefItem(value);
							fulfilOrdDtlRef.setTransactionUom(ordcustDetail.getTransactionUom());
							fulfilOrdDtlRef.setStandardUom(ordcustDetail.getStandardUom());
							fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
						}
					}
					log.info("Table OrdCust_Detail Processed ....");
					// Added code for SIM unapproved transfer
					if (list.getTsfNo() != null) {
						log.info("Transfer Number from Ordcust Table : " + list.getTsfNo());
						omsutilcommons.rollbackForTransfer(list.getTsfNo().toString());
						log.info("Transfer Table Processed ....");
					}
					log.info("customerOrderNo : " + customerOrderNo + "+<----------CustomerOrderNo : " + customerOrderNo + " --------------->");
					fulfilOrdRef.setCustomerOrderNo(customerOrderNo);
					log.info("customerOrderNo : " + customerOrderNo + "+<---------FulfilLoc Id : " + list.getFulfillLocId().longValue() + " ------------>");
					fulfilOrdRef.setFulfillLocId(list.getFulfillLocId().longValue());
					log.info("customerOrderNo : " + customerOrderNo + "<------FulfilLocType : " + list.getFulfillLocType() + " ----------->");
					fulfilOrdRef.setFulfillLocType(FulfillLocType.fromValue(list.getFulfillLocType()));
					log.info("customerOrderNo : " + customerOrderNo + "<-------Fulfil Order Number : " + list.getFulfillOrderNo() + " --------->");
					fulfilOrdRef.setFulfillOrderNo(String.valueOf(list.getFulfillOrderNo()));
					if (list.getSourceLocId() != null) {
						log.info("customerOrderNo : " + customerOrderNo + "<-----SourceLoc Id : " + list.getSourceLocId().longValue() + " --------->");
						fulfilOrdRef.setSourceLocId(list.getSourceLocId().longValue());
						log.info("customerOrderNo : " + customerOrderNo + "<---------Source Loc Type : " + list.getSourceLocType() + " --------->");
						fulfilOrdRef.setSourceLocType(SourceLocType.fromValue(list.getSourceLocType()));
					}
					log.info("list.getOrdcustNo() " + list.getOrdcustNo());
					fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);
					try {
						CancelFulfilOrdColRef colRef = new CancelFulfilOrdColRef();
						colRef.setFulfilOrdColRef(fulfilOrdColRef);
						InvocationSuccess result = OracleBaseAPIUtil.getOracleRMSClient().cancelFulfilOrdColRef(colRef).getInvocationSuccess();
						log.info("Result from RMS ---> " + result.getSuccessMessage());
					} catch (Exception e) {
						log.info("Exception " + e.getMessage());
					}
					log.info(">>> RMS Completed");
					try {
						omsutilcommons.rollbackforSIMOrder(customerOrderNo, fulfilOrdRef);
						log.info(">>> SIM Completed");
					} catch (Exception e) {
						log.info("customerOrderNo " + customerOrderNo + "Exception while calling SIM rollback " + e.getMessage());
					}
				}
			}
		}
		/*
		 * else if(ordcustList.size()==0) {
		 * log.info("No records in RMS for customerOrderNo"
		 * +customerOrderNo+"cancelling in SIM "); try { FulfilOrdRef fulfilOrdRef = new
		 * FulfilOrdRef(); rollbackforSIM(customerOrderNo,fulfilOrdRef);
		 * log.info("Rollback is success for SIM cancellation for customerOrderNo "
		 * +customerOrderNo); } catch(Exception e) {
		 * log.info("Exception in cancellation for customerOrderNo"+customerOrderNo+
		 * e.getMessage()); }
		 * 
		 * }
		 */
		// added code for SIM rollback for 3121 bug
		try {
			log.info("customerOrderNo " + customerOrderNo + "Irrespective of cancellation calling SIM");
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
			omsutilcommons.rollbackforSIM(customerOrderNo, fulfilOrdRef);
			log.info("Rollback is success for SIM cancellation for customerOrderNo " + customerOrderNo);
		} catch (Exception e) {
			log.info("Exception in cancellation for customerOrderNo" + customerOrderNo + e.getMessage());
		}
	} // End of rollbackRMS Method
} // end of class
