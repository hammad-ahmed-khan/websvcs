package extra.retail.sim.client.screen.fulfillmentorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.screen.reportformat.SimClientPrintUtility;
import oracle.retail.sim.client.uom.UomUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.reportrequest.FulfillmentOrderReportRequest;
import oracle.retail.sim.service.core.ClientServiceFactory;
import oracle.retail.sim.service.fulfillmentorderpick.FulfillmentOrderPickServices;

import extra.retail.sim.client.core.ExtraClientWrapperFactory;
import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.service.core.ExtraClientServiceFactory;

public class ExtraFulfillmentOrderDetailModel extends SimScreenModel {

	private FulfillmentOrder fulfillmentOrder;
	
	private ExtraFulfillmentOrder extraFulfillmentOrder;
	

	public void loadFulfillmentOrder() throws Exception {
		if ((Boolean) RepositoryManager.getStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED) == Boolean.TRUE) {
			RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED);
			this.fulfillmentOrder = ClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(this.fulfillmentOrder.getId());
			this.extraFulfillmentOrder = ExtraClientServiceFactory.getFulfillmentOrderServices().readFulfillmentOrder(this.extraFulfillmentOrder.getId());
			RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER, this.fulfillmentOrder);
			RepositoryManager.addStateObject(ExtraSimClientStateKey.EXTRA_SELECTED_FULFILLMENT_ORDER, this.extraFulfillmentOrder);
		} else {			
			this.fulfillmentOrder = (FulfillmentOrder) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER);
			this.extraFulfillmentOrder = (ExtraFulfillmentOrder) RepositoryManager.getStateObject(ExtraSimClientStateKey.EXTRA_SELECTED_FULFILLMENT_ORDER);
		}
	}

	public FulfillmentOrder getFulfillmentOrder() {
		return this.fulfillmentOrder;
	}

	public boolean isOrderEditable() throws Exception {
		if (this.fulfillmentOrder == null)
			loadFulfillmentOrder();
		return (this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW || this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.IN_PROGRESS);
	}

	public boolean isNotesEditable() {
		FulfillmentOrderStatus fulfillmentOrderStatus = this.fulfillmentOrder.getStatus();
		return (fulfillmentOrderStatus == FulfillmentOrderStatus.NEW || fulfillmentOrderStatus == FulfillmentOrderStatus.IN_PROGRESS);
	}

	public boolean isWebOrder() {
		return (this.fulfillmentOrder.getOrderType() == FulfillmentOrderType.WEB_ORDER);
	}

	public boolean isItemDetailOrigin() {
		String str = (String) RepositoryManager.getStateObject("CUSTOMER_ORDER_DETAIL_ORIGIN");
		return (str != null && str.equals(SimScreenName.ITEM_CUSTOMER_ORDER_SCREEN));
	}

	public List<ExtraFulfillmentOrderLineItemWrapper> getCustomerOrderItems() throws Exception {
		List<ExtraFulfillmentOrderLineItem> list = this.extraFulfillmentOrder.getLineItems();
		ArrayList<ExtraFulfillmentOrderLineItemWrapper> arrayList = new ArrayList<ExtraFulfillmentOrderLineItemWrapper>();
		for (ExtraFulfillmentOrderLineItem fulfillmentOrderLineItem : list)
			arrayList.add(ExtraClientWrapperFactory.createFulfillmentOrderLineItemWrapper(this.extraFulfillmentOrder, fulfillmentOrderLineItem, findConversionFactor(fulfillmentOrderLineItem)));
		return arrayList;
	}

	private BigDecimal findConversionFactor(FulfillmentOrderLineItem paramFulfillmentOrderLineItem) throws Exception {
		return UomUtility.getStandardUomToTargetUom(paramFulfillmentOrderLineItem.getStockItem(), paramFulfillmentOrderLineItem.getPreferredUom());
	}

	public void markFulfillmentOrderAsInProgress() throws Exception {
		if (this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.NEW) {
			ClientServiceFactory.getFulfillmentOrderServices().markFulfillmentOrderInProgress(this.fulfillmentOrder.getId());
			RepositoryManager.addStateObject("FULFILLMENT_ORDER_MODIFIED", Boolean.TRUE);
		}
	}

	public void printCustomerOrder() throws Exception {
		Long long_ = getStoreId();
		List<RetailStoreFormatPrinter> list = SimClientPrintUtility.selectFormatPrinter(long_, ReportFormat.CUSTOMER_ORDER);
		if (list == null || list.isEmpty())
			return;
		FulfillmentOrderReportRequest fulfillmentOrderReportRequest = BOFactory.createFulfillmentOrderReportRequest(this.fulfillmentOrder.getId());
		SimClientPrintUtility.printReportRequest((ReportRequest) fulfillmentOrderReportRequest, list, (MessageText) FulfillmentOrderMessageText.REPORT_PRINTED);
	}

	public void createFulfillmentOrderPick() throws Exception {

		FulfillmentOrderPickServices fulfillmentOrderPickServices = ClientServiceFactory.getFulfillmentOrderPickServices();
		
		FulfillmentOrderPickQueryFilter pickQueryFilter = BOFactory.createFulfillmentOrderPickQueryFilter();
		pickQueryFilter.setSimCustomerOrderId(fulfillmentOrder.getId());
		pickQueryFilter.setStatus(FulfillmentOrderPickStatus.NEW);
		List<FulfillmentOrderPickVO> list = fulfillmentOrderPickServices.findFulfillmentOrderPickVOs(pickQueryFilter);
		
		Long pickId = null;
		if (list != null && !list.isEmpty()) {
			pickId = list.get(0).getId();
		}
		if (pickId == null) {
			pickQueryFilter.setStatus(FulfillmentOrderPickStatus.IN_PROGRESS);
			list = fulfillmentOrderPickServices.findFulfillmentOrderPickVOs(pickQueryFilter);
			if (list != null && !list.isEmpty()) {
				pickId = list.get(0).getId();
			}
		}
		
		FulfillmentOrderPick pick = null;
		if (pickId == null) {
			pick = fulfillmentOrderPickServices.createFulfillmentOrderPickForFulfillmentOrder(fulfillmentOrder.getId());
		} else {
			pick = fulfillmentOrderPickServices.readFulfillmentOrderPick(pickId);
		}
		RepositoryManager.addStateObject(SimClientStateKey.SELECTED_FULFILLMENT_ORDER_PICK, pick);
        RepositoryManager.addStateObject(SimClientStateKey.CUSTOMER_ORDER_PICK_MODIFIED, Boolean.TRUE);
        RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_MODIFIED, Boolean.TRUE);
	}
}
