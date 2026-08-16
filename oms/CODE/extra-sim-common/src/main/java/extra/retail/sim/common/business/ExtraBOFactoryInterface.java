package extra.retail.sim.common.business;

import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportRequest;

import extra.retail.sim.common.baselv.BaseLV;
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

public interface ExtraBOFactoryInterface {

	ExtraFulfillmentOrderVO createFulfillmentOrderVO();

	ExtraFulfillmentOrderQueryFilter createFulfillmentOrderQueryFilter();

	BaseLV createBaseLV();

	AWBRequest createAWBRequest();

	AWBRequestLineItem createAWBRequestLineItem();

	UniqueSerialNumber createUniqueSerialNumber();

	ReportRequest createODDHandOverToCourierReportRequest(String trackingNumber);

	ItemBinLocation createItemBinLocation();

	StoreBinLocation createStoreBinLocation();

	ExtraFulfillmentOrder createFulfillmentOrder();

	ExtraFulfillmentOrderLineItem createFulfillmentOrderLineItem(StockItem paramStockItem);

	StockRequestReportQueryFilter createStockRequestReportQueryFilter();

	ReturnRequestItem createFulfillmentOrderLineItem();

	TransferReturnRequestQueryFilter createTransferReturnRequestQueryFilter();
}
