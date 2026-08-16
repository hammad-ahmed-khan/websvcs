package com.extra.oms.spareParts.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extra.oms.service.client.IOracleSIMClient;
import com.extra.oms.spareParts.dao.SparePartsDAO;
import com.extra.oms.spareParts.model.CancelRequest;
import com.extra.oms.spareParts.model.CancelResponse;
import com.extra.oms.spareParts.model.Items;
import com.extra.oms.spareParts.model.StockDeductionRequest;
import com.extra.oms.spareParts.model.StockDeductionResponse;
import com.extra.oms.spareParts.model.StockRecDetails;
import com.extra.oms.spareParts.model.StockRequest;
import com.extra.oms.spareParts.model.StockResponse;
import com.extra.oms.spareParts.model.TransferRecieveRequest;
import com.extra.oms.spareParts.model.TransferRecieveResponse;
import com.extra.oms.spareParts.util.UtillConstantCodes;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustmentResponse;

@Service
public class SparePartsService {

	private static final Logger log = LogManager.getLogger(SparePartsService.class);

	@Autowired
	SparePartsDAO sparePartsDAO;

	@Autowired
	IOracleSIMClient iOracleSIMClient;

	public StockResponse processRequest(StockRequest request) {
		String message = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		Map<String, BigDecimal> itemRequestQty = new HashMap<>();
		Map<String, BigDecimal> unApprovedItemQty = new HashMap<>();
		Map<String, BigDecimal> itemStockQty = new HashMap<>();
		StockResponse response = new StockResponse();
		String type = "";
		String requestJson = null;
		int seqNo = 0;
		try {
			requestJson = objectMapper.writeValueAsString(request);
			if ("Stock Request".equals(request.getType())) {
				log.info("Processing Stock Request-" + request.getTsfReqSeqId());
				type = "StockReq";
				seqNo = sparePartsDAO.insertRequestLog(request.getSrvReqId(), request.getTsfReqSeqId(), requestJson, type);
				itemStockQty = sparePartsDAO.stockCheckForRequest(request, itemRequestQty);
			} else {
				log.info("Processing Return Request-" + request.getTsfReqSeqId());
				type = "ReturnReq";
				seqNo = sparePartsDAO.insertRequestLog(request.getSrvReqId(), request.getTsfReqSeqId(), requestJson, type);
				itemStockQty = sparePartsDAO.stockCheckForReturn(request, itemRequestQty);
				unApprovedItemQty = sparePartsDAO.getUnapprovedQtyForReturn(request, unApprovedItemQty);
			}

			if (itemStockQty != null && !itemStockQty.isEmpty()) {
				message = compareQty(request, itemStockQty, itemRequestQty, unApprovedItemQty);
				log.info("Status- " + message);
				message = sparePartsDAO.persistStockRequest(request, itemStockQty, message);
				response.setSuccess(UtillConstantCodes.SuccessTrue);
				response.setCode(UtillConstantCodes.SuccessCode);
				response.setTsfReqSeqId(request.getTsfReqSeqId());
				response.setMessage(message);
			} else {
				message = UtillConstantCodes.NoRecordFoundMessage;
				log.info("Status- " + message);
				response.setSuccess(UtillConstantCodes.SuccessFalse);
				response.setCode(UtillConstantCodes.failedCode);
				response.setTsfReqSeqId(request.getTsfReqSeqId());
				response.setMessage(message);
			}

		} catch (Exception e) {
			log.error("Failed processing StockRequest", e);
			response.setSuccess(UtillConstantCodes.SuccessFalse);
			response.setCode(UtillConstantCodes.failedCode);
			response.setTsfReqSeqId(request.getTsfReqSeqId());
			response.setMessage(UtillConstantCodes.failedStatus);
		} finally {
			try {
				String responseJson = objectMapper.writeValueAsString(response);
				sparePartsDAO.updateResponseLog(request.getSrvReqId(), request.getTsfReqSeqId(), responseJson, type, seqNo);
			} catch (Exception e) {
				log.error("Error- ", e);
			}
		}
		return response;
	}

	public TransferRecieveResponse processReceiving(TransferRecieveRequest request) {
		log.info("Processing Stock Recieving-" + request.getTsfRecSeqId());
		String checkRecStatus = null;
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String type = "StockRec";
		TransferRecieveResponse response = new TransferRecieveResponse();
		String status = "S";
		String requestJson = null;
		String mainStatus = null;
		int seqNo = 0;
		try {
			requestJson = objectMapper.writeValueAsString(request);
			seqNo = sparePartsDAO.insertRequestLog(request.getSrvReqId(), request.getTsfRecSeqId(), requestJson, type);
			checkRecStatus = sparePartsDAO.checkRecStatus(request.getTsfRecSeqId());
			log.info("Fetched Receive status -" + checkRecStatus);
			if (checkRecStatus == null) {
				sparePartsDAO.insertRequest(request.getTsfRecSeqId(), request.getSrvReqId());
			}
			if (!status.equals(checkRecStatus)) {

				StockRecDetails details = sparePartsDAO.fetchRecItemDetails(request.getTsfRecSeqId());
				callReceiveInventoryAdjustment(details);
				mainStatus = "R";
				response.setSuccess(UtillConstantCodes.SuccessTrue);
				response.setCode(UtillConstantCodes.SuccessCode);
				response.setMessage(UtillConstantCodes.SuccessStatus);

			} else {
				response.setSuccess(UtillConstantCodes.SuccessTrue);
				response.setCode(UtillConstantCodes.SuccessCode);
				response.setMessage(UtillConstantCodes.SuccessStatus);
			}
		} catch (Exception e) {
			log.error("Error-", e);
			response.setSuccess(UtillConstantCodes.SuccessFalse);
			response.setCode(UtillConstantCodes.failedCode);
			response.setMessage(UtillConstantCodes.failedStatus);
		} finally {
			log.info("status -" + mainStatus);
			if (mainStatus != null) {
				sparePartsDAO.updateStockReqStatus(request.getTsfRecSeqId(), mainStatus, status);
			}
			try {
				String responseJson = objectMapper.writeValueAsString(response);
				sparePartsDAO.updateResponseLog(request.getSrvReqId(), request.getTsfRecSeqId(), responseJson, type, seqNo);
			} catch (Exception e) {
				log.error("Error- ", e);
			}
		}
		return response;
	}

	public StockDeductionResponse stockDeducting(StockDeductionRequest request) {
		log.info("Processing Stock Deduction-" + request.getSrSeqId());
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String type = "StockDed";
		String message = null;
		String status = null;
		Map<String, BigDecimal> itemMap = new HashMap<>();
		String UpdateMessage = null;
		int row = 0;
		StockDeductionResponse response = new StockDeductionResponse();
		String requestJson = null;
		int seqNo = 0;

		try {
			requestJson = objectMapper.writeValueAsString(request);
			seqNo = sparePartsDAO.insertRequestLog(request.getSrvReqId(), request.getSrSeqId(), requestJson, type);
			if (request.getSrvReqId() != null) {
				message = sparePartsDAO.checkMessage(request);
				row = sparePartsDAO.checkRow(request);
				log.info("Message-" + message + "  Existing records-" + row);
			}
			if (!UtillConstantCodes.DeductSuccessMessage.equals(message) || request.getSrvReqId() == null) {
				sparePartsDAO.persistIntoHeadAndItem(request);
				sparePartsDAO.checkStock(request, itemMap);
				message = compareQty(request);
				if ("S".equals(message)) {
					callDeductionInventoryAdjustment(request);
					status = "C";
					UpdateMessage = UtillConstantCodes.DeductSuccessMessage;
					response.setSrSeqId(request.getSrSeqId());
					response.setSrvReqId(request.getSrvReqId());
					response.setOmsServId(request.getOmsServId());
					response.setSuccess(UtillConstantCodes.SuccessTrue);
					response.setCode(UtillConstantCodes.SuccessCode);
					response.setMessage(UtillConstantCodes.DeductSuccessMessage);
				} else {
					UpdateMessage = UtillConstantCodes.DeductStockMessage;
					response.setSrSeqId(request.getSrSeqId());
					response.setSrvReqId(request.getSrvReqId());
					response.setOmsServId(request.getOmsServId());
					response.setSuccess(UtillConstantCodes.SuccessFalse);
					response.setCode(UtillConstantCodes.failedCode);
					response.setMessage(UtillConstantCodes.DeductStockMessage);
				}

			} else {
				response.setSrSeqId(request.getSrSeqId());
				response.setSrvReqId(request.getSrvReqId());
				response.setSuccess(UtillConstantCodes.SuccessTrue);
				response.setCode(UtillConstantCodes.SuccessCode);
				response.setMessage(UtillConstantCodes.SuccessStatus);
			}
		} catch (Exception e) {
			log.error("Error-", e);
			UpdateMessage = UtillConstantCodes.DeductFailMessage;
			response.setSrSeqId(request.getSrSeqId());
			response.setSrvReqId(request.getSrvReqId());
			response.setSuccess(UtillConstantCodes.SuccessFalse);
			response.setCode(UtillConstantCodes.failedCode);
			response.setMessage(UtillConstantCodes.failedStatus);
		} finally {
			sparePartsDAO.updateCommentsAndStatus(request, UpdateMessage, row, status);
			try {
				String responseJson = objectMapper.writeValueAsString(response);
				sparePartsDAO.updateResponseLog(request.getSrvReqId(), request.getSrSeqId(), responseJson, type, seqNo);
			} catch (Exception e) {
				log.error("Error- ", e);
			}
		}
		return response;
	}

	public CancelResponse stockCancellation(CancelRequest request) throws Exception {
		log.info("Processing Stock Cancellation -" + request.getSrSeqId());
		CancelResponse response = new CancelResponse();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String type = "StockCan";
		String status = null;
		String cancelStatus = "S";
		String message = null;
		List<Items> items = new ArrayList<>();
		String requestJson = null;
		int seqNo = 0;
		try {
			requestJson = objectMapper.writeValueAsString(request);
			seqNo = sparePartsDAO.insertRequestLog(request.getSrvReqId(), request.getSrSeqId(), requestJson, type);
			status = sparePartsDAO.checkCancelStatus(request.getSrSeqId(), status);
			items = sparePartsDAO.fetchItems(request.getSrSeqId(), request.getSrvReqId());
			log.info("status-" + status);
			if ("P".equals(status)) {
				log.info("If status is P");
				try {
					callCancelInventoryAdjustment(items, request);
					status = "X";
					log.info("Status-" + status);
				} catch (Exception e) {
					cancelStatus = "F";
					log.info("Status-" + status + "cancelStatus-" + cancelStatus);
					throw e;
				} finally {
					sparePartsDAO.insertCancelDetails(request, cancelStatus, status);
					sparePartsDAO.updateCancelStatus(request, status);
				}
			} else if ("C".equals(status) || status == null) {
				log.info("If status is C");
				try {
					callCancelReturnInventoryAdjustment(request, items);
					message = UtillConstantCodes.CancelReturnMessage;
					if ("C".equals(status) || status == null) {
						status = "X";
						log.info("Status-" + status);
					}
				} catch (Exception e) {
					cancelStatus = "F";
					log.info("Status-" + status + "cancelStatus-" + cancelStatus);
					throw e;
				} finally {
					sparePartsDAO.insertCancelDetails(request, cancelStatus, status);
					sparePartsDAO.updateCancelStatus(request, status);
				}
			} else if ("N".equals(status)) {
				log.info("If status is N");
				status = "Z";
				log.info("Status-" + status);
				sparePartsDAO.insertCancelDetails(request, cancelStatus, status);
				sparePartsDAO.updateCancelStatus(request, status);

			} else if ("R".equals(status)) {
				log.info("If status is R");
				callInventoryAdjustment(items, request);
				status = "X";
				log.info("Status-" + status);
				sparePartsDAO.updateCancelStatus(request, status);
			} else {
				if ("X".equals(status) || "Z".equals(status)) {
					message = UtillConstantCodes.CancelMessage + status;
				} else {
					message = UtillConstantCodes.NotCancelled + status;
				}
			}
			response.setSrSeqId(request.getSrSeqId());
			response.setSrvReqId(request.getSrvReqId());
			response.setSuccess(UtillConstantCodes.SuccessTrue);
			response.setCode(UtillConstantCodes.SuccessCode);
			if (message != null) {
				response.setMessage(message);
			} else {
				response.setMessage(UtillConstantCodes.CancelSuccess);
			}
		} catch (Exception e) {
			log.info("ERROR--", e);
			response.setSrSeqId(request.getSrSeqId());
			response.setSrvReqId(request.getSrvReqId());
			response.setSuccess(UtillConstantCodes.SuccessFalse);
			response.setCode(UtillConstantCodes.failedCode);
			response.setMessage(UtillConstantCodes.failedStatus);
		}
		try {
			String responseJson = objectMapper.writeValueAsString(response);
			sparePartsDAO.updateResponseLog(request.getSrvReqId(), request.getSrSeqId(), responseJson, type, seqNo);
		} catch (Exception e) {
			log.error("Error- ", e);
		}
		return response;
	}

	private void callCancelReturnInventoryAdjustment(CancelRequest request, List<Items> items) {
		log.info("Started calling Inventory Adjustment webServices to move inventory for cancel");
		Long firstReasonCode = sparePartsDAO.fetchFirstReasonCode(request.getSrSeqId(), request.getSrvReqId());
		if (firstReasonCode > 0) {
			try {
				SaveAndConfirmInventoryAdjustment inventoryAdjustment = new SaveAndConfirmInventoryAdjustment();
				SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse1 = null;
				StrAdjModVo StrAdjModVo = new StrAdjModVo();
				for (Items item : items) {
					StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
					strAdjItemMod.setItemId(item.getItem());
					strAdjItemMod.setReasonId(firstReasonCode.longValue());
					strAdjItemMod.setQuantity(item.getQty());
					strAdjItemMod.setCaseSize(new BigDecimal(1));
					StrAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
				}
				StrAdjModVo.setStoreId(items.get(0).getReqLoc().longValue());
				StrAdjModVo.setComments(request.getSrvReqId());
				inventoryAdjustment.setStrAdjModVo(StrAdjModVo);
				inventoryAdjustmentResponse1 = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment);
				log.info("Success calling adjustment webService to move inventory return with first Reason code.");
			} catch (Exception e) {
				log.error("Error while calling inventoryAdjustment webServices while Cancelling with first reason code", e);
				throw e;
			}

			try {
				SaveAndConfirmInventoryAdjustment inventoryAdjustment2 = new SaveAndConfirmInventoryAdjustment();
				SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse2 = null;
				StrAdjModVo StrAdjModVo2 = new StrAdjModVo();
				for (Items item : items) {
					StrAdjItmMod strAdjItemMod2 = new StrAdjItmMod();
					strAdjItemMod2.setItemId(item.getItem());
					Long reasonCode = item.getAvailToTechReasonCode();
					strAdjItemMod2.setReasonId(reasonCode);
					strAdjItemMod2.setQuantity(item.getQty());
					strAdjItemMod2.setCaseSize(new BigDecimal(1));
					StrAdjModVo2.getStrAdjItmMod().add(strAdjItemMod2);
				}
				StrAdjModVo2.setStoreId(items.get(0).getReqLoc().longValue());
				StrAdjModVo2.setComments(request.getSrvReqId());
				inventoryAdjustment2.setStrAdjModVo(StrAdjModVo2);
				inventoryAdjustmentResponse2 = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment2);
				log.info("Success calling adjustment webService to move inventory for return.");
			} catch (Exception e) {
				log.error("Error while calling inventoryAdjustment webServices while Cancelling with second reason code", e);
				throw e;
			}
		} else {
			log.info("Inventory adjustment api is not called due to the first reason code -" + firstReasonCode);
		}
	}

	public void callReceiveInventoryAdjustment(StockRecDetails recDetails) {
		log.info("Started calling Inventory Adjustment webServices to move inventory from non sellable to available");
		Long adjustmentId1 = null;
		Long adjustmentId2 = null;
		long firstReasonID = 284;
		try {

			SaveAndConfirmInventoryAdjustment inventoryAdjustment = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse = null;
			StrAdjModVo StrAdjModVo = new StrAdjModVo();
			for (Items items : recDetails.getItems()) {
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(items.getItem());
				strAdjItemMod.setReasonId(firstReasonID);
				strAdjItemMod.setQuantity(items.getQty());
				strAdjItemMod.setCaseSize(new BigDecimal(1));
				StrAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			}
			StrAdjModVo.setStoreId(recDetails.getReqLoc().longValue());
			StrAdjModVo.setComments(recDetails.getSrvReqId());
			inventoryAdjustment.setStrAdjModVo(StrAdjModVo);
			inventoryAdjustmentResponse = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment);
			adjustmentId1 = inventoryAdjustmentResponse.getStrAdjRef().getAdjustmentId();
			log.info("Success calling adjustment webService to move inventory from non sellable to available.");

			log.info("Started calling Inventory Adjustment webServices to move inventory from Available to Technician Sub bucket.");
			SaveAndConfirmInventoryAdjustment inventoryAdjustment2 = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse2 = null;
			StrAdjModVo StrAdjModVo2 = new StrAdjModVo();
			Long reason_code = sparePartsDAO.getTechReasonCode(recDetails.getTsfRecSeqId(), recDetails.getReqTechId());
			for (Items items : recDetails.getItems()) {
				StrAdjItmMod strAdjItemMod2 = new StrAdjItmMod();
				strAdjItemMod2.setItemId(items.getItem());
				strAdjItemMod2.setReasonId(reason_code);
				strAdjItemMod2.setQuantity(items.getQty());
				strAdjItemMod2.setCaseSize(new BigDecimal(1));
				StrAdjModVo2.getStrAdjItmMod().add(strAdjItemMod2);
			}
			StrAdjModVo2.setStoreId(recDetails.getReqLoc().longValue());
			StrAdjModVo2.setComments(recDetails.getSrvReqId());
			inventoryAdjustment2.setStrAdjModVo(StrAdjModVo2);
			inventoryAdjustmentResponse2 = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment2);
			adjustmentId2 = inventoryAdjustmentResponse2.getStrAdjRef().getAdjustmentId();
			log.info("Success calling adjustment webService to move inventory from Available to Technician Sub bucket.");
		} catch (Exception e) {
			log.error("Error while calling inventoryAdjustment webServices - Transfer receive", e);
			throw e;
		}
	}

	public void callDeductionInventoryAdjustment(StockDeductionRequest request) {
		log.info("Started calling Inventory Adjustment webServices");
		Long serviceId = null;
		log.info("Deducting from technician bucket ");

		try {
			SaveAndConfirmInventoryAdjustment inventoryAdjustment = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse = null;
			StrAdjModVo StrAdjModVo = new StrAdjModVo();
			for (Items item : request.getItems()) {
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(item.getItem());
				strAdjItemMod.setReasonId(item.getOrigReasonCode());
				strAdjItemMod.setQuantity(item.getQty());
				strAdjItemMod.setCaseSize(new BigDecimal(1));
				StrAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			}
			StrAdjModVo.setStoreId(request.getReqLoc().longValue());
			StrAdjModVo.setComments(request.getSrvReqId());
			inventoryAdjustment.setStrAdjModVo(StrAdjModVo);
			inventoryAdjustmentResponse = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment);
			log.info("Success calling Inventory Adjustment webService");
		} catch (Exception e) {
			log.error("Error while calling inventoryAdjustment webServices - Deducting From ", e);
			throw e;
		}

		try {
			log.info("Deducting from original bucket - " + request.getReasonCode());
			SaveAndConfirmInventoryAdjustment inventoryAdjustment2 = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse2 = null;
			StrAdjModVo StrAdjModVo2 = new StrAdjModVo();
			for (Items item : request.getItems()) {
				StrAdjItmMod strAdjItemMod2 = new StrAdjItmMod();
				strAdjItemMod2.setItemId(item.getItem());
				strAdjItemMod2.setReasonId(request.getReasonCode());
				strAdjItemMod2.setQuantity(item.getQty());
				strAdjItemMod2.setCaseSize(new BigDecimal(1));
				StrAdjModVo2.getStrAdjItmMod().add(strAdjItemMod2);
			}
			StrAdjModVo2.setStoreId(request.getReqLoc().longValue());
			StrAdjModVo2.setComments(request.getSrvReqId());
			inventoryAdjustment2.setStrAdjModVo(StrAdjModVo2);
			inventoryAdjustmentResponse2 = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment2);
			serviceId = inventoryAdjustmentResponse2.getStrAdjRef().getAdjustmentId();
			request.setOmsServId(serviceId);
		} catch (Exception e) {
			log.error("Error while calling inventoryAdjustment webServices - Deducting From ", e);
			throw e;
		}
	}

	public void callCancelInventoryAdjustment(List<Items> items, CancelRequest request) {
		log.info("Calling Inventory Adjustment webservice moving inventory from Available to Technician Sub bucket.");

		try {
			SaveAndConfirmInventoryAdjustment inventoryAdjustment = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse = null;
			StrAdjModVo StrAdjModVo = new StrAdjModVo();
			for (Items item : items) {
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(item.getItem());
				strAdjItemMod.setReasonId(284);
				strAdjItemMod.setQuantity(item.getQty());
				strAdjItemMod.setCaseSize(new BigDecimal(1));
				StrAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			}
			StrAdjModVo.setStoreId(items.get(0).getReqLoc().longValue());
			StrAdjModVo.setComments(request.getSrvReqId());
			inventoryAdjustment.setStrAdjModVo(StrAdjModVo);
			inventoryAdjustmentResponse = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment);
			log.info("Success Calling Inventory Adjustment webservice moving inventory from Available to Technician Sub bucket.");
		} catch (Exception e) {
			log.error("Error while calling inventoryAdjustment webServices - Deducting From Tech Bucket", e);
			throw e;
		}
	}

	public void callInventoryAdjustment(List<Items> items, CancelRequest request) {
		log.info("Calling Inventory Adjustment webservice moving inventory from Available to Technician Sub bucket.");

		try {
			SaveAndConfirmInventoryAdjustment inventoryAdjustment = new SaveAndConfirmInventoryAdjustment();
			SaveAndConfirmInventoryAdjustmentResponse inventoryAdjustmentResponse = null;
			StrAdjModVo StrAdjModVo = new StrAdjModVo();
			for (Items item : items) {
				StrAdjItmMod strAdjItemMod = new StrAdjItmMod();
				strAdjItemMod.setItemId(item.getItem());
				strAdjItemMod.setReasonId(item.getOrigReasonCode());
				strAdjItemMod.setQuantity(item.getQty());
				strAdjItemMod.setCaseSize(new BigDecimal(1));
				StrAdjModVo.getStrAdjItmMod().add(strAdjItemMod);
			}
			StrAdjModVo.setStoreId(items.get(0).getReqLoc().longValue());
			StrAdjModVo.setComments(request.getSrvReqId());
			inventoryAdjustment.setStrAdjModVo(StrAdjModVo);
			inventoryAdjustmentResponse = iOracleSIMClient.saveAndConfirmInventoryAdjustment(inventoryAdjustment);
			log.info("Success Calling Inventory Adjustment webservice moving inventory from Available to Technician Sub bucket.");
		} catch (Exception e) {
			log.error("Error while calling inventoryAdjustment webServices - Deducting From Tech Bucket", e);
			throw e;
		}
	}

	public String compareQty(StockRequest request, Map<String, BigDecimal> itemStockQty, Map<String, BigDecimal> itemRequestQty, Map<String, BigDecimal> unApprovedQty) {
		log.info("Comparing stock qty with requested qty");
		String message = null;
		BigDecimal unApprovedStockQty = BigDecimal.ZERO;
		for (Map.Entry<String, BigDecimal> entry : itemRequestQty.entrySet()) {
			String item = entry.getKey();
			BigDecimal requestQty = entry.getValue();

			if (itemStockQty.containsKey(item)) {
				BigDecimal stockQty = itemStockQty.get(item);
				if (unApprovedQty != null && !unApprovedQty.isEmpty() && unApprovedQty.containsKey(item)) {
					 unApprovedStockQty = unApprovedQty.get(item);
				}
				if ("Stock Request".equals(request.getType())) {
					if (stockQty != null && stockQty.compareTo(requestQty) >= 0) {
						message = UtillConstantCodes.PartInStock;
					} else {
						message = UtillConstantCodes.PartOnOrder;
						break;
					}
				} else {
					if ("Return Request".equals(request.getType()) && stockQty != null && stockQty.compareTo(requestQty) >= 0 && unApprovedStockQty.add(requestQty).compareTo(stockQty) <= 0) {
						message = UtillConstantCodes.ReturnRequestSuccess;
					} else {
						sparePartsDAO.updateStatus(request);
						message = UtillConstantCodes.NoStockAvail;
						break;
					}
				}
			}
		}

		return message;
	}

	public String compareQty(StockDeductionRequest request) {
		log.info("Comparing request qty with tech bucket qty");
		String stockStatus = null;
		for (Items item : request.getItems()) {
			if (item.getTechAvailQty().longValue() >= item.getQty().longValue()) {
				stockStatus = "S";
			} else {
				stockStatus = "F";
				break;
			}
		}
		log.info("stockStatus-" + stockStatus);
		return stockStatus;
	}

}