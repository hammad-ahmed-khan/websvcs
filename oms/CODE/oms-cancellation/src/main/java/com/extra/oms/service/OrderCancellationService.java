package com.extra.oms.service;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.common.model.OmsBackOrderDtl;
import com.extra.common.model.OmsCoFulfillDetail;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.common.model.OmsCustOrdItem;
import com.extra.common.model.OmsCustOrdReserve;
import com.extra.oms.model.OrderCancelDetailRequest;
import com.extra.oms.model.OrderCancelDetailResponse;
import com.extra.oms.common.BaseException;
import com.extra.oms.common.WebServiceException;
import com.extra.oms.dao.CancelOrderDAO;
import com.extra.oms.model.OmsCoCancelItem;
import com.extra.oms.model.OmsCoFoCancel;
import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.oms.model.POSOrderCancelResponse;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.LocType;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordItm;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;

@Service
public class OrderCancellationService {

	private static final Logger LOG = Logger.getLogger(OrderCancellationService.class);

	@Autowired
	private CancelOrderDAO cancelOrderDAO;

	@Autowired
	private BaseAPIService baseAPIService;

	@SuppressWarnings("unchecked")
	public POSOrderCancelResponse cancelOrder(POSOrderCancelRequest cancelRequest) throws BaseException {
		OmsCustOrdHead custOrdHead = cancelOrderDAO.validateAndGetDetail(cancelRequest);
		POSOrderCancelResponse response = validateRequest(cancelRequest, custOrdHead);
		if (response != null) {
			return response;
		}
		Long seqId = cancelOrderDAO.saveCancellationRequest(cancelRequest, custOrdHead);
		LOG.info("Cancallation seq id for the order number " + cancelRequest.getCustOrderNo() + " is " + seqId);
		response = new POSOrderCancelResponse();
		try {
			LOG.info("Calling the validation package for the sequence -> " + seqId);
			cancelOrderDAO.callCancelPackage(cancelRequest, custOrdHead, seqId);
		} catch (BaseException e) {
			LOG.error("Validation failed for the sequence -> " + seqId, e);
			response.setMessageStatus("F");
			response.setResponseMessage("FAILED");
			response.setMessageCode(e.getCode());
			response.setMessageDesc(e.getCode());
			return response;
		}
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("cancelItems", new ArrayList<OmsCoCancelItem>());
		updates.put("cancelFulfils", new ArrayList<OmsCoFoCancel>());
		updates.put("items", new ArrayList<OmsCustOrdItem>());
		updates.put("cancelOmsFulfils", new ArrayList<OmsCoFulfillDetail>());
		Map<String, String> sysParam = cancelOrderDAO.getSystemParams(Arrays.asList("OMS_INV_ADJ_RSN_CODE", "OMS_SYSTEM_OPTION"), "INV_UNRESV_CODE", "REASON_CODE", "RESV_INV_STATUS",
				"SHIPPING_CHARGE_DEPT");
		if ("R".equals(custOrdHead.getOrderCreateReserveInd()) && "P".equals(custOrdHead.getOrdPaymentStatus())) {
			LOG.info("SADDAD order cancellation and payment is pending");
			Map<BigDecimal, OmsCustOrdReserve> reservedItemMap = cancelOrderDAO.getReserveItems(cancelRequest, custOrdHead);
			cancelReservation(cancelRequest, custOrdHead, reservedItemMap, updates, sysParam);
		} else {
			LOG.info("Normal order cancellation for Ord No. ->" + cancelRequest.getCustOrderNo());
			processCancellation(cancelRequest, custOrdHead, updates, sysParam);
		}
		cancelOrderDAO.updateCancellationDetail(cancelRequest, custOrdHead, updates);
		notifyHybris(cancelRequest, custOrdHead, (List<OmsCoCancelItem>) updates.get("cancelItems"), (List<OmsBackOrderDtl>) updates.get("boUpdates"), sysParam);
		String status = cancelOrderDAO.cancelOddBooking(custOrdHead);
		cancelOrderDAO.saveJoodTransactionDetail(custOrdHead, cancelRequest.getCancellationItems());
		return prepareResponse(response, cancelRequest, custOrdHead, status);
	}

	private POSOrderCancelResponse prepareResponse(POSOrderCancelResponse response, POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, String status) {
		String successMessage = null;
		if ("S".equals(status)) {
			successMessage = "ODD Order cancelled successfully";
		} else {
			successMessage = "Order cancelled successfully";
		}
		response.setCancellationId(cancelRequest.getCancellationId());
		response.setResponseDatetimestamp(new Date());
		response.setMessageStatus("S");
		response.setResponseMessage("SUCCESS");
		for (OrderCancelDetailRequest reqItem : cancelRequest.getCancellationItems()) {
			OrderCancelDetailResponse itemResp = new OrderCancelDetailResponse();
			itemResp.setCancelQtySuom(reqItem.getCancelQtySuom());
			itemResp.setItem(reqItem.getItem());
			itemResp.setLineNo(reqItem.getLineNo());
			itemResp.setMessageCode("SUCCESS");
			itemResp.setMessageDesc(successMessage);
			response.getCustomerOrderCancelResponseItems().add(itemResp);
		}
		return response;
	}

	private POSOrderCancelResponse validateRequest(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead) {
		POSOrderCancelResponse response = new POSOrderCancelResponse();
		response.setMessageStatus("F");
		response.setCancellationId(cancelRequest.getCancellationId());
		response.setResponseDatetimestamp(new Date());
		response.setResponseMessage("FAILED");
		boolean hasError = false;
		if (custOrdHead == null) {
			LOG.error("Customer Order detail is not available for the customer order number " + cancelRequest.getCustOrderNo());
			response.setMessageCode("INVALID_ORDER_CODE");
			hasError = true;
		} else if (custOrdHead.getItemMap().isEmpty()) {
			LOG.error("Customer Order items are not available for the customer order number " + cancelRequest.getCustOrderNo());
			response.setMessageCode("NO_ITEMS_AVAILABLE");
			hasError = true;
		} else {
			Map<BigDecimal, OmsCustOrdItem> itemMap = custOrdHead.getItemMap();
			for (OrderCancelDetailRequest detail : cancelRequest.getCancellationItems()) {
				OmsCustOrdItem ordItem = itemMap.get(detail.getLineNo());
				if (ordItem == null) {
					LOG.error("Customer item is not available for the line number " + detail.getLineNo());
					OrderCancelDetailResponse detResponse = new OrderCancelDetailResponse();
					detResponse.setCancelQtySuom(detail.getCancelQtySuom());
					detResponse.setItem(detail.getItem());
					detResponse.setLineNo(detail.getLineNo());
					detResponse.setMessageCode("INVALID_LINE_NO");
					response.getCustomerOrderCancelResponseItems().add(detResponse);
					hasError = true;
					continue;
				} else if (!ordItem.getItem().equals(detail.getItem())) {
					LOG.error("Customer item number and line number mismatch. Received line number '" + detail.getLineNo() + "' and item number '" + detail.getItem() + "'");
					OrderCancelDetailResponse detResponse = new OrderCancelDetailResponse();
					detResponse.setCancelQtySuom(detail.getCancelQtySuom());
					detResponse.setItem(detail.getItem());
					detResponse.setLineNo(detail.getLineNo());
					detResponse.setMessageCode("ITEM_LINE_NO_MISMATCH");
					detResponse.setMessageDesc(String.format("Item %s and line no %s not matching with order", detail.getItem(), detail.getLineNo()));
					response.getCustomerOrderCancelResponseItems().add(detResponse);
					hasError = true;
					continue;
				}
				BigDecimal processedQty = ordItem.getCumQtyDelivered().add(ordItem.getQtyCancelled());
				if (ordItem.getQtyOrderedSuom().compareTo(processedQty) <= 0) {
					OrderCancelDetailResponse detResponse = new OrderCancelDetailResponse();
					detResponse.setCancelQtySuom(detail.getCancelQtySuom());
					detResponse.setItem(detail.getItem());
					detResponse.setLineNo(detail.getLineNo());
					detResponse.setMessageCode("ITEM_ALRDY_CLD");
					detResponse.setMessageDesc(String.format("Item %s already cancelled", ordItem.getItem()));
					response.getCustomerOrderCancelResponseItems().add(detResponse);
					hasError = true;
				} else if (detail.getCancelQtySuom().compareTo(ordItem.getQtyOrderedSuom().subtract(processedQty)) > 0) {
					OrderCancelDetailResponse detResponse = new OrderCancelDetailResponse();
					detResponse.setCancelQtySuom(detail.getCancelQtySuom());
					detResponse.setItem(detail.getItem());
					detResponse.setLineNo(detail.getLineNo());
					detResponse.setMessageCode("ITEM_QTY_UNAVB");
					detResponse.setMessageDesc(String.format("%g quantities are available for cancellation", ordItem.getQtyOrderedSuom().subtract(processedQty)));
					response.getCustomerOrderCancelResponseItems().add(detResponse);
					hasError = true;
				}
			}
		}
		return hasError ? response : null;
	}

	@SuppressWarnings("unchecked")
	private void cancelReservation(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, Map<BigDecimal, OmsCustOrdReserve> reservedItemMap, Map<String, Object> updates,
			Map<String, String> sysParam) throws BaseException {
		List<Entry<OrderCancelDetailRequest, OmsCustOrdReserve>> whRsrvs = new ArrayList<Map.Entry<OrderCancelDetailRequest, OmsCustOrdReserve>>();
		Map<Long, List<StrAdjItmMod>> strAdjMap = new HashMap<Long, List<StrAdjItmMod>>();
		updates.put("reserves", new ArrayList<OmsCustOrdReserve>());
		for (OrderCancelDetailRequest canReq : cancelRequest.getCancellationItems()) {
			OmsCustOrdReserve reserve = reservedItemMap.get(canReq.getLineNo());
			OmsCoCancelItem cancelItem = new OmsCoCancelItem();
			OmsCoFoCancel foCancel = new OmsCoFoCancel();
			if (reserve.getRmsResvQty().compareTo(canReq.getCancelQtySuom()) == -1) {
				throw new BaseException("INVALID_CANCEL_REQ_QTY");
			}
			if ("WH".equals(reserve.getRmsResvLocType())) {
				whRsrvs.add(new AbstractMap.SimpleImmutableEntry<OrderCancelDetailRequest, OmsCustOrdReserve>(canReq, reserve));
			} else if ("ST".equals(reserve.getRmsResvLocType())) {
				Long storeId = reserve.getRmsResvLoc().longValue();
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(canReq.getItem());
				strAdjItemMod.setReasonId(Integer.parseInt(sysParam.get("INV_UNRESV_CODE")));
				strAdjItemMod.setQuantity(canReq.getCancelQtySuom());
				strAdjItemMod.setCaseSize(BigDecimal.ONE);
				List<StrAdjItmMod> strAdjs = strAdjMap.get(storeId);
				if (strAdjs == null) {
					strAdjs = new ArrayList<StrAdjItmMod>();
					strAdjMap.put(storeId, strAdjs);
				}
				strAdjs.add(strAdjItemMod);
			}
			reserve.setRmsResvQty(reserve.getRmsResvQty().subtract(canReq.getCancelQtySuom()));
			reserve.setQty(reserve.getQty().subtract(canReq.getCancelQtySuom()));
			cancelItem.setCancelConfQty(canReq.getCancelQtySuom());
			cancelItem.setCancelReqQty(canReq.getCancelQtySuom());
			cancelItem.setComments(canReq.getItemComments());
			cancelItem.setItem(canReq.getItem());
			cancelItem.setLineNo(canReq.getLineNo());
			cancelItem.setOmsCancelId(cancelRequest.getOmsCancelId());

			foCancel.setFoCancelledOty(canReq.getCancelQtySuom());
			foCancel.setFulfillOrderNo(reserve.getFulfillOrderNo());
			foCancel.setItem(canReq.getItem());
			foCancel.setLineNo(canReq.getLineNo());
			foCancel.setOmsCancelId(cancelRequest.getOmsCancelId());
			foCancel.setWsResponse(null);
			foCancel.setFulfillLoc(reserve.getLoc());
			foCancel.setFulfillLocType(reserve.getLocType());
			foCancel.setSourceLoc(reserve.getRmsResvLoc());
			foCancel.setSourceLocType(reserve.getRmsResvLocType());
			cancelItem.setFulfils(Collections.singletonList(foCancel));

			OmsCustOrdItem item = custOrdHead.getItemMap().get(canReq.getLineNo());
			cancelItem.setOrdItem(item);
			item.setQtyCancelled(item.getQtyCancelled().add(canReq.getCancelQtySuom()));
			((List<OmsCustOrdItem>) updates.get("items")).add(item);

			((List<OmsCustOrdReserve>) updates.get("reserves")).add(reserve);
			((List<OmsCoFoCancel>) updates.get("cancelFulfils")).add(foCancel);
			((List<OmsCoCancelItem>) updates.get("cancelItems")).add(cancelItem);
		}
		if (!whRsrvs.isEmpty()) {
			cancelOrderDAO.cancelWHAdjusment(whRsrvs, sysParam, custOrdHead.getCustOrderNo());
		}
		for (Entry<Long, List<StrAdjItmMod>> entryStrAdj : strAdjMap.entrySet()) {
			try {
				baseAPIService.reverseInventoryAdjustment(entryStrAdj.getValue(), entryStrAdj.getKey(), custOrdHead.getCustOrderNo());
			} catch (WebServiceException wse) {
				LOG.error("Error while calling the base service to revert the inv adjustment for order no -> " + custOrdHead.getCustOrderNo(), wse);
				cancelOrderDAO.saveWSPublishReq(cancelRequest, wse.getWsSoapXML(), wse.getMessage());
			}
		}
		updates.put("rejectTender", true);
	}

	@SuppressWarnings("unchecked")
	private void processCancellation(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, Map<String, Object> updates, Map<String, String> sysParam) throws BaseException {
		Map<BigDecimal, OmsBackOrderDtl> backOrdMap = cancelOrderDAO.getBackOrderDetail(custOrdHead, cancelRequest);
		Map<BigDecimal, Set<OmsCoFulfillDetail>> fulfilDetailMap = cancelOrderDAO.getFulfilmentDetail(custOrdHead, cancelRequest);
		Map<String, Entry<BigDecimal, BigDecimal>> pickQtyMap = cancelOrderDAO.getPickedQuantities(custOrdHead.getCustOrderNo(), fulfilDetailMap.values());

		List<InvBackOrdDesc> inbBODescs = new ArrayList<InvBackOrdDesc>();
		List<FulfilOrdRef> rmsFulfilOrdRefs = new ArrayList<FulfilOrdRef>();
		List<FulfilOrdRef> simFulfilOrdRefs = new ArrayList<FulfilOrdRef>();
		updates.put("boUpdates", new ArrayList<OmsBackOrderDtl>());
		for (OrderCancelDetailRequest canReq : cancelRequest.getCancellationItems()) {
			OmsCoCancelItem cancelItem = new OmsCoCancelItem();
			OmsBackOrderDtl boOrd = backOrdMap.get(canReq.getLineNo());
			BigDecimal reqQty = canReq.getCancelQtySuom();

			cancelItem.setCancelReqQty(canReq.getCancelQtySuom());
			cancelItem.setComments(canReq.getItemComments());
			cancelItem.setItem(canReq.getItem());
			cancelItem.setLineNo(canReq.getLineNo());
			cancelItem.setOmsCancelId(cancelRequest.getOmsCancelId());

			OmsCustOrdItem item = custOrdHead.getItemMap().get(canReq.getLineNo());
			cancelItem.setOrdItem(item);
			cancelItem.setFulfils(new ArrayList<OmsCoFoCancel>());
			if (!(sysParam.get("SHIPPING_CHARGE_DEPT").equals(item.getItemDept().toString()) || item.getInvInd().equals("N"))) {
				if (boOrd != null) {
					BigDecimal boPendingQty = boOrd.getSourceQty().subtract(boOrd.getFulfillQty());
					BigDecimal canQty = reqQty.min(boPendingQty);
					if (canQty.intValue() > 0) {
						LOG.info("Inventory back order adjustment quantity" + canQty);
						InvBackOrdDesc boDesc = new InvBackOrdDesc();
						boDesc.setItem(canReq.getItem());
						boDesc.setBackorderQty(canQty.negate());
						if ("ST".equals(boOrd.getSourceLocType())) {
							boDesc.setLocType(LocType.S);
							boDesc.setLocation(boOrd.getSourceLoc().longValue());
						} else {
							boDesc.setLocType(LocType.W);
							Long location = boOrd.getSourceLoc().longValue();
							boDesc.setLocation(location / 10);
							boDesc.setChannelId(location.intValue() % 10);
						}
						boDesc.setUnitOfMeasure("EA");
						inbBODescs.add(boDesc);
						boOrd.setSourceQty(boOrd.getSourceQty().subtract(canQty));
						boOrd.setCancelledQty(canQty);
						if (boOrd.getSourceQty().equals(boOrd.getFulfillQty())) {
							boOrd.setBackorderStatus("S");
						}
						((List<OmsBackOrderDtl>) updates.get("boUpdates")).add(boOrd);
						reqQty = reqQty.subtract(canQty);
						cancelItem.setCancelConfQty(canQty);
					}
				}
				if (reqQty.intValue() > 0) {
					for (OmsCoFulfillDetail fulfil : fulfilDetailMap.get(canReq.getLineNo())) {
						BigDecimal fulfilPendingQty = fulfil.getFulfillConfQty().subtract(fulfil.getFulfillDeliverQty()).subtract(fulfil.getFulfillCancelQty());
						BigDecimal canQty = reqQty.min(fulfilPendingQty);
						BigDecimal fulfilCancelQty = canQty;
						if (canQty.intValue() > 0) {
							LOG.info("Cancelling the fulfilemnt quantity " + canQty);
							OmsCoFoCancel foCancel = new OmsCoFoCancel();

							foCancel.setFulfillOrderNo(fulfil.getFulfillOrderNo());
							foCancel.setItem(canReq.getItem());
							foCancel.setLineNo(canReq.getLineNo());
							foCancel.setOmsCancelId(cancelRequest.getOmsCancelId());
							if (SourceLocType.WH.equals(SourceLocType.valueOf(fulfil.getSourceLocType())) && fulfil.getFulfillLocType().equals("W")
									&& !fulfil.getSourceLoc().equals(fulfil.getFulfillLoc()) || (String.valueOf(cancelRequest.getCancellationRequestorId()).startsWith("8") && SourceLocType.WH.equals(SourceLocType.valueOf(fulfil.getSourceLocType())) && fulfil.getFulfillLocType().equals("S"))) {
								cancelOrderDAO.cancelCareraFulfilment(fulfil, canQty);
								foCancel.setFoCancelledOty(canQty);
								fulfil.setFulfillCancelQty(fulfil.getFulfillCancelQty().add(canQty));
								((List<OmsCoFulfillDetail>) updates.get("cancelOmsFulfils")).add(fulfil);
							} else {
								BigDecimal pendingQty = BigDecimal.ZERO;
								Entry<BigDecimal, BigDecimal> pickEntry = null;
								if (SourceLocType.ST.equals(SourceLocType.valueOf(fulfil.getSourceLocType())) && fulfil.getSourceLoc().equals(fulfil.getFulfillLoc())) {
									pickEntry = pickQtyMap.get(fulfil.getItem() + "~" + fulfil.getFulfillOrderNo());
								} else if (fulfil.getTsfNo() != null) {
									pickEntry = pickQtyMap.get(fulfil.getTsfNo().toString());
								}
								if (pickEntry != null) {
									pendingQty = pickEntry.getKey().subtract(pickEntry.getValue()).subtract(canQty);
									if (pendingQty.intValue() < 0) {
										if (custOrdHead.getDeliveryType().equals("S")
												&& ((SourceLocType.ST.equals(SourceLocType.valueOf(fulfil.getSourceLocType())) && fulfil.getSourceLoc().equals(fulfil.getFulfillLoc()))
														|| (SourceLocType.WH.equals(SourceLocType.valueOf(fulfil.getSourceLocType()))
																&& FulfillLocType.V.equals(FulfillLocType.valueOf(fulfil.getFulfillLocType()))))) {
											throw new BaseException("ITEM_SHIPPED_CANNOT_CANCEL");
										} else {
											fulfilCancelQty = canQty.add(pendingQty);
										}
									}
								}
								LOG.info("fulfilCancelQty ---- " + fulfilCancelQty);

								foCancel.setFoCancelledOty(fulfilCancelQty);
								fulfil.setFulfillCancelQty(fulfil.getFulfillCancelQty().add(canQty));
								((List<OmsCoFulfillDetail>) updates.get("cancelOmsFulfils")).add(fulfil);
								if (fulfilCancelQty.intValue() > 0) {
									rmsFulfilOrdRefs.add(getRMSFulfilOrdRef(custOrdHead.getCustOrderNo(), fulfilCancelQty, fulfil));
								}
								if (SourceLocType.ST.equals(SourceLocType.valueOf(fulfil.getSourceLocType())) && fulfil.getSourceLoc().equals(fulfil.getFulfillLoc())) {
									if (custOrdHead.getDeliveryType().equals("C") && pendingQty.intValue() < 0) {
										validateAndReversePickup(fulfil, custOrdHead, canQty);
									}
									if (fulfilCancelQty.intValue() > 0) {
										simFulfilOrdRefs.add(getSIMFulfilOrdRef(custOrdHead.getCustOrderNo(), fulfilCancelQty, fulfil));
									}
								}
							}
							LOG.info("canQty ---- " + canQty);

							reqQty = reqQty.subtract(canQty);
							((List<OmsCoFoCancel>) updates.get("cancelFulfils")).add(foCancel);
							cancelItem.getFulfils().add(foCancel);
							cancelItem.setCancelConfQty(cancelItem.getCancelConfQty().add(canQty));
							foCancel.setWsResponse("C");
							foCancel.setFulfillLoc(fulfil.getFulfillLoc());
							foCancel.setFulfillLocType(fulfil.getFulfillLocType());
							foCancel.setSourceLoc(fulfil.getSourceLoc());
							foCancel.setSourceLocType(fulfil.getSourceLocType());
							LOG.info("loc ---- " + fulfil.getSourceLocType());

							if (SourceLocType.SU.equals(SourceLocType.valueOf(fulfil.getSourceLocType()))) {
								LOG.info("loc ---- " + fulfil.getSourceLocType());
								updates.put("isPOOrder", true);
							}
							if (reqQty.intValue() < 1) {
								break;
							}
						}
					}
				}
			} else {
				/*
				 * Confirm quantity for non inventory item.
				 */
				cancelItem.setCancelConfQty(reqQty);
			}
			item.setQtyCancelled(item.getQtyCancelled().add(canReq.getCancelQtySuom()));
			((List<OmsCustOrdItem>) updates.get("items")).add(item);
			((List<OmsCoCancelItem>) updates.get("cancelItems")).add(cancelItem);
		}
		if (!inbBODescs.isEmpty()) {
			try {
				baseAPIService.adjustInventoryBackOrder(inbBODescs);
			} catch (WebServiceException wse) {
				LOG.error("Error while calling the base service to revert the back order inv adjustment for order no -> " + custOrdHead.getCustOrderNo(), wse);
				cancelOrderDAO.saveWSPublishReq(cancelRequest, wse.getWsSoapXML(), wse.getMessage());
			}
		}
		if (!rmsFulfilOrdRefs.isEmpty()) {
			try {
				baseAPIService.cancelRMSFulfilment(rmsFulfilOrdRefs);
			} catch (WebServiceException wse) {
				LOG.error("Error while calling the base service to cancel the RMS fulfilment for order no -> " + custOrdHead.getCustOrderNo(), wse);
				cancelOrderDAO.saveWSPublishReq(cancelRequest, wse.getWsSoapXML(), wse.getMessage());
			}
		}
		if (!simFulfilOrdRefs.isEmpty()) {
			try {
				baseAPIService.cancelSIMFulfilment(simFulfilOrdRefs);
			} catch (WebServiceException wse) {
				LOG.error("Error while calling the base service to cancel the SIM fulfilment for order no -> " + custOrdHead.getCustOrderNo(), wse);
				cancelOrderDAO.saveWSPublishReq(cancelRequest, wse.getWsSoapXML(), wse.getMessage());
			}
		}
	}

	private FulfilOrdRef getSIMFulfilOrdRef(String custOrderNo, BigDecimal canQty, OmsCoFulfillDetail fulfilDetail) {
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(custOrderNo);
		fulfilOrdRef.setFulfillOrderNo(fulfilDetail.getFulfillOrderNo().toString());
		if (!fulfilDetail.getFulfillLoc().equals(fulfilDetail.getSourceLoc())) {
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue());
			fulfilOrdRef.setSourceLocType(SourceLocType.valueOf(fulfilDetail.getSourceLocType()));
		}
		fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.valueOf(fulfilDetail.getFulfillLocType()));

		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(canQty);
		fulfilOrdDtlRef.setItem(fulfilDetail.getItem());
		fulfilOrdDtlRef.setStandardUom("EA");
		fulfilOrdDtlRef.setTransactionUom("EA");

		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		return fulfilOrdRef;
	}

	private FulfilOrdRef getRMSFulfilOrdRef(String custOrderNo, BigDecimal canQty, OmsCoFulfillDetail fulfilDetail) {
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();

		fulfilOrdRef.setCustomerOrderNo(custOrderNo);
		fulfilOrdRef.setFulfillOrderNo(fulfilDetail.getFulfillOrderNo().toString());
		if (!fulfilDetail.getFulfillLoc().equals(fulfilDetail.getSourceLoc())) {
			fulfilOrdRef.setSourceLocId(fulfilDetail.getSourceLoc().longValue());
			fulfilOrdRef.setSourceLocType(SourceLocType.valueOf(fulfilDetail.getSourceLocType()));
		}
		fulfilOrdRef.setFulfillLocId(fulfilDetail.getFulfillLoc().longValue());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.valueOf(fulfilDetail.getFulfillLocType()));

		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(canQty);
		fulfilOrdDtlRef.setItem(fulfilDetail.getItem());
		fulfilOrdDtlRef.setStandardUom("EA");
		fulfilOrdDtlRef.setTransactionUom("EA");

		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		return fulfilOrdRef;
	}

	private void validateAndReversePickup(OmsCoFulfillDetail fulfil, OmsCustOrdHead custOrdHead, BigDecimal canReqQty) throws BaseException {
		StrFordHdrColDesc hdrColDesc = baseAPIService.readSIMFulfilmentHeader(fulfil, custOrdHead.getCustOrderNo());
		StrFordItm strFordItm = null;
		if (hdrColDesc != null) {
			strFordItm = baseAPIService.readSIMFulfilmentDetail(fulfil, hdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
			BigDecimal onlyPickQty = strFordItm.getPickedQty().subtract(strFordItm.getDeliveredQty());
			BigDecimal onlyRsrvQty = strFordItm.getReservedQty().subtract(onlyPickQty);
			BigDecimal cancelPickQty = canReqQty.subtract(onlyRsrvQty);
			if (cancelPickQty.intValue() > 0) {
				baseAPIService.cancelDeliveryDraft(hdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId());
				baseAPIService.reversePickCFSOrder(strFordItm.getLineId(), hdrColDesc.getStrFordHdrDesc().get(0).getIntFulfillmentOrderId(), cancelPickQty);
			}
		}
	}

	private void notifyHybris(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, List<OmsCoCancelItem> items, List<OmsBackOrderDtl> backOrders, Map<String, String> sysParam)
			throws BaseException {
		if (!"HybrisCancellation".equals(cancelRequest.getComments())) {
			if ("S".equals(custOrdHead.getOrdPaymentStatus()) && cancelRequest.getRefundPreference().equals("CLEARING") && BigDecimal.valueOf(19008).compareTo(custOrdHead.getOrderRequestorId()) != 0
					&& cancelRequest.getCancellationRequestorId().startsWith("1")) {
				cancelOrderDAO.saveEInvoicingReq(cancelRequest);
			}
			String wsMessage = baseAPIService.createSOAPMessageSiebelNotification(cancelRequest, custOrdHead, items, backOrders, sysParam);
			if (wsMessage != null) {
				cancelOrderDAO.saveWSPublishReq(cancelRequest, wsMessage, "NO_ERROR");
			}
		}
	}
}
