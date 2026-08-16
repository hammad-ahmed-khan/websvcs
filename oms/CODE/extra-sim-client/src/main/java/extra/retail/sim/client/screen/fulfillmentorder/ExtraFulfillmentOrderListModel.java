package extra.retail.sim.client.screen.fulfillmentorder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.service.core.ClientServiceFactory;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.service.core.ExtraClientServiceFactory;

/********************************************************************************************************
 * Fulfillment Order List Model
 * <p>
 * Provides logic for the Customer Order List Screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraFulfillmentOrderListModel extends SimScreenModel {
	/**
	 * Returns a list of FulfillmentOrderVO matching the default filter settings.
	 * 
	 * @return A list of FulfillmentOrderVO matching the default filter settings.
	 */
	public List<ExtraFulfillmentOrderVO> findFulfillmentOrderVOs() throws Exception {
		return ExtraClientServiceFactory.getFulfillmentOrderServices().findFulfillmentOrderVOs(getFilter());
	}

	/**
	 * Returns whether or not the input CustomerOrderWrapper represents a Web Order
	 * 
	 * @param orderVO The CustomerOrderWrapper to test if it is a Web Order
	 * @return True if the input CustomerOrderWrapper represents a Web Order,
	 *         otherwise false.
	 */
	public boolean isWebOrder(FulfillmentOrderVO orderVO) {
		return orderVO.getOrderType() == FulfillmentOrderType.WEB_ORDER;
	}

	/**
	 * Reads a fulfillment order object corresponding to the input
	 * FulfillmentOrderVO and stores it in memory.
	 * 
	 * @param orderVO The input FulfillmentOrderVO for which to read the
	 *                corresponding fulfillment order.
	 */
	public void storeFulfillmentOrder(FulfillmentOrderVO orderVO) throws Exception {
		FulfillmentOrder fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(orderVO.getId());
		RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, fulfillmentOrder);
		
		
		ExtraFulfillmentOrder extraFulfillmentOrder = ExtraClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(orderVO.getId());
		RepositoryManager.addStateObject(ExtraSimClientStateKey.EXTRA_SELECTED_FULFILLMENT_ORDER, extraFulfillmentOrder);
	}

	/**
	 * Creates a fulfillment order query filter with default settings and returns
	 * it.
	 * 
	 * @return A fulfillment order query filter with default settings.
	 */
	public ExtraFulfillmentOrderQueryFilter getFilter() {
		ExtraFulfillmentOrderQueryFilter filter = (ExtraFulfillmentOrderQueryFilter) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER);
		if (filter == null) {
			filter = ExtraBOFactory.createFulfillmentOrderQueryFilter();
			filter.doSetStatus(FulfillmentOrderStatus.ACTIVE);
			filter.doSetStoreId(getStoreId());
		}
		return filter;
	}

	/**
	 * Prints reports for the list of input FulfillmentOrderVOs.
	 * 
	 * @param orderVOs The FulfillmentOrderVO which represent orders for which to
	 *                 print a report.
	 */
	public void printFulfillmentOrders(List<FulfillmentOrderVO> orderVOs) throws Exception {
		if (orderVOs.isEmpty()) {
			return;
		}
		Long storeId = getStoreId();
		List<RetailStoreFormatPrinter> formatPrinters = SimClientPrintUtility.selectFormatPrinter(storeId, ReportFormat.CUSTOMER_ORDER);
		if (formatPrinters == null || formatPrinters.isEmpty()) {
			return;
		}
		List<ReportRequest> requests = new ArrayList<ReportRequest>();
		for (FulfillmentOrderVO orderVO : orderVOs) {
			requests.add(BOFactory.createFulfillmentOrderReportRequest(orderVO.getId()));
		}
		SimClientPrintUtility.printReportRequests(requests, formatPrinters, FulfillmentOrderMessageText.REPORT_PRINTED);
	}

	/**
	 * Returns the description fields map for the customer order query filter.
	 * 
	 * @return The description fields map for the customer order query filter.
	 */
	public Map<String, String> getDescriptionMap() {
		Map<String, String> descriptionMap = new LinkedHashMap<String, String>();
		ExtraFulfillmentOrderQueryFilter filter = getFilter();
		if (filter.getFromDate() != null) {
			descriptionMap.put("From Date", LocaleManager.getShortDateFormatter().format(filter.getFromDate()));
		}
		if (filter.getToDate() != null) {
			descriptionMap.put("To Date", LocaleManager.getShortDateFormatter().format(filter.getToDate()));
		}
		if (filter.getItemId() != null) {
			descriptionMap.put("Item", filter.getItemId());
		}
		if (filter.getOrderType() != null) {
			descriptionMap.put("Reservation Type", filter.getOrderType().toString());
		}
		if (filter.getFulfillmentOrderId() != null) {
			descriptionMap.put("SIM Customer Order ID", filter.getFulfillmentOrderId().toString());
		}
		if (filter.getCustomerOrderId() != null) {
			descriptionMap.put("Customer Order ID", filter.getCustomerOrderId());
		}
		if (filter.getFulfillmentOrderExternalId() != null) {
			descriptionMap.put("Fulfillment Order ID", filter.getFulfillmentOrderExternalId());
		}
		if (filter.getBinId() != null) {
			descriptionMap.put("BIN ID", filter.getBinId());
		}
		if (filter.getStatus() != null) {
			descriptionMap.put("Status", Translator.getText(filter.getStatus().toString()));
		}
		if (filter.getCustomerName() != null) {
			descriptionMap.put("Customer Name", filter.getCustomerName());
		}
		if (filter.getTrackingId() != null) {
			descriptionMap.put("Tracking ID", filter.getTrackingId());
		}
		if (filter.getDeliveryMode() != null) {
			descriptionMap.put("Delivery Mode", filter.getDeliveryMode());
		}
		if (filter.getDeliverySlot() != null) {
			descriptionMap.put("Delivery Slot", filter.getDeliverySlot());
		}
		return descriptionMap;
	}
}
