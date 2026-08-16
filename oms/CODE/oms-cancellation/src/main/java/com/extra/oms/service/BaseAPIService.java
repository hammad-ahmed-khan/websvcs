/**
 * 
 */
package com.extra.oms.service;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.Holder;

import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.extra_services.ArrayOfOrderDetailStatus;
import org.datacontract.schemas._2004._07.extra_services.OrderDetailStatus;
import org.datacontract.schemas._2004._07.extra_services.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.extra.common.model.OmsBackOrderDtl;
import com.extra.common.model.OmsCoFulfillDetail;
import com.extra.common.model.OmsCustOrdHead;
import com.extra.oms.common.BaseException;
import com.extra.oms.common.WebServiceException;
import com.extra.oms.model.OmsCoCancelItem;
import com.extra.oms.model.OmsCoFoCancel;
import com.extra.oms.model.POSOrderCancelRequest;
import com.extra.services.omsstatus.ObjectFactory;
import com.extra.services.omsstatus.UpdateOrderStatus;
import com.oracle.retail.integration.base.bo.fodhdrcoldesc.v1.FodHdrColDesc;
import com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodHdrDesc;
import com.oracle.retail.integration.base.bo.fodhdrdesc.v1.FodStatus;
import com.oracle.retail.integration.base.bo.fodref.v1.FodRef;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreItmMod;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreModVo;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.fulfilordref.v1.FulfilOrdRef;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.invbackorddesc.v1.InvBackOrdDesc;
import com.oracle.retail.integration.base.bo.invocationsuccess.v1.InvocationSuccess;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjItmMod;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordDesc;
import com.oracle.retail.integration.base.bo.strforddesc.v1.StrFordItm;
import com.oracle.retail.integration.base.bo.strfordhdrcoldesc.v1.StrFordHdrColDesc;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriStatus;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordCriType;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.InventoryBackOrderPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.FulfillmentOrderReversePickPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;

/**
 * @author aibrahim
 *
 */
@Service
public class BaseAPIService {

	private static final Logger LOG = Logger.getLogger(BaseAPIService.class);

	@Autowired
	private ApplicationContext appContext;

	public void reverseInventoryAdjustment(List<StrAdjItmMod> adjItms, Long storeId, String custOrdNo) throws BaseException {
		InventoryAdjustmentPortType inventoryAdjustmentPort = appContext.getBean("inventoryAdjustmentPortType", InventoryAdjustmentPortType.class);
		StrAdjModVo strAdjModVo = new StrAdjModVo();
		strAdjModVo.getStrAdjItmMod().addAll(adjItms);
		strAdjModVo.setStoreId(storeId);
		strAdjModVo.setComments("Unreserved for customer order id :" + custOrdNo);
		try {
			inventoryAdjustmentPort.saveAndConfirmInventoryAdjustment(strAdjModVo);
		} catch (Exception e) {
			throw new WebServiceException(getSoapMessage(strAdjModVo, StrAdjModVo.class, "saveAndConfirmInventoryAdjustment", "http://www.oracle.com/retail/sim/integration/services/InventoryAdjustmentService/v1"), e);
		}
	}

	public void adjustInventoryBackOrder(List<InvBackOrdDesc> inbBODescs) throws BaseException {
		InventoryBackOrderPortType inventoryBackOrderPortType = appContext.getBean("inventoryBackOrderPortType", InventoryBackOrderPortType.class);
		InvBackOrdColDesc invBackOrdColDesc = new InvBackOrdColDesc();
		invBackOrdColDesc.setCollectionSize(inbBODescs.size());
		invBackOrdColDesc.getInvBackOrdDesc().addAll(inbBODescs);
		try {
			inventoryBackOrderPortType.createInvBackOrdColDesc(invBackOrdColDesc);
		} catch (Exception e) {
			throw new WebServiceException(getSoapMessage(invBackOrdColDesc, InvBackOrdColDesc.class, "createInvBackOrdColDesc", "http://www.oracle.com/retail/rms/integration/services/InventoryBackOrderService/v1"), e);
		}
	}

	public InvocationSuccess cancelRMSFulfilment(List<FulfilOrdRef> fulfilOrdRefs) throws BaseException {

		FulfillOrderPortType fulfillOrderPortType = appContext.getBean("fulfillOrderPortType", FulfillOrderPortType.class);

		FulfilOrdColRef fulfilOrdColRef = new FulfilOrdColRef();
		fulfilOrdColRef.getFulfilOrdRef().addAll(fulfilOrdRefs);
		fulfilOrdColRef.setCollectionSize(fulfilOrdRefs.size());
		InvocationSuccess response = null;
		try {
			response = fulfillOrderPortType.cancelFulfilOrdColRef(fulfilOrdColRef);
			if (!response.getSuccessMessage().equals("cancelFulfilOrdColRef service call was successful.")) {
				throw new BaseException("BASE_FULFILMENT_EXCEPTION");
			}
		} catch (Exception e) {
			throw new WebServiceException(getSoapMessage(fulfilOrdColRef, FulfilOrdColRef.class, "cancelFulfilOrdColRef", "http://www.oracle.com/retail/rms/integration/services/FulfillOrderService/v1"), e);
		}
		return response;
	}

	public void cancelSIMFulfilment(List<FulfilOrdRef> fulfilOrdRefs) throws BaseException {

		StoreFulfillmentOrderPortType simFulfillOrderPortType = appContext.getBean("storeFulfillmentOrderPortType", StoreFulfillmentOrderPortType.class);

		FulfilOrdColRef fulfilOrdColRefObj = new FulfilOrdColRef();
		Holder<FulfilOrdColRef> fulfilOrdColRef = new Holder<FulfilOrdColRef>(fulfilOrdColRefObj);

		fulfilOrdColRefObj.getFulfilOrdRef().addAll(fulfilOrdRefs);
		fulfilOrdColRefObj.setCollectionSize(fulfilOrdRefs.size());
		try {
			simFulfillOrderPortType.cancelFulfillmentOrderDetail(fulfilOrdColRef);
		} catch (Exception e) {
			throw new WebServiceException(getSoapMessage(fulfilOrdColRefObj, FulfilOrdColRef.class, "cancelFulfillmentOrderDetail", "http://www.oracle.com/retail/sim/integration/services/StoreFulfillmentOrderService/v1"), e);
		}
	}

	public StrFordHdrColDesc readSIMFulfilmentHeader(OmsCoFulfillDetail fulfil, String custOrdNo) throws BaseException {
		StoreFulfillmentOrderPortType simFulfillOrderPortType = appContext.getBean("storeFulfillmentOrderPortType", StoreFulfillmentOrderPortType.class);
		StrFordHdrColDesc desc = null;
		StrFordHdrCriVo strFordHdrCriVo = new StrFordHdrCriVo();
		strFordHdrCriVo.setItemId(fulfil.getItem());
		strFordHdrCriVo.setOrderType(StrFordCriType.WEB_ORDER);
		strFordHdrCriVo.setStoreId(fulfil.getFulfillLoc().longValue());
		strFordHdrCriVo.setCustomerOrderId(custOrdNo);
		strFordHdrCriVo.setExtFulfillmentOrderId(fulfil.getFulfillOrderNo().toString());
		strFordHdrCriVo.setStatus(StrFordCriStatus.NO_VALUE);
		try {
			desc = simFulfillOrderPortType.lookupFulfillmentOrderHeaders(strFordHdrCriVo);
		} catch (Exception e) {
			LOG.error("Error while reading the SIM fulfilment header", e);
			throw new BaseException();
		}
		return desc;
	}

	public StrFordItm readSIMFulfilmentDetail(OmsCoFulfillDetail fulfil, Long intRefFulfilOrdNo) throws BaseException {
		StoreFulfillmentOrderPortType simFulfillOrderPortType = appContext.getBean("storeFulfillmentOrderPortType", StoreFulfillmentOrderPortType.class);
		StrFordItm fordItm = null;
		StrFordRef strFordRef = new StrFordRef();
		strFordRef.setIntFulfillmentOrderId(intRefFulfilOrdNo);
		try {
			StrFordDesc strFordDesc = simFulfillOrderPortType.readFulfillmentOrderDetail(strFordRef);
			for (StrFordItm itm : strFordDesc.getStrFordItm()) {
				if (itm.getItemId().equals(fulfil.getItem()) && (fulfil.getLineNo().longValue() == itm.getLineId())) {
					fordItm = itm;
					break;
				}
			}
			return fordItm;
		} catch (Exception e) {
			LOG.error("Error while reading the SIM fulfilment detail", e);
			throw new BaseException();
		}
	}

	public void cancelDeliveryDraft(Long intRefFulfilId) throws BaseException {
		FulfillmentOrderDeliveryPortType fulfillmentOrderDeliveryPortType = appContext.getBean("fulfillmentOrderDeliveryPortType", FulfillmentOrderDeliveryPortType.class);
		StrFordRef strFordRef = new StrFordRef();
		strFordRef.setIntFulfillmentOrderId(intRefFulfilId);
		FodHdrColDesc fodHdrColDesc = null;
		try {
			fodHdrColDesc = fulfillmentOrderDeliveryPortType.lookupFulfillmentOrderDeliveryHeaders(strFordRef);
		} catch (com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalArgumentWSFaultException e) {
			e.printStackTrace();
		} catch (com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.IllegalStateWSFaultException e) {
			e.printStackTrace();
		} catch (com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.ValidationWSFaultException e) {
			e.printStackTrace();
		}
		for (FodHdrDesc hdrDesc : fodHdrColDesc.getFodHdrDesc()) {
			if (!(FodStatus.COMPLETED.equals(hdrDesc.getStatus()) || FodStatus.CANCELED.equals(hdrDesc.getStatus()))) {
				FodRef fodRef = new FodRef();
				fodRef.setDeliveryId(hdrDesc.getIntFulfillOrderDeliveryId());
				try {
					fulfillmentOrderDeliveryPortType.cancelFulfillmentOrderDelivery(fodRef);
				} catch (Exception e) {
					LOG.error("Error while cancel the delivery draft", e);
					throw new BaseException(e);
				}
			}
		}
	}

	public void reversePickCFSOrder(Long lineId, Long intFulfillmentOrderId, BigDecimal cancelPickQty) throws BaseException {
		FulfillmentOrderReversePickPortType fulfillmentOrderReversePickPortType = appContext.getBean("fulfillmentOrderReversePickPortType", FulfillmentOrderReversePickPortType.class);
		ForpCreModVo forpCreModVo = new ForpCreModVo();
		ForpCreItmMod forpCreItmMod = new ForpCreItmMod();
		forpCreItmMod.setFulfillmentOrderLineId(lineId);
		forpCreItmMod.setQuantity(cancelPickQty);
		forpCreModVo.setIntFulfillmentOrderId(intFulfillmentOrderId);
		try {
			ForpRef forpRef = fulfillmentOrderReversePickPortType.createReversePick(forpCreModVo);
			fulfillmentOrderReversePickPortType.confirmReversePick(forpRef);
		} catch (Exception e) {
			LOG.error("Error while reverse pick the CFS order", e);
			throw new BaseException(e);
		}
	}

	public String createSOAPMessageSiebelNotification(POSOrderCancelRequest cancelRequest, OmsCustOrdHead custOrdHead, List<OmsCoCancelItem> items, List<OmsBackOrderDtl> backOrders, Map<String, String> sysParam) throws BaseException {
		try {

			ObjectFactory factory = new ObjectFactory();
			OrderStatus status = new OrderStatus();
			UpdateOrderStatus orderStatus = factory.createUpdateOrderStatus();
			orderStatus.setOrderStatus(factory.createUpdateOrderStatusOrderStatus(status));

			org.datacontract.schemas._2004._07.extra_services.ObjectFactory stFactory = new org.datacontract.schemas._2004._07.extra_services.ObjectFactory();
			GregorianCalendar geCalendar = new GregorianCalendar();
			DatatypeFactory dateFactory = DatatypeFactory.newInstance();

			status.setEnitityId(cancelRequest.getEntityId());
			status.setApplicationId(cancelRequest.getApplicationId());
			status.setOrderId(cancelRequest.getCustOrderNo());
			status.setSubOrderId(stFactory.createOrderStatusSubOrderId("1"));
			status.setOmsOrderId(custOrdHead.getOmsCustOrdNo().toString());

			geCalendar.setTime(custOrdHead.getConsumerDlyTime());
			status.setDeliveryDate(dateFactory.newXMLGregorianCalendar(geCalendar));
			
			geCalendar.setTime(custOrdHead.getLastUpdateDatetime());
			status.setUpdateDate(dateFactory.newXMLGregorianCalendar(geCalendar));

			ArrayOfOrderDetailStatus ordStatuses = stFactory.createArrayOfOrderDetailStatus();
			status.setOrderDetailStatuses(ordStatuses);

			geCalendar.setTimeInMillis(System.currentTimeMillis());
			for (OmsCoCancelItem item : items) {
				if ((sysParam.get("SHIPPING_CHARGE_DEPT").equals(item.getOrdItem().getItemDept().toString()) || item.getOrdItem().getInvInd().equals("N"))) {
					OrderDetailStatus orderDetail = stFactory.createOrderDetailStatus();
					orderDetail.setOrderDetailId(item.getLineNo().longValue());
					orderDetail.setQuantity(item.getCancelConfQty().intValue());
					orderDetail.setProductSku(item.getItem());
					orderDetail.setFulfillType("S");
					orderDetail.setFulfillId(custOrdHead.getOrderRequestorId().intValue());
					orderDetail.setEventId("CAC");
					orderDetail.setEventComment("Cancelled by Customer");
					orderDetail.setEventReferenceId(cancelRequest.getOmsCancelId().longValue());
					orderDetail.setUpdateDate(dateFactory.newXMLGregorianCalendar(geCalendar));
					orderDetail.setSourceType("ST");
					orderDetail.setSourceId(custOrdHead.getOrderRequestorId().intValue());
					status.getOrderDetailStatuses().getOrderDetailStatus().add(orderDetail);
				} else {
					for (OmsCoFoCancel foCancel : item.getFulfils()) {
						OrderDetailStatus orderDetail = stFactory.createOrderDetailStatus();
						orderDetail.setOrderDetailId(item.getLineNo().longValue());
						orderDetail.setQuantity(foCancel.getFoCancelledOty().intValue());
						orderDetail.setProductSku(foCancel.getItem());
						orderDetail.setFulfillType(foCancel.getFulfillLocType());
						orderDetail.setFulfillId(foCancel.getFulfillLoc().intValue());
						orderDetail.setEventId("CAC");
						orderDetail.setEventComment("Cancelled by Customer");
						orderDetail.setEventReferenceId(cancelRequest.getOmsCancelId().longValue());
						orderDetail.setUpdateDate(dateFactory.newXMLGregorianCalendar(geCalendar));
						orderDetail.setSourceType(foCancel.getSourceLocType());
						orderDetail.setSourceId(foCancel.getSourceLoc().intValue());
						status.getOrderDetailStatuses().getOrderDetailStatus().add(orderDetail);
					}
				}
			}
			for (OmsBackOrderDtl backOrd : backOrders) {
				OrderDetailStatus orderDetail = stFactory.createOrderDetailStatus();
				orderDetail.setOrderDetailId(backOrd.getLineNo().longValue());
				orderDetail.setQuantity(backOrd.getCancelledQty().intValue());
				orderDetail.setProductSku(backOrd.getItem());
				orderDetail.setFulfillType(backOrd.getFulfillLocType());
				orderDetail.setFulfillId(backOrd.getFulfillLoc().intValue());
				orderDetail.setEventId("CAC");
				orderDetail.setEventComment("Cancelled by Customer");
				orderDetail.setEventReferenceId(cancelRequest.getOmsCancelId().longValue());
				orderDetail.setUpdateDate(dateFactory.newXMLGregorianCalendar(geCalendar));
				orderDetail.setSourceType(backOrd.getSourceLocType());
				orderDetail.setSourceId(backOrd.getSourceLoc().intValue());
				status.getOrderDetailStatuses().getOrderDetailStatus().add(orderDetail);
			}
			return getSoapMessage(orderStatus, UpdateOrderStatus.class, null, null);
		} catch (Exception e) {
			LOG.error("Error while creating the soap message for siebel notification", e);
			return null;
		}
	}

	private static <T> String getSoapMessage(T data, Class<T> dataClass, String operation, String opNamespace) {
		StringBuilder message = new StringBuilder();
		try {
			message.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">").append("<soap:Body>");
			if (operation != null) {
				message.append("<op:").append(operation).append(" xmlns:op=\"").append(opNamespace).append("\">");
			}
			JAXBContext jaxbContext = JAXBContext.newInstance(dataClass);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(data, stringWriter);
			message.append(stringWriter.toString());
			if (operation != null) {
				message.append("</op:").append(operation).append(">");
			}
			message.append("</soap:Body></soap:Envelope>");
		} catch (Exception e) {
			LOG.error("Error while creating the soap message", e);
		}
		return message.toString();
	}
}
