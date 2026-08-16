package extra.retail.sim.common.business;

import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportRequest;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.common.configutil.ExtraCommonConfigManager;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderLineItem;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.item.ItemBinLocation;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.common.shipment.AWBRequestLineItem;
import extra.retail.sim.common.spareparts.ReturnRequestItem;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;
import extra.retail.sim.common.store.StoreBinLocation;

public class ExtraBOFactory {

	private static ExtraBOFactoryInterface factory = getDefaultFactory();

	private static ExtraBOFactoryInterface getDefaultFactory() {
		return ExtraCommonConfigManager.getBOFactoryImpl();
	}

	public static ExtraFulfillmentOrderVO createFulfillmentOrderVO() {
		return factory.createFulfillmentOrderVO();
	}

	public static ExtraFulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter() {
		return factory.createFulfillmentOrderQueryFilter();
	}

	public static BaseLV createBaseLV() {
		return factory.createBaseLV();
	}

	public static AWBRequest createAWBRequest() {
		return factory.createAWBRequest();
	}

	public static AWBRequestLineItem createAWBRequestLineItem() {
		return factory.createAWBRequestLineItem();
	}

	public static UniqueSerialNumber createUniqueSerialNumber() {
		return factory.createUniqueSerialNumber();
	}

	public static ReportRequest createODDHandOverToCourierReportRequest(String trackingNumber) {
		return factory.createODDHandOverToCourierReportRequest(trackingNumber);
	}

	public static ItemBinLocation createItemBinLocation() {
		return factory.createItemBinLocation();
	}

	public static StoreBinLocation createStoreBinLocation() {
		return factory.createStoreBinLocation();
	}

	public static ExtraFulfillmentOrder createFulfillmentOrder() {
		return factory.createFulfillmentOrder();
	}

	public static ExtraFulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem) {
		return factory.createFulfillmentOrderLineItem(paramStockItem);
	}

	public static StockRequestReportQueryFilter createStockRequestReportQueryFilter() {
		return factory.createStockRequestReportQueryFilter();
	}

	public static ReturnRequestItem createReturnRequestItem() {
		return factory.createFulfillmentOrderLineItem();
	}

	public static TransferReturnRequestQueryFilter createTransferReturnRequestQueryFilter() {
		return factory.createTransferReturnRequestQueryFilter();
	}
}
