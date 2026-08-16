package com.logicinfo.oms.util;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;

import com.extra.oms.service.client.ICarreraClient;
import com.extra.oms.service.client.IOracleRMSClient;
import com.extra.oms.service.client.IOracleSIMClient;
import com.logicinfo.oms.beans.OMSUtilCommons;
import com.oracle.retail.integration.base.bo.forpcremodvo.v1.ForpCreModVo;
import com.oracle.retail.integration.base.bo.forpref.v1.ForpRef;
import com.oracle.retail.integration.base.bo.fulfilordcoldesc.v1.FulfilOrdColDesc;
import com.oracle.retail.integration.base.bo.fulfilordcolref.v1.FulfilOrdColRef;
import com.oracle.retail.integration.base.bo.invbackordcoldesc.v1.InvBackOrdColDesc;
import com.oracle.retail.integration.base.bo.stradjmodvo.v1.StrAdjModVo;
import com.oracle.retail.integration.base.bo.strfordhdrcrivo.v1.StrFordHdrCriVo;
import com.oracle.retail.integration.base.bo.strfordref.v1.StrFordRef;
import com.oracle.retail.integration.base.bo.ststsfapvmodvo.v1.StsTsfApvModVo;
import com.oracle.retail.integration.base.bo.ststsfhdrcrivo.v1.StsTsfHdrCriVo;
import com.oracle.retail.integration.base.bo.ststsfref.v1.StsTsfRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CancelFulfilOrdColRef;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.CreateFulfilOrdColDesc;
import com.oracle.retail.rms.integration.services.fulfillorderservice.v1.FulfillOrderPortType;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.CreateInvBackOrdColDesc;
import com.oracle.retail.rms.integration.services.inventorybackorderservice.v1.InventoryBackOrderPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.FulfillmentOrderDeliveryPortType;
import com.oracle.retail.sim.integration.services.fulfillmentorderdeliveryservice.v1.LookupFulfillmentOrderDeliveryHeaders;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.ConfirmReversePick;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.CreateReversePick;
import com.oracle.retail.sim.integration.services.fulfillmentorderreversepickservice.v1.FulfillmentOrderReversePickPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.InventoryAdjustmentPortType;
import com.oracle.retail.sim.integration.services.inventoryadjustmentservice.v1.SaveAndConfirmInventoryAdjustment;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CancelFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.CreateFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.LookupFulfillmentOrderHeaders;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.Ping;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.ReadFulfillmentOrderDetail;
import com.oracle.retail.sim.integration.services.storefulfillmentorderservice.v1.StoreFulfillmentOrderPortType;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ApproveTransfer;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.LookupTransferHeader;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.ReadTransferDetail;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.SavePendingTransferRequest;
import com.oracle.retail.sim.integration.services.storetostoretransferservice.v1.StoreToStoreTransferPortType;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer.Default;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxb.JAXBContextFactory;
import feign.soap.SOAPDecoder;
import feign.soap.SOAPEncoder;

/**
 * OracleBaseAPIUtil.java
 * aibrahim
 * 2024
 */
public class OracleBaseAPIUtil {

	private static IOracleRMSClient oracleRMSClient;

	private static IOracleSIMClient oracleSIMClient;

	private static ICarreraClient carreraClient;

	public static void initialize() throws Exception {
		synchronized (OracleBaseAPIUtil.class) {

			RequestInterceptor interceptor = new RequestInterceptor() {
				
				@Override
				public void apply(RequestTemplate template) {
					template.header("SOAPAction", " ");
					template.header("Content-Type", "text/xml;charset=UTF-8");
				}
			};
			
			JAXBContextFactory jaxbFactory = new JAXBContextFactory.Builder().withMarshallerJAXBEncoding("UTF-8").build();
			
			Map<String, QName> qNameMap = new HashMap<String, QName>();
			RequestWrapper wrapper = FulfillOrderPortType.class.getMethod("createFulfilOrdColDesc", FulfilOrdColDesc.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CreateFulfilOrdColDesc.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = InventoryBackOrderPortType.class.getMethod("createInvBackOrdColDesc", InvBackOrdColDesc.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CreateInvBackOrdColDesc.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = FulfillOrderPortType.class.getMethod("cancelFulfilOrdColRef", FulfilOrdColRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CancelFulfilOrdColRef.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = FulfillOrderPortType.class.getMethod("ping", String.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(com.oracle.retail.rms.integration.services.fulfillorderservice.v1.Ping.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
		
			oracleRMSClient = Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleRMSClient.class, OMSUtilCommons.getWebServiceURL("RMS_SERVER_URL"));

			qNameMap = new HashMap<String, QName>();
			wrapper = StoreFulfillmentOrderPortType.class.getMethod("createFulfillmentOrderDetail", FulfilOrdColDesc.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CreateFulfillmentOrderDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = InventoryAdjustmentPortType.class.getMethod("saveAndConfirmInventoryAdjustment", StrAdjModVo.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(SaveAndConfirmInventoryAdjustment.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = StoreToStoreTransferPortType.class.getMethod("savePendingTransferRequest", StsTsfApvModVo.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(SavePendingTransferRequest.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = StoreToStoreTransferPortType.class.getMethod("approveTransfer", StsTsfRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(ApproveTransfer.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = FulfillmentOrderReversePickPortType.class.getMethod("confirmReversePick", ForpRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(ConfirmReversePick.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = StoreFulfillmentOrderPortType.class.getMethod("lookupFulfillmentOrderHeaders", StrFordHdrCriVo.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(LookupFulfillmentOrderHeaders.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = StoreFulfillmentOrderPortType.class.getMethod("readFulfillmentOrderDetail", StrFordRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(ReadFulfillmentOrderDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = FulfillmentOrderDeliveryPortType.class.getMethod("lookupFulfillmentOrderDeliveryHeaders", StrFordRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(LookupFulfillmentOrderDeliveryHeaders.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
	
			wrapper = StoreToStoreTransferPortType.class.getMethod("lookupTransferHeader", StsTsfHdrCriVo.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(LookupTransferHeader.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
		
			wrapper = StoreToStoreTransferPortType.class.getMethod("readTransferDetail", StsTsfRef.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(ReadTransferDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));

			wrapper = StoreFulfillmentOrderPortType.class.getMethod("cancelFulfillmentOrderDetail", Holder.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CancelFulfillmentOrderDetail.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
	
			wrapper = FulfillmentOrderReversePickPortType.class.getMethod("createReversePick", ForpCreModVo.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(CreateReversePick.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
		
			wrapper = StoreFulfillmentOrderPortType.class.getMethod("ping", String.class).getAnnotation(RequestWrapper.class);
			qNameMap.put(Ping.class.getName(), new QName(wrapper.targetNamespace(), wrapper.localName(), "op"));
			
			oracleSIMClient = Feign.builder().retryer(new Default()).encoder(new SOAPEncoder(jaxbFactory, qNameMap)).decoder(new SOAPDecoder(jaxbFactory)).requestInterceptor(interceptor).target(IOracleSIMClient.class, OMSUtilCommons.getWebServiceURL("SIM_SERVER_URL"));

			carreraClient = Feign.builder().encoder(new GsonEncoder()).decoder(new GsonDecoder()).target(ICarreraClient.class, OMSUtilCommons.getWebServiceURL("RSB_SERVER_URL"));
		}
	}

	public static IOracleRMSClient getOracleRMSClient() {
		return oracleRMSClient;
	}

	public static IOracleSIMClient getOracleSIMClient() {
		return oracleSIMClient;
	}

	public static ICarreraClient getCarreraClient() {
		return carreraClient;
	}
}
