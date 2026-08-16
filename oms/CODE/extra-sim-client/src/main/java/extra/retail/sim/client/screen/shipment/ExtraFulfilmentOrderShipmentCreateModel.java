package extra.retail.sim.client.screen.shipment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import extra.retail.sim.client.screen.reportformat.ExtraSimClientPrintUtility;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.report.ExtraReportFormat;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.AWBRequestLineItem;
import extra.retail.sim.service.core.ExtraClientServiceFactory;
import extra.retail.sim.service.core.ExtraWSClientServiceFactory;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.ClientCommandFactory;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryCreateCommand;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryQueryFilter;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.fulfillmentorderdelivery.FulfillmentOrderDeliveryServices;

/**
 * ExtraFulfilmentOrderShipmentCreate.java
 * aibrahim
 * 2023
 */
public class ExtraFulfilmentOrderShipmentCreateModel extends SimScreenModel {

	private FulfillmentOrder fulfillmentOrder;

	private List<FulfillmentOrderPickVO> fulfillmentOrderPickVOs;

	private Long pickId = null;

	private Map<Long, List<FulfillmentOrderShipmentItemWrapper>> fulfillmentOrderPickWrapperMap;

	private Map<Long, FulfillmentOrderLineItem> lineItemMap = new HashMap<>();
	
	private Set<Integer> pendingItemWrapperSeqs = new HashSet<>();

	private Map<Long, Quantity> deliveryPendinglineQty = new HashMap<>();

	private FulfillmentOrderDelivery delivery;

	private boolean isPending = false;

	private boolean imeiPersisted = false;

	private List<String> imeiItems;

	public void loadFulfillmentOrder() {
		this.fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject("SELECTED_FULFILLMENT_ORDER");
		for (FulfillmentOrderLineItem lineItem : fulfillmentOrder.getLineItems()) {
			lineItemMap.put(lineItem.getId(), lineItem);
			isPending = isPending || lineItem.getPickedQuantity().compareTo(lineItem.getDeliveredQuantity()) != 0;
		}
		imeiPersisted = !isPending;
		fulfillmentOrderPickWrapperMap = new HashMap<>();
	}

	public List<Long> getFulfilmentOrderPickIds() throws Exception {
		if (fulfillmentOrder == null) {
			loadFulfillmentOrder();
		}
		List<Long> pickIds = new ArrayList<>();
		if (fulfillmentOrderPickVOs == null) {
			FulfillmentOrderPickQueryFilter filter = BOFactory.createFulfillmentOrderPickQueryFilter();
			filter.setSimCustomerOrderId(fulfillmentOrder.getId());
			filter.setStatus(FulfillmentOrderPickStatus.COMPLETED);
			fulfillmentOrderPickVOs = ClientServiceFactory.getFulfillmentOrderPickServices().findFulfillmentOrderPickVOs(filter);
		}
		for (FulfillmentOrderPickVO pickVO : fulfillmentOrderPickVOs) {
			pickIds.add(pickVO.getId());
		}
		return pickIds;
	}

	public Collection<FulfillmentOrderShipmentItemWrapper> getPickLineItemWrappers(Long selectedPickId) throws Exception {
		pickId = selectedPickId;
		List<FulfillmentOrderShipmentItemWrapper> itemWrappers = fulfillmentOrderPickWrapperMap.get(selectedPickId);
		Set<String> items = new HashSet<>();
		if (itemWrappers == null) {
			itemWrappers = new ArrayList<>();
			FulfillmentOrderPick pick = ClientServiceFactory.getFulfillmentOrderPickServices().readFulfillmentOrderPick(selectedPickId);
			int count = 1;
			for (FulfillmentOrderPickLineItem pickLineItem : pick.getLineItems()) {
				Quantity pickLineQty = pickLineItem.getQuantityOrZero();
				for (int q = 1; q <= pickLineQty.intValue();q++) {
					FulfillmentOrderShipmentItemWrapper itemWrapper = new FulfillmentOrderShipmentItemWrapper();
					itemWrapper.setFulfillmentOrder(fulfillmentOrder);
					itemWrapper.setOrderLineItem(this.lineItemMap.get(pickLineItem.getFulfillmentOrderLineItemId()));
					itemWrapper.setPickLineItem(pickLineItem);
					itemWrapper.setPick(pick);
					itemWrapper.setCartonNumber(1);
					itemWrapper.setSeqNo(count);
					pendingItemWrapperSeqs.add(count++);
					itemWrappers.add(itemWrapper);
					items.add(pickLineItem.getStockItem().getId());
				}
				if (pickLineQty.intValue() > 0) {					
					deliveryPendinglineQty.put(pickLineItem.getFulfillmentOrderLineItemId(), pickLineQty);
				}
			}
			fulfillmentOrderPickWrapperMap.put(selectedPickId, itemWrappers);
		} else {
			for (FulfillmentOrderShipmentItemWrapper wrapper : itemWrappers) {
				items.add(wrapper.getItemId());
				pendingItemWrapperSeqs.add(wrapper.getSeqNo());
			}
		}
		if (!isPending) {
			mapDelivery(null, FulfillmentOrderDeliveryStatus.COMPLETED);
		} else {
			lookUpUINEnable(items);
			mapDelivery(null, FulfillmentOrderDeliveryStatus.IN_PROGRESS, FulfillmentOrderDeliveryStatus.SUBMITTED);
		}
		if (!isDeliveryPending()) {
			loadCartonNumberAndIMEIDetail();
		}
		return itemWrappers;
	}

	private void loadCartonNumberAndIMEIDetail() throws Exception {
		Set<Long> deliveryLineIds = new HashSet<>();
		List<FulfillmentOrderShipmentItemWrapper> lineWrappers = fulfillmentOrderPickWrapperMap.get(pickId);
		for (FulfillmentOrderShipmentItemWrapper wrapper : lineWrappers) {
			deliveryLineIds.add(wrapper.getDeliveryLineItemId());
		}
		AWBRequest awbDetail = ExtraClientServiceFactory.getShipmentOrderServices().getAWBDetail(pickId);
		List<UniqueSerialNumber> imeis = ExtraClientServiceFactory.getShipmentOrderServices().getIMEIDetail(deliveryLineIds);
		for (FulfillmentOrderShipmentItemWrapper wrapper : lineWrappers) {
			if (awbDetail != null && awbDetail.getLineItems() != null) {
				Iterator<AWBRequestLineItem> awbIterator = awbDetail.getLineItems().iterator();
				AWBRequestLineItem awbRequestLineItem = null;
				while(awbIterator.hasNext()) {
					awbRequestLineItem = awbIterator.next();
					if (awbRequestLineItem.getDeliveryId().equals(wrapper.getDeliveryId()) && awbRequestLineItem.getPickLineItemId().equals(wrapper.getPickLineItemId())) {
						wrapper.setAwbRequestLineItem(awbRequestLineItem);
						awbIterator.remove();
						break;
					}
				}
			}
			if (imeis != null) {
				Iterator<UniqueSerialNumber> imeiIterator = imeis.iterator();
				UniqueSerialNumber serialNumber = null;
				while(imeiIterator.hasNext()) {
					serialNumber = imeiIterator.next();
					if (serialNumber.getFulOrdDlvId().equals(wrapper.getDeliveryId()) && serialNumber.getFulOrdDlvLineItemId().equals(wrapper.getDeliveryLineItemId())) {
						wrapper.setUniqueSerialNumber(serialNumber.getImeiNumber());
						imeiIterator.remove();
						imeiPersisted = true;
						break;
					}
				}
			}
		}
	}

	private void lookUpUINEnable(Set<String> items) throws Exception {
		imeiItems = ExtraClientServiceFactory.getShipmentOrderServices().lookupIMEIEnableItems(getStoreId(), items);
		if (imeiItems == null || imeiItems.isEmpty()) {
			imeiPersisted = true;;
		}
	}

	public boolean isIMEIEnabled(String item) {
		return imeiItems != null && imeiItems.contains(item);
	}

	public void savePendingDelivery() throws Exception {
		if (isDeliveryPending()) {
			FulfillmentOrderDeliveryCreateCommand command = ClientCommandFactory.createFulfillmentOrderDeliveryCreateForFulfillmentOrderCommand();
	        command.setFulfillmentOrder(fulfillmentOrder);
	        command.execute();
	        delivery = command.getDelivery();
	        for (FulfillmentOrderDeliveryLineItem lineItem : delivery.getLineItems()) {
	        	if (deliveryPendinglineQty.containsKey(lineItem.getFulfillmentOrderLineItemId())) {	        		
	        		lineItem.setQuantity(deliveryPendinglineQty.get(lineItem.getFulfillmentOrderLineItemId()));
	        	} else {
	        		lineItem.setQuantity(Quantity.ZERO);
	        	}
	        }
	        delivery.doSetId(ClientServiceFactory.getFulfillmentOrderDeliveryServices().updateFulfillmentOrderDelivery(delivery));
			mapDeliveryId(delivery.getId());
			mapDelivery(delivery.getId(), FulfillmentOrderDeliveryStatus.IN_PROGRESS, FulfillmentOrderDeliveryStatus.SUBMITTED);
		}
	}

	public void requestAWB() throws Exception {
		List<FulfillmentOrderShipmentItemWrapper> lineWrappers = fulfillmentOrderPickWrapperMap.get(pickId);

		AWBRequest awbRequest = ExtraBOFactory.createAWBRequest();
		awbRequest.setFulOrdId(this.fulfillmentOrder.getId());
		awbRequest.setStoreId(this.fulfillmentOrder.getStoreId());
		awbRequest.setPickId(pickId);
		awbRequest.setLineItems(new ArrayList<AWBRequestLineItem>(lineWrappers.size()));
		for (FulfillmentOrderShipmentItemWrapper wrapper : lineWrappers) {
			if (wrapper.getTrackingNumber() == null) {
				AWBRequestLineItem requestLineItem = new AWBRequestLineItem();
				requestLineItem.setCartonNumber(wrapper.getCartonNumber().toString());
				requestLineItem.setDeliveryId(wrapper.getDeliveryId());
				requestLineItem.setItem(wrapper.getItemId());
				requestLineItem.setPickLineItemId(wrapper.getPickLineItemId());
				requestLineItem.setQty(wrapper.getQtyPicked().intValue());
				awbRequest.getLineItems().add(requestLineItem);
			}
		}
		String trackingNumber = ExtraClientServiceFactory.getShipmentOrderServices().requestAWB(awbRequest);
		for (FulfillmentOrderShipmentItemWrapper wrapper : lineWrappers) {
			if (wrapper.getTrackingNumber() == null) {
				wrapper.setTrackingNumber(trackingNumber);
			}
		}
	}

	public boolean isDeliveryPending() {
		return !pendingItemWrapperSeqs.isEmpty();
	}

	public boolean isAWBAssigned() {
		return isAWBAssigned(fulfillmentOrderPickWrapperMap.get(pickId).iterator());
	}

	private boolean isAWBAssigned(Iterator<FulfillmentOrderShipmentItemWrapper> iterator) {
		return iterator.hasNext() ? iterator.next().getTrackingNumber() != null && isAWBAssigned(iterator) : true;
	}

	public boolean isIMEISaved() {
		return isIMEISaved(fulfillmentOrderPickWrapperMap.get(pickId).iterator());
	}

	private boolean isIMEISaved(Iterator<FulfillmentOrderShipmentItemWrapper> iterator) {
		return iterator.hasNext() ? isIMEISaved(iterator.next()) && isIMEISaved(iterator) : true;
	}

	private boolean isIMEISaved(FulfillmentOrderShipmentItemWrapper itemWrapper) {
		return !imeiItems.contains(itemWrapper.getItemId()) || !StringUtility.isNullOrEmpty(itemWrapper.getUniqueSerialNumber());
	}

	private void mapDelivery(Long skipDeliveryId, FulfillmentOrderDeliveryStatus ...deliveryStatus) throws Exception {
		for (FulfillmentOrderDeliveryStatus status : deliveryStatus) {
			if (!pendingItemWrapperSeqs.isEmpty()) {
				List<FulfillmentOrderDeliveryVO> deliveryVOs = loadDeliveries(status);
				for (FulfillmentOrderDeliveryVO deliveryVO : deliveryVOs) {
					if (!deliveryVO.getId().equals(skipDeliveryId)) {
						mapDeliveryId(deliveryVO.getId());
						if (pendingItemWrapperSeqs.isEmpty()) {
							break;
						}
					}
				}
			}
		}
	}

	private void mapDeliveryId(Long deliveryId) throws Exception {
		FulfillmentOrderDelivery delivery = null;
		if (deliveryId != null) {
			delivery = ClientServiceFactory.getFulfillmentOrderDeliveryServices().readFulfillmentOrderDelivery(deliveryId);
		}
		Map<Long, AtomicInteger> assignedQtyMap = new HashMap<>();
		for (FulfillmentOrderShipmentItemWrapper itemWrapper : fulfillmentOrderPickWrapperMap.get(pickId)) {
			if (itemWrapper.getDeliveryId() == null) {
				for (FulfillmentOrderDeliveryLineItem lineItem : delivery.getLineItems()) {
					AtomicInteger assignedQty = assignedQtyMap.get(lineItem.getId());
					if (assignedQty == null || assignedQty.get() < lineItem.getQuantityOrZero().intValue()) {
						itemWrapper.setDelivery(delivery);
						itemWrapper.setDeliveryLineItem(lineItem);
						if (assignedQty == null) {
							assignedQtyMap.put(lineItem.getId(),  new AtomicInteger(1));
						} else {							
							assignedQty.incrementAndGet();
						}
						pendingItemWrapperSeqs.remove(itemWrapper.getSeqNo());
						break;
					}
				}
			}
		}
	}

	private List<FulfillmentOrderDeliveryVO> loadDeliveries(FulfillmentOrderDeliveryStatus deliveryStatus) throws Exception {
		FulfillmentOrderDeliveryQueryFilter deliveryQueryFilter = BOFactory.createFulfillmentOrderDeliveryQueryFilter();
		deliveryQueryFilter.setFulfillmentOrderId(fulfillmentOrder.getId());
		deliveryQueryFilter.setStatus(deliveryStatus);
		return ClientServiceFactory.getFulfillmentOrderDeliveryServices().findFulfillmentOrderDeliveryVOs(deliveryQueryFilter);
	}

	public void cancelAWB() throws Exception {
		List<String> trackingNumbers = new ArrayList<>();
		for (FulfillmentOrderShipmentItemWrapper itemWrapper : fulfillmentOrderPickWrapperMap.get(pickId)) {
			String trackNumber = itemWrapper.getTrackingNumber();
			if (!itemWrapper.isDispatched()) {
				if (!trackingNumbers.contains(trackNumber)) {					
					trackingNumbers.add(trackNumber);
				}
				itemWrapper.setTrackingNumber(null);
			}
		}
		if (!trackingNumbers.isEmpty()) {
			ExtraClientServiceFactory.getShipmentOrderServices().cancelAWB(trackingNumbers);
		}
	}

	public void printAWB() throws Exception {
		ExtraClientServiceFactory.getShipmentOrderServices().printAWB(getStoreId(), fulfillmentOrderPickWrapperMap.get(pickId).get(0).getTrackingNumber());
	}

	public void cancelDelivery() throws Exception {
		Set<Long> cancelledDeliveryIds = new HashSet<>();
		FulfillmentOrderDeliveryServices deliveryServices = ClientServiceFactory.getFulfillmentOrderDeliveryServices();
		List<UniqueSerialNumber> listIMEI = new ArrayList<>();
		for (FulfillmentOrderShipmentItemWrapper itemWrapper : fulfillmentOrderPickWrapperMap.get(pickId)) {
			if (itemWrapper.isDispatched()) {
				continue;
			}
			if (!cancelledDeliveryIds.contains(itemWrapper.getDeliveryId())) {
				deliveryServices.cancelFulfillmentOrderDelivery(itemWrapper.getDeliveryId());
				cancelledDeliveryIds.add(itemWrapper.getDeliveryId());
			}
			UniqueSerialNumber details = new UniqueSerialNumber();
			details.setFulOrdId(this.fulfillmentOrder.getId());
			details.setFulOrdDlvLineItemId(itemWrapper.getDeliveryLineItemId());
			listIMEI.add(details);
			itemWrapper.setDelivery(null);
			itemWrapper.setDeliveryLineItem(null);
			itemWrapper.setUniqueSerialNumber(null);
			pendingItemWrapperSeqs.add(itemWrapper.getSeqNo());
		}
		ExtraWSClientServiceFactory.getIMEIServices().cancelIMEI(listIMEI);
	}

	public void saveIMEI() throws Exception {
		List<FulfillmentOrderShipmentItemWrapper> wrappers = fulfillmentOrderPickWrapperMap.get(pickId);
		List<UniqueSerialNumber> serialNumbers = new ArrayList<>(wrappers.size());
		for (FulfillmentOrderShipmentItemWrapper wrapper : wrappers) {
			if (wrapper.isDispatched() || !imeiItems.contains(wrapper.getItemId())) {
				continue;
			}
			if (StringUtility.isNullOrEmpty(wrapper.getUniqueSerialNumber())) {
				throw new BusinessException(ShipmentOrderMessage.IMEI_EMPTY);
			}
			UniqueSerialNumber number = new UniqueSerialNumber();
			number.setCustOrdId(wrapper.getCustomerOrderID());
			number.setFulOrdDlvId(wrapper.getDeliveryId());
			number.setFulOrdDlvLineItemId(wrapper.getDeliveryLineItemId());
			number.setFulOrdId(wrapper.getFulfillmentOrderId());
			number.setId(wrapper.getFulfillmentPickId());
			number.setImeiNumber(wrapper.getUniqueSerialNumber());
			number.setItemId(wrapper.getItemId());
			number.setStoreId(getStoreId());
			serialNumbers.add(number);
		}
		if (!serialNumbers.isEmpty()) {
			ExtraWSClientServiceFactory.getIMEIServices().saveIMEI(serialNumbers);
		}
		imeiPersisted = true;
	}

	public boolean isImeiPersisted() {
		return imeiPersisted;
	}

	public void handOOverToCourier() throws Exception {
		String trackingNumber = fulfillmentOrderPickWrapperMap.get(pickId).get(0).getTrackingNumber();
		List<RetailStoreFormatPrinter> formatPrinters = ExtraSimClientPrintUtility.selectFormatPrinter(getStoreId(), ExtraReportFormat.ODD_HANDOVER_TO_COURIER);
        if (formatPrinters.isEmpty()) {
            return;
        }
        SimClientPrintUtility.printReportRequests(Collections.singletonList(ExtraBOFactory.createODDHandOverToCourierReportRequest(trackingNumber)), formatPrinters, ShipmentOrderMessage.HO_TO_COURIER_PRINTED);
        ExtraClientServiceFactory.getShipmentOrderServices().updateHTC(trackingNumber);
	}
}
