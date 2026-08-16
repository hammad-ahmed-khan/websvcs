package com.extra.oms.custOrder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.bds.bean.carrera.TransferCreationResponse;
import com.extra.bds.bean.carrera.TransferRequest;
import com.extra.common.exception.BaseException;
import com.extra.common.exception.SoftException;
import com.extra.common.model.OmsBackOrderDtl;
import com.extra.common.model.OmsCoFulfillDetail;
import com.extra.common.model.OmsCustAddressInfo;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.common.model.OmsCustOrdItem;
import com.extra.common.model.OmsCustOrdReserve;
import com.extra.common.model.OmsUnapprovedTransfer;
import com.extra.common.model.TransferLineItem;
import com.extra.common.model.Wh;
import com.extra.oms.bean.ws.WhAdjModVo;
import com.extra.oms.custOrder.bean.ResourceBean;
import com.extra.oms.custOrder.dao.BackOrderDAO;
import com.extra.oms.custOrder.model.bo.BOStockAvailablityResource;
import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IOracleRMSClient;
import com.extra.oms.service.client.IOracleSIMClient;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.ConfirmType;
import com.oracle.retail.integration.base.bo.fulfilordcfmdesc.v1.FulfilOrdCfmDesc;
import com.oracle.retail.integration.base.bo.fulfilordcfmdtl.v1.FulfilOrdCfmDtl;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfilOrdDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.LocType;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvItmMod;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequest;

/**
 * @author aibrahim
 *
 */
public class BackOrderProcess implements Callable<Void> {

	private static final Logger LOG = Logger.getLogger(BackOrderProcess.class);

	private OmsCustOrdHead custOrdHead;

	private BOStockAvailablityResource availablityResource;

	private ResourceBean resourceBean;

	private Map<String, Object> updates = new HashMap<>();

	public BackOrderProcess(OmsCustOrdHead custOrdHead, BOStockAvailablityResource availablityResource, ResourceBean resourceBean) {
		this.custOrdHead = custOrdHead;
		this.availablityResource = availablityResource;
		this.resourceBean = resourceBean;
	}

	@Override
	public Void call() {
		try {
			LOG.info("Processing the back order " + custOrdHead.getCustOrderNo() + " and oms cust order number " + custOrdHead.getOmsCustOrdNo().toPlainString());
			BigDecimal maxFulfilOrdNo = availablityResource.getMaxFulfilOrdNo(custOrdHead.getCustOrderNo());
			boolean hasDataToProcess = false;
			if ("P".equals(custOrdHead.getOrdPaymentStatus()) && "R".equals(custOrdHead.getOrderCreateReserveInd()) ) {
				hasDataToProcess = createReservation(maxFulfilOrdNo);
			} else {
				hasDataToProcess = createFulfilment(maxFulfilOrdNo);
			}
			if (hasDataToProcess) {
				callExternalApi();
				resourceBean.getBackOrderDAO().updateBackOrderDetails(updates, custOrdHead.getCustOrderNo(), custOrdHead.getOmsCustOrdNo());
				LOG.info("Successfully processed the back order " + custOrdHead.getCustOrderNo() + " and oms cust order number " + custOrdHead.getOmsCustOrdNo().toPlainString());
			} else {
				LOG.warn("Stocts are not avaiable to process the back order number: " + custOrdHead.getCustOrderNo());
			}
		} catch (Exception e) {
			LOG.error("Error while processing the back order no: " + custOrdHead.getCustOrderNo(), e);
		}
		return null;
	}

	private boolean createFulfilment(BigDecimal maxFulfilOrdNo) throws Exception {
		Map<String, InvBackOrdDesc> invBackOrdDescs = new HashMap<>();
		Map<String, OmsBackOrderDtl> upBackOrderDtls = new HashMap<>(custOrdHead.getBackOrderDetails().size());
		Map<String, FulfilOrdDesc> rmsFulfilOrdMap = new HashMap<>();
		Map<String, FulfilOrdDesc> simFulfilOrds = new HashMap<>();
		List<TransferCreationRequest> carreraTransfers = new LinkedList<>();
		Map<String, BigDecimal> itemAvailabilityMap = new HashMap<>();
		Map<String, OmsCoFulfillDetail> coFulfils = new HashMap<>();
		List<String> tfrsFulfilS = new ArrayList<>();
		List<String> whStTrfsFulfils = new ArrayList<>();

		Set<String> rmsApiLocks = new TreeSet<>();
		Set<String> simApiLocks = new TreeSet<>();

		boolean hasDataToProcess = false;
		for (OmsBackOrderDtl backOrderDtl : custOrdHead.getBackOrderDetails()) {
			BigDecimal availableQty = availablityResource.getAvailableSOH(backOrderDtl.getItem(), backOrderDtl.getSourceLoc(), backOrderDtl.getSourceLocType(), backOrderDtl.getSourceQty().subtract(backOrderDtl.getFulfillQty()));
			if (availableQty.intValue() > 0) {
				hasDataToProcess = true;
				OmsCoFulfillDetail fulfillDetail = new OmsCoFulfillDetail();
				maxFulfilOrdNo = maxFulfilOrdNo.add(BigDecimal.ONE);
				String fulfilOrdNoKey = maxFulfilOrdNo.toPlainString() + "~" + backOrderDtl.getItem();
				fulfillDetail.setCombinationId(backOrderDtl.getCombinationId());
				fulfillDetail.setFulfillOrderNo(maxFulfilOrdNo);
				fulfillDetail.setFulfillLocType(backOrderDtl.getFulfillLocType());
				fulfillDetail.setFulfillLoc(backOrderDtl.getFulfillLoc());
				fulfillDetail.setLineNo(backOrderDtl.getLineNo());
				fulfillDetail.setItem(backOrderDtl.getItem());
				fulfillDetail.setOmsCustOrdNo(backOrderDtl.getOmsCustOrdNo());
				fulfillDetail.setSourceLocType(backOrderDtl.getSourceLocType());
				if (SourceLocType.WH.equals(SourceLocType.fromValue(backOrderDtl.getSourceLocType()))) {
					fulfillDetail.setSourceLoc(backOrderDtl.getWh().getPhysicalWh());
				} else {
					fulfillDetail.setSourceLoc(backOrderDtl.getSourceLoc());
				}
				fulfillDetail.setFulfillReqQty(availableQty);
				fulfillDetail.setFulfillConfQty(availableQty);
				fulfillDetail.setFulfillDeliverQty(BigDecimal.ZERO);
				fulfillDetail.setFulfillCancelQty(BigDecimal.ZERO);
				fulfillDetail.setFulfillStatus("C");
				coFulfils.put(fulfilOrdNoKey, fulfillDetail);
				itemAvailabilityMap.put(fulfilOrdNoKey, availableQty);
	
				if (("W".equals(backOrderDtl.getFulfillLocType()) && "WH".equals(backOrderDtl.getSourceLocType())) 
					    || ("WH".equals(backOrderDtl.getSourceLocType()) && "S".equals(backOrderDtl.getFulfillLocType()))) {					
					carreraTransfers.add(createCarreraFulfilment(backOrderDtl, maxFulfilOrdNo, availableQty));
				} else {
					FulfilOrdDesc fulfilOrdDesc = createBaseFulfilment(backOrderDtl, maxFulfilOrdNo, availableQty);
					rmsFulfilOrdMap.put(maxFulfilOrdNo.toPlainString(), fulfilOrdDesc);
					rmsApiLocks.add((backOrderDtl.getItem() + "-" + backOrderDtl.getSourceLoc()).intern());
					if (backOrderDtl.getSourceLoc().compareTo(backOrderDtl.getFulfillLoc()) == 0) {
						simFulfilOrds.put(maxFulfilOrdNo.toPlainString(), fulfilOrdDesc);
						simApiLocks.add((backOrderDtl.getItem() + "-" + backOrderDtl.getSourceLoc()).intern());
					} else if ("S".equals(backOrderDtl.getFulfillLocType()) && "ST".equals(backOrderDtl.getSourceLocType())) {
						tfrsFulfilS.add(maxFulfilOrdNo.toPlainString());
					} else if ("WH".equals(backOrderDtl.getSourceLocType())) {
						whStTrfsFulfils.add(maxFulfilOrdNo.toPlainString());
					}
				}
	
				InvBackOrdDesc backOrdDesc = new InvBackOrdDesc();
				backOrdDesc.setItem(backOrderDtl.getItem());
				backOrdDesc.setBackorderQty(availableQty.negate());
				if ("WH".equals(backOrderDtl.getSourceLocType())) {
					backOrdDesc.setLocType(LocType.W);
					Wh wh = backOrderDtl.getWh();
					backOrdDesc.setLocation(wh.getPhysicalWh().longValue());
					backOrdDesc.setChannelId(wh.getChannelId());
				} else {					
					backOrdDesc.setLocType(LocType.S);
					backOrdDesc.setLocation(backOrderDtl.getSourceLoc().longValue());
				}
				backOrdDesc.setUnitOfMeasure(backOrderDtl.getOmsCustOrdItem().getStandardUom());
				invBackOrdDescs.put(fulfilOrdNoKey, backOrdDesc);
				backOrderDtl.setFulfillQty(backOrderDtl.getFulfillQty().add(availableQty));
				if (backOrderDtl.getSourceQty().compareTo(backOrderDtl.getFulfillQty()) == 0) {
					backOrderDtl.setBackorderStatus("S");
				}
				upBackOrderDtls.put(fulfilOrdNoKey, backOrderDtl);
			}
		}
		if (!rmsFulfilOrdMap.isEmpty()) {
			updates.put("RMS_FULFILEMNTS", rmsFulfilOrdMap);
		}
		if (!simFulfilOrds.isEmpty()) {
			updates.put("SIM_FULFILMENTS", simFulfilOrds);
		}
		if (!invBackOrdDescs.isEmpty()) {
			updates.put("INV_ADJ_REVERSE", invBackOrdDescs);
		}
		if (!carreraTransfers.isEmpty()) {
			updates.put("CARRERA_FULFILS", carreraTransfers);
		}
		if (!coFulfils.isEmpty()) {
			updates.put("CO_FULFILS", coFulfils);
			updates.put("ITEM_AVAILABILTY", itemAvailabilityMap);
		}
		if (!upBackOrderDtls.isEmpty()) {
			updates.put("BACK_ORDS", upBackOrderDtls);
		}
		if (!tfrsFulfilS.isEmpty()) {
			updates.put("ST_TRANSERS", tfrsFulfilS);
		}
		if (!whStTrfsFulfils.isEmpty()) {
			updates.put("WH_ST_TRANSERS", whStTrfsFulfils);
		}
		if (!rmsApiLocks.isEmpty()) {
			updates.put("RMS_API_LOCKS", rmsApiLocks);
		}
		if (!simApiLocks.isEmpty()) {
			updates.put("SIM_API_LOCKS", simApiLocks);
		}
		return hasDataToProcess;
	}

	private TransferCreationRequest createCarreraFulfilment(OmsBackOrderDtl backOrderDtl, BigDecimal maxFulfilOrdNo, BigDecimal availableQty) {
		TransferCreationRequest request = new TransferCreationRequest();
		request.setCust_ord_no(custOrdHead.getCustOrderNo());
		request.setDest_id(backOrderDtl.getFulfillLoc().intValue());
		request.setFul_loc(backOrderDtl.getFulfillLoc().intValue());
		request.setFul_ord_no(maxFulfilOrdNo.intValue());
		request.setRef_no(custOrdHead.getOmsCustOrdNo().toPlainString() + maxFulfilOrdNo.toPlainString());
		request.setSrc_id(backOrderDtl.getSourceLoc().intValue());
		request.setSrc_loc(backOrderDtl.getWh().getPhysicalWh().intValue());

		TransferRequest itemReq = new TransferRequest();
		itemReq.setItem(backOrderDtl.getItem());
		itemReq.setQty(availableQty.intValue());
		List<TransferRequest> items = new ArrayList<TransferRequest>();
		items.add(itemReq);
		request.setCustomerItems(items);
		return request;
	}

	private boolean createReservation(BigDecimal maxFulfilOrdNo) {
		List<OmsCustOrdReserve> newCustOrdReserves = new LinkedList<>();
		List<OmsCustOrdReserve> existCustOrdReserves = new LinkedList<>();
		Map<String, InvBackOrdDesc> invBackOrdDescs = new HashMap<>();
		Map<Long, StrAdjModVo> strAdjModVoMap = new HashMap<>();
		List<WhAdjModVo> whAdjModVos = new LinkedList<>();
		Map<String, OmsBackOrderDtl> upBackOrderDtls = new HashMap<>(custOrdHead.getBackOrderDetails().size());

		boolean hasDataToProcess = false;
		for (OmsBackOrderDtl backOrderDtl : custOrdHead.getBackOrderDetails()) {
			BigDecimal availableQty = availablityResource.getAvailableSOH(backOrderDtl.getItem(), backOrderDtl.getSourceLoc(), backOrderDtl.getSourceLocType(), backOrderDtl.getSourceQty().subtract(backOrderDtl.getFulfillQty()));
			if (availableQty.intValue() > 0) {
				hasDataToProcess = true;
				Map<String, OmsCustOrdReserve> ordReserveMap = resourceBean.getBackOrderDAO().getReserveOrderItems(custOrdHead.getOmsCustOrdNo());
				OmsCustOrdReserve custOrdReserve = ordReserveMap.get(backOrderDtl.getLineNo() + "~" + backOrderDtl.getSourceLoc());
				if (custOrdReserve == null) {
					maxFulfilOrdNo = maxFulfilOrdNo.add(BigDecimal.ONE);
					custOrdReserve = new OmsCustOrdReserve();
					custOrdReserve.setCombinationId(backOrderDtl.getCombinationId());
					custOrdReserve.setCreatedBy("User");
					custOrdReserve.setFulfillOrderNo(maxFulfilOrdNo);
					custOrdReserve.setItem(backOrderDtl.getItem());
					custOrdReserve.setLineNo(backOrderDtl.getLineNo());
					custOrdReserve.setLoc(backOrderDtl.getFulfillLoc());
					custOrdReserve.setLocType(backOrderDtl.getFulfillLocType());
					custOrdReserve.setOmsCustOrdNo(custOrdHead.getOmsCustOrdNo());
					custOrdReserve.setPaymentStatus("P");
					custOrdReserve.setQty(availableQty);
					custOrdReserve.setResvStatus("RES");
					custOrdReserve.setRmsResvLoc(backOrderDtl.getSourceLoc());
					custOrdReserve.setRmsResvLocType(backOrderDtl.getSourceLocType());
					custOrdReserve.setRmsResvQty(availableQty);
					custOrdReserve.setVirtualWH(backOrderDtl.getSourceLoc());
					newCustOrdReserves.add(custOrdReserve);
				} else {
					custOrdReserve.setRmsResvQty(custOrdReserve.getRmsResvQty().add(availableQty));
					custOrdReserve.setQty(custOrdReserve.getQty().add(availableQty));
					existCustOrdReserves.add(custOrdReserve);
				}
				if ("ST".equals(backOrderDtl.getSourceLocType())) {
					Long storeId = backOrderDtl.getSourceLoc().longValue();
					StrAdjModVo strAdjModVo = strAdjModVoMap.get(storeId);

					if (strAdjModVo == null) {
						strAdjModVo = new StrAdjModVo();
						strAdjModVo.setStoreId(storeId);
						strAdjModVo.setComments("Reserver for Customer Order No: " + custOrdHead.getCustOrderNo());
						strAdjModVoMap.put(storeId, strAdjModVo);
					}
					
					StrAdjItmMod adjItmMod = new StrAdjItmMod();
					adjItmMod.setItemId(backOrderDtl.getItem());
					adjItmMod.setQuantity(availableQty);
					adjItmMod.setReasonId(Integer.parseInt(availablityResource.getSystemParam("INV_RESV_CODE")));
					adjItmMod.setCaseSize(BigDecimal.ONE);
					strAdjModVo.getStrAdjItmMod().add(adjItmMod);
				} else if ("WH".equals(backOrderDtl.getSourceLocType())) {
					WhAdjModVo whAdjModVo = new WhAdjModVo();
					whAdjModVo.setItem(backOrderDtl.getItem());
					whAdjModVo.setLocation(backOrderDtl.getSourceLoc());
					whAdjModVo.setLocationType("W");
					whAdjModVo.setQty(availableQty);
					whAdjModVo.setReasonCode(Integer.parseInt(availablityResource.getSystemParam("REASON_CODE")));
					whAdjModVo.setStatus(Integer.parseInt(availablityResource.getSystemParam("RESV_INV_STATUS")));
					whAdjModVo.setUserId(custOrdHead.getCustOrderNo() + "_BKR");
					whAdjModVos.add(whAdjModVo);
				}
				InvBackOrdDesc backOrdDesc = new InvBackOrdDesc();
				backOrdDesc.setItem(backOrderDtl.getItem());
				backOrdDesc.setBackorderQty(availableQty.negate());
				if ("WH".equals(backOrderDtl.getSourceLocType())) {
					backOrdDesc.setLocType(LocType.W);
					Wh wh = backOrderDtl.getWh();
					backOrdDesc.setLocation(wh.getPhysicalWh().longValue());
					backOrdDesc.setChannelId(wh.getChannelId());
				} else {					
					backOrdDesc.setLocType(LocType.S);
					backOrdDesc.setLocation(backOrderDtl.getSourceLoc().longValue());
				}
				backOrdDesc.setUnitOfMeasure(backOrderDtl.getOmsCustOrdItem().getStandardUom());

				String fulfilOrdNoKey = maxFulfilOrdNo.toPlainString() + "~" + backOrderDtl.getItem();
				invBackOrdDescs.put(fulfilOrdNoKey, backOrdDesc);

				backOrderDtl.setFulfillQty(backOrderDtl.getFulfillQty().add(availableQty));
				if (backOrderDtl.getSourceQty().compareTo(backOrderDtl.getFulfillQty()) == 0) {
					backOrderDtl.setBackorderStatus("S");
				}
				upBackOrderDtls.put(fulfilOrdNoKey, backOrderDtl);
			}
		}
		if (!newCustOrdReserves.isEmpty()) {
			updates.put("NEW_RESERVES", newCustOrdReserves);
		}
		if (!existCustOrdReserves.isEmpty()) {
			updates.put("EX_RESERVES", existCustOrdReserves);
		}
		if (!invBackOrdDescs.isEmpty()) {
			updates.put("INV_ADJ_REVERSE", invBackOrdDescs);
		}
		if (!strAdjModVoMap.isEmpty()) {
			updates.put("STR_ADJUSTMENTS", strAdjModVoMap);
		}
		if (!whAdjModVos.isEmpty()) {
			updates.put("WH_ADJUSTMENT", whAdjModVos);
		}
		if (!upBackOrderDtls.isEmpty()) {
			updates.put("BACK_ORDS", upBackOrderDtls);
		}
		return hasDataToProcess;
	}

	private FulfilOrdDesc createBaseFulfilment(OmsBackOrderDtl backOrderDetail, BigDecimal exFulfilOrdNo, BigDecimal availablityQty) throws Exception {

		FulfilOrdDesc fulfilOrdDesc = new FulfilOrdDesc();
		fulfilOrdDesc.setCustomerOrderNo(custOrdHead.getCustOrderNo());
		fulfilOrdDesc.setFulfillOrderNo(exFulfilOrdNo.toPlainString());

		if (!backOrderDetail.getFulfillLoc().equals(backOrderDetail.getSourceLoc())) {
			fulfilOrdDesc.setSourceLocType(SourceLocType.valueOf(backOrderDetail.getSourceLocType()));
			if (SourceLocType.WH.equals(SourceLocType.fromValue(backOrderDetail.getSourceLocType()))) {
				fulfilOrdDesc.setSourceLocId(backOrderDetail.getWh().getPhysicalWh().longValue());
			} else {
				fulfilOrdDesc.setSourceLocId(backOrderDetail.getSourceLoc().longValue());
			}
		}
		fulfilOrdDesc.setFulfillLocId(backOrderDetail.getFulfillLoc().longValue());
		fulfilOrdDesc.setFulfillLocType(FulfillLocType.valueOf(backOrderDetail.getFulfillLocType()));
		fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.fromValue(availablityResource.getSystemParam("OMS_PARTIAL_DLV_IND")));
		fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(custOrdHead.getDeliveryType()));
		fulfilOrdDesc.setComments("Application Id:" + custOrdHead.getApplicationId());		
		GregorianCalendar gregCal = new GregorianCalendar();
		if (custOrdHead.getConsumerDlyTime() != null) {
			gregCal.setTimeInMillis(custOrdHead.getConsumerDlyTime().getTime());
			if ("WH".equals(backOrderDetail.getSourceLocType())) {
				gregCal.add(5, Calendar.YEAR);
			}
		} else {
			gregCal.setTimeInMillis(System.currentTimeMillis());
		}
		fulfilOrdDesc.setConsumerDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregCal));
		
		OmsCustOrdItem custOrdItem = backOrderDetail.getOmsCustOrdItem();
		FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
		fulfilOrdDtl.setItem(backOrderDetail.getItem());
		fulfilOrdDtl.setOrderQtySuom(availablityQty);
		fulfilOrdDtl.setComments(custOrdItem.getComments());
		fulfilOrdDtl.setUnitRetail(custOrdItem.getUnitRetail());
		fulfilOrdDtl.setRetailCurr(custOrdItem.getRetailCurr());
		fulfilOrdDtl.setStandardUom(custOrdItem.getStandardUom());
		fulfilOrdDtl.setTransactionUom(custOrdItem.getTransactionUom());
		fulfilOrdDtl.setSubstituteInd(custOrdItem.getSubstituteAllowInd());
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);

		OmsCustAddressInfo addressInfo = custOrdHead.getCustAddressInfo();
		FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
		fulfilOrdCustDesc.setCustomerNo(custOrdHead.getCustId());
		fulfilOrdCustDesc.setDeliverFirstName(addressInfo.getDeliverFirstName());
		fulfilOrdCustDesc.setDeliverPhoneticFirst(addressInfo.getDeliverPhoneticFirst());
		fulfilOrdCustDesc.setDeliverLastName(addressInfo.getDeliverLastName());
		fulfilOrdCustDesc.setDeliverPhoneticLast(addressInfo.getDeliverPhoneticLast());
		fulfilOrdCustDesc.setDeliverPreferredName(addressInfo.getDeliverPreferredName());
		fulfilOrdCustDesc.setDeliverAdd1(addressInfo.getDeliverAdd1());
		fulfilOrdCustDesc.setDeliverAdd2(addressInfo.getDeliverAdd2());
		fulfilOrdCustDesc.setDeliverCity(addressInfo.getDeliverCity());
		fulfilOrdCustDesc.setDeliverState(addressInfo.getDeliverState());
		fulfilOrdCustDesc.setDeliverCountryId(addressInfo.getDeliverCountry());
		fulfilOrdCustDesc.setDeliverCounty(addressInfo.getDeliverCountry());
		fulfilOrdCustDesc.setDeliverPost(addressInfo.getDeliverPost());
		fulfilOrdCustDesc.setDeliverPhone(addressInfo.getDeliverPhone());
		
		fulfilOrdCustDesc.setBillFirstName(addressInfo.getBillFirstName());
		fulfilOrdCustDesc.setBillPhoneticFirst(addressInfo.getBillPhoneticFirst());
		fulfilOrdCustDesc.setBillLastName(addressInfo.getBillLastName());
		fulfilOrdCustDesc.setBillPhoneticLast(addressInfo.getBillPhoneticLast());
		fulfilOrdCustDesc.setBillPreferredName(addressInfo.getBillPreferredName());
		fulfilOrdCustDesc.setBillCompanyName(addressInfo.getBillCompanyName());
		fulfilOrdCustDesc.setBillAdd1(addressInfo.getBillAdd1());
		fulfilOrdCustDesc.setBillAdd2(addressInfo.getBillAdd2());
		fulfilOrdCustDesc.setBillAdd3(addressInfo.getBillAdd3());
		fulfilOrdCustDesc.setBillCity(addressInfo.getBillCity());
		fulfilOrdCustDesc.setBillState(addressInfo.getBillState());
		fulfilOrdCustDesc.setBillCountryId(addressInfo.getBillCounty());
		fulfilOrdCustDesc.setBillCounty(addressInfo.getBillCounty());
		fulfilOrdCustDesc.setBillPost(addressInfo.getBillPost());
		fulfilOrdCustDesc.setBillJurisdiction(addressInfo.getBillJurisdiction());
		fulfilOrdCustDesc.setBillPhone(addressInfo.getBillPhone());
		
		fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
	
		return fulfilOrdDesc;
	}

	@SuppressWarnings("unchecked")
	private void callExternalApi() throws BaseException {
		Map<String, FulfilOrdDesc> rmsFulfilOrdMap = (Map<String, FulfilOrdDesc>) updates.get("RMS_FULFILEMNTS");
		Map<String, FulfilOrdDesc> simFulfilOrdMap = (Map<String, FulfilOrdDesc>) updates.get("SIM_FULFILMENTS");
		List<TransferCreationRequest> carreraTransfers = (List<TransferCreationRequest>) updates.get("CARRERA_FULFILS");
		Map<String, OmsBackOrderDtl> upBackOrderDtls = (Map<String, OmsBackOrderDtl>) updates.get("BACK_ORDS");
		Map<String, OmsCoFulfillDetail> coFulfils = (Map<String, OmsCoFulfillDetail>) updates.get("CO_FULFILS");
		Map<String, InvBackOrdDesc> invBackOrdDescs = (Map<String, InvBackOrdDesc>) updates.get("INV_ADJ_REVERSE");
		Map<String, BigDecimal> itemAvailabiltyMap = (Map<String, BigDecimal>) updates.get("ITEM_AVAILABILTY");
		Map<String, TransferCreationRequest> tsfNoMap = new HashMap<>();

		if (carreraTransfers != null && !carreraTransfers.isEmpty()) {
			ICarreraClient carreraClient = resourceBean.getCarreraClient();
			for (TransferCreationRequest creationRequest : carreraTransfers) {		
				TransferCreationResponse response = null;
				try {					
					response = carreraClient.getOrderItemsResponse(creationRequest);
				} catch (Exception e) {
					rollBackFulfilments(tsfNoMap, null);
					throw new BaseException("Error while calling carrera tranfer api for order " + custOrdHead.getCustOrderNo() + " fulfil order no " + creationRequest.getFul_ord_no(), e);
				}
				if (response.isSuccess()) {
					tsfNoMap.put(response.getTsf_No(), creationRequest);
					for (TransferRequest item: creationRequest.getCustomerItems()) {
						coFulfils.get(creationRequest.getFul_ord_no() + "~" + item.getItem()).setTsfNo(new BigDecimal(response.getTsf_No()));
					}
				} else {
					rollBackFulfilments(tsfNoMap, null);
					throw new BaseException("Carrera fulfillment failed for order " + custOrdHead.getCustOrderNo() + " and item " + creationRequest.getCustomerItems().get(0).getItem() + " source location " + creationRequest.getSrc_id() + ". Message received: " + response.getMessage());
				}
			}
		}
		if (rmsFulfilOrdMap != null && !rmsFulfilOrdMap.isEmpty()) {
			List<OmsCustOrdItem> updatedItems = new ArrayList<>(rmsFulfilOrdMap.size());
			Set<String> rmsApiLocks = (Set<String>) updates.get("RMS_API_LOCKS");
			
			FulfilOrdColDesc fulfilOrdCustDesc = new FulfilOrdColDesc();
			fulfilOrdCustDesc.setCollectionSize(rmsFulfilOrdMap.size());
			fulfilOrdCustDesc.getFulfilOrdDesc().addAll(rmsFulfilOrdMap.values());
			FulfilOrdCfmCol fulfilOrdCfmCol = null;
			try {
				CreateFulfilOrdColDesc colDesc = new CreateFulfilOrdColDesc();
				colDesc.setFulfilOrdColDesc(fulfilOrdCustDesc);
				Iterator<String> rmsLockIterator = rmsApiLocks.iterator();
				fulfilOrdCfmCol = synchronizeRMSApi(rmsLockIterator.next(), rmsLockIterator, colDesc);
			} catch (Exception e) {
				rollBackFulfilments(tsfNoMap, null);
				throw new BaseException("Error while calling RMS fulfilment api for order " + custOrdHead.getCustOrderNo(), e);
			}
			if (fulfilOrdCfmCol.getCollectionSize() == 0) {
				for (OmsBackOrderDtl backOrderDtl : upBackOrderDtls.values()) {
					backOrderDtl.getOmsCustOrdItem().setStatus("S");
					updatedItems.add(backOrderDtl.getOmsCustOrdItem());
				}
			} else {
				for (FulfilOrdCfmDesc fulfilOrdCfmDesc : fulfilOrdCfmCol.getFulfilOrdCfmDesc()) {
					ConfirmType confirmType = fulfilOrdCfmDesc.getConfirmType();
					if (ConfirmType.P.equals(confirmType)) {
						for (FulfilOrdCfmDtl fulfilOrdCfmDtl : fulfilOrdCfmDesc.getFulfilOrdCfmDtl()) {
							LOG.warn("Stock partially available for order number " + custOrdHead.getCustOrderNo() + " and fulfil order number " + fulfilOrdCfmDesc.getFulfillOrderNo());
							String fulfilOrdkey = fulfilOrdCfmDesc.getFulfillOrderNo() + "~" + fulfilOrdCfmDtl.getItem();
							OmsBackOrderDtl backOrderDtl = upBackOrderDtls.get(fulfilOrdkey);
							backOrderDtl.setFulfillQty(backOrderDtl.getFulfillQty().subtract(itemAvailabiltyMap.get(fulfilOrdkey)).add(fulfilOrdCfmDtl.getConfirmQty()));

							backOrderDtl.getOmsCustOrdItem().setStatus("P");
							updatedItems.add(backOrderDtl.getOmsCustOrdItem());
							
							OmsCoFulfillDetail coFulfillDetail = coFulfils.get(fulfilOrdkey);
							coFulfillDetail.setFulfillReqQty(fulfilOrdCfmDtl.getConfirmQty());
							coFulfillDetail.setFulfillConfQty(fulfilOrdCfmDtl.getConfirmQty());
							
							InvBackOrdDesc backOrdDesc = invBackOrdDescs.get(fulfilOrdkey);
							backOrdDesc.setBackorderQty(fulfilOrdCfmDtl.getConfirmQty().negate());
							
							if (simFulfilOrdMap != null && simFulfilOrdMap.containsKey(fulfilOrdCfmDesc.getFulfillOrderNo())) {
								FulfilOrdDtl fulfilOrdDtl = simFulfilOrdMap.get(fulfilOrdCfmDesc.getFulfillOrderNo()).getFulfilOrdDtl().get(0);
								fulfilOrdDtl.setOrderQtySuom(fulfilOrdCfmDtl.getConfirmQty());
							}
						}
					} else if (ConfirmType.X.equals(confirmType)) {
						for (FulfilOrdDtl fulfilOrdDtl : rmsFulfilOrdMap.get(fulfilOrdCfmDesc.getFulfillOrderNo()).getFulfilOrdDtl()) {
							LOG.warn("Stock not available for order number " + custOrdHead.getCustOrderNo() + " and fulfil order number " + fulfilOrdCfmDesc.getFulfillOrderNo());
							String fulfilOrdkey = fulfilOrdCfmDesc.getFulfillOrderNo() + "~" + fulfilOrdDtl.getItem();
							upBackOrderDtls.remove(fulfilOrdkey);
							coFulfils.remove(fulfilOrdkey);
							invBackOrdDescs.remove(fulfilOrdkey);
							if (simFulfilOrdMap != null) {								
								simFulfilOrdMap.remove(fulfilOrdCfmDesc.getFulfillOrderNo());
							}
						}
					}
				}
			}
			if (!updatedItems.isEmpty()) {
				updates.put("OMS_ITEMS", updatedItems);
			}
			if (simFulfilOrdMap != null && !simFulfilOrdMap.isEmpty()) {
				
				fulfilOrdCustDesc = new FulfilOrdColDesc();
				fulfilOrdCustDesc.setCollectionSize(rmsFulfilOrdMap.size());
				fulfilOrdCustDesc.getFulfilOrdDesc().addAll(simFulfilOrdMap.values());
				try {
					CreateFulfillmentOrderDetail orderDetail = new CreateFulfillmentOrderDetail();
					orderDetail.setFulfilOrdColDesc(fulfilOrdCustDesc);
					Set<String> simApiLocks = (Set<String>) updates.get("SIM_API_LOCKS");
					Iterator<String> simLockIterator = simApiLocks.iterator();
					synchronizeSIMApi(simLockIterator.next(), simLockIterator, orderDetail);
				} catch (Exception e) {
					rollBackFulfilments(tsfNoMap, rmsFulfilOrdMap.values());
					throw new BaseException("Error while calling SIM Fulfilment for the order " + custOrdHead.getCustOrderNo(), e);
				}
			}
		}
		BackOrderDAO backOrderDAO = resourceBean.getBackOrderDAO();
		Map<String, StrAdjModVo> strAdjMap = (Map<String, StrAdjModVo>) updates.get("STR_ADJUSTMENTS");
		List<WhAdjModVo> whAdjModVos = (List<WhAdjModVo>) updates.get("WH_ADJUSTMENT");

		List<WhAdjModVo> crWhAdjModVos = new ArrayList<>();
		List<StrAdjModVo> crAdjModVos = new ArrayList<>();

		if (strAdjMap != null && !strAdjMap.isEmpty()) {
			IOracleSIMClient oracleSIMClient = resourceBean.getOracleSIMClient();
			for (StrAdjModVo adjModVo : strAdjMap.values()) {
				try {
					SaveAndConfirmInventoryAdjustment adjustment = new SaveAndConfirmInventoryAdjustment();
					adjustment.setStrAdjModVo(adjModVo);
					oracleSIMClient.saveAndConfirmInventoryAdjustment(adjustment);
					crAdjModVos.add(adjModVo);
				} catch (Exception e) {
					rollBackReservation(crAdjModVos, crWhAdjModVos);
					throw new BaseException("Error while SIM stock adjustment for order number: " + custOrdHead.getCustOrderNo() +  " and location is " + adjModVo.getStoreId(), e);
				}
			}
		}

		if (whAdjModVos != null && !whAdjModVos.isEmpty()) {
			for (WhAdjModVo adjModVo : whAdjModVos) {
				try {
					backOrderDAO.callWHAdjustment(custOrdHead.getCustOrderNo(), adjModVo, availablityResource.getSystemParam("RESV_INV_STATUS"), availablityResource.getSystemParam("REASON_CODE"));
				} catch (BaseException be) {
					rollBackReservation(crAdjModVos, crWhAdjModVos);
					throw be;
				} catch (Exception e) {
					rollBackReservation(crAdjModVos, crWhAdjModVos);
					throw new BaseException("Error while WH stock adjustment for order number: " + custOrdHead.getCustOrderNo() +  " and location is " + adjModVo.getLocation());
				}
				crWhAdjModVos.add(adjModVo);
			}
		}
	
		if (invBackOrdDescs != null && !invBackOrdDescs.isEmpty()) {
			for (InvBackOrdDesc backOrdDesc : invBackOrdDescs.values()) {
				resourceBean.getInventoryBackOrderService().addInvBackOrdEntry(backOrdDesc);
			}
		}

		List<String> stTrfs = (List<String>) updates.get("ST_TRANSERS");
		if (stTrfs != null && !stTrfs.isEmpty()) {
			IOracleSIMClient oracleSIMClient = resourceBean.getOracleSIMClient();
			List<OmsUnapprovedTransfer> unapprovedTransfers = new ArrayList<>();
			try {
				/*
				 * Waiting for RIB to create transfer in SIM.
				 */
				Thread.sleep(2000);
			} catch (InterruptedException ie) {
			}
			Map<String, TransferLineItem> trfsDtlMap = backOrderDAO.getTransferDetails(custOrdHead.getCustOrderNo(), stTrfs);
			for (String fulfilOrd : trfsDtlMap.keySet()) {
				TransferLineItem transfer = trfsDtlMap.get(fulfilOrd);
				StsTsfApvModVo apvModVo = new StsTsfApvModVo();
				StsTsfApvItmMod apvItmMod = new StsTsfApvItmMod();
				FulfilOrdDesc fulfilOrdDesc = rmsFulfilOrdMap.get(fulfilOrd);

				try {
					OmsCoFulfillDetail fulfillDetail = coFulfils.get(fulfilOrd + "~" + transfer.getItem());
					fulfillDetail.setTsfNo(BigDecimal.valueOf(transfer.getTransferNo()));
					if (transfer.getId() == 0) {
						throw new SoftException("Transfer not yet created for this order.");
					}
					apvModVo.setTransferId(transfer.getId());
					apvItmMod.setApprovedQuantity(fulfilOrdDesc.getFulfilOrdDtl().get(0).getOrderQtySuom());
					apvItmMod.setLineId(transfer.getLineId());
					apvModVo.getStsTsfApvItmMod().add(apvItmMod);

					SavePendingTransferRequest request = new SavePendingTransferRequest();
					request.setStsTsfApvModVo(apvModVo);
					oracleSIMClient.savePendingTransferRequest(request);

					StsTsfRef stsTsfRef = new StsTsfRef();
					stsTsfRef.setStoreId(fulfilOrdDesc.getSourceLocId());
					stsTsfRef.setTransferId(transfer.getId());
					ApproveTransfer approveTransfer = new ApproveTransfer();
					approveTransfer.setStsTsfRef(stsTsfRef);
					oracleSIMClient.approveTransfer(approveTransfer);

					fulfillDetail.setTsfApprovalStatus("A");
				} catch (Exception e) {
					LOG.warn("Error while approving the transfer for order number " + custOrdHead.getCustOrderNo() + " and fulfil no " + fulfilOrd, e);
					OmsUnapprovedTransfer unapprovedTransfer = new OmsUnapprovedTransfer();
					unapprovedTransfer.setItem(fulfilOrdDesc.getFulfilOrdDtl().get(0).getItem());
					unapprovedTransfer.setLocation(BigDecimal.valueOf(fulfilOrdDesc.getSourceLocId()));
					unapprovedTransfer.setOmsCustOrdNo(custOrdHead.getOmsCustOrdNo());
					unapprovedTransfer.setTsfNo(BigDecimal.valueOf(transfer.getTransferNo()));
					unapprovedTransfer.setUnapprovedQty(fulfilOrdDesc.getFulfilOrdDtl().get(0).getOrderQtySuom());
					unapprovedTransfers.add(unapprovedTransfer);
				}
			}
			if (!unapprovedTransfers.isEmpty()) {
				updates.put("UN_APPD_TRFS", unapprovedTransfers);
			}
		}
	}

	private void rollBackFulfilments(Map<String, TransferCreationRequest> tsfNoMap, Collection<FulfilOrdDesc> rmsFulfils) {
		if (tsfNoMap != null && !tsfNoMap.isEmpty()) {
			try {
				resourceBean.getBackOrderDAO().rbCarreraTransfers(tsfNoMap);
			} catch (Exception e) {
				LOG.warn("Error while rollback carrera fulfilment for order numer: " + custOrdHead.getCustOrderNo(), e);
			}
		}
		if (rmsFulfils != null && !rmsFulfils.isEmpty()) {
			FulfilOrdColRef ordColRef = new FulfilOrdColRef();
			ordColRef.setCollectionSize(rmsFulfils.size());
			for (FulfilOrdDesc fulfilOrdDesc : rmsFulfils) {
				FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
				fulfilOrdRef.setCustomerOrderNo(custOrdHead.getCustOrderNo());
				fulfilOrdRef.setFulfillOrderNo(fulfilOrdDesc.getFulfillOrderNo());
				if (fulfilOrdDesc.getSourceLocType() != null) {
					fulfilOrdRef.setSourceLocId(fulfilOrdDesc.getSourceLocId());
					fulfilOrdRef.setSourceLocType(com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType.fromValue(fulfilOrdDesc.getSourceLocType().name()));
				}
				fulfilOrdRef.setFulfillLocId(fulfilOrdDesc.getFulfillLocId());
				fulfilOrdRef.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType.fromValue(fulfilOrdDesc.getFulfillLocType().name()));

				for (FulfilOrdDtl fulfilOrdDtl : fulfilOrdDesc.getFulfilOrdDtl()) {
					FulfilOrdDtlRef dtlRef = new FulfilOrdDtlRef();
					dtlRef.setItem(fulfilOrdDtl.getItem());
					dtlRef.setCancelQtySuom(fulfilOrdDtl.getOrderQtySuom());
					dtlRef.setStandardUom(fulfilOrdDtl.getStandardUom());
					dtlRef.setTransactionUom(fulfilOrdDtl.getTransactionUom());
					fulfilOrdRef.getFulfilOrdDtlRef().add(dtlRef);
				}
			}
			try {
				CancelFulfilOrdColRef colRef = new CancelFulfilOrdColRef();
				colRef.setFulfilOrdColRef(ordColRef);
				resourceBean.getOracleRMSClient().cancelFulfilOrdColRef(colRef);
			} catch (Exception e) {
				LOG.warn("Error while rollback rms fulfillment for order numer: " + custOrdHead.getCustOrderNo(), e);
			}
		}
	}

	private void rollBackReservation(List<StrAdjModVo> crAdjModVos, List<WhAdjModVo> whAdjModVos) {
		
		if (crAdjModVos != null && !crAdjModVos.isEmpty()) {
			Long unResvResnId = Long.parseLong(availablityResource.getSystemParam("INV_UNRESV_CODE"));
			for (StrAdjModVo strAdjModVo : crAdjModVos) {
				try {
					strAdjModVo.setComments("Rollback UnReserver for Customer Order No: " + custOrdHead.getCustOrderNo());
					for (StrAdjItmMod adjItmMod : strAdjModVo.getStrAdjItmMod()) {
						adjItmMod.setReasonId(unResvResnId);
					}
					SaveAndConfirmInventoryAdjustment adjustment = new SaveAndConfirmInventoryAdjustment();
					adjustment.setStrAdjModVo(strAdjModVo);
					resourceBean.getOracleSIMClient().saveAndConfirmInventoryAdjustment(adjustment);
				} catch (Exception e) {
					LOG.warn("Error while reverting the store stock adjustment for order " + custOrdHead.getCustOrderNo() + " and store id " + strAdjModVo.getStoreId() , e);
				}
			}
		}

		if (whAdjModVos != null && !whAdjModVos.isEmpty()) {
			try {
				resourceBean.getBackOrderDAO().rbWHAdjustment(custOrdHead.getCustOrderNo(), whAdjModVos, availablityResource.getSystemParam("RESV_INV_STATUS"), availablityResource.getSystemParam("REASON_CODE"));
			} catch (Exception e) {
				LOG.warn("Error while rollback WH stock adjustment for order numer: " + custOrdHead.getCustOrderNo(), e);
			}
		}
	}

	private FulfilOrdCfmCol synchronizeRMSApi(String rmsLockKey, Iterator<String> keyIterator, CreateFulfilOrdColDesc colDesc) {
		synchronized (rmsLockKey) {
			if (keyIterator.hasNext()) {
				return synchronizeRMSApi(keyIterator.next(), keyIterator, colDesc);
			} else {
				IOracleRMSClient oracleRMSClient = resourceBean.getOracleRMSClient();
				return oracleRMSClient.createFulfilOrdColDesc(colDesc).getFulfilOrdCfmCol();
			}
		}
	}

	private void synchronizeSIMApi(String simLockKey, Iterator<String> keyIterator, CreateFulfillmentOrderDetail orderDetail) {
		synchronized (simLockKey) {
			if (keyIterator.hasNext()) {
				synchronizeSIMApi(keyIterator.next(), keyIterator, orderDetail);
			} else {
				IOracleSIMClient oracleSIMClient = resourceBean.getOracleSIMClient();
				oracleSIMClient.createFulfillmentOrderDetail(orderDetail).getFulfilOrdCfmCol();
			}
		}
	}
}
