/**
 * 
 */
package com.extra.oms.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extra.bds.bean.carrera.TransferCreationRequest;
import com.extra.bds.bean.carrera.TransferCreationResponse;
import com.extra.bds.bean.carrera.TransferRequest;
import com.extra.oms.common.BaseException;
import com.extra.oms.common.util.NumberUtil;
import com.extra.oms.core.bean.CustomerAddressInfo;
import com.extra.oms.core.bean.ItemFulfilment;
import com.extra.oms.core.bean.OrderInfo;
import com.extra.oms.core.bean.RequestType;
import com.extra.oms.core.bean.UserInfo;
import com.extra.oms.core.dao.FulfilmentDAO;
import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IOracleRMSClient;
import com.extra.oms.service.client.IOracleSIMClient;
import com.oracle.retail.integration.base.bo.fulfilordcfmcol.v1.FulfilOrdCfmCol;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilordcustdesc.v1.FulfilOrdCustDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.DeliveryType;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfilOrdDesc;
import com.oracle.retail.integration.base.bo.fulfilorddesc.v1.YesNoInd;
import com.oracle.retail.integration.base.bo.fulfilorddtl.v1.FulfilOrdDtl;
import com.oracle.retail.integration.base.bo.fulfilorddtlref.v1.FulfilOrdDtlRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfillLocType;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.SourceLocType;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.EntityAlreadyExistsWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalArgumentWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.IllegalStateWSFaultException;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.ValidationWSFaultException;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;

/**
 * @author aibrahim
 *
 */
@Service
public class FulfilmentService {

	private static final Logger _LOG = Logger.getLogger(FulfilmentService.class);

	@Autowired
	private FulfilmentDAO fulfilmentDAO;

	@Autowired
	private IOracleRMSClient oracleRMSClient;

	@Autowired
	private IOracleSIMClient oracleSIMClient;

	@Autowired
	private ICarreraClient carreraTransfer;

	public List<ItemFulfilment> searchFulfilments(Map<String, Object> params) {
		return fulfilmentDAO.searchFulfilments(params);
	}

	@Transactional(rollbackFor = BaseException.class)
	public ItemFulfilment cancelFulfilmentReq(ItemFulfilment fulfilment, UserInfo user) throws BaseException {
		ItemFulfilment cancelFulRes = null;
		String status = "F", message = null;
		try {
			fulfilmentDAO.insertFulfilmentRequest(fulfilment, user, RequestType.CN);
			cancelFulRes = cancelFulfilment(fulfilment);
			status = "S";
		} catch (Exception e) {
			_LOG.error("Error while cancelling the fulfilment", e);
			message = e.getMessage();
			throw (e instanceof BaseException ? (BaseException)e : new BaseException(message));
		} finally {
			fulfilmentDAO.updateFulfilmentRequest(fulfilment, status, message);
		}
		return cancelFulRes;
	}

	public ItemFulfilment cancelFulfilment(ItemFulfilment fulfilment) throws BaseException {
		_LOG.info(String.format("Validating the fulfilment, fulfilment order no %s, item no# %s, line no# %s", fulfilment.getFulfilOrdNo(), fulfilment.getItem(), fulfilment.getLineNo()));
		try {
			ItemFulfilment exFulfilment = fulfilmentDAO.validateAndLockFulfilment(fulfilment);
			BigDecimal confQty = exFulfilment.getCancelledQty().add(exFulfilment.getDeliveryQty());
			if (exFulfilment.getRequestQty().intValue() <= confQty.intValue()) {
				throw new BaseException("INVALID_QUANTITY_ON_CANCEL");
			}
			confQty = confQty.add(fulfilment.getExFulfilment().getCancelledQty());
			if (exFulfilment.getRequestQty().intValue() < confQty.intValue()) {
				throw new BaseException("CANCEL_QUANTITY_MAX_REACHED");
			}
			if (SourceLocType.WH.equals(SourceLocType.valueOf(fulfilment.getExFulfilment().getSrcLocationType())) && fulfilment.getExFulfilment().getFulfilLocationType().equals("W") && !fulfilment.getExFulfilment().getSrcLocation().equals(fulfilment.getExFulfilment().getFulfilLocation())) {
				fulfilmentDAO.cancelCareraFulfilment(exFulfilment);
			} else {
				cancelRMSFulfilment(fulfilment, exFulfilment);
				if (SourceLocType.ST.equals(SourceLocType.valueOf(fulfilment.getExFulfilment().getSrcLocationType())) && fulfilment.getExFulfilment().getSrcLocation().equals(fulfilment.getExFulfilment().getFulfilLocation())) {
					cancelSIMFulfilment(fulfilment);
				}
			}
			fulfilment.setCancelledQty(exFulfilment.getCancelledQty().add(fulfilment.getExFulfilment().getCancelledQty()));
			fulfilmentDAO.updateOMSFulfiment(fulfilment);
			_LOG.info("Fulfilment updated successfully.");
		} catch (Exception e) {
			_LOG.error("Error while calling the base web service", e);
			throw e instanceof BaseException ? (BaseException) e : new BaseException(e);
		}
		return fulfilment;
	}


	@Transactional(rollbackFor = BaseException.class)
	public ItemFulfilment createWithCancelFulfilment(ItemFulfilment fulfilment, boolean cancelAndCreate, UserInfo user) throws BaseException {

		ItemFulfilment createFulRes = null;
		String status = "F", message = null;
		try {
			fulfilmentDAO.insertFulfilmentRequest(fulfilment, user, cancelAndCreate ? RequestType.CC : RequestType.CR);
			if (!fulfilment.getSrcLocationType().equals("SU")) {
				BigDecimal availQty = fulfilmentDAO.getStockAvailablity(fulfilment);
				if (NumberUtil.zeroIfNull(availQty).subtract(fulfilment.getRequestQty()).intValue() < 0) {				
					throw new BaseException("STOCK_NOT_AVAILABLE");
				}
			}
			if (cancelAndCreate && NumberUtil.zeroIfNull(fulfilment.getExFulfilment().getCancelledQty()).intValue() > 0) {
				cancelFulfilment(fulfilment);
			}
			createFulRes = createFulfilment(fulfilment, cancelAndCreate, user);
			status = "S";
		} catch (Exception e) {
			_LOG.error("Error while creating the fulfilment", e);
			message = e.getMessage();
			throw (e instanceof BaseException ? (BaseException)e : new BaseException(message));
		} finally {
			fulfilmentDAO.updateFulfilmentRequest(fulfilment, status, message);
		}
		return createFulRes;
	}

	public ItemFulfilment createFulfilment(ItemFulfilment fulfilment, boolean cancelAndCreate, UserInfo user) throws BaseException {

		ItemFulfilment fulfilAddr = fulfilmentDAO.getAddresDetail(fulfilment);
		fulfilment.setFulfilOrdNo(fulfilAddr.getFulfilOrdNo());
		try {
			if (SourceLocType.WH.equals(SourceLocType.valueOf(fulfilment.getSrcLocationType())) && fulfilment.getFulfilLocationType().equals("W") && !fulfilment.getSrcLocation().equals(fulfilment.getFulfilLocation())) {
				createCarreraFulfilment(fulfilment);
			} else {
				createRMSFulfilment(fulfilment, fulfilAddr.getOrderInfo().getAddressInfo());
				if (SourceLocType.ST.equals(SourceLocType.valueOf(fulfilment.getSrcLocationType())) && fulfilment.getSrcLocation().equals(fulfilment.getFulfilLocation())) {
					try {
						createSIMFulfilment(fulfilment, fulfilAddr.getOrderInfo().getAddressInfo());
					} catch (Exception e) {
						_LOG.error("Error while creating SIM Fulfilment. Rollback RMS fulfilment");
						rollbackRMSFulfilment(fulfilment);
						throw e;
					}
				} else {
					fulfilment.setTsfNo(fulfilmentDAO.getTSFNo(fulfilment));
				}
			}
			fulfilmentDAO.createOmsFulfilment(fulfilment);
		} catch (Exception e) {
			_LOG.error("Error while calling the base web service", e);
			throw e instanceof BaseException ? (BaseException) e : new BaseException(e);
		}
		return fulfilment;
	}

	private void rollbackRMSFulfilment(ItemFulfilment fulfilment) {
		try {
			FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
			FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
	
			fulfilOrdRef.setCustomerOrderNo(fulfilment.getOrderInfo().getOrderNumber());
			fulfilOrdRef.setFulfillOrderNo(fulfilment.getFulfilOrdNo().toString());
			if (!fulfilment.getFulfilLocation().equals(fulfilment.getSrcLocation())) {
				fulfilOrdRef.setSourceLocId(fulfilment.getSrcLocation());
				fulfilOrdRef.setSourceLocType(SourceLocType.valueOf(fulfilment.getSrcLocationType()));
			}
			fulfilOrdRef.setFulfillLocId(fulfilment.getFulfilLocation());
			fulfilOrdRef.setFulfillLocType(FulfillLocType.valueOf(fulfilment.getFulfilLocationType()));
			
			FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
			fulfilOrdDtlRef.setCancelQtySuom(fulfilment.getRequestQty());
			fulfilOrdDtlRef.setItem(fulfilment.getItem());
			fulfilOrdDtlRef.setStandardUom("EA");
			fulfilOrdDtlRef.setTransactionUom("EA");
	
			fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
			fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);

			CancelFulfilOrdColRef cancelFulfilOrdColRef = new CancelFulfilOrdColRef();
			cancelFulfilOrdColRef.setFulfilOrdColRef(fulfilOrdColRef);
			oracleRMSClient.cancelFulfilOrdColRef(cancelFulfilOrdColRef);
			_LOG.info("RMS fulfilment rollbacked successfully");
		} catch (Exception e) {
			_LOG.warn("Error while rollback RMS fulfilment", e);
		}
	}

	private void createCarreraFulfilment(ItemFulfilment fulfilment) throws BaseException {
		TransferCreationRequest request = new TransferCreationRequest();
		request.setCust_ord_no(fulfilment.getOrderInfo().getOrderNumber());
		request.setDest_id(fulfilment.getFulfilLocCHId());
		request.setFul_loc(fulfilment.getFulfilLocCHId());
		request.setFul_ord_no(fulfilment.getFulfilOrdNo().intValue());
		request.setRef_no(fulfilment.getOrderInfo().getOmsOrderNumber() + fulfilment.getFulfilOrdNo());
		request.setSrc_id(fulfilment.getSrcLocCHId().intValue());
		request.setSrc_loc(fulfilment.getSrcLocCHId().intValue());

		TransferRequest itemReq = new TransferRequest();
		itemReq.setItem(fulfilment.getItem());
		itemReq.setQty(fulfilment.getRequestQty().intValue());
		List<TransferRequest> items = new ArrayList<TransferRequest>();
		items.add(itemReq);

		request.setCustomerItems(items);
		TransferCreationResponse response = carreraTransfer.getOrderItemsResponse(request);
		if (!response.isSuccess()) {
			_LOG.error("Error while creating the carrera transfer. " + response.getMessage());
			throw new BaseException(response.getMessage());
		}
		fulfilment.setTsfNo(response.getTsf_No());
		_LOG.info("Carrera transfer created successfully");
	}

	public BigDecimal getStockAvailablity(ItemFulfilment fulfilment) throws BaseException {
		return fulfilmentDAO.getStockAvailablity(fulfilment);
	}

	private InvocationSuccess cancelRMSFulfilment(ItemFulfilment fulfilment, ItemFulfilment exFulfilment) throws EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, BaseException {

		FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();

		fulfilOrdRef.setCustomerOrderNo(fulfilment.getOrderInfo().getOrderNumber());
		fulfilOrdRef.setFulfillOrderNo(exFulfilment.getFulfilOrdNo().toString());
		if (!exFulfilment.getFulfilLocation().equals(exFulfilment.getSrcLocation())) {
			fulfilOrdRef.setSourceLocId(exFulfilment.getSrcLocation());
			fulfilOrdRef.setSourceLocType(SourceLocType.valueOf(exFulfilment.getSrcLocationType()));
		}
		fulfilOrdRef.setFulfillLocId(exFulfilment.getFulfilLocation());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.valueOf(exFulfilment.getFulfilLocationType()));
		
		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(fulfilment.getExFulfilment().getCancelledQty());
		fulfilOrdDtlRef.setItem(fulfilment.getItem());
		fulfilOrdDtlRef.setStandardUom("EA");
		fulfilOrdDtlRef.setTransactionUom("EA");

		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		fulfilOrdColRef.getFulfilOrdRef().add(fulfilOrdRef);

		CancelFulfilOrdColRef cancelFulfilOrdColRef = new CancelFulfilOrdColRef();
		cancelFulfilOrdColRef.setFulfilOrdColRef(fulfilOrdColRef);
		InvocationSuccess response = oracleRMSClient.cancelFulfilOrdColRef(cancelFulfilOrdColRef).getInvocationSuccess();
		if (!response.getSuccessMessage().equals("cancelFulfilOrdColRef service call was successful.")) {
			throw new BaseException("BASE_FULFILMENT_EXCEPTION");
		}
		return response;
	}

	private void cancelSIMFulfilment(ItemFulfilment fulfilment) throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException {

		FulfilOrdColRef fulfilOrdColRefObj = new FulfilOrdColRef();
		
		FulfilOrdRef fulfilOrdRef = new FulfilOrdRef();
		fulfilOrdRef.setCustomerOrderNo(fulfilment.getOrderInfo().getOrderNumber());
		fulfilOrdRef.setFulfillOrderNo(fulfilment.getFulfilOrdNo().toString());
		if (!fulfilment.getExFulfilment().getFulfilLocation().equals(fulfilment.getExFulfilment().getSrcLocation())) {
			fulfilOrdRef.setSourceLocId(fulfilment.getExFulfilment().getSrcLocation());
			fulfilOrdRef.setSourceLocType(SourceLocType.valueOf(fulfilment.getExFulfilment().getSrcLocationType()));
		}
		fulfilOrdRef.setFulfillLocId(fulfilment.getExFulfilment().getFulfilLocation());
		fulfilOrdRef.setFulfillLocType(FulfillLocType.valueOf(fulfilment.getExFulfilment().getFulfilLocationType()));
		fulfilOrdColRefObj.getFulfilOrdRef().add(fulfilOrdRef);

		FulfilOrdDtlRef fulfilOrdDtlRef = new FulfilOrdDtlRef();
		fulfilOrdDtlRef.setCancelQtySuom(fulfilment.getExFulfilment().getCancelledQty());
		fulfilOrdDtlRef.setItem(fulfilment.getItem());
		fulfilOrdDtlRef.setStandardUom("EA");
		fulfilOrdDtlRef.setTransactionUom("EA");

		fulfilOrdRef.getFulfilOrdDtlRef().add(fulfilOrdDtlRef);
		CancelFulfillmentOrderDetail cancelFulfillmentOrderDetail = new CancelFulfillmentOrderDetail();
		cancelFulfillmentOrderDetail.setFulfilOrdColRef(fulfilOrdColRefObj);
		oracleSIMClient.cancelFulfillmentOrderDetail(cancelFulfillmentOrderDetail);
	}

	private void createRMSFulfilment(ItemFulfilment fulfilment, CustomerAddressInfo addressInfo) throws EntityAlreadyExistsWSFaultException, IllegalArgumentWSFaultException, IllegalStateWSFaultException, ValidationWSFaultException, DatatypeConfigurationException {

		FulfilOrdColDesc fulfilOrdColDesc = new FulfilOrdColDesc();

		FulfilOrdDesc fulfilOrdDesc = new FulfilOrdDesc();
		fulfilOrdDesc.setCustomerOrderNo(fulfilment.getOrderInfo().getOrderNumber());
		fulfilOrdDesc.setFulfillOrderNo(fulfilment.getFulfilOrdNo().toString());
		if (!fulfilment.getFulfilLocation().equals(fulfilment.getSrcLocation())) {
			fulfilOrdDesc.setSourceLocId(fulfilment.getSrcLocation());
			fulfilOrdDesc.setSourceLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType.valueOf(fulfilment.getSrcLocationType()));
		}
		fulfilOrdDesc.setFulfillLocId(fulfilment.getFulfilLocation());
		fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.valueOf(fulfilment.getFulfilLocationType()));
		fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.N);
		fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(fulfilment.getOrderInfo().getDeliveryType()));
		GregorianCalendar greCal = new GregorianCalendar();
		fulfilOrdDesc.setConsumerDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(greCal));

		FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
		fulfilOrdCustDesc.setCustomerNo("1");
		fulfilOrdCustDesc.setDeliverFirstName(addressInfo.getDeliverFirstName());
		fulfilOrdCustDesc.setDeliverPhoneticFirst(addressInfo.getDeliverPhoneticFirst());
		fulfilOrdCustDesc.setDeliverLastName(addressInfo.getDeliverLastName());
		fulfilOrdCustDesc.setDeliverPhoneticLast(addressInfo.getDeliverPhoneticLast());
		fulfilOrdCustDesc.setDeliverPreferredName(addressInfo.getDeliverPreferredName());
		fulfilOrdCustDesc.setDeliverAdd1(addressInfo.getDeliverAdd1());
		fulfilOrdCustDesc.setDeliverCity(addressInfo.getDeliverCity());
		fulfilOrdCustDesc.setDeliverState(addressInfo.getDeliverState());
		fulfilOrdCustDesc.setDeliverCountryId(addressInfo.getDeliverCountryId());
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
		fulfilOrdCustDesc.setBillCountryId(addressInfo.getBillCountryId());
		fulfilOrdCustDesc.setBillCounty(addressInfo.getBillCounty());
		fulfilOrdCustDesc.setBillPost(addressInfo.getBillPost());
		fulfilOrdCustDesc.setBillJurisdiction(addressInfo.getBillJurisdiction());
		fulfilOrdCustDesc.setBillPhone(addressInfo.getBillPhone());
		
		fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
	
		FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
		fulfilOrdDtl.setItem(fulfilment.getItem());
		fulfilOrdDtl.setOrderQtySuom(fulfilment.getRequestQty());
		fulfilOrdDtl.setStandardUom("EA");
		fulfilOrdDtl.setTransactionUom("EA");
		fulfilOrdDtl.setSubstituteInd("N");
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);
		
		fulfilOrdColDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
		fulfilOrdColDesc.setCollectionSize(fulfilOrdColDesc.getFulfilOrdDesc().size());
		CreateFulfilOrdColDesc createFulfilOrdColDesc = new CreateFulfilOrdColDesc();
		createFulfilOrdColDesc.setFulfilOrdColDesc(fulfilOrdColDesc);

		FulfilOrdCfmCol fulfilOrdCfmCol = oracleRMSClient.createFulfilOrdColDesc(createFulfilOrdColDesc).getFulfilOrdCfmCol();
		_LOG.info("Number of fulfilments created successfully -> " + fulfilOrdCfmCol.getCollectionSize());
	}

	private void createSIMFulfilment(ItemFulfilment fulfilment, CustomerAddressInfo addressInfo) throws com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalArgumentWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.IllegalStateWSFaultException, com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ValidationWSFaultException, DatatypeConfigurationException {
		
		FulfilOrdColDesc fulfilOrdCfmDesc = new FulfilOrdColDesc();
		FulfilOrdDesc fulfilOrdDesc = new FulfilOrdDesc();
		fulfilOrdDesc.setCustomerOrderNo(fulfilment.getOrderInfo().getOrderNumber());
		fulfilOrdDesc.setFulfillOrderNo(fulfilment.getFulfilOrdNo().toString());
		if (!fulfilment.getFulfilLocation().equals(fulfilment.getSrcLocation())) {
			fulfilOrdDesc.setSourceLocId(fulfilment.getSrcLocation());
			fulfilOrdDesc.setSourceLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.SourceLocType.valueOf(fulfilment.getSrcLocationType()));
		}
		fulfilOrdDesc.setFulfillLocId(fulfilment.getFulfilLocation());
		fulfilOrdDesc.setFulfillLocType(com.oracle.retail.integration.base.bo.fulfilorddesc.v1.FulfillLocType.valueOf(fulfilment.getFulfilLocationType()));
		fulfilOrdDesc.setPartialDeliveryInd(YesNoInd.N);
		fulfilOrdDesc.setDeliveryType(DeliveryType.valueOf(fulfilment.getOrderInfo().getDeliveryType()));
		GregorianCalendar greCal = new GregorianCalendar();
		fulfilOrdDesc.setConsumerDeliveryDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(greCal));
		
		FulfilOrdCustDesc fulfilOrdCustDesc = new FulfilOrdCustDesc();
		fulfilOrdCustDesc.setCustomerNo("1");
		fulfilOrdCustDesc.setDeliverFirstName(addressInfo.getDeliverFirstName());
		fulfilOrdCustDesc.setDeliverPhoneticFirst(addressInfo.getDeliverPhoneticFirst());
		fulfilOrdCustDesc.setDeliverLastName(addressInfo.getDeliverLastName());
		fulfilOrdCustDesc.setDeliverPhoneticLast(addressInfo.getDeliverPhoneticLast());
		fulfilOrdCustDesc.setDeliverPreferredName(addressInfo.getDeliverPreferredName());
		fulfilOrdCustDesc.setDeliverAdd1(addressInfo.getDeliverAdd1());
		fulfilOrdCustDesc.setDeliverCity(addressInfo.getDeliverCity());
		fulfilOrdCustDesc.setDeliverState(addressInfo.getDeliverState());
		fulfilOrdCustDesc.setDeliverCountryId(addressInfo.getDeliverCountryId());
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
		fulfilOrdCustDesc.setBillCountryId(addressInfo.getBillCountryId());
		fulfilOrdCustDesc.setBillCounty(addressInfo.getBillCounty());
		fulfilOrdCustDesc.setBillPost(addressInfo.getBillPost());
		fulfilOrdCustDesc.setBillJurisdiction(addressInfo.getBillJurisdiction());
		fulfilOrdCustDesc.setBillPhone(addressInfo.getBillPhone());
		
		fulfilOrdDesc.setFulfilOrdCustDesc(fulfilOrdCustDesc);
		
		FulfilOrdDtl fulfilOrdDtl = new FulfilOrdDtl();
		fulfilOrdDtl.setItem(fulfilment.getItem());
		fulfilOrdDtl.setOrderQtySuom(fulfilment.getRequestQty());
		fulfilOrdDtl.setStandardUom("EA");
		fulfilOrdDtl.setTransactionUom("EA");
		fulfilOrdDtl.setSubstituteInd("N");
		fulfilOrdDesc.getFulfilOrdDtl().add(fulfilOrdDtl);

		fulfilOrdCfmDesc.getFulfilOrdDesc().add(fulfilOrdDesc);
		fulfilOrdCfmDesc.setCollectionSize(fulfilOrdCfmDesc.getFulfilOrdDesc().size());

		CreateFulfillmentOrderDetail createFulfillmentOrderDetail = new CreateFulfillmentOrderDetail();
		createFulfillmentOrderDetail.setFulfilOrdColDesc(fulfilOrdCfmDesc);
		oracleSIMClient.createFulfillmentOrderDetail(createFulfillmentOrderDetail);
	}

	public List<ItemFulfilment> getFulfilments(Workbook workbook) throws BaseException {
		Sheet sheet = workbook.getSheetAt(0);
		int lastRow = sheet.getLastRowNum();
		if (lastRow < 1) {
			throw new BaseException("BULK_FULFIL_INVALID_NO_OF_MIN_ROWS");
		} else if (lastRow > 1000) {
			throw new BaseException("BULK_FULFIL_INVALID_NO_OF_MAX_ROWS");
		}
		String cellValue = null, key = null;
		Map<String, ItemFulfilment> fulfilmentMap = new LinkedHashMap<String, ItemFulfilment>();
		Set<String> omsOrderNumbers = new HashSet<String>();
		Set<String> items = new HashSet<String>();
		Set<Integer> fulfulOrdNos = new HashSet<Integer>();
		DataFormatter formatter = new DataFormatter();
		try {
			for(int i = 1;i <= lastRow;i++) {
				ItemFulfilment fulfilment = new ItemFulfilment();
				OrderInfo orderInfo = new OrderInfo();
				ItemFulfilment exFulfilment = new ItemFulfilment();
	
				Row row = sheet.getRow(i);
				orderInfo.setOrderNumber(formatter.formatCellValue(row.getCell(0)));
				orderInfo.setOmsOrderNumber(formatter.formatCellValue(row.getCell(1)));

				cellValue = formatter.formatCellValue(row.getCell(2));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid fulfiment order number for order number '" + orderInfo.getOrderNumber() + "'in C" + (i + 1));
				}
				fulfilment.setFulfilOrdNo(Long.parseLong(cellValue));
				fulfilment.setLineNo(formatter.formatCellValue(row.getCell(3)));
				fulfilment.setItem(formatter.formatCellValue(row.getCell(4)));
	
				cellValue = formatter.formatCellValue(row.getCell(5));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid request quantity for order number '" + orderInfo.getOrderNumber() + "'in F" + (i + 1));
				}
				exFulfilment.setRequestQty(new BigDecimal(cellValue));
				
				cellValue = formatter.formatCellValue(row.getCell(6));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid old source location for order number '" + orderInfo.getOrderNumber() + "'in G" + (i + 1));
				}
				exFulfilment.setSrcLocationType(formatter.formatCellValue(row.getCell(7)));
				Long locationId = Long.parseLong(cellValue);
				if (exFulfilment.getSrcLocationType().equals("WH")) {
					exFulfilment.setSrcLocCHId(locationId);
					exFulfilment.setSrcLocation(locationId / 10);
				} else {
					exFulfilment.setSrcLocation(locationId);
				}
				
				cellValue = formatter.formatCellValue(row.getCell(8));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid old fulfil location for order number '" + orderInfo.getOrderNumber() + "'in I" + (i + 1));
				}
				exFulfilment.setFulfilLocationType(formatter.formatCellValue(row.getCell(9)));
				locationId = Long.parseLong(cellValue);
				if (exFulfilment.getFulfilLocationType().equals("W")) {
					exFulfilment.setFulfilLocCHId(locationId.intValue());
					exFulfilment.setFulfilLocation(locationId / 10);
				} else {
					exFulfilment.setFulfilLocation(locationId);
				}

				cellValue = formatter.formatCellValue(row.getCell(10));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid request quantity for order number '" + orderInfo.getOrderNumber() + "'in K" + (i + 1));
				}
				fulfilment.setRequestQty(new BigDecimal(cellValue));
				fulfilment.setCancelledQty(fulfilment.getRequestQty());
				exFulfilment.setCancelledQty(fulfilment.getRequestQty());

				fulfilment.setSrcLocationType(formatter.formatCellValue(row.getCell(12)));
				cellValue = formatter.formatCellValue(row.getCell(11));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid new source location for order number '" + orderInfo.getOrderNumber() + "'in L" + (i + 1));
				}
				locationId = Long.parseLong(cellValue);
				if (fulfilment.getSrcLocationType().equals("WH")) {
					fulfilment.setSrcLocCHId(locationId);
					fulfilment.setSrcLocation(locationId / 10);
				} else {
					fulfilment.setSrcLocation(locationId);
				}
				
				cellValue = formatter.formatCellValue(row.getCell(13));
				if (!NumberUtils.isNumber(cellValue)) {
					throw new BaseException("Invalid new fulfil location for order number '" + orderInfo.getOrderNumber() + "'in N" + (i + 1));
				}
				fulfilment.setFulfilLocationType(formatter.formatCellValue(row.getCell(14)));
				locationId = Long.parseLong(cellValue);
				if (fulfilment.getFulfilLocationType().equals("W")) {
					fulfilment.setFulfilLocCHId(locationId.intValue());
					fulfilment.setFulfilLocation(locationId / 10);
				} else {
					fulfilment.setFulfilLocation(locationId);
				}
				
				fulfilment.setRemarks("Invalid order number / oms number / item / fulfilment number");
				
				key = orderInfo.getOmsOrderNumber() + "~" + fulfilment.getItem() + "~" + fulfilment.getFulfilOrdNo();
				fulfilment.setOrderInfo(orderInfo);
				fulfilment.setExFulfilment(exFulfilment);
				if (fulfilmentMap.containsKey(key)) {
					String message = "Duplicate Order for order number -> " + orderInfo.getOrderNumber() + " Item -> " + fulfilment.getItem();
					_LOG.error(message);
					throw new BaseException(message);
				}
				omsOrderNumbers.add(orderInfo.getOmsOrderNumber());
				items.add(fulfilment.getItem());
				fulfulOrdNos.add(fulfilment.getFulfilOrdNo().intValue());
				fulfilmentMap.put(key, fulfilment);
			}
		} catch (RuntimeException e) {
			_LOG.error("Error while processing the file", e);
			throw new BaseException();
		} finally {
			try {
				workbook.close();
			} catch (Exception e) {
			}
		}
		return fulfilmentDAO.validateFulfilments(fulfilmentMap, omsOrderNumbers, items, fulfulOrdNos);
	}

	public Workbook getBulkFulfilTemplate() throws BaseException {
		XSSFWorkbook wb = new XSSFWorkbook();
		Sheet sh = wb.createSheet();
		Row row = sh.createRow(0);
	
		final XSSFCellStyle cellStyle = wb.createCellStyle();

		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		// cellStyle.setFillBackgroundColor(new XSSFColor(new java.awt.Color(189, 215, 238)));

		Cell cell = row.createCell(0);
		cell.setCellValue("CustomerOrderNo");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(1);
		cell.setCellValue("OmsCustOrdNo");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(2);
		cell.setCellValue("FulfillNo");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(3);
		cell.setCellValue("LineNo");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(4);
		cell.setCellValue("Item");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(5);
		cell.setCellValue("QTY");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(6);
		cell.setCellValue("Old Source Location");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(7);
		cell.setCellValue("Old Source Type");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(8);
		cell.setCellValue("Old Fulfil Location");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(9);
		cell.setCellValue("Old Fulfill Type");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(10);
		cell.setCellValue("Fulfilment QTY");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(11);
		cell.setCellValue("New Source Location");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(12);
		cell.setCellValue("New Source Type");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(13);
		cell.setCellValue("New Fulfil Location");
		cell.setCellStyle(cellStyle);

		cell = row.createCell(14);
		cell.setCellValue("New Fulfill Type");
		cell.setCellStyle(cellStyle);

		/*
		 * for (int i = 0;i < 15;i++) { sh.autoSizeColumn(i); }
		 */
		return wb;
	}
}
